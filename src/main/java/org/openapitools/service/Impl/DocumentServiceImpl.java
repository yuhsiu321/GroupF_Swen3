package org.openapitools.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.minio.MinioClient;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.openapitools.configuration.MinioUtil;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.persistence.entities.DocumentEntity;
import org.openapitools.persistence.repositories.DocumentRepository;
import org.openapitools.service.DocumentService;
import org.openapitools.service.common.enums.FileContentTypeEnum;
import org.openapitools.service.common.exception.BaseException;
import org.openapitools.service.mapper.DocumentMapper;
import org.openapitools.service.mapper.DocumentMapperImpl;
import org.openapitools.service.model.Document;
import org.openapitools.service.model.DocumentPageQuertReq;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.openapitools.service.common.constant.CommonConstant.BASE_FILE_PATH;


@Service
public class DocumentServiceImpl implements DocumentService {

    @Resource
    private DocumentRepository documentRepository;

    @Resource
    private  OcrServiceImpl ocrServiceImpl;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private MinioClient minioClient;


    private final DocumentMapper documentMapper = new DocumentMapperImpl();


    /**
     * 上传文件
     *
     * @param documentDTO 文件上传对象
     * @param document    真实的文件对象
     */
    @Override
    public void uploadDocument(Document documentDTO, MultipartFile document) {
        try {
            String text = ocrServiceImpl.recognizeText(document);
            String uploadedFileName = saveUploadedFile(document);
            String objectName = saveUploadedFileToMinio(document);
            updateDocumentMetadata(documentDTO, objectName);
            DocumentEntity documentToBeSaved = documentMapper.toEntity(documentDTO);
            documentToBeSaved.setContent(text);
            documentToBeSaved.setTitle(document.getOriginalFilename());
            documentRepository.save(documentToBeSaved);
            IndexQuery indexQuery = new IndexQuery();
            //填充id
            indexQuery.setId(documentToBeSaved.getId() + "");
            indexQuery.setSource(JSON.toJSONString(documentToBeSaved));
            elasticsearchRestTemplate.bulkIndex(Collections.singletonList(indexQuery), IndexCoordinates.of("es_document"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String saveUploadedFileToMinio(MultipartFile document) {
        MinioUtil minioUtil = new MinioUtil(minioClient);
        return minioUtil.upload(document);
    }

    @Override
    public Document getDocument(Integer id, Integer page, Boolean fullPermissions) {
        DocumentEntity documentEntity = documentRepository.findById(id).orElse(null);
        if (documentEntity != null) {
            // If using a mapper to convert Entity to DTO, perform the conversion here
            return documentMapper.toDto(documentEntity);
        } else {
            // Handle case where document is not found, maybe return null or throw an exception
            return null;
        }
    }

    @Override
    public ResponseEntity<Document> getDocument(Integer id) {
        List<Document> documentDTOS = new ArrayList<>();
        for (DocumentEntity document : documentRepository.findAll()) {
            documentDTOS.add(documentMapper.toDto(document));
        }
        return null;
    }

    @Override
    public ResponseEntity<Boolean> getDocumentThumb(Integer id) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        HttpServletResponse resp = ((ServletRequestAttributes) requestAttributes).getResponse();
        //根据文档id找到文档详情
        DocumentEntity documentEntity = documentRepository.findById(id).orElse(null);
        assert documentEntity != null;
        getOutPutStream(documentEntity.getArchivedFileName(), resp);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Boolean> getDocumentThumbOfMinio(Integer id) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        try {
        HttpServletResponse resp = ((ServletRequestAttributes) requestAttributes).getResponse();
        //根据文档id找到文档详情
        DocumentEntity documentEntity = documentRepository.findById(id).orElse(null);
        assert documentEntity != null;
        MinioUtil minioUtil = new MinioUtil(minioClient);
        String preview = minioUtil.preview(documentEntity.getArchivedFileName());
        URL url = new URL(preview);
        InputStream inputStream = url.openStream();
            resp.setContentType(getContentTypeByFileName(documentEntity.getTitle()));
            resp.setHeader("Content-Disposition", "attachment;filename=" + new String(documentEntity.getTitle().getBytes(), StandardCharsets.ISO_8859_1));
            try (ServletOutputStream outputStream = resp.getOutputStream()) {
                int bytesRead;
                byte[] buffer = new byte[1024];
                while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (FileNotFoundException e) {
            throw new BaseException("50001", "找不到文件");
        } catch (Exception e) {
            throw new BaseException("4001", "无权限访问");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public Page<DocumentEntity> getDocuments(DocumentPageQuertReq req) {
        PageRequest pageRequest;
        if (StringUtils.isBlank(req.getOrdering()) || req.getOrdering().contains("-")) {
            pageRequest = PageRequest.of(req.getPage() == null ? 0 : req.getPage(), req.getPageSize());
        } else {
            pageRequest = PageRequest.of(req.getPage() == null ? 0 : req.getPage(), req.getPageSize(), Sort.by(req.getOrdering()));
        }

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
        //如果有查询条件
        if (!StringUtils.isBlank(req.getQuery())) {
            BoolQueryBuilder shouldQuery = QueryBuilders.boolQuery();
            shouldQuery.should(QueryBuilders.wildcardQuery("title","*" +  req.getQuery() + "*"));
            shouldQuery.should(QueryBuilders.wildcardQuery("content","*" +  req.getQuery() + "*"));
            boolQuery.must(shouldQuery);
            query.withQuery(boolQuery);
        }
        SearchHits<DocumentEntity> searchHits = elasticsearchRestTemplate.search(query.build(), DocumentEntity.class, IndexCoordinates.of("es_document"));

        List<DocumentEntity> dataList = new ArrayList<>();
        searchHits.getSearchHits().forEach(hit -> dataList.add(hit.getContent()));
        //取需要的结果
        int firstIndex = pageRequest.getPageNumber();
        List<DocumentEntity> collect = dataList.stream().skip((long) firstIndex * pageRequest.getPageSize()).limit(pageRequest.getPageSize()).collect(Collectors.toList());
        return new PageImpl<>(collect, pageRequest, dataList.size());
    }

    private FileContentTypeEnum getEnums(String fileName) {
        int i = fileName.lastIndexOf(".");
        String suffix = fileName.substring(i + 1);
        return FileContentTypeEnum.getBySuffix(suffix);
    }


    private String getContentTypeByFileName(String fileName) {
        if (isBlank(fileName)) {
            return FileContentTypeEnum.DOWN_LOAD.getContentType();
        }
        String contentType = getEnums(fileName).getContentType();
        return contentType;
    }

    /**
     * 根据文件名找到真实的文件并转化为流返回页面
     *
     * @param fileName 文件名 uuid
     * @param resp     响应对象
     */
    private void getOutPutStream(String fileName, HttpServletResponse resp) {
        InputStream ins;
        try {
            if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                //根据文件名找到存储地址，转化为流
                String basePath = BASE_FILE_PATH + fileName;
                ins = Files.newInputStream(Paths.get(basePath));
            } else {
                ClassPathResource fileIcon = new ClassPathResource("file.png");
                File file = fileIcon.getFile();
                ins = Files.newInputStream(file.toPath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            resp.setContentType(getContentTypeByFileName(fileName));
            resp.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));
            try (ServletOutputStream outputStream = resp.getOutputStream()) {
                int bytesRead;
                byte[] buffer = new byte[1024];
                while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (FileNotFoundException e) {
            throw new BaseException("50001", "找不到文件");
        } catch (Exception e) {
            throw new BaseException("4001", "无权限访问");
        }
    }


    private String saveUploadedFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + File.separator + file.getOriginalFilename();
        String uploadDirectory = BASE_FILE_PATH + fileName;
        Path uploadPath = Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        InputStream inputStream = file.getInputStream();
        File dstFile = new File(uploadDirectory);
        if (!dstFile.exists()) {
            dstFile.createNewFile();
        } else {
            dstFile.delete();
            dstFile.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(dstFile);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) > -1) {
            fileOutputStream.write(bytes, 0, len);
        }
        return fileName;
    }

    private void updateDocumentMetadata(Document documentDTO, String uploadedFileName) {
        documentDTO.setCreated(OffsetDateTime.now());
        documentDTO.setAdded(OffsetDateTime.now());
        documentDTO.setModified(OffsetDateTime.now());
        documentDTO.content("");
        documentDTO.setAdded(OffsetDateTime.now());
        documentDTO.setArchivedFileName(JsonNullable.of(uploadedFileName));
    }
}

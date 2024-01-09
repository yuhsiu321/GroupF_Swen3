package org.openapitools.api;


import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.openapitools.configuration.ChannelMap;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.persistence.entities.DocumentEntity;
import org.openapitools.service.DocumentService;
import org.openapitools.service.model.Document;
import org.openapitools.service.model.DocumentPageQuertReq;
import org.openapitools.service.model.DocumentQueryResponse;
import org.openapitools.util.EsBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;


@Controller
@RequestMapping
@CrossOrigin
public class ApiApiController implements ApiApi {

    private final NativeWebRequest request;

    @Resource
    private DocumentService documentService;

    @Autowired
    public ApiApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }


    @Override
    public ResponseEntity<Boolean> getDocumentThumb(
            @Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id
    ) {
        documentService.getDocumentThumbOfMinio(id);

         return new ResponseEntity<>( HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Map<Object, Object>> uploadDocument(
            @Parameter(name = "title", description = "") @Valid @RequestParam(value = "title", required = false) String title,
            @Parameter(name = "created", description = "") @Valid @RequestParam(value = "created", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime created,
            @Parameter(name = "document_type", description = "") @Valid @RequestParam(value = "document_type", required = false) Integer documentType,
            @Parameter(name = "tags", description = "") @Valid @RequestPart(value = "tags", required = false) List<Integer> tags,
            @Parameter(name = "correspondent", description = "") @Valid @RequestParam(value = "correspondent", required = false) Integer correspondent,
            @Parameter(name = "document", description = "") @RequestPart(value = "document", required = false) List<MultipartFile> document
    ) {
        Document document1 = new Document();
        document1.setTitle(JsonNullable.of(title));
        document1.setCreated(OffsetDateTime.now());
        document1.setDocumentType(JsonNullable.of(documentType));
        document1.setTags(JsonNullable.of(tags));
        document1.setCorrespondent(JsonNullable.of(correspondent));
        document1.setCorrespondent(JsonNullable.of(correspondent));
        //每次上传一个文件，所以取第一个即可
        documentService.uploadDocument(document1, document.get(0));
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //上传完成之后通过websocket刷新进度,这里实际应该发送给对应的channel,但是因为这个项目前端工程没法改，发送给所有的客户端
                ChannelMap.getChannelMap().forEach((channelId, channel) -> {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("status", "SUCCESS");
                    data.put("filename", title);
                    TextWebSocketFrame tws = new TextWebSocketFrame(JSON.toJSONString(data));
                    channel.writeAndFlush(tws);
                });
            }
        };
        return new ResponseEntity<>(objectObjectHashMap, HttpStatus.OK);
    }


    public ResponseEntity<DocumentQueryResponse> getDocuments(
            @Parameter(name = "Page", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "Page", required = false) Integer page,
            @Parameter(name = "page_size", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "page_size", required = false) Integer pageSize,
            @Parameter(name = "query", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "query", required = false) String query,
            @Parameter(name = "ordering", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "ordering", required = false) String ordering,
            @Parameter(name = "tags__id__all", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "tags__id__all", required = false) List<Integer> tagsIdAll,
            @Parameter(name = "document_type__id", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "document_type__id", required = false) Integer documentTypeId,
            @Parameter(name = "storage_path__id__in", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "storage_path__id__in", required = false) Integer storagePathIdIn,
            @Parameter(name = "correspondent__id", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "correspondent__id", required = false) Integer correspondentId,
            @Parameter(name = "truncate_content", description = "", in = ParameterIn.QUERY) @Valid @RequestParam(value = "truncate_content", required = false) Boolean truncateContent
    ) {

        DocumentPageQuertReq req = new DocumentPageQuertReq();
        req.setPage(page);
        req.setPageSize(pageSize);
        req.setQuery(query);
        req.setOrdering(ordering);
        Page<DocumentEntity> documents = documentService.getDocuments(req);
        DocumentQueryResponse documentQueryResponse = new DocumentQueryResponse();
        Object[] array = Stream.of(5, 5).toArray();
        documentQueryResponse.setAll(array);
        documentQueryResponse.setNext(documents.getNumber() + 1);
        documentQueryResponse.setPrevious(documents.getNumber() - 1);
        documentQueryResponse.setCount(documents.getContent().size());
        documentQueryResponse.setResults(documents.getContent());
        return new ResponseEntity<>(documentQueryResponse, HttpStatus.OK);

    }


    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/test/"
    )
    public ResponseEntity<Map<Object, Object>> test() {

//        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
//        SearchHits<DocumentEntity> searchHits = elasticsearchRestTemplate.search(query.build(), DocumentEntity.class, IndexCoordinates.of("es_document"));

//        //基本设置
        Map<String, Object> settingMap = new HashMap<>();

        org.springframework.data.elasticsearch.core.document.Document settingDocument = org.springframework.data.elasticsearch.core.document.Document.from(settingMap);

        Map<String, String> mapping = new HashMap<>();

        Map<String, Object> mapping1 = new HashMap<>();

        Map<String, String> basicFieldMap = EsBeanUtils.getBasicFieldMap(new DocumentEntity());
        Set<String> keySet = basicFieldMap.keySet();
        Map<String, Object> properties = new HashMap<>();
        for (String str : keySet) {
            Map<String, Object> message = new HashMap<>();
            message.put("type", basicFieldMap.get(str));
            properties.put(str, message);
        }
        mapping1.put("properties", properties);

        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of("es_document"));
        if (!existsIndex("es_document")) {
            indexOperations.create(settingMap);
        }


        //字段映射
        org.springframework.data.elasticsearch.core.document.Document dataDocument = org.springframework.data.elasticsearch.core.document.Document.from(mapping1);
        boolean bool = indexOperations.putMapping(dataDocument);
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);

    }


    public boolean existsIndex(String index) {
        try {
            GetIndexRequest request = new GetIndexRequest();
            request.indices(index);
            boolean exists = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(index)).exists();
            return exists;
        } catch (Exception e) {

        }
        return false;
    }

}

package org.openapitools.service.Impl;

import org.openapitools.persistence.entities.DocumentEntity;
import org.openapitools.persistence.repositories.DocumentRepository;
import org.openapitools.service.DocumentService;
import org.openapitools.service.mapper.DocumentMapper;
import org.openapitools.service.model.Document;
import org.openapitools.service.model.GetDocument200Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;


    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }


    @Override
    public void uploadDocument(Document documentDTO, MultipartFile document) {
        try {
            String uploadedFileName = saveUploadedFile(document);
            updateDocumentMetadata(documentDTO, uploadedFileName);

            // Save documentDTO (with updated metadata) to the database using the repository
            DocumentEntity documentToBeSaved = documentMapper.toEntity(documentDTO);
            documentRepository.save(documentToBeSaved);

            // Additional logic if needed, such as handling relationships or other operations
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file processing errors or database save exceptions
            // You might want to throw an exception or handle it accordingly
        }
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

    private String saveUploadedFile(MultipartFile file) throws IOException {
        String uploadDirectory = "your/upload/directory/";
        Path uploadPath = Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDirectory, originalFileName);
        file.transferTo(filePath);

        return originalFileName;
    }

    private void updateDocumentMetadata(Document documentDTO, String uploadedFileName) {
        documentDTO.setCreated(OffsetDateTime.now());
        documentDTO.setAdded(OffsetDateTime.now());
        documentDTO.setModified(OffsetDateTime.now());
        documentDTO.content("");
        documentDTO.setAdded(OffsetDateTime.now());
    }
}

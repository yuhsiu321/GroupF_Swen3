package org.openapitools.service;

import org.openapitools.persistence.entities.DocumentEntity;
import org.openapitools.service.model.Document;
import org.openapitools.service.model.DocumentPageQuertReq;
import org.openapitools.service.model.GetDocument200Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.util.List;

public interface DocumentService {
    void uploadDocument(Document documentDTO, MultipartFile document);
    Document getDocument(Integer id, Integer page, Boolean fullPermissions);
    ResponseEntity<Document> getDocument(Integer id);


    ResponseEntity<Boolean> getDocumentThumb(Integer id);

    public ResponseEntity<Boolean> getDocumentThumbOfMinio(Integer id);


    Page<DocumentEntity> getDocuments(DocumentPageQuertReq req);
}

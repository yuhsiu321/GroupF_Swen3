package org.openapitools.service;

import org.openapitools.service.model.Document;
import org.openapitools.service.model.GetDocument200Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;

public interface DocumentService {
    void uploadDocument(Document documentDTO, MultipartFile document);
    Document getDocument(Integer id, Integer page, Boolean fullPermissions);
    ResponseEntity<Document> getDocument(Integer id);


}

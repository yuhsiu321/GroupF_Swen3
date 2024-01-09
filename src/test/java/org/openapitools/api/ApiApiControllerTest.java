package org.openapitools.api;

import org.junit.jupiter.api.Test;
import org.openapitools.configuration.WebSocketServer;
import org.openapitools.persistence.entities.DocumentEntity;
import org.openapitools.service.DocumentService;
import org.openapitools.service.model.Document;
import org.openapitools.service.model.DocumentPageQuertReq;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.annotation.Resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 做单元测试时不会启动服务器，导致websocket报错
 * 加(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApiControllerTest {
    @Resource
    private DocumentService documentService;
    @Test
    void getDocumentThumb() {
        ResponseEntity<Boolean> documentThumb = documentService.getDocumentThumb(1);
        System.out.println(documentThumb);
    }

    @Test
    void uploadDocument() throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("file.png");
        InputStream inputStream = classPathResource.getInputStream();
        MultipartFile multipartFile = new MockMultipartFile("name",inputStream);
        documentService.uploadDocument(new Document(),multipartFile);
    }

    @Test
    void getDocuments() {
        DocumentPageQuertReq documentPageQuertReq = new DocumentPageQuertReq();
        documentPageQuertReq.setPageSize(50);
        documentPageQuertReq.setQuery("ABC");
        Page<DocumentEntity> documents = documentService.getDocuments(documentPageQuertReq);
        System.out.println(documents.getContent());
    }
}
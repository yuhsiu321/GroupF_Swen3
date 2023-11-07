package org.openapitools.service.mapper.Impl;

import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.persistence.entities.DocumentEntity;
import org.openapitools.service.mapper.DocumentMapper;
import org.openapitools.service.model.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapperImpl extends AbstractMapper<DocumentEntity, Document> implements DocumentMapper {

    @Override
    public Document toDto(DocumentEntity documentEntity) {

        return Document.builder()
                .id(documentEntity.getId())
                .correspondent(JsonNullable.of(documentEntity.getCorrespondent()))
                .documentType(JsonNullable.of(documentEntity.getDocumentType()))
                .storagePath(JsonNullable.of(documentEntity.getStoragePath()))
                .title(JsonNullable.of(documentEntity.getTitle()))
                .content(JsonNullable.of(documentEntity.getContent()))
                .created(documentEntity.getCreated())
                .createdDate(documentEntity.getCreatedDate())
                .modified(documentEntity.getModified())
                .added(documentEntity.getAdded())
                .archiveSerialNumber(JsonNullable.of(documentEntity.getArchiveSerialNumber()))
                .originalFileName(JsonNullable.of(documentEntity.getOriginalFileName()))
                .archivedFileName(JsonNullable.of(documentEntity.getArchivedFileName()))
                .build();
    }

    @Override
    public DocumentEntity toEntity(Document document) {

        return DocumentEntity.builder()
                .id(document.getId())
                .correspondent(document.getCorrespondent().get())
                .documentType(document.getDocumentType().get())
                .storagePath(document.getStoragePath().get())
                .title(document.getTitle().get())
                .content(document.getContent().get())
                .created(document.getCreated())
                .createdDate(document.getCreatedDate())
                .modified(document.getModified())
                .added(document.getAdded())
                .archiveSerialNumber(document.getArchiveSerialNumber().get())
                .originalFileName(document.getOriginalFileName().get())
                .archivedFileName(document.getArchivedFileName().get())
                .build();
    }
}

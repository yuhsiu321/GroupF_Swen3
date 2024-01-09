package org.openapitools.persistence.entities;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @org.springframework.data.annotation.Id
    @Field(type = FieldType.Long)
    private Integer id;

    @Field(type = FieldType.Integer)
    private Integer correspondent;

    @Field(type = FieldType.Integer)
    private Integer documentType;

    @Field(type = FieldType.Integer)
    private Integer storagePath;

    @Field(type = FieldType.Keyword)
    private String title;

    @Field(type = FieldType.Keyword)
    private String content;






    @Field(type = FieldType.Text)
    private String archiveSerialNumber;

    @Field(type = FieldType.Text)
    private String originalFileName;

    @Field(type = FieldType.Text)
    private String archivedFileName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCorrespondent() {
        return correspondent;
    }

    public void setCorrespondent(Integer correspondent) {
        this.correspondent = correspondent;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public Integer getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(Integer storagePath) {
        this.storagePath = storagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




    public String getArchiveSerialNumber() {
        return archiveSerialNumber;
    }

    public void setArchiveSerialNumber(String archiveSerialNumber) {
        this.archiveSerialNumber = archiveSerialNumber;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getArchivedFileName() {
        return archivedFileName;
    }

    public void setArchivedFileName(String archivedFileName) {
        this.archivedFileName = archivedFileName;
    }
}

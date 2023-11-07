package org.openapitools.persistence.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer correspondent;

    private Integer documentType;

    private Integer storagePath;

    private String title;

    private String content;

    private List<Integer> tags;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime created;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime createdDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime modified;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime added;

    private String archiveSerialNumber;

    private String originalFileName;

    private String archivedFileName;

    @ManyToOne
    @JoinColumn(name = "correspondent_id")
    private CorrespondentEntity correspondentEntity;

    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentTypeEntity documentTypeEntity;

    @ManyToMany
    private List<DocTagEntity> docTags;
}

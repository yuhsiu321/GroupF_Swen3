package org.openapitools.persistence.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorrespondentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Long matchingAlgorithm;

    private Boolean isInsensitive;

    private Long documentCount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime lastCorrespondence;

    @OneToMany(mappedBy = "correspondent")
    private Set<DocumentEntity> correspondentDocumentEntities;


}

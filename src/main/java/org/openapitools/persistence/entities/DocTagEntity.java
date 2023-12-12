package org.openapitools.persistence.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String slug;
    private String name;
    private String color;
    private String match;
    private long matchingAlgorithm;
    private boolean isInsensitive;
    private boolean isInboxTag;
    private long documentCount;


}

package org.openapitools.persistence.repositories;

import org.openapitools.persistence.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {
}

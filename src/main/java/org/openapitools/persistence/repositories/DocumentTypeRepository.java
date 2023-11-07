package org.openapitools.persistence.repositories;

import org.openapitools.persistence.entities.DocumentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTypeRepository extends JpaRepository<DocumentTypeEntity, Long> {
}

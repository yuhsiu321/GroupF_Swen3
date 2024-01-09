package org.openapitools.persistence.repositories;

import org.openapitools.persistence.entities.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {
    Page<DocumentEntity> findByTitleLike(String title, Pageable pageable);

}

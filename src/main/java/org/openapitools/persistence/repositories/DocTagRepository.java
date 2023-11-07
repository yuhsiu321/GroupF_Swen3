package org.openapitools.persistence.repositories;

import org.openapitools.persistence.entities.DocTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocTagRepository extends JpaRepository<DocTagEntity, Long> {
}

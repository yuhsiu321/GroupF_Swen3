package org.openapitools.persistence.repositories;

import org.openapitools.persistence.entities.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserReposutory extends JpaRepository<AuthUser, Integer> {
}

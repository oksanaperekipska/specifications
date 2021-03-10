package com.demo.repository;

import com.demo.domain.PersonStatus;

import com.demo.domain.enumeration.PStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PersonStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonStatusRepository extends JpaRepository<PersonStatus, PStatus> {
}

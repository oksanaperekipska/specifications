package com.demo.repository;

import com.demo.domain.Group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Group entity.
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    @Query(value = "select distinct gp from Group gp left join fetch gp.members",
        countQuery = "select count(distinct gp) from Group gp")
    Page<Group> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct gp from Group gp left join fetch gp.members")
    List<Group> findAllWithEagerRelationships();

    @Query("select gp from Group gp left join fetch gp.members where gp.id =:id")
    Optional<Group> findOneWithEagerRelationships(@Param("id") Long id);

    @EntityGraph(attributePaths = {"superAdmin", "tags"})
    @Query("select gp from Group gp where gp.id in :ids")
    List<Group> findCustomById(@Param("ids")Iterable<Long> ids, Sort sort);
}

package com.dmscherr.engage.repository;

import com.dmscherr.engage.model.Category;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Can add custom queries here that can be used in the Controller methods
 */
@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @Query(
            value = "WITH RECURSIVE ancestors(id, parent_id, name, lvl) AS ("
                    + "   SELECT cat.id, cat.parent_id, cat.name, 1 AS lvl "
                    + "   FROM categories cat "
                    + "   WHERE cat.id = :categoryId "
                    + "   UNION ALL "
                    + "   SELECT parent.id, parent.parent_id, parent.name, child.lvl + 1 AS lvl "
                    + "   FROM categories parent "
                    + "   JOIN ancestors child "
                    + "   ON parent.id = child.parent_id "
                    + " )"
                    + "SELECT name from ancestors ORDER BY lvl DESC"
            , nativeQuery = true)
    List<String> findAncestry(@Param("categoryId") Long categoryId);
}
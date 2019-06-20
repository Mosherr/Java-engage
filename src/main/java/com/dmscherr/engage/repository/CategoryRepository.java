package com.dmscherr.engage.repository;

import com.dmscherr.engage.model.Category;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Can add custom queries here that can be used in the Controller methods
 */
@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {


}
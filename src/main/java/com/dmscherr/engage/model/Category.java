package com.dmscherr.engage.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Category Model used to define an object/db table
 */
@Entity
@JsonIgnoreProperties({"children"})
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private String name;

	@OneToMany
	@JoinColumn(name = "parent_id")
	private List<Category> children;

	@ManyToOne
	@JoinColumn(name="parent_id")
	private Category parent;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(Category children) {
		this.children.add(children);
	}
}

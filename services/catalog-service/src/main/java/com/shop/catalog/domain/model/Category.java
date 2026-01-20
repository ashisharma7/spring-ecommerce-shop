package com.shop.catalog.domain.model;

import com.shop.catalog.domain.exception.InvalidCategoryStateException;
import com.shop.catalog.util.ValueValidators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@SuperBuilder
public class Category extends AuditableEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @Size(min = 3, max = 255, message = "Category name must be between 3 to 255 characters")
    private String name;

    @Column(nullable = false)
    @Size(min = 5, max = 2000, message = "Category description must be between 5 to 2000 characters")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> subcategories = new HashSet<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();

    public void updateName(String name) {
        if (ValueValidators.isNullOrBlank(name))
            throw new InvalidCategoryStateException("Category name cannot be null or blank");
        if (ValueValidators.isNotValidLength(name, 3, 255))
            throw new InvalidCategoryStateException("Category name must be between 3 to 255 characters");
        this.name = name;
    }

    public void updateDescription(String description) {
        if (ValueValidators.isNullOrBlank(description))
            throw new InvalidCategoryStateException("Category description cannot be null or blank");
        if (ValueValidators.isNotValidLength(description, 5, 2000))
            throw new InvalidCategoryStateException("Category description must be between 5 to 2000 characters");
        this.description = description;
    }

    public void updateIsActive(Boolean isActive) {
        if (Objects.isNull(isActive))
            throw new InvalidCategoryStateException("Category active status cannot be null");
        this.isActive = isActive;
    }

    public void updateParent(Category parent) {
        if (Objects.isNull(parent))
            throw new InvalidCategoryStateException("Parent category cannot be updated as null, use removeParent() instead");
        if (Objects.nonNull(this.parent))
            this.parent.subcategories.remove(this);
        this.parent = parent;
        this.parent.subcategories.add(this);
    }

    public void removeParent() {
        if (Objects.isNull(this.parent))
            throw new InvalidCategoryStateException("Category has no parent");
        this.parent.subcategories.remove(this);
        this.parent = null;
    }

    public void addSubCategory(Category subCategory) {
        if (Objects.isNull(subCategory))
            throw new InvalidCategoryStateException("Subcategory to add cannot be null");
        subCategory.updateParent(this);
    }

    public void addProduct(Product product) {
        if (Objects.isNull(product))
            throw new InvalidCategoryStateException("Product to add cannot be null");
        product.updateCategory(this);
    }

    @PrePersist
    @PreUpdate
    public void validate() {
        // Validate name
        if (ValueValidators.isNullOrBlank(name))
            throw new InvalidCategoryStateException("Category name cannot be null or blank");
        if (ValueValidators.isNotValidLength(name, 3, 255))
            throw new InvalidCategoryStateException("Category name must be between 3 to 255 characters");

        // Validate description
        if (ValueValidators.isNullOrBlank(description))
            throw new InvalidCategoryStateException("Category description cannot be null or blank");
        if (ValueValidators.isNotValidLength(description, 5, 2000))
            throw new InvalidCategoryStateException("Category description must be between 5 to 2000 characters");

        //Validate isActive
        if (Objects.isNull(isActive))
            throw new InvalidCategoryStateException("Category active status cannot be null");
    }

}

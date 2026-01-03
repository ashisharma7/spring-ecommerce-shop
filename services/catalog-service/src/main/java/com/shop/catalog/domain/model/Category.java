package com.shop.catalog.domain.model;

import com.shop.catalog.domain.exception.InvalidCategoryStateException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter @Builder
public class Category {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> subcategories;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

    public static Category createCategory(String name, String description, Boolean isActive) {
        return Category.builder()
                .name(name)
                .description(description)
                .isActive(isActive)
                .createdAt(Instant.now())
                .build();
    }

    public static Category createCategory(@NotBlank String name,
                                          @NotBlank String description,
                                          @NonNull Boolean isActive,
                                          @NonNull Category parent) {
        Category category = createCategory(name, description, isActive);
        parent.addSubCategory(category);
        return category;
    }

    public static Category createCategory(@NotBlank String name, @NotBlank String description, @NonNull Boolean isActive,
                                          @NotEmpty List<Category> subcategories) {
        Category category = createCategory(name, description, isActive);
        subcategories.forEach(category::addSubCategory);
        return category;
    }

    public void addParent(@NonNull Category parent) {
        if (this.parent != null) {
            throw new InvalidCategoryStateException("Parent can't be overridden you need to remove current before adding another");
        }
        this.parent = parent;
    }

    public void removeParent() {
        if (this.parent == null) {
            throw new InvalidCategoryStateException("Category has no parent");
        }
        this.parent.subcategories.remove(this);
        this.parent = null;
    }

    public void addSubCategory(@NonNull Category subCategory) {
        subCategory.addParent(this);
        this.subcategories.add(subCategory);
    }

    public void addProduct(@NonNull Product product) {
        this.products.add(product.assignCategory(this));
    }
}

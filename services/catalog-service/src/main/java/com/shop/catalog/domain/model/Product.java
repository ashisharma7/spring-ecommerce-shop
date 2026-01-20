package com.shop.catalog.domain.model;

import com.shop.catalog.domain.exception.InvalidProductStateException;
import com.shop.catalog.util.ValueValidators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter @SuperBuilder
public class Product extends AuditableEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @Size(min = 3, max = 255, message = "Product Name must be between 3 and 255 characters")
    private String name;

    @Column(nullable = false)
    @Size(min = 5, max = 2000, message = "Product Description must be between 5 and 2000 characters")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String image;

    @Column(nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Category category;

    public void updateName(String name) {
        if (ValueValidators.isNullOrBlank(name))
            throw new InvalidProductStateException("Product Name cannot be null or blank");
        if (ValueValidators.isNotValidLength(name, 3, 255))
            throw new InvalidProductStateException("Product Name must be between 3 to 255 characters");
        this.name = name;
    }

    public void updateDescription(String description) {
        if (ValueValidators.isNullOrBlank(description))
            throw new InvalidProductStateException("Product Description cannot be null or blank");
        if (ValueValidators.isNotValidLength(description, 5, 2000))
            throw new InvalidProductStateException("Product Description must be between 5 to 2000 characters");
        this.description = description;
    }

    public void updatePrice(BigDecimal price) {
        if (Objects.isNull(price))
            throw new InvalidProductStateException("Product Price cannot be null");
        if (ValueValidators.isEqualOrLessThanZero(price))
            throw new InvalidProductStateException("Product Price cannot be zero or negative");
        this.price = price;
    }

    public void updateImage(String image) {
        this.image = image;
    }

    public void updateAvailable(Boolean available) {
        if (Objects.isNull(available))
            throw new InvalidProductStateException("Product Availability status cannot be null");
        this.available = available;
    }

    public void updateCategory(Category category) {
        if (Objects.isNull(category))
            throw new InvalidProductStateException("Product must have a category");
        this.category = category;
        category.getProducts().add(this);
    }

    @PrePersist
    @PreUpdate
    public void validate() {
        // Validate name
        if (ValueValidators.isNullOrBlank(name))
            throw new InvalidProductStateException("Product Name cannot be null or blank");
        if (ValueValidators.isNotValidLength(name, 3,255))
            throw new InvalidProductStateException("Product Name must be between 3 to 255 characters");

        // Validate description
        if (ValueValidators.isNullOrBlank(description))
            throw new InvalidProductStateException("Product Description cannot be null or blank");
        if (ValueValidators.isNotValidLength(description, 5,2000))
            throw new InvalidProductStateException("Product Description must be between 5 to 2000 characters");

        // Validate price
        if (Objects.isNull(price))
            throw new InvalidProductStateException("Product Price cannot be null");
        if (ValueValidators.isEqualOrLessThanZero(price))
            throw new InvalidProductStateException("Product Price cannot be less than or equal to zero");

        // Validate availability
        if (Objects.isNull(available))
            throw new InvalidProductStateException("Product Availability status cannot be null");

        // Validate category
        if (Objects.isNull(category))
            throw new InvalidProductStateException("Product must have a category");
    }

}

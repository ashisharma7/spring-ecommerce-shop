package com.shop.catalog.domain.model;

import com.shop.catalog.domain.exception.InvalidProductStateException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter @Builder
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String image;

    @Column(nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Category category;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public static Product createProduct( @NotBlank String name,
                                        @NotBlank String description,
                                        @NonNull BigDecimal price,
                                        String image,
                                        @NonNull Boolean available,
                                        @NonNull Category category) {
        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .image(image)
                .available(available)
                .category(category)
                .createdAt(Instant.now())
                .build();
    }

    public Product updateName(@NotBlank String name) {
        this.name = name;
        return this;
    }

    public Product updateDescription(@NotBlank String description) {
        this.description = description;
        return this;
    }

    public Product updatePrice(@NonNull BigDecimal price) {
        if (BigDecimal.ZERO.compareTo(price) >= 0) {
            throw new InvalidProductStateException("Price cannot be zero or negative");
        }
        this.price = price;
        return this;
    }

    public Product updateImage(@NotBlank String image) {
        this.image = image;
        return this;
    }

    public Product updateAvailable(@NonNull Boolean available) {
        this.available = available;
        return this;
    }

    public Product assignCategory(@NonNull Category category) {
        this.category = category;
        return this;
    }

}

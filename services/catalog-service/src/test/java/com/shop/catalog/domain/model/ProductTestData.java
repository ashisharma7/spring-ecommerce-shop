package com.shop.catalog.domain.model;

import java.math.BigDecimal;

public final class ProductTestData {
    public static final String VALID_PRODUCT_NAME = "Smartphone";
    public static final String VALID_PRODUCT_DESCRIPTION = "Smartphone with 6GB RAM";
    public static final BigDecimal VALID_PRODUCT_PRICE = BigDecimal.valueOf(10000);
    public static final Boolean VALID_PRODUCT_ACTIVE = true;

    private ProductTestData() {
    }

    // Positive Test Data
    public static Product validProduct() {
        Product product = new Product();
        product.updateName(VALID_PRODUCT_NAME);
        product.updateDescription(VALID_PRODUCT_DESCRIPTION);
        product.updatePrice(VALID_PRODUCT_PRICE);
        product.updateImage(null);
        product.updateAvailable(VALID_PRODUCT_ACTIVE);
        product.updateCategory(CategoryTestData.validCategory());
        return product;
    }

    public static Product validProduct_WithImage() {
        Product product = validProduct();
        product.updateImage("image-url");
        return product;
    }

    // Negative Test Data
    public static Product invalidProduct_NullName() {
        return Product.builder()
                .name(null)
                .description(VALID_PRODUCT_DESCRIPTION)
                .price(VALID_PRODUCT_PRICE)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_BlankName() {
        return Product.builder()
                .name("   ")
                .description(VALID_PRODUCT_DESCRIPTION)
                .price(VALID_PRODUCT_PRICE)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_ShortName() {
        return Product.builder()
                .name("AB")
                .description(VALID_PRODUCT_DESCRIPTION)
                .price(VALID_PRODUCT_PRICE)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_LongName() {
        return Product.builder()
                .name("A".repeat(256))
                .description(VALID_PRODUCT_DESCRIPTION)
                .price(VALID_PRODUCT_PRICE)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_NullDescription() {
        return Product.builder()
                .name(VALID_PRODUCT_NAME)
                .description(null)
                .price(VALID_PRODUCT_PRICE)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_BlankDescription() {
        return Product.builder()
                .name(VALID_PRODUCT_NAME)
                .description("   ")
                .price(VALID_PRODUCT_PRICE)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_ShortDescription() {
        return Product.builder()
                .name(VALID_PRODUCT_NAME)
                .description("Desc")
                .price(VALID_PRODUCT_PRICE)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_LongDescription() {
        return Product.builder()
                .name(VALID_PRODUCT_NAME)
                .description("D".repeat(2001))
                .price(VALID_PRODUCT_PRICE)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static  Product invalidProduct_NullPrice() {
        return Product.builder()
                .name(VALID_PRODUCT_NAME)
                .description(VALID_PRODUCT_DESCRIPTION)
                .price(null)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_ZeroPrice() {
        return Product.builder()
                .name(VALID_PRODUCT_NAME)
                .description(VALID_PRODUCT_DESCRIPTION)
                .price(BigDecimal.ZERO)
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_NegativePrice() {
        return Product.builder()
                .name(VALID_PRODUCT_NAME)
                .description(VALID_PRODUCT_DESCRIPTION)
                .price(BigDecimal.valueOf(-100))
                .available(VALID_PRODUCT_ACTIVE)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static Product invalidProduct_NullAvailability() {
        return Product.builder()
                .name(VALID_PRODUCT_NAME)
                .description(VALID_PRODUCT_DESCRIPTION)
                .price(VALID_PRODUCT_PRICE)
                .available(null)
                .category(CategoryTestData.validCategory())
                .build();
    }

    public static  Product invalidProduct_NullCategory() {
        return Product.builder()
                .name(VALID_PRODUCT_NAME)
                .description(VALID_PRODUCT_DESCRIPTION)
                .price(VALID_PRODUCT_PRICE)
                .available(VALID_PRODUCT_ACTIVE)
                .category(null)
                .build();
    }

}

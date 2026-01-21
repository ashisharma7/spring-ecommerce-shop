package com.shop.catalog.domain.model;

import com.shop.catalog.domain.exception.InvalidProductStateException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    @Test
    void shouldCreateProductWithoutImage() {
        Product product = ProductTestData.validProduct();

        assertEquals(ProductTestData.VALID_PRODUCT_NAME, product.getName());
        assertEquals(ProductTestData.VALID_PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(ProductTestData.VALID_PRODUCT_PRICE, product.getPrice());
        assertNull(product.getImage());
        assertEquals(ProductTestData.VALID_PRODUCT_ACTIVE, product.getAvailable());
        assertNotNull(product.getCategory());
        assertDoesNotThrow(product::validate);
    }

    @Test
    void shouldCreateProductWithImage() {
        Product product = ProductTestData.validProduct_WithImage();

        assertEquals(ProductTestData.VALID_PRODUCT_NAME, product.getName());
        assertEquals(ProductTestData.VALID_PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(ProductTestData.VALID_PRODUCT_PRICE, product.getPrice());
        assertEquals("image-url", product.getImage());
        assertEquals(ProductTestData.VALID_PRODUCT_ACTIVE, product.getAvailable());
        assertNotNull(product.getCategory());
        assertDoesNotThrow(product::validate);
    }

    @Test
    void shouldUpdateNameWithValidName() {
        Product product = ProductTestData.validProduct();

        assertEquals(ProductTestData.VALID_PRODUCT_NAME, product.getName());

        String newName = "Updated Product Name";
        product.updateName(newName);

        assertDoesNotThrow(() -> product.updateName(newName));
        assertEquals(newName, product.getName());
    }

    @Test
    void shouldUpdateDescriptionWithValidDescription() {
        Product product = ProductTestData.validProduct();

        assertEquals(ProductTestData.VALID_PRODUCT_DESCRIPTION, product.getDescription());

        String newDescription = "Updated Product Description";
        product.updateDescription(newDescription);

        assertDoesNotThrow(() -> product.updateDescription(newDescription));
        assertEquals(newDescription, product.getDescription());
    }

    @Test
    void shouldUpdatePriceWithValidPrice() {
        Product product = ProductTestData.validProduct();

        assertEquals(ProductTestData.VALID_PRODUCT_PRICE, product.getPrice());

        BigDecimal newPrice = BigDecimal.TEN;
        product.updatePrice(newPrice);

        assertDoesNotThrow(() -> product.updatePrice(newPrice));
        assertEquals(newPrice, product.getPrice());
    }

    @Test
    void shouldUpdateImageWithValidImage() {
        Product product = ProductTestData.validProduct();

        assertNull(product.getImage());

        String newImage = "image.jpg";
        product.updateImage(newImage);

        assertEquals(newImage, product.getImage());
    }

    @Test
    void shouldUpdateAvailabilityStatus() {
        Product product = ProductTestData.validProduct();

        assertEquals(ProductTestData.VALID_PRODUCT_ACTIVE, product.getAvailable());

        product.updateAvailable(Boolean.FALSE);

        assertDoesNotThrow(() -> product.updateAvailable(Boolean.FALSE));
        assertEquals(Boolean.FALSE, product.getAvailable());
    }

    @Test
    void shouldUpdateCategoryWithValidCategory() {
        Product product = ProductTestData.validProduct();
        Category newCategory = CategoryTestData.validCategory();

        assertNotNull(product.getCategory());

        product.updateCategory(newCategory);

        assertDoesNotThrow(() -> product.updateCategory(newCategory));
        assertEquals(newCategory, product.getCategory());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Product product = ProductTestData.invalidProduct_NullName();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Name cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        Product product = ProductTestData.invalidProduct_BlankName();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Name cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenNameIsTooShort() {
        Product product = ProductTestData.invalidProduct_ShortName();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Name must be between 3 to 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenNameExceedsMaxLength() {
        Product product = ProductTestData.invalidProduct_LongName();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Name must be between 3 to 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        Product product = ProductTestData.invalidProduct_NullDescription();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Description cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        Product product = ProductTestData.invalidProduct_BlankDescription();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Description cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort() {
        Product product = ProductTestData.invalidProduct_ShortDescription();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Description must be between 5 to 2000 characters");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionExceedsMaxLength() {
        Product product = ProductTestData.invalidProduct_LongDescription();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Description must be between 5 to 2000 characters");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        Product product = ProductTestData.invalidProduct_NullPrice();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Price cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        Product product = ProductTestData.invalidProduct_ZeroPrice();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Price cannot be less than or equal to zero");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        Product product = ProductTestData.invalidProduct_NegativePrice();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Price cannot be less than or equal to zero");
    }

    @Test
    void shouldThrowExceptionWhenAvailabilityIsNull() {
        Product product = ProductTestData.invalidProduct_NullAvailability();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Availability status cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsNull() {
        Product product = ProductTestData.invalidProduct_NullCategory();

        assertThatThrownBy(product::validate)
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product must have a category");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNameWithNull() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updateName(null))
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Name cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNameWithBlank() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updateName("   "))
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Name cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNameWithShortName() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updateName("Na"))
                .isInstanceOf(InvalidProductStateException.class)
                .hasMessage("Product Name must be between 3 to 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNameWithLongName() {
        Product product = ProductTestData.validProduct();
        var longName = "N".repeat(256);

        assertThatThrownBy(() -> product.updateName(longName))
                .isInstanceOf(InvalidProductStateException.class)
                .hasMessage("Product Name must be between 3 to 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDescriptionWithNull() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updateDescription(null))
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Description cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDescriptionWithBlank() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updateDescription("   "))
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Description cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDescriptionWithShortDesc() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updateDescription("Desc"))
                .isInstanceOf(InvalidProductStateException.class)
                .hasMessage("Product Description must be between 5 to 2000 characters");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDescriptionWithLongDesc() {
        Product product = ProductTestData.validProduct();
        var longDescription = "D".repeat(2001);

        assertThatThrownBy(() -> product.updateDescription(longDescription))
                .isInstanceOf(InvalidProductStateException.class)
                .hasMessage("Product Description must be between 5 to 2000 characters");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPriceWithNull() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updatePrice(null))
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Price cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPriceWithZero() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updatePrice(BigDecimal.ZERO))
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Price cannot be zero or negative");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPriceWithNegative() {
        Product product = ProductTestData.validProduct();
        var negativePrice = BigDecimal.valueOf(-5);

        assertThatThrownBy(() -> product.updatePrice(negativePrice))
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Price cannot be zero or negative");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingAvailabilityWithNull() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updateAvailable(null))
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product Availability status cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingCategoryWithNull() {
        Product product = ProductTestData.validProduct();

        assertThatThrownBy(() -> product.updateCategory(null))
            .isInstanceOf(InvalidProductStateException.class)
            .hasMessage("Product must have a category");
    }

    @Test
    void shouldAddProductToCategoryWhenUpdatingCategory() {
        Product product = ProductTestData.validProduct();
        Category newCategory = CategoryTestData.validCategory();

        product.updateCategory(newCategory);

        assertEquals(newCategory, product.getCategory());
        assertTrue(newCategory.getProducts().contains(product));
    }

}

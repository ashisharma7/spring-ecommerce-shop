package com.shop.catalog.domain.model;

import com.shop.catalog.domain.exception.InvalidCategoryStateException;
import org.junit.jupiter.api.Test;

import static com.shop.catalog.domain.model.CategoryTestData.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateCategoryWithValidData() {
        Category category = validCategory();

        assertEquals(VALID_NAME, category.getName());
        assertEquals(VALID_DESCRIPTION, category.getDescription());
        assertTrue(category.getIsActive());
        assertNull(category.getParent());
        assertTrue(category.getSubcategories().isEmpty());
        assertTrue(category.getProducts().isEmpty());
        assertDoesNotThrow(category::validate);
    }

    @Test
    void shouldUpdateNameWithValidName() {
        Category category = invalidCategory_NullName();

        assertNull(category.getName());

        category.updateName(VALID_NAME);

        assertEquals(VALID_NAME, category.getName());
    }

    @Test
    void shouldUpdateDescriptionWithValidDescription() {
        Category category = invalidCategory_NullDescription();

        assertNull(category.getDescription());

        category.updateName(VALID_NAME);

        assertEquals(VALID_NAME, category.getName());
    }

    @Test
    void shouldUpdateIsActiveStatus() {
        Category category = validCategory();

        assertTrue(category.getIsActive());

        category.updateIsActive(Boolean.FALSE);

        assertFalse(category.getIsActive());
    }

    @Test
    void shouldAddSubcategoryToParent() {
        Category category = validCategory();

        assertTrue(category.getSubcategories().isEmpty());

        Category subcategory = validSubCategory();
        category.addSubCategory(subcategory);

        assertEquals(1, category.getSubcategories().size());
        assertEquals(category, subcategory.getParent());
    }

    @Test
    void shouldCreateCategoryWithParent() {
        Category category = validCategory_WithParent();
        Category parent = category.getParent();

        assertNotNull(parent);
        assertEquals(VALID_NAME, parent.getName());
        assertTrue(parent.getSubcategories().contains(category));
    }

    @Test
    void shouldUpdateParentCategory() {
        Category category = validCategory_WithParent();
        Category oldParent = category.getParent();

        assertEquals(oldParent, category.getParent());
        assertTrue(oldParent.getSubcategories().contains(category));

        Category newParent = validCategory();
        category.updateParent(newParent);

        assertEquals(newParent, category.getParent());
        assertFalse(oldParent.getSubcategories().contains(category));
        assertTrue(newParent.getSubcategories().contains(category));
    }

    @Test
    void shouldRemoveParentCategory() {
        Category category = validCategory_WithParent();
        Category parent = category.getParent();

        assertNotNull(category.getParent());
        assertTrue(parent.getSubcategories().contains(category));

        category.removeParent();

        assertNull(category.getParent());
        assertFalse(parent.getSubcategories().contains(category));
    }

    @Test
    void shouldAddProductToCategory() {
        Category category = validCategory();

        assertTrue(category.getProducts().isEmpty());

        Product product = ProductTestData.validProduct();
        category.addProduct(product);

        assertEquals(1, category.getProducts().size());
        assertEquals(category, product.getCategory());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Category category = invalidCategory_NullName();
        assertThatThrownBy(category::validate)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category name cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        Category category = invalidCategory_BlankName();
        assertThatThrownBy(category::validate)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category name cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenNameIsTooShort() {
        Category category = invalidCategory_ShortName();
        assertThatThrownBy(category::validate)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category name must be between 3 to 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenNameExceedsMaxLength() {
        Category category = invalidCategory_LongName();
        assertThatThrownBy(category::validate)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category name must be between 3 to 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenUpdateToInvalidName() {
        Category category = invalidCategory_NullName();
        String longName = "A".repeat(256);

        assertThatThrownBy(() -> category.updateName(null))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category name cannot be null or blank");

        assertThatThrownBy(() -> category.updateName("  "))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category name cannot be null or blank");

        assertThatThrownBy(() -> category.updateName("AB"))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category name must be between 3 to 255 characters");

        assertThatThrownBy(() -> category.updateName(longName))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category name must be between 3 to 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        Category category = invalidCategory_NullDescription();
        assertThatThrownBy(category::validate)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category description cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        Category category = invalidCategory_BlankDescription();
        assertThatThrownBy(category::validate)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category description cannot be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort() {
        Category category = invalidCategory_ShortDescription();
        assertThatThrownBy(category::validate)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category description must be between 5 to 2000 characters");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionExceedsMaxLength() {
        Category category = invalidCategory_LongDescription();
        assertThatThrownBy(category::validate)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category description must be between 5 to 2000 characters");
    }

    @Test
    void shouldThrowExceptionWhenUpdateToInvalidDescription() {
        Category category = invalidCategory_NullDescription();
        String longDesc = "A".repeat(2001);

        assertThatThrownBy(() -> category.updateDescription(null))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category description cannot be null or blank");

        assertThatThrownBy(() -> category.updateDescription("  "))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category description cannot be null or blank");

        assertThatThrownBy(() -> category.updateDescription("AB"))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category description must be between 5 to 2000 characters");

        assertThatThrownBy(() -> category.updateDescription(longDesc))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category description must be between 5 to 2000 characters");
    }

    @Test
    void shouldThrowExceptionWhenIsActiveIsNull() {
        Category category = invalidCategory_NullActive();
        assertThatThrownBy(category::validate)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category active status cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenUpdateIsActiveToNull() {
        Category category = validCategory();
        assertThatThrownBy(() -> category.updateIsActive(null))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category active status cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenRemovingParentThatDoesNotExist() {
        Category category = validCategory();

        assertThatThrownBy(category::removeParent)
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Category has no parent");
    }

    @Test
    void shouldThrowExceptionWhenAddingNullSubcategory() {
        Category category = validCategory();

        assertThatThrownBy(() -> category.addSubCategory(null))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Subcategory to add cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenAddingNullProduct() {
        Category category = validCategory();

        assertThatThrownBy(() -> category.addProduct(null))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Product to add cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingParentWithNull() {
        Category category = validCategory_WithParent();

        assertThatThrownBy(() ->  category.updateParent(null))
                .isInstanceOf(InvalidCategoryStateException.class)
                .hasMessageContaining("Parent category cannot be updated as null, use removeParent() instead");
    }

}

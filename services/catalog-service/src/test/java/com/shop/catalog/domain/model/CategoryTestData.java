package com.shop.catalog.domain.model;

import java.util.UUID;

public final class CategoryTestData {
    public static final UUID VALID_ID = UUID.randomUUID();
    public static final String VALID_NAME = "Electronics";
    public static final String VALID_DESCRIPTION = "Category for electronic products";
    public static final Boolean VALID_ACTIVE = true;

    public static final String VALID_SUB_NAME = "Sub Electronics";
    public static final String VALID_SUB_DESCRIPTION = "Subcategory for electronic products";

    private CategoryTestData() {
    }

    //Positive Test Data
    public static Category validCategory() {
        Category category = new Category();
        category.updateName(VALID_NAME);
        category.updateDescription(VALID_DESCRIPTION);
        category.updateIsActive(VALID_ACTIVE);
        return category;
    }

    public static Category validSubCategory() {
        Category category = new Category();
        category.updateName(VALID_SUB_NAME);
        category.updateDescription(VALID_SUB_DESCRIPTION);
        category.updateIsActive(VALID_ACTIVE);
        return category;
    }

    public static Category validCategory_WithParent() {
        Category child = validSubCategory();
        child.updateParent(validCategory());
        return child;
    }

    //Negative Test Data
    public static Category invalidCategory_NullName() {
        return Category.builder()
                .name(null)
                .description(VALID_DESCRIPTION)
                .isActive(VALID_ACTIVE)
                .build();
    }

    public static Category invalidCategory_BlankName() {
        return Category.builder()
                .name("   ")
                .description(VALID_DESCRIPTION)
                .isActive(VALID_ACTIVE)
                .build();
    }

    public static Category invalidCategory_ShortName() {
        return Category.builder()
                .name("AB")
                .description(VALID_DESCRIPTION)
                .isActive(VALID_ACTIVE)
                .build();
    }

    public static Category invalidCategory_LongName() {
        String longName = "E".repeat(256);
        return Category.builder()
                .name(longName)
                .description(VALID_DESCRIPTION)
                .isActive(VALID_ACTIVE)
                .build();
    }

    public static Category invalidCategory_NullDescription() {
        return Category.builder()
                .name(VALID_NAME)
                .description(null)
                .isActive(VALID_ACTIVE)
                .build();
    }

    public static Category invalidCategory_BlankDescription() {
        return Category.builder()
                .name(VALID_NAME)
                .description("    ")
                .isActive(VALID_ACTIVE)
                .build();
    }

    public static Category invalidCategory_ShortDescription() {
        return Category.builder()
                .name(VALID_NAME)
                .description("Desc")
                .isActive(VALID_ACTIVE)
                .build();
    }

    public static Category invalidCategory_LongDescription() {
        String longDescription = "D".repeat(2001);
        return Category.builder()
                .name(VALID_NAME)
                .description(longDescription)
                .isActive(VALID_ACTIVE)
                .build();
    }

    public static Category invalidCategory_NullActive() {
        return Category.builder()
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION)
                .isActive(null)
                .build();
    }

}


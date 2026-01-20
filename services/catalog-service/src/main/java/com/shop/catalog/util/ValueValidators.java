package com.shop.catalog.util;

import java.math.BigDecimal;
import java.util.Objects;

public final class ValueValidators {

    private ValueValidators() {
    }

    public static boolean isNullOrBlank(String str) {
        return Objects.isNull(str) || str.isBlank();
    }

    public static boolean isNotValidLength(String str, int minLength, int maxLength) {
        if (Objects.isNull(str)) return Boolean.TRUE;
        int length = str.trim().length();
        return length < minLength || length > maxLength;
    }

    public static boolean isEqualOrLessThanZero(BigDecimal number) {
        return Objects.nonNull(number) && BigDecimal.ZERO.compareTo(number) >= 0;
    }

}

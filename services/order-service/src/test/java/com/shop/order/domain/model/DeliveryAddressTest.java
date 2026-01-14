package com.shop.order.domain.model;

import com.shop.order.domain.exception.InvalidOrderStateException;
import com.shop.order.testutil.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryAddressTest {

    @Test
    void shouldCreateDeliveryAddress_WithAllFields() {
        DeliveryAddress deliveryAddress = TestData.createValidDeliveryAddress();

        assertEquals("Ashish Sharma", deliveryAddress.getFullName());
        assertEquals("+91-9876543210", deliveryAddress.getPhone());
        assertEquals("123 Main Street", deliveryAddress.getLine1());
        assertEquals("Apt 4B", deliveryAddress.getLine2());
        assertEquals("Mumbai", deliveryAddress.getCity());
        assertEquals("Maharashtra", deliveryAddress.getState());
        assertEquals("400001", deliveryAddress.getPinCode());
        assertEquals("India", deliveryAddress.getCountry());
    }

    @Test
    void shouldCreateDeliveryAddress_WithoutLine2() {
        DeliveryAddress deliveryAddress = TestData.createValidDeliveryAddressWithoutLine2();

        assertEquals("Ashish Sharma", deliveryAddress.getFullName());
        assertEquals("+91-9876543210", deliveryAddress.getPhone());
        assertEquals("123 Main Street", deliveryAddress.getLine1());
        assertNull(deliveryAddress.getLine2());
        assertEquals("Mumbai", deliveryAddress.getCity());
        assertEquals("Maharashtra", deliveryAddress.getState());
        assertEquals("400001", deliveryAddress.getPinCode());
        assertEquals("India", deliveryAddress.getCountry());
    }

    @Test
    void shouldValidateAddress_WithValidData() {
        DeliveryAddress deliveryAddress = TestData.createValidDeliveryAddress();

        assertEquals("Ashish Sharma", deliveryAddress.getFullName());
        assertEquals("+91-9876543210", deliveryAddress.getPhone());
        assertEquals("123 Main Street", deliveryAddress.getLine1());
        assertEquals("Apt 4B", deliveryAddress.getLine2());
        assertEquals("Mumbai", deliveryAddress.getCity());
        assertEquals("Maharashtra", deliveryAddress.getState());
        assertEquals("400001", deliveryAddress.getPinCode());
        assertEquals("India", deliveryAddress.getCountry());

        assertDoesNotThrow(deliveryAddress::validateAddress);
    }

    @Test
    void shouldThrowException_WhenValidatingAddress_WithBlankFullName() {
        DeliveryAddress deliveryAddress = TestData.createInvalidDeliveryAddress_BlankFullName();

        assertEquals(" ", deliveryAddress.getFullName());
        assertEquals("+91-9876543210", deliveryAddress.getPhone());
        assertEquals("123 Main Street", deliveryAddress.getLine1());
        assertEquals("Mumbai", deliveryAddress.getCity());
        assertEquals("Maharashtra", deliveryAddress.getState());
        assertEquals("400001", deliveryAddress.getPinCode());
        assertEquals("India", deliveryAddress.getCountry());

        assertThrows(InvalidOrderStateException.class, deliveryAddress::validateAddress);
    }

    @Test
    void shouldThrowException_WhenValidatingAddress_WithBlankPhone() {
        DeliveryAddress deliveryAddress = TestData.createInvalidDeliveryAddress_BlankPhone();

        assertEquals("Ashish Sharma", deliveryAddress.getFullName());
        assertEquals(" ", deliveryAddress.getPhone());
        assertEquals("123 Main Street", deliveryAddress.getLine1());
        assertEquals("Mumbai", deliveryAddress.getCity());
        assertEquals("Maharashtra", deliveryAddress.getState());
        assertEquals("400001", deliveryAddress.getPinCode());
        assertEquals("India", deliveryAddress.getCountry());

        assertThrows(InvalidOrderStateException.class, deliveryAddress::validateAddress);
    }

    @Test
    void shouldThrowException_WhenValidatingAddress_WithBlankLine1() {
        DeliveryAddress deliveryAddress = TestData.createInvalidDeliveryAddress_BlankLine1();

        assertEquals("Ashish Sharma", deliveryAddress.getFullName());
        assertEquals("+91-9876543210", deliveryAddress.getPhone());
        assertEquals(" ", deliveryAddress.getLine1());
        assertEquals("Mumbai", deliveryAddress.getCity());
        assertEquals("Maharashtra", deliveryAddress.getState());
        assertEquals("400001", deliveryAddress.getPinCode());
        assertEquals("India", deliveryAddress.getCountry());

        assertThrows(InvalidOrderStateException.class, deliveryAddress::validateAddress);
    }

    @Test
    void shouldThrowException_WhenValidatingAddress_WithBlankCity() {
        DeliveryAddress deliveryAddress = TestData.createInvalidDeliveryAddress_BlankCity();

        assertEquals("Ashish Sharma", deliveryAddress.getFullName());
        assertEquals("+91-9876543210", deliveryAddress.getPhone());
        assertEquals("123 Main Street", deliveryAddress.getLine1());
        assertEquals(" ", deliveryAddress.getCity());
        assertEquals("Maharashtra", deliveryAddress.getState());
        assertEquals("400001", deliveryAddress.getPinCode());
        assertEquals("India", deliveryAddress.getCountry());

        assertThrows(InvalidOrderStateException.class, deliveryAddress::validateAddress);
    }

    @Test
    void shouldThrowException_WhenValidatingAddress_WithBlankState() {
        DeliveryAddress deliveryAddress = TestData.createInvalidDeliveryAddress_BlankState();

        assertEquals("Ashish Sharma", deliveryAddress.getFullName());
        assertEquals("+91-9876543210", deliveryAddress.getPhone());
        assertEquals("123 Main Street", deliveryAddress.getLine1());
        assertEquals("Mumbai", deliveryAddress.getCity());
        assertEquals(" ", deliveryAddress.getState());
        assertEquals("400001", deliveryAddress.getPinCode());
        assertEquals("India", deliveryAddress.getCountry());

        assertThrows(InvalidOrderStateException.class, deliveryAddress::validateAddress);
    }

    @Test
    void shouldThrowException_WhenValidatingAddress_WithBlankPinCode() {
        DeliveryAddress deliveryAddress = TestData.createInvalidDeliveryAddress_BlankPinCode();

        assertEquals("Ashish Sharma", deliveryAddress.getFullName());
        assertEquals("+91-9876543210", deliveryAddress.getPhone());
        assertEquals("123 Main Street", deliveryAddress.getLine1());
        assertEquals("Mumbai", deliveryAddress.getCity());
        assertEquals("Maharashtra", deliveryAddress.getState());
        assertEquals(" ", deliveryAddress.getPinCode());
        assertEquals("India", deliveryAddress.getCountry());

        assertThrows(InvalidOrderStateException.class, deliveryAddress::validateAddress);
    }

    @Test
    void shouldThrowException_WhenValidatingAddress_WithBlankCountry() {
        DeliveryAddress deliveryAddress = TestData.createInvalidDeliveryAddress_BlankCountry();

        assertEquals("Ashish Sharma", deliveryAddress.getFullName());
        assertEquals("+91-9876543210", deliveryAddress.getPhone());
        assertEquals("123 Main Street", deliveryAddress.getLine1());
        assertEquals("Mumbai", deliveryAddress.getCity());
        assertEquals("Maharashtra", deliveryAddress.getState());
        assertEquals("400001", deliveryAddress.getPinCode());
        assertEquals(" ", deliveryAddress.getCountry());

        assertThrows(InvalidOrderStateException.class, deliveryAddress::validateAddress);
    }

    @Test
    void shouldReturnFormattedAddress() {
        DeliveryAddress deliveryAddress = TestData.createValidDeliveryAddress();
        String expectedAddress = """
                Ashish Sharma
                123 Main Street, Apt 4B, Mumbai, Maharashtra - 400001, India
                +91-9876543210""";
        assertEquals(expectedAddress, deliveryAddress.getFullAddress());
    }

    @Test
    void shouldReturnFormattedAddress_WithoutLine2() {
        DeliveryAddress deliveryAddress = TestData.createValidDeliveryAddressWithoutLine2();
        String expectedAddress = """
                Ashish Sharma
                123 Main Street, Mumbai, Maharashtra - 400001, India
                +91-9876543210""";
        assertEquals(expectedAddress, deliveryAddress.getFullAddress());
    }

}

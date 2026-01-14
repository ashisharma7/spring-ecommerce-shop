package com.shop.order.domain.model;

import com.shop.order.domain.exception.InvalidOrderStateException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryAddress {

    @Column(name = "delivery_full_name", nullable = false)
    private String fullName;

    @Column(name = "delivery_phone", nullable = false)
    private String phone;

    @Column(name = "delivery_line1", nullable = false)
    private String line1;

    @Column(name = "delivery_line2")
    private String line2;

    @Column(name = "delivery_city", nullable = false)
    private String city;

    @Column(name = "delivery_state", nullable = false)
    private String state;

    @Column(name = "delivery_pin_code", nullable = false)
    private String pinCode;

    @Column(name = "delivery_country", nullable = false)
    private String country;

    public static DeliveryAddress create(String fullName, String phone, String line1,
                                         String line2, String city, String state,
                                         String pinCode, String country) {
        return DeliveryAddress.builder()
                .fullName(fullName)
                .phone(phone)
                .line1(line1)
                .line2(line2)
                .city(city)
                .state(state)
                .pinCode(pinCode)
                .country(country)
                .build();
    }

    protected void validateAddress() {
        if (null == fullName || fullName.isBlank()) {
            throw new InvalidOrderStateException("Cannot save Order: Full name is required in delivery address");
        }
        if (null == phone || phone.isBlank()) {
            throw new InvalidOrderStateException("Cannot save Order: Phone number is required in delivery address");
        }
        if (null == line1 || line1.isBlank()) {
            throw new InvalidOrderStateException("Cannot save Order: Line 1 is required in delivery address");
        }
        if (null == city || city.isBlank()) {
            throw new InvalidOrderStateException("Cannot save Order: City is required in delivery address");
        }
        if (null == state || state.isBlank()) {
            throw new InvalidOrderStateException("Cannot save Order: State is required in delivery address");
        }
        if (null == pinCode || pinCode.isBlank()) {
            throw new InvalidOrderStateException("Cannot save Order: PinCode is required in delivery address");
        }
        if (null == country || country.isBlank()) {
            throw new InvalidOrderStateException("Cannot save Order: Country is required in delivery address");
        }
    }

    public String getFullAddress() {
        return this.fullName +
                "\n" + this.line1 +
                ((null == this.line2 || this.line2.isBlank()) ? "" : ", " + this.line2) +
                ", " + this.city +
                ", " + this.state +
                " - " + this.pinCode +
                ", " + this.country +
                "\n" + this.phone;
    }

}

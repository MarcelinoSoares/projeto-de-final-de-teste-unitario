package school.cesar.eta.unit;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a physical address with validation. Demonstrates complex object testing with builder pattern.
 */
public class Address {
    // Brazilian ZIP code pattern: XXXXX-XXX or US pattern: XXXXX or XXXXX-XXXX
    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^(\\d{5}(-\\d{3})?|\\d{5}(-\\d{4})?)$");

    private final String street;
    private final String number;
    private final String complement;
    private final String neighborhood;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String country;

    private Address(Builder builder) {
        this.street = builder.street;
        this.number = builder.number;
        this.complement = builder.complement;
        this.neighborhood = builder.neighborhood;
        this.city = builder.city;
        this.state = builder.state;
        this.zipCode = builder.zipCode;
        this.country = builder.country;
    }

    /**
     * Builder pattern for Address creation.
     */
    public static class Builder {
        private String street;
        private String number;
        private String complement;
        private String neighborhood;
        private String city;
        private String state;
        private String zipCode;
        private String country = "Brazil";

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder complement(String complement) {
            this.complement = complement;
            return this;
        }

        public Builder neighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * Builds the Address with validation.
         *
         * @return validated Address instance
         * @throws IllegalArgumentException
         *             if required fields are missing or invalid
         */
        public Address build() {
            validateRequiredFields();
            validateZipCode();
            return new Address(this);
        }

        private void validateRequiredFields() {
            if (isBlank(street)) {
                throw new IllegalArgumentException("Street is required");
            }
            if (isBlank(number)) {
                throw new IllegalArgumentException("Number is required");
            }
            if (isBlank(city)) {
                throw new IllegalArgumentException("City is required");
            }
            if (isBlank(state)) {
                throw new IllegalArgumentException("State is required");
            }
            if (isBlank(zipCode)) {
                throw new IllegalArgumentException("Zip code is required");
            }
        }

        private void validateZipCode() {
            if (!ZIP_CODE_PATTERN.matcher(zipCode).matches()) {
                throw new IllegalArgumentException("Invalid zip code format");
            }
        }

        private boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }
    }

    /**
     * Returns formatted address for display.
     *
     * @return formatted address string
     */
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street).append(", ").append(number);

        if (complement != null && !complement.trim().isEmpty()) {
            sb.append(" - ").append(complement);
        }

        if (neighborhood != null && !neighborhood.trim().isEmpty()) {
            sb.append(", ").append(neighborhood);
        }

        sb.append("\n");
        sb.append(city).append(" - ").append(state);
        sb.append(", ").append(zipCode);
        sb.append(", ").append(country);

        return sb.toString();
    }

    /**
     * Checks if this address is in the same city as another.
     *
     * @param other
     *            address to compare
     * @return true if same city and state
     */
    public boolean isSameCity(Address other) {
        if (other == null) {
            return false;
        }
        return Objects.equals(this.city, other.city) && Objects.equals(this.state, other.state);
    }

    // Getters
    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getComplement() {
        return complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(number, address.number)
                && Objects.equals(complement, address.complement) && Objects.equals(neighborhood, address.neighborhood)
                && Objects.equals(city, address.city) && Objects.equals(state, address.state)
                && Objects.equals(zipCode, address.zipCode) && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, complement, neighborhood, city, state, zipCode, country);
    }

    @Override
    public String toString() {
        return "Address{" + "street='" + street + '\'' + ", number='" + number + '\'' + ", city='" + city + '\''
                + ", state='" + state + '\'' + ", zipCode='" + zipCode + '\'' + '}';
    }
}
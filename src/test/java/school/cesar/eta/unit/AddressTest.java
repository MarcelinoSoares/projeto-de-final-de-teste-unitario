package school.cesar.eta.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Address Test Suite")
public class AddressTest {

    private Address.Builder builder;

    @BeforeEach
    void setUp() {
        builder = new Address.Builder();
    }

    @Nested
    @DisplayName("Builder Validation Tests")
    class BuilderValidationTests {

        @Test
        @DisplayName("Should build valid address with all fields")
        void build_allFieldsProvided_success() {
            // Arrange
            builder.street("Rua das Flores").number("123").complement("Apt 456").neighborhood("Centro").city("Recife")
                    .state("PE").zipCode("50000-000").country("Brazil");

            // Act
            Address address = builder.build();

            // Assert
            assertNotNull(address);
            assertEquals("Rua das Flores", address.getStreet());
            assertEquals("123", address.getNumber());
            assertEquals("Apt 456", address.getComplement());
            assertEquals("Centro", address.getNeighborhood());
            assertEquals("Recife", address.getCity());
            assertEquals("PE", address.getState());
            assertEquals("50000-000", address.getZipCode());
            assertEquals("Brazil", address.getCountry());
        }

        @Test
        @DisplayName("Should build address without optional fields")
        void build_onlyRequiredFields_success() {
            // Arrange
            builder.street("Av. Boa Viagem").number("1000").city("Recife").state("PE").zipCode("51011-000");

            // Act
            Address address = builder.build();

            // Assert
            assertNotNull(address);
            assertNull(address.getComplement());
            assertNull(address.getNeighborhood());
            assertEquals("Brazil", address.getCountry()); // Default value
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { "   ", "\t", "\n" })
        @DisplayName("Should throw exception when street is invalid")
        void build_invalidStreet_throwsException(String street) {
            // Arrange
            builder.street(street).number("123").city("Recife").state("PE").zipCode("50000-000");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.build());
            assertEquals("Street is required", exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when number is invalid")
        void build_invalidNumber_throwsException(String number) {
            // Arrange
            builder.street("Rua Test").number(number).city("Recife").state("PE").zipCode("50000-000");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.build());
            assertEquals("Number is required", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when city is null")
        void build_nullCity_throwsException() {
            // Arrange
            builder.street("Rua Test").number("123").city(null).state("PE").zipCode("50000-000");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.build());
            assertEquals("City is required", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when state is null")
        void build_nullState_throwsException() {
            // Arrange
            builder.street("Rua Test").number("123").city("Recife").state(null).zipCode("50000-000");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.build());
            assertEquals("State is required", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = { "12345", "12345-6789", "00000-000" })
        @DisplayName("Should accept valid zip code formats")
        void build_validZipCodeFormats_success(String zipCode) {
            // Arrange
            builder.street("Rua Test").number("123").city("Recife").state("PE").zipCode(zipCode);

            // Act
            Address address = builder.build();

            // Assert
            assertEquals(zipCode, address.getZipCode());
        }

        @ParameterizedTest
        @ValueSource(strings = { "1234", "123456", "12345-", "12345-67", "ABCDE-FGHI", "12.345-678" })
        @DisplayName("Should throw exception for invalid zip code formats")
        void build_invalidZipCodeFormat_throwsException(String zipCode) {
            // Arrange
            builder.street("Rua Test").number("123").city("Recife").state("PE").zipCode(zipCode);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> builder.build());
            assertEquals("Invalid zip code format", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Formatted Address Tests")
    class FormattedAddressTests {

        @Test
        @DisplayName("Should format complete address correctly")
        void getFormattedAddress_completeAddress_correctFormat() {
            // Arrange
            Address address = new Address.Builder().street("Rua das Flores").number("123").complement("Apt 456")
                    .neighborhood("Centro").city("Recife").state("PE").zipCode("50000-000").country("Brazil").build();

            // Act
            String formatted = address.getFormattedAddress();

            // Assert
            String expected = "Rua das Flores, 123 - Apt 456, Centro\n" + "Recife - PE, 50000-000, Brazil";
            assertEquals(expected, formatted);
        }

        @Test
        @DisplayName("Should format address without optional fields")
        void getFormattedAddress_withoutOptionalFields_correctFormat() {
            // Arrange
            Address address = new Address.Builder().street("Av. Boa Viagem").number("1000").city("Recife").state("PE")
                    .zipCode("51011-000").build();

            // Act
            String formatted = address.getFormattedAddress();

            // Assert
            String expected = "Av. Boa Viagem, 1000\n" + "Recife - PE, 51011-000, Brazil";
            assertEquals(expected, formatted);
        }

        @Test
        @DisplayName("Should handle empty complement correctly")
        void getFormattedAddress_emptyComplement_ignored() {
            // Arrange
            Address address = new Address.Builder().street("Rua Test").number("100").complement("   ").city("Recife")
                    .state("PE").zipCode("50000-000").build();

            // Act
            String formatted = address.getFormattedAddress();

            // Assert
            // O complemento vazio não deve aparecer na formatação
            assertFalse(formatted.contains(" -    ")); // Empty complement should NOT be included
            assertTrue(formatted.startsWith("Rua Test, 100\n")); // Should go directly to next line
            assertTrue(formatted.contains("Recife - PE, 50000-000, Brazil"));
        }
    }

    @Nested
    @DisplayName("Same City Tests")
    class SameCityTests {

        private Address address1;
        private Address address2;

        @BeforeEach
        void setUp() {
            address1 = new Address.Builder().street("Rua A").number("100").city("Recife").state("PE")
                    .zipCode("50000-000").build();
        }

        @Test
        @DisplayName("Should return true for same city and state")
        void isSameCity_sameCityAndState_true() {
            // Arrange
            address2 = new Address.Builder().street("Rua B").number("200").city("Recife").state("PE")
                    .zipCode("51000-000").build();

            // Act & Assert
            assertTrue(address1.isSameCity(address2));
            assertTrue(address2.isSameCity(address1));
        }

        @Test
        @DisplayName("Should return false for different city")
        void isSameCity_differentCity_false() {
            // Arrange
            address2 = new Address.Builder().street("Rua B").number("200").city("Olinda").state("PE")
                    .zipCode("53000-000").build();

            // Act & Assert
            assertFalse(address1.isSameCity(address2));
        }

        @Test
        @DisplayName("Should return false for different state")
        void isSameCity_differentState_false() {
            // Arrange
            address2 = new Address.Builder().street("Rua B").number("200").city("Recife").state("RJ")
                    .zipCode("20000-000").build();

            // Act & Assert
            assertFalse(address1.isSameCity(address2));
        }

        @Test
        @DisplayName("Should return false when comparing with null")
        void isSameCity_nullAddress_false() {
            // Act & Assert
            assertFalse(address1.isSameCity(null));
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal for identical addresses")
        void equals_identicalAddresses_true() {
            // Arrange
            Address address1 = createCompleteAddress();
            Address address2 = createCompleteAddress();

            // Act & Assert
            assertEquals(address1, address2);
            assertEquals(address1.hashCode(), address2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different addresses")
        void equals_differentAddresses_false() {
            // Arrange
            Address address1 = new Address.Builder().street("Rua A").number("100").city("Recife").state("PE")
                    .zipCode("50000-000").build();

            Address address2 = new Address.Builder().street("Rua B").number("100").city("Recife").state("PE")
                    .zipCode("50000-000").build();

            // Act & Assert
            assertNotEquals(address1, address2);
        }

        @Test
        @DisplayName("Should handle equals edge cases correctly")
        void equals_edgeCases_correctBehavior() {
            // Arrange
            Address address = createCompleteAddress();

            // Act & Assert
            assertEquals(address, address); // Same instance
            assertNotEquals(address, null); // Null comparison
            assertNotEquals(address, "Not an address"); // Different type
        }

        private Address createCompleteAddress() {
            return new Address.Builder().street("Rua das Flores").number("123").complement("Apt 456")
                    .neighborhood("Centro").city("Recife").state("PE").zipCode("50000-000").country("Brazil").build();
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate meaningful toString")
        void toString_validAddress_containsKeyInfo() {
            // Arrange
            Address address = new Address.Builder().street("Rua Test").number("123").city("Recife").state("PE")
                    .zipCode("50000-000").build();

            // Act
            String result = address.toString();

            // Assert
            assertTrue(result.contains("Rua Test"));
            assertTrue(result.contains("123"));
            assertTrue(result.contains("Recife"));
            assertTrue(result.contains("PE"));
            assertTrue(result.contains("50000-000"));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle special characters in address fields")
        void build_specialCharacters_success() {
            // Arrange
            builder.street("Rua José & Maria's").number("123-A").complement("Apt. #456").neighborhood("São João")
                    .city("São Paulo").state("SP").zipCode("01000-000");

            // Act
            Address address = builder.build();

            // Assert
            assertEquals("Rua José & Maria's", address.getStreet());
            assertEquals("123-A", address.getNumber());
            assertEquals("Apt. #456", address.getComplement());
            assertEquals("São João", address.getNeighborhood());
        }

        @Test
        @DisplayName("Should handle very long street names")
        void build_veryLongStreetName_success() {
            // Arrange
            String longStreet = "Rua " + "A".repeat(200);
            builder.street(longStreet).number("1").city("City").state("ST").zipCode("12345-678");

            // Act
            Address address = builder.build();

            // Assert
            assertEquals(longStreet, address.getStreet());
            assertTrue(address.getFormattedAddress().contains(longStreet));
        }

        @Test
        @DisplayName("Should handle unicode characters")
        void build_unicodeCharacters_success() {
            // Arrange
            builder.street("Rua 中文路").number("123").city("東京").state("日本").zipCode("12345-678");

            // Act
            Address address = builder.build();

            // Assert
            assertEquals("Rua 中文路", address.getStreet());
            assertEquals("東京", address.getCity());
            assertEquals("日本", address.getState());
        }

        @Test
        @DisplayName("Should handle addresses with only spaces in optional fields")
        void getFormattedAddress_spacesInOptionalFields_ignored() {
            // Arrange
            Address address = new Address.Builder().street("Main St").number("100").complement("    ")
                    .neighborhood("   ").city("City").state("ST").zipCode("12345").build();

            // Act
            String formatted = address.getFormattedAddress();

            // Assert
            assertEquals("Main St, 100\nCity - ST, 12345, Brazil", formatted);
        }

        @Test
        @DisplayName("Should handle mixed valid and invalid zip codes")
        void build_edgeCaseZipCodes_correctValidation() {
            // Valid edge cases
            assertDoesNotThrow(
                    () -> new Address.Builder().street("St").number("1").city("C").state("S").zipCode("00000").build());
            assertDoesNotThrow(() -> new Address.Builder().street("St").number("1").city("C").state("S")
                    .zipCode("99999-999").build());
            assertDoesNotThrow(() -> new Address.Builder().street("St").number("1").city("C").state("S")
                    .zipCode("00000-0000").build());

            // Invalid edge cases
            assertThrows(IllegalArgumentException.class, () -> new Address.Builder().street("St").number("1").city("C")
                    .state("S").zipCode("12345-").build());
            assertThrows(IllegalArgumentException.class, () -> new Address.Builder().street("St").number("1").city("C")
                    .state("S").zipCode("-12345").build());
        }

        @Test
        @DisplayName("Should maintain immutability after creation")
        void immutability_tryToModifyBuilder_doesNotAffectCreatedAddress() {
            // Arrange
            builder.street("Original Street").number("100").city("Original City").state("OS").zipCode("12345-678");
            Address address = builder.build();

            // Act - Try to modify builder after creating address
            builder.street("Modified Street").city("Modified City");
            Address address2 = builder.build();

            // Assert - First address should remain unchanged
            assertEquals("Original Street", address.getStreet());
            assertEquals("Original City", address.getCity());
            assertEquals("Modified Street", address2.getStreet());
            assertEquals("Modified City", address2.getCity());
        }

        @Test
        @DisplayName("Should handle null country gracefully")
        void build_nullCountry_usesDefault() {
            // Arrange
            builder.street("Street").number("1").city("City").state("ST").zipCode("12345").country(null);

            // Act
            Address address = builder.build();

            // Assert
            assertNull(address.getCountry());
            assertTrue(address.getFormattedAddress().contains("null"));
        }

        @Test
        @DisplayName("Should correctly compare addresses with null fields")
        void equals_withNullOptionalFields_correctComparison() {
            // Arrange
            Address addr1 = new Address.Builder().street("St").number("1").city("C").state("S").zipCode("12345")
                    .build();
            Address addr2 = new Address.Builder().street("St").number("1").city("C").state("S").zipCode("12345")
                    .build();
            Address addr3 = new Address.Builder().street("St").number("1").complement("Apt 1").city("C").state("S")
                    .zipCode("12345").build();

            // Act & Assert
            assertEquals(addr1, addr2);
            assertNotEquals(addr1, addr3);
            assertEquals(addr1.hashCode(), addr2.hashCode());
        }
    }
}
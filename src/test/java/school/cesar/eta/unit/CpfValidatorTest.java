package school.cesar.eta.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for CpfValidator class.
 */
@DisplayName("CpfValidator Test Suite")
public class CpfValidatorTest {

    @Test
    @DisplayName("Should validate correct CPF with formatting")
    void isValid_correctFormattedCpf_true() {
        // These are valid test CPFs
        assertTrue(CpfValidator.isValid("123.456.789-09"));
        assertTrue(CpfValidator.isValid("111.444.777-35"));
    }

    @Test
    @DisplayName("Should validate correct CPF without formatting")
    void isValid_correctUnformattedCpf_true() {
        assertTrue(CpfValidator.isValid("12345678909"));
        assertTrue(CpfValidator.isValid("11144477735"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "111.111.111-11", "222.222.222-22", "333.333.333-33", "444.444.444-44", "555.555.555-55",
            "666.666.666-66", "777.777.777-77", "888.888.888-88", "999.999.999-99", "000.000.000-00" })
    @DisplayName("Should reject CPF with all same digits")
    void isValid_allSameDigits_false(String cpf) {
        assertFalse(CpfValidator.isValid(cpf));
    }

    @Test
    @DisplayName("Should reject CPF with wrong check digits")
    void isValid_wrongCheckDigits_false() {
        assertFalse(CpfValidator.isValid("123.456.789-00")); // Wrong check digits
        assertFalse(CpfValidator.isValid("123.456.789-10")); // Wrong check digits
    }

    @ParameterizedTest
    @ValueSource(strings = { "123.456.789", // Missing check digits
            "123.456.789-0", // Only one check digit
            "123.456.789-012", // Too many digits
            "12.345.678-90", // Missing digit
            "1234.567.890-12", // Extra digit
            "abc.def.ghi-jk", // Letters
            "123.456.78a-90" // Letter in the middle
    })
    @DisplayName("Should reject invalid CPF formats")
    void isValid_invalidFormats_false(String cpf) {
        assertFalse(CpfValidator.isValid(cpf));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Should reject null CPF")
    void isValid_null_false(String cpf) {
        assertFalse(CpfValidator.isValid(cpf));
    }

    @Test
    @DisplayName("Should format unformatted CPF correctly")
    void format_unformattedCpf_correctFormat() {
        assertEquals("123.456.789-09", CpfValidator.format("12345678909"));
        assertEquals("111.444.777-35", CpfValidator.format("11144477735"));
    }

    @Test
    @DisplayName("Should format already formatted CPF correctly")
    void format_formattedCpf_sameFormat() {
        assertEquals("123.456.789-09", CpfValidator.format("123.456.789-09"));
    }

    @Test
    @DisplayName("Should return null when formatting invalid length CPF")
    void format_invalidLength_null() {
        assertNull(CpfValidator.format("123"));
        assertNull(CpfValidator.format("123456789012")); // Too long
        assertNull(CpfValidator.format(null));
    }

    @Test
    @DisplayName("Should unformat CPF correctly")
    void unformat_formattedCpf_onlyDigits() {
        assertEquals("12345678909", CpfValidator.unformat("123.456.789-09"));
        assertEquals("11144477735", CpfValidator.unformat("111.444.777-35"));
    }

    @Test
    @DisplayName("Should handle unformat with various inputs")
    void unformat_variousInputs_correctResult() {
        assertEquals("12345678909", CpfValidator.unformat("123-456-789-09"));
        assertEquals("12345678909", CpfValidator.unformat("123 456 789 09"));
        assertEquals("12345678909", CpfValidator.unformat("123/456/789/09"));
        assertEquals("", CpfValidator.unformat(null));
        assertEquals("", CpfValidator.unformat(""));
    }

    @Test
    @DisplayName("Should generate valid random CPF")
    void generateRandom_called_validCpf() {
        // Generate multiple CPFs and validate all
        for (int i = 0; i < 100; i++) {
            String cpf = CpfValidator.generateRandom();
            assertNotNull(cpf);
            assertTrue(CpfValidator.isValid(cpf), "Generated CPF should be valid: " + cpf);
            assertEquals(14, cpf.length(), "Generated CPF should have correct format length");
            assertTrue(cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"), "Generated CPF should match format pattern");
        }
    }

    @Test
    @DisplayName("Should not allow instantiation of utility class")
    void constructor_private_cannotInstantiate() {
        // Use reflection to verify constructor is private
        try {
            var constructor = CpfValidator.class.getDeclaredConstructor();
            assertFalse(constructor.canAccess(null));
        } catch (NoSuchMethodException e) {
            fail("Constructor should exist");
        }
    }
}
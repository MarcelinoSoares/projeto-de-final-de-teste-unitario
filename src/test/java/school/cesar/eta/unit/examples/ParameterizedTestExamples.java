package school.cesar.eta.unit.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Examples of parameterized tests using JUnit 5. This class demonstrates various ways to provide test data.
 */
@DisplayName("Parameterized Test Examples")
public class ParameterizedTestExamples {

    @ParameterizedTest
    @ValueSource(strings = { "racecar", "radar", "level", "noon" })
    @DisplayName("Should identify palindromes")
    void isPalindrome_validPalindromes_true(String candidate) {
        assertTrue(isPalindrome(candidate));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 3, 5, 7, 9, 11, 13 })
    @DisplayName("Should identify odd numbers")
    void isOdd_oddNumbers_true(int number) {
        assertTrue(number % 2 != 0);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "  ", "\t", "\n" })
    @DisplayName("Should identify blank strings")
    void isBlank_blankStrings_true(String input) {
        assertTrue(isBlank(input));
    }

    @ParameterizedTest
    @EnumSource(Month.class)
    @DisplayName("Should validate all months have between 1 and 12")
    void monthValue_allMonths_between1And12(Month month) {
        int monthValue = month.getValue();
        assertTrue(monthValue >= 1 && monthValue <= 12);
    }

    @ParameterizedTest
    @EnumSource(value = Month.class, names = { "APRIL", "JUNE", "SEPTEMBER", "NOVEMBER" })
    @DisplayName("Should identify months with 30 days")
    void monthsWith30Days(Month month) {
        assertEquals(30, month.length(false));
    }

    @ParameterizedTest
    @EnumSource(value = Month.class, mode = EnumSource.Mode.EXCLUDE, names = { "FEBRUARY" })
    @DisplayName("Should verify non-February months have fixed days")
    void nonFebruaryMonths_fixedDays(Month month) {
        assertEquals(month.length(false), month.length(true));
    }

    @ParameterizedTest
    @CsvSource({ "1, 1, 2", "2, 3, 5", "10, 90, 100", "-5, 5, 0", "0, 0, 0" })
    @DisplayName("Should add two numbers correctly")
    void add_twoNumbers_correctSum(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }

    @ParameterizedTest
    @CsvSource({ "John, Doe, 'John Doe'", "Jane, Smith, 'Jane Smith'", "'Mary Ann', Jones, 'Mary Ann Jones'",
            "José, Silva, 'José Silva'" })
    @DisplayName("Should concatenate names correctly")
    void fullName_firstAndLast_concatenated(String first, String last, String expected) {
        assertEquals(expected, first + " " + last);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    @DisplayName("Should process CSV file data")
    void processCSVData(String input, String expected) {
        // This would read from src/test/resources/test-data.csv
        assertNotNull(input);
        assertNotNull(expected);
    }

    @ParameterizedTest
    @MethodSource("stringProvider")
    @DisplayName("Should process strings from method source")
    void testWithMethodSource(String argument) {
        assertNotNull(argument);
        assertFalse(argument.isEmpty());
    }

    static Stream<String> stringProvider() {
        return Stream.of("apple", "banana", "orange", "grape");
    }

    @ParameterizedTest
    @MethodSource("multipleArgumentsProvider")
    @DisplayName("Should process multiple arguments")
    void testWithMultipleArguments(String str, int num, LocalDate date) {
        assertNotNull(str);
        assertTrue(num > 0);
        assertNotNull(date);
        assertTrue(date.isBefore(LocalDate.now()));
    }

    static Stream<Arguments> multipleArgumentsProvider() {
        return Stream.of(Arguments.of("foo", 1, LocalDate.of(2020, 1, 1)),
                Arguments.of("bar", 2, LocalDate.of(2021, 2, 2)), Arguments.of("baz", 3, LocalDate.of(2022, 3, 3)));
    }

    @ParameterizedTest
    @ArgumentsSource(CustomArgumentsProvider.class)
    @DisplayName("Should use custom arguments provider")
    void testWithCustomArgumentsProvider(String argument) {
        assertTrue(argument.startsWith("custom-"));
    }

    static class CustomArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of("custom-1", "custom-2", "custom-3").map(Arguments::of);
        }
    }

    // Helper methods
    private boolean isPalindrome(String str) {
        if (str == null)
            return false;
        String clean = str.toLowerCase().replaceAll("[^a-z0-9]", "");
        return clean.equals(new StringBuilder(clean).reverse().toString());
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
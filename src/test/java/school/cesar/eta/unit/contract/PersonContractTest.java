package school.cesar.eta.unit.contract;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import school.cesar.eta.unit.Person;
import school.cesar.eta.unit.Address;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contract tests for Person class.
 *
 * These tests ensure that the public API of the Person class maintains its expected behavior across versions.
 */
@DisplayName("Person Contract Tests")
public class PersonContractTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
    }

    @Test
    @DisplayName("Contract: getName() should throw IllegalStateException when no names are set")
    void contract_getName_noNames_throwsIllegalStateException() {
        // This is a contract that must be maintained
        assertThrows(IllegalStateException.class, () -> person.getName());
    }

    @Test
    @DisplayName("Contract: addToFamily() should create bidirectional relationship")
    void contract_addToFamily_bidirectionalRelationship() {
        // Given
        Person relative = new Person();

        // When
        person.addToFamily(relative);

        // Then - This behavior is part of the contract
        assertTrue(person.isFamily(relative), "Person should have relative in family");
        assertTrue(relative.isFamily(person), "Relative should have person in family (bidirectional)");
    }

    @Test
    @DisplayName("Contract: isAdult() should return true for age >= 18")
    void contract_isAdult_ageThreshold() {
        // Given
        person.setBirthday(LocalDate.now().minusYears(18));

        // Then - The age threshold of 18 is part of the contract
        assertTrue(person.isAdult());

        // Given
        person.setBirthday(LocalDate.now().minusYears(18).plusDays(1));

        // Then
        assertFalse(person.isAdult());
    }

    @Test
    @DisplayName("Contract: getFamily() should return defensive copy")
    void contract_getFamily_defensiveCopy() {
        // Given
        Person relative = new Person();
        person.addToFamily(relative);

        // When
        var familyList = person.getFamily();
        familyList.clear();

        // Then - The original family should not be modified
        assertEquals(1, person.getFamily().size(), "Original family list should be unchanged");
    }

    @Test
    @DisplayName("Contract: setBirthday() should not accept future dates")
    void contract_setBirthday_noFutureDates() {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // Then - This validation is part of the contract
        assertThrows(IllegalArgumentException.class, () -> person.setBirthday(futureDate));
    }

    @Test
    @DisplayName("Contract: equals() should be based on id, firstName, lastName, and birthday")
    void contract_equals_fieldsUsed() {
        // Given
        Person p1 = new Person();
        Person p2 = new Person();

        // Same data
        p1.setId(1L);
        p1.setFirstName("John");
        p1.setLastName("Doe");
        p1.setBirthday(LocalDate.of(1990, 1, 1));

        p2.setId(1L);
        p2.setFirstName("John");
        p2.setLastName("Doe");
        p2.setBirthday(LocalDate.of(1990, 1, 1));

        // Then - These fields determine equality
        assertEquals(p1, p2);

        // Change address - should still be equal
        p1.setAddress(
                new Address.Builder().street("Street").number("1").city("City").state("ST").zipCode("12345").build());

        assertEquals(p1, p2, "Address should not affect equality");
    }
}
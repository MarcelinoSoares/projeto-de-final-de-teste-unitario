package school.cesar.eta.unit.properties;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import school.cesar.eta.unit.Person;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property-based tests for Person class.
 *
 * These tests verify invariants and properties that should hold for all valid inputs.
 */
public class PersonPropertyTest {

    @Property
    @Label("Age calculation should always be non-negative")
    void ageIsNeverNegative(@ForAll("validBirthdays") LocalDate birthday) {
        // Given
        Person person = new Person();
        person.setBirthday(birthday);

        // When
        Integer age = person.getAge();

        // Then
        assertNotNull(age);
        assertTrue(age >= 0, "Age should never be negative");
    }

    @Property
    @Label("Age in months should be 12 times age in years (approximately)")
    void ageInMonthsRelationship(@ForAll("validBirthdays") LocalDate birthday) {
        // Given
        Person person = new Person();
        person.setBirthday(birthday);

        // When
        Integer ageYears = person.getAge();
        Integer ageMonths = person.getAgeInMonths();

        // Then
        assertNotNull(ageYears);
        assertNotNull(ageMonths);
        // Allow for up to 11 months difference
        assertTrue(Math.abs(ageMonths - (ageYears * 12)) <= 11,
                "Age in months should be approximately 12 times age in years");
    }

    @Property
    @Label("Family relationships should be symmetric")
    void familyRelationshipSymmetry(@ForAll("persons") Person person1, @ForAll("persons") Person person2) {
        // When
        person1.addToFamily(person2);

        // Then
        assertTrue(person1.isFamily(person2));
        assertTrue(person2.isFamily(person1));
    }

    @Property
    @Label("Adding same person multiple times should not increase family size")
    void familyIdempotence(@ForAll("persons") Person person, @ForAll("persons") Person relative,
            @ForAll @IntRange(min = 1, max = 10) int times) {
        // When
        for (int i = 0; i < times; i++) {
            person.addToFamily(relative);
        }

        // Then
        assertEquals(1, person.getFamily().size());
    }

    @Property
    @Label("Name concatenation should preserve individual names")
    void namePreservation(@ForAll @AlphaChars @StringLength(min = 1, max = 50) String firstName,
            @ForAll @AlphaChars @StringLength(min = 1, max = 50) String lastName) {
        // Given
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);

        // When
        String fullName = person.getName();

        // Then
        assertTrue(fullName.contains(firstName));
        assertTrue(fullName.contains(lastName));
        assertEquals(firstName + " " + lastName, fullName);
    }

    @Property
    @Label("Equals should be reflexive, symmetric, and transitive")
    void equalsProperties(@ForAll("personsWithId") Person p1, @ForAll("personsWithId") Person p2,
            @ForAll("personsWithId") Person p3) {
        // Reflexive
        assertEquals(p1, p1);

        // Symmetric
        if (p1.equals(p2)) {
            assertEquals(p2, p1);
        }

        // Transitive
        if (p1.equals(p2) && p2.equals(p3)) {
            assertEquals(p1, p3);
        }

        // Consistent with hashCode
        if (p1.equals(p2)) {
            assertEquals(p1.hashCode(), p2.hashCode());
        }
    }

    @Provide
    Arbitrary<LocalDate> validBirthdays() {
        LocalDate minDate = LocalDate.of(1900, 1, 1);
        LocalDate maxDate = LocalDate.now();
        long daysBetween = maxDate.toEpochDay() - minDate.toEpochDay();

        return Arbitraries.longs().between(0, daysBetween).map(days -> minDate.plusDays(days));
    }

    @Provide
    Arbitrary<Person> persons() {
        return Combinators.combine(Arbitraries.longs().between(1L, 1000L),
                Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20),
                Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20)).as((id, firstName, lastName) -> {
                    Person p = new Person();
                    p.setId(id);
                    p.setFirstName(firstName);
                    p.setLastName(lastName);
                    return p;
                });
    }

    @Provide
    Arbitrary<Person> personsWithId() {
        return Arbitraries.longs().between(1L, 10L).map(id -> {
            Person p = new Person();
            p.setId(id);
            p.setFirstName("Name" + id);
            p.setLastName("Surname" + id);
            p.setBirthday(LocalDate.of(2000, 1, 1));
            return p;
        });
    }
}
package school.cesar.eta.unit.accessibility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import school.cesar.eta.unit.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * API Accessibility tests to ensure the public API is user-friendly and follows best practices.
 */
@DisplayName("API Accessibility Tests")
public class ApiAccessibilityTest {

    @Test
    @DisplayName("Person class should have reasonable number of public methods")
    void person_publicMethodCount_reasonable() {
        // Given
        Class<?> personClass = Person.class;

        // When
        long publicMethodCount = Arrays.stream(personClass.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers())).filter(m -> !m.getName().equals("toString"))
                .filter(m -> !m.getName().equals("equals")).filter(m -> !m.getName().equals("hashCode")).count();

        // Then
        assertTrue(publicMethodCount <= 25,
                "Person class has too many public methods (" + publicMethodCount + "). Consider refactoring.");
    }

    @Test
    @DisplayName("All public methods should have meaningful names")
    void publicMethods_naming_meaningful() {
        // Given
        List<Class<?>> classesToCheck = Arrays.asList(Person.class, Address.class, PersonService.class,
                CpfValidator.class);

        // When/Then
        for (Class<?> clazz : classesToCheck) {
            List<String> poorlyNamedMethods = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(m -> Modifier.isPublic(m.getModifiers())).map(Method::getName)
                    .filter(name -> name.length() < 3).filter(name -> !name.equals("of")) // Common factory method
                    .collect(Collectors.toList());

            assertTrue(poorlyNamedMethods.isEmpty(),
                    clazz.getSimpleName() + " has poorly named methods: " + poorlyNamedMethods);
        }
    }

    @Test
    @DisplayName("Builder pattern should be fluent")
    void addressBuilder_fluency_verified() {
        // Given/When
        Address address = new Address.Builder().street("Street").number("123").city("City").state("ST").zipCode("12345")
                .build();

        // Then
        assertNotNull(address);
        // All builder methods should return Builder for fluency
        Arrays.stream(Address.Builder.class.getDeclaredMethods()).filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> !m.getName().equals("build")).forEach(m -> assertEquals(Address.Builder.class,
                        m.getReturnType(), "Builder method " + m.getName() + " should return Builder for fluency"));
    }

    @Test
    @DisplayName("Exceptions should have descriptive messages")
    void exceptions_messages_descriptive() {
        // Given
        Person person = new Person();

        // When/Then - Test various exception messages
        IllegalArgumentException firstNameEx = assertThrows(IllegalArgumentException.class,
                () -> person.setFirstName(null));
        assertTrue(firstNameEx.getMessage().toLowerCase().contains("first name"),
                "Exception message should mention 'first name'");

        IllegalArgumentException birthdayEx = assertThrows(IllegalArgumentException.class,
                () -> person.setBirthday(null));
        assertTrue(birthdayEx.getMessage().toLowerCase().contains("birthday"),
                "Exception message should mention 'birthday'");
    }

    @Test
    @DisplayName("Static utility methods should be in utility classes")
    void staticMethods_inUtilityClasses_verified() {
        // CpfValidator should only have static methods
        boolean allStatic = Arrays.stream(CpfValidator.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers())).allMatch(m -> Modifier.isStatic(m.getModifiers()));

        assertTrue(allStatic, "All public methods in CpfValidator should be static");

        // CpfValidator should have private constructor
        assertEquals(0, Arrays.stream(CpfValidator.class.getConstructors())
                .filter(c -> Modifier.isPublic(c.getModifiers())).count(),
                "Utility class should not have public constructor");
    }

    @Test
    @DisplayName("Required fields should be validated in constructors/builders")
    void requiredFields_validation_enforced() {
        // Address Builder should validate required fields
        Address.Builder builder = new Address.Builder();

        assertThrows(IllegalArgumentException.class, () -> builder.build(), "Builder should validate required fields");

        // PersonService should validate dependencies
        assertThrows(IllegalArgumentException.class, () -> new PersonService(null, null),
                "Constructor should validate dependencies");
    }

    @Test
    @DisplayName("Collections returned should be defensive copies")
    void collections_defensiveCopies_verified() {
        // Given
        Person person = new Person();
        Person relative = new Person();
        person.addToFamily(relative);

        // When
        List<Person> family1 = person.getFamily();
        List<Person> family2 = person.getFamily();

        // Then
        assertNotSame(family1, family2, "Should return different list instances");
        assertEquals(family1, family2, "Lists should have same content");

        // Modifying returned list should not affect original
        family1.clear();
        assertEquals(1, person.getFamily().size(), "Original family should be unchanged");
    }
}
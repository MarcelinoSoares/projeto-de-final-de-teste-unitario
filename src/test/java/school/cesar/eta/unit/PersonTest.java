package school.cesar.eta.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Person Test Suite")
public class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
    }

    @Nested
    @DisplayName("getName() Tests")
    class GetNameTests {

        @Test
        @DisplayName("Should return full name when both first and last names are set")
        public void getName_firstNameJonLastNameSnow_jonSnow() {
            // Arrange
            String firstName = "Jon";
            String lastName = "Snow";
            person.setFirstName(firstName);
            person.setLastName(lastName);

            // Act
            String actualName = person.getName();

            // Assert
            assertEquals("Jon Snow", actualName, "Should concatenate first and last name with a space");
        }

        @Test
        @DisplayName("Should return only first name when last name is not set")
        public void getName_firstNameJonNoLastName_jon() {
            // Arrange
            String firstName = "Jon";
            person.setFirstName(firstName);

            // Act
            String actualName = person.getName();

            // Assert
            assertEquals("Jon", actualName, "Should return only first name when last name is null");
        }

        @Test
        @DisplayName("Should return only last name when first name is not set")
        public void getName_noFirstNameLastNameSnow_snow() {
            // Arrange
            String lastName = "Snow";
            person.setLastName(lastName);

            // Act
            String actualName = person.getName();

            // Assert
            assertEquals("Snow", actualName, "Should return only last name when first name is null");
        }

        @Test
        @DisplayName("Should throw IllegalStateException when both names are null")
        public void getName_noFirstNameNorLastName_throwsException() {
            // Arrange - person already has null names by default

            // Act & Assert
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> person.getName(),
                    "Should throw IllegalStateException when both names are null");

            assertEquals("Name must be filled", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("isBirthdayToday() Tests")
    class IsBirthdayTodayTests {

        private Person personWithFixedDate;
        private final LocalDate FIXED_TODAY = LocalDate.of(2020, 8, 7);

        @BeforeEach
        void setUp() {
            // Create a Person with overridden getNow() method
            personWithFixedDate = new Person() {
                @Override
                public LocalDate getNow() {
                    return FIXED_TODAY;
                }
            };
        }

        @Test
        @DisplayName("Should return false when birthday is in different month and day")
        public void isBirthdayToday_differentMonthAndDay_false() {
            // Arrange
            LocalDate birthday = LocalDate.of(1991, 7, 19);
            personWithFixedDate.setBirthday(birthday);

            // Act
            boolean isBirthdayToday = personWithFixedDate.isBirthdayToday();

            // Assert
            assertFalse(isBirthdayToday, "Should return false when birthday month and day are different from today");
        }

        @Test
        @DisplayName("Should return false when birthday is in same month but different day")
        public void isBirthdayToday_sameMonthDifferentDay_false() {
            // Arrange
            LocalDate birthday = LocalDate.of(1991, 8, 6);
            personWithFixedDate.setBirthday(birthday);

            // Act
            boolean isBirthdayToday = personWithFixedDate.isBirthdayToday();

            // Assert
            assertFalse(isBirthdayToday, "Should return false when only the month matches");
        }

        @Test
        @DisplayName("Should return true when birthday month and day match today")
        public void isBirthdayToday_sameMonthAndDay_true() {
            // Arrange
            LocalDate birthday = LocalDate.of(1991, 8, 7);
            personWithFixedDate.setBirthday(birthday);

            // Act
            boolean isBirthdayToday = personWithFixedDate.isBirthdayToday();

            // Assert
            assertTrue(isBirthdayToday, "Should return true when birthday month and day match today");
        }

        @Test
        @DisplayName("Should return false when birthday is null")
        public void isBirthdayToday_nullBirthday_false() {
            // Arrange - birthday is null by default

            // Act
            boolean isBirthdayToday = personWithFixedDate.isBirthdayToday();

            // Assert
            assertFalse(isBirthdayToday, "Should return false when birthday is null");
        }
    }

    @Nested
    @DisplayName("Family Management Tests")
    class FamilyManagementTests {

        private Person person1;
        private Person person2;

        @BeforeEach
        void setUp() {
            person1 = new Person();
            person2 = new Person();
        }

        @Test
        @DisplayName("Should add person to family and create bidirectional relationship")
        public void addToFamily_somePerson_familyHasNewMember() {
            // Arrange - persons already created

            // Act
            person1.addToFamily(person2);

            // Assert
            assertTrue(person1.isFamily(person2), "Person1 should have person2 in family");
            assertTrue(person2.isFamily(person1), "Person2 should have person1 in family (bidirectional)");
            assertEquals(1, person1.getFamily().size(), "Person1 family size should be 1");
            assertEquals(1, person2.getFamily().size(), "Person2 family size should be 1");
        }

        @Test
        @DisplayName("Should update both persons' families when adding family member")
        public void addToFamily_somePerson_personAddedAlsoHasItsFamilyUpdated() {
            // Arrange
            Person person3 = new Person();

            // Act
            person1.addToFamily(person2);
            person1.addToFamily(person3);

            // Assert
            assertTrue(person1.isFamily(person2), "Person1 should have person2 in family");
            assertTrue(person1.isFamily(person3), "Person1 should have person3 in family");
            assertTrue(person2.isFamily(person1), "Person2 should have person1 in family");
            assertTrue(person3.isFamily(person1), "Person3 should have person1 in family");

            List<Person> person1Family = person1.getFamily();
            assertEquals(2, person1Family.size(), "Person1 should have 2 family members");
            assertTrue(person1Family.contains(person2), "Person1 family should contain person2");
            assertTrue(person1Family.contains(person3), "Person1 family should contain person3");
        }

        @Test
        @DisplayName("Should return false when checking if non-relative is family")
        public void isFamily_nonRelativePerson_false() {
            // Arrange - persons already created

            // Act
            boolean areFamily = person1.isFamily(person2);

            // Assert
            assertFalse(areFamily, "Should return false for persons not in family");
        }

        @Test
        @DisplayName("Should return true when checking if relative is family")
        public void isFamily_relativePerson_true() {
            // Arrange
            person1.addToFamily(person2);

            // Act
            boolean areFamily = person1.isFamily(person2);

            // Assert
            assertTrue(areFamily, "Should return true for persons in family");
        }

        @Test
        @DisplayName("Should not add null person to family")
        public void addToFamily_nullPerson_noExceptionAndNotAdded() {
            // Arrange - person already created

            // Act
            person.addToFamily(null);

            // Assert
            assertTrue(person.getFamily().isEmpty(), "Family should remain empty when trying to add null");
        }

        @Test
        @DisplayName("Should not add self to family")
        public void addToFamily_selfReference_noExceptionAndNotAdded() {
            // Arrange - person already created

            // Act
            person.addToFamily(person);

            // Assert
            assertTrue(person.getFamily().isEmpty(), "Family should remain empty when trying to add self");
        }

        @Test
        @DisplayName("Should not add duplicate person to family")
        public void addToFamily_duplicatePerson_notAddedTwice() {
            // Arrange
            Person relative = new Person();
            person.addToFamily(relative);
            int initialSize = person.getFamily().size();

            // Act
            person.addToFamily(relative);

            // Assert
            assertEquals(initialSize, person.getFamily().size(), "Family size should not change when adding duplicate");
            assertEquals(1, person.getFamily().size(), "Family should contain only one instance of the relative");
        }
    }

    @Nested
    @DisplayName("Setter Validation Tests")
    class SetterValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when setting null first name")
        public void setFirstName_null_throwsException() {
            // Arrange - person already created

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> person.setFirstName(null), "Should throw IllegalArgumentException for null first name");

            assertEquals("First name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when setting empty first name")
        public void setFirstName_empty_throwsException() {
            // Arrange
            String emptyName = "   ";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> person.setFirstName(emptyName), "Should throw IllegalArgumentException for empty first name");

            assertEquals("First name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when setting null last name")
        public void setLastName_null_throwsException() {
            // Arrange - person already created

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> person.setLastName(null), "Should throw IllegalArgumentException for null last name");

            assertEquals("Last name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when setting empty last name")
        public void setLastName_empty_throwsException() {
            // Arrange
            String emptyName = "";

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> person.setLastName(emptyName), "Should throw IllegalArgumentException for empty last name");

            assertEquals("Last name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when setting null birthday")
        public void setBirthday_null_throwsException() {
            // Arrange - person already created

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> person.setBirthday(null), "Should throw IllegalArgumentException for null birthday");

            assertEquals("Birthday cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Additional Coverage Tests")
    class AdditionalCoverageTests {

        @Test
        @DisplayName("Should return immutable copy of family list")
        public void getFamily_modifyReturnedList_originalListUnchanged() {
            // Arrange
            Person relative = new Person();
            person.addToFamily(relative);

            // Act
            List<Person> familyList = person.getFamily();
            familyList.clear(); // Try to modify returned list

            // Assert
            assertEquals(1, person.getFamily().size(), "Original family list should remain unchanged");
        }

        @Test
        @DisplayName("Should properly set and get valid names")
        public void settersAndGetters_validValues_workCorrectly() {
            // Arrange
            String firstName = "John";
            String lastName = "Doe";
            LocalDate birthday = LocalDate.of(1990, 1, 1);

            // Act
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setBirthday(birthday);

            // Assert
            assertEquals(firstName, person.getFirstName(), "First name should be set correctly");
            assertEquals(lastName, person.getLastName(), "Last name should be set correctly");
            assertEquals(birthday, person.getBirthday(), "Birthday should be set correctly");
        }

        @Test
        @DisplayName("Should handle complex family relationships")
        public void addToFamily_complexRelationships_maintainsBidirectionalIntegrity() {
            // Arrange
            Person parent = new Person();
            Person child1 = new Person();
            Person child2 = new Person();
            Person grandchild = new Person();

            // Act
            parent.addToFamily(child1);
            parent.addToFamily(child2);
            child1.addToFamily(child2);
            child1.addToFamily(grandchild);

            // Assert
            // Parent relationships
            assertTrue(parent.isFamily(child1), "Parent should be family with child1");
            assertTrue(parent.isFamily(child2), "Parent should be family with child2");
            assertFalse(parent.isFamily(grandchild), "Parent should not be directly family with grandchild");

            // Child1 relationships
            assertTrue(child1.isFamily(parent), "Child1 should be family with parent");
            assertTrue(child1.isFamily(child2), "Child1 should be family with child2");
            assertTrue(child1.isFamily(grandchild), "Child1 should be family with grandchild");

            // Verify family sizes
            assertEquals(2, parent.getFamily().size(), "Parent should have 2 family members");
            assertEquals(3, child1.getFamily().size(), "Child1 should have 3 family members");
            assertEquals(2, child2.getFamily().size(), "Child2 should have 2 family members");
            assertEquals(1, grandchild.getFamily().size(), "Grandchild should have 1 family member");
        }

        @Test
        @DisplayName("Should verify getNow() returns current date by default")
        public void getNow_defaultImplementation_returnsCurrentDate() {
            // Arrange
            Person regularPerson = new Person();
            LocalDate beforeCall = LocalDate.now();

            // Act
            LocalDate actualDate = regularPerson.getNow();
            LocalDate afterCall = LocalDate.now();

            // Assert
            assertNotNull(actualDate, "getNow() should not return null");
            assertTrue(!actualDate.isBefore(beforeCall) && !actualDate.isAfter(afterCall),
                    "getNow() should return current date");
        }
    }
}

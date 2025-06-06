package school.cesar.eta.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
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
            person1.setId(1L);
            person2 = new Person();
            person2.setId(2L);
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
            person3.setId(3L);

            // Act
            person1.addToFamily(person2);
            person1.addToFamily(person3);

            // Assert
            assertTrue(person1.isFamily(person2), "Person1 should have person2 in family");
            assertTrue(person1.isFamily(person3), "Person1 should have person3 in family");
            assertTrue(person2.isFamily(person1), "Person2 should have person1 in family");
            assertTrue(person3.isFamily(person1), "Person3 should have person1 in family");

            // Note: person2 and person3 are not directly related, only through person1
            assertFalse(person2.isFamily(person3), "Person2 should not have person3 in family");
            assertFalse(person3.isFamily(person2), "Person3 should not have person2 in family");

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

        @Test
        @DisplayName("Should throw IllegalArgumentException when setting future birthday")
        public void setBirthday_futureDate_throwsException() {
            // Arrange
            LocalDate futureDate = LocalDate.now().plusDays(1);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> person.setBirthday(futureDate), "Should throw IllegalArgumentException for future birthday");

            assertEquals("Birthday cannot be in the future", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Age Calculation Tests")
    class AgeCalculationTests {

        private Person personWithFixedDate;
        private final LocalDate FIXED_TODAY = LocalDate.of(2024, 1, 15);

        @BeforeEach
        void setUp() {
            personWithFixedDate = new Person() {
                @Override
                public LocalDate getNow() {
                    return FIXED_TODAY;
                }
            };
        }

        @Test
        @DisplayName("Should calculate age correctly")
        public void getAge_validBirthday_correctAge() {
            // Arrange
            personWithFixedDate.setBirthday(LocalDate.of(2000, 1, 15));

            // Act
            Integer age = personWithFixedDate.getAge();

            // Assert
            assertEquals(24, age, "Should calculate age as 24");
        }

        @Test
        @DisplayName("Should return null when birthday is not set")
        public void getAge_noBirthday_null() {
            // Act
            Integer age = personWithFixedDate.getAge();

            // Assert
            assertNull(age, "Should return null when birthday is not set");
        }

        @Test
        @DisplayName("Should identify adult correctly")
        public void isAdult_ageAbove18_true() {
            // Arrange
            personWithFixedDate.setBirthday(LocalDate.of(2000, 1, 1));

            // Act & Assert
            assertTrue(personWithFixedDate.isAdult(), "Should be adult when age is above 18");
        }

        @Test
        @DisplayName("Should identify minor correctly")
        public void isAdult_ageBelow18_false() {
            // Arrange
            personWithFixedDate.setBirthday(LocalDate.of(2010, 1, 1));

            // Act & Assert
            assertFalse(personWithFixedDate.isAdult(), "Should not be adult when age is below 18");
        }

        @Test
        @DisplayName("Should identify exactly 18 as adult")
        public void isAdult_exactlyAge18_true() {
            // Arrange
            personWithFixedDate.setBirthday(LocalDate.of(2006, 1, 15));

            // Act & Assert
            assertTrue(personWithFixedDate.isAdult(), "Should be adult when exactly 18");
        }

        @Test
        @DisplayName("Should return false for isAdult when no birthday")
        public void isAdult_noBirthday_false() {
            // Act & Assert
            assertFalse(personWithFixedDate.isAdult(), "Should not be adult when no birthday");
        }
    }

    @Nested
    @DisplayName("Address and Location Tests")
    class AddressLocationTests {

        @Test
        @DisplayName("Should set and get address correctly")
        public void setGetAddress_validAddress_success() {
            // Arrange
            Address address = new Address.Builder().street("Main St").number("123").city("Recife").state("PE")
                    .zipCode("50000-000").build();

            // Act
            person.setAddress(address);

            // Assert
            assertEquals(address, person.getAddress());
        }

        @Test
        @DisplayName("Should return true when persons live in same city")
        public void livesInSameCity_sameCity_true() {
            // Arrange
            Person person1 = new Person();
            Person person2 = new Person();

            Address address1 = new Address.Builder().street("Street A").number("100").city("Recife").state("PE")
                    .zipCode("50000-000").build();

            Address address2 = new Address.Builder().street("Street B").number("200").city("Recife").state("PE")
                    .zipCode("51000-000").build();

            person1.setAddress(address1);
            person2.setAddress(address2);

            // Act & Assert
            assertTrue(person1.livesInSameCity(person2));
        }

        @Test
        @DisplayName("Should return false when persons live in different cities")
        public void livesInSameCity_differentCity_false() {
            // Arrange
            Person person1 = new Person();
            Person person2 = new Person();

            Address address1 = new Address.Builder().street("Street A").number("100").city("Recife").state("PE")
                    .zipCode("50000-000").build();

            Address address2 = new Address.Builder().street("Street B").number("200").city("Olinda").state("PE")
                    .zipCode("53000-000").build();

            person1.setAddress(address1);
            person2.setAddress(address2);

            // Act & Assert
            assertFalse(person1.livesInSameCity(person2));
        }

        @Test
        @DisplayName("Should return false when one person has no address")
        public void livesInSameCity_oneWithoutAddress_false() {
            // Arrange
            Person person1 = new Person();
            Person person2 = new Person();

            Address address = new Address.Builder().street("Street A").number("100").city("Recife").state("PE")
                    .zipCode("50000-000").build();

            person1.setAddress(address);
            // person2 has no address

            // Act & Assert
            assertFalse(person1.livesInSameCity(person2));
        }

        @Test
        @DisplayName("Should return false when comparing with null person")
        public void livesInSameCity_nullPerson_false() {
            // Arrange
            Address address = new Address.Builder().street("Street A").number("100").city("Recife").state("PE")
                    .zipCode("50000-000").build();

            person.setAddress(address);

            // Act & Assert
            assertFalse(person.livesInSameCity(null));
        }
    }

    @Nested
    @DisplayName("ID Management Tests")
    class IdManagementTests {

        @Test
        @DisplayName("Should set and get ID correctly")
        public void setGetId_validId_success() {
            // Arrange
            Long id = 123L;

            // Act
            person.setId(id);

            // Assert
            assertEquals(id, person.getId());
        }

        @Test
        @DisplayName("Should allow null ID")
        public void setId_null_success() {
            // Act
            person.setId(null);

            // Assert
            assertNull(person.getId());
        }
    }

    @Nested
    @DisplayName("Equals, HashCode and ToString Tests")
    class EqualsHashCodeToStringTests {

        @Test
        @DisplayName("Should be equal for same person data")
        public void equals_sameData_true() {
            // Arrange
            Person person1 = createCompletePerson(1L, "John", "Doe", LocalDate.of(1990, 1, 1));
            Person person2 = createCompletePerson(1L, "John", "Doe", LocalDate.of(1990, 1, 1));

            // Act & Assert
            assertEquals(person1, person2);
            assertEquals(person1.hashCode(), person2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different IDs")
        public void equals_differentId_false() {
            // Arrange
            Person person1 = createCompletePerson(1L, "John", "Doe", LocalDate.of(1990, 1, 1));
            Person person2 = createCompletePerson(2L, "John", "Doe", LocalDate.of(1990, 1, 1));

            // Act & Assert
            assertNotEquals(person1, person2);
        }

        @Test
        @DisplayName("Should handle equals edge cases")
        public void equals_edgeCases_correctBehavior() {
            // Arrange
            Person person1 = createCompletePerson(1L, "John", "Doe", LocalDate.of(1990, 1, 1));

            // Act & Assert
            assertEquals(person1, person1); // Same instance
            assertNotEquals(person1, null); // Null comparison
            assertNotEquals(person1, "Not a person"); // Different type
        }

        @Test
        @DisplayName("Should generate meaningful toString")
        public void toString_completePerson_containsAllInfo() {
            // Arrange
            Person personWithFixedDate = new Person() {
                @Override
                public LocalDate getNow() {
                    return LocalDate.of(2024, 1, 1);
                }
            };
            personWithFixedDate.setId(123L);
            personWithFixedDate.setFirstName("John");
            personWithFixedDate.setLastName("Doe");
            personWithFixedDate.setBirthday(LocalDate.of(1990, 1, 1));

            // Act
            String result = personWithFixedDate.toString();

            // Assert
            assertTrue(result.contains("123"));
            assertTrue(result.contains("John Doe"));
            assertTrue(result.contains("34")); // age
            assertTrue(result.contains("1990-01-01"));
        }

        private Person createCompletePerson(Long id, String firstName, String lastName, LocalDate birthday) {
            Person p = new Person();
            p.setId(id);
            p.setFirstName(firstName);
            p.setLastName(lastName);
            p.setBirthday(birthday);
            return p;
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
            parent.setId(1L);
            Person child1 = new Person();
            child1.setId(2L);
            Person child2 = new Person();
            child2.setId(3L);
            Person grandchild = new Person();
            grandchild.setId(4L);

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

            // Child2 relationships
            assertTrue(child2.isFamily(parent), "Child2 should be family with parent");
            assertTrue(child2.isFamily(child1), "Child2 should be family with child1");
            assertFalse(child2.isFamily(grandchild), "Child2 should not be directly family with grandchild");

            // Grandchild relationships
            assertTrue(grandchild.isFamily(child1), "Grandchild should be family with child1");
            assertFalse(grandchild.isFamily(parent), "Grandchild should not be directly family with parent");
            assertFalse(grandchild.isFamily(child2), "Grandchild should not be directly family with child2");

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

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle person with maximum age")
        void getAge_veryOldPerson_calculatesCorrectly() {
            // Arrange
            Person ancientPerson = new Person() {
                @Override
                public LocalDate getNow() {
                    return LocalDate.of(2024, 1, 1);
                }
            };
            ancientPerson.setBirthday(LocalDate.of(1900, 1, 1));

            // Act
            Integer age = ancientPerson.getAge();

            // Assert
            assertEquals(124, age);
            assertTrue(ancientPerson.isAdult());
        }

        @Test
        @DisplayName("Should handle person born yesterday")
        void getAge_bornYesterday_ageZero() {
            // Arrange
            Person baby = new Person() {
                @Override
                public LocalDate getNow() {
                    return LocalDate.of(2024, 1, 2);
                }
            };
            baby.setBirthday(LocalDate.of(2024, 1, 1));

            // Act
            Integer age = baby.getAge();

            // Assert
            assertEquals(0, age);
            assertFalse(baby.isAdult());
        }

        @Test
        @DisplayName("Should handle leap year birthdays")
        void isBirthdayToday_leapYearBirthday_correctBehavior() {
            // Test person born on Feb 29
            Person leapYearPerson = new Person() {
                @Override
                public LocalDate getNow() {
                    return LocalDate.of(2024, 2, 29); // 2024 is a leap year
                }
            };
            leapYearPerson.setBirthday(LocalDate.of(2000, 2, 29));

            assertTrue(leapYearPerson.isBirthdayToday());

            // Test same person on non-leap year
            Person nonLeapYearPerson = new Person() {
                @Override
                public LocalDate getNow() {
                    return LocalDate.of(2023, 2, 28); // 2023 is not a leap year
                }
            };
            nonLeapYearPerson.setBirthday(LocalDate.of(2000, 2, 29));

            assertFalse(nonLeapYearPerson.isBirthdayToday());
        }

        @Test
        @DisplayName("Should handle names with special characters")
        void setName_specialCharacters_accepted() {
            // Arrange & Act
            person.setFirstName("José-María");
            person.setLastName("O'Connor-Smith");

            // Assert
            assertEquals("José-María O'Connor-Smith", person.getName());
        }

        @Test
        @DisplayName("Should handle very long names")
        void setName_veryLongNames_accepted() {
            // Arrange
            String longFirstName = "A".repeat(100);
            String longLastName = "B".repeat(100);

            // Act
            person.setFirstName(longFirstName);
            person.setLastName(longLastName);

            // Assert
            assertEquals(longFirstName + " " + longLastName, person.getName());
        }

        @Test
        @DisplayName("Should handle circular family relationships")
        void addToFamily_circularRelationship_handledCorrectly() {
            // Arrange
            Person person1 = new Person();
            person1.setId(1L);
            Person person2 = new Person();
            person2.setId(2L);
            Person person3 = new Person();
            person3.setId(3L);

            // Act - Create circular relationship
            person1.addToFamily(person2);
            person2.addToFamily(person3);
            person3.addToFamily(person1);

            // Assert - All should be family with each other
            assertTrue(person1.isFamily(person2));
            assertTrue(person2.isFamily(person3));
            assertTrue(person3.isFamily(person1));

            // But transitive relationships don't exist automatically
            assertTrue(person1.isFamily(person3)); // Direct relationship added
            assertTrue(person2.isFamily(person1)); // Direct relationship added
            assertTrue(person3.isFamily(person2)); // Direct relationship added
        }

        @Test
        @DisplayName("Should handle maximum family size")
        void addToFamily_manyFamilyMembers_handledCorrectly() {
            // Arrange
            Person mainPerson = new Person();
            mainPerson.setId(0L);
            List<Person> familyMembers = new ArrayList<>();

            // Create 100 family members
            for (int i = 1; i <= 100; i++) {
                Person member = new Person();
                member.setId((long) i);
                familyMembers.add(member);
            }

            // Act - Add all as family
            for (Person member : familyMembers) {
                mainPerson.addToFamily(member);
            }

            // Assert
            assertEquals(100, mainPerson.getFamily().size());
            for (Person member : familyMembers) {
                assertTrue(mainPerson.isFamily(member));
                assertTrue(member.isFamily(mainPerson));
            }
        }

        @Test
        @DisplayName("Should handle toString with all null fields")
        void toString_allNullFields_handledGracefully() {
            // Arrange - person with no fields set

            // Act
            String result = person.toString();

            // Assert
            assertTrue(result.contains("No Name"));
            assertTrue(result.contains("age=null"));
            assertTrue(result.contains("id=null"));
        }

        @Test
        @DisplayName("Should handle equals with different combinations of null fields")
        void equals_variousNullCombinations_correctBehavior() {
            // Arrange
            Person p1 = new Person();
            Person p2 = new Person();

            // Both with null fields
            assertEquals(p1, p2);

            // One with ID
            p1.setId(1L);
            assertNotEquals(p1, p2);

            // Both with same ID
            p2.setId(1L);
            assertEquals(p1, p2);

            // Add name to one
            p1.setFirstName("John");
            assertNotEquals(p1, p2);
        }

        @Test
        @DisplayName("Should handle birthday exactly at midnight")
        void setBirthday_midnightTime_accepted() {
            // Arrange
            LocalDate midnightBirthday = LocalDate.of(2000, 1, 1);

            // Act
            person.setBirthday(midnightBirthday);

            // Assert
            assertEquals(midnightBirthday, person.getBirthday());
        }

        @Test
        @DisplayName("Should handle concurrent family additions")
        void addToFamily_concurrentAdditions_maintainsConsistency() {
            // This test simulates what would happen with concurrent modifications
            // In a real concurrent scenario, you'd need thread-safe collections

            // Arrange
            Person central = new Person();
            central.setId(0L);
            Person member1 = new Person();
            member1.setId(1L);
            Person member2 = new Person();
            member2.setId(2L);

            // Act - Simulate "concurrent" additions
            central.addToFamily(member1);
            member1.addToFamily(member2);
            central.addToFamily(member2);
            member2.addToFamily(central); // This should be a no-op

            // Assert
            assertEquals(2, central.getFamily().size());
            assertEquals(2, member1.getFamily().size());
            assertEquals(2, member2.getFamily().size());
        }

        @Test
        @DisplayName("Should validate and format CPF correctly")
        void setCpf_validCpf_formattedAndStored() {
            // Arrange & Act
            person.setCpf("12345678909");

            // Assert
            assertEquals("123.456.789-09", person.getCpf());

            // Test with already formatted CPF
            person.setCpf("111.444.777-35");
            assertEquals("111.444.777-35", person.getCpf());
        }

        @Test
        @DisplayName("Should reject invalid CPF")
        void setCpf_invalidCpf_throwsException() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> person.setCpf("123.456.789-00"));
            assertThrows(IllegalArgumentException.class, () -> person.setCpf("111.111.111-11"));
        }

        @Test
        @DisplayName("Should handle null CPF")
        void setCpf_null_accepted() {
            // Arrange
            person.setCpf("123.456.789-09");

            // Act
            person.setCpf(null);

            // Assert
            assertNull(person.getCpf());
        }

        @Test
        @DisplayName("Should calculate age in months correctly")
        void getAgeInMonths_variousAges_correctCalculation() {
            // Arrange
            Person personWithAge = new Person() {
                @Override
                public LocalDate getNow() {
                    return LocalDate.of(2024, 3, 15);
                }
            };

            // Test 2 years and 3 months
            personWithAge.setBirthday(LocalDate.of(2021, 12, 15));
            assertEquals(27, personWithAge.getAgeInMonths());

            // Test exactly 1 year
            personWithAge.setBirthday(LocalDate.of(2023, 3, 15));
            assertEquals(12, personWithAge.getAgeInMonths());

            // Test less than a month
            personWithAge.setBirthday(LocalDate.of(2024, 3, 1));
            assertEquals(0, personWithAge.getAgeInMonths());
        }

        @Test
        @DisplayName("Should calculate age in days correctly")
        void getAgeInDays_variousAges_correctCalculation() {
            // Arrange
            Person personWithAge = new Person() {
                @Override
                public LocalDate getNow() {
                    return LocalDate.of(2024, 1, 15);
                }
            };

            // Test exactly 1 year (365 days)
            personWithAge.setBirthday(LocalDate.of(2023, 1, 15));
            assertEquals(365L, personWithAge.getAgeInDays());

            // Test 1 week
            personWithAge.setBirthday(LocalDate.of(2024, 1, 8));
            assertEquals(7L, personWithAge.getAgeInDays());

            // Test leap year (2020 was a leap year)
            Person leapYearPerson = new Person() {
                @Override
                public LocalDate getNow() {
                    return LocalDate.of(2021, 2, 28);
                }
            };
            leapYearPerson.setBirthday(LocalDate.of(2020, 2, 28));
            assertEquals(366L, leapYearPerson.getAgeInDays()); // Leap year has 366 days
        }

        @Test
        @DisplayName("Should return null for age calculations when no birthday")
        void ageCalculations_noBirthday_null() {
            // Assert
            assertNull(person.getAge());
            assertNull(person.getAgeInMonths());
            assertNull(person.getAgeInDays());
        }
    }
}

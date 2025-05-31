package school.cesar.eta.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersonService Test Suite")
public class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PersonService service;

    @Captor
    private ArgumentCaptor<Person> personCaptor;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

    @Captor
    private ArgumentCaptor<String> nameCaptor;

    private Person testPerson;

    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setFirstName("John");
        testPerson.setLastName("Doe");
        testPerson.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should throw exception when repository is null")
        void constructor_nullRepository_throwsException() {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new PersonService(null, emailService));
            assertEquals("Repository cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when email service is null")
        void constructor_nullEmailService_throwsException() {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new PersonService(repository, null));
            assertEquals("Email service cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Create Person Tests")
    class CreatePersonTests {

        @Test
        @DisplayName("Should create person and send welcome email")
        void createPerson_validPerson_success() {
            // Arrange
            Person savedPerson = new Person();
            savedPerson.setFirstName("John");
            savedPerson.setLastName("Doe");
            when(repository.save(any(Person.class))).thenReturn(savedPerson);

            // Act
            Person result = service.createPerson(testPerson);

            // Assert
            assertNotNull(result);
            verify(repository).save(testPerson);
            verify(emailService).sendWelcomeEmail("john.doe@example.com", "John Doe");
        }

        @Test
        @DisplayName("Should throw exception when person is null")
        void createPerson_nullPerson_throwsException() {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> service.createPerson(null));
            assertEquals("Person cannot be null", exception.getMessage());
            verify(repository, never()).save(any());
            verify(emailService, never()).sendWelcomeEmail(anyString(), anyString());
        }

        @Test
        @DisplayName("Should throw exception when person has no names")
        void createPerson_personWithoutNames_throwsException() {
            // Arrange
            Person invalidPerson = new Person();

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> service.createPerson(invalidPerson));
            assertEquals("Person must have at least one name", exception.getMessage());
        }

        @Test
        @DisplayName("Should generate email with only first name")
        void createPerson_onlyFirstName_generatesCorrectEmail() {
            // Arrange
            Person person = new Person();
            person.setFirstName("Jane");
            when(repository.save(any(Person.class))).thenReturn(person);

            // Act
            service.createPerson(person);

            // Assert
            verify(emailService).sendWelcomeEmail(emailCaptor.capture(), nameCaptor.capture());
            assertEquals("jane@example.com", emailCaptor.getValue());
            assertEquals("Jane", nameCaptor.getValue());
        }
    }

    @Nested
    @DisplayName("Find Person Tests")
    class FindPersonTests {

        @Test
        @DisplayName("Should find person by ID")
        void findPerson_existingId_returnsPerson() {
            // Arrange
            Long id = 1L;
            when(repository.findById(id)).thenReturn(Optional.of(testPerson));

            // Act
            Person result = service.findPerson(id);

            // Assert
            assertNotNull(result);
            assertEquals(testPerson, result);
            verify(repository).findById(id);
        }

        @Test
        @DisplayName("Should throw exception when person not found")
        void findPerson_nonExistingId_throwsException() {
            // Arrange
            Long id = 999L;
            when(repository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            PersonNotFoundException exception = assertThrows(PersonNotFoundException.class,
                    () -> service.findPerson(id));
            assertEquals("Person not found with id: 999", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Update Person Tests")
    class UpdatePersonTests {

        @Test
        @DisplayName("Should update all person fields")
        void updatePerson_allFields_success() {
            // Arrange
            Long id = 1L;
            Person existingPerson = new Person();
            existingPerson.setFirstName("Old");
            existingPerson.setLastName("Name");

            Person updatedInfo = new Person();
            updatedInfo.setFirstName("New");
            updatedInfo.setLastName("Name");
            updatedInfo.setBirthday(LocalDate.of(1995, 5, 5));

            when(repository.findById(id)).thenReturn(Optional.of(existingPerson));
            when(repository.save(any(Person.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            Person result = service.updatePerson(id, updatedInfo);

            // Assert
            assertEquals("New", result.getFirstName());
            assertEquals("Name", result.getLastName());
            assertEquals(LocalDate.of(1995, 5, 5), result.getBirthday());
            verify(repository).save(personCaptor.capture());
            assertEquals("New", personCaptor.getValue().getFirstName());
        }

        @Test
        @DisplayName("Should update only provided fields")
        void updatePerson_partialUpdate_success() {
            // Arrange
            Long id = 1L;
            Person existingPerson = new Person();
            existingPerson.setFirstName("John");
            existingPerson.setLastName("Doe");
            existingPerson.setBirthday(LocalDate.of(1990, 1, 1));

            Person updatedInfo = new Person();
            updatedInfo.setFirstName("Jane");
            // lastName and birthday are null - should not be updated

            when(repository.findById(id)).thenReturn(Optional.of(existingPerson));
            when(repository.save(any(Person.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            Person result = service.updatePerson(id, updatedInfo);

            // Assert
            assertEquals("Jane", result.getFirstName());
            assertEquals("Doe", result.getLastName()); // Unchanged
            assertEquals(LocalDate.of(1990, 1, 1), result.getBirthday()); // Unchanged
        }
    }

    @Nested
    @DisplayName("Delete Person Tests")
    class DeletePersonTests {

        @Test
        @DisplayName("Should delete existing person")
        void deletePerson_existingId_success() {
            // Arrange
            Long id = 1L;
            when(repository.existsById(id)).thenReturn(true);
            when(repository.deleteById(id)).thenReturn(true);

            // Act
            service.deletePerson(id);

            // Assert
            verify(repository).existsById(id);
            verify(repository).deleteById(id);
        }

        @Test
        @DisplayName("Should throw exception when person not found")
        void deletePerson_nonExistingId_throwsException() {
            // Arrange
            Long id = 999L;
            when(repository.existsById(id)).thenReturn(false);

            // Act & Assert
            PersonNotFoundException exception = assertThrows(PersonNotFoundException.class,
                    () -> service.deletePerson(id));
            assertEquals("Person not found with id: 999", exception.getMessage());
            verify(repository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Find Adults Tests")
    class FindAdultsTests {

        @Test
        @DisplayName("Should return only adults")
        void findAdults_mixedAges_returnsOnlyAdults() {
            // Arrange
            Person adult1 = createPersonWithAge(25);
            Person adult2 = createPersonWithAge(30);
            Person minor = createPersonWithAge(15);
            Person exactlyEighteen = createPersonWithAge(18);

            List<Person> allPersons = Arrays.asList(adult1, adult2, minor, exactlyEighteen);
            when(repository.findAll()).thenReturn(allPersons);

            // Act
            List<Person> adults = service.findAdults();

            // Assert
            assertEquals(3, adults.size());
            assertTrue(adults.contains(adult1));
            assertTrue(adults.contains(adult2));
            assertTrue(adults.contains(exactlyEighteen));
            assertFalse(adults.contains(minor));
        }

        @Test
        @DisplayName("Should handle persons without birthday")
        void findAdults_personWithoutBirthday_excluded() {
            // Arrange
            Person withBirthday = createPersonWithAge(25);
            Person withoutBirthday = new Person();
            withoutBirthday.setFirstName("No");
            withoutBirthday.setLastName("Birthday");

            when(repository.findAll()).thenReturn(Arrays.asList(withBirthday, withoutBirthday));

            // Act
            List<Person> adults = service.findAdults();

            // Assert
            assertEquals(1, adults.size());
            assertTrue(adults.contains(withBirthday));
        }

        private Person createPersonWithAge(int age) {
            Person person = new Person();
            person.setFirstName("Person");
            person.setLastName("Age" + age);
            person.setBirthday(LocalDate.now().minusYears(age));
            return person;
        }
    }

    @Nested
    @DisplayName("Birthday Greetings Tests")
    class BirthdayGreetingsTests {

        @Test
        @DisplayName("Should send greetings to all birthday people")
        void sendBirthdayGreetings_multipleBirthdays_sendsAllGreetings() {
            // Arrange
            Person person1 = new Person();
            person1.setFirstName("Alice");
            person1.setLastName("Smith");
            person1.setBirthday(LocalDate.now().minusYears(25));

            Person person2 = new Person();
            person2.setFirstName("Bob");
            person2.setLastName("Jones");
            person2.setBirthday(LocalDate.now().minusYears(30));

            when(repository.findBirthdayToday()).thenReturn(Arrays.asList(person1, person2));

            // Act
            int count = service.sendBirthdayGreetings();

            // Assert
            assertEquals(2, count);
            verify(emailService).sendBirthdayGreeting("alice.smith@example.com", "Alice Smith", 25);
            verify(emailService).sendBirthdayGreeting("bob.jones@example.com", "Bob Jones", 30);
        }

        @Test
        @DisplayName("Should handle empty birthday list")
        void sendBirthdayGreetings_noBirthdays_returnsZero() {
            // Arrange
            when(repository.findBirthdayToday()).thenReturn(Collections.emptyList());

            // Act
            int count = service.sendBirthdayGreetings();

            // Assert
            assertEquals(0, count);
            verify(emailService, never()).sendBirthdayGreeting(anyString(), anyString(), anyInt());
        }
    }

    @Nested
    @DisplayName("Family Management Tests")
    class FamilyManagementTests {

        @Test
        @DisplayName("Should add family member successfully")
        void addFamilyMember_validIds_success() {
            // Arrange
            Long personId = 1L;
            Long familyMemberId = 2L;

            Person person = new Person();
            person.setFirstName("Person");

            Person familyMember = new Person();
            familyMember.setFirstName("Family");

            when(repository.findById(personId)).thenReturn(Optional.of(person));
            when(repository.findById(familyMemberId)).thenReturn(Optional.of(familyMember));
            when(repository.save(any(Person.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            service.addFamilyMember(personId, familyMemberId);

            // Assert
            assertTrue(person.isFamily(familyMember));
            assertTrue(familyMember.isFamily(person));
            verify(repository, times(2)).save(any(Person.class));
        }

        @Test
        @DisplayName("Should throw exception when adding self as family")
        void addFamilyMember_sameId_throwsException() {
            // Arrange
            Long id = 1L;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> service.addFamilyMember(id, id));
            assertEquals("Cannot add self as family member", exception.getMessage());
            verify(repository, never()).findById(any());
        }

        @Test
        @DisplayName("Should find family members")
        void findFamilyMembers_existingPerson_returnsFamilyList() {
            // Arrange
            Long personId = 1L;
            Person person = new Person();
            person.setId(1L);
            Person family1 = new Person();
            family1.setId(2L);
            Person family2 = new Person();
            family2.setId(3L);
            person.addToFamily(family1);
            person.addToFamily(family2);

            when(repository.findById(personId)).thenReturn(Optional.of(person));

            // Act
            List<Person> family = service.findFamilyMembers(personId);

            // Assert
            assertEquals(2, family.size());
            assertTrue(family.contains(family1));
            assertTrue(family.contains(family2));
        }
    }

    @Nested
    @DisplayName("Statistics Tests")
    class StatisticsTests {

        @Test
        @DisplayName("Should calculate correct statistics")
        void getStatistics_multiplePersons_correctCalculations() {
            // Arrange
            Person adult1 = createPersonWithAge(25);
            Person adult2 = createPersonWithAge(35);
            Person minor = createPersonWithAge(10);

            when(repository.findAll()).thenReturn(Arrays.asList(adult1, adult2, minor));

            // Act
            PersonService.PersonStatistics stats = service.getStatistics();

            // Assert
            assertEquals(3, stats.getTotalCount());
            assertEquals(2, stats.getAdultCount());
            assertEquals(23.33, stats.getAverageAge(), 0.01);
        }

        @Test
        @DisplayName("Should handle empty repository")
        void getStatistics_emptyRepository_zeroValues() {
            // Arrange
            when(repository.findAll()).thenReturn(Collections.emptyList());

            // Act
            PersonService.PersonStatistics stats = service.getStatistics();

            // Assert
            assertEquals(0, stats.getTotalCount());
            assertEquals(0, stats.getAdultCount());
            assertEquals(0.0, stats.getAverageAge());
        }

        private Person createPersonWithAge(int age) {
            Person person = new Person();
            person.setFirstName("Test");
            person.setLastName("Person");
            person.setBirthday(LocalDate.now().minusYears(age));
            return person;
        }
    }
}
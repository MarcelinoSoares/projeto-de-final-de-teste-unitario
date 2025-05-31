package school.cesar.eta.unit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import school.cesar.eta.unit.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for PersonService with real implementations. Uses in-memory repository instead of mocks.
 */
@DisplayName("PersonService Integration Tests")
public class PersonServiceIntegrationTest {

    private PersonService service;
    private InMemoryPersonRepository repository;
    private TestEmailService emailService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPersonRepository();
        emailService = new TestEmailService();
        service = new PersonService(repository, emailService);
    }

    @Test
    @DisplayName("Should create person and send email in integrated flow")
    void createPerson_fullIntegration_success() {
        // Arrange
        Person person = new Person();
        person.setFirstName("Integration");
        person.setLastName("Test");
        person.setBirthday(LocalDate.of(1990, 5, 15));

        // Act
        Person created = service.createPerson(person);

        // Assert
        assertNotNull(created.getId());
        assertEquals(1, repository.count());
        assertEquals(1, emailService.getSentEmails().size());

        TestEmailService.SentEmail sentEmail = emailService.getSentEmails().get(0);
        assertEquals("integration.test@example.com", sentEmail.to);
        assertEquals("Integration Test", sentEmail.name);
        assertEquals("WELCOME", sentEmail.type);
    }

    @Test
    @DisplayName("Should handle complete CRUD operations")
    void crudOperations_fullFlow_success() {
        // Create
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        Person created = service.createPerson(person);
        Long id = created.getId();

        // Read
        Person found = service.findPerson(id);
        assertEquals("John", found.getFirstName());

        // Update
        Person updateData = new Person();
        updateData.setFirstName("Jane");
        Person updated = service.updatePerson(id, updateData);
        assertEquals("Jane", updated.getFirstName());
        assertEquals("Doe", updated.getLastName());

        // Delete
        service.deletePerson(id);
        assertThrows(PersonNotFoundException.class, () -> service.findPerson(id));
    }

    @Test
    @DisplayName("Should send birthday greetings to multiple people")
    void sendBirthdayGreetings_multipleBirthdays_sendsCorrectEmails() {
        // Arrange
        LocalDate today = LocalDate.now();

        Person person1 = createPersonWithBirthday("Alice", "Smith", today.minusYears(25));
        Person person2 = createPersonWithBirthday("Bob", "Jones", today.minusYears(30));
        Person person3 = createPersonWithBirthday("Charlie", "Brown", today.minusYears(20).minusDays(1));

        repository.save(person1);
        repository.save(person2);
        repository.save(person3);

        // Act
        int count = service.sendBirthdayGreetings();

        // Assert
        assertEquals(2, count);
        assertEquals(2, emailService.getBirthdayEmails().size());

        List<TestEmailService.SentEmail> birthdayEmails = emailService.getBirthdayEmails();
        assertTrue(birthdayEmails.stream().anyMatch(e -> e.name.equals("Alice Smith") && e.age == 25));
        assertTrue(birthdayEmails.stream().anyMatch(e -> e.name.equals("Bob Jones") && e.age == 30));
    }

    @Test
    @DisplayName("Should manage family relationships correctly")
    void familyManagement_complexRelationships_maintainsIntegrity() {
        // Arrange
        Person parent = createAndSavePerson("Parent", "Family");
        Person child1 = createAndSavePerson("Child1", "Family");
        Person child2 = createAndSavePerson("Child2", "Family");

        // Act
        service.addFamilyMember(parent.getId(), child1.getId());
        service.addFamilyMember(parent.getId(), child2.getId());
        service.addFamilyMember(child1.getId(), child2.getId());

        // Assert
        List<Person> parentFamily = service.findFamilyMembers(parent.getId());
        assertEquals(2, parentFamily.size());

        List<Person> child1Family = service.findFamilyMembers(child1.getId());
        assertEquals(2, child1Family.size());

        // Verify bidirectional relationships
        Person updatedParent = service.findPerson(parent.getId());
        Person updatedChild1 = service.findPerson(child1.getId());
        assertTrue(updatedParent.isFamily(updatedChild1));
        assertTrue(updatedChild1.isFamily(updatedParent));
    }

    @Test
    @DisplayName("Should calculate statistics correctly")
    void getStatistics_variousAges_correctCalculations() {
        // Arrange
        createAndSavePersonWithAge("Adult1", 25);
        createAndSavePersonWithAge("Adult2", 35);
        createAndSavePersonWithAge("Teen", 16);
        createAndSavePersonWithAge("Child", 10);
        createAndSavePerson("NoAge", "Person"); // No birthday

        // Act
        PersonService.PersonStatistics stats = service.getStatistics();

        // Assert
        assertEquals(5, stats.getTotalCount());
        assertEquals(2, stats.getAdultCount());
        // Average age calculation: (25+35+16+10+0)/5 = 86/5 = 17.2
        assertEquals(17.2, stats.getAverageAge(), 0.1);
    }

    // Helper methods
    private Person createPersonWithBirthday(String firstName, String lastName, LocalDate birthday) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setBirthday(birthday);
        return person;
    }

    private Person createAndSavePerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return repository.save(person);
    }

    private Person createAndSavePersonWithAge(String firstName, int age) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName("Test");
        person.setBirthday(LocalDate.now().minusYears(age));
        return repository.save(person);
    }

    /**
     * In-memory implementation of PersonRepository for testing.
     */
    private static class InMemoryPersonRepository implements PersonRepository {
        private final Map<Long, Person> storage = new HashMap<>();
        private final AtomicLong idGenerator = new AtomicLong(1);

        @Override
        public Person save(Person person) {
            if (person == null) {
                throw new IllegalArgumentException("Person cannot be null");
            }
            if (person.getId() == null) {
                person.setId(idGenerator.getAndIncrement());
            }
            storage.put(person.getId(), person);
            return person;
        }

        @Override
        public Optional<Person> findById(Long id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public List<Person> findByLastName(String lastName) {
            return storage.values().stream().filter(p -> lastName.equals(p.getLastName())).collect(ArrayList::new,
                    (list, p) -> list.add(p), ArrayList::addAll);
        }

        @Override
        public List<Person> findAll() {
            return new ArrayList<>(storage.values());
        }

        @Override
        public boolean deleteById(Long id) {
            return storage.remove(id) != null;
        }

        @Override
        public long count() {
            return storage.size();
        }

        @Override
        public boolean existsById(Long id) {
            return storage.containsKey(id);
        }

        @Override
        public List<Person> findBirthdayToday() {
            LocalDate today = LocalDate.now();
            return storage.values().stream().filter(p -> p.getBirthday() != null)
                    .filter(p -> p.getBirthday().getMonth() == today.getMonth()
                            && p.getBirthday().getDayOfMonth() == today.getDayOfMonth())
                    .collect(ArrayList::new, (list, p) -> list.add(p), ArrayList::addAll);
        }
    }

    /**
     * Test implementation of EmailService that records sent emails.
     */
    private static class TestEmailService implements EmailService {
        private final List<SentEmail> sentEmails = new ArrayList<>();

        @Override
        public void sendWelcomeEmail(String email, String name) {
            sentEmails.add(new SentEmail(email, name, "WELCOME", 0));
        }

        @Override
        public void sendBirthdayGreeting(String email, String name, int age) {
            sentEmails.add(new SentEmail(email, name, "BIRTHDAY", age));
        }

        @Override
        public void sendNotification(String email, String subject, String body) {
            sentEmails.add(new SentEmail(email, subject, "NOTIFICATION", 0));
        }

        public List<SentEmail> getSentEmails() {
            return new ArrayList<>(sentEmails);
        }

        public List<SentEmail> getBirthdayEmails() {
            return sentEmails.stream().filter(e -> "BIRTHDAY".equals(e.type)).collect(ArrayList::new,
                    (list, e) -> list.add(e), ArrayList::addAll);
        }

        static class SentEmail {
            final String to;
            final String name;
            final String type;
            final int age;

            SentEmail(String to, String name, String type, int age) {
                this.to = to;
                this.name = name;
                this.type = type;
                this.age = age;
            }
        }
    }
}
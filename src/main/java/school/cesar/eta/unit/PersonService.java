package school.cesar.eta.unit;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Person business logic. Demonstrates service layer testing with mocks.
 */
public class PersonService {

    private final PersonRepository repository;
    private final EmailService emailService;

    /**
     * Constructor with dependency injection.
     *
     * @param repository
     *            the person repository
     * @param emailService
     *            the email service
     */
    public PersonService(PersonRepository repository, EmailService emailService) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        if (emailService == null) {
            throw new IllegalArgumentException("Email service cannot be null");
        }
        this.repository = repository;
        this.emailService = emailService;
    }

    /**
     * Creates a new person with validation.
     *
     * @param person
     *            the person to create
     * @return the created person
     * @throws IllegalArgumentException
     *             if person is invalid
     */
    public Person createPerson(Person person) {
        validatePerson(person);

        Person saved = repository.save(person);

        // Send welcome email
        String email = generateEmail(person);
        emailService.sendWelcomeEmail(email, person.getName());

        return saved;
    }

    /**
     * Finds a person by ID.
     *
     * @param id
     *            the person ID
     * @return the person
     * @throws PersonNotFoundException
     *             if not found
     */
    public Person findPerson(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));
    }

    /**
     * Updates a person's information.
     *
     * @param id
     *            the person ID
     * @param updatedPerson
     *            the updated information
     * @return the updated person
     * @throws PersonNotFoundException
     *             if not found
     */
    public Person updatePerson(Long id, Person updatedPerson) {
        Person existing = findPerson(id);

        if (updatedPerson.getFirstName() != null) {
            existing.setFirstName(updatedPerson.getFirstName());
        }
        if (updatedPerson.getLastName() != null) {
            existing.setLastName(updatedPerson.getLastName());
        }
        if (updatedPerson.getBirthday() != null) {
            existing.setBirthday(updatedPerson.getBirthday());
        }

        return repository.save(existing);
    }

    /**
     * Deletes a person.
     *
     * @param id
     *            the person ID
     * @throws PersonNotFoundException
     *             if not found
     */
    public void deletePerson(Long id) {
        if (!repository.existsById(id)) {
            throw new PersonNotFoundException("Person not found with id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Finds all adults (18+ years old).
     *
     * @return list of adult persons
     */
    public List<Person> findAdults() {
        return repository.findAll().stream().filter(this::isAdult).collect(Collectors.toList());
    }

    /**
     * Sends birthday greetings to all persons with birthday today.
     *
     * @return number of greetings sent
     */
    public int sendBirthdayGreetings() {
        List<Person> birthdayPeople = repository.findBirthdayToday();

        for (Person person : birthdayPeople) {
            String email = generateEmail(person);
            int age = calculateAge(person);
            emailService.sendBirthdayGreeting(email, person.getName(), age);
        }

        return birthdayPeople.size();
    }

    /**
     * Finds family members of a person.
     *
     * @param personId
     *            the person ID
     * @return list of family members
     */
    public List<Person> findFamilyMembers(Long personId) {
        Person person = findPerson(personId);
        return person.getFamily();
    }

    /**
     * Adds a family member relationship.
     *
     * @param personId
     *            the person ID
     * @param familyMemberId
     *            the family member ID
     */
    public void addFamilyMember(Long personId, Long familyMemberId) {
        if (personId.equals(familyMemberId)) {
            throw new IllegalArgumentException("Cannot add self as family member");
        }

        Person person = findPerson(personId);
        Person familyMember = findPerson(familyMemberId);

        person.addToFamily(familyMember);

        repository.save(person);
        repository.save(familyMember);
    }

    /**
     * Gets statistics about persons in repository.
     *
     * @return statistics object
     */
    public PersonStatistics getStatistics() {
        List<Person> allPersons = repository.findAll();

        long totalCount = allPersons.size();
        long adultCount = allPersons.stream().filter(this::isAdult).count();
        double averageAge = allPersons.stream().mapToInt(this::calculateAge).average().orElse(0.0);

        return new PersonStatistics(totalCount, adultCount, averageAge);
    }

    private void validatePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (person.getFirstName() == null && person.getLastName() == null) {
            throw new IllegalArgumentException("Person must have at least one name");
        }
    }

    private String generateEmail(Person person) {
        String firstName = person.getFirstName() != null ? person.getFirstName().toLowerCase() : "";
        String lastName = person.getLastName() != null ? person.getLastName().toLowerCase() : "";

        if (!firstName.isEmpty() && !lastName.isEmpty()) {
            return firstName + "." + lastName + "@example.com";
        } else if (!firstName.isEmpty()) {
            return firstName + "@example.com";
        } else {
            return lastName + "@example.com";
        }
    }

    private boolean isAdult(Person person) {
        if (person.getBirthday() == null) {
            return false;
        }
        return calculateAge(person) >= 18;
    }

    private int calculateAge(Person person) {
        if (person.getBirthday() == null) {
            return 0;
        }
        return Period.between(person.getBirthday(), LocalDate.now()).getYears();
    }

    /**
     * Statistics class for persons.
     */
    public static class PersonStatistics {
        private final long totalCount;
        private final long adultCount;
        private final double averageAge;

        public PersonStatistics(long totalCount, long adultCount, double averageAge) {
            this.totalCount = totalCount;
            this.adultCount = adultCount;
            this.averageAge = averageAge;
        }

        public long getTotalCount() {
            return totalCount;
        }

        public long getAdultCount() {
            return adultCount;
        }

        public double getAverageAge() {
            return averageAge;
        }
    }
}
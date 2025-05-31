package school.cesar.eta.unit;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Person entities. Demonstrates repository pattern for testing with mocks.
 */
public interface PersonRepository {

    /**
     * Saves a person to the repository.
     *
     * @param person
     *            the person to save
     * @return the saved person with generated ID
     * @throws IllegalArgumentException
     *             if person is null
     */
    Person save(Person person);

    /**
     * Finds a person by ID.
     *
     * @param id
     *            the person ID
     * @return Optional containing the person if found
     */
    Optional<Person> findById(Long id);

    /**
     * Finds all persons by last name.
     *
     * @param lastName
     *            the last name to search
     * @return list of persons with matching last name
     */
    List<Person> findByLastName(String lastName);

    /**
     * Finds all persons.
     *
     * @return list of all persons
     */
    List<Person> findAll();

    /**
     * Deletes a person by ID.
     *
     * @param id
     *            the person ID
     * @return true if deleted, false if not found
     */
    boolean deleteById(Long id);

    /**
     * Counts total number of persons.
     *
     * @return total count
     */
    long count();

    /**
     * Checks if a person exists by ID.
     *
     * @param id
     *            the person ID
     * @return true if exists
     */
    boolean existsById(Long id);

    /**
     * Finds persons whose birthday is today.
     *
     * @return list of persons with birthday today
     */
    List<Person> findBirthdayToday();
}
package school.cesar.eta.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonTest {
    private Person person;
    private Person firstPerson;
    private Person secondPerson;

    @Spy
    private List<Person> family;

    @BeforeEach
    public void setup() {
        person = new Person() {
            @Override
            public LocalDate getNow() {
                return LocalDate.of(2020, 8, 7);
            }
        };

        firstPerson = spy(new Person());
        secondPerson = spy(new Person());
    }

    private LocalDate createBirthday(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    @Test
    public void getName_firstNameJonLastNameSnow_jonSnow() {
        person.setFirstName("Jon");
        person.setLastName("Snow");
        Assertions.assertEquals("Jon Snow",person.getName());
    }

    @Test
    public void getName_firstNameJonNoLastName_jon() {
        person.setFirstName("Jon");
        Assertions.assertEquals("Jon", person.getName());
    }

    @Test
    public void getName_noFirstNameLastNameSnow_snow() {
        person.setLastName("Snow");
        Assertions.assertEquals("Snow",person.getName());
    }

    @Test
    public void getName_noFirstNameNorLastName_throwsException() {
        Assertions.assertThrows(IllegalStateException.class, () -> person.getName(), "Name must be filled");
    }

    @Test
    public void isBirthdayToday_differentMonthAndDay_false() {
        int year = 1991;
        int month = 7;
        int dayOfMonth = 19;

        LocalDate localDate = createBirthday(year, month, dayOfMonth);
        person.setBirthday(localDate);
        boolean actualResult = person.isBirthdayToday();
        Assertions.assertFalse(actualResult);
    }

    @Test
    public void isBirthdayToday_sameMonthDifferentDay_false() {
        int year = 1991;
        int month = 8;
        int dayOfMonth = 6;

        LocalDate localDate = createBirthday(year, month, dayOfMonth);
        person.setBirthday(localDate);
        boolean actualResult = person.isBirthdayToday();
        Assertions.assertFalse(actualResult);
    }

    @Test
    public void isBirthdayToday_sameMonthAndDay_true() {
        int year = 1991;
        int month = 8;
        int dayOfMonth = 7;

        LocalDate localDate = createBirthday(year, month, dayOfMonth);
        person.setBirthday(localDate);
        boolean actualResult = person.isBirthdayToday();
        Assertions.assertTrue(actualResult);
    }

    @Test
    public void addToFamily_somePerson_familyHasNewMember() {
        Person person1 = new Person();
        Person person2 = new Person();
        person1.addToFamily(person2);
        Assertions.assertTrue(person1.isFamily(person2));
        Assertions.assertTrue(person2.isFamily(person1));
    }

    @Test
    public void addToFamily_somePerson_personAddedAlsoHasItsFamilyUpdated() {
        Person person1 = new Person();
        Person person2 = new Person();
        
        person1.addToFamily(person2);
        
        Assertions.assertTrue(person1.isFamily(person2));
        Assertions.assertTrue(person2.isFamily(person1));
    }

    @Test
    public void isFamily_nonRelativePerson_false() {
        Person person1 = new Person();
        Person person2 = new Person();
        Assertions.assertFalse(person1.isFamily(person2));
    }

    @Test
    public void isFamily_relativePerson_true() {
        Person person1 = new Person();
        Person person2 = new Person();
        person1.addToFamily(person2);
        Assertions.assertTrue(person1.isFamily(person2));
    }

    @Test
    public void setFirstName_null_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> person.setFirstName(null));
    }

    @Test
    public void setFirstName_empty_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> person.setFirstName("   "));
    }

    @Test
    public void setLastName_null_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> person.setLastName(null));
    }

    @Test
    public void setLastName_empty_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> person.setLastName(""));
    }

    @Test
    public void setBirthday_null_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> person.setBirthday(null));
    }

    @Test
    public void addToFamily_nullPerson_noExceptionAndNotAdded() {
        person.addToFamily(null);
        Assertions.assertTrue(person.getFamily().isEmpty());
    }

    @Test
    public void addToFamily_selfReference_noExceptionAndNotAdded() {
        person.addToFamily(person);
        Assertions.assertTrue(person.getFamily().isEmpty());
    }

    @Test
    public void addToFamily_duplicatePerson_notAddedTwice() {
        Person relative = new Person();
        person.addToFamily(relative);
        person.addToFamily(relative);
        Assertions.assertEquals(1, person.getFamily().size());
    }
}

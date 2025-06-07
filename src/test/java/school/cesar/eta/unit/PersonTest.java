package school.cesar.eta.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PersonTest {

    private Person person;

    @BeforeEach
    public void setupTest() {
        person = new Person() {
            @Override
            public LocalDate getNow() {
                int year = 2020;
                int month = 8;
                int dayOfMonth = 7;
                return LocalDate.of(year, month, dayOfMonth);
            }
        };
    }

    @Test
    public void getName_firstNameJonLastNameSnow_jonSnow() {
        person.setFirstName("Jon");
        person.setLastName("Snow");
        Assertions.assertEquals("Jon Snow", person.getName());
    }

    @Test
    public void getName_firstNameJonNoLastName_jon() {
        person.setFirstName("Jon");
        Assertions.assertEquals("Jon", person.getName());
    }

    @Test
    public void getName_noFirstNameLastNameSnow_snow() {
        person.setLastName("Snow");
        Assertions.assertEquals("Snow", person.getName());
    }

    @Test
    public void getName_noFirstNameNorLastName_throwsException() {
        Assertions.assertThrows(IllegalStateException.class, () -> person.getName());
    }

    @Test
    public void isBirthdayToday_differentMonth_false() {
        person.setBirthday(LocalDate.of(1990, 7, 7));
        Assertions.assertFalse(person.isBirthdayToday());
    }

    @Test
    public void isBirthdayToday_sameMonthDifferentDay_false() {
        person.setBirthday(LocalDate.of(1990, 8, 8));
        Assertions.assertFalse(person.isBirthdayToday());
    }

    @Test
    public void isBirthdayToday_sameMonthSameDay_true() {
        person.setBirthday(LocalDate.of(1990, 8, 7));
        Assertions.assertTrue(person.isBirthdayToday());
    }

    @Test
    public void isBirthdayToday_nullBirthday_false() {
        // Não definimos birthday, então permanece null
        Assertions.assertFalse(person.isBirthdayToday());
    }

    @Test
    public void getAge_bornInLeapYearBeforeBirthday_27() {
        person.setBirthday(LocalDate.of(1992, 9, 21));
        Assertions.assertEquals(27, person.getAge());
    }

    @Test
    public void getAge_bornInLeapYearAfterBirthday_28() {
        person.setBirthday(LocalDate.of(1992, 7, 21));
        Assertions.assertEquals(28, person.getAge());
    }

    @Test
    public void getAge_bornInNonLeapYearBeforeBirthday_26() {
        person.setBirthday(LocalDate.of(1993, 9, 21));
        Assertions.assertEquals(26, person.getAge());
    }

    @Test
    public void getAge_bornInNonLeapYearAfterBirthday_27() {
        person.setBirthday(LocalDate.of(1993, 7, 21));
        Assertions.assertEquals(27, person.getAge());
    }

    @Test
    public void getAge_bornToday_0() {
        person.setBirthday(LocalDate.of(2020, 8, 7));
        Assertions.assertEquals(0, person.getAge());
    }

    @Test
    public void addToFamily_somePerson_familyHasNewMember() {
        Person newFamilyMember = new Person();
        person.addToFamily(newFamilyMember);
        Assertions.assertTrue(person.isFamily(newFamilyMember));
    }

    @Test
    public void addToFamily_somePerson_personAddedAlsoHasItsFamilyUpdated() {
        Person newFamilyMember = new Person();
        person.addToFamily(newFamilyMember);
        Assertions.assertTrue(newFamilyMember.isFamily(person));
    }

    @Test
    public void isFamily_nonRelativePerson_false() {
        Person nonRelative = new Person();
        Assertions.assertFalse(person.isFamily(nonRelative));
    }

    @Test
    public void isFamily_relativePerson_true() {
        Person relative = new Person();
        person.addToFamily(relative);
        Assertions.assertTrue(person.isFamily(relative));
    }

    @Test
    public void isFamily_samePerson_false() {
        // Uma pessoa não pode ser família de si mesma
        Assertions.assertFalse(person.isFamily(person));
    }

    @Test
    public void isFamily_nullPerson_false() {
        Assertions.assertFalse(person.isFamily(null));
    }

    @Test
    public void addToFamily_samePerson_doesNotAddItself() {
        int initialFamilySize = person.getFamily().size();
        person.addToFamily(person);
        Assertions.assertEquals(initialFamilySize, person.getFamily().size());
    }

    @Test
    public void addToFamily_nullPerson_doesNotAddNull() {
        int initialFamilySize = person.getFamily().size();
        person.addToFamily(null);
        Assertions.assertEquals(initialFamilySize, person.getFamily().size());
    }
}

package school.cesar.eta.unit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private String firstName = null;
    private String lastName = null;
    private LocalDate birthday;
    private List<Person> family = new ArrayList<>();

    /**
     * Retorna a data atual. Pode ser sobrescrito para facilitar testes.
     *
     * @return data atual
     */
    public LocalDate getNow() {
        return LocalDate.now();
    }

    /**
     * Retorna o primeiro nome da pessoa.
     *
     * @return primeiro nome
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Define o primeiro nome da pessoa.
     *
     * @param firstName
     *            primeiro nome não nulo e não vazio
     *
     * @throws IllegalArgumentException
     *             se o nome for nulo ou vazio
     */
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        this.firstName = firstName;
    }

    /**
     * Retorna o sobrenome da pessoa.
     *
     * @return sobrenome
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Define o sobrenome da pessoa.
     *
     * @param lastName
     *            sobrenome não nulo e não vazio
     *
     * @throws IllegalArgumentException
     *             se o sobrenome for nulo ou vazio
     */
    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.lastName = lastName;
    }

    /**
     * Retorna a data de nascimento da pessoa.
     *
     * @return data de nascimento
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Define a data de nascimento da pessoa.
     *
     * @param birthday
     *            data de nascimento não nula
     *
     * @throws IllegalArgumentException
     *             se a data for nula
     */
    public void setBirthday(LocalDate birthday) {
        if (birthday == null) {
            throw new IllegalArgumentException("Birthday cannot be null");
        }
        this.birthday = birthday;
    }

    /**
     * Retorna o nome completo da pessoa.
     *
     * @return nome completo ou parcial
     *
     * @throws IllegalStateException
     *             se ambos os nomes forem nulos
     */
    public String getName() {
        if (this.firstName == null && this.lastName == null) {
            throw new IllegalStateException("Name must be filled");
        }
        if (this.firstName != null && this.lastName != null) {
            return this.firstName + " " + this.lastName;
        } else if (this.firstName != null) {
            return this.firstName;
        }
        return lastName;
    }

    /**
     * Verifica se hoje é o aniversário da pessoa.
     *
     * @return true se hoje for o aniversário, false caso contrário
     */
    public boolean isBirthdayToday() {
        if (this.birthday == null) {
            return false;
        }
        LocalDate now = getNow();
        return now.getDayOfMonth() == this.birthday.getDayOfMonth() && now.getMonth() == this.birthday.getMonth();
    }

    /**
     * Adiciona uma pessoa à família, garantindo bidirecionalidade e evitando duplicidade ou auto-referência.
     *
     * @param person
     *            pessoa a ser adicionada
     */
    public void addToFamily(Person person) {
        if (person == null || person == this || this.family.contains(person)) {
            return;
        }
        this.family.add(person);
        if (!person.family.contains(this)) {
            person.addToFamily(this);
        }
    }

    /**
     * Verifica se a pessoa informada faz parte da família.
     *
     * @param person
     *            pessoa a ser verificada
     *
     * @return true se for da família, false caso contrário
     */
    public boolean isFamily(Person person) {
        return this.family.contains(person);
    }

    /**
     * Retorna uma cópia da lista de familiares.
     *
     * @return lista de familiares
     */
    public List<Person> getFamily() {
        return new ArrayList<>(family);
    }
}

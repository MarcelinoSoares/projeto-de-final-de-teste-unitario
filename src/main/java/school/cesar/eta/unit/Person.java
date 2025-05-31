package school.cesar.eta.unit;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a person with personal information and family relationships.
 *
 * <p>
 * This class provides functionality to manage person data including:
 * <ul>
 * <li>Personal information (name, birthday, address)</li>
 * <li>Age calculation and adult status verification</li>
 * <li>Family relationship management</li>
 * <li>Birthday verification</li>
 * </ul>
 *
 * <p>
 * <b>Example usage:</b>
 *
 * <pre>{@code
 * Person person = new Person();
 * person.setFirstName("John");
 * person.setLastName("Doe");
 * person.setBirthday(LocalDate.of(1990, 5, 15));
 *
 * // Check if adult
 * if (person.isAdult()) {
 *     System.out.println(person.getName() + " is an adult");
 * }
 *
 * // Add family member
 * Person spouse = new Person();
 * spouse.setFirstName("Jane");
 * spouse.setLastName("Doe");
 * person.addToFamily(spouse);
 * }</pre>
 *
 * @author CESAR School
 * @version 2019.2-Recife
 * @since 1.0
 */
public class Person {
    private Long id;
    private String firstName = null;
    private String lastName = null;
    private LocalDate birthday;
    private Address address;
    private String cpf;
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
     * Retorna o ID da pessoa.
     *
     * @return ID da pessoa
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o ID da pessoa.
     *
     * @param id
     *            ID da pessoa
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna o CPF da pessoa.
     *
     * @return CPF formatado ou null se não definido
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF da pessoa.
     *
     * <p>
     * O CPF é validado usando {@link CpfValidator}. Aceita CPF com ou sem formatação. O CPF é armazenado no formato
     * padrão XXX.XXX.XXX-XX.
     *
     * @param cpf
     *            CPF a ser definido
     * @throws IllegalArgumentException
     *             se o CPF for inválido
     */
    public void setCpf(String cpf) {
        if (cpf != null && !CpfValidator.isValid(cpf)) {
            throw new IllegalArgumentException("Invalid CPF: " + cpf);
        }
        this.cpf = cpf != null ? CpfValidator.format(cpf) : null;
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
     *             se a data for nula ou no futuro
     */
    public void setBirthday(LocalDate birthday) {
        if (birthday == null) {
            throw new IllegalArgumentException("Birthday cannot be null");
        }
        if (birthday.isAfter(getNow())) {
            throw new IllegalArgumentException("Birthday cannot be in the future");
        }
        this.birthday = birthday;
    }

    /**
     * Retorna o endereço da pessoa.
     *
     * @return endereço
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Define o endereço da pessoa.
     *
     * @param address
     *            endereço da pessoa
     */
    public void setAddress(Address address) {
        this.address = address;
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
     * Calcula a idade da pessoa.
     *
     * @return idade em anos ou null se não tiver data de nascimento
     */
    public Integer getAge() {
        if (this.birthday == null) {
            return null;
        }
        return Period.between(this.birthday, getNow()).getYears();
    }

    /**
     * Calcula a idade da pessoa em meses.
     *
     * <p>
     * Este método calcula o número total de meses desde o nascimento até hoje. Por exemplo, uma pessoa com 2 anos e 3
     * meses terá 27 meses de idade.
     *
     * @return idade em meses ou null se não tiver data de nascimento
     */
    public Integer getAgeInMonths() {
        if (this.birthday == null) {
            return null;
        }
        Period period = Period.between(this.birthday, getNow());
        return period.getYears() * 12 + period.getMonths();
    }

    /**
     * Calcula a idade da pessoa em dias.
     *
     * <p>
     * Este método calcula o número total de dias desde o nascimento até hoje. Leva em consideração anos bissextos e o
     * número exato de dias em cada mês.
     *
     * @return idade em dias ou null se não tiver data de nascimento
     */
    public Long getAgeInDays() {
        if (this.birthday == null) {
            return null;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(this.birthday, getNow());
    }

    /**
     * Verifica se a pessoa é adulta (18 anos ou mais).
     *
     * @return true se for adulta, false caso contrário
     */
    public boolean isAdult() {
        Integer age = getAge();
        return age != null && age >= 18;
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
     * <p>
     * Este método estabelece uma relação bidirecional entre duas pessoas. Quando A adiciona B como família, B
     * automaticamente tem A como família também. O método previne:
     * <ul>
     * <li>Auto-referências (uma pessoa não pode ser família de si mesma)</li>
     * <li>Duplicatas (a mesma pessoa não é adicionada múltiplas vezes)</li>
     * <li>Referências nulas</li>
     * </ul>
     *
     * <p>
     * <b>Exemplo:</b>
     *
     * <pre>{@code
     * Person parent = new Person();
     * Person child = new Person();
     * parent.addToFamily(child);
     *
     * // Ambos agora são família um do outro
     * assert parent.isFamily(child); // true
     * assert child.isFamily(parent); // true
     * }</pre>
     *
     * @param person
     *            pessoa a ser adicionada como família
     */
    public void addToFamily(Person person) {
        if (person == null || person == this || this.family.contains(person)) {
            return;
        }
        this.family.add(person);
        if (!person.family.contains(this)) {
            person.family.add(this);
        }
    }

    /**
     * Verifica se a pessoa informada faz parte da família.
     *
     * <p>
     * Este método verifica apenas relações diretas de família. Não verifica relações transitivas. Por exemplo, se A é
     * família de B e B é família de C, este método retornará false para A.isFamily(C) a menos que A e C tenham sido
     * explicitamente adicionados como família.
     *
     * @param person
     *            pessoa a ser verificada
     *
     * @return true se a pessoa está na lista de família, false caso contrário
     */
    public boolean isFamily(Person person) {
        return this.family.contains(person);
    }

    /**
     * Retorna uma cópia da lista de familiares.
     *
     * <p>
     * Este método retorna uma nova lista contendo todos os familiares. Modificações na lista retornada não afetam a
     * lista interna de familiares da pessoa.
     *
     * @return lista imutável de familiares
     */
    public List<Person> getFamily() {
        return new ArrayList<>(family);
    }

    /**
     * Verifica se a pessoa mora na mesma cidade que outra.
     *
     * <p>
     * Para que duas pessoas sejam consideradas como morando na mesma cidade, ambas devem ter endereços definidos e os
     * endereços devem ter a mesma cidade e estado.
     *
     * <p>
     * <b>Exemplo:</b>
     *
     * <pre>{@code
     * Person person1 = new Person();
     * person1.setAddress(
     *         new Address.Builder().city("Recife").state("PE").street("Rua A").number("1").zipCode("50000-000").build());
     *
     * Person person2 = new Person();
     * person2.setAddress(
     *         new Address.Builder().city("Recife").state("PE").street("Rua B").number("2").zipCode("51000-000").build());
     *
     * assert person1.livesInSameCity(person2); // true
     * }</pre>
     *
     * @param other
     *            outra pessoa para comparação
     *
     * @return true se moram na mesma cidade e estado, false caso contrário ou se algum endereço for null
     */
    public boolean livesInSameCity(Person other) {
        if (other == null || this.address == null || other.address == null) {
            return false;
        }
        return this.address.isSameCity(other.address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName)
                && Objects.equals(lastName, person.lastName) && Objects.equals(birthday, person.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthday);
    }

    @Override
    public String toString() {
        String nameStr;
        try {
            nameStr = getName();
        } catch (IllegalStateException e) {
            nameStr = "No Name";
        }

        return "Person{" + "id=" + id + ", name='" + nameStr + '\'' + ", age=" + getAge() + ", birthday=" + birthday
                + '}';
    }
}

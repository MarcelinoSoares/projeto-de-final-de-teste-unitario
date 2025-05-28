# 🚀 Técnicas Avançadas de Teste

Este documento apresenta técnicas avançadas de teste que podem ser aplicadas para melhorar ainda mais a qualidade dos testes unitários.

## 📋 Índice

- [Testes Parametrizados](#testes-parametrizados)
- [Testes de Propriedades](#testes-de-propriedades)
- [Testes de Mutação](#testes-de-mutação)
- [Testes de Performance](#testes-de-performance)
- [Técnicas de Isolamento](#técnicas-de-isolamento)
- [Padrões Avançados](#padrões-avançados)

## 🔄 Testes Parametrizados

### O que são?
Testes parametrizados permitem executar o mesmo teste com diferentes conjuntos de dados, reduzindo duplicação de código.

### Exemplo com JUnit 5

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

public class PersonParameterizedTest {
    
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    @DisplayName("Should throw exception for empty/blank first names")
    void setFirstName_blankStrings_throwsException(String invalidName) {
        // Arrange
        Person person = new Person();
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> person.setFirstName(invalidName));
    }
    
    @ParameterizedTest
    @CsvSource({
        "Jon, Snow, Jon Snow",
        "Arya, Stark, Arya Stark",
        "Tyrion, Lannister, Tyrion Lannister"
    })
    @DisplayName("Should return full name for different name combinations")
    void getName_validNames_returnsFullName(String firstName, String lastName, String expected) {
        // Arrange
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        
        // Act
        String actual = person.getName();
        
        // Assert
        assertEquals(expected, actual);
    }
    
    @ParameterizedTest
    @MethodSource("provideBirthdayScenarios")
    @DisplayName("Should correctly identify birthdays")
    void isBirthdayToday_variousScenarios_correctResult(
            LocalDate today, LocalDate birthday, boolean expected) {
        // Arrange
        Person person = new Person() {
            @Override
            public LocalDate getNow() {
                return today;
            }
        };
        person.setBirthday(birthday);
        
        // Act
        boolean actual = person.isBirthdayToday();
        
        // Assert
        assertEquals(expected, actual);
    }
    
    static Stream<Arguments> provideBirthdayScenarios() {
        return Stream.of(
            Arguments.of(LocalDate.of(2020, 8, 7), LocalDate.of(1990, 8, 7), true),
            Arguments.of(LocalDate.of(2020, 8, 7), LocalDate.of(1990, 8, 6), false),
            Arguments.of(LocalDate.of(2020, 8, 7), LocalDate.of(1990, 7, 7), false),
            Arguments.of(LocalDate.of(2020, 2, 29), LocalDate.of(1992, 2, 29), true)
        );
    }
}
```

## 🎲 Testes de Propriedades

### Property-Based Testing
Testa propriedades que devem ser verdadeiras para qualquer entrada válida.

```java
@Test
@DisplayName("Adding person to family should always be bidirectional")
void addToFamily_anyValidPerson_alwaysBidirectional() {
    // Property: If A is family of B, then B is family of A
    for (int i = 0; i < 100; i++) {
        // Arrange
        Person personA = createRandomPerson();
        Person personB = createRandomPerson();
        
        // Act
        personA.addToFamily(personB);
        
        // Assert - Bidirectional property
        assertTrue(personA.isFamily(personB));
        assertTrue(personB.isFamily(personA));
    }
}

@Test
@DisplayName("Family relationship should be symmetric")
void isFamily_relationship_symmetric() {
    // Property: isFamily(A, B) == isFamily(B, A)
    Person p1 = new Person();
    Person p2 = new Person();
    Person p3 = new Person();
    
    p1.addToFamily(p2);
    p2.addToFamily(p3);
    
    // Symmetry check
    assertEquals(p1.isFamily(p2), p2.isFamily(p1));
    assertEquals(p2.isFamily(p3), p3.isFamily(p2));
    assertEquals(p1.isFamily(p3), p3.isFamily(p1));
}
```

## 🧬 Testes de Mutação

### O que são?
Testes de mutação modificam o código de produção para verificar se os testes detectam as mudanças.

### Exemplo com PIT (Pitest)

```xml
<!-- Adicionar ao pom.xml -->
<plugin>
    <groupId>org.pitest</groupId>
    <artifactId>pitest-maven</artifactId>
    <version>1.14.2</version>
    <configuration>
        <targetClasses>
            <param>school.cesar.eta.unit.*</param>
        </targetClasses>
        <targetTests>
            <param>school.cesar.eta.unit.*</param>
        </targetTests>
    </configuration>
</plugin>
```

### Executar testes de mutação:
```bash
mvn org.pitest:pitest-maven:mutationCoverage
```

## ⚡ Testes de Performance

### Medindo tempo de execução

```java
@Test
@DisplayName("Should handle large families efficiently")
void addToFamily_largeFamily_performsWell() {
    // Arrange
    Person person = new Person();
    List<Person> relatives = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
        relatives.add(new Person());
    }
    
    // Act & Measure
    long startTime = System.nanoTime();
    for (Person relative : relatives) {
        person.addToFamily(relative);
    }
    long endTime = System.nanoTime();
    
    // Assert
    long duration = (endTime - startTime) / 1_000_000; // Convert to ms
    assertTrue(duration < 100, "Adding 1000 family members should take less than 100ms");
    assertEquals(1000, person.getFamily().size());
}

@Test
@Timeout(value = 2, unit = TimeUnit.SECONDS)
@DisplayName("Should complete within timeout")
void complexOperation_shouldCompleteQuickly() {
    // Test that must complete within 2 seconds
}
```

## 🔒 Técnicas de Isolamento

### Usando Test Fixtures

```java
public class PersonTestFixtures {
    
    public static Person createPersonWithBirthday(int year, int month, int day) {
        Person person = new Person();
        person.setBirthday(LocalDate.of(year, month, day));
        return person;
    }
    
    public static Person createPersonWithFullName(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return person;
    }
    
    public static List<Person> createFamily(int size) {
        List<Person> family = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Person person = new Person();
            person.setFirstName("Person" + i);
            person.setLastName("Test");
            family.add(person);
        }
        return family;
    }
}
```

### Builders para Testes

```java
public class PersonTestBuilder {
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private List<Person> family = new ArrayList<>();
    
    public PersonTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    
    public PersonTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    
    public PersonTestBuilder withBirthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }
    
    public PersonTestBuilder withFamily(Person... relatives) {
        this.family.addAll(Arrays.asList(relatives));
        return this;
    }
    
    public Person build() {
        Person person = new Person();
        if (firstName != null) person.setFirstName(firstName);
        if (lastName != null) person.setLastName(lastName);
        if (birthday != null) person.setBirthday(birthday);
        for (Person relative : family) {
            person.addToFamily(relative);
        }
        return person;
    }
}

// Uso:
@Test
void testWithBuilder() {
    Person person = new PersonTestBuilder()
        .withFirstName("Jon")
        .withLastName("Snow")
        .withBirthday(LocalDate.of(1990, 1, 1))
        .build();
    
    assertEquals("Jon Snow", person.getName());
}
```

## 🎯 Padrões Avançados

### Object Mother Pattern

```java
public class PersonMother {
    
    public static Person simple() {
        return new PersonTestBuilder()
            .withFirstName("John")
            .withLastName("Doe")
            .build();
    }
    
    public static Person withBirthdayToday() {
        return new PersonTestBuilder()
            .withFirstName("Birthday")
            .withLastName("Person")
            .withBirthday(LocalDate.now())
            .build();
    }
    
    public static Person withLargeFamily() {
        PersonTestBuilder builder = new PersonTestBuilder()
            .withFirstName("Patriarch")
            .withLastName("Family");
        
        for (int i = 0; i < 10; i++) {
            builder.withFamily(simple());
        }
        
        return builder.build();
    }
}
```

### Custom Assertions

```java
public class PersonAssert {
    private final Person actual;
    
    private PersonAssert(Person actual) {
        this.actual = actual;
    }
    
    public static PersonAssert assertThat(Person actual) {
        return new PersonAssert(actual);
    }
    
    public PersonAssert hasFullName(String expectedFullName) {
        assertEquals(expectedFullName, actual.getName());
        return this;
    }
    
    public PersonAssert hasFirstName(String expectedFirstName) {
        assertEquals(expectedFirstName, actual.getFirstName());
        return this;
    }
    
    public PersonAssert hasFamilySize(int expectedSize) {
        assertEquals(expectedSize, actual.getFamily().size());
        return this;
    }
    
    public PersonAssert hasBirthdayToday() {
        assertTrue(actual.isBirthdayToday(), 
            "Expected person to have birthday today");
        return this;
    }
}

// Uso:
@Test
void testWithCustomAssertions() {
    Person person = new Person();
    person.setFirstName("Jon");
    person.setLastName("Snow");
    
    PersonAssert.assertThat(person)
        .hasFirstName("Jon")
        .hasFullName("Jon Snow")
        .hasFamilySize(0);
}
```

### Testes de Contrato

```java
public abstract class PersonContract {
    
    protected abstract Person createPerson();
    
    @Test
    void shouldNotAcceptNullFirstName() {
        Person person = createPerson();
        assertThrows(IllegalArgumentException.class, 
            () -> person.setFirstName(null));
    }
    
    @Test
    void shouldMaintainBidirectionalFamily() {
        Person person1 = createPerson();
        Person person2 = createPerson();
        
        person1.addToFamily(person2);
        
        assertTrue(person1.isFamily(person2));
        assertTrue(person2.isFamily(person1));
    }
}

// Implementação específica
public class StandardPersonContractTest extends PersonContract {
    @Override
    protected Person createPerson() {
        return new Person();
    }
}
```

## 📊 Análise de Cobertura Avançada

### Cobertura de Branches

```java
@Test
@DisplayName("Should cover all branches in getName()")
void getName_allBranches_covered() {
    Person person = new Person();
    
    // Branch 1: Both names null
    assertThrows(IllegalStateException.class, () -> person.getName());
    
    // Branch 2: Only first name
    person.setFirstName("Jon");
    assertEquals("Jon", person.getName());
    
    // Branch 3: Only last name
    person = new Person();
    person.setLastName("Snow");
    assertEquals("Snow", person.getName());
    
    // Branch 4: Both names present
    person.setFirstName("Jon");
    assertEquals("Jon Snow", person.getName());
}
```

### Cobertura de Condições

```java
@Test
@DisplayName("Should test all conditions in isBirthdayToday()")
void isBirthdayToday_allConditions_tested() {
    Person person = new Person() {
        @Override
        public LocalDate getNow() {
            return LocalDate.of(2020, 8, 7);
        }
    };
    
    // Condition 1: birthday is null
    assertFalse(person.isBirthdayToday());
    
    // Condition 2: different month
    person.setBirthday(LocalDate.of(1990, 7, 7));
    assertFalse(person.isBirthdayToday());
    
    // Condition 3: different day
    person.setBirthday(LocalDate.of(1990, 8, 6));
    assertFalse(person.isBirthdayToday());
    
    // Condition 4: same month and day
    person.setBirthday(LocalDate.of(1990, 8, 7));
    assertTrue(person.isBirthdayToday());
}
```

## 🎓 Conclusão

Estas técnicas avançadas ajudam a criar testes mais robustos, mantíveis e expressivos. Lembre-se:

1. **Use testes parametrizados** para reduzir duplicação
2. **Teste propriedades** além de casos específicos
3. **Meça performance** quando relevante
4. **Crie abstrações** para facilitar manutenção
5. **Busque 100% de cobertura** de branches e condições

---

<div align="center">
  <p><strong>Próximo Nível:</strong> Explore ferramentas como AssertJ, Testcontainers e WireMock!</p>
  <p>Continue aprendendo e melhorando seus testes! 🚀</p>
</div> 
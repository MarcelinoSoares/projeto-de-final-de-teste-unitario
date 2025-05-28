# 🧪 Guia de Testes Unitários

Este guia fornece informações detalhadas sobre como escrever testes unitários eficazes para o projeto ETA.

## 📋 Índice

- [Conceitos Fundamentais](#conceitos-fundamentais)
- [Anatomia de um Teste](#anatomia-de-um-teste)
- [JUnit 5 - Recursos Essenciais](#junit-5---recursos-essenciais)
- [Mockito - Mocks e Spies](#mockito---mocks-e-spies)
- [Padrões e Convenções](#padrões-e-convenções)
- [Casos de Teste do Projeto](#casos-de-teste-do-projeto)
- [Dicas e Truques](#dicas-e-truques)
- [Erros Comuns](#erros-comuns)

## 🎯 Conceitos Fundamentais

### O que é um Teste Unitário?
Um teste unitário é um código que verifica se uma unidade específica do código (geralmente um método) funciona corretamente de forma isolada.

### Características de Bons Testes
- **Rápidos**: Executam em milissegundos
- **Independentes**: Não dependem de outros testes
- **Repetíveis**: Sempre produzem o mesmo resultado
- **Auto-verificáveis**: Passam ou falham sem intervenção manual
- **Pontuais**: Escritos próximo ao código que testam

### Princípio FIRST
- **F**ast (Rápido)
- **I**ndependent (Independente)
- **R**epeatable (Repetível)
- **S**elf-validating (Auto-validável)
- **T**imely (Pontual)

## 🏗 Anatomia de um Teste

### Padrão AAA (Arrange-Act-Assert)

```java
@Test
public void metodoDeTeste() {
    // Arrange (Preparar)
    // Configure os objetos e dados necessários
    Person person = new Person();
    person.setFirstName("Maria");
    person.setLastName("Silva");
    
    // Act (Agir)
    // Execute a ação que você quer testar
    String nomeCompleto = person.getName();
    
    // Assert (Afirmar)
    // Verifique se o resultado é o esperado
    assertEquals("Maria Silva", nomeCompleto);
}
```

### Nomenclatura: UnitOfWork_StateUnderTest_ExpectedBehavior

```java
// Formato: método_cenário_resultadoEsperado

@Test
public void getName_firstNameAndLastNameSet_returnsFullName() {
    // Unidade de trabalho: getName
    // Estado sob teste: firstName e lastName definidos
    // Comportamento esperado: retorna nome completo
}

@Test
public void setBirthday_nullValue_throwsException() {
    // Unidade de trabalho: setBirthday
    // Estado sob teste: valor null
    // Comportamento esperado: lança exceção
}
```

## 🛠 JUnit 5 - Recursos Essenciais

### Anotações Principais

```java
import org.junit.jupiter.api.*;

public class ExemploTest {
    
    @BeforeAll
    static void setupClass() {
        // Executado uma vez antes de todos os testes
    }
    
    @BeforeEach
    void setup() {
        // Executado antes de cada teste
    }
    
    @Test
    void meuTeste() {
        // Um teste individual
    }
    
    @AfterEach
    void tearDown() {
        // Executado após cada teste
    }
    
    @AfterAll
    static void tearDownClass() {
        // Executado uma vez após todos os testes
    }
    
    @Disabled("Motivo para desabilitar")
    @Test
    void testeDesabilitado() {
        // Este teste não será executado
    }
}
```

### Assertions Comuns

```java
import static org.junit.jupiter.api.Assertions.*;

// Igualdade
assertEquals(esperado, atual);
assertEquals(esperado, atual, "Mensagem de erro");

// Verdadeiro/Falso
assertTrue(condicao);
assertFalse(condicao);

// Nulo/Não Nulo
assertNull(objeto);
assertNotNull(objeto);

// Mesma referência
assertSame(objeto1, objeto2);
assertNotSame(objeto1, objeto2);

// Arrays
assertArrayEquals(arrayEsperado, arrayAtual);

// Exceções
assertThrows(TipoException.class, () -> {
    // Código que deve lançar exceção
});

// Múltiplas assertions
assertAll("grupo de assertions",
    () -> assertEquals(1, 1),
    () -> assertTrue(true),
    () -> assertNotNull(objeto)
);
```

### Testando Exceções

```java
@Test
public void setFirstName_nullValue_throwsIllegalArgumentException() {
    // Arrange
    Person person = new Person();
    
    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> person.setFirstName(null)
    );
    
    // Verificar mensagem (opcional)
    assertEquals("First name cannot be null or empty", exception.getMessage());
}
```

## 🎭 Mockito - Mocks e Spies

### Diferença entre Mock e Spy

- **Mock**: Objeto completamente falso, todos os métodos retornam valores padrão
- **Spy**: Objeto real com capacidade de sobrescrever comportamentos específicos

### Configuração

```java
@ExtendWith(MockitoExtension.class)
public class PersonTest {
    
    @Mock
    private List<Person> mockList;
    
    @Spy
    private Person spyPerson;
    
    @BeforeEach
    void setup() {
        // Alternativa manual
        mockList = mock(List.class);
        spyPerson = spy(new Person());
    }
}
```

### Uso de Spy no Projeto

```java
@Test
public void isBirthdayToday_sameMonthAndDay_true() {
    // Arrange - Criando spy para controlar getNow()
    Person person = spy(new Person());
    
    // Definindo comportamento específico
    when(person.getNow()).thenReturn(LocalDate.of(2020, 8, 7));
    
    // Configurando aniversário
    person.setBirthday(LocalDate.of(1990, 8, 7));
    
    // Act
    boolean result = person.isBirthdayToday();
    
    // Assert
    assertTrue(result);
    
    // Verificar interações (opcional)
    verify(person, times(1)).getNow();
}
```

### Stubbing (Definindo Comportamentos)

```java
// Retornar valor específico
when(mock.metodo()).thenReturn(valor);

// Lançar exceção
when(mock.metodo()).thenThrow(new RuntimeException());

// Múltiplas chamadas
when(mock.metodo())
    .thenReturn(valor1)
    .thenReturn(valor2);

// Com argumentos específicos
when(mock.metodo("arg")).thenReturn(valor);

// Com qualquer argumento
when(mock.metodo(any())).thenReturn(valor);
```

## 📏 Padrões e Convenções

### Organização dos Testes

```java
public class PersonTest {
    // 1. Declaração de variáveis
    private Person person;
    
    // 2. Setup
    @BeforeEach
    void setup() {
        person = new Person();
    }
    
    // 3. Testes agrupados por método
    // Testes do getName()
    @Test
    public void getName_firstNameJonLastNameSnow_jonSnow() { }
    
    @Test
    public void getName_firstNameJonNoLastName_jon() { }
    
    // Testes do isBirthdayToday()
    @Test
    public void isBirthdayToday_differentMonthAndDay_false() { }
    
    @Test
    public void isBirthdayToday_sameMonthAndDay_true() { }
    
    // 4. Métodos auxiliares no final
    private LocalDate createBirthday(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }
}
```

### Boas Práticas

1. **Um assert por teste** (quando possível)
```java
// ✅ Bom
@Test
public void getName_firstNameSet_returnsFirstName() {
    person.setFirstName("João");
    assertEquals("João", person.getName());
}

// ❌ Evitar
@Test
public void testMultipleThings() {
    person.setFirstName("João");
    assertEquals("João", person.getFirstName());
    assertNotNull(person.getName());
    assertTrue(person.getName().length() > 0);
}
```

2. **Dados de teste claros**
```java
// ✅ Bom - Intenção clara
String nomeEsperado = "João Silva";
String primeiroNome = "João";
String sobrenome = "Silva";

// ❌ Evitar - Valores mágicos
assertEquals("João Silva", person.getName());
```

3. **Testes independentes**
```java
// ✅ Bom - Cada teste cria seus próprios dados
@Test
public void test1() {
    Person person = new Person();
    // ...
}

@Test
public void test2() {
    Person person = new Person();
    // ...
}
```

## 📝 Casos de Teste do Projeto

### 1. Testes do método getName()

```java
// Cenário: Nome completo
@Test
public void getName_firstNameJonLastNameSnow_jonSnow() {
    // Testa concatenação de primeiro nome e sobrenome
}

// Cenário: Apenas primeiro nome
@Test
public void getName_firstNameJonNoLastName_jon() {
    // Testa retorno quando apenas firstName está definido
}

// Cenário: Apenas sobrenome
@Test
public void getName_noFirstNameLastNameSnow_snow() {
    // Testa retorno quando apenas lastName está definido
}

// Cenário: Nenhum nome
@Test
public void getName_noFirstNameNorLastName_throwsException() {
    // Testa exceção quando ambos são null
}
```

### 2. Testes do método isBirthdayToday()

```java
// Cenário: Aniversário em data diferente
@Test
public void isBirthdayToday_differentMonthAndDay_false() {
    // Usa spy para controlar data atual
}

// Cenário: Mesmo mês, dia diferente
@Test
public void isBirthdayToday_sameMonthDifferentDay_false() {
    // Testa precisão da comparação
}

// Cenário: É aniversário hoje
@Test
public void isBirthdayToday_sameMonthAndDay_true() {
    // Testa caso positivo
}
```

### 3. Testes do método addToFamily()

```java
// Cenário: Adicionar membro válido
@Test
public void addToFamily_somePerson_familyHasNewMember() {
    // Testa adição básica
}

// Cenário: Relação bidirecional
@Test
public void addToFamily_somePerson_personAddedAlsoHasItsFamilyUpdated() {
    // Verifica se ambos têm a relação
}

// Cenário: Pessoa nula
@Test
public void addToFamily_nullPerson_noExceptionAndNotAdded() {
    // Testa tratamento de null
}

// Cenário: Auto-referência
@Test
public void addToFamily_selfReference_noExceptionAndNotAdded() {
    // Testa proteção contra adicionar a si mesmo
}

// Cenário: Duplicação
@Test
public void addToFamily_duplicatePerson_notAddedTwice() {
    // Testa proteção contra duplicatas
}
```

## 💡 Dicas e Truques

### 1. Use @DisplayName para testes mais legíveis
```java
@Test
@DisplayName("Deve retornar nome completo quando primeiro nome e sobrenome estão definidos")
public void getName_firstNameJonLastNameSnow_jonSnow() {
    // ...
}
```

### 2. Parametrize testes repetitivos
```java
@ParameterizedTest
@ValueSource(strings = {"", " ", "   "})
public void setFirstName_emptyStrings_throwsException(String invalidName) {
    assertThrows(IllegalArgumentException.class, 
        () -> person.setFirstName(invalidName));
}
```

### 3. Use métodos auxiliares
```java
private Person createPersonWithBirthday(int year, int month, int day) {
    Person person = new Person();
    person.setBirthday(LocalDate.of(year, month, day));
    return person;
}
```

### 4. Mensagens de erro úteis
```java
assertEquals(expected, actual, 
    () -> "Expected name to be '" + expected + "' but was '" + actual + "'");
```

## ❌ Erros Comuns

### 1. Testar implementação ao invés de comportamento
```java
// ❌ Ruim - Testa implementação interna
@Test
public void testInternalList() {
    // Verifica tipo específico de lista
}

// ✅ Bom - Testa comportamento
@Test
public void addToFamily_newPerson_isInFamily() {
    // Verifica se pessoa está na família
}
```

### 2. Testes muito complexos
```java
// ❌ Ruim - Lógica complexa no teste
@Test
public void complexTest() {
    for (int i = 0; i < 10; i++) {
        if (i % 2 == 0) {
            // ...
        }
    }
}

// ✅ Bom - Simples e direto
@Test
public void simpleTest() {
    // Arrange, Act, Assert
}
```

### 3. Dependência entre testes
```java
// ❌ Ruim - Depende de estado compartilhado
private static Person sharedPerson = new Person();

// ✅ Bom - Cada teste é independente
@BeforeEach
void setup() {
    person = new Person();
}
```

### 4. Ignorar casos extremos
```java
// ✅ Sempre teste:
// - Valores null
// - Strings vazias
// - Coleções vazias
// - Valores limite
// - Casos de erro
```

## 📚 Recursos Adicionais

### Documentação Oficial
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ (alternativa aos assertions)](https://assertj.github.io/doc/)

### Livros Recomendados
- "Test Driven Development" - Kent Beck
- "Growing Object-Oriented Software, Guided by Tests" - Freeman & Pryce
- "Effective Unit Testing" - Lasse Koskela

### Práticas Avançadas
- Test-Driven Development (TDD)
- Behavior-Driven Development (BDD)
- Property-Based Testing
- Mutation Testing

---

<div align="center">
  <p><strong>Lembre-se:</strong> Testes são documentação executável do seu código!</p>
  <p>Escreva testes que você gostaria de encontrar ao manter o código no futuro. 🚀</p>
</div> 
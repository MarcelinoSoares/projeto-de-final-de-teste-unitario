# 📚 Referência da API - Classe Person

Este documento detalha todos os métodos públicos da classe `Person` que você precisará testar.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Construtores](#construtores)
- [Métodos Getters](#métodos-getters)
- [Métodos Setters](#métodos-setters)
- [Métodos de Negócio](#métodos-de-negócio)
- [Exemplos de Uso](#exemplos-de-uso)

## 🔍 Visão Geral

A classe `Person` representa uma pessoa com as seguintes características:
- **Nome**: Primeiro nome e sobrenome
- **Data de nascimento**: Para cálculos de aniversário
- **Família**: Lista de pessoas relacionadas

### Atributos Privados
```java
private String firstName = null;
private String lastName = null;
private LocalDate birthday;
private List<Person> family = new ArrayList<>();
```

## 🏗 Construtores

### Person()
```java
public Person()
```
Construtor padrão que cria uma nova instância de Person com todos os atributos em seus valores padrão.

**Exemplo:**
```java
Person person = new Person();
```

## 📖 Métodos Getters

### getFirstName()
```java
public String getFirstName()
```
Retorna o primeiro nome da pessoa.

**Retorno:** `String` - O primeiro nome ou `null` se não definido

**Exemplo:**
```java
person.setFirstName("João");
String nome = person.getFirstName(); // "João"
```

### getLastName()
```java
public String getLastName()
```
Retorna o sobrenome da pessoa.

**Retorno:** `String` - O sobrenome ou `null` se não definido

**Exemplo:**
```java
person.setLastName("Silva");
String sobrenome = person.getLastName(); // "Silva"
```

### getBirthday()
```java
public LocalDate getBirthday()
```
Retorna a data de nascimento da pessoa.

**Retorno:** `LocalDate` - A data de nascimento ou `null` se não definida

**Exemplo:**
```java
LocalDate nascimento = person.getBirthday();
```

### getFamily()
```java
public List<Person> getFamily()
```
Retorna uma cópia da lista de familiares.

**Retorno:** `List<Person>` - Nova lista contendo os familiares

**Exemplo:**
```java
List<Person> familiares = person.getFamily();
```

### getNow()
```java
public LocalDate getNow()
```
Retorna a data atual. Este método pode ser sobrescrito em testes para controlar a data.

**Retorno:** `LocalDate` - A data atual

**Exemplo:**
```java
LocalDate hoje = person.getNow(); // LocalDate.now()
```

## ✏️ Métodos Setters

### setFirstName(String firstName)
```java
public void setFirstName(String firstName)
```
Define o primeiro nome da pessoa.

**Parâmetros:**
- `firstName` - O primeiro nome (não pode ser `null` ou vazio)

**Exceções:**
- `IllegalArgumentException` - Se o nome for `null` ou vazio/apenas espaços

**Exemplo:**
```java
person.setFirstName("Maria"); // OK
person.setFirstName(null);    // Lança IllegalArgumentException
person.setFirstName("   ");   // Lança IllegalArgumentException
```

### setLastName(String lastName)
```java
public void setLastName(String lastName)
```
Define o sobrenome da pessoa.

**Parâmetros:**
- `lastName` - O sobrenome (não pode ser `null` ou vazio)

**Exceções:**
- `IllegalArgumentException` - Se o sobrenome for `null` ou vazio/apenas espaços

**Exemplo:**
```java
person.setLastName("Santos"); // OK
person.setLastName(null);    // Lança IllegalArgumentException
person.setLastName("");      // Lança IllegalArgumentException
```

### setBirthday(LocalDate birthday)
```java
public void setBirthday(LocalDate birthday)
```
Define a data de nascimento da pessoa.

**Parâmetros:**
- `birthday` - A data de nascimento (não pode ser `null`)

**Exceções:**
- `IllegalArgumentException` - Se a data for `null`

**Exemplo:**
```java
person.setBirthday(LocalDate.of(1990, 5, 15)); // OK
person.setBirthday(null); // Lança IllegalArgumentException
```

## 🎯 Métodos de Negócio

### getName()
```java
public String getName()
```
Retorna o nome completo da pessoa ou apenas o nome/sobrenome disponível.

**Retorno:** `String` - O nome conforme as regras:
- Se ambos firstName e lastName existem: retorna "firstName lastName"
- Se apenas firstName existe: retorna firstName
- Se apenas lastName existe: retorna lastName

**Exceções:**
- `IllegalStateException` - Se ambos firstName e lastName forem `null`

**Exemplos:**
```java
// Caso 1: Nome completo
person.setFirstName("João");
person.setLastName("Silva");
person.getName(); // "João Silva"

// Caso 2: Apenas primeiro nome
person.setFirstName("Maria");
person.getName(); // "Maria"

// Caso 3: Apenas sobrenome
person.setLastName("Santos");
person.getName(); // "Santos"

// Caso 4: Nenhum nome
person.getName(); // Lança IllegalStateException
```

### isBirthdayToday()
```java
public boolean isBirthdayToday()
```
Verifica se hoje é o aniversário da pessoa.

**Retorno:** `boolean`
- `true` se hoje é o aniversário (mesmo dia e mês)
- `false` caso contrário ou se birthday for `null`

**Exemplos:**
```java
// Assumindo que hoje é 07/08/2020
person.setBirthday(LocalDate.of(1990, 8, 7));
person.isBirthdayToday(); // true

person.setBirthday(LocalDate.of(1990, 8, 6));
person.isBirthdayToday(); // false

person.setBirthday(LocalDate.of(1990, 7, 7));
person.isBirthdayToday(); // false
```

### addToFamily(Person person)
```java
public void addToFamily(Person person)
```
Adiciona uma pessoa à família, criando uma relação bidirecional.

**Parâmetros:**
- `person` - A pessoa a ser adicionada à família

**Comportamento:**
- Se `person` for `null`: não faz nada
- Se `person` for a própria pessoa: não faz nada
- Se `person` já estiver na família: não adiciona novamente
- Caso contrário: adiciona à família e cria relação bidirecional

**Exemplos:**
```java
Person pai = new Person();
Person filho = new Person();

// Adiciona filho à família do pai
pai.addToFamily(filho);
// Agora: pai.isFamily(filho) == true
// E também: filho.isFamily(pai) == true

// Tentativas inválidas (não fazem nada)
pai.addToFamily(null);     // Ignora null
pai.addToFamily(pai);      // Ignora auto-referência
pai.addToFamily(filho);    // Ignora duplicata
```

### isFamily(Person person)
```java
public boolean isFamily(Person person)
```
Verifica se uma pessoa faz parte da família.

**Parâmetros:**
- `person` - A pessoa a ser verificada

**Retorno:** `boolean`
- `true` se a pessoa está na família
- `false` caso contrário

**Exemplo:**
```java
Person mae = new Person();
Person filha = new Person();

mae.isFamily(filha); // false

mae.addToFamily(filha);
mae.isFamily(filha); // true
filha.isFamily(mae); // true (relação bidirecional)
```

## 💻 Exemplos de Uso

### Exemplo Completo 1: Criando uma Pessoa
```java
// Criar pessoa
Person pessoa = new Person();

// Definir dados básicos
pessoa.setFirstName("Ana");
pessoa.setLastName("Costa");
pessoa.setBirthday(LocalDate.of(1995, 3, 20));

// Obter nome completo
String nomeCompleto = pessoa.getName(); // "Ana Costa"

// Verificar aniversário
boolean aniversarioHoje = pessoa.isBirthdayToday(); // Depende da data atual
```

### Exemplo Completo 2: Criando uma Família
```java
// Criar membros da família
Person pai = new Person();
pai.setFirstName("Carlos");
pai.setLastName("Oliveira");

Person mae = new Person();
mae.setFirstName("Maria");
mae.setLastName("Oliveira");

Person filho = new Person();
filho.setFirstName("Pedro");
filho.setLastName("Oliveira");

// Estabelecer relações familiares
pai.addToFamily(mae);
pai.addToFamily(filho);
mae.addToFamily(filho);

// Verificar relações
pai.isFamily(mae);   // true
pai.isFamily(filho); // true
mae.isFamily(filho); // true
filho.isFamily(pai); // true (bidirecional)
filho.isFamily(mae); // true (bidirecional)

// Obter lista de familiares
List<Person> familiaDoPai = pai.getFamily(); // [mae, filho]
```

### Exemplo Completo 3: Tratamento de Erros
```java
Person pessoa = new Person();

// Tentativas que lançam exceção
try {
    pessoa.setFirstName(null);
} catch (IllegalArgumentException e) {
    System.out.println("Nome não pode ser null");
}

try {
    pessoa.getName(); // Sem nome definido
} catch (IllegalStateException e) {
    System.out.println("Deve definir pelo menos um nome");
}

try {
    pessoa.setBirthday(null);
} catch (IllegalArgumentException e) {
    System.out.println("Data de nascimento não pode ser null");
}
```

## 📝 Notas Importantes

1. **Imutabilidade das Listas**: O método `getFamily()` retorna uma nova lista, não a lista interna
2. **Relações Bidirecionais**: `addToFamily()` sempre cria relações em ambas as direções
3. **Validações**: Os setters validam os parâmetros e lançam exceções apropriadas
4. **Comparação de Datas**: `isBirthdayToday()` compara apenas dia e mês, ignorando o ano

---

<div align="center">
  <p>Esta referência cobre todos os métodos públicos que você precisará testar.</p>
  <p>Use-a como guia ao implementar seus testes! 📖</p>
</div> 
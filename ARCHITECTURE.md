# Arquitetura do Projeto

## Visão Geral

Este projeto segue uma arquitetura em camadas com separação clara de responsabilidades:

```
┌─────────────────────────────────────────────┐
│            Camada de Testes                 │
│  (Unit, Integration, Contract, Property)    │
└─────────────────────────────────────────────┘
                      │
┌─────────────────────────────────────────────┐
│           Camada de Serviço                 │
│         (PersonService)                     │
└─────────────────────────────────────────────┘
                      │
┌─────────────────────────────────────────────┐
│           Camada de Domínio                 │
│    (Person, Address, CpfValidator)          │
└─────────────────────────────────────────────┘
                      │
┌─────────────────────────────────────────────┐
│         Camada de Repositório               │
│        (PersonRepository)                   │
└─────────────────────────────────────────────┘
```

## Componentes Principais

### 1. Domínio (Domain Layer)
- **Person**: Entidade principal com lógica de negócio
- **Address**: Value Object com Builder pattern
- **CpfValidator**: Utility class para validação de CPF

### 2. Serviço (Service Layer)
- **PersonService**: Orquestra operações complexas
- **EmailService**: Interface para notificações

### 3. Repositório (Repository Layer)
- **PersonRepository**: Interface para persistência
- **InMemoryPersonRepository**: Implementação para testes

### 4. Exceções
- **PersonNotFoundException**: Exceção de domínio customizada

## Padrões de Design Utilizados

### 1. Builder Pattern
```java
Address address = new Address.Builder()
    .street("Rua A")
    .number("123")
    .city("Recife")
    .state("PE")
    .zipCode("50000-000")
    .build();
```

### 2. Repository Pattern
```java
public interface PersonRepository {
    Person save(Person person);
    Optional<Person> findById(Long id);
    List<Person> findAll();
    void delete(Long id);
}
```

### 3. Dependency Injection
```java
public class PersonService {
    private final PersonRepository repository;
    private final EmailService emailService;
    
    public PersonService(PersonRepository repository, 
                        EmailService emailService) {
        this.repository = Objects.requireNonNull(repository);
        this.emailService = Objects.requireNonNull(emailService);
    }
}
```

### 4. Value Object Pattern
- Address é imutável após criação
- Equals/HashCode baseados em valores

### 5. Factory Method Pattern
```java
public static String generateRandom() {
    // Gera CPF válido para testes
}
```

## Decisões Arquiteturais

### 1. Imutabilidade
- Address é imutável (thread-safe)
- Listas retornadas são cópias defensivas

### 2. Validação
- Validação no momento da atribuição (fail-fast)
- Mensagens de erro descritivas

### 3. Testabilidade
- Injeção de dependências
- Interfaces para componentes externos
- Método getNow() para facilitar testes com datas

### 4. Separação de Responsabilidades
- Domínio não conhece persistência
- Serviços não conhecem detalhes de implementação
- Testes organizados por tipo e propósito

## Fluxo de Dados

```
User Input → Validation → Domain Object → Service → Repository
                ↓                            ↓
            Exception                   Email Service
```

## Estratégia de Testes

### 1. Pirâmide de Testes
- **Unit Tests**: 80% (testes isolados)
- **Integration Tests**: 15% (testes de integração)
- **Contract Tests**: 5% (garantia de API)

### 2. Tipos de Testes
- **Testes Unitários**: Lógica de negócio isolada
- **Testes de Integração**: Fluxo completo
- **Testes de Contrato**: API pública estável
- **Testes de Propriedade**: Invariantes do sistema
- **Testes de Performance**: Benchmarks com JMH
- **Testes de Mutação**: Qualidade dos testes

## Métricas de Qualidade

- **Cobertura de Código**: > 95%
- **Complexidade Ciclomática**: < 10 por método
- **Duplicação de Código**: < 3%
- **Dívida Técnica**: < 1 dia

## Evolução Futura

### Possíveis Melhorias
1. Event Sourcing para auditoria
2. CQRS para separar leitura/escrita
3. Cache para operações custosas
4. Async/Reactive para escalabilidade

### Pontos de Extensão
- Novos tipos de validação
- Diferentes implementações de repositório
- Novos serviços de notificação
- Internacionalização 
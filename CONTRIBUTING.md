# 🤝 Guia de Contribuição

Obrigado por considerar contribuir com o Projeto de Testes Unitários da ETA! Este documento fornece diretrizes e instruções detalhadas para ajudá-lo a contribuir de forma efetiva.

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Posso Contribuir?](#como-posso-contribuir)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Fluxo de Trabalho](#fluxo-de-trabalho)
- [Padrões de Código](#padrões-de-código)
- [Escrevendo Testes](#escrevendo-testes)
- [Mensagens de Commit](#mensagens-de-commit)
- [Pull Request](#pull-request)
- [Revisão de Código](#revisão-de-código)

## 📜 Código de Conduta

### Nossos Princípios
- 🤝 Seja respeitoso e inclusivo
- 💡 Aceite feedback construtivo
- 🎯 Foque na qualidade do código
- 📚 Compartilhe conhecimento
- ⏰ Respeite prazos estabelecidos

## 🎯 Como Posso Contribuir?

### Para Alunos
1. **Implemente os testes unitários** conforme especificado
2. **Siga os padrões** estabelecidos neste guia
3. **Documente** seu código quando necessário
4. **Teste localmente** antes de submeter

### Tipos de Contribuição
- 🧪 Implementação de testes unitários
- 📝 Melhorias na documentação
- 🐛 Correção de bugs (apenas nos testes)
- 💡 Sugestões de melhorias

## 🛠 Configuração do Ambiente

### 1. Requisitos do Sistema
```bash
# Verificar Java
java -version  # Deve ser 11 ou superior

# Verificar Maven
mvn -version   # Deve ser 3.6 ou superior

# Verificar Git
git --version
```

### 2. Configuração do Git
```bash
# Configure seu nome
git config --global user.name "Seu Nome"

# Configure seu email
git config --global user.email "seu.email@example.com"

# Configure editor padrão (opcional)
git config --global core.editor "code --wait"  # Para VS Code
```

### 3. Fork e Clone
```bash
# 1. Faça fork pelo GitHub (botão Fork)

# 2. Clone seu fork
git clone https://github.com/SEU-USUARIO/projeto-de-final-de-teste-unitario.git

# 3. Entre no diretório
cd projeto-de-final-de-teste-unitario

# 4. Adicione o repositório original como upstream
git remote add upstream https://github.com/cesar-school/eta-unit-testing-project-2019.2-Recife.git

# 5. Verifique os remotes
git remote -v
```

## 🔄 Fluxo de Trabalho

### 1. Sincronize com Upstream
```bash
# Busque atualizações
git fetch upstream

# Mude para main
git checkout main

# Merge com upstream
git merge upstream/main

# Push para seu fork
git push origin main
```

### 2. Crie uma Branch
```bash
# Crie e mude para nova branch
git checkout -b feature/implementa-testes-person

# Nomenclatura de branches:
# feature/descricao - Para novas funcionalidades
# fix/descricao     - Para correções
# docs/descricao    - Para documentação
```

### 3. Desenvolva
```bash
# Verifique status
git status

# Adicione arquivos modificados
git add src/test/java/school/cesar/eta/unit/PersonTest.java

# Ou adicione todos
git add .

# Faça commit
git commit -m "feat: implementa teste getName com múltiplos cenários"
```

### 4. Teste Localmente
```bash
# Execute todos os testes
mvn clean test

# Execute teste específico
mvn test -Dtest=PersonTest

# Gere relatório de cobertura
mvn clean test jacoco:report
```

### 5. Push e Pull Request
```bash
# Push para seu fork
git push origin feature/implementa-testes-person

# Depois, crie o Pull Request pelo GitHub
```

## 📏 Padrões de Código

### Estilo Java
```java
// ✅ BOM: Nome descritivo seguindo padrão
@Test
public void getName_firstNameJonLastNameSnow_jonSnow() {
    // Implementação
}

// ❌ RUIM: Nome genérico
@Test
public void testGetName() {
    // Implementação
}
```

### Estrutura dos Testes
```java
@Test
public void methodName_scenario_expectedResult() {
    // Arrange - Preparação
    Person person = new Person();
    person.setFirstName("Jon");
    person.setLastName("Snow");
    
    // Act - Ação
    String result = person.getName();
    
    // Assert - Verificação
    assertEquals("Jon Snow", result);
}
```

### Imports
```java
// Ordem dos imports:
// 1. Java padrão
import java.time.LocalDate;
import java.util.List;

// 2. Bibliotecas externas
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Spy;

// 3. Classes do projeto
import school.cesar.eta.unit.Person;

// 4. Imports estáticos
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
```

## 🧪 Escrevendo Testes

### Checklist para Cada Teste
- [ ] Nome segue padrão `UnitOfWork_StateUnderTest_ExpectedBehavior`
- [ ] Testa apenas um comportamento
- [ ] Usa AAA pattern (Arrange, Act, Assert)
- [ ] Assertions são claras e específicas
- [ ] Não tem lógica complexa
- [ ] Testa casos extremos quando aplicável
- [ ] Usa mocks apropriadamente

### Exemplos de Bons Testes

#### Teste Simples
```java
@Test
public void setFirstName_nullValue_throwsException() {
    // Arrange
    Person person = new Person();
    
    // Act & Assert
    assertThrows(IllegalArgumentException.class, 
        () -> person.setFirstName(null),
        "Should throw exception for null first name");
}
```

#### Teste com Mock
```java
@Test
public void isBirthdayToday_birthdayIsToday_true() {
    // Arrange
    Person person = spy(new Person());
    LocalDate today = LocalDate.of(2020, 8, 7);
    LocalDate birthday = LocalDate.of(1990, 8, 7);
    
    when(person.getNow()).thenReturn(today);
    person.setBirthday(birthday);
    
    // Act
    boolean result = person.isBirthdayToday();
    
    // Assert
    assertTrue(result);
    verify(person, times(1)).getNow();
}
```

#### Teste de Coleção
```java
@Test
public void addToFamily_newMember_familyContainsMember() {
    // Arrange
    Person person = new Person();
    Person relative = new Person();
    
    // Act
    person.addToFamily(relative);
    
    // Assert
    assertTrue(person.isFamily(relative));
    assertTrue(relative.isFamily(person));
    assertEquals(1, person.getFamily().size());
}
```

## 📝 Mensagens de Commit

### Formato
```
<tipo>: <descrição>

[corpo opcional]

[rodapé opcional]
```

### Tipos
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação (sem mudança de código)
- `refactor`: Refatoração
- `test`: Adição ou correção de testes
- `chore`: Tarefas de manutenção

### Exemplos
```bash
# ✅ Bom
git commit -m "feat: implementa teste para método getName com nome completo"
git commit -m "test: adiciona casos de teste para valores nulos em setFirstName"
git commit -m "docs: atualiza README com instruções de execução"

# ❌ Ruim
git commit -m "mudanças"
git commit -m "fix"
git commit -m "WIP"
```

## 🔄 Pull Request

### Template de PR
```markdown
## Descrição
Breve descrição do que foi implementado.

## Testes Implementados
- [ ] getName_firstNameJonLastNameSnow_jonSnow
- [ ] getName_firstNameJonNoLastName_jon
- [ ] getName_noFirstNameLastNameSnow_snow
- [ ] ... (liste todos os testes implementados)

## Checklist
- [ ] Código segue os padrões do projeto
- [ ] Todos os testes passam localmente
- [ ] Cobertura de código adequada
- [ ] Sem modificações em Person.java
- [ ] Commits seguem convenção

## Screenshots (se aplicável)
Adicione screenshots do relatório de cobertura.

## Observações Adicionais
Qualquer informação relevante.
```

### Boas Práticas para PR
1. **Título claro**: "Implementa testes unitários para classe Person"
2. **Descrição detalhada**: Explique o que foi feito
3. **PR pequeno**: Foque apenas nos testes
4. **Responda feedback**: Seja receptivo a sugestões

## 👀 Revisão de Código

### Para Revisores
- ✅ Verifique se os testes seguem o padrão de nomenclatura
- ✅ Confirme que Person.java não foi modificada
- ✅ Valide a lógica dos testes
- ✅ Verifique cobertura de casos extremos
- ✅ Sugira melhorias construtivamente

### Para Contribuidores
- 📝 Responda todos os comentários
- 🔄 Faça as correções solicitadas
- 💬 Explique decisões quando necessário
- ✅ Marque conversas como resolvidas

## 🆘 Precisa de Ajuda?

### Recursos
- 📚 [Documentação JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- 📖 [Guia Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- 🎥 [Vídeos sobre TDD](https://www.youtube.com/results?search_query=test+driven+development+java)

### Contatos
- 💬 Abra uma issue para dúvidas
- 📧 Email: instrutor@cesar.school
- 👥 Fórum da disciplina

---

<div align="center">
  <p><strong>Lembre-se:</strong> Qualidade > Quantidade</p>
  <p>Bons testes são a base de software confiável! 🚀</p>
</div> 
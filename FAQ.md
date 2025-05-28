# ❓ Perguntas Frequentes (FAQ)

Este documento responde as dúvidas mais comuns sobre o projeto de testes unitários.

## 📋 Índice

- [Geral](#geral)
- [Configuração e Ambiente](#configuração-e-ambiente)
- [Implementação dos Testes](#implementação-dos-testes)
- [Mockito e Spies](#mockito-e-spies)
- [Submissão e Avaliação](#submissão-e-avaliação)
- [Problemas Comuns](#problemas-comuns)

## 🌐 Geral

### 1. Posso modificar a classe Person.java?
**Não!** A classe `Person.java` não deve ser modificada. Você deve apenas implementar os testes na classe `PersonTest.java`.

### 2. Quantos testes preciso implementar para ter nota máxima?
Você precisa implementar corretamente **10 dos 11 testes** disponíveis para obter a nota máxima.

### 3. Posso usar outras bibliotecas além de JUnit e Mockito?
Não é necessário. O projeto já inclui todas as dependências necessárias (JUnit 5 e Mockito).

### 4. Qual o prazo para entrega?
Você tem **15 dias** a partir do início do projeto para submeter seu Pull Request.

## 🛠 Configuração e Ambiente

### 1. Qual versão do Java devo usar?
O projeto requer **Java 11 ou superior**. Verifique com:
```bash
java -version
```

### 2. Como configuro o projeto na minha IDE?

**IntelliJ IDEA:**
1. File → Open → Selecione a pasta do projeto
2. Aguarde a importação do Maven
3. File → Project Structure → Project SDK → Java 11+

**Eclipse:**
1. File → Import → Maven → Existing Maven Projects
2. Selecione a pasta do projeto
3. Finish

**VS Code:**
1. Instale as extensões: "Java Extension Pack"
2. Abra a pasta do projeto
3. O VS Code detectará automaticamente o projeto Maven

### 3. O Maven não está funcionando. O que fazer?
```bash
# Limpe o cache do Maven
mvn clean

# Force a atualização das dependências
mvn dependency:purge-local-repository
mvn clean install -U
```

## 🧪 Implementação dos Testes

### 1. O que significa o padrão UnitOfWork_StateUnderTest_ExpectedBehavior?

- **UnitOfWork**: O método sendo testado
- **StateUnderTest**: A condição/cenário do teste
- **ExpectedBehavior**: O resultado esperado

Exemplo:
```java
getName_firstNameJonLastNameSnow_jonSnow
// Método: getName
// Estado: firstName="Jon", lastName="Snow"
// Esperado: retorna "Jon Snow"
```

### 2. Como estruturo meu teste?
Use o padrão AAA:
```java
@Test
public void nomeDoTeste() {
    // Arrange - Prepare os dados
    Person person = new Person();
    person.setFirstName("João");
    
    // Act - Execute a ação
    String result = person.getName();
    
    // Assert - Verifique o resultado
    assertEquals("João", result);
}
```

### 3. Preciso testar todos os cenários possíveis?
Foque nos cenários especificados nos nomes dos métodos de teste. Cada teste deve verificar exatamente o que seu nome sugere.

### 4. Como testo exceções?
Use `assertThrows`:
```java
@Test
public void metodo_condicao_lancaExcecao() {
    Person person = new Person();
    
    assertThrows(IllegalArgumentException.class, () -> {
        person.setFirstName(null);
    });
}
```

## 🎭 Mockito e Spies

### 1. Quando devo usar Spy?
Use Spy quando precisar controlar o comportamento de métodos específicos mantendo o comportamento real dos outros. No projeto, é útil para controlar `getNow()` nos testes de aniversário.

### 2. Como uso o Spy corretamente?
```java
@Test
public void teste() {
    // Crie o spy
    Person person = spy(new Person());
    
    // Defina comportamento específico
    when(person.getNow()).thenReturn(LocalDate.of(2020, 8, 7));
    
    // Use normalmente
    person.setBirthday(LocalDate.of(1990, 8, 7));
    assertTrue(person.isBirthdayToday());
}
```

### 3. Qual a diferença entre Mock e Spy?
- **Mock**: Objeto completamente falso, todos os métodos retornam valores padrão
- **Spy**: Objeto real com alguns métodos sobrescritos

### 4. O @Spy da variável family é necessário?
Não é obrigatório usá-lo. Foi incluído como exemplo, mas você pode implementar os testes sem ele.

## 📤 Submissão e Avaliação

### 1. Como faço o fork do repositório?
1. Acesse o repositório original no GitHub
2. Clique no botão "Fork" (canto superior direito)
3. Selecione sua conta
4. Clone seu fork: `git clone https://github.com/SEU-USUARIO/nome-do-repo.git`

### 2. Como crio o Pull Request?
1. Faça commit das suas mudanças
2. Push para seu fork: `git push origin sua-branch`
3. No GitHub, clique em "Pull Request"
4. Selecione base: repositório original, compare: seu fork
5. Preencha o template e submeta

### 3. Posso fazer múltiplos commits?
Sim! Faça quantos commits precisar. O importante é que o código final esteja correto.

### 4. E se eu encontrar um erro após submeter o PR?
Você pode continuar fazendo commits na mesma branch. O PR será atualizado automaticamente.

## ⚠️ Problemas Comuns

### 1. "Cannot resolve symbol" no import
```bash
# Recarregue as dependências
mvn clean install
```
Na IDE, faça "Reload Maven Project" ou "Refresh Dependencies".

### 2. Testes passam localmente mas falham no CI
Possíveis causas:
- Dependência da ordem de execução dos testes
- Diferenças de timezone/locale
- Testes não independentes

Solução: Garanta que cada teste seja independente e não dependa de estado compartilhado.

### 3. "No tests found"
Verifique:
- Os métodos de teste têm a anotação `@Test`
- Os métodos são `public void`
- A classe de teste termina com `Test`

### 4. Erro de cobertura baixa
Execute localmente para verificar:
```bash
mvn clean test jacoco:report
# Abra target/site/jacoco/index.html
```

### 5. Git push rejeitado
```bash
# Sincronize com o upstream
git fetch upstream
git merge upstream/main
# Resolva conflitos se houver
git push origin sua-branch
```

## 💡 Dicas Extras

### 1. Como debugar um teste?
Na maioria das IDEs, clique com botão direito no teste → Debug

### 2. Como executar apenas um teste?
```bash
mvn test -Dtest=PersonTest#nomeDoMetodo
```

### 3. Como ver o que o Mockito está fazendo?
```java
// Use verify para debug
verify(spy, times(1)).metodo();

// Ou imprima interações
System.out.println(mockingDetails(spy).getInvocations());
```

### 4. Formatação automática do código
```bash
mvn formatter:format
```

## 🆘 Ainda com Dúvidas?

1. **Releia a documentação**: README.md, CONTRIBUTING.md, TESTING_GUIDE.md
2. **Verifique issues existentes**: Alguém pode ter tido a mesma dúvida
3. **Abra uma nova issue**: Descreva claramente seu problema
4. **Contate o instrutor**: instrutor@cesar.school

---

<div align="center">
  <p><strong>Dica Final:</strong> Comece simples! Implemente primeiro os testes mais básicos e vá evoluindo.</p>
  <p>Boa sorte! 🍀</p>
</div> 
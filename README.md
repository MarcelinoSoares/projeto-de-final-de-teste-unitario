# 🎓 Projeto de Testes Unitários - ETA CESAR School
> **Turma:** 2019.2-Recife  
> **Disciplina:** Engenharia de Testes Automatizados (ETA)  
> **Complexidade:** Baixa

[![Java CI with Maven](https://github.com/cesar-school/eta-unit-testing-project-2019.2-Recife/workflows/CI/badge.svg)](https://github.com/cesar-school/eta-unit-testing-project-2019.2-Recife/actions?query=workflow%3A%22CI%22)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=cesar-school_eta-unit-testing-project-2019.2-Recife&metric=coverage)](https://sonarcloud.io/dashboard?id=cesar-school_eta-unit-testing-project-2019.2-Recife)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=cesar-school_eta-unit-testing-project-2019.2-Recife&metric=alert_status)](https://sonarcloud.io/dashboard?id=cesar-school_eta-unit-testing-project-2019.2-Recife)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=cesar-school_eta-unit-testing-project-2019.2-Recife&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=cesar-school_eta-unit-testing-project-2019.2-Recife)

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Executando os Testes](#-executando-os-testes)
- [Relatórios de Cobertura](#-relatórios-de-cobertura)
- [Guia de Contribuição](#-guia-de-contribuição)
- [Critérios de Avaliação](#-critérios-de-avaliação)
- [Boas Práticas](#-boas-práticas)
- [Troubleshooting](#-troubleshooting)
- [Contato e Suporte](#-contato-e-suporte)

## 📚 Sobre o Projeto

Este é um projeto educacional desenvolvido para a disciplina de **Engenharia de Testes Automatizados** da CESAR School. O objetivo principal é praticar a escrita de testes unitários seguindo as melhores práticas da indústria.

### Objetivos de Aprendizagem
- ✅ Escrever testes unitários eficazes usando JUnit 5
- ✅ Aplicar o padrão de nomenclatura `UnitOfWork_StateUnderTest_ExpectedBehavior`
- ✅ Utilizar Mockito para criar mocks e spies
- ✅ Alcançar alta cobertura de código
- ✅ Praticar Test-Driven Development (TDD)

### Descrição do Desafio
O projeto contém **11 cenários de teste** que devem ser implementados. Cada teste vale **1 ponto**, e implementar corretamente **10 dos 11 testes** garante a nota máxima.

## 🛠 Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 11+ | Linguagem de programação principal |
| **Maven** | 3.6+ | Gerenciamento de dependências e build |
| **JUnit Jupiter** | 5.10.2 | Framework de testes unitários |
| **Mockito** | 5.2.0 | Framework para criação de mocks |
| **JaCoCo** | 0.8.11 | Ferramenta de cobertura de código |
| **SonarCloud** | - | Análise estática de código |
| **GitHub Actions** | - | CI/CD pipeline |

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java JDK 11** ou superior
  ```bash
  java -version  # Deve mostrar versão 11 ou superior
  ```
- **Apache Maven 3.6+**
  ```bash
  mvn -version  # Deve mostrar versão 3.6 ou superior
  ```
- **Git**
  ```bash
  git --version
  ```
- **IDE recomendada:** IntelliJ IDEA, Eclipse ou VS Code com extensões Java

## 🚀 Instalação e Configuração

### 1. Clone o repositório
```bash
# Via HTTPS
git clone https://github.com/seu-usuario/projeto-de-final-de-teste-unitario.git

# Via SSH
git clone git@github.com:seu-usuario/projeto-de-final-de-teste-unitario.git

# Entre no diretório
cd projeto-de-final-de-teste-unitario
```

### 2. Compile o projeto
```bash
mvn clean compile
```

### 3. Execute os testes
```bash
mvn test
```

## 📁 Estrutura do Projeto

```
projeto-de-final-de-teste-unitario/
├── .github/
│   └── workflows/
│       └── maven.yml          # Configuração do CI/CD
├── src/
│   ├── main/
│   │   └── java/
│   │       └── school/cesar/eta/unit/
│   │           └── Person.java # Classe principal a ser testada
│   └── test/
│       └── java/
│           └── school/cesar/eta/unit/
│               └── PersonTest.java # Classe de testes
├── target/                    # Arquivos gerados (build, relatórios)
├── .gitignore                # Arquivos ignorados pelo Git
├── pom.xml                   # Configuração do Maven
└── README.md                 # Este arquivo
```

### Descrição dos Arquivos Principais

#### `Person.java`
Classe que representa uma pessoa com as seguintes funcionalidades:
- **Atributos:** firstName, lastName, birthday, family
- **Métodos principais:**
  - `getName()`: Retorna o nome completo
  - `isBirthdayToday()`: Verifica se hoje é aniversário
  - `addToFamily()`: Adiciona membros à família
  - `isFamily()`: Verifica parentesco

#### `PersonTest.java`
Classe de testes que você deve implementar, contendo 11 cenários de teste.

## 🧪 Executando os Testes

### Comando básico
```bash
mvn test
```

### Com relatório detalhado
```bash
mvn clean test -Dtest=PersonTest
```

### Executar teste específico
```bash
mvn test -Dtest=PersonTest#getName_firstNameJonLastNameSnow_jonSnow
```

### Executar com debug
```bash
mvn -Dmaven.surefire.debug test
```

## 📊 Relatórios de Cobertura

### JaCoCo - Relatório Local
Após executar os testes, o relatório de cobertura é gerado automaticamente:

1. Execute os testes com cobertura:
   ```bash
   mvn clean test jacoco:report
   ```

2. Abra o relatório no navegador:
   ```bash
   # Linux/Mac
   open target/site/jacoco/index.html
   
   # Windows
   start target/site/jacoco/index.html
   ```

### SonarCloud - Análise Online
O projeto está integrado com o SonarCloud para análise contínua de qualidade. Acesse o [dashboard do projeto](https://sonarcloud.io/dashboard?id=cesar-school_eta-unit-testing-project-2019.2-Recife) para visualizar:
- 📈 Cobertura de código
- 🐛 Bugs e vulnerabilidades
- 💡 Code smells
- 📏 Duplicação de código

## 🤝 Guia de Contribuição

### 1. Fork do Repositório
1. Acesse o repositório original no GitHub
2. Clique no botão "Fork" no canto superior direito
3. O repositório será copiado para sua conta

### 2. Clone seu Fork
```bash
git clone https://github.com/SEU-USUARIO/projeto-de-final-de-teste-unitario.git
cd projeto-de-final-de-teste-unitario
```

### 3. Crie uma Branch
```bash
git checkout -b feature/implementacao-testes
```

### 4. Implemente os Testes
Edite o arquivo `src/test/java/school/cesar/eta/unit/PersonTest.java` e implemente o corpo dos métodos de teste.

**⚠️ IMPORTANTE:** Não modifique a classe `Person.java`!

### 5. Commit suas Mudanças
```bash
git add .
git commit -m "feat: implementa testes unitários da classe Person"
```

### 6. Push para seu Fork
```bash
git push origin feature/implementacao-testes
```

### 7. Abra um Pull Request
1. Acesse seu fork no GitHub
2. Clique em "Pull Request"
3. Selecione sua branch e o repositório de destino
4. Descreva suas implementações
5. Submeta o PR

## 📝 Critérios de Avaliação

### Pontuação
- **Total de testes:** 11
- **Valor por teste:** 1 ponto
- **Nota máxima:** 10 pontos (implementando corretamente 10 dos 11 testes)

### Critérios Técnicos
1. **Correção:** O teste deve passar e testar corretamente o comportamento esperado
2. **Nomenclatura:** Seguir o padrão `UnitOfWork_StateUnderTest_ExpectedBehavior`
3. **Clareza:** Código limpo e legível
4. **Cobertura:** Testar todos os cenários relevantes
5. **Boas práticas:** Uso adequado de assertions e mocks

### Exemplo de Nomenclatura Correta
```java
@Test
public void getName_firstNameJonLastNameSnow_jonSnow() {
    // Arrange
    person.setFirstName("Jon");
    person.setLastName("Snow");
    
    // Act
    String result = person.getName();
    
    // Assert
    assertEquals("Jon Snow", result);
}
```

## 💡 Boas Práticas

### Estrutura dos Testes (AAA Pattern)
```java
@Test
public void nomeDoMetodo() {
    // Arrange - Preparação dos dados
    
    // Act - Execução da ação
    
    // Assert - Verificação do resultado
}
```

### Dicas Importantes
1. **Use `@BeforeEach`** para configuração comum entre testes
2. **Teste um comportamento por vez**
3. **Use nomes descritivos** para variáveis e métodos
4. **Evite lógica complexa** nos testes
5. **Teste casos extremos** (null, vazio, limites)
6. **Use Mockito** quando necessário para isolar comportamentos

### Exemplo com Mockito
```java
@Test
public void exemplo_usandoMockito() {
    // Criando um spy
    Person personSpy = spy(new Person());
    
    // Definindo comportamento
    when(personSpy.getNow()).thenReturn(LocalDate.of(2020, 8, 7));
    
    // Verificando chamadas
    verify(personSpy, times(1)).getNow();
}
```

## 🔧 Troubleshooting

### Problema: Testes não executam
```bash
# Limpe o cache do Maven
mvn clean
mvn dependency:purge-local-repository
```

### Problema: Erro de compilação
```bash
# Verifique a versão do Java
java -version
# Deve ser 11 ou superior

# Force a recompilação
mvn clean compile -U
```

### Problema: Relatório JaCoCo não gerado
```bash
# Execute com o goal específico
mvn clean test jacoco:report
```

### Problema: Erro no GitHub Actions
- Verifique se o código compila localmente
- Certifique-se de que todos os testes passam
- Verifique os logs do CI no GitHub

## 📞 Contato e Suporte

### Dúvidas Frequentes
- **Posso modificar a classe Person?** Não, apenas implemente os testes
- **Quantos testes preciso passar?** Mínimo 10 dos 11 para nota máxima
- **Prazo de entrega?** 15 dias a partir do início do projeto

### Canais de Suporte
- 📧 **Email:** instrutor@cesar.school
- 💬 **Issues:** Abra uma issue neste repositório
- 📚 **Documentação JUnit:** [junit.org/junit5/docs](https://junit.org/junit5/docs/current/user-guide/)
- 📖 **Documentação Mockito:** [javadoc.io/doc/org.mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

---

<div align="center">
  <p>Desenvolvido com ❤️ para a disciplina de ETA - CESAR School</p>
  <p>© 2019.2 - Recife</p>
</div>

# [ETA] Unit Testing Project<br> 2019.2-Recife

[![Java CI with Maven](https://github.com/cesar-school/eta-unit-testing-project-2019.2-Recife/workflows/CI/badge.svg)](https://github.com/cesar-school/eta-unit-testing-project-2019.2-Recife/actions?query=workflow%3A%22CI%22)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=cesar-school_eta-unit-testing-project-2019.2-Recife&metric=coverage)](https://sonarcloud.io/dashboard?id=cesar-school_eta-unit-testing-project-2019.2-Recife)

## Sobre o Projeto
O projeto contém 11 testes, que valem 1 ponto cada se implementado de forma correta. O aluno que escrever corretamente 10 dos 11 testes fará jus a nota máxima.

## Pré-requisitos
- Java 11 ou superior
- Maven 3.6+
- Git

## Instalação e Execução dos Testes
```sh
# Clone o repositório
$ git clone <url-do-repositorio>
$ cd <nome-do-repositorio>

# Execute os testes
$ mvn clean test
```

## Relatório de Cobertura
Após rodar os testes, abra o arquivo `target/site/jacoco/index.html` no navegador para visualizar o relatório de cobertura de código.

## Como contribuir
Usando o Github, cada aluno deverá fazer um “fork” do repositório do projeto.

No arquivo `src/test/java/school/cesar/eta/unit/PersonTest.java` estão as assinaturas dos métodos de teste, seguindo o padrão de nomenclatura visto em aula `UnitOfWork_StateUnderTest_ExpectedBehavior`. Cada aluno terá 15 dias e, de forma individual, deverá escrever o corpo dos métodos de teste relacionados à classe `Person`, que fica em `src/main/java/school/cesar/eta/unit/Person.java`. Nenhuma modificação deverá ser feita em `Person`, a classe testada.

Uma vez concluída a escrita dos testes, o aluno deverá fazer um “commit” em seu repositório e abrir um “pull request” tendo o repositório original como destino. Será por meio dele que o aluno será avaliado e o feedback será dado.

## Contato
Dúvidas? Abra uma issue ou envie um e-mail para o instrutor responsável.

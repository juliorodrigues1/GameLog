# GameLog API

## Descrição
A GameLog API é uma aplicação para gerenciar e processar logs de jogos. Ela permite o upload de arquivos de log, processa os dados e retorna relatórios detalhados sobre os jogos.

## Tecnologias Utilizadas
- Java 21
- Spring Boot
- Springdoc OpenAPI
- JPA (Jakarta Persistence API)
- Maven

## Para executar aplicação via Docker ou maven:
    docker compose up -d 
### ou
    mvn spring-boot:run

## Para rodar os teste execulte:
    mvn test

## Para acessar swagger:
    http://localhost:8080/swagger-ui/index.html

## Para acessar banco H2:
    http://localhost:8080/h2-console
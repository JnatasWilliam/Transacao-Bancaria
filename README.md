# 💸 Transação API

Sistema de gerenciamento de contas e transações financeiras, desenvolvido com Spring Boot e JPA. Permite o cadastro de usuários, criação de contas vinculadas, consulta de saldo e operações como depósito e transferência.

## 📦 Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- Hibernate
- MySQL
- Lombok

## 🧠 Estrutura do projeto

- `Usuario`: entidade que representa o usuário do sistema.
- `Conta`: entidade vinculada a um usuário, contendo saldo.
- `Transacao`: operações financeiras entre contas.
- `Command`: camada de escrita (criação de dados).
- `Query`: camada de leitura (consultas e relatórios).

## 🔐 Autenticação

O sistema utiliza JWT para autenticação. Após login, o token deve ser enviado no header:

Authorization: Bearer <seu_token_jwt>

---

## 📄 Endpoints principais

### 🧑‍💼 Usuário

- `POST /api/auth/register`: cria um novo usuário e conta vinculada.
- `POST /api/auth/login`: autentica e retorna o token JWT.

### 💰 Conta

- `GET /api/query/transacoes/idconta`: retorna o ID da conta do usuário autenticado.
- `GET /api/query/transacoes/saldodetalhado`: retorna saldo e dados da conta.

### 💸 Transações

- `POST /api/command/transacoes/depositar`: realiza depósito.
- `POST /api/command/transacoes/transferir`: realiza transferência entre contas.

---

## 🛠️ Configuração local

1. Clone o projeto:
   ```bash
   git clone https://github.com/seu-usuario/transacao-api.git


2 - Configure o banco de dados no src/main/resources/application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/transacoes
spring.datasource.username=root
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
3 - Rode a aplicação:
./mvnw spring-boot:run
🧪 TestesExecute os testes com:./mvnw test

🧭 Diagrama de arquitetura (texto)
Certifique-se de que o banco MySQL esteja acessível e configurado corretamente no application.properties.[CommandController]
    └── POST /depositar
    └── POST /transferir
        ↳ [Conta] ↔ [Transacao]

[QueryController]
    └── GET /saldodetalhado
    └── GET /idconta
        ↳ [Conta] ↔ [Usuario]

[MySQL] ← [Spring Data JPA] ← [Hibernate]
Separação clara entre leitura (Query) e escrita (Command), com autenticação via JWT e persistência em MySQL.

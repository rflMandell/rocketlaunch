# Rocket Launch Simulator

> Sistema para simulação e gerenciamento de lançamentos de foguetes,
> desenvolvido como Global Solution 2026-1
> Domain Driven Design (DDD) — FIAP Engenharia de Software.

---

## Descrição do Problema

O setor aeroespacial exige controle rigoroso sobre cada etapa de uma missão espacial.
Falhas no planejamento, condições climáticas adversas, janelas de lançamento
perdidas ou sobrecarga de payload podem resultar em perda de missão e prejuízos
bilionários. Este sistema modela o ciclo completo de um lançamento, do cadastro
do foguete até o registro de telemetria em voo, aplicando regras operacionais reais.

---

## Objetivo da Solução

Prover uma API REST robusta que gerencie:

- Cadastro e controle de status de foguetes e suas cargas úteis
- Planejamento de missões espaciais com controle de ciclo de vida
- Agendamento de lançamentos com validação de janelas de tempo
- Verificação de condições climáticas antes do lançamento
- Registro de telemetria durante o voo
- Detecção e tratamento automático de falhas críticas

---

## Integrantes do Grupo

| Nome                       | RM       |
|----------------------------|----------|
| Rafael Mandel              | RM560333 |
| Luis Felipe Crivellaro     | RM560877 |
| Felipe Silva do Prado Lima | RM559848 |

---

## Vídeos

| Tipo             | URL                                                                  |
|------------------|----------------------------------------------------------------------|
| 🎥 Vídeo Técnico | [Clique aqui e acesse o video Tecnico](https://youtu.be/3IW2fICThek) |
| 🎤 Vídeo Pitch   | [Clique aqui e acesse o video Pitch](https://youtu.be/xeOOzurx25M)   |
> Lembrar de substituir as URLs acima após publicar os vídeos no YouTube

---

## Decisões Arquiteturais

### Arquitetura em Camadas
```
controller  →  service  →  repository  →  banco de dados
↑
domain (entities, enums, embedded)
↑
dto (request / response)
↑
exception (handler global)
```

- **Controllers**: exclusivamente responsáveis por receber requisições e
  devolver respostas HTTP. Nenhuma regra de negócio.
- **Services**: toda a lógica de negócio, validações e orquestração.
- **Repositories**: acesso ao banco com queries customizadas via JPQL.
- **Domain**: entidades JPA com métodos de domínio encapsulados.
- **DTOs**: separação total entre o modelo de domínio e o contrato da API.

### Separação entre Domínio e API

Entidades JPA nunca são expostas diretamente. Toda entrada usa classes
`*Request` e toda saída usa classes `*Response`, com método estático `from()`
para conversão.

---

## Decisões Técnicas

| Decisão                      | Justificativa                                            |
|------------------------------|----------------------------------------------------------|
| PostgreSQL + Docker          | Banco relacional robusto, fácil de subir localmente      |
| Spring Data JPA              | Reduz boilerplate de acesso a dados com suporte a JPQL   |
| Lombok                       | Elimina getters/setters/builders repetitivos             |
| Records para DTOs            | Imutabilidade e concisão para objetos de transferência   |
| `@Transactional`             | Garante atomicidade em operações compostas               |
| `@Embedded` em RocketSpec    | Agrupa especificações técnicas sem criar tabela separada |
| `@PrePersist` / `@PreUpdate` | Auditoria automática de datas sem lógica no service      |

---

## Estratégias de Modelagem

### Entidades Centrais

| Entidade           | Descrição                                                         |
|--------------------|-------------------------------------------------------------------|
| `Rocket`           | Foguete com especificações técnicas embutidas via `RocketSpec`    |
| `Mission`          | Missão espacial com ciclo de vida controlado                      |
| `Launch`           | Evento de lançamento que conecta foguete, missão e janela         |
| `Telemetry`        | Dados coletados durante o voo (altitude, velocidade, combustível) |
| `WeatherCondition` | Condições climáticas registradas no momento do lançamento         |

### Entidades Auxiliares

| Entidade       | Descrição                                  |
|----------------|--------------------------------------------|
| `Payload`      | Carga útil transportada pelo foguete       |
| `LaunchWindow` | Janela de tempo disponível para lançamento |
| `Failure`      | Falha registrada durante a missão          |

### Relacionamentos
```
Rocket    ──<   Payload        (OneToMany)
Mission   ──<   Launch         (OneToMany)
Launch    >──   Rocket         (ManyToOne)
Launch    >──   Mission        (ManyToOne)
Launch    >──   WeatherCondition (ManyToOne)
Launch    ──<   Telemetry      (OneToMany)
Launch    ──<   Failure        (OneToMany)
Launch    >──<  LaunchWindow   (ManyToMany)
Rocket          RocketSpec     (@Embedded)
```

### Regras de Negócio Implementadas

- Lançamento só é executado se houver janela de lançamento ativa
- Vento acima de 65 km/h, visibilidade abaixo de 5 km ou precipitação
  acima de 10 mm bloqueiam o lançamento
- Foguete em `MAINTENANCE` ou `DECOMMISSIONED` não pode ser utilizado
- Peso total de payloads não pode exceder a capacidade do foguete
- Falha com severidade `CRITICAL` aborta o lançamento automaticamente
- Telemetria só pode ser registrada com lançamento `IN_FLIGHT`
- Ciclo de status da missão: `PLANNED → IN_PROGRESS → COMPLETED / FAILED`

---

## Design Patterns

### Strategy — Validação de Lançamento

Interface `LaunchValidator` com três implementações independentes,
executadas em sequência antes de cada lançamento:

- `WeatherValidator` — valida condições climáticas
- `RocketAvailabilityValidator` — valida disponibilidade do foguete
- `LaunchWindowValidator` — valida existência de janela ativa

O Spring injeta automaticamente todas as implementações via `List<LaunchValidator>`,
permitindo adicionar novas validações sem alterar o `LaunchService`.

### Builder — Construção do Launch

O `Launch.builder()` do Lombok é utilizado para construir o objeto
de lançamento de forma legível, dado que ele agrega múltiplas entidades.

### Factory Method — Criação de Telemetry

O método privado `buildTelemetry()` no `TelemetryService` centraliza
a construção do objeto `Telemetry`, isolando a lógica de criação
do fluxo principal do serviço.

---

## Tecnologias Utilizadas

| Tecnologia      | Versão | Uso                           |
|-----------------|--------|-------------------------------|
| Java            | 21     | Linguagem principal           |
| Spring Boot     | 3.4.5  | Framework principal           |
| Spring Data JPA | 3.4.5  | Persistência e ORM            |
| Hibernate       | 6.x    | Implementação JPA             |
| PostgreSQL      | 16     | Banco de dados relacional     |
| Docker          | -      | Container do banco de dados   |
| Maven           | 3.x    | Gerenciamento de dependências |
| Lombok          | -      | Redução de boilerplate        |
| Bean Validation | 3.x    | Validação de entrada          |

---

## Instruções de Execução

### Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker

### 1. Clone o repositório

```bash
git clone https://github.com/rflMandell/rocketlaunch.git
cd rocketlauch
```

### 2. Suba o banco de dados

```bash
docker-compose up -d
```

### 3. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### 4. Verifique que está rodando

```bash
curl http://localhost:8080/api/rockets
```

---

## Endpoints Principais

### Rockets — `/api/rockets`

| Método   | Endpoint                           | Descrição                  |
|----------|------------------------------------|----------------------------|
| `POST`   | `/api/rockets`                     | Cadastrar foguete          |
| `GET`    | `/api/rockets`                     | Listar foguetes (paginado) |
| `GET`    | `/api/rockets/{id}`                | Buscar foguete por ID      |
| `GET`    | `/api/rockets/status/{status}`     | Filtrar por status         |
| `PATCH`  | `/api/rockets/{id}/status?status=` | Atualizar status           |
| `DELETE` | `/api/rockets/{id}`                | Remover foguete            |

### Missions — `/api/missions`

| Método  | Endpoint                        | Descrição                 |
|---------|---------------------------------|---------------------------|
| `POST`  | `/api/missions`                 | Criar missão              |
| `GET`   | `/api/missions`                 | Listar missões (paginado) |
| `GET`   | `/api/missions/{id}`            | Buscar missão por ID      |
| `GET`   | `/api/missions/status/{status}` | Filtrar por status        |
| `PATCH` | `/api/missions/{id}/cancel`     | Cancelar missão           |

### Launches — `/api/launches`

| Método | Endpoint                                | Descrição                     |
|--------|-----------------------------------------|-------------------------------|
| `POST` | `/api/launches`                         | Agendar lançamento            |
| `GET`  | `/api/launches`                         | Listar lançamentos (paginado) |
| `GET`  | `/api/launches/{id}`                    | Buscar lançamento por ID      |
| `GET`  | `/api/launches/mission/{missionId}`     | Lançamentos de uma missão     |
| `POST` | `/api/launches/{id}/windows/{windowId}` | Associar janela               |
| `POST` | `/api/launches/{id}/execute`            | Executar lançamento           |
| `POST` | `/api/launches/{id}/complete`           | Concluir lançamento           |
| `POST` | `/api/launches/{id}/abort`              | Abortar lançamento            |

### Launch Windows — `/api/launch-windows`

| Método  | Endpoint                     | Descrição                |
|---------|------------------------------|--------------------------|
| `POST`  | `/api/launch-windows`        | Criar janela             |
| `GET`   | `/api/launch-windows`        | Listar todas as janelas  |
| `GET`   | `/api/launch-windows/{id}`   | Buscar janela por ID     |
| `GET`   | `/api/launch-windows/active` | Listar janelas ativas    |
| `PATCH` | `/api/launch-windows/expire` | Expirar janelas vencidas |

### Payloads — `/api/payloads`

| Método   | Endpoint                          | Descrição            |
|----------|-----------------------------------|----------------------|
| `POST`   | `/api/payloads`                   | Cadastrar carga útil |
| `GET`    | `/api/payloads/{id}`              | Buscar por ID        |
| `GET`    | `/api/payloads/rocket/{rocketId}` | Cargas de um foguete |
| `DELETE` | `/api/payloads/{id}`              | Remover carga útil   |

### Telemetry — `/api/launches/{launchId}/telemetry`

| Método | Endpoint                                    | Descrição                    |
|--------|---------------------------------------------|------------------------------|
| `POST` | `/api/launches/{launchId}/telemetry`        | Registrar telemetria         |
| `GET`  | `/api/launches/{launchId}/telemetry`        | Listar telemetria (paginado) |
| `GET`  | `/api/launches/{launchId}/telemetry/latest` | Último registro              |

### Failures — `/api/launches/{launchId}/failures`

| Método | Endpoint                                        | Descrição           |
|--------|-------------------------------------------------|---------------------|
| `POST` | `/api/launches/{launchId}/failures`             | Reportar falha      |
| `GET`  | `/api/launches/{launchId}/failures`             | Listar falhas       |
| `GET`  | `/api/launches/{launchId}/failures/{failureId}` | Buscar falha por ID |
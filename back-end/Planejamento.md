# 📱 API Recarga de Celular

> API REST de recarga de celular com arquitetura hexagonal, contract-first, mensageria assíncrona e autenticação JWT.

## Stack

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem |
| Spring Boot 3.x | Framework |
| Maven | Build |
| MongoDB | Banco de Dados |
| Apache Kafka | Mensageria |
| Spring Security + JWT | Autenticação |
| OpenAPI 3.0 + openapi-generator | Contract-First (geração de DTOs e interfaces) |
| Docker + Docker Compose | Infraestrutura |
| JUnit 5 + Mockito + Testcontainers | Testes |

---

## Arquitetura Hexagonal

```
recharge-api/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── src/main/resources/
│   ├── openapi/
│   │   └── recharge-api.yaml              ← Contrato (fonte de verdade)
│   └── application.yml
└── src/main/java/com/recharge/
    ├── RechargeApplication.java
    │
    ├── domain/                             ← CORE (zero dependências externas)
    │   ├── model/
    │   │   ├── Recharge.java
    │   │   ├── RechargeStatus.java
    │   │   └── User.java
    │   ├── port/
    │   │   ├── in/                         ← Ports de entrada (use cases)
    │   │   │   ├── CreateRechargeUseCase.java
    │   │   │   ├── GetRechargeHistoryUseCase.java
    │   │   │   └── AuthenticateUserUseCase.java
    │   │   └── out/                        ← Ports de saída (driven)
    │   │       ├── RechargeRepository.java
    │   │       ├── UserRepository.java
    │   │       ├── RechargeEventPublisher.java
    │   │       └── RechargeProvider.java
    │   └── service/
    │       ├── RechargeService.java
    │       └── AuthService.java
    │
    ├── adapter/
    │   ├── in/
    │   │   ├── web/                        ← REST Controllers (entrada)
    │   │   │   ├── RechargeController.java
    │   │   │   ├── AuthController.java
    │   │   │   ├── mapper/
    │   │   │   │   └── RechargeDtoMapper.java
    │   │   │   └── dto/                    ← Gerado pelo openapi-generator
    │   │   └── kafka/                      ← Kafka Consumer (entrada)
    │   │       └── RechargeEventConsumer.java
    │   └── out/
    │       ├── persistence/                ← MongoDB (saída)
    │       │   ├── MongoRechargeRepository.java
    │       │   ├── MongoUserRepository.java
    │       │   ├── document/
    │       │   │   ├── RechargeDocument.java
    │       │   │   └── UserDocument.java
    │       │   └── mapper/
    │       │       └── RechargeDocumentMapper.java
    │       ├── messaging/                  ← Kafka Producer (saída)
    │       │   └── KafkaRechargeEventPublisher.java
    │       └── provider/                   ← Mock operadora (saída)
    │           └── MockRechargeProvider.java
    │
    └── config/
        ├── SecurityConfig.java
        ├── JwtTokenProvider.java
        ├── KafkaConfig.java
        └── BeanConfig.java                ← Wiring hexagonal (ports ↔ adapters)
```

---

## Endpoints da API

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `POST` | `/api/auth/register` | Público | Registrar usuário |
| `POST` | `/api/auth/login` | Público | Login → retorna JWT |
| `POST` | `/api/auth/refresh` | JWT | Renovar token |
| `POST` | `/api/recharges` | JWT | Criar recarga → retorna 202 |
| `GET` | `/api/recharges?page=0&size=10` | JWT | Histórico do usuário (paginado) |
| `GET` | `/api/recharges/{id}` | JWT | Detalhe de uma recarga |
| `GET` | `/api/recharges/{id}/status/stream` | JWT | SSE — status em tempo real |

---

## Fluxo da Recarga

```
Usuário ──POST /api/recharges──▶ Controller
                                    │
                                    ▼
                              RechargeService
                              ┌─────┴─────┐
                              ▼           ▼
                         MongoDB      Kafka
                      (PENDING)   (recharge-requested)
                                      │
                    ◀── 202 Accepted ──┘
                                      │
                                      ▼
                              RechargeEventConsumer
                                      │
                                      ▼
                              MockRechargeProvider
                              (simula operadora)
                                      │
                                      ▼
                                   Kafka
                            (recharge-processed)
                                      │
                                      ▼
                                   MongoDB
                          (COMPLETED ou FAILED)
                                      │
                                      ▼
Usuário ──GET /api/recharges──▶ Histórico atualizado
```

---

## Fases de Desenvolvimento

### Fase 1 — Fundação

- [ ] **1.1** Criar projeto Maven com Spring Boot 3.x e Java 21
- [ ] **1.2** Adicionar dependências (`spring-boot-starter-web`, `spring-boot-starter-data-mongodb`, `spring-boot-starter-security`, `spring-kafka`, `jjwt`, `springdoc-openapi`, `openapi-generator-maven-plugin`, `lombok`, `spring-boot-starter-validation`)
- [ ] **1.3** Criar estrutura de pastas hexagonal (`domain/`, `adapter/`, `config/`)
- [ ] **1.4** Escrever contrato OpenAPI (`src/main/resources/openapi/recharge-api.yaml`)
- [ ] **1.5** Configurar `openapi-generator-maven-plugin` no `pom.xml` para gerar DTOs e interfaces
- [ ] **1.6** Criar `docker-compose.yml` (MongoDB, Kafka + Zookeeper, Kafka UI)
- [ ] **1.7** Verificar: `mvn clean compile` gera código sem erros + `docker-compose up` sobe infraestrutura

### Fase 2 — Domínio e Autenticação

- [ ] **2.1** Implementar entidades de domínio: `Recharge`, `RechargeStatus`, `User`
- [ ] **2.2** Definir ports de entrada: `CreateRechargeUseCase`, `GetRechargeHistoryUseCase`, `AuthenticateUserUseCase`
- [ ] **2.3** Definir ports de saída: `RechargeRepository`, `UserRepository`, `RechargeEventPublisher`, `RechargeProvider`
- [ ] **2.4** Implementar `RechargeService` (use cases de recarga)
- [ ] **2.5** Implementar `AuthService` (registro com bcrypt + login com geração JWT)
- [ ] **2.6** Configurar `SecurityConfig` (endpoints públicos vs. protegidos)
- [ ] **2.7** Implementar `JwtTokenProvider` (gerar/validar tokens) e `JwtAuthenticationFilter`
- [ ] **2.8** Implementar `AuthController` (`/api/auth/register`, `/api/auth/login`, `/api/auth/refresh`)
- [ ] **2.9** Verificar: Register + Login funcionam, endpoints protegidos retornam 401 sem token

### Fase 3 — Adapters e Mensageria

- [ ] **3.1** Adapter MongoDB: `RechargeDocument`, `UserDocument`, repositories, mappers domínio ↔ documento
- [ ] **3.2** Adapter Kafka Producer: `KafkaRechargeEventPublisher` publica em `recharge-requested`
- [ ] **3.3** Adapter Kafka Consumer: `RechargeEventConsumer` consome `recharge-requested`, processa via `MockRechargeProvider`, publica em `recharge-processed`
- [ ] **3.4** Consumer de `recharge-processed` atualiza status no MongoDB
- [ ] **3.5** `KafkaConfig` (serializers JSON, consumer groups, criação de tópicos)
- [ ] **3.6** `MockRechargeProvider` (simula operadora: delay aleatório, 90% sucesso)
- [ ] **3.7** Controllers REST: `RechargeController` implementa interfaces geradas pelo openapi-generator
- [ ] **3.8** `BeanConfig` — wiring hexagonal (conecta ports com adapters)
- [ ] **3.9** CORS config no `SecurityConfig` para frontend React
- [ ] **3.10** Verificar: fluxo completo funciona — criar recarga → Kafka → processamento → status atualizado

### Fase 4 — Infraestrutura e Testes

- [ ] **4.1** Criar `Dockerfile` multi-stage (stage 1: Maven build, stage 2: JRE slim)
- [ ] **4.2** Atualizar `docker-compose.yml` com serviço da aplicação (`depends_on`: MongoDB, Kafka)
- [ ] **4.3** Configurar variáveis de ambiente (MONGO_URI, KAFKA_BOOTSTRAP_SERVERS, JWT_SECRET)
- [ ] **4.4** Testes unitários: `RechargeServiceTest`, `AuthServiceTest` (JUnit 5 + Mockito)
- [ ] **4.5** Testes de integração: Testcontainers (MongoDB + Kafka), fluxo completo
- [ ] **4.6** Testes de segurança: sem JWT → 401, usuário só vê suas recargas
- [ ] **4.7** Teste de contrato: validar controllers vs. spec OpenAPI
- [ ] **4.8** Verificar: `docker-compose up` sobe tudo, `mvn test` passa, Swagger UI acessível em `/swagger-ui.html`

### Fase 5 (Futura) — Frontend React

> A ser detalhada após back-end completo.

- [ ] **5.1** Criar projeto React 18+ com TypeScript, Vite e TailwindCSS
- [ ] **5.2** Gerar client SDK TypeScript a partir do `recharge-api.yaml` (openapi-generator)
- [ ] **5.3** Páginas: Login, Register, Dashboard (histórico paginado), Nova Recarga
- [ ] **5.4** Atualização de status em tempo real via SSE (`EventSource`)
- [ ] **5.5** Autenticação: JWT em httpOnly cookie ou memory (nunca localStorage)
- [ ] **5.6** Adicionar serviço frontend no `docker-compose.yml`

---

## Decisões Técnicas

| Decisão | Justificativa |
|---|---|
| **Contract-First** | DTOs e interfaces são GERADOS a partir do YAML — não editar manualmente |
| **202 Accepted** | Processamento de recarga é assíncrono via Kafka |
| **Mock Provider** | Simula operadora (delay aleatório + 90% sucesso) — sem integração real |
| **Paginação** | `GET /api/recharges` aceita `page` e `size` — necessário para frontend |
| **CORS** | Configurado no back-end para permitir requests do frontend React |
| **Refresh Token** | Evita deslogar usuário quando JWT expira |
| **SSE** | Server-Sent Events para status em tempo real (simples e unidirecional) |

---

## Como Rodar

```bash
# Subir infraestrutura (MongoDB, Kafka, Kafka UI)
docker-compose up -d

# Build e execução
mvn clean compile
mvn spring-boot:run

# Testes
mvn test

# Swagger UI
# http://localhost:8080/swagger-ui.html
```

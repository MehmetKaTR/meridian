<div align="center">

# 🏦 Meridian

### Digital Banking & Investment Platform — Enterprise Microservices

*Çift taraflı muhasebe defteri (double-entry ledger) üzerine kurulu, çoklu döviz destekli, olay güdümlü bir fintech platformu.*

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.1.3-blue)
![Oracle](https://img.shields.io/badge/Oracle-23ai-red)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![Status](https://img.shields.io/badge/status-in%20progress-yellow)

</div>

---

## 📖 Genel Bakış

**Meridian**, modern bir dijital bankanın çekirdek yeteneklerini mikroservis mimarisiyle inşa eden bir portföy/öğrenme projesidir. Her para hareketi, **çift taraflı muhasebe (double-entry)** defterine dengeli olarak kaydedilir — para asla yoktan var olmaz, kaybolmaz; yalnızca bir hesaptan diğerine taşınır.

Proje, gerçek bankacılık kavramlarını (KYC, çoklu döviz cüzdan, muhasebe defteri, idempotent transfer) kurumsal yazılım desenleriyle (DDD, rich domain model, aggregate root, CQRS'e hazır yapı) birleştirir.

> ⚠️ **Durum:** Aktif geliştirme aşamasında. **FAZ 1 (Çekirdek & Ledger)** tamamlandı; ödemeler, kart/fatura/döviz, yatırım & canlı piyasa, kredi/risk ve frontend fazları planlı.

---

## 🏛️ Mimari

```
                        ┌─────────────────┐
      İstemci  ───────► │   API Gateway   │  (Spring Cloud Gateway, reactive)
                        └────────┬────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              ▼                  ▼                  ▼
        ┌───────────┐     ┌───────────┐     ┌───────────┐
        │ user-kyc  │     │  account  │────►│  ledger   │   (Feign: account → ledger)
        │  (8081)   │     │  (8082)   │     │  (8083)   │
        └─────┬─────┘     └─────┬─────┘     └─────┬─────┘
              │                 │                 │
         Oracle: userkyc   Oracle: accountsvc  Oracle: ledgersvc   (şema-per-servis)

        Altyapı:  Eureka (discovery) · Config Server · Oracle 23ai · Redis · Kafka
```

- **Database-per-service:** her servis kendi Oracle şemasıyla izole. Servisler arası **foreign key yok** — yalnızca id referansı; tutarlılık kod tarafında (Feign) sağlanır.
- **Servis keşfi:** Eureka. Servisler birbirini **isimle** bulur (IP yazılmaz).
- **Merkezî yapılandırma:** Spring Cloud Config.
- **Servisler arası iletişim:** OpenFeign (senkron HTTP).

---

## 🧩 Servisler

| Servis | Port | Sorumluluk | Öne çıkan |
|--------|------|------------|-----------|
| **discovery** | 8761 | Servis kayıt/keşif (Eureka Server) | — |
| **config-server** | 8888 | Merkezî yapılandırma | native backend |
| **api-gateway** | 8080 | Tek giriş kapısı, yönlendirme | reactive (WebFlux) |
| **user-kyc** | 8081 | Kullanıcılar + kimlik doğrulama (KYC) | validation, auditing |
| **account** | 8082 | Hesaplar + çoklu döviz cüzdanlar + transfer | `Money` VO, rich Wallet, Feign |
| **ledger** ⭐ | 8083 | Çift taraflı muhasebe defteri | double-entry, aggregate root, idempotency |
| **common** | — | Paylaşılan kütüphane | `BaseEntity`, `Money`, `GlobalExceptionHandler` |

---

## 💡 Öne Çıkan Kurumsal Desenler

- **Double-Entry Ledger:** Her işlem, en az iki dengeli kayıttan (borç = alacak) oluşur. `JournalEntry` bir **aggregate root** olarak denge kuralını kendi içinde zorlar — dengesiz kayıt oluşturulamaz.
- **Rich Domain Model (DDD):** İş kuralları entity'nin içinde. `Money` (negatif olamaz, farklı döviz toplanamaz), `Wallet` (yetersiz bakiye çekilemez), `JournalEntry` (denge).
- **Value Object:** `Money` — tutar + döviz, değişmez (immutable), değere göre eşitlik.
- **Idempotency:** Fişler benzersiz `reference` ile — aynı transfer iki kez işlenmez.
- **JPA Auditing:** `createdAt`/`updatedAt` otomatik (`BaseEntity`).
- **Flyway migration + `ddl-auto=validate`:** Şema versiyonlu ve elle kontrollü; Hibernate yalnızca doğrular.
- **Merkezî hata yönetimi:** `common`'daki `@RestControllerAdvice` — tutarlı 400/409 yanıtları, tüm servislerde paylaşılır.

---

## 🛠️ Teknoloji Yığını

**Çekirdek:** Java 21 · Spring Boot 4.1.1 · Spring Cloud 2025.1.3
**Mikroservis:** Eureka · Spring Cloud Gateway · Spring Cloud Config · OpenFeign
**Veri:** Oracle 23ai · Spring Data JPA / Hibernate · Flyway · Redis
**Mesajlaşma:** Apache Kafka (KRaft)
**Araçlar:** Lombok · Maven (multi-module monorepo) · Docker Compose

---

## 🗺️ Yol Haritası

| Faz | Kapsam | Durum |
|-----|--------|-------|
| **FAZ 0** | İskelet (Eureka, Config, Gateway, Compose) | ✅ Tamam |
| **FAZ 1** | Çekirdek & Ledger (user-kyc, account, ledger, iç transfer) | ✅ Tamam |
| **FAZ 2** | Ödemeler (P2P/dış, SAGA + compensation, Resilience4j, Kafka + Outbox) | 🔜 Planlı |
| **FAZ 3** | Kart · Fatura · Döviz (FX) | 🔜 Planlı |
| **FAZ 4** | Yatırım & Canlı Piyasa (WebSocket, gerçek fiyat) | 🔜 Planlı |
| **FAZ 5** | Kredi · Risk/Fraud · Analitik (CQRS) | 🔜 Planlı |
| **FAZ 6** | Olgunluk (Keycloak, Vault, gözlemlenebilirlik) + React frontend | 🔜 Planlı |

---

## 🚀 Çalıştırma

```bash
# 1) Altyapıyı kaldır (Oracle + Redis + Kafka)
docker compose up -d

# 2) Altyapı servisleri (ayrı terminaller)
cd discovery      && ./mvnw spring-boot:run
cd config-server  && ./mvnw spring-boot:run

# 3) İş servisleri
cd user-kyc && ./mvnw spring-boot:run    # :8081
cd account  && ./mvnw spring-boot:run    # :8082
cd ledger   && ./mvnw spring-boot:run    # :8083
```

**Örnek — iç transfer:**
```bash
curl -X POST http://localhost:8082/api/transfers \
  -H "Content-Type: application/json" \
  -d '{"fromWalletId":1,"toWalletId":2,"fromLedgerAccountId":1,"toLedgerAccountId":2,
       "amount":30,"currency":"TRY","reference":"transfer-001"}'
```

---

<div align="center">

**Mehmet Kaan Genç** · [GitHub](https://github.com/MehmetKaTR) · Java / Spring Boot Backend Developer

</div>

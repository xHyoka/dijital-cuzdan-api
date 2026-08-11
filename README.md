# Dijital Cüzdan API

Kullanıcıların hesap açıp para yatırma, çekme ve transfer yapabildiği RESTful bir dijital cüzdan uygulaması.

## Özellikler

- Kullanıcı kaydı ve hesap yönetimi
- Para yatırma (Deposit)
- Para çekme (Withdraw) — yetersiz bakiye kontrolü
- Kullanıcılar arası para transferi (`@Transactional` ile atomik işlem)
- İşlem geçmişi
- Katmanlı mimari (Controller / Service / Repository)
- DTO pattern, validation (`@Valid`, `@NotBlank`, `@Email`)
- Global exception handling (`@RestControllerAdvice`)

## Kullanılan Teknolojiler

- Java 21
- Spring Boot 4.1
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Maven

## Kurulum

### Gereksinimler
- Java 21+
- PostgreSQL
- Maven

### Adımlar

**1) Repoyu klonla:**
```bash
git clone https://github.com/kullaniciadin/dijital-cuzdan-api.git
cd dijital-cuzdan-api
```

**2) Veritabanı oluştur:**
PostgreSQL'de yeni bir veritabanı oluştur (örneğin: `Banka`)

**3) `application.properties` dosyasını düzenle:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/VERITABANI_ADINIZ
spring.datasource.username=KULLANICI_ADINIZ
spring.datasource.password=SIFRENIZ
spring.jpa.hibernate.ddl-auto=update
```

**4) Uygulamayı çalıştır:**
```bash
mvn spring-boot:run
```

Uygulama `http://localhost:8080` adresinde çalışmaya başlar.

## API Endpoint'leri

### Kullanıcı
| Method | URL | Açıklama |
|--------|-----|----------|
| POST | `/user/api/register` | Yeni kullanıcı kaydı |

### Hesap İşlemleri
| Method | URL | Açıklama |
|--------|-----|----------|
| POST | `/account/api/deposit/{accountId}` | Para yatırma |
| POST | `/account/api/withdraw/{accountId}` | Para çekme |
| POST | `/account/api/transfer/{fromAccountId}` | Para transferi |
| GET | `/account/api/history/{accountId}` | İşlem geçmişi |

## Örnek İstekler

### Kullanıcı Kaydı
```json
POST /user/api/register
{
    "username": "tunahan",
    "password": "1234",
    "tcKimlikNo": "12345678901",
    "email": "tunahan@test.com"
}
```

### Para Yatırma
```json
POST /account/api/deposit/1
{
    "amount": 500
}
```

### Para Transferi
```json
POST /account/api/transfer/1
{
    "toAccountId": 2,
    "amount": 100
}
```

## Mimari
Controller → Service → Repository → PostgreSQL
↑ ↑
DTO Exception
Handler


## Geliştirici

**Tunahan Can**  
GitHub: [github.com/kullaniciadin](https://github.com/xHyoka)
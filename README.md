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
-  Bakiye sorgulama
- Swagger UI ile interaktif API dokümantasyonu (http://localhost:8080/swagger-ui/index.html)
- PasswordEncoder ile güvenli şifre değiştirme endpoint'i
- JWT tabanlı kimlik doğrulama (authentication) ve yetkilendirme (authorization)
- Unit testler (JUnit 5)
- Redis ile token blacklist (logout sonrası token geçersiz kılma)
- Redis tabanlı Caching mekanizması ile performans optimizasyonu
- Docker ve Docker Compose ile containerize edilmiş çalışma ortamı

## Kullanılan Teknolojiler

- Java 21
- Spring Boot 4.1
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Maven
- Spring Security
- JWT
- JUnit 5, Mockito
- Redis (token blacklist)


## Kurulum

### Gereksinimler
- Java 21+
- PostgreSQL
- Maven

### Adımlar

**1) Repoyu klonla:**
git clone https://github.com/xHyoka/dijital-cuzdan-api.git
cd dijital-cuzdan-api

**2) Veritabanı oluştur:** PostgreSQL'de yeni bir veritabanı oluştur (örneğin: `Banka`)

**3) Redis'i çalıştır (Docker ile):**
docker run -d -p 6379:6379 --name redis-container redis

**4) `application.properties` dosyasını düzenle:**
spring.datasource.url=jdbc:postgresql://localhost:5432/VERITABANI_ADINIZ
spring.datasource.username=KULLANICI_ADINIZ
spring.datasource.password=SIFRENIZ
spring.jpa.hibernate.ddl-auto=update

spring.data.redis.host=localhost
spring.data.redis.port=6379

jwt.secret=SIZIN_GIZLI_ANAHTARINIZ
jwt.expiration=3600000

**5) Uygulamayı çalıştır:**
mvn spring-boot:run

Uygulama `http://localhost:8080` adresinde çalışmaya başlar.

## API Endpoint'leri

### Kimlik Doğrulama

| Method | URL                  | Açıklama                          |
| ------ | -------------------- | ---------------------------------- |
| POST   | `/auth/register`     | Yeni kullanıcı kaydı               |
| POST   | `/auth/login`        | Giriş yap, JWT token al            |
| POST   | `/auth/logout`       | Çıkış yap, token'ı blacklist'e ekle |

### Hesap İşlemleri

| Method | URL                                     | Açıklama       |
| ------ | ---------------------------------------- | -------------- |
| POST   | `/account/api/deposit/{accountId}`       | Para yatırma   |
| POST   | `/account/api/withdraw/{accountId}`      | Para çekme     |
| POST   | `/account/api/transfer/{fromAccountId}`  | Para transferi |
| GET    | `/account/api/history/{accountId}`       | İşlem geçmişi  |

**Not:** Hesap işlemleri endpoint'leri JWT ile korumalıdır. İsteklerde `Authorization: Bearer <token>` header'ı gönderilmelidir.

### Giriş Yapma

POST /auth/login
{
    "username": "tunahan",
    "password": "1234"
}

Response:
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

### Çıkış Yapma

POST /auth/logout
Header: Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Response: "Çıkış yapıldı"

## Mimari
Controller → Service → Repository → PostgreSQL
↑ ↑
DTO Exception
Handler


## Geliştirici

**Tunahan Can**  
GitHub: [github.com/xHyoka](https://github.com/xHyoka)

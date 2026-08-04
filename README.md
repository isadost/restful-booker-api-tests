# Restful Booker API Test Otomasyonu

Bu proje, [Restful Booker](https://restful-booker.herokuapp.com/apidoc/index.html)
servisinin REST API uçlarını Java, REST Assured ve JUnit ile otomatik olarak test eder.

## Kullanılan teknolojiler

- Java 21
- REST Assured 6
- JUnit 5
- Maven
- GitHub Actions

## Test kapsamı

| Test | HTTP metodu | Kontrol |
| --- | --- | --- |
| Rezervasyon listesini getir | `GET` | `200`, JSON ve boş olmayan liste |
| Rezervasyon oluştur | `POST` | `200`, rezervasyon kimliği ve gönderilen alanlar |
| Rezervasyonu görüntüle | `GET` | Oluşturulan kaydın alanları |
| Rezervasyonu güncelle | `PUT` | `200` ve güncellenen alanlar |
| Rezervasyonu sil | `DELETE` | `201`; ardından `GET` isteğinde `404` |

## Projeyi çalıştırma

Bilgisayarda Java 21 ve Maven kurulu olmalıdır.

```bash
mvn clean test
```

Farklı bir API adresi kullanılacaksa:

```bash
API_BASE_URL=https://ornek-api.test mvn clean test
```

## Proje yapısı

```text
src/test/java/io/github/isadost/
├── config/ApiConfig.java
└── tests/BookingApiTest.java
```

- `ApiConfig`: API'nin temel adresini yönetir.
- `BookingApiTest`: GET, POST, PUT ve DELETE testlerini içerir.
- `.github/workflows/api-tests.yml`: Testleri GitHub Actions üzerinde otomatik çalıştırır.

## Test akışı

REST Assured kodları **Given – When – Then** düzeniyle yazılmıştır:

- `given()` → İsteğin adres, başlık, parametre ve gövde hazırlığı
- `when()` → HTTP isteğinin gönderilmesi
- `then()` → Durum kodu, içerik türü ve cevap gövdesinin doğrulanması

## Mülakatta kısa anlatım

> Bu projede bir otel rezervasyon API'sini REST Assured ve JUnit ile test ettim.
> GET ile listeleme ve kayıt okuma, POST ile yeni rezervasyon oluşturma, PUT ile
> güncelleme ve DELETE ile silme senaryolarını otomatikleştirdim. Durum kodu,
> JSON içerik türü ve cevap alanlarını doğruladım. Yetki gerektiren işlemler için
> auth uç noktasından token alıp isteğe cookie olarak ekledim. Maven ile testleri
> çalıştırdım ve GitHub Actions ile sürekli test akışı oluşturdum.

## Not

Restful Booker herkese açık bir eğitim servisidir. Servis geçici olarak kapalıysa
veya internet bağlantısı yoksa testler uygulama kodundan bağımsız olarak başarısız olabilir.

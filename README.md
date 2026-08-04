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

GitHub Actions üzerinde `smoke` etiketli salt-okunur GET testi çalıştırılır. `crud`
etiketli POST, PUT ve DELETE testleri yerel ortamda `mvn clean test` komutuyla
çalıştırılır. Herkese açık Restful Booker servisi bazı bulut/CI çıkışlarından gelen
yazma isteklerine zaman zaman `418` döndürebildiği için bu ayrım CI sonucunu kararlı tutar.

## Test akışı

REST Assured kodları **Given – When – Then** düzeniyle yazılmıştır:

- `given()` → İsteğin adres, başlık, parametre ve gövde hazırlığı
- `when()` → HTTP isteğinin gönderilmesi
- `then()` → Durum kodu, içerik türü ve cevap gövdesinin doğrulanması

## Proje Hakkında

Bu proje, bir otel rezervasyon sistemine ait REST API uçlarının otomatik olarak
test edilmesini amaçlar. Rezervasyonların listelenmesi ve görüntülenmesi `GET`,
yeni rezervasyon oluşturulması `POST`, mevcut kayıtların güncellenmesi `PUT`
ve silinmesi `DELETE` senaryolarıyla kontrol edilir. Testlerde durum kodu, JSON
içerik türü ve cevap alanları doğrulanır. Yetki gerektiren işlemler için kimlik
doğrulama uç noktasından alınan token, isteklere cookie olarak eklenir. Testlerin
çalıştırılması Maven ile, sürekli test süreci ise GitHub Actions ile yönetilir.

## Not

Restful Booker herkese açık bir eğitim servisidir. Servis geçici olarak kapalıysa
veya internet bağlantısı yoksa testler uygulama kodundan bağımsız olarak başarısız olabilir.

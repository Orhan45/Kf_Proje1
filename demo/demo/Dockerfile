# Aşama 1: Uygulamayı derlemek için bir build ortamı oluştur
FROM openjdk:17-jdk-slim AS builder

# Ortam değişkenleri: Tutarlı dil ve karakter seti ayarları için
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# Çalışma dizinini belirle
WORKDIR /app

# Maven Wrapper dosyalarını ve pom.xml'i kopyala
# Bu, bağımlılıkların her derlemede yeniden indirilmesini engeller
COPY .mvn .mvn
COPY mvnw .
# Windows mvnw.cmd dosyasını sadece Windows derlemeleri için kopyala, aksi takdirde gereksiz
# COPY mvnw.cmd .
COPY pom.xml .

# mvnw betiğine çalıştırma izni ver
RUN chmod +x mvnw

# Bağımlılıkları indir (sadece pom.xml değiştiğinde yeniden çalışır)
# -B: Batch modu, etkileşimsiz çalışır
RUN ./mvnw dependency:go-offline -B

# Projenin kalan kaynak kodunu kopyala
COPY src src

# Uygulamayı derle ve JAR dosyasını oluştur
# -DskipTests: Testleri atla
# -Dproject.build.sourceEncoding=UTF-8 -Dfile.encoding=UTF-8: Karakter kodlamasını ayarla
RUN ./mvnw clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8 -Dfile.encoding=UTF-8

# Aşama 2: Nihai çalışma imajını oluştur (daha küçük ve sadece JRE içerir)
# ÖNEMLİ: jdk-slim yerine jre-slim kullanılarak görüntü boyutu önemli ölçüde küçültülür
FROM openjdk:17-jre-slim

# Çalışma dizinini belirle
WORKDIR /app

# Güvenlik için ayrı bir kullanıcı ve grup oluştur
# Uygulamanın root olmayan bir kullanıcı olarak çalışmasını sağlar
RUN groupadd --system springgroup && useradd --system --gid springgroup springuser
# app dizininin sahipliğini springuser'a ver
RUN chown -R springuser:springgroup /app

# İlk aşamada oluşturulan JAR dosyasını kopyala
# Dosya adını dinamik olarak bulmak için wildcard kullan
COPY --from=builder /app/target/*.jar app.jar

# Uygulama başladıktan sonra 8080 portunu dışarıya aç
EXPOSE 8080

# Sağlık kontrolü ekle
# Spring Boot Actuator'ı kullanıyorsanız, /actuator/health endpoint'ini kontrol edebilirsiniz.
# Bu örnek, bir HTTP GET isteği yapar ve başarılı bir yanıt bekler (200 OK).
# Eğer uygulamanızda health endpoint yoksa veya farklı bir porttaysa bu kısmı ayarlamanız gerekir.
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Uygulamayı çalıştıracak kullanıcıyı belirle
USER springuser

# Uygulamayı başlatma komutu
ENTRYPOINT ["java", "-jar", "app.jar"]


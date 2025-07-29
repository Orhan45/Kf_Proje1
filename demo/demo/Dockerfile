# Aşama 1: Uygulamayı derlemek için bir build ortamı oluştur
FROM openjdk:17-slim AS builder

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8 -Dfile.encoding=UTF-8

# Aşama 2: Nihai çalışma imajı
FROM openjdk:17-slim

WORKDIR /app

RUN groupadd --system springgroup && useradd --system --gid springgroup springuser
RUN chown -R springuser:springgroup /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

USER springuser

ENTRYPOINT ["java", "-jar", "app.jar"]

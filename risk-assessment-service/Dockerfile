FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/risk-assessment-service-*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
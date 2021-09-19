FROM openjdk:11-slim
EXPOSE 8080
WORKDIR /app
COPY build/libs/*-all.jar application.jar
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-jar", "application.jar"]

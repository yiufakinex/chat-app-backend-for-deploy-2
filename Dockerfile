FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-alpine
COPY --from=build /target/chat-app-0.0.1-SNAPSHOT.jar chat-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","chat-app.jar"]
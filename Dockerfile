FROM openjdk:17-alpine
EXPOSE 8080
ADD target/auth-0.0.1-SNAPSHOT.jar authapp.jar
ENTRYPOINT ["java","-jar", "/authapp.jar"]
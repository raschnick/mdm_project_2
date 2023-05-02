FROM openjdk:17-jdk-slim

WORKDIR /usr/src/app
COPY . .

EXPOSE 8080
CMD ["java", "-jar", "/usr/src/app/target/mdm_project_2-0.0.1-SNAPSHOT.jar"]
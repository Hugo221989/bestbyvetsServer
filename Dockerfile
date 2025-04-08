FROM openjdk:23-jdk
COPY target/vet-0.0.1-SNAPSHOT.jar /app/vet.jar
ENTRYPOINT ["java", "-jar", "/app/vet.jar"]

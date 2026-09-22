FROM eclipse-temurin:21-jdk
LABEL authors="TUNAHAN"
EXPOSE 8080
COPY target/tunahan-0.0.1-SNAPSHOT.jar tunahan-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","tunahan-0.0.1-SNAPSHOT.jar"]
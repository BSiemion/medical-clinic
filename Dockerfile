FROM openjdk:17
COPY target/medical-0.0.1-SNAPSHOT.jar medical.jar
ENTRYPOINT ["java","-jar","/medical.jar"]
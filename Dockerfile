FROM openjdk:11

RUN apt-get update
RUN apt-get install -y libsodium-dev

COPY build/libs/oracle-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
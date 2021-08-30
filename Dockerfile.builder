FROM openjdk:11

RUN apt-get update
RUN apt-get install -y libsodium-dev

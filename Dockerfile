FROM openjdk:8-jdk-alpine
CMD mkdir /tmp/safe
CMD mkdir /tmp/unsafe
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
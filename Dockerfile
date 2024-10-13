FROM openjdk:17

WORKDIR /tmp

COPY ./build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
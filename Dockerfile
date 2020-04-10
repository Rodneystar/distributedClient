FROM openjdk:11.0.6-jre-buster

COPY build/libs/distributedClient-0.0.1-SNAPSHOT.jar /app/distributedClient.jar

WORKDIR /app/

CMD ["java", "-jar", "distributedClient.jar"]
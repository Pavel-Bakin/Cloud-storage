FROM adoptopenjdk/openjdk11:alpine-jre

EXPOSE 8081

COPY target/Cloud-storage-0.0.1-SNAPSHOT.jar Cloud-storage.jar

CMD ["java", "-jar", "app.jar"]
FROM openjdk:21-jdk-slim

WORKDIR /opt/project


COPY ./AzatSiteBack ./



#COPY --from=builder /opt/project/target/Slavic-0.0.1-SNAPSHOT.jar /opt/project/target/azat.jar

EXPOSE 8089

CMD ["java", "-jar", "/opt/project/target/Slavic-0.0.1-SNAPSHOT.jar"]

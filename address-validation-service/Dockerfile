FROM maven:3.6.3-jdk-11-openj9 AS mvn

WORKDIR /usr/src/online-shop
COPY pom.xml ./
RUN mvn package dependency:go-offline # cache dependencies
COPY src ./src
RUN mvn clean package -Dservice.name=address-validation-service -DskipTests

FROM openjdk:11

EXPOSE 8080
COPY --from=mvn /usr/src/online-shop/target/runner/meecrowave-core-runner.jar /opt/meecrowave-runner.jar
COPY --from=mvn /usr/src/online-shop/target/address-validation-service.war /opt/address-validation-service.war
COPY src/main/resources/log4j2.xml /opt/log4j2.xml
ENTRYPOINT ["java", "--illegal-access=permit", "-Djava.net.preferIPv4Stack=true", "-Dlog4j2.configurationFile=/opt/log4j2.xml", "-jar", "/opt/meecrowave-runner.jar", "--webapp", "/opt/address-validation-service.war"]

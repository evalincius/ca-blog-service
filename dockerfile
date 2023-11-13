FROM eclipse-temurin:20-jdk-jammy as builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN apt-get update && \
    apt-get install dos2unix && \
    apt-get clean
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -DskipTests=true
 
FROM eclipse-temurin:20-jre-jammy
WORKDIR /opt/app
EXPOSE 8090
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/opt/app/*.jar" ]

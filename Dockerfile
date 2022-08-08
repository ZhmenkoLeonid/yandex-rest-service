FROM openjdk:11.0.12-jre
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} yandexrestapp.jar
ENTRYPOINT ["java","-jar","/yandexrestapp.jar"]
EXPOSE 80
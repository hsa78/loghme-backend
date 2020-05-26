
FROM maven:3.6.1-jdk-8 AS maven-app
WORKDIR /app
COPY src/ /app/src/
COPY pom.xml /app/pom.xml
RUN mvn clean
RUN mvn package
VOLUME /app

FROM tomcat:9.0.20-jre8 AS tomcat-app
COPY --from=maven-app /app/target/CA7_backend.war /usr/local/tomcat/webapps

CMD ["catalina.sh", "run"]
#COPY /target/CA7_backend.war /usr/local/tomcat/webapps/
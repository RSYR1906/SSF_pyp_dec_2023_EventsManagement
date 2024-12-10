# Base image with JDK used to build and run the java application
FROM maven:3.9.9-eclipse-temurin-23

ARG APP_DIR=/app

# Directory where the source code will reside
# Directory where u copy the project to (in the next step)
WORKDIR ${APP_DIR}

LABEL description="This is SSF PYP December 2023"
LABEL name="vttp5a-ssf-pyp-dec-2023"

# Copy the required files and/or directories into the image
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY src src
COPY .mvn .mvn
COPY events.json .
# Package the application using RUN directive
# This will download the dependencies defined in pom.xml
# Compile and packget to jar
RUN mvn package -Dmaven.test.skip=true

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/eventmanagement-0.0.1-SNAPSHOT.jar"]
FROM node:14 AS node_base_image 

RUN echo "NODE Version:" && node --version
RUN echo "NPM Version:" && npm --version
ARG TEST1=`node --version`

CMD ["echo", $TEST1]

WORKDIR /curiosityreact

COPY /curiosityreact .

RUN rm package-lock.json

RUN npm install
 
RUN npm run build

FROM maven:3.8.4-jdk-11-slim as maven

ARG SPRING_DATASOURCE_USERNAME 
ARG SPRING_DATASOURCE_PASSWORD
ARG SPRING_DATASOURCE_HOST
ARG SPRING_DATASOURCE_PORT
ARG SPRING_DATASOURCE_DBNAME

COPY ./pom.xml ./pom.xml
COPY ./src ./src
COPY --from=node_base_image /curiosityreact/build src/main/resources/static

RUN mvn dependency:go-offline -B

RUN mvn package

FROM openjdk:11-jre-slim


COPY --from=maven target/curiosity-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

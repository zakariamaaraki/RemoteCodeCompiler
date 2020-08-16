# Build stage

FROM maven AS BUILD_STAGE
WORKDIR /compiler
COPY . .
RUN ["mvn", "clean", "install", "-DskipTests"]

# Run stage

USER root

FROM openjdk:11.0.6-jre-slim
WORKDIR /app

COPY --from=BUILD_STAGE /compiler/target/*.jar compiler.jar

RUN apt-get update && apt-get install -y docker.io
    
ADD ./entrypoint.sh ./entry.sh    
ADD utility /compiler

RUN chmod a+x ./entry.sh

EXPOSE 8082

ENTRYPOINT ["./entry.sh"]
 

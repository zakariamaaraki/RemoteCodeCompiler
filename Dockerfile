# Build stage

FROM maven:3.6.0 AS BUILD_STAGE
WORKDIR /compiler
COPY . .
RUN ["mvn", "clean", "install", "-Dmaven.test.skip=true"]

# Run stage
FROM openjdk:11.0.6-jre-slim
WORKDIR /compiler

USER root

COPY --from=BUILD_STAGE /compiler/target/*.jar compiler.jar

RUN apt update && apt install -y docker.io
    
ADD executions executions

ADD entrypoint.sh entrypoint.sh

RUN chmod a+x ./entrypoint.sh

EXPOSE 8082

ENTRYPOINT ["./entrypoint.sh"]


# Build image by typing the following command : "docker image build . -t onlinecompiler"
# Run the container by typing the following command : "docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -t onlinecompiler"

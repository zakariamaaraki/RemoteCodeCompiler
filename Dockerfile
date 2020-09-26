# Build stage

FROM maven AS BUILD_STAGE
WORKDIR /compiler
COPY . .
RUN ["mvn", "clean", "install", "-DskipTests"]

# Run stage
FROM openjdk:11.0.6-jre-slim
WORKDIR /compiler

USER root

COPY --from=BUILD_STAGE /compiler/target/*.jar compiler.jar

RUN apt update && apt install -y docker.io
    
ADD utility utility
ADD utility_c utility_c
ADD utility_cpp utility_cpp
ADD utility_py utility_py

ADD entrypoint.sh entrypoint.sh

RUN chmod a+x ./entrypoint.sh

EXPOSE 8082

ENTRYPOINT ["./entrypoint.sh"]


# Build image by typing the following command : "docker image build . -t compilertest"  
# Run the container by typing the following command : "docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -t compilertest"

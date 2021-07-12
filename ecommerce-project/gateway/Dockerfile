FROM    openjdk:11-slim

VOLUME  /tmp
COPY    build/libs/*.jar ApiGateway.jar
ENTRYPOINT  ["java","-jar","ApiGateway.jar"]
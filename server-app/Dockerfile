FROM    openjdk:11-slim

VOLUME  /tmp
COPY    build/libs/*.jar DiscoveryService.jar
ENTRYPOINT  ["java","-jar","DiscoveryService.jar"]
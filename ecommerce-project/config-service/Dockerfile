FROM    openjdk:11-slim

VOLUME  /tmp
COPY    apiEncryptionKey.jks apiEncryptionKey.jks
COPY    build/libs/*.jar ConfigServer.jar
ENTRYPOINT  ["java","-jar","ConfigServer.jar"]
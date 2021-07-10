FROM    openjdk:11-slim
VOLUME  /tmp
COPY    build/libs/user-service-*.jar app.jar
ENTRYPOINT  ["java","-jar","app.jar"]

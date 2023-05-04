FROM openjdk:11
WORKDIR /opt/asm
COPY  target/demo-0.0.1-SNAPSHOT.jar asm.jar
CMD [ "sh", "-c", "java  -jar asm.jar" ]

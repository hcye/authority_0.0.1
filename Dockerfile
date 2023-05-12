FROM harbor.synsense-neuromorphic.com/local/openjdk
WORKDIR /opt/asm
COPY  target/demo-0.0.1-SNAPSHOT.jar asm.jar
CMD [ "sh", "-c", "java  -jar asm.jar" ]

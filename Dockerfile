FROM harbor.synsense-neuromorphic.com/local/openjdk
WORKDIR /opt/asm
COPY  target/asm-2.1.jar asm.jar
CMD [ "sh", "-c", "java  -jar asm.jar" ]

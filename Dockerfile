FROM ibmjava:8
WORKDIR /
ADD target/StudIP-Telegram-1.0-SNAPSHOT.jar StudIP-Telegram.jar
RUN mkdir -p /data
CMD java -jar StudIP-Telegram.jar
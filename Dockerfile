FROM java:8
WORKDIR /
ADD out/artifacts/StudIP_Telegram_jar/StudIP-Telegram.jar StudIP-Telegram.jar
RUN mkdir -p /StudIP/Files/
CMD java -jar StudIP-Telegram.jar

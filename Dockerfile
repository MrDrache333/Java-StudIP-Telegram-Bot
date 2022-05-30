FROM java:8
WORKDIR /
ADD out/artifacts/StudIP_Telegram_jar/StudIP-Telegram.jar StudIP-Telegram.jar
CMD java -jar StudIP-Telegram.jar
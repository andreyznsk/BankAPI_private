FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /workspace/BankApi/BankAPIMain

RUN mkdir /app

COPY /BankAPIMain/target/BankAPI.jar /app/BankAPI.jar
COPY /BankAPIMain/database.zip /app/
WORKDIR /app
RUN unzip database.zip
CMD "java" "-jar" "BankAPI.jar"


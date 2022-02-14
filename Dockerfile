FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /workspace/BankApi/BankAPIMain

RUN mkdir /app

COPY /BankAPIMain/target/BankAPI.jar /app/BankAPI.jar
COPY /BankAPIMain/database/data.sql /app/database/data.sql
WORKDIR /app
CMD "java" "-jar" "BankAPI.jar"


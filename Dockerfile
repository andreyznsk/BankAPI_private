FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /workspace/BankApi/BankAPIMain

COPY /BankAPIMain/target/BankApi.jar /
COPY /BankAPIMain/target/classes/database/data.sql /classes/database/data.sql


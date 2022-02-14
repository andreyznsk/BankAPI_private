FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /workspace/BankApi/BankAPIMain

RUN mkdir /app

COPY /BankAPIMain/target/BankAPI.jar /app/BankAPI.jar
COPY /BankAPIMain/database/*.sql /app/database/
WORKDIR /app
CMD "java" "-jar" "BankAPI.jar"


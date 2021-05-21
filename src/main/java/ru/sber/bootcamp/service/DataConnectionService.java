package ru.sber.bootcamp.service;

import ru.sber.bootcamp.model.entity.Account;

import java.util.List;

public interface DataConnectionService {


    void start();

    void stop();

    List<Account> findAllAccuont();
}

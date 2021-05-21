create table Account
(
    id IDENTITY PRIMARY KEY,
    account_number BIGINT NOT NULL,
    balance smallint DEFAULT 0,
    open_date DATE NOT NULL
);

INSERT INTO Account (account_number, balance, open_date) VALUES (1111,0,'2020-01-01');
INSERT INTO Account (account_number, balance, open_date) VALUES (1112,0,'2020-01-01');




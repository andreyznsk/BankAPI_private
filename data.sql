create table Account
(
    id IDENTITY PRIMARY KEY,
    account_number BIGINT NOT NULL UNIQUE,
    balance DECIMAL(20,2) DEFAULT 0,
    open_date DATE NOT NULL
);

INSERT INTO Account (account_number, balance, open_date) VALUES (1111,0.0,'2020-01-01');
INSERT INTO Account (account_number, balance, open_date) VALUES (1112,0.0,'2020-01-01');

create table Client(
  id IDENTITY PRIMARY KEY,
  account_number BIGINT NOT NULL,
  firstName VARCHAR(255),
  lastName VARCHAR(255),
  phone_number BIGINT UNIQUE,
  foreign key (account_number) references Account(account_number)
);

INSERT INTO Client (account_number, firstName, lastName, phone_number)
VALUES (1111,'Ivan','Ivanov', 89008001234);
INSERT INTO Client (account_number, firstName, lastName, phone_number)
VALUES (1112,'Петр','Петров', 88009001235);

CREATE TABLE Card(
    id IDENTITY PRIMARY KEY,
    account_number BIGINT NOT NULL,
    card_number BIGINT NOT NULL UNIQUE,
    date_valid_thru DATE NOT NULL,
    cvc_code SMALLINT NOT NULL,
    foreign key (account_number) references Account(account_number)
);

INSERT INTO Card (account_number, card_number, date_valid_thru, cvc_code)
VALUES (1111, 1111222233334441, '2023-01-01', 111);
INSERT INTO Card (account_number, card_number, date_valid_thru, cvc_code)
VALUES (1111, 1111222233334442, '2023-01-01', 112);
INSERT INTO Card (account_number, card_number, date_valid_thru, cvc_code)
VALUES (1112, 1112222233334441, '2023-01-01', 121);
INSERT INTO Card (account_number, card_number, date_valid_thru, cvc_code)
VALUES (1112, 1112222233334442, '2023-01-01', 122);
INSERT INTO Card (account_number, card_number, date_valid_thru, cvc_code)
VALUES (1112, 1112222233334443, '2023-01-01', 123);
INSERT INTO Card (account_number, card_number, date_valid_thru, cvc_code)
VALUES (1112, 1112222233334444, '2023-01-01', 124);




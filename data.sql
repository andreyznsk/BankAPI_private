create table Account
(
    id IDENTITY PRIMARY KEY,
    account_number BIGINT NOT NULL,
    balance smallint DEFAULT 0,
    open_date DATE NOT NULL
);

INSERT INTO Account (account_number, balance, open_date) VALUES (1111,0,'2020-01-01');
INSERT INTO Account (account_number, balance, open_date) VALUES (1112,0,'2020-01-01');

create table Client(
  id IDENTITY PRIMARY KEY,
  account_id BIGINT NOT NULL,
  firstName VARCHAR(255),
  lastName VARCHAR(255),
  phone_number BIGINT UNIQUE,
  foreign key (account_id) references Account(id)
);

INSERT INTO Client (account_id, firstName, lastName, phone_number)
VALUES (1,'Иван','Иванов', 89008001234);
INSERT INTO Client (account_id, firstName, lastName, phone_number)
VALUES (2,'Петр','Петров', 88009001235);




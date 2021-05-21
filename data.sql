create table CLIENTS
(
    ID  IDENTITY NOT NULL PRIMARY KEY,
    CUSTOMER_NAME    VARCHAR(255) NOT NULL,
    CUSTOMER_ACCOUNT BIGINT NOT NULL
);

insert into CLIENTS values ('Andrey',1111222233334444);
insert into CLIENTS values ('Sergey',1111222233334445);
insert into CLIENTS values ('Nikolay',1111222233334446);
insert into CLIENTS values ('Michail',1111222233334447);
insert into CLIENTS values ('Olga',1111222233334448);
insert into CLIENTS values ('Evgeny',1111222233334449);
insert into CLIENTS values ('Kristina',1111222233334410);
insert into CLIENTS values ('Ekaterina',1111222233334411);

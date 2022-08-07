create schema if not exists mainschema;

create table if not exists mainschema.Card(

    id number not null primary key ,
    rest number
);

insert into mainschema.Card (id, rest) values ( 6, 100 );
insert into mainschema.Card (id, rest) values ( 2, 200 );
insert into mainschema.Card (id, rest) values ( 8, 300 );
insert into mainschema.Card (id, rest) values ( 4, 400 );
insert into mainschema.Card (id, rest) values ( 10, 500 );

create table if not exists mainschema.Card1(

    id number not null primary key ,
    rest number
);

insert into mainschema.Card1 (id, rest) values ( 5, 100 );
insert into mainschema.Card1 (id, rest) values ( 1, 200 );
insert into mainschema.Card1 (id, rest) values ( 7, 300 );
insert into mainschema.Card1 (id, rest) values ( 3, 400 );
insert into mainschema.Card1 (id, rest) values ( 9, 500 );

create table if not exists mainschema.Ophistory(

    id number auto_increment not null primary key,
    trnId number,
    direction varchar(100),
    repoop varchar(100),
    card number(100),
    resultt varchar(100)

)


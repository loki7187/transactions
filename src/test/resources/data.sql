create schema if not exists mainschema;

create table if not exists mainschema.Card(

    id number not null primary key ,
    rest number
);

insert into mainschema.Card (id, rest) values ( 1, 100 );
insert into mainschema.Card (id, rest) values ( 2, 200 );
insert into mainschema.Card (id, rest) values ( 3, 300 );
insert into mainschema.Card (id, rest) values ( 4, 400 );
insert into mainschema.Card (id, rest) values ( 5, 500 );


create table person(
    id integer primary key auto_increment,
    name varchar(255),
    age integer

);

create table services(
    id integer primary key auto_increment,
    domain varchar(255),
    status boolean
);
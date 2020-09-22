create schema shop;
USE shop;
create table products
(
	id bigint auto_increment,
	name varchar(255) not null,
	price double not null,
	deleted boolean default false not null,
	constraint products_pk
		primary key (id)
);

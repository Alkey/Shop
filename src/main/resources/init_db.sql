create schema shop;
USE shop;
create table if not exists products
(
	id bigint auto_increment
		primary key,
	name varchar(255) not null,
	price double not null,
	deleted tinyint(1) default 0 not null
);

create table if not exists roles
(
	role_id bigint auto_increment
		primary key,
	role_name varchar(256) null
);

create table if not exists users
(
	user_id bigint auto_increment
		primary key,
	name varchar(256) null,
	login varchar(256) not null,
	password varchar(256) not null,
	deleted tinyint(1) default 0 not null,
	salt varbinary(500) not null,
	constraint users_login_uindex
		unique (login),
	constraint users_name_uindex
		unique (name)
);

create table if not exists orders
(
	order_id bigint auto_increment
		primary key,
	user_id bigint null,
	deleted tinyint(1) default 0 not null,
	constraint orders_users__fk
		foreign key (user_id) references users (user_id)
);

create table if not exists orders_products
(
	order_id bigint null,
	product_id bigint null,
	constraint orders_products_orders__fk
		foreign key (order_id) references orders (order_id),
	constraint orders_products_products__fk
		foreign key (product_id) references products (id)
);

create table if not exists shopping_carts
(
	cart_id bigint auto_increment
		primary key,
	user_id bigint null,
	deleted tinyint(1) default 0 not null,
	constraint shopping_carts_users__fk
		foreign key (user_id) references users (user_id)
);

create table if not exists shopping_carts_products
(
	cart_id bigint null,
	product_id bigint null,
	constraint shopping_carts_products_products__fk
		foreign key (product_id) references products (id),
	constraint shopping_carts_products_shopping_carts__fk
		foreign key (cart_id) references shopping_carts (cart_id)
);

create table if not exists users_roles
(
	user_id bigint null,
	role_id bigint null,
	constraint users_roles_roles__fk
		foreign key (role_id) references roles (role_id),
	constraint users_roles_users__fk
		foreign key (user_id) references users (user_id)
);

INSERT INTO shop.roles (role_name) VALUES ('ADMIN');
INSERT INTO shop.roles (role_name) VALUES ('USER');

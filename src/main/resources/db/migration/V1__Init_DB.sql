create sequence hibernate_sequence start 1 increment 1;

create table product (
    id int8 not null,
    filename varchar(255),
    basket_id int8,
    name varchar(255) not null ,
    price int4 not null,
    user_id int8,
    primary key (id)
);

create table basket (
    id int8 not null,
    basket_user_id int8,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table usr (
    id int8 not null,
    activation_code varchar(255),
    active boolean not null,
    email varchar(255),
    filename_avo varchar(255),
    password varchar(24) not null,
    username varchar(24) not null,
    primary key (id)
);

alter table if exists product
    add constraint product_user_fk
        foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
        foreign key (user_id) references usr;

alter table if exists product
    add constraint product_basket_fk
        foreign key (basket_id) references basket;

alter table if exists basket
    add constraint basket_user_fk
        foreign key (basket_user_id) references usr;
create table if not exists users
(
    id       bigserial primary key,
    username varchar(255) unique
);

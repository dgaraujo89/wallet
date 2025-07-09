
create user wallet_api with login encrypted password '123456';

create database wallet with owner wallet_api;

\c wallet;
create table if not exists payments(
  id uuid primary key,
  merchant_id varchar(80) not null,
  amount numeric(18,2) not null,
  currency varchar(3) not null,
  masked_card varchar(30) not null,
  encrypted_card text not null,
  status varchar(20) not null,
  created_at timestamp not null
);
grant select, insert, update, delete on table payments to vaultadmin;
alter role vaultadmin createrole;

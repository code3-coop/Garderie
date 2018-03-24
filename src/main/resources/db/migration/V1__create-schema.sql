create table parents (
  id serial,
  parent_1_name text,
  parent_2_name text,
  parent_1_phone text,
  parent_2_phone text
);
alter table parents add primary key(id);

create table "group" (
  id serial,
  name text,
  educator text
);
alter table "group" add primary key(id);

create table child (
  id serial,
  firstname text,
  lastname text,
  birthdate date,
  image_url text,
  parents bigint,
  "group" bigint
);

alter table child add primary key(id);
alter table child add foreign key (parents) references parents(id);
alter table child add foreign key ("group") references "group"(id);

create table presence (
 date date,
 state text, --present, absent, toCheck
 child bigint,
 absence_reason text,
 last_modification timestamp,
 author text
);

alter table presence add primary key(date, child);
alter table presence add foreign key (child) references child(id);

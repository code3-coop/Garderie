alter table presence add column day_part text default 'am';
alter table presence drop constraint presence_pkey;

alter table presence add primary key(date, child_id, day_part);

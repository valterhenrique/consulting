# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table customer (
  id                            bigint not null,
  name                          varchar(255),
  address                       varchar(255),
  city                          varchar(255),
  state                         varchar(255),
  postcode                      varchar(255),
  phone                         varchar(255),
  email                         varchar(255),
  constraint pk_customer primary key (id)
);
create sequence customer_seq;

create table invoice (
  id                            bigint not null,
  invoice_id                    varchar(255),
  customer_id                   bigint not null,
  date                          date,
  constraint pk_invoice primary key (id)
);
create sequence invoice_seq;

create table invoice_detail (
  id                            bigint not null,
  service_id                    bigint not null,
  description                   varchar(255),
  amount                        double,
  invoice_id                    bigint not null,
  constraint pk_invoice_detail primary key (id)
);
create sequence invoice_detail_seq;

create table service (
  id                            bigint not null,
  description                   varchar(255),
  amount                        double,
  constraint pk_service primary key (id)
);
create sequence service_seq;

alter table invoice add constraint fk_invoice_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_invoice_customer_id on invoice (customer_id);

alter table invoice_detail add constraint fk_invoice_detail_service_id foreign key (service_id) references service (id) on delete restrict on update restrict;
create index ix_invoice_detail_service_id on invoice_detail (service_id);

alter table invoice_detail add constraint fk_invoice_detail_invoice_id foreign key (invoice_id) references invoice (id) on delete restrict on update restrict;
create index ix_invoice_detail_invoice_id on invoice_detail (invoice_id);


# --- !Downs

alter table invoice drop constraint if exists fk_invoice_customer_id;
drop index if exists ix_invoice_customer_id;

alter table invoice_detail drop constraint if exists fk_invoice_detail_service_id;
drop index if exists ix_invoice_detail_service_id;

alter table invoice_detail drop constraint if exists fk_invoice_detail_invoice_id;
drop index if exists ix_invoice_detail_invoice_id;

drop table if exists customer;
drop sequence if exists customer_seq;

drop table if exists invoice;
drop sequence if exists invoice_seq;

drop table if exists invoice_detail;
drop sequence if exists invoice_detail_seq;

drop table if exists service;
drop sequence if exists service_seq;


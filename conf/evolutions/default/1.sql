# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "FOLLOWINGS" ("ID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"CREATED_AT" TIMESTAMP NOT NULL,"USER_ID" BIGINT NOT NULL,"FOLLOWING_ID" BIGINT NOT NULL);
create table "POSTS" ("ID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"CREATED_AT" TIMESTAMP NOT NULL,"MESSAGE" VARCHAR NOT NULL,"USER_ID" BIGINT NOT NULL);
create table "USERS" ("ID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"CREATED_AT" TIMESTAMP NOT NULL,"U_ID" VARCHAR NOT NULL,"NAME" VARCHAR NOT NULL,"BIO" VARCHAR,"LOCATION" VARCHAR);
create unique index "idx_uid" on "USERS" ("U_ID");
alter table "FOLLOWINGS" add constraint "FOLLOWING_USER_ID_FK" foreign key("USER_ID") references "USERS"("ID") on update NO ACTION on delete NO ACTION;
alter table "FOLLOWINGS" add constraint "FOLLOWING_FOLLOWING_ID_FK" foreign key("FOLLOWING_ID") references "USERS"("ID") on update NO ACTION on delete NO ACTION;
alter table "POSTS" add constraint "POST_FK" foreign key("USER_ID") references "USERS"("ID") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "FOLLOWINGS" drop constraint "FOLLOWING_USER_ID_FK";
alter table "FOLLOWINGS" drop constraint "FOLLOWING_FOLLOWING_ID_FK";
alter table "POSTS" drop constraint "POST_FK";
drop table "FOLLOWINGS";
drop table "POSTS";
drop table "USERS";

CREATE DATABASE bugtracker;

USE bugtracker;

DROP TABLE IF EXISTS bugs;
CREATE TABLE bugs (
  id int not null auto_increment primary key,
  category int not null,
  priority int not null,
  title varchar(40) not null,
  description varchar(255),
  indate datetime,
  outdate datetime,
  reporter varchar(40),
  bugstate int
);

DROP TABLE IF EXISTS category;
CREATE TABLE category (
  id int not null primary key,
  title varchar(40) not null,
  description varchar(255) 
);

INSERT INTO category (id, title) VALUES (1, 'Tag Library');
INSERT INTO category (id, title) VALUES (2, 'Kernel/SQL,DB');
INSERT INTO category (id, title) VALUES (3, 'Kernel/EventHandling');

DROP TABLE IF EXISTS priority;
CREATE TABLE priority (
  id int not null primary key,
  title varchar(40) not null,
  description varchar(255) 
);

INSERT INTO priority (id, title) VALUES (1, 'high');
INSERT INTO priority (id, title) VALUES (2, 'middle');
INSERT INTO priority (id, title) VALUES (3, 'low');

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
  phone varchar(15),
  bugstate int,
  contactFirst tinyint default '0',
  server_cat_id int,
  server_model_id int
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


DROP TABLE IF EXISTS server_categories;
CREATE TABLE server_categories (
  id int(11) not null primary key,
  name varchar(45)
);

INSERT INTO server_categories VALUES (1,'Mainframe');
INSERT INTO server_categories VALUES (2,'Mini');
INSERT INTO server_categories VALUES (3,'PC');


DROP TABLE IF EXISTS server_models;
CREATE TABLE server_models (
  id int(11) not null primary key,
  cat_id int(11) not null,
  name varchar(45)
);

INSERT INTO server_models VALUES (1,1,'IBM 3090');
INSERT INTO server_models VALUES (2,1,'IBM 4300');
INSERT INTO server_models VALUES (3,1,'IBM 9121');
INSERT INTO server_models VALUES (4,1,'Unisys A-Series');
INSERT INTO server_models VALUES (5,1,'Unisys 2200-Series');
INSERT INTO server_models VALUES (6,2,'IBM AS/400');
INSERT INTO server_models VALUES (7,2,'Unisys 5000');
INSERT INTO server_models VALUES (8,3,'Compaq ProLiant DL360');
INSERT INTO server_models VALUES (9,3,'Compaq ProLiant DL760');
INSERT INTO server_models VALUES (10,3,'Compaq ProLiant ML330');
INSERT INTO server_models VALUES (11,3,'Compaq ProLiant ML570');
INSERT INTO server_models VALUES (12,3,'Dell PowerEdge 1650');
INSERT INTO server_models VALUES (13,3,'Dell PowerEdge 4600');
INSERT INTO server_models VALUES (14,3,'Dell PowerEdge 6600');
INSERT INTO server_models VALUES (15,3,'HP lh3000');
INSERT INTO server_models VALUES (16,3,'HP lh6000');
INSERT INTO server_models VALUES (17,3,'HP tc2100');
INSERT INTO server_models VALUES (18,3,'HP tc4100');

//
//  tested on sapdb 7.3
//
//DROP TABLE bugs
//
CREATE TABLE bugs (
  id integer not null default serial ,
  category integer not null,
  priority integer not null,
  title varchar(40) not null,
  description varchar(255),
  indate date,
  outdate date,
  reporter varchar(40),
  phone varchar(15),
  bugstate int,
  primary key (id)
)
//
//DROP TABLE category
CREATE TABLE category (
  id integer not null,
  title varchar(40) not null,
  description varchar(255),
  primary key (id)
)
//
INSERT INTO category (id, title) VALUES (1, 'Tag Library')
//
INSERT INTO category (id, title) VALUES (2, 'Kernel/SQL,DB')
//
INSERT INTO category (id, title) VALUES (3, 'Kernel/EventHandling')
//
//DROP TABLE priority
//
CREATE TABLE priority (
  id integer not null,
  title varchar(40) not null,
  description varchar(255),
  primary key (id)
)
//
INSERT INTO priority (id, title) VALUES (1, 'high')
//
INSERT INTO priority (id, title) VALUES (2, 'middle')
//
INSERT INTO priority (id, title) VALUES (3, 'low')



//DROP TABLE server_categories;
CREATE TABLE server_categories (
  id integer not null,
  name varchar(45),
  primary key (id)
);

INSERT INTO server_categories VALUES (1,'Mainframe');
INSERT INTO server_categories VALUES (2,'Mini');
INSERT INTO server_categories VALUES (3,'PC');


//DROP TABLE server_models;
CREATE TABLE server_models (
  id integer not null,
  cat_id integer not null,
  name varchar(45),
  primary key (id)
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
INSERT INTO server_models VALUES (15,3,'HP lc2000');
INSERT INTO server_models VALUES (15,3,'HP lh3000');
INSERT INTO server_models VALUES (16,3,'HP lh6000');
INSERT INTO server_models VALUES (17,3,'HP tc2100');
INSERT INTO server_models VALUES (18,3,'HP tc4100');





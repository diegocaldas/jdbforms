/* create bugtracker-database on PostgreSQL */
/* (tested on 7.0.3) */

/* attention: make sure you are using a different database/scheme than */
/* for dbforms-testsuite, because there may be conflicts in table-names and -definitions */

DROP SEQUENCE sq_bugs;
CREATE SEQUENCE sq_bugs;
DROP TABLE bugs;
CREATE TABLE bugs (
  id INTEGER DEFAULT nextval('sq_bugs'),
  category INTEGER not null,
  priority INTEGER not null,
  title VARCHAR(40) not null,
  description VARCHAR(255),
  indate DATE,
  outdate DATE,
  reporter VARCHAR(40),
  phone VARCHAR(15),  
  bugstate INTEGER,  
  PRIMARY KEY (id)
);


DROP TABLE category;
CREATE TABLE category (
  id INTEGER not null,
  title VARCHAR(40) not null,
  description VARCHAR(255),
  PRIMARY KEY (id)
);

INSERT INTO category (id, title) VALUES (1, 'Tag Library');
INSERT INTO category (id, title) VALUES (2, 'Kernel/SQL,DB');
INSERT INTO category (id, title) VALUES (3, 'Kernel/EventHandling');
INSERT INTO category (id, title) VALUES (4, 'DevGui');

DROP TABLE  priority;
CREATE TABLE priority (
  id INTEGER not null,
  title VARCHAR(40) not null,
  description VARCHAR(255),
  PRIMARY KEY (id)
);

INSERT INTO priority (id, title) VALUES (1, 'high');
INSERT INTO priority (id, title) VALUES (2, 'middle');
INSERT INTO priority (id, title) VALUES (3, 'low');


DROP TABLE server_categories;
CREATE TABLE server_categories (
  id INTEGER not null,
  name VARCHAR(45),
  PRIMARY KEY (id)
);

INSERT INTO server_categories VALUES (1,'Mainframe');
INSERT INTO server_categories VALUES (2,'Mini');
INSERT INTO server_categories VALUES (3,'PC');


DROP TABLE server_models;
CREATE TABLE server_models (
  id INTEGER not null,
  cat_id INTEGER not null,
  name VARCHAR(45),
  PRIMARY KEY (id)
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

/* end */

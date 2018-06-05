/* create bugtracker-database on ORACLE */
/* (tested on or.8.1.6) */

/* thanks to Jana Ramakrishnan who contributed a similar file */

/* attention: make sure you are using a different database/scheme than */
/* for dbforms-testsuite, because there may be conflicts in table-names and -definitions */

DROP TABLE bugs;

DROP SEQUENCE sq_bugs;
CREATE SEQUENCE sq_bugs increment by 1 start with 1;


CREATE TABLE bugs (
  id number(11) not null,
  category number(11) not null,
  priority number(11) not null,
  title varchar(40) not null,
  description varchar(255),
  indate date,
  outdate date,
  reporter varchar(40),
  phone varchar(15),
  bugstate number(11),
  contactFirst tinyint(4) default '0',
  server_cat_id int(11),
  server_model_id int(11),  
  PRIMARY KEY (id)
);


CREATE TRIGGER bugs_trigger
before insert on bugs
for each row
begin
  select sq_bugs.nextval into :new.id FROM dual;
end;
/

DROP TABLE category;
CREATE TABLE category (
  id number(11) not null,
  title varchar(40) not null,
  description varchar(255),
  PRIMARY KEY (id)
);

INSERT INTO category (id, title) VALUES (1, 'Tag Library');
INSERT INTO category (id, title) VALUES (2, 'Kernel/SQL,DB');
INSERT INTO category (id, title) VALUES (3, 'Kernel/EventHandling');
INSERT INTO category (id, title) VALUES (4, 'DevGui');

DROP TABLE  priority;
CREATE TABLE priority (
  id number(11) not null,
  title varchar(40) not null,
  description varchar(255),
  PRIMARY KEY (id)
);

INSERT INTO priority (id, title) VALUES (1, 'high');
INSERT INTO priority (id, title) VALUES (2, 'middle');
INSERT INTO priority (id, title) VALUES (3, 'low');


DROP TABLE server_categories;
CREATE TABLE server_categories (
  id int(11) not null,
  name varchar(45),
  PRIMARY KEY (id)
);

INSERT INTO server_categories VALUES (1,'Mainframe');
INSERT INTO server_categories VALUES (2,'Mini');
INSERT INTO server_categories VALUES (3,'PC');


DROP TABLE server_models;
CREATE TABLE server_models (
  id int(11) not null,
  cat_id int(11) not null,
  name varchar(45),
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

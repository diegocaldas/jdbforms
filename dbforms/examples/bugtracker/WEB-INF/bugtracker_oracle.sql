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
  bugstate number(11),  
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

/* end */

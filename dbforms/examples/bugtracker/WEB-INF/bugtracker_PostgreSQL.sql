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

/* end */

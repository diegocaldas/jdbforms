/************************************************** 
   DBFORMS bugtracker database - InterBase
   Myles McLeod (mylesmcleod@mindspring.com)
   6/17/2001
***************************************************/

/*
 create example database on InterBase 5.5
*/

CREATE DATABASE 'c:\DBFORMS\data\bugtracker.gdb';
CONNECT 'c:\DBFORMS\data\bugtracker.gdb';

/*
 Dumping data for table 'bugs'
*/

DROP TABLE bugs;

/*
 Table structure for table 'bugs'
*/

CREATE TABLE bugs (
  id INTEGER not null,
  category INTEGER not null,
  priority INTEGER not null,
  title varchar(40) not null,
  description varchar(255),
  indate date,
  outdate date,
  reporter varchar(40),
  phone varchar(40),
  bugstate INTEGER,  
  PRIMARY KEY (id)
);

/*
 Generator for table 'bugs'
*/

DELETE FROM RDB$GENERATORS WHERE RDB$GENERATOR_NAME = 'BUGS_GEN';
CREATE GENERATOR bugs_gen;

/*
 Trigger for table 'bugs'
*/

SET TERM !! ;

CREATE TRIGGER bugs_trigger FOR bugs
BEFORE INSERT AS
BEGIN
  new.id = GEN_ID(bugs_gen,1);
END!!

SET TERM ; !!

/*
 Dumping data for table 'category'
*/

DROP TABLE category;
CREATE TABLE category (
  id INTEGER not null,
  title varchar(40) not null,
  description varchar(255),
  PRIMARY KEY (id)
);

INSERT INTO category (id, title) VALUES (1, 'Tag Library');
INSERT INTO category (id, title) VALUES (2, 'Kernel/SQL,DB');
INSERT INTO category (id, title) VALUES (3, 'Kernel/EventHandling');
INSERT INTO category (id, title) VALUES (4, 'DevGui');

/*
 Dumping data for table 'priority'
*/

DROP TABLE  priority;
CREATE TABLE priority (
  id INTEGER not null,
  title varchar(40) not null,
  description varchar(255),
  PRIMARY KEY (id)
);

INSERT INTO priority (id, title) VALUES (1, 'high');
INSERT INTO priority (id, title) VALUES (2, 'middle');
INSERT INTO priority (id, title) VALUES (3, 'low');

COMMIT;

/* end */

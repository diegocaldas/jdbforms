/* create example database on PostgreSQL */
/* (tested on 7.0.3) */

/* Table structure for table 'customer' */

DROP TABLE customer;
CREATE TABLE customer (
  id INTEGER DEFAULT '0' NOT NULL,
  firstname VARCHAR(50),
  lastname VARCHAR(50),
  address VARCHAR(30),
  pcode VARCHAR(10),
  city VARCHAR(40),
  PRIMARY KEY (id)
);

/* example data for table 'customer' */

INSERT INTO customer VALUES (1,'Frank','Berger','Oberalm 23','3232','Steinakirchen');
INSERT INTO customer VALUES (2,'Lord','Vader','Death Planet','21445','Alpha Taurus 2');
INSERT INTO customer VALUES (3,'Sandra','Finninger','5th Avenue','2323','Bearberry');
INSERT INTO customer VALUES (6,'John','King','Rosegarden 23','3568','Mondsville');

/* Table structure for table 'orders' */

DROP SEQUENCE sq_orders;
CREATE SEQUENCE sq_orders;


DROP TABLE orders;
CREATE TABLE orders (
  order_id INTEGER DEFAULT nextval('sq_orders'),
  customer_id INTEGER DEFAULT '0' NOT NULL,
  service_id INTEGER,
  orderdate DATE,
  PRIMARY KEY (order_id,customer_id)
);

/* example data for table 'orders' */

INSERT INTO orders VALUES (1,1,204,'12-12-2001');
INSERT INTO orders VALUES (2,1,203,'11-12-2001');



/* Table structure for table 'complaint' */



DROP SEQUENCE sq_complaint;
CREATE SEQUENCE sq_complaint;

DROP TABLE complaint;
CREATE TABLE complaint (
  complaint_id INTEGER DEFAULT nextval('sq_complaint'),
  customer_id INTEGER DEFAULT '0' NOT NULL,
  usermessage VARCHAR(255),
  incomingdate DATE,
  priority INTEGER,
  PRIMARY KEY (complaint_id,customer_id)
);

/* example data for table 'complaint' */

INSERT INTO complaint VALUES (1,1,'my dog has stomache trouble!','14-12-2001',3);
INSERT INTO complaint VALUES (2,1,'my citty has stomache trouble, too! what did you give her?!','15-12-2001',3);


/* Table structure for table 'priority' */

DROP TABLE priority;
CREATE TABLE priority (
  id VARCHAR(10) DEFAULT '' NOT NULL,
  shortname VARCHAR(30),
  description VARCHAR(100),
  PRIMARY KEY (id)
);

/* example data for table 'priority' */

INSERT INTO priority VALUES ('1','low','nothing to fear');
INSERT INTO priority VALUES ('2','middle','problems with customer (making bla bla on the phone)');
INSERT INTO priority VALUES ('3','high','pet services inc.? never heard about it');

/* Table structure for table 'service' */

DROP TABLE service;
CREATE TABLE service (
  id INTEGER DEFAULT '0' NOT NULL,
  name VARCHAR(30),
  description VARCHAR(255),
  PRIMARY KEY (id)
);

/* example data for table 'service' */

INSERT INTO service VALUES (100,'dog walking','talking the dog for a walk');
INSERT INTO service VALUES (101,'Crocodile walk','talking the croco for a walk');
INSERT INTO service VALUES (201,'feeding the hamster','');
INSERT INTO service VALUES (202,'feeding the parrot','');
INSERT INTO service VALUES (203,'feeding the cat','');
INSERT INTO service VALUES (204,'feeding the dog','');
INSERT INTO service VALUES (300,'riding the horse','');

/* Table structure for table 'pets' */


DROP SEQUENCE sq_pets;
CREATE SEQUENCE sq_pets;

DROP TABLE pets;
CREATE TABLE pets (
  pet_id INTEGER DEFAULT nextval('sq_pets'),
  name VARCHAR(30),
  customer INTEGER,
  portrait_pic VARCHAR(255),
  story OID,
  PRIMARY KEY (pet_id)
);


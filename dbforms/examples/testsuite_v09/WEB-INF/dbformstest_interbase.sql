/*
 create example database on InterBase 5.5
*/

CREATE DATABASE 'c:\DBFORMS\data\dbformstest.gdb';
CONNECT 'c:\DBFORMS\data\dbformstest.gdb';

/*
 Dumping data for table 'customer'
*/

DROP TABLE customer;

/*
 Table structure for table 'customer'
*/

CREATE TABLE customer (
  id INTEGER DEFAULT 0 NOT NULL,
  firstname VARCHAR(50),
  lastname VARCHAR(50),
  address VARCHAR(30),
  pcode VARCHAR(10),
  city VARCHAR(40),
  PRIMARY KEY (id)
);

INSERT INTO customer VALUES (1,'Frank','Berger','Oberalm 23','3232','Steinakirchen');
INSERT INTO customer VALUES (2,'Lord','Vader','Death Planet','21445','Alpha Taurus 2');
INSERT INTO customer VALUES (3,'Sandra','Finninger','5th Avenue','2323','Bearberry');
INSERT INTO customer VALUES (6,'John','King','Rosegarden 23','3568','Mondsville');

/*
 Dumping data for table 'orders'
*/

DROP TABLE orders;

/*
 Table structure for table 'orders'
*/

CREATE TABLE orders (
  order_id INTEGER DEFAULT 0 NOT NULL,
  customer_id INTEGER DEFAULT 0 NOT NULL,
  service_id INTEGER,
  orderdate DATE,
  PRIMARY KEY (order_id,customer_id)
);


/*
 Generator for table 'orders'
*/

DELETE FROM RDB$GENERATORS WHERE RDB$GENERATOR_NAME = 'ORDERS_GEN';
CREATE GENERATOR orders_gen;

/*
 Trigger for table 'orders'
*/

SET TERM !! ;

CREATE TRIGGER orders_trigger FOR orders
BEFORE INSERT AS
BEGIN
  new.order_id = GEN_ID(orders_gen,1);
END!!

SET TERM ; !!


INSERT INTO orders VALUES (1,1,204,'12/12/2001');
INSERT INTO orders VALUES (2,1,203,'12/13/2001');

/*
 Dumping data for table 'complaint'
*/

DROP TABLE complaint;

/*
 Table structure for table 'complaint'
*/

CREATE TABLE complaint (
  complaint_id INTEGER DEFAULT 0 NOT NULL ,
  customer_id INTEGER DEFAULT 0 NOT NULL,
  usermessage VARCHAR(255),
  incomingdate DATE,
  priority INTEGER,
  PRIMARY KEY (complaint_id,customer_id)
);

/*
 Generator for table 'complaint'
*/

DELETE FROM RDB$GENERATORS WHERE RDB$GENERATOR_NAME = 'COMPLAINT_GEN';
CREATE GENERATOR complaint_gen;

/*
 Trigger for table 'complaint'
*/

SET TERM !! ;

CREATE TRIGGER complaint_trigger FOR complaint
BEFORE INSERT AS
BEGIN
  new.complaint_id = GEN_ID(complaint_gen,1);
END!!

SET TERM ; !!


INSERT INTO complaint VALUES (1,1,'my dog has stomache trouble!','12/14/2001',3);
INSERT INTO complaint VALUES (2,1,'my cat has stomache trouble, too! what did you give her?!','12/15/2001',3);

/*
 Dumping data for table 'priority'
*/

DROP TABLE priority;

/*
 Table structure for table 'priority'
*/

CREATE TABLE priority (
  id VARCHAR(10) DEFAULT '' NOT NULL,
  shortname VARCHAR(30),
  description VARCHAR(100),
  PRIMARY KEY (id)
);

INSERT INTO priority VALUES ('1','low','nothing to fear');
INSERT INTO priority VALUES ('2','middle','problems with customer (making bla bla on the phone)');
INSERT INTO priority VALUES ('3','high','pet services inc.? never heard about it');

/*
 Dumping data for table 'service'
*/

DROP TABLE service;

/*
 Table structure for table 'service'
*/

CREATE TABLE service (
  id INTEGER DEFAULT 0 NOT NULL,
  name VARCHAR(30),
  description VARCHAR(255),
  PRIMARY KEY (id)
);

INSERT INTO service VALUES (100,'dog walking','talking the dog for a walk');
INSERT INTO service VALUES (101,'Crocodile walk','talking the croco for a walk');
INSERT INTO service VALUES (201,'feeding the hamster','');
INSERT INTO service VALUES (202,'feeding the parrot','');
INSERT INTO service VALUES (203,'feeding the cat','');
INSERT INTO service VALUES (204,'feeding the dog','');
INSERT INTO service VALUES (300,'riding the horse','');

/*
 Dumping data for table 'pets'
*/

DROP TABLE pets;

/*
 Table structure for table 'pets'
*/

CREATE TABLE pets (
  pet_id INTEGER DEFAULT 0 NOT NULL,
  customer INTEGER,
  name VARCHAR(30),
  portrait_pic VARCHAR(255),
  story BLOB,
  PRIMARY KEY (pet_id)
);

/*
 Generator for table 'complaint'
*/

DELETE FROM RDB$GENERATORS WHERE RDB$GENERATOR_NAME = 'PETS_GEN';
CREATE GENERATOR pets_gen;

/*
 Trigger for table 'pets'
*/

SET TERM !! ;

CREATE TRIGGER pets_trigger FOR pets
BEFORE INSERT AS
BEGIN
  new.pet_id = GEN_ID(pets_gen,1);
END!!

SET TERM ; !!

COMMIT;



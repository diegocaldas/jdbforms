/* create example database on ORACLE */
/* (tested on or.8.1.6) */


/* Table structure for table 'customer' */


DROP TABLE customer;

CREATE TABLE customer (
  id number(11) DEFAULT '0' NOT NULL,
  firstname varchar(50),
  lastname varchar(50),
  address varchar(30),
  pcode varchar(10),
  city varchar(40),
  PRIMARY KEY (id)
);

/* example data for table 'customer' */

INSERT INTO customer VALUES (1,'Frank','Berger','Oberalm 23','3232','Steinakirchen');
INSERT INTO customer VALUES (2,'Lord','Vader','Death Planet','21445','Alpha Taurus 2');
INSERT INTO customer VALUES (3,'Sandra','Finninger','5th Avenue','2323','Bearberry');
INSERT INTO customer VALUES (6,'John','King','Rosegarden 23','3568','Mondsville');



/* Table structure for table 'orders' */


DROP TABLE orders;

DROP SEQUENCE sq_orders;
CREATE SEQUENCE sq_orders increment by 1 start with 1;


CREATE TABLE orders (
  order_id number(11) DEFAULT '0' NOT NULL,
  customer_id number(11) DEFAULT '0' NOT NULL,
  service_id number(11),
  orderdate date,
  PRIMARY KEY (order_id,customer_id)
);

CREATE TRIGGER orders_trigger
BEFORE INSERT ON orders
FOR EACH ROW
BEGIN
  SELECT sq_orders.nextval INTO :new.order_id FROM dual;
END;
/

/* example data for table 'orders' */

INSERT INTO orders VALUES (1,1,204,'12-12-2001');
INSERT INTO orders VALUES (2,1,203,'11-12-2001');



/* Table structure for table 'complaint' */


DROP TABLE complaint;

DROP SEQUENCE sq_complaint;
CREATE SEQUENCE sq_complaint increment by 1 start with 1;

CREATE TABLE complaint (
  complaint_id number(11) DEFAULT '0' NOT NULL,
  customer_id number(11) DEFAULT '0' NOT NULL,
  usermessage varchar(255),
  incomingdate date,
  priority number(11),
  PRIMARY KEY (complaint_id,customer_id)
);

CREATE TRIGGER complaint_trigger
BEFORE INSERT ON complaint
FOR EACH ROW
BEGIN
  SELECT sq_complaint.nextval INTO :new.complaint_id FROM dual;
END;
/

/* example data for table 'complaint' */

INSERT INTO complaint VALUES (1,1,'my dog has stomache trouble!','14-12-2001',3);
INSERT INTO complaint VALUES (2,1,'my citty has stomache trouble, too! what did you give her?!','15-12-2001',3);


/* Table structure for table 'priority' */

DROP TABLE priority;

CREATE TABLE priority (
  id varchar(10) DEFAULT '' NOT NULL,
  shortname varchar(30),
  description varchar(100),
  PRIMARY KEY (id)
);

/* example data for table 'priority' */

INSERT INTO priority VALUES ('1','low','nothing to fear');
INSERT INTO priority VALUES ('2','middle','problems with customer (making bla bla on the phone)');
INSERT INTO priority VALUES ('3','high','pet services inc.? never heard about it');

/* Table structure for table 'service' */

DROP TABLE service;

CREATE TABLE service (
  id number(11) DEFAULT '0' NOT NULL,
  name varchar(30),
  description varchar(255),
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

DROP TABLE pets;

DROP SEQUENCE sq_pets;
CREATE SEQUENCE sq_pets increment by 1 start with 1;

CREATE TABLE pets (
  pet_id number(11) DEFAULT '0' NOT NULL,  
  name varchar(30),
  customer number(11),
  portrait_pic varchar(255),
  story blob,
  PRIMARY KEY (pet_id)
);

CREATE TRIGGER pets_trigger
BEFORE INSERT ON pets
FOR EACH ROW
BEGIN
  SELECT sq_pets.nextval INTO :new.pet_id FROM dual;
END;
/

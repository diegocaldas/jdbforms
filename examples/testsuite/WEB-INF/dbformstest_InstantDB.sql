; contributed by Marco Azzalini

; First load the JDBC driver and open a database.
d jdbc.idbDriver;
o jdbc:idb=dbforms.prp;

;
; create example database on MySQL
;

; e CREATE DATABASE dbformstest


;
; Table structure for table 'customer'
;

e DROP TABLE customer;
e CREATE TABLE customer (
  id int DEFAULT '0' PRIMARY KEY NOT NULL,
  firstname char(50),
  lastname char(50),
  address char(30),
  pcode char(10),
  city char(40)
);

;
; Dumping data for table 'customer'
;

e INSERT INTO customer VALUES (1,'Frank','Berger','Oberalm 23','3232','Steinakirchen');
e INSERT INTO customer VALUES (2,'Lord','Vader','Death Planet','21445','Alpha Taurus 2');
e INSERT INTO customer VALUES (3,'Sandra','Finninger','5th Avenue','2323','Bearberry');
e INSERT INTO customer VALUES (6,'John','King','Rosegarden 23','3568','Mondsville');

;
; Table structure for table 'orders'
;

e DROP TABLE orders;
e CREATE TABLE orders (
  order_id int DEFAULT '0' PRIMARY KEY NOT NULL AUTO INCREMENT,
  customer_id int DEFAULT '0' NOT NULL,
  service_id int,
  orderdate date
);

e SET INCREMENT_BASE 1 ON orders.order_id;

;
; Dumping data for table 'orders'
;

e INSERT INTO orders VALUES (1,1,204,'2001/12/15');
e INSERT INTO orders VALUES (2,1,203,'2001/12/13');

;
; Table structure for table 'complaint'
;

e DROP TABLE complaint;
e CREATE TABLE complaint (
  complaint_id int DEFAULT '0' PRIMARY KEY NOT NULL AUTO INCREMENT,
  customer_id int DEFAULT '0' NOT NULL,
  usermessage char(255),
  incomingdate date,
  priority int
);

e SET INCREMENT_BASE 1 ON complaint.complaint_id;

;
; Dumping data for table 'complaint'
;

e INSERT INTO complaint VALUES (1,1,'my dog has stomache trouble!','2001/12/14',3);
e INSERT INTO complaint VALUES (2,1,'my cat has stomache trouble, too! what did you give her?!','2001/12/15',3);

;
; Table structure for table 'priority'
;

e DROP TABLE  priority;
e CREATE TABLE priority (
  id char(10) DEFAULT '' PRIMARY KEY NOT NULL,
  shortname char(30),
  description char(100)
);

;
; Dumping data for table 'priority'
;

e INSERT INTO priority VALUES ('1','low','nothing to fear');
e INSERT INTO priority VALUES ('2','middle','problems with customer (making bla bla on the phone)');
e INSERT INTO priority VALUES ('3','high','pet services inc.? never heard about it');

;
; Table structure for table 'service'
;

e DROP TABLE  service;
e CREATE TABLE service (
  id int DEFAULT '0' PRIMARY KEY NOT NULL,
  name char(30),
  description char(255)
);

;
; Dumping data for table 'service'
;

e INSERT INTO service VALUES (100,'dog walking','talking the dog for a walk');
e INSERT INTO service VALUES (101,'Crocodile walk','talking the croco for a walk');
e INSERT INTO service VALUES (201,'feeding the hamster','');
e INSERT INTO service VALUES (202,'feeding the parrot','');
e INSERT INTO service VALUES (203,'feeding the cat','');
e INSERT INTO service VALUES (204,'feeding the dog','');
e INSERT INTO service VALUES (300,'riding the horse','');

;
; Table structure for table 'pets'
;

e DROP TABLE pets;
e CREATE TABLE pets (
  pet_id int DEFAULT '0' PRIMARY KEY NOT NULL AUTO INCREMENT,
  customer int,
  name varchar(30),
  portrait_pic varchar(255),
  story binary
);

; e SET INCREMENT_BASE 1 ON pets.pet_id;

c close;

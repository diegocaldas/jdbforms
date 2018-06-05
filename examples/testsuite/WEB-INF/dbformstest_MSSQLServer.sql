-- contributed by Marco Azzalini

---- create example database on MySQL




--CREATE DATABASE dbformstest
--GO


-- Table structure for table 'customer'


DROP TABLE  customer;
CREATE TABLE customer (
  id int DEFAULT '0' NOT NULL,
  firstname char(50),
  lastname char(50),
  address char(30),
  pcode char(10),
  city char(40),
  PRIMARY KEY (id)
);

--
-- Dumping data for table 'customer'
--

INSERT INTO customer VALUES (1,'Frank','Berger','Oberalm 23','3232','Steinakirchen');
INSERT INTO customer VALUES (2,'Lord','Vader','Death Planet','21445','Alpha Taurus 2');
INSERT INTO customer VALUES (3,'Sandra','Finninger','5th Avenue','2323','Bearberry');
INSERT INTO customer VALUES (6,'John','King','Rosegarden 23','3568','Mondsville');

--
-- Table structure for table 'orders'
--

DROP TABLE  orders;
CREATE TABLE orders (
  order_id int  IDENTITY(1,1) NOT NULL ,
  customer_id int  DEFAULT '0' NOT NULL,
  service_id int ,
  orderdate datetime,
  PRIMARY KEY (order_id,customer_id)
);

--
-- Dumping data for table 'orders'
--

INSERT INTO orders (customer_id,service_id,orderdate) VALUES (1,204,'12/12/2001');
INSERT INTO orders (customer_id,service_id,orderdate) VALUES (1,203,'11/11/2001');

--
-- Table structure for table 'complaint'
--

DROP TABLE  complaint;
CREATE TABLE complaint (
  complaint_id int  IDENTITY(1,1)  NOT NULL ,
  customer_id int  DEFAULT '0' NOT NULL,
  usermessage char(255),
  incomingdate datetime,
  priority int ,
  PRIMARY KEY (complaint_id,customer_id)
);

--
-- Dumping data for table 'complaint'
--

INSERT INTO complaint (customer_id,usermessage,incomingdate,priority) VALUES (1,'my dog has stomache trouble!','12/10/2001',3);
INSERT INTO complaint (customer_id,usermessage,incomingdate,priority) VALUES (1,'my cat has stomache trouble, too! what did you give her?!','12/10/2001',3);

--
-- Table structure for table 'priority'
--

DROP TABLE  priority;
CREATE TABLE priority (
  id char(10) DEFAULT '' NOT NULL,
  shortname char(30),
  description char(100),
  PRIMARY KEY (id)
);

--
-- Dumping data for table 'priority'
--

INSERT INTO priority VALUES ('1','low','nothing to fear');
INSERT INTO priority VALUES ('2','middle','problems with customer (making bla bla on the phone)');
INSERT INTO priority VALUES ('3','high','pet services inc.? never heard about it');

--
-- Table structure for table 'service'
--

DROP TABLE  service;
CREATE TABLE service (
  id int  DEFAULT '0' NOT NULL,
  name char(30),
  description char(255),
  PRIMARY KEY (id)
);

--
-- Dumping data for table 'service'
--

INSERT INTO service VALUES (100,'dog walking','talking the dog for a walk');
INSERT INTO service VALUES (101,'Crocodile walk','talking the croco for a walk');
INSERT INTO service VALUES (201,'feeding the hamster','');
INSERT INTO service VALUES (202,'feeding the parrot','');
INSERT INTO service VALUES (203,'feeding the cat','');
INSERT INTO service VALUES (204,'feeding the dog','');
INSERT INTO service VALUES (300,'riding the horse','');

--
-- Table structure for table 'pets'
--

DROP TABLE  pets;
CREATE TABLE pets (
  pet_id int  IDENTITY(1,1) NOT NULL,
  customer int ,
  name varchar(30),
  portrait_pic varchar(255),
  story varbinary(4000),
  PRIMARY KEY (pet_id)
);

--
-- Dumping data for table 'pets'
--



drop table book;
drop table author;
-- -----------------------------------------------------------------------
-- author
-- -----------------------------------------------------------------------
create table author
(
    author_id INTEGER NOT NULL IDENTITY ,
    name VARCHAR (50) NOT NULL ,
    organisation VARCHAR (50)
);
-- -----------------------------------------------------------------------
-- book
-- -----------------------------------------------------------------------
create table book
(
    book_id INTEGER NOT NULL IDENTITY,
    isbn VARCHAR (15) NOT NULL ,
    author_id INTEGER NOT NULL ,
    title VARCHAR (255) DEFAULT 'N/A' NOT NULL ,
    FOREIGN KEY (author_id)
    REFERENCES author (author_id)

);
CREATE INDEX book_isbn ON book (isbn);

CREATE TABLE TIMEPLAN 
(
   time TIMESTAMP PRIMARY KEY, 
   REMARK VARCHAR(255)
);

CREATE TABLE KEYTEST(KEY_1 VARCHAR(10),KEY_2 VARCHAR(10),KEY_3 VARCHAR(10),KEY_4 INTEGER);
CREATE TABLE BLOBTEST(NAME VARCHAR(255),FILE VARCHAR(255),FILENAME VARCHAR(255),FILEDATA LONGVARBINARY);
CREATE TABLE LIBRARY(LIBRARY_ID INTEGER NOT NULL IDENTITY PRIMARY KEY,COMPANYNAME VARCHAR(50) NOT NULL);
CREATE TABLE CUSTOMER(CUSTOMER_ID INTEGER NOT NULL IDENTITY PRIMARY KEY,NAME VARCHAR(50) NOT NULL);
CREATE TABLE CUSTOMER_LIBRARY(CUSTOMER_ID INTEGER NOT NULL,LIBRARY_ID INTEGER NOT NULL);

-- -----------------------------------------------------------------------
-- insert some data
-- -----------------------------------------------------------------------
INSERT INTO AUTHOR VALUES(1,'Eco, Umberto','organisation 11')
INSERT INTO AUTHOR VALUES(2,'Douglas, Adam','organisation 2')
INSERT INTO BOOK VALUES(1,'3-423-12445-4',1,'Die Insel des vorigen Tages')
INSERT INTO BOOK VALUES(2,'3-423-12445-5',1,'Das Foucaltsche Pendel')
INSERT INTO BOOK VALUES(3,'42-1',2,'Hijacking through the Galaxy 1')
INSERT INTO BOOK VALUES(4,'42-2',2,'Hijacking through the Galaxy 2')
INSERT INTO BOOK VALUES(5,'42-3',2,'Hijacking through the Galaxy 3')
INSERT INTO BOOK VALUES(6,'42-4',2,'Hijacking through the Galaxy 4')
INSERT INTO BOOK VALUES(7,NULL,NULL,'Test null value')
INSERT INTO BOOK VALUES(8,'42-5',2,'Luca''s favorite thing to eat is  "delicious Italian pasta"')
INSERT INTO BOOK VALUES(9,'42-6',2,'Hijacking through the Galaxy 6')
INSERT INTO KEYTEST VALUES('key1','key2','key3',1)
INSERT INTO KEYTEST VALUES('key1','key2','key3',2)
INSERT INTO KEYTEST VALUES('key1','key2','key3',3)
INSERT INTO KEYTEST VALUES('key1','key2','key3',4)
INSERT INTO KEYTEST VALUES('key1','key2','key3',5)
INSERT INTO TIMEPLAN VALUES('1900-01-01 00:00:00.0',2.3E0,2,NULL)
INSERT INTO TIMEPLAN VALUES('2002-10-11 00:00:00.0',3.4E0,3,'2. Testvalue2')
INSERT INTO TIMEPLAN VALUES('2100-10-10 00:00:00.0',4.54E0,3,'1. Testvalue test')
INSERT INTO LIBRARY VALUES(1,'First Library inc')
INSERT INTO LIBRARY VALUES(2,'Second Library inc')
INSERT INTO CUSTOMER VALUES(1,'First customer John Kazzus')
INSERT INTO CUSTOMER VALUES(2,'Second customer Joanna LaSilona')
INSERT INTO CUSTOMER VALUES(3,'Third customer Alabao Masino')
INSERT INTO CUSTOMER VALUES(4,'Fourth customer Meo Mara')
INSERT INTO CUSTOMER_LIBRARY VALUES(1,1)
INSERT INTO CUSTOMER_LIBRARY VALUES(1,2)
INSERT INTO CUSTOMER_LIBRARY VALUES(2,3)
INSERT INTO CUSTOMER_LIBRARY VALUES(2,4)
INSERT INTO CUSTOMER_LIBRARY VALUES(2,2)

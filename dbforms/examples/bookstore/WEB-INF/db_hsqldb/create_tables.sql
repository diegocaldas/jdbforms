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

-- -----------------------------------------------------------------------
-- insert some data
-- -----------------------------------------------------------------------
INSERT INTO AUTHOR  (author_id, name, organisation) VALUES(1, 'Eco, Umberto','organisation 1');
INSERT INTO AUTHOR  (author_id, name, organisation) VALUES(2, 'Douglas, Adam','organisation 2');
INSERT INTO BOOK  (book_id, isbn, author_id, title) VALUES(1, '3-423-12445-4',1,'Die Insel des vorigen Tages');
INSERT INTO BOOK  (book_id, isbn, author_id, title) VALUES(2, '3-423-12445-5',1'Das Foucaltsche Pendel');
INSERT INTO BOOK  (book_id, isbn, author_id, title) VALUES(3, '42-1',2,'Hijacking through the Galaxy 1');
INSERT INTO BOOK  (book_id, isbn, author_id, title) VALUES(4, '42-2',2,'Hijacking through the Galaxy 2');
INSERT INTO BOOK  (book_id, isbn, author_id, title) VALUES(5, '42-3',2,'Hijacking through the Galaxy 3');
INSERT INTO BOOK  (book_id, isbn, author_id, title) VALUES(6, '42-4',2,'Hijacking through the Galaxy 4');
INSERT INTO BOOK  (book_id, isbn, author_id, title) VALUES(7, '42-5',2,'Hijacking through the Galaxy 5');
INSERT INTO TIMEPLAN (time, remark) values ('2001-10-10', '1. Testvalue');
INSERT INTO TIMEPLAN (time, remark) values ('2001-10-11', '2. Testvalue');


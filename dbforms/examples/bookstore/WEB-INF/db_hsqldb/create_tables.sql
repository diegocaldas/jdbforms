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

-- -----------------------------------------------------------------------
-- insert some data
-- -----------------------------------------------------------------------
INSERT INTO AUTHOR  (author_id, name, organisation) VALUES(1, '1 Eco, Umberto','test2121');
INSERT INTO AUTHOR  (author_id, name, organisation) VALUES(2, '2 test','test');
INSERT INTO AUTHOR  (author_id, name, organisation) VALUES(3, '3 tes','redsasdf21111');
INSERT INTO AUTHOR  (author_id, name, organisation) VALUES(4, '4 tes','redsasdf');
INSERT INTO BOOK  (book_id, isbn, author_id, title) VALUES(5, '3-423-12445-4',1,'Die Insel des vorigen Tages');

drop table book;

drop table author;

-- -----------------------------------------------------------------------
-- author
-- -----------------------------------------------------------------------

create table author
(
    author_id INTEGER NOT NULL ,
    name VARCHAR (50) NOT NULL ,
    organisation VARCHAR (50) NULL ,
    PRIMARY KEY (author_id)
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


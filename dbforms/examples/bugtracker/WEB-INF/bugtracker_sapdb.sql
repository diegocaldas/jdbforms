//
//  tested on sapdb 7.3
//
//DROP TABLE bugs
//
CREATE TABLE bugs (
  id integer not null default serial ,
  category integer not null,
  priority integer not null,
  title varchar(40) not null,
  description varchar(255),
  indate date,
  outdate date,
  reporter varchar(40),
  bugstate int,
  primary key (id)
)
//
//DROP TABLE category
CREATE TABLE category (
  id integer not null,
  title varchar(40) not null,
  description varchar(255),
  primary key (id)
)
//
INSERT INTO category (id, title) VALUES (1, 'Tag Library')
//
INSERT INTO category (id, title) VALUES (2, 'Kernel/SQL,DB')
//
INSERT INTO category (id, title) VALUES (3, 'Kernel/EventHandling')
//
//DROP TABLE priority
//
CREATE TABLE priority (
  id integer not null,
  title varchar(40) not null,
  description varchar(255),
  primary key (id)
)
//
INSERT INTO priority (id, title) VALUES (1, 'high')
//
INSERT INTO priority (id, title) VALUES (2, 'middle')
//
INSERT INTO priority (id, title) VALUES (3, 'low')


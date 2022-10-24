USE fplibrarydb;

DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS publication;
DROP TABLE IF EXISTS participant;
DROP TABLE IF EXISTS author;

CREATE TABLE author (
  id INT NOT NULL AUTO_INCREMENT,
  fullname VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id));

CREATE TABLE publication (
  id INT NOT NULL AUTO_INCREMENT,
  publication_name VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id));

CREATE TABLE book (
  id INT NOT NULL AUTO_INCREMENT,
  author_id INT NOT NULL,
  title VARCHAR(255) NOT NULL,
  publication_id INT NOT NULL,
  publication_year INT UNSIGNED NOT NULL,
  quantity INT UNSIGNED NOT NULL,
  available INT UNSIGNED NOT NULL,
  isbn VARCHAR(13) NOT NULL UNIQUE,
  PRIMARY KEY (id),
  CONSTRAINT fk_publication 
    FOREIGN KEY (publication_id)
    REFERENCES publication (id) ON DELETE RESTRICT
								ON UPDATE CASCADE,
  CONSTRAINT fk_author 
    FOREIGN KEY (author_id)
    REFERENCES author (id) ON DELETE RESTRICT
						   ON UPDATE CASCADE,
  UNIQUE (author_id, title, publication_id, publication_year));

CREATE TABLE participant (
  id INT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  user_password VARCHAR(40) NOT NULL,
  first_name VARCHAR(20) NOT NULL,
  last_name VARCHAR(20) NOT NULL,
  phone VARCHAR(12) NOT NULL UNIQUE,
  passport VARCHAR(8) NOT NULL UNIQUE,
  user_role ENUM('admin', 'librarian', 'reader') NOT NULL,
  is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id));

CREATE TABLE wishlist (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  book_id INT NOT NULL,
  state ENUM('new', 'processed', 'canceled', 'completed') NOT NULL DEFAULT 'new',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  return_time TIMESTAMP NULL,
  fine INT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT fk_book
    FOREIGN KEY (book_id)
    REFERENCES book (id) ON DELETE CASCADE
						 ON UPDATE CASCADE,
  CONSTRAINT fk_user
    FOREIGN KEY (user_id)
    REFERENCES participant (id) ON DELETE RESTRICT
								ON UPDATE CASCADE);


INSERT INTO participant VALUES (DEFAULT, 'admin@library.com', SHA1('nimda'), 'Mykola', 'Petrenko', '380970000000', 'AA000000', 'admin', DEFAULT); 
INSERT INTO participant VALUES (DEFAULT, 'lib1@library.com', SHA1('lib1'), 'Oksana', 'Stovbenko', '380971111111', 'AA111111', 'librarian', DEFAULT); 
INSERT INTO participant VALUES (DEFAULT, 'reader1@gmail.com', SHA1('read1'), 'Reader1', 'Readerenko1', '380970000001', 'AA000001', 'reader', DEFAULT); 
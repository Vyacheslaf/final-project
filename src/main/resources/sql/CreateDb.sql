DROP DATABASE IF EXISTS fplibrarydb;
CREATE DATABASE fplibrarydb DEFAULT CHARACTER SET utf8mb4;

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
  passport VARCHAR(8) NOT NULL,
  user_role ENUM('admin', 'librarian', 'reader') NOT NULL,
  is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  UNIQUE (passport, user_role));

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

DELIMITER &&
CREATE PROCEDURE insert_book (IN var_author VARCHAR(255), 
							  IN var_title VARCHAR(255), 
                              IN var_publication_name VARCHAR(255),
                              IN var_publication_year INT UNSIGNED,
                              IN var_quantity INT UNSIGNED,
                              IN var_isbn VARCHAR(13))
BEGIN
--  INSERT IGNORE INTO author VALUES (DEFAULT, var_author);
  INSERT INTO author (fullname) 
    SELECT * FROM (SELECT var_author AS fullname) AS tmp 
    WHERE NOT EXISTS (SELECT id FROM author WHERE fullname = var_author) LIMIT 1;
--  INSERT IGNORE INTO publication VALUES (DEFAULT, var_publication);
  INSERT INTO publication (publication_name) 
    SELECT * FROM (SELECT var_publication_name AS publication_name) AS tmp 
    WHERE NOT EXISTS (SELECT id FROM publication WHERE publication_name = var_publication_name) LIMIT 1;
  INSERT INTO book VALUES (DEFAULT, 
						 (SELECT id FROM author WHERE fullname = var_author), 
                         var_title,
                         (SELECT id FROM publication WHERE publication_name = var_publication_name),
                         var_publication_year, var_quantity, var_quantity, var_isbn);
--  SET @author_id = (SELECT id FROM author WHERE fullname = var_author);
--  SET @publication_id = (SELECT id FROM publication WHERE publication_name = var_publication_name);
--  INSERT INTO book (author_id, title, publication_id, publication_year, quantity, available) 
--    SELECT * FROM (SELECT @author_id AS author_id, 
--						  var_title AS title, 
--                          @publication_id AS publication_id, 
--                          var_publication_year AS publication_year,
--                          var_quantity AS quantity,
--                          var_quantity AS available) AS tmp
--    WHERE NOT EXISTS (SELECT id FROM book WHERE author_id = @author_id 
--											AND title = var_title 
--                                            AND publication_id = @publication_id
--                                            AND publication_year = var_publication_year) LIMIT 1;
END &&
DELIMITER ;

DELIMITER &&
CREATE PROCEDURE update_book (IN var_author VARCHAR(255), 
							  IN var_title VARCHAR(255), 
                              IN var_publication_name VARCHAR(255),
                              IN var_publication_year INT UNSIGNED,
                              IN var_quantity INT UNSIGNED,
                              IN var_isbn VARCHAR(13),
                              IN var_available INT UNSIGNED,
                              IN var_id INT)
BEGIN
--  INSERT IGNORE INTO author VALUES (DEFAULT, var_author);
  INSERT INTO author (fullname) 
    SELECT * FROM (SELECT var_author AS fullname) AS tmp 
    WHERE NOT EXISTS (SELECT id FROM author WHERE fullname = var_author) LIMIT 1;
--  INSERT IGNORE INTO publication VALUES (DEFAULT, var_publication);
  INSERT INTO publication (publication_name) 
    SELECT * FROM (SELECT var_publication_name AS publication_name) AS tmp 
    WHERE NOT EXISTS (SELECT id FROM publication WHERE publication_name = var_publication_name) LIMIT 1;
  UPDATE book SET author_id = (SELECT id FROM author WHERE fullname = var_author),
				  title = var_title,
                  publication_id = (SELECT id FROM publication WHERE publication_name = var_publication_name),
                  publication_year = var_publication_year, 
                  quantity = var_quantity,
                  available = var_available,
                  isbn = var_isbn
			WHERE id = var_id;
END &&
DELIMITER ;

INSERT INTO participant VALUES (DEFAULT, 'admin@library.com', SHA1('nimda'), 'Mykola', 'Petrenko', '380979289766', 'AA000000', 'admin', DEFAULT); 
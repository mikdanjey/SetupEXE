CREATE TABLE IF NOT EXISTS users (
  id         INT(10)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  last_name  VARCHAR(50) NOT NULL,
  email      VARCHAR(50) NOT NULL UNIQUE,
  created_at TIMESTAMP                        DEFAULT 0,
  updated_at TIMESTAMP                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO users (first_name, last_name, email) VALUES ('Mani', 'Kandan', 'mani@gmail.com');

SELECT *
FROM users;
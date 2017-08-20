CREATE TABLE IF NOT EXISTS users (
  id         INT(10)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  last_name  VARCHAR(50) NOT NULL,
  email      VARCHAR(50) NOT NULL UNIQUE,
  created_at timestamp   NULL DEFAULT NULL,
  updated_at timestamp   NULL DEFAULT NULL
);
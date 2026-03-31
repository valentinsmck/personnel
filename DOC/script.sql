CREATE DATABASE IF NOT EXISTS personnel
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE personnel;

CREATE TABLE IF NOT EXISTS LIGUE(
   id_ligue INT AUTO_INCREMENT,
   nom_ligue VARCHAR(255) NOT NULL,
   PRIMARY KEY(id_ligue),
   UNIQUE(nom_ligue)
);

CREATE TABLE IF NOT EXISTS EMPLOYE(
   id_employe INT AUTO_INCREMENT,
   prenom_employe VARCHAR(255) NOT NULL,
   mail_employe VARCHAR(255) NOT NULL,
   password_employe VARCHAR(255) NOT NULL,
   nom_employe VARCHAR(255) NOT NULL,
   rôle_employe VARCHAR(50),
   date_arrivee_employe DATE,
   date_départ_employe VARCHAR(50),
   id_ligue INT,
   PRIMARY KEY(id_employe),
   UNIQUE(mail_employe),
   FOREIGN KEY(id_ligue) REFERENCES LIGUE(id_ligue)
);

CREATE USER IF NOT EXISTS 'personnel_user'@'localhost' IDENTIFIED BY 'personnel_pwd';
GRANT ALL PRIVILEGES ON personnel.* TO 'personnel_user'@'localhost';
FLUSH PRIVILEGES;

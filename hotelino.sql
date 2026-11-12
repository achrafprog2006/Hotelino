CREATE DATABASE IF NOT EXISTS hotelino;
USE hotelino;

CREATE TABLE IF NOT EXISTS chambre (
  id_chambre INT AUTO_INCREMENT PRIMARY KEY,
  numero INT NOT NULL UNIQUE,
  capacite INT NOT NULL,
  prix DECIMAL(10,2) NOT NULL,
  statut TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS client (
  id_client INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(50) NOT NULL,
  prenom VARCHAR(50) NOT NULL,
  cin VARCHAR(20) NOT NULL UNIQUE,
  telephone VARCHAR(20),
  email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS reservation (
  id_reservation INT AUTO_INCREMENT PRIMARY KEY,
  id_client INT NOT NULL,
  id_chambre INT NOT NULL,
  date_arrivee DATE NOT NULL,
  date_depart DATE NOT NULL,
  statut TINYINT NOT NULL DEFAULT 0,
  montant_total DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (id_client) REFERENCES client(id_client),
  FOREIGN KEY (id_chambre) REFERENCES chambre(id_chambre)
);

USE hotelino;

-- Test Data
INSERT INTO chambre (numero, capacite, prix, statut) VALUES
(101, 1, 450.00, 0),
(104, 2, 650.00, 0),
(105, 2, 700.00, 1)
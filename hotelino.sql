CREATE DATABASE IF NOT EXISTS hotelino;
USE hotelino;

CREATE TABLE IF NOT EXISTS chambre (
    id_chambre INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    etage INT NOT NULL,
    type TINYINT NOT NULL DEFAULT 0,
    capacite INT NOT NULL,
    prix_par_nuit DECIMAL(10,2) NOT NULL,
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
    date_creation DATE NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    nb_personnes INT NOT NULL,
    statut TINYINT NOT NULL DEFAULT 0,
    montant_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_client) REFERENCES client(id_client),
    FOREIGN KEY (id_chambre) REFERENCES chambre(id_chambre)
);

CREATE TABLE IF NOT EXISTS paiement (
    id_paiement INT AUTO_INCREMENT PRIMARY KEY,
    id_reservation INT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_paiement DATE NOT NULL,
    mode TINYINT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_reservation) REFERENCES reservation(id_reservation) ON DELETE CASCADE
);

USE hotelino;

INSERT INTO chambre (numero, etage, type, capacite, prix_par_nuit, statut) VALUES
('101', 1, 0, 1,  450.00, 0),
('104', 1, 1, 2,  650.00, 0),
('105', 1, 1, 2,  700.00, 1),
('201', 2, 2, 4, 1200.00, 0),
('202', 2, 1, 3,  800.00, 2);

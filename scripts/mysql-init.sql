-- Script d'initialisation MySQL pour Docker
-- Ce script est exécuté lors de l'initialisation du conteneur MySQL

USE medilabo_patients;

-- Création de la table patients si elle n'existe pas
CREATE TABLE IF NOT EXISTS patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    gender VARCHAR(1) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(255)
);

-- Vérification que la table a été créée
SHOW TABLES;
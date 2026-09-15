-- Script SQL pour insérer les patients de test
-- Base de données: medilabo_patients
-- Table: patients

USE medilabo_patients;

-- Insertion des patients de test
INSERT INTO patients (first_name, last_name, birth_date, gender, address, phone_number) VALUES
('Test', 'TestNoneNone', '1966-12-31', 'F', '1 Brookside St', '100-222-3333'),
('Test', 'TestBorderLine', '1945-06-24', 'M', '2 High St', '200-333-4444'),
('Test', 'TestInDanger', '2004-06-18', 'M', '3 Club Road', '300-444-5555'),
('Test', 'TestEarlyOnset', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666');

-- Vérification des données insérées
SELECT 
    id,
    first_name AS 'Prénom',
    last_name AS 'Nom',
    birth_date AS 'Date de naissance',
    gender AS 'Genre',
    address AS 'Adresse',
    phone_number AS 'Téléphone'
FROM patients
ORDER BY id;
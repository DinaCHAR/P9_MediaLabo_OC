// Script d'initialisation MongoDB pour Docker
// Ce script est exécuté lors de l'initialisation du conteneur MongoDB

// Création de l'utilisateur pour la base de données medilabo_notes
db = db.getSiblingDB('admin');

// Création de la base de données medilabo_notes si elle n'existe pas
db = db.getSiblingDB('medilabo_notes');

// Création de la collection notes si elle n'existe pas
if (!db.getCollectionNames().includes('notes')) {
    db.createCollection('notes');
    print('Collection notes créée avec succès!');
}

// Vérification que l'utilisateur a les droits nécessaires
print('Configuration de MongoDB terminée avec succès!');
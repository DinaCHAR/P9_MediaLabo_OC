// Script d'insertion des notes avec encodage UTF-8 correct
// Force UTF-8 encoding
print('Setting UTF-8 encoding...');

// Connexion à la base de données avec authentification
// Note: Dans le contexte d'initialisation Docker, ce script sera exécuté avec les identifiants admin
use('medilabo_notes');

// Suppression des notes existantes
print('Suppression des notes existantes...');
db.notes.deleteMany({});

// Insertion avec caractères Unicode explicites pour corriger les accents
db.notes.insertMany([
  {
    patient_id: 1,
    content: "Le patient d\u00e9clare qu'il 'se sent tr\u00e8s bien' Poids \u00e9gal ou inf\u00e9rieur au poids recommand\u00e9",
    created_at: new Date('2023-12-01T10:00:00Z'),
    updated_at: new Date('2023-12-01T10:00:00Z')
  },
  {
    patient_id: 2,
    content: "Le patient d\u00e9clare qu'il ressent beaucoup de stress au travail Il se plaint \u00e9galement que son audition est anormale derni\u00e8rement",
    created_at: new Date('2023-12-01T09:00:00Z'),
    updated_at: new Date('2023-12-01T09:00:00Z')
  },
  {
    patient_id: 2,
    content: "Le patient d\u00e9clare avoir fait une r\u00e9action aux m\u00e9dicaments au cours des 3 derniers mois Il remarque \u00e9galement que son audition continue d'\u00eatre anormale",
    created_at: new Date('2023-12-02T14:30:00Z'),
    updated_at: new Date('2023-12-02T14:30:00Z')
  },
  {
    patient_id: 3,
    content: "Le patient d\u00e9clare qu'il fume depuis peu",
    created_at: new Date('2023-12-01T11:00:00Z'),
    updated_at: new Date('2023-12-01T11:00:00Z')
  },
  {
    patient_id: 3,
    content: "Le patient d\u00e9clare qu'il est fumeur et qu'il a cess\u00e9 de fumer l'ann\u00e9e derni\u00e8re Il se plaint \u00e9galement de crises d'apn\u00e9e respiratoire anormales Tests de laboratoire indiquant un taux de cholest\u00e9rol LDL \u00e9lev\u00e9",
    created_at: new Date('2023-12-02T15:00:00Z'),
    updated_at: new Date('2023-12-02T15:00:00Z')
  },
  {
    patient_id: 4,
    content: "Le patient d\u00e9clare qu'il lui est devenu difficile de monter les escaliers Il se plaint \u00e9galement d'\u00eatre essouffl\u00e9 Tests de laboratoire indiquant que les anticorps sont \u00e9lev\u00e9s R\u00e9action aux m\u00e9dicaments",
    created_at: new Date('2023-12-01T12:00:00Z'),
    updated_at: new Date('2023-12-01T12:00:00Z')
  },
  {
    patient_id: 4,
    content: "Le patient d\u00e9clare qu'il a mal au dos lorsqu'il reste assis pendant longtemps",
    created_at: new Date('2023-12-02T16:00:00Z'),
    updated_at: new Date('2023-12-02T16:00:00Z')
  },
  {
    patient_id: 4,
    content: "Le patient d\u00e9clare avoir commenc\u00e9 \u00e0 fumer depuis peu H\u00e9moglobine A1C sup\u00e9rieure au niveau recommand\u00e9",
    created_at: new Date('2023-12-03T10:30:00Z'),
    updated_at: new Date('2023-12-03T10:30:00Z')
  },
  {
    patient_id: 4,
    content: "Taille, Poids, Cholest\u00e9rol, Vertige et R\u00e9action",
    created_at: new Date('2023-12-04T09:15:00Z'),
    updated_at: new Date('2023-12-04T09:15:00Z')
  }
]);

print('Notes insérées avec succès avec encodage UTF-8!');
print('Nombre total de notes: ' + db.notes.countDocuments());
# MediLabo Solutions - Système de Gestion Médicale

## Description
Application de gestion médicale basée sur une architecture microservices simplifiée pour la gestion des patients, notes médicales et évaluation des risques de diabète. 

## Architecture Microservices Simplifiée

### Services Principaux
- **Gateway Service** - Port 8080 : Passerelle API avec authentification JWT et routage statique
- **Patient Service** - Port 8081 : Gestion des données patients (MySQL)
- **Note Service** - Port 8082 : Gestion des notes médicales (MongoDB)
- **Risk Assessment Service** - Port 8083 : Évaluation des risques de diabète
- **Frontend Service** - Port 8084 : Interface utilisateur web (Thymeleaf)

### Bases de Données
- **MySQL 8.0** : Stockage des données patients
- **MongoDB 6.0** : Stockage des notes médicales

## Technologies Utilisées
- **Backend** : Spring Boot 3.2.0, Spring Cloud Gateway
- **Frontend** : Thymeleaf, HTML5, CSS3, JavaScript
- **Bases de données** : MySQL, MongoDB
- **Conteneurisation** : Docker & Docker Compose
- **API Gateway** : Spring Cloud Gateway avec routage statique
- **Communication** : RestTemplate/WebClient
- **Sécurité** : JWT (simplifiée)

## Structure du Projet

```
Projet_9/
├── docker-compose.yml
├── README.md
├── gateway-service/          # Passerelle API avec routage statique
├── patient-service/          # Service de gestion des patients
├── note-service/            # Service de gestion des notes
├── risk-assessment-service/ # Service d'évaluation des risques
└── frontend-service/        # Interface utilisateur web
```

## Sécurité

- **Authentification**: JWT tokens via Spring Security
- **Autorisation**: Role-based access control (RBAC)
- **Protection des données**: Chiffrement des données sensibles
- **HTTPS**: Communication sécurisée entre services
- **Validation**: Validation stricte des entrées utilisateur

## Base de Données - Normalisation 3NF

### Patient Service (MySQL)

**Table: patients**
- id (PK)
- first_name
- last_name
- date_of_birth
- gender
- address
- phone_number
- created_at
- updated_at

### Note Service (MongoDB)

**Collection: patient_notes**
- _id
- patient_id
- note_content
- created_date
- updated_date


## Green Code - Bonnes Pratiques Éco-responsables

### Principes Appliqués

1. **Optimisation des Ressources**
   - Utilisation de profils Spring pour différents environnements
   - Configuration de pools de connexions optimisés
   - Pagination des résultats pour limiter les transferts de données
   - Architecture simplifiée sans overhead de service discovery

2. **Architecture Efficiente**
   - Microservices légers avec responsabilités bien définies
   - Communication directe entre services pour réduire la latence
   - Routage statique pour éviter la complexité du service discovery
   - Architecture épurée pour optimiser l'utilisation des ressources

3. **Développement Durable**
   - Code clean et maintenable
   - Tests automatisés pour réduire les bugs en production
   - Documentation complète pour faciliter la maintenance
   - Monitoring et observabilité pour optimiser les performances

### Actions Green Code Recommandées

#### Niveau Infrastructure
- [ ] Utiliser des images Docker Alpine (plus légères)
- [ ] Configurer des limites de ressources dans Docker
- [ ] Implémenter un système de monitoring énergétique
- [ ] Optimiser les requêtes de base de données
- [ ] Utiliser des CDN pour les ressources statiques

#### Niveau Application
- [ ] Implémenter la pagination sur toutes les listes
- [ ] Utiliser des lazy loading pour les données
- [ ] Optimiser les algorithmes de calcul de risque
- [ ] Implémenter un cache local avec Spring Cache
- [ ] Compresser les réponses HTTP (Gzip)

#### Niveau Frontend
- [ ] Minifier les ressources CSS/JS
- [ ] Optimiser les images (format WebP)
- [ ] Implémenter le lazy loading des images
- [ ] Utiliser des Progressive Web App (PWA) features
- [ ] Réduire le nombre de requêtes HTTP

#### Niveau Base de Données
- [ ] Indexer les colonnes fréquemment utilisées
- [ ] Archiver les anciennes données
- [ ] Optimiser les requêtes avec EXPLAIN
- [ ] Utiliser des vues matérialisées si nécessaire
- [ ] Implémenter une stratégie de sauvegarde efficiente

### Métriques Green Code

- **Consommation CPU**: Monitoring via Micrometer
- **Utilisation mémoire**: Profiling avec JProfiler
- **Temps de réponse**: Mesure des latences
- **Taille des transferts**: Monitoring du trafic réseau
- **Efficacité énergétique**: Ratio performance/consommation

## Déploiement

### Prérequis
- Docker & Docker Compose
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- MongoDB 6.0+


### URLs d'Accès (Docker)

- **Application Frontend**: http://localhost:8084
- **Gateway API**: http://localhost:8080
- **Services**: Accessibles via le Gateway uniquement
# P9_MediaLabo_OC
# P9_MediaLabo_OC
# P9_MediaLabo_OC

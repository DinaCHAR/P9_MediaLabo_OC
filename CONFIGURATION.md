# Configuration Centralisée

Ce projet utilise une configuration centralisée via le fichier `application-global.yml` pour gérer l'environnement Docker.

## Fichier de Configuration Globale

### `application-global.yml`

Ce fichier contient :
- **Profil Spring** : `docker` (par défaut)
- **URLs des services** pour l'environnement Docker : utilise les noms de service Docker
- **Configuration de logging** commune

### URLs des Services

#### Environnement Docker (`docker` profile)
- Patient Service: `http://patient-service:8081`
- Note Service: `http://note-service:8082`
- Risk Assessment Service: `http://risk-assessment-service:8083`
- Gateway Service: `http://gateway-service:8080`

## Configuration des Services

### Services Modifiés pour Utiliser la Configuration Globale

1. **risk-assessment-service** :
   - Utilise `${services.patient.url}` et `${services.note.url}` dans les clients Feign
   - Configuration Docker uniquement

2. **gateway-service** :
   - Configuration des URLs de services supprimée
   - Utilise la configuration globale pour le routage

3. **frontend-service** :
   - Utilise `${services.gateway.url}` pour la configuration du gateway

4. **patient-service** et **note-service** :
   - Gardent leur configuration de base de données
   - Pas d'URLs de services car ils ne font pas d'appels externes

## Utilisation

### Démarrage avec Docker Compose

Pour démarrer tous les services :
```bash
docker-compose up --build
```

### Démarrage Manuel (Mode Docker)

Pour chaque service, utilisez :
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.additional-location=file:../application-global.yml --spring.profiles.active=docker"
```

### Exemple de Démarrage Complet (Mode Docker)

```bash
# Terminal 1 - Patient Service
cd patient-service
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.additional-location=file:../application-global.yml --spring.profiles.active=docker"

# Terminal 2 - Note Service
cd note-service
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.additional-location=file:../application-global.yml --spring.profiles.active=docker"

# Terminal 3 - Risk Assessment Service
cd risk-assessment-service
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.additional-location=file:../application-global.yml --spring.profiles.active=docker"

# Terminal 4 - Gateway Service
cd gateway-service
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.additional-location=file:../application-global.yml --spring.profiles.active=docker"

# Terminal 5 - Frontend Service
cd frontend-service
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.additional-location=file:../application-global.yml --spring.profiles.active=docker"
```

## Avantages

1. **Configuration centralisée** : Un seul fichier pour gérer l'environnement Docker
2. **Simplicité** : Configuration unique et cohérente
3. **Maintenance simplifiée** : Modifications centralisées
4. **Docker-first** : Optimisé pour les conteneurs
5. **Cohérence** : Même configuration pour tous les services

## Personnalisation

Pour ajouter un nouvel environnement, modifiez `application-global.yml` :

```yaml
---
spring:
  config:
    activate:
      on-profile: mon-environnement

services:
  patient:
    url: http://mon-patient-service:8081
  # ... autres services
```

Puis utilisez :
```bash
--spring.profiles.active=mon-environnement
```
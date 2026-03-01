# Kata API for shopping

## Prérequis

- **Java SDK 17** (obligatoire)
- **Maven 3.8+**

---

## Lancer le projet

```bash
# 1. Cloner le projet et se placer dans le dossier
```bash
git clone https://github.com/Souhail136/kata.git
cd kata-api
```

### 2. Compiler le projet

```bash
mvn clean compile
```

### 3. Lancer les tests

```bash
mvn test
```

### 4. Démarrer l'application

```bash
mvn spring-boot:run
```

Application démarrée sur **http://localhost:8080**

---

## Endpoints

| Méthode | URL | Description |
|--------|-----|-------------|
| `GET`  | `/api/v1/produits` | Liste tous les produits avec prix TTC |
| `GET`  | `/api/v1/produits/{id}` | Récupère un produit par son identifiant |
| `POST` | `/api/v1/panier/facture` | Soumet un panier, retourne la facture détaillée |

**Swagger UI** : http://localhost:8080/swagger-ui.html

### Exemple — Calcul de facture

```bash
curl -X POST http://localhost:8080/api/v1/panier/facture \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      { "productId": 5, "quantity": 1 },
      { "productId": 8, "quantity": 2 }
    ]
  }'
```

---

## Architecture

Ce projet applique l'**Architecture Hexagonale (Ports & Adapters)** :

```
domain/                          - Cœur métier — zéro dépendance framework
  model/                         - Agrégats, Value Objects (Money, Quantity, ProductId)
  model/valueobject/
  repository/ProductCatalog      - Port secondaire (interface)
  service/TaxPolicy              - Règles fiscales
  service/InvoiceService         - Orchestration métier
  exception/                     - Exceptions domaine typées

application/                     - Cas d'usage — orchestre le domaine
  port/in/                       - Ports primaires (interfaces)
  dto/                           - Vues et commandes applicatives
  usecase/                       - Implémentations des ports primaires

infrastructure/                  - Détails techniques — dépend du domaine/application
  adapter/in/web/                - Contrôleurs REST (adaptateurs primaires)
  adapter/out/catalog/           - Client HTTP + adaptateur catalogue (port secondaire)
  logging/                       - Intercepteurs HTTP entrant/sortant + MDC correlation ID
  config/                        - Configuration Spring (RestTemplate, OpenAPI)
```

### Règles d'architecture vérifiées automatiquement (ArchUnit)

Les tests `HexagonalArchitectureTest` échouent si une règle est violée :
- Le **domaine** ne dépend ni de l'application, ni de l'infrastructure, ni de Spring Web
- L'**application** ne dépend pas de l'infrastructure

---

## Règles de calcul des taxes

| Type de produit | TVA | Taxe importation |
|----------------|-----|-----------------|
| Nourriture     | 0%  | 5% si importé   |
| Médicaments    | 0%  | 5% si importé   |
| Livres         | 10% | 5% si importé   |
| Autres         | 20% | 5% si importé   |

**Formule :**  
`Pttc = Pht + arrondi(Pht × tva/100) + arrondi(Pht × ti/100)`

Chaque taxe est arrondie **indépendamment** aux **5 centimes supérieurs**.

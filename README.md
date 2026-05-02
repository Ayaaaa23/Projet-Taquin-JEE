#  Puzzle Glissant (Taquin)

Un jeu de taquin (puzzle glissant) développé avec **Spring MVC** et **Thymeleaf**, conforme aux exigences du projet de Master 1 ISII (Architecture Web JEE).  
Le joueur doit reconstituer une image mélangée en déplaçant les pièces. Trois niveaux de difficulté, score, sauvegarde automatique et manuelle, chronomètre, et interface responsive.

---

##  Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Technologies utilisées](#-technologies-utilisées)
- [Installation et lancement](#-installation-et-lancement)
- [Utilisation](#-utilisation)
- [Architecture du projet](#-architecture-du-projet)
- [Aperçu](#-aperçu)
- [Auteurs](#-auteurs)

---

##  Fonctionnalités

-  3 niveaux de difficulté : 3x3, 4x4, 5x5
-  Grille mélangée aléatoirement (toujours soluble)
-  Affichage de l’image modèle (aperçu)
-  Score (nombre de mouvements)
- Chronomètre qui démarre au premier mouvement
- Indice : bouton affichant/masquant les numéros des pièces (aide ou vérification)
-  Scroll préservé : la page ne remonte plus après un déplacement, même sur grilles 4x4 et 5x5
- Images adaptées : les fragments suivent l’ordre colonne par colonne (contrainte de l’outil de découpage, gérée par transposition de la grille solution)
- Sauvegarde automatique après chaque déplacement
-  Sauvegarde manuelle via un bouton
-  Reprise d’une partie sauvegardée
- Bouton "Rejouer" (mélange la même grille, reset score et chrono)
-  Message de victoire animé (CSS pur)
-  Interface responsive et intuitive
- Utilisation d’images découpées pour les pièces

---

##  Technologies utilisées

| Catégorie       | Technologie                           |
|----------------|---------------------------------------|
| Framework Web  | Spring MVC (configuration manuelle)   |
| Moteur de templates | Thymeleaf                         |
| Persistance     | Spring Data JPA + Hibernate           |
| Base de données | H2 (relationnelle)                    |
| Serveur         | Apache Tomcat 10 (déploiement war)    |
| Build           | Maven                                 |
| Front-end       | HTML5 / CSS3 / JavaScript pur (fetch) |
| JSON            | Jackson (conversion grille ↔ JSON)    |

**Aucune bibliothèque externe front-end** (Bootstrap, jQuery, etc.) n’a été utilisée – respect strict des consignes.

---

##  Installation et lancement

### Prérequis

- JDK 17
- Apache Maven 3.9+
- Apache Tomcat 10 (testé avec 10.1.23)

### Étapes

1. **Cloner le dépôt**  
   git clone https://github.com/votre-compte/taquin-mvc.git
   cd taquin-mvc
2. **Générer le fichier WAR (à la racine du projet**  
 mvn clean package
  Le fichier target/ROOT.war est produit.
3. **Déployer sur Tomcat**
 Copier ROOT.war dans le dossier webapps de votre installation Tomcat 10.
 Supprimer l’ancien dossier ROOT s’il existe. (optionelle mais préférable)

4. **Démarrer Tomcat**
   cd /chemin/vers/tomcat/bin
   ./startup.sh   # Linux/Mac
    startup.bat    # Windows

   
  5. **Accéder à l’application** 
   Ouvrez votre navigateur à l’adresse : http://localhost:8080/
   

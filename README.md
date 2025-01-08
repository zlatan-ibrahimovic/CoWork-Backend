# CoWork-Backend

Bienvenue sur le **Backend** de mon application **CoWork**, une application Spring Boot conçue pour gérer la logique et les données d'un Gestionnaire de Tâches Collaboratif.

---

## Prérequis

Avant de commencer, assurez-vous d'avoir les outils suivants installés sur votre machine :

- **Java 17 ou supérieur** : Téléchargez [Java ici](https://adoptium.net/).
- **Maven** (si vous ne souhaitez pas utiliser le Maven Wrapper intégré au projet) : Téléchargez [Maven ici](https://maven.apache.org/download.cgi).
- Un IDE pour Java : Nous recommandons **IntelliJ IDEA**, **Eclipse**, ou **VS Code**.

---

## Initialisation du projet

### Étapes pour cloner et démarrer le projet :

1. **Clonez le dépôt** :
   Téléchargez ou clonez ce dépôt en exécutant la commande suivante dans votre terminal :
   
   ```bash
   git clone https://github.com/Tsuna21/CoWork-Backend.git
   ```

2. **Accédez au répertoire du projet :**

    ```bash
    cd Nom du dossier racine
    ```

3. **Lancez l'application :** Si vous utilisez le Maven Wrapper inclus dans le projet, exécutez :

    ```bash
    ./mvnw spring-boot:run  # Pour Linux/Mac
    mvnw.cmd spring-boot:run # Pour Windows
    ```

    Si Maven est installé globalement :

    ```bash
    mvn spring-boot:run
    ```

4.  **Accédez à l'application dans le navigateur :** Une fois l'application lancée, ouvrez votre
    navigateur à l'adresse suivante :

    ```bash
    http://localhost:8080
    ```
# Commandes Maven principales

Voici quelques commandes utiles pour travailler avec le projet Maven :

* **Lancer l'application :**

    ```bash
    mvn spring-boot:run
    ```
* **Compiler et construire le projet :**

    ```bash
    mvn clean install
    ```
* **Nettoyer les fichiers générés :**
 
    ```bash
    mvn clean
    ```
* **Créer un fichier exécutable .jar :**

    ```bash
    mvn package
    ```
Le fichier .jar généré sera disponible dans le dossier target/.

# Structure du projet

Voici un aperçu des dossiers et fichiers principaux du projet Spring Boot :

* src/main/java: Contient le code source principal de l'application.
* src/main/ressources: Contient les fichiers de configuration et les ressources (comme les fichiers application.properties).
* pom.xml: Fichier de configuration Maven où sont définies les dépendances et les plugins.
* target/: Dossier généré automatiquement après la compilation, contenant les fichiers .class et le .jar.
* mvnw et mvnw.cmd : Scripts Maven Wrapper pour exécuter Maven sans installation globale.

# FAQ

## Que faire si mvn spring-boot:run ne fonctionne pas ?

1. Assurez-vous que Java est bien installé et configuré (vérifiez avec java -version).
2. Si Maven n'est pas reconnu, utilisez le Maven Wrapper inclus (./mvnw ou mvnw.cmd).
3. Consultez les erreurs affichées dans la console et corrigez-les selon les instructions.

## Comment changer le port par défaut ?

Ajouter ou modifier cette ligne dans application.properties:

    ```bash
    server.port=8081
    ```
L'application utilisera désormais le port '8081' au lieu de '8080'.

# Ressources Utiles

Voici quelques ressources pour approfondir vos connaissances sur Spring Boot et Maven :

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Initializr](https://start.spring.io/): Générateur de projets Spring Boot.
* [Maven Central Repository](https://mvnrepository.com/): Trouver des dépendances Maven.
* [Spring Boot Guide pour Débutants](https://spring.io/guides)

# Auteur

Ce projet a été réalisé dans le cadre d'un stage sur Spring Boot et Maven.
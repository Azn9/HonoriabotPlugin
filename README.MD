# HonoriaBot API

Ce plugin est une interface pour le bot Discord d'Honoria, permettant aux joueurs de link leur compte Discord et Minecraft et de faciliter l'ajout de coins aux joueurs.

Ce plugin nécessite une clé d'API et une URL d'API pour fonctionner.

---

### Utilisation de l'API :

1. Importez le plugin via maven ou gradle :

- Maven
```xml
<repositories>
    <repository>
        <url>https://nexus.azn9.dev/repository/public-snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>dev.azn9</groupId>
        <artifactId>honoriabot-api</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

- Gradle
```groovy
repositories {
    maven {
        url = 'https://nexus.azn9.dev/repository/public-snapshots/'
    }
}

dependencies {
    implementation 'dev.azn9:honoriabot-api:0.0.1-SNAPSHOT'
}
```

2. Dans votre plugin, utilisez l'api :
```Java
HonoriabotApi api = HonoriabotApi.getPlugin(HonoriabotApi.class);

api.addCoins(uuid, coins);
```

### La méthode ci-dessus peut renvoyer les réponses suivantes :
 - INVALID_API_KEY : La clé d'API n'est pas valide
 - INVALID_DATA : Les données envoyées sont invalides
 - UNLINKED_USER : Le joueur spécifié n'a pas lié son compte Discord
 - DB_ERROR : Une erreur est survenue (merci de me le signaler)
 - SUCCESS : Oké !
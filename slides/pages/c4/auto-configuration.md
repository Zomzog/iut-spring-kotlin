---
layout: cover
hideInToc: false
---

# Auto-Configuration - Starters

---
layout: full
class: text-left
---

## Qu'est-ce que l'auto-configuration ?

<div v-click>

- Configure automatiquement des beans Spring en fonction de :
  - Dépendances présentes sur le classpath.
  - Propriétés de configuration.

</div>

<div v-click>

- **Avantages** :
  - Réduit le code répétitif (boilerplate).
  - Simplifie la configuration d'une application.

</div>

---
layout: full
class: text-left
---

## Comment fonctionne l'auto-configuration

<div v-click>

- **Spring Factories** :
  - Le fichier `org.springframework.boot.autoconfigure.AutoConfiguration.imports` dans `META-INF/spring`.
  - Liste les classes d'auto-configuration.

</div>

<div v-click>

- **Annotations conditionnelles** :
  - `@ConditionalOnClass` : vérifie la présence d'une classe sur le classpath.
  - `@ConditionalOnMissingBean` : s'assure qu'aucun bean équivalent n'est déjà défini.
  - `@ConditionalOnProperty` : active la configuration selon la valeur d'une propriété.

  ---

  [Voir aussi — Cours 2 : Configuration (Conditional Bean)](/slides/pages/c2/config.md)

</div>

---
layout: full
class: text-left
---

## Exemple pratique

````md magic-move
```kotlin {all|1,3}
package bzh.zomzog.iut.amphi.autoconfig

@Configuration
class MyAutoConfiguration {

  @Bean
  fun myService(): MyService = MyService()

}
```

```kotlin
package bzh.zomzog.iut.amphi.autoconfig.autoconfig

@Configuration
@ConditionalOnClass(name = ["bzh.zomzog.iut.amphi.MyService"]) // active seulement si MyService est présent
class MyAutoConfiguration {

  @Bean
  fun myService(): MyService = MyService()

}
```

```kotlin
package bzh.zomzog.iut.amphi.autoconfig.autoconfig

@Configuration
@ConditionalOnClass(name = ["bzh.zomzog.iut.amphi.MyService"]) // active seulement si MyService est présent
class MyAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun myService(): MyService = MyService()

  // @ConditionalOnMissingBean permet à un utilisateur de définir
  // son propre bean `MyService` sans provoquer de conflit :
  // Spring choisira la définition fournie par l'utilisateur.
}
```
````

<div v-click='1'>
```properties
# src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
bzh.zomzog.iut.amphi.autoconfig.MyAutoConfiguration
```
</div>

---
layout: full
class: text-left
---

## Désactiver l'auto-configuration

<div v-click>

- Utiliser `spring.autoconfigure.exclude` dans un `application.yml` :

```yaml
spring:
  autoconfigure:
    exclude: bzh.zomzog.iut.amphi.autoconfig.MyAutoConfiguration
```

</div>

<div v-click>

## Personnaliser l'auto-configuration

- **Remplacer les valeurs par défaut** dans les properties
  exemple : `spring.jpa.hibernate.ddl-auto`

</div>
<div v-click>

- Définir vos propres beans pour remplacer ceux fournis automatiquement.
  - **@Bean** quand ils utilisent les annotations `@Conditional...`.
  - **@Primary** sinon

</div>


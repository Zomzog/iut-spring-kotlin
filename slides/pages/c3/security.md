---
layout: full
class: text-left
---

## Spring Security

<!--
C'est un sujet vaste et on va discuter d'une petite partie
-->

---
layout: full
class: text-left
---

## Vocabulaire

---
layout: full
class: text-left
---

## Authentication

Qui suis-je?

[mermaid]

```
%%{init: { 'logLevel': 'debug', 'theme': 'dark'} }%%
flowchart TD
    Credentials["Credentials (login / password)"] --> auth["LDAP, ActiveDirectory, Database, CSV..."]
    auth --> User

```

---
layout: full
class: text-left
---

## Authorization

Que puis-je faire?

Toujours après l'authentication

Read ? Write ? Admin ? Publish?

---
layout: full
class: text-left
---

## Role

Ensemble de droits sur l'application

---
layout: full
class: text-left
---

## User

Information de base sur l'utilisateur connécté

login, role...

---
layout: full
class: text-left
---

## UserDetail

API Spring pour faire la phase d'authentification

---
layout: full
class: text-left
---

## Dépendances

```kotlin
implementation("org.springframework.boot:spring-boot-starter-security")
testImplementation("org.springframework.security:spring-security-test")
```

---
layout: full
class: text-left
---

## Attention

[WARNING]
====
Ajouter cette dépandance active directement la sécurité

De base toute requete doit etre authentifié,
donc tout répond un 401.
====

---
layout: full
class: text-left
---

## Enable Web Security

```kotlin
@Configuration
@EnableWebSecurity
class MySecurityConfig {
```

<!--
Ajouter l'annotation EnableWebSecurity va signaler à Spring qu'il doit chercher des beans de configuration de spring-security pour du MVC
-->

---
layout: full
class: text-left
---

## Security Filter

```kotlin
import org.springframework.security.config.annotation.web.invoke

@Bean
open fun filterChain(http: HttpSecurity): SecurityFilterChain {
  http {
    csrf { disable() }
    authorizeHttpRequests {
      authorize("/ponies", permitAll)
      authorize(anyRequest, authenticated)
    }
    httpBasic { }
    formLogin { }
  }
  return http.build()
}
```

<!--
Ce bean permet de configurer le fonctionnement de spring security
-->

---
layout: full
class: text-left
---

## Attention

[WARNING]
====
Bien ajouter cet import qui ne s'ajoute pas toujours automatiquement

import org.springframework.security.config.annotation.web.invoke
====

---
layout: full
class: text-left
---

## Form Login

formLogin { }

image:login.png[]

`/login` && `/logout`

<!--
FormLogin permet de se connecter via un formulaire,
de base un formulaire est généré par spring security a l'adresse `/login`.
Spring fournir aussi un endpoint `/logout`.

La session est gére par un cookie `JSESSIONID`.
-->

---
layout: full
class: text-left
---

## Http Basic

httpBasic { }

[source, bash]

```
BASE=$(echo -ne "login:password" | base64 --wrap 0)
```

[source, bash]

```
curl \
 -H "Authorization: Basic $BASE" \
 http://localhost:8080
```

<!--
Basic Auth comme d'autres (Bearer...) sont des moyens d'authentification pour des APIs

Pour Basic on fournit un login et un mot de passe encodé en base64 dans le header Authorization.
-->

---
layout: full
class: text-left
---

## Autres

Cross Site Request Forgery

```kotlin
csrf { disable() }
```

Cross-Origin Resource Sharing

```kotlin
cors { disable() }
```

<!--
On peut configuer ou supprimer des sécurités comme le CSRF ou le CORS
-->

---
layout: full
class: text-left
---

## authorizeHttpRequests

```kotlin
  http {
    authorizeHttpRequests {
      authorize("/ponies", permitAll)
      authorize("/admin", hasRole("ADMIN"))
      authorize(anyRequest, authenticated)
    }
  }
}
```

```kotlin
fun authorize(pattern: String,
              access: AuthorizationManager<RequestAuthorizationContext>)
```

<!--
Cette partie de la configuration permet de definir les droits d'accès.
On peut les donner par pattern ou pour toutes les requetes.

On peut enlever la sécurité pour certaines requetes (permitAll),
juste etre authentifié (authenticated),
ou définir des droits spécifiques par role, ip...)
-->

---
layout: full
class: text-left
---

## Alternative pour les droits

```kotlin
@Configuration
@EnableMethodSecurity
```

```kotlin
@PreAuthorize("hasRole('ADMIN')")
fun myMethod() ...
```

<!--
Une alternative à la gestion MVC par path,
la gestion par PreAuthorize sur les methodes
-->

---
layout: full
class: text-left
---

## authentification

```kotlin
@Bean
fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
}
```

<!--
Avant de parler gestion authentification,
on ne stock jamais un mot de passe en claire.
-->

---
layout: full
class: text-left
---

## In Memory User Detail Manager

```kotlin
@Bean
fun userDetailService(passwordEncoder: PasswordEncoder): UserDetailsManager {
    val admin = User.withUsername("admin")
        .password(passwordEncoder.encode("1234"))
        .roles("ADMIN")
        .build()
    val demo = User.withUsername("login")
        .password(passwordEncoder.encode("password"))
        .roles("ADMIN")
        .build()
    return InMemoryUserDetailsManager(admin, demo)
}
```

<!--
Le plus rapide et le plus simple,
tout est en mémoire, donc à chaque redémarrage c'est perdu.

Avec ce bean, Spring a son contrat pour transformer un user/password en User.

C'est l'authentification.
-->

---
layout: full
class: text-left
---

## Jdbc User Detail

```kotlin
@Bean
fun userDetailService(dataSource: DataSource,
                      passwordEncoder: PasswordEncoder): UserDetailsManager {
  val user1 = User.withUsername("u1")
      .password(passwordEncoder.encode("pw"))
      .roles("USER")
      .build()
  return JdbcUserDetailsManager(dataSource).apply {
      createUser(user1)
  }
```

<!--
Un autre moyen très similaire,
avec un stockage en base de donnée
-->

---
layout: full
class: text-left
---

## Jdbc User Detail

[source,sql]

```
CREATE TABLE USERS (
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(500) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE AUTHORITIES (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
);

CREATE UNIQUE INDEX ix_auth_username ON AUTHORITIES (username, authority);
```

---
layout: full
class: text-left
---

## Récupération du User

Par "injection", on demande le Principal à Spring

```kotlin
@GetMapping
fun admin(principal: Principal): ResponseEntity<String> {
  println("Login: ${principal.name}")
}
```

---
layout: full
class: text-left
---

## Récupération du User

Pour du MVC, sur le Thread, par appel au SecurityContextHolder

```kotlin
SecurityContextHolder.getContext().authentication.principal.let {
  println("Login: ${principal.name}")
}
```

<!--
Dans le cadre de SpringMVC le contexte de sécurité est lié au Thread.

Il est donc important si on veut multi-threader une requete de prendre soin de copier ce contexte.
-->

---
layout: full
class: text-left
---

## TEST !

```kotlin
@WebMvcTest
@Import(MySecurityFilterConfig::class)
class HelloControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `happy path`() {
        mockMvc.get("/openEndpoint")
            .andExpect {
                status { isIAmATeapot() }
            }
    }
}
```

<!--
@Import du security filter

/!\ il faut qu'il n'y ai aucune dependance autre (bdd...)
-->

---
layout: full
class: text-left
---

## WithAnonymousUser

```kotlin
@WithAnonymousUser
@Test
fun `admin without auth`() {
    mockMvc.get("/admin")
        .andExpect {
            status { isUnauthorized() }
        }
}
```

---
layout: full
class: text-left
---

## WithMockUser

```kotlin
@WithMockUser
@Test
fun `admin without admin`() {
    mockMvc.get("/admin")
        .andExpect {
            status { isForbidden() }
        }
}
```

---
layout: full
class: text-left
---

## WithMockUser

```kotlin
@WithMockUser(roles =[ "ADMIN"])
@Test
fun `admin with admin`() {
    mockMvc.get("/admin")
        .andExpect {
            status { isOk() }
        }
}
```

== Spring Security

[NOTE.speaker]
--
C'est un sujet vaste et on va discuter d'une petite partie
--

== Vocabulaire

== Authentication

Qui suis-je?

[mermaid]
----
%%{init: { 'logLevel': 'debug', 'theme': 'dark'} }%%
flowchart TD
    Credentials["Credentials (login / password)"] --> auth["LDAP, ActiveDirectory, Database, CSV..."]
    auth --> User
----

== Authorization

Que puis-je faire?

Toujours après l'authentication

Read ? Write ? Admin ? Publish?

== Role

Ensemble de droits sur l'application

== User

Information de base sur l'utilisateur connécté

login, role...

== UserDetail

API Spring pour faire la phase d'authentification

== Dépendances

[source,kotlin]
----
implementation("org.springframework.boot:spring-boot-starter-security")
testImplementation("org.springframework.security:spring-security-test")
----

== Attention

[WARNING]
====
Ajouter cette dépandance active directement la sécurité

De base toute requete doit etre authentifié,
donc tout répond un 401.
====

== Enable Web Security

[source,kotlin]
----
@Configuration
@EnableWebSecurity
class MySecurityConfig {
----

[NOTE.speaker]
--
Ajouter l'annotation EnableWebSecurity va signaler à Spring qu'il doit chercher des beans de configuration de spring-security pour du MVC
--

== Security Filter

[source, kotlin]
----
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
----

[NOTE.speaker]
--
Ce bean permet de configurer le fonctionnement de spring security
--

== Attention

[WARNING]
====
Bien ajouter cet import qui ne s'ajoute pas toujours automatiquement

import org.springframework.security.config.annotation.web.invoke
====

== Form Login

formLogin { }

image:login.png[]

`/login` && `/logout`

[NOTE.speaker]
--
FormLogin permet de se connecter via un formulaire,
de base un formulaire est généré par spring security a l'adresse `/login`.
Spring fournir aussi un endpoint `/logout`.

La session est gére par un cookie `JSESSIONID`.
--

== Http Basic

httpBasic { }

[source, bash]
----
BASE=$(echo -ne "login:password" | base64 --wrap 0)
----

[source, bash]
----
curl \
 -H "Authorization: Basic $BASE" \
 http://localhost:8080
----

[NOTE.speaker]
--
Basic Auth comme d'autres (Bearer...) sont des moyens d'authentification pour des APIs

Pour Basic on fournit un login et un mot de passe encodé en base64 dans le header Authorization.
--

== Autres

Cross Site Request Forgery

[source, kotlin]
----
csrf { disable() }
----

Cross-Origin Resource Sharing

[source, kotlin]
----
cors { disable() }
----

[NOTE.speaker]
--
On peut configuer ou supprimer des sécurités comme le CSRF ou le CORS
--

== authorizeHttpRequests

[source, kotlin]
----
  http {
    authorizeHttpRequests {
      authorize("/ponies", permitAll)
      authorize("/admin", hasRole("ADMIN"))
      authorize(anyRequest, authenticated)
    }
  }
}
----

[source, kotlin]
----
fun authorize(pattern: String,
              access: AuthorizationManager<RequestAuthorizationContext>)
----

[NOTE.speaker]
--
Cette partie de la configuration permet de definir les droits d'accès.
On peut les donner par pattern ou pour toutes les requetes.

On peut enlever la sécurité pour certaines requetes (permitAll),
juste etre authentifié (authenticated),
ou définir des droits spécifiques par role, ip...)
--

== Alternative pour les droits

[source,kotlin]
----
@Configuration
@EnableMethodSecurity
----

[source,kotlin]
----
@PreAuthorize("hasRole('ADMIN')")
fun myMethod() ...
----

[NOTE.speaker]
--
Une alternative à la gestion MVC par path, 
la gestion par PreAuthorize sur les methodes
--

== authentification

[source,kotlin]
----
@Bean
fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
}
----

[NOTE.speaker]
--
Avant de parler gestion authentification, 
on ne stock jamais un mot de passe en claire.
--

== In Memory User Detail Manager

[source,kotlin]
----
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
----

[NOTE.speaker]
--
Le plus rapide et le plus simple,
tout est en mémoire, donc à chaque redémarrage c'est perdu.

Avec ce bean, Spring a son contrat pour transformer un user/password en User.

C'est l'authentification.
--

== Jdbc User Detail

[source,kotlin]
----
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
----

[NOTE.speaker]
--
Un autre moyen très similaire, 
avec un stockage en base de donnée
--

== Jdbc User Detail

[source,sql]
----
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
----

== Récupération du User

Par "injection", on demande le Principal à Spring

[source,kotlin]
----
@GetMapping
fun admin(principal: Principal): ResponseEntity<String> {
  println("Login: ${principal.name}")
}
----


== Récupération du User

Pour du MVC, sur le Thread, par appel au SecurityContextHolder

[source,kotlin]
----
SecurityContextHolder.getContext().authentication.principal.let {
  println("Login: ${principal.name}")
}
----

[NOTE.speaker]
--
Dans le cadre de SpringMVC le contexte de sécurité est lié au Thread.

Il est donc important si on veut multi-threader une requete de prendre soin de copier ce contexte.
--

== TEST !

[source,kotlin]
----
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
----

[NOTE.speaker]
--
@Import du security filter 

/!\ il faut qu'il n'y ai aucune dependance autre (bdd...)
--

== WithAnonymousUser

[source,kotlin]
----
@WithAnonymousUser
@Test
fun `admin without auth`() {
    mockMvc.get("/admin")
        .andExpect {
            status { isUnauthorized() }
        }
}
----

== WithMockUser

[source,kotlin]
----
@WithMockUser
@Test
fun `admin without admin`() {
    mockMvc.get("/admin")
        .andExpect {
            status { isForbidden() }
        }
}
----

== WithMockUser

[source,kotlin]
----
@WithMockUser(roles =[ "ADMIN"])
@Test
fun `admin with admin`() {
    mockMvc.get("/admin")
        .andExpect {
            status { isOk() }
        }
}
----

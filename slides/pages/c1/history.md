---
layout: cover
---

# Spring

---

# Spring != Jakarta

Spring a été créé en 2003

Alternative légère pour répondre à la complexité des premières versions des spécifications J2EE

Pas d'EJB

Mais des servlets

<!--

Concurrent de Jakarta sur certains points

Utilisateur sur d'autres
-->

---
layout: full
transition: fade
---

# Spring - Histoire

```mermaid
%%{init: { 'gitGraph': {'showBranches': false, 'showCommitLabel':true,'mainBranchName': 'Spring'}} }%%
gitGraph
    commit id:"2002"
    commit id:"2004" tag:"1.0"
    commit id:"2006" tag:"2.0"
    commit id:"2009" tag:"3.0"
    commit id:"2013" tag:"4.0"
    commit id:"2017" tag:"5.0"
    commit id:"2022" tag:"6.0"
```

<!--

Springboot est une surcouche de Spring

Les deux sont liés
-->

---
layout: full
---

# Spring - Histoire

```mermaid
%%{init: {'gitGraph': {'showBranches': true, 'showCommitLabel':true,'mainBranchName': 'Spring'}} }%%
gitGraph
    commit id:"2002"
    commit id:"2004" tag:"1.0"
    commit id:"2006" tag:"2.0"
    commit id:"2009" tag:"3.0"
    commit id:"2013" tag:"4.0"
    branch SpringBoot
    commit id:"2014" tag:"1.0.0"
    checkout Spring
    commit id:"2017" tag:"5.0"
    checkout SpringBoot
    merge Spring id:"2018" tag:"2.0.0"
    checkout Spring
    commit id:"2022" tag:"6.0"
    checkout SpringBoot
    merge Spring id:"2022-11" tag:"3.0.0"
```

<!--

Springboot est une surcouche de Spring

Les deux sont liés
-->

---

# Spring

Spring est en premier lieu un système d'injection de dépendances

Spring fournit des librairies d'abstraction d'autres frameworks

spring-webmvc

spring-security

spring-data

spring-kafka

...

<!--

-->

---
layout: full
---

# Spring

```mermaid

flowchart TD
    customCode --> war{war}
    spring-libs --> war
    web.xml --> war
    war --> tomcat
    tomcat --> debug
```

<!--

Fonctionnement similaire, car il fonctionne dans un serveur JakartaEE

Remote debug pour travailler
-->

---
layout: full
---

# Spring Boot

```mermaid
flowchart TD
    customCode --> jar{jar}
    spring-libs --> springboot-starter
    tomcat --> springboot-starter
    springboot-starter --> jar
    jar --> run(java -jar mon.jar)
```

<!--

Tomcat est une application en java donc peut être dans le jar

On parle de fatJar # 1 jar avec tout dedans

Debug direct comme si c'était un projet simple
-->

---
layout: TwoColumnsTitle
clicks: 2
---

::title::

# Spring vs Spring Boot

::left::

<div>Spring</div>
<img src="/ingredients.jpg" class="h-96 w-96 object-scale-down" ></img>

::right::
<div v-if="$clicks > 0">SpringBoot</div>
<img v-if="$clicks == 1" src="/gateau.jpg" class="h-96 w-96 object-scale-down" ></img>
<img v-if="$clicks == 2" src="/alsa.jpg" class="h-96 w-96 object-scale-down"></img>

<!--

Spring est souvant présenté comme une sorte de boite à outils

SpringBoot comme le résultat direct

C'est pas si simple même si ça l'est plus
-->

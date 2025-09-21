---
layout: cover
background: /snow-white-blow.gif
---

# Java

---

# Java SE - Java Standard Edition

<br/>

<v-click>

JSR : Java Specification Requests, constituant les spécifications

</v-click>

<v-click>

JRE : un Java Runtime Environment, contenant le seul environnement d'exécution

</v-click>

<v-click>

JDK : Java Development Kit (JDK), contenant les bibliothèques logicielles (compilateur, debug...)

</v-click>

<!--

Java se découpe en 3 grandes parties
Ca permet de faire des applications java "brut"

-->

---

# Jakarta EE

Historique des noms

<v-click>

J2EE : 1999 - 2006

</v-click>

<v-click>

Java EE : 2006 - 2019

</v-click>

<v-click>

Jakarta EE : 2019 - ????

</v-click>

<!--
Multiple renommage du projet

Changement de nom en jakarta suite au transfert de Oracle vers Eclipse
-->

---

# Jakarta EE

Ensemble de spécifications pour faire des applications pour les entreprises

Les versions sont retro-compatible

<!--

-->

[%notitle]
---

# Jakarta EE

image:archi-jee.svg[width#100%]

<!--
JPA - Java Persistence API

JTA - Java Transaction API

JMS - Java Message Service

EJB - Entreprise Java Bean

CDI - Contexts and Dependency Injection

Servlet - Point entrée application

JSP - Java Server page

JSF - Java server face

EL - Expressions Languages

JAX.WS - Java Api for XML Web Service

JAX.RS - Java API for RESTful Web Service
-->

---

# JPA - Java Persistence API

Par exemple la communication avec une base de données

---

# JTA - Java Transaction API

Par exemple une transaction SQL

---

# JMS - Java Message Service

Par exemple la communication avec ActiveMq

---

# EJB - Entreprise Java Bean

Composant logiciel pouvant être appelé par le serveur

---

# CDI - Contexts and Dependency Injection

---

# Servlet - Point entrée application

Classe Java de génération de contenu dynamique

Non limité au HTTP (JDBC...)

---

# JSP - Java Server page

Génération de contenu statique (html...)

---

# JSF - Java server faces

EL - Expressions Languages

Application "riche" ie communication avec le serveur (validation...)

<!--
AJAX
-->

---

# JAX

JAX.WS - Java Api for XML Web Service

JAX.RS - Java API for RESTful Web Service

Communication http moderne

---

# Serveur Application

Implementation des spécifications JavaEE

- Glassfish

- WebLogic

- WebSphere

- JBoss

- ...

<!--
Glassfish # Implementation de référence par JakartaEE

Serveurs complets qui couvrent tout (Servlet, EJB, JPA..)
-->

---

# Fonctionnement

Création de l'appication java et packaging en .WAR

[fragment, step#1]
Installation et lancement du serveur

[fragment, step#2]
Ajout du WAR dans le serveur

[fragment, step#3]
Déploiement du code et mapping des servlets

[%notitle]
---

# Jakarta EE

image:archi-jee.svg[width#100%]

<!--
Faut-il tout même pour un micro serivce?
-->

---

# Conteneur Web / Servlet

Serveurs léger qui ne font "que" les parties servlet et jsp

Exemple de serveurs développés en Java

- Tomcat

- Jetty

<!--
Des serveurs écrits en Java
-->

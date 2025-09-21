---
layout: cover
background: /snow-white-blow.gif
---

# Java

---

# Java SE - Java Standard Edition

<v-click>

**JSR** : Java Specification Requests, constituant les spécifications

</v-click>

<v-click>

**JRE** : Java Runtime Environment, contenant le seul environnement d'exécution

</v-click>

<v-click>

**JDK** : Java Development Kit, contenant les bibliothèques logicielles (compilateur, debug...)

</v-click>

<!--

Java se découpe en 3 grandes parties
Ca permet de faire des applications java "brut"

-->

---

# Jakarta EE

Historique des noms

<v-click>

**J2EE** : 1999 - 2006

</v-click>

<v-click>

**Java EE** : 2006 - 2019

</v-click>

<v-click>

**Jakarta EE** : 2019 -

</v-click>

<!--
Multiple renommage du projet

Changement de nom en jakarta suite au transfert de Oracle vers Eclipse
-->

---

# Jakarta EE

Ensemble de spécifications pour faire des applications pour les entreprises

Les versions sont <v-click>presque</v-click> retro-compatible

<!--

Le passage à Jakarta EE a changé beaucoup de packages
-->

---
layout: center
clicks: 10
---

<style>
.fall {
  transform: translateY(-200px);
  opacity: 0;
  transition: all 0.8s ease-out;
}
.fall.v-enter-active {
  transform: translateY(0);
  opacity: 1;
}
</style>
<div
  class="grid grid-cols-9 gap-6 text-center text-lg font-bold max-w-3xl mx-auto"
>

  <div col-span-6 col-start-4 grid grid-cols-2 gap-6>
    <div
         v-motion
         :initial="{ x: 0, y: -1000, opacity: 1 }"
         :enter="{ x: 0, y: -1000, opacity: 1 }"
         :click-10="{ x: 0, y: 0, opacity: 1 }"
         :duration="1000"
         class="p-4 border rounded-lg shadow-lg">SOAP</div>
    <div
         v-motion
         :initial="{ x: 0, y: -1000, opacity: 1 }"
         :enter="{ x: 0, y: -1000, opacity: 1 }"
         :click-9="{ x: 0, y: 0, opacity: 1 }"
         :duration="1000"
         class="p-4 border rounded-lg shadow-lg">REST</div>
  </div>
  <div col-span-6 col-start-4 grid grid-cols-2 gap-6>
    <div
         v-motion
         :initial="{ x: 0, y: -1000, opacity: 1 }"
         :enter="{ x: 0, y: -1000, opacity: 1 }"
         :click-10="{ x: 0, y: 0, opacity: 1 }"
         :duration="1000"
         class="p-4 border rounded-lg shadow-lg">JAX.WS</div>
    <div
         v-motion
         :initial="{ x: 0, y: -1000, opacity: 1 }"
         :enter="{ x: 0, y: -1000, opacity: 1 }"
         :click-9="{ x: 0, y: 0, opacity: 1 }"
         :duration="1000"
         class="p-4 border rounded-lg shadow-lg">JAX.RS</div>
  </div>
  <div col-span-1
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-6="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">JSP</div>
  <div col-span-1
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-7="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">JSF</div>
  <div col-span-1
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-10="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">EL</div>
  <div col-span-6
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-8="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">Data</div>
  <div col-span-9
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-5="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">Servlet</div>
  <div col-span-2
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-10="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">EJB</div>
  <div col-span-7
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-4="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">CDI</div>
  <div col-span-3
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-1="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">JPA</div>
  <div col-span-3
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-3="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">JTA</div>
  <div col-span-3
       v-motion
       :initial="{ x: 0, y: -1000, opacity: 1 }"
       :enter="{ x: 0, y: -1000, opacity: 1 }"
       :click-2="{ x: 0, y: 0, opacity: 1 }"
       :duration="1000"
       class="p-4 border rounded-lg shadow-lg">JMS</div>
</div>

<!--
JPA - Java Persistence API

Par exemple la communication avec une base de données

JTA - Java Transaction API

Par exemple une transaction SQL

JMS - Java Message Service

Par exemple la communication avec ActiveMq

EJB - Entreprise Java Bean

Composant logiciel pouvant être appelé par le serveur

CDI - Contexts and Dependency Injection

Servlet - Point entrée application

Classe Java de génération de contenu dynamique

Non limité au HTTP (JDBC...)

JSP - Java Server page

Génération de contenu statique (html...)

JSF - Java server face

EL - Expressions Languages

Application "riche" ie communication avec le serveur (validation...)

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

# CDI - Contexts and Dependency Injection

---

# EJB - Entreprise Java Bean

Composant logiciel pouvant être appelé par le serveur

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

Implementation des spécifications JakartaEE

<div v-click>
Glassfish

WebLogic

WebSphere

JBoss

...
</div>

<!--
Glassfish # Implementation de référence par JakartaEE

Serveurs complets qui couvrent tout (Servlet, EJB, JPA..)
-->

---

# Fonctionnement

Création de l'appication java et packaging en .WAR

<div v-click>
Installation et lancement du serveur
</div>

<div v-click>
Ajout du WAR dans le serveur
</div>

<div v-click>
Déploiement du code et mapping des servlets
</div>

---

<div
  class="grid grid-cols-9 gap-6 text-center text-lg font-bold max-w-3xl mx-auto"
>

  <div col-span-6 col-start-4 grid grid-cols-2 gap-6>
    <div
         class="p-4 border rounded-lg shadow-lg">SOAP</div>
    <div
         class="p-4 border rounded-lg shadow-lg">REST</div>
  </div>
  <div col-span-6 col-start-4 grid grid-cols-2 gap-6>
    <div
         class="p-4 border rounded-lg shadow-lg">JAX.WS</div>
    <div
         class="p-4 border rounded-lg shadow-lg">JAX.RS</div>
  </div>
  <div col-span-1
       class="p-4 border rounded-lg shadow-lg">JSP</div>
  <div col-span-1
       class="p-4 border rounded-lg shadow-lg">JSF</div>
  <div col-span-1
       class="p-4 border rounded-lg shadow-lg">EL</div>
  <div col-span-6
       class="p-4 border rounded-lg shadow-lg">Data</div>
  <div col-span-9
       class="p-4 border rounded-lg shadow-lg">Servlet</div>
  <div col-span-2
       class="p-4 border rounded-lg shadow-lg">EJB</div>
  <div col-span-7
       class="p-4 border rounded-lg shadow-lg">CDI</div>
  <div col-span-3
       class="p-4 border rounded-lg shadow-lg">JPA</div>
  <div col-span-3
       class="p-4 border rounded-lg shadow-lg">JTA</div>
  <div col-span-3
       class="p-4 border rounded-lg shadow-lg">JMS</div>
</div>

---

<div
  class="grid grid-cols-9 gap-6 text-center text-lg font-bold max-w-3xl mx-auto"
>

  <div col-span-6 col-start-4 grid grid-cols-2 gap-6>
    <div
         class="p-4 border rounded-lg shadow-lg">SOAP</div>
    <div
         class="p-4 border rounded-lg shadow-lg">REST</div>
  </div>
  <div col-span-6 col-start-4 grid grid-cols-2 gap-6>
    <div
         class="p-4 border rounded-lg shadow-lg">JAX.WS</div>
    <div
         class="p-4 border rounded-lg shadow-lg">JAX.RS</div>
  </div>
  <div col-span-1
       class="p-4 border rounded-lg shadow-lg">JSP</div>
  <div col-span-1 opacity-25
       class="p-4 border rounded-lg shadow-lg">JSF</div>
  <div col-span-1 opacity-25
       class="p-4 border rounded-lg shadow-lg">EL</div>
  <div col-span-6
       class="p-4 border rounded-lg shadow-lg">Data</div>
  <div col-span-9
       class="p-4 border rounded-lg shadow-lg">Servlet</div>
  <div col-span-2 opacity-25
       class="p-4 border rounded-lg shadow-lg">EJB</div>
  <div col-span-7 opacity-25
       class="p-4 border rounded-lg shadow-lg">CDI</div>
  <div col-span-3 opacity-25
       class="p-4 border rounded-lg shadow-lg">JPA</div>
  <div col-span-3 opacity-25
       class="p-4 border rounded-lg shadow-lg">JTA</div>
  <div col-span-3 opacity-25
       class="p-4 border rounded-lg shadow-lg">JMS</div>
</div>

<!--
Faut-il tout même pour un micro serivce?
-->

---

# Conteneur Web / Servlet

Serveurs léger qui ne font "que" les parties servlet et jsp

Exemple de serveurs développés en Java

Tomcat

Jetty

<!--
Des serveurs écrits en Java
-->

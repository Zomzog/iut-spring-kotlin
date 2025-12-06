---
layout: cover
hideInToc: false
---

# Logs

---
layout: full
class: text-left
---

## Niveaux de log

- ERROR

- WARN

- INFO

- DEBUG

- TRACE

<!--
ERROR > WARN > INFO > DEBUG > TRACE
-->

---
layout: full
class: text-left
---

## Logback.

Système de gestion des journaux d'évenements (logs)

Il gère la destination et le niveau de log.

---
layout: full
class: text-left
---

## /src/main/resources/logback.xml

````md magic-move
```xml{all|3-9|10-12|13}
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <layout class="ch.qos.logback.classic.PatternLayout">
      <Pattern>
        %d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n
      </Pattern>
    </layout>
  </appender>
  <root level="INFO">
    <appender-ref ref="CONSOLE"/>
  </root>
  <logger name="org.springframework.web" level="DEBUG"/>
</configuration>
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <layout class="ch.qos.logback.classic.PatternLayout">
      <Pattern>
        %d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n
      </Pattern>
    </layout>
  </appender>
  <appender name="FILE" class="ch.qos.logback.core.FileAppender">
    <file>/tmp/tests.log</file>
    <append>true</append>
    <encoder>
      <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>
    </encoder>
  </appender>
  <root level="INFO">
    <appender-ref ref="CONSOLE"/>
    <appender-ref ref="FILE"/>
  </root>
  <logger name="org.springframework.web" level="DEBUG"/>
</configuration>
```
````

<!--
CONSOLE est un appender sur la sortie standard

root est l'appender de base

On peut avoir deux appender sur les logs
-->

---
layout: full
class: text-left
---

## logback.xml

```xml{3-4|all}
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
  <include resource="org/springframework/boot/logging/logback/console-appender.xml" />
  <appender name="FILE" class="ch.qos.logback.core.FileAppender">...</appender>
  <logger name="iut.nantes" level="debug" additivity="false">
      <appender-ref ref="FILE"/>
      <appender-ref ref="CONSOLE"/>
  </logger>
  <root level="INFO">
      <appender-ref ref="CONSOLE"/>
  </root>
</configuration>
```

<!--
On peut avoir des appender pour seulement certains packages

/!\ si je ne met pas CONSOLE root ne prend plus le log
-->

---
layout: full
class: text-left
---

## Jansi

```xml{4|6|all}
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <withJansi>true</withJansi>
    <encoder>
        <pattern>[%thread] %highlight(%-5level) %cyan(%logger{15}) -%kvp -%msg %n</pattern>
    </encoder>
    </appender>
    <root level="DEBUG">
    <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

<div v-click>

  <div style="white-space:nowrap;font-family:monospace;margin:0;padding:0;">
    <span style="color:#f0f0f0;">[main]</span>
    <span style="color:#2ecc71;font-weight:bold;">DEBUG</span>
    <span style="color:#00bcd4;">c.e.MyService</span>
    <span style="color:#f0f0f0;">--</span>
    <span style="color:#f0f0f0;">Valeur initiale du compteur :</span>
    <span style="color:#f0f0f0;">0</span>
  </div>

  <div style="white-space:nowrap;font-family:monospace;margin:0;padding:0;margin-top:0.25rem;">
    <span style="color:#f0f0f0;">[http-nio-8080]</span>
    <span style="color:#e74c3c;font-weight:bold;">ERROR</span>
    <span style="color:#00bcd4;">c.e.MyController</span>
    <span style="color:#f0f0f0;">--</span>
    <span style="color:#f0f0f0;">Échec de la connexion.</span>
  </div>

</div>
<!--
On peut mettre de la couleur (pratique en dev)
-->

---
layout: full
class: text-left
---

## Logs dans Spring

Simple Logging Facade for Java

SLF4J sert d'<span v-mark.underline.red>abstraction pour divers frameworks</span> de journalisation (java.util.logging, logback, log4j...)
permettant à l'utilisateur final de brancher le framework de journalisation souhaité au moment du déploiement.

<div v-click>

De base dans Spring :

code -> <span v-mark.circle.red=2>slf4j</span> -> logback

</div>

<div v-click>

```kotlin
class Demo {
    private val logger = LoggerFactory.getLogger(javaClass)
}
```

```kotlin
fun hello() {
    logger.trace("trace of ${name}")
    logger.warn("warning with exception", Exception())
}
```

</div>

---
layout: full
class: text-left
---

## Logs dans Kotlin

Optionel :

oshai:kotlin-logging.

Lightweight Multiplatform logging framework <span v-mark.underline.red>for Kotlin</span>

<div v-click>

code -> <span v-mark.circle.red=2>oshai</span> -> slf4j -> logback

</div>

<div v-click>

```kotlin
private val logger = KotlinLogging.logger {}
```

```kotlin
logger.debug(Exception("Demo")) { "Protocol: ${request.protocol}" }
```

</div>

---
layout: full
class: text-left
---

## logback spring

logback.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
  <include resource="org/springframework/boot/logging/logback/console-appender.xml" />
  <root level="INFO">
    <appender-ref ref="CONSOLE" />
  </root>
  <logger name="org.springframework.web" level="DEBUG"/>
</configuration>
```

---
layout: full
class: text-left
---

## logback-spring.xml

```xml{3,8|all}
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <springProfile name="dev">
      <appender name="MY_APPENDER" class="ch.qos.logback.core.ConsoleAppender">
          ....
      </appender>
  </springProfile>
  <springProfile name="default">
      <appender name="MY_APPENDER" class="ch.qos.logback.core.FileAppender">
          ....
      </appender>
  </springProfile>
  <root level="INFO">
      <appender-ref ref="MY_APPENDER"/>
  </root>
</configuration>
```

---
layout: full
class: text-left
---

## Alternative au logback.xml

application.yml

<div v-click>

```yaml
logging:
  level:
    org.springframework.web: DEBUG
    bzh.zomzog.prez: WARN
```

</div>

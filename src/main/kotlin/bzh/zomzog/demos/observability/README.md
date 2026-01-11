Observability demo

Run:

./gradlew :bootRun --args='--spring.config.location=classpath:/application-observability.yml'

Visit `http://localhost:8080/demo/hello` and `http://localhost:8080/actuator/prometheus`

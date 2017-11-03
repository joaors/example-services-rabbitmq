# Base java:8
FROM java:8

# Add jar to container
ADD /target/logs-jar-with-dependencies.jar logs-jar-with-dependencies.jar

# Entry with exec
ENTRYPOINT exec java $JAVA_OPTS -jar /logs-jar-with-dependencies.jar
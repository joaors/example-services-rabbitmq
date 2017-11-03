# Base java:8
FROM java:8

# Add jar to container
ADD target/alunos-persist-jar-with-dependencies.jar /home/alunos-persist-jar-with-dependencies.java

# Entry with exec
ENTRYPOINT exec java $JAVA_OPTS -jar /home/alunos-persist-jar-with-dependencies.java
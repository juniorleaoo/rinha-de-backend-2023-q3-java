FROM openjdk:21-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=80 -XX:+UseParallelGC -jar /app.jar ${0} ${@}"]
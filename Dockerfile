FROM openjdk:21-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
ARG JAVA_OPTS="-XX:MaxRAMPercentage=80 -XX:+UseParallelGC"
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
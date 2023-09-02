FROM openjdk:19-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -XX:MaxRAMPercentage=80 -XX:+UseParallelGC --enable-preview -jar /app.jar ${0} ${@}"]
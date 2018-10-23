FROM openjdk:8-jre-alpine
MAINTAINER Rodolpho S. Couto
RUN apk update && apk add curl jq tzdata
ENV TZ America/Sao_Paulo
RUN mkdir -p /app
WORKDIR /app
COPY build/libs/spring-fu-sample.jar /app
ENTRYPOINT exec java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm -jar spring-fu-sample.jar
CMD [""]
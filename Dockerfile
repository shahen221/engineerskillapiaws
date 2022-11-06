FROM openjdk:8-jdk-alpine
MAINTAINER shah syed
VOLUME /tmp
RUN apk add --no-cache git
RUN git clone https://github.com/shahen221/engineerskillapiaws.git
WORKDIR engineer-skill-api-aws
RUN apk add --no-cache gradle
RUN gradle bootJar
WORKDIR target
RUN mv *.jar EngineerSkillAPIAWS.jar
ENTRYPOINT ["java","-jar","/engineer-skill-api-aws/target/EngineerSkillAPIAWS.jar"]
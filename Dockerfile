FROM openjdk:8-jdk-alpine
MAINTAINER shah syed
VOLUME /tmp
RUN apk add --no-cache git
RUN git clone --branch master https://github.com/shahen221/engineerskillapiaws.git
FROM gradle:6.9.2-jdk8
WORKDIR /EngineerSkillAPIAWS
copy . /EngineerSkillAPIAWS
RUN gradle bootJar
WORKDIR target
copy *.jar /EngineerSkillAPIAWS/target/EngineerSkillAPIAWS.jar
ENTRYPOINT ["java","-jar","/EngineerSkillAPIAWS/target/EngineerSkillAPIAWS.jar"]
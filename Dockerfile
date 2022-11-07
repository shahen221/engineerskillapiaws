FROM openjdk:8-jdk-alpine
MAINTAINER shah syed
VOLUME /tmp
FROM gradle:6.9.2-jdk8
WORKDIR /EngineerSkillAPIAWS
copy . /EngineerSkillAPIAWS/
RUN gradle bootJar
WORKDIR target
RUN echo "Before copying jar file"
RUN ls -l /EngineerSkillAPIAWS/build/libs
RUN cp /EngineerSkillAPIAWS/build/libs/EngineerSkillAPIAWS.jar /EngineerSkillAPIAWS/target/EngineerSkillAPIAWS.jar
ENTRYPOINT ["java","-jar","/EngineerSkillAPIAWS/target/EngineerSkillAPIAWS.jar"]
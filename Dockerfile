# FROM alpine/git:1.0.4 AS git
# 
# RUN git clone https://gitlab.maxiv.lu.se/kits-ims/ISPyB.git && \
#     mv ISPyB /var/ISPyB



FROM maven:3.5.3-jdk-7-slim AS builder

# If the git section of the Dockerfile hadn't been uncommented
#   COPY --from=git /var/ISPyB /var/ISPyB
#   WORKDIR /var/ISPyB

# With the repo in the same dir, we have to do:
COPY . /var/ISPyB
WORKDIR /var/ISPyB

RUN mvn install:install-file -Dfile=dependencies/securityfilter.jar -DgroupId=securityfilter -DartifactId=securityfilter -Dversion=1.0 -Dpackaging=jar && \
    mvn install:install-file -Dfile=dependencies/securityaes.jar -DgroupId=securityaes -DartifactId=securityaes -Dversion=1.0 -Dpackaging=jar && \
    mvn install:install-file -Dfile=dependencies/jhdf.jar -DgroupId=jhdf -DartifactId=jhdf -Dversion=1.0 -Dpackaging=jar && \
    mvn install:install-file -Dfile=dependencies/jhdf5.jar -DgroupId=jhdf5 -DartifactId=jhdf5 -Dversion=1.0 -Dpackaging=jar && \
    mvn install:install-file -Dfile=dependencies/jhdf5obj.jar -DgroupId=jhdf5obj -DartifactId=jhdf5obj -Dversion=1.0 -Dpackaging=jar && \
    mvn install:install-file -Dfile=dependencies/jhdfobj.jar -DgroupId=jhdfobj -DartifactId=jhdfobj -Dversion=1.0 -Dpackaging=jar && \
    mvn install:install-file -Dfile=dependencies/ispyb-userportal-gen-1.9.jar -DgroupId=ispyb -DartifactId=ispyb-userportal-gen -Dversion=1.9 -Dpackaging=jar && \
    mvn install:install-file -Dfile=dependencies/Struts-Layout-1.2.jar -DgroupId=struts-layout -DartifactId=struts-layout -Dversion=1.2 -Dpackaging=jar && \
    mvn install -Dispyb.site=MAXIV -Dispyb.env=production && \
    cp ispyb-ear/target/ispyb.ear /var/ispyb.ear



FROM jboss/wildfly:8.2.0.Final

# Add a admin user for the wildfly management interface
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#70365 --silent   

# Add the ear file to deploy
COPY --from=builder /var/ispyb.ear /opt/jboss/wildfly/standalone/deployments/

# Add the MySQL connector and change the standalone.xml file to reflect your setup.
COPY configuration/standalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml
ADD configuration/mysql /opt/jboss/wildfly/modules/system/layers/base/com/mysql

ENTRYPOINT ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

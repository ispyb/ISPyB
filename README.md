# ISPyB Project [![Build Status](https://travis-ci.org/antolinos/ispyb.png)](https://travis-ci.org/antolinos/ispyb)


1. [Installing](#installing)
2. [Versioning](#versioning)
3. [Database creation and update](#database-creation-and-update)
4. [Database schema](#database-schema)
5. [Graylog on Widlfly 8.2](#graylog)

# Installing

1. Clone or fork the ISPyB repository and then clone it by typing:
```
git clone https://github.com/ispyb/ISPyB.git
```

2. ISPyB uses some local libraries located on /dependencies then some jars should be added to your local maven repository

```
cd dependencies
mvn install:install-file -Dfile=securityfilter.jar -DgroupId=securityfilter -DartifactId=securityfilter -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=securityaes.jar -DgroupId=securityaes -DartifactId=securityaes -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jhdf.jar -DgroupId=jhdf -DartifactId=jhdf -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jhdf5.jar -DgroupId=jhdf5 -DartifactId=jhdf5 -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jhdf5obj.jar -DgroupId=jhdf5obj -DartifactId=jhdf5obj -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jhdfobj.jar -DgroupId=jhdfobj -DartifactId=jhdfobj -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=Struts-Layout-1.2.jar -DgroupId=struts-layout -DartifactId=struts-layout -Dversion=1.2 -Dpackaging=jar
mvn install:install-file -Dfile=ojdbc6.jar -DgroupId=ojdbc6 -DartifactId=ojdbc6 -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=ispyb-WSclient-userportal-gen-1.3.jar -DgroupId=ispyb -DartifactId=ispyb-WSclient-userportal-gen -Dversion=1.3 -Dpackaging=jar
```

3. Configure the SITE property

Copy the settings.xml present in ispyb-parent/configuration into ~/.m2/settings.xml if you are using a Linux machine or configure maven accordingly to use this setting file [https://maven.apache.org/settings.html](https://maven.apache.org/settings.html)

For example:
```
<settings>
	<proxies>
		<proxy>
			<id>esrf</id>
			<active>true</active>
			<protocol>http</protocol>
			<host>proxy.esrf.fr</host>
			<port>3128</port>
			<nonProxyHosts>localhost</nonProxyHosts>
		</proxy>
		<proxy>
			<id>esrf</id>
			<active>true</active>
			<protocol>https</protocol>
			<host>proxy.esrf.fr</host>
			<port>3128</port>
			<nonProxyHosts>localhost</nonProxyHosts>
		</proxy>
	</proxies>
	<profiles>
		<profile>
			<id>ESRF</id>
			<properties>
				<ispyb.site>ESRF</ispyb.site>
				<smis.ws.usr>******</smis.ws.usr>
				<smis.ws.pwd>******</smis.ws.pwd>
				<jboss.home>/opt/wildfly</jboss.home>
			</properties>
		</profile>
	</profiles>
	<activeProfiles>
		<activeProfile>ESRF</activeProfile>
	</activeProfiles>
</settings>
```



These properties will set the profile to be used in the ispyb-ejb pom.xml to configure ISPyB.


4. Build the project by using maven
```
mvn clean install
```

If the build has succeed a summary repost should appear:
```
[INFO] Reactor Summary:
[INFO] 
[INFO] ispyb-parent ...................................... SUCCESS [0.251s]
[INFO] ispyb-ejb3 ........................................ SUCCESS [10.243s]
[INFO] ispyb-ws .......................................... SUCCESS [1.751s]
[INFO] ispyb-ui .......................................... SUCCESS [7.212s]
[INFO] ispyb-ear ......................................... SUCCESS [5.048s]
[INFO] ispyb-bcr ......................................... SUCCESS [2.217s]
[INFO] ispyb-bcr-ear ..................................... SUCCESS [1.806s]

```

## Versioning

Use versions:set from the versions-maven plugin:
```
mvn versions:set -DnewVersion=5.0.0
```

If you are happy with the change then:
```
mvn versions:commit
```
Otherwise
```
mvn versions:revert
```


## Database creation and update
Run the creation scripts present in the module ispyb-ejb, to run the scripts you will need a user “pxadmin” with full permissions.

ispyb-ejb/db/scripts/pyconfig.sql
This corresponds to the menu options, and contains both structure and data

ispyb-ejb/db/scripts /pydb.sql
This corresponds to the ISPyB metadata and contains only the database structure.

ispyb-ejb/db/scripts/schemaStatus.sql
This corresponds to the entries present in SchemaStatus table and gives an overview of the executed update scripts.

The creation scripts are normally updated for each tag, but if you are using the trunk version you may have to run the update scripts present in :
ispyb-ejb/db/scripts/ahead

Check before the entries in SchemaStatus table to know which scripts to execute.
The scripts already run for the current tag are in :
ispyb-ejb/db/scripts/passed

### Creating an update script
The 1st line must be:
```
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_06_06_blabla.sql','ONGOING');
```
then the update script

....

and the last line must be:
```
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_06_06_blabla.sql';
```
This allows to keep the SchemaStus table uptodate and to know which scripts have been run.
You can look for examples in ispyb-ejb/db/scripts/passed/2017

### Database schema

Please do not forget to update the database schema :
 https://github.com/ispyb/ISPyB/blob/master/documentation/database/ISPyB_DataModel_5.mwb 
 
This schema can be updated using MySQLWorkbench (free tool from MySQL).


### Graylog on wildfly 8.2

Download [biz.paluch.logging](http://logging.paluch.biz) that provides logging to logstash using the Graylog Extended Logging Format (GELF) 1.0 and 1.1. 

Then create a custom handler on standalone.xml
```
  <profile>
        <subsystem xmlns="urn:jboss:domain:logging:2.0">
	...
	<custom-handler name="GelfLogger" class="biz.paluch.logging.gelf.wildfly.WildFlyGelfLogHandler" module="biz.paluch.logging">
		<level name="INFO"/>
		<properties>
		    <property name="host" value="udp:graylog-dau.esrf.fr"/>
		    <property name="port" value="12201"/>
		    <property name="version" value="1.0"/>
		    <property name="facility" value="ispyb-test"/>
		    <property name="timestampPattern" value="yyyy-MM-dd"/>
		</properties>
	</custom-handler>
	...
	<logger category="ispyb">
		<level name="INFO"/>
		<handlers>
		    <handler name="ISPYB"/>
		    <handler name="GelfLogger"/>
		</handlers>
	</logger>
```

I had some problems with the unvalid messages because of timestapPatter. It was fixed by using:
```
<property name="timestampPattern" value="yyyy-MM-dd"/>
```




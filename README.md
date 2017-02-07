# ISPyB Project


## Installing

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

3. Build the project by using maven
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


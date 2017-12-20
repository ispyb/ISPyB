# ISPyB Developers Guide

**Version 4.0: New Server WildFly 8.2 and using Maven**

**Version 3.7: New eclipse configuration**

**Version 3.6: Archiving data at ESRF added**

**Version 3.5: Folder structure updated with js; ant targets updated and
authentication with the web services added**

**Version 3.4: Default timeout updated**

**Version 3.3: Use of the DoNotCommit.properties files to store sensible data**

**Version 3.2: New server pyproserv; new logging**

## Table of contents

  * [Software required](#software-required)
  * [Installation](#installation)
  * [Installation on ispyb production machine](#installation-on-ispyb-prod-machine)
  * [Using Maven](#using-maven)
  * [Project Structure TODO](#project-structure-todo)
  * [First steps](#first-steps)
  * [Profiles: site specific files and configuration](#profiles-site-specific-files-and-configuration)
  * [Naming Conventions TODO](#naming-conventions-todo)
  * [Security](#security)
  * [Update ISPyB from SMIS database](#update-ispyb-from-smis-database)
  * [Archiving](#archiving)
  * [UML data model](#uml-data-model)
  * [Using junit testing TODO](#using-junit-testing-todo)
  * [Some hints](#some-hints)

## Software required

All the software required may be directly downloaded from internet

  * A Java SE Development Kit JDK 7
    * <http://www.oracle.com/technetwork/java/javase/downloads/index.html>

  * WildFly Application Server 8.2.0Final
    * <http://www.jboss.org/jbossas/downloads/>

  * Eclipse IDE for java EE developers (last version)
    * <http://www.eclipse.org/downloads/>
    * from eclipse interface connect to marketplace and fetch JBOSSTOOLS.

  * XDoclet 1.2.3
    * <http://xdoclet.sourceforge.net/xdoclet/install.html>

  * MySQL 5.X
    * <http://dev.mysql.com/downloads/mysql/5.2.html>

  * Axis1.4 libraries
    * <http://axis.apache.org/axis/>

## Installation

### Checkout Ispyb from SourceForge with SVN

#### Install SVN/Subversive in Eclipse

The Subversive distribution consists of two parts: the **Subversive plug-in**
and Subversive **SVN Connectors**. Both parts are required.

 1. Use Eclipse marketplace to install **Subversive plug-in**

 2. Select install software in the Help menu of eclipse, then use the address:

    <http://community.polarion.com/projects/subversive/download/eclipse/3.0/luna-site/>

    and retrieve the SVN connectors.

#### Connect to ISPyB SourceForge repository

 1. Open the SVN Repository Exploring View in Eclipse

 2. Create a new repository location with:

    * URL: <https://forge.epn-campus.eu/svn/ispyb4>

    * User/Password: empty for checkout (anonymous login), your SourceForge
      username/password for checkin/checkout

 3. Checkout `trunk/ISPyB_project`

### Install on development platform WILDFLY 8.2

Firstly, the software above (see [Installation](#installation)) has to be
installed along with the definition of the following environment variables and
respective locations:

  * `JAVA_HOME=/j2sdk_root/j2sdk1.7.x.x`

  * `JBOSS_HOME=/jboss_root/wildfly-8.2Final`

Then, the project must be imported from SVN or other folder to Eclipse and the
respective paths referencing libraries corrected.

To avoid errors when using specified-type list, xdoclet 1.2.3 needs a small
fix: use `xjavadoc-1.5-snapshot050611.jar` to replace `xjavadoc-1.1.jar` in
xdoclet lib directory.  (Find this jar in the files directory on the forge.)
Remove the `xjavadoc-1.1.jar`.

Change `standalone.conf` to adapt the `JAVA_OPTS` as they were for JBOSS6.

### Database connection

Copy the `mysql` folder present from `ispyb-parent/configuration/mysql` to the
`wildfly-8.2.0.Final/modules/system/layers/base/com` folder.

### Configuring standalone.xml

Save the original `standalone.xml` from
`wildfly-8.2.0.Final/standalone/configuration/standalone.xml` to
`wildfly-8.2.0.Final/standalone/configuration/standalone.xml.orig`.

Copy the `standalone.xml.example` present in `ispyb-parent/documentation` to
`wildfly-8.2.0.Final/standalone/configuration`.

Customize it with your database as follows.

Update the datasources:

```xml
<datasource jndi-name="java:jboss/ispybconfigDS" pool-name="ispybconfigDS"
    enabled="true" use-java-context="true">
  <connection-url>jdbc:mysql://pydevserv.esrf.fr:3308/pyconfig</connection-url>
  <driver>mysql-connector-java-5.1.21.jar</driver>
  <security>
    <user-name>pxuser</user-name>
    <password>*****</password>
  </security>
</datasource>
<datasource jndi-name="java:jboss/ispybDS" pool-name="ispybDS"
    enabled="true" use-java-context="true">
  <connection-url>jdbc:mysql://pydevserv.esrf.fr:3308/pydb</connection-url>
  <driver>mysql-connector-java-5.1.21.jar</driver>
  <security>
    <user-name>pxuser</user-name>
    <password>*****</password>
  </security>
</datasource>
```

Change the drivers definition if needed:

```xml
<driver name="mysql-connector-java-5.1.21.jar" module="com.mysql"/>
```

Add a security domain:

```xml
<security-domain name="ispyb" cache-type="default">
  <authentication>
    <login-module code="ispyb.server.security.LdapLoginModule" flag="required">
      <module-option name="java.naming.factory.initial"
          value="com.sun.jndi.ldap.LdapCtxFactory"/>
      <module-option name="java.naming.provider.url"
          value="ldap://ldap.esrf.fr:389/"/>
      <module-option name="java.naming.security.authentication"
          value="simple"/>
      <module-option name="allowEmptyPasswords"
          value="false"/>
      <module-option name="principalDNPrefix"
          value="uid="/>
      <module-option name="principalDNSuffix"
          value=",ou=People,dc=esrf,dc=fr"/>
      <module-option name="groupUniqueMember"
          value="uniqueMember"/>
      <module-option name="groupAttributeID"
          value="cn"/>
      <module-option name="groupCtxDN"
          value="ou=Pxwebgroups,dc=esrf,dc=fr"/>
    </login-module>
  </authentication>
</security-domain>
```

Add a listener for ajp if needed:

```xml
<server name="default-server">
  <!-- ... -->
  <ajp-listener name="ajp" socket-binding="ajp" max-parameters="20000"/>
  <http-listener name="default" socket-binding="http" max-parameters="20000"/>
  <!-- ... -->
</server>
```

If you want to be able to edit directly the jsps without having to redeploy,
change:

```xml
<jsp-config/>
```

to:

```xml
<jsp-config development="true"/>
```

Configure the logging and add the user logged to every entry:

```xml
<formatter name="PATTERN">
  <pattern-formatter
      pattern="%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p \[%c\] (%t) (userId %X{userId}) %s%e%n"/>
</formatter>
```

### Using HDF libraries

Create a module `hdfgroup` in:

```
modules/system/layers/base/org
```

to have:

```
modules/system/layers/base/org/hdfgroup/lib64
```

Copy into the `lib64` directory above the:

```
libjhdf.so  libjhdf.dll
libjhdf5.so libjhdf.dll
```

files present in `ispyb-parent/configuration/hdf5` folder.

Add the HDF5 config JVM OPTS in the `standalone.conf`:

```
-Djava.library.path=/ispyb/jboss/modules/system/layers/base/org/hdfgroup/lib64
```

### Increase JVM memory size

In the file `${JBOSS_HOME}/bin/standalone.conf`, replace

```
JAVA_OPTS="$JAVA_OPTS -Dprogram.name=$PROGNAME"
```

with (on local computer):

```
JAVA_OPTS="$JAVA_OPTS -Dprogram.name=$PROGNAME -Xms256m -Xmx1024m -XX:MaxPermSize=512m"
```

or on the production server with:

```
JAVA_OPTS="$JAVA_OPTS -Dprogram.name=$PROGNAME -Xms1024m -Xmx4096m -XX:MaxPermSize=1024m"
```

This is to avoid the "Out of Memory" error when processing big request to the
DB, per example with the update ISPyB from SMIS database process.

In Eclipse, double click on the server name in the sever window to open the
jboss overview, then click on "Open launch configuration" and add the above
parameters in the VM arguments.

#### Change the JBoss transaction timeout TODO

In the file

```
${JBOSS_HOME}/server/default/deploy/transaction-jboss-beans.xml
```

modify the `defaultTimeout` (300 by default):

```xml
<bean name="CoordinatorEnvironmentBean"
    class="com.arjuna.ats.arjuna.common.CoordinatorEnvironmentBean">

  <annotation>@org.jboss.aop.microcontainer.aspects.jmx.JMX(name="jboss.jta:name=CoordinatorEnvironmentBean", exposedInterface=com.arjuna.ats.arjuna.common.CoordinatorEnvironmentBeanMBean.class, registerDirectly=true)</annotation>

  <constructor factoryClass="com.arjuna.ats.arjuna.common.arjPropertyManager"
      factoryMethod="getCoordinatorEnvironmentBean"/>

  <property name="enableStatistics">false</property>
  <property name="defaultTimeout">900</property>
</bean>
```

This is to avoid the transaction timeout when running the cron job in charge of
the database update (WSClient).

## Installation on ispyb production machine

The production machine is `pyproserv`.

Same as [Installation](#installation) except for xdoclet. There is no need to
install them.

jboss is on `/ispyb/jboss`.

Special ispyb commands exist and should be used to start, stop, copy .ear,
launch database update from smis.

It is possible to have their full list and how to use them by typing `ispyb
help`.

For example:

  * To stop jboss server: `ispyb stop jboss`

  * To start jboss server: `ispyb start jboss`

  * To copy new .ear: `ispyb deploy ispyb.ear`

Only files in `jboss/server/default/conf` and in `jboss/server/default/deploy`
can be updated.

Backups repositories exist which store old versions of copied and replaced
files: `/ispyb/backup-deploy` and `/ispyb/backup-conf`.

Different logs can be found in `/usr/local/logs`:

  * `jboss_console.log`: to see it use `tail –f jboss_console.log`

  * `jboss_run.log`: keeps track of stops and restarts of jboss

  * `ispyb_run.log`: keeps track of ispyb commands

New folder for uploaded files in: `/nobackup.uploads/files` (up to 4 Go).

## Using Maven

### Install maven 3.1.1

RFC: `ispyb.site` is already on `pom.xml` and could be moved to
`ispyb-parent/pom.xml`.

Configure your own `settings.xml` (copy the one from `maven/conf` and update
them for you).

A part of the site profile are set in this file, see an example in
`documentation/settings.xml.example`.

### Import ISPyB

Import the ispyb-parent project.  In the project `ispyb-parent` you will find
the 6 projects:

  * `ispyb-ear`: contains the 3 followings modules as jars and wars, has to be
    deployed on the server.

  * `ispyb-ejb`: contains all the ejbs + some others classes like security,
    commons, hdf5, it contains also all the properties and the Constants class
    which will be used by `ispyb-ws` and `ispyb-ui` modules.

  * `ispyb-ui`: contains the "web client" part with all the web user
    interfaces, struts actions/forms, tiles, jsp, js, ...

  * `ispyb-ws`: contains all the webservices.

  * `ispyb-bcr`: contains the "web client" part of the bar code reader
    application used for the dewar tracking.

  * `ispyb-bcr-ear`: contains the preceding module as war and is linked to
    the `ispyb-ejb` through a dependency in the `pom.xml`, this module can be
    deployed or not separately from `ispyb.ear`.

In Eclipse, select "import project", then select the maven project and point to
the `pom.xml` of the `ispyb-parent`.

If some projects are missing, select once more "import project", maven project,
and point to the pom of the missing module.

Run maven install on the 4 modules.

You will need to install missing jars (maven can not find them in maven
repository) picking them in the jboss6 ispyb installation.

Added all these libraries in `ispyb-parent/configuration/maven/jar` as well as
a script (`installCustomJavaLibrariesToMaven.sh`) that will install them on the
maven local repository:

```
mvn install:install-file -Dfile=xxx\ISPyB_project\server\lib\securityfilter.jar -DgroupId=securityfilter -DartifactId=securityfilter -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=xxx\ISPyB_project\server\lib\securityaes.jar -DgroupId=securityaes -DartifactId=securityaes -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=xxx\server\default\lib\jhdf.jar -DgroupId=jhdf -DartifactId=jhdf -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=xxx\server\default\lib\jhdf5.jar -DgroupId=jhdf5 -DartifactId=jhdf5 -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=xxx\server\default\lib\jhdf5obj.jar -DgroupId=jhdf5obj -DartifactId=jhdf5obj -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=xxx\server\default\lib\jhdfobj.jar -DgroupId=jhdfobj -DartifactId=jhdfobj -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=xxx\ISPyB_project\client\lib\struts-layout-1.2.jar -DgroupId=struts-layout -DartifactId=struts-layout -Dversion=1.2 -Dpackaging=jar
```

To use the BCR for dewar tracking:

```
mvn install:install-file -Dfile=dewarAPI.jar -DgroupId=ispyb -DartifactId=dewarAPI -Dversion=1.0 -Dpackaging=jar
```

### Webservices

Import the project `ispyb-WSclient-gen` to generate a jar containing the
classes built from wsdl.

Customize the `bindings.xml` file in `src/main/resources` to the correct wsdl
location.

### Site and deployment customization

In the `pom.xml` of `ispyb-ui` and `ispyb-ejb` you will find profiles, there
are for now 2 types of profiles:

  * Deployment profiles

    `DEV`, `ALT`, and `PROD` define the way the application is deployed: for
    example with `DEV`, the javascript is not minimized.

  * Site configuration profiles (see [Profiles: site specific files and
    configuration](#profiles-site-specific-files-and-configuration))

    `ESRF`, `EMBL`, `SOLEIL`, `MAXIV` profiles contains the properties
    previously defined in the `ISPyB_XXX.properties`.

    These profiles with their properties are defined in the `ispyb-ejb/pom.xml`
    only because the other modules are dependent on this one.

    The profile is defined in the maven `settings.xml`
    (see [Profiles: site specific files and
    configuration](#profiles-site-specific-files-and-configuration)).

Properties:

  * `DoNotCommit.properties`

    These properties have been put outside of the project, in the maven
    `settings.xml`, so you can safely define them, and get rid of the
    `DoNotCommit.properties`.

## Project Structure TODO

### Overview

The project structure is divided in 3 big areas (the main folders):

  * `ispyb-ui`: contains all the client side files.

  * `ispyb-ejb`: contains all the server side files + commons + properties.

  * `ispyb-ws`: contains all the web services.

Besides that, there is one more folder in the project root:

  * `ispyb-parent`: is the parent folder, containing also some documentation
    and configuration files.

### Folder structure

#### ispyb-ejb

  * `src/main/java`

    Source classes for server side *Entity EJBs (CMPs)* and *Session Facade
    EJBs*.  Source classes for common classes.

  * `src/main/resources/META-INF`

    Persistence info:

      * `persistence.xml`

  * `db/scripts`

    Folder to store the sql scripts to generate, update the database structure
    and data.

#### ispyb-ui

  * `client/src`

    Source classes for client side: *Struts* *Actions* and *Forms* and others
    classes.

  * `client/build`

    Compiled classes from `client/src` folder.

  * `client/dist`

    War client file, which contains `client/build`, `client/generate/meta`,
    `client/lib` and `client/web` folders.

  * `client/etc/conf`

    Configuration files to be deployed in *WEB-INF* web application folder.  At
    the moment there are two files:

    * `authfilter.xml`: Authentication configuration for this web application.

    * `tiles-defs.xml`: Tiles definition file.

  * `client/etc/mergedir`

    Folder where are the files that will be mixed with the *XDoclet* generated
    tags defined in the files located in `client/src` folder to create the
    following files:

    * `jboss-web.xml`

    * `struts-config.xml`

    * `web.xml`

    For additional information about the names of the files to be put in
    this folder, consult the files with the names above and located in
    `client/generate/meta` folder.

  * `client/etc/resources`

    Folder where the resources properties are.

  * `client/generate/meta`

    XDoclet generated meta info. Contains web, Struts and Jboss descriptors:

    * `jboss-web.xml`

    * `struts-config.xml`

    * `validation.xml`

    * `web.xml`

  * `client/generate/src`

    This folder was not used at the moment of this manual.

  * `client/lib`

    Folder with the libraries used by the client. At the moment there are only
    struts framework libraries.

  * `client/web`

    This folder is the root of the web application and contains other
    subfolders:

    * `client/web/css`: Cascade style sheets files.

    * `client/web/images`: images.

    * `client/web/js`: js files:

      * `client/web/js/css`: all css used

      * `client/web/js/external`: external js used

      * `client/web/js/ispyb`: js files for ispyb:

        * `client/web/js/ispyb/biosaxs`: js files for biosaxs

        * `client/web/js/ispyb/min`: js file minified

        * `client/web/js/ispyb/mx`: js files for ispyb mx

        * `client/web/js/ispyb/utils`: js files utils

    * `client/web/tiles`: *JSPs* for tiles applications:

      * `client/web/tiles/bodies`: All bodies of the pages. In this folder
        there are several folders to better divide the different pages.

      * `client/web/tiles/common`: Common *JSPs* fragments to all website:

        * `client/web/tiles/common/footer`

        * `client/web/tiles/common/header`

        * `client/web/tiles/common/left`

      * `client/web/tiles/layouts`: *JSPs* with the layout definitions of the
        pages.

      * `client/web/WEB-INF`: Internal tld files for struts framework.

  * `client/applet/src`

    This folder contains the source of applets used in html pages. At the
    moment only exists one.

  * `client/applet/build`

    This folder contains the applets built.

  * `client/applet/lib`

    This folder contains all the JDLImage code modified to better fit our
    project and built in a jar file. This project is a courtesy of John
    Campbell (<http://www.ccp4.ac.uk/jwc/image_applet/ImageDisplay_ccp4.html>)
    and the source code is present on CVS.

#### ispyb-ws

  * `src/main/java`

    Source classes for webservices.

### Menu Structure

#### Database Model

![](images/menu-db-model.png)

#### How it works

There are three main tables:

  * `Menu`: contains the hierarchy of menus, menu target URL and menu name.

  * `MenuGroup`: Contains all sort of groups existing in ISPyB.

  * `Menu_has_MenuGroup`: Establish the relation m-n of the above tables.

However, just the two first tables are mapped as EJBs.

## First steps

### Server Side: First CMPs

To be filled with ejb3 automated generation or using ispyb templates.

The eclipse templates are found on `/server/etc/ispyb_templates.xml`.

See [How to configure ejb3 for ISPyB](ISPyB_ejb3.md).

The database connection shall be set in the `persistence.xml`
file which should be in `META_INF` of `ejb3.jar`, it is found on
`ispyb-ejb/src/main/resources/META-INF`.

### Client Side: First WebPages

#### Creating a simple page

This project uses Tiles for layout presentation.  Thus, instead of creating a
complete page with included tags repeatedly for every page (for instance, to
include header, footer and menus), we just create one part of the page.

For most of the pages associated to business functionalities a body is just
created. The rest of the layout of this page is defined in tiles configuration
file.

To create a simple text page it is needed to:

 1. Create a JSP with the body of the page and put it in
    `client/web/tiles/bodies/<content related folder>`.

 2. Create an entry with the page definition (`definition` tag with `extends`
    attribute) in `client/etc/conf/tiles-defs.xml`. It is necessary to define
    the name of the page, which layout it uses and fill in all the values
    defined in that layout. You can check the layout definition in the top of
    this xml file. For instance:

    ```xml
    <definition name="user.welcome.page" extends="site.main.layout" >
      <put name="title" value="ISPyB" />
      <put name="body" value="/tiles/bodies/welcome/user.jsp" />
      <put name="links" value="null" />
    </definition>
    ```

 3. Then, and because we cannot request a page in the way it has been
    previously defined, we have to create an action to call the new page (only
    if is called directly by http request). To that, we must create a new entry
    tag called `action` in `client/etc/mergedir/struts-actions.xml` which has
    two main attributes: `path`, the name requested in URL, and `forward`, the
    previous definition in `tiles-defs.xml`:

    ```xml
    <action
      path="/user/welcomeUserPage"
      forward="user.welcome.page"
      scope="request"
      validate="false"/>
    ```

#### Creating a form

To create a form submission page is necessary to create a page (a page with a
form) in the way like described above. The JSP body of this page has to contain
a struts form tag like:

```
<html:form action="<action name>.do">
```

where `<action name>` in the tag above is the action to be invoked when the
submit button is pressed.

This action is an **Action Class** and the form passed to it is the **Action
Form** and contains the data filled in the page form. Both definitions have to
be developed.

For the Form Action there are 3 possibilities to define it:

 1. If you don’t need JavaScript validation before submit the form, it can be
    defined in `client/etc/mergedir/struts-forms.xml` as a `DynaValidatorForm`.
    (See logon.)

 2. If you need JavaScript validation the easiest way to do it is coping the
    auto generated form file which is in `server/generate/form` to client
    source folder, modify it in order to have only the fields you need and add
    XDoclet `@struts.validator` tags for validation requirements.

 3. You can also simply copy one already existing.

All parameters passed through Action Form belong to the request.

Only one Form, `BreadCrumbForm`, is used to keep some parameters in the
session. These parameters will be displayed in the breadcrumbs bar at the top
of the body of the page to show useful information.

To code the action, create a new class in client source folder which extends
either `Action` or `DispatchAction` class and include on the top of it the
XDoclet definitions for this action:

  * `@struts.action`

    * `name`: Name of the form defined in `struts-forms.xml`.

    * `path`: `<name of the action>.do`.

    * `input`: Tiles definition page where the form is filled.

    * `parameter`: In case of using `DispatchAction`, this parameter is used to
      select the name of the method defined in the action class.

  * `@struts.action-forward`

    * `name`: Name of the forward returned by this action.

    * `path`: Tiles definition page to be forwarded.

#### Validation and Errors

Struts Layout only provides features for validation on server side.

The validation consists in putting messages in one queue (`ActionErrors`) which
uses a different name to index every entry:

  * `ActionErrors.GLOBAL_ERROR`: For global errors.

  * The name of the field in the form.

It's also important to use the `saveErrors` method to save only once the queue
on the request: `saveErrors(request, errors)`.

The following example is standard use of what has been described:

```java
public ActionForward displayFromMenu(ActionMapping mapping, ActionForm actForm,
    HttpServletRequest request, HttpServletResponse response) {

  ActionErrors errors = new ActionErrors();

  try {

    // ...

    // Check Required fields populated
    // The error queue is a parameter
    if (!this.validate(form, errors)) {
      ActionForward f =  mapping.findForward("shippingCreatePage");
      saveErrors(request, errors);
      return f;
    }

    // ...

    // EXAMPLE OF GLOBAL ERROR
    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.detail",
        "Nah Nah Nah c'est pas bon"));

  } catch (Exception e) {
    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.detail",
        e.toString()));
    ClientLogger.getInstance().error(e.toString());
  }

  saveErrors(request, errors);

  if (!errors.isEmpty()) {
    return (mapping.findForward("error"));
  }
  return mapping.findForward("shippingViewPage");
}

private boolean validate(ViewShippingForm form, ActionErrors errors) {
  boolean requiredFieldsPresent = true;

  if (form.getInfo().getProjectCode().length() == 0) {
    requiredFieldsPresent = false;
    ActionError actionError = new ActionError("errors.required",
        "Shipping Label");
    errors.add("info.projectCode", l_actionErrorPassword);
  }
  return requiredFieldsPresent;
}
```

## Profiles: site specific files and configuration

The site profile is defined in the `pom.xml` in `ispyb-ejb` module. The site
profile contains all the properties linked to a specific profile. See the
example of the `NEWSITE` profile in the `pom.xml`.

For example it defines the `ispyb.site` property which value will be loaded
and used in the Constants class (`SITE_IS_ESRF()` or `SITE_IS_SOLEIL()`, or
`SITE_ATTRIBUTE` for example).

The properties are defined in `ISPyB.properties` present in the `ispyb-ejb`
module, and the correct values depend on the site where it is installed.
The values of the `ISPyB.properties` are replaced by those in the `pom.xml`
profile.

You have to add in your maven `settings.xml` the active profile and its name
like:

```xml
<!-- ... -->
<profiles>
  <!-- ... -->
  <profile>
    <id>NEWSITE</id>
    <properties>
      <ispyb.site>NEWSITE</ispyb.site>
      <jboss.home>C:/java/appServers/wildfly-8.2.0.Final</jboss.home>
    </properties>
  </profile>
</profiles>
<!-- ... -->
<activeProfiles>
<activeProfile>NEWSITE</activeProfile>
</activeProfiles>
<!-- ... -->
```

Then, you have to customize the layout of the jsps according to your site
preferences.

You have to edit the following files of `ispyb-ui` module: `logon.jsp`,
`logo_bar.jsp`, `tab_bar.jsp`, `top_bar.jsp`, `context_bar.jsp`,
`unloggedLayout.jsp`. In these files use the `SITE_ATTRIBUTE` constant to
define what should be displayed in your case.

## Naming Conventions TODO

### Packages

#### Server Side TODO

All packages should have the prefix:

  * `ispyb.server.*`

The Entity and Facade EJBs should have the prefix:

  * `ispyb.server.data.`: for experiments database

  * `ispyb.server.config.`: for configuration database

#### Client Side TODO

All packages should have the prefix:

  * `ispyb.client.*`

### Actions

All Action classes should have the suffix:

  * `Action`

The name of the class has to be the same as the action name:

  * `<name>Action.java`

  * `<name>.do`

For example:

```java
/**
 * @struts.action path="/security/logon"
 */
public class LogonAction extends Action {
  // ...
}
```

### Tiles

All definitions created on `tiles-defs.xml` should have as name the prefix:

  * `<group name>.<menu name>.<identification name>.page`

For instance:

  * `user.shipping.container.create.page`

This **page** belongs to the **user** group, menu **shipping** and **creates**
a **container**.

## Security

### Security with JBOSS TODO

JBoss has several predefined ways to do the authentication and authorization.

To take advantage of these features, an application-policy should be chosen
from `server/default/cong/login-config.xml` or a new one has to be developed
and configured in this file.

In the client side, the policy to apply should be defined in web application
`jboss-web.xml` which is created from compilation using the file
`client/etc/mergedir/jbossweb-resource-env-ref.xml`:

```xml
<!-- Configuration to ESRF: LDAP Module -->

<security-domain>java:/jaas/ispyb </security-domain>
<context-root>ispyb</context-root>
```

### ESRF LDAP Structure

Because LDAP configuration of the ESRF can not be used with standard LDAP
Module provided with JBOSS, a new LDAP module has been developed.

Following is described how to configure the authentication with the developed
LDAP Module.

ESRF LDAP configuration (all groups belonging to the group):

```
dn: ou=Pxwebgroups,dc=esrf,dc=fr
ou: Pxwebgroups
objectClass: top
objectClass: organizationalunit
```

Example of the User group:

```
dn: cn=User,ou=Pxwebgroups,dc=esrf,dc=fr
ou: Pxwebgroups
objectClass: top
objectClass: groupOfUniqueNames
cn: User
uniqueMember: uid=ifx999,ou=people,dc=esrf,dc=fr
uniqueMember: uid=mx9999,ou=people,dc=esrf,dc=fr
uniqueMember: uid=leal,ou=people,dc=esrf,dc=fr
```

See the entries added in `standalone.xml` in [Installation](#installation).

### Simple authentication TODO

One of the aims of ISPyB is the portability and therefore the possibility of
using it in others facilities. The simple authentication is a built in module
which provides the same authentication schema as we use in ESRF LDAP. Its name
is `UsersRolesLoginModule`.

The policy should be added in the `standalone.xml` (see
[Installation](#installation)).

```xml
<application-policy name="simpleIspyb">

  <authentication>
    <login-module code="org.jboss.security.auth.spi.UsersRolesLoginModule"
        flag="required">
      <module-option
          name="usersProperties">props/users.properties</module-option>
      <module-option
          name="rolesProperties">props/roles.properties</module-option>
    </login-module>
  </authentication>

</application-policy>
```

Users and the respective passwords can be added in the following file
`jboss/server/default/conf/props/users.properties` with the syntax:

```
<username>=<password>
```

The roles assigned to each user are defined in
`jboss-3.2.6/server/default/conf/props/roles.properties` with the syntax:

```
<username>=<role>[,<role>...]
```

These are examples of both files:

  * `roles.properties`

    ```
    mx9999=User
    ifx999=User
    manager=User,Manager
    ```

  * `users.properties`

    ```
    mx9999=password1
    ifx999=password2
    manager=manager
    ```

### ISPyB Authorization and Authentication Model

The authentication method in ISPyB is based in the standard tomcat servlet:
`j_security_check`. See `tiles/bodies/logon/login.jsp` for more details.

The authorization method is based in URL patterns.

See `client/etc/mergedir/web-security.xml` for constraints and roles defined.

### Web service authentication

The web service authentication is done through ldap at ESRF:

```java
@RolesAllowed({ "WebService", "User", "Industrial"})
@SecurityDomain("ispyb")
@WebContext(authMethod="BASIC", secureWSDLAccess=false, transportGuarantee="NONE")
```

  * `@RolesAllowed` specifies the list of roles permitted to access web
    services.
  * `@SecurityDomain` references the application policy defined in the
    `standalome.xml` file.
  * `@WebContext` enables HTTP basic authentication.

## Update ISPyB from SMIS database

### Manual update

#### For each individual proposal

For each proposal account there is the possibility to update the database: all
sessions and samplesheets information concerning the proposal will be updated.

There is also the possibility to update only the samplesheet information.

#### For MXPress manager account

When logged as `fxmanage` it is possible to update the database for 1 or
several proposals and from/to specified dates.

### WSClient to update ISPyB from SMIS

WSClient is another eclipse java project which is saved on SVN.

It contains the client of the web service created from the class
`UpdateFromSMIS.java` of ISPyB project (`ispyb.server.smis`).

The class is `BatchUpdateFromSMIS.java`.

The client is called from a cron job on the ispyb machine every night (launched
at 04:12 am).

In the case this update fails, there is always the possibility to update
manually the database.

### Timeout

It is possible to change the timeout of the web service on the ispyb server
(see [Installation](#installation)).

## Archiving

At ESRF, data are archiving, but can be restored. See below the rules:

What will be archived are the subdirectories of `/data/pyarch/idXX` that have
names of proposals (e.g., `mx1234`). Criteria for archiving:

  * proposal end date or prolongation date older than XX months.

  * proposal has been restored for more than YY months XX and YY will be
    tuneable parameters, say 12 and 2 months, respectively.

Restorations will be done:

  * automatically as soon as a proposal be rescheduled or prolonged.

  * triggered by ISPyB upon user access. (On the data collection page,
    "Restore" button.)

Archiving will be done once a week. Once archived, the top-level directory
(e.g., `/data/pyarch/id14eh1/mx1234`) will be emptied, it permissions
changed so that only ISPyB can write to it (user jboss), and a single file
`.AUTO-ARCHIVED` created inside it.

If ISPyB needs a restoration, it will create a file `.RESTORE-PLEASE`
in the directory. Restoration will be performed ASAP and the both flags
(`.AUTO-ARCHIVED` and `.RESTORE-PLEASE`) removed. Original directory
permissions will be restored too.

## UML data model

The database model is written using DBDesigner (free download from
sourceforge). Then it is possible to connect to a MySQL database to update
either the database or the model, or to generate a new database.

When using dbdesigner, **do not use** standards inserts, and default values,
sometimes these lead to error when synchronising database with model.

## Using junit testing TODO

### Standalone mode

Copy the files `testJaas.config` and `standaloneClient.policy` of
`<ISPyB_project>/server/etc` to

```
jboss-6.1.0.Final/server/default/conf
```

### Testing

Write tests extending the EJB3Test.

## Some hints

JSP code: when using `"` inside a value = `"` use `\"` instead

### Start local JBoss using the machine name

Run JBoss with the option:

```
-b 0.0.0.0
```

Possibility to log into ISPyB from Web User Office tool (WUOT like SUN set, SMIS ...) by means of servlet filter.

Following is the changes todo to adapt it to the database of your WUOT:
	1/ Update "/client/etc/mergedir/securityfilter-config.xml" file to set the url and settings to connect to the database of your Web User Office Tool. 

	2/ Complete the table of roles of your Web User Office Tool with ISPyB's roles (User, Manager, Industrial, Fedexmanager, localcontactn blom, webservice ...).

	3/ According to the database set at step 1 for your WUOT:
		- user's credentials are verified in DatabaseLoginModuleHelper.doVerifyCredentials(...) 
		- user's roles are retrieved in DatabaseLoginModuleHelper.doGetRoleNamesForUser(...)
	
	4/ Open the build.xml file and:
		- remove uncomment the two lines below <!-- Copy securityfilter files -->

	5/ Comment the declaration of XxxLoginModule set in the [JBOSS_HOME]/server/default/conf/login-config.xml file.
		If you do not comment it the container managed security will not be desactivated.
	
	6/ Two ways to log into the application:
		- either from Web User Office Tool by calling the ISPyB login form (i.e.: j_security_check).
		- or from ISPyB web portal and supply login/password.

/*
 * $Id: LICENSE,v 1.5 2003/01/06 05:03:03 maxcooper Exp $
 * $Revision: 1.5 $
 * $Date: 2003/01/06 05:03:03 $
 *
 * ====================================================================
 * The SecurityFilter Software License, Version 1.1
 *
 * (this license is derived and fully compatible with the Apache Software
 * License - see http://www.apache.org/LICENSE.txt)
 *
 * Copyright (c) 2002 SecurityFilter.org. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by
 *        SecurityFilter.org (http://www.securityfilter.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "SecurityFilter" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact license@securityfilter.org .
 *
 * 5. Products derived from this software may not be called "SecurityFilter",
 *    nor may "SecurityFilter" appear in their name, without prior written
 *    permission of SecurityFilter.org.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE SECURITY FILTER PROJECT OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */
package ispyb.server.security;

import ispyb.common.util.Constants;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.securityfilter.realm.SimpleSecurityRealmBase;

import synchrotron.soleil.sunset.crypto.aes.AES;

/**
 * An implementation of SecurityRealmInterface that authenticates against a database
 * 
 * This LoginModule authenticates users with a password. 
 * 
 * It behaves exactly as a container managed security for HTTP / HTTPS requests.
 *  
 * But user's Principal will not automatically be propagated to EJB calls. Furthermore Single Sign-On is not supported by SecurityFilter.  
 *
 * This LoginModule recognizes the debug option. If set to true in the login Configuration, debug messages are sent to the output stream.
 *
 * @author CHADO
 */
public class DatabaseLoginModuleSecurityFilter extends SimpleSecurityRealmBase {
	
	private final Logger LOG = Logger.getLogger(DatabaseLoginModuleSecurityFilter.class);
	
	/**
	 * debug
	 */
	protected boolean debug_ = false;
	
	/**
	 * The roles from database attributes to username 
	 */
	public String [] usernameRoles;
	
	/**
	 * The options related to the authentication
	 */
	protected Map<String, String> securityOptions;
	
    /**
     * The JDBC driver to use.
     */
    protected String driverName; 
   

    /**
     * The connection URL to use when trying to connect to the database.
     */
    protected String databaseDriverUrl;
   
   
    /**
     * The username to use when trying to connect to the database.
     */
    protected String databaseUserName;


    /**
     * The password to use when trying to connect to the database.
     */
    protected String databaseUserPassword;

    /**
     * The query to retrieve user's username.
     */
    protected String userQuery;	


    /**
     * The query to retrieve user's roles
     */
    protected String roleQuery;
    
    /**
     * logger
     */
    protected String debug;
    
   /**
    * Authenticate a user.
    * 
    * @param username a proposal id
    * @param password a password, either sent from the Web User Office Tool or as entered by the user from ISPyB portal
    *
    * @return null if the user cannot be authenticated, otherwise a Principal object is returned
    */
   public boolean booleanAuthenticate(String username, String password) {
	   	
	    boolean valid = false;
	    
    	// retrieve, decode & decrypt password received
	    if (Constants.SITE_IS_SOLEIL()) {
	    	password = getDecryptPass(password);
	    }
	    
    	debug_ = "true".equalsIgnoreCase((String) getDebug());
	    
    	try {
	    	// verify user credential  
			DatabaseLoginModuleHelper.verifyCredentials(getSecurityOptions(), username, password);
			valid = true;
			
			// get user's role to log in to 
			if (usernameRoles == null) {			   
			    usernameRoles = DatabaseLoginModuleHelper.getRoleNamesForUser(getSecurityOptions(), username, password);
		    }
		    
		    String value = null;
		    for (String  role_ : usernameRoles) {
				value = role_;
				if (debug_) {
					LOG.debug(".booleanAuthenticate() --- usernameRoles value = "+ value );
				}
			}

		} catch (LoginException e) {
			LOG.error(".booleanAuthenticate() --- Exception -- wrong crendentials " + " validAuthentication = " + valid);
			e.printStackTrace();
			return valid;
		}
	   return true;
   }

   /**
    * Get decrypted password received from the Web User Office Tool (WUOT)
    * 
    * @param pwdComposed - if coming from WUOT then: pwdComposed = <i>prefix</i> + <i>Key</i> + <i>Password</i> if not pwdComposed = password.</br>
    * <i>prefix</i> equals UOT </br> <i>Key</i> is in Base64 encoded scheme </br> <i>Password</i> is encoded in Base64 after AES encryption
    * @return string value of the password
    */
   public String getDecryptPass(String pwdComposed) {
	   String password = null;
	   
	   // access from UOT
	   if (pwdComposed.startsWith("UOT,")) {
		   // retrieve key & password
		   String[] pwdSplitted = pwdComposed.split(",");
		   String keyBase64 = pwdSplitted[1];
		   String pwd = pwdSplitted[2];
		   // decoded & decrypt password received
			try {
				String keyStr = new String(Base64.decodeBase64(keyBase64.getBytes("utf-8")));
				password = AES.getInstance().decrypt(keyStr, pwd);
			} catch (UnsupportedEncodingException UnsupportedEncoding) {
				LOG.error(".getDecryptPass() --- Unsupported Encoding "+ UnsupportedEncoding);
			}
	   } else {
		   // direct access from ispyb portal
		   password = pwdComposed;
	   }
	   
	   return password;
   }
      
   /**
    * The role
    *
    * @param username - The name of the user
    * @param role - Name of a role
    * @return true if the user is in the role, false otherwise
    */
   public boolean isUserInRole(String username, String role) {
	   
	   boolean valid = false;
	   
	   ArrayList<String> dbRoles = new ArrayList<String>();
	   for (String dbRole : usernameRoles) {
		   dbRoles.add(dbRole);			   
	   }
	   
	   if (dbRoles.contains(role)) {
		   valid = true;
	   }
	
	   return valid;
   }
	
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
		if (debug_) {
			LOG.debug(" : driverName set to \'" + driverName + "\'");
		}
	}

	public String getDatabaseDriverUrl() {
		return databaseDriverUrl;
	}

	public void setDatabaseDriverUrl(String databaseDriverUrl) {
		this.databaseDriverUrl = databaseDriverUrl;
		if (debug_) {
			LOG.debug(" : databaseDriverUrl set to \'" + databaseDriverUrl + "\'");
		}
	}

	public String getDatabaseUserName() {
		return databaseUserName;
	}

	public void setDatabaseUserName(String databaseUserName) {
		this.databaseUserName = databaseUserName;
		if (debug_) {
			LOG.debug(" : databaseUserName set to \'" + databaseUserName + "\'");
		}
	}

	public String getDatabaseUserPassword() {
		return databaseUserPassword;
	}

	public void setDatabaseUserPassword(String databaseUserPassword) {
		this.databaseUserPassword = databaseUserPassword;
		if (debug_) {
			LOG.debug(" : databaseUserPassword set to \' *********** \'");
		}
	}

	public String getUserQuery() {
		return userQuery;
	}

	public void setUserQuery(String userQuery) {
		this.userQuery = userQuery;
		if (debug_) {
			LOG.debug(" : userQuery set to \'" + userQuery + "\'");
		}
	}

	public String getRoleQuery() {
		return roleQuery;
	}

	public void setRoleQuery(String roleQuery) {
		this.roleQuery = roleQuery;
		if (debug_) {
			LOG.debug(" : roleQuery set to \'" + roleQuery + "\'");
		}
	}
	
	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public Map<String, String> getSecurityOptions() {
		Map<String, String> options = new HashMap<String, String>();
		if (debug_) {
			LOG.debug(" securityOptions() : getDriverName() set to \'" + getDriverName() + "\'");
		}
		options.put(Constants.DB_DRIVER_NAME, getDriverName());
		options.put(Constants.DB_CONNECTION_URL, getDatabaseDriverUrl());
		options.put(Constants.DB_USER_NAME, getDatabaseUserName());
		options.put(Constants.DB_USER_PASSWORD, getDatabaseUserPassword());
		options.put(Constants.DB_USER_QUERY, getUserQuery());
		options.put(Constants.DB_ROLE_QUERY, getRoleQuery());
		options.put(Constants.DB_DEBUG, getDebug());
		if (debug_) {
			LOG.debug(" : -options.driverName = \'" + (String)options.get(Constants.DB_DRIVER_NAME) + "\'");
			LOG.debug(" : -options.databaseDriverUrl = \'" + (String)options.get(Constants.DB_CONNECTION_URL) + "\'");
			LOG.debug(" : -options.databaseUserName = \'" + (String)options.get(Constants.DB_USER_NAME) + "\'");
			LOG.debug(" : -options.databaseUserPassword = \'" + (String)options.get(Constants.DB_USER_PASSWORD) + "\'");
			LOG.debug(" : -options.userQuery = \'" + (String)options.get(Constants.DB_USER_QUERY) + "\'");
			LOG.debug(" : -options.roleQuery = \'" + (String)options.get(Constants.DB_ROLE_QUERY) + "\'");
			LOG.debug(" : -options.debug = \'" + (String)options.get(Constants.DB_DEBUG) + "\'");
		}
		this.securityOptions = options;

		return securityOptions;
	}

	public void setSecurityOptions(Map<String, String> securityOptions) {
		this.securityOptions = securityOptions;
	}
	
	public DatabaseLoginModuleSecurityFilter() {
		super();
	}
	
}

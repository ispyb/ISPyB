package ispyb.server.security;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;
import org.jboss.security.SimpleGroup;

/**
 * An implementation of LoginModule that authenticates against a database
 * 
 * This LoginModule authenticates users with a password.
 *
 * This LoginModule only recognizes any user who enters the required password:   Go JAAS
 *
 * If the user successfully authenticates itself, a Principal with the user name is added to the Subject.
 *
 * This LoginModule recognizes the debug option. If set to true in the login Configuration, debug messages are sent to the output stream.
 *
 * @author CHADO
 */
public class DatabaseLoginModule implements LoginModule {

	/** flag that indicates if authentication was successful */
	protected boolean succeeded = false;
	/** flag that indicates that the authentication has been committed */
	protected boolean commited = false;
	/** an implementation of a {@link Principal} that holds the name of a user */
	protected Principal user;
	/** the user name for which authentication should be done */
	protected String username;
	/** the password of the user for which authentication should be done */
	protected char[] pwd;
	
	/** the {@link Subject} for the authentication */
	protected Subject subject;
	/** the {@link CallbackHandler} used for the authentication */
	protected CallbackHandler callbackHandler;
	/** the options related to the authentication */
	protected Map<String, ?> options;
	
	// default role. If a user has a valid password and no Pxgroup assigned,
	// so it will be a User. This might be changed on the future.
	protected static final String DEFAULT_GROUP = "User";
	/** debug */
	protected static boolean debug = false;
		
	protected transient SimpleGroup userRoles = new SimpleGroup("Roles");
	
	private final Logger LOG = Logger.getLogger(DatabaseLoginModule.class);
	
	public DatabaseLoginModule() {
		super();
	}
	
	public DatabaseLoginModule(String username, String usernameUOT) {
		this.username = username;
		this.pwd = new char[usernameUOT.length()];
		for (int i = 0; i < usernameUOT.length(); i++) {
			pwd[i] = usernameUOT.charAt(i);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	/**
     * This method is called if the overall authentication of LoginContext failed.
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules did not succeed).
     *
     * If this authentication attempt of LoginModule succeeded 
     * (checked by retrieving the private state saved by the login and commit methods),
     * then this method cleans up any state that was originally saved.
     *
     * @exception LoginException if the abort fails.
     *
     * @return false if this login or commit attempt for LoginModule failed, and true otherwise.
     */
	public boolean abort() throws LoginException {
		if (!succeeded)
			return false;
		
		if (commited) {
			logout();
		} else {
			user = null;
			clean();
		}
		
		if (debug) {
			LOG.debug(".abort() Abort Succeeded \n");
		}
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	/**
     * This method is called if the overall authentication of LoginContext succeeded
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules succeeded).
     *
     * If this LoginModule authentication attempt succeeded 
     * (checked by retrieving the private state saved by the login method), 
     * then this method associates a Principal with the Subject located in the LoginModule.  
     * If this LoginModule authentication attempt failed, then this method removes any state that was originally saved.
     *
     * @exception LoginException if the commit fails.
     *
     * @return true if the login and commit LoginModule attempts succeeded, or false otherwise.
     */
	public boolean commit() throws LoginException {

		if (succeeded == false)
			return false;
		else if (subject.isReadOnly())
			throw new LoginException("Subject is Readonly");

		// Add the user roles to the Subject's principal set
		Set<Principal> principal = subject.getPrincipals();
		if (!principal.contains(userRoles))
			principal.add(userRoles);

		commited = true;
		
		if (debug) {
			LOG.debug(".commit() Commit Succeeded \n");
		}
		
		return true;
	}
	

	/*
	 * (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	/**
     * Initialize this LoginModule.
     *
     * @param subject the Subject to be authenticated. 
     *
     * @param callbackHandler a CallbackHandler for communicating with the end user (prompting for user names and passwords, for example). 
     *
     * @param sharedState shared LoginModule state. 
     *
     * @param options options specified in the login Configuration for this particular LoginModule.
     */
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.options = options;
		
		// Initialize configured options
		debug = "true".equalsIgnoreCase((String) options.get("debug"));
		
		if (debug) {
			LOG.debug(".initialize() Initialize Succeeded \n");
		}
	}
	
	/**
	 * Removes the credentials from memory in a save way
	 */
	public void clean() {
		username = null;
		
		// for extra safety we are clearing every char
		if (pwd != null) {
			for (int i = 0; i < pwd.length; i++)
				pwd[i]=' ';
			pwd = null;
		}
		if (debug) {
			LOG.debug(".clean() Clean Succeeded \n");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	/**
     * Authenticate the user by prompting for a user name and password.
     *
     *
     * @return true in all cases since this LoginModule should not be ignored.
     *
     * @exception FailedLoginException if the authentication fails.
     *
     * @exception LoginException if this LoginModule is unable to perform the authentication.
     */
	public boolean login() throws LoginException {
		
		if (callbackHandler == null)
			 throw new LoginException("Error: no CallbackHandler available to garner authentication information from the user");

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("user name: ");
		callbacks[1] = new PasswordCallback("password: ", false);
		
		try {
		    callbackHandler.handle(callbacks);
		    username = ((NameCallback)callbacks[0]).getName();
		    char[] tmpPassword = ((PasswordCallback)callbacks[1]).getPassword();
		    if (tmpPassword == null) {
				// treat a NULL password as an empty password
				tmpPassword = new char[0];
		    }
		    pwd = new char[tmpPassword.length];
		    System.arraycopy(tmpPassword, 0, pwd, 0, tmpPassword.length);
		    ((PasswordCallback)callbacks[1]).clearPassword();
		    
		    userRoles = DatabaseLoginModuleHelper.getRoleNamesForUser(options, username, new String (pwd), DEFAULT_GROUP);

		    DatabaseLoginModuleHelper.verifyCredentials(options, username, new String (pwd));
		    succeeded = true;
		    
		    if (debug) {
				LOG.debug(".login() Login Succeeded \n");
			}
		    
		} catch (java.io.IOException ioe) {
		    throw new LoginException(ioe.toString());
		} catch (UnsupportedCallbackException uce) {
		    throw new LoginException("Error: " + uce.getCallback().toString() +	" fail to garner authentication information from the user");
		}
		
		return true;
	}

   
	/*
	 * (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	/**
     * Logout the user.
     *
     * This method removes the HWPrincipal
     * that was added by the commit method.
     *
     * @exception LoginException if the logout fails.
     *
     * @return true in all cases since this LoginModule should not be ignored.
     */
	public boolean logout() throws LoginException {
		
		subject.getPrincipals().remove(user);
		succeeded = false;
		commited = false;
		user = null;
		clean();
		
		if (debug) {
			LOG.debug(".logout() Succeeded \n");
		}
		return true;
	}

}

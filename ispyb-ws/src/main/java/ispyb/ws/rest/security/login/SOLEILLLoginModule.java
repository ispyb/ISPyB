package ispyb.ws.rest.security.login;

import ispyb.common.util.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

public class SOLEILLLoginModule{
	private final static Logger logger = Logger.getLogger(SOLEILLLoginModule.class);
	
	private static String driverName = "oracle.jdbc.driver.OracleDriver";
	private static String databaseDriverUrl = "jdbc:oracle:thin:@locahost:1521:xe";
	private static String databaseUserName = "sun2";
	private static String databaseUserPassword = "sun2";
	private static String userQuery = "select PASSWORD, TITLE, FIRSTNAME, LASTNAME from USERS where STATUS = 'A' and upper(USERNAME) = '";
	private static String roleQuery = "select distinct(RT.name) as ROLE_NAME from ROLE_TYPES RT, ROLES R, USERS U where RT.ROLETID=R.TYPEID and R.USERID=U.USERID and U.USERNAME = '";
	
	private static Map<String, String> getConnectionProperties() {
		Map<String, String> properties = new HashMap<String, String>();
		if (Constants.DB_DRIVER_NAME_VALUE == null) {
			properties.put(Constants.DB_DRIVER_NAME, driverName);
		} else {
			properties.put(Constants.DB_DRIVER_NAME, Constants.DB_DRIVER_NAME_VALUE);
		}
		if (Constants.DB_CONNECTION_URL_VALUE == null) {
			properties.put(Constants.DB_CONNECTION_URL, databaseDriverUrl);
		} else {
			properties.put(Constants.DB_CONNECTION_URL, Constants.DB_CONNECTION_URL_VALUE);
		}
		if (Constants.DB_USER_NAME_VALUE == null) {
			properties.put(Constants.DB_USER_NAME, databaseUserName);
		} else {
			properties.put(Constants.DB_USER_NAME, Constants.DB_USER_NAME_VALUE);
		}
		if (Constants.DB_USER_PASSWORD_VALUE == null) {
			properties.put(Constants.DB_USER_PASSWORD, databaseUserPassword);
		} else {
			properties.put(Constants.DB_USER_PASSWORD, Constants.DB_USER_PASSWORD_VALUE);
		}
		properties.put(Constants.DB_USER_QUERY, userQuery);
		properties.put(Constants.DB_ROLE_QUERY, roleQuery);
		
		logger.info("Constants.DB_DRIVER_NAME_VALUE = " + Constants.DB_DRIVER_NAME_VALUE);
		logger.info("Constants.DB_CONNECTION_URL_VALUE = " + Constants.DB_CONNECTION_URL_VALUE);
		
		return properties;
	}
	

	public static List<String> authenticate(String username, String password) throws NamingException, LoginException, SQLException {
		return getRoleNames(getConnection(getConnectionProperties()), username, password);
	}
	
	/**
	 * Creates a connection from an Jndi datasource or an new connection is created
	 * 
	 * @param properties - is a map of settings to connect to database
	 * @return <b>Connection</b> {@link Connection}
	 * @throws NamingException
	 * @throws SQLException
	 */
	private static Connection getConnection(Map<String, ?> properties) throws NamingException, SQLException {

		String driver = (String) properties.get(Constants.DB_DRIVER_NAME);
		String url = (String) properties.get(Constants.DB_CONNECTION_URL);
		String dbUser = (String) properties.get(Constants.DB_USER_NAME);
		String dbPassword = (String) properties.get(Constants.DB_USER_PASSWORD);
		
		logger.info("DatabaseLoginModuleHelper - driver = " + driver);
		logger.info("DatabaseLoginModuleHelper - url = " + url);
		logger.info("DatabaseLoginModuleHelper - dbUser = " + dbUser);
		logger.info("DatabaseLoginModuleHelper - dbPassword = *********** ");
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return  DriverManager.getConnection(url, dbUser, dbPassword);
	}
	
	/**
	 * Retrieve user's roles
	 * 
	 * @param connection - {@link Connection}
	 * @param username - is the username of the user
	 * @param password - is the login of the user
	 * @return Array string value of user's roles
	 * @throws NamingException
	 * @throws SQLException
	 * @throws LoginException
	 */
	private static List<String> getRoleNames(Connection connection, String username, String password) throws NamingException, SQLException, LoginException {
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			if (username == null)
				throw new LoginException("Invalid username");
		
				username = password;
			String sql = roleQuery + username + "'";
			logger.info("roleQuery = " + sql);

			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			List<String> roles = new ArrayList<String>();
			while (resultSet.next()) {
				roles.add(resultSet.getString("ROLE_NAME"));
			}
			return roles;
		} catch (SQLException sqle) {
			List<String> roles = new ArrayList<String>();	
			roles.add("User");
			return roles;
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
				}
		}
	}
}


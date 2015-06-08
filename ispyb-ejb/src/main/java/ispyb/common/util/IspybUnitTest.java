/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/

package ispyb.common.util;

import java.io.File;
import java.io.FileReader;
import java.rmi.RMISecurityManager;
import java.util.Properties;

import javax.security.auth.login.LoginContext;

import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.BeforeClass;

public class IspybUnitTest {

	protected static Properties properties;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties ispybProperties = new Properties();
		ispybProperties.load(new FileReader(new File("build.properties")));
		String jbossPath = ispybProperties.getProperty("jboss.home") + "/server/default/conf/testJAAS.config";
		String jbossPolicy = ispybProperties.getProperty("jboss.home") + "/server/default/conf/standaloneClient.policy";

		System.out.println("Setting environment up. Fix path is required " + jbossPath);
		System.setProperty("java.security.auth.login.config", jbossPath);
		UsernamePasswordHandler handler = null;
		String username = "mx415";
		String password = "pimx415";
		handler = new UsernamePasswordHandler(username, password);
		LoginContext lc = new LoginContext("testEJB", handler);
		lc.login();
		System.setProperty("java.security.policy", jbossPolicy);
		System.setSecurityManager(new RMISecurityManager());
		properties = new Properties();
		properties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		properties.put("java.naming.factory.url.pkgs", "=org.jboss.naming:org.jnp.interfaces");
		properties.put("java.naming.provider.url", "localhost:1099");
	}

}

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
 ******************************************************************************************************************************/

package ispyb.server.common.util.ejb;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * <p>
 * Handles lookup of EJB3 services. By convention, all services implementations must be named as
 * &lt;servicename&gt;Bean.
 * </p>
 * 
 * @author armanet, delageni
 */
public class Ejb3ServiceLocator {

	/* CONSTANTS */

	// common prefix for all ejb jndi names
	private static final String EJB3_CONTEXT = "ispyb/";

	// suffix for remote services
	private static final String EJB3_REMOTE_SUFFIX = "/remote";

	// suffix for local services
	private static final String EJB3_LOCAL_SUFFIX = "/local";

	// suffix for all services (servicenameBean)
	private static final String EJB3_COMMON_SUFFIX = "Bean";

	// singleton instance
	private static Ejb3ServiceLocator serviceLocator;
	 
    private static final String PKG_INTERFACES = "org.jboss.ejb.client.naming";

	/* SINGLETON METHOD */

	/**
	 * @return the singleton instance of Ejb3ServiceLocator
	 */
	public static Ejb3ServiceLocator getInstance() {
		if (serviceLocator == null) {
			serviceLocator = new Ejb3ServiceLocator();
		}
		return serviceLocator;
	}

	/* INSTANCE VARIABLES */

	private Ejb3ServiceLocator() {
	}

	/* PUBLIC METHODS */

	/**
	 * Returns the local service from the service class.
	 * 
	 * @param serviceClass
	 *            the service interface class.
	 * @return the service object.
	 * @throws NamingException
	 */
	public Object getLocalService(Class serviceClass) throws NamingException {
		String serviceJndiNameOld = EJB3_CONTEXT + serviceClass.getSimpleName() + EJB3_COMMON_SUFFIX + EJB3_LOCAL_SUFFIX;
		
		String classname = serviceClass.getSimpleName();
				
        final String moduleName = "ispyb-ejb3";
//        final String moduleName = "ispyb";
        final String distinctName = "";
               
        // The EJB name which by default is the simple class name of the bean implementation class     

        final String beanName = classname + EJB3_COMMON_SUFFIX;
        
        // the view fully qualified class name
        final String viewClassName = serviceClass.getName()   ;//+ "Local";
              
        // let's do the lookup        
        String serviceJndiName = "java:app/" + moduleName + "/" + beanName + "!" + viewClassName;       
//        String serviceJndiName = "java:app/" + beanName + "!" + viewClassName;
        		
		return getObjectRef(serviceJndiName, null);
	}

	/**
	 * Returns the remote service from the service class.
	 * 
	 * @param serviceClass
	 *            the service interface class.
	 * @return the service object.
	 * @throws NamingException
	 */
	public Object getRemoteService(Class serviceClass) throws NamingException {
		//String serviceJndiNameOld = EJB3_CONTEXT + serviceClass.getSimpleName() + EJB3_COMMON_SUFFIX + EJB3_REMOTE_SUFFIX;
		//Object ref = getObjectRef(serviceJndiNameOld, null);
		//Object realized = PortableRemoteObject.narrow(ref, serviceClass);
					
			String classname = serviceClass.getSimpleName();
						
			final String appName = "ispyb";
	        final String moduleName = "ispyb-ejb3";
//	        final String moduleName = "ispyb";
	        final String distinctName = "";
	        	        
	        // The EJB name which by default is the simple class name of the bean implementation class	        
	        final String beanName = classname + EJB3_COMMON_SUFFIX;
	        
	        // the view fully qualified class name
	        final String viewClassName = serviceClass.getName() ;
	        
	        // let's do the lookup
	        String serviceJndiName = "ejb:"+ appName + "/" + moduleName + "/" + beanName + "!" + viewClassName;
	        
	        		
			return getObjectRef(serviceJndiName, null);
		//}
		//return realized;
	}

	public Object getRemoteService(Class serviceClass, Properties props) throws NamingException {
		String serviceJndiName = EJB3_CONTEXT + serviceClass.getSimpleName() + EJB3_COMMON_SUFFIX + EJB3_REMOTE_SUFFIX;
		Object ref = getObjectRef(serviceJndiName, props);
		Object realized = PortableRemoteObject.narrow(ref, serviceClass);
		return realized;
	}

	/* PRIVATE METHODS */

	private Object getObjectRef(String serviceJndiName, Properties properties) throws NamingException {
		Context initialCtx;
		if (properties == null) {
			properties = new Properties();
			properties.put(Context.URL_PKG_PREFIXES, PKG_INTERFACES);
			properties.put("jboss.naming.client.ejb.context", true);
		} else {
			properties.put(Context.URL_PKG_PREFIXES, PKG_INTERFACES);
		}
		initialCtx = new InitialContext(properties);
		
		Object objref = initialCtx.lookup(serviceJndiName);
		return objref;
	}


}
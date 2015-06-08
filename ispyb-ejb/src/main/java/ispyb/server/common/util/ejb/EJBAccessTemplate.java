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

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import java.nio.file.AccessDeniedException;
import org.apache.log4j.Logger;

/**
 * <p>
 * Defines an EJB calls template, used when multiple calls are generating the same EJB exceptions.
 * </p>
 * <p>
 * The handled exceptions are:
 * <ul>
 * <li>AccessDeniedException. Rollback done automatically</li>
 * <li>VOValidateException. No rollback</li>
 * <li>NamingException. Rollback done automatically</li>
 * <li>RemoteException. Rollback done automatically</li>
 * <li>CreateException. Rollback done automatically</li>
 * <li>RemoveException. Rollback done automatically</li>
 * </ul>
 * </p>
 * 
 * @author armanet
 */
public class EJBAccessTemplate {

	/*
	 * Constants
	 */

	public static final String ADE_LOG_MESSAGE = "Access denied for user";

	public static final String VOVE_LOG_MESSAGE = "Could not validate value object";

	public static final String NE_LOG_MESSAGE = "Could not look up one of the needed beans";

	public static final String FE_LOG_MESSAGE = "Could not look up one of the needed beans";

	public static final String CE_LOG_MESSAGE = "Could not create one of the needed beans";

	public static final String REMOTE_LOG_MESSAGE = "Could not access to the bean";

	public static final String RE_LOG_MESSAGE = "Could not remove the bean";

	public static final String ANY_LOG_MESSAGE = "Exception unhandled by EJBAccessTemplate";

	/*
	 * INSTANCE VARIABLES
	 */

	// the caller logger
	private final Logger logger;

	// the caller ejb session context
	private final SessionContext context;

	// the caller
	private final Object parent;

	/**
	 * Builds a new template instance.
	 * 
	 * @param logger
	 *            the caller logger. Must be not null (needed for exception logging).
	 * @param context
	 *            the EJB caller context.
	 * @param parent
	 *            the EJB caller.
	 */
	public EJBAccessTemplate(Logger logger, SessionContext context, Object parent) {
		super();
		this.logger = logger;
		this.context = context;
		this.parent = parent;
	}

	/**
	 * Executes the given EJB calls given within a {@link EJBAccessCallback} and catch common EJB exceptions.
	 * 
	 * @param accessCallback
	 * @throws Exception
	 */
	public Object execute(EJBAccessCallback accessCallback) throws Exception {
		try {
			return accessCallback.doInEJBAccess(this.parent);
		} catch (AccessDeniedException e) {
			logger.warn(ADE_LOG_MESSAGE, e);
			this.context.setRollbackOnly();
			throw e;
		} catch (NamingException e) {
			logger.error(NE_LOG_MESSAGE, e);
			this.context.setRollbackOnly();
			throw e;
		} catch (RemoteException e) {
			logger.error(NE_LOG_MESSAGE, e);
			this.context.setRollbackOnly();
			throw e;
		} catch (CreateException e) {
			logger.error(CE_LOG_MESSAGE, e);
			this.context.setRollbackOnly();
			throw e;
		} catch (RemoveException e) {
			logger.error(RE_LOG_MESSAGE, e);
			this.context.setRollbackOnly();
			throw e;
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug(ANY_LOG_MESSAGE, e);
			}
			this.context.setRollbackOnly();
			throw e;
		}
	}

}

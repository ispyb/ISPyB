/*************************************************************************************************
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
 ****************************************************************************************************/

package ispyb.server.common.util.ejb;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;
import java.sql.Types;

/**
 * Represents an order imposed upon a Criteria result set. It allows to cast a string as a decimal
 * @author BODIN
 *
 */
public class CastDecimalOrder extends Order {

	private String propertyName;
	private boolean ascending ;
	private boolean ignoreCase;
	
	private static final long serialVersionUID = -3452675050833542304L;

	public CastDecimalOrder(String propertyName, boolean ascending) {
		super(propertyName, ascending);
		this.propertyName = propertyName ;
		this.ascending = ascending ;
		this.ignoreCase = false;
	}

	
	/**
	 * render the SQL fragment 'CAST(name AS DECIMAL)'
	 */
	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	throws HibernateException {
		String [] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
		Type type = criteriaQuery.getTypeUsingProjection(criteria, propertyName);
		StringBuffer  fragment = new StringBuffer ();
		fragment.append("CAST(");
		for ( int i=0; i<columns.length; i++ ) {
			SessionFactoryImplementor factory = criteriaQuery.getFactory();
			boolean lower = ignoreCase && type.sqlTypes( factory )[i]==Types.VARCHAR;
			if (lower) {
				fragment.append( factory.getDialect().getLowercaseFunction() )
				.append('(');
			}
			fragment.append( columns[i] );
			if (lower) fragment.append(')');
			fragment.append(" AS DECIMAL)");
			//fragment.append( ascending ? " asc" : " desc" );
			if ( i<columns.length-1 ) fragment.append(", ");
		}
		return fragment.toString();
	}

}

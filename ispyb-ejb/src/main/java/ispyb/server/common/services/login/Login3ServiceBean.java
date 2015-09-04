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

package ispyb.server.common.services.login;


import ispyb.server.common.vos.login.Login3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;



@Stateless
public class Login3ServiceBean implements Login3Service, Login3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Override
	public void persist(Login3VO transientInstance) {
		try {
			entityManager.persist(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public Login3VO findByToken(String token) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Login3VO.class);
		criteria.add(Restrictions.eq("token", token));
		List<Login3VO> loginList = criteria.list();
		if (loginList.size() == 1){
			return loginList.get(0);
		}
		/** If loginList size is different to 1 means that either there is no token on the database or that there are several and in both cases it is not valid **/
		return null;
	}
	
	@Override
	public Login3VO findBylastByLogin(String login) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Login3VO.class);
		criteria.add(Restrictions.eq("login", login));
		criteria.addOrder(Order.desc("loginId"));
		List<Login3VO> loginList = criteria.list();
		if (loginList != null && loginList.size() > 0){
			return loginList.get(0);
		}
		/** If loginList size is different to 1 means that either there is no token on the database or that there are several and in both cases it is not valid **/
		return null;
	}

}
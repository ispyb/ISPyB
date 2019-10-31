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
package ispyb.server.common.services.shipping;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.ContainerHistory3VO;

/**
 * <p>
 * This session bean handles ISPyB ContainerHistory3.
 * </p>
 */
@Stateless
public class ContainerHistory3ServiceBean implements ContainerHistory3Service,
		ContainerHistory3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(ContainerHistory3ServiceBean.class);
	
	private static final String FIND_BY_PK() {
		return "from ContainerHistory3VO vo where vo.containerHistoryId = :pk";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public ContainerHistory3ServiceBean() {
	};

	/**
	 * Create new ContainerHistory3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public ContainerHistory3VO create(final ContainerHistory3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}
	
	public ContainerHistory3VO create(final  Container3VO container, final String location, final String status) throws Exception {
		
		checkCreateChangeRemoveAccess();
		ContainerHistory3VO vo = new ContainerHistory3VO(container, location, status);
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the ContainerHistory3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public ContainerHistory3VO update(final ContainerHistory3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the ContainerHistory3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
				
		checkCreateChangeRemoveAccess();
		ContainerHistory3VO vo = findByPk(pk);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the ContainerHistory3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ContainerHistory3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the ContainerHistory3 value object
	 */
	public ContainerHistory3VO findByPk(final Integer pk)
			throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (ContainerHistory3VO) entityManager.createQuery(FIND_BY_PK())
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	/**
	 * Check if user has access rights to create, change and remove ContainerHistory3 entities. If not set rollback
	 * only and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContainerHistory3VO> findByContainerId(final Integer containerId)
			throws Exception {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(ContainerHistory3VO.class);
		
		if (containerId != null){
			criteria.createCriteria("containerVO").add(Restrictions.eq("containerId", containerId));
		}
		
		return criteria.list();
	}
	
	
	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(ContainerHistory3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getContainerHistoryId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getContainerHistoryId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}
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

import ispyb.server.common.daos.shipping.Container3DAO;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.daos.sample.DiffractionPlan3DAO;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB Container3.
 * </p>
 */
@Stateless
public class Container3ServiceBean implements Container3Service, Container3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Container3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@EJB
	private Container3DAO dao;
	
	@EJB
	private Crystal3Service crystal3Service;

	@Resource
	private SessionContext context;

	public Container3ServiceBean() {
	};

	/**
	 * Create new Container3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Container3VO create(final Container3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Container3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Container3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Container3VO update(final Container3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Container3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Container3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Container3VO vo = findByPk(pk, false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Container3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Container3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.delete(vo);
				return vo;
			}

		});
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Container3 value object
	 */
	public Container3VO findByPk(final Integer pk, final boolean fetchSamples) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Container3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Container3VO found = dao.findByPk(pk, fetchSamples);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Container3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Container3VO> findAll() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Container3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Container3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<Container3VO> findByDewarId(final Integer dewarId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Container3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Container3VO> foundEntities = dao.findFiltered(null, dewarId, null, null);
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<Container3VO> findByProposalIdAndStatus(final Integer proposalId, final String containerStatusProcess)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Container3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Container3VO> foundEntities = dao.findFiltered(proposalId, null, containerStatusProcess, null);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove Container3 entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Container3VO> findByBarCode(final Integer dewarId, final String code)throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Container3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Container3VO> foundEntities = dao.findFiltered(null, dewarId, null, code);
				return foundEntities;
			}

		});
	}

	@Override
	public Container3VO savePuck(Container3VO container) throws Exception {
		Container3VO containerDB = this.findByPk(container.getContainerId(), true);
		
		/** Removing all samples **/
		/** Never do this, it removes the data collections **/
		/*for (BLSample3VO sample : containerDB.getSampleVOs()) {
			entityManager.remove(sample);
		}*/
		
		containerDB.setCapacity(container.getCapacity());
		containerDB.setCode(container.getCode());
		
		Set<String> locations = new HashSet<String>();
		/** Adding Sample **/
		for (BLSample3VO sample : container.getSampleVOs()) {
			System.out.println("-------------------------");
			System.out.println("Storing sample: " + sample.getCode() + " " + sample.getName());
			System.out.println("sampleId: " + sample.getBlSampleId());
			System.out.println("location: " + sample.getLocation());
			locations.add(sample.getLocation());
			/** We create a new sample **/
			//sample.setBlSampleId(null);
			
			System.out.println("\t\t\t Creating new diffraction plan ");
			DiffractionPlan3VO diff = sample.getDiffractionPlanVO();
			System.out.println("Diffraction: " + diff.toString());
			//diff.setDiffractionPlanId(null);
			diff = entityManager.merge(diff);
			
			Crystal3VO crystal = sample.getCrystalVO();
			
			Crystal3VO searchCrystal = crystal3Service.findByAcronymAndCellParam(sample.getCrystalVO().getProteinVO().getAcronym(), crystal, null); 
			if (searchCrystal != null ){
				/** Crystal for this acronym and cell unit parameters already exist **/
				System.out.println("Crystal found");
				sample.setCrystalVO(searchCrystal);
//				searchCrystal.getSampleVOs().add(sample);
			}
			else{
				/** Crystal not found then we create a new one **/
				crystal.setCrystalId(null);
				Protein3VO protein = entityManager.find(Protein3VO.class, sample.getCrystalVO().getProteinVO().getProteinId());
				crystal.setProteinVO(protein);
				crystal = entityManager.merge(crystal);
				sample.setCrystalVO(crystal);
			}
			
			sample.setDiffractionPlanVO(diff);

//			sample.setBlSampleId(null);
			sample.setContainerVO(containerDB);
			sample = entityManager.merge(sample);
		}
		/** Retrieving container **/
		containerDB = this.findByPk(container.getContainerId(), true);
		
		System.out.println("sample locations: " + locations.toString());
		List<BLSample3VO> toBeRemoved = new ArrayList<BLSample3VO>();
		
		/** Locations of potentially removed samples **/
		Set<String> totalLocations = new HashSet<String>();
		for (BLSample3VO sample : containerDB.getSampleVOs()) {
			if (!locations.contains(sample.getLocation())){
				toBeRemoved.add(sample);
			}
			totalLocations.add(sample.getLocation());
		}
		System.out.println("total locations: " + totalLocations.toString());
		
		System.out.println("to be removed: " + toBeRemoved.size());
		for (BLSample3VO sample : toBeRemoved) {
			System.out.println("Removing" + sample.getLocation());
			entityManager.remove(sample);
		}
		
		return this.findByPk(container.getContainerId(), true);
	}

}
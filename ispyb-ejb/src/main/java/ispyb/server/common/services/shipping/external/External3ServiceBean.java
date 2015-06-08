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
package ispyb.server.common.services.shipping.external;

import ispyb.server.biosaxs.services.SQLQueryKeeper;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSubSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

/**
 * <p>
 * This session bean handles ISPyB Shipping3.
 * </p>
 */
@Stateless
public class External3ServiceBean implements External3Service, External3ServiceLocal {


	
	 
	 private final static Logger LOG = Logger.getLogger(External3ServiceBean.class);

	 @PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@EJB
	private Proposal3Service proposal3service;

	@EJB
	private Protein3Service protein3Service;

	@Override
	public Shipping3VO storeShipping(String proposalCode, String proposalNumber, Shipping3VO shipping) throws Exception {
		List<Proposal3VO> proposals = proposal3service.findByCodeAndNumber(proposalCode, proposalNumber, false, false, false);
		if (proposals != null) {
			if (proposals.size() > 0) {
				Proposal3VO proposal = proposals.get(0);
				Set<Dewar3VO> dewars = shipping.getDewarVOs();
				Shipping3VO shipping3VO = this.createShipping(shipping, proposal);
				/** Creating dewars **/
				for (Dewar3VO dewar3vo : dewars) {
					Container3VO[] containers = dewar3vo.getContainers();
					dewar3vo = this.createDewar(dewar3vo, shipping3VO);
					for (Container3VO container3vo : containers) {
						Set<BLSample3VO> samples = container3vo.getSampleVOs();
						container3vo.setSampleVOs(new HashSet<BLSample3VO>());
						container3vo.setDewarVO(dewar3vo);
						container3vo = this.entityManager.merge(container3vo);
						for (BLSample3VO blSample3VO : samples) {
							/** Creating crystals **/
							if (blSample3VO.getCrystalVO() != null) {
								Crystal3VO crystal3VO = this.createCrystal(blSample3VO, proposals.get(0));
								blSample3VO.setCrystalVO(crystal3VO);
								blSample3VO.setContainerVO(container3vo);
							}
							/** Creating Diffraction Pla **/
							if (blSample3VO.getDiffractionPlanVO() != null) {
								DiffractionPlan3VO plan = this.createDiffractionPlan(blSample3VO.getDiffractionPlanVO());
								blSample3VO.setDiffractionPlanVO(plan);
							}

							Set<BLSubSample3VO> subSamples = blSample3VO.getBlSubSampleVOs();
							blSample3VO.setBlSubSampleVOs(new HashSet<BLSubSample3VO>());
							blSample3VO = this.entityManager.merge(blSample3VO);

							/** Creating the subsamples **/
							if (subSamples != null) {
								for (BLSubSample3VO blSubSample3VO : subSamples) {
									blSubSample3VO.setBlSampleVO(blSample3VO);
									this.entityManager.merge(blSubSample3VO);
								}
							}

						}
					}
				}
				return shipping3VO;
			}
		} else {
			throw new Exception("- Proposal not found " + proposalCode + proposalNumber);
		}
		return null;
	}

	private DiffractionPlan3VO createDiffractionPlan(DiffractionPlan3VO plan) {
		return this.entityManager.merge(plan);
	}

	private Crystal3VO createCrystal(BLSample3VO blSample3VO, Proposal3VO proposal) throws Exception {
		Crystal3VO crystal3VO = blSample3VO.getCrystalVO();
		if (crystal3VO.getProteinVO() != null) {
			Protein3VO aux = crystal3VO.getProteinVO();
			List<Protein3VO> proteins = protein3Service.findByAcronymAndProposalId(proposal.getProposalId(), aux.getAcronym());
			if (proteins != null) {
				if (proteins.size() > 0) {
					crystal3VO.setProteinVO(proteins.get(0));
					if (crystal3VO.getDiffractionPlanVO() != null) {
						crystal3VO.setDiffractionPlanVO(this.createDiffractionPlan(crystal3VO.getDiffractionPlanVO()));
					}
					return this.entityManager.merge(crystal3VO);
				} else {
					System.out.println("No protein found: " + aux.getAcronym());
				}
			}
		} else {
			System.out.println("Error retrieving protein list");
		}
		return null;
	}

	private Dewar3VO createDewar(Dewar3VO dewar3vo, Shipping3VO shipping3vo) {
		dewar3vo.setContainerVOs(new HashSet<Container3VO>());
		dewar3vo.setShippingVO(shipping3vo);
		return this.entityManager.merge(dewar3vo);
	}

	private Shipping3VO createShipping(Shipping3VO shipping, Proposal3VO proposalVO) {
		shipping.setDewarVOs(new HashSet<Dewar3VO>());
		shipping.setProposalVO(proposalVO);
		shipping.setCreationDate(GregorianCalendar.getInstance().getTime());
		return this.entityManager.merge(shipping);
	}

	@Override
	public List<Map<String, Object>> getDataCollectionByProposal(String proposalCode, String proposalNumber) {
		String mySQLQuery = SQLQueryKeeper.getDataCollection();
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalCode", proposalCode);
		query.setParameter("proposalNumber", proposalNumber);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}

	/**
	 * Return session by proposal code and number
	 * @param code MX, opd, SAXS
	 * @param number 
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSessionsByProposalCodeAndNumber(String proposalCode, String proposalNumber){
		String mySQLQuery = SQLQueryKeeper.getSessionByCodeAndNumber();
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalCode", proposalCode);
		query.setParameter("proposalNumber", proposalNumber);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
		
	}
	
	private List<Map<String, Object>> executeSQLQuery(SQLQuery query){
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getDataCollectionFromShippingId(int shippingId){
		String mySQLQuery = SQLQueryKeeper.getDataCollectionFromShippingId();
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("shippingId", shippingId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
		
	}
	
	@Override
	public List<Map<String, Object>> getAllDataCollectionFromShippingId(int shippingId){
		String mySQLQuery = SQLQueryKeeper.getAllDataCollectionFromShippingId();
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("shippingId", shippingId);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
		
	}
	
}
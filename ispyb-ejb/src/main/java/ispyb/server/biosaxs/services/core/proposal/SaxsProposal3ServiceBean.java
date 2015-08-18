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

package ispyb.server.biosaxs.services.core.proposal;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.biosaxs.services.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.assembly.Assembly3VO;
import ispyb.server.biosaxs.vos.assembly.AssemblyHasMacromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class SaxsProposal3ServiceBean implements SaxsProposal3Service, SaxsProposal3ServiceLocal {
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Override
	public List<Macromolecule3VO> findMacromoleculesByProposalId(int proposalId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Macromolecule3VO> criteria = builder.createQuery(Macromolecule3VO.class);
		Root<Macromolecule3VO> projectRoot = criteria.from(Macromolecule3VO.class);
		criteria.select(projectRoot);
		criteria.where(builder.equal(projectRoot.get("proposalId"), proposalId));
		return entityManager.createQuery(criteria).getResultList();
	}
	
	@Override
	public Macromolecule3VO findMacromoleculesById(Integer macromoleculeId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Macromolecule3VO> criteria = builder.createQuery(Macromolecule3VO.class);
		Root<Macromolecule3VO> projectRoot = criteria.from(Macromolecule3VO.class);
		criteria.select(projectRoot);
		criteria.where(builder.equal(projectRoot.get("macromoleculeId"), macromoleculeId));
		return entityManager.createQuery(criteria).getSingleResult();
	}
	

	@Override
	public List<Macromolecule3VO> findMacromoleculesBy(String acronym, int proposalId) {
		List<Macromolecule3VO> result = new ArrayList<Macromolecule3VO>();
		List<Macromolecule3VO> macromolecules = this.findMacromoleculesByProposalId(proposalId);
		for (Macromolecule3VO macromolecule3vo : macromolecules) {
			if (macromolecule3vo.getAcronym().equals(acronym)) {
				result.add(macromolecule3vo);
			}
		}
		return result;
	}

	@Override
	public List<Buffer3VO> findBuffersByProposalId(int proposalId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Buffer3VO> criteria = builder.createQuery(Buffer3VO.class);
		Root<Buffer3VO> projectRoot = criteria.from(Buffer3VO.class);
		criteria.select(projectRoot);
		criteria.where(builder.equal(projectRoot.get("proposalId"), proposalId));
		return entityManager.createQuery(criteria).getResultList();
	}

	@Override
	public Macromolecule3VO merge(Macromolecule3VO macromolecule3vo) {
		return entityManager.merge(macromolecule3vo);
	}

	@Override
	public Buffer3VO merge(Buffer3VO buffer3vo) {
		return entityManager.merge(buffer3vo);
	}

	@Override
	public StockSolution3VO merge(StockSolution3VO stockSolution3VO) {
		return this.entityManager.merge(stockSolution3VO);
	}

	@Override
	public List<StockSolution3VO> findStockSolutionsByProposalId(Integer proposalId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StockSolution3VO> criteria = builder.createQuery(StockSolution3VO.class);
		Root<StockSolution3VO> projectRoot = criteria.from(StockSolution3VO.class);
		criteria.select(projectRoot);
		criteria.where(builder.equal(projectRoot.get("proposalId"), proposalId));
		return entityManager.createQuery(criteria).getResultList();
	}

	@Override
	public void createAssembly(int macromoleculeId, List<Integer> macromolecules) {
		Assembly3VO assembly = new Assembly3VO();
		assembly.setMacromoleculeId(macromoleculeId);
		assembly.setCreationDate(GregorianCalendar.getInstance().getTime());
		assembly = this.entityManager.merge(assembly);

		for (Integer macromolId : macromolecules) {
			AssemblyHasMacromolecule3VO assemblyHasMacromolecule3VO = new AssemblyHasMacromolecule3VO();
			assemblyHasMacromolecule3VO.setAssemblyId(assembly.getAssemblyId());
			assemblyHasMacromolecule3VO.setMacromolecule3VO(this.entityManager.find(Macromolecule3VO.class, macromolId));
			this.entityManager.merge(assemblyHasMacromolecule3VO);
		}
	}

	@Override
	public List<Assembly3VO> findAssembliesByProposalId(Integer proposalId) {
		StringBuilder ejbQLQuery = new StringBuilder("SELECT DISTINCT(assembly) FROM Assembly3VO assembly ");
		ejbQLQuery.append("LEFT JOIN FETCH assembly.assemblyHasMacromolecules3VOs m ");
		ejbQLQuery.append("LEFT JOIN FETCH m.macromolecule3VO ");
		ejbQLQuery.append("WHERE m.macromolecule3VO.proposalId = :proposalId ");
		TypedQuery<Assembly3VO> query = entityManager.createQuery(ejbQLQuery.toString(), Assembly3VO.class).setParameter("proposalId",
				proposalId);
		return query.getResultList();
	}

	@Override
	public List<Assembly3VO> test(String queryString) {
		TypedQuery<Assembly3VO> query = entityManager.createQuery(queryString, Assembly3VO.class);
		return query.getResultList();
	}

	@Override
	public StockSolution3VO findStockSolutionById(int stockSolutionId) {
		return entityManager.find(StockSolution3VO.class, stockSolutionId);

	}

	@Override
	public void remove(StockSolution3VO stockSolution3VO) {
		this.entityManager.remove(this.findStockSolutionById(stockSolution3VO.getStockSolutionId()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StockSolution3VO> findStockSolutionsByBoxId(String dewarId) {
		String query = SQLQueryKeeper.getStockSolutionsByBoxId(dewarId);
		Query EJBQuery = this.entityManager.createQuery(query);
		return EJBQuery.getResultList();

	}

	@Override
	public Proposal3VO findProposalById(int proposalId) {
		return entityManager.find(Proposal3VO.class, proposalId);
	}
	
	@Override
	public List<Proposal3VO> findProposalByLoginName(String loginName, String site) {

		String userName = loginName;
		if (site != null){
			if (site.equals(Constants.SITE_ESRF) || site.equals(Constants.SITE_MAXIV)) {
				/**
				 * Contains a number then we assume is a proposal and for ESRF if the login name is "IFX xxx" then the username should be
				 * "fx xxx"
				 */
				if (loginName.matches("(.*)[0-9](.*)")) {
					ArrayList<String> authentitionInfo;
					authentitionInfo = StringUtils.GetProposalNumberAndCode(loginName);
					userName = authentitionInfo.get(0) + authentitionInfo.get(2);
				}
			}
		}

		List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
		/**
		 * If user is a proposal it is linked by proposalCode and proposalNumber in the proposal table
		 */
		proposals.addAll(this.findProposalByCodeAndNumber(userName));

		/**
		 * In case login name is a user we look for it on Persons though proposalHasPerson
		 */
		proposals.addAll(this.findProposalByPerson(loginName));

		/**
		 * Removing duplicated proposals
		 */
		List<Proposal3VO> result = new ArrayList<Proposal3VO>();
		HashSet<Integer> proposalsId = new HashSet<Integer>();
		for (Proposal3VO proposal : proposals) {
			if (!proposalsId.contains(proposal.getProposalId())) {
				result.add(proposal);
				proposalsId.add(proposal.getProposalId());
			}
		}
		
		return result;
	

	}

	@SuppressWarnings("unchecked")
	private List<Proposal3VO> findProposalByCodeAndNumber(String loginName) {
		String query = "SELECT proposal FROM Proposal3VO proposal WHERE concat(proposalCode, proposalNumber)=:loginName";
		Query EJBQuery = this.entityManager.createQuery(query);
		EJBQuery.setParameter("loginName", loginName);
		return EJBQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Proposal3VO> findProposalByPerson(String loginName) {
		String queryPerson = "SELECT person FROM Person3VO person WHERE login=:loginName";
		Query EJBQueryPerson = this.entityManager.createQuery(queryPerson);
		EJBQueryPerson.setParameter("loginName", loginName);
		@SuppressWarnings("unchecked")
		List<Person3VO> persons = EJBQueryPerson.getResultList();
		List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
		if (persons != null) {
			if (persons.size() > 0) {
				for (Person3VO person3vo : persons) {
					if (person3vo.getProposalVOs() != null) {
						if (person3vo.getProposalVOs().size() > 0) {
							proposals.addAll(person3vo.getProposalVOs());
						}
						if (person3vo.getProposalDirectVOs().size() > 0) {
							proposals.addAll(person3vo.getProposalDirectVOs());
						}
					}
				}
			}
		}
		return proposals;
	}

	/**
	 * It looks for proposal based on the login name It looks for proposal in: - Proposal table concatenating proposalCode and
	 * proposalNumber - Person table y the column login Then both systems, by proposal and by user are allowed
	 */
	public List<Proposal3VO> findProposalByLoginName(String loginName) {
		return this.findProposalByLoginName(loginName, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Proposal3VO> findAllProposals() {
		String query = "SELECT proposal FROM Proposal3VO proposal";
		Query EJBQuery = this.entityManager.createQuery(query);
		return EJBQuery.getResultList();
	}

	
}

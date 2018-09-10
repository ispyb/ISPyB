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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.assembly.Assembly3VO;
import ispyb.server.biosaxs.vos.assembly.AssemblyHasMacromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Additive3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;

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
/*
	@Override
	public List<Additive3VO> findAdditivesByProposalId(int proposalId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Additive3VO> criteria = builder.createQuery(Additive3VO.class);
		Root<Additive3VO> projectRoot = criteria.from(Additive3VO.class);
		criteria.select(projectRoot);
		criteria.where(builder.equal(projectRoot.get("proposalId"), proposalId));
		return entityManager.createQuery(criteria).getResultList();
	}
*/
	@Override
	public List<Additive3VO> findAdditivesByBufferId(int bufferId) {
		StringBuilder ejbQLQuery = new StringBuilder("SELECT DISTINCT(additive) FROM Additive3VO additive ");
		ejbQLQuery.append("LEFT JOIN FETCH additive.bufferhasadditive3VOs b ");
		ejbQLQuery.append("LEFT JOIN FETCH b.buffer3VO ");
		ejbQLQuery.append("WHERE b.buffer3VO.bufferId = :bufferId ");
		TypedQuery<Additive3VO> query = entityManager.createQuery(ejbQLQuery.toString(), Additive3VO.class).setParameter("bufferId",
				bufferId);
		return query.getResultList();
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
	public Additive3VO merge(Additive3VO additive3vo) {
		return entityManager.merge(additive3vo);
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
	
}

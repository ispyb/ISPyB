/*******************************************************************************
 * Copyright (c) 2004-2015
 * Contributors: L. Armanet, C. Borges, M. Camerlenghi, L. Cardonne,
 *               S. Delageniere, L. Duparchy, A. Madeira, S. Ohlsson,
 *               P. Pascal, N. Salgueiro, I. Schneider, S.Schulze,
 *               I. Tocquet, F. Torres
 * 
 * This file is part of the ESRF User Portal.
 * 
 * The ESRF User Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The ESRF User Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ESRF User Portal. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package ispyb.server.common.services.robot;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.robot.RobotAction3VO;

@Stateless
public class RobotAction3ServiceBean implements RobotAction3Service, RobotAction3ServiceLocal {

	private static final String FIND_BY_PK() {
		return "from RobotAction3VO vo where vo.robotActionId = :pk";
	}

	private static String FIND_BY_SESSION() { 
		return "from RobotAction3VO vo where vo.sessionId = :sessionId order by vo.startTime desc";
	}

	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	public RobotAction3ServiceBean() {
	}

	@Override
	public RobotAction3VO merge(RobotAction3VO detachedInstance) {
		try {
			RobotAction3VO result = entityManager.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void deleteByPk(Integer pk) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(RobotAction3VO vo) throws Exception {
		entityManager.remove(vo);
		
	}

	@Override
	public RobotAction3VO findByPk(Integer pk) throws Exception {
		try {
			return (RobotAction3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RobotAction3VO> findBySession(Integer sessionId) throws Exception {	
		try {
			List<RobotAction3VO> listVOs = this.entityManager.createQuery(FIND_BY_SESSION())
			.setParameter("sessionId", sessionId).getResultList();
			return listVOs;
		} catch (NoResultException e) {
			return null;
		}
	}

}

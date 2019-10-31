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

import javax.ejb.Remote;

import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.ContainerHistory3VO;

@Remote
public interface ContainerHistory3Service {

	/**
	 * Create new Container3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public ContainerHistory3VO create(final ContainerHistory3VO vo) throws Exception;
	
	public ContainerHistory3VO create(final  Container3VO container, final String location, final String status) throws Exception;


	/**
	 * Update the Container3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public ContainerHistory3VO update(final ContainerHistory3VO vo) throws Exception;

	/**
	 * Remove the Container3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the Container3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ContainerHistory3VO vo) throws Exception;

	/**
	 * Finds a Container3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param fetchSamples
	 * @return the value object
	 */
	public ContainerHistory3VO findByPk(final Integer pk) throws Exception;

	public List<ContainerHistory3VO> findByContainerId(final Integer containerId) throws Exception;
	
	//public List<ContainerHistory3VO> findByProposalIdAndStatus(final Integer proposalId, final String containerStatus)
	//		throws Exception;
	
	//public List<ContainerHistory3VO> findByLocation(final String location)throws Exception;

	
}

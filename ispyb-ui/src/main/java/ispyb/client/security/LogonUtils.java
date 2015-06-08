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
 ******************************************************************************/
package ispyb.client.security;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class LogonUtils {

	private final static Logger LOG = Logger.getLogger(LogonUtils.class);

	public static String getProposalType(String userName) {
		ArrayList authenticationInfo = StringUtils.GetProposalNumberAndCode(userName);
		String code = (String) authenticationInfo.get(0);
		String proposalNumber = (String) authenticationInfo.get(2);
		String type = null;
		Integer number = 0;
		try {
			number = new Integer(proposalNumber);
		} catch (NumberFormatException e) {
			if (!code.equals(Constants.LOGIN_PREFIX_OPID)){
				LOG.info("the user logged is NOT a proposal");
				return Constants.PROPOSAL_MX;
			}
		}
		try {
			Proposal3Service proposalService = (Proposal3Service) Ejb3ServiceLocator.getInstance().getLocalService(
					Proposal3Service.class);
			List<Proposal3VO> list = proposalService.findByCodeAndNumber(code, proposalNumber, false, false, false);
			if (!list.isEmpty())
				type = list.get(0).getType();
			return type;

		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
			return null;
		}
	}
}

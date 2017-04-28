package ispyb.ws.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Path;

import ispyb.server.common.vos.login.Login3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.ws.ParentWebService;

@Path("/")
public class RestWebService extends ParentWebService {
	protected long now;
		
	/**
	 * Check if the logged user has the right to see the given proposal data
	 * this is complementary to the SecurityInterceptor
	 * @param token
	 * @param proposalId
	 * @return
	 * @throws NamingException
	 */
//	protected boolean isProposalAllowedforToken (String token, int proposalId  ) throws NamingException {
//		
//		if ( token == null) 
//			return false;
//		
//		Login3VO login3VO;
//			login3VO = this.getLogin3Service().findByToken(token);
//			List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
//			if (login3VO != null){
//				if (login3VO.isValid()){
//					if (login3VO.isLocalContact() || login3VO.isManager() ){
//						// later add a test for localcontact to give access only to sessions attached to their beamlines
//						return true;				
//					}
//					if (login3VO.isUser()) {
//						proposals = this.getProposal3Service().findProposalByLoginName(login3VO.getUsername());
//						if (proposals == null || proposals.size()<1) 
//							return false;
//						for (Iterator<Proposal3VO> iterator = proposals.iterator(); iterator.hasNext();) {
//							Proposal3VO proposal3vo = (Proposal3VO) iterator.next();
//							if (proposal3vo.getProposalId().intValue() == proposalId)
//								return true;
//						}
//					}
//				}
//			}
//				
//		return false;		
//	}

	

}
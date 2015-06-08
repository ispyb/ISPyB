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
package ispyb.server.mx.services.collections;

import ispyb.common.util.ESRFBeamlineEnum;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.mx.daos.collections.Session3DAO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebMethod;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB Session3.
 * </p>
 */
@Stateless
public class Session3ServiceBean implements Session3Service, Session3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Session3ServiceBean.class);

	@EJB
	private Session3DAO dao;

	@Resource
	private SessionContext context;

	public Session3ServiceBean() {
	};

	/**
	 * Create new Session3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Session3VO create(final Session3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Session3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Session3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Session3VO update(final Session3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Session3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Session3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Session3VO vo = findByPk(pk, false, false, false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Session3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Session3VO vo) throws Exception {
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
	 * @return the Session3 value object
	 */
	public Session3VO findByPk(final Integer pk, final boolean withDataCollectionGroup, final boolean withEnergyScan,
			final boolean withXFESpectrum) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Session3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Session3VO found = dao.findByPk(pk, withDataCollectionGroup, withEnergyScan, withXFESpectrum);
				return found;
			}

		});
	}

	public SessionWS3VO findForWSByPk(final Integer pk, final boolean withDataCollectionGroup, final boolean withEnergyScan,
			final boolean withXFESpectrum) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (SessionWS3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Session3VO found = dao.findByPk(pk, withDataCollectionGroup, withEnergyScan, withXFESpectrum);
				SessionWS3VO sesLight = getWSSessionVO(found);
				return sesLight;
			}

		});
	}

	/**
	 * Find all Session3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Session3VO> findAll(final boolean withDataCollectionGroup, final boolean withEnergyScan, final boolean withXFESpectrum)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Session3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Session3VO> foundEntities = dao.findAll(withDataCollectionGroup, withEnergyScan, withXFESpectrum);
				return foundEntities;
			}

		});
	}

	public Integer updateUsedSessionsFlag(final Integer proposalId) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.updateUsedSessionsFlag(proposalId);
				return foundEntities;
			}

		});
	}

	public Integer hasDataCollectionGroups(final Integer sessionId) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.hasDataCollectionGroups(sessionId);
				return foundEntities;
			}

		});
	};

	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Session3VO> findFiltered(final Integer proposalId, final Integer nbMax, final String beamline, final Date date1,
			final Date date2, final Date endDate, final boolean usedFlag, final String operatorSiteNumber) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		LOG.info("Find sessions for proposalId : " + proposalId + " on beamline : " + beamline);

		return (List<Session3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Session3VO> foundEntities = dao.findFiltered(proposalId, nbMax, beamline, date1, date2, endDate, usedFlag,
						null/* nbShifts */, operatorSiteNumber);
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<Session3VO> findByShippingId(final Integer shippingId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Session3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Session3VO> foundEntities = dao.findByShippingId(shippingId);
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<Session3VO> findByStartDateAndBeamLineNameAndNbShifts(final Integer proposalId, final Date startDateBegin,
			final Date startDateEnd, final String beamlineName, final Integer nbShifts) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Session3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Session3VO> foundEntities = dao.findFiltered(proposalId, null/* nbMax */, beamlineName, startDateBegin,
						startDateEnd, null/* endDate */, false/* usedFlag */, nbShifts, null);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove Session3 entities. If not set rollback only and throw
	 * AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				// TODO add a check
				return null;
			}

		});
	}

	/**
	 * returns the session for a specified proposal with endDate > today or null
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSByProposalCodeAndNumber(final String code, final String number, final String beamLineName)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public SessionWS3VO[] doInEJBAccess(Object parent) throws Exception {
				List<Session3VO> foundEntities = dao.findSessionByProposalCodeAndNumber(code, number, beamLineName);
				SessionWS3VO[] vos;
				vos = getWSSessionVOs(foundEntities);
				return vos;
			};
		};
		SessionWS3VO[] ret = (SessionWS3VO[]) template.execute(callBack);
		LOG.info("findForWSByProposalCodeAndNumber : code= " + code + ", number= " + number + ", beamlineName= " + beamLineName);
		return ret;
	}

	/**
	 * returns the list of sessions which have to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSToBeProtected(final Integer delay, final Integer window) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public SessionWS3VO[] doInEJBAccess(Object parent) throws Exception {
				List<Session3VO> foundEntities = dao.findSessionToBeProtected(delay, window);
				if (foundEntities == null)
					return null;
				SessionWS3VO[] vos;
				vos = getWSSessionVOs(foundEntities);
				return vos;
			};
		};
		SessionWS3VO[] ret = (SessionWS3VO[]) template.execute(callBack);
		String listSessionIds = "";

		if (ret != null) {
			for (int i = 0; i < ret.length; i++) {
				listSessionIds = ret[i].getSessionId() + ", ";
			}
		}
		LOG.info("findForWSToBeProtected : " + listSessionIds);
		return ret;
	}

	/**
	 * returns the list of sessions which have to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSNotProtectedToBeProtected(final Date date1, final Date date2) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public SessionWS3VO[] doInEJBAccess(Object parent) throws Exception {
				List<Session3VO> foundEntities = dao.findSessionNotProtectedToBeProtected(date1, date2);
				if (foundEntities == null)
					return null;
				SessionWS3VO[] vos;
				vos = getWSSessionVOs(foundEntities);
				return vos;
			};
		};
		SessionWS3VO[] ret = (SessionWS3VO[]) template.execute(callBack);
		LOG.info("findForWSNotProtectedToBeProtected");
		return ret;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Session3VO getLightSession3VO(Session3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Session3VO otherVO = (Session3VO) vo.clone();
		otherVO.setDataCollectionGroupVOs(null);
		otherVO.setEnergyScanVOs(null);
		otherVO.setXfeSpectrumVOs(null);
		return otherVO;
	}

	private SessionWS3VO[] getWSSessionVOs(List<Session3VO> entities) throws CloneNotSupportedException {
		if (entities == null)
			return null;
		ArrayList<SessionWS3VO> results = new ArrayList<SessionWS3VO>(entities.size());
		for (Session3VO vo : entities) {
			SessionWS3VO otherVO = getWSSessionVO(vo);
			if (otherVO != null)
				results.add(otherVO);
		}
		SessionWS3VO[] tmpResults = new SessionWS3VO[results.size()];
		return results.toArray(tmpResults);
	}

	private SessionWS3VO getWSSessionVO(Session3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Session3VO otherVO = getLightSession3VO(vo);
		Integer beamLineSetupId = null;
		Integer proposalId = null;
		beamLineSetupId = otherVO.getBeamLineSetupVOId();
		proposalId = otherVO.getProposalVOId();
		otherVO.setBeamLineSetupVO(null);
		otherVO.setProposalVO(null);
		SessionWS3VO wsSession = new SessionWS3VO(otherVO);
		wsSession.setBeamLineSetupId(beamLineSetupId);
		wsSession.setProposalId(proposalId);
		return wsSession;
	}

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.updateProposalId(newProposalId, oldProposalId);
				return foundEntities;
			}

		});
	}

	/**
	 * returns the session with the given expSessionPk
	 * 
	 * @param expSessionPk
	 * @return
	 */
	public Session3VO findByExpSessionPk(final Long expSessionPk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Session3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Session3VO> foundEntities = dao.findByExpSessionPk(expSessionPk);
				if (foundEntities == null || foundEntities.size() == 0) {
					return null;
				} else {
					return foundEntities.get(0);
				}
			}

		});
	}

	/**
	 * returns the session linked to the given autoProcScaling
	 * 
	 * @param autoProcScalingId
	 * @return
	 * @throws Exception
	 */
	public Session3VO findByAutoProcScalingId(final Integer autoProcScalingId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Session3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Session3VO foundEntities = dao.findByAutoProcScalingId(autoProcScalingId);
				return foundEntities;
			}

		});
	}

	/**
	 * launch the data confidentiality for the specified session
	 */
	public void protectSession(Integer sessionId) throws Exception {
		if (sessionId != null) {

			Session3VO sessionVO = this.findByPk(sessionId, false, false, false);
			LOG.info("session to be protected = " + sessionId);

			// Check if the session exists
			if (sessionVO == null) {
				LOG.info("session does not exist");
			}
			// Check if the session is already protected
			else if (sessionVO.getProtectedData() != null && sessionVO.getProtectedData().equals("OK")) {
				LOG.info("session is already protected :  " + sessionVO.getProtectedData());
			} else {

				// Check the minimum delay to protect : 2 hours
				Date lastUpdate = sessionVO.getLastUpdate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				Integer delayToTrigger = 2;
				cal.add(Calendar.HOUR_OF_DAY, -delayToTrigger);
				Date limitDate = cal.getTime();

				if (lastUpdate.before(limitDate)) {

					try {
						// proposal account
						String proposalAccount = sessionVO.getProposalVO().getProposalAccount();
						// beamline
						ESRFBeamlineEnum abl = ESRFBeamlineEnum.retrieveBeamlineWithName(sessionVO.getBeamlineName());
						String beamline = abl == null ? "" : abl.getDirectoryName();
						// mx
						String isMx = "true";
						// directory
						Date folderDate = sessionVO.getStartDate();
						SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
						String directory = "";
						if (folderDate != null)
							directory = dt.format(folderDate);
						// call data protection tool
						//TODO put back when runtime error found
//						HttpClient client = new DefaultHttpClient();
//						List<NameValuePair> qparams = new ArrayList<NameValuePair>();
//						qparams.add(new BasicNameValuePair("user", proposalAccount));
//						qparams.add(new BasicNameValuePair("bl", beamline));
//						qparams.add(new BasicNameValuePair("dir", directory));
//						qparams.add(new BasicNameValuePair("mx", isMx));
//						LOG.debug("post user = " + proposalAccount + ", beamline = " + beamline + ", directory = " + directory);
//						URI uri = URIUtils.createURI("http", "dch.esrf.fr", -1, "/protect.php",
//								URLEncodedUtils.format(qparams, "UTF-8"), null);
//						HttpPost post = new HttpPost(uri);
//						HttpResponse response = client.execute(post);
//						BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						String line = "";
						String protectedData = "";
//						while ((line = rd.readLine()) != null) {
//							System.out.println(line);
//							protectedData += line;
//						}

						// result to log into ispyb
						if (protectedData.length() > 1024)
							protectedData = protectedData.substring(0, 1024);
						sessionVO.setProtectedData(protectedData);
						this.update(sessionVO);
						LOG.debug("end of session protection");
					} catch (IOException e) {
						//
						LOG.error("WS ERROR IOException: getDataToBeProtected " + sessionVO.getSessionId());
						e.printStackTrace();
					} catch (Exception e) {
						//
						LOG.error("WS ERROR: getDataToBeProtected " + sessionVO.getSessionId());
						e.printStackTrace();
					}

				} else {
					LOG.info("session not protected because too recent");
				}
			}
		}
	}

	/**
	 * get the number of datcollections which have more then 4 images
	 * 
	 * @param sesId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfCollects(final Integer sesId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.getNbOfCollects(sesId);
				return foundEntities;
			}

		});
	}

	/**
	 * get the number of datacollections which have less/or 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfTests(final Integer dcgId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.getNbOfTests(dcgId);
				return foundEntities;
			}

		});
	}
}
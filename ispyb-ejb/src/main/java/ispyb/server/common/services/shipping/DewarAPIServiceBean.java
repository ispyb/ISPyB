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

import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.DewarAPIBean;
import ispyb.server.common.vos.shipping.DewarLocation3VO;
import ispyb.server.common.vos.shipping.DewarLocationList3VO;
import ispyb.server.common.vos.shipping.DewarTransportHistory3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB DewarAPI.
 * </p>
 */
@Stateless
public class DewarAPIServiceBean implements DewarAPIService, DewarAPIServiceLocal {

	private final static Logger LOG = Logger.getLogger(DewarAPIServiceBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 4090232872703888491L;

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	@Resource
	private SessionContext context;

	public DewarAPIServiceBean() {
	};

	/**
	 * @param dewarBarCode
	 * @return
	 * 
	 */
	public boolean dewarExists(String dewarBarCode) {

		try {
			Dewar3Service _dewarFacade = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);

			if (Constants.DEWAR_BARCODE_STYLE.equals("DLS"))
				dewarBarCode = dewarBarCode.toLowerCase();
			List<Dewar3VO> dewars = _dewarFacade.findByBarCode(dewarBarCode);
			Dewar3VO myDewar = dewars.get(0);
		} catch (Exception e) {
			LOG.debug("Dewar Tracking (dewarExists): cannot find barcode '" + dewarBarCode + "'");
			// e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Add event entry in DewarLocation table
	 * 
	 * @param dewarBarCode
	 * @param username
	 * @param dateTime
	 * @param location
	 * @param courierName
	 * @param trackingNumber
	 * @return
	 * 
	 */
	public boolean addDewarLocation(String dewarBarCode, String username, Date dateTime, String location, String courierName,
			String trackingNumber) {

		try {
			// Write data into DB
			DewarLocation3Service event = (DewarLocation3Service) ejb3ServiceLocator.getLocalService(DewarLocation3Service.class);

			LOG.debug("Dewar Tracking (addDewarLocation): '" + dewarBarCode + "' / '" + username + "' / '" + dateTime + "' / '"
					+ location + "' / " + courierName + "' / '" + trackingNumber + "'");

			DewarLocation3VO vo = new DewarLocation3VO();
			vo.setCourierName(courierName);
			vo.setCourierTrackingNumber(trackingNumber);
			vo.setDateTime(dateTime);
			vo.setLocationName(location);
			vo.setDewarNumber(dewarBarCode);
			vo.setUserId(username);
			event.create(vo);
		} catch (Exception e) {
			LOG.debug("Dewar Tracking (addDewarLocation): cannot insert dewar event for barcode '" + dewarBarCode + "'");
			e.printStackTrace();
			;
			return false;
		}

		return true;
	}

	/**
	 * Get possible dewar locations values from DewarLocationList table
	 * 
	 * @return
	 * 
	 * 
	 */
	public ArrayList<String> getDewarLocations() {

		try {
			DewarLocationList3Service locationService = (DewarLocationList3Service) ejb3ServiceLocator
					.getLocalService(DewarLocationList3Service.class);
			List<DewarLocationList3VO> myList = locationService.findAll();

			ArrayList<String> myLocations = new ArrayList<String>(myList.size());
			for (Iterator<DewarLocationList3VO> i = myList.iterator(); i.hasNext();) {
				String myLocation = (i.next()).getLocationName();
				// ClientLogger.getInstance().debug("Location = "+myLocations);
				myLocations.add(myLocation);
			}

			LOG.debug("Dewar locations = " + myLocations);
			return myLocations;
		} catch (Exception e) {
			LOG.debug("Dewar Tracking: cannot find dewar location list");
			e.printStackTrace();
			;
			return null;
		}
	}

	/**
	 * Fetch dewar info
	 * 
	 * @param dewarBarCode
	 * @return
	 * 
	 */
	public DewarAPIBean fetchDewar(String dewarBarCode) {
		
		DewarAPIBean dewarAPI = new DewarAPIBean();

		try {
			// Init facades
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
			Shipping3Service shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);

			int dewarId;
			int shippingId;
			Integer sessionId = null;
			int proposalId;
//			String parcelName;
//			String shippingName;
//			String sendingLabContactEmail;
//			String  returnLabContactEmail;

			// Get shipping Id
			try {
				if (Constants.DEWAR_BARCODE_STYLE.equals("DLS"))
					dewarBarCode = dewarBarCode.toLowerCase();
				List<Dewar3VO> dewars = dewarService.findByBarCode(dewarBarCode);
				Dewar3VO myDewar = dewars.get(0);
				dewarId = myDewar.getDewarId();
				dewarAPI.setDewarId(dewarId);
				dewarAPI.setParcelName(myDewar.getCode());
				shippingId = myDewar.getShippingVOId();

			} catch (Exception e) {
				LOG.debug("Dewar Tracking (fetchDewar): cannot find shippingId for barcode '" + dewarBarCode + "'");
				// e.printStackTrace();
				return null;
			}

			// Get shipping info
			try {
				Shipping3VO shippings = shippingService.findByPk(shippingId, false);
				dewarAPI.setShippingName(shippings.getShippingName());
				
				proposalId = shippings.getProposalVOId();
				LabContact3VO labContacts1 = shippings.getSendingLabContactVO();
				Person3VO persons1 = labContacts1.getPersonVO();
				LabContact3VO labContacts2 = shippings.getReturnLabContactVO();
				Person3VO persons2 = labContacts2.getPersonVO();

				// Get sendingLabContactEmail and returnLabContactEmail
				if (persons1.getEmailAddress() != null && persons2.getEmailAddress() != null) {

					dewarAPI.setSendingLabContactEmail(persons1.getEmailAddress());
					dewarAPI.setReturnLabContactEmail(persons2.getEmailAddress());
				} else {
					LOG.debug("Dewar Tracking (fetchDewar): cannot find labContact emails for barcode '" + dewarBarCode + "'");
					return null;
				}

			} catch (Exception e) {
				LOG.debug("Dewar Tracking (fetchDewar): cannot find shipping info for barcode '" + dewarBarCode + "'");
				// e.printStackTrace();
				return null;
			}

			// Get beamLineName and localContact
			// old ISPyB web pages : dewar is attached to session
			// EXI : shipment is attached to session
			
			try {
				Dewar3VO dewar = dewarService.findByPk(dewarId, false, false);
				if (dewar.getSessionVO() != null && dewar.getSessionVO().getSessionId() != null ) {
					sessionId = dewar.getSessionVO().getSessionId();
					
				} else if (dewar.getShippingVO().getFirstExp().getSessionId() != null) {
					sessionId = dewar.getShippingVO().getFirstExp().getSessionId();
					
				}
				Session3VO session = sessionService.findByPk(sessionId, false, false, false);

				dewarAPI.setBeamLineName(session.getBeamlineName());
				dewarAPI.setLocalContact(session.getBeamlineOperator());
				dewarAPI.setStartDate(session.getStartDate());
				
			} catch (Exception e) {
				LOG.error("Dewar Tracking (fetchDewar): cannot find session info for barcode '" + dewarBarCode + "'", e);
				// e.printStackTrace();
				// return false ;
				dewarAPI.setBeamLineName("'location not specified in your shipment'");
				dewarAPI.setLocalContact("");
			}

			// Get proposal title and code - Slow...
			try {
				Proposal3VO proposals = proposalService.findByPk(proposalId);
				dewarAPI.setProposalTitle(proposals.getTitle());
				dewarAPI.setProposalCode(proposals.getCode());
				dewarAPI.setProposalNumber(proposals.getNumber());

			} catch (Exception e) {
				LOG.debug("Dewar Tracking (fetchDewar): cannot find proposal info for barcode '" + dewarBarCode + "'");
				// e.printStackTrace();
				return null;
			}

		} catch (Exception e) {
			LOG.debug("Dewar Tracking (fetchDewar): cannot find info for barcode '" + dewarBarCode + "'");
			e.printStackTrace();
			;
			return null;
		}

		return dewarAPI;
	}

	/**
	 * Update Dewar table (dewarStatus, storageLocation, trackingNumberFromESRF) Update Shipping table (shippingStatus, return Courier,
	 * dateOfShippingToUser) Add entry in DewarTransportHistory table
	 * 
	 * @param dewarBarCode
	 * @param location
	 * @param courierName
	 * @param TrackingNumber
	 * @return
	 * 
	 */
	public boolean updateDewar(String dewarBarCode, String location, String courierName, String TrackingNumber) {

		LOG.debug("Dewar Tracking (updateDewar): updating info for dewar barcode '" + dewarBarCode + "'");

		try {
			// Time stamp
			Date dateTime = getDateTime();

			// Dewar status
			String dewarStatus = "";
			if (location.equals("") || location.equals("SENT"))
				dewarStatus = Constants.SHIPPING_STATUS_SENT_TO_USER;
			else
				dewarStatus = Constants.SHIPPING_STATUS_AT_ESRF;
			if (Constants.SITE_IS_ESRF() && location != null
					&& Arrays.binarySearch(Constants.DEWAR_AUTOMATIC_PROCESSING_STATE, location) >= 0) {
				dewarStatus = Constants.SHIPPING_STATUS_PROCESS;
			}
			// Init facades
			Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
			Container3Service containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
			Shipping3Service shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
			DewarTransportHistory3Service dewarTransportHistoryService = (DewarTransportHistory3Service) ejb3ServiceLocator
					.getLocalService(DewarTransportHistory3Service.class);
			// Get dewar and shipping Id
			int dewarId;
			int shippingId;
			if (Constants.DEWAR_BARCODE_STYLE.equals("DLS"))
				dewarBarCode = dewarBarCode.toLowerCase();
			List<Dewar3VO> dewars = dewarService.findByBarCode(dewarBarCode);
			Dewar3VO dewar = null;
			if (dewars.size() == 1) {
				dewar = dewars.get(0);
				dewarId = dewar.getDewarId();
				shippingId = dewar.getShippingVOId();
			} else {
				LOG.debug("Dewar Tracking (updateDewar): cannot find dewar '" + dewarBarCode + "'");
				return false;
			}

			// Update Dewar table (dewarStatus, storageLocation, trackingNumberFromESRF)
			// and DewarFull for display
			dewar.setStorageLocation(location);
			dewar.setDewarStatus(dewarStatus);
			// dewarFull.setEventsNumber(dewarFull.getEventsNumber() + 1);
			if (TrackingNumber != null && !TrackingNumber.equals("")) {
				dewar.setTrackingNumberFromSynchrotron(TrackingNumber);
			}
			dewar.setTimeStamp(getDateTime());
			dewarService.update(dewar);

			// Update Shipping table (shippingStatus, return Courier, dateOfShippingToUser)
			// and ShippingFull for display
			Shipping3VO shipping = shippingService.findByPk(shippingId, false);
			if (shipping != null) {
				// Update for db
				shipping.setShippingStatus(dewarStatus);
				if (dewarStatus.equals(Constants.SHIPPING_STATUS_SENT_TO_USER)) {
					shipping.setReturnCourier(courierName);
					shipping.setDateOfShippingToUser(dateTime);
				}
				shipping.setTimeStamp(getDateTime());
				shippingService.update(shipping);

				// nbEvents += 1;
				// shippingFull.setEventsNumber(shippingFull.getEventsNumber() + 1);
			} else {
				LOG.debug("Dewar Tracking (updateDewar): cannot find shipping for dewar '" + dewarBarCode + "'");
				return false;
			}

			// Update Container status (avoid to have containers in processing mode in MxCube)
			List<Container3VO> containers = containerService.findByDewarId(dewarId);
			for (int i = 0; i < containers.size(); i++) {
				Container3VO container = containers.get(i);
				container.setContainerStatus(dewarStatus);
				containerService.update(container);

			}

			// Add entry in DewarTransportHistory table
			DewarTransportHistory3VO newHistory = new DewarTransportHistory3VO();
			newHistory.setDewarStatus(dewarStatus);
			newHistory.setStorageLocation(location);
			newHistory.setArrivalDate(dateTime);
			newHistory.setDewarVO(dewar);
			dewarTransportHistoryService.create(newHistory);

		} catch (Exception e) {
			LOG.debug("Dewar Tracking (updateDewar): cannot update dewar '" + dewarBarCode + "'");
			e.printStackTrace();
			;
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	private Date getDateTime() {

		java.util.Date today = new java.util.Date();
		return today;
	}
}
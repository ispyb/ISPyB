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
/*
 * createContainerAction.java
 * @author ludovic.launer@esrf.fr
 * Mar 4, 2005
 */

package ispyb.client.mx.container;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.common.util.DBTools;
import ispyb.common.util.Constants;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * 
 * @struts.action name="viewContainerForm" path="/user/createContainerAction"
 *                type="ispyb.client.mx.container.CreateContainerAction" input="user.shipping.container.create.page"
 *                validate="false" parameter="reqCode" scope="session"
 * 
 * @struts.action-forward name="containerCreatePage" path="user.shipping.container.create.page"
 * 
 * @struts.action-forward name="sampleForContainerCreatePage" path="user.sample.create.page"
 * 
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="dewarViewPage" path=
 *                        "/menuSelected.do?leftMenuId=11&amp;topMenuId=10&amp;targetUrl=%2Fuser%2FviewDewarAction.do%3FreqCode%3DdisplaySlave"
 * 
 * @struts.action-forward name="viewUnasignedContainerPage" path=
 *                        "/menuSelected.do?leftMenuId=47&amp;topMenuId=10&amp;targetUrl=%2Fuser%2FviewDewarAction.do%3FreqCode%3DdisplayFreeContainers"
 * 
 * @struts.action-forward name="createSamplePage" path=
 *                        "/menuSelected.do?leftMenuId=11&amp;topMenuId=10&amp;targetUrl=%2Fuser%2FcreateSampleAction.do%3FreqCode%3Ddisplay"
 * 
 * @struts.action-forward name="viewSampleForContainerPage" path=
 *                        "/menuSelected.do?leftMenuId=11&amp;topMenuId=10&amp;targetUrl=%2Fuser%2FviewSample.do%3FreqCode%3DdisplayForContainer"
 */

public class CreateContainerAction extends org.apache.struts.actions.DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Dewar3Service dewarService;

	private Container3Service containerService;

	private BLSample3Service blSample3Service;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		this.containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
		this.blSample3Service = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	// Session Attributes
	Integer mProposalId = null;

	/**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();

		try {

			Dewar3VO dlv = new Dewar3VO();

			// Save Originator for return path
			BreadCrumbsForm.getIt(request).setFromPage(new URL(request.getHeader("Referer")).getFile());

			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			String dewarId = request.getParameter(Constants.DEWAR_ID);
			Boolean isSelectingContainer = new Boolean(request.getParameter("isSelectingContainer"));
			Boolean selectForBreadCrumb = new Boolean(request.getParameter("selectForBreadCrumb"));

			// ----- Update BreadCrumbs with dewar information
			if (dewarId != null) // Dewar specified = Add Container to Dewar
			{
				dlv = dewarService.findByPk(new Integer(dewarId), false, false);
				BreadCrumbsForm.getIt(request).setSelectedDewar(dlv);
				isSelectingContainer = Boolean.TRUE;
			} else // No Dewar specified
			{
				if (!isSelectingContainer.booleanValue())
					BreadCrumbsForm.getItClean(request);
			}

			request.setAttribute("isSelectingContainer", isSelectingContainer);
			request.setAttribute("selectForBreadCrumb", selectForBreadCrumb);
			BreadCrumbsForm.getIt(request).setSelectedContainer(null);

			/*
			 * Insert in form the list of containers: <name, capacity>
			 */
			ViewContainerForm form = (ViewContainerForm) actForm;
			form.setContainersInfo(new ArrayList());

			for (int i = 0; i < Constants.CONTAINER_TYPE.length; i++) {
				ContainerDO cont = new ContainerDO(i, Constants.CONTAINER_TYPE[i], Constants.CONTAINER_CAPACITY[i]);
				form.getContainersInfo().add(cont);
			}

			FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("containerCreatePage");
	}

	/**
	 * updateDisplay
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward updateDisplay(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		try {
			// Save Originator for return path
			BreadCrumbsForm.getIt(request).setFromPage(new URL(request.getHeader("Referer")).getFile());
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewContainerForm form = (ViewContainerForm) actForm; // Parameters
			// submited by
			// form
			String containerId = request.getParameter(Constants.CONTAINER_ID);
			// ---------------------------------------------------------------------------------------------------

			/*
			 * Insert in form the list of containers: <name, capacity>
			 */
			form.setContainersInfo(new ArrayList());
			for (int i = 0; i < Constants.CONTAINER_TYPE.length; i++) {
				ContainerDO cont = new ContainerDO(i, Constants.CONTAINER_TYPE[i], Constants.CONTAINER_CAPACITY[i]);
				form.getContainersInfo().add(cont);
			}

			// Retrieve Container information
			Container3VO info = containerService.findByPk(new Integer(containerId), false);

			form.setContainer3VO(info);

			String fromPage = BreadCrumbsForm.getIt(request).getFromPage(); // to know from which page action is called
			BreadCrumbsForm.getItClean(request).setSelectedContainer(info);
			BreadCrumbsForm.getIt(request).setFromPage(fromPage);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("containerCreatePage");
	}

	/**
	 * update
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewContainerForm form = (ViewContainerForm) actForm;
			Container3VO selectedContainer = null;
			// Submited by form
			Boolean isSelectingContainer = new Boolean(request.getParameter("isSelectingContainer"));

			if (BreadCrumbsForm.getIt(request).getSelectedContainer() != null) // Update existing container
				selectedContainer = form.getContainer3VO();
			else // Assign existing container to Dewar
			{
				Integer dewarId = BreadCrumbsForm.getIt(request).getSelectedDewar().getDewarId();
				Dewar3VO dewarVO = dewarService.findByPk(dewarId, false, false);
				Integer containerId = form.getTheContainerId();

				selectedContainer = containerService.findByPk(containerId, false);
				selectedContainer.setDewarVO(dewarVO);
			}

			// ----- Update database
			containerService.update(selectedContainer);

			// update also sampleFull BMP to have a correct view
			// old BlSampleFullFacadeLocal blsampleFull = BlSampleFullFacadeUtil.getLocalHome().create();
			// old ArrayList sampleList = (ArrayList)
			// blsampleFull.findByContainerId(selectedContainer.getContainerId());
			List<BLSample3VO> sampleList = this.blSample3Service.findByContainerId(selectedContainer.getContainerId());

			Iterator<BLSample3VO> it = sampleList.iterator();

			while (it.hasNext()) {
				// old BlSampleFullValue fullValue = (BlSampleFullValue) it.next();
				BLSample3VO fullValue = it.next();

				Container3VO container3VO = containerService.findByPk(fullValue.getContainerVOId(), false);
				// old fullValue.setContainerCode(selectedContainer.getCode());
				container3VO.setCode(selectedContainer.getCode());
				containerService.update(container3VO);

			}

			// Add to BreadCrumbs if selecting the Container
			if (isSelectingContainer.booleanValue())
				request.setAttribute("isSelectingContainer", isSelectingContainer);

			BreadCrumbsForm.getIt(request).setSelectedContainer(selectedContainer);

			// ----- Return to Originator -----
			String originator = BreadCrumbsForm.getIt(request).getFromPage();
			if (originator != null) {
				BreadCrumbsForm.getIt(request).setFromPage(null);
				response.sendRedirect(originator);
			}
			// ---------------------------------

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return mapping.findForward("dewarViewPage");
	}

	/**
	 * save used when the create container is called from shipment menu
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward save(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		if (BreadCrumbsForm.getIt(request).getSelectedContainer() != null) // Redirect to the update
			return this.update(mapping, actForm, request, response);

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewContainerForm form = (ViewContainerForm) actForm; // Parameters
			Integer dewarId = null;
			// submited by form
			Boolean isSelectingContainer = new Boolean(request.getParameter("isSelectingContainer"));

			// Check mandatory attributes are present
			// if (BreadCrumbsForm.getIt(request).getSelectedDewar() == null) throw new
			// Exception("Trying to create Container info with no selectedDewar");

			if (BreadCrumbsForm.getIt(request).getSelectedDewar() != null)
				dewarId = BreadCrumbsForm.getIt(request).getSelectedDewar().getDewarId();
			// ---------------------------------------------------------------------------------------------------
			// Build Query
			// Insert into DB
			Container3VO info = form.getContainer3VO();
			Container3VO cont = new Container3VO(info);
			cont.setContainerId(null);// to force the creation of a new one
			// Populate with info from form
			Dewar3VO dewarVO = dewarService.findByPk(dewarId, false, false);
			cont.setDewarVO(dewarVO);
			cont.setTimeStamp(new Timestamp(new Date().getTime()));
			cont.setContainerStatus(Constants.SHIPPING_STATUS_OPENED);

			cont = containerService.create(cont);

			// Add to BreadCrumbs if selecting the Container
			if (isSelectingContainer.booleanValue()) {
				BreadCrumbsForm.getIt(request).setSelectedContainer(cont);
			}

			// // ----- Return to Originator -----
			// String originator = BreadCrumbsForm.getIt(request).getFromPage();
			// if (isSelectingContainer.booleanValue()) originator += "&isSelectingContainer=true";
			// if (originator != null)
			// {
			// BreadCrumbsForm.getIt(request).setFromPage(null);
			// response.sendRedirect(originator);
			// return mapping.findForward(originator);
			// }
			// // ---------------------------------
			//
			// else if (BreadCrumbsForm.getIt(request).getSelectedDewar() == null)
			// {
			// BreadCrumbsForm.getItClean(request);
			// return mapping.findForward("viewUnasignedContainerPage");
			// }
			// else
			return new ActionForward(Constants.PAGE_SAMPLE_CREATE, false);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward selectForBreadCrumb(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewContainerForm form = (ViewContainerForm) actForm; // Parameters
			Integer containerId = form.getTheContainerId();

			Container3VO clv = containerService.findByPk(containerId, false);
			BreadCrumbsForm.getIt(request).setSelectedContainer(clv);

			// ----- Return to Originator -----
			String originator = BreadCrumbsForm.getIt(request).getFromPage();
			if (originator != null) {
				// originator += "&isSelectingContainer=true";
				BreadCrumbsForm.getIt(request).setFromPage(null);
				// TODO Redirect to the originator, making sure that we have a isSelectingContainer=true in the url
				// response.sendRedirect(originator);
				return mapping.findForward("createSamplePage");
			} else
				return mapping.findForward("dewarViewPage");
			// ---------------------------------

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}

	/**
	 * Delete the Dewar
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		try {
			// Save Originator for return path
			BreadCrumbsForm.getIt(request).setFromPage(new URL(request.getHeader("Referer")).getFile());
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			Integer containerId = new Integer(request.getParameter(Constants.CONTAINER_ID));

			Container3VO container = DBTools.getSelectedContainer(containerId);

			// Delete
			containerService.delete(container);

			// ----- Return to Originator -----
			String originator = BreadCrumbsForm.getIt(request).getFromPage();
			if (originator != null) {
				BreadCrumbsForm.getIt(request).setFromPage(null);
				response.sendRedirect(originator);
			}
			// ---------------------------------
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("dewarViewPage");
	}

	public ActionForward clone(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		try {
			// Save Originator for return path
			BreadCrumbsForm.getIt(request).setFromPage(new URL(request.getHeader("Referer")).getFile());
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			Integer containerId = new Integer(request.getParameter(Constants.CONTAINER_ID));

			// Clone
			cloneContainer(containerId);

			// ----- Return to Originator -----
			String originator = BreadCrumbsForm.getIt(request).getFromPage();
			if (originator != null) {
				BreadCrumbsForm.getIt(request).setFromPage(null);
				response.sendRedirect(originator);
			}
			// ---------------------------------

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("dewarViewPage");
	}

	/**
	 * 
	 * @param containerId
	 * @return
	 * @throws Exception
	 */
	public Container3VO cloneContainer(Integer containerId) throws Exception {
		Container3VO newContainer = null;
		Container3VO sourceContainer = DBTools.getSelectedContainer(containerId);

		if (sourceContainer != null) {
			newContainer = new Container3VO(sourceContainer);
			newContainer.setTimeStamp(new Timestamp(new Date().getTime()));

			containerService.create(newContainer);
		}

		return newContainer;
	}

	/**
	 * RetrieveSessionAttributes
	 * 
	 * @param request
	 *            The Request to retrieve the Session Attributes from Populates the class Attributes with Attributes
	 *            stored in the Session
	 */
	public void retrieveSessionAttributes(HttpServletRequest request) {
		this.mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
	}

}

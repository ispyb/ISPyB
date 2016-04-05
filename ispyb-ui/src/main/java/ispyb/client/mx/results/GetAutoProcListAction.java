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
package ispyb.client.mx.results;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProc3Service;
import ispyb.server.mx.services.autoproc.AutoProcScalingStatistics3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Class to meet Ajax request for the autoproc list. (select box on viewResults_top.jsp)
 * 
 * @struts.action name="getAutoProcListForm" path="/user/getAutoProcList"
 *                input="/tiles/bodies/mx/results/viewAutoProcList.jsp" validate="false" parameter="reqCode"
 *                scope="request"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="success" path="/tiles/bodies/mx/results/viewAutoProcList.jsp"
 */

public class GetAutoProcListAction extends DispatchAction {
	
	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private AutoProc3Service apService;
	
	private AutoProcScalingStatistics3Service apssService;
	
	private double DEFAULT_RMERGE = 50.0;

	private double DEFAULT_ISIGMA = 1.0;
	
	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		
		this.apService = (AutoProc3Service) ejb3ServiceLocator.getLocalService(AutoProc3Service.class);
		this.apssService = (AutoProcScalingStatistics3Service) ejb3ServiceLocator.getLocalService(AutoProcScalingStatistics3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}
	
	
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		GetAutoProcListForm form = (GetAutoProcListForm) actForm;

		Integer dataCollectionId = new Integer(0);
		String rMerge = form.getRmerge();
		String iSigma = form.getIsigma();
		boolean anomalous = form.getAnomalous();

		double rMerge_d = DEFAULT_RMERGE;
		double iSigma_d = DEFAULT_ISIGMA;

		try {
			if (!rMerge.equals("undefined") && !rMerge.equals(""))
				rMerge_d = Double.parseDouble(rMerge);
			if (!iSigma.equals("undefined") && !iSigma.equals(""))
				iSigma_d = Double.parseDouble(iSigma);
			if (!dataCollectionId.equals("undefined") && !dataCollectionId.equals(""))

				if (BreadCrumbsForm.getIt(request).getSelectedDataCollection() != null){
					dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
				}else if (form.getDataCollectionId() != null){
					dataCollectionId = Integer.parseInt(form.getDataCollectionId());
				}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}

		if (dataCollectionId == 0)
			return mapping.findForward("success");

		try {
			// nb total autoProc
			int nbRemoved = 0;
			
			List<AutoProc3VO> autoProcs = apService.findByDataCollectionId(dataCollectionId);

			// find autoProcs, sorting directly in the database
			List<AutoProc3VO> autoProcsAnomalous = apService
					.findByAnomalousDataCollectionIdAndOrderBySpaceGroupNumber(dataCollectionId, anomalous);

			ArrayList<AutoProc3VO> autoProcList = new ArrayList<AutoProc3VO>();
			if (autoProcsAnomalous != null) {
				autoProcList = new ArrayList<AutoProc3VO>(autoProcsAnomalous);
			}
			for (Iterator<AutoProc3VO> i = autoProcList.iterator(); i.hasNext();) {
				AutoProc3VO apv = (AutoProc3VO) i.next();

				List<AutoProcScalingStatistics3VO> scalingStatistics = apssService.findByAutoProcId(
						apv.getAutoProcId(), "innerShell");
				boolean existsUnderRmergeAndOverSigma = false;

				for (Iterator<AutoProcScalingStatistics3VO> j = scalingStatistics.iterator(); j.hasNext();) {
					AutoProcScalingStatistics3VO stats = (AutoProcScalingStatistics3VO) j.next();
					if (stats.getRmerge() != null && stats.getRmerge() < rMerge_d && stats.getMeanIoverSigI() > iSigma_d)
						existsUnderRmergeAndOverSigma = true;
				}

				if (!existsUnderRmergeAndOverSigma){
					i.remove();
					nbRemoved = nbRemoved +1;
				}
			}
			form.setNbAutoProc(autoProcs.size());
			form.setNbRemoved(nbRemoved);
			form.setAutoProcs(autoProcList);
			
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("success");
		}

		return mapping.findForward("success");
	}

	// private ArrayList<AutoProc3VO> sortAutoProcList(Collection<AutoProc3VO> autoProcs, Collection<SpaceGroupValue>
	// spaceGroups){
	// ArrayList<AutoProc3VO> autoProcList = new ArrayList<AutoProc3VO>(autoProcs.size());
	//
	// /* We need to sort by SpaceGroupNumber, which is not included
	// * in AutoProcBean. EJBQL does not include an ORDER BY statement. We look up each element's
	// * spaceGroupNumber and compare it to all other elements in the list. Finding the largest,
	// * we add this to a new list and remove it from the old. This is a nightmare,
	// * fortunately this list appears to almost always be small.
	// */
	// outer:
	// while(!autoProcs.isEmpty()){
	// for(Iterator i = autoProcs.iterator(); i.hasNext(); ){
	// AutoProc3VO apv = (AutoProc3VO) i.next();
	// Integer spaceGroupNumber = new Integer(0);
	//
	//
	// for(Iterator j = spaceGroups.iterator(); j.hasNext(); ){
	// SpaceGroupValue sgv = (SpaceGroupValue) j.next();
	// if(sgv.getSpaceGroupShortName().toUpperCase().equals(apv.getSpaceGroup().replace(" ", "").toUpperCase())){
	// spaceGroupNumber = sgv.getSpaceGroupNumber() == null ? 0 : sgv.getSpaceGroupNumber();
	// break;
	// }
	// }
	//
	//
	// int higher_than_me = 0;
	//
	// for(Iterator j = autoProcs.iterator(); j.hasNext(); ){
	// AutoProc3VO comp = (AutoProc3VO) j.next();
	// if(comp == apv) continue;
	//
	// Integer compSpaceGroupNumber = new Integer(0);
	//
	// for(Iterator k = spaceGroups.iterator(); k.hasNext(); ){
	// SpaceGroupValue sgv = (SpaceGroupValue) k.next();
	// if(sgv.getSpaceGroupShortName().toUpperCase().equals(comp.getSpaceGroup().replace(" ", "").toUpperCase())){
	// compSpaceGroupNumber = sgv.getSpaceGroupNumber() == null ? 0 : sgv.getSpaceGroupNumber();
	// break;
	// }
	// }
	//
	// if(compSpaceGroupNumber.compareTo(spaceGroupNumber) > 0)
	// higher_than_me++;
	// }
	//
	// if(higher_than_me == 0){
	// autoProcList.add(apv);
	// i.remove();
	// continue outer;
	// }
	// }
	// }
	//
	// return autoProcList;
	// }
}

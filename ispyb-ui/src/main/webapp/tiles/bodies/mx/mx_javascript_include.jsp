<%--------------------------------------------------------------------------------------------------
This file is part of ISPyB.

ISPyB is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ISPyB is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.

Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, , A. De Maria Antolinos
--------------------------------------------------------------------------------------------------%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<%@page import="ispyb.common.util.Constants"%>
<%@ page isELIgnored="false" %>

<script type='text/javascript' src="<%=request.getContextPath()%>/js/external/toggleDiv.js"></script>
<script type='text/javascript' src="<%=request.getContextPath()%>/js/external/progressbar.js"></script>

<%
		String jsWebserverURL = request.getContextPath() + "/js/";
		String jsWebserverURLUtils = jsWebserverURL + "ispyb/utils/";
		String jsWebserverURLMx = jsWebserverURL + "ispyb/mx/";
		String jsWebserverURLMxUtils = jsWebserverURL + "ispyb/mx/utils/";
		String jsWebserverURLSession = jsWebserverURLMx + "session/";	
		String jsWebserverURLCollection = jsWebserverURLMx + "collection/";	
		String jsWebserverURLMesh = jsWebserverURLMx + "collection/mesh/";	
		String jsWebserverURLDehydration = jsWebserverURLMx + "collection/dehydration/";	
		String jsWebserverURLAdmin = jsWebserverURLMx + "admin/";
		String jsWebserverURLResult = jsWebserverURLMx + "result/";
		String jsWebserverURLRanking = jsWebserverURLMx + "ranking/";
		String jsWebserverURLShipping = jsWebserverURLMx + "shipping/";

	%>
			
<script type="text/javascript" src="<%=jsWebserverURL%>external/extjs-4.1.1/ext-all.js"></script>

<script type="text/javascript" src="<%=jsWebserverURL%>external/jquery-1.7.1/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsWebserverURL%>external/jquery-ui-1.8.24.custom.min.js"></script>

<script type="text/javascript" src="<%=jsWebserverURLUtils%>event.js"></script>
<script type="text/javascript" src="<%=jsWebserverURLUtils%>genericwindow.js"></script>
<script type="text/javascript" src="<%=jsWebserverURLUtils%>errorPanel.js"></script>
<script type="text/javascript" src="<%=jsWebserverURLUtils%>utils.js"></script>	


<script type="text/javascript" src="<%=jsWebserverURL%>external/canvg.js"></script>
<script type="text/javascript" src="<%=jsWebserverURL%>external/rgbcolor.js"></script>
<script type="text/javascript" src="<%=jsWebserverURL%>external/svgenie.js"></script>

<script src="<%=jsWebserverURLSession%>sessionForm.js"></script>	
	
	<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
	
	<%
	String JS_MINIMIZED = Constants.JS_MINIMIZED;
%>
			
<c:set var="minimized" value="<%=JS_MINIMIZED %>"/>
<c:choose>
	<c:when test="${minimized}">
	<script type="text/javascript" src="<%=jsWebserverURL%>ispyb/min/ispyb-mx-min.js"></script>
</c:when> 
	<c:otherwise>
	
	<script type="text/javascript" src="<%=jsWebserverURLMxUtils%>mxsinterfaceutils.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLSession%>mainSession.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLSession%>session.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLSession%>sessionGrid.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLSession%>sessionForm.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLSession%>sessionWindow.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLSession%>searchSessionPanel.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLAdmin%>mainDataConfidentialityLog.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLAdmin%>dataConfidentialityPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLAdmin%>viewLogOptionPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLAdmin%>launchDataConfidentialityPanel.js"></script>
	
	<script src="<%=jsWebserverURLCollection%>mainCollectionGroup.js"></script>
	<script src="<%=jsWebserverURLCollection%>collectionGroup.js"></script>
	<script src="<%=jsWebserverURLCollection%>dataCollectionGroupGrid.js"></script>
	<script src="<%=jsWebserverURLCollection%>xrfSpectraGrid.js"></script>
	<script src="<%=jsWebserverURLCollection%>energyScanGrid.js"></script>
	<script src="<%=jsWebserverURLCollection%>referencePanel.js"></script>	
	<script src="<%=jsWebserverURLCollection%>crystalClassGrid.js"></script>
	<script src="<%=jsWebserverURLCollection%>sessionStatsPanel.js"></script>
	<script src="<%=jsWebserverURLCollection%>reportSessionPanel.js"></script>
	<script src="<%=jsWebserverURLCollection%>sessionViewGrid.js"></script>
	<script src="<%=jsWebserverURLCollection%>mainSessionSummary.js"></script>
	<script src="<%=jsWebserverURLCollection%>sessionSummaryObject.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>mainCreatePuck.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>shipping.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>createPuckGrid.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>puckHeadPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>mainFillShipment.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>dewarPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>fillShipmentPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>dewarWindow.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>dewarForm.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>puckWindow.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>puckForm.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLShipping%>helpPanel.js"></script>


	<script type="text/javascript" src="<%=jsWebserverURLCollection%>mainCollection.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>mainLastCollect.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>mainCollectionInDC.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>collection.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>dataCollectionGrid.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>searchDataCollectionPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>imagePanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>imageTabPanel.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>mainWorkflow.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>workflowPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>xtalSnapshotPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>wallImagePanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>referencePanel.js"></script>	
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>resultPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>webPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>reportPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>parametersPanel.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLCollection%>viewDataCollection_ajax.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>meshScanPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>meshTabPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>mapPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>positionGrid.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>colorRangeManager.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>mapsettingswindow.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>mapSettingsPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>grid.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>gridcontroller.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>gridmodel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>gridview.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>sliderWidget.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>meshWallImagePanel.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>graphics/graphics_svg.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLMesh%>graphics/graphicsprimitives.js"></script>

	<script type="text/javascript" src="<%=jsWebserverURLDehydration%>dehydrationPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLDehydration%>chart/exptCellChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLDehydration%>chart/exptCellTimeChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLDehydration%>chart/exptLabelitChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLDehydration%>chart/exptResChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLDehydration%>chart/exptResolutionTimeChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLDehydration%>chart/exptSpotsTimeChart.js"></script>	

	<script type="text/javascript" src="<%=jsWebserverURLRanking%>autoProcRankingData.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLRanking%>autoProcRanking.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLRanking%>panel/chartPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLRanking%>chart/radarChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLRanking%>chart/lineChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLRanking%>chart/stackedBarChart.js"></script>
	
	
	<script type="text/javascript" src="<%=jsWebserverURLResult%>mainAutoprocessing.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>result.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>mainResult.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLResult%>experimentPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>beamlinePanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>characterisationParamPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>characterisationResultPanel.js"></script>
	
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoprocessingPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcProgramAttachmentGraph.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcProgramAttachmentGraphPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcProgramAttachmentGraphChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>wilsonChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcGrid.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcListPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcParamPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcActionPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcReportPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcDataPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcLeftPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcRightPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcFilePanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcStatusGrid.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>autoProcStatusPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>interruptedAutoProcPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>denzoPanel.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>cumulativeIntensityDistributionChart.js"></script>
	<script type="text/javascript" src="<%=jsWebserverURLResult%>reprocessingPanel.js"></script>
	
</c:otherwise>
</c:choose>	

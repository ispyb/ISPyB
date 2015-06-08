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

Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
--------------------------------------------------------------------------------------------------%>		
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page isELIgnored="false" %>
<%@page import="ispyb.common.util.Constants"%> 
		
			<%
				String JS_MINIMIZED = Constants.JS_MINIMIZED;	
				Boolean productionMode = true;
				String externalURL = request.getContextPath() + "/js/external/";
				String cssURL = request.getContextPath() + "/js/css/";
				String jsWebserverURL = request.getContextPath();
				
				//java.util.Properties prop = new java.util.Properties();
                //java.io.InputStream in = getServletContext().getResourceAsStream("/WEB-INF/classes/ISPyB.properties");
               // prop.load(in);
                //System.out.println("Alternative JS path: " + prop.getProperty("js.alternate.path"));
               // in.close();
               // jsWebserverURL = prop.getProperty("js.alternate.path");
				
				
			%>
			
			<!-- EXTERNAL: Ext 4.1.1 -->
			<script type="text/javascript" src="<%=externalURL%>extjs-4.1.1/bootstrap.js"></script>  
			
			<!-- EXTERNAL: JQuery 1.7.1 -->
			<script type="text/javascript" src="<%=externalURL%>jquery-1.7.1/jquery-1.7.1.min.js"></script>
			
			<script type="text/javascript" src="<%=externalURL%>webgl/Three49custom.js"></script>
			<script type="text/javascript" src="<%=externalURL%>webgl/GLmol.js"></script> 
			<script type="text/javascript" src="<%=externalURL%>moment.min.js"></script>
			
			<!-- EXTERNAL: calendar -->
			<script type="text/javascript" src="<%=externalURL%>calendar/fullcalendar-1.6.4/jquery-ui.custom.min.js"></script>
			<script type="text/javascript" src="<%=externalURL%>calendar/fullcalendar-1.6.4/fullcalendar.min.js"></script>
				
			<!-- EXTERNAL: Qtip2 -->
			<script type="text/javascript" src="<%=externalURL%>jquery-1.7.1/jquery.qtip.min.js"></script>

			<!-- dygraph -->
			<script type="text/javascript" src="<%=externalURL%>dygraph/dygraph-dev.js"></script>
			<script type="text/javascript" src="<%=externalURL%>dygraph/external/dygraph-extra.js"></script>
			
			
			<c:set var="minimized" value="<%=JS_MINIMIZED %>"/>
			<c:set var="url" value="<%= jsWebserverURL %>"/>
			<c:set var="type" value="text/javascript"/>
			
			<c:choose>
				<c:when test="${minimized}">
					<!-- <script type="${type}" src="${url}/js/ispyb/biosaxs/biosaxs_min.js"></script>-->
					<script type="${type}" src="${url}/js/ispyb/min/ispyb-bx-min.js"></script>
					<script type="${type}" src="${url}/js/ispyb/min/ispyb-mx-min.js"></script>
				</c:when> 
				<c:otherwise>
					<!--  UTILS -->
					<script type="${type}" src="${url}/js/ispyb/biosaxs/utils/genericwindow.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/utils/event.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/utils/biosaxsinterfaceutils.js"/></script>
					<script type="${type}" src="${url}/js/external/json2.js"/></script>
					
					<!--  DATA -->
					<script type="${type}" src="${url}/js/ispyb/biosaxs/data/proposal.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/data/experiment.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/data/experiments.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/data/dataadapter.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/data/shipment.js"/></script>
					   
					<!--  GRAPH --> 
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/dygraphwidget.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/stddevdygraph.js"/></script>  
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/hplcgraph.js"/></script>  
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/genericgraph.js"/></script>
					
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/boxwhiskergraph.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/rangewhiskergraph.js"/></script>
					
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/layoutmanager.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/dataset/dataset.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/graphdataset.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/graphitem.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/vertex.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/edge.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/networkdatasetformatter.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/itemgraphformatter.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/vertexgraphformatter.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/commons/data/edgegraphformatter.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/graphcanvas.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/dependencies/dom-utils.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/dependencies/graphics/graphics_svg.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/dependencies/normalization.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/dependencies/colors.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/dependencies/geometry.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/graph/networkwidget.js"/></script>
					     
					<!--  FORM -->
					<script type="${type}" src="${url}/js/ispyb/biosaxs/form/experimentheaderform.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/form/experimentform.js"/></script>  
					<script type="${type}" src="${url}/js/ispyb/biosaxs/form/stocksolutionform.js"/></script>  
					<script type="${type}" src="${url}/js/ispyb/biosaxs/form/caseform.js"/></script>  
					<script type="${type}" src="${url}/js/ispyb/biosaxs/form/shipmentform.js"/></script>  
					<script type="${type}" src="${url}/js/ispyb/biosaxs/form/bufferform.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/form/macromoleculeform.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/form/resultsummaryform.js"/></script>
					
					<!--  WINDOW -->  
					<script type="${type}" src="${url}/js/ispyb/biosaxs/window/experimentwindow.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/window/multipleeditmeasurementgridwindow.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/window/stocksolutionwindow.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/window/casewindow.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/window/bufferwindow.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/window/macromoleculewindow.js"/></script>
					
				
					<!--  WIDGET -->
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/calendarwidget.js"/></script>
					
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/prepareexperimentwidget.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/shippingwidget.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/sampleplatewidget.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/sampleplategroup_v2.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/window/datacollectioncurvevisualizer.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/window/datacollectionframetree.js"/></script>
					
					
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/warningwidget.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/concentrationhtmltablewidget.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/qualitycontrolresultswidget.js"/></script>
				
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/wizard/wizardwidget.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/wizard/genericstepwizardform.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/wizard/experimenttypesetepwizardform.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/wizard/macromoleculeselectorwizardform.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/wizard/measurementcreatorstepwizardform.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/wizard/finalstepwizardform.js"/></script>
					
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/pdb/pdbviewer.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/widget/pdb/datacollectionpdbwidget.js"/></script>
					 
				    <!--  GRID -->
				    <script type="${type}" src="${url}/js/ispyb/biosaxs/grid/volumegrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/additivegrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/analysisgrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/buffergrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/casegrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/experimentgrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/macromoleculegrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/measurementgrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/resultsassemblygrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/shipmentgrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/specimengrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/stocksolutiongrid.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/grid/templategrid.js"/></script>
				    <script type="${type}" src="${url}/js/ispyb/biosaxs/grid/framegrid.js"/></script>   
				     <script type="${type}" src="${url}/js/ispyb/biosaxs/grid/queuegrid.js"/></script>     
					
					<!--  OTHERS -->  
					<script type="${type}" src="${url}/js/ispyb/biosaxs/tabs/shipmenttabs.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/tabs/templatetabs.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/tabs/hplctabs.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/tabs/experimenttabs.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/tabs/resulttabs.js"/></script>
					<script type="${type}" src="${url}/js/ispyb/biosaxs/entrypoint.js"/></script>
				</c:otherwise>
			</c:choose>
			
			
			
			
			
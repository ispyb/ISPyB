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

<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Related Information bar --%>
<layout:row width="100%" styleClass="PANEL_BREAD_CRUMBS" space="false">
	<layout:grid cols="10"  borderSpacing="5">

		<%-- Selected Proposal Info --%>
		<jsp:include page="selectedProposal.jsp" flush="true" />

		<%-- Selected Shipping Info --%>
		<jsp:include page="selectedShipping.jsp" flush="true" />
		
		<%-- Selected Dewar Info --%>
		<jsp:include page="selectedDewar.jsp" flush="true" />
		
		<%-- Selected Container Info --%>
		<jsp:include page="selectedContainer.jsp" flush="true" />
		
		<%-- Selected Sample Info --%>
		<jsp:include page="selectedSample.jsp" flush="true" />
		
		<%-- Selected Crystal Info --%>
		<jsp:include page="selectedCrystal.jsp" flush="true" />
		
		<%-- Selected Protein Info --%>
		<jsp:include page="selectedProtein.jsp" flush="true" />

		<%-- Selected Session Info --%>
		<jsp:include page="selectedSession.jsp" flush="true" />
		
		<%-- Selected DataCollectionGroup Info --%>
		<jsp:include page="selectedDataCollectionGroup.jsp" flush="true" />
		
		<%-- Selected Workflow Info --%>
		<jsp:include page="selectedWorkflow.jsp" flush="true" />
		
		<%-- Selected DataCollection Info --%>
		<jsp:include page="selectedDataCollection.jsp" flush="true" />
				
		<%-- Selected Image Info --%>
		<jsp:include page="selectedImage.jsp" flush="true" />

		<%-- Selected Screening --%>
		<jsp:include page="selectedScreening.jsp" flush="true" />


   </layout:grid>
</layout:row>
<layout:row width="100%">
		    <%-- Samples in sample changer --%>
		    <jsp:include page="samplesInSampleChanger.jsp" flush="true" />
	    </layout:row>
<layout:row width="100%">
		    <%-- Samples not linked to any container --%>
		    <jsp:include page="freeSamples.jsp" flush="true" />
	    </layout:row>


<%-------------------------%>

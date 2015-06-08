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

<%@ page import="ispyb.common.util.Constants"%>

<%
	String targetSample = request.getContextPath() + "/user/analyseSample.do?reqCode=editSample";
    String targetDataCollections = request.getContextPath() + "/user/viewDataCollection.do?reqCode=displayForSample";
	String targetViewSampleDetails = request.getContextPath() + "/user/viewSample.do?reqCode=displayAllDetails";
%>
<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />
<%-------------------------%>


<%-- To not show a empty table when no samples exists --%>	
<logic:empty name="analyseSampleForm" property="listInfo">
           <h4>No&nbsp;samples&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>

<logic:notEmpty name="analyseSampleForm" property="listInfo">
<layout:grid cols="1"  borderSpacing="10">
	<%-- Page --%>
	
	<layout:skin/>
	<layout:panel key="Samples" align="left" styleClass="PANEL">
	<layout:pager maxPageItems="<%=ispyb.common.util.Constants.MAX_PAG_ITEMS%>">
	
	<layout:collection 	name="analyseSampleForm" 
							property="listInfo"
							styleClass="LIST">
	
			<layout:collectionItem title="Name" property="name" sortable="true" href="<%=targetViewSampleDetails%>" paramId="blSampleId" paramProperty="blSampleId"  />
			<layout:collectionItem title="Code" property="code" sortable="true"/>
			<layout:collectionItem title="Protein" property="acronym" sortable="true"/>
			<layout:collectionItem title="SpaceGroup" property="spaceGroup" sortable="false"/>
			<layout:collectionItem title="Comments" property="comments" sortable="false"/>
			<layout:collectionItem title="Sample<br>status" property="blSampleStatus" sortable="true"/>
			<layout:collectionItem title="Completion<br>stage" property="completionStage" sortable="false"/>
			<layout:collectionItem title="Structure<br>stage" property="structureStage" sortable="false"/>
			<layout:collectionItem title="Publication<br>stage" property="publicationStage" sortable="false"/>
			<layout:collectionItem title="Publication<br>comments" property="publicationComments" sortable="false"/>
			<layout:collectionItem title="Edit<br>sample" href="<%=targetSample%>"
		                           paramId="blSampleId" paramProperty="blSampleId">
				<img src="<%=request.getContextPath()%>/images/Edit_16x16_01.png" border="0"/>
			</layout:collectionItem>
			<layout:collectionItem title="Data<br>Collections" href="<%=targetDataCollections%>" paramId="name" paramProperty="name">
				View
			</layout:collectionItem>
		</layout:collection>
	
	</layout:pager>
	</layout:panel>
</layout:grid>
</logic:notEmpty>

<%-- to display messages --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

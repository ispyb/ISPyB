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

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<bean:define name="viewSampleForm" property="freeSampleList" id="myList" type="java.util.List"	scope="request"/>	

		<logic:notEmpty name ="viewSampleForm" property = "freeSampleList">

		<layout:panel key="Select samples for processing" align="center" styleClass="PANEL">

			 <layout:grid cols="1"  borderSpacing="10">
				<layout:form action="/user/prepareExpSample.do" reqCode="updateSampleStatus">

				<layout:collection 	name="myList" styleClass="LIST"
									id="currInfo"
									selectProperty="blSampleId"  selectName="selectedSamplesList" selectType="checkbox">

					<layout:collectionItem title="" >&nbsp;
					</layout:collectionItem>
					<layout:collectionItem title="Protein" property="crystalVO.proteinVO.acronym"/>
		
					<layout:collectionItem title="Sample<br>name" property="name" />
					<layout:collectionItem title="Sample<br>code" property="code" />
					<layout:collectionItem title="Sample<br>status" property="blSampleStatus" />

				</layout:collection>

				<layout:row>										
								
								<layout:submit reqCode="updateSampleStatus"><layout:message key="set to processing"/></layout:submit>
								<layout:submit reqCode="cleanSampleStatus"><layout:message key="clean status"/></layout:submit>
				</layout:row>

				</layout:form>	
			</layout:grid>	

		</layout:panel>				

		</logic:notEmpty>
		
		<logic:empty name ="viewSampleForm" property = "freeSampleList">
			<h4>No&nbsp;Samples&nbsp;have&nbsp;been&nbsp;found</h4>
		</logic:empty>
		

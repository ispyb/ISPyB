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

<%@page import="java.util.Calendar,java.util.GregorianCalendar"%>
<%@page import="ispyb.common.util.Constants"%>

<layout:skin includeScript="true" />

<br>
<layout:panel key="Search data collection" align="left" styleClass="PANEL">
<layout:form action="/user/viewDataCollection.do" reqCode="displayForCustomQuery">
	
	<layout:grid cols="1" styleClass="SEARCH_GRID">	
		<layout:column>
					<layout:text key="Sample name:" 	property="sampleName" 	styleClass="FIELD" 	mode="E,E,E"/>
					<layout:text key="Protein acronym:" 	property="proteinAcronym" 	styleClass="FIELD" 	mode="E,E,E"/>
					<layout:text key="Beamline:" 	property="beamlineName" 	styleClass="FIELD" 	mode="E,E,E"/>
					<layout:space/>
					
					<layout:date key="Experiment date between DD-MM-YYYY:" property="experimentDateStart" styleClass="FIELD" mode="E,E,E"
						patternKey="<%=Constants.DATE_FORMAT%>" 
						startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
						endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>	
					<layout:date key="and DD-MM-YYYY:" property="experimentDateEnd" styleClass="FIELD" mode="E,E,E"
						patternKey="<%=Constants.DATE_FORMAT%>" 
						startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
						endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>
						
					<layout:space/>
					<layout:text key="Min number of images:" 	property="minNumberOfImages" 	styleClass="FIELD" 	mode="E,E,E"/>
					<layout:text key="Max number of images:" 	property="maxNumberOfImages" 	styleClass="FIELD" 	mode="E,E,E"/>
						
					<layout:space/>
					<layout:text key="Image prefix:" 	property="imagePrefix" 	styleClass="FIELD" 	mode="E,E,E"/>
					
					<layout:space/>
					<layout:text key="Max Data Collections retrieved:" 	property="maxRecords" value="<%=Constants.MAX_RETRIEVED_DATACOLLECTIONS%>" styleClass="FIELD" 	mode="E,E,E"/>
					
		</layout:column>		
			
		<layout:space/>
		<layout:row styleClass="rowAlignCenter">
			<layout:reset/>
			<layout:submit><layout:message key="Search"/></layout:submit>
		</layout:row>
		<TR><TD></TD><TD class="FIELD">You may use the * character to do a search with an incomplete Name.</FONT></TD></TR>
	</layout:grid>
	
</layout:form>	
</layout:panel>


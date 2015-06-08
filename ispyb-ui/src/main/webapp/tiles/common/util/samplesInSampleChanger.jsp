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

<%-- Samples in sample changer -----------------------------------------------------------------------------%>
<logic:present name="breadCrumbsForm" property="samplesInSampleChanger">

    <layout:form action="/user/clearSamplesInSampleChanger.do" reqCode="clearSampleChanger" onsubmit="return window.confirm('Do you really want to delete all samples in the sample changer?');">
        <layout:grid cols="2" styleClass="SEARCH_GRID">	
		  <layout:column>		
			  <layout:text name="breadCrumbsForm" key="List:" property="samplesInSampleChanger" 	styleClass="PANEL_BREAD_CRUMBS_FIELD"	mode="I,I,I"/>
          </layout:column>
		  <layout:column>
		  	<layout:submit reqCode="clearSampleChanger">
	        	<layout:message key="Clear all samples in sample changer"/>
	        </layout:submit>
	      </layout:column>			
	    </layout:grid>
    </layout:form>

</logic:present>

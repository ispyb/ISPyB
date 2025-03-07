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

<%
	String targetUpdateDB 	= request.getContextPath() + "/updateDB.do?reqCode=updateProposal"; 
%>

<h3>
<br>
If some protein acronyms are missing in the list, you can get them by <a href="<%=targetUpdateDB%>" >updating ISPyB database</a> (this may take a few minutes).
</h3>
</br>
<h3><font color= '#ff6622'>
Note that the new/duplicated samplesheets need to be approved by Safety in the User Portal prior to be imported in ISPyB.
</font></h3>
<br>
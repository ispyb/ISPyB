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

<%@page import="ispyb.common.util.Constants"%>


<br/>
<br/>
<br/>


<table align="center" width="100%" height="100%">
    <tr>
        <td align="center" valign="top">  
<html:form action="/userProposalTypeChoose.do">

			<table width="400" height="100%">
				<TR>
					<TD>Experiment technique:</TD>
					
			
						<TD><html:select property="value">							
							<html:option value="MX">MX</html:option>
							<html:option value="BX">BioSAXS</html:option>							
						</html:select></TD>
						<TD><html:submit property="submit" value="Submit"/></TD>
					
					
				</TR>
			 </table>
</html:form>
        </td>
    </tr>
</table>

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
<%@ page import="ispyb.common.util.Constants"%>

<jsp:useBean id="adminVar" class="ispyb.server.common.util.AdminUtils" scope="page" />

<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
	<!-- bar with image -->
	<bean:define scope="page" id="serverName" value="<%=request.getServerName()%>" 	type="java.lang.String"/>
	
	   <TR bgColor=#6888a8>
	     <TD class= "ispybgen"><IMG src="<%=request.getContextPath()%>/images/ISPyB_Logo_02.gif" align=left></TD>
	     <TD class= "ispybgen" align=right>
	     	<H2 style="text-decoration: blink;"><font color="FFFFFF">
	        <%=adminVar.getValue("warningMessage")%>&nbsp;
	
	        <!-- WARNING MESSAGES ON LOCALHOST AND PYTEST -->
			<logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.test\")%>"  scope="page">
			 		TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal>
			<logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.localhost\")%>"  scope="page">
			 		LOCAL TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal>
	     
	     	</font></H2></TD>
	   </TR>
</c:if>
<c:if test="${SITE_ATTRIBUTE eq 'ALBA'}">
	<!-- bar with image -->
    <bean:define scope="page" id="serverName" value="<%=request.getLocalName()%>" 	type="java.lang.String"/>
	   <TR bgColor=#6888a8>
	     <TD class= "ispybgen"><IMG src="<%=request.getContextPath()%>/images/ISPyB_Logo_02.gif" align=left></TD>
	     <TD class= "ispybgen" align=right>
	     	<H2 style="text-decoration: blink;"><font color="FFFFFF">
	        <%=adminVar.getValue("warningMessage")%>&nbsp;
	        <!-- WARNING MESSAGES ON LOCALHOST AND PYTEST -->
			<!-- <logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.test\")%>"  scope="page">
			 		TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal>
			<logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.localhost\")%>"  scope="page">
			 		LOCAL TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal> -->

	     	</font></H2></TD>
	   </TR>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
		<%-- bar with image --%>
	    <tr bgColor="#6888a8">
	    	<td>
	    		<table width="100%" cellpadding="0" cellspacing="0">
	    		<tr>
	    			<td><img src="<%=request.getContextPath()%>/images/ISPyB_Logo.gif" align="left" /></td>
	
		<%-- If the user is logged in --%>
	
					<logic:present name="<%=Constants.ROLES%>">
		  			<td align="right">
						<font color="#FFFF00"><%=adminVar.getValue("warningMessage")%></font>
	
						<bean:define id="roles" name="<%=Constants.ROLES%>" scope="session" type="java.util.ArrayList"/>
						<bean:define id="numberOfRoles" value="<%=(new Integer(roles.size())).toString()%>" />
						<logic:greaterThan name="numberOfRoles" value="1">
					
						<table align="right">
						<tr>
							<td style="font-weight:normal;color:#ffffff">Role:&nbsp;</td>
							<td style="font-weight:normal;color:#ffffff">
								<html:form action="/rolesChoose.do" styleClass="searchform">
								<html:select property="value" onchange="rolesChooseForm.submit()" styleClass="searchbox">					
	 							<%-- <html:option value="0"> -- Select a new Role -- </html:option>   --%>
								<html:options collection="roles" property="id" labelProperty="name"/>
								</html:select>
								</html:form>
							</td>	
						</tr>
						</table>
						
						</logic:greaterThan>
					</td>
					</logic:present>
	
	    		</tr>
	    		</table>
	    	</td>
	    </tr>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
	<!-- bar with image -->
	<bean:define scope="page" id="serverName" value="<%=request.getServerName()%>" 	type="java.lang.String"/>
	
	   <TR bgColor=#6888a8>
	     <TD class= "ispybgen"><IMG src="<%=request.getContextPath()%>/images/ISPyB_Logo_02.gif" align=left></TD>
	     <TD class= "ispybgen" align=right>
	     	<H2 style="text-decoration: blink;"><font color="FFFFFF">
	        <%=adminVar.getValue("warningMessage")%>&nbsp;
	
	        <!-- WARNING MESSAGES ON LOCALHOST AND PYTEST -->
			<logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.test\")%>"  scope="page">
			 		TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal>
			<logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.localhost\")%>"  scope="page">
			 		LOCAL TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal>
	     
	     	</font></H2></TD>
	   </TR>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
	<!-- bar with image -->
	<bean:define scope="page" id="serverName" value="<%=request.getServerName()%>" 	type="java.lang.String"/>
	
	   <TR bgColor=#ffffff>
	     <TD class= "ispybgen" valign="bottom" align="left"><IMG src="<%=request.getContextPath()%>/images/ISPyB_Logo_02.gif"/><IMG src="<%=request.getContextPath()%>/images/max_iv_logo.png"/></TD>
	     <TD class= "ispybgen" align="right">
	     	<H2 style="text-decoration: blink;"><font color="8DC73F">
	        <%=adminVar.getValue("warningMessage")%>&nbsp;
	
	        <!-- WARNING MESSAGES ON LOCALHOST AND PYTEST -->
			<logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.test\")%>"  scope="page">
			 		TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal>
			<logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.localhost\")%>"  scope="page">
			 		LOCAL TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal>
	     
	     	</font></H2></TD>
	   </TR>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
	<!-- bar with image -->
	<bean:define scope="page" id="serverName" value="<%=request.getServerName()%>" 	type="java.lang.String"/>
	
	   <TR bgColor=#6888a8>
	     <TD class= "ispybgen"><IMG src="<%=request.getContextPath()%>/images/ISPyB_Logo_02.gif" align=left></TD>
	     <TD class= "ispybgen" align=right>
	     	<H2 style="text-decoration: blink;"><font color="FFFFFF">
	        <%=adminVar.getValue("warningMessage")%>&nbsp;
	
	        <!-- WARNING MESSAGES ON LOCALHOST AND PYTEST -->
			<logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.test\")%>"  scope="page">
			 		TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal>
			<logic:equal name="serverName" value="<%=Constants.getProperty(\"ISPyB.server.name.localhost\")%>"  scope="page">
			 		LOCAL TEST SERVER on <%=adminVar.getValue("databaseName")%>&nbsp;
			</logic:equal>
	     
	     	</font></H2></TD>
	   </TR>
</c:if>
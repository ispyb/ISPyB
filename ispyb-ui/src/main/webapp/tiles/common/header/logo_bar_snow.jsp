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

<!--  Christmas animation !  -->
	<script type="text/javascript">		
		window.onload = initSnow;
	</script>

	<!-- bar with image -->
	<!-- bar with image -->
	<bean:define scope="page" id="serverName" value="<%=request.getServerName()%>" 	type="java.lang.String"/>
	
	   <TR bgColor=#6888a8> 
	   	 <TD class= "ispybgen" valign="bottom" align="left">
	   	 	<table>
	   	 		<tr>	   
	     			<TD class= "ispybgen" style="width:70px"><IMG src="<%=request.getContextPath()%>/images/ISPyB_Logo_03.gif" align="left"></TD>
	     			<TD class= "ispybgen" ><canvas id='canvas' style='height:45px;' ></canvas></TD>     
	     		</tr>
	     	</table>
	     </TD>
	    
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

<script type="text/javascript">		
		window.onload = function(){
						var repeat = 1000;

						//canvas init
						var canvas = document.getElementById("canvas");
						var ctx = canvas.getContext("2d");
	
						//canvas dimensions
						var W = window.innerWidth - 500;
						var H = 45;
						canvas.width = W;
						canvas.height = H;
	
						//snowflake particles
						var mp = 25; //max particles
						var particles = [];
						for(var i = 0; i < mp; i++)
						{
							particles.push({
								x: Math.random()*W, //x-coordinate
								y: Math.random()*H, //y-coordinate
								r: Math.random()*4+1, //radius
								d: Math.random()*mp //density
							})
						}
	
						//Lets draw the flakes
						function draw()
						{
							repeat = repeat - 2;
							var ctext = "ISPyB".split("").join(String.fromCharCode(8202));
							if (repeat > 0){
								ctx.clearRect(0, 0, W, H);
					
								ctx.font = "bold 30px Verdana";								
								ctx.fillText(ctext, 5, 25);
								ctx.font = "bold 10px Arial ";
								ctx.fillText("Information System for Protein crystallographY Beamlines", 5, 42);

								ctx.fillStyle = "rgba(255, 255, 255, 0.8)";
								ctx.beginPath();
								for(var i = 0; i < mp; i++)
								{
									var p = particles[i];
									ctx.moveTo(p.x, p.y);
									ctx.arc(p.x, p.y, p.r, 0, Math.PI*2, true);
								}
								ctx.fill();
								update();
							}
							else{
								ctx.clearRect(0, 0, W, H);
								ctx.font = "bold 30px Verdana ";
								ctx.fillText(ctext, 5, 25);

								ctx.font = "bold 10px Arial ";
								ctx.fillText("Information System for Protein crystallographY Beamlines", 5, 42);
								clearInterval(interval);
							}			
						}
	
						//Function to move the snowflakes
						//angle will be an ongoing incremental flag. Sin and Cos functions will be applied to it to create vertical and horizontal movements of the flakes
						var angle = 0;
						function update()
						{
							angle += 0.01;
							for(var i = 0; i < mp; i++)
							{
								var p = particles[i];
								//Updating X and Y coordinates
								//We will add 1 to the cos function to prevent negative values which will lead flakes to move upwards
								//Every particle has its own density which can be used to make the downward movement different for each flake
								//Lets make it more random by adding in the radius
								p.y += Math.cos(angle+p.d) + 1 + p.r/2;
								p.x += Math.sin(angle) * 2;
			
								//Sending flakes back from the top when it exits
								//Lets make it a bit more organic and let flakes enter from the left and right also.
								if(p.x > W+5 || p.x < -5 || p.y > H)
								{
									if(i%6 > 0) //66.67% of the flakes
									{
										particles[i] = {x: Math.random()*W, y: -10, r: p.r, d: p.d};
									}
									else
									{
										//If the flake is exitting from the right
										if(Math.sin(angle) > 0)
										{
											//Enter from the left
											particles[i] = {x: -5, y: Math.random()*H, r: p.r, d: p.d};
										}
										else
										{
											//Enter from the right
											particles[i] = {x: W+5, y: Math.random()*H, r: p.r, d: p.d};
										}
									}
								}
							}
						}	
						//animation loop
						interval = setInterval(draw, 33);
};
</script>
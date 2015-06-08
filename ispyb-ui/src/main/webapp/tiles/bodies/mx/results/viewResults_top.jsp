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

<%@ include file="../../mx/mx_css_include.jsp" %> 		
<%@ include file="../../mx/mx_javascript_include.jsp" %> 



<script TYPE="text/javascript">
	function setAnomalous(targetResults, isAnomalous){
		window.location.href=targetResults+ "&anomalous="+isAnomalous;
		return;
	}
</script> 

<% 
	String target 					= request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImage";
	String targetGetDataFromFile	= request.getContextPath() + "/user/viewResults.do?reqCode=getDataFromFile";
	//String targetScreening 			= request.getContextPath() + "/user/viewScreening.do?reqCode=display";
	String targetLogFile			= request.getContextPath() + "/user/viewResults.do?reqCode=displayProgramLogFiles";
	String targetImageDownload 		= request.getContextPath() + "/user/imageDownload.do?reqCode=getImageJpgThumb";
	String targetImageDownloadFile 	= request.getContextPath() + "/user/imageDownload.do?reqCode=getImageJpgFromFile";
	String targetImageDownloadFileFullSize 	= request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImageFromFile";
	int	nbSnapshotDisplayed			= 0;
	
	String targetScreeningStrategy 	= request.getContextPath() + "/user/viewScreeningStrategy.do?reqCode=display";
	
	    
%>

	
<%------------- DNA Strategies ---------------------%>


<logic:present name="viewResultsForm"  property="listStrategiesWedgeInfo"> 
<logic:notEmpty name="viewResultsForm"  property="listStrategiesWedgeInfo">
	<h4>Strategies Wedge</h4>
		
			<%-- ----- Rows ----- --%>
			<logic:iterate name="viewResultsForm" property="<%= \"listStrategiesWedgeInfo\" %>" id="strategyWedgeInfo" indexId="strategyWedgeIndex" type="ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO">
				 <table cellspacing="1" cellpadding="1" border="0" width="99%" align="CENTER" class="LIST" >
				<%-- ----- Headers ----- --%>
				<tr>
					<th class="LIST">Wedge number</th>
					<th class="LIST">Resolution<br>&Aring;</th>
					<th class="LIST">Completeness</th>
					<th class="LIST">Multiplicity</th>
					<th class="LIST">Total dose</th>
					<th class="LIST">Number of<br>images</th>
					<th class="LIST">Phi<br>&deg;</th>
					<th class="LIST">Kappa<br>&deg;</th>
					<th class="LIST">Wavelength</th>
					<th class="LIST">Comments</th>
				</tr>
				 <tr class="LIST" id="strategyWedgeRow_<%=strategyWedgeIndex%>" > 
				<%--	<tr class="LIST" id="strategyWedgeRow_<%=strategyWedgeIndex%>" >--%>
					<bean:define name="viewResultsForm" 	scope="request" id="viewResultsForm" 	type="ispyb.client.mx.results.ViewResultsForm"/>
					<td><bean:write name="strategyWedgeInfo" property="wedgeNumber"/></td>
					<td><bean:write name="strategyWedgeInfo" property="resolution"/></td>
					<td><bean:write name="strategyWedgeInfo" property="completeness"/></td>
					<td><bean:write name="strategyWedgeInfo" property="multiplicity"/></td>
					<td><bean:write name="strategyWedgeInfo" property="doseTotal"/></td>
					<td><bean:write name="strategyWedgeInfo" property="numberOfImages"/></td>
					<td><bean:write name="strategyWedgeInfo" property="phi"/></td>
					<td><bean:write name="strategyWedgeInfo" property="kappa"/></td>
					<td><bean:write name="strategyWedgeInfo" property="wavelength"/></td>
					<td><bean:write name="strategyWedgeInfo" property="comments"/></td>
				</tr>
				</table>
				<br/>
				<logic:present name="viewResultsForm"  property="listStrategiesSubWedgeInfoAll">
				<logic:notEmpty name="viewResultsForm"  property="listStrategiesSubWedgeInfoAll">
				<logic:notEmpty name="viewResultsForm"  property="<%= \"listStrategiesSubWedgeInfoAll[\"+strategyWedgeIndex+\"]\" %>">
					<table cellspacing="1" cellpadding="1" border="0" width="99%" align="CENTER" class="LIST" id="strategySubWedgeTable_<%=strategyWedgeIndex%>"  >
					<%-- ----- Headers ----- --%>
					<tr>
						<th class="LIST">Sub Wedge<br>number</th>
						<th class="LIST">Rotation axis<br>&deg;</th>
						<th class="LIST">Axis start<br>&deg;</th>
						<th class="LIST">Axis end<br>&deg;</th>
						<th class="LIST">Exposure time<br>s</th>
						<th class="LIST">Transmission<br>&#37;</th>
						<th class="LIST">Oscillation range</th>
						<th class="LIST">Completeness</th>
						<th class="LIST">Multiplicity</th>
						<th class="LIST">Total dose</th>
						<th class="LIST">Number of<br>images</th>
						<th class="LIST">Comments</th>
					</tr>
					<%-- ----- Rows ----- --%>
				 	<logic:iterate name="viewResultsForm" property="<%= \"listStrategiesSubWedgeInfoAll[\"+strategyWedgeIndex+\"]\" %>" id="strategySubWedgeInfo" indexId="strategySubWedgeIndex" type="ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO">
						<tr class="LIST"  >
							<td><bean:write name="strategySubWedgeInfo" property="subWedgeNumber"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="rotationAxis"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="axisStart"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="axisEnd"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="exposureTime"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="transmission"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="oscillationRange"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="completeness"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="multiplicity"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="doseTotal"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="numberOfImages"/></td>
							<td><bean:write name="strategySubWedgeInfo" property="comments"/></td>
						</tr>
					</logic:iterate>
				</table>
				<br/>
				</logic:notEmpty>
				
			</logic:notEmpty>
			</logic:present>
			
			</logic:iterate>
		
</logic:notEmpty>
</logic:present>
<%--/Strategy Wedge--%>

<%------------- Crystal Snapshots ---------------------%>
<layout:grid cols="1"  borderSpacing="10">
<layout:column>
	<logic:present name="viewResultsForm"  property="listSnapshots">
	<logic:notEmpty name="viewResultsForm"  property="listSnapshots">
	<layout:tabs width="650">
		<layout:tab key="Crystal Snapshots" width="150">
				<logic:iterate id="sn" name="viewResultsForm"  property="listSnapshots">
					<logic:equal name="sn" property="filePresent" value="true">
						<bean:define id="imgSrc" name="sn"  property="fileLocation" type="java.lang.String"/>
							<%nbSnapshotDisplayed ++;%>
							<a href="<%= targetImageDownloadFileFullSize + '&' + Constants.IMG_SNAPSHOT_URL_PARAM + '=' + imgSrc %>">
								<layout:img width="316" height="197" src="<%= targetImageDownloadFile + \"&\" + Constants.IMG_SNAPSHOT_URL_PARAM + \"=\" + imgSrc %>" border="0" alt="Crystal Snapshot"/>
							</a>
							<% if (nbSnapshotDisplayed==2) {out.write("<br>");} %>
					</logic:equal>
				</logic:iterate>
			<layout:text key='Expected Snapshots location:'	name='viewResultsForm' property='expectedSnapshotPath'  styleClass='FIELD_DISPLAY' mode='I,I,I'/>
		</layout:tab>
	</layout:tabs>
	</logic:notEmpty>
	</logic:present>
</layout:column>
</layout:grid>

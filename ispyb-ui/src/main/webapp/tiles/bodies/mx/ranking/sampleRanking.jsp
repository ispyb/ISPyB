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

<% 
    String targetResults = request.getContextPath() + "/user/viewResults.do?reqCode=display";
    String targetExportPDF 					= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportRankAsPdf";
    String targetExportRTF 					= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportRankAsRtf";
    String exportPDFTitle 					= "View PDF report";
    String exportRTFTitle 					= "View DOC report";
%>

<script language="javascript"> 

	function setValue(myfield,value)
	{
		myfield.value = value;
		return false;
	}
	
</script> 

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- To display messages --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<layout:space/>

<%-- reports --%>
<layout:grid cols="1">
	<layout:row><%-- RTF report--%>
		<layout:link title="RTF report" href="<%= targetExportRTF %>" paramName="sampleRankingForm" paramProperty="sessionId" styleClass="FIELD">
			<img src="<%=request.getContextPath()%>/images/Word.png" border="0" onmouseover="return overlib('Export this table as DOC');" onmouseout="return nd();"/>
				<%= exportRTFTitle %>
		</layout:link>
				
		<%-- PDF report--%>
		<layout:link title="PDF report" href="<%= targetExportPDF %>" paramName="sampleRankingForm" paramProperty="sessionId" styleClass="FIELD">
			<img src="<%=request.getContextPath()%>/images/pdf.png" border="0" onmouseover="return overlib('Export this table as PDF');" onmouseout="return nd();"/>
				<%= exportPDFTitle %>
		</layout:link>
	</layout:row>
</layout:grid>

<%-- Empty table --%>
<logic:empty name="sampleRankingForm" property="sampleRankingValue">
           <h4>No&nbsp;Data Collection&nbsp;have&nbsp;been&nbsp;selected</h4>
</logic:empty>

<%-- Not empty table --%>
<logic:notEmpty name="sampleRankingForm" property="sampleRankingValue">
<html:form action="/user/sampleRanking">
	<html:hidden property="reqCode" value="update"/>
	<html:hidden property="actionName" value="none"/>
	<html:hidden name="sampleRankingForm" property="sortOrder" />

	<layout:grid cols="1" borderSpacing="10">
		
		
		<%-- Ranking table --%>
		<layout:column >
		<layout:form action="/user/sampleRanking.do" reqCode="rank">
		<layout:panel key="Sample Ranking (Rank - Value)" align="center" styleClass="PANEL">
		<TR><TD>
		
		
		<table cellspacing="1" cellpadding="1" border="0" width="99%" align="CENTER" class="LIST">
			
			<%-- Headers --%>
			<tr>
			
				<%-- Select sample checkbox --%>
				<th class="LIST">Select<br>Sample</th>
				
			
				<%-- Image Prefix --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnImagePrefix');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Image Prefix</td>
				</tr></tbody></table></th>
				
				<%-- Run number --%>
				<th class="LIST">Run No</th>
				
				<%-- Start Time --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnStartTime');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Start<br>time</td>
				</tr></tbody></table></th>
				
				<%-- Space Group --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnSpaceGroup');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Space<br>Group</td>
				</tr></tbody></table></th>
				
				<%-- Unit Cells --%>
				<th class="LIST">UC<br>a</th>
				<th class="LIST">UC<br>b</th>
				<th class="LIST">UC<br>c</th>
				
				<%-- Separator --%>
				<th class="LIST"></th>
				
				<%-- Ranking resolution --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnTheoreticalResolutionRank');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Ranking<br>resol.&nbsp;&#197;</td>
				</tr></tbody></table></th>
				
				<%-- Total exposure time --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnExposureTimeRank');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Exposure<br>time&nbsp;s</td>
				</tr></tbody></table></th>
				
				<%-- Mosaicity --%>	
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnMosaicityRank');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Mosaicity<br>&nbsp;&#176;</td>
				</tr></tbody></table></th>
				
				<%-- Number of spots indexed --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnNumberOfSpotsRank');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Number of<br>spots</td>
				</tr></tbody></table></th>
				
				<%-- Number of images --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnNumberOfImagesRank');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Number of<br>images</td>
				</tr></tbody></table></th>
				
				<%-- Total --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnTotalRank');" onmouseover="return overlib('Sort on column');" onmouseout="return nd();" type="image"></td>
					<td>Total</td>
				</tr></tbody></table></th>
			</tr>
			
			<%-- Weight and Action --%>
			<tr>
			<td class="LIST" colspan=8>
			
			<th class="LIST"></th>

			<td class="LIST" nowrap onmouseover="return overlib('Set weight for this criterion<br>(0: ignored 10: highest),<br>then click on Rank button');" onmouseout="return nd();">
        		Wt:&nbsp;
        		<html:select size="1" name="sampleRankingForm" property="weightRankingResolution" styleClass="FIELD_COMBO">
					<html:options collection="weightValueList" property="value" labelProperty="label" />
				</html:select>
			</td>
			<td class="LIST" nowrap onmouseover="return overlib('Set weight for this criterion<br>(0: ignored 10: highest),<br>then click on Rank button');" onmouseout="return nd();">
        		Wt:&nbsp;
        		<html:select size="1" name="sampleRankingForm" property="weightExposureTime" styleClass="FIELD_COMBO">
					<html:options collection="weightValueList" property="value" labelProperty="label" />
				</html:select>
			</td>
			<td class="LIST" nowrap onmouseover="return overlib('Set weight for this criterion<br>(0: ignored 10: highest),<br>then click on Rank button');" onmouseout="return nd();">
        		Wt:&nbsp;
        		<html:select size="1" name="sampleRankingForm" property="weightMosaicity" styleClass="FIELD_COMBO">
					<html:options collection="weightValueList" property="value" labelProperty="label" />
				</html:select>
			</td>
			<td class="LIST" nowrap onmouseover="return overlib('Set weight for this criterion<br>(0: ignored 10: highest),<br>then click on Rank button');" onmouseout="return nd();">
        		Wt:&nbsp;
        		<html:select size="1" name="sampleRankingForm" property="weightNumberOfSpots" styleClass="FIELD_COMBO">
					<html:options collection="weightValueList" property="value" labelProperty="label" />
				</html:select>
			</td>
			<td class="LIST" nowrap onmouseover="return overlib('Set weight for this criterion<br>(0: ignored 10: highest),<br>then click on Rank button');" onmouseout="return nd();">
        		Wt:&nbsp;
        		<html:select size="1" name="sampleRankingForm" property="weightNumberOfImages" styleClass="FIELD_COMBO">
					<html:options collection="weightValueList" property="value" labelProperty="label" />
				</html:select>
			</td>			
			
			<td class="LIST">
  			<div align=center>
    			<input class="FIELD_BUTTON" name="Action" type="submit" value="Rank" onclick="setValue(this.form.actionName,'Rank');" onmouseover="return overlib('Rank samples based on weighted criteria');" onmouseout="return nd();">
    		</div>
			</td>
			
			</tr>
	
			<%-- Rows --%>
		  <logic:iterate name="sampleRankingForm" property="sampleRankingValue" id="currInfo" indexId="index" type="ispyb.client.mx.ranking.SampleRankingVO">
						
	            <html:hidden name="sampleRankingForm" property="<%= \"idList[\" + index + \"]\" %>" value="<%=currInfo.getDataCollectionId().toString()%>" />         	
				
				<%-- Swap row color on imagePrefix change --%>
				<logic:notEqual name="currInfo" property="imagePrefix" value="${previousImagePrefix}" >
					<bean:define id="styleTDtmp" value="${styleTD1}"/>
					<bean:define id="styleTD1" value="${styleTD2}"/>
					<bean:define id="styleTD2" value="${styleTDtmp}"/>
				</logic:notEqual>	
				
				<tr class="LIST" onmouseover="this.className='LIST_OVER'" onmouseout="this.className='LIST'">

					<%-- Selection checkbox --%>
					<td> <html:checkbox property="<%=\"selectedItems[\" + currInfo.getDataCollectionId() + \"]\" %>" value="on"/> </td>
					
					
					<%-- Image Prefix 
					<td nowrap> --%>
					<td nowrap> 
					<div align=left>
					<html:link href="<%=targetResults + \"&dataCollectionId=\" + currInfo.getDataCollectionId()+\"&tabKeyResult=Characterisation results\"%>" styleClass="LIST">
						<bean:write name="currInfo" property="imagePrefix" />
					</html:link>
					</div>
					</td>
					
					<%-- Run number --%>
					<td><bean:write name="currInfo" property="dataCollectionNumber"/></td>
					
					<%-- Start Time --%>
					<td>
						<logic:empty name="breadCrumbsForm" property="selectedSession">
							<bean:write name="currInfo" property="startTime" format="dd-MM-yyyy HH:mm:ss"/>
						</logic:empty>
						<logic:notEmpty name="breadCrumbsForm" property="selectedSession">
							<bean:write name="currInfo" property="startTime" format="HH:mm:ss"/>
						</logic:notEmpty>
					</td>
					
					<%-- Space Group --%>
					<td><bean:write name="currInfo" property="spaceGroup"/></td>
					
					<%-- Unit Cells --%>
					<td><bean:write name="currInfo" property="unitCell_a" format="0"/></td>
					<td><bean:write name="currInfo" property="unitCell_b" format="0"/></td>
					<td><bean:write name="currInfo" property="unitCell_c" format="0"/></td>
					
					<%-- Separator --%>
					<th class="LIST"></th>
					
					<%-- Ranking resolution --%>
					<logic:notEqual name="currInfo" property="theoreticalResolutionRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="theoreticalResolutionRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="theoreticalResolutionValue"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
			      		<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="theoreticalResolutionRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="theoreticalResolutionValue" format="0.00"/></i></td>
            			</tr></tbody></table>
					</td>
			      	
			      	<%-- Total exposure time --%>	
			      	<logic:notEqual name="currInfo" property="totalExposureTimeRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="totalExposureTimeRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="totalExposureTimeValue"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
						<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="totalExposureTimeRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="totalExposureTimeValue" format="0.0"/></i></td>
            			</tr></tbody></table>
					</td>
					
					<%-- Mosaicity --%>	
					<logic:notEqual name="currInfo" property="mosaicityRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="mosaicityRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="mosaicityValue"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
						<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="mosaicityRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="mosaicityValue" format="0.00"/></i></td>
            			</tr></tbody></table>
					</td>
					
					<%-- Number of spots indexed --%>	
					<logic:notEqual name="currInfo" property="numberOfSpotsIndexedRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="numberOfSpotsIndexedRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="numberOfSpotsIndexedValue"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
						<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="numberOfSpotsIndexedRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="numberOfSpotsIndexedValue" format="0"/></i></td>
            			</tr></tbody></table>
					</td>
					
					<%-- Number of images --%>	
					<logic:notEqual name="currInfo" property="numberOfImagesRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="numberOfImagesRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="numberOfImagesValue"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
					  	<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="numberOfImagesRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="numberOfImagesValue" format="0"/></i></td>
            			</tr></tbody></table>
					</td>
		
					<%-- Total --%>
					<logic:notEqual name="currInfo" property="totalRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="totalRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="totalScore"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
						<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="totalRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="totalScore" format="0"/>&nbsp;%</i></td>
            			</tr></tbody></table>
					</td>
	
				</tr>
				
				<bean:define id="previousImagePrefix" value="<%=currInfo.getImagePrefix()%>"/>
			</logic:iterate>
		
		</table>
		
		<%-- Action buttons --%>
		<br>
		<div align=center>
	  		<%-- <input name="Action" type="submit" value="Distribution Chart" onclick="setValue(this.form.actionName,'DistributionChart');" onmouseover="return overlib('Draw distribution chart for all samples');" onmouseout="return nd();"> --%>
			<%-- <input name="Action" type="submit" value="Chart" onclick="setValue(this.form.actionName,'ComparisonChart');" onmouseover="return overlib('Draw comparison chart for selected samples');" onmouseout="return nd();"> --%>
			<input name="Action" type="submit" value="Chart" onclick="setValue(this.form.actionName,'ComparisonFlexChart');" onmouseover="return overlib('Draw comparison chart for selected samples');" onmouseout="return nd();"> 
     		<input name="Action" type="submit" value="Rank" onclick="setValue(this.form.actionName,'Rank');" onmouseover="return overlib('Rank samples based on weighted criteria');" onmouseout="return nd();">
			<%--  to be removed later if not used and get rid of all generated DNA files
			<input name="Action" type="submit" value="Export" onclick="setValue(this.form.actionName,'Select');" onmouseover="return overlib('Export selected samples for DNA data collect');" onmouseout="return nd();">
			--%>
		</div>
		</TD></TR>
		</layout:panel>
		</layout:form>
		</layout:column>
	</layout:grid>
</html:form>
</logic:notEmpty>

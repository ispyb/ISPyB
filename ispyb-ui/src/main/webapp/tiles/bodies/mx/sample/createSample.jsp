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
<%@page import="ispyb.common.util.Constants"%>

<%
	String 	targetCreateCrystal	= request.getContextPath() + "/user/createCrystal.do?reqCode=display";
	String 	createSampleTitle	= "Create a new Sample";
	String 	actionToPerform		= "/user/createSampleAction.do";
	String 	reqCode				= "display";
%>

<%--  if edit sample, change the title, the action and set isEditingSample  --%>
<%-- TODO clean later isEditing <logic:present name="breadCrumbsForm" 	property="selectedSample" scope="session">--%>
<bean:define id="isEditingSample" name="viewSampleForm" property="editMode"></bean:define>
<logic:equal name="isEditingSample" value="1">
	<%
	//isEditing				= true;
	createSampleTitle		= "Edit Selected Sample";
	actionToPerform			= "/user/editSampleAction.do";
	reqCode					= "editSample";
	%>
</logic:equal>	

<%-------------------------%>
<table>
	<tr>
		<td>
		<div id="panelElementTable" style="display: none;">
		      <map id="FrontPageMap0" name="FrontPageMap0">
		        <area shape="rect" coords="31, 18, 61, 50"
		        href="javascript:setAnomalousScatterer(1)" />
		        <area shape="rect" coords="525, 18, 554, 50"
		        href="javascript:setAnomalousScatterer(2)" />
		        <area shape="rect" coords="31, 50, 61, 83"
		        href="javascript:setAnomalousScatterer(3)" />
		        <area shape="rect" coords="61, 50, 90, 83"
		        href="javascript:setAnomalousScatterer(4)" />
		        <area shape="rect" coords="380, 50, 409, 83"
		        href="javascript:setAnomalousScatterer(5)" />
		        <area shape="rect" coords="410, 50, 438, 83"
		        href="javascript:setAnomalousScatterer(6)" />
		        <area shape="rect" coords="439, 50, 467, 83"
		        href="javascript:setAnomalousScatterer(7)" />
		        <area shape="rect" coords="468, 50, 496, 83"
		        href="javascript:setAnomalousScatterer(8)" />
		        <area shape="rect" coords="497, 50, 525, 83"
		        href="javascript:setAnomalousScatterer(9)" />
		        <area shape="rect" coords="526, 50, 554, 83"
		        href="javascript:setAnomalousScatterer(10)" />
		        <area shape="rect" coords="31, 83, 61, 116"
		        href="javascript:setAnomalousScatterer(11)" />
		        <area shape="rect" coords="61, 83, 90, 116"
		        href="javascript:setAnomalousScatterer(12)" />
		        <area shape="rect" coords="380, 83, 409, 116"
		        href="javascript:setAnomalousScatterer(13)" />
		        <area shape="rect" coords="410, 83, 438, 116"
		        href="javascript:setAnomalousScatterer(14)" />
		        <area shape="rect" coords="439, 83, 467, 116"
		        href="javascript:setAnomalousScatterer(15)" />
		        <area shape="rect" coords="468, 83, 496, 116"
		        href="javascript:setAnomalousScatterer(16)" />
		        <area shape="rect" coords="497, 83, 525, 116"
		        href="javascript:setAnomalousScatterer(17)" />
		        <area shape="rect" coords="526, 83, 554, 116"
		        href="javascript:setAnomalousScatterer(18)" />
		        <area shape="rect" coords="31, 116, 61, 149"
		        href="javascript:setAnomalousScatterer(19)" />
		        <area shape="rect" coords="61, 116, 90, 149"
		        href="javascript:setAnomalousScatterer(20)" />
		        <area shape="rect" coords="90, 116, 119, 149"
		        href="javascript:setAnomalousScatterer(21)" />
		        <area shape="rect" coords="119, 116, 148, 149"
		        href="javascript:setAnomalousScatterer(22)" />
		        <area shape="rect" coords="148, 116, 177, 149"
		        href="javascript:setAnomalousScatterer(23)" />
		        <area shape="rect" coords="177, 116, 206, 149"
		        href="javascript:setAnomalousScatterer(24)" />
		        <area shape="rect" coords="206, 116, 235, 149"
		        href="javascript:setAnomalousScatterer(25)" />
		        <area shape="rect" coords="235, 116, 264, 149"
		        href="javascript:setAnomalousScatterer(26)" />
		        <area shape="rect" coords="264, 116, 293, 149"
		        href="javascript:setAnomalousScatterer(27)" />
		        <area shape="rect" coords="293, 116, 322, 149"
		        href="javascript:setAnomalousScatterer(28)" />
		        <area shape="rect" coords="322, 116, 351, 149"
		        href="javascript:setAnomalousScatterer(29)" />
		        <area shape="rect" coords="351, 116, 380, 149"
		        href="javascript:setAnomalousScatterer(30)" />
		        <area shape="rect" coords="380, 116, 409, 149"
		        href="javascript:setAnomalousScatterer(31)" />
		        <area shape="rect" coords="409, 116, 438, 149"
		        href="javascript:setAnomalousScatterer(32)" />
		        <area shape="rect" coords="438, 116, 467, 149"
		        href="javascript:setAnomalousScatterer(33)" />
		        <area shape="rect" coords="467, 116, 496, 149"
		        href="javascript:setAnomalousScatterer(34)" />
		        <area shape="rect" coords="496, 116, 525, 149"
		        href="javascript:setAnomalousScatterer(35)" />
		        <area shape="rect" coords="525, 116, 554, 149"
		        href="javascript:setAnomalousScatterer(36)" />
		        <area shape="rect" coords="31, 149, 61, 182"
		        href="javascript:setAnomalousScatterer(37)" />
		        <area shape="rect" coords="61, 149, 90, 182"
		        href="javascript:setAnomalousScatterer(38)" />
		        <area shape="rect" coords="90, 149, 119, 182"
		        href="javascript:setAnomalousScatterer(39)" />
		        <area shape="rect" coords="119, 149, 148, 182"
		        href="javascript:setAnomalousScatterer(40)" />
		        <area shape="rect" coords="148, 149, 177, 182"
		        href="javascript:setAnomalousScatterer(41)" />
		        <area shape="rect" coords="177, 149, 206, 182"
		        href="javascript:setAnomalousScatterer(42)" />
		        <area shape="rect" coords="206, 149, 235, 182"
		        href="javascript:setAnomalousScatterer(43)" />
		        <area shape="rect" coords="235, 149, 264, 182"
		        href="javascript:setAnomalousScatterer(44)" />
		        <area shape="rect" coords="264, 149, 293, 182"
		        href="javascript:setAnomalousScatterer(45)" />
		        <area shape="rect" coords="293, 149, 322, 182"
		        href="javascript:setAnomalousScatterer(46)" />
		        <area shape="rect" coords="322, 149, 351, 182"
		        href="javascript:setAnomalousScatterer(47)" />
		        <area shape="rect" coords="351, 149, 380, 182"
		        href="javascript:setAnomalousScatterer(48)" />
		        <area shape="rect" coords="380, 149, 409, 182"
		        href="javascript:setAnomalousScatterer(49)" />
		        <area shape="rect" coords="409, 149, 438, 182"
		        href="javascript:setAnomalousScatterer(50)" />
		        <area shape="rect" coords="438, 149, 467, 182"
		        href="javascript:setAnomalousScatterer(51)" />
		        <area shape="rect" coords="467, 149, 496, 182"
		        href="javascript:setAnomalousScatterer(52)" />
		        <area shape="rect" coords="496, 149, 525, 182"
		        href="javascript:setAnomalousScatterer(53)" />
		        <area shape="rect" coords="525, 149, 554, 182"
		        href="javascript:setAnomalousScatterer(54)" />
		        <area shape="rect" coords="31, 182, 61, 215"
		        href="javascript:setAnomalousScatterer(55)" />
		        <area shape="rect" coords="61, 182, 90, 215"
		        href="javascript:setAnomalousScatterer(56)" />
		        <area shape="rect" coords="90, 182, 119, 215"
		        href="javascript:setAnomalousScatterer(57)" />
		        <area shape="rect" coords="87, 276, 117, 310"
		        href="javascript:setAnomalousScatterer(58)" />
		        <area shape="rect" coords="117, 276, 146, 310"
		        href="javascript:setAnomalousScatterer(59)" />
		        <area shape="rect" coords="146, 276, 175, 310"
		        href="javascript:setAnomalousScatterer(60)" />
		        <area shape="rect" coords="175, 276, 204, 310"
		        href="javascript:setAnomalousScatterer(61)" />
		        <area shape="rect" coords="204, 276, 233, 310"
		        href="javascript:setAnomalousScatterer(62)" />
		        <area shape="rect" coords="233, 276, 262, 310"
		        href="javascript:setAnomalousScatterer(63)" />
		        <area shape="rect" coords="262, 276, 291, 310"
		        href="javascript:setAnomalousScatterer(64)" />
		        <area shape="rect" coords="291, 276, 320, 310"
		        href="javascript:setAnomalousScatterer(65)" />
		        <area shape="rect" coords="320, 276, 349, 310"
		        href="javascript:setAnomalousScatterer(66)" />
		        <area shape="rect" coords="349, 276, 378, 310"
		        href="javascript:setAnomalousScatterer(67)" />
		        <area shape="rect" coords="378, 276, 407, 310"
		        href="javascript:setAnomalousScatterer(68)" />
		        <area shape="rect" coords="407, 276, 436, 310"
		        href="javascript:setAnomalousScatterer(69)" />
		        <area shape="rect" coords="436, 276, 465, 310"
		        href="javascript:setAnomalousScatterer(70)" />
		        <area shape="rect" coords="465, 276, 494, 310"
		        href="javascript:setAnomalousScatterer(71)" />
		        <area shape="rect" coords="119, 182, 148, 215"
		        href="javascript:setAnomalousScatterer(72)" />
		        <area shape="rect" coords="148, 182, 177, 215"
		        href="javascript:setAnomalousScatterer(73)" />
		        <area shape="rect" coords="177, 182, 206, 215"
		        href="javascript:setAnomalousScatterer(74)" />
		        <area shape="rect" coords="206, 182, 235, 215"
		        href="javascript:setAnomalousScatterer(75)" />
		        <area shape="rect" coords="235, 182, 264, 215"
		        href="javascript:setAnomalousScatterer(76)" />
		        <area shape="rect" coords="264, 182, 293, 215"
		        href="javascript:setAnomalousScatterer(77)" />
		        <area shape="rect" coords="293, 182, 322, 215"
		        href="javascript:setAnomalousScatterer(78)" />
		        <area shape="rect" coords="322, 182, 351, 215"
		        href="javascript:setAnomalousScatterer(79)" />
		        <area shape="rect" coords="351, 182, 380, 215"
		        href="javascript:setAnomalousScatterer(80)" />
		        <area shape="rect" coords="380, 182, 409, 215"
		        href="javascript:setAnomalousScatterer(81)" />
		        <area shape="rect" coords="409, 182, 438, 215"
		        href="javascript:setAnomalousScatterer(82)" />
		        <area shape="rect" coords="438, 182, 467, 215"
		        href="javascript:setAnomalousScatterer(83)" />
		        <area shape="rect" coords="467, 182, 496, 215"
		        href="javascript:setAnomalousScatterer(84)" />
		        <area shape="rect" coords="496, 182, 525, 215"
		        href="javascript:setAnomalousScatterer(85)" />
		        <area shape="rect" coords="525, 182, 554, 215"
		        href="javascript:setAnomalousScatterer(86)" />
		        <area shape="rect" coords="31, 215, 61, 248"
		        href="javascript:setAnomalousScatterer(87)" />
		        <area shape="rect" coords="61, 215, 90, 248"
		        href="javascript:setAnomalousScatterer(88)" />
		        <area shape="rect" coords="90, 215, 119, 248"
		        href="javascript:setAnomalousScatterer(89)" />
		        <area shape="rect" coords="87, 310, 117, 342"
		        href="javascript:setAnomalousScatterer(90)" />
		        <area shape="rect" coords="117, 310, 146, 342"
		        href="javascript:setAnomalousScatterer(91)" />
		        <area shape="rect" coords="146, 310, 175, 342"
		        href="javascript:setAnomalousScatterer(92)" />
		        <area shape="rect" coords="175, 310, 204, 342"
		        href="javascript:setAnomalousScatterer(93)" />
		        <area shape="rect" coords="204, 310, 233, 342"
		        href="javascript:setAnomalousScatterer(94)" />
		        <area shape="rect" coords="233, 310, 262, 342"
		        href="javascript:setAnomalousScatterer(95)" />
		        <area shape="rect" coords="262, 310, 291, 342"
		        href="javascript:setAnomalousScatterer(96)" />
		        <area shape="rect" coords="291, 310, 320, 342"
		        href="javascript:setAnomalousScatterer(97)" />
		        <area shape="rect" coords="320, 310, 349, 342"
		        href="javascript:setAnomalousScatterer(98)" />
		        <area shape="rect" coords="349, 310, 378, 342"
		        href="javascript:setAnomalousScatterer(99)" />
		        <area shape="rect" coords="378, 310, 407, 342"
		        href="javascript:setAnomalousScatterer(100)" />
		        <area shape="rect" coords="407, 310, 436, 342"
		        href="javascript:setAnomalousScatterer(101)" />
		        <area shape="rect" coords="436, 310, 465, 342"
		        href="javascript:setAnomalousScatterer(102)" />
		        <area shape="rect" coords="465, 310, 494, 342"
		        href="javascript:setAnomalousScatterer(103)" />
		        <area shape="rect" coords="119, 215, 148, 248"
		        href="javascript:setAnomalousScatterer(104)" />
		        <area shape="rect" coords="148, 215, 177, 248"
		        href="javascript:setAnomalousScatterer(105)" />
		        <area shape="rect" coords="177, 215, 206, 248"
		        href="javascript:setAnomalousScatterer(106)" />
		        <area shape="rect" coords="206, 215, 235, 248"
		        href="javascript:setAnomalousScatterer(107)" />
		        <area shape="rect" coords="235, 215, 264, 248"
		        href="javascript:setAnomalousScatterer(108)" />
		        <area shape="rect" coords="264, 215, 293, 248"
		        href="javascript:setAnomalousScatterer(109)" />
		        <area shape="rect" coords="293, 215, 322, 248"
		        href="javascript:setAnomalousScatterer(110)" />
		        <area shape="rect" coords="100, 10, 370, 77"
		        href="javascript:setAnomalousScatterer(p_law)" />
		        <area shape="rect" coords="11, 273, 85, 308"
		        href="javascript:setAnomalousScatterer(r_metals)" />
		        <area shape="rect" coords="11, 310, 86, 341"
		        href="javascript:setAnomalousScatterer(r_metals)" />
		        <area shape="rect" coords="14, 412, 122, 442"
		        href="javascript:setAnomalousScatterer(n_metals)" />
		        <area shape="rect" coords="15, 449, 125, 477"
		        href="javascript:setAnomalousScatterer(a_metals)" />
		        <area shape="rect" coords="155, 414, 290, 440"
		        href="javascript:setAnomalousScatterer(t_metals)" />
		        <area shape="rect" coords="313, 413, 446, 441"
		        href="javascript:setAnomalousScatterer(r_metals)" />
		        <area shape="rect" coords="313, 449, 428, 477"
		        href="javascript:setAnomalousScatterer(metals)" />
		        <area shape="rect" coords="463, 415, 558, 439"
		        href="javascript:setAnomalousScatterer(halogens)" />
		        <area shape="rect" coords="464, 451, 579, 475"
		        href="javascript:setAnomalousScatterer(n_gases)" />
		        <area coords="0,0,10000,10000" href="javascript:setAnomalousScatterer(p_law)" />
		      </map><img src="<%=request.getContextPath()%>/images/periodic_table.gif"
		      alt="Clickable Map of the Periodic Table" border="0" width="580" height="355"
		      usemap="#frontpagemap0" /><!--webbot bot="ImageMap" i-checksum="55988"endspan -->
		    </div>
		</td>
		<td>
<a name="CreateSample"></a>
<layout:grid cols="2"  borderSpacing="1">
<layout:tabs width="600">
	<layout:tab key="<%=createSampleTitle%>" width="100">
	<%----------------------------------------------------- form to create sample --------------------------------------------------------------------%>
		<layout:grid cols="2"  borderSpacing="1">
				<layout:form action="<%=actionToPerform%>" reqCode="<%=reqCode%>">	
		<%-- beamline sample --%>
					<layout:grid cols="1" styleClass="SEARCH_GRID">	
					<layout:row>
		<%-- 1st row --%>					
					<layout:column>

		<%-- Protein and Crystal form space group--%>
						<layout:row>
								<layout:select 	key="Select a Protein Acronym<br>with a SpaceGroup (1)" 	property="theCrystalId" size="1" styleClass="FIELD"	mode="E,E,E" isRequired="true" onchange="storeValue(this)"> 
									<layout:option key="-- Please select an acronym+crystal --" value="0" />
									<layout:optionsCollection property="listCrystal" label="designation" value="crystalId"/>
								</layout:select>
							<layout:message key="or" styleClass="FIELD"/>
							<layout:link href="<%=targetCreateCrystal%>" styleClass="FIELD">
							<logic:equal name="viewSampleForm" property="isAllowedToCreateProtein" value="true" >
								<b>Create&nbsp;new&nbsp;protein/crystal&nbsp;form</b>
							</logic:equal>
							<logic:notEqual name="viewSampleForm" property="isAllowedToCreateProtein" value="true" >
								<b>Create&nbsp;new&nbsp;crystal&nbsp;form</b>
							</logic:notEqual>
							</layout:link>
							<layout:space>
							</layout:space>
						</layout:row>						
						<layout:space/>					
			
			<%-- Sample --%>
						<layout:text key="Sample Name:<br>&nbsp;(the image name will be : 'acronym'-'sample name'_'run number'_'image number')<br>&nbsp;Authorized characters are a-z, A-Z, 0-9, underscore and dash." property="info.name" 	styleClass="FIELD"	mode="E,E,E" isRequired="true" 	onchange="storeValue(this)"/>
						<layout:space/>
						<layout:text key="Datamatrix Code:<br>&nbsp;(needed to use sample changer)<br>&nbsp;Authorized characters are a-z, A-Z, 0-9." 	property="info.code" 	styleClass="OPTIONAL"	mode="E,E,E"					onchange="storeValue(this)">
						<img src="<%=request.getContextPath()%>/images/DMCode.gif" border=0>
						</layout:text>		
						<layout:space/>
						
						
			<%-- location in container --%>
			            <layout:text key="Location in container:<br>&nbsp;(position on cane or position in puck)" 	property="info.location"		styleClass="OPTIONAL"	mode="E,E,E"	size="7"	onchange="storeValue(this)"/>
			            <layout:space/>
						<layout:text key="Holder Length in mm:<br>&nbsp;(typically between 15mm and 27mm, Spine default is 22mm)" 	property="info.holderLength"	styleClass="FIELD"	mode="E,E,E"	size="4"	onchange="storeValue(this)"/>
						<layout:space/>
						<layout:select 	key="Loop Type" 	property="info.loopType" 	styleClass="OPTIONAL"	onchange="storeValue(this)">
							<layout:option value="Nylon"/>
							<layout:option value="Litho Loop"/>
							<layout:option value="Micromount"/>
							<layout:option value="Other"/>
						</layout:select>
						<layout:space/><layout:space/>
						<layout:textarea key="Comments" 	property="info.comments"	styleClass="OPTIONAL"	mode="E,E,E" rows="3"/>	
						<layout:space/>
			<%-- status --%>
						<logic:equal name="isEditingSample" value="1">
							<layout:text key="Sample Status :" property="info.blSampleStatus" 	styleClass="FIELD"	mode="E,E,E" />
							<layout:space/>  
						</logic:equal>
						<layout:space/>											   
						<%--	
						<layout:checkbox key="Set in sample changer (if no usable Data Matrix code)" value="1" property="info.isInSampleChanger" styleClass="FIELD" mode="E,E,E"/>	
						--%>
			</layout:column>
						
			<layout:column>
						
			<%--------------------------------------------------- DM in Sample Changer ---------------------------------------------------%>
			<bean:define name="viewSampleForm" property="listDMCodes" 	id="myListDMCodes" 	type="java.util.List" toScope="request"/>  
			<bean:define name="viewSampleForm" property="listBeamlines" 	id="mylistBeamlines" 	type="java.util.List" toScope="request"/>  
				<logic:notEmpty name="mylistBeamlines">
					<layout:column>
						<layout:tabs width="200">
								<layout:tab key="DM codes in SC" width="100">
									<layout:message key="DM codes inside Sample Changer on:" styleClass="FIELD"/><layout:write property="codeInScDate"/>
												
										<layout:radios 	key="Select a Beamline:" property="selectedBeamline" styleClass="FIELD" onclick="this.form.submit()">
												<logic:iterate name="viewSampleForm" property="listBeamlines" id="item" type="java.lang.String">
													<logic:notEmpty name="item">
														<layout:option  value="<%=item%>" key="<%=item%>" />
													</logic:notEmpty>
												</logic:iterate>
										</layout:radios>
															
									<logic:notEmpty name="myListDMCodes">
									<layout:row>
										<layout:grid cols="2" styleClass="SEARCH_GRID">	
											<layout:column>
												<img src='<%=request.getContextPath()%>/images/DMCode.gif' border='0' />
											</layout:column>	
											<layout:column>
												<layout:button value="<< Copy" onclick='copySelectedDM(this.form.code)'/>
											</layout:column>
										</layout:grid>
										
										<layout:select 	key="" 	property="code" styleClass="FIELD"   size="10" isRequired="false" >
											<layout:optionsCollection property="listDMCodes" value="datamatrixCode" label="datamatrixCodeFull"/>
										</layout:select>
									</layout:row>
									</logic:notEmpty>									
									
								</layout:tab>
						</layout:tabs>	
				</layout:column>
				</logic:notEmpty>
			<%-----------------------------------------------------------------------------------------------------------------------------%>		
			</layout:column>						
			</layout:row>		
			<%---------------------------------------------- Diffraction Plan -------------------------------------------------------------------%>
			<bean:define name="viewSampleForm" property="nb_max_experiment_kind_detailsConstants" id="NB_MAX_EXPERIMENT_KIND_DETAILS"/>
			<logic:equal name="isEditingSample" value="1">
			<layout:grid align="left">
				<layout:tabs width="600" styleClass="PANEL">
			
					<layout:tab key="Diffraction Plan" width="100">
						<%-- Diffraction Plan --%>
							<layout:grid cols="2" styleClass="SEARCH_GRID">	
								<layout:column>
									<layout:text key="Id" 				property="difPlanInfo.diffractionPlanId" 	styleClass="FIELD"	mode="H,H,H"/>
									<layout:text key="Already Observed Resolution (A)" 	property="difPlanInfo.observedResolution" 	styleClass="FIELD"	mode="E,E,E"/>
									<layout:text key="Minimal Resolution (A)" 		property="difPlanInfo.minimalResolution" 	styleClass="FIELD"	mode="E,E,E" onmouseover="return overlib('The resolution at which......');" onmouseout="return nd();"/>
									<layout:text key="Required Resolution (A)" 		property="difPlanInfo.requiredResolution" 	styleClass="FIELD"	mode="E,E,E"/>
									<layout:text key="Exposure Time (s)" 		property="difPlanInfo.exposureTime" 		styleClass="FIELD"	mode="E,E,E"/>
									<layout:text key="Oscillation Range (degres)" 		property="difPlanInfo.oscillationRange" 	styleClass="FIELD"	mode="E,E,E"/>
									<layout:text key="Anomalous Scatterer" 		property="difPlanInfo.anomalousScatterer" 	styleClass="FIELD"	mode="E,E,E" onclick="showhide('panelElementTable', 'block');"/>
									<layout:text key="Preferred Beam Size Diameter (mm)" 	property="difPlanInfo.preferredBeamDiameter" 	styleClass="FIELD"	mode="E,E,E" />
									<layout:text key="Number of positions" 	property="difPlanInfo.numberOfPositions" 	styleClass="FIELD"	mode="E,E,E" />
									<layout:text key="Radiation sensitivity" 	property="difPlanInfo.radiationSensitivity" 	styleClass="FIELD"	mode="E,E,E" />
									<layout:text key="Aimed completeness" 	property="difPlanInfo.aimedCompleteness" 	styleClass="FIELD"	mode="E,E,E" />
									<layout:text key="Aimed multiplicity" 	property="difPlanInfo.aimedMultiplicity" 	styleClass="FIELD"	mode="E,E,E" />

									<layout:textarea key="Comments" 	property="difPlanInfo.comments"				styleClass="FIELD"	mode="E,E,E" rows="3"/>	
								</layout:column>
									
								<layout:column>
									<layout:tabs  styleClass="PANEL">
										<layout:tab key="Experiment Details" width="200">
										<layout:select 	key="Type of experiment" 	property="difPlanInfo.experimentKind" size="1" styleClass="FIELD"	mode="E,E,E" isRequired="true" onchange="storeValue(this);ExperimentKindChanged(this)"> 
											<layout:options property="listExperimentKind"/>
										</layout:select>
										<layout:text key="Number of Wavelengths" 	property="numberOfWaveLength" 			styleClass="FIELD"	mode="E,E,E" onchange="nbWavelengthChanged(this, ${NB_MAX_EXPERIMENT_KIND_DETAILS})"/>
										<logic:iterate name="viewSampleForm" property="listExperimentKindDetails" id="item" indexId="index">
										<layout:row>	
											<layout:select 	key="${index+1}" 	property="experimentKindDetail_${index+1}.dataCollectionType" size="1" 	styleClass="FIELD"	mode="E,E,E" onchange="storeValue(this)"> 
												<layout:options property="listDataCollectionType"/>
											</layout:select>
											<layout:select 	key="" 	property="experimentKindDetail_${index+1}.dataCollectionKind" size="1" 		styleClass="FIELD"	mode="E,E,E" onchange="storeValue(this);DataCollectionKindChanged(this,${index+1})"> 
												<layout:options property="listDataCollectionKind"/>
											</layout:select>
											<layout:text key="" 	property="experimentKindDetail_${index+1}.wedgeValue" 				styleClass="FIELD"	mode="E,E,E"/>
										</layout:row>
										</logic:iterate>
										</layout:tab>
									</layout:tabs>
								</layout:column>
								
							</layout:grid>
							
							<%-- Xml Diffraction --%>
							<%--
							<layout:row>
								<logic:notEmpty name = "viewSampleForm" property="difPlanInfo.xmlDocument">
									<bean:define name="viewSampleForm" property="difPlanInfo.xmlDocument" id="xmlDoc" 	/>
									<layout:panel key="Xml Diffraction Plan" align="center" styleClass="PANEL" >
											<layout:textarea key=""	name="xmlDoc" property="xml" mode="R,R,R" styleClass="XML"/>
									</layout:panel>
								</logic:notEmpty>
							</layout:row>
							--%>					
					</layout:tab>
				
					<layout:tab key="Assign same Diffraction plan to ...">
						<%-- Assign Diffraction Plan --%>
							<bean:define name="viewSampleForm" property="listInfo" id="myList" type="java.util.List"/>
							<logic:empty name="myList">
							           <h4>No&nbsp;samples&nbsp;have&nbsp;been&nbsp;found</h4>
							</logic:empty>
			
							<logic:notEmpty name="myList">				
								<layout:grid cols="1"  borderSpacing="10">
									<layout:panel key="Samples" align="left" styleClass="PANEL">
									
										<layout:collection 	name="myList" styleClass="LIST" id="currInfo"
													selectProperty="blSampleId"  selectName="selectedSamplesList" selectType="checkbox"	>
								
												<layout:collectionItem title="" >&nbsp;</layout:collectionItem>
												<layout:collectionItem title="Protein" property="crystalVO.proteinVO.acronym" sortable="false"/>	
												<layout:collectionItem title="Sample<br>name" property="name" sortable="false" paramId="blSampleId" paramProperty="blSampleId"/>
												<layout:collectionItem title="Space Group" 			property="crystalVO.spaceGroup" 					sortable="false"/>																																					
												<layout:collectionItem title="Cell a" 				property="crystalVO.cellA" 						sortable="false"/>																																					
												<layout:collectionItem title="Cell b" 				property="crystalVO.cellB" 						sortable="false"/>																																					
												<layout:collectionItem title="Cell c" 				property="crystalVO.cellC" 						sortable="false"/>																																					
												<layout:collectionItem title="Cell alpha" 			property="crystalVO.cellAlpha" 					sortable="false"/>																																					
												<layout:collectionItem title="Cell beta" 			property="crystalVO.cellBeta" 					sortable="false"/>																																					
												<layout:collectionItem title="Cell gamma"			property="crystalVO.cellGamma" 					sortable="false"/>		
												
										</layout:collection>
								
									</layout:panel>
								</layout:grid>
							</logic:notEmpty>
					</layout:tab>
					
				</layout:tabs>
				<layout:space/>
			</layout:grid>
			</logic:equal>
					<layout:space/>
					<layout:row>
						<layout:cell width="200">&nbsp;</layout:cell>
						<layout:cell>
							<layout:reset/>&nbsp;&nbsp;&nbsp;					
							<layout:submit reqCode="save" onmouseover="return overlib('Save the sample data');" onmouseout="return nd();"><layout:message key="Save"/></layout:submit>
						</layout:cell>
					</layout:row>
					<layout:space/>   
					<layout:space/>   
					
					<layout:row>
						<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
							<layout:message key="(1) If you do not see your proteins, create a new one using the <b>Create new protein/crystal form</b>." styleClass="FIELD"/>
						</c:if>
						<c:if test="${SITE_ATTRIBUTE ne 'MAXIV'}">
							<layout:message key="(1) If you do not see your proteins, update ISPyB database: <b>Update Database</b> menu." styleClass="FIELD"/>
							
							
						</c:if>
						</layout:row>	
						
				</layout:grid>				


			</layout:form>	
		</layout:grid>					
	</layout:tab>	
	
	<%------------ Retrieve existing Sample ------------%>		
	<bean:define name="viewSampleForm" property="freeSampleList" id="myList" type="java.util.List"	toScope="request"/>	

	<logic:notEmpty name ="breadCrumbsForm" property = "selectedContainer">
	<logic:notEmpty name ="viewSampleForm" property = "freeSampleList">
	
	<layout:tab key="Select existing Sample" width="100">
		<layout:grid cols="1"  borderSpacing="10">
			<layout:form action="/user/createSampleAction.do" reqCode="saveExisting">
				<layout:collection 	name="myList" styleClass="LIST" id="currInfo" selectProperty="blSampleId"  selectName="selectedSamplesList" selectType="checkbox">
				<layout:collectionItem title="Protein" property="crystalVO.proteinVO.acronym"/>
					<layout:collectionItem title="Sample<br>name" property="name" />
					<layout:collectionItem title="Smp<br>code" property="code" />
				</layout:collection>
				<layout:row>	
					<layout:reset/>
						<layout:submit reqCode="saveExisting">
						<layout:message key="Select"/></layout:submit>
				</layout:row>
			</layout:form>	
		</layout:grid>					
	</layout:tab>
	</logic:notEmpty>
	</logic:notEmpty>	
</layout:tabs>

</layout:grid>

<layout:space/>

</td>	
	</tr>
</table>

<%-----------------   JAVASCRIPT    ------------------%>

<script language="JavaScript" type="text/javascript">
<!--
function ExperimentKindChanged(element)
{
experimentKind = element.value;
if (experimentKind == 'SAD' || experimentKind == 'SAD - Inverse Beam')
	{
	document.getElementsByName("numberOfWaveLength")[0].value=1;
	document.getElementsByName("numberOfWaveLength")[0].disabled = true;
	nbWavelengthChanged(document.getElementsByName("numberOfWaveLength")[0],99);
	}
if (experimentKind == 'MAD' || experimentKind == 'MAD - Inverse Beam')
	{
	document.getElementsByName("numberOfWaveLength")[0].value=1;
	document.getElementsByName("numberOfWaveLength")[0].disabled = false;
	nbWavelengthChanged(document.getElementsByName("numberOfWaveLength")[0],99);
	}
if (experimentKind == 'OSC')
	{
	document.getElementsByName("numberOfWaveLength")[0].value=0;
	document.getElementsByName("numberOfWaveLength")[0].disabled = true;
	nbWavelengthChanged(document.getElementsByName("numberOfWaveLength")[0],99);
	}
}
function nbWavelengthChanged(element, max)
	{
	if (element.value > max)
		{
		alert('Maximum number of Wavelength is : ' + max);
		element.value = max;
		}
	
	experimentKind = document.getElementsByName("difPlanInfo.experimentKind")[0].value;
	displayWedgeValue = false;
	if (experimentKind == 'SAD - Inverse Beam' || experimentKind == 'MAD - Inverse Beam')
		{
		displayWedgeValue = true;
		}
	else
		{
		for (e=1;e<=3;e++)
			{document.getElementsByName('experimentKindDetail_' + e + '.wedgeValue')[0].disabled = true;}
		}

	var e=1;
	for (;e<=element.value;e++)
		{
		if (displayWedgeValue) 
			{
			document.getElementsByName('experimentKindDetail_' + e + '.wedgeValue')[0].disabled = false;
			}
		document.getElementsByName('experimentKindDetail_' + e + '.dataCollectionType')[0].disabled = false;
		document.getElementsByName('experimentKindDetail_' + e + '.dataCollectionKind')[0].disabled = false;
		}
	e=element.value;
	for (e++;e<=max;e++)
		{
		document.getElementsByName('experimentKindDetail_' + e + '.wedgeValue')[0].disabled = true;
		document.getElementsByName('experimentKindDetail_' + e + '.dataCollectionType')[0].disabled = true;
		document.getElementsByName('experimentKindDetail_' + e + '.dataCollectionKind')[0].disabled = true;
		}
	}
function DataCollectionKindChanged(element, wavelengthNb)
{
dataCollectionKind = element.value;
displayWedgeValue = false;

if (dataCollectionKind == 'Inverse')
	{
	displayWedgeValue = true;
	}
document.getElementsByName('experimentKindDetail_' + wavelengthNb + '.wedgeValue')[0].disabled = !displayWedgeValue;
}



function copySelectedDM(selectList)
	{
	var a 			= document.getElementsByName("info.code")[0];
	var b 			= document.getElementsByName("info.location")[0];
	var i_startDM		= selectList.value.lastIndexOf("#") + 1;
	var dmCode 		= selectList.value.substr(i_startDM, selectList.value.length);
	var containerLocation	= selectList.value.substr(i_startDM - 2, 1);
	
	a.value = dmCode;
	b.value = containerLocation;
	}
function setAnomalousScatterer(element)
	{
	var elementName;
	switch (element)
		{
		case	1	:	elementName=	"H";	break;
		case	2	:	elementName=	"He";	break;
		case	3	:	elementName=	"Li";	break;
		case	4	:	elementName=	"Be";	break;
		case	5	:	elementName=	"B";	break;
		case	6	:	elementName=	"C";	break;
		case	7	:	elementName=	"N";	break;
		case	8	:	elementName=	"O";	break;
		case	9	:	elementName=	"F";	break;
		case	10	:	elementName=	"Ne";	break;
		case	11	:	elementName=	"Na";	break;
		case	12	:	elementName=	"Mg";	break;
		case	13	:	elementName=	"Al";	break;
		case	14	:	elementName=	"Si";	break;
		case	15	:	elementName=	"P";	break;
		case	16	:	elementName=	"Si";	break;
		case	17	:	elementName=	"Cl";	break;
		case	18	:	elementName=	"Ar";	break;
		case	19	:	elementName=	"K";	break;
		case	20	:	elementName=	"Ca";	break;
		case	21	:	elementName=	"Sc";	break;
		case	22	:	elementName=	"Ti";	break;
		case	23	:	elementName=	"V";	break;
		case	24	:	elementName=	"Cr";	break;
		case	25	:	elementName=	"Mn";	break;
		case	26	:	elementName=	"Fe";	break;
		case	27	:	elementName=	"Co";	break;
		case	28	:	elementName=	"Ni";	break;
		case	29	:	elementName=	"Cu";	break;
		case	30	:	elementName=	"Zn";	break;
		case	31	:	elementName=	"Ga";	break;
		case	32	:	elementName=	"Ge";	break;
		case	33	:	elementName=	"As";	break;
		case	34	:	elementName=	"Se";	break;
		case	35	:	elementName=	"Br";	break;
		case	36	:	elementName=	"Kr";	break;
		case	37	:	elementName=	"Rb";	break;
		case	38	:	elementName=	"Sr";	break;
		case	39	:	elementName=	"Y";	break;
		case	40	:	elementName=	"Zr";	break;
		case	41	:	elementName=	"Nb";	break;
		case	42	:	elementName=	"Mo";	break;
		case	43	:	elementName=	"Tc";	break;
		case	44	:	elementName=	"Ru";	break;
		case	45	:	elementName=	"Rh";	break;
		case	46	:	elementName=	"Pd";	break;
		case	47	:	elementName=	"Ag";	break;
		case	48	:	elementName=	"Cd";	break;
		case	49	:	elementName=	"In";	break;
		case	50	:	elementName=	"Sn";	break;
		case	51	:	elementName=	"Sb";	break;
		case	52	:	elementName=	"Te";	break;
		case	53	:	elementName=	"In";	break;
		case	54	:	elementName=	"Xe";	break;
		case	55	:	elementName=	"Cs";	break;
		case	56	:	elementName=	"Ba";	break;
		case	57	:	elementName=	"La";	break;
		case	58	:	elementName=	"Ce";	break;
		case	59	:	elementName=	"Pr";	break;
		case	60	:	elementName=	"Nd";	break;
		case	61	:	elementName=	"Pm";	break;
		case	62	:	elementName=	"Sm";	break;
		case	63	:	elementName=	"Eu";	break;
		case	64	:	elementName=	"Gd";	break;
		case	65	:	elementName=	"Tb";	break;
		case	66	:	elementName=	"Dy";	break;
		case	67	:	elementName=	"Ho";	break;
		case	68	:	elementName=	"Er";	break;
		case	69	:	elementName=	"Tm";	break;
		case	70	:	elementName=	"Yb";	break;
		case	71	:	elementName=	"Lu";	break;
		case	72	:	elementName=	"Hf";	break;
		case	73	:	elementName=	"Ta";	break;
		case	74	:	elementName=	"W";	break;
		case	75	:	elementName=	"Re";	break;
		case	76	:	elementName=	"Os";	break;
		case	77	:	elementName=	"Ir";	break;
		case	78	:	elementName=	"Pt";	break;
		case	79	:	elementName=	"Au";	break;
		case	80	:	elementName=	"Hg";	break;
		case	81	:	elementName=	"TI";	break;
		case	82	:	elementName=	"Pb";	break;
		case	83	:	elementName=	"Bi";	break;
		case	84	:	elementName=	"Po";	break;
		case	85	:	elementName=	"At";	break;
		case	86	:	elementName=	"Rn";	break;
		case	87	:	elementName=	"Fr";	break;
		case	88	:	elementName=	"Ra";	break;
		case	89	:	elementName=	"Ac";	break;
		case	90	:	elementName=	"Th";	break;
		case	91	:	elementName=	"Pa";	break;
		case	92	:	elementName=	"U";	break;
		case	93	:	elementName=	"Np";	break;
		case	94	:	elementName=	"Pu";	break;
		case	95	:	elementName=	"Am";	break;
		case	96	:	elementName=	"Cm";	break;
		case	97	:	elementName=	"Bk";	break;
		case	98	:	elementName=	"Cr";	break;
		case	99	:	elementName=	"Es";	break;
		case	100	:	elementName=	"Fm";	break;
		case	101	:	elementName=	"Md";	break;
		case	102	:	elementName=	"No";	break;
		case	103	:	elementName=	"Lr";	break;
		case	104	:	elementName=	"Rf";	break;
		case	105	:	elementName=	"Ha";	break;
		case	106	:	elementName=	"106";	break;
		case	107	:	elementName=	"107";	break;
		case	108	:	elementName=	"108";	break;
		case	109	:	elementName=	"109";	break;
		case	110	:	elementName=	"110";	break;
		}
	document.getElementsByName("difPlanInfo.anomalousScatterer")[0].value = elementName;
	showhide('panelElementTable', 'none');
	}
	
function showhide(layer_ref, state) {
if (document.all) { //IS IE 4 or 5 (or 6 beta)
eval( "document.all." + layer_ref + ".style.display = state");
}
if (document.layers) { //IS NETSCAPE 4 or below
document.layers[layer_ref].display = state;
}
if (document.getElementById &&!document.all) {
hza = document.getElementById(layer_ref);
hza.style.display = state;
}
}
//-->
</script>


<logic:notEqual name="isEditingSample" value="1">
	<script>
	// ------ Retrieves values ------
	var elementNames = new Array('info.holderLength', 'info.loopType');
	
	for(n=0;n<elementNames.length;n++)
		{
		document.getElementsByName(elementNames[n])[0].value = getCookie(elementNames[n]);
		}
	</script>
</logic:notEqual>

<script>
	// ------ Retrieves values ------
	var nbWavelength;
	nbWavelength = document.getElementsByName('numberOfWaveLength')[0].value;
	nbWavelengthChanged(document.getElementsByName('numberOfWaveLength')[0],99);
</script>

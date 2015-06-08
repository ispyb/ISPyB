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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv"
	style="position: absolute; visibility: hidden; z-index: 1000;"></div>


<%
	String targetDeleteLigand = request.getContextPath()
			+ "/user/createComplexAction.do?reqCode=deleteLigand";
	String targetDeleteDomain = request.getContextPath()
			+ "/user/createComplexAction.do?reqCode=deleteDomain";
%>

<%-- Create Complex --%>
<layout:panel key="Create complex" align="left" styleClass="PANEL">
	<layout:form action="/user/createComplexAction.do" reqCode="save">
		<layout:row>
			<layout:column>
				<layout:panel key="Complex" width="250px" align="left"
					styleClass="PANEL">
					<layout:text key="Name" property="complex.name" styleClass="FIELD" mode="E,E,I" />
					
					<layout:select 	key="Type" property="complex.type" styleClass="FIELD" mode="E,E,I">
						<layout:option value="Sample"/>
						<layout:option value="Buffer"/>							
					</layout:select>
					
					<layout:select key="Protein Acronym" property="proteinId" styleClass="COMBO" mode="E,E,I" isRequired="true">
						<bean:define name="ViewComplexForm" property="listOfProteins" id="myProteinList" type="java.util.List" />
						<layout:options collection="myProteinList" property="proteinId" labelProperty="acronym" />
					</layout:select>
				</layout:panel>
			</layout:column>
			<layout:column>
				<layout:panel key="Comment" width="250px" align="left"
					styleClass="PANEL">
					<layout:textarea key="" rows="5" size="25"
						property="complex.comment" styleClass="FIELD" mode="E,E,I" />
				</layout:panel>
			</layout:column>
		</layout:row>
		<layout:grid cols="1" styleClass="SEARCH_GRID">
			<layout:row>
				<layout:panel key="Ligands" width="250px" styleClass="PANEL">
					<layout:text key="Ligand" property="ligandInfo.ligand3VO.name" styleClass="FIELD"
						mode="E,E,I" />
					<layout:text key="Qty(mM)" property="ligandInfo.ligand3VO.quantity"
						styleClass="FIELD" mode="E,E,I" />
					<layout:panel align="center">
						<layout:row>
							<layout:submit reqCode="addLigand" mode="D,D,N"
								onclick="alertCourierAccountsDetails();">
								<layout:message key="Add" />
							</layout:submit>
							<layout:submit reqCode="clearLigand" mode="D,D,N"
								onclick="alertCourierAccountsDetails();">
								<layout:message key="Clear" />
							</layout:submit>
						</layout:row>
					</layout:panel>
					<logic:notEmpty name="ViewComplexForm" property="listOfLigands">
						<layout:panel align="center">
							<layout:row>
								<layout:collection name="ViewComplexForm"
									property="listOfLigands" styleClass="LIST" styleClass2="LIST2"
									width="200px" id="currLigands">
									<layout:collectionItem title="name" property="ligand3VO.name"
										paramId="ligand3VO.name" paramProperty="ligand3VO.name" />
									<layout:collectionItem title="quantity" property="ligand3VO.quantity"
										paramId="ligand3VO.quantity" paramProperty="ligand3VO.quantity" />
									<%-- Actions --%>
									<layout:collectionItem title="Remove" width="60px">
										<html:link href="<%=targetDeleteLigand %>"
											paramName="currLigands" paramId="infoId"
											paramProperty="infoId"
											onclick="return window.confirm('Do you really want to delete this ligand?');">
											<img src="<%=request.getContextPath()%>/images/cancel.png" border=0 onmouseover="return overlib('Delete the ligand');" onmouseout="return nd();">
										</html:link>
									</layout:collectionItem>
								</layout:collection>
							</layout:row>
						</layout:panel>
					</logic:notEmpty>
				</layout:panel>

				<layout:column>
					<layout:panel key="Deuteration" width="250px" align="left"
						styleClass="PANEL">
						<layout:select key="Protein" property="deuterationProteinId" styleClass="COMBO" mode="E,E,I" isRequired="false">
							<bean:define name="ViewComplexForm"
								property="listOfProteinsForDomain" id="myDeutProteinList"
								type="java.util.List" toScope="request" />
							<layout:options collection="myDeutProteinList" property="proteinId" labelProperty="acronym" />
						</layout:select>
						<layout:text key="Deut. (%)" property="domainInfo.domain3VO.deuterationPercentage" styleClass="FIELD" mode="E,E,I" />


						<layout:panel align="center">
							<layout:row>
								<layout:submit reqCode="addDomain" mode="D,D,N"
									onclick="alertCourierAccountsDetails();">
									<layout:message key="Add" />
								</layout:submit>
								<layout:submit reqCode="clearDomain" mode="D,D,N"
									onclick="alertCourierAccountsDetails();">
									<layout:message key="Clear" />
								</layout:submit>
							</layout:row>
						</layout:panel>

						<logic:notEmpty name="ViewComplexForm" property="listOfDomains">
							<layout:panel align="center">
								<layout:row>
									<layout:collection name="ViewComplexForm"
										property="listOfDomains" styleClass="LIST" styleClass2="LIST2"
										width="200px" id="currdomains">
										<layout:collectionItem title="Protein"
											property="domain3VO.protein3VO.acronym" paramId="domain3VO.protein3VO.acronym"
											paramProperty="domain3VO.protein3VO.acronym" />
										<layout:collectionItem title="Percentage"
											property="domain3VO.deuterationPercentage"
											paramId="domain3VO.deuterationPercentage"
											paramProperty="domain3VO.deuterationPercentage" />

										<%-- Actions --%>
										<layout:collectionItem title="Remove" width="60px">
											<html:link href="<%=targetDeleteDomain %>"
												paramName="currdomains" paramId="infoId"
												paramProperty="infoId"
												onclick="return window.confirm('Do you really want to delete this domain?');">
												<img src="<%=request.getContextPath()%>/images/cancel.png"
													border=0 onmouseover="return overlib('Delete the domain');"
													onmouseout="return nd();">
											</html:link>
										</layout:collectionItem>
									</layout:collection>
								</layout:row>
							</layout:panel>
						</logic:notEmpty>
					</layout:panel>

				</layout:column>
			</layout:row>
			<layout:row>
				<layout:submit reqCode="save" mode="D,D,N"
					onclick="alertCourierAccountsDetails();">
					<layout:message key="Save" />
				</layout:submit>
			</layout:row>
		</layout:grid>
	</layout:form>
</layout:panel>




<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />


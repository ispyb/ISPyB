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

<%@ include file="../../biosaxs/project/biosaxs_css_include.jsp"%>

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/bubble_tooltip.css" />
<layout:skin includeScript="true" />
<script
	src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv"
	style="position: absolute; visibility: hidden; z-index: 1000;"></div>


<script type="text/javascript">
	Ext.onReady(function() {
		function drawGrid(data, targetId) {
			var parsed = [];
			for (var i = 0; i < data.length;  i++){
				if (data[i].type == "MB"){
					data[i].type = "MX";
					parsed.push(JSON.parse(JSON.stringify(data[i])));
					data[i].type = "BX";
					parsed.push(JSON.parse(JSON.stringify(data[i])));
				}
				else{
					parsed.push(data[i]);
				}
			}

			var store = Ext.create('Ext.data.Store', {
				fields : [ {
					name : 'code'
				}, {
					name : 'number'
				}, {
					name : 'title'
				}, {
					name : 'type'
				} ],
				data : parsed
			});
			
			var grid = Ext.create('Ext.grid.Panel', {
				store : store,
				columns : [ {
					text : 'Proposal',
					flex : 0.2,
					sortable : false,
					dataIndex : 'code',
					renderer : function(a, b, record) {
						return record.raw.code + record.raw.number;
					}
				}, {
					text : 'Title',
					flex : 0.6,
					sortable : true,
					dataIndex : 'title'
				}, {
					text : 'Type',
					width : 75,
					sortable : true,
					dataIndex : 'type'
				},
				{
					renderer : function(a,b, record){ 
						var html = "<form action='/ispyb/user/userproposalchooseaction.do'>";
						html = html + "<input type='hidden' name='reqCode' value='goToProposal'>";
						html = html + "<input type='hidden' name='userProposalType' value='" + record.raw.type +"'>";
						html = html + "<input type='hidden' name='proposalId' value='" + record.raw.proposalId +"'>";
						html = html + "<input type='submit' value='GO'></input></form>";
						return html;
					},
					with : 50
				} ],
				height : 350,
				width : 600,
				title : 'Proposals',
				renderTo : targetId,
				viewConfig : {
					stripeRows : true
				}
			});
		};

		$.ajax({
			type : "POST",
			url : "/ispyb/user/userproposalchooseaction.do?reqCode=getProposalList",
			datatype : "text/json",
			success : function(json) {
				drawGrid(json, "mainPanel");
			},
			error : function(xhr) {
				console.log(xhr.responseText);
			}
		});

	});
</script>


<layout:skin includeScript="true" />
<script
	src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv"
	style="position: absolute; visibility: hidden; z-index: 1000;"></div>

<div id="mainPanel"></div>
<div id="editor-grid"></div>
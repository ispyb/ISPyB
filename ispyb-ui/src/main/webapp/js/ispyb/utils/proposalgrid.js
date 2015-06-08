function ProposalGrid(){
	
	
};


ProposalGrid.prototype.show = function(data, targetId){
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
		data : parsed,
		groupField : 'type'
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
			dataIndex : 'type',
			renderer : function(i, val, record){
				if (record.data.type == "BX"){
					return "SAXS";
				}
				return record.data.type;
			}
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
			width : 100
		} ],
		height : 350,
		width : 600,
		title : 'Proposals',
		renderTo : targetId,
		features : [
						{
    							id: 'type',
    							ftype: 'groupingsummary',
    							groupHeaderTpl: '{name}',
    							hideGroupedHeader: false,
    							enableGroupingMenu: true
						}
		],
		viewConfig : {
			stripeRows : true
		},
		listeners : {
			itemdblclick : function(dataview, record, item, e) {
				window.location.href = "/ispyb/user/userproposalchooseaction.do?reqCode=goToProposal&userProposalType=" + record.raw.type +"&proposalId=" + record.raw.proposalId;
			}
		}
	});
	
	
};
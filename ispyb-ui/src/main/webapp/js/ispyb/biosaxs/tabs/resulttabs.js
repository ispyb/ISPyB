function ResultTabs() {
}

ResultTabs.prototype.draw = function(targetId, data, macromoleculeId) {
	var panel = this.getPanel(targetId, data, macromoleculeId);
	return Ext.create('Ext.container.Container', {
		renderTo : targetId,
		items : [ BUI.getMacromoleculeHeader(macromoleculeId), panel ]
	});

};

ResultTabs.prototype._splitBySpecimen = function(data) {
	var splitted = {};

	for ( var i = 0; i < data.length; i++) {
		var row = data[i];
		if (splitted[row.macromoleculeId + "_" + row.bufferId] == null) {
			splitted[row.macromoleculeId + "_" + row.bufferId] = [];
		}
		splitted[row.macromoleculeId + "_" + row.bufferId].push(row);
	}
	return splitted;

};

ResultTabs.prototype._getTabTitle = function(key, data) {
	var macromoleculeId = key.split("_")[0];
	var bufferId = key.split("_")[1];

	return BIOSAXS.proposal.getMacromoleculeById(macromoleculeId).acronym + " + " + BIOSAXS.proposal.getBufferById(bufferId).acronym + "(" + data.length + ")";
};

ResultTabs.prototype.getPanel = function(targetId, data, macromoleculeId) {

	var dataFiltered = new AnalysisGrid({
		hideNulls : true,
		grouped : true,
		sorters : [ {
			property : 'quality',
			direction : 'DESC'
		} ]
	})._prepareData(data);

	var items = [ {
		tabConfig : {
			title : 'Analysis (' + dataFiltered.length + ')'
		},
		items : [ {
			xtype : 'container',
			layout : 'vbox',
			padding : 10,
			items : [ new AnalysisGrid({
				isScatteringPlotVisible : false
			}).getPanel(dataFiltered) ]
		} ]
	}, {
		tabConfig : {
			title : 'Concentration Effects'
		},
		items : [ new ResultSummaryForm().getPanel(data) ]
	} ];

	var splitted = this._splitBySpecimen(dataFiltered);
	for ( var key in splitted) {
		items.push({
			tabConfig : {
				title : this._getTabTitle(key, splitted[key])
			},
			items : [ new ResultSummaryForm().getPanel(splitted[key]) ]
		});
	}

	this.panel = Ext.createWidget('tabpanel', {
		style : {
			padding : 2
		},
		items : items
	});
	return this.panel;

};

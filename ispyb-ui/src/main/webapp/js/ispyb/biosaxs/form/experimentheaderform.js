/**
 * Shows the header for the experiments changing the color and parameters depending on experiment type
 * 
 */
function ExperimentHeaderForm(args) {
	this.id = BUI.id();
	this.backgroundColor = '#FFFFFF';
}

ExperimentHeaderForm.prototype.getHTMLSource = function(experiment) {
	var html = BUI.createFormLabel("Name :", experiment.json.name, 75, 400, this.backgroundColor);
	if (experiment.json.type == "HPLC") {
		if (experiment.getHPLCMacromolecule() != null){
			html = html + BUI.createFormLabel("Molecule :", experiment.getHPLCMacromolecule().acronym, 75, 400, this.backgroundColor);
		}
	}
	else{
		html = html + BUI.createFormLabel("Type :", experiment.json.type, 75, 400, this.backgroundColor);
	}
	
	html = html + BUI.createFormLabel("Date :", experiment.json.creationDate, 75, 400, this.backgroundColor);
	return html;
};

ExperimentHeaderForm.prototype.getHTMLDownload = function(experiment) {
	var bgcolor = "background-color:" + this.backgroundColor + ";";
	var html = "<div  style='height:25px;" + bgcolor + "'>";
	if ((experiment.json.type == "CALIBATION") || (experiment.json.type == "STATIC")) {
		html = html + "<img src='/ispyb/images/Edit_16x16_01.png' /><a style='" + bgcolor
				+ "'color:blue;' href='/ispyb/user/dataadapter.do?reqCode=getSourceFile&type=scattering&experimentId=" + experiment.experimentId
				+ "' title='File used by the BM control software'>  Download Source File</a></div>";
		html = html
				+ "<div style='"
				+ bgcolor
				+ "'height:25px;'>"
				+ "<img style='width:16px;height:16px' src='/ispyb/images/pdf.png' /><a style='color:blue;' href='/ispyb/user/dataadapter.do?reqCode=getReport&experimentId="
				+ experiment.experimentId + "&format=PDF&filename=" + experiment.name
				+ "&type=DATA_ACQUISITION'   title='Download report'>  Download Report</a></div>";
	}
	if (experiment.json.type == "TEMPLATE") {
		html = html
				+ "<img src='/ispyb/images/Edit_16x16_01.png' /><a ' href='/ispyb/user/dataadapter.do?reqCode=getTemplateSourceFile&experimentId="
				+ experiment.experimentId + "' style='color:blue;color:blue;cursor: hand; cursor: pointer;' >  Download Source File</a></div>";
	}

	if (experiment.json.type == "HPLC") {
		html = html + "<img src='/ispyb/images/Edit_16x16_01.png' /><a ' href='/ispyb/user/dataadapter.do?reqCode=getH5File&experimentId="
				+ experiment.experimentId + "' style='color:blue;color:blue;cursor: hand; cursor: pointer;' >  Download h5 File</a></div>";
	}

	return html;
};

ExperimentHeaderForm.prototype.getTopPanel = function(experiment) {
	return {
		xtype : 'container',
		layout : 'hbox',
		items : [ {
			margin : "0 0 0 0",
			width : 475,
			border : 0,
			html : this.getHTMLSource(experiment)
		}, {
			margin : "0 0 0 0",
			width : 475,
			border : 0,
			html : this.getHTMLDownload(experiment)
		} ]
	};
};

ExperimentHeaderForm.prototype.getButton = function(experiment) {
	var _this = this;
	return Ext.create('Ext.Button', {
		text : 'EDIT',
		minWidth : '100',
		margin : '10 0 0 30',
		handler : function() {
			var experimentWindow = new ExperimentWindow();
			experimentWindow.onSaved.attach(function(sender, data) {
				_this.experiment.json.name = data.name;
				_this.experiment.json.type = data.type;
				_this.experiment.json.comments = data.comments;
				_this.panel.remove(_this.panel.items.items[0]);
				_this.panel.remove(_this.panel.items.items[0]);
				_this.panel.insert(_this.getTopPanel(_this.experiment));
				_this.panel.insert(_this.getBottomPanel(_this.experiment));
			});
			experimentWindow.show(experiment);
		}
	});
};

ExperimentHeaderForm.prototype.getBottomPanel = function(experiment) {
	return {
		xtype : 'container',
		layout : 'hbox',
		margin : '10 0 0 0',
		items : [ this.getComments(experiment), this.getButton(experiment) ]
	};
};

ExperimentHeaderForm.prototype.getComments = function(experiment) {
	return {
		xtype : 'textareafield',
		labelStyle : 'font-weight: bold;',
		name : 'comments',
		fieldLabel : 'Comments',
		labelWidth : 70,
		height : 40,
		minWidth : '450',
		readOnly : true,
		value : experiment.json.comments
	};
};

ExperimentHeaderForm.prototype.getPanel = function(experiment) {
	this.experiment = experiment;

	if (experiment.json.type == 'CALIBRATION') {
		this.backgroundColor = '#EFFBFB';
	}
	if (experiment.json.type == 'TEMPLATE') {
		this.backgroundColor = '#E0F8E6';
	}

	this.panel = Ext.create('Ext.container.Container', {
		frame : false,
		layout : 'vbox',
		padding : 5,
		bodyPadding : '5 5 0 0',
		width : 890,
		margin : '0 0 10 0',
		height : 120,
		style : {
			borderColor : '#99bce8',
			borderStyle : 'solid',
			borderWidth : '1px',
			'background-color' : this.backgroundColor
		},
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 100
		},
		items : [ this.getTopPanel(experiment), this.getBottomPanel(experiment) ]
	});
	return this.panel;
};

ExperimentHeaderForm.prototype.input = function() {
	return {
		experiment : DATADOC.getExperiment_10()
	};
};

ExperimentHeaderForm.prototype.test = function(targetId) {
	var experimentHeaderForm = new ExperimentHeaderForm();
	var panel = experimentHeaderForm.getPanel(new Experiment(experimentHeaderForm.input().experiment));
	panel.render(targetId);

};

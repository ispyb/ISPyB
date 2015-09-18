/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ISPyB is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P.
 * Brenchereau, M. Bodin
 ******************************************************************************/

// AutoProc File Panel (one of the tab), composed of a part with files and a graph part
function AutoProcFilePanel(args) {
	var _this = this;
	this.width = 600;
	this.height = 700;
	/** Events * */
	this.onAutoProcGraphSelected = new Event(this);

	// in case of no file
	this.noFilePanel = null;
	// panel with the files list
	this.filesPanel = null;
	// panel with graph
	this.graphPanel = null;
	// data
	this.data = null;
	
	this.autoProcPlotStore = null;
	this.autoProcPlot= null;
	this.graphCombo = null;

	this.bodyStyle = 'background-color:#dfe8f5;';

	/* type 1 for XDS, 2 for SCALE, 3 for SCALA/AIMLESS, 4 for SCALEPACK, 5 for TRUNCATE, 6 for DIMPLE */
	this.type = 1;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.type != null) {
			this.type = args.type;
		}
	}

	// Define the model for a Criteria
	Ext.define('AutoProcPlotModel', {
				extend : 'Ext.data.Model',
				fields : [{
							type : 'string',
							name : 'autoProcProgramAttachmentId'
						}, {
							type : 'string',
							name : 'fileName'
						}]
			});
}


// returns true if we are in the correct step
AutoProcFilePanel.prototype.isAttachmentInStep = function(step) {
	return ((this.type == 1 && step == "XDS") ||
			(this.type == 2 && step == "XSCALE") ||
			(this.type == 3 && step == "SCALA") ||
			(this.type == 4 && step == "SCALEPACK") || 
			(this.type == 5 && step == "TRUNCATE") || 
			(this.type == 6 && step == "DIMPLE"));
};


// returns the items to display
AutoProcFilePanel.prototype.getItems = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var items = [];
	//title for Input Files
	var inputF = {
		xtype : 'displayfield',
		fieldLabel : 'Input Files',
		labelStyle : labelStyle
	};
	// title for Log files
	var logF = {
		xtype : 'displayfield',
		fieldLabel : 'Log Files',
		labelStyle : labelStyle
	};
	// title for Output files
	var outputF = {
		xtype : 'displayfield',
		fieldLabel : 'Output Files',
		labelStyle : labelStyle
	};
	// title for Correction files
	var correctionF = {
		xtype : 'displayfield',
		fieldLabel : 'Correction Files',
		labelStyle : labelStyle,
		labelWidth: 300
	};

	var inputFiles = [];
	var logFiles = [];
	var outputFiles = [];
	var correctionFiles = [];
	var graphFiles = [];

	// iterates over the attachments and send them in the correct category (input, output, log or correction)
	if (data && data.autoProcDetail && data.autoProcDetail.autoProcProgAttachmentsWebBeans) {
		for (var i = 0; i < data.autoProcDetail.autoProcProgAttachmentsWebBeans.length; i++) {
			attachment = data.autoProcDetail.autoProcProgAttachmentsWebBeans[i];
			// right step?
			if (attachment.ispybAutoProcAttachment &&  _this.isAttachmentInStep(attachment.ispybAutoProcAttachment.step)) {
				var category = attachment.ispybAutoProcAttachment.fileCategory;
				var href = 'viewResults.do?reqCode=displayAutoProcProgAttachment&autoProcProgramAttachmentId=' +  attachment.autoProcProgramAttachmentId;
				if (category == "correction") {
					href = 'viewResults.do?reqCode=displayCorrectionFile&fileName=' + attachment.fileName;
				}
				var buttonsItems = [];
				// create the buttons corresponding to the file
				var button = new Ext.Button({
							text : attachment.fileName,
							href : href,
							hrefTarget : '_self',
							tooltip : attachment.ispybAutoProcAttachment.description
						});
				buttonsItems.push(button);
				// can this attachment have a graph?
				var hasGraph = attachment.ispybAutoProcAttachment.hasGraph;
				// if yes, we create the button
				if (hasGraph == true){
					var buttonGraph = new Ext.Button({
							id:''+attachment.autoProcProgramAttachmentId,
							name:''+attachment.fileName,
							tooltip : 'View Graph',
							icon : '../images/icon_graph.png',
							handler : function(args) {
								_this.updateGraph(args.name);
							},
							style : {
								marginLeft : '10px'
							}
						});
					buttonsItems.push(buttonGraph);
				}
				// panel containing the file button and graph eventually
				var panelFile = Ext.create('Ext.Panel', {
						layout : {
							type : 'hbox', // Arrange child items vertically
							align : 'LEFT', // Each takes up full width
							bodyPadding : 10
						},
						border : 0,
						bodyStyle : _this.bodyStyle,
						collapsible : false,
						frame : false,
						style : {
								marginBottom : '10px',
								marginTop : '10px'
							},
						items : buttonsItems
					});
				
				// choose the correct category
				if (category == "input") {
					inputFiles.push(panelFile);
				} else if (category == "output") {
					outputFiles.push(panelFile);
				} else if (category == "log") {
					logFiles.push(panelFile);
				} else if (category == "correction") {
					correctionFiles.push(panelFile);
				}
				
			}
		}
		// input
		if (inputFiles.length > 0) {
			items.push(inputF);
		}
		for (var i = 0; i < inputFiles.length; i++) {
			items.push(inputFiles[i]);
		}
		// log
		if (logFiles.length > 0) {
			items.push(logF);
		}
		for (var i = 0; i < logFiles.length; i++) {
			items.push(logFiles[i]);
		}
		// output
		if (outputFiles.length > 0) {
			items.push(outputF);
		}
		for (var i = 0; i < outputFiles.length; i++) {
			items.push(outputFiles[i]);
		}
		// correction
		if (correctionFiles.length > 0) {
			items.push(correctionF);
		}
		for (var i = 0; i < correctionFiles.length; i++) {
			items.push(correctionFiles[i]);
		}
		
	}
	return items;
};

// builds and returns the main panel
AutoProcFilePanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;

	// get the items to display
	var items = _this.getItems(data);
	// get 'No file' eventually
	var html = _this.getHtml(items);
	// get the layout, vbox or fit
	var typeLayout = _this.getLayout(items);

	// graph panel
	_this.graphPanel = Ext.create('Ext.Panel', {
				layout : {
					type : "fit"
				},
				bodyBorder : false,
				bodyStyle : _this.bodyStyle,
				collapsible : false,
				frame : false,
				items : []
			});

	// no file panel
	_this.noFilePanel = Ext.create('Ext.Panel', {
				layout : {
					type : "fit"
				},
				bodyBorder : false,
				bodyStyle : _this.bodyStyle,
				collapsible : false,
				frame : false,
				html : html

			});
	// panel with all buttons
	_this.filesPanel = Ext.create('Ext.Panel', {
				layout : {
					type : "vbox"
				},
				bodyBorder : false,
				bodyStyle : _this.bodyStyle,
				collapsible : false,
				frame : false,
				items : items

			});

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				layout : {
					type : "fit"
				},
				bodyStyle : _this.bodyStyle,
				collapsible : false,
				frame : false,
				items : [_this.noFilePanel, _this.filesPanel, _this.graphPanel]

			});
	return _this.panel;
};

// return the code for No File information
AutoProcFilePanel.prototype.getHtml = function(items) {
	var html = "";
	if (items.length == 0) {
		html = "<html><h4>No Files have been found</h4></html>";
	}
	return html;
};

// returns the layout, depending if there are file or not
AutoProcFilePanel.prototype.getLayout = function(items) {
	var typeLayout = "vbox";
	if (items.length == 0) {
		typeLayout = "fit";
	}
	return typeLayout;
};

// an autoProc has been selected => clear the panels and rebuild
AutoProcFilePanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	_this.data = data;
	// clear component in the files panel
	var f;
	while (f = _this.filesPanel.items.first()) {
		_this.filesPanel.remove(f, true);
	}
	_this.filesPanel.doLayout();
	var items = _this.getItems(data);
	var html = _this.getHtml(items);
	_this.filesPanel.add(items);
	_this.noFilePanel.update(html);
	// graph clean
	_this.cleanGraphPanel();
	_this.panel.doLayout();
};


// graph to update
AutoProcFilePanel.prototype.updateGraph = function(value) {
	var _this = this;
	var autoProcProgramAttachmentId = -1;
	for (var i = 0; i < _this.data.autoProcDetail.autoProcProgAttachmentsWebBeans.length; i++) {
		if (value == _this.data.autoProcDetail.autoProcProgAttachmentsWebBeans[i].fileName) {
			autoProcProgramAttachmentId = _this.data.autoProcDetail.autoProcProgAttachmentsWebBeans[i].autoProcProgramAttachmentId;
			break;
		}
	}
	if (autoProcProgramAttachmentId != -1) {
		_this.onAutoProcGraphSelected.notify({
					'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
				});
	}
};

// clean the graph panel
AutoProcFilePanel.prototype.cleanGraphPanel = function() {
	var _this = this;
	var f;
	while (f = _this.graphPanel.items.first()) {
		_this.graphPanel.remove(f, true);
	}
	_this.graphPanel.doLayout();
};

// display a graph
AutoProcFilePanel.prototype.displayGraphAttachment = function(dataAttachment) {
	var _this = this;
	_this.cleanGraphPanel();
	var items = [];
	var args = [];
	args.width = _this.width;
	args.height = _this.height;
	var autoProcProgramAttachmentGraphPanel = new AutoProcProgramAttachmentGraphPanel(args);
	items.push(autoProcProgramAttachmentGraphPanel.getPanel(dataAttachment));
	_this.graphPanel.add(items);
	_this.graphPanel.doLayout();
	_this.panel.doLayout();
};

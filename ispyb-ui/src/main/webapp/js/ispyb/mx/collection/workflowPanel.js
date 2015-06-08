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
// Workflow panel tab
function WorkflowPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// returns the main panel
WorkflowPanel.prototype.getPanel = function(data) {
	var _this = this;

	var labelStyle = 'font-weight:bold;';
	// workflow
	var workflow = data.workflowVO;
	// Status
	var iconWorkflow = "../images/Sphere_White_12.png";
	var mess = "";
	if (workflow.status) {
		mess = workflow.status;
	}
	if (mess) {
		if (mess.search("Error") != -1 || mess.search("Failure") != -1) {
			iconWorkflow = "../images/Sphere_Red_12.png";
		} else if (mess.search("Started") != -1 || mess.search("Launched") != -1) {
			iconWorkflow = "../images/Sphere_Orange_12.png";
		} else if (mess.search("Success") != -1) {
			iconWorkflow = "../images/Sphere_Green_12.png";
		}
	}

	// data workflow
	var dataAvailable = false;
	if (workflow && workflow.dataFile && workflow.dataFile.existFile) {
		dataAvailable = (workflow.dataFile.existFile == 1);
	}
	var dataFileName = "";
	if (workflow && workflow.dataFile && workflow.dataFile.fileName) {
		dataFileName = workflow.dataFile.fileName;
	}
	var buttonDataFilePath = new Ext.Button({
		text : dataFileName,
		style : {
			marginBottom : '10px'
		},
		handler : function() {
			window.location.href = "viewDataCollection.do?reqCode=downloadFile&fullFilePath=" + workflow.dataFile.fileFullPath;
		},
		disabled : !dataAvailable
	});

	
	// log 
	var logAvailable = false;
	if (workflow.logFile && workflow.logFile.existFile) {
		logAvailable = workflow.logFile.existFile == 1;
	}
	var logFileName = "";
	if (workflow && workflow.logFile && workflow.logFile.fileName) {
		logFileName = workflow.logFile.fileName;
	}
	var buttonLogFilePath = new Ext.Button({
		text : logFileName,
		style : {
			marginBottom : '10px'
		},
		handler : function() {
			window.location.href = "viewDataCollection.do?reqCode=downloadFile&fullFilePath=" + workflow.logFile.fileFullPath;
		},
		disabled : !logAvailable
	});

	var value1 = "";
	var value2 = "";
	if (workflow && workflow.workflowMesh) {
		value1 = workflow.workflowMesh.value1;
		value2 = workflow.workflowMesh.value2;
	}

	// items to display
	var items = [{
		xtype : 'field',
		name : 'icon',
		fieldSubTpl : new Ext.XTemplate("<img src='" + iconWorkflow + "' border=0\/>")
	}, {
		xtype : 'displayfield',
		name : 'title',
		fieldLabel : 'Title',
		labelStyle : labelStyle,
		value : workflow.workflowTitle
	}, {
		xtype : 'textareafield',
		name : 'comments',
		fieldLabel : 'Comments',
		readOnly : true,
		columns : 30,
		width : 500,
		rows : 7,
		labelStyle : labelStyle,
		value : workflow.comments
	},

	{
		xtype : 'textareafield',
		name : 'status',
		fieldLabel : 'Status',
		labelStyle : labelStyle,
		readOnly : true,
		columns : 30,
		width : 500,
		rows : 4,
		value : workflow.status
	}];
	if (data.displayMesh == 1) {
		items.push({
					xtype : 'displayfield',
					name : 'value1',
					fieldLabel : 'Value 1',
					labelStyle : labelStyle,
					value : value1
				}, {
					xtype : 'displayfield',
					name : 'value2',
					fieldLabel : 'Value 2',
					labelStyle : labelStyle,
					value : value2
				});
	}
	if (logFileName != "")
		items.push(buttonLogFilePath);
	if (dataFileName != "")
		items.push(buttonDataFilePath);

	if (logAvailable) {
		items.push({
					xtype : 'textareafield',
					// grow: true,
					labelStyle : labelStyle,
					name : 'logFileTxt',
					fieldLabel : 'Log File',
					readOnly : true,
					columns : 30,
					width : 800,
					rows : 15,
					value : workflow.logFile.fullFileContent
				});
	}

	// main panel
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'vbox'
				},
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : items

			});

	return _this.panel;
};
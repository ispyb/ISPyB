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
// panel for starting the reprocessing
function ReprocessingPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	
	this.data = null;
	this.onUploadFile = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// builds and returns the main panel
ReprocessingPanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'vbox'
				},
				title : "Reprocessing",
				collapsible : false,
				frame : true,
				width : 550,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : [{
					xtype : 'label',
					text : 'To submit a new autoprocessing task upload edited XDS.INP file and press Start processing.',
					margin : '2 2 0 0',
					cls : "inline-help"
				},
				         
				         {
							xtype : 'checkbox',
							margin : '2 2 0 0',
							boxLabel : "<span style='font-weight:bold;'>EDNAproc</span>",
							checked : true,
							disabled : true,
							width : 100
						 }				
						
						],
				buttons : [{
							text : 'Upload XDS.INP file',
							icon : '../images/add.png',
							handler : function() {
								_this.openUploadDialog('XDSInputFile', 'Upload XDS Input File');
								}
							},
							{
							text : 'Start processing',
							icon : '../images/Next-16x16.png',
							handler : function() {
								_this.startProcessing();
								}
							}
						]
			});
	return _this.panel;
};

//ReprocessingPanel.prototype.startProcessing = function() {
//	var _this = this;
//	var dataCollectionId = _this.data.dataCollectionId;
//	_this.panel.setLoading("Submitting job to the autoprocessing queue");
//	document.location.href = "viewResults.do?reqCode=relaunchProcessing";
	
//	_this.panel.setLoading(false);
//};

//launch the protection of sessions
ReprocessingPanel.prototype.startProcessing = function () {
		var _this = this;
		//_this.panel.setLoading("Submitting job to the autoprocessing queue");
		$.ajax({
				type : this.type,
				url : "viewResults.do?reqCode=relaunchProcessing",
				data : {},
				datatype : "text/json",
			});
		//_this.panel.setLoading(false);
		alert("New autoprocessing task submitted to the queue.")
    };


ReprocessingPanel.prototype.openUploadDialog = function(type, title) {
	var _this = this;
	function onClose() {
		w.destroy();
		_this._update();	
	}

	var w = Ext.create('Ext.window.Window', {
		title : title,
		height : 200,
		width : 400,
		modal : true,
		buttons : [ {
			text : 'Close',
			handler : function() {
				onClose();
			}
		} ],
		layout : 'fit',
		items : {
			html : "<iframe style='width:500px' src='uploadXDSInputFile.do?reqCode=display&type=" + type + "'></iframe>"
		},
		listeners : {
			onEsc : function() {
				onClose();
			},
			close : function() {
				onClose();
			}
		}
	}).show();
};



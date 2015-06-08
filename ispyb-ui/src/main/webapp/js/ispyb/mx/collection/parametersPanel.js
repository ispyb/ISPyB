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
// Parameter panel in the dataCollection page (ISig and rsymm)
function ParametersPanel(args) {
	var _this = this;
	this.width = 500;
	this.height = 250;

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
ParametersPanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'vbox'
				},
				style : {
					marginLeft : '10px', 
					marginTop : '10px'
				},
				collapsible : true,
				frame : true,
				title: 'Parameters',
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 180
				},
				defaultType : 'textfield',
				items : [{
							fieldLabel : 'Ignore RSymm in the<br/>  low resolution shell over',
							name : 'rsymm',
							value : data.rMerge
						}, {
							fieldLabel : 'Ignore I / Sigma in the<br/>  low resolution shell under',
							name : 'isigma',
							value : data.iSigma
						}],
				buttons : [{
							text : 'Update',
							handler : function() {
								_this.update();
							}
						}]

			});

	return _this.panel;
};


// update button: redirection
ParametersPanel.prototype.update = function() {
	var _this = this;
	var rMerge = Ext.getCmp(this.panel.getItemId()).getValues().rsymm;
	var iSigma = Ext.getCmp(this.panel.getItemId()).getValues().isigma;
	if (_this.data.dataCollectionId){
		var dataCollectionId = _this.data.dataCollectionId;
		document.location.href =   "viewResults.do?reqCode=display&dataCollectionId="+dataCollectionId+"&rmerge="+rMerge+"&isigma="+iSigma;
	}else if (_this.data.dataCollectionGroupId){
		var dataCollectionGroupId = _this.data.dataCollectionGroupId;
		document.location.href =   "viewDataCollection.do?reqCode=displayForDataCollectionGroup&dataCollectionGroupId="+dataCollectionGroupId+"&rmerge="+rMerge+"&isigma="+iSigma;
	}else{
		var sessionId = _this.data.sessionId;
		document.location.href =   "viewDataCollection.do?reqCode=displayForSession&sessionId="+sessionId+"&rmerge="+rMerge+"&isigma="+iSigma;
	}
	
};


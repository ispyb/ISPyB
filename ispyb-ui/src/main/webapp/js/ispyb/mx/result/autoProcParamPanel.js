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
// panel with the Rsymm and I/Sigma values
function AutoProcParamPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	
	this.data = null;

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
AutoProcParamPanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'hbox'
				},
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : [{
							fieldLabel : 'RSymm threshold in lower shell',
							name : 'rsymm',
							anchor : '95%',
							value : data.rMerge
						}, {
							fieldLabel : 'I/Sigma threshold in lower shell',
							name : 'isigma',
							anchor : '95%',
							value : data.iSigma,
							style : {
								marginLeft : '10px'
							}
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


// update the values => call to the server
AutoProcParamPanel.prototype.update = function() {
	var _this = this;
	var rMerge = Ext.getCmp(this.panel.getItemId()).getValues().rsymm;
	var iSigma = Ext.getCmp(this.panel.getItemId()).getValues().isigma;
	var dataCollectionId = _this.data.dataCollectionId;
	document.location.href =   "viewResults.do?reqCode=display&dataCollectionId="+dataCollectionId+"&rmerge="+rMerge+"&isigma="+iSigma;
	
};



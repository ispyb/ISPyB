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
// define the parameters of the map (title, range, etc) that can be set by the user
function MapSettingsPanel(args) {
	this.actions = new Array();

	if (args != null) {
		if (args.actions != null) {
			this.actions = args.actions;
		}
	}

}

MapSettingsPanel.prototype.getPanel = function(data) {
	var _this = this;
	var mapTitle = data.mapTitle;
	var maxValue = data.maxValue;
	var minValue = data.minValue;
	if (this.panel == null) {
		this.panel = Ext.createWidget('form', {
			bodyPadding : 5,
			width : 60,
			frame : true,
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 60
			},
			items : [{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'container',
					flex : 1,
					border : false,
					layout : 'anchor',
					defaultType : 'textfield',
					items : [{

								fieldLabel : 'Map title',
								name : 'mapTitle',
								id : 'mapTitle',
								tooltip : "Title of the map",
								value : mapTitle
							}, {
								xtype : 'container',
								flex : 1,
								layout : 'anchor',
								defaultType : 'textfield',
								items : [{
									xtype : 'numberfield',
									fieldLabel : 'Value max',
									allowNegative : false,
									name : 'maxValue',
									value : maxValue,
									decimalPrecision : 0,
									minValue : 1,
									// maxValue: 3000,
									validator : function(val) {
										if (!Ext.isEmpty(val)) {
											if (val < 0)
												return "Value cannot be negative";
											return true;
										} else {
											return "Value cannot be empty";
										}
									}

								}]
							}//, 
//							{
//								xtype : 'container',
//								flex : 1,
//								layout : 'anchor',
//								defaultType : 'textfield',
//								items : [{
//									xtype : 'numberfield',
//									fieldLabel : 'Value min',
//									allowNegative : false,
//									name : 'minValue',
//									value : minValue,
//									decimalPrecision : 0,
//									minValue : 0,
//									// maxValue: 3000,
//									validator : function(val) {
//										if (!Ext.isEmpty(val)) {
//											if (val < 0)
//												return "Value cannot be negative";
//											return true;
//										} else {
//											return "Value cannot be empty";
//										}
//									}
//
//								}]
//							}
							]
				}]
			}]
		});
	}
	return this.panel;
};

MapSettingsPanel.prototype.getMapTitle = function() {
	var mapTitle = Ext.getCmp(this.panel.getItemId()).getValues().mapTitle;
	return mapTitle;
};

MapSettingsPanel.prototype.getMaxValue = function() {
	var maxValue = Ext.getCmp(this.panel.getItemId()).getValues().maxValue;
	return maxValue;
};


MapSettingsPanel.prototype.getMinValue = function() {
	var minValue = Ext.getCmp(this.panel.getItemId()).getValues().minValue;
	return minValue;
};

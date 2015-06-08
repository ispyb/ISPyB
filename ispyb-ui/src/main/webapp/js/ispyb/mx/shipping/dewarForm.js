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
 * Brenchereau, M. Bodin,  A. De Maria Antolinos
 ******************************************************************************/

/*global Ext */
// Dewar form (jsut the label for now)
function DewarForm(args) {
	
	this.dewar = null;

	/** STRUCTURES * */

}


// return the dewar with the values from the form
DewarForm.prototype.getDewar = function () {
	var _this = this;
	this.dewar.dewarName = Ext.getCmp(this.panel.getItemId()).getValues().dewarName;
	
	return this.dewar;
};


// refresh data with the given dewar
DewarForm.prototype.refresh = function (dewar) {
	this.dewar = dewar;
};


// builds and returns the panel 
DewarForm.prototype.getPanel = function (dewar) {
	this.dewar = dewar;
	var _this = this;
	if (this.panel == null) {
		this.panel = Ext.createWidget('form', {
			bodyPadding : 5,
			frame : true,
			border : 0,
			layout: 'fit',
			fieldDefaults : {
				labelAlign : 'left'
				//anchor : '100%'
			},
			items : [{
				xtype : 'container',
				border : false,
				defaultType : 'textfield',
				items : [{
					fieldLabel : 'Dewar label',
					name : 'dewarName',
					tooltip : "Dewar label",
					width: 350,
					allowBlank : false,
					maxLength: 45,
					value : this.dewar.dewarName
				}]
			}]

		});
	}
	return this.panel;
};

// is a valid form?
DewarForm.prototype.isValid = function () {
	return this.panel.getForm().isValid();
};

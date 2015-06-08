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

// Puck form to edit a puck (just the label for now)
function PuckForm(args) {
	this.puck = null;

	/** STRUCTURES * */

}

// returns the puck with new values
PuckForm.prototype.getPuck = function () {
	var _this = this;
	this.puck.puckName = Ext.getCmp(this.panel.getItemId()).getValues().puckName;
	
	return this.puck;
};

// refresh the data
PuckForm.prototype.refresh = function (puck) {
	this.puck = puck;
};


// builds and returns the panel- form
PuckForm.prototype.getPanel = function (puck) {
	this.puck = puck;
	var _this = this;
	if (this.panel == null) {
		this.panel = Ext.createWidget('form', {
			bodyPadding : 5,
			frame : true,
			border : 0,
			layout : {
				type : 'vbox'
			},
			fieldDefaults : {
				labelAlign : 'left'
				//anchor : '100%'
			},
			items : [{
				xtype : 'container',
				border : false,
				defaultType : 'textfield',
				items : [{
					fieldLabel : 'Puck label',
					name : 'puckName',
					tooltip : "Puck label",
					allowBlank : false,
					width: 350,
					maxLength: 45,
					value : this.puck.puckName
				}]
			}]

		});
	}
	return this.panel;
};

// is it a valid form?
PuckForm.prototype.isValid = function () {
	return this.panel.getForm().isValid();
};




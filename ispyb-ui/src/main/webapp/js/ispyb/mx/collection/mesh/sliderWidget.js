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

/*global Event */
/*global Ext */

// to zoom the cell size of the map
function SliderWidget(args) {
	var _this = this;
	this.sliderValue = 30;
	this.sliderChange = new Event(this);
}

// build the slider
SliderWidget.prototype.draw = function () {
	var _this = this;
	return Ext.create('Ext.slider.Single', {
			hideLabel : true,
			width : 214,
			value : _this.sliderValue,
			increment : 1,
			minValue : 10,
			maxValue : 50,
			listeners : {
				change : function (slider, val) {
					_this.sliderValue = val;
					_this.sliderChange.notify();
				}
			}
		});
};

// returns the slider value
SliderWidget.prototype.getSliderValue = function () {
	var _this = this;
	return _this.sliderValue;
};
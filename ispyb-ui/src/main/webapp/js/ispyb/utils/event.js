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
// **** EVENT INTERFACE ****//
/*
 * var Event = function (sender) { this._sender = sender; this._listeners = []; };
 */

function Event(sender) {
	this._sender = sender;
	this._listeners = [];
}

Event.prototype = {
	attach : function(listener) {
		this._listeners.push(listener);
	},
	notify : function(args) {
		for (var i = 0; i < this._listeners.length; i++) {
			this._listeners[i](this._sender, args);
		}
	}
};

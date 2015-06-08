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
var BUI = {

	getTipHTML : function(message) {
		return "<div class='panelMacro' ><table class='tipMacro'><colgroup span='1'><col span='1' width='24'><col span='1'></colgroup><tbody><tr><td colspan='1' rowspan='1' valign='top'><img align='middle' src='https://cwiki.apache.org/confluence/images/emoticons/check.gif' width='16' height='16' alt='' border='0'></td><td colspan='1' rowspan='1'><b>Tip</b><br clear='none'>"+
		message + "</td></tr></tbody></table></div>";
	},

	getWarningHTML : function(message) {
		return "<div class='panelMacro' ><table class='warningMacro'><colgroup span='1'><col span='1' width='24'><col span='1'></colgroup><tbody><tr><td colspan='1' rowspan='1' valign='top'><img align='middle' src='https://cwiki.apache.org/confluence/images/emoticons/warning.gif' width='16' height='16' alt='' border='0'></td><td colspan='1' rowspan='1'><b>Warning</b><br clear='none'>" +
		message + "</td></tr></tbody></table></div>";
	},

	getErrorHTML : function(message) {
		return "<div class='panelMacro' ><table class='errorMacro'><colgroup span='1'><col span='1' width='24'><col span='1'></colgroup><tbody><tr><td colspan='1' rowspan='1' valign='top'><img align='middle' src='https://cwiki.apache.org/confluence/images/emoticons/forbidden.gif' width='16' height='16' alt='' border='0'></td><td colspan='1' rowspan='1'><b>Error</b><br clear='none'>" + 
		message + "</td></tr></tbody></table></div>";
	},

	showWarning : function(warning) {
		var warningWidget = new WarningWidget();
		warningWidget.draw(warning);
	},
	showError : function(message) {
		Ext.Msg.show({
			title : 'Error',
			msg : message,
			icon : Ext.Msg.ERROR,
			animEl : 'elId'
		});
	}

};

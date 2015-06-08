/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/
package ispyb.client.mx.collection;

import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;

public class AutoProcShellWrapper {
	private AutoProcScalingStatistics3VO[] scalingStatsInner;

	private AutoProcScalingStatistics3VO[] scalingStatsOuter;

	private AutoProcScalingStatistics3VO[] scalingStatsOverall;

	private AutoProc3VO[] autoProcs;
	
	private AutoProcIntegration3VO[] autoProcIntegrations;

	public AutoProc3VO[] getAutoProcs() {
		return autoProcs;
	}

	public void setAutoProcs(AutoProc3VO[] autoProcs) {
		this.autoProcs = autoProcs;
	}

	public AutoProcScalingStatistics3VO[] getScalingStatsInner() {
		return scalingStatsInner;
	}

	public void setScalingStatsInner(AutoProcScalingStatistics3VO[] scalingStatsInner) {
		this.scalingStatsInner = scalingStatsInner;
	}

	public AutoProcScalingStatistics3VO[] getScalingStatsOuter() {
		return scalingStatsOuter;
	}

	public void setScalingStatsOuter(AutoProcScalingStatistics3VO[] scalingStatsOuter) {
		this.scalingStatsOuter = scalingStatsOuter;
	}

	public AutoProcScalingStatistics3VO[] getScalingStatsOverall() {
		return scalingStatsOverall;
	}

	public void setScalingStatsOverall(AutoProcScalingStatistics3VO[] scalingStatsOverall) {
		this.scalingStatsOverall = scalingStatsOverall;
	}

	public AutoProcIntegration3VO[] getAutoProcIntegrations() {
		return autoProcIntegrations;
	}

	public void setAutoProcIntegrations(AutoProcIntegration3VO[] autoProcIntegrations) {
		this.autoProcIntegrations = autoProcIntegrations;
	}

}

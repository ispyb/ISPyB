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
/*
 * viewShippingForm.java
 * @author ludovic.launer@esrf.fr
 * Dec 14, 2004
 */

package ispyb.client.biosaxs.project;

import ispyb.client.biosaxs.project.info.ProjectListInfo1;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewProjectForm"
 */
public class ProjectListForm extends ActionForm implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private List<ProjectListInfo1> projectsView = new ArrayList<ProjectListInfo1>();
	private List<Experiment3VO> _projects = new ArrayList<Experiment3VO>();
	
	
	public ProjectListForm() {
		super();
	}


	public List<Experiment3VO> getProjects() {
		return _projects;
	}

	public void setProjects(List<Experiment3VO> _projects) {
		this._projects = _projects;
		
		projectsView = new ArrayList<ProjectListInfo1>();
		for (Experiment3VO experiment : _projects) {
			projectsView.add(new ProjectListInfo1(experiment));
		}
	}


	public List<ProjectListInfo1> getProjectsView() {
		return projectsView;
	}


	public void setProjectsView(List<ProjectListInfo1> projectsView) {
		this.projectsView = projectsView;
	}

}

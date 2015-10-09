/****************************************************************************************************
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
 *****************************************************************************************************/
package ispyb.client.mx.collection;

import java.util.List;

import ispyb.common.util.Constants;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.collections.WorkflowMesh3VO;

/**
 * workflow information for displaying 
 * @author BODIN
 *
 */
public class Workflow extends Workflow3VO{

	private static final long serialVersionUID = -3818299533648695330L;
	
	// result file info
	protected List<FileInformation> resultFiles;
	
	//log File info
	protected FileInformation logFile;
	
	// data file info -- for dehydration
	protected FileInformation dataFile;
	
	// workflowMesh -- for mesh
	protected WorkflowMesh3VO workflowMesh;
	
	public Workflow() {
		super();
	}
	
	public Workflow(Workflow3VO vo, FileInformation logFile) {
		super(vo);
		this.logFile = logFile;
	}

	public List<FileInformation> getResultFiles() {
		return resultFiles;
	}

	public void setResultFiles(List<FileInformation> resultFiles) {
		this.resultFiles = resultFiles;
	}

	public FileInformation getLogFile() {
		return logFile;
	}

	public void setLogFile(FileInformation logFile) {
		this.logFile = logFile;
	}

	public FileInformation getDataFile() {
		return dataFile;
	}

	public void setDataFile(FileInformation dataFile) {
		this.dataFile = dataFile;
	}

	public WorkflowMesh3VO getWorkflowMesh() {
		return workflowMesh;
	}

	public void setWorkflowMesh(WorkflowMesh3VO workflowMesh) {
		this.workflowMesh = workflowMesh;
	}

}
	

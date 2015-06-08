/*************************************************************************************************
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
 ****************************************************************************************************/
package ispyb.server.mx.vos.autoproc;

import java.util.Date;

public class PhasingProgramAttachmentWS3VO extends PhasingProgramAttachment3VO{

	private static final long serialVersionUID = 1L;

	protected Integer phasingProgramRunId;

	public PhasingProgramAttachmentWS3VO() {
		super();
	}

	public PhasingProgramAttachmentWS3VO(Integer phasingProgramRunId) {
		super();
		this.phasingProgramRunId = phasingProgramRunId;
	}
	
	public PhasingProgramAttachmentWS3VO(Integer phasingProgramAttachmentId, String fileType,
			String fileName, String filePath, Date recordTimeStamp) {
		super();
		this.phasingProgramAttachmentId = phasingProgramAttachmentId;
		this.fileType = fileType;
		this.fileName = fileName;
		this.filePath = filePath;
		this.recordTimeStamp = recordTimeStamp;
	}

	public Integer getPhasingProgramRunId() {
		return phasingProgramRunId;
	}

	public void setPhasingProgramRunId(Integer phasingProgramRunId) {
		this.phasingProgramRunId = phasingProgramRunId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", phasingProgramRunId="+this.phasingProgramRunId;
		return s;
	}
	

}

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
package ispyb.server.mx.services.utils.bean;

import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.IspybAutoProcAttachment3VO;

import java.io.Serializable;

/**
 * @author DELAGENI
 * 
 */
public class AutoProcAttachmentWebBean extends AutoProcProgramAttachment3VO implements Serializable {

	private static final long serialVersionUID = 7528277447748052029L;

	protected boolean autoProcProgramAttachmentIdHasBeenSet = false;

	protected AutoProcProgram3VO autoProcProgramVO;

	protected boolean autoProcProgramIdHasBeenSet = false;

	protected boolean fileTypeHasBeenSet = false;

	protected boolean fileNameHasBeenSet = false;

	protected boolean filePathHasBeenSet = false;

	// file type
	protected IspybAutoProcAttachment3VO ispybAutoProcAttachment = null;

	public AutoProcAttachmentWebBean() {
		super();
	}

	public AutoProcAttachmentWebBean(AutoProcProgramAttachment3VO att) {

		this.autoProcProgramAttachmentId = att.getAutoProcProgramAttachmentId();
		this.autoProcProgramAttachmentIdHasBeenSet = true;
		this.autoProcProgramVO = att.getAutoProcProgramVO();
		autoProcProgramIdHasBeenSet = true;
		this.fileType = att.getFileType();
		fileTypeHasBeenSet = true;
		this.fileName = att.getFileName();
		fileNameHasBeenSet = true;
		this.filePath = att.getFilePath();
		filePathHasBeenSet = true;
		this.recordTimeStamp = att.getRecordTimeStamp();
	}

	
	@Override
	public java.lang.Integer getAutoProcProgramVOId() {
		return autoProcProgramVO.getAutoProcProgramId();
	}

	@Override
	public AutoProcProgram3VO getAutoProcProgramVO() {
		return autoProcProgramVO;
	}

	@Override
	public void setAutoProcProgramVO(AutoProcProgram3VO autoProcProgramVO) {
		this.autoProcProgramVO = autoProcProgramVO;
	}

		
	public IspybAutoProcAttachment3VO getIspybAutoProcAttachment() {
		return this.ispybAutoProcAttachment;
	}

	public void setIspybAutoProcAttachment(IspybAutoProcAttachment3VO ispybAutoProcAttachment) {
		this.ispybAutoProcAttachment = ispybAutoProcAttachment;
	}

	public boolean isOutput() {
		return this.ispybAutoProcAttachment == null || this.ispybAutoProcAttachment.getFileCategory().equals("output");
	}

	public boolean isInput() {
		return this.ispybAutoProcAttachment == null || !this.ispybAutoProcAttachment.getFileCategory().equals("input");
	}
}

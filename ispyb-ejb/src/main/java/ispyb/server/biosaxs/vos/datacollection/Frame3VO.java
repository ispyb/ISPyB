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
 ******************************************************************************************************************************/

package ispyb.server.biosaxs.vos.datacollection;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "Frame")
public class Frame3VO implements java.io.Serializable {

	protected Integer frameId;
	protected String filePath;
	protected String comments;
	protected Date creationDate;
	

	public Frame3VO() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "frameId", unique = true, nullable = false)
	public Integer getFrameId() {
		return this.frameId;
	}

	public void setFrameId(Integer frameId) {
		this.frameId = frameId;
	}

	@Column(name = "filePath", length = 255)
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "creationDate")
	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	
	@Column(name = "comments", length = 45)
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "frame3VO")
//	public Set<Frametolist3VO> getFrametolist3VOs() {
//		return this.frametolist3VOs;
//	}
//
//	public void setFrametolist3VOs(Set<Frametolist3VO> frametolist3VOs) {
//		this.frametolist3VOs = frametolist3VOs;
//	}

}

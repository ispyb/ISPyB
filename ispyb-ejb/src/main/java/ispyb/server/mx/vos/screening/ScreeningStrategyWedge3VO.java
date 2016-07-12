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

package ispyb.server.mx.vos.screening;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import ispyb.server.common.vos.ISPyBValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.OrderBy;

/**
 * ScreeningStrategyWedge3 value object mapping table ScreeningStrategyWedge
 * 
 */
@Entity
@Table(name = "ScreeningStrategyWedge")
public class ScreeningStrategyWedge3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ScreeningStrategyWedge3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "screeningStrategyWedgeId")
	protected Integer screeningStrategyWedgeId;
	
	@ManyToOne
	@JoinColumn(name = "screeningStrategyId")
	private ScreeningStrategy3VO screeningStrategyVO;
	
	@Column(name = "wedgeNumber")
	protected Integer wedgeNumber;
	
	@Column(name = "resolution")
	protected Double resolution;
	
	@Column(name = "completeness")
	protected Double completeness;
	
	@Column(name = "multiplicity")
	protected Double multiplicity;
	
	@Column(name = "doseTotal")
	protected Double doseTotal;
	
	@Column(name = "numberOfImages")
	protected Integer numberOfImages;
	
	@Column(name = "phi")
	protected Double phi;
	
	@Column(name = "kappa")
	protected Double kappa;
	
	@Column(name = "chi")
	protected Double chi;

	@Column(name = "comments")
	protected String comments;
	
	@Column(name = "wavelength")
	protected Double wavelength;
	
	@OneToMany
	@JoinColumn(name = "screeningStrategyWedgeId")
	@OrderBy(clause = "screeningStrategySubWedgeId")
	private Set<ScreeningStrategySubWedge3VO> screeningStrategySubWedgeVOs;
	
	public ScreeningStrategyWedge3VO(){
		super();
	}

	
	public ScreeningStrategyWedge3VO(Integer screeningStrategyWedgeId,
			ScreeningStrategy3VO screeningStrategyVO, Integer wedgeNumber,
			Double resolution, Double completeness, Double multiplicity,
			Double doseTotal, Integer numberOfImages, Double phi, Double kappa, 
			Double chi, String comments, Double wavelength) {
		super();
		this.screeningStrategyWedgeId = screeningStrategyWedgeId;
		this.screeningStrategyVO = screeningStrategyVO;
		this.wedgeNumber = wedgeNumber;
		this.resolution = resolution;
		this.completeness = completeness;
		this.multiplicity = multiplicity;
		this.doseTotal = doseTotal;
		this.numberOfImages = numberOfImages;
		this.phi = phi;
		this.kappa = kappa;
		this.chi = chi;
		this.comments = comments ;
		this.wavelength = wavelength ;
	}
	
	public ScreeningStrategyWedge3VO(ScreeningStrategyWedge3VO vo) {
		super();
		this.screeningStrategyWedgeId = vo.getScreeningStrategyWedgeId();
		this.screeningStrategyVO = vo.getScreeningStrategyVO();
		this.wedgeNumber = vo.getWedgeNumber();
		this.resolution = vo.getResolution();
		this.completeness = vo.getCompleteness();
		this.multiplicity = vo.getMultiplicity();
		this.doseTotal = vo.getDoseTotal();
		this.numberOfImages = vo.getNumberOfImages();
		this.phi = vo.getPhi();
		this.kappa = vo.getKappa();
		this.chi = vo.getChi();
		this.comments = vo.getComments() ;
		this.wavelength = vo.getWavelength() ;
	}
	
	public void fillVOFromWS(ScreeningStrategyWedgeWS3VO vo) {
		this.screeningStrategyWedgeId = vo.getScreeningStrategyWedgeId();
		this.screeningStrategyVO = null;
		this.wedgeNumber = vo.getWedgeNumber();
		this.resolution = vo.getResolution();
		this.completeness = vo.getCompleteness();
		this.multiplicity = vo.getMultiplicity();
		this.doseTotal = vo.getDoseTotal();
		this.numberOfImages = vo.getNumberOfImages();
		this.phi = vo.getPhi();
		this.kappa = vo.getKappa();
		this.chi = vo.getChi();
		this.comments = vo.getComments() ;
		this.wavelength = vo.getWavelength() ;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getScreeningStrategyWedgeId() {
		return screeningStrategyWedgeId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setScreeningStrategyWedgeId(Integer screeningStrategyWedgeId) {
		this.screeningStrategyWedgeId = screeningStrategyWedgeId;
	}


	public ScreeningStrategy3VO getScreeningStrategyVO() {
		return screeningStrategyVO;
	}


	public void setScreeningStrategyVO(ScreeningStrategy3VO screeningStrategyVO) {
		this.screeningStrategyVO = screeningStrategyVO;
	}


	public Integer getScreeningStrategyVOId() {
		return screeningStrategyVO == null ? null : screeningStrategyVO.getScreeningStrategyId();
	}

	public Integer getWedgeNumber() {
		return wedgeNumber;
	}

	public void setWedgeNumber(Integer wedgeNumber) {
		this.wedgeNumber = wedgeNumber;
	}

	public Double getResolution() {
		return resolution;
	}

	public void setResolution(Double resolution) {
		this.resolution = resolution;
	}

	public Double getCompleteness() {
		return completeness;
	}

	public void setCompleteness(Double completeness) {
		this.completeness = completeness;
	}

	public Double getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(Double multiplicity) {
		this.multiplicity = multiplicity;
	}

	public Double getDoseTotal() {
		return doseTotal;
	}

	public void setDoseTotal(Double doseTotal) {
		this.doseTotal = doseTotal;
	}

	public Integer getNumberOfImages() {
		return numberOfImages;
	}

	public void setNumberOfImages(Integer numberOfImages) {
		this.numberOfImages = numberOfImages;
	}

	public Double getPhi() {
		return phi;
	}

	public void setPhi(Double phi) {
		this.phi = phi;
	}

	public Double getKappa() {
		return kappa;
	}

	public void setKappa(Double kappa) {
		this.kappa = kappa;
	}
	
	

	public Double getChi() {
		return chi;
	}


	public void setChi(Double chi) {
		this.chi = chi;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}


	public Double getWavelength() {
		return wavelength;
	}


	public void setWavelength(Double wavelength) {
		this.wavelength = wavelength;
	}


	public Set<ScreeningStrategySubWedge3VO> getScreeningStrategySubWedgeVOs() {
		return screeningStrategySubWedgeVOs;
	}


	public void setScreeningStrategySubWedgeVOs(
			Set<ScreeningStrategySubWedge3VO> screeningStrategySubWedgeVOs) {
		this.screeningStrategySubWedgeVOs = screeningStrategySubWedgeVOs;
	}

	public ScreeningStrategySubWedge3VO[] getScreeningStrategySubWedgesTab(){
		return this.screeningStrategySubWedgeVOs == null ? null : screeningStrategySubWedgeVOs.toArray(new ScreeningStrategySubWedge3VO[this.screeningStrategySubWedgeVOs.size()]);
	}
	
	public ArrayList<ScreeningStrategySubWedge3VO> getScreeningStrategySubWedgesList(){
		return this.screeningStrategySubWedgeVOs == null ? null : new ArrayList<ScreeningStrategySubWedge3VO>(Arrays.asList(getScreeningStrategySubWedgesTab()));
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//TODO
	}
	
	public String toWSString(){
		String s = "screeningStrategyWedgeId="+this.screeningStrategyWedgeId +", "+
		"wedgeNumber="+this.wedgeNumber+", "+
		"resolution="+this.resolution+", "+
		"completeness="+this.completeness+", "+
		"multiplicity="+this.multiplicity+", "+
		"doseTotal="+this.doseTotal+", "+
		"numberOfImages="+this.numberOfImages+", "+
		"phi="+this.phi+", "+
		"kappa="+this.kappa+", "+
		"chi="+this.chi+", "+
		"comments="+this.comments+", "+
		"wavelength="+this.wavelength;
		
		return s;
	}
}


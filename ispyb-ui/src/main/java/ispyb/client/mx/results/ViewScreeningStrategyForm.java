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
/**
 * 
 * ViewResultsForm.java
 */

package ispyb.client.mx.results;




import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewScreeningStrategyForm"
 */

public class ViewScreeningStrategyForm extends ActionForm implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer dataCollectionId;
	// screening strategy
	private Integer screeningStrategyId;
	private ScreeningStrategy3VO screeningStrategy = new ScreeningStrategy3VO();
	private ScreeningStrategyValueInfo screeningStrategyValueInfo;
	
	private String DNAContent;

	private String DNASelectedFile;
	
	// screening strategy wedge
	private ScreeningStrategyWedge3VO screeningStrategyWedge = new ScreeningStrategyWedge3VO();

	private ScreeningStrategyWedge3VO[] screeningStrategyWedgeList;

	private List<ScreeningStrategyWedgeValueInfo> listStrategiesWedgeInfo;
	
	// screening strategy sub wedge
	private ScreeningStrategySubWedge3VO screeningStrategySubWedge = new ScreeningStrategySubWedge3VO();

	private ScreeningStrategySubWedge3VO[][] screeningStrategySubWedgeListAll;

	private List<ScreeningStrategySubWedgeValueInfo>[] listStrategiesSubWedgeInfoAll;
	
	private ScreeningStrategySubWedge3VO[] screeningStrategySubWedgeList;

	private List<ScreeningStrategySubWedgeValueInfo> listStrategiesSubWedgeInfo;
	
	
	
	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}
	
	public Integer getScreeningStrategyId() {
		return screeningStrategyId;
	}

	public void setScreeningStrategyId(Integer screeningStrategyId) {
		this.screeningStrategyId = screeningStrategyId;
	}
	
	
	/**
	 * @return Returns the screeningStrategy.
	 */
	public ScreeningStrategy3VO getScreeningStrategy() {
		return screeningStrategy;
	}

	/**
	 * @param screeningStrategy
	 *            The screeningStrategy to set.
	 */
	public void setScreeningStrategy(ScreeningStrategy3VO screeningStrategy) {
		this.screeningStrategy = screeningStrategy;
	}
	
	/**
	 * @return Returns the screeningStrategyValueInfo
	 */
	public ScreeningStrategyValueInfo getScreeningStrategyValueInfo() {
		return screeningStrategyValueInfo;
	}

	/**
	 * @param screeningStrategyValueInfo
	 *            The screeningStrategyValueInfo to set.
	 */
	public void setScreeningStrategyValueInfo(ScreeningStrategyValueInfo screeningStrategyValueInfo) {
		this.screeningStrategyValueInfo = screeningStrategyValueInfo;
	}
	
	/**
	 * @return Returns the screeningStrategyWedge.
	 */
	public ScreeningStrategyWedge3VO getScreeningStrategyWedge() {
		return screeningStrategyWedge;
	}

	/**
	 * @param screeningStrategyWedge
	 *            The screeningStrategyWedge to set.
	 */
	public void setScreeningStrategyWedge(ScreeningStrategyWedge3VO screeningStrategyWedge) {
		this.screeningStrategyWedge = screeningStrategyWedge;
	}
	
	/**
	 * @return Returns the screeningStrategyWedgeList.
	 */
	public ScreeningStrategyWedge3VO[] getScreeningStrategyWedgeList() {
		return screeningStrategyWedgeList;
	}

	/**
	 * @param screeningStrategyWedgeList
	 *            The screeningStrategyWedgeList to set.
	 */
	public void setScreeningStrategyWedgeList(ScreeningStrategyWedge3VO[] screeningStrategyWedgeList) {
		this.screeningStrategyWedgeList = screeningStrategyWedgeList;
	}
	
	/**
	 * @return Returns the list of DNA strategies wedge+ data
	 */
	public List<ScreeningStrategyWedgeValueInfo> getListStrategiesWedgeInfo() {
		return listStrategiesWedgeInfo;
	}

	/**
	 * @param listStrategiesWedgeInfo
	 *            The listStrategiesWedgeInfo to set.
	 */
	public void setListStrategiesWedgeInfo(List<ScreeningStrategyWedgeValueInfo> listStrategiesWedgeInfo) {
		this.listStrategiesWedgeInfo = listStrategiesWedgeInfo;
	}
	
	public String getDNAContent() {
		return DNAContent;
	}

	public void setDNAContent(String dNAContent) {
		DNAContent = dNAContent;
	}

	public String getDNASelectedFile() {
		return DNASelectedFile;
	}

	public void setDNASelectedFile(String DNASelectedFile) {
		this.DNASelectedFile = DNASelectedFile;
	}

	
	/**
	 * @return Returns the screeningStrategySubWedge.
	 */
	public ScreeningStrategySubWedge3VO getScreeningStrategySubWedge() {
		return screeningStrategySubWedge;
	}

	/**
	 * @param screeningStrategySubWedge
	 *            The screeningStrategySubWedge to set.
	 */
	public void setScreeningStrategySubWedge(ScreeningStrategySubWedge3VO screeningStrategySubWedge) {
		this.screeningStrategySubWedge = screeningStrategySubWedge;
	}
	
	/**
	 * @return Returns the screeningStrategySubWedgeList.
	 */
	public ScreeningStrategySubWedge3VO[][] getScreeningStrategySubWedgeListAll() {
		return screeningStrategySubWedgeListAll;
	}

	/**
	 * @param screeningStrategySubWedgeList
	 *            The screeningStrategyWedgeList to set.
	 */
	public void setScreeningStrategySubWedgeListAll(ScreeningStrategySubWedge3VO[][] screeningStrategySubWedgeListAll) {
		this.screeningStrategySubWedgeListAll = screeningStrategySubWedgeListAll;
	}
	
	/**
	 * @return Returns the list of DNA strategies sub wedge+ data
	 */
	public List<ScreeningStrategySubWedgeValueInfo>[] getListStrategiesSubWedgeInfoAll() {
		return listStrategiesSubWedgeInfoAll;
	}

	/**
	 * @param listStrategiesSubWedgeInfo
	 *            The listStrategiesSubWedgeInfo to set.
	 */
	public void setListStrategiesSubWedgeInfoAll(List<ScreeningStrategySubWedgeValueInfo>[] listStrategiesSubWedgeInfoAll) {
		this.listStrategiesSubWedgeInfoAll = listStrategiesSubWedgeInfoAll;
	}
	
	/**
	 * @return Returns the screeningStrategySubWedgeList.
	 */
	public ScreeningStrategySubWedge3VO[] getScreeningStrategySubWedgeList() {
		return screeningStrategySubWedgeList;
	}

	/**
	 * @param screeningStrategySubWedgeList
	 *            The screeningStrategyWedgeList to set.
	 */
	public void setScreeningStrategySubWedgeList(ScreeningStrategySubWedge3VO[] screeningStrategySubWedgeList) {
		this.screeningStrategySubWedgeList = screeningStrategySubWedgeList;
	}
	
	/**
	 * @return Returns the list of DNA strategies sub wedge+ data
	 */
	public List<ScreeningStrategySubWedgeValueInfo> getListStrategiesSubWedgeInfo() {
		return listStrategiesSubWedgeInfo;
	}

	/**
	 * @param listStrategiesSubWedgeInfo
	 *            The listStrategiesSubWedgeInfo to set.
	 */
	public void setListStrategiesSubWedgeInfo(List<ScreeningStrategySubWedgeValueInfo> listStrategiesSubWedgeInfo) {
		this.listStrategiesSubWedgeInfo = listStrategiesSubWedgeInfo;
	}
}

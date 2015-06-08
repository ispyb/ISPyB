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
 * SampleRankingComparator.java
 * @author patrice.brenchereau@esrf.fr
 * March 09, 2009
 */

package ispyb.client.mx.ranking;

import java.util.Comparator;

public class SampleRankingComparator implements Comparator<SampleRankingVO> {
	
	/**
	 * =========================================
	 * Generic comparator for SampleRankingValue
	 * =========================================
	 */
	
	public int compare(SampleRankingVO v1, SampleRankingVO v2) {
		return compareIntDesc(v1.getTotalRank(),v2.getTotalRank());
	}
	
	/**
	 * =========================================
	 * Comparators for ranking
	 * =========================================
	 */
	
	static final Comparator<SampleRankingVO> ORDER_ImagePrefix = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO o1, SampleRankingVO o2) {
			return +(o1.getImagePrefix() ).compareTo(o2.getImagePrefix()); 
		}
	};
	
	public static final Comparator<SampleRankingVO> ORDER_StartTime = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO o1, SampleRankingVO o2) {
			return +(o1.getStartTime() ).compareTo(o2.getStartTime()); 
		}
	};
	
	public static final Comparator<SampleRankingVO> ORDER_SpaceGroup = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO o1, SampleRankingVO o2) {
			return +(o1.getSpaceGroup() ).compareTo(o2.getSpaceGroup()); 
		}
	};
	
	public static final Comparator<SampleRankingVO> ORDER_RANK_TheoreticalResolution = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO o1, SampleRankingVO o2) {
			return +(o1.getTheoreticalResolutionRank() ).compareTo(o2.getTheoreticalResolutionRank()); 
		}
	};
	
	public static final Comparator<SampleRankingVO> ORDER_RANK_TotalExposureTime = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO o1, SampleRankingVO o2) {
			return +(o1.getTotalExposureTimeRank() ).compareTo(o2.getTotalExposureTimeRank()); 
		}
	};
	
	public static final Comparator<SampleRankingVO> ORDER_RANK_Mosaicity = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO o1, SampleRankingVO o2) {
			return +(o1.getMosaicityRank() ).compareTo(o2.getMosaicityRank()); 
		}
	};
	
	public static final Comparator<SampleRankingVO> ORDER_RANK_NumberOfSpotsIndexed = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO o1, SampleRankingVO o2) {
			return +(o1.getNumberOfSpotsIndexedRank() ).compareTo(o2.getNumberOfSpotsIndexedRank()); 
		}
	};
	
	public static final Comparator<SampleRankingVO> ORDER_RANK_NumberOfImages = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO o1, SampleRankingVO o2) {
			return +(o1.getNumberOfImagesRank() ).compareTo(o2.getNumberOfImagesRank()); 
		}
	};
	
	public static final Comparator<SampleRankingVO> ORDER_RANK_Total = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO o1, SampleRankingVO o2) {
			return +(o1.getTotalRank() ).compareTo(o2.getTotalRank()); 
		}
	};
	
	/**
	 * =========================================
	 * Comparators for values
	 * =========================================
	 */

	static final Comparator<SampleRankingVO> ORDER_VALUE_TheoreticalResolution_ASC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareDoubleAsc(v1.getTheoreticalResolutionValue(),v2.getTheoreticalResolutionValue());
		}
	};

	static final Comparator<SampleRankingVO> ORDER_VALUE_TotalExposureTime_ASC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareDoubleAsc(v1.getTotalExposureTimeValue(),v2.getTotalExposureTimeValue());
		}
	};
	
	static final Comparator<SampleRankingVO> ORDER_VALUE_Mosaicity_ASC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareDoubleAsc(v1.getMosaicityValue(),v2.getMosaicityValue());
		}
	};

	static final Comparator<SampleRankingVO> ORDER_VALUE_NumberOfSpotsIndexed_DESC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareIntDesc(v1.getNumberOfSpotsIndexedValue(),v2.getNumberOfSpotsIndexedValue());
		}
	};
	
	static final Comparator<SampleRankingVO> ORDER_VALUE_NumberOfImages_ASC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareIntAsc(v1.getNumberOfImagesValue(),v2.getNumberOfImagesValue());
		}
	};

	
		
	/**
	 * =========================================
	 * Comparators for scoring
	 * =========================================
	 */

	static final Comparator<SampleRankingVO> ORDER_SCORE_TheoreticalResolution_DESC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareDoubleDesc(v1.getTheoreticalResolutionScore(),v2.getTheoreticalResolutionScore());
		}
	};

	static final Comparator<SampleRankingVO> ORDER_SCORE_TotalExposureTime_DESC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareDoubleDesc(v1.getTotalExposureTimeScore(),v2.getTotalExposureTimeScore());
		}
	};
	
	static final Comparator<SampleRankingVO> ORDER_SCORE_Mosaicity_DESC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareDoubleDesc(v1.getMosaicityScore(),v2.getMosaicityScore());
		}
	};

	static final Comparator<SampleRankingVO> ORDER_SCORE_NumberOfSpotsIndexed_DESC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareDoubleDesc(v1.getNumberOfSpotsIndexedScore(),v2.getNumberOfSpotsIndexedScore());
		}
	};
	
	static final Comparator<SampleRankingVO> ORDER_SCORE_NumberOfImages_DESC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareDoubleDesc(v1.getNumberOfImagesScore(),v2.getNumberOfImagesScore());
		}
	};

	static final Comparator<SampleRankingVO> ORDER_SCORE_Total_DESC = new Comparator<SampleRankingVO>() {
		public int compare(SampleRankingVO v1, SampleRankingVO v2) {
			return compareDoubleDesc(v1.getTotalScore(),v2.getTotalScore());
		}
	};
	
	/**
	 * Compares 2 Doubles
	 * @param val1
	 * @param val2
	 * @return
	 */
	private static int compareDoubleDesc(Double val1, Double val2 ) {
		if ( val1 == null && val2 == null ) return 0;
		else if (val1 == null) 	return -1;
		else if (val2 == null) 	return 1;
		else if (val1 == val2 ) return 0;
		else if (val1 > val2 ) 	return -1;
		else if (val1 < val2 ) 	return 1;
		return 0;
	}
	
	/**
	 * Compares 2 Doubles
	 * @param val1
	 * @param val2
	 * @return
	 */
	private static int compareDoubleAsc(Double val1, Double val2 ) {
		if ( val1 == null && val2 == null ) return 0;
		else if (val1 == null) 	return 1;
		else if (val2 == null) 	return -1;
		else if (val1 == val2 ) return 0;
		else if (val1 > val2 ) 	return 1;
		else if (val1 < val2 ) 	return -1;
		return 0;
	}
	
	/**
	 * Compares 2 Integers
	 * @param val1
	 * @param val2
	 * @return
	 */
	private static int compareIntDesc(Integer val1, Integer val2 ) {
		if ( val1 == null && val2 == null ) return 0;
		else if (val1 == null) 	return -1;
		else if (val2 == null) 	return 1;
		else if (val1 == val2 ) return 0;
		else if (val1 > val2 ) 	return -1;
		else if (val1 < val2 ) 	return 1;
		return 0;
	}	
	
	/**
	 * Compares 2 Integers
	 * @param val1
	 * @param val2
	 * @return
	 */
	private static int compareIntAsc(Integer val1, Integer val2 ) {
		if ( val1 == null && val2 == null ) return 0;
		else if (val1 == null) 	return 1;
		else if (val2 == null) 	return -1;
		else if (val1 == val2 ) return 0;
		else if (val1 > val2 ) 	return 1;
		else if (val1 < val2 ) 	return -1;
		return 0;
	}
}

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

package ispyb.client.mx.ranking.autoProcRanking;


import java.util.Comparator;

public class AutoProcRankingComparator implements Comparator<AutoProcRankingVO>{
	/**
	 * =========================================
	 * Generic comparator for AutoProcRankingValue
	 * =========================================
	 */
	
	public int compare(AutoProcRankingVO v1, AutoProcRankingVO v2) {
		return compareIntDesc(v1.getTotalRank(),v2.getTotalRank());
	}
	
	/**
	 * =========================================
	 * Comparators for ranking
	 * =========================================
	 */
	
	static final Comparator<AutoProcRankingVO> ORDER_ImagePrefix = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO o1, AutoProcRankingVO o2) {
			return +(o1.getImagePrefix() ).compareTo(o2.getImagePrefix()); 
		}
	};
	
	public static final Comparator<AutoProcRankingVO> ORDER_StartTime = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO o1, AutoProcRankingVO o2) {
			return +(o1.getStartTime() ).compareTo(o2.getStartTime()); 
		}
	};
	
	public static final Comparator<AutoProcRankingVO> ORDER_SpaceGroup = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO o1, AutoProcRankingVO o2) {
			return +(o1.getSpaceGroup() ).compareTo(o2.getSpaceGroup()); 
		}
	};
	
	public static final Comparator<AutoProcRankingVO> ORDER_RANK_OverallRFactor = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO o1, AutoProcRankingVO o2) {
			return +(o1.getOverallRFactorRank() ).compareTo(o2.getOverallRFactorRank()); 
		}
	};
	
	public static final Comparator<AutoProcRankingVO> ORDER_RANK_HighestResolution= new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO o1, AutoProcRankingVO o2) {
			return +(o1.getHighestResolutionRank() ).compareTo(o2.getHighestResolutionRank()); 
		}
	};
	
	public static final Comparator<AutoProcRankingVO> ORDER_RANK_Completeness = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO o1, AutoProcRankingVO o2) {
			return +(o1.getCompletenessRank() ).compareTo(o2.getCompletenessRank()); 
		}
	};
	
	
	public static final Comparator<AutoProcRankingVO> ORDER_RANK_Total = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO o1, AutoProcRankingVO o2) {
			return +(o1.getTotalRank() ).compareTo(o2.getTotalRank()); 
		}
	};
	
	/**
	 * =========================================
	 * Comparators for values
	 * =========================================
	 */

	static final Comparator<AutoProcRankingVO> ORDER_VALUE_OverallRFactor_ASC = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO v1, AutoProcRankingVO v2) {
			return compareFloatAsc(v1.getOverallRFactorValue(),v2.getOverallRFactorValue());
		}
	};

	static final Comparator<AutoProcRankingVO> ORDER_VALUE_HighestResolution_ASC = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO v1, AutoProcRankingVO v2) {
			return compareFloatAsc(v1.getHighestResolutionValue(),v2.getHighestResolutionValue());
		}
	};
	
	static final Comparator<AutoProcRankingVO> ORDER_VALUE_Completeness_ASC = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO v1, AutoProcRankingVO v2) {
			return compareFloatAsc(v1.getCompletenessValue(),v2.getCompletenessValue());
		}
	};

	
	
		
	/**
	 * =========================================
	 * Comparators for scoring
	 * =========================================
	 */

	static final Comparator<AutoProcRankingVO> ORDER_SCORE_OverallRFactor_DESC = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO v1, AutoProcRankingVO v2) {
			return compareFloatDesc(v1.getOverallRFactorScore(),v2.getOverallRFactorScore());
		}
	};

	static final Comparator<AutoProcRankingVO> ORDER_SCORE_HighestResolution_DESC = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO v1, AutoProcRankingVO v2) {
			return compareFloatDesc(v1.getHighestResolutionScore(),v2.getHighestResolutionScore());
		}
	};
	
	static final Comparator<AutoProcRankingVO> ORDER_SCORE_Completeness_DESC = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO v1, AutoProcRankingVO v2) {
			return compareFloatDesc(v1.getCompletenessScore(),v2.getCompletenessScore());
		}
	};

	

	static final Comparator<AutoProcRankingVO> ORDER_SCORE_Total_DESC = new Comparator<AutoProcRankingVO>() {
		public int compare(AutoProcRankingVO v1, AutoProcRankingVO v2) {
			return compareFloatDesc(v1.getTotalScore(),v2.getTotalScore());
		}
	};
	
	/**
	 * Compares 2 Float
	 * @param val1
	 * @param val2
	 * @return
	 */
	private static int compareFloatDesc(Float val1, Float val2 ) {
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
	private static int compareFloatAsc(Float val1, Float val2 ) {
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

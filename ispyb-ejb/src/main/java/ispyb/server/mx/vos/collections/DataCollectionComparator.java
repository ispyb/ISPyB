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
package ispyb.server.mx.vos.collections;



import java.util.Comparator;

public class DataCollectionComparator implements Comparator<DataCollection3VO> {

	public int compare(DataCollection3VO o1, DataCollection3VO o2) {
		return -(o1.getDataCollectionId()).compareTo(o2.getDataCollectionId());
	}

	public static final Comparator<DataCollection3VO> sortOnImagePrefixAsc = new Comparator<DataCollection3VO>() {
		public int compare(DataCollection3VO o1, DataCollection3VO o2) {
			// System.out.println("sortOnImagePrefixAsc "+o1.getImagePrefix()+"/"+o2.getImagePrefix());
			return -(o1.getImagePrefix()).compareTo(o2.getImagePrefix());
		}
	};

	public static final Comparator<DataCollection3VO> sortOnImagePrefixDesc = new Comparator<DataCollection3VO>() {
		public int compare(DataCollection3VO o1, DataCollection3VO o2) {
			// System.out.println("sortOnImagePrefixAsc "+o1.getImagePrefix()+"/"+o2.getImagePrefix());
			return +(o1.getImagePrefix()).compareTo(o2.getImagePrefix());
		}
	};

	public static final Comparator<DataCollection3VO> sortOnStartTimeAsc = new Comparator<DataCollection3VO>() {
		public int compare(DataCollection3VO o1, DataCollection3VO o2) {
			// System.out.println("sortOnStartTimeAsc "+o1.getStartTime()+"/"+o2.getStartTime()+" : "+-(o1.getStartTime()
			// ).compareTo(o2.getStartTime()));
			return -(o1.getStartTime()).compareTo(o2.getStartTime());
		}
	};

	public static final Comparator<DataCollection3VO> sortOnStartTimeDesc = new Comparator<DataCollection3VO>() {
		public int compare(DataCollection3VO o1, DataCollection3VO o2) {
			// System.out.println("sortOnStartTimeAsc "+o1.getStartTime()+"/"+o2.getStartTime()+" : "+(o1.getStartTime()
			// ).compareTo(o2.getStartTime()));
			return +(o1.getStartTime()).compareTo(o2.getStartTime());
		}
	};

}

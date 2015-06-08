/*******************************************************************************
 * Copyright (c) 2004-2013
 * Contributors: L. Armanet, M. Camerlenghi, L. Cardonne, S. Delageniere,
 *               L. Duparchy, S. Ohlsson, P. Pascal, I. Schneider, S.Schulze,
 *               F. Torres
 * 
 * This file is part of the MIS tools package.
 * 
 * The MIS tools package is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The MIS tools package is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MIS tools package.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ispyb.client.common.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * array utilities class
 */
public class MISArrayUtils {

	public final static String FORMER_OBJECT_CONSTANT = "${OLD}";

	private MISArrayUtils() {
	}

	/**
	 * Adds an element to an array. The element type must be the same as the type of the array elements.
	 * 
	 * @param array
	 *            must not be null
	 * @param element
	 * @return
	 */
	public static final Object[] addElement(final Object[] array, final Object element) {
		Object[] result = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
		System.arraycopy(array, 0, result, 0, array.length);
		result[array.length] = element;
		return result;
	}

	/**
	 * Sets an element of an array at a specified position. The element type must be the same as the type of the array
	 * elements. If the array is not big enough, its size is increased with null elements.
	 * 
	 * @param array
	 *            must not be null
	 * @param element
	 * @param index
	 *            the position to set the element, must not be less than zero
	 * @return
	 */
	public static final Object[] setElement(final Object[] array, final Object element, final int index) {
		if (array.length >= index + 1) {
			array[index] = element;
			return array;
		}
		Object[] result = (Object[]) Array.newInstance(array.getClass().getComponentType(), index + 1);
		System.arraycopy(array, 0, result, 0, array.length);
		result[index] = element;
		return result;
	}

	/**
	 * Extract elements from a object array
	 * 
	 * @param array
	 *            the array to be parsed
	 * @param columnIndex
	 *            the index of the array to be extracted
	 * @return
	 */
	public static Object[] extractElements(final Object[] array, final int columnIndex) {
		if (isNull(array)) {
			return null;
		}
		if (columnIndex < 0) {
			return array;
		}
		Object[] outArray = new Object[array.length];
		try {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					continue;
				}
				outArray[i] = array[columnIndex];
			}
			return outArray;
		} catch (Exception e) {
			return array;
		}
	}

	/**
	 * Removes all elements with a certain value from an array
	 * 
	 * @param array
	 * @param value
	 * @return
	 */
	public static Object[] removeElements(final Object[] array, final Object value) {
		if (array == null) {
			return null;
		}
		ArrayList al = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			if (value == null && array[i] == null) {
				continue;
			}
			if (array[i] != null && array[i].equals(value)) {
				continue;
			}
			al.add(array[i]);
		}
		Object[] result = (Object[]) Array.newInstance(array.getClass().getComponentType(), al.size());
		result = al.toArray(result);
		return result;
	}

	/**
	 * Remove all elements that are null from the array.
	 * 
	 * @param array
	 * @return
	 */
	public static Object[] removeNullElements(final Object[] array) {
		return removeElements(array, null);
	}

	/**
	 * Removes a number of elements from an array
	 * 
	 * @param array
	 * @param start
	 * @param length
	 * @return
	 */
	public static Object[] removeElements(final Object[] array, final int start, final int length) {
		if (array == null) {
			return null;
		}
		if (start >= array.length) {
			return array;
		}
		int newSize = start;
		if (start + length < array.length) {
			newSize = array.length - length;
		}
		Object[] result = (Object[]) Array.newInstance(array.getClass().getComponentType(), newSize);
		if (start != 0) {
			// copy first part
			System.arraycopy(array, 0, result, 0, start);
		}
		if (newSize > start) {
			// copy second part
			System.arraycopy(array, start + length, result, start, newSize - start);
		}
		return result;
	}

	/**
	 * Removes an element from an array
	 * 
	 * @param array
	 * @param index
	 * @return
	 */
	public static Object[] removeElement(final Object[] array, final int index) {
		return removeElements(array, index, 1);
	}

	/**
	 * Remove duplicate objects from an array
	 * 
	 * @param array
	 *            the objects array to be parsed
	 * @return an array of objects
	 */
	public static Object[] removeDuplicates(final Object[] array) {
		if (isNull(array)) {
			return null;
		}
		try {
			List listNewPks = new ArrayList();
			listNewPks.addAll(new LinkedHashSet(Arrays.asList(array)));
			return listNewPks.toArray();
		} catch (Exception e) {
			return array;
		}
	}

	/**
	 * Merge collections where all objects are of the same type.
	 * 
	 * @param col1
	 * @param col2
	 * @return
	 */
	public static <T> Collection<T> mergeCollection(final Collection<T> col1, final Collection<T> col2) {
		if (col1 == null) {
			if (col2 == null) {
				return null;
			}
			return col2;
		}
		if (col2 == null) {
			return col1;
		}
		Collection<T> result = new ArrayList<T>(col1.size() + col2.size());
		result.addAll(col1);
		result.addAll(col2);
		return result;
	}

	/**
	 * Merge arrays where all objects are of the same type.
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static Object[] mergeArrays(final Object[] array1, final Object[] array2) {
		if (array1 == null) {
			if (array2 == null) {
				return null;
			}
			return array2;
		}
		if (array2 == null) {
			return array1;
		}
		if (!array1.getClass().getComponentType().equals(array2.getClass().getComponentType())) {
			throw new IllegalArgumentException("Arrays cannot be merged sinced array types are not the same!");
		}
		Object[] result = (Object[]) Array.newInstance(array1.getClass().getComponentType(), array1.length
				+ array2.length);
		System.arraycopy(array1, 0, result, 0, array1.length);
		System.arraycopy(array2, 0, result, array1.length, array2.length);
		return result;
	}

	/**
	 * Merge arrays and define type of result. This can be used if the arrays contain different object types which are
	 * subtypes of the result type.
	 * 
	 * @param array1
	 * @param array2
	 * @param arrayClass
	 * @return
	 */
	public static Object[] mergeArrays(final Object[] array1, final Object[] array2, final Class arrayClass) {
		if (array1 == null) {
			if (array2 == null) {
				return null;
			}
			Object[] result = (Object[]) Array.newInstance(arrayClass, array2.length);
			System.arraycopy(array2, 0, result, 0, array2.length);
			return result;
		}
		if (array2 == null) {
			Object[] result = (Object[]) Array.newInstance(arrayClass, array1.length);
			System.arraycopy(array1, 0, result, 0, array1.length);
			return result;
		}
		Object[] result = (Object[]) Array.newInstance(arrayClass, array1.length + array2.length);
		System.arraycopy(array1, 0, result, 0, array1.length);
		System.arraycopy(array2, 0, result, array1.length, array2.length);
		return result;
	}

	/**
	 * Checks if at least one array element equals the passed parameter.
	 * 
	 * @param arr
	 *            the array
	 * @param obj
	 *            the element to compare
	 * @return true, if at least one array element equals to the passed parameter, otherwise false
	 */
	public static boolean equalsToElement(final Object[] arr, final Object obj) {
		if (arr == null) {
			if (obj == null) {
				return true;
			}
			return false;
		}
		if (obj == null) {
			return false;
		}
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] instanceof String && obj instanceof String) {
				if (((String) arr[i]).equalsIgnoreCase((String) obj)) {
					return true;
				}
			} else if (arr[i].equals(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if at least one array element equals the passed parameter.
	 * 
	 * @param arr
	 *            the array
	 * @param obj
	 *            the element to compare
	 * @return true, if at least one array element equals to the passed parameter, otherwise false
	 */
	public static <T> boolean equalsToElement(final List<T> arr, final T obj) {
		if (arr == null) {
			if (obj == null) {
				return true;
			}
			return false;
		}
		if (obj == null) {
			return false;
		}
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i) instanceof String && obj instanceof String) {
				if (((String) arr.get(i)).equalsIgnoreCase((String) obj)) {
					return true;
				}
			} else if (arr.get(i).equals(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Expands an array by a number of empty elements.
	 * 
	 * @param array
	 *            , cannot be null
	 * @param length
	 *            the number of empty (null) elements to add
	 */
	public static Object[] expandArray(final Object[] array, final int length) {
		Object[] result = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length + length);
		System.arraycopy(array, 0, result, 0, array.length);
		return result;
	}

	/**
	 * Expands a collection by a number of empty elements.
	 * 
	 * @param col
	 *            , cannot be null
	 * @param length
	 *            the number of empty (null) elements to add
	 */
	public static <T> Collection<T> expandCollection(final Collection<T> col, final int length) {
		for (int i = 0; i < length; i++)
			col.add(null);
		return col;
	}

	/**
	 * Checks if an array is null or empty or all its elements are null.
	 * 
	 * @param array
	 * @return true if array is null, empty or contains only null elements
	 */
	public static boolean isNull(final Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a collection is null or empty or all its elements are null.
	 * 
	 * @param col
	 * @return true if collection is null, empty or contains only null elements
	 */
	public static boolean isNull(final Collection col) {
		if (col == null || col.isEmpty()) {
			return true;
		}
		Iterator it = col.iterator();
		while (it.hasNext()) {
			if (it.next() != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if an array is null or empty
	 * 
	 * @param array
	 * @return true if array is null or has no element
	 */
	public static boolean isNullOrEmpty(final Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if an array is null or empty
	 * 
	 * @param array
	 * @return true if array is null or has no element
	 */
	public static <T> boolean isNullOrEmpty(final Collection<T> array) {
		if (array == null || array.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if an array is null or empty
	 * 
	 * @param array
	 * @return true if array is null or has no element
	 */
	public static boolean isNullAll(final Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}
		int nullElems = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] instanceof String) {
				String objectStr = (String) array[i];
				if (objectStr == null || objectStr.length() == 0) {
					nullElems++;
				}
			} else if (array[i] == null) {
				nullElems++;
			}
		}
		return (nullElems == array.length ? true : false);
	}

	/**
	 * Removes null elements from beginning and end of array, returns null if array is empty or has only null elements
	 * 
	 * @param array
	 * @return
	 */
	public static Object[] trim(final Object[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		int i = 0;
		for (i = 0; i < array.length && array[i] == null; i++) {
			;
		}
		if (i == array.length) {
			return null;
		}
		int startElement = i;
		for (i = array.length - 1; i >= 0 && array[i] == null; i--) {
			;
		}
		int endElement = i;
		if (startElement == 0 && endElement == array.length - 1) {
			return array;
		}
		Object[] result = new Object[endElement - startElement + 1];
		System.arraycopy(array, startElement, result, 0, result.length);
		return result;
	}

	/**
	 * Move all null elements to end of array. The order of the other elements remains. Afterwards a loop that is only
	 * interested in all elements that are not null can break as soon as it finds a null element.
	 * 
	 * @param array
	 * @return
	 */
	public static Object[] sortNullElements(Object[] array) {
		if (array == null) {
			return null;
		}
		if (array.length == 0) {
			return array;
		}
		Object[] tmp = new Object[array.length];
		int k = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				tmp[k++] = array[i];
			}
		}
		System.arraycopy(tmp, 0, array, 0, tmp.length);
		return array;
	}

	/**
	 * Move all null elements to end of the list. The order of the other elements remains. Afterwards a loop that is
	 * only interested in all elements that are not null can break as soon as it finds a null element.
	 * 
	 * @param col
	 * @return
	 */
	public static <T> List<T> sortNullElements(List<T> col) {
		if (col == null) {
			return null;
		}
		if (col.isEmpty()) {
			return col;
		}
		// Sorting
		Collections.sort(col, new Comparator<T>() {
			@Override
			public int compare(T obj1, T obj2) {
				if ((obj1 == null && obj2 == null) || (obj1 != null && obj2 != null))
					return 0;
				// null objects go to the end
				if (obj1 != null && obj2 == null)
					return -1;
				return 1;
			}
		});
		return col;
	}

	/**
	 * Create a copy of an array.
	 * 
	 * @param array
	 * @return
	 */
	public static Object[] copy(final Object[] array) {
		if (array == null) {
			return null;
		}
		Object[] result = (Object[]) Array.newInstance(array.getClass().getComponentType(), array.length);
		if (array.length == 0) {
			return array;
		}
		System.arraycopy(array, 0, result, 0, array.length);
		return result;
	}

	/**
	 * Compares two arrays for equality. Arrays are equal if they have the same number of elements in the same order
	 * with the same values.
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static boolean equal(final Object[] a1, final Object[] a2) {
		if (a1 == null) {
			if (a2 == null) {
				return true;
			}
			return false;
		}
		if (a2 == null) {
			return false;
		}
		if (a1.length != a2.length) {
			return false;
		}
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] == null) {
				if (a2[2] == null) {
					continue;
				}
				return false;
			}
			if (a2[i] == null) {
				return false;
			}
			if (!a1[i].equals(a2[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param objectArray
	 *            an object array
	 * @return the size of the array
	 */
	public static int getSize(final Object[] objectArray) {
		if (objectArray == null) {
			return 0;
		}
		return objectArray.length;
	}

	/**
	 * 
	 * @param collection
	 * @return the size of the collection
	 */
	public static <T> int getSize(final Collection<T> collection) {
		if (collection == null) {
			return 0;
		}
		return collection.size();
	}

	/**
	 * converts an array of objects to an array of strings using the toString method of each object
	 * 
	 * @param array
	 * @return
	 */
	public static String[] convert2String(final Object[] array) {
		if (array == null) {
			return null;
		}
		if (array.length == 0) {
			return new String[0];
		}
		String[] strArray = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				strArray[i] = null;
			} else {
				strArray[i] = array[i].toString();
			}
		}
		return strArray;
	}

	/**
	 * converts a table of objects to a table of strings using the toString method of each object
	 * 
	 * @param array
	 * @return
	 */
	public static String[][] convertTable2String(final Object[][] array) {
		if (array == null) {
			return null;
		}
		if (array.length == 0) {
			return new String[0][0];
		}
		String[][] strArray = new String[array.length][];
		for (int i = 0; i < array.length; i++) {
			strArray[i] = convert2String(array[i]);
		}
		return strArray;
	}

	/**
	 * getArrayElement to get the "expandable" Array
	 * 
	 * @param index
	 * @param array
	 * @return
	 */
	public static Object getElement(int index, Object[] array) {
		if (array == null || index >= array.length) {
			return null;
		}
		return array[index];
	}

	public static final class TableSize {
		public int cols = 0;

		public int rows = 0;

		public TableSize(int cols, int rows) {
			this.cols = cols;
			this.rows = rows;
		}

		public void calcMaxCol(int newCols) {
			if (newCols > this.cols)
				this.cols = newCols;
		}

		public void calcMaxRow(int newRows) {
			if (newRows > this.rows)
				this.rows = newRows;
		}

		public void calcMaxTableSize(TableSize ts) {
			this.calcMaxRow(ts.rows);
			this.calcMaxCol(ts.cols);
		}
	}

	public final static TableSize getTableSize(Object[][] array) {
		TableSize ts = new TableSize(0, 0);
		if (array == null)
			return ts;
		ts.rows = array.length;
		for (Object[] row : array) {
			if (row != null)
				ts.calcMaxCol(row.length);
		}
		return ts;
	}

	public final static Object[][] fill(Object[][] array, Object emptyElemVal) {
		if (array == null)
			return null;
		int maxCol = getTableSize(array).cols;
		Object[][] result = new Object[array.length][maxCol];
		for (int i = 0; i < result.length; i++) {
			for (int k = 0; k < result[i].length; k++) {
				if (array[i].length > k)
					result[i][k] = array[i][k];
				else
					result[i][k] = emptyElemVal;
			}
		}
		return result;
	}

	public final static Object[] getTableCol(Object[][] array, int colNum) {
		if (array == null)
			return null;
		TableSize ts = getTableSize(array);
		if (colNum >= ts.cols)
			return null;
		Object[] result = new Object[ts.rows];
		for (int i = 0; i < array.length; i++) {
			if (colNum >= array[i].length)
				continue;
			result[i] = array[i][colNum];
		}
		return result;
	}

	/**
	 * Interface for extracting one property of an object
	 * 
	 * @param <C>
	 */
	public static interface PropExtractor<C, Z> {
		/**
		 * Must return the property that should be extracted from the object
		 * 
		 * @param obj
		 * @return
		 */
		public C extract(final Z obj);
	}

	/**
	 * Creates a list based on one property which is fetched from a list of objects.
	 * 
	 * @param <C>
	 * @param list
	 * @param extractor
	 * @return
	 */
	public static final <C, Z> List<C> extractPropList(final List<Z> list, final PropExtractor<C, Z> extractor) {
		if (list == null)
			return null;
		List<C> propList = new ArrayList<C>();
		for (Z obj : list) {
			propList.add(extractor.extract(obj));
		}
		return propList;
	}
}
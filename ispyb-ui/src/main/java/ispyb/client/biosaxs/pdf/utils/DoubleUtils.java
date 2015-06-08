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

package ispyb.client.biosaxs.pdf.utils;

/**
 * @author DEMARIAA
 *
 */
public class DoubleUtils {

	public final static String Digits     = "(\\p{Digit}+)";
	public final static String HexDigits  = "(\\p{XDigit}+)";
   // an exponent is 'e' or 'E' followed by an optionally 
   // signed decimal integer.
	public final static String Exp        = "[eE][+-]?"+Digits;
	public final static String fpRegex    =
       ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
        "[+-]?(" + // Optional sign character
        "NaN|" +           // "NaN" string
        "Infinity|" +      // "Infinity" string

        // A decimal floating-point string representing a finite positive
        // number without a leading sign has at most five basic pieces:
        // Digits . Digits ExponentPart FloatTypeSuffix
        // 
        // Since this method allows integer-only strings as input
        // in addition to strings of floating-point literals, the
        // two sub-patterns below are simplifications of the grammar
        // productions from the Java Language Specification, 2nd 
        // edition, section 3.10.2.

        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
        "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

        // . Digits ExponentPart_opt FloatTypeSuffix_opt
        "(\\.("+Digits+")("+Exp+")?)|"+

  // Hexadecimal strings
  "((" +
   // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
   "(0[xX]" + HexDigits + "(\\.)?)|" +

   // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
   "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

   ")[pP][+-]?" + Digits + "))" +
        "[fFdD]?))" +
        "[\\x00-\\x20]*");// Optional trailing "whitespace"
}

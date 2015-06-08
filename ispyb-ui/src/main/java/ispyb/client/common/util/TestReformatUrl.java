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
package ispyb.client.common.util;

import ispyb.common.util.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestReformatUrl {

	public final static String htmlText2 = "<a href='edna_html/ref-testscale_1_001_pred.jpg' target='_blank'>"
			+ "<img src='edna_html/ref-testscale_1_001_pred_thumbnail.jpg'  /></a> blablablab "
			+ "<a href='edna_html/ref-testscale_1_001_pred.jpg' target='_blank'>"
			+ "<img src='edna_html/ref-testscale_1_001_pred_thumbnail.jpg'  /></a>";

	public final static String fileName = "W:\\"
			+ "id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/index.html";

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		// String orig = htmlText2;
		// orig = FileUtil.fileToString(fileName);
		// String pathImg = "/ispyb/ispyb/user/imageDownload.do?reqCode=getEDNAImage";
		// String pathHref = "/ispyb/ispyb/user/viewResults.do?reqCode=viewEDNAImage";
		// String fullDNAPath =
		// "/data/pyarch/id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/edna_html/";
		//
		// // reformat href tags
		// // href='edna_html/ref-testscale_1_001_pred.jpg'
		// //
		// href='viewResults.do?reqCode=viewEDNAImage&EDNAImagePath=/data/pyarch/id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/edna_html/ref-testscale_1_001_pred_thumbnail.jpg
		// // String href_pattern_edna = "<A HREF='edna_html/*.jpg'";
		//
		// String href_pattern_edna = "<A HREF='edna_html/([^']*)'";
		// Pattern patternHref = Pattern.compile(href_pattern_edna, Pattern.CASE_INSENSITIVE);
		//
		// String href_pattern_subs = "<A HREF='" + pathHref + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullDNAPath +
		// "$1"
		// + "'";
		//
		// Matcher matcherHref = patternHref.matcher(orig);
		// String tmpHref = matcherHref.replaceAll(href_pattern_subs);
		// System.out.println("tmpHref = " + tmpHref);
		//
		// // reformat img tags
		// // <img src='edna_html/ref-testscale_1_001_pred_thumbnail.jpg'
		// // <img
		// //
		// src='imageDownload.do?reqCode=getEDNAImage&EDNAImagePath=/data/pyarch/id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/edna_html/ref-testscale_1_001_pred_thumbnail.jpg'
		// String image_pattern_edna = "<IMG SRC='edna_html/([^']*)'";
		// Pattern pattern = Pattern.compile(image_pattern_edna, Pattern.CASE_INSENSITIVE);
		// String image_pattern_subs = "<img src='" + pathImg + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullDNAPath +
		// "$1"
		// + "'";
		//
		// Matcher matcher = pattern.matcher(tmpHref);
		// String tmp1 = matcher.replaceAll(image_pattern_subs);
		//
		// System.out.println("tmp1 = " + tmp1);
		//
		// // String tmp2 = StringUtils.formatEDNAFileURL(orig, pathImg, pathHref, fullDNAPath);
		// // System.out.println("tmp2 = " + tmp2);

		// reformat href tags for txt files
		// <a href=filename.txt
		// href='viewDatacollection.do?reqCode=downloadFile&fullFilePath=/data/pyarch/id29\mx1101\20100909\hemo\4087\filename.txt'

		String newOrig = " bla bla <TD ALIGN=LEFT><a HREF=hemo_4087_09_Sep_2010_01.txt>hemo_4087_09_Sep_2010_01.txt</a></TD>"
				+ "</TR></TABLE></TD></TR></TABLE></LEFT><H2><FONT color=#009999>Spectrum, Continuum and Fitted values :</FONT></H2><CENTER>"
				+ "<IMG SRC=hemo_4087_09_Sep_2010_01.png ALT=fit graph ALIGN=center></CENTER><H2><FONT color=#009999>Fit Parameters :</FONT></H2>"
				+ "<CENTER><TABLE border=0 cellpadding=0 cellspacing=2 width=80%><TR>";
		String pathHrefFile = "/data/pyarch/id29/mx1101/20100909/hemo/4087/";

		String href_pattern_txt = "<A HREF=([^>]*.txt)>";

		Pattern patternHref3 = Pattern.compile(href_pattern_txt, Pattern.CASE_INSENSITIVE);

		String href_pattern_subs3 = "<A HREF='viewDatacollection.do?reqCode=downloadFile&" + Constants.FULL_FILE_PATH
				+ "=" + pathHrefFile + "$1" + "'>";

		Matcher matcherHref3 = patternHref3.matcher(newOrig);
		String tmp3 = matcherHref3.replaceAll(href_pattern_subs3);

		System.out.println("tmp3 = " + tmp3);

	}
}

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
package ispyb.common.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class PDFFormFiller extends PdfWriter {
	/*************
	 * Constants *
	 *************/

	private static final Logger LOG = Logger.getLogger(PDFFormFiller.class);

	// Default values for setting radio buttons and checkboxes in the forms.
	// Note that those values are the default ones set by Adobe Designer when
	// creating a new form. But you can change them to your values.
	public final static String FORM_FIELDS_RADIO_OFF = "0";

	public final static String FORM_FIELDS_RADIO_ON = "1";

	// Definition of Page orientations
	public final static int PAGE_PORTRAIT = 0;

	public final static int PAGE_LANDSCAPE = 1;

	/**********************
	 * Instance variables *
	 **********************/

	private PdfReader reader; // PDF reader object that get the form file data

	private PdfStamper stamper; // PDF stamper object used to get the form fields

	private AcroFields formFields; // The form fields

	private boolean useDefaultFieldNames = true; // By default the form field names' prefix and suffix are used

	protected PdfWriter writer; // PDF writer object

	protected int pageOrientation; // The page orientation (Portrait, Landscape)

	/***************
	 * Constructor *
	 ***************/

	public PDFFormFiller() {
	}

	/*********************
	 * Getters & Setters *
	 *********************/

	/**
	 * @return a Map of form fields
	 */
	public Map getFields() {
		if (formFields == null)
			return null;
		return formFields.getFields();
	}

	/**
	 * @return Returns the pageOrientation.
	 */
	public int getPageOrientation() {
		return pageOrientation;
	}

	/**
	 * @param pageOrientation
	 *            The pageOrientation to set.
	 */
	public void setPageOrientation(int pageOrientation) {
		this.pageOrientation = ((pageOrientation <= 0) ? PAGE_PORTRAIT : PAGE_LANDSCAPE);
		if (this.writer != null) {
			if (this.pageOrientation == PAGE_PORTRAIT)
				this.writer.setPageSize(PageSize.A4);
			else
				this.writer.setPageSize(PageSize.A4.rotate());
		}
	}

	/**
	 * Set all the values of fields in the form. For tasks that repeatedly fill the same PDF form with different field
	 * values the use of the cache has dramatic speed advantages.
	 * 
	 * @param formFields
	 *            the PDF form field names and values
	 */
	public void setFields(Map formFields) {
		if (formFields == null || formFields.isEmpty())
			return;
		try {
			this.formFields.setFieldCache(new HashMap());
			Iterator it = formFields.keySet().iterator();
			while (it.hasNext()) {
				Object item = it.next();
				String name = "";
				if (item != null && !item.toString().equals(""))
					name = (item.toString());
				Object value = formFields.get(name);
				this.setField(name, value);
			}
		} catch (Exception e) {
			LOG.error("setFields", e);
		}
	}

	/**
	 * @return Returns the useDefaultFieldNames.
	 */
	public boolean isUseDefaultFieldNames() {
		return useDefaultFieldNames;
	}

	/**
	 * @param useDefaultFieldNames
	 *            The useDefaultFieldNames to set.
	 */
	public void setUseDefaultFieldNames(boolean useDefaultFieldNames) {
		this.useDefaultFieldNames = useDefaultFieldNames;
	}

	/*****************
	 * Other Methods *
	 *****************/

	/**
	 * Initialize the PDF document
	 * 
	 * @param pdfForm
	 *            the path to the PDF form to be filled
	 * @param os
	 *            the output stream object
	 * @param pageOrientation
	 *            the page orientation (Portrait or Landscape)
	 * @throws Exception
	 */
	public void init(String pdfForm, OutputStream os, int pageOrientation) throws Exception {
		if (LOG.isDebugEnabled())
			LOG.debug("Initializing with template: " + pdfForm);
		try {
			this.reader = new PdfReader(pdfForm);
			this.stamper = new PdfStamper(this.reader, os);
			this.writer = this.stamper.getWriter();
			this.writer.setPdfVersion(PdfWriter.VERSION_1_3);
			this.setPageOrientation(pageOrientation);
			this.formFields = this.stamper.getAcroFields();
		} catch (Exception e) {
			LOG.error("init", e);
			throw new Exception("Error initializing the PDF objects for file \"" + pdfForm + "\".");
		}
	}
	
	/**
	 * Initialize the PDF document
	 * 
	 * @param pdfForm
	 *            the path to the PDF form to be filled
	 * @param os
	 *            the output stream object
	 * @param pageOrientation
	 *            the page orientation (Portrait or Landscape)
	 * @throws Exception
	 */
	private void init(byte[] bs, OutputStream os, int pageOrientation) throws Exception {
		try {
			this.reader = new PdfReader(bs);
			this.stamper = new PdfStamper(this.reader, os);
			this.writer = this.stamper.getWriter();
			this.writer.setPdfVersion(PdfWriter.VERSION_1_3);
			this.setPageOrientation(pageOrientation);
			this.formFields = this.stamper.getAcroFields();
		} catch (Exception e) {
			LOG.error("init", e);
			throw new Exception("Error initializing the PDF objects for file bytes.");
		}
	}
	
	public void init(InputStream input, OutputStream os) throws Exception {
		try {
			this.reader = new PdfReader(input);
			this.stamper = new PdfStamper(this.reader, os);
			this.writer = this.stamper.getWriter();
			this.writer.setPdfVersion(PdfWriter.VERSION_1_3);
			this.setPageOrientation(PAGE_PORTRAIT);
			this.formFields = this.stamper.getAcroFields();
		} catch (Exception e) {
			LOG.error("init", e);
			throw new Exception("Error initializing the PDF objects for file bytes.");
		}
	}

	/**
	 * Initialize the PDF document using the default page orientation
	 * 
	 * @param pdfForm
	 *            the path to the PDF form to be filled
	 * @param os
	 *            the output stream object
	 * @throws Exception
	 */
	public void init(String pdfForm, OutputStream os) throws Exception {
		init(pdfForm, os, PAGE_PORTRAIT);
	}

	/**
	 * Initialize the PDF document using the default page orientation
	 * 
	 * @param pdfForm
	 *            the path to the PDF form to be filled
	 * @param os
	 *            the output stream object
	 * @throws Exception
	 */
	public void init(byte[] bs, OutputStream os) throws Exception {
		init(bs, os, PAGE_PORTRAIT);
	}


	
	/**
	 * Render and finalize the PDF form filled with data
	 * 
	 * @throws Exception
	 */
	public void render() throws Exception {
		// This is mandatory not to have the "Expected a dict object"
		// message when the result ouput is openned!!!
		PdfDictionary pdfDictionary = (PdfDictionary) PdfReader.getPdfObject(this.reader.getCatalog().get(
				PdfName.ACROFORM));
		pdfDictionary.remove(new PdfName("XFA"));
		// ///////////////////////////////////////////////////////////

		if (this.stamper != null) {
			this.stamper.setFormFlattening(true);
			this.stamper.close();
			this.stamper = null;
		}
		if (this.reader != null) {
			this.reader.close();
			this.reader = null;
		}
		if (this.writer != null) {
			if (!this.writer.isCloseStream())
				this.writer.close();
			this.writer = null;
		}
		this.formFields = null;
	}

	/**
	 * Set the value of a field in the form
	 * 
	 * @param name
	 *            the field name used to access the PDF form field
	 * @param value
	 *            the value to be set
	 */
	private void setField(String name, Object value) {
		if (name == null)
			return;
		try {
			if (this.useDefaultFieldNames) {
				int nbOfPagesInTemplate = this.reader.getNumberOfPages();
				Map fields = this.formFields.getFields();
				for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
					String key = (String) i.next();
					if (key != null && key.toLowerCase().indexOf(name.toLowerCase()) != -1)
						this.formFields.setField(key, (value == null ? "" : value.toString()));
				}
			}
		} catch (Exception e) {
			LOG.error("setField", e);
		}
	}
}

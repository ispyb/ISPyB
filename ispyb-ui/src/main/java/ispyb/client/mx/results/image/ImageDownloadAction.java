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
/*
 * ImageDownload.java
 * 
 * Created on 6 avr. 2005
 *
 */
package ispyb.client.mx.results.image;

import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.FileUtil;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.common.util.StringUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.vos.collections.Image3VO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * @struts.action path="/user/imageDownload" parameter="reqCode" scope="request"
 * 
 * @author ricardo.leal@esrf.fr
 * 
 * @version 0.1
 */
public class ImageDownloadAction extends DispatchAction {

	private static String[] GZIP_SUFIX = { ".gz", ".tgz" };

	protected Image3Service imageService;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger(ImageDownloadAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	public ActionForward getImageForApplet(ActionMapping map, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			String imageIdStr = request.getParameter(Constants.IMAGE_ID);
			Integer imageId;
			try {
				imageId = new Integer(imageIdStr);
			} catch (NumberFormatException ne) {
				imageId = new Integer(imageIdStr.substring(0, imageIdStr.indexOf(";")));
			}

			// two variables to guarantee the user fecths only its own images
			List<Image3VO> imageFetchedList = imageService.findByImageIdAndProposalId(imageId, proposalId);

			if (imageFetchedList.size() == 1) {

				Image3VO imgValue = imageFetchedList.get(0);

				String fileLocation = imgValue.getFileLocation();
				String archiveFileLocation = fileLocation;
				if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_MAXIV() || Constants.SITE_IS_SOLEIL()) {
					archiveFileLocation = fileLocation.replaceAll("external", "archive").replaceAll("inhouse",
							"archive");
				}
				String sourceFileName = archiveFileLocation + "/" + imgValue.getFileName();

				// LOG.debug("Downloading image: " + sourceFileName);

				try {

					byte[] imageBytes = FileUtil.readBytes(sourceFileName);
					response.setContentLength(imageBytes.length);
					ServletOutputStream out = response.getOutputStream();
					if (isGZipped(imgValue.getFileName()))
						response.setContentType("application/x-gzip");
					else
						response.setContentType("image/tiff");

					out.write(imageBytes);
					out.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				LOG.warn("List fechtched has a size != 1");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public ActionForward getImageJpgThumb(ActionMapping map, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			String OSname = System.getProperty("os.name");
			boolean isWindows = (OSname.indexOf("Win") != -1) ? true : false;
			boolean isLocalContact = Confidentiality.isLocalContact(request);
			boolean isManager = Confidentiality.isManager(request);
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (proposalId == null) {
				try {
					proposalId = new Integer(request.getParameter(Constants.PROPOSAL_ID));
				} catch (NumberFormatException e) {
				}
			}

			String imageIdStr = request.getParameter(Constants.IMAGE_ID);
			Integer imageId = null;
			try {
				imageId = new Integer(imageIdStr);
			} catch (NumberFormatException ne) {
				if (imageIdStr.indexOf(";") > -1)
					imageId = new Integer(imageIdStr.substring(0, imageIdStr.indexOf(";")));
			}

			// two variables to guarantee the user fecths only its own images
			List<Image3VO> imageFetchedList = null;
			if (isLocalContact || isManager) {
				Image3VO img = imageService.findByPk(imageId);
				imageFetchedList = new ArrayList<Image3VO>();
				imageFetchedList.add(img);
			} else {
				imageFetchedList = imageService.findByImageIdAndProposalId(imageId, proposalId);
			}

			if (imageFetchedList != null && imageFetchedList.size() == 1) {

				Image3VO imgValue = imageFetchedList.get(0);

				String sourceFileName = imgValue.getJpegThumbnailFileFullPath();
				sourceFileName = PathUtils.FitPathToOS(sourceFileName);

				LOG.debug("Downloading image: " + sourceFileName);

				try {

					byte[] imageBytes = FileUtil.readBytes(sourceFileName);
					response.setContentLength(imageBytes.length);
					ServletOutputStream out = response.getOutputStream();
					response.setContentType("image/jpg");

					out.write(imageBytes);
					out.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				LOG.warn("List fechtched has a size != 1");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public ActionForward getImageJpg(ActionMapping map, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			String OSname = System.getProperty("os.name");
			boolean isWindows = (OSname.indexOf("Win") != -1) ? true : false;
			boolean isLocalContact = Confidentiality.isLocalContact(request);
			boolean isManager = Confidentiality.isManager(request);
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (proposalId == null) {
				try {
					proposalId = new Integer(request.getParameter(Constants.PROPOSAL_ID));
				} catch (NumberFormatException e) {
					if (request.getSession().getAttribute(Constants.PROPOSAL_NUMBER) != null){
						
					}
				}
			}
			if (proposalId == null && (!isLocalContact && !isManager)){
				return null;
			}
			String imageIdStr = request.getParameter(Constants.IMAGE_ID);
			Integer imageId;
			try {
				imageId = new Integer(imageIdStr);
			} catch (NumberFormatException ne) {
				imageId = new Integer(imageIdStr.substring(0, imageIdStr.indexOf(";")));
			}
			// two variables to guarantee the user fecths only its own images
			List<Image3VO> imageFetchedList = null;
			if (isLocalContact || isManager) {
				Image3VO img = imageService.findByPk(imageId);
				imageFetchedList = new ArrayList<Image3VO>();
				imageFetchedList.add(img);
			} else {
				imageFetchedList = imageService.findByImageIdAndProposalId(imageId, proposalId);
			}

			if (imageFetchedList != null && imageFetchedList.size() == 1) {

				Image3VO imgValue = imageFetchedList.get(0);

				String sourceFileName = imgValue.getJpegFileFullPath();

				sourceFileName = PathUtils.FitPathToOS(sourceFileName);

				LOG.debug("Downloading image: " + sourceFileName);

				try {

					byte[] imageBytes = FileUtil.readBytes(sourceFileName);
					response.setContentLength(imageBytes.length);
					ServletOutputStream out = response.getOutputStream();
					response.setContentType("image/jpg");

					out.write(imageBytes);
					out.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				LOG.warn("List fechtched has a size != 1");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public ActionForward getEDNAImage(ActionMapping map, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {

			String sourceFileName = request.getParameter(Constants.EDNA_IMAGE_PATH);

			sourceFileName = PathUtils.FitPathToOS(sourceFileName);
			if (sourceFileName == null )
				return null;

			LOG.debug("Downloading image: " + sourceFileName);

			try {

				byte[] imageBytes = FileUtil.readBytes(sourceFileName);
				response.setContentLength(imageBytes.length);
				ServletOutputStream out = response.getOutputStream();
				response.setContentType("image/jpg");

				out.write(imageBytes);
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getImageJpgFromFile(ActionMapping map, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			String sourceFileName = FileUtil.getRequestParameter(request, Constants.IMG_SNAPSHOT_URL_PARAM);
			//String sourceFileName = request.getParameter(Constants.IMG_SNAPSHOT_URL_PARAM);
			

			// Confidentiality (check if object proposalId has access to file)
			if (!Confidentiality.isAccessAllowed(request, sourceFileName)) {
				byte[] imageBytes = FileUtil.readBytes(request.getRealPath("/images/noEntrySign_01.png"));
				response.setContentLength(imageBytes.length);
				ServletOutputStream out = response.getOutputStream();
				response.setContentType("image/png");
				out.write(imageBytes);
				out.close();
			}

			byte[] imageBytes = FileUtil.readBytes(sourceFileName);
			response.setContentLength(imageBytes.length);
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("image/jpg");

			out.write(imageBytes);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getImageDNA(ActionMapping map, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			String sourceFileName = request.getParameter(Constants.IMG_DNA_PATH_PARAM);
			LOG.debug("Downloading image: " + sourceFileName);

			try {
				byte[] imageBytes = FileUtil.readBytes(sourceFileName);
				response.setContentLength(imageBytes.length);
				ServletOutputStream out = response.getOutputStream();
				response.setContentType("image/jpg");

				out.write(imageBytes);
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * returns true if a filename ends with gzip extension
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isGZipped(String fileName) {
		for (int i = 0; i < GZIP_SUFIX.length; i++) {
			if (fileName.toLowerCase().endsWith(GZIP_SUFIX[i]))
				return true;
		}
		return false;
	}
	
	
}

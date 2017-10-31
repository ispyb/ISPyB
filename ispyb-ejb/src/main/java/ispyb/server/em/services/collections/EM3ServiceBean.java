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
package ispyb.server.em.services.collections;

import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.em.vos.CTF;
import ispyb.server.em.vos.MotionCorrection;
import ispyb.server.em.vos.Movie;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This session bean handles ISPyB AutoProc3.
 * </p>
 */
@Stateless
public class EM3ServiceBean implements EM3Service, EM3ServiceLocal {

	protected Logger log = LoggerFactory.getLogger(EM3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@EJB
	private DataCollection3Service dataCollection3Service;

	@EJB
	private Session3Service session3Service;

	@EJB
	private Proposal3Service proposal3Service;

	@EJB
	private DataCollectionGroup3Service dataCollectionGroup3Service;

	private Session3VO getSession(String proposal, String beamlineName) throws Exception {
		List<Proposal3VO> proposals = proposal3Service.findProposalByLoginName(proposal);
		log.info("Found {} proposals for loginName = {}. technique=EM loginName={}", String.valueOf(proposals.size()), proposal, proposal);

		if (proposals.size() == 1) {
			/** Looking for a session **/
			int proposalId = proposals.get(0).getProposalId();
			List<Session3VO> sessions = session3Service.findSessionByDateProposalAndBeamline(proposalId, beamlineName, Calendar.getInstance().getTime());

			if (sessions.size() > 0) {
				/** Session found **/
				Session3VO session = sessions.get(0);
				log.info("Existing session found. technique=EM sessionId={} beamlineName={}", session.getSessionId(), session.getBeamlineName());
				return session;
			} else {
				Session3VO session = new Session3VO();
				session.setBeamlineName(beamlineName);
				session.setComments("Session created automatically by ISPyB");
				session.setStartDate(Calendar.getInstance().getTime());
				session.setEndDate(Calendar.getInstance().getTime());
				session.setProposalVO(proposals.get(0));
				session = session3Service.create(session);
				return session;
			}
		} else {
			if (proposals.size() == 0) {
				log.error("No proposal found for. technique=EM loginName={}", proposal);
				throw new Exception("No proposal found for " + proposal);
			} else {
				log.error("Several proposals found ({}) {}. technique=EM loginName={}", proposals.size(), proposals.toArray().toString(), proposal);
				throw new Exception("Several proposals found for " + proposal);
			}
		}

	}

	@Override
	public Movie addMovie(String proposal, String sampleAcronym, String movieDirectory, String moviePath, String movieNumber, String micrographPath,
			String thumbnailMicrographPath, String xmlMetaDataPath, String voltage, String sphericalAberration, String amplitudeContrast, String magnification,
			String scannedPixelSize, String noImages, String dosePerImage, String positionX, String positionY, String beamlineName, Date startTime, String gridSquareSnapshotFullPath)
			throws Exception {

		/**
		 * Look for a data collection that represents the GRID. We take the
		 * latest dataCollection
		 **/
		List<DataCollection3VO> dataCollectionList = dataCollection3Service.findFiltered(movieDirectory, null, null, null, null, null);
		log.info("Found {} dataCollection(s) for movieDirectory {}. technique=EM ", dataCollectionList.size(), movieDirectory);
		DataCollection3VO grid = null;

		/**
		 * As it is sorted startTime DESC we take the first in case of having
		 * several
		 **/
		if (dataCollectionList.size() > 0) {
			grid = dataCollectionList.get(0);
			log.info("Found dataCollection. technique=EM dataCollectionId={}", grid.getDataCollectionId());
		} else {
			/** There are not DataCollections so we created a new one **/
			Session3VO session = this.getSession(proposal, beamlineName);

			/** Creating datacollection **/
			DataCollectionGroup3VO group = new DataCollectionGroup3VO();
			group.setSessionVO(session);
			group.setExperimentType("EM");
			group.setStartTime(Calendar.getInstance().getTime());
			group.setXtalSnapshotFullPath(gridSquareSnapshotFullPath);
			
			log.info("Creating dataCollectionGroup. technique=EM sessionId={}", session.getSessionId());
			group = dataCollectionGroup3Service.create(group);
			log.info("Created dataCollectionGroup. technique=EM sessionId={} dataCollectionGroupId={}", session.getSessionId(),
					group.getDataCollectionGroupId());

			/** Creating data Collection **/
			log.info("Creating dataCollection for dataCollectionGroup. technique=EM sessionId={} dataCollectionGroupId={}", session.getSessionId(),
					group.getDataCollectionGroupId());
			DataCollection3VO dataCollection = new DataCollection3VO();
			dataCollection.setDataCollectionGroupVO(group);
			dataCollection.setImageDirectory(movieDirectory);
			dataCollection.setNumberOfImages(Integer.parseInt(noImages));
			dataCollection.setStartTime(startTime);

//			voltage
//			sphericalAberration
//			amplitudeContrast
//			magnification
//			scannedPixelSize
			
			
			grid = dataCollection3Service.create(dataCollection);

		}

		Movie movie = new Movie();
		log.info("DataCollectionId:" + grid.getDataCollectionId());
		movie.setDataCollectionId(grid.getDataCollectionId());
		movie.setMicrographPath(micrographPath);
		movie.setMoviePath(moviePath);
		movie.setThumbnailMicrographPath(thumbnailMicrographPath);
		movie.setPositionX(positionX);
		movie.setPositionY(positionY);
		movie.setMovieNumber(Integer.parseInt(movieNumber));
		movie.setDosePerImage(dosePerImage);
		movie.setXmlMetaDataPath(xmlMetaDataPath);

		log.info("Creating movie. technique=EM moviePath={}", moviePath);
		movie = entityManager.merge(movie);
		log.info("Created movie. technique=EM moviePath={} movieId", moviePath, movie.getMovieId());

		return movie;
	}

	public Movie findMovieByMovieFullPath(String movieFullPath) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Movie.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT
																	// RESULTS !
		crit.add(Restrictions.eq("movieFullPath", movieFullPath));
		crit.addOrder(Order.desc("movieId"));

		@SuppressWarnings("unchecked")
		List<Movie> movies = (List<Movie>) crit.list();

		if (movies.size() > 0) {
			return movies.get(0);
		}
		log.error("Found no movies for movieFullPath {}. movieFullPath={}", movieFullPath, movieFullPath);
		return null;
	}

	public MotionCorrection findMotionCorrectionByMovieFullPath(String movieFullPath) {
		Movie movie = this.findMovieByMovieFullPath(movieFullPath);
		if (movie != null) {
			Session session = (Session) this.entityManager.getDelegate();
			Criteria crit = session.createCriteria(MotionCorrection.class);
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT
																		// RESULTS
																		// !
			crit.add(Restrictions.eq("movieId", movie.getMovieId()));
			crit.addOrder(Order.desc("motionCorrectionId"));

			@SuppressWarnings("unchecked")
			List<MotionCorrection> motions = (List<MotionCorrection>) crit.list();
			if (motions.size() > 0) {
				return motions.get(0);
			} else {
				log.error("Found no motionCorrection for movieFullPath {}. movieFullPath={}", movieFullPath, movieFullPath);
				return null;
			}
		}
		log.error("Found no movies for movieFullPath {}. movieFullPath={}", movieFullPath, movieFullPath);
		return null;
	}

	@Override
	public MotionCorrection addMotionCorrection(String proposal, String movieFullPath, String firstFrame, String lastFrame, String dosePerFrame,
			String doseWeight, String totalMotion, String averageMotionPerFrame, String driftPlotFullPath, String micrographFullPath,
			String micrographSnapshotFullPath, String correctedDoseMicrographFullPath, String logFileFullPath) {

		log.info("Looking for movie. proposal={} movieFullPath={}", proposal, movieFullPath);
		Movie movie = this.findMovieByMovieFullPath(movieFullPath);
		if (movie != null) {
			MotionCorrection motion = new MotionCorrection();
			motion.setMovieId(movie.getMovieId());
			motion.setFirstFrame(firstFrame);
			motion.setLastFrame(lastFrame);
			motion.setDosePerFrame(dosePerFrame);
			motion.setDoseWeight(doseWeight);
			motion.setTotalMotion(totalMotion);
			motion.setAverageMotionPerFrame(averageMotionPerFrame);
			motion.setDriftPlotFullPath(driftPlotFullPath);
			motion.setMicrographFullPath(micrographFullPath);
			motion.setMicrographSnapshotFullPath(micrographSnapshotFullPath);
			motion.setCorrectedDoseMicrographFullPath(correctedDoseMicrographFullPath);
			motion.setLogFileFullPath(logFileFullPath);
			try {
				log.info("Creating motion Correction. technique=EM movieFullPath={}", movieFullPath);
				motion = this.entityManager.merge(motion);
				log.info("Created motion Correction. technique=EM movieFullPath={}", movieFullPath);
				return motion;
			} catch (Exception exp) {
				throw exp;
			}
		}
		return null;
	}

	@Override
	public CTF addCTF(String proposal, String movieFullPath, String spectraImageSnapshotFullPath, String spectraImageFullPath, String defocusU, String defocusV, String angle,
			String crossCorrelationCoefficient, String resolutionLimit, String estimatedBfactor, String logFilePath) {

		log.info("Looking for motion correction. proposal={} movieFullPath={}", proposal, movieFullPath);
		MotionCorrection motion = this.findMotionCorrectionByMovieFullPath(movieFullPath);
		if (motion != null) {
			CTF ctf = new CTF();
			ctf.setMotionCorrectionId(motion.getMotionCorrectionId());
			ctf.setSpectraImageThumbnailFullPath(spectraImageSnapshotFullPath);
			ctf.setSpectraImageFullPath(spectraImageFullPath);
			ctf.setDefocusU(defocusU);
			ctf.setDefocusV(defocusV);
			ctf.setAngle(angle);
			ctf.setCrossCorrelationCoefficient(crossCorrelationCoefficient);
			ctf.setResolutionLimit(resolutionLimit);
			ctf.setEstimatedBfactor(estimatedBfactor);
			ctf.setLogFilePath(logFilePath);
			try {
				log.info("Creating CTF. technique=EM movieFullPath={}", movieFullPath);
				ctf = this.entityManager.merge(ctf);
				log.info("Created CTF. technique=EM movieFullPath={}", movieFullPath);
				return ctf;
			} catch (Exception exp) {
				throw exp;
			}
		}

		return null;
	}

}

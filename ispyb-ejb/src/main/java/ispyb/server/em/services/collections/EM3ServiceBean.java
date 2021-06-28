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

import ispyb.common.util.StringUtils;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.em.vos.CTF;
import ispyb.server.em.vos.MotionCorrection;
import ispyb.server.em.vos.Movie;
import ispyb.server.em.vos.ParticlePicker;
import ispyb.server.em.vos.ParticleClassification;
import ispyb.server.em.vos.ParticleClassificationGroup;
import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.services.collections.BeamLineSetup3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.services.ws.rest.WsServiceBean;
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
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
public class EM3ServiceBean extends WsServiceBean implements EM3Service, EM3ServiceLocal {

	private final String ByDataCollectionId = getViewTableQuery() + " where Movie_dataCollectionId = :dataCollectionId and Proposal_proposalId=:proposalId";

	private final String StatsByDataCollectionId = getStatsQuery() + " where dataCollectionId in (:dataCollectionIdList) and BLSession.proposalId=:proposalId";
	
	private final String StatsByDataCollectionGroupId = getStatsQuery() + " where DataCollection.dataCollectionGroupId=:dataCollectionGroupId";
	
	private final String getStatsBySessionId = "select * from v_em_stats where sessionId = :sessionId and proposalId=:proposalId";

	private final String getClassificationBySessionId = "select * from v_em_classification where sessionId = :sessionId and proposalId=:proposalId";
	
	protected final Logger LOG = LoggerFactory.getLogger(EM3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@EJB
	private DataCollection3Service dataCollection3Service;

	@EJB
	private Session3Service session3Service;

	@EJB
	private Proposal3Service proposal3Service;
	
	@EJB
	private Protein3Service protein3Service;
	
	@EJB
	private Crystal3Service crystal3Service;

	@EJB
	private DataCollectionGroup3Service dataCollectionGroup3Service;
	
	@EJB
	private BLSample3Service sample3Service;
	
	@EJB
	private BeamLineSetup3Service beamLineSetup3Service;

	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/em/movie/getViewTableQuery.sql");
	}
	
	private String getStatsQuery(){
		return this.getQueryFromResourceFile("/queries/em/stats/getStatsQuery.sql");
	}
	
	@Override
	public List<Map<String, Object>> getMoviesDataByDataCollectionId(int proposalId, int dataCollectionId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(ByDataCollectionId);
		query.setParameter("dataCollectionId", dataCollectionId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}
	
	private Integer getProposalId(String proposal, String beamlineName) throws Exception {
		List<Proposal3VO> proposals = proposal3Service.findProposalByLoginName(proposal);
		LOG.info("Found {} proposals for loginName = {}. technique=EM beamlineName={}", String.valueOf(proposals.size()), proposal, beamlineName);

		if (proposals.size() == 1) {
			return proposals.get(0).getProposalId();
		}
		return null;
	}
	
	
	private Session3VO getSession(String proposal, String beamlineName) throws Exception {
		Integer proposalId = this.getProposalId(proposal, beamlineName);
		if (proposalId != null) {
			/** Looking for a session on Today **/
			List<Session3VO> sessions = session3Service.findSessionByDateProposalAndBeamline(proposalId, beamlineName, Calendar.getInstance().getTime());

			if (sessions.size() > 0) {
				/** Session found **/
				Session3VO session = sessions.get(0);
				LOG.info("Existing session found. technique=EM sessionId={} beamlineName={}", session.getSessionId(), session.getBeamlineName());
				return session;
			} else {
				LOG.info("No sessions found for proposal {} and time {}.", proposal, Calendar.getInstance().getTime().toString());
				Session3VO session = new Session3VO();
				session.setBeamlineName(beamlineName);
				session.setComments("Session created automatically by ISPyB");
				session.setStartDate(Calendar.getInstance().getTime());
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, 1);
				session.setEndDate(calendar.getTime());
				session.setProposalVO(proposal3Service.findByPk(proposalId));
				
				BeamLineSetup3VO beamlineSetup = new BeamLineSetup3VO();
				try{
				    beamlineSetup = beamLineSetup3Service.create(beamlineSetup);
				}
				catch(Exception exp){
					LOG.info("BeamlineSetup could not persist");
				}
				if (beamlineSetup.getBeamLineSetupId() != null){
					session.setBeamLineSetupVO(beamlineSetup);
				}
				LOG.info("Creating session  proposal={}. technique=EM proposal={}", proposal,proposal);
				session = session3Service.create(session);
				LOG.info("Session created for  proposal={}. technique=EM proposal={}", proposal,proposal);
				return session;
			}
		} 
		else{
			LOG.error("No proposal found for proposal={}. technique=EM proposal={}", proposal,proposal);
			throw new Exception("No proposal found for proposal=" + proposal);
		}

	}

	private DataCollectionGroup3VO getDataCollectionGroupBySessionId(List<DataCollectionGroup3VO> dataCollectionGroup, Session3VO session) throws Exception{
		for (DataCollectionGroup3VO dataCollectionGroup3VO : dataCollectionGroup) {
			LOG.info("Comparing session of dataCollectionGroup = {} sessionId={}", dataCollectionGroup3VO.getSessionVO().getSessionId(), session.getSessionId());
			if (dataCollectionGroup3VO.getSessionVO().getSessionId().equals(session.getSessionId())){
				return dataCollectionGroup3VO;
			}
		}
		return null;
	}
	
	private DataCollectionGroup3VO createDataCollectionGroup(Session3VO session) throws Exception{
		DataCollectionGroup3VO group = new DataCollectionGroup3VO();
		group.setSessionVO(session);
		group.setExperimentType("EM");
		group.setStartTime(Calendar.getInstance().getTime());					
		LOG.info("Creating dataCollectionGroup. technique=EM sessionId={}", session.getSessionId());
		group = dataCollectionGroup3Service.create(group);
		LOG.info("Created dataCollectionGroup. technique=EM sessionId={} dataCollectionGroupId={}", session.getSessionId(),
				group.getDataCollectionGroupId());
		return group;
	}
	
	private DataCollectionGroup3VO createDataCollectionGroup(Session3VO session, BLSample3VO sample) throws Exception{
		DataCollectionGroup3VO group = this.createDataCollectionGroup(session);
		group.setBlSampleVO(sample);
		return dataCollectionGroup3Service.update(group);
	}
	
	
	private DataCollectionGroup3VO getDataCollectionBySampleAndSession(String sampleAcronym, int proposalId, Session3VO session) throws Exception{
		List<BLSample3VO> samples = sample3Service.findByAcronymAndProposalId(sampleAcronym, proposalId, null);
		LOG.info("{} samples found for sample acronym = {} proposalId = {} sessionId = {} ", samples.size(), sampleAcronym, proposalId, session.getSessionId());
		
		/** Get data collection groups for that sample acronym **/
		List<DataCollectionGroup3VO> groups = new ArrayList<DataCollectionGroup3VO>();
		for (BLSample3VO blSample3VO : samples) {
			groups.addAll(dataCollectionGroup3Service.findBySampleId(blSample3VO.getBlSampleId(), false, false));
		}
		LOG.info("{} data collections found for sample acronym = {} proposalId = {} sessionId = {} ", groups.size(), sampleAcronym, proposalId, session.getSessionId());
		
		
		/** Look for a DCGroup for that session **/
		for (DataCollectionGroup3VO dataCollectionGroup3VO : groups) {
			if (dataCollectionGroup3VO.getSessionVOId().equals(session.getSessionId())){
				LOG.info("Data collection group found for sample acronym = {} proposalId = {} sessionId = {} ", sampleAcronym, proposalId, session.getSessionId());
				return dataCollectionGroup3VO;
			}
		}
		
		return null;
				
	}
	
	
	private DataCollectionGroup3VO getDataCollectionGroup(String sampleAcronym, String proposal, String beamlineName, Session3VO session, String proteinAcronym) throws Exception{
		int proposalId = this.getProposalId(proposal, beamlineName);
		
				
		
		DataCollectionGroup3VO group = this.getDataCollectionBySampleAndSession(sampleAcronym, proposalId, session);
		if (group != null){
			return group;
		}
		LOG.info("No sample acronym found for sampleAcronym = {} and proposal = {} sessionId = {} ", sampleAcronym, proposal, session.getSessionId());
		/** Samples are null or 0 **/
		
		List<Protein3VO> proteins = protein3Service.findByAcronymAndProposalId(proposalId, proteinAcronym);
		Protein3VO protein = new Protein3VO();
		if (proteins.size() == 0){
			LOG.warn("Protein {} does not exist on ISPyB for proposal={}. ", proteinAcronym, proposal);
			/** It creates the protein **/
			
			protein.setProposalVO(proposal3Service.findByPk(proposalId));
			protein.setAcronym(proteinAcronym);
			protein.setName(proteinAcronym);
			LOG.info("Creating Protein with proteinAcronym = {} and proposal = {} ", proteinAcronym, proposal);
			protein = protein3Service.create(protein);
			LOG.info("Created Protein with proteinAcronym = {} and proteinId = {} and proposal = {} ", proteinAcronym, protein.getProteinId(), proposal);

			/** It created the crystal form **/
			Crystal3VO crystal = new Crystal3VO();
			crystal.setProteinVO(protein);
			crystal.setComments("Crystal created automatically from ISPyB for EM proteins");
			crystal.setName(proteinAcronym);
			LOG.info("Creating Crystal with proteinAcronym = {} and proposal = {} ", proteinAcronym, proposal);
			crystal = crystal3Service.create(crystal);
			LOG.info("Created Crystal with proteinAcronym = {} and proteinId = {} and proposal = {} ", proteinAcronym, protein.getProteinId(), proposal);
			
			Set<Crystal3VO> crystals = new HashSet<Crystal3VO>();
			crystals.add(crystal);
			protein.setCrystalVOs(crystals);
			LOG.info("Adding Crystal to Protein with proteinAcronym = {} and proposal = {} ", proteinAcronym, proposal);
			protein = protein3Service.update(protein);
			LOG.info("Added Crystal to Protein with proteinAcronym = {} and proposal = {} ", proteinAcronym, proposal);
		}
		else{
			protein = proteins.get(0);
		}
		
		
		BLSample3VO sample = new BLSample3VO();
		sample.setName(sampleAcronym);
		LOG.info("Find cyrstal for proteinId = {}", protein.getProteinId());
		List<Crystal3VO> crystals = crystal3Service.findByProteinId(protein.getProteinId());
		LOG.info("Found {} crystals", crystals.size());
		sample.setCrystalVO(crystals.get(0));
		LOG.info("Creating Sample with sampleAcronym = {} and proposal = {} ", sampleAcronym, proposal);
		sample = sample3Service.create(sample);
		LOG.info("Created Sample with sampleAcronym = {} and proposal = {} ", sampleAcronym, proposal);
		return this.createDataCollectionGroup(session, sample);
    }
	
	@Override
	public Movie addMovie(String proposal, String proteinAcronym, String sampleAcronym, String movieDirectory, String moviePath, String movieNumber, String micrographPath,
			String thumbnailMicrographPath, String xmlMetaDataPath, String voltage, String sphericalAberration, String amplitudeContrast, String magnification,
			String scannedPixelSize, String noImages, String dosePerImage, String positionX, String positionY, String beamlineName, Date startTime, String gridSquareSnapshotFullPath)
			throws Exception {
		
		// we need to retrieve the session to update the lastUpdate field used by dataconfidentiality
		Session3VO session = this.getSession(proposal, beamlineName);

		/**
		 * Look for a data collection that represents the gridSquare. We take the latest dataCollection
		 **/
		List<DataCollection3VO> dataCollectionList = dataCollection3Service.findFiltered(movieDirectory, null, null, null, null, null);
		LOG.info("Found {} dataCollection(s) for movieDirectory {}. technique=EM ", dataCollectionList.size(), movieDirectory);
		DataCollection3VO gridSquare = null;

		/**
		 * As it is sorted startTime DESC we take the first in case of having
		 * several
		 **/
		if (dataCollectionList.size() > 0) {
			/** There are  DataCollections then we are adding a movie to an existing gridSquare **/
			gridSquare = dataCollectionList.get(0);
			LOG.info("Found dataCollection. technique=EM dataCollectionId={}", gridSquare.getDataCollectionId());
			
			//TODO : check if can be removed because should be the session loaded by this.getSession(proposal, beamlineName);
			session = gridSquare.getDataCollectionGroupVO().getSessionVO();
			
		} else {
			
			/** There are not DataCollections then we are not adding a movie to an existing gridSquare **/
		

			/** 
			 *  Data Collection Group with a BLSample represents a GRID 
			 *  Uniqueness of BLSample acronym per grid is assumed 
			 * **/
			DataCollectionGroup3VO group = this.getDataCollectionGroup(sampleAcronym, proposal, beamlineName, session, proteinAcronym);
			
															
			/** Creating data Collection GridSquare**/
			LOG.info("Creating dataCollection for dataCollectionGroup. technique=EM sessionId={} dataCollectionGroupId={}", session.getSessionId(), group.getDataCollectionGroupId());
			DataCollection3VO dataCollection = new DataCollection3VO();
			dataCollection.setDataCollectionGroupVO(group);
			dataCollection.setImageDirectory(movieDirectory);
			dataCollection.setNumberOfImages(Integer.parseInt(noImages));
			dataCollection.setStartTime(startTime);
			dataCollection.setXtalSnapshotFullPath1(gridSquareSnapshotFullPath);
			try{
				dataCollection.setXbeamPix(Double.valueOf(scannedPixelSize));
			}
			catch(Exception exp){
				LOG.error("scannedPixelSize {} can not be converted into a double. technique=EM scannedPixelSize={}", scannedPixelSize, scannedPixelSize);
			}
			
			try{
				dataCollection.setVoltage(Float.valueOf(voltage));
			}
			catch(Exception exp){
				LOG.error("Voltage {} can not be converted into a double. technique=EM voltage={}", voltage, voltage);
			}
			
			if (session.getBeamLineSetupVO() != null){
				try{
					session.getBeamLineSetupVO().setCS(Float.valueOf(sphericalAberration));
				}
				catch(Exception exp){
					LOG.info("Spherical Abberation {} can not be converted into a double. technique=EM voltage={}", sphericalAberration);
				}
			}
			
			if (magnification != null){
				try{
					dataCollection.setMagnification(Integer.valueOf(magnification));
				}
				catch(Exception exp){
					LOG.info("Magnification {} can not be converted into a Integer. technique=EM voltage={}", magnification);
				}
			}
						
//			amplitudeContrast
//			scannedPixelSize						
			gridSquare = dataCollection3Service.create(dataCollection);

		}

		/** Adding movie **/
		Movie movie = new Movie();
		LOG.info("DataCollectionId:" + gridSquare.getDataCollectionId());
		movie.setDataCollectionId(gridSquare.getDataCollectionId());
		movie.setMicrographPath(micrographPath);
		movie.setMoviePath(moviePath);
		movie.setThumbnailMicrographPath(thumbnailMicrographPath);
		movie.setPositionX(positionX);
		movie.setPositionY(positionY);
		movie.setMovieNumber(Integer.parseInt(movieNumber));
		movie.setDosePerImage(dosePerImage);
		movie.setXmlMetaDataPath(xmlMetaDataPath);

		LOG.info("Creating movie. technique=EM moviePath={}", moviePath);
		movie = entityManager.merge(movie);
		LOG.info("Created movie. technique=EM moviePath={} movieId", moviePath, movie.getMovieId());
		
		this.updateSessionLastUpdate(session);

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
		LOG.error("Found no movies for movieFullPath {}. movieFullPath={}", movieFullPath, movieFullPath);
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
				LOG.error("Found no motionCorrection for movieFullPath {}. movieFullPath={}", movieFullPath, movieFullPath);
				return null;
			}
		}
		LOG.error("Found no movies for movieFullPath {}. movieFullPath={}", movieFullPath, movieFullPath);
		return null;
	}

	@Override
	public MotionCorrection addMotionCorrection(String proposal, String movieFullPath, String firstFrame, String lastFrame, String dosePerFrame,
			String doseWeight, String totalMotion, String averageMotionPerFrame, String driftPlotFullPath, String micrographFullPath,
			String micrographSnapshotFullPath, String correctedDoseMicrographFullPath, String logFileFullPath) throws Exception {

		LOG.info("Looking for movie. proposal={} movieFullPath={}", proposal, movieFullPath);
		Movie movie = this.findMovieByMovieFullPath(movieFullPath);
		MotionCorrection motion = null;
		
		if (movie != null) {
			motion = new MotionCorrection();
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
				LOG.info("Creating motion Correction. technique=EM movieFullPath={}", movieFullPath);
				motion = this.entityManager.merge(motion);
				LOG.info("Created motion Correction. technique=EM movieFullPath={}", movieFullPath);
				
				// update the session linked to the gridsquare of this movie
				DataCollection3VO gridSquare = dataCollection3Service.findByPk(movie.getDataCollectionId(), false/*withImage*/, false/*withAutoProcIntegration*/);
				this.updateSessionLastUpdate(gridSquare.getDataCollectionGroupVO().getSessionVO());
				
			} catch (Exception exp) {
				throw exp;
			}			
			
		}
		return motion;
	}

	@Override
	public CTF addCTF(String proposal, String movieFullPath, String spectraImageSnapshotFullPath, String spectraImageFullPath, String defocusU, String defocusV, String angle,
			String crossCorrelationCoefficient, String resolutionLimit, String estimatedBfactor, String logFilePath) {

		LOG.info("Looking for motion correction. proposal={} movieFullPath={}", proposal, movieFullPath);
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
				LOG.info("Creating CTF. technique=EM movieFullPath={}", movieFullPath);
				ctf = this.entityManager.merge(ctf);
				LOG.info("Created CTF. technique=EM movieFullPath={}", movieFullPath);
				return ctf;
			} catch (Exception exp) {
				throw exp;
			}
		}

		return null;
	}
	
	@Override
	public ParticlePicker addParticlePicker(String proposal, String firstMoviePath, String lastMoviePath,
			String pickingProgram, String particlePickingTemplate, String particleDiameter, String numberOfParticles,
			String fullPathToParticleFile) {

		LOG.info("Looking for first motion correction. proposal={} movieFullPath={}", proposal, firstMoviePath);
		MotionCorrection motionFirst = this.findMotionCorrectionByMovieFullPath(firstMoviePath);
		if (motionFirst != null) {
			ParticlePicker particlePicker = new ParticlePicker();
			AutoProcProgram3VO autoProcProgram = new AutoProcProgram3VO();
			autoProcProgram.setProcessingPrograms(pickingProgram);
			autoProcProgram = this.entityManager.merge(autoProcProgram);
			AutoProcProgramAttachment3VO autoProcProgramAttachment = new AutoProcProgramAttachment3VO();
			autoProcProgramAttachment.setAutoProcProgramVO(autoProcProgram);
			autoProcProgramAttachment.setFileName("particleFile");
			autoProcProgramAttachment.setFilePath(fullPathToParticleFile);
			autoProcProgramAttachment.setFileType("Result");
			autoProcProgramAttachment = this.entityManager.merge(autoProcProgramAttachment);
			particlePicker.setProgramId(autoProcProgram.getAutoProcProgramId());
			particlePicker.setFirstMotionCorrectionId(motionFirst.getMotionCorrectionId());
			particlePicker.setParticlePickingTemplate(particlePickingTemplate);
			particlePicker.setParticleDiameter(particleDiameter);
			particlePicker.setNumberOfParticles(numberOfParticles);
			try {
				LOG.info("Creating ParticlePicker. technique=EM");
				particlePicker = this.entityManager.merge(particlePicker);
				LOG.info("Created ParticlePicker. technique=EM");
				return particlePicker;
			} catch (Exception exp) {
				throw exp;
			}
		}

		return null;
	}

	@Override
	public ParticleClassificationGroup addParticleClassificationGroup(String particlePickerId, String type,
			String batchNumber, String numberOfParticlesPerBatch, String numberOfClassesPerBatch, 
			String symmetry, String classificationProgram) {

		ParticleClassificationGroup particleClassificationGroup = new ParticleClassificationGroup();
		AutoProcProgram3VO autoProcProgram = new AutoProcProgram3VO();
		autoProcProgram.setProcessingPrograms(classificationProgram);
		autoProcProgram = this.entityManager.merge(autoProcProgram);
		particleClassificationGroup.setProgramId(autoProcProgram.getAutoProcProgramId());
		particleClassificationGroup.setParticlePickerId(Integer.parseInt(particlePickerId));
		particleClassificationGroup.setType(type);
		particleClassificationGroup.setBatchNumber(batchNumber);
		particleClassificationGroup.setNumberOfParticlesPerBatch(numberOfParticlesPerBatch);
		particleClassificationGroup.setNumberOfClassesPerBatch(numberOfClassesPerBatch);
		particleClassificationGroup.setSymmetry(symmetry);
		try {
			LOG.info("Creating ParticleClassificationGroup technique=EM");
			particleClassificationGroup = this.entityManager.merge(particleClassificationGroup);
			LOG.info("Created ParticleClassificationGroup technique=EM");
			return particleClassificationGroup;
		} catch (Exception exp) {
			throw exp;
		}
	}

	@Override
	public ParticleClassification addParticleClassification(String particleClassificationGroupId, String classNumber, 
			String classImageFullPath, String particlesPerClass, String classDistribution, String rotationAccuracy,
			String translationAccuracy, String estimatedResolution,	String overallFourierCompleteness) {

		ParticleClassification particleClassification = new ParticleClassification();
		particleClassification.setParticleClassificationGroupId(Integer.parseInt(particleClassificationGroupId));
		particleClassification.setClassNumber(classNumber);
		particleClassification.setClassImageFullPath(classImageFullPath);
		particleClassification.setParticlesPerClass(particlesPerClass);
		particleClassification.setClassDistribution(classDistribution);
		particleClassification.setRotationAccuracy(rotationAccuracy);
		particleClassification.setTranslationAccuracy(translationAccuracy);
		particleClassification.setEstimatedResolution(estimatedResolution);
		particleClassification.setOverallFourierCompleteness(overallFourierCompleteness);
		try {
			LOG.info("Creating ParticleClassification. technique=EM");
			particleClassification = this.entityManager.merge(particleClassification);
			LOG.info("Created ParticleClassification. technique=EM");
			return particleClassification;
		} catch (Exception exp) {
			throw exp;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Movie> getMoviesByDataCollectionId(int proposalId, int dataCollectionId) throws Exception{
		List<DataCollection3VO> dataCollections =  dataCollection3Service.findByProposalId(proposalId, dataCollectionId);
		if (dataCollections.size() == 1){
			Session session = (Session) this.entityManager.getDelegate();
			return session.createCriteria(Movie.class).add(Restrictions.eq("dataCollectionId", dataCollectionId)).addOrder(Order.desc("movieId")).list();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Movie getMovieByDataCollectionId(int proposalId, int dataCollectionId, int movieId) throws Exception{
		List<DataCollection3VO> dataCollections =  dataCollection3Service.findByProposalId(proposalId, dataCollectionId);
		if (dataCollections.size() == 1){
			Session session = (Session) this.entityManager.getDelegate();
			return (Movie) session.createCriteria(Movie.class)
					.add(Restrictions.eq("dataCollectionId", dataCollectionId))
					.add(Restrictions.eq("movieId", movieId)).addOrder(Order.asc("movieId")).list().get(0);
		}
		return null;
	}
	
	
	@Override
	public List<String> getDoseByDataCollectionId(int proposalId, int dataCollectionId) throws Exception{
		List<Movie> movies = this.getMoviesByDataCollectionId(proposalId, dataCollectionId);
		List<String> doses = new ArrayList<String>();
		for (Movie movie : movies) {
			doses.add(movie.getDosePerImage());
		}
		return doses;
	}

	@Override
	public MotionCorrection getMotionCorrectionByMovieId(int proposalId, int dataCollectionId, int movieId) throws Exception {
		Movie movie = this.getMovieByDataCollectionId(proposalId, dataCollectionId, movieId);
		if (movie != null){
			Session session = (Session) this.entityManager.getDelegate();
			@SuppressWarnings("unchecked")
			List<MotionCorrection> motionCorrectionList = session.createCriteria(MotionCorrection.class).add(Restrictions.eq("movieId", movie.getMovieId())).addOrder(Order.desc("motionCorrectionId")).list();
			if (motionCorrectionList.size() > 0){
				return motionCorrectionList.get(0);
			}
		}
		return null;
	}

	@Override
	public CTF getCTFByMovieId(int proposalId, int dataCollectionId, int movieId) throws Exception {
		MotionCorrection motion = this.getMotionCorrectionByMovieId(proposalId, dataCollectionId, movieId);
		if (motion != null){
			Session session = (Session) this.entityManager.getDelegate();
			@SuppressWarnings("unchecked")
			List<CTF> ctfs = session.createCriteria(CTF.class).add(Restrictions.eq("motionCorrectionId", motion.getMotionCorrectionId())).addOrder(Order.desc("CTFid")).list();
			if (ctfs.size() > 0){
				return ctfs.get(0);
			}
	
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getStatsByDataCollectionIds(int proposalId, String dataCollectionIdList) {
		
		
		Session session = (Session) this.entityManager.getDelegate();
		String queryString = StatsByDataCollectionId.replace(":dataCollectionIdList", dataCollectionIdList).replace(":proposalId", String.valueOf(proposalId));
		System.out.println(queryString);
		SQLQuery query = session.createSQLQuery(queryString);
		return executeSQLQuery(query);
		
	}

	@Override
	public Collection<? extends Map<String, Object>> getStatsByDataDataCollectionGroupId(Integer dataCollectionGroupId) {
		Session session = (Session) this.entityManager.getDelegate();
		String queryString = StatsByDataCollectionGroupId.replace(":dataCollectionGroupId", String.valueOf(dataCollectionGroupId));
		System.out.println(queryString);
		SQLQuery query = session.createSQLQuery(queryString);
		return executeSQLQuery(query);
	}

	@Override
	public List<Map<String, Object>> getStatsBySessionId(int proposalId, int sessionId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(getStatsBySessionId);
		System.out.println(getStatsBySessionId);
		query.setParameter("sessionId", sessionId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}	
	
	@Override
	public List<Map<String, Object>> getClassificationBySessionId(int proposalId, int sessionId) {
		Session session = (Session) this.entityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(getClassificationBySessionId);
		System.out.println(getClassificationBySessionId);
		query.setParameter("sessionId", sessionId);
		query.setParameter("proposalId", proposalId);
		return executeSQLQuery(query);
	}	

	@Override
	public ParticleClassification getClassificationByClassificationId(int proposalId, int particleClassificationId) {
		Session session = (Session) this.entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<ParticleClassification> ParticleClassificationList = session.createCriteria(ParticleClassification.class).add(Restrictions.eq("particleClassificationId", particleClassificationId)).list();
		if (ParticleClassificationList.size() > 0){
			return ParticleClassificationList.get(0);
		}
		return null;
	}
	
	private void updateSessionLastUpdate(Session3VO vo) throws Exception {
		try {			
			Timestamp currentTimeStamp = StringUtils.getCurrentTimeStamp();
			
			if (vo == null || (vo.getLastUpdate() != null && vo.getLastUpdate().after(currentTimeStamp))) {
				// do nothing
				return;
			}									
			vo.setLastUpdate(currentTimeStamp);
			session3Service.update(vo);						
			LOG.debug("Session updated " + vo.getSessionId());	
			return ;

		} catch (Exception e) {
			LOG.error("ERROR: updateSessionLastUpdate - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
		
	}


}


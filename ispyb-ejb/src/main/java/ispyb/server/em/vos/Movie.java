package ispyb.server.em.vos;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "Movie")
public class Movie implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name = "movieId")
	protected Integer movieId;
	
	@Column(name = "movieNumber")
	protected Integer movieNumber;
	
	@Column(name = "dataCollectionId")
	protected Integer dataCollectionId;
	
	@Column(name = "movieFullPath")
	protected String movieFullPath;
	
	@Column(name = "positionX")
	protected String positionX;
	
	@Column(name = "positionY")
	protected String positionY;
	
	@Column(name = "micrographFullPath")
	protected String micrographFullPath;
	
	@Column(name = "micrographSnapshotFullPath")
	protected String thumbnailMicrographFullPath;
	
	@Column(name = "xmlMetaDataFullPath")
	protected String xmlMetaDataFullPath;
	
	@Column(name = "dosePerImage")
	protected String dosePerImage;
	
	@Column(name = "createdTimeStamp")
	protected Date createdTimeStamp;

	
	public Integer getMovieId() {
		return movieId;
	}

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	public Integer getMovieNumber() {
		return movieNumber;
	}

	public void setMovieNumber(Integer movieNumber) {
		this.movieNumber = movieNumber;
	}

	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public String getMoviePath() {
		return movieFullPath;
	}

	public void setMoviePath(String moviePath) {
		this.movieFullPath = moviePath;
	}

	public String getPositionX() {
		return positionX;
	}

	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}

	public String getPositionY() {
		return positionY;
	}

	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}

	public String getMicrographPath() {
		return micrographFullPath;
	}

	public void setMicrographPath(String micrographPath) {
		this.micrographFullPath = micrographPath;
	}

	public String getThumbnailMicrographPath() {
		return thumbnailMicrographFullPath;
	}

	public void setThumbnailMicrographPath(String thumbnailMicrographPath) {
		this.thumbnailMicrographFullPath = thumbnailMicrographPath;
	}

	public String getXmlMetaDataPath() {
		return xmlMetaDataFullPath;
	}

	public void setXmlMetaDataPath(String xmlMetaDataPath) {
		this.xmlMetaDataFullPath = xmlMetaDataPath;
	}

	public String getDosePerImage() {
		return dosePerImage;
	}

	public void setDosePerImage(String dosePerImage) {
		this.dosePerImage = dosePerImage;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}
	 

	

}

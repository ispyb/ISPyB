# How to configure ejb3 for ISPyB

## Changes in ejbs

Create 3 ejbs for service: 1 interface remote, 1 interface local and 1
implementation.

New ejb:

```java
package ispyb.server.data.ejb3.services.sample;

import javax.ejb.Local;

@Local
public interface BLSample3ServiceLocal extends BLSample3Service {

}
```

## Manage the links in ejbs

### Declare all links whenever they exist

Use the `Set` to retrieve the collection of objects linked:

```java
@Fetch(value = FetchMode.SELECT)
@OneToMany
@JoinColumn(name = "shippingId")
private Set<Dewar3VO> dewarVOs;
```

The annotation `@Fetch(value = FetchMode.SELECT)` is used in case of multiple
eager collections in an hibernate objects to limit the number of queries and
the duplication of retrieved objects

Add a getter for the pk of the parent Object with a different name.

Example:

```java
public Integer getProposalVOId() {
  return proposalVO == null ? null : proposalVO.getProposalId();
}
```

### Create the "light" WS object for webservices

Example:

```java
public class BLSampleWS3VO extends BLSample3VO {

  private static final long serialVersionUID = 1L;

  private Integer crystalId;

  public BLSampleWS3VO() {
    super();
  }

  public BLSampleWS3VO(BLSample3VO vo) {
    super(vo);
  }

  public Integer getCrystalId() {
    return crystalId;
  }

  public void setCrystalId(Integer crystalId) {
    this.crystalId = crystalId;
  }
}
```

**Add the following methods in the VO:**

```java
public void fillVOFromWS(BLSampleWS3VO vo) {
  this.blSampleId = vo.getBlSampleId();
  this.diffractionPlanId = vo.getDiffractionPlanId();
  this.crystalVO = null;
  this.containerId = vo.getContainerId();
  this.name = vo.getName();
  this.code = vo.getCode();
  this.location = vo.getLocation();
  this.holderLength = vo.getHolderLength();
  this.loopLength = vo.getLoopLength();
  this.loopType = vo.getLoopType();
  this.wireWidth = vo.getWireWidth();
  this.comments = vo.getComments();
  this.completionStage = vo.getCompletionStage();
  this.structureStage = vo.getStructureStage();
  this.publicationStage = vo.getPublicationStage();
  this.publicationComments = vo.getPublicationComments();
  this.blSampleStatus = vo.getBlSampleStatus();
  this.isInSampleChanger = vo.getIsInSampleChanger();
  this.lastKnownCenteringPosition = vo.getLastKnownCenteringPosition();
}

@Override
public Object clone() throws CloneNotSupportedException {
  // TODO Auto-generated method stub
  return super.clone();
}
```

The clone will be used to detach the vos and create "light" WS vos when called
for webservices and be able to set all linked Sets to null

**In the serviceBean:**

The first method is used to detach and put all Sets to null:

```java
private BLSample3VO getLightBLSampleVO(BLSample3VO vo) throws
    CloneNotSupportedException {
  BLSample3VO otherVO = (BLSample3VO) vo.clone();
  otherVO.setDataCollectionVOs(null);
  return otherVO;
}
```

The second method is used to set the Id of the WS VO:

```java
private BLSampleWS3VO getWSBLSampleVO(BLSample3VO vo) throws
    CloneNotSupportedException {
  BLSample3VO otherVO = getLightBLSampleVO(vo);
  Integer crystalId = null;
  crystalId = otherVO.getCrystalVOId();
  otherVO.setCrystalVO(null);
  BLSampleWS3VO wsSample = new BLSampleWS3VO(otherVO);
  wsSample.setCrystalId(crystalId);
  return wsSample;
}
```

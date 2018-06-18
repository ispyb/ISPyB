select dataCollectionId, 
(select count(*) from Movie where Movie.dataCollectionId = DataCollection.dataCollectionId) as movieCount,

(select count(*) from MotionCorrection 
inner join Movie on Movie.movieId =  MotionCorrection.movieId 
where Movie.dataCollectionId = DataCollection.dataCollectionId) as motionCorrectionCount,

(select count(*) from CTF 
inner join MotionCorrection on MotionCorrection.motionCorrectionId =  CTF.motionCorrectionId 
inner join Movie on Movie.movieId =  MotionCorrection.movieId 
where Movie.dataCollectionId = DataCollection.dataCollectionId) as ctfCorrectionCount


from DataCollection
INNER JOIN DataCollectionGroup on  DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId
INNER JOIN BLSession on  BLSession.sessionId = DataCollectionGroup.sessionId

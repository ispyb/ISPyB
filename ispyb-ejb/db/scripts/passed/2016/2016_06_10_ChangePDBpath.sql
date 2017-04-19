use pydb;
-- only for ESRF
UPDATE Crystal 
SET pdbFilePath = replace(pdbFilePath, "/data/pyapdb/","/data/pyarch/pdb/" ) 
WHERE pdbFileName IS NOT NULL;




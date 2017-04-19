use pydb;
-- source DBMerging_DLS/20160630_ScreeningStrategyWedge_chi.sql;
alter table `ScreeningStrategyWedge` add column `chi` float NULL AFTER `kappa`;
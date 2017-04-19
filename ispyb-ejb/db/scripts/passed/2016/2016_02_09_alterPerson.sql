ALTER TABLE `Person` ADD `siteId` INT NULL AFTER `laboratoryId` ;

ALTER TABLE `Person` ADD INDEX ( `siteId` ) ;



ALTER TABLE `bsar`.`moduleschedule` 
ADD COLUMN `time_one` VARCHAR(5) NULL AFTER `module_scheduled`,
ADD COLUMN `time_two` VARCHAR(5) NULL AFTER `time_one`,
ADD COLUMN `time_three` VARCHAR(5) NULL AFTER `time_two`,
ADD COLUMN `time_four` VARCHAR(5) NULL AFTER `time_three`,
ADD COLUMN `time_five` VARCHAR(5) NULL AFTER `time_four`;

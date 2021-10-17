-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema trend
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema trend
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `trend` DEFAULT CHARACTER SET utf8 ;
USE `trend` ;

-- -----------------------------------------------------
-- Table `trend`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trend`.`member` (
  `mid` BIGINT NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `nickname` VARCHAR(45) NOT NULL,
  `authority` VARCHAR(45) NOT NULL,
  `flag` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`mid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trend`.`review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trend`.`review` (
  `rid` BIGINT NOT NULL AUTO_INCREMENT,
  `mid` BIGINT NULL DEFAULT NULL,
  `dong` VARCHAR(45) NULL DEFAULT NULL,
  `store` VARCHAR(45) NULL DEFAULT NULL,
  `content` VARCHAR(45) NULL DEFAULT NULL,
  `reg_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `star_score` FLOAT NULL DEFAULT NULL,
  `flag` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`rid`),
  INDEX `fk_review_uid_idx` (`mid` ASC) VISIBLE,
  CONSTRAINT `fk_review_id`
    FOREIGN KEY (`mid`)
    REFERENCES `trend`.`member` (`mid`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 14422
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trend`.`file`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trend`.`file` (
  `fid` BIGINT NOT NULL AUTO_INCREMENT,
  `rid` BIGINT NULL DEFAULT NULL,
  `file_name` VARCHAR(200) NULL DEFAULT NULL,
  `file_size` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`fid`),
  INDEX `fk_file_id_idx` (`rid` ASC) VISIBLE,
  CONSTRAINT `fk_file_id`
    FOREIGN KEY (`rid`)
    REFERENCES `trend`.`review` (`rid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `trend`.`google`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trend`.`google` (
  `gid` BIGINT NOT NULL,
  `google_keyword` VARCHAR(45) NOT NULL,
  `google_review_date` VARCHAR(45) NULL DEFAULT NULL,
  `google_stars` INT NULL DEFAULT NULL,
  `google_star_avg` FLOAT NULL DEFAULT NULL,
  `google_review_txt` VARCHAR(500) NULL DEFAULT NULL,
  `google_emotion` INT NULL DEFAULT NULL,
  PRIMARY KEY (`gid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `trend`.`store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trend`.`store` (
  `sid` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `area` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`sid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

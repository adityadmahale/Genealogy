-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema genealogy
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema genealogy
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `genealogy` DEFAULT CHARACTER SET utf8 ;
USE `genealogy` ;

-- -----------------------------------------------------
-- Table `genealogy`.`person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`person` (
  `person_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`location` (
  `location_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`location_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`occupation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`occupation` (
  `occupation_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`occupation_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`person_attribute`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`person_attribute` (
  `person_id` INT NOT NULL,
  `date_of_birth` DATE NULL,
  `date_of_death` DATE NULL,
  `gender` CHAR(1) NULL,
  `location_id_birth` INT NULL,
  `location_id_death` INT NULL,
  `occupation_id` INT NULL,
  PRIMARY KEY (`person_id`),
  INDEX `fk_person_attribute_location1_idx` (`location_id_birth` ASC),
  INDEX `fk_person_attribute_location2_idx` (`location_id_death` ASC),
  INDEX `fk_person_attribute_occupation1_idx` (`occupation_id` ASC),
  CONSTRAINT `fk_person_attribute_person`
    FOREIGN KEY (`person_id`)
    REFERENCES `genealogy`.`person` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_attribute_location1`
    FOREIGN KEY (`location_id_birth`)
    REFERENCES `genealogy`.`location` (`location_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_attribute_location2`
    FOREIGN KEY (`location_id_death`)
    REFERENCES `genealogy`.`location` (`location_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_attribute_occupation1`
    FOREIGN KEY (`occupation_id`)
    REFERENCES `genealogy`.`occupation` (`occupation_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`reference`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`reference` (
  `reference_id` INT NOT NULL AUTO_INCREMENT,
  `source` VARCHAR(500) NOT NULL,
  `person_id` INT NOT NULL,
  `record_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`reference_id`),
  INDEX `fk_reference_person1_idx` (`person_id` ASC),
  CONSTRAINT `fk_reference_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `genealogy`.`person` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`note`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`note` (
  `note_id` INT NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(500) NOT NULL,
  `person_id` INT NOT NULL,
  `record_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`note_id`),
  INDEX `fk_note_person1_idx` (`person_id` ASC),
  CONSTRAINT `fk_note_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `genealogy`.`person` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`media`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`media` (
  `media_id` INT NOT NULL AUTO_INCREMENT,
  `file_location` VARCHAR(500) NOT NULL,
  UNIQUE INDEX `file_location_UNIQUE` (`file_location` ASC),
  PRIMARY KEY (`media_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`city`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`city` (
  `city_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`city_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`media_attribute`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`media_attribute` (
  `media_id` INT NOT NULL,
  `year` VARCHAR(4) NULL,
  `date` DATE NULL,
  `city_id` INT NOT NULL,
  PRIMARY KEY (`media_id`),
  INDEX `fk_media_attribute_city1_idx` (`city_id` ASC),
  CONSTRAINT `fk_media_attribute_media1`
    FOREIGN KEY (`media_id`)
    REFERENCES `genealogy`.`media` (`media_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_media_attribute_city1`
    FOREIGN KEY (`city_id`)
    REFERENCES `genealogy`.`city` (`city_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`tag` (
  `tag_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`media_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`media_tag` (
  `tag_id` INT NOT NULL,
  `media_id` INT NOT NULL,
  INDEX `fk_media_tag_tag1_idx` (`tag_id` ASC),
  INDEX `fk_media_tag_media1_idx` (`media_id` ASC),
  PRIMARY KEY (`tag_id`, `media_id`),
  CONSTRAINT `fk_media_tag_tag1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `genealogy`.`tag` (`tag_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_media_tag_media1`
    FOREIGN KEY (`media_id`)
    REFERENCES `genealogy`.`media` (`media_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`person_in_media`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`person_in_media` (
  `person_id` INT NOT NULL,
  `media_id` INT NOT NULL,
  INDEX `fk_person_in_media_person1_idx` (`person_id` ASC),
  INDEX `fk_person_in_media_media1_idx` (`media_id` ASC),
  PRIMARY KEY (`person_id`, `media_id`),
  CONSTRAINT `fk_person_in_media_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `genealogy`.`person` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_in_media_media1`
    FOREIGN KEY (`media_id`)
    REFERENCES `genealogy`.`media` (`media_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`partner`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`partner` (
  `person_id` INT NOT NULL,
  `partner_id` INT NOT NULL,
  PRIMARY KEY (`person_id`),
  INDEX `fk_partner_person1_idx` (`person_id` ASC),
  UNIQUE INDEX `partner_id_UNIQUE` (`partner_id` ASC),
  CONSTRAINT `fk_partner_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `genealogy`.`person` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `genealogy`.`child`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `genealogy`.`child` (
  `person_id` INT NOT NULL,
  `child_id` INT NOT NULL,
  INDEX `fk_child_person1_idx` (`person_id` ASC),
  PRIMARY KEY (`child_id`, `person_id`),
  CONSTRAINT `fk_child_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `genealogy`.`person` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

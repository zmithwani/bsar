-- MySQL dump 10.13  Distrib 5.7.22, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bsar
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.35-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `fingerprint`
--

DROP TABLE IF EXISTS `fingerprint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fingerprint` (
  `finger_print_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`finger_print_id`),
  KEY `userID` (`user_id`),
  CONSTRAINT `fingerprint_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fingerprint`
--

LOCK TABLES `fingerprint` WRITE;
/*!40000 ALTER TABLE `fingerprint` DISABLE KEYS */;
/*!40000 ALTER TABLE `fingerprint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `module`
--

DROP TABLE IF EXISTS `module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `module` (
  `module_id` int(11) NOT NULL AUTO_INCREMENT,
  `module_name` varchar(255) NOT NULL,
  `module_code` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`module_id`),
  UNIQUE KEY `moduleName` (`module_name`),
  UNIQUE KEY `moduleCode` (`module_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `module`
--

LOCK TABLES `module` WRITE;
/*!40000 ALTER TABLE `module` DISABLE KEYS */;
INSERT INTO `module` VALUES (1,'SCIENCE','SCI','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(2,'COMPUTER SCIENCE','COMP','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(3,'OTHER','OTH','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL);
/*!40000 ALTER TABLE `module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `moduleactivity`
--

DROP TABLE IF EXISTS `moduleactivity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moduleactivity` (
  `module_activity_id` int(11) NOT NULL AUTO_INCREMENT,
  `module_id` int(11) DEFAULT NULL,
  `module_activity` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`module_activity_id`),
  KEY `moduleID` (`module_id`),
  CONSTRAINT `moduleactivity_ibfk_1` FOREIGN KEY (`module_id`) REFERENCES `module` (`module_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moduleactivity`
--

LOCK TABLES `moduleactivity` WRITE;
/*!40000 ALTER TABLE `moduleactivity` DISABLE KEYS */;
INSERT INTO `moduleactivity` VALUES (1,1,'CLASSROOM','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(2,1,'PRACTICAL ','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(3,2,'THEORY','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(4,2,'LAB','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL);
/*!40000 ALTER TABLE `moduleactivity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `moduleschedule`
--

DROP TABLE IF EXISTS `moduleschedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moduleschedule` (
  `module_schedule_id` int(11) NOT NULL AUTO_INCREMENT,
  `module_activity_id` int(11) DEFAULT NULL,
  `module_scheduled` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`module_schedule_id`),
  KEY `moduleActivityID` (`module_activity_id`),
  CONSTRAINT `moduleschedule_ibfk_1` FOREIGN KEY (`module_activity_id`) REFERENCES `moduleactivity` (`module_activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moduleschedule`
--

LOCK TABLES `moduleschedule` WRITE;
/*!40000 ALTER TABLE `moduleschedule` DISABLE KEYS */;
INSERT INTO `moduleschedule` VALUES (1,1,'2020-02-18 18:30:00','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(2,2,'2020-02-18 18:30:00','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(3,3,'2020-02-18 18:30:00','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(4,4,'2020-02-18 18:30:00','2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL);
/*!40000 ALTER TABLE `moduleschedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email_address` varchar(255) NOT NULL,
  `user_type_id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `locked` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `userTypeID` (`user_type_id`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`user_type_id`) REFERENCES `usertype` (`user_type_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (115,'admin123','pass1234','smartsystems1720@rediffmail.com',1,NULL,NULL,NULL,NULL,'ACTIVE'),(120,'tuytuytutyuy','O1eEvzG5','smaer@ytuytutyu.tyyutyuty',2,'2020-02-18 13:56:26',NULL,'2020-02-20 11:59:04',NULL,'ACTIVE'),(121,'uyyuuy','TPz8dbEB','uy@tytytyty.tyy',3,'2020-02-18 13:57:02',NULL,'2020-02-20 12:49:49',NULL,'ACTIVE');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usermodule`
--

DROP TABLE IF EXISTS `usermodule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usermodule` (
  `user_id` int(11) DEFAULT NULL,
  `module_id` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  KEY `userID` (`user_id`),
  KEY `moduleID` (`module_id`),
  CONSTRAINT `usermodule_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `usermodule_ibfk_2` FOREIGN KEY (`module_id`) REFERENCES `module` (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usermodule`
--

LOCK TABLES `usermodule` WRITE;
/*!40000 ALTER TABLE `usermodule` DISABLE KEYS */;
INSERT INTO `usermodule` VALUES (120,1,'2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(120,2,'2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(121,1,'2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL),(121,2,'2020-02-18 18:30:00',NULL,'2020-02-18 18:30:00',NULL);
/*!40000 ALTER TABLE `usermodule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usertype`
--

DROP TABLE IF EXISTS `usertype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usertype` (
  `user_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type_name` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_type_id`),
  UNIQUE KEY `userTypeName` (`user_type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usertype`
--

LOCK TABLES `usertype` WRITE;
/*!40000 ALTER TABLE `usertype` DISABLE KEYS */;
INSERT INTO `usertype` VALUES (1,'ADMIN','2020-02-18 00:00:00',NULL,'2020-02-18 00:00:00',NULL),(2,'TUTOR','2020-02-18 00:00:00',NULL,'2020-02-18 00:00:00',NULL),(3,'STUDENT','2020-02-18 00:00:00',NULL,'2020-02-18 00:00:00',NULL);
/*!40000 ALTER TABLE `usertype` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-25 18:26:50

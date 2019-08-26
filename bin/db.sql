-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: intan
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Mon, 26 Aug 2019 07:57:13 +0200

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
-- Table structure for table `admin`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(191) NOT NULL,
  `password` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `admin` VALUES (9,'admin','21232f297a57a5a743894a0e4a801fc3');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `admin` with 1 row(s)
--

--
-- Table structure for table `angsuran`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `angsuran` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_pembiayaan` int(11) NOT NULL,
  `nama` varchar(191) NOT NULL,
  `tanggal` date NOT NULL,
  `pokok` int(11) NOT NULL,
  `basil` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `angsuran`
--

LOCK TABLES `angsuran` WRITE;
/*!40000 ALTER TABLE `angsuran` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `angsuran` VALUES (2,1,'dfsadf','2019-08-09',124124,1241241240),(3,1,'dfsadf','2019-08-09',124124,1241241240),(4,1,'dfsadf','2019-08-09',124124,1241241240),(5,1,'dfsadf','2019-08-09',124124,1241241240),(6,1,'dfsadf','2019-08-09',124124,1241241240),(7,1,'dfsadf','2019-08-09',124124,1241241240),(8,1,'dfsadf','2019-08-09',124124,1241241240),(9,1,'dfsadf','2019-08-09',124124,1241241240),(10,1,'dfsadf','2019-08-09',124124,1241241240),(11,1,'dfsadf','2019-08-09',124124,1241241240),(12,1,'dfsadf','2019-08-09',124124,1241241240),(13,1,'dfsadf','2019-08-09',124124,1241241240),(14,1,'dfsadf','2019-08-09',124124,1241241240),(15,1,'dfsadf','2019-08-09',124124,1241241240),(16,1,'dfsadf','2019-08-09',124124,1241241240),(17,1,'dfsadf','2019-08-09',124124,1241241240),(18,1,'dfsadf','2019-08-09',124124,1241241240),(19,1,'dfsadf','2019-08-09',124124,1241241240),(20,1,'dfsadf','2019-08-09',124124,1241241240),(21,1,'dfsadf','2019-08-09',124124,1241241240),(22,1,'dfsadf','2019-08-09',124124,1241241240),(23,1,'dfsadf','2019-08-09',124124,1241241240),(24,1,'dfsadf','2019-08-09',124124,1241241240),(25,1,'dfsadf','2019-08-09',124124,1241241240),(26,1,'dfsadf','2019-08-09',124124,1241241240),(27,1,'dfsadf','2019-08-09',124124,1241241240),(28,1,'dfsadf','2019-08-09',124124,1241241240),(29,1,'dfsadf','2019-08-09',124124,1241241240),(30,1,'dfsadf','2019-08-09',124124,1241241240),(31,1,'dfsadf','2019-08-09',124124,1241241240),(32,1,'dfsadf','2019-08-09',124124,1241241240),(33,1,'dfsadf','2019-08-09',124124,1241241240),(34,1,'dfsadf','2019-08-09',124124,1241241240),(35,1,'dfsadf','2019-08-09',124124,1241241240),(36,1,'dfsadf','2019-08-09',124124,1241241240),(37,1,'dfsadf','2019-08-09',124124,1241241240),(38,1,'dfsadf','2019-08-09',124124,1241241240),(39,1,'dfsadf','2019-08-09',124124,1241241240),(40,1,'dfsadf','2019-08-09',124124,1241241240),(41,1,'dfsadf','2019-08-09',124124,1241241240),(42,1,'dfsadf','2019-08-09',124124,1241241240),(43,1,'dfsadf','2019-08-09',124124,1241241240),(44,1,'dfsadf','2019-08-09',124124,1241241240),(45,1,'dfsadf','2019-08-09',124124,1241241240),(46,1,'dfsadf','2019-08-09',124124,1241241240),(47,1,'dfsadf','2019-08-09',124124,1241241240),(48,1,'dfsadf','2019-08-09',124124,1241241240),(49,1,'dfsadf','2019-08-09',124124,1241241240),(50,1,'dfsadf','2019-08-09',124124,1241241240),(51,1,'dfsadf','2019-08-09',124124,1241241240),(52,1,'dfsadf','2019-08-09',124124,1241241240),(53,1,'dfsadf','2019-08-09',124124,1241241240),(54,1,'dfsadf','2019-08-09',124124,1241241240),(55,1,'dfsadf','2019-08-09',124124,1241241240),(56,1,'dfsadf','2019-08-09',124124,1241241240),(57,1,'dfsadf','2019-08-09',124124,1241241240),(58,1,'dfsadf','2019-08-09',124124,1241241240),(59,1,'dfsadf','2019-08-09',124124,1241241240),(60,1,'dfsadf','2019-08-09',124124,1241241240),(61,1,'dfsadf','2019-08-09',124124,1241241240),(62,1,'dfsadf','2019-08-09',124124,1241241240),(63,1,'dfsadf','2019-08-09',124124,1241241240),(64,1,'dfsadf','2019-08-09',124124,1241241240),(65,1,'dfsadf','2019-08-09',124124,1241241240),(66,1,'dfsadf','2019-08-09',124124,1241241240),(67,1,'dfsadf','2019-08-09',124124,1241241240),(68,1,'dfsadf','2019-08-09',124124,1241241240),(69,1,'dfsadf','2019-08-09',124124,1241241240),(70,1,'dfsadf','2019-08-09',124124,1241241240),(71,1,'dfsadf','2019-08-09',124124,1241241240),(72,1,'dfsadf','2019-08-09',124124,1241241240),(73,1,'dfsadf','2019-08-09',124124,1241241240),(74,1,'dfsadf','2019-08-09',124124,1241241240),(75,1,'dfsadf','2019-08-09',124124,1241241240),(76,1,'dfsadf','2019-08-09',124124,1241241240),(77,1,'dfsadf','2019-08-09',124124,1241241240),(78,1,'dfsadf','2019-08-09',124124,1241241240),(79,1,'dfsadf','2019-08-09',124124,1241241240),(80,1,'dfsadf','2019-08-09',124124,1241241240),(81,1,'dfsadf','2019-08-09',124124,1241241240),(82,1,'dfsadf','2019-08-09',124124,1241241240),(83,1,'dfsadf','2019-08-09',124124,1241241240),(84,1,'dfsadf','2019-08-09',124124,1241241240),(85,1,'dfsadf','2019-08-09',124124,1241241240),(86,1,'dfsadf','2019-08-09',124124,1241241240),(87,1,'dfsadf','2019-08-09',124124,1241241240),(88,1,'dfsadf','2019-08-09',124124,1241241240),(89,1,'dfsadf','2019-08-09',124124,1241241240),(90,1,'dfsadf','2019-08-09',124124,1241241240),(91,1,'dfsadf','2019-08-09',124124,1241241240),(92,1,'dfsadf','2019-08-09',124124,1241241240),(93,1,'dfsadf','2019-08-09',124124,1241241240),(94,1,'dfsadf','2019-08-09',124124,1241241240),(95,1,'dfsadf','2019-08-09',124124,1241241240),(96,1,'dfsadf','2019-08-09',124124,1241241240),(97,1,'dfsadf','2019-08-09',124124,1241241240),(98,1,'dfsadf','2019-08-09',124124,1241241240),(99,1,'dfsadf','2019-08-09',124124,1241241240),(100,1,'dfsadf','2019-08-09',124124,1241241240),(101,1,'dfsadf','2019-08-09',124124,1241241240),(102,1,'dfsadf','2019-08-09',124124,1241241240),(103,1,'dfsadf','2019-08-09',124124,1241241240),(104,1,'dfsadf','2019-08-09',124124,1241241240),(105,1,'dfsadf','2019-08-09',124124,1241241240),(106,1,'dfsadf','2019-08-09',124124,1241241240),(107,1,'dfsadf','2019-08-09',124124,1241241240),(108,1,'dfsadf','2019-08-09',124124,1241241240),(109,1,'dfsadf','2019-08-09',124124,1241241240),(110,1,'dfsadf','2019-08-09',124124,1241241240),(111,1,'dfsadf','2019-08-09',124124,1241241240),(112,1,'dfsadf','2019-08-09',124124,1241241240),(113,1,'dfsadf','2019-08-09',124124,1241241240),(114,1,'dfsadf','2019-08-09',124124,1241241240),(115,1,'dfsadf','2019-08-09',124124,1241241240),(116,1,'dfsadf','2019-08-09',124124,1241241240);
/*!40000 ALTER TABLE `angsuran` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `angsuran` with 115 row(s)
--

--
-- Table structure for table `pembiayaan`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pembiayaan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `no_pembiayaan` varchar(191) NOT NULL,
  `nama` varchar(191) NOT NULL,
  `tanggal` date NOT NULL,
  `plafon` int(11) NOT NULL,
  `jatuh_tempo` date NOT NULL,
  `basil` int(11) NOT NULL,
  `pokok` int(11) NOT NULL,
  `administrasi` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pembiayaan`
--

LOCK TABLES `pembiayaan` WRITE;
/*!40000 ALTER TABLE `pembiayaan` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `pembiayaan` VALUES (1,'111','Test 1','2019-08-01',1000000,'2020-08-03',2000000,3000000,4000000),(2,'1112','Test 12','2019-02-01',1000000,'2020-09-03',2000000,3000000,4000000);
/*!40000 ALTER TABLE `pembiayaan` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `pembiayaan` with 2 row(s)
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on: Mon, 26 Aug 2019 07:57:13 +0200

-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: intan
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Tue, 03 Sep 2019 05:14:33 +0200

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
-- Table structure for table `anggota`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anggota` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(191) NOT NULL,
  `alamat` varchar(191) NOT NULL,
  `no_ktp` varchar(191) NOT NULL,
  `pekerjaan` varchar(191) NOT NULL,
  `no_hp` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anggota`
--

LOCK TABLES `anggota` WRITE;
/*!40000 ALTER TABLE `anggota` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `anggota` VALUES (4,'Test','124124','124124','124124','124124'),(5,'AFASF','asfafas','fasfas','fasfas','fasfasf');
/*!40000 ALTER TABLE `anggota` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `anggota` with 2 row(s)
--

--
-- Table structure for table `angsuran`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `angsuran` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_pembiayaan` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `pokok` int(11) NOT NULL,
  `basil` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_pembiayaan` (`id_pembiayaan`),
  CONSTRAINT `angsuran_ibfk_1` FOREIGN KEY (`id_pembiayaan`) REFERENCES `pembiayaan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=601 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `angsuran`
--

LOCK TABLES `angsuran` WRITE;
/*!40000 ALTER TABLE `angsuran` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `angsuran` VALUES (599,19,'2019-09-25',666666,50000),(600,19,'2019-09-20',666666,50000);
/*!40000 ALTER TABLE `angsuran` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `angsuran` with 2 row(s)
--

--
-- Table structure for table `pembiayaan`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pembiayaan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_anggota` int(11) NOT NULL,
  `no_pembiayaan` varchar(191) NOT NULL,
  `tanggal` date NOT NULL,
  `waktu` int(11) NOT NULL,
  `plafon` int(11) NOT NULL,
  `jatuh_tempo` date NOT NULL,
  `basil` int(11) NOT NULL,
  `pokok` int(11) NOT NULL,
  `administrasi` int(11) NOT NULL,
  `jaminan` varchar(191) NOT NULL,
  `tanggal_pelunasan` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_anggota` (`id_anggota`),
  CONSTRAINT `pembiayaan_ibfk_1` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pembiayaan`
--

LOCK TABLES `pembiayaan` WRITE;
/*!40000 ALTER TABLE `pembiayaan` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `pembiayaan` VALUES (19,5,'123','2019-07-31',3,2000000,'2019-10-31',150000,666666,75000,'Motor',NULL);
/*!40000 ALTER TABLE `pembiayaan` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `pembiayaan` with 1 row(s)
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on: Tue, 03 Sep 2019 05:14:33 +0200

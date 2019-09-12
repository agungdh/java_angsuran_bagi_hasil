-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: intan
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Thu, 12 Sep 2019 08:48:50 +0200

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
  `id_admin` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(191) NOT NULL,
  `password` varchar(191) NOT NULL,
  PRIMARY KEY (`id_admin`)
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
  `id_anggota` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(191) NOT NULL,
  `alamat` varchar(191) NOT NULL,
  `no_ktp` varchar(191) NOT NULL,
  `pekerjaan` varchar(191) NOT NULL,
  `no_hp` varchar(191) NOT NULL,
  PRIMARY KEY (`id_anggota`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anggota`
--

LOCK TABLES `anggota` WRITE;
/*!40000 ALTER TABLE `anggota` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `anggota` VALUES (7,'aulia','metro','1234576','kuliah','086521'),(10,'intan','metro','1234567890','mahasiswa','085766747310'),(15,'fahmi','gaya baru','12345678','mahasiswa','08756652'),(16,'ani','metro','12345678','dagang','086527432'),(17,'endang','lampung','1807065212760004','pedagang','082372173713'),(18,'dedi irawan','metro','18065331','dosen','08576666'),(19,'aaaaaa','jdsfjks','jgd','sjfjsd','jdsds'),(21,'fitria','gb','1234556','kuliah','09723'),(22,'cahyani','df','45t','fsds','335'),(23,'ini','fgd','535','fdg','44'),(24,'intan fitria','fef','3423','sadf','24');
/*!40000 ALTER TABLE `anggota` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `anggota` with 11 row(s)
--

--
-- Table structure for table `angsuran`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `angsuran` (
  `id_angsuran` int(11) NOT NULL AUTO_INCREMENT,
  `id_pembiayaan` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `pokok` int(11) NOT NULL,
  `basil` int(11) NOT NULL,
  PRIMARY KEY (`id_angsuran`),
  KEY `id_pembiayaan` (`id_pembiayaan`),
  CONSTRAINT `angsuran_ibfk_1` FOREIGN KEY (`id_pembiayaan`) REFERENCES `pembiayaan` (`id_pembiayaan`)
) ENGINE=InnoDB AUTO_INCREMENT=723 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `angsuran`
--

LOCK TABLES `angsuran` WRITE;
/*!40000 ALTER TABLE `angsuran` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `angsuran` VALUES (705,42,'2019-10-08',3333333,250000),(706,42,'2019-11-04',3333333,0),(707,42,'2019-11-04',3333333,0),(708,43,'2019-09-13',1034370,310311),(709,43,'2019-10-07',1034370,310311),(710,43,'2019-10-07',1034370,310311),(711,43,'2019-10-07',1034370,310311),(712,43,'2019-10-07',1034370,310311),(713,43,'2019-10-07',1034370,310311),(714,43,'2019-10-07',1034370,310311),(715,43,'2019-10-07',1034370,310311),(716,43,'2019-10-07',1034370,310311),(717,43,'2019-10-07',1034370,310311),(718,43,'2019-10-07',1034370,0),(719,43,'2019-10-07',1034370,0),(720,44,'2019-10-08',1666666,125000),(721,44,'2019-11-04',1666666,0),(722,44,'2019-11-04',1666666,0);
/*!40000 ALTER TABLE `angsuran` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `angsuran` with 18 row(s)
--

--
-- Table structure for table `pembiayaan`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pembiayaan` (
  `id_pembiayaan` int(11) NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`id_pembiayaan`),
  KEY `id_anggota` (`id_anggota`),
  CONSTRAINT `pembiayaan_ibfk_1` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pembiayaan`
--

LOCK TABLES `pembiayaan` WRITE;
/*!40000 ALTER TABLE `pembiayaan` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `pembiayaan` VALUES (42,23,'012','2019-09-12',3,10000000,'2019-12-12',750000,3333333,460,'sfddsf','2019-09-12'),(43,22,'111','2019-09-20',12,12412441,'2020-09-20',3723732,1034370,124124,'124w ea','2019-09-12'),(44,24,'001','2019-09-12',3,5000000,'2019-12-12',375000,1666666,420,'efw','2019-09-12'),(45,23,'12','2019-09-02',10,5000000,'2020-07-02',1250000,500000,50,'gg',NULL);
/*!40000 ALTER TABLE `pembiayaan` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `pembiayaan` with 4 row(s)
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on: Thu, 12 Sep 2019 08:48:50 +0200

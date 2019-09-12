-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: intan
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Thu, 12 Sep 2019 07:41:11 +0200

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anggota`
--

LOCK TABLES `anggota` WRITE;
/*!40000 ALTER TABLE `anggota` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `anggota` VALUES (7,'aulia','metro','1234576','kuliah','086521'),(10,'intan','metro','1234567890','mahasiswa','085766747310'),(15,'fahmi','gaya baru','12345678','mahasiswa','08756652'),(16,'ani','metro','12345678','dagang','086527432'),(17,'endang','lampung','1807065212760004','pedagang','082372173713'),(18,'dedi irawan','metro','18065331','dosen','08576666'),(19,'aaaaaa','jdsfjks','jgd','sjfjsd','jdsds');
/*!40000 ALTER TABLE `anggota` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `anggota` with 7 row(s)
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
) ENGINE=InnoDB AUTO_INCREMENT=685 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `angsuran`
--

LOCK TABLES `angsuran` WRITE;
/*!40000 ALTER TABLE `angsuran` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `angsuran` VALUES (647,30,'2019-10-02',1000000,250000),(648,30,'2019-11-05',1000000,250000),(658,30,'2020-01-05',1000000,250000),(677,39,'2019-10-09',1000000,125000),(678,30,'2020-01-05',1000000,250000),(679,30,'2020-01-05',1000000,250000),(680,30,'2020-01-05',1000000,250000),(681,30,'2020-01-05',1000000,250000),(682,30,'2020-01-05',1000000,250000),(683,30,'2020-01-05',1000000,0),(684,30,'2020-01-05',1000000,0);
/*!40000 ALTER TABLE `angsuran` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `angsuran` with 11 row(s)
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
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pembiayaan`
--

LOCK TABLES `pembiayaan` WRITE;
/*!40000 ALTER TABLE `pembiayaan` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `pembiayaan` VALUES (30,7,'001','2019-09-06',10,10000000,'2020-07-06',2500000,1000000,75000,'kulkas','2019-09-12'),(35,15,'004','2019-09-09',3,10000000,'2019-12-09',750000,3333333,75000,'ktp',NULL),(36,16,'005','2019-09-09',5,10000000,'2020-02-09',1250000,2000000,65000,'STNK Motor',NULL),(37,17,'006','2019-09-10',12,5000000,'2020-09-10',1500000,416666,75000,'SHM',NULL),(38,18,'008','2019-09-10',12,5000000,'2020-09-10',1500000,416666,75000,'BPKB Motor',NULL),(39,16,'009','2019-09-10',5,5000000,'2020-02-10',625000,1000000,65000,'khh',NULL);
/*!40000 ALTER TABLE `pembiayaan` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `pembiayaan` with 6 row(s)
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on: Thu, 12 Sep 2019 07:41:11 +0200

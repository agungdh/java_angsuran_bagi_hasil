-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: mutasi
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Sat, 24 Aug 2019 05:41:30 +0200

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
-- Table structure for table `gaji`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gaji` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_karyawan` int(11) NOT NULL,
  `gaji` int(11) NOT NULL,
  `t_jabatan` int(11) NOT NULL,
  `t_keluarga` int(11) NOT NULL,
  `t_komunikasi` int(11) NOT NULL,
  `u_kehadiran` int(11) NOT NULL,
  `purna_tugas` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_karyawan` (`id_karyawan`),
  CONSTRAINT `gaji_ibfk_1` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gaji`
--

LOCK TABLES `gaji` WRITE;
/*!40000 ALTER TABLE `gaji` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `gaji` VALUES (11,8,220,330,440,550,660,770),(13,11,350,20,40,40,40,10);
/*!40000 ALTER TABLE `gaji` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `gaji` with 2 row(s)
--

--
-- Table structure for table `jabatan`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jabatan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jabatan` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jabatan`
--

LOCK TABLES `jabatan` WRITE;
/*!40000 ALTER TABLE `jabatan` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `jabatan` VALUES (2,'Direktur'),(3,'Manager');
/*!40000 ALTER TABLE `jabatan` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `jabatan` with 2 row(s)
--

--
-- Table structure for table `karyawan`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `karyawan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_jabatan` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `nik` varchar(50) NOT NULL,
  `kelamin` varchar(50) NOT NULL,
  `tempat_lahir` varchar(50) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `agama` varchar(20) NOT NULL,
  `pendidikan` varchar(50) NOT NULL,
  `pekerjaan` varchar(50) NOT NULL,
  `status_kawin` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_jabatan` (`id_jabatan`),
  CONSTRAINT `karyawan_ibfk_1` FOREIGN KEY (`id_jabatan`) REFERENCES `jabatan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `karyawan`
--

LOCK TABLES `karyawan` WRITE;
/*!40000 ALTER TABLE `karyawan` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `karyawan` VALUES (8,3,'Agung Sapto Margono Dh','15753002','Laki-Laki','Nunggalrejo, Punggur, Lampung Tengah','1997-03-25','Islam','D3','Programmer','Lajang'),(11,2,'Agung 2','15753003','Laki-Laki','Nunggalrejo, Punggur, Lampung Tengah','1997-03-25','Islam','D3','Programmer','Lajang');
/*!40000 ALTER TABLE `karyawan` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `karyawan` with 2 row(s)
--

--
-- Table structure for table `mutasi`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mutasi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_karyawan` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `no_surat` varchar(50) NOT NULL,
  `asal` varchar(100) NOT NULL,
  `tujuan` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_karyawan` (`id_karyawan`),
  CONSTRAINT `mutasi_ibfk_1` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mutasi`
--

LOCK TABLES `mutasi` WRITE;
/*!40000 ALTER TABLE `mutasi` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `mutasi` VALUES (9,8,'2019-08-02','12345','qq','aa'),(10,11,'2019-08-03','2345','aa','zz');
/*!40000 ALTER TABLE `mutasi` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `mutasi` with 2 row(s)
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on: Sat, 24 Aug 2019 05:41:30 +0200

CREATE DATABASE  IF NOT EXISTS `railway` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `railway`;
-- MySQL dump 10.13  Distrib 5.7.20, for Linux (x86_64)
--
-- Host: localhost    Database: railway
-- ------------------------------------------------------
-- Server version	5.7.20-0ubuntu0.16.04.1

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
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `password`
--

LOCK TABLES `password` WRITE;
/*!40000 ALTER TABLE `password` DISABLE KEYS */;
INSERT INTO `password` VALUES (1,'','1',NULL,NULL),(2,'','11',NULL,NULL),(3,'','1',NULL,NULL),(4,'\��[#��\�Io\��\�0���=��~���g�\�r\��qYG\�c�IL��.\��_.$f\r��\�]�q6�hx�','Ҵ\0�','PBKDF2WithHmacSHA1',1000),(5,'0y)F�\��x\�Р|Mc�S\�g[�Z�6UJJF�� {5����ż�=*\�\�\�\�_��\�Ʌ\�B','D<i\�','PBKDF2WithHmacSHA1',1000),(6,'\'@F��Y@$ʇ}b�{P6�͙\�\�)��\�\�C\�K�\��\��\�\�c�O�$.�\�M\����c�C�5�','\�\�?�','PBKDF2WithHmacSHA1',1000),(7,'=�6���/)\�%\Z=58�\����\�Jf\n�I\�im(\��\�{��\r�<���\r�/q\�OEdÓi��ݼ{�\�@','�>\�i','PBKDF2WithHmacSHA1',1000),(8,'\�F?1/\�\�,\�uE�\\�!8�\�N��/�c�c,�\�U1\'��^@%\'��Hj���&N6dc��g+\r�G0Li�','V\�\�\'','PBKDF2WithHmacSHA1',1000),(9,'�u����i�G�R��\�@W\�Y��\Zu�\�G\�ă\�\�\�wJa�\�whLt�)��K���%��\��	v\n�','\�y4M','PBKDF2WithHmacSHA1',1000),(10,'g����\�Ϋ\�\\\�%]zк\�.\0���,\Z�*�%T\0{\\���cL�Y�^�\�yQ\0\�-K^Z�\�\�\�','\�\�\�','PBKDF2WithHmacSHA1',1000),(11,'S�(\��\\\ry\��je\�P\�\�\�[_\�Qy�V���7������\�.��M�\�&A�}�l�D{�\�','��g5','PBKDF2WithHmacSHA1',1000),(12,'�E9*\��ż^����յ�\�y��d�\�2�l)_:״\�!�f�\��I����\�+\��Ly\��w��>ڇ�\�\�','\�;��','PBKDF2WithHmacSHA1',1000),(13,'����{�_�\�\�`f�_�֒��\ZA\�\�N��+6d\�\��\�{A�8zU�J\�\�ʳ$\�P\nn���.','��','PBKDF2WithHmacSHA1',1000),(14,'\0x\��\�@�o>\��\�Ȏ�9\�Ո�\��\�/�\� \�j�u\�<B\��\�p\n$�\�A!3O��\�5��\�','-�T\�','PBKDF2WithHmacSHA1',1000);
/*!40000 ALTER TABLE `password` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `price_per_km_for_vagon`
--

LOCK TABLES `price_per_km_for_vagon` WRITE;
/*!40000 ALTER TABLE `price_per_km_for_vagon` DISABLE KEYS */;
INSERT INTO `price_per_km_for_vagon` VALUES (1,25.89,1),(2,20.77,2),(3,30.56,3),(4,14.55,4),(5,10.44,5);
/*!40000 ALTER TABLE `price_per_km_for_vagon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Адміністратор','Administrator','Адміністратор'),(2,'Користувач','User','Користувач');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `route`
--

LOCK TABLES `route` WRITE;
/*!40000 ALTER TABLE `route` DISABLE KEYS */;
INSERT INTO `route` VALUES (1,1,1,1,0),(2,2,2,1,14),(3,3,3,1,30),(4,4,4,1,126),(5,5,5,1,196),(6,6,6,1,121),(7,7,7,1,268),(8,8,8,1,296),(9,9,9,1,301),(10,10,10,1,350),(11,11,11,1,409),(12,12,12,1,430),(13,13,13,1,468),(14,14,14,1,545),(15,1,1,2,0),(16,2,15,2,157),(17,3,16,2,221),(18,4,17,2,367),(19,5,18,2,435),(20,6,19,2,486),(21,7,20,2,550),(22,8,21,2,627),(23,1,21,3,0),(24,2,22,3,141),(25,3,23,3,196),(26,4,24,3,231),(27,5,25,3,267),(28,1,25,4,0),(29,2,24,4,36),(30,3,23,4,71),(31,4,22,4,126),(32,5,21,4,267),(33,1,14,5,0),(34,2,26,5,154),(35,3,27,5,241),(36,4,2,5,475),(37,5,1,5,489);
/*!40000 ALTER TABLE `route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `route_num`
--

LOCK TABLES `route_num` WRITE;
/*!40000 ALTER TABLE `route_num` DISABLE KEYS */;
INSERT INTO `route_num` VALUES (1,111),(2,222),(3,333),(4,444),(5,555);
/*!40000 ALTER TABLE `route_num` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `route_rate`
--

LOCK TABLES `route_rate` WRITE;
/*!40000 ALTER TABLE `route_rate` DISABLE KEYS */;
INSERT INTO `route_rate` VALUES (1,1.5),(2,1);
/*!40000 ALTER TABLE `route_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `station`
--

LOCK TABLES `station` WRITE;
/*!40000 ALTER TABLE `station` DISABLE KEYS */;
INSERT INTO `station` VALUES (1,'Київ','Київ-Пасажирський','Kyiv','Kyiv-Pasazhyrs\'kyi','Київ','Київ-Пасажирський'),(2,'Київ','Дарниця','Kyiv','Darnytsia','Київ','Дарниця'),(3,'Бровари','Бровари','Brovary','Brovary','Бровари','Бровари'),(4,'Ніжин','Ніжин','Nizhyn','Nizhyn','Ніжин','Ніжин'),(5,'Бахмач','Бахмач','Bahmach','Bahmach','Бахмач','Бахмач'),(6,'Конотоп','Конотоп','Konotop','Konotop','Конотоп','Конотоп'),(7,'Путивль','Путивль','Putyvl\'','Putyvl\'','Путивль','Путивль'),(8,'Ворожба','Ворожба','Vorozhba','Vorozhba','Ворожба','Ворожба'),(9,'Білопілля','Білопілля','Bilopillia','Bilopillia','Білопілля','Білопілля'),(10,'Суми','Суми','Sumy','Sumy','Суми','Суми'),(11,'Смородино','Смородино','Smorodyno','Smorodyno','Смородино','Смородино'),(12,'Кириківка','Кириківка','Kyrykivka','Kyrykivka','Кириківка','Кириківка'),(13,'Богодухів','Богодухів','Bogoduhiv','Bogoduhiv','Богодухів','Богодухів'),(14,'Харків','Харків-Пасажирський','Kharkiv','Kharkiv-Pasazhyrs\'kyi','Харків','Харків-Пасажирський'),(15,'Козятин','Козятин-Пасажирський','Koziatyn','Koziatyn-Pasazhyrs\'kyi','Козятин','Козятин-Пасажирський'),(16,'Вінниця','Вінниця','Vinnytsia','Koziatyn','Вінниця','Вінниця'),(17,'Хмельницький','Хмельницький','Khmel\'nyts\'kyi','Koziatyn','Хмельницький','Хмельницький'),(18,'Підволочиськ','Підволочиськ','Pidvolochys\'k','Pidvolochys\'k','Підволочиськ','Підволочиськ'),(19,'Тернопіль','Тернопіль-Пасажирський','Ternopil\'','Ternopil\'-Pasazhyrs\'kyi','Тернопіль','Тернопіль-Пасажирський'),(20,'Злочів','Злочів','Zlochiv','Zlochiv','Злочів','Злочів'),(21,'Львів','Львів','L\'viv','L\'viv','Львів','Львів'),(22,'Івано-Франківськ','Івано-Франківськ','Ivano-Frankivs\'k','Ivano-Frankivs\'k','Івано-Франківськ','Івано-Франківськ'),(23,'Коломия','Коломия','Kolomyia','Ivano-Frankivs\'k','Коломия','Коломия'),(24,'Снятин','Снятин','Sniatyn','Sniatyn','Снятин','Снятин'),(25,'Чернівці','Чернівці','Chernivtsi','Chernivtsi','Чернівці','Чернівці'),(26,'Полтава','Полтава-Київська','Poltava','Poltava-Kyivs\'ka','Полтава','Полтава-Київська'),(27,'Миргород','Миргород','Myrgorod','Myrgorod','Миргород','Миргород');
/*!40000 ALTER TABLE `station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `timetable`
--

LOCK TABLES `timetable` WRITE;
/*!40000 ALTER TABLE `timetable` DISABLE KEYS */;
INSERT INTO `timetable` VALUES (28,1,'2017-12-08 00:22:00','2017-12-08 00:22:00',1),(29,2,'2017-12-08 00:42:00','2017-12-08 00:40:00',1),(30,3,'2017-12-08 00:57:00','2017-12-08 00:56:00',1),(31,4,'2017-12-08 01:58:00','2017-12-08 01:56:00',1),(32,5,'2017-12-08 03:14:00','2017-12-08 03:12:00',1),(33,6,'2017-12-08 03:57:00','2017-12-08 03:55:00',1),(34,7,'2017-12-08 04:46:00','2017-12-08 04:26:00',1),(35,8,'2017-12-08 04:55:00','2017-12-08 04:54:00',1),(36,9,'2017-12-08 05:38:00','2017-12-08 05:33:00',1),(37,10,'2017-12-08 04:46:00','2017-12-08 06:25:00',1),(38,11,'2017-12-08 06:43:00','2017-12-08 06:27:00',1),(39,12,'2017-12-08 06:36:00','2017-12-08 06:34:00',1),(40,13,'2017-12-08 06:44:00','2017-12-08 06:25:00',1),(41,14,'2017-12-08 07:15:00','2017-12-08 07:14:00',1),(42,1,'2017-12-08 10:37:00','2017-12-08 10:35:00',2),(43,15,'2017-12-08 10:54:00','2017-12-08 10:51:00',2),(44,16,'2017-12-08 11:56:00','2017-12-08 11:51:00',2),(45,17,'2017-12-08 13:56:00','2017-12-08 13:53:00',2),(46,18,'2017-12-08 14:58:00','2017-12-08 14:56:00',2),(47,19,'2017-12-08 15:49:00','2017-12-08 15:44:00',2),(48,20,'2017-12-08 16:47:00','2017-12-08 16:45:00',2),(49,21,'2017-12-08 16:47:00','2017-12-08 18:01:00',2),(50,21,'2017-12-08 20:55:00','2017-12-08 19:08:00',3),(51,22,'2017-12-08 20:59:00','2017-12-08 20:55:00',3),(52,23,'2017-12-08 21:44:00','2017-12-08 21:42:00',3),(53,24,'2017-12-08 22:13:00','2017-12-08 22:11:00',3),(54,25,'2017-12-08 22:44:00','2017-12-08 22:13:00',3),(55,25,'2017-12-08 06:43:00','2017-12-08 06:11:00',4),(56,24,'2017-12-08 06:46:00','2017-12-08 06:45:00',4),(57,23,'2017-12-08 07:12:00','2017-12-08 07:10:00',4),(58,22,'2017-12-08 08:02:00','2017-12-08 07:55:00',4),(59,21,'2017-12-08 09:51:00','2017-12-08 09:51:00',4),(60,14,'2017-12-08 08:58:00','2017-12-08 07:22:00',5),(61,26,'2017-12-08 09:00:00','2017-12-08 08:58:00',5),(62,27,'2017-12-08 09:52:00','2017-12-08 09:50:00',5),(63,2,'2017-12-08 11:48:00','2017-12-08 11:46:00',5),(64,1,'2017-12-08 12:02:00','2017-12-08 11:50:00',5);
/*!40000 ALTER TABLE `timetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `train`
--

LOCK TABLES `train` WRITE;
/*!40000 ALTER TABLE `train` DISABLE KEYS */;
INSERT INTO `train` VALUES (1,'776П',1,18),(2,'191К',2,20),(3,'702П',3,24),(4,'702Д',4,17),(7,'725О',5,28);
/*!40000 ALTER TABLE `train` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `train_rate`
--

LOCK TABLES `train_rate` WRITE;
/*!40000 ALTER TABLE `train_rate` DISABLE KEYS */;
INSERT INTO `train_rate` VALUES (1,1,1),(2,1.5,2);
/*!40000 ALTER TABLE `train_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Олександр','Пєточкін','mail@mail.ua',1,1,1,NULL),(2,'Василь','Макогон','mail2@mail.ua',2,2,0,NULL),(3,'Микола','Чуднов','mail3@mail.ua',3,2,0,NULL),(4,'Olga','Kulakova','email@mm.ua',1,1,0,NULL),(5,'Vika','Taranova','email@mm1.ua',1,1,1,NULL),(6,'Olga','Kulakova','email@mm.ua',1,1,0,NULL),(7,'Olga','Kulakova','email@mm.ua',1,1,0,NULL),(8,'Olga','Kulakova','email@mm.ua',1,1,0,NULL),(9,'Ivan','Ivanov','ivan@mail.ua',4,2,0,'uk_UA'),(10,'Ivan','Ivanov','ivan@mail.ua',5,1,1,'uk_UA'),(11,'Homm','Jooo','hhh@nn.nn',6,1,0,'uk_UA'),(12,'John','Smith','John.Smith@mail.com',7,2,0,'en_GB'),(13,'Vitalii','Liashenko','vitalii@mail.com',8,2,0,'uk_UA'),(14,'vitalii','liashenko','vitaliimail.com',9,2,0,'uk_UA'),(15,'Mаслов ','Василь','maslov@mail.com',10,1,0,'uk_UA'),(16,'Petrov','Petro','petrov@mail.com',11,2,0,'en_GB'),(17,'Piatov','Pavlo','piatov@mail.com',12,2,0,'uk_UA'),(18,'Pypkin','Pypa','pypkin@mail.com',13,2,0,'uk_UA'),(19,'Володимир','Кириченко','kir.v@mail.com',14,2,0,'uk_UA');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `vagon_type`
--

LOCK TABLES `vagon_type` WRITE;
/*!40000 ALTER TABLE `vagon_type` DISABLE KEYS */;
INSERT INTO `vagon_type` VALUES (1,'Купе','Compartment','Купе',36),(2,'Плацкарт','Reserved seat','Плацкарт',54),(3,'Люкс','Lux','Люкс',20),(4,'Сидячий (1 клас)','Sitting (1 class)','Сидячий (1 клас)',42),(5,'Сидячий (2 клас)','Sitting (1 class)','Сидячий (2 клас)',68);
/*!40000 ALTER TABLE `vagon_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-05 12:51:22

-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: test
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat_message`
--

DROP TABLE IF EXISTS `chat_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_message` (
  `chat_id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `recipient` varchar(255) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `type` tinyint DEFAULT NULL,
  PRIMARY KEY (`chat_id`),
  CONSTRAINT `chat_message_chk_1` CHECK ((`type` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message`
--

LOCK TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author_name` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs1slvnkuemjsq2kj4h3vhx7i1` (`post_id`),
  CONSTRAINT `FKs1slvnkuemjsq2kj4h3vhx7i1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_block`
--

DROP TABLE IF EXISTS `friend_block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_block` (
  `block_friend_id` bigint NOT NULL AUTO_INCREMENT,
  `addressee_id` bigint NOT NULL,
  `requester_id` bigint NOT NULL,
  PRIMARY KEY (`block_friend_id`),
  KEY `FKpwdromg1nyuxprhfbxe4xw1wr` (`addressee_id`),
  KEY `FKdvo5rfbkwasudeo18h7ehiqjq` (`requester_id`),
  CONSTRAINT `FKdvo5rfbkwasudeo18h7ehiqjq` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKpwdromg1nyuxprhfbxe4xw1wr` FOREIGN KEY (`addressee_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_block`
--

LOCK TABLES `friend_block` WRITE;
/*!40000 ALTER TABLE `friend_block` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend_block` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_requests`
--

DROP TABLE IF EXISTS `friend_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_requests` (
  `friend_request_id` bigint NOT NULL AUTO_INCREMENT,
  `accepted_at` datetime(6) DEFAULT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('PENDING','ACCEPTED','REJECTED') NOT NULL DEFAULT 'PENDING',
  `addressee_id` bigint NOT NULL,
  `requester_id` bigint NOT NULL,
  PRIMARY KEY (`friend_request_id`),
  KEY `FKsoyl82l4vmnu6mhihbuua8ddy` (`addressee_id`),
  KEY `FKri3588r9atpppe038o2abrasc` (`requester_id`),
  CONSTRAINT `FKri3588r9atpppe038o2abrasc` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKsoyl82l4vmnu6mhihbuua8ddy` FOREIGN KEY (`addressee_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_requests`
--

LOCK TABLES `friend_requests` WRITE;
/*!40000 ALTER TABLE `friend_requests` DISABLE KEYS */;
INSERT INTO `friend_requests` VALUES (2,'2024-07-24 16:05:06.974489','2024-07-24 08:00:43','ACCEPTED',1,2);
/*!40000 ALTER TABLE `friend_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES (1,'user/assets/images/post/3ba7bb74-89d6-4293-b24d-79b4c9c3b1d3_279455187_594598114865951_7440947279832479676_n.jpg');
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `is_read` bit(1) NOT NULL,
  `type` varchar(255) NOT NULL,
  `addressee_id` bigint NOT NULL,
  `post_id` bigint DEFAULT NULL,
  `requester_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK44a2v4gnkoan3dk07ioschkjj` (`addressee_id`),
  KEY `FK4ba33qvlquxq8icg5nfaqlhy7` (`post_id`),
  KEY `FK71e8feaf5xvh8byj81w1fm5pq` (`requester_id`),
  CONSTRAINT `FK44a2v4gnkoan3dk07ioschkjj` FOREIGN KEY (`addressee_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK4ba33qvlquxq8icg5nfaqlhy7` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK71e8feaf5xvh8byj81w1fm5pq` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (2,'user1 nguyen đã chấp nhận lời mời kết bạn của bạn','2024-07-24 16:05:06.984569',_binary '','ACCEPT FRIENDREQUEST',2,NULL,1),(3,'user1 nguyen đã đăng bài viết lên trang cá nhân của bạn','2024-07-24 23:32:28.586304',_binary '','POST',2,8,1),(4,'user1 nguyen đã đăng bài viết lên trang cá nhân của bạn','2024-07-24 23:34:17.830433',_binary '','POST',2,10,1),(5,'Hôm nay là sinh nhật của user2 tran. Hãy chúc mừng nhật bạn ấy thôi nào ??','2024-07-24 23:36:44.472966',_binary '\0','BIRTHDAY',1,NULL,2),(6,'Hôm nay là ngày sinh nhật của bạn. Chúc bạn sinh nhật vui vẻ ??','2024-07-24 23:38:27.499237',_binary '','BIRTHDAY',2,NULL,2),(7,' đã thích bài viết của bạn!','2024-07-24 23:51:44.942339',_binary '','LIKE',2,11,1);
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_token`
--

DROP TABLE IF EXISTS `password_reset_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5lwtbncug84d4ero33v3cfxvl` (`user_id`),
  CONSTRAINT `FK5lwtbncug84d4ero33v3cfxvl` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_token`
--

LOCK TABLES `password_reset_token` WRITE;
/*!40000 ALTER TABLE `password_reset_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `elapsed_time` varchar(255) DEFAULT NULL,
  `likes` int NOT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `receiver_id` bigint DEFAULT NULL,
  `sender_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrbhqp3fuiaoff9fscafs3042l` (`receiver_id`),
  KEY `FKnkflk5bc8b0bebnvpagehvnd8` (`sender_id`),
  CONSTRAINT `FKnkflk5bc8b0bebnvpagehvnd8` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKrbhqp3fuiaoff9fscafs3042l` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (4,'<p>di ngu</p>',NULL,0,'2024-07-24 21:11:32.498947',2,1),(5,'<p>di ngu</p>',NULL,0,'2024-07-24 21:11:35.039129',1,1),(6,'<p>index&nbsp;</p>',NULL,0,'2024-07-24 22:03:00.954658',1,1),(7,'<p>gg</p>',NULL,0,'2024-07-24 22:12:49.834632',2,1),(8,'<p>hdddd</p>',NULL,0,'2024-07-24 23:31:59.018493',2,1),(9,'<p>mmmm</p>',NULL,0,'2024-07-24 23:33:00.696674',1,1),(10,'<p>tu test</p>',NULL,0,'2024-07-24 23:33:50.178651',2,1),(11,'<p>dfsdf</p>',NULL,1,'2024-07-24 23:51:39.646358',2,2);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_images`
--

DROP TABLE IF EXISTS `post_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_images` (
  `post_id` bigint NOT NULL,
  `images_id` bigint NOT NULL,
  UNIQUE KEY `UKm78offcf9uxb8jox1hpjfcftf` (`images_id`),
  KEY `FK4436mqgshkhub17yvq5ku91f7` (`post_id`),
  CONSTRAINT `FK4436mqgshkhub17yvq5ku91f7` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK7qbn4kangy50vix0gbupf9rkp` FOREIGN KEY (`images_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_images`
--

LOCK TABLES `post_images` WRITE;
/*!40000 ALTER TABLE `post_images` DISABLE KEYS */;
INSERT INTO `post_images` VALUES (9,1);
/*!40000 ALTER TABLE `post_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_likes`
--

DROP TABLE IF EXISTS `post_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_likes` (
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`post_id`,`user_id`),
  KEY `FKc85he3c94qv5vmyutmf0plp69` (`user_id`),
  CONSTRAINT `FKc85he3c94qv5vmyutmf0plp69` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKmxmoc9p5ndijnsqtvsjcuoxm3` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_likes`
--

LOCK TABLES `post_likes` WRITE;
/*!40000 ALTER TABLE `post_likes` DISABLE KEYS */;
INSERT INTO `post_likes` VALUES (11,1);
/*!40000 ALTER TABLE `post_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_videos`
--

DROP TABLE IF EXISTS `post_videos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_videos` (
  `post_id` bigint NOT NULL,
  `videos_id` bigint NOT NULL,
  UNIQUE KEY `UKaft9mjkc31ju0awgngvbtremx` (`videos_id`),
  KEY `FK13hn7k9frbxaf4xfrlq46uj19` (`post_id`),
  CONSTRAINT `FK13hn7k9frbxaf4xfrlq46uj19` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FKnucf5bll0mo5egfrk2ytw9h2s` FOREIGN KEY (`videos_id`) REFERENCES `video` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_videos`
--

LOCK TABLES `post_videos` WRITE;
/*!40000 ALTER TABLE `post_videos` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_videos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `handle_date` datetime(6) DEFAULT NULL,
  `is_handled` bit(1) NOT NULL,
  `reported_type` varchar(255) NOT NULL,
  `resolution` varchar(255) DEFAULT NULL,
  `post_id` bigint NOT NULL,
  `reporter_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnuqod1y014fp5bmqjeoffcgqy` (`post_id`),
  KEY `FKndpjl61ubcm2tkf7ml1ynq13t` (`reporter_id`),
  CONSTRAINT `FKndpjl61ubcm2tkf7ml1ynq13t` FOREIGN KEY (`reporter_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKnuqod1y014fp5bmqjeoffcgqy` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(2,'USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statistic`
--

DROP TABLE IF EXISTS `statistic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statistic` (
  `id_statistic` bigint NOT NULL AUTO_INCREMENT,
  `visit_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `visitors` bigint NOT NULL,
  PRIMARY KEY (`id_statistic`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statistic`
--

LOCK TABLES `statistic` WRITE;
/*!40000 ALTER TABLE `statistic` DISABLE KEYS */;
INSERT INTO `statistic` VALUES (1,'2024-07-23 17:00:00',1),(2,'2024-07-23 17:00:00',1),(3,'2024-07-23 17:00:00',1),(4,'2024-07-23 17:00:00',1),(5,'2024-07-23 17:00:00',1),(6,'2024-07-23 17:00:00',1),(7,'2024-07-23 17:00:00',1),(8,'2024-07-23 17:00:00',1),(9,'2024-07-23 17:00:00',1),(10,'2024-07-23 17:00:00',2),(11,'2024-07-23 17:00:00',1),(12,'2024-07-23 17:00:00',1),(13,'2024-07-23 17:00:00',1),(14,'2024-07-23 17:00:00',1),(15,'2024-07-23 17:00:00',1),(16,'2024-07-23 17:00:00',1),(17,'2024-07-23 17:00:00',1),(18,'2024-07-23 17:00:00',1),(19,'2024-07-23 17:00:00',1),(20,'2024-07-23 17:00:00',1),(21,'2024-07-23 17:00:00',1),(22,'2024-07-23 17:00:00',1),(23,'2024-07-23 17:00:00',1),(24,'2024-07-23 17:00:00',1),(25,'2024-07-23 17:00:00',1),(26,'2024-07-23 17:00:00',1),(27,'2024-07-23 17:00:00',1),(28,'2024-07-23 17:00:00',1),(29,'2024-07-23 17:00:00',1),(30,'2024-07-23 17:00:00',1),(31,'2024-07-23 17:00:00',1),(32,'2024-07-23 17:00:00',1),(33,'2024-07-23 17:00:00',1),(34,'2024-07-23 17:00:00',1),(35,'2024-07-23 17:00:00',1),(36,'2024-07-23 17:00:00',1),(37,'2024-07-23 17:00:00',1),(38,'2024-07-23 17:00:00',1),(39,'2024-07-23 17:00:00',1),(40,'2024-07-23 17:00:00',1),(41,'2024-07-23 17:00:00',1),(42,'2024-07-23 17:00:00',1),(43,'2024-07-23 17:00:00',1),(44,'2024-07-23 17:00:00',1),(45,'2024-07-23 17:00:00',1),(46,'2024-07-23 17:00:00',1),(47,'2024-07-23 17:00:00',1),(48,'2024-07-23 17:00:00',1),(49,'2024-07-23 17:00:00',1),(50,'2024-07-23 17:00:00',1),(51,'2024-07-23 17:00:00',1),(52,'2024-07-23 17:00:00',1),(53,'2024-07-23 17:00:00',1),(54,'2024-07-23 17:00:00',1),(55,'2024-07-23 17:00:00',1),(56,'2024-07-23 17:00:00',1),(57,'2024-07-23 17:00:00',1),(58,'2024-07-23 17:00:00',1),(59,'2024-07-23 17:00:00',1),(60,'2024-07-23 17:00:00',1),(61,'2024-07-23 17:00:00',1),(62,'2024-07-23 17:00:00',1),(63,'2024-07-23 17:00:00',1),(64,'2024-07-23 17:00:00',1),(65,'2024-07-23 17:00:00',2),(66,'2024-07-23 17:00:00',1),(67,'2024-07-23 17:00:00',1),(68,'2024-07-23 17:00:00',1),(69,'2024-07-23 17:00:00',1),(70,'2024-07-23 17:00:00',1),(71,'2024-07-23 17:00:00',1),(72,'2024-07-23 17:00:00',1),(73,'2024-07-23 17:00:00',1),(74,'2024-07-23 17:00:00',2),(75,'2024-07-23 17:00:00',2),(76,'2024-07-23 17:00:00',1),(77,'2024-07-23 17:00:00',2),(78,'2024-07-24 17:00:00',2),(79,'2024-07-24 17:00:00',2),(80,'2024-07-24 17:00:00',2),(81,'2024-07-24 17:00:00',2);
/*!40000 ALTER TABLE `statistic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_status` bit(1) NOT NULL,
  `active_status` bit(1) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `bio` varchar(255) DEFAULT NULL,
  `cover_photo` varchar(255) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `date_of_birth` date NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `gender` bit(1) NOT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  `phone_number` varchar(10) DEFAULT NULL,
  `user_name` varchar(50) NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKlqjrcobrh9jc8wpcar64q1bfh` (`user_name`),
  UNIQUE KEY `UK4bgmpi98dylab6qdvf9xyaxu4` (`phone_number`),
  KEY `FKn82ha3ccdebhokx3a8fgdqeyy` (`role_id`),
  CONSTRAINT `FKn82ha3ccdebhokx3a8fgdqeyy` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,_binary '',_binary '','/user/assets/images/profile/f945cb07-78b0-4922-afff-27564daf1a42_1af76321-5f0c-451a-ad90-1435deacd65e_d66ca45a-a654-4f70-aeaf-50d8450671d5_351130890_3439848036343507_2381651913436659575_n.jpg','',NULL,'2024-07-24','2024-07-27','user1@gmail.com','user1 nguyen',_binary '','2024-07-24 23:51:28.703178','$2a$10$MUtnsceIr1V7RG.2PklTt.Kn08hjcqaQEhSTOwoMo2UzEQZXZM6O6','0123456789','quy',2),(2,_binary '',_binary '','/user/assets/images/profile/df493674-fe7a-440e-a616-ded47eb989a5_8cf01ae4-cc8d-4e76-a086-684a6285dff0_278676506_1449774715479558_7140619298110173106_n.jpg','',NULL,'2024-07-24','2024-07-24','user2@gmail.com','user2 tran',_binary '','2024-07-24 23:39:59.028648','$2a$10$vpwgj09k8mf3UFq20MZa7.BJwfoEb7JoLc/7pak/ayLOiZsvPJ3dK','0352477783','nam',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-25 11:20:25

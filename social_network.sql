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
  `content` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs1slvnkuemjsq2kj4h3vhx7i1` (`post_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_requests`
--

LOCK TABLES `friend_requests` WRITE;
/*!40000 ALTER TABLE `friend_requests` DISABLE KEYS */;
INSERT INTO `friend_requests` VALUES (8,'2024-08-02 21:16:33.067165','2024-08-02 14:16:19','ACCEPTED',4,3);
/*!40000 ALTER TABLE `friend_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_membership`
--

DROP TABLE IF EXISTS `group_membership`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_membership` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_censored` bit(1) NOT NULL,
  `group_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqoye24ka7bmdd6snx0frflcmv` (`group_id`),
  KEY `FK5226h4qqd80sxck7cg5s55u44` (`user_id`),
  CONSTRAINT `FK5226h4qqd80sxck7cg5s55u44` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKqoye24ka7bmdd6snx0frflcmv` FOREIGN KEY (`group_id`) REFERENCES `group_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_membership`
--

LOCK TABLES `group_membership` WRITE;
/*!40000 ALTER TABLE `group_membership` DISABLE KEYS */;
INSERT INTO `group_membership` VALUES (7,_binary '',3,3),(20,_binary '',3,4);
/*!40000 ALTER TABLE `group_membership` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_table`
--

DROP TABLE IF EXISTS `group_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_table` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `admin_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjti7kb2rl6bfdb77s7cx81xw0` (`admin_id`),
  CONSTRAINT `FKjti7kb2rl6bfdb77s7cx81xw0` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_table`
--

LOCK TABLES `group_table` WRITE;
/*!40000 ALTER TABLE `group_table` DISABLE KEYS */;
INSERT INTO `group_table` VALUES (3,'/user/assets/images/profile/601b56dc-894b-4708-a605-ccb5edaaef52_GauTruc.png','dfsdf','nhom cua quy',3);
/*!40000 ALTER TABLE `group_table` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES (1,'user/assets/images/post/324961088_1197653667553080_5512993496072532610_n.jpg'),(2,'user/assets/images/post/325480580_782603966912492_6854818947646257253_n.jpg');
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
  `group_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK44a2v4gnkoan3dk07ioschkjj` (`addressee_id`),
  KEY `FK4ba33qvlquxq8icg5nfaqlhy7` (`post_id`),
  KEY `FK71e8feaf5xvh8byj81w1fm5pq` (`requester_id`),
  KEY `FKcuf29gvqg0junohi1ubmnog5o` (`group_id`),
  CONSTRAINT `FK44a2v4gnkoan3dk07ioschkjj` FOREIGN KEY (`addressee_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK4ba33qvlquxq8icg5nfaqlhy7` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK71e8feaf5xvh8byj81w1fm5pq` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKcuf29gvqg0junohi1ubmnog5o` FOREIGN KEY (`group_id`) REFERENCES `group_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (28,'đã gửi yêu cầu đăng bài trong nhóm nhom cua quy','2024-08-03 00:20:41.293574',_binary '','REVIEW_POST',3,27,4,3),(29,'đã gửi yêu cầu đăng bài trong nhóm nhom cua quy','2024-08-03 09:19:08.294216',_binary '\0','REVIEW_POST',3,28,4,3),(30,'Bài viết của bạn đã được chấp nhận','2024-08-03 10:57:26.214288',_binary '','APPROVE_POST',4,10,3,3),(31,'Bài viết của bạn đã được chấp nhận','2024-08-03 11:42:48.335489',_binary '\0','APPROVE_POST',4,28,3,3);
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
  `is_censored` bit(1) NOT NULL,
  `likes` int NOT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `group_receive_id` bigint DEFAULT NULL,
  `receiver_id` bigint DEFAULT NULL,
  `sender_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8feg631ui1tkuajoegcy3u28n` (`group_receive_id`),
  KEY `FKrbhqp3fuiaoff9fscafs3042l` (`receiver_id`),
  KEY `FKnkflk5bc8b0bebnvpagehvnd8` (`sender_id`),
  CONSTRAINT `FK8feg631ui1tkuajoegcy3u28n` FOREIGN KEY (`group_receive_id`) REFERENCES `group_table` (`id`),
  CONSTRAINT `FKnkflk5bc8b0bebnvpagehvnd8` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKrbhqp3fuiaoff9fscafs3042l` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'<p>gdfg</p>',_binary '',0,'2024-08-02 10:22:52.881123',NULL,4,3),(2,'<p>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</p>',_binary '',0,'2024-08-02 16:14:36.812688',NULL,3,3),(3,'<p>up trong group</p><p>&nbsp;</p>',_binary '',0,'2024-08-02 16:29:57.063380',3,NULL,3),(4,'<p>hello</p>',_binary '',1,'2024-08-02 20:55:08.452847',NULL,3,3),(5,'<p>hi</p>',_binary '',2,'2024-08-02 20:55:21.559688',NULL,4,3),(9,'<p>test trong group</p>',_binary '',0,'2024-08-02 21:52:02.533771',3,NULL,3),(10,'<p>nam test trong group</p><p>&nbsp;</p>',_binary '',0,'2024-08-02 21:58:57.041950',3,NULL,4),(11,'<p>di ngu di</p><p>&nbsp;</p>',_binary '\0',0,'2024-08-02 22:05:29.173777',3,NULL,3),(12,'<p>day di</p>',_binary '\0',0,'2024-08-02 22:05:57.804343',3,NULL,3),(13,'<p>hello</p>',_binary '\0',0,'2024-08-02 22:13:04.354025',3,NULL,3),(14,'<p>helo</p><p>&nbsp;</p>',_binary '\0',0,'2024-08-02 22:17:46.734856',NULL,3,3),(15,'<p>ddd</p>',_binary '\0',0,'2024-08-02 22:23:22.033962',NULL,4,4),(16,'<p>hrrh</p>',_binary '',0,'2024-08-02 22:24:06.699893',NULL,3,4),(18,'<p>fffffff</p>',_binary '\0',0,'2024-08-02 23:07:35.343356',3,NULL,4),(19,'<p>fffffff</p>',_binary '\0',0,'2024-08-02 23:08:08.269948',3,NULL,4),(20,'<p>dd</p>',_binary '\0',0,'2024-08-02 23:20:12.570573',3,NULL,4),(21,'<p>ffdf</p>',_binary '\0',0,'2024-08-02 23:22:52.660419',3,NULL,4),(22,'<p>cccc</p>',_binary '\0',0,'2024-08-02 23:23:35.792885',3,NULL,4),(23,'<p>fdfdfd</p>',_binary '\0',0,'2024-08-02 23:58:08.279526',3,NULL,3),(24,'<p>dfsdfsf</p>',_binary '\0',0,'2024-08-03 00:00:50.648615',3,NULL,3),(25,'<p>fg</p>',_binary '\0',0,'2024-08-03 00:01:26.729254',3,NULL,3),(26,'<p>ddd</p>',_binary '',0,'2024-08-03 00:02:28.394677',3,NULL,3),(27,'<p>ffff</p>',_binary '\0',0,'2024-08-03 00:20:34.318140',3,NULL,4),(28,'<p>fgfgf</p>',_binary '',0,'2024-08-03 09:19:05.347599',3,NULL,4);
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
INSERT INTO `post_images` VALUES (28,1),(28,2);
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
INSERT INTO `post_likes` VALUES (5,3),(4,4),(5,4);
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
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statistic`
--

LOCK TABLES `statistic` WRITE;
/*!40000 ALTER TABLE `statistic` DISABLE KEYS */;
INSERT INTO `statistic` VALUES (1,'2024-07-31 17:00:00',1),(2,'2024-07-31 17:00:00',1),(3,'2024-07-31 17:00:00',2),(4,'2024-07-31 17:00:00',2),(5,'2024-07-31 17:00:00',1),(6,'2024-07-31 17:00:00',1),(7,'2024-07-31 17:00:00',1),(8,'2024-07-31 17:00:00',1),(9,'2024-07-31 17:00:00',1),(10,'2024-07-31 17:00:00',1),(11,'2024-07-31 17:00:00',1),(12,'2024-07-31 17:00:00',1),(13,'2024-07-31 17:00:00',3),(14,'2024-07-31 17:00:00',3),(15,'2024-07-31 17:00:00',3),(16,'2024-07-31 17:00:00',3),(17,'2024-07-31 17:00:00',4),(18,'2024-07-31 17:00:00',4),(19,'2024-07-31 17:00:00',4),(20,'2024-07-31 17:00:00',4),(21,'2024-07-31 17:00:00',3),(22,'2024-07-31 17:00:00',3),(23,'2024-07-31 17:00:00',3),(24,'2024-07-31 17:00:00',3),(25,'2024-07-31 17:00:00',3),(26,'2024-08-01 17:00:00',3),(27,'2024-08-01 17:00:00',3),(28,'2024-08-01 17:00:00',4),(29,'2024-08-01 17:00:00',3),(30,'2024-08-01 17:00:00',3),(31,'2024-08-01 17:00:00',3),(32,'2024-08-01 17:00:00',4),(33,'2024-08-01 17:00:00',3),(34,'2024-08-01 17:00:00',3),(35,'2024-08-01 17:00:00',3),(36,'2024-08-01 17:00:00',3),(37,'2024-08-01 17:00:00',3),(38,'2024-08-01 17:00:00',3),(39,'2024-08-01 17:00:00',3),(40,'2024-08-01 17:00:00',3),(41,'2024-08-01 17:00:00',3),(42,'2024-08-01 17:00:00',4),(43,'2024-08-01 17:00:00',3),(44,'2024-08-01 17:00:00',4),(45,'2024-08-01 17:00:00',3),(46,'2024-08-01 17:00:00',3),(47,'2024-08-01 17:00:00',3),(48,'2024-08-01 17:00:00',3),(49,'2024-08-01 17:00:00',4),(50,'2024-08-01 17:00:00',4),(51,'2024-08-01 17:00:00',4),(52,'2024-08-01 17:00:00',3),(53,'2024-08-01 17:00:00',4),(54,'2024-08-01 17:00:00',4),(55,'2024-08-01 17:00:00',4),(56,'2024-08-01 17:00:00',4),(57,'2024-08-01 17:00:00',4),(58,'2024-08-01 17:00:00',4),(59,'2024-08-01 17:00:00',4),(60,'2024-08-01 17:00:00',4),(61,'2024-08-01 17:00:00',4),(62,'2024-08-01 17:00:00',4),(63,'2024-08-01 17:00:00',4),(64,'2024-08-01 17:00:00',4),(65,'2024-08-01 17:00:00',4),(66,'2024-08-01 17:00:00',4),(67,'2024-08-01 17:00:00',4),(68,'2024-08-01 17:00:00',4),(69,'2024-08-01 17:00:00',4),(70,'2024-08-01 17:00:00',4),(71,'2024-08-01 17:00:00',4),(72,'2024-08-01 17:00:00',4),(73,'2024-08-01 17:00:00',4),(74,'2024-08-01 17:00:00',4),(75,'2024-08-01 17:00:00',3),(76,'2024-08-01 17:00:00',3),(77,'2024-08-01 17:00:00',4),(78,'2024-08-01 17:00:00',3),(79,'2024-08-01 17:00:00',4),(80,'2024-08-01 17:00:00',4),(81,'2024-08-01 17:00:00',3),(82,'2024-08-01 17:00:00',4),(83,'2024-08-01 17:00:00',4),(84,'2024-08-01 17:00:00',4),(85,'2024-08-01 17:00:00',3),(86,'2024-08-01 17:00:00',3),(87,'2024-08-01 17:00:00',3),(88,'2024-08-01 17:00:00',3),(89,'2024-08-01 17:00:00',3),(90,'2024-08-01 17:00:00',3),(91,'2024-08-01 17:00:00',3),(92,'2024-08-01 17:00:00',3),(93,'2024-08-01 17:00:00',3),(94,'2024-08-01 17:00:00',3),(95,'2024-08-01 17:00:00',3),(96,'2024-08-01 17:00:00',3),(97,'2024-08-01 17:00:00',3),(98,'2024-08-01 17:00:00',3),(99,'2024-08-01 17:00:00',3),(100,'2024-08-01 17:00:00',3),(101,'2024-08-01 17:00:00',3),(102,'2024-08-01 17:00:00',3),(103,'2024-08-01 17:00:00',3),(104,'2024-08-01 17:00:00',3),(105,'2024-08-01 17:00:00',3),(106,'2024-08-01 17:00:00',3),(107,'2024-08-01 17:00:00',3),(108,'2024-08-01 17:00:00',3),(109,'2024-08-01 17:00:00',3),(110,'2024-08-01 17:00:00',3),(111,'2024-08-01 17:00:00',3),(112,'2024-08-01 17:00:00',3),(113,'2024-08-01 17:00:00',3),(114,'2024-08-01 17:00:00',3),(115,'2024-08-01 17:00:00',3),(116,'2024-08-01 17:00:00',3),(117,'2024-08-01 17:00:00',3),(118,'2024-08-01 17:00:00',3),(119,'2024-08-01 17:00:00',3),(120,'2024-08-01 17:00:00',3),(121,'2024-08-01 17:00:00',3),(122,'2024-08-01 17:00:00',3),(123,'2024-08-01 17:00:00',3),(124,'2024-08-01 17:00:00',3),(125,'2024-08-01 17:00:00',3),(126,'2024-08-01 17:00:00',3),(127,'2024-08-01 17:00:00',3),(128,'2024-08-01 17:00:00',3),(129,'2024-08-01 17:00:00',3),(130,'2024-08-01 17:00:00',3),(131,'2024-08-01 17:00:00',3),(132,'2024-08-01 17:00:00',3),(133,'2024-08-01 17:00:00',3),(134,'2024-08-01 17:00:00',3),(135,'2024-08-01 17:00:00',4),(136,'2024-08-01 17:00:00',4),(137,'2024-08-01 17:00:00',3),(138,'2024-08-01 17:00:00',3),(139,'2024-08-01 17:00:00',3),(140,'2024-08-01 17:00:00',3),(141,'2024-08-01 17:00:00',4),(142,'2024-08-01 17:00:00',3),(143,'2024-08-01 17:00:00',4),(144,'2024-08-01 17:00:00',4),(145,'2024-08-01 17:00:00',3),(146,'2024-08-01 17:00:00',3),(147,'2024-08-01 17:00:00',3),(148,'2024-08-01 17:00:00',4),(149,'2024-08-01 17:00:00',3),(150,'2024-08-01 17:00:00',3),(151,'2024-08-01 17:00:00',4),(152,'2024-08-01 17:00:00',3),(153,'2024-08-01 17:00:00',4),(154,'2024-08-01 17:00:00',3),(155,'2024-08-01 17:00:00',3),(156,'2024-08-01 17:00:00',3),(157,'2024-08-01 17:00:00',4),(158,'2024-08-01 17:00:00',4),(159,'2024-08-01 17:00:00',3),(160,'2024-08-01 17:00:00',3),(161,'2024-08-01 17:00:00',3),(162,'2024-08-01 17:00:00',3),(163,'2024-08-01 17:00:00',3),(164,'2024-08-01 17:00:00',4),(165,'2024-08-01 17:00:00',4),(166,'2024-08-01 17:00:00',4),(167,'2024-08-01 17:00:00',4),(168,'2024-08-01 17:00:00',4),(169,'2024-08-01 17:00:00',4),(170,'2024-08-01 17:00:00',3),(171,'2024-08-01 17:00:00',4),(172,'2024-08-01 17:00:00',3),(173,'2024-08-01 17:00:00',3),(174,'2024-08-01 17:00:00',3),(175,'2024-08-01 17:00:00',3),(176,'2024-08-01 17:00:00',3),(177,'2024-08-01 17:00:00',3),(178,'2024-08-01 17:00:00',3),(179,'2024-08-01 17:00:00',3),(180,'2024-08-02 17:00:00',3),(181,'2024-08-02 17:00:00',4),(182,'2024-08-02 17:00:00',3),(183,'2024-08-02 17:00:00',3),(184,'2024-08-02 17:00:00',3),(185,'2024-08-02 17:00:00',3),(186,'2024-08-02 17:00:00',3),(187,'2024-08-02 17:00:00',3),(188,'2024-08-02 17:00:00',3),(189,'2024-08-02 17:00:00',3),(190,'2024-08-02 17:00:00',4),(191,'2024-08-02 17:00:00',3),(192,'2024-08-02 17:00:00',3),(193,'2024-08-02 17:00:00',4),(194,'2024-08-02 17:00:00',4),(195,'2024-08-02 17:00:00',4),(196,'2024-08-02 17:00:00',4),(197,'2024-08-02 17:00:00',4),(198,'2024-08-02 17:00:00',4),(199,'2024-08-02 17:00:00',4),(200,'2024-08-02 17:00:00',4),(201,'2024-08-02 17:00:00',4),(202,'2024-08-02 17:00:00',4),(203,'2024-08-02 17:00:00',4),(204,'2024-08-02 17:00:00',4),(205,'2024-08-02 17:00:00',4),(206,'2024-08-02 17:00:00',4),(207,'2024-08-02 17:00:00',4),(208,'2024-08-02 17:00:00',4),(209,'2024-08-02 17:00:00',4),(210,'2024-08-02 17:00:00',3),(211,'2024-08-02 17:00:00',3),(212,'2024-08-02 17:00:00',3),(213,'2024-08-02 17:00:00',3),(214,'2024-08-02 17:00:00',3),(215,'2024-08-02 17:00:00',3),(216,'2024-08-02 17:00:00',3);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (3,_binary '',_binary '','/user/assets/images/profile/66ebe4fd-d750-403a-8092-2f2b9788b0da_324042873_892292125299907_176441753814452640_n.jpg','',NULL,'2024-08-01','2024-08-13','user2@gmail.com','Nguyễn Hữu Quý',_binary '','2024-08-03 09:18:46.829326','$2a$10$hGpBj.Crey0XrThGMEC7FeZ86awiW1jD0ws79xRnp3F.oYcQk7eUO','0741258963','quy',2),(4,_binary '',_binary '','/user/assets/images/profile/6eed4555-1cf8-43fa-a8c8-b8e7b95c23bc_324961088_1197653667553080_5512993496072532610_n.jpg','',NULL,'2024-08-01','2024-08-22','us2er2@gmail.com','namm',_binary '','2024-08-03 09:19:19.149463','$2a$10$R8tSjEHrFBVtRucmqOezmeqzIpICb9zykr3cRCZEvPpO86odc8dbS','0123456985','nam',2);
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

-- Dump completed on 2024-08-03 11:47:34

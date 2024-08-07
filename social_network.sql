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
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (2,'fgdf','2024-08-06 13:59:52.522913',2,2),(3,'vv','2024-08-06 14:00:15.120248',2,2),(4,'j','2024-08-06 14:01:36.505243',2,2),(5,'ff','2024-08-06 14:18:54.776125',2,1),(6,'ggg','2024-08-06 14:19:42.087976',2,2),(7,'dg','2024-08-06 14:20:23.232035',2,2),(8,'dg','2024-08-06 14:22:40.346874',2,2),(9,'dg','2024-08-06 14:24:43.689736',2,2),(10,'vbc','2024-08-06 14:25:58.856126',3,2),(11,'dfdf','2024-08-06 14:27:05.961739',3,1),(12,'gfd','2024-08-06 14:28:56.317177',3,3),(13,'gfd','2024-08-06 14:30:49.483859',3,3),(14,'yu','2024-08-06 14:31:23.480367',3,1),(15,'','2024-08-06 14:32:46.493946',3,1),(16,'dfs','2024-08-06 14:33:19.726450',3,2),(17,'dfgg','2024-08-06 14:34:28.321800',4,3),(18,'dfgg','2024-08-06 14:35:31.403880',4,3),(19,'jh','2024-08-06 15:40:19.084609',4,1),(20,'dsfd','2024-08-06 15:48:24.935452',3,1),(21,'dsfsdf','2024-08-06 16:51:56.050508',3,1),(22,'vvvv','2024-08-06 16:53:55.496335',3,1),(23,'fff','2024-08-06 16:54:58.387869',3,1),(24,'fdfdf','2024-08-06 17:00:05.448296',3,1),(25,'yggy','2024-08-06 17:00:35.427474',3,1),(26,'ssss','2024-08-06 17:01:16.839988',3,1),(27,'rrr','2024-08-06 17:02:38.991077',3,1);
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
INSERT INTO `friend_requests` VALUES (1,'2024-08-06 13:59:47.750122','2024-08-06 06:59:45','ACCEPTED',1,2),(2,'2024-08-06 14:28:46.878273','2024-08-06 07:28:32','ACCEPTED',2,3);
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_membership`
--

LOCK TABLES `group_membership` WRITE;
/*!40000 ALTER TABLE `group_membership` DISABLE KEYS */;
INSERT INTO `group_membership` VALUES (4,_binary '',3,1),(6,_binary '',3,3),(7,_binary '\0',3,2);
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
  `is_active` bit(1) DEFAULT NULL,
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
INSERT INTO `group_table` VALUES (3,'/user/assets/images/profile/32f653fc-92d2-43ca-98fe-fdc0529d08fc_GauTruc.png','aaa','aaaa',1,_binary '');
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
  `post_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe2l07hc93u2bbjnl80meu3rn4` (`post_id`),
  CONSTRAINT `FKe2l07hc93u2bbjnl80meu3rn4` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES (1,'user/assets/images/post/279455187_594598114865951_7440947279832479676_n.jpg',NULL);
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
  `group_id` bigint DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  `requester_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK44a2v4gnkoan3dk07ioschkjj` (`addressee_id`),
  KEY `FKcuf29gvqg0junohi1ubmnog5o` (`group_id`),
  KEY `FK4ba33qvlquxq8icg5nfaqlhy7` (`post_id`),
  KEY `FK71e8feaf5xvh8byj81w1fm5pq` (`requester_id`),
  CONSTRAINT `FK44a2v4gnkoan3dk07ioschkjj` FOREIGN KEY (`addressee_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK4ba33qvlquxq8icg5nfaqlhy7` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK71e8feaf5xvh8byj81w1fm5pq` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKcuf29gvqg0junohi1ubmnog5o` FOREIGN KEY (`group_id`) REFERENCES `group_table` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (5,'user1 nguyen đã chấp nhận lời mời kết bạn của bạn','2024-08-06 13:59:47.757597',_binary '','ACCEPT FRIENDREQUEST',2,NULL,NULL,1),(6,' đã thích bài viết của bạn!','2024-08-06 13:59:50.202131',_binary '','LIKE',1,NULL,2,2),(7,'user2 tran đã bình luận bài viết của bạn!','2024-08-06 13:59:52.528913',_binary '','COMMENT',1,NULL,2,2),(8,'user2 tran đã bình luận bài viết của bạn!','2024-08-06 13:59:52.532999',_binary '','COMMENT',1,NULL,2,2),(9,'user2 tran đã bình luận bài viết của bạn!','2024-08-06 14:00:15.124252',_binary '','COMMENT',1,NULL,2,2),(10,'user2 tran đã bình luận bài viết của bạn!','2024-08-06 14:00:15.127253',_binary '','COMMENT',1,NULL,2,2),(11,'user2 tran đã bình luận bài viết của bạn!','2024-08-06 14:23:22.196050',_binary '','COMMENT',1,NULL,2,2),(12,'user2 tran đã bình luận bài viết của bạn!','2024-08-06 14:24:54.245915',_binary '','COMMENT',1,NULL,2,2),(13,'user2 tran đã đăng bài viết lên trang cá nhân của bạn','2024-08-06 14:25:25.551660',_binary '','POST',1,NULL,3,2),(14,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 14:27:38.025155',_binary '','COMMENT',2,NULL,3,1),(15,'user2 tran đã chấp nhận lời mời kết bạn của bạn','2024-08-06 14:28:46.882849',_binary '','ACCEPT FRIENDREQUEST',3,NULL,NULL,2),(16,'admin LE đã bình luận bài viết của bạn!','2024-08-06 14:31:03.357415',_binary '','COMMENT',2,NULL,3,3),(17,'admin LE đã bình luận về một bài viết liên quan đến bạn!','2024-08-06 14:31:04.458137',_binary '','COMMENT',1,NULL,3,3),(18,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 14:33:05.888870',_binary '','COMMENT',2,NULL,3,1),(19,'admin LE đã bình luận bài viết của bạn!','2024-08-06 14:34:45.727036',_binary '','COMMENT',2,NULL,4,3),(20,'admin LE đã bình luận bài viết của bạn!','2024-08-06 14:34:46.078188',_binary '','COMMENT',2,NULL,4,3),(21,'admin LE đã bình luận bài viết của bạn!','2024-08-06 14:35:51.360965',_binary '','COMMENT',2,NULL,4,3),(22,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 15:40:21.295856',_binary '\0','COMMENT',2,NULL,4,1),(23,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 15:48:26.878387',_binary '\0','COMMENT',2,NULL,3,1),(24,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 16:51:57.448870',_binary '\0','COMMENT',2,NULL,3,1),(25,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 16:53:58.895153',_binary '\0','COMMENT',2,NULL,3,1),(26,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 16:54:59.261019',_binary '\0','COMMENT',2,NULL,3,1),(27,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 17:00:06.139833',_binary '\0','COMMENT',2,NULL,3,1),(28,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 17:00:36.281184',_binary '\0','COMMENT',2,NULL,3,1),(29,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 17:01:17.484400',_binary '\0','COMMENT',2,NULL,3,1),(30,'user1 nguyen đã bình luận bài viết của bạn!','2024-08-06 17:02:39.479176',_binary '\0','COMMENT',2,NULL,3,1),(31,'đã gửi yêu cầu tham gia nhóm aaaa','2024-08-06 23:37:58.811826',_binary '','REQUEST_TO_JOIN_GROUP',1,3,NULL,2),(32,'đã gửi yêu cầu tham gia nhóm aaaa','2024-08-06 23:38:09.345544',_binary '','REQUEST_TO_JOIN_GROUP',1,3,NULL,3),(33,'Chúc mừng! Bạn đã là thành viên của nhóm aaaa??','2024-08-06 23:40:56.466949',_binary '\0','ACCEPTED_JOINING_GROUP',2,3,NULL,1),(34,'Chúc mừng! Bạn đã là thành viên của nhóm aaaa??','2024-08-06 23:41:02.387384',_binary '\0','ACCEPTED_JOINING_GROUP',3,3,NULL,1),(35,'đã gửi yêu cầu đăng bài trong nhóm aaaa','2024-08-06 23:59:16.098167',_binary '','REVIEW_POST',1,3,5,2),(36,'Bài viết của bạn đã được chấp nhận','2024-08-07 00:00:01.551439',_binary '\0','APPROVE_POST',2,3,5,1),(37,'đã gửi yêu cầu tham gia nhóm aaaa','2024-08-07 10:54:56.095974',_binary '','REQUEST_TO_JOIN_GROUP',1,3,NULL,2),(38,'đã gửi yêu cầu đăng bài trong nhóm aaaa','2024-08-07 10:58:17.716497',_binary '','REVIEW_POST',1,3,6,3);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (2,'<p>dgvdfg</p>',_binary '',1,'2024-08-06 13:59:34.336690',NULL,1,1),(3,'<p>gdffdg</p>',_binary '',0,'2024-08-06 14:25:25.540135',NULL,1,2),(4,'<p>fdgf</p>',_binary '',0,'2024-08-06 14:34:15.531924',NULL,2,2),(5,'<p>asas</p>',_binary '',0,'2024-08-06 23:59:16.084413',3,NULL,2),(6,'<p>dfgdg</p>',_binary '\0',0,'2024-08-07 10:58:17.701496',3,NULL,3);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
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
INSERT INTO `post_likes` VALUES (2,2);
/*!40000 ALTER TABLE `post_likes` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statistic`
--

LOCK TABLES `statistic` WRITE;
/*!40000 ALTER TABLE `statistic` DISABLE KEYS */;
INSERT INTO `statistic` VALUES (1,'2024-08-05 17:00:00',1),(2,'2024-08-05 17:00:00',2),(3,'2024-08-05 17:00:00',1),(4,'2024-08-05 17:00:00',1),(5,'2024-08-05 17:00:00',2),(6,'2024-08-05 17:00:00',2),(7,'2024-08-05 17:00:00',1),(8,'2024-08-05 17:00:00',1),(9,'2024-08-05 17:00:00',1),(10,'2024-08-05 17:00:00',2),(11,'2024-08-05 17:00:00',1),(12,'2024-08-05 17:00:00',1),(13,'2024-08-05 17:00:00',2),(14,'2024-08-05 17:00:00',1),(15,'2024-08-05 17:00:00',2),(16,'2024-08-05 17:00:00',1),(17,'2024-08-05 17:00:00',1),(18,'2024-08-05 17:00:00',2),(19,'2024-08-05 17:00:00',2),(20,'2024-08-05 17:00:00',2),(21,'2024-08-05 17:00:00',1),(22,'2024-08-05 17:00:00',1),(23,'2024-08-05 17:00:00',3),(24,'2024-08-05 17:00:00',2),(25,'2024-08-05 17:00:00',3),(26,'2024-08-05 17:00:00',1),(27,'2024-08-05 17:00:00',2),(28,'2024-08-05 17:00:00',3),(29,'2024-08-05 17:00:00',2),(30,'2024-08-05 17:00:00',2),(31,'2024-08-05 17:00:00',3),(32,'2024-08-05 17:00:00',3),(33,'2024-08-05 17:00:00',3),(34,'2024-08-05 17:00:00',3),(35,'2024-08-05 17:00:00',3),(36,'2024-08-05 17:00:00',2),(37,'2024-08-05 17:00:00',2),(38,'2024-08-05 17:00:00',2),(39,'2024-08-05 17:00:00',1),(40,'2024-08-05 17:00:00',1),(41,'2024-08-05 17:00:00',1),(42,'2024-08-05 17:00:00',1),(43,'2024-08-05 17:00:00',1),(44,'2024-08-05 17:00:00',1),(45,'2024-08-05 17:00:00',1),(46,'2024-08-05 17:00:00',1),(47,'2024-08-05 17:00:00',1),(48,'2024-08-05 17:00:00',2),(49,'2024-08-05 17:00:00',3),(50,'2024-08-05 17:00:00',1),(51,'2024-08-05 17:00:00',1),(52,'2024-08-06 17:00:00',1),(53,'2024-08-06 17:00:00',1),(54,'2024-08-06 17:00:00',2),(55,'2024-08-06 17:00:00',2),(56,'2024-08-06 17:00:00',2),(57,'2024-08-06 17:00:00',2),(58,'2024-08-06 17:00:00',2),(59,'2024-08-06 17:00:00',1),(60,'2024-08-06 17:00:00',1),(61,'2024-08-06 17:00:00',1),(62,'2024-08-06 17:00:00',2),(63,'2024-08-06 17:00:00',2),(64,'2024-08-06 17:00:00',1),(65,'2024-08-06 17:00:00',2),(66,'2024-08-06 17:00:00',2),(67,'2024-08-06 17:00:00',1),(68,'2024-08-06 17:00:00',1),(69,'2024-08-06 17:00:00',1),(70,'2024-08-06 17:00:00',1),(71,'2024-08-06 17:00:00',1),(72,'2024-08-06 17:00:00',1),(73,'2024-08-06 17:00:00',1),(74,'2024-08-06 17:00:00',1),(75,'2024-08-06 17:00:00',2),(76,'2024-08-06 17:00:00',3),(77,'2024-08-06 17:00:00',1),(78,'2024-08-06 17:00:00',1),(79,'2024-08-06 17:00:00',2),(80,'2024-08-06 17:00:00',3),(81,'2024-08-06 17:00:00',4),(82,'2024-08-06 17:00:00',4),(83,'2024-08-06 17:00:00',4);
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
INSERT INTO `user` VALUES (1,_binary '',_binary '\0','/user/assets/images/profile/d57038e1-b522-47cc-b536-fd4c7336116a_324042873_892292125299907_176441753814452640_n.jpg','Quý đây','/user/assets/images/profile/bad8ae54-e623-4ac8-8300-a12b8d8710e0_279455187_594598114865951_7440947279832479676_n.jpg','2024-08-06','2024-08-16','user1@gmail.com','user1 nguyen',_binary '','2024-08-07 11:04:40.249963','$2a$10$mcTLY35DI.Z/b6VX8t0a9.xrpgoFkwazI3QHf2BYAVWHX8nUsiA5.','0123456789','quy',2),(2,_binary '',_binary '\0','/user/assets/images/profile/6f36135b-4beb-4bb6-8354-5f4948c45398_324961088_1197653667553080_5512993496072532610_n.jpg','nam day','/user/assets/images/profile/80eb18e4-48f9-4538-aedd-8160d0197d4a_279455187_594598114865951_7440947279832479676_n.jpg','2024-08-06','2024-08-01','user2@gmail.com','user2 tran',_binary '','2024-08-07 11:05:00.780218','$2a$10$3E7COTF4iwBPKM/pVCWLZeB0fyherjms268hkqkQ1FZO0bIJdFpme','0352477783','nam',2),(3,_binary '',_binary '\0','/user/assets/images/profile/c9ea2b9d-8eb9-4775-b3d7-cf1df0596188_Cat_and_Dog.png','viet day','/user/assets/images/profile/7758fc75-8d35-496f-a90d-669c3cf8d039_369509067_658592803009021_2283685630784746568_n.jpg','2024-08-06','2024-08-10','tranthihieu321@gmail.com','admin LE',_binary '','2024-08-07 11:05:49.291036','$2a$10$QJpJFOOju8uh20J7kR07TOr3mtEf0ab75mw2hH.80eSRI3gDOqF8K','0987654321','viet',2),(4,_binary '',_binary '','/user/assets/images/profile/f5183091-076c-4346-b6c9-1b56c9301fb1_Picture1.png','admin day','/user/assets/images/profile/96415d71-41b5-4371-8382-0a270a33bc6c_394406732_1403894557216953_5815200498922665863_n.jpg','2024-08-07','2024-08-22','admin@gmail.com','admin trigg',_binary '\0',NULL,'$2a$10$VpUNxvh4mpNrci6dW6DVredCp73VOmyYw7EEb5RdCESQnZxufL.va','0147852369','admin',1);
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
  `post_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm1rcfqyngqcam9rcq4bx21sbl` (`post_id`),
  CONSTRAINT `FKm1rcfqyngqcam9rcq4bx21sbl` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES (1,'user/assets/images/post/video-1656257903.mp4',NULL);
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

-- Dump completed on 2024-08-07 11:08:12

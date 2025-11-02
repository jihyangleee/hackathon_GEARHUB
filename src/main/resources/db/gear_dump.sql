-- MySQL 덤프 스크립트 (논리적 의존성 순서 반영)
--
-- 1. 의존성이 없는 테이블 (category, user)
-- 2. 1번 테이블을 참조하는 테이블 (article, journal)
-- 3. 1, 2번 테이블을 참조하는 테이블 (equipment, comment)
-- 4. 1, 3번 테이블을 참조하는 테이블 (wishlist)
-- ------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 데이터베이스 생성 및 선택
CREATE DATABASE IF NOT EXISTS gear;
USE gear;

--
-- 1순위: 의존성이 없는 테이블 (category, user)
--

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `category_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'캠핑과 관련된 다양한 정보와 팁을 제공합니다.','캠핑','camping'),(2,'등산과 트래킹을 위한 장비 및 노하우를 공유합니다.','등산','hiking'),(3,'낚시를 위한 장비, 포인트, 그리고 낚시 팁을 알려드립니다.','낚시','fishing'),(4,'클라이밍을 즐기는 사람들을 위한 장비와 안전 가이드입니다.','클라이밍','climbing'),(5,'건강한 취미와 이동수단을 동시에 즐길 수 있는 자전거의 세계를 소개합니다.','자전거','cycling'),(6,'테니스 입문자부터 숙련자까지 즐길 수 있는 장비와 기술 정보를 제공합니다.','테니스','tennis');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'2025-10-30 02:29:00.478922','hanna1234@naver.com','$2a$10$zO.QLy/vgPAnbUKAX3op0Og48OhrkCj0y3bHAiigHVwlnyPL3L57O','choihanna',NULL,''),(2,'2025-10-30 02:29:26.115671','hanna123@gmail.com','$2a$10$EWu0OP4uEKxKCX6AxyZXkufwIpya6CFcFpEylQfIhxX/nqiuOv65C','hanna',NULL,''),(7,'2025-10-31 02:11:32.933981','hannachoe53@gmail.com','$2a$10$n3J22zXarLyBvueeg1vcsupjuasSeqcaKWiA3CP4bb7t4lzg.cYUO','hannachoi','http://localhost:8080/uploads/d31adc0c-44ee-4896-b7f5-5b5c4d9dc72d_IMG_6942.jpeg',''),(8,'2025-11-01 08:23:09.964394','hannachoe53@naver.com','Qwer1234!','hanna-choi',NULL,''),(9,'2025-11-01 11:34:34.198008','hannachoe53@qwer.com','$2a$10$fvxbrJANoMWCB2rIjBr6Z.EvFLzMQ4ma5Rb3VY7Ge0Z7FNg4rMkC2','abcdef',NULL,'');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


--
-- 2순위: 1순위 테이블을 참조하는 테이블 (article, journal)
--

--
-- Table structure for table `article`
--
DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `article_id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `content` text,
  `slug` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`article_id`),
  UNIQUE KEY `UKlc76j4bqg2jrk06np18eve5yj` (`slug`),
  KEY `FKy5kkohbk00g0w88fi05k2hcw` (`category_id`),
  CONSTRAINT `FKy5kkohbk00g0w88fi05k2hcw` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (1,1,'2025-11-01 03:29:42.000000','2025-11-01 03:29:42.000000','캠핑을 처음 시작하는 분들을 위한 필수 장비, 준비물, 그리고 안전 수칙을 담은 가이드입니다.','캠핑마스터','캠핑은 자연 속에서 휴식을 취하고 새로운 경험을 할 수 있는 멋진 활동입니다.\n                                                                                                          하지만 처음 시작하는 초보 캠퍼들에게는 무엇부터 준비해야 할지 막막할 수 있습니다.\n                                                                                                          이 글에서는 텐트, 침낭, 코펠 등 필수 캠핑 장비부터 캠핑장 선택 요령,\n                                                                                                          그리고 안전하고 즐거운 캠핑을 위한 팁까지 모두 알려드립니다.','beginner-camping-guide','초보 캠퍼를 위한 완벽 가이드'),(2,1,'2025-11-01 03:29:42.000000','2025-11-01 03:29:42.000000','캠핑장에서 간편하게 만들 수 있는 맛있고 특별한 요리 레시피 5가지를 소개합니다.','요리하는 캠퍼','캠핑의 꽃은 역시 맛있는 음식이죠! 복잡한 준비 없이도 캠핑 분위기를 살릴 수 있는\n                                                                                                          쉽고 맛있는 레시피 5가지를 준비했습니다. 바비큐, 찌개, 볶음밥, 디저트까지 다양한 메뉴를 즐겨보세요.','easy-camping-recipes','캠핑 요리의 모든 것: 쉽고 맛있는 레시피 5가지'),(3,1,'2025-11-01 03:29:42.000000','2025-11-01 03:29:42.000000','자연을 보호하며 캠핑을 즐기기 위한 친환경 실천 방법들을 알아봅니다.','그린 캠퍼','캠핑은 자연을 만끽하는 활동이지만, 동시에 환경에 영향을 줄 수도 있습니다.\n                                                                                                          이 글에서는 쓰레기 줄이기, 친환경 세제 사용, 불 피우기 주의사항 등\n                                                                                                          환경을 보호하며 캠핑을 즐기는 방법을 소개합니다.','eco-friendly-camping-tips','지속 가능한 캠핑을 위한 친환경 팁'),(4,2,'2025-11-01 03:31:27.000000','2025-11-01 03:31:27.000000','등산을 처음 시작하는 분들을 위한 안전 수칙과 기본 장비 가이드입니다.','하이커민','등산은 자연과 교감할 수 있는 훌륭한 활동이지만, 안전 준비가 부족하면 위험할 수 있습니다.\n                                                                                                          이 글에서는 등산 전 준비물, 등산화 선택, 스트레칭, 하산 시 주의사항 등을 다룹니다.','mountain-safety-guide','등산 초보를 위한 안전 가이드'),(5,2,'2025-11-01 03:31:27.000000','2025-11-01 03:31:27.000000','대한민국에서 꼭 가봐야 할 등산 코스 다섯 곳을 소개합니다.','트래킹러버','한라산, 지리산, 설악산 등 국내 대표 명산을 중심으로 초보자부터 숙련자까지 즐길 수 있는\n                                                                                                          다양한 코스를 추천합니다. 계절별로 다른 매력을 느껴보세요.','top5-hiking-trails','국내 최고의 등산 코스 TOP 5'),(6,3,'2025-11-01 03:31:29.000000','2025-11-01 03:31:29.000000','처음 낚시를 시작하는 사람들을 위한 장비, 포인트, 그리고 기본 테크닉을 알려드립니다.','낚시왕','낚시는 인내심과 기술이 필요한 취미입니다. 이 글에서는 낚싯대 선택부터 미끼 종류, 포인트 선정까지\n                                                                                                          초보자가 알아야 할 기본기를 소개합니다.','beginner-fishing-tips','초보 낚시꾼을 위한 가이드'),(7,3,'2025-11-01 03:31:29.000000','2025-11-01 03:31:29.000000','바다낚시와 민물낚시의 차이점을 비교하고 입문자에게 맞는 낚시를 추천합니다.','피싱맨','낚시 환경, 장비, 어종이 다른 두 낚시의 매력을 자세히 비교합니다.\n                                                                                                          자신에게 맞는 낚시 스타일을 찾아보세요.','sea-vs-freshwater-fishing','바다 낚시 vs 민물 낚시, 무엇이 다를까?'),(8,4,'2025-11-01 03:31:36.000000','2025-11-01 03:31:36.000000','클라이밍을 시작하기 전 꼭 준비해야 할 필수 장비 목록을 소개합니다.','클라이머김','안전벨트, 로프, 카라비너, 초크백 등 클라이밍의 핵심 장비를 다룹니다.\n                                                                                                          안전하게 즐기기 위한 팁도 함께 확인하세요.','climbing-gear-checklist','클라이밍 장비 체크리스트'),(9,4,'2025-11-01 03:31:36.000000','2025-11-01 03:31:36.000000','실내와 실외 클라이밍의 차이점, 장단점, 그리고 추천 장소를 비교합니다.','볼더러정','초보자에게는 실내 클라이밍이 접근성이 좋고, 숙련자에게는 실외 클라이밍이 도전적입니다.\n                                                                                                          자신의 스타일에 맞는 환경을 선택해보세요.','indoor-vs-outdoor-climbing','실내 vs 실외 클라이밍, 어떤 게 나을까?'),(10,5,'2025-11-01 05:21:06.000000','2025-11-01 05:21:06.000000','처음 자전거를 시작하는 사람들을 위한 필수 장비와 안전 수칙을 소개합니다.','라이더민','자전거는 단순한 이동수단을 넘어 건강한 라이프스타일의 시작입니다. 이 글에서는 입문용 자전거 선택법, 헬멧과 장비 고르는 법, 도심 라이딩 시 주의할 점 등 초보자가 알아야 할 모든 정보를 제공합니다. 안전하고 즐거운 라이딩을 위한 첫걸음을 함께 해보세요.','beginner-cycling-guide','자전거 입문자를 위한 완벽 가이드'),(11,5,'2025-11-01 05:21:06.000000','2025-11-01 05:21:06.000000','안전하고 편안한 주행을 위한 필수 자전거 장비 리스트를 정리했습니다.','기어박','자전거를 즐기기 위해선 헬멧, 장갑, 라이트, 공구세트 등 준비가 필수입니다. 이 글에서는 각 장비의 역할과 선택 시 고려해야 할 포인트를 소개합니다. 올바른 장비 선택이 라이딩의 만족도를 높이고, 안전을 지켜줍니다.','cycling-gear-checklist','라이더를 위한 자전거 장비 체크리스트'),(12,6,'2025-11-01 05:21:13.000000','2025-11-01 05:21:13.000000','처음 테니스를 배우는 분들을 위한 기본 자세, 라켓 선택법, 경기 규칙을 안내합니다.','테니스코치박','테니스는 기술과 체력이 조화된 매력적인 스포츠입니다. 이 글에서는 초보자가 꼭 알아야 할 포핸드, 백핸드 기본 자세와 서브 연습법, 그리고 자신에게 맞는 라켓 고르는 방법을 자세히 설명합니다. 올바른 기초가 탄탄한 실력으로 이어집니다.','tennis-beginner-guide','테니스 초보자를 위한 입문 가이드'),(13,6,'2025-11-01 05:21:13.000000','2025-11-01 05:21:13.000000','라켓 무게, 밸런스, 그립 크기 등 나에게 딱 맞는 테니스 라켓을 고르는 법을 알려드립니다.','라켓마스터','라켓은 테니스 실력을 좌우하는 핵심 장비입니다. 이 글에서는 다양한 브랜드의 특징과 플레이 스타일별 라켓 추천 리스트를 제공합니다. 자신의 스타일에 맞는 라켓을 선택해 보다 즐거운 테니스 라이프를 시작하세요.','tennis-racket-guide','테니스 라켓 선택의 모든 것');
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `journal`
--

DROP TABLE IF EXISTS `journal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `journal` (
  `journal_id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `purchased_date` date DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `equipment_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`journal_id`),
  KEY `FK422gwvrsuvdvp1v41qivhcwv9` (`equipment_id`),
  KEY `FKjaueygmi2gs6yi766n25ptvnp` (`user_id`),
  CONSTRAINT `FKjaueygmi2gs6yi766n25ptvnp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `journal`
--

LOCK TABLES `journal` WRITE;
/*!40000 ALTER TABLE `journal` DISABLE KEYS */;
/*!40000 ALTER TABLE `journal` ENABLE KEYS */;
UNLOCK TABLES;


--
-- 3순위: 1, 2순위 테이블을 참조하는 테이블 (equipment, comment)
--

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `equipment_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `purchase_url` text,
  `price` int DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `article_id` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`equipment_id`),
  KEY `fk_equipment_article` (`article_id`),
  KEY `fk_equipment_category` (`category_id`),
  CONSTRAINT `fk_equipment_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE SET NULL,
  CONSTRAINT `fk_equipment_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES (3,'캠핑용 LED 랜턴','따뜻한 색감의 충전식 랜턴, 3단 밝기 조절 가능','https://thumbnail.coupangcdn.com/thumbnails/remote/320x320ex/image/retail/images/1315321465570274-95137a2e-f7a9-41c5-98c7-8af8a6943023.jpg','https://www.coupang.com/vp/products/7853897945?itemId=23009013943&vendorItemId=90042865103&pickType=COU_PICK&q=%EC%BA%A0%ED%95%91%EC%9A%A9%20%EB%9E%9C%ED%84%B4&searchId=38ebd605731631&sourceType=search&itemsCount=36&searchRank=5&rank=5',54000,NULL,1,1,'2025-11-01 08:56:30','2025-11-01 08:56:30'),(4,'폴딩 캠핑 테이블','알루미늄 프레임, 간편 조립형, 4인용 캠핑 테이블','https://thumbnail.coupangcdn.com/thumbnails/remote/320x320ex/image/retail/images/2021/12/09/17/2/3370c9dd-dd35-44a0-b5b7-ae649bcbff2d.jpg','https://www.coupang.com/vp/products/6212695021?itemId=12538690315&vendorItemId=79807104210&sourceType=srp_product_ads&clickEventId=928b5a20-b6b4-11f0-b89d-7281ac067990&korePlacement=15&koreSubPlacement=1&clickEventId=928b5a20-b6b4-11f0-b89d-7281ac067990&korePlacement=15&koreSubPlacement=1',58000,NULL,2,1,'2025-11-01 08:56:30','2025-11-01 08:56:30'),(5,'캠핑 접의식 의자','스테인리스 2P 세트, 보관용 파우치 포함','https://thumbnail.coupangcdn.com/thumbnails/remote/320x320ex/image/retail/images/338549949030981-7d7e8d2f-6c95-4ca4-bd7b-9d8993256fbd.jpg','https://brand.naver.com/logos/products/5489950507?nl-query=%EC%BA%A0%ED%95%91%20%EC%9D%98%EC%9E%90&nl-au=f35e17d060d143f6a3b65cd6c0596938&NaPm=ci%3Df35e17d060d143f6a3b65cd6c0596938%7Cct%3Dmhfgpuef%7Ctr%3Dnslcrm%7Csn%3D308252%7Chk%3Db110d126150d6011f8f91144471d34a09c050629',159000,NULL,3,1,'2025-11-01 08:56:30','2025-11-01 08:56:30'),(6,'캠핑용 LED 랜턴','따뜻한 색감의 충전식 랜턴, 3단 밝기 조절 가능','https://thumbnail.coupangcdn.com/thumbnails/remote/320x320ex/image/retail/images/1315321465570274-95137a2e-f7a9-41c5-98c7-8af8a6943023.jpg','https://www.coupang.com/vp/products/7853897945?itemId=23009013943&vendorItemId=90042865103&pickType=COU_PICK&q=%EC%BA%A0%ED%95%91%EC%9A%A9%20%EB%9E%9C%ED%84%B4&searchId=38ebd605731631&sourceType=search&itemsCount=36&searchRank=5&rank=5',54000,NULL,1,1,'2025-11-01 17:13:01','2025-11-01 17:13:01'),(7,'폴딩 캠핑 테이블','알루미늄 프레임, 간편 조립형, 4인용 캠핑 테이블','https://thumbnail.coupangcdn.com/thumbnails/remote/320x320ex/image/retail/images/2021/12/09/17/2/3370c9dd-dd35-44a0-b5b7-ae649bcbff2d.jpg','https://www.coupang.com/vp/products/6212695021?itemId=12538690315&vendorItemId=79807104210&sourceType=srp_product_ads&clickEventId=928b5a20-b6b4-11f0-b89d-7281ac067990&korePlacement=15&koreSubPlacement=1&clickEventId=928b5a20-b6b4-11f0-b89d-7281ac067990&korePlacement=15&koreSubPlacement=1',58000,NULL,2,1,'2025-11-01 17:13:01','2025-11-01 17:13:01'),(8,'캠핑 접의식 의자','스테인리스 2P 세트, 보관용 파우치 포함','https://thumbnail.coupangcdn.com/thumbnails/remote/320x320ex/image/retail/images/338549949030981-7d7e8d2f-6c95-4ca4-bd7b-9d8993256fbd.jpg','https://brand.naver.com/logos/products/5489950507?nl-query=%EC%BA%A0%ED%95%91%20%EC%9D%98%EC%9E%90&nl-au=f35e17d060d143f6a3b65cd6c0596938&NaPm=ci%3Df35e17d060d143f6a3b65cd6c0596938%7Cct%3Dmhfgpuef%7Ctr%3Dnslcrm%7Csn%3D308252%7Chk%3Db110d126150d6011f8f91144471d34a09c050629',159000,NULL,3,1,'2025-11-01 17:13:01','2025-11-01 17:13:01'),(9,'캠핑용 LED 랜턴','따뜻한 색감의 충전식 랜턴, 3단 밝기 조절 가능','https://thumbnail.coupangcdn.com/thumbnails/remote/320x320ex/image/retail/images/1315321465570274-95137a2e-f7a9-41c5-98c7-8af8a6943023.jpg','https://www.coupang.com/vp/products/7853897945?itemId=23009013943&vendorItemId=90042865103&pickType=COU_PICK&q=%EC%BA%A0%ED%95%91%EC%9A%A9%20%EB%9E%9C%ED%84%B4&searchId=38ebd605731631&sourceType=search&itemsCount=36&searchRank=5&rank=5',54000,NULL,1,1,'2025-11-01 17:13:05','2025-11-01 17:13:05'),(10,'폴딩 캠핑 테이블','알루미늄 프레임, 간편 조립형, 4인용 캠핑 테이블','https://thumbnail.coupangcdn.com/thumbnails/remote/320x320ex/image/retail/images/2021/12/09/17/2/3370c9dd-dd35-44a0-b5b7-ae649bcbff2d.jpg','https://www.coupang.com/vp/products/6212695021?itemId=12538690315&vendorItemId=79807104210&sourceType=srp_product_ads&clickEventId=928b5a20-b6b4-11f0-b89d-7281ac067990&korePlacement=15&koreSubPlacement=1&clickEventId=928b5a20-b6b4-11f0-b89d-7281ac067990&korePlacement=15&koreSubPlacement=1',58000,NULL,2,1,'2025-11-01 17:13:05','2025-11-01 17:13:05'),(11,'캠핑 접의식 의자','스테인리스 2P 세트, 보관용 파우치 포함','https://thumbnail.coupangcdn.com/thumbnails/remote/320x320ex/image/retail/images/338549949030981-7d7e8d2f-6c95-4ca4-bd7b-9d8993256fbd.jpg','https://brand.naver.com/logos/products/5489950507?nl-query=%EC%BA%A0%ED%95%91%20%EC%9D%98%EC%9E%90&nl-au=f35e17d060d143f6a3b65cd6c0596938&NaPm=ci%3Df35e17d060d143f6a3b65cd6c0596938%7Cct%3Dmhfgpuef%7Ctr%3Dnslcrm%7Csn%3D308252%7Chk%3Db110d126150d6011f8f91144471d34a09c050629',159000,NULL,3,1,'2025-11-01 17:13:05','2025-11-01 17:13:05');
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `article_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `content` text NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `author` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id` (`article_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
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
-- 4순위: 1, 3순위 테이블을 참조하는 테이블 (wishlist)
--

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishlist` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `wishlist_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `wishlist_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `equipment` (`equipment_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wishlist`
--

LOCK TABLES `wishlist` WRITE;
/*!40000 ALTER TABLE `wishlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `wishlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- 스크립트 마무리 설정
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-01 23:16:30
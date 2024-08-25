-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: test
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系統管理','system',0,1,'/sys','','M','','2022-07-04 14:56:29','2022-07-04 14:56:31','系统管理目錄'),(2,'盤查資訊','monitor',0,2,'/bsns','','M','','2022-07-04 14:59:43','2024-05-10 15:54:07','業務管理目錄'),(3,'用戶管理','user',1,1,'/sys/user','sys/user/index','C','system:user:list','2022-07-04 15:20:51','2022-07-04 15:20:53','用户管理菜單'),(4,'角色管理','peoples',1,2,'/sys/role','sys/role/index','C','system:role:list','2022-07-04 15:23:35','2022-07-04 15:23:39','角色管理菜單'),(5,'菜單管理','tree-table',1,3,'/sys/menu','sys/menu/index','C','system:menu:list','2022-07-04 15:23:41','2022-07-04 15:23:43','菜單管理菜單'),(6,'監測數據','tree',2,1,'/bsns/carbonMonitoring','bsns/carbonMonitoring','C','','2022-07-04 15:24:40','2022-07-04 15:24:44','部門管理菜單'),(8,'用戶查詢','#',3,1,'',NULL,'F','system:user:query','2022-07-04 15:24:42','2022-07-04 15:24:46','用户查詢按钮'),(9,'用戶新增','#',3,2,'','','F','system:user:add','2022-07-04 15:24:42','2022-07-04 15:24:46','添加用户按鈕'),(10,'用戶修改','#',3,3,'','','F','system:user:edit','2022-07-04 15:24:42','2022-07-04 15:24:46','修改用户按鈕'),(11,'用戶刪除','#',3,4,'','','F','system:user:delete','2022-07-04 15:24:42','2022-07-04 15:24:46','删除用户按鈕'),(12,'分配角色','#',3,5,'','','F','system:user:role','2022-07-04 15:24:42','2022-07-04 15:24:46','分配角色按鈕'),(13,'重置密碼','#',3,6,'','','F','system:user:resetPwd','2022-07-04 15:24:42','2022-07-04 15:24:46','重置密码按鈕'),(14,'測試2','',3,7,'','sys/test','F','system:user:test','2023-07-15 12:22:48','2023-07-15 12:22:58',NULL),(15,'角色查询','#',4,1,'',NULL,'F','system:role:query','2022-07-04 15:24:42','2022-07-04 15:24:46','角色查询按鈕'),(16,'角色新增','#',4,2,'','','F','system:role:add','2022-07-04 15:24:42','2022-07-04 15:24:46','添加用户按鈕'),(17,'角色修改','#',4,3,'','','F','system:role:edit','2022-07-04 15:24:42','2022-07-04 15:24:46','修改用户按鈕'),(18,'角色刪除','#',4,4,'',NULL,'F','system:role:delete','2022-07-04 15:24:42','2022-07-04 15:24:46','删除用户按鈕'),(19,'分配權限','#',4,5,'','','F','system:role:menu','2022-07-04 15:24:42','2022-07-04 15:24:46','分配權限按鈕'),(20,'菜單查詢','#',5,1,'',NULL,'F','system:menu:query','2022-07-04 15:24:42','2022-07-04 15:24:46','菜單查詢按鈕'),(21,'菜單新增','#',5,2,'',NULL,'F','system:menu:add','2022-07-04 15:24:42','2022-07-04 15:24:46','添加菜單按鈕'),(22,'菜單修改','#',5,3,'',NULL,'F','system:menu:edit','2022-07-04 15:24:42','2022-07-04 15:24:46','修改菜單按鈕'),(23,'菜單刪除','#',5,4,'',NULL,'F','system:menu:delete','2022-07-04 15:24:42','2022-07-04 15:24:46','删除菜單按鈕'),(28,'業務表單','edit',0,3,'/formPage','','M','','2024-05-09 22:41:06',NULL,NULL),(29,'醫療部門表單','form',28,1,'/formPage/normal','formPage/normal/index','C','system:form:normal','2024-05-09 22:48:42',NULL,NULL),(30,'人事部門表單','form',28,2,'/formPage/personal','formPage/personal/index','C','system:form:personal','2024-05-09 23:06:27',NULL,NULL),(31,'產生報表','edit',2,3,'/bsns/financial','bsns/financial','C','','2024-05-10 23:03:59',NULL,NULL),(32,'人事部門','edit',0,4,'/personal','','M','','2024-05-11 16:54:28',NULL,NULL),(33,'採購部','peoples',32,1,'/personal/personone','personal/personone','C','system:purchase:list','2024-05-11 17:05:18',NULL,NULL),(34,'人資室','edit',32,2,'/personal/HumanResourcesOffice','personal/HumanResourcesOffice/index','C','system:person:list','2024-05-11 17:08:02',NULL,NULL),(35,'計算薪資','job',34,1,'','','F','system:person:cauculate','2024-05-11 23:45:56',NULL,NULL),(36,'假期安排','edit',34,2,'','','F','system:person:vacation','2024-05-11 23:57:43',NULL,NULL),(37,'績效處理','edit',34,3,'','','F','system:person:performance','2024-05-11 23:59:23',NULL,NULL);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-25 18:57:49

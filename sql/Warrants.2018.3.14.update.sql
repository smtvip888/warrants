/*
 Navicat Premium Data Transfer

 Source Server         : 60.205.188.180
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : 60.205.188.180:3306
 Source Schema         : Warrants

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 14/03/2018 13:23:13
*/

SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Procedure structure for P_GetChildUsers
-- ----------------------------
DROP PROCEDURE IF EXISTS `P_GetChildUsers`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `P_GetChildUsers`(IN `P_UserId` bigint)
BEGIN
	DECLARE sTemp varchar(20000);
	DECLARE sTempChd varchar(20000);

	SET sTemp = '0';
	SET sTempChd = cast(P_UserId as char);

	WHILE sTempChd is not NULL DO
		SET sTemp = CONCAT(sTemp,',',sTempChd);
		SELECT group_concat(UserId) INTO sTempChd FROM Users where FIND_IN_SET(parentUserId,sTempChd);
	END WHILE;
	#SELECT sTemp;
	SELECT * FROM Users WHERE FIND_IN_SET(UserId,sTemp);
END
;;
delimiter ;


SET FOREIGN_KEY_CHECKS = 1;

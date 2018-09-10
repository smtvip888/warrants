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

 Date: 12/02/2018 21:18:43
*/

SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Procedure structure for P_DelUserId
-- ----------------------------
DROP PROCEDURE IF EXISTS `P_DelUserId`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `P_DelUserId`(IN P_UserId bigint)
PL:BEGIN
	DECLARE P_ParentUserId BIGINT;
	DECLARE P_RefundUserId BIGINT;
	DECLARE P_PayUserId BIGINT;
	DECLARE P_Rid BIGINT; 
	DECLARE P_UserName varchar(50); 
	DECLARE P_PlaceArea INT;
	DECLARE P_RegDate datetime;
	
	DECLARE P_Shares BIGINT;
	DECLARE P_ShareId BIGINT;
	DECLARE P_PriceId INT;
	DECLARE P_SharePrice DECIMAL(10,3);
	DECLARE P_ShareDate datetime;

	DECLARE P_BonusType INT;
	DECLARE P_CashBonus DECIMAL(10,3);
	DECLARE P_DTFTBonus DECIMAL(10,3);
	DECLARE P_LYJFBonus DECIMAL(10,3);
	DECLARE P_JTFTBonus DECIMAL(10,3);
	DECLARE P_SCJFBonus DECIMAL(10,3);
	DECLARE P_ZCJF DECIMAL(10,3);
	DECLARE P_PayDTFT DECIMAL(10,3); 
	DECLARE P_PayDTFTId BIGINT;
	DECLARE P_PayZCJF DECIMAL(10,3); 
	DECLARE P_Price DECIMAL(10,3); 
	
	

	IF((SELECT COUNT(*) FROM Users WHERE UserId = P_UserId) = 0) THEN
		LEAVE PL ;  
	END IF;
	
	IF((SELECT COUNT(*) FROM Users WHERE ParentUserId = P_UserId) > 0) THEN
		LEAVE PL ;  
	END IF;

	IF((SELECT COUNT(*) FROM Users WHERE RecommendUserId = P_UserId) > 0) THEN
		LEAVE PL ;  
	END IF;

	SELECT FromUserId INTO P_PayUserId FROM Users_Bonus WHERE UserId = P_UserId AND InOrOut = 1 ORDER BY Id DESC LIMIT 0,1;
	IF(P_PayUserId IS NULL OR P_PayUserId = 0) THEN
		LEAVE PL ;  
	END IF;

	
	SELECT UserName,ParentUserId,PlaceArea,RegDate FROM Users WHERE UserId = P_UserId INTO P_UserName,P_ParentUserId,P_PlaceArea,P_RegDate;
	SET P_ParentUserId = IFNULL(P_ParentUserId,0);

	SELECT Price INTO P_Price FROM Net_Users WHERE UserId = P_UserId  ORDER BY NetUserId DESC LIMIT 0,1;
	SET P_Price = IFNULL(P_Price,0);

	BEGIN
		SELECT MIN(Id) INTO P_Rid FROM Users_Bonus WHERE FromUserId = P_UserId AND InOrOut = 1;
		SET P_Rid = IFNULL(P_Rid,0);
		WHILE (P_Rid > 0 AND P_Rid IS NOT NULL) DO
			SELECT UserId,BonusType,CashBonus,DTFTBonus,LYJFBonus,JTFTBonus,SCJFBonus FROM Users_Bonus WHERE Id = P_Rid 
			INTO P_RefundUserId,P_BonusType,P_CashBonus,P_DTFTBonus,P_LYJFBonus,P_JTFTBonus,P_SCJFBonus;
			SET P_BonusType = IFNULL(P_BonusType,0);
			SET P_CashBonus = IFNULL(P_CashBonus,0);
			SET P_DTFTBonus = IFNULL(P_DTFTBonus,0);
			SET P_LYJFBonus = IFNULL(P_LYJFBonus,0);
			SET P_SCJFBonus = IFNULL(P_SCJFBonus,0);

			
			UPDATE Users_Asset 
			SET TotalBonus = TotalBonus - P_CashBonus ,BonusSurplus = BonusSurplus - P_CashBonus , DTFTJJ = DTFTJJ - P_DTFTBonus , DTFTJJYE = DTFTJJYE - P_DTFTBonus
					, LYJF = LYJF - P_LYJFBonus , LYJFYE = LYJFYE - P_LYJFBonus , SCJF = SCJF - P_SCJFBonus , SCJFYE = SCJFYE - P_SCJFBonus
			WHERE UserId = P_RefundUserId;

			
			UPDATE Shares_Statistics SET DTBonus = DTBonus - P_CashBonus,DTFT = DTFT - P_DTFTBonus,DTLY = DTLY - P_LYJFBonus WHERE to_days(cdate) = to_days(P_RegDate);

			
				
			

			SELECT MIN(Id) INTO P_Rid FROM Users_Bonus WHERE FromUserId = P_UserId AND Id > P_Rid  AND InOrOut = 1;
			SET P_Rid = IFNULL(P_Rid,0);
			SET P_CashBonus = 0;
			SET P_DTFTBonus = 0;
			SET P_LYJFBonus = 0;
			SET P_SCJFBonus = 0;
		END WHILE;
		DELETE FROM Users_Bonus WHERE FromUserId = P_UserId AND InOrOut = 1 AND BonusType <> 2;
	END;

	BEGIN 
		
		SELECT Id,DTFTBonus FROM Users_Bonus WHERE UserId = P_PayUserId AND FromUserId = P_PayUserId AND Remark LIKE CONCAT('%',P_UserName) ORDER BY Id DESC LIMIT 0,1 INTO P_PayDTFTId,P_PayDTFT;
		SET P_PayDTFTId = IFNULL(P_PayDTFTId,0);
		SET P_PayDTFT = IFNULL(P_PayDTFT,0);
		
		SELECT Amount INTO P_PayZCJF FROM Users_ZCJF WHERE UserId = P_PayUserId AND FromUserId = P_UserId;
		SET P_PayZCJF = IFNULL(P_PayZCJF,0);

		UPDATE Users_Asset SET DTFTJJYE = DTFTJJYE + P_PayDTFT,ZCJFYYE = ZCJFYYE + P_PayZCJF WHERE UserId = P_PayUserId;
		IF(P_PayDTFTId > 0) THEN
			DELETE FROM Users_Bonus WHERE Id = P_PayDTFTId;
		END IF;
		DELETE FROM Users_Bonus WHERE UserId = P_UserId AND FromUserId = P_PayUserId;
		DELETE FROM Users_ZCJF WHERE UserId = P_PayUserId AND FromUserId = P_UserId;
	END;
	
	BEGIN 
		WHILE (P_ParentUserId > 0 AND P_ParentUserId IS NOT NULL) DO
			IF (P_PlaceArea = 0) THEN
				UPDATE Users_Asset SET TeamNum = TeamNum - 1,TeamTurnover = TeamTurnover - P_Price,LeftTotalTurnover = LeftTotalTurnover - P_Price,LeftTurnoverSurplus = LeftTurnoverSurplus - P_Price WHERE UserId = P_ParentUserId;
				IF( (SELECT IFNULL(LeftTurnoverSurplus,0) FROM Users_Asset WHERE UserId = P_ParentUserId) < 0 ) THEN
					UPDATE Users_Asset SET LeftTurnoverSurplus = 0 WHERE UserId = P_ParentUserId;
				END IF;
				IF((SELECT COUNT(*) FROM Users_Bonus WHERE UserId = P_ParentUserId AND FromUserId = P_UserId AND BonusType = 2) > 0) THEN
					UPDATE Users_Asset SET RightTurnoverSurplus = RightTurnoverSurplus + P_Price WHERE UserId = P_ParentUserId;
				END IF;
			ELSE
				UPDATE Users_Asset SET TeamNum = TeamNum - 1,TeamTurnover = TeamTurnover - P_Price,RightTotalTurnover = RightTotalTurnover - P_Price,RightTurnoverSurplus = RightTurnoverSurplus - P_Price WHERE UserId = P_ParentUserId;
				IF( (SELECT IFNULL(RightTurnoverSurplus,0) FROM Users_Asset WHERE UserId = P_ParentUserId) < 0 ) THEN
					UPDATE Users_Asset SET RightTurnoverSurplus = 0 WHERE UserId = P_ParentUserId;
				END IF;
				IF((SELECT COUNT(*) FROM Users_Bonus WHERE UserId = P_ParentUserId AND FromUserId = P_UserId AND BonusType = 2) > 0) THEN
					UPDATE Users_Asset SET LeftTurnoverSurplus = LeftTurnoverSurplus + P_Price WHERE UserId = P_ParentUserId;
				END IF;
			END IF;

			DELETE FROM Users_Bonus WHERE UserId = P_ParentUserId AND FromUserId = P_UserId AND BonusType = 2; 

			SELECT ParentUserId,PlaceArea FROM Users WHERE UserId = P_ParentUserId INTO P_ParentUserId,P_PlaceArea;
			SET P_ParentUserId = IFNULL(P_ParentUserId,0);
		END WHILE;

		UPDATE Shares_Statistics SET TotalProfit = TotalProfit + P_Price WHERE to_days(cdate) = to_days(P_RegDate);  

	END;

	BEGIN  #删除已买股数并扣除相应统计
		SELECT MIN(ShareId) INTO P_ShareId FROM Shares WHERE UserId = P_UserId AND IsOriginalStake = 1;
		SET P_ShareId = IFNULL(P_ShareId,0);
		WHILE (P_ShareId > 0 ) DO
			SELECT Price,Shares,cdate FROM Shares WHERE ShareId = P_ShareId INTO P_SharePrice,P_Shares,P_ShareDate;
			SET P_Shares = IFNULL(P_Shares,0);
			
			SELECT Id INTO P_PriceId FROM Shares_Price WHERE Price = P_SharePrice AND cdate < P_ShareDate ORDER BY Id DESC LIMIT 0,1;
			SET P_PriceId = IFNULL(P_PriceId,0);
			
			UPDATE Shares_Price SET SuccesShares = SuccesShares - P_Shares WHERE Id  = P_PriceId;
			DELETE FROM Shares WHERE ShareId = P_ShareId;
			
			SELECT MIN(ShareId) INTO P_ShareId FROM Shares WHERE UserId = P_UserId AND ShareId > P_ShareId AND IsOriginalStake = 1;
			SET P_ShareId = IFNULL(P_ShareId,0);
		END WHILE;

	END;

	BEGIN 
		DELETE FROM Shares_Buy WHERE UserId = P_UserId;
		DELETE FROM Shares_Sell WHERE UserId = P_UserId;
		DELETE FROM Users_Cash_Buy WHERE UserId = P_UserId;
		DELETE FROM Users_Cash_Sell WHERE UserId = P_UserId;
		DELETE FROM Users_Profile WHERE UserId = P_UserId;
		DELETE FROM Users_ZCJF WHERE UserId = P_UserId;
		DELETE FROM Users_Asset WHERE UserId = P_UserId;
		DELETE FROM Users_Accounts WHERE UserId = P_UserId OR clientUserId = P_UserId;
		DELETE FROM login_log WHERE user_id = P_UserId;
		DELETE FROM performance WHERE user_id = P_UserId;
		DELETE FROM Orders WHERE PayUserId = P_UserId;
		DELETE FROM Users WHERE UserId = P_UserId;
		DELETE FROM Net_Users WHERE UserId = P_UserId;
	END;

END
;;
delimiter ;


-- ----------------------------
-- Procedure structure for P_GuidanceSell
-- ----------------------------
DROP PROCEDURE IF EXISTS `P_GuidanceSell`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `P_GuidanceSell`(IN `P_BeginDate` datetime,IN `P_EndDate` datetime,IN `P_ProductId` int,IN `P_MaxShares` int)
BEGIN
	DECLARE P_UserId BIGINT;
	DECLARE P_SurplusShares BIGINT; #结余股数
	DECLARE P_UserName VARCHAR(100);
	DECLARE P_CurPrice decimal(10,3);     #当前价格
	DECLARE P_ThisDeductProfit decimal(10,3);   #本次指导销售扣除收益

	DECLARE P_Now TIMESTAMP DEFAULT UTC_TIMESTAMP();  #获取当前UTC时间

	SELECT Price INTO P_CurPrice FROM Shares_Price ORDER BY Id DESC LIMIT 0,1;
	
	SELECT MIN(UserId) INTO P_UserId FROM Net_Users WHERE IsOut = 0 AND ProductId = P_ProductId 
		AND cdate >= date_format(P_BeginDate,'%Y-%m-%d 00:00:01')	AND cdate <= date_format(P_EndDate,'%Y-%m-%d 23:59:59');
	SET P_UserId = IFNULL(P_UserId,0);

	
	
	WHILE (P_UserId > 0 AND P_UserId IS NOT NULL) DO
			#SELECT P_UserId;
			SET P_ThisDeductProfit = 0;
			SET P_UserId = IFNULL(P_UserId,0);


			SELECT UserName INTO P_UserName FROM Users WHERE UserId = P_UserId;
			SELECT SUM(Shares - OutShares) INTO P_SurplusShares FROM Shares WHERE UserId = P_UserId;
			
		IF ( P_SurplusShares > P_MaxShares ) THEN
			INSERT INTO Logs (LogType,UserType,UserName,Remark,cdate) VALUES (17,1,P_UserName,CONCAT('指导销售:扣除溢出权证',(P_SurplusShares - P_MaxShares)),P_Now);

			SET P_ThisDeductProfit = (P_SurplusShares - P_MaxShares) * P_CurPrice;
			SET P_ThisDeductProfit = IFNULL(P_ThisDeductProfit,0);
			UPDATE Net_Users SET DeductProfit = DeductProfit + P_ThisDeductProfit WHERE UserId = P_UserId;

			UPDATE Shares SET OutShares = Shares,Remark = CONCAT(Remark,'|',P_Now,' 指导销售')  WHERE UserId = P_UserId AND (Shares - OutShares)> 0;

			INSERT INTO  Shares(BuyId,UserId,NetUserId,ShareType,IsOriginalStake,Price,Shares,SplitCount,LastOutDate,Remark,cdate) 
				SELECT 0 AS BuyId, UserId,NetUserId,ShareType, 0 AS IsOriginalStake,Price,P_MaxShares AS Shares,SplitCount,P_Now AS LastOutDate,CONCAT('指导销售',P_MaxShares),P_Now AS cdate 
				FROM Shares WHERE UserId = P_UserId ORDER BY ShareId DESC LIMIT 0,1;
			
		END IF;

		SELECT MIN(UserId) INTO P_UserId FROM Net_Users WHERE UserId > P_UserId AND IsOut = 0 AND ProductId = P_ProductId 
			AND cdate >= date_format(P_BeginDate,'%Y-%m-%d 00:00:01')	AND cdate <= date_format(P_EndDate,'%Y-%m-%d 23:59:59');
		SET P_UserId = IFNULL(P_UserId,0);
	END WHILE;
	

END
;;
delimiter ;


-- ----------------------------
-- Procedure structure for P_SplitShares
-- ----------------------------
DROP PROCEDURE IF EXISTS `P_SplitShares`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `P_SplitShares`()
PL:BEGIN
		
		DECLARE P_Sys_InitialPrice decimal(10,3);  #初始价格
		DECLARE P_Sys_SplitPrice decimal(10,3);    #拆分价格
		DECLARE P_Sys_UnitShares BIGINT;    #权证书

		DECLARE P_CurPrice  decimal(10,3);  #当前价格
		


		DECLARE P_UserId BIGINT;
		DECLARE P_NetUserId BIGINT;
		DECLARE P_ProductId INT;
		DECLARE P_MaxShares  decimal(10,3);  #用户最大可持有股数
		DECLARE P_UserCurTotalShares INT; #用户当前总持有股数
		DECLARE P_UserFairishShares INT DEFAULT 0;  #用户还可以获得股数
		DECLARE P_ThisFairishShares INT DEFAULT 0;  #用户拆分可得股数
		DECLARE P_UserThisTotalShares INT DEFAULT 0;  #本次价格差收益


		DECLARE P_SplitCount INT DEFAULT 0;  #拆分次数
		DECLARE P_SurplusShares INT;  #结余股数

		DECLARE P_SplitUsersCount INT DEFAULT 0; #拆分用户数
		DECLARE P_Split_S_LargessShares INT DEFAULT 0; #拆分赠送总S股数
		DECLARE P_Split_D_LargessShares INT DEFAULT 0; #拆分赠送总D股数

		DECLARE P_Now TIMESTAMP DEFAULT UTC_TIMESTAMP();  #获取当前UTC时间

		IF(P_Now = CURRENT_TIMESTAMP()) THEN
			SET P_Now = CURRENT_TIMESTAMP();
		END IF;


		SELECT InitialPrice,SplitPrice,UnitShares  FROM Shares_Config LIMIT 0,1 INTO P_Sys_InitialPrice,P_Sys_SplitPrice,P_Sys_UnitShares;

		SELECT Price FROM Shares_Price ORDER BY Id DESC LIMIT 0,1 INTO P_CurPrice;


		IF(P_Sys_InitialPrice >= P_Sys_SplitPrice) THEN
			LEAVE PL;  #跳出执行存储过程
		END IF;
		

		#SELECT MIN(NetUserId) INTO P_NetUserId FROM Net_Users WHERE IsOut = 0 AND (SELECT  COUNT(*) FROM Shares WHERE (Shares-OutShares) > 0 AND NetUserId = Net_Users.NetUserId) > 0;
		SELECT MIN(NetUserId) INTO P_NetUserId FROM Net_Users WHERE IsOut = 0;
		SET P_NetUserId = IFNULL(P_NetUserId,0);

		#SELECT P_CurPrice,P_Sys_SplitPrice,P_NetUserId;

		WHILE (P_NetUserId > 0) DO

			
			SELECT UserId,ProductId FROM Net_Users WHERE NetUserId = P_NetUserId INTO P_UserId,P_ProductId ;

			SELECT KCGS INTO P_MaxShares FROM Products WHERE ProductId = P_ProductId;  #最大可持有股数 （包含拆分后）

			SELECT  SUM(Shares-OutShares) INTO P_UserCurTotalShares FROM Shares WHERE NetUserId = P_NetUserId; #当前总持有股数
			SET P_UserCurTotalShares = IFNULL(P_UserCurTotalShares,0);

			SET P_UserFairishShares = 0;

			#IF (P_UserId = 344) THEN
			#	SELECT P_UserCurTotalShares AS UserCurTotalShares,P_MaxShares AS MaxShares ,P_UserFairishShares AS UserFairishShares ;
			#END IF;
			
			IF(P_UserCurTotalShares >= P_MaxShares) THEN   #现持有股数 超出最大持股数 抹平
					UPDATE Shares SET OutShares = Shares WHERE ShareType = 1 AND NetUserId = P_NetUserId;

					INSERT INTO  Shares(UserId,NetUserId,ShareType,IsOriginalStake,Price,Shares,SplitCount,LastOutDate,cdate) 
					SELECT UserId,NetUserId,ShareType, 1 AS IsOriginalStake,Price,P_MaxShares AS Shares,SplitCount,P_Now AS LastOutDate,P_Now AS cdate 
					FROM Shares WHERE ShareType = 1 AND NetUserId = P_NetUserId ORDER BY ShareId DESC LIMIT 0,1;
			ELSE
					SET P_UserFairishShares =  P_MaxShares - P_UserCurTotalShares;
			END IF;

			#IF (P_UserId = 344) THEN 
			#	SELECT P_UserFairishShares AS UserFairishShares1;
			#END IF;

			SET P_UserThisTotalShares = P_UserCurTotalShares * (P_CurPrice - P_Sys_InitialPrice) / P_Sys_InitialPrice;
			SET P_UserThisTotalShares = IFNULL(P_UserThisTotalShares,0);

			IF(P_UserFairishShares >= P_UserThisTotalShares) THEN
					SET P_UserFairishShares = P_UserThisTotalShares;
			END IF;

			IF(P_UserThisTotalShares >= P_UserFairishShares) THEN   #拆分差大于可得收益时使用可得收益
				SET P_ThisFairishShares = P_UserFairishShares;  
			ELSE
				SET P_ThisFairishShares = P_UserFairishShares * (P_CurPrice - P_Sys_InitialPrice) / P_Sys_InitialPrice;  #本次拆分可得股数
			END IF;

			IF (P_ThisFairishShares > 0) THEN  #本次拆分可得股数
				SET P_SplitUsersCount = P_SplitUsersCount + 1;  #如果可获得股数大于0 则算一个用户
			
				SET P_Split_S_LargessShares = P_Split_S_LargessShares + P_ThisFairishShares;

				INSERT INTO  Shares(UserId,NetUserId,ShareType,IsOriginalStake,Price,Shares,SplitCount,Remark,LastOutDate,cdate) 
				SELECT UserId,NetUserId,ShareType, 0 AS IsOriginalStake,Price,P_ThisFairishShares AS Shares,SplitCount,CONCAT((SplitCount + 1), '次拆分时配送权证') AS Remark,P_Now AS LastOutDate,P_Now AS cdate 
				FROM Shares WHERE  NetUserId = P_NetUserId ORDER BY ShareId DESC LIMIT 0,1;
			END IF;			
			
			UPDATE Shares SET SplitCount= SplitCount + 1 ,LastOutDate = P_Now WHERE NetUserId = P_NetUserId; #更新用户拆分次数
			
			#SELECT MIN(NetUserId) INTO P_NetUserId FROM Net_Users WHERE NetUserId > P_NetUserId AND IsOut = 0 AND (SELECT  COUNT(*) FROM Shares WHERE (Shares-OutShares) > 0 AND NetUserId = Net_Users.NetUserId) > 0;
			SELECT MIN(NetUserId) INTO P_NetUserId FROM Net_Users WHERE NetUserId > P_NetUserId AND IsOut = 0;
			SET P_NetUserId = IFNULL(P_NetUserId,0);
 
		END WHILE;

		INSERT INTO Shares_Price (Price,Shares,cdate) VALUES (P_Sys_InitialPrice,P_Sys_UnitShares,P_Now);

		INSERT INTO Shares_Split (SplitPrice,afterSplitPrice,UserCount,Largess_S_Shares,Largess_D_Shares,cdate) 
			VALUES (P_CurPrice,P_Sys_InitialPrice,P_SplitUsersCount,P_Split_S_LargessShares,P_Split_D_LargessShares,P_Now);


END
;;
delimiter ;


SET FOREIGN_KEY_CHECKS = 1;

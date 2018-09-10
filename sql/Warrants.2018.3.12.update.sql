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

 Date: 12/03/2018 22:25:15
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
	DECLARE P_RecommendUserId BIGINT;
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
	
	DECLARE P_ParentUserName varchar(50);   #安置人账号
	DECLARE P_RecommendUserName varchar(50); #推荐人账号
	DECLARE P_CountryId INT; 
	DECLARE P_CountryName varchar(50);     #国家
	DECLARE P_PlaceAreaName varchar(50);     #安置区域
	DECLARE P_Now TIMESTAMP DEFAULT UTC_TIMESTAMP();  #获取当前UTC时间


	IF((SELECT COUNT(*) FROM Users WHERE UserId = P_UserId) = 0) THEN
		LEAVE PL;  
	END IF;
	
	IF((SELECT COUNT(*) FROM Users WHERE ParentUserId = P_UserId) > 0) THEN
		LEAVE PL;  
	END IF;

	IF((SELECT COUNT(*) FROM Users WHERE RecommendUserId = P_UserId) > 0) THEN
		LEAVE PL ;  
	END IF;

	SELECT FromUserId INTO P_PayUserId FROM Users_Bonus WHERE UserId = P_UserId AND InOrOut = 1 ORDER BY Id DESC LIMIT 0,1;
	SET P_PayUserId = IFNULL(P_PayUserId,0);
	IF(P_PayUserId IS NULL OR P_PayUserId = 0) THEN
		LEAVE PL ;  
	END IF;
	
	SELECT UserName,ParentUserId,RecommendUserId,RegCountry,PlaceArea,RegDate FROM Users WHERE UserId = P_UserId INTO P_UserName,P_ParentUserId,P_RecommendUserId,P_CountryId,P_PlaceArea,P_RegDate;
	SET P_ParentUserId = IFNULL(P_ParentUserId,0);
	
	SELECT UserName INTO P_ParentUserName FROM Users WHERE  UserId = P_ParentUserId;
	SELECT UserName INTO P_RecommendUserName FROM Users WHERE  UserId = P_RecommendUserId;
	SELECT CountryName INTO P_CountryName FROM Country WHERE  CountryId = P_CountryId;
	SET P_ParentUserName = IFNULL(P_ParentUserName,'无');
	SET P_RecommendUserName = IFNULL(P_RecommendUserName,'无');
	SET P_CountryName = IFNULL(P_CountryName,'无');
	
	IF(P_PlaceArea = 0) THEN
		SET P_PlaceAreaName = '左区';
	ELSE
		SET P_PlaceAreaName = '右区';
	END IF;


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
				IF( (SELECT IFNULL(LeftTurnoverSurplus,0) FROM Users_Asset WHERE UserId = P_ParentUserId  LIMIT 0,1) < 0 ) THEN
					UPDATE Users_Asset SET LeftTurnoverSurplus = 0 WHERE UserId = P_ParentUserId;
				END IF;
				IF((SELECT COUNT(*) FROM Users_Bonus WHERE UserId = P_ParentUserId AND FromUserId = P_UserId AND BonusType = 2  LIMIT 0,1) > 0) THEN
					UPDATE Users_Asset SET RightTurnoverSurplus = RightTurnoverSurplus + P_Price WHERE UserId = P_ParentUserId;
				END IF;
			ELSE

				UPDATE Users_Asset SET TeamNum = TeamNum - 1,TeamTurnover = TeamTurnover - P_Price,RightTotalTurnover = RightTotalTurnover - P_Price,RightTurnoverSurplus = RightTurnoverSurplus - P_Price WHERE UserId = P_ParentUserId;

				IF( (SELECT IFNULL(RightTurnoverSurplus,0) FROM Users_Asset WHERE UserId = P_ParentUserId  LIMIT 0,1) < 0 ) THEN
					UPDATE Users_Asset SET RightTurnoverSurplus = 0 WHERE UserId = P_ParentUserId;
				END IF;

				IF((SELECT COUNT(*) FROM Users_Bonus WHERE UserId = P_ParentUserId AND FromUserId = P_UserId AND BonusType = 2  LIMIT 0,1) > 0) THEN
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

	INSERT INTO Logs (LogType,UserType,UserName,Remark,cdate) VALUES (8,2,'DBManager',CONCAT('删除会员：',P_UserName ,'|UserId：',P_UserId,'|推荐人：',P_RecommendUserName,'|安置人：',P_ParentUserName,'|区域：',P_PlaceAreaName,'|国家：',P_CountryName,'|注册时间',P_RegDate),P_Now);

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
	DECLARE P_ProductName VARCHAR(100);

	DECLARE P_Now TIMESTAMP DEFAULT UTC_TIMESTAMP();  #获取当前UTC时间

	SELECT Price INTO P_CurPrice FROM Shares_Price ORDER BY Id DESC LIMIT 0,1;
	
	
	SELECT name INTO P_ProductName FROM Products WHERE ProductId = P_ProductId ORDER BY ProductId DESC LIMIT 0,1;
	SET P_ProductName = IFNULL(P_ProductName,'无');
	
	SELECT MIN(UserId) INTO P_UserId FROM Net_Users WHERE IsOut = 0 AND ProductId = P_ProductId 
		AND cdate >= date_format(P_BeginDate,'%Y-%m-%d 00:00:01')	AND cdate <= date_format(P_EndDate,'%Y-%m-%d 23:59:59');
	SET P_UserId = IFNULL(P_UserId,0);

	SET P_MaxShares = IFNULL(P_MaxShares,0);
	
	
	WHILE (P_UserId > 0 AND P_UserId IS NOT NULL) DO
			#SELECT P_UserId;
			SET P_ThisDeductProfit = 0;
			SET P_UserId = IFNULL(P_UserId,0);


			SELECT UserName INTO P_UserName FROM Users WHERE UserId = P_UserId ORDER BY UserId DESC LIMIT 0,1;
			SET P_UserName = IFNULL(P_UserName,'无');
			
			SELECT SUM(Shares - OutShares) INTO P_SurplusShares FROM Shares WHERE UserId = P_UserId;
			SET P_SurplusShares = IFNULL(P_SurplusShares,0);
			
		IF ( P_SurplusShares > P_MaxShares ) THEN
			INSERT INTO Logs (LogType,UserType,UserName,Remark,cdate) VALUES (13,1,'DB指导销售',CONCAT('指导销售会员:',P_UserName,'|等级：',P_ProductName,'|持有权证:', P_SurplusShares,'|指导值:', P_MaxShares,'|扣除权证：',(P_SurplusShares - P_MaxShares)),P_Now);

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
-- Procedure structure for P_Shares_Buy
-- ----------------------------
DROP PROCEDURE IF EXISTS `P_Shares_Buy`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `P_Shares_Buy`(IN P_BuyId bigint)
PL:BEGIN
	
		DECLARE P_Sys_InitialPrice decimal(10,3);  #初始价格
		DECLARE P_Sys_SplitPrice decimal(10,3);    #拆分价格
		DECLARE P_Sys_UnitPrice decimal(10,3);     #单位价格涨幅0.001
		DECLARE P_Sys_UnitShares BIGINT;    #单位价格股数
		DECLARE P_Sys_JTSXFBL decimal(10,3);    #交易手续费
		DECLARE P_Sys_JTTXBL decimal(10,3);    #静态提现比例
		DECLARE P_Sys_JTFTBL decimal(10,3);    #单位价格股数
		DECLARE P_Sys_JTSCJFBL decimal(10,3);    #单位价格股数

		DECLARE P_PriceId INT;     #价格ID
		DECLARE P_CurPrice decimal(10,3);     #当前价格
		DECLARE P_CurPriceDate DATETIME ;     #当前价格
		DECLARE P_CurPriceSaledShares INT;         #当前价格已售股数股数
		DECLARE P_CurPriceMaxShares INT;         #当前价格最大可售股数

		DECLARE P_UserId BIGINT;  #买入用户Id
		DECLARE P_UserName varchar(50); 
		DECLARE P_NetUserId BIGINT;  #买入用户网体Id
		DECLARE P_FundType INT;  #买入资金类型
		DECLARE P_BuyAmount decimal(10,3);     #挂买金额
		DECLARE P_SuccesBuyAmount decimal(10,3);     #已买金额
		DECLARE P_CanBuyShares INT;     #可买股数
		DECLARE P_ShareType TINYINT DEFAULT 2;  #股权类型 1多次拆分2单次拆分

		DECLARE P_UserBuyShares INT DEFAULT 0;     #用户成功购买股数

		DECLARE P_SellId BIGINT;   #挂卖交易ID
		DECLARE P_SellUserId BIGINT; #挂卖用户ID
		DECLARE P_SellNetUserId BIGINT;# 挂卖用户网体ID
		DECLARE P_SellShareType TINYINT ; #挂卖股票类型
		DECLARE P_SellProductId BIGINT;#挂卖用户产品ID
		DECLARE P_ProductCJBS INT; #挂卖用户购买产品出局倍数
		DECLARE P_ProductMaxProfit decimal(10,3); #产品总收益
		DECLARE P_SellUserProfit decimal(10,3); #挂卖用户总收益
		DECLARE P_CanSellShares BIGINT;    #挂卖交易记录可卖股数
		DECLARE P_SellIsOut INT DEFAULT 0; #挂卖是否已出局
		DECLARE P_ThisTradeShares BIGINT;   #本次可交易股数
		DECLARE P_ThisTradeSharesProfit decimal(10,3);   #本次可交易股数收益
		DECLARE P_ThisTradeJYF decimal(10,3);   #本次可交易费
		DECLARE P_ThisTradeBonus decimal(10,3);   #本次可交易收益
		DECLARE P_ThisTradeJTFTBonus decimal(10,3);   #本次可交易静态复投
		DECLARE P_ThisTradeJTSCBonus decimal(10,3);   #本次可交易静态商城


		DECLARE P_SplitUserCount BIGINT DEFAULT 0;  #拆分用户数
		DECLARE P_LargessShares BIGINT DEFAULT 0;  #赠送股数

		DECLARE P_Now TIMESTAMP DEFAULT UTC_TIMESTAMP();  #获取当前UTC时间

		SELECT InitialPrice,SplitPrice,UnitPrice,UnitShares,JTSXFBL,JTTXBL,JTFTBL,JTSCJFBL  FROM Shares_Config LIMIT 0,1  
		INTO P_Sys_InitialPrice,P_Sys_SplitPrice,P_Sys_UnitPrice,P_Sys_UnitShares,P_Sys_JTSXFBL,P_Sys_JTTXBL,P_Sys_JTFTBL,P_Sys_JTSCJFBL;

		
		#不存在股权价格记录则新增初始价格
		IF((SELECT COUNT(*) FROM Shares_Price) = 0) THEN
			INSERT INTO Shares_Price (Price,Shares,cdate) VALUES (P_Sys_InitialPrice,P_Sys_UnitShares,P_Now);
		END IF;

		#验证是否可以拆分
		#IF((SELECT Price FROM Shares_Price ORDER BY Id DESC  LIMIT 0,1 ) >= P_Sys_SplitPrice) THEN
		#	CALL P_SplitShares;
		#LEAVE PL ;  #跳出执行存储过程
		#END IF;


		SELECT Id,Price,Shares,SuccesShares,cdate FROM Shares_Price ORDER BY Id DESC LIMIT 0,1 INTO P_PriceId,P_CurPrice,P_CurPriceMaxShares,P_CurPriceSaledShares,P_CurPriceDate;

		SELECT UserId,NetUserId,FundType,Price,SuccesPrice FROM Shares_Buy WHERE BuyId = P_BuyId INTO P_UserId,P_NetUserId,P_FundType, P_BuyAmount,P_SuccesBuyAmount;

		SELECT UserName INTO P_UserName FROM Users WHERE UserId = P_UserId ORDER BY UserId DESC LIMIT 0,1;
		SET P_UserName = IFNULL(P_UserName,'无');

		SET P_ShareType = 1;

		IF (P_BuyAmount <= P_SuccesBuyAmount) THEN
			UPDATE Shares_Buy SET Status = 0 WHERE BuyId = P_BuyId;
			LEAVE PL ;  #跳出执行存储过程
		END IF;

		IF ((P_BuyAmount - P_SuccesBuyAmount) < P_CurPrice) THEN
			CALL P_Refund_Buy(P_BuyId);
			LEAVE PL ;  #跳出执行存储过程
		END IF;

		SET P_CanBuyShares =  FLOOR((P_BuyAmount - P_SuccesBuyAmount) / P_CurPrice);  #用户结余金额 可买股数

		IF(P_CanBuyShares > 0) THEN
			
			SET P_ThisTradeShares = P_CanBuyShares;
			SET P_CanBuyShares = 0;
			
			INSERT INTO Shares (BuyId,UserId,NetUserId,ShareType,IsOriginalStake,Price,Shares,LastSalePrice,LastOutDate,cdate) VALUES (P_BuyId,P_UserId,P_NetUserId,P_ShareType,1,P_CurPrice,P_ThisTradeShares,P_CurPrice,P_Now,P_Now);
			INSERT INTO Shares_Sell_Buy_Cross (BuyId,SellId,Shares,Price,Amount,cdate) VALUES (P_BuyId,0,P_ThisTradeShares,P_CurPrice,(P_ThisTradeShares * P_CurPrice),P_Now);
			UPDATE Shares_Price SET SuccesShares = SuccesShares + P_ThisTradeShares  WHERE Id = P_PriceId;
			UPDATE Shares_Buy SET SuccesShares = SuccesShares + P_ThisTradeShares , SuccesPrice = SuccesPrice + (P_ThisTradeShares * P_CurPrice) WHERE BuyId = P_BuyId;
			
			INSERT INTO Logs (LogType,UserType,UserName,Remark,cdate) VALUES (16,2,'DB自动买入',CONCAT('会员：',P_UserName ,'|UserId：',P_UserId,'|买入：',P_ThisTradeShares,'|价格：',P_CurPrice,'|成交金额：',(P_ThisTradeShares * P_CurPrice)),P_Now);

			#更新每日统计
			UPDATE Shares_Statistics 
			SET BuyShares = BuyShares + P_ThisTradeShares,SysSellShares = SysSellShares + P_ThisTradeShares
			, BuyAmount = BuyAmount + (P_ThisTradeShares * P_CurPrice),SysSellAmount = SysSellAmount + (P_ThisTradeShares * P_CurPrice)
			WHERE to_days(cdate) = to_days(P_Now);

			IF (P_CanBuyShares = 0) THEN
				CALL P_Refund_Buy(P_BuyId);
				LEAVE PL ;  #跳出执行存储过程
			END IF;

		END IF;



END
;;
delimiter ;


SET FOREIGN_KEY_CHECKS = 1;

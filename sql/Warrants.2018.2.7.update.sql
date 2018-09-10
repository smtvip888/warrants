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

 Date: 07/02/2018 12:54:32
*/

SET FOREIGN_KEY_CHECKS = 0;



alter table Shares_Config modify EnabledTrade BIGINT(18) default 0 COMMENT 'SubId起始值';

-- ----------------------------
-- Procedure structure for P_UpdateTeamCount
-- ----------------------------
DROP PROCEDURE IF EXISTS `P_UpdateTeamCount`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `P_UpdateTeamCount`(IN P_UserId bigint)
BEGIN

	DECLARE P_Sys_DTFTBL decimal(10,3);    #动态复投比例
	DECLARE P_Sys_DTTXBL decimal(10,3);    #动态电子积分比例
	DECLARE P_Sys_DTLYJFBL decimal(10,3);  #动态旅游积分比例
	DECLARE P_Sys_JDJBL decimal(10,3);  #见点奖比例
	DECLARE P_Sys_DPJZFDBS decimal(10,3);  #对碰奖周封顶倍数
	DECLARE P_Sys_SubId BIGINT;  #SubId 起始值
	
	DECLARE P_UserName varchar(20);   #会员编号
	DECLARE P_ParentUserId BIGINT;
	DECLARE P_Invested decimal(10,3);    #投资额
	DECLARE P_PlaceArea INT;

	DECLARE P_LeftTurnoverSurplus decimal(20,3);  #左区结余
	DECLARE P_RightTurnoverSurplus decimal(20,3);  #右区结余

	DECLARE P_UserBonus decimal(10,3) DEFAULT 0;  #用户可得总积分
	DECLARE P_UserCashBonus decimal(10,3) DEFAULT 0;  #用户可提现积分
	DECLARE P_UserFTBonus decimal(10,3) DEFAULT 0;  #用户复投积分
	DECLARE P_UserLYBonus decimal(10,3) DEFAULT 0;  #用户旅游积分
	
	DECLARE	P_ReIsOut INT;  #是否出局
	DECLARE P_ReProductId INT;  #推荐人购买的理财计划
	DECLARE P_ReInvested decimal(10,3);    #上级投资额
	DECLARE P_ReTodayProfit decimal(10,3);    #上级今日收益
	DECLARE P_ReWeekProfit decimal(10,3);    #上级本周收益
	DECLARE P_ReWeekMaxProfit decimal(10,3);    #上级周最大收益


	DECLARE P_DPJBL decimal(10,3);   #对碰奖比例
	DECLARE P_JDJCS INT DEFAULT 0;   #见点奖层数

	DECLARE P_LoopCount INT DEFAULT 0;   #循环次数
	DECLARE P_SubId BIGINT;  


 	DECLARE P_Now TIMESTAMP DEFAULT UTC_TIMESTAMP();  #获取当前UTC时间

	SELECT DTFTBL,DTTXBL,DTLYJFBL,JDJBL,DPJZFDBS,EnabledTrade  FROM Shares_Config LIMIT 0,1  INTO P_Sys_DTFTBL,P_Sys_DTTXBL,P_Sys_DTLYJFBL,P_Sys_JDJBL,P_Sys_DPJZFDBS,P_Sys_SubId;

	SELECT Price INTO P_Invested  FROM Net_Users WHERE UserId = P_UserId  ORDER BY NetUserId DESC LIMIT 0,1 ;

	SELECT UserName,ParentUserId,PlaceArea FROM Users WHERE UserId = P_UserId INTO P_UserName,P_ParentUserId,P_PlaceArea;

	SELECT MAX(SubId) INTO P_SubId FROM Users;
	SET P_SubId =  IFNULL(P_SubId,0);
	IF( P_Sys_SubId > P_SubId) THEN
		UPDATE Users SET SubId = P_Sys_SubId WHERE UserId = P_UserId;  #更新SubId
	ELSE
		UPDATE Users SET SubId = P_SubId + 1 WHERE UserId = P_UserId;  #更新SubId
	END IF;
	

	WHILE (P_ParentUserId > 0 AND P_ParentUserId IS NOT NULL)  DO
		SET P_LoopCount = P_LoopCount + 1;
		
		#SELECT  P_PlaceArea;
		#更新团队人数及业绩
		IF (P_PlaceArea = 0) THEN
			UPDATE Users_Asset SET TeamNum = TeamNum + 1,TeamTurnover = TeamTurnover + P_Invested,LeftTotalTurnover = LeftTotalTurnover + P_Invested,LeftTurnoverSurplus = LeftTurnoverSurplus + P_Invested WHERE UserId = P_ParentUserId;
		ELSE
			UPDATE Users_Asset SET TeamNum = TeamNum + 1,TeamTurnover = TeamTurnover + P_Invested,RightTotalTurnover = RightTotalTurnover + P_Invested,RightTurnoverSurplus = RightTurnoverSurplus + P_Invested WHERE UserId = P_ParentUserId;
		END IF;


		#计算用户对碰奖金
		SELECT LeftTurnoverSurplus,RightTurnoverSurplus FROM Users_Asset WHERE UserId = P_ParentUserId INTO P_LeftTurnoverSurplus,P_RightTurnoverSurplus;

		SELECT ProductId,Price,IsOut FROM Net_Users WHERE UserId = P_ParentUserId  ORDER BY NetUserId DESC LIMIT 0,1  INTO P_ReProductId,P_ReInvested,P_ReIsOut;
		SET P_ReIsOut = IFNULL(P_ReIsOut,1);

		SET P_ReWeekMaxProfit = P_ReInvested * P_Sys_DPJZFDBS;  #本周可得最大收益

		SELECT SUM(Amount)  INTO P_ReTodayProfit FROM Users_Bonus WHERE UserId = P_ParentUserId AND TradeType = 1 AND BonusType = 2 AND InOrOut = 1 AND date_format(cdate,'%Y-%m-%d') = date_format(P_Now,'%Y-%m-%d');
		SET P_ReTodayProfit = IFNULL(P_ReTodayProfit,0);

		SELECT SUM(Amount)  INTO P_ReWeekProfit FROM Users_Bonus WHERE UserId = P_ParentUserId AND TradeType = 1 AND BonusType = 2 AND InOrOut = 1 AND YEARWEEK(date_format(cdate,'%Y-%m-%d')) = YEARWEEK(date_format(P_Now,'%Y-%m-%d'));
		SET P_ReWeekProfit = IFNULL(P_ReWeekProfit,0);   #本周已得收益

		SELECT DPJ,JDJCS FROM Products WHERE ProductId = P_ReProductId  INTO P_DPJBL,P_JDJCS;

		SET P_LeftTurnoverSurplus = IFNULL(P_LeftTurnoverSurplus,0);
		SET P_RightTurnoverSurplus = IFNULL(P_RightTurnoverSurplus,0);

		IF(P_LeftTurnoverSurplus > P_RightTurnoverSurplus) THEN
			SET P_UserBonus = P_RightTurnoverSurplus;
		ELSE
			SET P_UserBonus = P_LeftTurnoverSurplus;
		END IF;

		SET P_UserBonus = P_UserBonus * (P_DPJBL/100);

		IF P_ReTodayProfit  >= P_ReInvested THEN
			SET P_UserBonus = 0;
    ELSE
			IF (P_ReTodayProfit + P_UserBonus) > P_ReInvested THEN
					SET P_UserBonus = P_ReInvested - P_ReTodayProfit;
			END IF;
		END IF;


		IF P_ReWeekProfit  >= P_ReWeekMaxProfit THEN   #计算周收益是否封顶
			SET P_UserBonus = 0;
    ELSE
			IF (P_ReWeekProfit + P_UserBonus) > P_ReWeekMaxProfit THEN
					SET P_UserBonus = P_ReWeekMaxProfit - P_ReWeekProfit;
			END IF;
		END IF;


		SELECT P_UserBonus,P_ReTodayProfit, P_LeftTurnoverSurplus,P_RightTurnoverSurplus,P_ParentUserId,P_PlaceArea;


		IF(P_UserBonus > 0) THEN
			SET P_UserCashBonus = P_UserBonus * (P_Sys_DTTXBL/100);
			SET P_UserFTBonus = P_UserBonus * (P_Sys_DTFTBL/100);
			SET P_UserLYBonus = P_UserBonus * (P_Sys_DTLYJFBL/100);

			INSERT INTO Users_Bonus(UserId,FromUserId,TradeType,BonusType,InOrOut,Amount,CashBonus,DTFTBonus,LYJFBonus,Remark,cdate) 
			VALUES (P_ParentUserId,P_UserId,1,2,1,P_UserBonus,P_UserCashBonus,P_UserFTBonus,P_UserLYBonus,CONCAT('激活会员:',P_UserName,'获得平衡奖'),P_Now);
					
			#更新每日统计
			UPDATE Shares_Statistics SET DTBonus = DTBonus + P_UserCashBonus,DTFT = DTFT + P_UserFTBonus,DTLY = DTLY + P_UserLYBonus WHERE to_days(cdate) = to_days(P_Now);

			UPDATE Users_Asset 
			SET TotalBonus = TotalBonus + P_UserCashBonus ,BonusSurplus = BonusSurplus + P_UserCashBonus
			, DTFTJJ = DTFTJJ + P_UserFTBonus , DTFTJJYE = DTFTJJYE + P_UserFTBonus, LYJF = LYJF + P_UserLYBonus , LYJFYE = LYJFYE + P_UserLYBonus
			WHERE UserId = P_ParentUserId;

		END IF;

		#更新计算后的小区业绩
		IF(P_LeftTurnoverSurplus > P_RightTurnoverSurplus) THEN
			UPDATE Users_Asset SET LeftTurnoverSurplus = LeftTurnoverSurplus - P_RightTurnoverSurplus,RightTurnoverSurplus = 0 WHERE UserId = P_ParentUserId;
		ELSE
			UPDATE Users_Asset SET RightTurnoverSurplus = P_RightTurnoverSurplus - LeftTurnoverSurplus,LeftTurnoverSurplus = 0 WHERE UserId = P_ParentUserId;
		END IF;


		
		#计算用户见点奖
		SET P_UserBonus = P_Invested * (P_Sys_JDJBL / 100);
		#SELECT P_UserBonus,P_LoopCount;
		IF(P_UserBonus > 0 AND P_JDJCS >= P_LoopCount ) THEN
			SET P_UserCashBonus = P_UserBonus * (P_Sys_DTTXBL/100);
			SET P_UserFTBonus = P_UserBonus * (P_Sys_DTFTBL/100);
			SET P_UserLYBonus = P_UserBonus * (P_Sys_DTLYJFBL/100);

			INSERT INTO Users_Bonus(UserId,FromUserId,TradeType,BonusType,InOrOut,Amount,CashBonus,DTFTBonus,LYJFBonus,Remark,cdate) 
			VALUES (P_ParentUserId,P_UserId,1,4,1,P_UserBonus,P_UserCashBonus,P_UserFTBonus,P_UserLYBonus,CONCAT('激活会员:',P_UserName,'获得见点奖'),P_Now);
					
			#更新每日统计
			UPDATE Shares_Statistics SET DTBonus = DTBonus + P_UserCashBonus,DTFT = DTFT + P_UserFTBonus,DTLY = DTLY + P_UserLYBonus WHERE to_days(cdate) = to_days(P_Now);

			#更新账户余额
			UPDATE Users_Asset 
			SET TotalBonus = TotalBonus + P_UserCashBonus ,BonusSurplus = BonusSurplus + P_UserCashBonus , DTFTJJ = DTFTJJ + P_UserFTBonus , DTFTJJYE = DTFTJJYE + P_UserFTBonus, LYJF = LYJF + P_UserLYBonus , LYJFYE = LYJFYE + P_UserLYBonus
			WHERE UserId = P_ParentUserId;

		END IF;

		SET P_UserBonus = 0;
		SET P_UserCashBonus = 0;
		SET P_UserFTBonus = 0;
		SET P_UserLYBonus = 0;
		SET P_LeftTurnoverSurplus = 0;
		SET P_RightTurnoverSurplus = 0;
		SET P_ReProductId = 0;
		SET P_ReWeekProfit = 0;
		SET P_ReWeekMaxProfit = 0;
		
		SELECT ParentUserId,PlaceArea  FROM Users WHERE UserId = P_ParentUserId INTO P_ParentUserId,P_PlaceArea;
		
	END WHILE;



END
;;
delimiter ;


SET FOREIGN_KEY_CHECKS = 1;

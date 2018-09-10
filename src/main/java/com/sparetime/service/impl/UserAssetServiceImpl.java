package com.sparetime.service.impl;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.cons.TradeTypeEnum;
import com.sparetime.dao.mapper.FunctionMapper;
import com.sparetime.dao.mapper.NetUserMapper;
import com.sparetime.dao.mapper.UserAssetMapper;
import com.sparetime.domian.*;
import com.sparetime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UserAssetServiceImpl implements UserAssetService {

    @Autowired
    private UserAssetMapper userAssetMapper;

    @Autowired
    private UserZCJFService userZCJFService;

    @Autowired
    private NetUserService netUserService;

    @Autowired
    private UserBonusService userBonusService;

    @Autowired
    private SharesConfigService sharesConfigService;

    @Autowired
    private UserCashBonusGetService userCashBonusGetService;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @Override
    public UserAsset queryByUserId(BigDecimal userId) {
        return userAssetMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public void addZCJF(User user,BigDecimal fromUserId, String remark, BigDecimal amount) {

        UserAsset userAsset = userAssetMapper.selectByUserId(user.getUserId());
        if (userAsset == null)
            throw new RuntimeException("userAssets not exits by userID : " + user.getUserId());

        userAsset.setZCJF(userAsset.getZCJF().add(amount));
        userAsset.setZCJFYYE(userAsset.getZCJFYYE().add(amount));
        userAssetMapper.update(userAsset);

        NetUser netUser = netUserService.queryByUserId(user.getUserId());

        UserZCJF userZCJF = new UserZCJF();
        userZCJF.setUserId(user.getUserId());
        userZCJF.setAmount(amount);
        userZCJF.setFromUserId(fromUserId);
        userZCJF.setInOrOut(CashBonusInOrOutEnum.进账.getCode());
        userZCJF.setNetUserId(netUser == null ? null : netUser.getNetUserId());
        userZCJF.setRemark(remark);
        userZCJF.setIsdel(0);
        userZCJFService.insert(userZCJF);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(19);
        log.setUserType(2);
        log.setIp(ip);
        log.setUserName(manager.getName());
        log.setCdate(new Date());
        log.setRemark("注册积分充值：" + user.getUserName() + " + 充值金额：" + amount );
        logService.insert(log);
    }

    @Override
    @Transactional
    public void delZCJF(UserZCJF userZCJF) {
        UserAsset userAsset = userAssetMapper.selectByUserId(userZCJF.getUserId());
        if (userAsset == null)
            throw new RuntimeException("userAssets not exits by userID : " + userZCJF.getUserId());

        userAsset.setZCJF(userAsset.getZCJF().subtract(userZCJF.getAmount()));
        userAsset.setZCJFYYE(userAsset.getZCJFYYE().subtract(userZCJF.getAmount()));
        userAssetMapper.update(userAsset);

        userZCJFService.del(userZCJF.getId());
    }

    @Override
    public int update(UserAsset userAsset) {
        return userAssetMapper.update(userAsset);
    }

    @Override
    @Transactional
    public void transferZCJF(User out, User in, BigDecimal num, String ip) {

        UserAsset outAsset = userAssetMapper.selectByUserId(out.getUserId());
        if (outAsset.getZCJFYYE().compareTo(num) == -1)
            throw new RuntimeException("余额不足");

        if (num.compareTo(BigDecimal.ZERO) == -1)
            throw new RuntimeException("金额不能为负");

        String _outIds = functionMapper.callP_GetTransferUserIds(out.getUserId(), in.getUserId());
        List<BigDecimal> outIds = new ArrayList<>();
        if (!StringUtils.isEmpty(_outIds)){
            Arrays.asList( _outIds.split(",")).forEach(v -> {
                outIds.add(new BigDecimal(v));
            });
        }
        if (out.getIsService() == 1){
            if (!outIds.remove(out.getUserId())){
                throw new RuntimeException("转入用户不在可允许的用户范围内");
            }
        }else {
            if (!outIds.remove(in.getUserId())){
                throw new RuntimeException("转入用户不在可允许的用户范围内");
            }
        }


        UserAsset inAsset = userAssetMapper.selectByUserId(in.getUserId());

        outAsset.setZCJFYYE(outAsset.getZCJFYYE().subtract(num));
        inAsset.setZCJFYYE(inAsset.getZCJFYYE().add(num));

        userAssetMapper.update(outAsset);
        userAssetMapper.update(inAsset);

        UserZCJF outUserZCJF = new UserZCJF();
        outUserZCJF.setRemark("转出到" + in.getUserName());
        outUserZCJF.setInOrOut(CashBonusInOrOutEnum.支出.getCode());
        outUserZCJF.setFromUserId(in.getUserId());
        outUserZCJF.setAmount(num);
        outUserZCJF.setUserId(out.getUserId());
        outUserZCJF.setIsdel(0);
        outUserZCJF.setIp(ip);

        UserZCJF inUserZCJF = new UserZCJF();
        inUserZCJF.setUserId(in.getUserId());
        inUserZCJF.setAmount(num);
        inUserZCJF.setFromUserId(out.getUserId());
        inUserZCJF.setInOrOut(CashBonusInOrOutEnum.进账.getCode());
        inUserZCJF.setRemark("从" + out.getUserName() + "转入");
        inUserZCJF.setIsdel(0);
        inUserZCJF.setIp(ip);

        userZCJFService.insert(outUserZCJF);
        userZCJFService.insert(inUserZCJF);
    }

    @Override
    @Transactional
    public void convert(BigDecimal userId, BigDecimal surplus, String ip) {

        UserAsset userAsset = userAssetMapper.selectByUserId(userId);
        if (userAsset.getBonusSurplus().compareTo(surplus) == -1)
            throw new RuntimeException("余额不足");

        userAsset.setBonusSurplus(userAsset.getBonusSurplus().subtract(surplus));
        userAsset.setZCJFYYE(userAsset.getZCJFYYE().add(surplus));
        userAssetMapper.update(userAsset);

        UserBonus userBonus = new UserBonus();
        userBonus.setUserId(userId);
        userBonus.setFromUserId(userId);
        userBonus.setTradeType(TradeTypeEnum.动态.getCode());
        userBonus.setBonusType(6);
        userBonus.setInOrOut(CashBonusInOrOutEnum.支出.getCode());
        userBonus.setAmount(surplus);
        userBonus.setCashBonus(surplus);
        userBonus.setRemark("转出到注册积分");
        userBonus.setDTFTBonus(BigDecimal.ZERO);
        userBonus.setLYJFBonus(BigDecimal.ZERO);
        userBonus.setJTFTBonus(BigDecimal.ZERO);
        userBonus.setSCJFBonus(BigDecimal.ZERO);
        userBonus.setJYFBonus(BigDecimal.ZERO);
        userBonus.setIp(ip);
        userBonusService.insert(userBonus);

        UserZCJF userZCJF = new UserZCJF();
        userZCJF.setUserId(userId);
        userZCJF.setFromUserId(userId);
        userZCJF.setAmount(surplus);
        userZCJF.setInOrOut(CashBonusInOrOutEnum.进账.getCode());
        userZCJF.setRemark("源自注册积分转入");
        userZCJF.setIsdel(0);
        userZCJF.setIp(ip);
        userZCJFService.insert(userZCJF);
    }

    @Override
    @Transactional
    public void getCash(BigDecimal userId, UserCashBonusGet cashBonusGet) {

        UserAsset userAsset = userAssetMapper.selectByUserId(userId);
        SharesConfig config = sharesConfigService.getConfig();

        BigDecimal SXF = cashBonusGet.getAmount().multiply(config.getTXSXF()).divide(new BigDecimal("100"));
        if (userAsset.getBonusSurplus().compareTo(cashBonusGet.getAmount().add(SXF)) == -1)
            throw new RuntimeException("余额不足");

        userAsset.setBonusSurplus(userAsset.getBonusSurplus().subtract(cashBonusGet.getAmount().add(SXF)));
        userAssetMapper.update(userAsset);

        cashBonusGet.setUserId(userId);
        cashBonusGet.setStatus(1);
        userCashBonusGetService.insert(cashBonusGet);

        UserBonus userBonus = new UserBonus();
        userBonus.setUserId(userId);
        userBonus.setFromUserId(userId);
        userBonus.setTradeType(TradeTypeEnum.动态.getCode());
        userBonus.setBonusType(0);
        userBonus.setInOrOut(CashBonusInOrOutEnum.支出.getCode());
        userBonus.setAmount(cashBonusGet.getAmount().add(SXF));
        userBonus.setCashBonus(cashBonusGet.getAmount());
        userBonus.setJYFBonus(SXF);
        userBonus.setRemark("积分提取");
        userBonus.setDTFTBonus(BigDecimal.ZERO);
        userBonus.setLYJFBonus(BigDecimal.ZERO);
        userBonus.setJTFTBonus(BigDecimal.ZERO);
        userBonus.setSCJFBonus(BigDecimal.ZERO);
        userBonusService.insert(userBonus);
    }

    @Override
    @Transactional
    public void deductLYJFYE(String userName, BigDecimal amount, String remark) {

        User user = userService.queryByName(userName);
        if (user == null)
            throw new RuntimeException("用户不存在");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == -1)
            throw new RuntimeException("金额不合法");

        UserAsset userAsset = userAssetMapper.selectByUserId(user.getUserId());
        if (amount.compareTo(userAsset.getLYJFYE()) == 1){
            throw new RuntimeException("余额不足");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        UserBonus userBonus = new UserBonus();
        userBonus.setUserId(user.getUserId());
        userBonus.setFromUserId(user.getUserId());
        userBonus.setTradeType(2);
        userBonus.setBonusType(8);
        userBonus.setInOrOut(2);
        userBonus.setAmount(amount);
        userBonus.setLYJFBonus(amount);
        userBonus.setJYFBonus(BigDecimal.ZERO);
        userBonus.setSCJFBonus(BigDecimal.ZERO);
        userBonus.setJTFTBonus(BigDecimal.ZERO);
        userBonus.setCashBonus(BigDecimal.ZERO);
        userBonus.setDTFTBonus(BigDecimal.ZERO);
        userBonus.setRemark(remark);
        userBonus.setIp(ip);
        userBonusService.insert(userBonus);

        Log log = new Log();
        log.setUserName(userName);
        log.setLogType(17);
        log.setUserType(1);
        log.setRemark("管理员"+ manager.getName() + "|扣除" + amount + "|" + remark);
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        userAsset.setLYJFYE(userAsset.getLYJFYE().subtract(amount));
        userAssetMapper.update(userAsset);
    }
}

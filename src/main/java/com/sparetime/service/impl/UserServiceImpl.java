package com.sparetime.service.impl;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.cons.PlaceAreaEnum;
import com.sparetime.common.cons.TradeTypeEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.*;
import com.sparetime.domian.*;
import com.sparetime.domian.extend.UserGuide;
import com.sparetime.domian.extend.UserTree;
import com.sparetime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private NetUserMapper netUserMapper;

    @Autowired
    private UserAssetMapper userAssetMapper;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private UserZCJFMapper userZCJFMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserJTFTBonusMapper userJTFTBonusMapper;

    @Autowired
    private UserBonusService userBonusService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private LogService logService;

    @Autowired
    private ManagerService managerService;

    @Override
    public List<User> queryListByExample(User user, Page page) {
        FieldUtil.spaceToNull(user);
        return userMapper.selectByExample(user, page);
    }

    @Override
    public User queryByKey(BigDecimal id) {
        return userMapper.selectByKey(id);
    }

    @Override
    @Transactional
    public void updateSelective(User user, UserProfile profile) {

        User old = userMapper.selectByKey(user.getUserId());
        UserProfile oldP = userProfileService.queryByUserId(profile.getUserId());
        String remarkP = FieldUtil.diffField(oldP, profile);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request.getSession().getAttribute("managerId") != null){
            Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
            String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

            Log log = new Log();
            log.setLogType(8);
            log.setUserType(2);
            log.setUserName(manager.getName());
            log.setRemark(FieldUtil.diffField(old, user) + remarkP);
            log.setIp(ip);
            log.setCdate(new Date());
            logService.insert(log);
        }

        FieldUtil.spaceToNull(user);
        if (!StringUtils.isEmpty(user.getPassword()))
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (!StringUtils.isEmpty(user.getPassword1()))
            user.setPassword1(bCryptPasswordEncoder.encode(user.getPassword1()));
        userMapper.updateSelective(user);
        userProfileService.update(profile);
    }

    @Override
    public BigDecimal getUserIdByUserSN(String userSN) {
        if (!StringUtils.isEmpty(userSN)){
            User userParam = new User();
            userParam.setUserSN(userSN);
            List<User> users = userMapper.selectByExample(userParam,null);
            if (!CollectionUtils.isEmpty(users)){
                return users.get(0).getUserId();
            }
        }
        return new BigDecimal(-1);
    }

    @Override
    @Transactional
    public synchronized BigDecimal addUser(User user, BigDecimal productId, BigDecimal loginUserId, String bankAccountName, String idCard, BigDecimal ZCJFYYE, BigDecimal DTFTJJYE) {

        if (!Pattern.compile("[a-zA-Z]").matcher(user.getUserName()).find()){
            throw new RuntimeException("用户名必须包含字母");
        }

        user.setRegDate(new Date());
        if (userMapper.selectByName(user.getUserName()) != null)
            throw new RuntimeException("该用户已存在");

        if (loginUserId != null){
            String parentIds = functionMapper.callP_GetParentUserIds(user.getParentUserId());
            boolean isSameTeam = false;
            if (!StringUtils.isEmpty(parentIds)){
                for (String id : parentIds.split(",")){
                    if (new BigDecimal(id).compareTo(user.getRecommendUserId()) == 0){
                        isSameTeam = true;
                        break;
                    }
                }
            }

            if (!isSameTeam)
                throw new RuntimeException("推荐人和安置人不在同一团队");

            isSameTeam = false;
            if (!StringUtils.isEmpty(parentIds)){
                for (String id : parentIds.split(",")){
                    if (new BigDecimal(id).compareTo(loginUserId) == 0){
                        isSameTeam = true;
                        break;
                    }
                }
            }
            if (!isSameTeam){
                throw new RuntimeException("你和安置人不在同一团队");
            }

            Date regDate = Date.from(LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
            if (userProfileService.countByIdCard(idCard, regDate) >= 3)
                throw new RuntimeException("同一个身份证每月只能注册3个账号");
        }

        while (true){
            String userSN = User.builderUserSN();
            if (getUserIdByUserSN(userSN).compareTo(new BigDecimal(-1)) == 0) {
                user.setUserSN(userSN);
                break;
            }
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPassword1(bCryptPasswordEncoder.encode(user.getPassword1()));
        user.setStatus("1");

        userMapper.insert(user);

        Product product = productMapper.selectByKey(productId);
        if (product.getIsShow() != 0) throw new RuntimeException("product不合法");

        NetUser netUser = new NetUser();
        netUser.setUserId(user.getUserId());
        netUser.setRecommendUserId(user.getRecommendUserId());
        netUser.setParentUserId(user.getParentUserId());
        netUser.setPlaceArea(Integer.parseInt(user.getPlaceArea()+""));
        netUser.setProductId(productId);
        netUser.setPrice(product.getInvested());
        netUser.setTotalProfit(BigDecimal.ZERO);
        netUser.setLastSalePrice(BigDecimal.ZERO);
        netUser.setIsOut(0);
        netUser.setCdate(new Date());
        netUserMapper.insert(netUser);

        if (loginUserId != null){
            UserAsset userAsset = userAssetMapper.selectByUserId(loginUserId);
            synchronized (this){
                if (userAsset.getZCJFYYE().compareTo(ZCJFYYE) == -1)
                    throw new RuntimeException("注册积分不足");

                if (userAsset.getDTFTJJYE().compareTo(DTFTJJYE) == -1)
                    throw new RuntimeException("报单积分不足");

                userAsset.setZCJFYYE(userAsset.getZCJFYYE().subtract(ZCJFYYE));
                userAsset.setDTFTJJYE(userAsset.getDTFTJJYE().subtract(DTFTJJYE));
                userAssetMapper.update(userAsset);
            }

            UserZCJF userZCJF = new UserZCJF();
            userZCJF.setUserId(loginUserId);
            userZCJF.setAmount(ZCJFYYE);
            userZCJF.setFromUserId(user.getUserId());
            userZCJF.setInOrOut(CashBonusInOrOutEnum.支出.getCode());
            userZCJF.setRemark("扣除注册积分");
            userZCJF.setIsdel(0);
            userZCJF.setIp(user.getIp());
            userZCJF.setCdate(new Date());
            userZCJFMapper.insert(userZCJF);

            Order order = new Order();
            order.setIsUp(0);
            order.setNetUserId(netUser.getNetUserId());
            order.setPayAmount(product.getInvested());
            order.setPayUserId(user.getUserId());
            order.setPrice(product.getInvested());
            order.setProductId(productId);
            order.setUserId(loginUserId);
            order.setCdate(new Date());
            orderMapper.insert(order);

            UserBonus userBonus = new UserBonus();
            userBonus.setUserId(user.getUserId());
            userBonus.setAmount(product.getInvested().multiply(product.getPGBL()).divide(new BigDecimal("100")));
            userBonus.setJTFTBonus(userBonus.getAmount());
            userBonus.setBonusType(8);
            userBonus.setFromUserId(loginUserId);
            userBonus.setInOrOut(1);
            userBonus.setNetUserId(netUser.getNetUserId());
            userBonus.setRemark("注册"+ user.getUserName());
            userBonus.setTradeType(TradeTypeEnum.静态.getCode());
            userBonus.setCashBonus(BigDecimal.ZERO);
            userBonus.setDTFTBonus(BigDecimal.ZERO);
            userBonus.setLYJFBonus(BigDecimal.ZERO);
            userBonus.setSCJFBonus(BigDecimal.ZERO);
            userBonus.setJYFBonus(BigDecimal.ZERO);
            userBonus.setIp(user.getIp());
            userBonusService.insert(userBonus);

//            UserBonus userBonus1 = new UserBonus();
//            userBonus1.setUserId(user.getUserId());
//            userBonus1.setAmount(DTFTJJYE);
//            userBonus1.setJTFTBonus(BigDecimal.ZERO);
//            userBonus1.setBonusType(0);
//            userBonus1.setFromUserId(loginUserId);
//            userBonus1.setInOrOut(1);
//            userBonus1.setNetUserId(netUser.getNetUserId());
//            userBonus1.setRemark("注册"+ user.getUserName());
//            userBonus1.setTradeType(TradeTypeEnum.动态.getCode());
//            userBonus1.setCashBonus(BigDecimal.ZERO);
//            userBonus1.setDTFTBonus(DTFTJJYE);
//            userBonus1.setLYJFBonus(BigDecimal.ZERO);
//            userBonus1.setSCJFBonus(BigDecimal.ZERO);
//            userBonus1.setJYFBonus(BigDecimal.ZERO);
//            userBonus1.setIp(user.getIp());
//            userBonusService.insert(userBonus1);

            if (DTFTJJYE.compareTo(BigDecimal.ZERO) == 1){
                UserBonus userBonus2 = new UserBonus();
                userBonus2.setUserId(loginUserId);
                userBonus2.setFromUserId(loginUserId);
                userBonus2.setTradeType(1);
                userBonus2.setBonusType(8);
                userBonus2.setInOrOut(2);
                userBonus2.setAmount(DTFTJJYE);
                userBonus2.setDTFTBonus(DTFTJJYE);
                userBonus2.setCashBonus(BigDecimal.ZERO);
                userBonus2.setLYJFBonus(BigDecimal.ZERO);
                userBonus2.setJTFTBonus(BigDecimal.ZERO);
                userBonus2.setSCJFBonus(BigDecimal.ZERO);
                userBonus2.setJYFBonus(BigDecimal.ZERO);
                userBonus2.setRemark("注册" + user.getUserName());
                userBonus2.setIp(user.getIp());
                userBonus2.setCdate(new Date());
                userBonusService.insert(userBonus2);
            }

            Performance performance = new Performance();
            performance.setUserId(user.getUserId());
            performance.setAmount(product.getInvested());
            performance.setProductName(product.getName());
            performanceService.insert(performance);
        }
        userAssetMapper.insertUserId(user.getUserId(), product.getInvested().multiply(product.getPGBL()).divide(new BigDecimal("100")));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        Log log = new Log();
        log.setLogType(8);
        log.setIp(ip);
        log.setCdate(new Date());
        if (loginUserId != null){
            //functionMapper.callP_PayBonus(user.getUserId());
            log.setRemark("新增会员: 用户" + userMapper.selectByKey(loginUserId).getUserName() + "（" + loginUserId + "）注册->" + user.getUserName() + "（" + user.getUserId() + "）");
            log.setUserType(1);
            log.setUserName(userMapper.selectByKey(loginUserId).getUserName());
        }else {
            Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
            log.setRemark("新增会员: 管理员" + manager.getName() + "（" + manager.getId() + "）注册->" + user.getUserName() + "（" + user.getUserId() + "）");
            log.setUserName(manager.getName());
            log.setUserType(2);
        }
        logService.insert(log);
        functionMapper.callP_PayBonus(user.getUserId());

        UserProfile userProfile = userProfileService.queryByUserId(user.getUserId());
        if (userProfile != null){
            userProfile.setBankAccountName(bankAccountName);
            userProfile.setIdCard(idCard);
            userProfileService.update(userProfile);
        }

        return user.getUserId();
    }


    @Override
    public boolean existByParentUserIdAndPlaceArea(BigDecimal parentUserId, int placeArea) {
        return userMapper.countByParentUserIdAndPlaceArea(parentUserId, placeArea) > 0;
    }

    @Override
    public User queryByName(String name) {
        return userMapper.selectByName(name);
    }

    @Override
    public UserTree getParentUser(User user, int level) {

        UserTree userTree = new UserTree();
        if (user != null){
            userTree.setUser(user);
            userTree.setUserAsset(userAssetMapper.selectByUserId(user.getUserId()));

            getParents(userTree, level);
        }
        return userTree;
    }

    @Override
    @Transactional
    public void upgrade(BigDecimal userId, BigDecimal productId) {

        UserAsset userAsset = userAssetMapper.selectByUserId(userId);
        NetUser netUser = netUserMapper.selectByUserId(userId);
        Product oldProduct = productMapper.selectByKey(netUser.getProductId());
        Product newProduct = productMapper.selectByKey(productId);

        if (userAsset.getZCJFYYE().compareTo(newProduct.getInvested().subtract(netUser.getPrice())) == -1)
            throw new RuntimeException("余额不足");

        UserZCJF userZCJF = new UserZCJF();
        userZCJF.setUserId(userId);
        userZCJF.setNetUserId(netUser.getNetUserId());
        userZCJF.setAmount(newProduct.getInvested().subtract(oldProduct.getInvested()));
        userZCJF.setFromUserId(userId);
        userZCJF.setInOrOut(CashBonusInOrOutEnum.支出.getCode());
        userZCJF.setRemark("升级支出");
        userZCJF.setIsdel(0);
        userZCJF.setCdate(new Date());
        userZCJFMapper.insert(userZCJF);

        Order order = new Order();
        order.setUserId(userId);
        order.setNetUserId(netUser.getNetUserId());
        order.setProductId(productId);
        order.setPrice(newProduct.getInvested());
        order.setPayAmount(newProduct.getInvested().subtract(oldProduct.getInvested()));
        order.setIsUp(1);
        order.setPayUserId(userId);
        order.setCdate(new Date());
        orderMapper.insert(order);

        BigDecimal balance = newProduct.getInvested().multiply(newProduct.getPGBL()).divide(new BigDecimal("100")).subtract(
                oldProduct.getInvested().multiply(oldProduct.getPGBL()).divide(new BigDecimal("100")));

        UserBonus userBonus = new UserBonus();
        userBonus.setUserId(userId);
        userBonus.setFromUserId(userId);
        userBonus.setNetUserId(netUser.getNetUserId());
        userBonus.setTradeType(TradeTypeEnum.静态.getCode());
        userBonus.setBonusType(0);
        userBonus.setInOrOut(CashBonusInOrOutEnum.进账.getCode());
        userBonus.setAmount(balance);
        userBonus.setJTFTBonus(balance);
        userBonus.setCashBonus(BigDecimal.ZERO);
        userBonus.setDTFTBonus(BigDecimal.ZERO);
        userBonus.setLYJFBonus(BigDecimal.ZERO);
        userBonus.setSCJFBonus(BigDecimal.ZERO);
        userBonus.setJYFBonus(BigDecimal.ZERO);
        userBonus.setRemark("升级获得配送积分");
        userBonusService.insert(userBonus);

        userAsset.setZCJFYYE(userAsset.getZCJFYYE().subtract(newProduct.getInvested().subtract(netUser.getPrice())));
        userAsset.setJTFTJJYE(userAsset.getJTFTJJYE().add(balance));
        userAssetMapper.update(userAsset);

        User user = userMapper.selectByKey(userId);
        user.setUpgraded(1);
        userMapper.updateSelective(user);

        Performance performance = new Performance();
        performance.setUserId(userId);
        performance.setProductName(newProduct.getName());
        performance.setAmount(newProduct.getInvested().subtract(oldProduct.getInvested()));
        performanceService.insert(performance);

        netUser.setPrice(newProduct.getInvested());
        netUser.setProductId(productId);
        netUserMapper.update(netUser);

        functionMapper.callP_PayBonus_Upgrade(userId, newProduct.getInvested().subtract(oldProduct.getInvested()));
    }

    private void getParents(UserTree tree, int level){

        if (level <= 0)
            return;

        User query = new User();
        query.setParentUserId(tree.getUser().getUserId());
        List<User> list = userMapper.selectByExample(query, null);

        if (CollectionUtils.isEmpty(list))
            return;

        list.forEach(user -> {
            UserTree userTree = new UserTree();
            userTree.setUser(user);
            userTree.setUserAsset(userAssetMapper.selectByUserId(user.getUserId()));
            if (user.getPlaceArea() == PlaceAreaEnum.左.getCode())
                tree.setLeft(userTree);
            else if (user.getPlaceArea() == PlaceAreaEnum.右.getCode())
                tree.setRight(userTree);
            getParents(userTree, level -1);
        });
    }

    @Override
    @Transactional
    public void changeProfile(User user, UserProfile userProfile, String ip){

        Log log = new Log();
        log.setLogType(8);
        log.setUserType(1);

        UserProfile oldPro = userProfileService.queryByUserId(userProfile.getUserId());
        StringBuffer remark = new StringBuffer(FieldUtil.diffField(oldPro, userProfile));

        User oldUser = userMapper.selectByKey(userProfile.getUserId());
        if (!user.getMobile().equals(oldUser.getMobile())){
            remark.append("mobile:" + oldUser.getMobile() + "->" + user.getMobile() + " | ");
        }

        if (!user.getEmail().equals(oldUser.getEmail())){
            remark.append("email:" + oldUser.getEmail() + "->" + user.getEmail() + " | ");
        }

        log.setRemark(remark.toString());
        log.setUserName(oldUser.getUserName());
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        userProfileService.update(userProfile);
        userMapper.updateMobileAndEmail(user.getMobile(), user.getEmail(), user.getUserId());
    }

    @Override
    public void changePassword(String password, BigDecimal userId, String ip) {
        userMapper.changePassword(bCryptPasswordEncoder.encode(password), userId);
        User user = userMapper.selectByKey(userId);
        Log log = new Log();
        log.setLogType(2);
        log.setUserType(1);
        log.setUserName(user.getUserName());
        log.setRemark("修改登录密码");
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);
    }

    @Override
    public void changePassword1(String password1, BigDecimal userId, String ip) {
        userMapper.changePassword1(bCryptPasswordEncoder.encode(password1), userId);
        User user = userMapper.selectByKey(userId);
        Log log = new Log();
        log.setLogType(3);
        log.setUserType(1);
        log.setUserName(user.getUserName());
        log.setRemark("修改交易密码");
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);
    }

    @Override
    @Transactional
    public void active(BigDecimal userId, BigDecimal productId) {

        UserAsset userAsset = userAssetMapper.selectByUserId(userId);
        NetUser netUser = netUserMapper.selectByUserId(userId);
        Product product = productMapper.selectByKey(productId);

        if (netUser.getPrice().compareTo(product.getInvested()) == 1)
            throw new RuntimeException("申请等级过低");

        if (userAsset.getZCJFYYE().compareTo(product.getInvested()) == -1)
            throw  new RuntimeException("余额不足");

        Order order = new Order();
        order.setIsUp(0);
        order.setNetUserId(netUser.getNetUserId());
        order.setPayAmount(product.getInvested());
        order.setPayUserId(userId);
        order.setPrice(product.getInvested());
        order.setProductId(productId);
        order.setUserId(userId);
        order.setCdate(new Date());
        orderMapper.insert(order);

        UserZCJF userZCJF = new UserZCJF();
        userZCJF.setUserId(userId);
        userZCJF.setAmount(product.getInvested());
        userZCJF.setFromUserId(userId);
        userZCJF.setInOrOut(CashBonusInOrOutEnum.支出.getCode());
        userZCJF.setRemark("原点激活扣除注册积分");
        userZCJF.setIsdel(0);
        userZCJF.setCdate(new Date());
        userZCJFMapper.insert(userZCJF);

        UserBonus userBonus = new UserBonus();
        userBonus.setUserId(userId);
        userBonus.setAmount(product.getInvested().multiply(product.getPGBL()).divide(new BigDecimal("100")));
        userBonus.setJTFTBonus(userBonus.getAmount());
        userBonus.setBonusType(8);
        userBonus.setFromUserId(userId);
        userBonus.setInOrOut(1);
        userBonus.setNetUserId(netUser.getNetUserId());
        userBonus.setRemark("原点激活");
        userBonus.setTradeType(TradeTypeEnum.静态.getCode());
        userBonusService.insert(userBonus);

        userAsset.setZCJFYYE(userAsset.getZCJFYYE().subtract(product.getInvested()));
        userAsset.setJTFTJJYE(userAsset.getJTFTJJYE().add(product.getInvested().multiply(product.getPGBL()).divide(new BigDecimal("100"))));
        userAssetMapper.update(userAsset);

        netUser.setProductId(productId);
        netUser.setPrice(product.getInvested());
        netUser.setIsOut(0);
        netUser.setTotalProfit(BigDecimal.ZERO);
        netUserMapper.update(netUser);

        Performance performance = new Performance();
        performance.setUserId(userId);
        performance.setAmount(product.getInvested());
        performance.setProductName(product.getName());
        performanceService.insert(performance);
    }

    @Override
    public long countRegInfo(Date startTime, Date endTime, String name) {
        return userMapper.countRegInfo(startTime, endTime, name);
    }

    @Override
    public List<UserGuide> queryGuideList(UserGuide query, Page page) {
        return userMapper.selectGuideList(query, page);
    }

    @Override
    @Transactional
    public void guide(Date startTime, Date endTime, Integer productId, BigDecimal num) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(13);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("时间：" + startTime + "-" + endTime + " | 等级ID：" + productId + " | 数量：" + num);
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        functionMapper.callGuidanceSell(startTime, endTime, productId, num);
    }

    @Override
    public User queryBySubId(BigDecimal subId) {
        return userMapper.selectBySubId(subId);
    }

    @Override
    public Long queryMaxSubId() {
        return userMapper.selectMaxSubId();
    }
}

package com.sparetime.controller.admin;

import com.sparetime.domian.ShareBuy;
import com.sparetime.domian.ShareSellBuyCross;
import com.sparetime.service.ShareBuyService;
import com.sparetime.service.ShareSellBuyCrossService;
import com.sparetime.service.SharesBuyLogService;
import com.sparetime.service.SharesSellRefundLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Controller
@RequestMapping("/admin/shareSellBuyCross")
public class ShareSellBuyCrossController {

    @Autowired
    private ShareSellBuyCrossService shareSellBuyCrossService;

    @Autowired
    private ShareBuyService shareBuyService;

    @Autowired
    private SharesSellRefundLogService sharesSellRefundLogService;

    @Autowired
    private SharesBuyLogService sharesBuyLogService;



    @RequestMapping("/info")
    public String info(BigDecimal sellId, BigDecimal buyId, Model model){

        List<ShareSellBuyCross> crosses = null;
        List<?> logs = null;

        if (sellId != null) {
            crosses = shareSellBuyCrossService.queryListBySellId(sellId);
            logs = sharesSellRefundLogService.queryListBySellId(sellId);
        }else if (buyId != null){
            crosses = shareSellBuyCrossService.queryListByBuyId(buyId);
            logs = sharesBuyLogService.queryListByBuyId(buyId);
        }

        List<ShareBuy> buys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(crosses)){
            crosses.forEach(cross-> buys.add(shareBuyService.queryByKey(cross.getBuyId())));
        }

        model.addAttribute("crosses", crosses == null ? new ArrayList<>() : crosses);
        model.addAttribute("buys", buys);
        model.addAttribute("logs", logs == null ? new ArrayList<>() : logs);
        return "admin/share_sell_buy_cross/info";
    }
}

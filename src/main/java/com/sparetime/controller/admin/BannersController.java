package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FileUtil;
import com.sparetime.domian.Banners;
import com.sparetime.domian.SysConfig;
import com.sparetime.service.BannersService;
import com.sparetime.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/banners")
public class BannersController {

    @Autowired
    private BannersService bannersService;

    @Autowired
    private SysConfigService sysConfigService;

    @RequestMapping("/list")
    public String list(Banners query, Page page, Model model){

        List<Banners> list = bannersService.queryListByExample(query, page);

        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("query", query);
        return "admin/banners/list";
    }

    @RequestMapping("toAdd")
    public String toAdd(Banners banners){
        return "admin/banners/add";
    }

    @RequestMapping("add")
    public String add(@Valid Banners banners, BindingResult result, MultipartFile img, HttpServletRequest request) throws  Exception{

        if (banners.getSort() == null) banners.setSort(10);

        if (img == null){
            FieldError fieldError = new FieldError("banners", "imgurl", "图片不能为空");
            result.addError(fieldError);
        }

        if (result.hasFieldErrors()){
            return "admin/banners/add";
        }
        SysConfig sysConfig = sysConfigService.queryByKey("file_path");
        String path =  FileUtil.upload(new MultipartFile[]{img}, sysConfig.getValue(), request.getServerPort()).get(0);

        banners.setImgurl(path);
        bannersService.insert(banners);
        return "redirect:/admin/banners/list";
    }

    @RequestMapping("toUpdate")
    public String toUpdate(Banners banners, Model model){
        model.addAttribute("banners", bannersService.queryByKey(banners.getId()));
        return "admin/banners/update";
    }

    @RequestMapping("update")
    public String update(@Valid Banners banners, BindingResult result, MultipartFile img, HttpServletRequest request) throws Exception{
        if ((img == null || img.isEmpty()) && StringUtils.isEmpty(banners.getImgurl()) ){
            FieldError fieldError = new FieldError("banners", "imgurl", "图片不能为空");
            result.addError(fieldError);
        }

        if (result.hasFieldErrors()){
            return "admin/banners/update";
        }

        if (img != null && !img.isEmpty()){
            SysConfig sysConfig = sysConfigService.queryByKey("file_path");
            String path = FileUtil.upload(new MultipartFile[]{img}, sysConfig.getValue(), request.getServerPort()).get(0);
            banners.setImgurl(path);
        }
        bannersService.update(banners);
        return "redirect:/admin/banners/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(Long id){
        bannersService.delete(id);
        return "/admin/banners/list";
    }
}

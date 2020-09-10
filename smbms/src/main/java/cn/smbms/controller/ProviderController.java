package cn.smbms.controller;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/provider")
public class ProviderController {
    @Autowired
    private ProviderService providerService;

    @RequestMapping("/query")
    public String query(@RequestParam(value = "queryProName", defaultValue = "") String queryProName,
                        @RequestParam(value = "queryProCode", defaultValue = "") String queryProCode,
                        Model model,
                        @RequestParam(value = "pageIndex", defaultValue = "1") Integer currentPageNo) {
        List<Provider> providerList = new ArrayList<Provider>();
        int pageSize = Constants.pageSize;
        int totalCount = providerService.getProviderCount(queryProName, queryProCode);
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        providerList = providerService.getProviderList(queryProName, queryProCode, currentPageNo, pageSize);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "providerlist";
    }

    @RequestMapping("/addProvider")
    public String addProvider(@ModelAttribute("provider") Provider provider) {
        return "provideradd";
    }

    @RequestMapping("/addProviderSave")
    public String addProviderSave(@ModelAttribute("provider") @Valid Provider provider, BindingResult result, HttpSession session,
                                  @RequestParam("a_idPicPath") MultipartFile multipartFile, Model model,
                                  @RequestParam("a_workPicPath") MultipartFile multipartFile1) {
        if (result.hasErrors()) {
            return "provideradd";
        }
        String savePath = null;
        if (!multipartFile.isEmpty()) {
            String oldName = multipartFile.getOriginalFilename();
            long size = multipartFile.getSize();
            String surfix = FilenameUtils.getExtension(oldName);
            if (size > 500 * 1024) {
                model.addAttribute("uploadFileError", "上传图片不能超过500k");
                return "provideradd";
            } else {
                String[] types = {"jpg", "png", "jpeg", "pneg", "gif"};
                if (!Arrays.asList(types).contains(surfix)) {
                    model.addAttribute("uploadFileError", "上传图片的类型只能为jpg,png,jpeg,pneg,gif");
                    return "provideradd";
                } else {
                    String targetPath = session.getServletContext().getRealPath("statics" + File.separator + "upload");
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(100000) + "_Personal." + surfix;
                    File saveFile = new File(targetPath, fileName);
                    if (!saveFile.exists()) {
                        saveFile.mkdirs();
                    }
                    try {
                        multipartFile.transferTo(saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadFileError", "上传图片失败");
                        return "provideradd";
                    }
                    savePath = targetPath + File.separator + fileName;
                }
            }
        }

        String savePath1 = null;
        if (!multipartFile1.isEmpty()) {
            String oldName = multipartFile1.getOriginalFilename();
            long size = multipartFile1.getSize();
            String surfix = FilenameUtils.getExtension(oldName);
            if (size > 500 * 1024) {
                model.addAttribute("uploadFileError", "上传图片不能超过500k");
                return "provideradd";
            } else {
                String[] types = {"jpg", "png", "jpeg", "pneg", "gif"};
                if (!Arrays.asList(types).contains(surfix)) {
                    model.addAttribute("uploadFileError", "上传图片的类型只能为jpg,png,jpeg,pneg,gif");
                    return "provideradd";
                } else {
                    String targetPath = session.getServletContext().getRealPath("statics" + File.separator + "upload");
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(100000) + "_Personal." + surfix;
                    File saveFile = new File(targetPath, fileName);
                    if (!saveFile.exists()) {
                        saveFile.mkdirs();
                    }
                    try {
                        multipartFile1.transferTo(saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadFileError", "上传图片失败");
                        return "provideradd";
                    }
                    savePath1 = targetPath + File.separator + fileName;
                }
            }
        }

        provider.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        provider.setIdPicPath(savePath);
        provider.setWorkPicPath(savePath1);
        boolean flag = false;
        flag = providerService.add(provider);
        if (flag) {
            return "redirect:/provider/query";
        } else {
            return "provideradd";
        }
    }

    @RequestMapping("/modifyProvider/{id}")
    public String modifyProvider(@PathVariable String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Provider provider = null;
            provider = providerService.getProviderById(id);
            if (provider.getIdPicPath() != null) {
                String idPicPath = provider.getIdPicPath();
                String[] split = idPicPath.split("\\\\");
                provider.setIdPicPath(split[split.length - 1]);
            }
            model.addAttribute("provider", provider);
            return "providermodify";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/modifyPorviderSave")
    public String modifyProviderSave(Provider provider, HttpSession session,
                                     @RequestParam("a_idPicPath") MultipartFile multipartFile,
                                     Model model) {
        String savePath = null;
        if (!multipartFile.isEmpty()) {
            String oldName = multipartFile.getOriginalFilename();
            long size = multipartFile.getSize();
            String surfix = FilenameUtils.getExtension(oldName);
            if (size > 500 * 1024) {
                model.addAttribute("uploadFileError", "上传图片不能超过500k");
                return "provideradd";
            } else {
                String[] types = {"jpg", "png", "jpeg", "pneg", "gif"};
                if (!Arrays.asList(types).contains(surfix)) {
                    model.addAttribute("uploadFileError", "上传图片的类型只能为jpg,png,jpeg,pneg,gif");
                    return "provideradd";
                } else {
                    String targetPath = session.getServletContext().getRealPath("statics" + File.separator + "upload");
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(100000) + "_Personal." + surfix;
                    File saveFile = new File(targetPath, fileName);
                    if (!saveFile.exists()) {
                        saveFile.mkdirs();
                    }
                    try {
                        multipartFile.transferTo(saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadFileError", "上传图片失败");
                        return "provideradd";
                    }
                    savePath = targetPath + File.separator + fileName;
                }
            }
        }
        provider.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        provider.setIdPicPath(savePath);
        boolean flag = false;
        flag = providerService.modify(provider);
        if (flag) {
            return "redirect:/provider/query";
        } else {
            return "providermodify";
        }
    }

    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Provider provider = null;
            provider = providerService.getProviderById(id);
            model.addAttribute("provider", provider);
            return "providerview";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/hasProCode")
    @ResponseBody
    public String hasProCode(@RequestParam("proCode") String proCode) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(proCode)) {
            //userCode == null || userCode.equals("")
            resultMap.put("proCode", "exist");
        } else {
            int res = providerService.queryProviderByCode(proCode);
            if (res == 1) {
                resultMap.put("proCode", "exist");
            } else {
                resultMap.put("proCode", "notexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping("/show")
    @ResponseBody
    public String show(@RequestParam("id") String id) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Provider provider = null;
            provider = providerService.getProviderById(id);
            return JSON.toJSONString(provider);
        } else {
            return "null";
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "proid", defaultValue = "0") String id) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)) {
            int flag = providerService.deleteProviderById(id);
            if (flag == 0) {//删除成功
                resultMap.put("delResult", "true");
            } else if (flag == -1) {//删除失败
                resultMap.put("delResult", "false");
            } else if (flag > 0) {//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        return JSONArray.toJSONString(resultMap);
    }
}

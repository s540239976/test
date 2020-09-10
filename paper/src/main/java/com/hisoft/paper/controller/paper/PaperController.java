package com.hisoft.paper.controller.paper;

import com.hisoft.paper.pojo.Paper;
import com.hisoft.paper.pojo.User;
import com.hisoft.paper.service.paper.PaperService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/paper")
public class PaperController {
    @Autowired
    private PaperService paperService;

    @RequestMapping("/query")
    public String query(Model model,
                        @RequestParam(value = "title", defaultValue = "") String title,
                        @RequestParam(value = "type", defaultValue = "0") Integer type) {
        List<Paper> paperList = paperService.queryPaperList(title, type);
        model.addAttribute(paperList);
        return "paperlist";
    }

    @RequestMapping("/addPaper")
    public String addPaper() {
        return "paperadd";
    }

    @RequestMapping("/addPaperSave")
    public String addPaperSave(Paper paper, @RequestParam("paperFile") MultipartFile multipartFile,
                               HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String savePath = null;
        if (!multipartFile.isEmpty()) {
            String oldName = multipartFile.getOriginalFilename();
            long size = multipartFile.getSize();
            String surfix = FilenameUtils.getExtension(oldName);
            if (size > 500 * 1024) {
                model.addAttribute("uploadFileError", "上传文件不能超过500k");
                return "paperadd";
            } else {
                String[] types = {"doc", "docx"};
                if (!Arrays.asList(types).contains(surfix)) {
                    model.addAttribute("uploadFileError", "上传文件的类型只能为doc,docx");
                    return "paperadd";
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
                        model.addAttribute("uploadFileError", "上传文件失败");
                        return "useradd";
                    }
                    savePath = targetPath + File.separator + fileName;
                    paper.setFileName(fileName);
                }
            }
        }
        paper.setPaperPath(savePath);
        paper.setCreationDate(new Date());
        paper.setCreatedBy(String.valueOf(user.getId()));
        Integer res = paperService.addPaper(paper);
        if (res == 1) {
            return "redirect:/paper/query";
        } else {
            return "paperadd";
        }
    }

    @RequestMapping("modifyPaper")
    public String modifyPaper(@RequestParam("id") Integer id, Model model) {
        Paper paper = paperService.quereyPaperById(id);
        model.addAttribute("paper", paper);
        return "papermodify";
    }

    @RequestMapping("/modifyPaperSave")
    public String modifyPaperSave(Paper paper, HttpSession session) {
        User user = (User) session.getAttribute("user");
        paper.setModifyBy(String.valueOf(user.getId()));
        paper.setModifyDate(new Date());
        Integer res = paperService.modifyPaper(paper);
        if (res == 1) {
            return "redirect:/paper/query";
        } else {
            return "papermodify";
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(@RequestParam("id") Integer id){
        Integer res = paperService.delete(id);
        Map<String,String> map = new HashMap<>();
        if(res==1){
            map.put("delResult","true");
        }else{
            map.put("delResult","false");
        }
        return map;
    }

    @RequestMapping("/check")
    @ResponseBody
    public Map check(@RequestParam("title") String title){
        Integer res = paperService.queryPaperByName(title);
        Map<String,String> map = new HashMap<>();
        if(res==null){
            map.put("result","true");
        }else{
            map.put("result","false");
        }
        return map;
    }
}

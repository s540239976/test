package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.role.RoleServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(Constants.USER_SESSION);
        return "login";
    }

    @RequestMapping("/query")
    public String query(@RequestParam(value = "queryname", defaultValue = "") String queryUserName,
                        @RequestParam(value = "queryUserRole", defaultValue = "0") Integer queryUserRole,
                        @RequestParam(value = "pageIndex", defaultValue = "1") Integer currentPageNo,
                        Model model) {
        List<User> userList = null;
        int pageSize = Constants.pageSize;
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数
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

        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }

    @RequestMapping("/addUser")
    public String addUser(@ModelAttribute("user") User user) {
        return "useradd";
    }

    @RequestMapping("/addUserSave")
    public String addUserSave(User user, HttpSession session, @RequestParam("a_idPicPath") MultipartFile multipartFile, Model model) {
        String savePath = null;
        if (!multipartFile.isEmpty()) {
            String oldName = multipartFile.getOriginalFilename();
            long size = multipartFile.getSize();
            String surfix = FilenameUtils.getExtension(oldName);
            if (size > 500 * 1024) {
                model.addAttribute("uploadFileError", "上传图片不能超过500k");
                return "useradd";
            } else {
                String[] types = {"jpg", "png", "jpeg", "pneg", "gif"};
                if (!Arrays.asList(types).contains(surfix)) {
                    model.addAttribute("uploadFileError", "上传图片的类型只能为jpg,png,jpeg,pneg,gif");
                    return "useradd";
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
                        return "useradd";
                    }
                    savePath = targetPath + File.separator + fileName;
                }
            }
        }
        user.setCreationDate(new Date());
        user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setIdPicPath(savePath);
        if (userService.add(user)) {
            return "redirect:/user/query";
        } else {
            return "useradd";
        }
    }

    @RequestMapping("/modifyUser")
    public String modifyUser(@RequestParam(value = "uid", defaultValue = "") String id,
                             Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            if (user.getIdPicPath() != null) {
                String idPicPath = user.getIdPicPath();
                String[] split = idPicPath.split("\\\\");
                user.setIdPicPath(split[split.length - 1]);
            }
            model.addAttribute("user", user);
            return "usermodify";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/modifyUserSave")
    public String modifyUserSave(User user, HttpSession session,
                                 @RequestParam("a_idPicPath") MultipartFile multipartFile,
                                 Model model) {
        String savePath = null;
        if (!multipartFile.isEmpty()) {
            String oldName = multipartFile.getOriginalFilename();
            long size = multipartFile.getSize();
            String surfix = FilenameUtils.getExtension(oldName);
            if (size > 500 * 1024) {
                model.addAttribute("uploadFileError", "上传图片不能超过500k");
                return "useradd";
            } else {
                String[] types = {"jpg", "png", "jpeg", "pneg", "gif"};
                if (!Arrays.asList(types).contains(surfix)) {
                    model.addAttribute("uploadFileError", "上传图片的类型只能为jpg,png,jpeg,pneg,gif");
                    return "useradd";
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
                        return "useradd";
                    }
                    savePath = targetPath + File.separator + fileName;
                }
            }
        }
        user.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        user.setIdPicPath(savePath);
        if (userService.modify(user)) {
            return "redirect:/user/query";
        } else {
            return "usermodify";
        }
    }

    @RequestMapping("/hasUserCode")
    @ResponseBody
    public String hasUserCode(@RequestParam("userCode") String userCode) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        } else {
            User user = userService.selectUserCodeExist(userCode);
            if (null != user) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "notexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "userview";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }

    //    produces = {"application/json;charset=utf-8"}
    @RequestMapping("/show")
    @ResponseBody
    public String show(@RequestParam("id") String id) {
        User user = null;
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            user = userService.getUserById(id);
            return JSONArray.toJSONString(user);
        } else {
            return "null";
        }
    }

    @RequestMapping("/modifyPassword")
    public String modifyPassword() {
        return "pwdmodify";
    }

    @RequestMapping("/modifyPasswordSave")
    public String modifyPasswordSave(@RequestParam("newpassword") String newpassword,
                                     HttpSession session, Model model) {
        Object o = session.getAttribute(Constants.USER_SESSION);
        boolean flag = false;
        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            flag = userService.updatePwd(((User) o).getId(), newpassword);
            if (flag) {
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                session.removeAttribute(Constants.USER_SESSION);//session注销
            } else {
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        } else {
            model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }
        return "pwdmodify";
    }

    @RequestMapping("/checkPassword")
    @ResponseBody
    public String checkPassword(HttpSession session,
                                @RequestParam("oldpassword") String oldpassword) {
        Object o = session.getAttribute(Constants.USER_SESSION);
        Map<String, String> resultMap = new HashMap<String, String>();

        if (null == o) {//session过期
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {//旧密码输入为空
            resultMap.put("result", "error");
        } else {
            String sessionPwd = ((User) o).getUserPassword();
            if (oldpassword.equals(sessionPwd)) {
                resultMap.put("result", "true");
            } else {//旧密码输入不正确
                resultMap.put("result", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping("/queryRole")
    @ResponseBody
    public String queryRole() {
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        return JSONArray.toJSONString(roleList);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "uid", defaultValue = "0") Integer delId) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (delId <= 0) {
            resultMap.put("delResult", "notexist");
        } else {
            if (userService.deleteUserById(delId)) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping("/tologin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/main")
    public String main(HttpSession session) {
        if (session.getAttribute(Constants.USER_SESSION) == null) {
            return "redirect:/user/tologin";
        }
        return "frame";
    }

    @RequestMapping("/error")
    public String error() {
        return "error";
    }

//    @ExceptionHandler(RuntimeException.class)
//    public String execption(RuntimeException e, HttpServletRequest request) {
//        request.setAttribute("e", e);
//        return "login";
//    }

}

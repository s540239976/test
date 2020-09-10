package cn.smbms.controller;

import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;


    @RequestMapping("/query")
    public String query(Model model) {
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        return "rolelist";
    }

    @RequestMapping("/toRoleAdd")
    public String toRoleAdd() {
        return "roleadd";
    }

    @RequestMapping("/roleAddSave")
    public String roleAddSave(Role role, HttpSession session) {
        role.setCreationDate(new Date());
        role.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        int res = roleService.addRole(role);
        if (res == 1) {
            return "redirect:/role/query";
        } else {
            return "rolelist";
        }
    }

    @RequestMapping("/checkRoleCode")
    @ResponseBody
    public String checkRoleCode(@RequestParam("roleCode") String roleCode) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(roleCode)) {
            resultMap.put("roleCode", "exist");
        } else {
            Integer res = roleService.queryRoleByName(roleCode);
            if (res != null) {
                resultMap.put("roleCode", "exist");
            } else {
                resultMap.put("roleCode", "notexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping("/modifyRole")
    public String modifyRole(@RequestParam("rid") String rid, Model model) {
        Role role = roleService.queryRoleById(rid);
        model.addAttribute("role", role);
        return "modifyrole";
    }

    @RequestMapping("/modifyRoleSave")
    public String modifyRoleSave(Role role, HttpSession session) {
        role.setModifyDate(new Date());
        role.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        Integer res = roleService.modifyRole(role);
        if (res == 1) {
            return "redirect:/role/query";
        } else {
            return "modifyRole";
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(@RequestParam("rid") String delId) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (roleService.delete(delId)!=null) {
            resultMap.put("delResult", "true");
        } else {
            resultMap.put("delResult", "false");
        }
        return resultMap;
    }
}

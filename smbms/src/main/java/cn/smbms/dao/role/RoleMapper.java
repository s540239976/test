package cn.smbms.dao.role;

import cn.smbms.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleMapper {
    public List<Role> getRoleList();

    public int addRole(Role role);

    public Integer queryRoleByName(String roleName);

    public Integer modifyRole(Role role);

    public Role queryRoleById(String id);

    public Integer delete(String id);

}

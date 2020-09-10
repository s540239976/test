package cn.smbms.service.role;

import java.util.List;

import cn.smbms.pojo.Role;

public interface RoleService {
	
	public List<Role> getRoleList();

	public int addRole(Role role);

	public Integer queryRoleByName(String roleName);

	public Integer modifyRole(Role role);

	public Role queryRoleById(String id);

	public Integer delete(String id);
}

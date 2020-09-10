package cn.smbms.service.role;

import java.sql.Connection;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.role.RoleDao;
import cn.smbms.dao.role.RoleDaoImpl;
import cn.smbms.dao.role.RoleMapper;
import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private UserMapper userMapper;

	@Override
	public List<Role> getRoleList() {
		List<Role> roleList = null;
		try {
			roleList = roleMapper.getRoleList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roleList;
	}

	@Override
	@Transactional
	public int addRole(Role role) {
		int res = -1;
		res = roleMapper.addRole(role);
		return res;
	}

	@Override
	public Integer queryRoleByName(String roleName) {
		Integer res = roleMapper.queryRoleByName(roleName);
		return res;
	}

	@Override
	public Integer modifyRole(Role role) {
		Integer res = roleMapper.modifyRole(role);
		return res;
	}

	@Override
	public Role queryRoleById(String id) {
		Role role = roleMapper.queryRoleById(id);
		return role;
	}

	@Override
	@Transactional
	public Integer delete(String id) {
		Integer res =null;
		Integer flag = userMapper.queryUserByRid(id);
		if(flag==null){
			res = roleMapper.delete(id);
		}
		return res;
	}
}

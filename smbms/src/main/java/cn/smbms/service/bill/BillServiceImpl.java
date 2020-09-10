package cn.smbms.service.bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BillDao;
import cn.smbms.dao.bill.BillDaoImpl;
import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillServiceImpl implements BillService {
	@Autowired
	private BillDao billDao;
	@Autowired
	private BillMapper billMapper;

	@Override
	@Transactional
	public boolean add(Bill bill) {
		boolean flag = false;
		try {
			if(billMapper.add(bill) > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public List<Bill> getBillList(Bill bill) {
		List<Bill> billList = null;
		System.out.println("query productName ---- > " + bill.getProductName());
		System.out.println("query providerId ---- > " + bill.getProviderId());
		System.out.println("query isPayment ---- > " + bill.getIsPayment());
		try {
			billList = billMapper.getBillList(bill);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return billList;
	}

	@Override
	@Transactional
	public boolean deleteBillById(String delId) {
		boolean flag = false;
		try {
			if(billMapper.deleteBillById(delId) > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public Bill getBillById(String id) {
		Bill bill = null;
		try{
			bill = billMapper.getBillById(id);
		}catch (Exception e) {
			e.printStackTrace();
			bill = null;
		}
		return bill;
	}

	@Override
	@Transactional
	public boolean modify(Bill bill) {
		boolean flag = false;
		try {

			if(billMapper.modify(bill) > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public int queryBillByCode(String billCode) {
		int res = -1;
		Connection connection = BaseDao.getConnection();
		try{
			res = billDao.queryBillByCode(connection,billCode);
		}catch (Exception e) {
			e.printStackTrace();
			res = -2;
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return res;
	}
}

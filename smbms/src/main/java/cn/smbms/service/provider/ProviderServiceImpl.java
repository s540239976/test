package cn.smbms.service.provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BillDao;
import cn.smbms.dao.bill.BillDaoImpl;
import cn.smbms.dao.bill.BillMapper;
import cn.smbms.dao.provider.ProviderDao;
import cn.smbms.dao.provider.ProviderDaoImpl;
import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProviderServiceImpl implements ProviderService {
    @Autowired
    private ProviderDao providerDao;
    @Autowired
    private BillDao billDao;
    @Autowired
    private ProviderMapper providerMapper;
    @Autowired
    private BillMapper billMapper;

    @Override
    @Transactional
    public boolean add(Provider provider) {
        boolean flag = false;
        try {
            if (providerMapper.add(provider) > 0)
                flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Provider> getProviderList(String proName, String proCode,int currentPageNo, int pageSize) {
        // TODO Auto-generated method stub
        List<Provider> providerList = null;
        System.out.println("query proName ---- > " + proName);
        System.out.println("query proCode ---- > " + proCode);
        try {

            providerList = providerMapper.getProviderList(proName, proCode,currentPageNo,pageSize);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return providerList;
    }

    /**
     * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
     * 若订单表中无该供应商的订单数据，则可以删除
     * 若有该供应商的订单数据，则不可以删除
     * 返回值billCount
     * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
     * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
     * <p>
     * ---判断
     * 如果billCount = -1 失败
     * 若billCount >= 0 成功
     */
    @Override
    public int deleteProviderById(String delId) {
        int billCount = -1;
        try {
            billCount = billMapper.getBillCountByProviderId(delId);
            if (billCount == 0) {
                Provider provider = providerMapper.getProviderById(String.valueOf(delId));
                if(provider.getIdPicPath()==null){
                    providerMapper.deleteProviderById(delId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            billCount = -1;
        }
        return billCount;
    }

    @Override
    public Provider getProviderById(String id) {
        Provider provider = null;
        try {
            provider = providerMapper.getProviderById(id);
        } catch (Exception e) {
            e.printStackTrace();
            provider = null;
        }
        return provider;
    }

    @Override
    public boolean modify(Provider provider) {
        boolean flag = false;
        try {

            if (providerMapper.modify(provider) > 0)
                flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public int queryProviderByCode(String billCode) {
        int res = -1;
        Connection connection = BaseDao.getConnection();
        try {
            res = providerDao.queryProviderByCode(connection, billCode);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            res = -2;
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return res;
    }

    @Override
    public int getProviderCount(String queryProName, String queryProCode) {
        int res = 0;
        res = providerMapper.getProviderCount(queryProName, queryProCode);
        return res;
    }

}

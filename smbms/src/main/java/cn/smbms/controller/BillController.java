package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {
    @Autowired
    private ProviderService providerService;
    @Autowired
    private BillService billService;

    @RequestMapping("/query")
    public String query(@RequestParam(value = "queryProductName",defaultValue = "") String queryProductName,
                        @RequestParam(value = "queryProviderId",defaultValue = "0") Integer queryProviderId,
                        @RequestParam(value = "queryIsPayment",defaultValue = "0") Integer queryIsPayment,
                        Model model){
        List<Provider> providerList = providerService.getProviderList("","",0,15);
        model.addAttribute("providerList", providerList);

        Bill bill = new Bill();
        bill.setIsPayment(queryIsPayment);
        bill.setProductName(queryProductName);
        bill.setProviderId(queryProviderId);
        List<Bill>  billList = billService.getBillList(bill);
        model.addAttribute("billList", billList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        return "billlist";
    }

    @RequestMapping("/addBill")
    public String addBill(@ModelAttribute("bill") Bill bill){
        return "billadd";
    }

    @RequestMapping("/addBillSave")
    public String addBillSave(@ModelAttribute("bill") @Valid Bill bill, BindingResult result, HttpSession session){
        if(result.hasErrors()){
            return "billadd";
        }
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        boolean flag = false;
        flag = billService.add(bill);
        if(flag){
            return "redirect:/bill/query";
        }else{
            return "billadd";
        }
    }

    @RequestMapping("/modifyBill")
    public String modifyBill(@RequestParam("billid") String id,Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            Bill bill = null;
            bill = billService.getBillById(id);
            model.addAttribute("bill", bill);
            return "billmodify";
        }else{
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/modifyBillSave")
    public String modifyBillSave(Bill bill,HttpSession session){
        bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        boolean flag = false;
        flag = billService.modify(bill);
        if(flag){
            return "redirect:/bill/query";
        }else{
            return "billmodify";
        }
    }

    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id,Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            Bill bill = null;
            bill = billService.getBillById(id);
            model.addAttribute("bill", bill);
            return "billview";
        }else{
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/hasBillCode")
    @ResponseBody
    public String hasBillCode(@RequestParam("billCode") String billCode) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(billCode)) {
            //userCode == null || userCode.equals("")
            resultMap.put("billCode", "exist");
        } else {
            int res = billService.queryBillByCode(billCode);
            if (res == 1) {
                resultMap.put("billCode", "exist");
            } else {
                resultMap.put("billCode", "notexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping("/show")
    @ResponseBody
    public String show(@RequestParam("id")String id){
        if(!StringUtils.isNullOrEmpty(id)){
            Bill bill = null;
            bill = billService.getBillById(id);
            return JSON.toJSONString(bill);
        }else{
            return "null";
        }
    }

    @RequestMapping("/queryProvider")
    @ResponseBody
    public String queryProvider(){
        List<Provider> providerList = new ArrayList<Provider>();
        providerList = providerService.getProviderList("","",0,15);
        return JSONArray.toJSONString(providerList);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("billid")String id){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            boolean flag = billService.deleteBillById(id);
            if(flag){//删除成功
                resultMap.put("delResult", "true");
            }else{//删除失败
                resultMap.put("delResult", "false");
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
       return JSONArray.toJSONString(resultMap);
    }
}

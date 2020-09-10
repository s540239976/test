package com.hisoft.paper.service.paper;

import com.hisoft.paper.dao.paper.PaperMapper;
import com.hisoft.paper.pojo.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperMapper paperMapper;
    @Override
    public List<Paper> queryPaperList(String title,Integer type) {
        List<Paper> paperList = paperMapper.queryPaperList(title,type);
        return paperList;
    }

    @Override
    public Integer addPaper(Paper paper) {
        Integer res = paperMapper.addPaper(paper);
        return res;
    }

    @Override
    public Paper quereyPaperById(Integer id) {
        Paper paper = paperMapper.quereyPaperById(id);
        return paper;
    }

    @Override
    public Integer modifyPaper(Paper paper) {
        Integer res = paperMapper.modifyPaper(paper);
        return res;
    }

    @Override
    public Integer delete(Integer id) {
        Integer res = paperMapper.delete(id);
        return res;
    }

    @Override
    public Integer queryPaperByName(String title) {
        Integer res = paperMapper.queryPaperByName(title);
        return res;
    }
}

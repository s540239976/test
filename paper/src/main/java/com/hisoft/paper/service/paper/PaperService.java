package com.hisoft.paper.service.paper;

import com.hisoft.paper.pojo.Paper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperService {
    List<Paper> queryPaperList(String title,Integer type);

    Integer addPaper(Paper paper);

    Paper quereyPaperById(Integer id);

    Integer modifyPaper(Paper paper);

    Integer delete(Integer id);

    Integer queryPaperByName(String title);
}

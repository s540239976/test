package com.hisoft.paper.dao.paper;

import com.hisoft.paper.pojo.Paper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperMapper {
    List<Paper> queryPaperList(@Param("title") String title,@Param("type") Integer type);

    Integer addPaper(Paper paper);

    Paper quereyPaperById(@Param("id") Integer id);

    Integer modifyPaper(Paper paper);

    Integer delete(@Param("id") Integer id);

    Integer queryPaperByName(String title);
}

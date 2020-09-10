package com.hisoft.paper.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Paper {
    private int id;
    private String title;
    private int type;
    private String paperSummary;
    private String paperPath;
    private String createdBy;
    private Date creationDate;
    private String modifyBy;
    private Date modifyDate;
    private String fileName;

    private String author;
    private String typeName;
}

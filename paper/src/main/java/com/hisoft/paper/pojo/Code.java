package com.hisoft.paper.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Code {
    private int id;
    private String code;
    private String codeName;
    private String codeTypeName;
    private String parCode;
    private int sequenceNum;
    private String createdBy;
    private Date creationDate;
    private String modifyBy;
    private Date modifyDate;
  
}

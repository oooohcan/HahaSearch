package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpiderFtpRequest implements Serializable {
    private static final long serialVersionUID = 1227841801177214731L;
    private String target;
    private String name;
    private String uname;
    private String upwd;
    private String visitDir;
    private int deep;
}

package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpiderCancelRequest implements Serializable {
    private static final long serialVersionUID = 1227841801177214731L;
    private int index;
}

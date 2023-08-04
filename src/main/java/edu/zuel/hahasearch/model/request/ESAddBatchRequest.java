package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ESAddBatchRequest implements Serializable {
    private static final long serialVersionUID = -5839144360087867756L;
    private List<ESAddOneRequest> esList;
}

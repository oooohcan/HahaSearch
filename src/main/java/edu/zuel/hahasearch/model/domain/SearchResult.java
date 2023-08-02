package edu.zuel.hahasearch.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {
    private Integer id;
    private String title;
    private String content;
    private String website;
    private String imgUrl;
    private String type;
    private String other;
    private Date date;
}

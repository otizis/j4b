package cc.jaxer.blog.unsplash;

import lombok.Data;

import java.util.List;

@Data
public class SearchResp {
    int total;
    int total_pages;
    List<Photo> results;
}

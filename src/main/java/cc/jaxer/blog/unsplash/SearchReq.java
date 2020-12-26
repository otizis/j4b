package cc.jaxer.blog.unsplash;

import lombok.Data;

@Data
public class SearchReq {
    String query;
    int page = 1;
    int per_page = 10;
}

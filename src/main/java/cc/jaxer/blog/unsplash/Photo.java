package cc.jaxer.blog.unsplash;

import lombok.Data;

@Data
public class Photo {
    String id;
    int width;
    int height;
    Urls urls;
}

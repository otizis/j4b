package cc.jaxer.blog.unsplash;

import cc.jaxer.blog.common.AppConstant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class Urls {
    String raw;
    String full;
    String regular;
    String small;
    String thumb;
    /**
     * 非接口参数，便于替换 代理
     */
    String domain;

    public void replaceDomain(String proxyUrl) {
        if(StringUtils.isNotBlank(proxyUrl)){
            this.domain = proxyUrl;
        }else{
            this.domain = AppConstant.UNSPLASH_DOMAIN;
        }
        this.raw = this.raw.replace(AppConstant.UNSPLASH_DOMAIN, "");
        this.full = this.full.replace(AppConstant.UNSPLASH_DOMAIN, "");
        this.regular = this.regular.replace(AppConstant.UNSPLASH_DOMAIN, "");
        this.small = this.small.replace(AppConstant.UNSPLASH_DOMAIN, "");
        this.thumb = this.thumb.replace(AppConstant.UNSPLASH_DOMAIN, "");
    }
}

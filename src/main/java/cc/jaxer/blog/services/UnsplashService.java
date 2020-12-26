package cc.jaxer.blog.services;

import cc.jaxer.blog.common.ConfigCodeEnum;
import cc.jaxer.blog.entities.ConfigEntity;
import cc.jaxer.blog.mapper.ConfigMapper;
import cc.jaxer.blog.unsplash.Photo;
import cc.jaxer.blog.unsplash.SearchReq;
import cc.jaxer.blog.unsplash.SearchResp;
import cc.jaxer.blog.unsplash.Urls;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnsplashService {
    String apiUrl = "https://api.unsplash.com/";

    @Autowired
    private ConfigService configService;

    public SearchResp search(SearchReq searchReq) {
        // b857176babeceddbe3163cd7592dc24662642f4bf66e7869fc3694dda7300b45
        String appid =  configService.getConf(ConfigCodeEnum.unsplash_appid);
        //链式构建请求
        HttpResponse execute = HttpRequest.get(apiUrl + "search/photos")
                .header("Accept-Version", "v1")
                .header(Header.AUTHORIZATION, " Client-ID " + appid)//头信息，多个头信息多次调用此方法即可
                .form("query", searchReq.getQuery())//表单内容
                .form("page", searchReq.getPage())//表单内容
                .form("per_page", searchReq.getPer_page())//表单内容
                .timeout(10000)//超时，毫秒
                .execute();

        int status = execute.getStatus();
        if (status == HttpStatus.HTTP_OK) {
            String body = execute.body();
            SearchResp searchResp = JSONUtil.toBean(body, SearchResp.class);
            String proxyUrl = configService.getConf(ConfigCodeEnum.unsplash_proxy);
            for (Photo result : searchResp.getResults()) {
                Urls urls = result.getUrls();
                urls.replaceDomain(proxyUrl);
            }
            return searchResp;
        }
        return null;
    }
}

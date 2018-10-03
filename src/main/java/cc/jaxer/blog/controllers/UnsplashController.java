package cc.jaxer.blog.controllers;

import java.io.InputStream;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.jaxer.blog.common.NeedLogin;
import cc.jaxer.blog.mapper.ConfigMapper;

@RestController
public class UnsplashController
{
    @Autowired
    private ConfigMapper configMapper;

    private final String appid = "b857176babeceddbe3163cd7592dc24662642f4bf66e7869fc3694dda7300b45";

    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    @RequestMapping("/unsplash/search")
    @NeedLogin
    public void searchImage(String keyword,HttpServletResponse response) throws Exception{
        if(StringUtils.isEmpty(keyword)){
            return ;
        }
        URI uri = new URIBuilder()
        .setScheme("https")
        .setHost("api.unsplash.com")
        .setPath("/search/photos")
        .setParameter("query", keyword)
        .setParameter("per_page", "50")
        .build();

        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("Accept-Version", "v1");
        httpGet.addHeader("Authorization", "Client-ID " + appid);
        CloseableHttpResponse result = httpclient.execute(httpGet);
        HttpEntity entity = result.getEntity();
        InputStream content = entity.getContent();
        IOUtils.copy(content, response.getOutputStream());
    }
}

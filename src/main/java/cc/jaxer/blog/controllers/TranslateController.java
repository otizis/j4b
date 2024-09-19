package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.ConfigCodeEnum;
import cc.jaxer.blog.common.R;
import cc.jaxer.blog.entities.ReplyEntity;
import cc.jaxer.blog.services.ConfigService;
import com.aliyun.alimt20181012.models.TranslateGeneralRequest;
import com.aliyun.alimt20181012.models.TranslateGeneralResponse;
import com.aliyun.alimt20181012.models.TranslateGeneralResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/translate")
@Slf4j
public class TranslateController
{
    @Autowired
    private ConfigService configService;

    @Value("${alinls.setAppkey:}")
    public String appKey;

    @Value("${alinls.access-key-id:}")
    public String accessKeyId;

    @Value("${alinls.access-key-secret:}")
    public String accessKeySecret;

    public com.aliyun.alimt20181012.Client createClient()
    {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config().setAccessKeyId(
                accessKeyId).setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/alimt
        config.endpoint = "mt.aliyuncs.com";
        try
        {
            return new com.aliyun.alimt20181012.Client(config);
        }
        catch (Exception e)
        {
            log.error("", e);
        }
        return null;
    }

    @RequestMapping(path = {"/zh2en"})
    @ResponseBody
    public R zh2en(@RequestBody ReplyEntity reply,
                   @RequestHeader(value = "Referer", required = false) String referer)
    {
        if(StringUtils.isEmpty(referer) || StringUtils.isBlank(reply.getContent()))
        {
            return R.error();
        }
        String conf = configService.getConfDefaultCache(ConfigCodeEnum.referer_filter_conf, "");
        String[] refererFilterConf = conf.split(";");
        // 校验来源
        if (!StringUtils.containsAny(referer, refererFilterConf))
        {
            return R.error("501");
        }

        com.aliyun.alimt20181012.Client client = createClient();
        if (client == null)
        {
            return R.error("client is null");

        }
        TranslateGeneralRequest translateGeneralRequest = new TranslateGeneralRequest()
                .setFormatType("text")
                .setSourceLanguage("zh")
                .setTargetLanguage("en")
                .setSourceText(reply.getContent())
                .setScene("general");
        RuntimeOptions runtime = new RuntimeOptions();
        try
        {
            // 复制代码运行请自行打印 API 的返回值
            TranslateGeneralResponse translateGeneralResponse = client.translateGeneralWithOptions(
                    translateGeneralRequest,
                    runtime);
            TranslateGeneralResponseBody body = translateGeneralResponse.getBody();
            if(body.getCode() == 200){
                TranslateGeneralResponseBody.TranslateGeneralResponseBodyData data = body.getData();
                return R.ok().put("translated",data.getTranslated());
            }else{
                log.error(translateGeneralResponse.toString());
                return R.error(body.getMessage());
            }
        }
        catch (Exception _error)
        {
            log.error("translateGeneralWithOptions",_error);
        }

        return R.ok();
    }


}

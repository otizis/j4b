package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.AppConstant;
import cc.jaxer.blog.common.NeedLogin;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


@Controller
@RequestMapping("/audio")
@Slf4j
public class AudioController
{
    @Value("${fileupload.path}")
    private String nginxServerPath;

    // 地域ID，常量，固定值。
    public static final String REGIONID = "cn-shanghai";
    public static final String ENDPOINTNAME = "cn-shanghai";
    public static final String PRODUCT = "nls-filetrans";
    public static final String DOMAIN = "filetrans.cn-shanghai.aliyuncs.com";
    public static final String API_VERSION = "2018-08-17";
    public static final String POST_REQUEST_ACTION = "SubmitTask";

    // 请求参数
    public static final String KEY_APP_KEY = "appkey";
    public static final String KEY_FILE_LINK = "file_link";
    public static final String KEY_VERSION = "version";
    public static final String KEY_ENABLE_WORDS = "enable_words";
    // 是否开启智能分轨（开启智能分轨，即可在两方对话的语音情景下，依据每句话识别结果中的ChannelId，判断该句话的发言人为哪一方。
    // 通常先发言一方ChannelId为0，8k双声道开启分轨后默认为2个人，声道channel0和channel1就是音轨编号）。
    public static final String KEY_AUTO_SPLIT = "auto_split";
    // 大于16 kHz采样率的音频是否进行自动降采样（降为16 kHz），默认为false，开启时需要设置version为4.0。
    public static final String KEY_SR_ADAPTIVE = "enable_sample_rate_adaptive";
    //ITN（逆文本inverse text normalization）中文数字转换阿拉伯数字。设置为True时，中文数字将转为阿拉伯数字输出，默认值：False。
    public static final String KEY_NUMBER_TEXT = "enable_inverse_text_normalization";
    // 响应参数
    public static final String KEY_TASK = "Task";
    public static final String KEY_TASK_ID = "TaskId";
    public static final String KEY_STATUS_TEXT = "StatusText";
    // 状态值
    public static final String STATUS_SUCCESS = "SUCCESS";


    @Value("${alinls.setAppkey:}")
    public String appKey;

    @Value("${alinls.access-key-id:}")
    public String accessKeyId;

    @Value("${alinls.access-key-secret:}")
    public String accessKeySecret;

    // 阿里云鉴权client
    IAcsClient client;


    @RequestMapping(path = {"/tran.html"})
    @NeedLogin(isPage = true)
    public String audioTran(@RequestParam(value = "path",required = false) String path,
                            HttpServletRequest servletRequest,
                           ModelMap modelMap )
    {
        String serverName = servletRequest.getServerName();
        int serverPort = servletRequest.getServerPort();
        System.out.println(serverName+":"+serverPort);

        modelMap.put("audioUrl", path);
        File file = new File(nginxServerPath + path);
        if(!file.exists()){
            return "redirect:/";
        }

        File resultFile = new File(nginxServerPath + path + "result.json");
        if(resultFile.exists()){
            modelMap.put("message", "请查看");
            modelMap.put("resultJson", path + "result.json");
        }else{
            //不存在查询一次
            File stateFile = new File(nginxServerPath + path + ".taskId");
            if(stateFile.exists())
            {
                String taskId = FileUtil.readString(stateFile, StandardCharsets.UTF_8);
                modelMap.put("taskId", taskId);
                if(StringUtils.isNotEmpty(taskId))
                {
                    JSONObject result = this.query(taskId);
                    String statusText = result.getStr("StatusText");
                    if ("SUCCESS".equals(statusText))
                    {
                        FileUtil.writeString(result.getStr("Result"), resultFile, StandardCharsets.UTF_8);
                        modelMap.put("message", "成功请刷新");
                    }else if("RUNNING".equals(statusText))
                    {
                        modelMap.put("message", "进行中请等待");
                    }
                }
            }else{
                // 发起
                String taskId = this.start("http://"+serverName+":"+serverPort+ AppConstant.OSS_PATH + "/" + path);
                FileUtil.writeString(taskId, stateFile, StandardCharsets.UTF_8);
                modelMap.put("taskId", taskId);
            }
        }
        return "admin/audioTran";
    }


    public JSONObject query(String taskId){
        CommonRequest getRequest = new CommonRequest();
        getRequest.setDomain(DOMAIN);   // 设置域名，固定值。
        getRequest.setVersion(API_VERSION);         // 设置中国站的版本号。
        getRequest.setAction("GetTaskResult");           // 设置action，固定值。
        getRequest.setProduct(PRODUCT);          // 设置产品名称，固定值。
        getRequest.putQueryParameter("TaskId", taskId);  // 设置任务ID为查询参数。
        getRequest.setMethod(MethodType.GET);

        CommonResponse getResponse = null;
        try
        {
            getResponse = client.getCommonResponse(getRequest);
        }
        catch (ClientException e)
        {
            log.error("",e);
            return null;
        }
        if (getResponse.getHttpStatus() != 200) {
            log.error("识别结果查询请求失败，Http错误码： {}", getResponse.getHttpStatus());
            log.error("识别结果查询请求失败：{} ", getResponse.getData());
            return null;
        }
        log.info("识别查询结果：{}" , getResponse.getData());

        return JSONUtil.parseObj(getResponse.getData());
    }

    public String start(String fileLink){
        // 设置endpoint
        try {
            DefaultProfile.addEndpoint(ENDPOINTNAME, REGIONID, PRODUCT, DOMAIN);
        } catch (ClientException e) {
            log.error("",e);
        }
        // 创建DefaultAcsClient实例并初始化
        DefaultProfile profile = DefaultProfile.getProfile(REGIONID, accessKeyId, accessKeySecret);
        this.client = new DefaultAcsClient(profile);

        /**
         * 1. 创建CommonRequest，设置请求参数。
         */
        CommonRequest postRequest = new CommonRequest();
        // 设置域名
        postRequest.setDomain(DOMAIN);
        // 设置API的版本号，格式为YYYY-MM-DD。
        postRequest.setVersion(API_VERSION);
        // 设置action
        postRequest.setAction(POST_REQUEST_ACTION);
        // 设置产品名称
        postRequest.setProduct(PRODUCT);
        /**
         * 2. 设置录音文件识别请求参数，以JSON字符串的格式设置到请求Body中。
         */

        HashMap<String ,Object> taskObject = new HashMap<String ,Object>();
        // 设置appkey
        taskObject.put(KEY_APP_KEY, appKey);
        // 设置音频文件访问链接
        taskObject.put(KEY_FILE_LINK, fileLink);
        // 新接入请使用4.0版本，已接入（默认2.0）如需维持现状，请注释掉该参数设置。
        taskObject.put(KEY_VERSION, "4.0");
        // 设置是否输出词信息，默认为false，开启时需要设置version为4.0及以上。
        taskObject.put(KEY_ENABLE_WORDS, false);
        //        taskObject.put("auto_split",false);

        taskObject.put("first_channel_only", true);

        /**
         * 是否开启智能分轨（开启智能分轨，即可在两方对话的语音情景下，依据每句话识别结果中的ChannelId，
         * 判断该句话的发言人为哪一方。通常先发言一方ChannelId为0，8k双声道开启分轨后默认为2个人，
         * 声道channel0和channel1就是音轨编号）。
         */
        taskObject.put(KEY_AUTO_SPLIT, true);
        taskObject.put(KEY_SR_ADAPTIVE, true);
        taskObject.put(KEY_NUMBER_TEXT, true);

        String task = JSONUtil.toJsonStr(taskObject);
        log.info("录音转写任务开始:{}",task);
        // 设置以上JSON字符串为Body参数。
        postRequest.putBodyParameter(KEY_TASK, task);
        // 设置为POST方式的请求。
        postRequest.setMethod(MethodType.POST);
        /**
         * 3. 提交录音文件识别请求，获取录音文件识别请求任务的ID，以供识别结果查询使用。
         */
        String taskId = null;
        try {
            CommonResponse postResponse = client.getCommonResponse(postRequest);
            log.info("提交录音文件识别请求的响应:{}",postResponse.getData());
            if (postResponse.getHttpStatus() == 200) {
                JSONObject parse = JSONUtil.parseObj(postResponse.getData());

                String statusText = parse.getStr(KEY_STATUS_TEXT);
                if (STATUS_SUCCESS.equals(statusText)) {
                    taskId = parse.getStr(KEY_TASK_ID);
                }
            }
        } catch (ClientException e) {
            log.error("",e);
        }
        return taskId;
    }

}

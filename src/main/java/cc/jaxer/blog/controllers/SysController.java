package cc.jaxer.blog.controllers;

import cc.jaxer.blog.common.J4bUtils;
import cc.jaxer.blog.common.R;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class SysController
{
    @RequestMapping("/sys/login")
    public R login(@RequestBody HashMap<String, String> request)
    {
        String access = request.get("access");
        if (!"j4bj4b".equals(access))
        {
            R.error();
        }
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, 1);
        J4bUtils.addToken(token, now.getTimeInMillis());
        return R.ok("token",token);
    }
}

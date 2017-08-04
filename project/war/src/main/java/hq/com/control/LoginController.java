package hq.com.control;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.vo.OutParam;
import hq.com.enums.SystemCodeEnum;
import hq.com.exception.DisabledAccountException;
import hq.com.exception.IllegalOptionException;
import hq.com.exception.IncorrectCredentialsException;
import hq.com.exception.UnknownAccountException;
import hq.com.jpa.dto.ShiroUserDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 2:30 2017/5/29 0029
 * @Modified By
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController {
    public static Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private Producer captchaProducer = null;

    /**
     * 登录验证
     *
     * @param name
     * @param password
     * @return
     */
    @RequestMapping(value = "/login",
            method = {RequestMethod.POST, RequestMethod.GET},
            headers = "Accept=application/x-www-form-urlencoded;charset=utf-8",
            consumes = "application/x-www-form-urlencoded;charset=utf-8",
            produces = "application/json;charset=utf-8")
    @ResponseBody
    public OutParam login(@RequestParam("name") String name, @RequestParam("password") String password, HttpServletRequest request) throws IllegalOptionException {
        try {
            //验证码验证
            System.out.println(request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY));
            UsernamePasswordToken np = new UsernamePasswordToken(name, password);
            SecurityUtils.getSubject().login(np);
            ShiroUserDto sud = (ShiroUserDto) SecurityUtils.getSubject().getSession().getAttribute("user");
            OutParam op = new OutParam();
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
            op.setContent(sud);
            return op;
        } catch (IncorrectCredentialsException e) {
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_USER_NMAE_PASSWORD_ERROR);
        } catch (DisabledAccountException e) {
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_USER_DISABLED_ACCOUNT);
        } catch (UnknownAccountException e) {
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_USER_UNKNOWN_ACCOUNT);
        } catch (ExcessiveAttemptsException e) {
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_USER_EXCESSIVE_ATTEMPTS);
        }
    }

    @RequestMapping(value = "/logout",
            method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public OutParam logout() throws IllegalOptionException {
        try {
            SecurityUtils.getSubject().logout();
            OutParam op = new OutParam();
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
            return op;
        } catch (Exception e) {
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 验证码
     */
    @RequestMapping(value = "/codec",
            method = RequestMethod.GET)
    @ResponseBody
    public void codec(@RequestParam("sid") String sessionId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();//修改sid存储redis缓存中
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return;
    }


    @Override
    public Object decodeRequest(HttpServletRequest request) {
        return null;
    }

    @Override
    public Object decodeRequest(Map<String, Map<String, Object>> map) {
        return null;
    }

    @Override
    public String encodeResponse(Object obj) {
        return JSONObject.toJSONString(obj);
    }
}

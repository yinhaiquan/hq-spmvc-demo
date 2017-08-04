package hq.com.handler;

import hq.com.aop.ctx.SpringApplicationContext;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.vo.OutParam;
import hq.com.enums.SystemCodeEnum;
import hq.com.exception.IllegalOptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @title : 全局异常捕捉处理
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/24 18:38 星期三
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    public static Logger log = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);
    public static final String DEFAULT_ERROR_VIEW = "../error.jsp";

    /**
     * 返回系统错误状态码
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = IllegalOptionException.class)
    @ResponseBody
    public OutParam systemErrorHandler(Exception e) throws Exception {
        log.info("IllegalOptionException错误日志:{}", e.getMessage());
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        OutParam op = new OutParam();
        op.setCode(SystemCodeEnum.SYSTEM_ERROR.getCode());
        op.setDesc(SystemCodeEnum.SYSTEM_ERROR.getDesc());
        if (e instanceof IllegalOptionException){
           op.setCode(((IllegalOptionException) e).getS_().getCode());
           op.setDesc(((IllegalOptionException) e).getS_().getDesc());
        }
        return op;
    }

    /**
     * 返回系统错误页面
     *
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = IllegalArgumentsException.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        log.info("IllegalArgumentsException错误日志:{}",e.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        if (e instanceof IllegalArgumentsException) {
            IllegalArgumentsException iae = (IllegalArgumentsException) e;
            modelAndView.addObject("name", iae.getTitle());
            modelAndView.addObject("msg", iae.getMessage());
        } else {
            modelAndView.addObject("name", "exception");
            modelAndView.addObject("msg", e.getMessage());
        }
        modelAndView.addObject("title", SpringApplicationContext.getMessage("error.title"));
        modelAndView.addObject("theme", SpringApplicationContext.getMessage("error.theme"));
        modelAndView.addObject("errorname", SpringApplicationContext.getMessage("error.name"));
        modelAndView.addObject("desc", SpringApplicationContext.getMessage("error.desc"));
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);
        return modelAndView;
    }
}

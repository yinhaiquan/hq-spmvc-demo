package hq.com.moudle.vo;

import hq.com.aop.annotation.Bean;
import hq.com.aop.annotation.Parameter;
import hq.com.aop.aopenum.ParamType;
import hq.com.aop.vo.InParam;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/3 12:38 星期四
 */
@Bean
@Component("adminInfo")
public class AdminInParam extends InParam implements Serializable {
    private static final long serialVersionUID = 838383809283923835L;

    @Parameter(value = "page", type = ParamType.INT, desc = "page")
    private int page;
    @Parameter(value = "pageSize", type = ParamType.INT, desc = "pageSize")
    private int pageSize;

    public AdminInParam() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

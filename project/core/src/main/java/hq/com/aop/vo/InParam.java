package hq.com.aop.vo;

import hq.com.aop.annotation.Parameter;
import hq.com.aop.aopenum.ParamType;

import java.io.Serializable;

/**
 * @title : 入参
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/23 10:31 星期二
 */
public class InParam implements Param,Serializable {
    private static final long serialVersionUID = -5971063176377856473L;
    @Parameter(value = "page", type = ParamType.INT, desc = "页码")
    private int page;
    @Parameter(value = "pageSize", type = ParamType.INT, desc = "页面大小")
    private int pageSize;
    @Parameter(value = "order", type = ParamType.STRING, desc = "排序字段")
    private String order;
    @Parameter(value = "orderType", type = ParamType.STRING, desc = "排序类型")
    private String orderType;

    public Object decode() {
        return null;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderType() {
        return orderType.equalsIgnoreCase("asc")?"asc":"desc";
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "InParam{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", order='" + order + '\'' +
                ", orderType='" + orderType + '\'' +
                '}';
    }
}

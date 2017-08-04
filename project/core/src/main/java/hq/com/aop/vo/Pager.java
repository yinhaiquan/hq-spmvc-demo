package hq.com.aop.vo;

import java.io.Serializable;
import java.util.Collection;

/**
 * @title : 分页信息实体类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/3 10:44 星期四
 */
public class Pager implements Serializable {
    private static final long serialVersionUID = 5047449429591066308L;
    /*总页数*/
    private int pages;
    /*页面大小*/
    private int pageSize;
    /*数据列表*/
    private Collection<?> rows;
    /*总数*/
    private long total;

    public Pager() {
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Collection<?> getRows() {
        return rows;
    }

    public void setRows(Collection<?> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

package hq.com.moudle.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import hq.com.aop.vo.OutParam;
import hq.com.aop.vo.Pager;
import hq.com.enums.SystemCodeEnum;
import hq.com.moudle.dao.AdminDao;
import hq.com.moudle.po.Admin;
import hq.com.moudle.service.AdminService;
import hq.com.moudle.vo.AdminInParam;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/3 12:40 星期四
 */
@Service("adminSvc")
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao ad;

    @Override
    public OutParam findUsers(AdminInParam adminInParam) {
        Page page = PageHelper.startPage(adminInParam.getPage(), adminInParam.getPageSize(), true);
        List<Admin> list = ad.findUsers(new HashedMap());
        Pager pager = new Pager();
        pager.setPages(page.getPages());
        pager.setPageSize(adminInParam.getPageSize());
        pager.setRows(list);
        pager.setTotal(page.getTotal());
        OutParam op = new OutParam();
        op.setContent(pager);
        op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
        op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        return op;
    }
}

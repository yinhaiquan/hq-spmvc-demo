package hq.com;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import hq.com.moudle.dao.AdminDao;
import hq.com.moudle.po.Admin;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 8:04 2017/5/29 0029
 * @Modified By
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-mybatis2.xml"})
public class DataBaseTest {
    @Autowired
    private AdminDao ad;

    @Test
    public void testT() throws SQLException {
        Admin admin = ad.find(1);
        System.out.println(admin);
        Page page = PageHelper.startPage(-1, 10, true);
//        page.setOrderBy("n_id desc");
        page.setOrderBy("n_id asc");
        List<Admin> list = ad.findUsers(new HashedMap());
        System.out.println(page.getPages());
        System.out.println(list.get(0).getId());
        System.out.println(page.getTotal());
        System.out.println(list.size());
    }
}

package hq.com;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import hq.com.aop.annotation.TableField;
import hq.com.auth.dao.AuthDao;
import hq.com.auth.po.InterfaceAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/7 17:04 星期四
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-mybatis2.xml"})
public class AuthTest {

    @Resource
    private AuthDao authDao;

    @Test
    public void testFindInterfaceAuthList(){
        Page page = PageHelper.startPage(1, 10, true);
        page.setOrderBy("n_id desc");
        System.out.println(page);
        List<InterfaceAuth> list = authDao.findInterfaceAuthList(null,null,null);
        for (InterfaceAuth ifa : list){
            System.out.println(ifa);
        }
    }

    @Test
    public void testUpdate() throws NoSuchFieldException {
        String field = InterfaceAuth.class.getDeclaredField("isLogin").getAnnotation(TableField.class).value();
        System.out.println(field);
//        authDao.updateISign(true,1);
    }
}

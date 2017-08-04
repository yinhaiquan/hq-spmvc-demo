//package hq.com;
//
//import hq.com.base.Pager;
//import hq.com.jpa.dao.ShiroUserDao;
//import hq.com.jpa.po.ShiroUser;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Describle:
// * @Author: YinHq
// * @Date: Created By 上午 10:48 2017/5/30 0030
// * @Modified By
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:spring/spring-data-jpa.xml"})
//public class UserTest {
//
//    @Resource
//    private ShiroUserDao iUserDao;
//
//
//    @Test
//    public void testFindUsers(){
//        ShiroUser u = iUserDao.findUserInfo("123","123456");
//        System.out.println(123);
//        System.out.println(u);
//    }
//
//    @Test
//    public void testSelectList(){
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("name","123");
//        Pager pager = new Pager(1,2);
//        pager.setOrder("desc");
//        pager.setSort("createTime");
//        Pager pager2 = iUserDao.selectShiroUserList(map,pager);
//        System.out.println(pager2.getData().size());
//        System.out.println(pager2.getTotalResults());
//    }
//
//    @Test
//    public void testSave(){
//        ShiroUser su = new ShiroUser();
//        su.setAdress("sdfds");
//        su.setBirthday(new Date());
//        su.setCreateTime(new Date());
//        su.setName("345");
//        su.setSex(1);
//        su.setStatus(0);
//        su.setPassword("23456");
//        su.setPhoneNumber("2344");
//        su = iUserDao.saveAndFlush(su);
//        System.out.println(su.getId());
//    }
//    @Test
//    public void testupdate(){
//        iUserDao.updateUserStatus(2,1);
//    }
//
//}

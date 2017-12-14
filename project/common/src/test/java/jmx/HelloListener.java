package jmx;

import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/14 16:20 星期四
 */
public class HelloListener implements NotificationListener {


    @Override
    public void handleNotification(Notification notification, Object handback) {
        if (handback instanceof Hello){
            Hello hello = (Hello) handback;
            System.out.println("JMX消息通知：");
            hello.showName(notification.getMessage());
        }
    }
}

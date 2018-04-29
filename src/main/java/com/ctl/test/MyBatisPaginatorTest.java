package com.ctl.test;

import com.ctl.test.mapper.PersonMapper;
import com.ctl.test.po.Person;
import com.ctl.test.po.PersonExample;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: MyBatisPaginatorTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.ctl.com</p>
 *
 * @author ctl
 * @version 1.0
 * @date 2018-04-26 10:29
 */
public class MyBatisPaginatorTest {
    public static Logger logger = LoggerFactory.getLogger(MyBatisPaginatorTest.class);
    public static void main(String[] args) {
        ApplicationContext context=  new ClassPathXmlApplicationContext("spring/applicationContext.xml");//名字只可以是applicationContext.xml
        String pidName= ManagementFactory.getRuntimeMXBean().getName();
        logger.info("线程MyBatisPaginatorTest pidName:"+pidName);
        String[] beanNames = context.getBeanDefinitionNames();
        int allBeansCount = context.getBeanDefinitionCount();
        logger.info("所有beans的数量是：" + allBeansCount);
        for (String beanName : beanNames) {
            Class<?> beanType = context.getType(beanName);
            Package beanPackage = beanType.getPackage();
            Object bean = context.getBean(beanName);
            logger.info("BeanName:" + beanName);
            logger.info("Bean的类型：" + beanType);
            logger.info("Bean所在的包：" + beanPackage);
            logger.info("\r\n");
        }
        PersonMapper mapper= (PersonMapper)context.getBean("personMapper");
        Map<String,Object> dataMap=new HashMap<>();
        PersonExample example=new PersonExample();
        logger.info( ""+mapper.countByExample(example));
        logger.info(""+mapper.selectByParams(dataMap,new PageBounds(2,10)));
        PageList<Person> peopleList = (PageList<Person>) mapper.selectByParams(dataMap, new PageBounds(2, 10));
        Paginator paginator = peopleList.getPaginator();
        logger.info("totalPages:"+paginator.getTotalPages());
        logger.info("totalCount:"+paginator.getTotalCount());
        logger.info("page:"+paginator.getPage());
        logger.info(JSONArray.fromObject(peopleList).toString());
    }
}

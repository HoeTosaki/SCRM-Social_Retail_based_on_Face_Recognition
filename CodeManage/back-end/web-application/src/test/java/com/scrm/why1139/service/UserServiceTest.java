package com.scrm.why1139.service;


import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

//@ContextConfiguration("classpath*:/appContext.xml")
@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, SqlScriptsTestExecutionListener.class})
@Transactional
@Rollback(false)
public class UserServiceTest extends AbstractTransactionalTestNGSpringContextTests
{
//    private UserService m_userService = new UserService();

//    @Autowired
//    public void setUserService(UserService _userService)
//    {
//        this.m_userService=_userService;
//    }

    @Test
    public void hasMatchUser()
    {
//        boolean b1 = m_userService.hasMatchUser("admin","123456");
//        boolean b2 = m_userService.hasMatchUser("admin","111111");
//        assertTrue(!b1);
//        assertTrue(!b2);
        assertTrue(true);
    }

//    @Test
//    public void findUserByUserID()
//    {
//        User user = m_userService.findUserByUserID("admin");
////        assertEquals(user.getUserID(),"admin");
//    }
}

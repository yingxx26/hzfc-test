package com.hzfc.management.jsbsb;

import com.hzfc.management.jsbsb.modules.test.service.TestService;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User2;
import com.hzfc.management.jsbsb.modules.testShiwu.service.TestUser2Service;
import com.hzfc.management.jsbsb.modules.testShiwu.service.TestUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HzfcApplicationTests {
    @Autowired
    private TestService testService;

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private TestUser2Service testUser2Service;

    @Test
    public void contextLoads() throws ScriptException {

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String value = "";

        String formulaValue = "14/7*100+2";
        value = engine.eval(formulaValue).toString();
        System.out.println();

    }

    @Test
    public void testUser() throws IOException {

        User user = new User();
        user.setName("user1");

        testUserService.testSaveUser(user);


    }

    @Test
    public void testUser2() throws IOException {


        User2 user2 = new User2();
        user2.setName("user2");
        testUser2Service.testSaveUser2(user2);

    }

    @Test
    public void testsw1() throws IOException {

        User user = new User();
        user.setName("user1");

        testUserService.testSaveUser(user);

    }

}

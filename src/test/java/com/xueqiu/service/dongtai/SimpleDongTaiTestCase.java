package com.xueqiu.service.dongtai;

import com.xueqiu.service.com.xueqiu.service.login.LoginTestCase;
import com.xueqiu.service.com.xueqiu.service.tags.Smoke;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleDongTaiTestCase extends LoginTestCase {
    @BeforeClass
    public static void beforeClass(){

        System.out.println("before class com.xueqiu.service.dongtai.SimpleDongTaiTestCase");
    }

    @Before
    public void before(){
        System.out.println("before");
    }

    @After
    public void after(){
        System.out.println("after");
    }
    @Test
    public void testB(){
        System.out.println("testB");

    }

    @Category(Smoke.class)
    @Test
    public void test1(){
        System.out.println("test1");

    }
    @Category(Smoke.class)
    @Test
    public void test2(){
        System.out.println("test2");

    }

    @Test
    public void testC(){
        System.out.println("testC");

    }

    @Category(Smoke.class)
    @Test
    public void testA(){
        System.out.println("testA");
        assertEquals("1 vs 2",1, 2);


    }

    @AfterClass
    public static void afterClass(){
        System.out.println("after class SimpleTestCase");
    }
}

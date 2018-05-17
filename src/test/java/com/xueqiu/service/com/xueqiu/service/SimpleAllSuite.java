package com.xueqiu.service.com.xueqiu.service;

import com.xueqiu.service.dongtai.SimpleDongTaiTestCase;
import com.xueqiu.service.zixuan.SimpleZiXuanTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SimpleDongTaiTestCase.class,
        SimpleZiXuanTestCase.class
})
public class SimpleAllSuite {
}

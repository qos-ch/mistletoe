package ch.qos.mistletoe.suiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({MyTest1.class, MyTest2.class})
public class MyCollection {

}

package foo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ATest {

  @Test
  public void a0() {
    assertEquals(4, 2 + 2);
  }

  @Test
  public void a1() {
    assertTrue("2 and 2", 2 + 2 == 4);
  }
}

package example;

import org.testng.Assert;
import org.testng.annotations.Test;

public class QuickSortTest {
  @Test
  public void testSort() throws Exception {
    Assert.assertEquals(QuickSort.sort(new int[]{}), new int[]{});
    Assert.assertEquals(QuickSort.sort(new int[]{1}), new int[]{1});
    Assert.assertEquals(QuickSort.sort(new int[]{1, 2}), new int[]{1, 2});
    Assert.assertEquals(QuickSort.sort(new int[]{1, 2, 3}), new int[]{1, 2, 3});
    Assert.assertEquals(QuickSort.sort(new int[]{3, 2, 1}), new int[]{1, 2, 3});
    Assert.assertEquals(QuickSort.sort(new int[]{1, 5, 3, 4, 8, 6}), new int[]{1, 3, 4, 5, 6, 8});
    Assert.assertEquals(QuickSort.sort(new int[]{1, 5, 3, 4, 8, 6, 0}), new int[]{0, 1, 3, 4, 5, 6, 8});
  }

  @Test(expectedExceptions = AssertionError.class)
  public void testSortFail() throws Exception {
    Assert.assertEquals(QuickSort.sort(new int[]{1, 5, 3, 4, 8, 6, 0}), new int[]{0, 1, 3, 4, 5, 8, 6});
  }
}

package example;

/**
 * Created with IntelliJ IDEA.
 * Date: 10/6/14  3:41 PM
 */
public class TwitterApiHelperTest {
  @org.testng.annotations.Test(enabled = false)
  public void testSetUpTwitterApi() {
    TwitterApiHelper.ClientWrapper client = null;
    try {
      client = TwitterApiHelper.setUpTwitterApi();
      client.connect();
      int i = 100;
      while (!client.isDone()) {
        String msg = client.next();
        System.out.print(msg);
        if (i-- == 0) client.stop();
      }
    } catch (InterruptedException ie) {
      if (client != null) client.stop();
    }
  }
}

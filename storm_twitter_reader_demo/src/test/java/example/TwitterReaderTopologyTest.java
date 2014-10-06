package example;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.utils.Utils;
import org.testng.annotations.Test;

public class TwitterReaderTopologyTest {
  @Test
  public void testRunTridentTopology() throws Exception {
    LocalCluster localCluster = new LocalCluster();
    localCluster.submitTopology("twitter_demo_topo", new Config(), TwitterReaderTopology.getTridentTopology());
    Utils.sleep(30 * 1000);
    localCluster.killTopology("twitter_demo_topo");
  }
}

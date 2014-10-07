package example;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.utils.Utils;
import com.google.common.collect.Lists;
import org.testng.annotations.Test;

public class TwitterReaderTopologyTest {
  @Test
  public void testRunTridentTopology() throws Exception {
    LocalDRPC drpc = new LocalDRPC();
    LocalCluster localCluster = new LocalCluster();

    Config config = new Config();
    config.setDebug(false);
    config.setMaxSpoutPending(20);
    //config.put(Config.DRPC_SERVERS, Lists.newArrayList("localhost"));
    //config.put(Config.STORM_CLUSTER_MODE, new String("distributed"));

    localCluster.submitTopology("twitter_demo_topo", config, TwitterReaderTopology.getTridentTopology(drpc));

    Utils.sleep(30 * 1000);

    Utils.sleep(5 * 1000);
    System.out.println("============================ DRPC RESULT for count_all: " + drpc.execute("count_all", "location"));

    Utils.sleep(5 * 1000);
    System.out.println("============================ DRPC RESULT for count_locations: " + drpc.execute("count_locations", "location"));

    localCluster.killTopology("twitter_demo_topo");

    localCluster.shutdown();
  }
}

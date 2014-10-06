package example;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.IRichSpout;
import backtype.storm.tuple.Fields;
import storm.trident.Stream;
import storm.trident.TridentTopology;

public class TwitterReaderTopology {

  public static void main(String[] args) {
    StormTopology topology = getTridentTopology();

    Config config = new Config();
    config.setDebug(false);
//    config.put(Config.DRPC_SERVERS, Lists.newArrayList("localhost"));
    config.put(Config.STORM_CLUSTER_MODE, new String("distributed"));
    try {
      StormSubmitter.submitTopology("twitter_demo_topo", config, topology);
    } catch (AlreadyAliveException e) {
      System.err.print(e);
    } catch (InvalidTopologyException e) {
      System.err.print(e);
    }

  }

  public static StormTopology getTridentTopology() {
    TridentTopology topology = new TridentTopology();
    IRichSpout spout = new TwitterSpout();

    Stream stream = topology
        .newStream("tweets", spout)
        .each(new Fields("followers_count", "friends_count", "statuses_count"), new BotFilter())
        .each(new Fields("user_name", "location", "tweet_text"), new PrintTextFilter())
        .parallelismHint(8);
    return topology.build();
  }
}

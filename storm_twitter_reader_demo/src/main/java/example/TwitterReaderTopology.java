package example;

import backtype.storm.Config;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.IRichSpout;
import backtype.storm.tuple.Fields;
import com.google.common.collect.Lists;
import storm.trident.Stream;
import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.*;
import storm.trident.testing.MemoryMapState;
import storm.trident.testing.Split;

public class TwitterReaderTopology {

  public static void main(String[] args) {
    StormTopology topology = getTridentTopology(null);

    Config config = new Config();
    config.setDebug(false);
    config.put(Config.DRPC_SERVERS, Lists.newArrayList("localhost"));
    config.put(Config.STORM_CLUSTER_MODE, new String("distributed"));
    try {
      StormSubmitter.submitTopology("twitter_demo_topo", config, topology);
    } catch (AlreadyAliveException e) {
      System.err.print(e);
    } catch (InvalidTopologyException e) {
      System.err.print(e);
    }
  }

  public static StormTopology getTridentTopology(LocalDRPC drpc) {
    TridentTopology topology = new TridentTopology();
    IRichSpout spout = new TwitterSpout();

    Fields botFilterFields = new Fields("followers_count", "friends_count", "statuses_count");
    Fields tweetFields = new Fields("user_name", "location", "tweet_text");
    Fields countFields = new Fields("count");
    Fields locationFields = new Fields("location");

    Stream stream = topology
        .newStream("tweets", spout)
        .each(botFilterFields, new BotFilter())
        .project(tweetFields)
        .each(tweetFields, new PrintTextFilter())
        .parallelismHint(2);

    //LRUMemoryMapState.Factory stateFactory = new LRUMemoryMapState.Factory(1000);

    TridentState state = stream
        .groupBy(locationFields)
        .persistentAggregate(new MemoryMapState.Factory(), new Count(), countFields);
    //.persistentAggregate(stateFactory, new Count(), countFields);

//    topology.newDRPCStream("count_all", drpc)
//        .each(new Fields("args"), new Split(), new Fields("location")) .groupBy(new Fields("location"))
//        .stateQuery(state, new Fields("location"), new MapGet(), new Fields("count"))
//        .each(new Fields("count"), new FilterNull())
//        .aggregate(new Fields("count"), new Sum(), new Fields("sum"));

    topology.newDRPCStream("count_all", drpc)
        .stateQuery(state, new Fields("args"), new TupleCollectionGet(), locationFields);

//    topology.newDRPCStream("count_locations", drpc)
//        .stateQuery(state, new Fields("args"), new TupleCollectionGet(), stateFields)
//        .groupBy(new Fields("location"))
//        .aggregate(new Fields("count"), new Sum(), new Fields("location_sum"))
//        .project(new Fields("location", "location_sum"));

    topology.newDRPCStream("count_locations", drpc)
        .each(new Fields("args"), new Split(), locationFields)
        .groupBy(locationFields)
        .stateQuery(state, locationFields, new MapGet(), countFields)
        .each(countFields, new FilterNull())
        .aggregate(countFields, new Sum(), new Fields("sum"));

    return topology.build();
  }
}

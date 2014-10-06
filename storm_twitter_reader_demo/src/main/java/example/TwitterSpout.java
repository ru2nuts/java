package example;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Date: 10/6/14  4:17 PM
 */
public class TwitterSpout extends BaseRichSpout {

  private TwitterApiHelper.ClientWrapper client = null;
  private SpoutOutputCollector spoutOutputCollector;

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declare(new Fields(
        "user_name",
        "location",
        "tweet_text",
        "followers_count",
        "friends_count",
        "statuses_count"));
  }

  @Override
  public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
    try {
      this.spoutOutputCollector = spoutOutputCollector;
      client = TwitterApiHelper.setUpTwitterApi();
      client.connect();
    } catch (InterruptedException e) {

    }
  }

  @Override
  public void nextTuple() {
    while (!client.isDone()) {
      try {
        String msg = client.next();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(msg);
        JSONObject user = (JSONObject) obj.get("user");
        Long followersCount = (Long) user.get("followers_count");
        Long friendsCount = (Long) user.get("friends_count");
        Long statusesCount = (Long) user.get("statuses_count");
        String userName = (String) user.get("name");
        String location = (String) user.get("location");
        String text = (String) obj.get("text");
        spoutOutputCollector.emit(new Values(userName, location, text, followersCount, friendsCount, statusesCount));
      } catch (ParseException e) {
        spoutOutputCollector.reportError(e);
      } catch (InterruptedException e) {
        spoutOutputCollector.reportError(new IllegalStateException("Interrupted while waiting for next item from Twitter API", e));
      }
    }
  }

  @Override
  public void close() {
    super.close();
    if (client != null) client.stop();
  }
}
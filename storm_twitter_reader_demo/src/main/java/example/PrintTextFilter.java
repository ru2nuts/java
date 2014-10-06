package example;

import org.apache.commons.lang.StringUtils;
import storm.trident.operation.Filter;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

import java.util.Map;

/**
 * Just print couple values.
 */
public class PrintTextFilter implements Filter {
  @Override
  public boolean isKeep(TridentTuple objects) {
    System.out.println(
        ">>>" + objects.getStringByField("user_name") +
        "(from" + objects.getStringByField("location") +
        ") says:" + StringUtils.substring(objects.getStringByField("tweet_text"), 0, 100));
    return true;
  }

  @Override
  public void prepare(Map map, TridentOperationContext tridentOperationContext) {
  }

  @Override
  public void cleanup() {
  }
}

package example;

import storm.trident.operation.Filter;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

import java.util.Map;

/**
 * Very naive bot filter.
 */
public class BotFilter implements Filter {
  @Override
  public boolean isKeep(TridentTuple objects) {
    return (objects.getLongByField("followers_count") > 3 &&
        objects.getLongByField("friends_count") > 3 &&
        objects.getLongByField("statuses_count") > 25);
  }

  @Override
  public void prepare(Map map, TridentOperationContext tridentOperationContext) {
  }

  @Override
  public void cleanup() {
  }
}

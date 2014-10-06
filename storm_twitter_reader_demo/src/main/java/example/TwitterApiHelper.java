package example;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * Date: 10/6/14  3:15 PM
 */
public class TwitterApiHelper {

  public static ClientWrapper setUpTwitterApi() throws InterruptedException {
    BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
    BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
    Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
    StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
// Optional: set up some followings and track terms
    //List<Long> followings = Lists.newArrayList(1234L, 566788L);
    List<String> terms = Lists.newArrayList("health", "boston");
    //hosebirdEndpoint.followings(followings);
    hosebirdEndpoint.trackTerms(terms);

// These secrets should be read from a config file
    Authentication hosebirdAuth = new OAuth1("x8FU1WT4FKoHPcycqVPE4v97y", "qitSZ4XWRYLq8saP2oJiChrGwkiS89za8PSlHfSSH80ftwrceQ", "703789069-jpvK1G7OVPUXPb7bKmG0Y6w6KuofUxQEcfk1enH1", "WQBd40tYHglS7F5jSup7f9wwfd76AwtZwNRV79IOdbAaX");

    ClientBuilder builder = new ClientBuilder()
        .name("Hosebird-Client-ru2nuts")                              // optional: mainly for the logs
        .hosts(hosebirdHosts)
        .authentication(hosebirdAuth)
        .endpoint(hosebirdEndpoint)
        .processor(new StringDelimitedProcessor(msgQueue))
        .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

    Client hosebirdClient = builder.build();

    return new ClientWrapper(hosebirdClient, msgQueue);
  }


  public static class ClientWrapper {
    private final Client client;
    private final BlockingQueue<String> queue;

    public ClientWrapper(Client client, BlockingQueue<String> queue) {
      this.client = client;
      this.queue = queue;
    }

    public void connect() {
      client.connect();
    }

    public boolean isDone() {
      return client.isDone();
    }

    public void stop() {
      client.stop();
    }

    public String next() throws InterruptedException {
      return queue.take();
    }
  }
}

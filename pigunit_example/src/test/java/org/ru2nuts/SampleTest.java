package org.ru2nuts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;

/*
//following import clauses were used to resolve runtime dependencies
import org.apache.log4j.Logger;
import org.antlr.runtime.tree.Tree;
import org.joda.time.ReadableInstant;
import jline.ConsoleReaderInputStream;
import org.apache.hadoop.fs.local.LocalFs;
*/


@Test
public class SampleTest {

  static {
    BasicConfigurator.configure();
  }

  private static final String PIG_SCRIPT = SampleTest.class.getClassLoader().getResource("top_queries.pig").getFile();
  private static Cluster cluster;
  private static final Log LOG = LogFactory.getLog(SampleTest.class);
  private PigTest test;

  @BeforeClass
  public static void setUpOnce() throws IOException {
    System.getProperties().setProperty("mapreduce.framework.name", "yarn");
    cluster = PigTest.getCluster();
  }

  @Test
  public void testTopQueries() throws IOException, ParseException {
    String[] args = {
        "n=2",
    };
    test = new PigTest(PIG_SCRIPT, args);

    String[] input = {
        "yahoo",
        "yahoo",
        "yahoo",
        "twitter",
        "facebook",
        "facebook",
        "linkedin",
    };

    String[] output = {
        "(yahoo,3)",
        "(facebook,2)",
    };

    LOG.info("Starting script");
    test.assertOutput("data", input, "queries_limit", output);
    LOG.info("Done");
  }
}

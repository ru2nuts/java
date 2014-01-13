package org.ru2nuts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.hadoop.fs.Path;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.antlr.runtime.tree.Tree;
import org.joda.time.ReadableInstant;

import jline.ConsoleReaderInputStream;
//import com.hadoop.compression.lzo.LzoCodec;

import org.apache.hadoop.fs.local.LocalFs;


import java.io.IOException;
import java.util.Properties;

@Test
public class SampleTest {

  static {
    BasicConfigurator.configure();
  }

  //"test/data/pigunit/top_queries.pig";
  private static final String PIG_SCRIPT = SampleTest.class.getClassLoader().getResource("top_queries.pig").getFile();
  //private static final String PIG_SCRIPT_MACRO = "test/data/pigunit/top_queries_macro.pig";
  private static Cluster cluster;
  private static final Log LOG = LogFactory.getLog(SampleTest.class);
  private PigTest test;

  @BeforeClass
  public static void setUpOnce() throws IOException {
//    System.getProperties().setProperty("pigunit.exectype.cluster", "true");
//    System.getProperties().setProperty("pigunit.exectype.local", "true");
    System.getProperties().setProperty("mapreduce.framework.name", "yarn");

    cluster = PigTest.getCluster();

//    cluster.listStatus(new Path("/"));
//    cluster.update(
//        new Path("test/data/pigunit/top_queries_input_data.txt"),
//        new Path("top_queries_input_data.txt"));
  }

  @Test
  public void testGenderAgeByCampaign() throws IOException, ParseException {
    //PigTest pt = new PigTest("src/main/pig/bluekai/gender-age_by_campaign.pig");
    String[] args = {
        "n=2",
    };

    //System.getProperties().setProperty("pigunit.exectype.cluster", "true");
//    FileInputStream inputStream = new FileInputStream(PIG_SCRIPT);
//    String[] scriptLines = null;
//    try {
//      scriptLines = IOUtils.readLines(inputStream).toArray(new String[]{});
//      System.out.println(scriptLines);
//    } finally {
//      inputStream.close();
//    }
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

    //test.runScript();
    test.assertOutput("data", input, "queries_limit", output);
  }


}

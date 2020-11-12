package cn.test.mongdb;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/11/4
 */
import com.mongodb.*;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class TestMongoDB extends AbstractJavaSamplerClient {
    private static MongoClient m;
    private static DB db;
    // private static AtomicLong read_key = new AtomicLong(0);
    // private static FileOutputStream fos;
    static {
        try {
            // MongoDB连接池配置
            m = new MongoClient("172.30.208.58", 27017);
            db = m.getDB("dmp"); // 获取dmp数据库
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("rw", "w");
        return params;
    }

    public SampleResult runTest(JavaSamplerContext context) {
        // System.out.println(read_key.getAndIncrement());
        SampleResult results = new SampleResult();
        // System.out.println(context.getParameter("rw"));
        results.sampleStart();
        // 1、获取一个集合foo
        BasicDBObject queryObject = new BasicDBObject("context", "test0");

        DBObject dbObject = db.getCollection("jsontest").findOne(queryObject);
        if (dbObject == null) {
            results.setSuccessful(false);
            results.setResponseCode("500");
        } else {
            results.setResponseMessage(dbObject.toString());
            results.setResponseCodeOK();
            results.setSuccessful(true);
        }
        results.sampleEnd();
        return results;
    }

    public static void main(String args[]) throws Exception {
        Arguments a = new Arguments();
        a.addArgument("rw", "w");
        final JavaSamplerContext c = new JavaSamplerContext(a);
        long startTime = System.currentTimeMillis();
        TestMongoDB t = new TestMongoDB();
        t.runTest(c);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

}
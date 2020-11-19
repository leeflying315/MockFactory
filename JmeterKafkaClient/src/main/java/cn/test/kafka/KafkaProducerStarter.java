package cn.test.kafka;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/11/19
 */
public class KafkaProducerStarter extends AbstractJavaSamplerClient {

    @Override
    /**
     * JMeter界面中展示出此方法所设置的默认参数。
     * @return
     */
    public Arguments getDefaultParameters() {

        Arguments args = new Arguments();
        args.addArgument("topic", "test");
        args.addArgument("message", "hello");

        return args;
    }
    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        String topic = arg0.getParameter("topic");
        String message = arg0.getParameter("message");

        // System.out.println(read_key.getAndIncrement());
        SampleResult results = new SampleResult();
        // System.out.println(context.getParameter("rw"));
        results.sampleStart();
        // 1、获取一个集合foo
        Boolean result = KafkaUtil.getInstance().sendMsgToProcess(topic, message);
        if (!result) {
            results.setSuccessful(false);
            results.setResponseCode("500");
        } else {
            results.setResponseCodeOK();
            results.setSuccessful(true);
        }
        results.sampleEnd();
        return results;
    }

    public static void main(String[] args) {
        Arguments a = new Arguments();
        a.addArgument("topic", "test");
        a.addArgument("message", "hello");

        final JavaSamplerContext c = new JavaSamplerContext(a);
        long startTime = System.currentTimeMillis();
        new KafkaProducerStarter().runTest(c);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}

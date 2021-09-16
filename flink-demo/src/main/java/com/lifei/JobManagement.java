package com.lifei;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterDescriptor;
import org.apache.flink.client.deployment.ClusterRetrieveException;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.util.Preconditions;
import org.apache.flink.util.function.FunctionUtils;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.log4j.BasicConfigurator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/9/8
 */
@Slf4j
public class JobManagement {
    private static String applicationId = "application_1631101260938_0022";
    private static String flinkConfDir = "flink-demo\\src\\main\\resources\\flink-yarn";
    private static String yarnConfDir = "flink-demo\\src\\main\\resources\\yarn-conf";

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure(); //自动快速地使用缺省Log4j环境。
        ClusterClient<ApplicationId> client =
                getClusterClient(applicationId);
        Collection<JobStatusMessage> collection =  client.listJobs().get();
        collection.stream().forEach(key->{
            log.info("key is {}",key.getJobName());
        });
        JobGraph jobGraph = buildJobGraph();
        JobID result = client.submitJob(jobGraph).get();
        log.info("result is {}", result);
    }

    public static JobGraph buildJobGraph() throws Exception {

        int parallelism = 1;

        String jarFilePath =
                "E:\\workspace\\MockFactory\\flink-job\\target\\flink-job-v1-jar-with-dependencies.jar";
//        List<URL> classpaths = Collections.singletonList(new
//                URL("file://E:\\workspace\\MockFactory\\flink-job\\target\\flink-job-v1-jar-with-dependencies.jar"));
        List<URL> classpaths= Collections.singletonList(new URL("http://node1:50070/webhdfs/v1/MyPattern478.jar?op=OPEN&t=1631090934889"));
        File runJarFile = new File(jarFilePath);
//        SavepointRestoreSettings savepointRestoreSettings = dealSavepointRestoreSettings(jobParamsInfo.getConfProperties());
        Preconditions.checkArgument(FileUtils.getFile(runJarFile).exists(), "runJarPath not exist!");

        PackagedProgram program;
        PackagedProgram.Builder packagedProgramBuilder = PackagedProgram.newBuilder()
                .setJarFile(runJarFile)
                .setEntryPointClassName("com.lifei.FlinkStarter")
                .setUserClassPaths(classpaths);
        program = packagedProgramBuilder.build();

        Configuration flinkConfig = getFlinkConfiguration();
        log.info("flink config is {}", flinkConfig);
        JobGraph jobGraph = PackagedProgramUtils.createJobGraph(program, flinkConfig, parallelism, false);

        return jobGraph;
    }

    public static Configuration getFlinkConfiguration() {
        return GlobalConfiguration.loadConfiguration(flinkConfDir);
    }

    private static ClusterClient<ApplicationId> getClusterClient(String yid)
            throws ClusterRetrieveException, IOException {
        ApplicationId applicationId = ConverterUtils.toApplicationId(yid);
        Configuration flinkConfiguration =
                getFlinkConfiguration();

        ClusterDescriptor<ApplicationId> clusterDescriptor =
                createYarnClusterDescriptor(flinkConfiguration);

        ClusterClientProvider<ApplicationId> retrieve = clusterDescriptor.retrieve(applicationId);
        ClusterClient<ApplicationId> clusterClient = retrieve.getClusterClient();

        return clusterClient;
    }
    public static YarnClusterDescriptor createYarnClusterDescriptor(Configuration flinkConfig) throws IOException {
        FileSystem.initialize(flinkConfig, null);
        // 安全认证环境

        YarnConfiguration yarnConf = getYarnConf(yarnConfDir);
        YarnClient yarnClient = createYarnClient(yarnConf);

        YarnClusterDescriptor clusterDescriptor = new YarnClusterDescriptor(
                flinkConfig,
                yarnConf,
                yarnClient,
                YarnClientYarnClusterInformationRetriever.create(yarnClient),
                false);

        return clusterDescriptor;
    }

    public static YarnConfiguration getYarnConf(String yarnConfDir) throws IOException {
        YarnConfiguration yarnConf = new YarnConfiguration();
        org.apache.flink.util.FileUtils.listFilesInDirectory(new File(yarnConfDir).toPath(), JobManagement::isXmlFile)
                .stream()
                .map(FunctionUtils.uncheckedFunction(org.apache.flink.util.FileUtils::toURL))
                .forEach(yarnConf::addResource);

        log.info("yarn conf is {}", yarnConf);
        haYarnConf(yarnConf);
        return yarnConf;
    }

    private static boolean isXmlFile(java.nio.file.Path file) {
        return true;
    }
    public static YarnClient createYarnClient(YarnConfiguration yarnConf) {
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(yarnConf);
        yarnClient.start();

        return yarnClient;
    }

    /**
     * deal yarn HA conf
     */
    private static org.apache.hadoop.conf.Configuration haYarnConf(org.apache.hadoop.conf.Configuration yarnConf) {
        Iterator<Map.Entry<String, String>> iterator = yarnConf.iterator();
        iterator.forEachRemaining((Map.Entry<String, String> entry) -> {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.startsWith("yarn.resourcemanager.hostname.")) {
                String rm = key.substring("yarn.resourcemanager.hostname.".length());
                String addressKey = "yarn.resourcemanager.address." + rm;
                if (yarnConf.get(addressKey) == null) {
                    yarnConf.set(addressKey, value + ":" + YarnConfiguration.DEFAULT_RM_PORT);
                }
            }
        });
//        yarnConf.set("yarn.resourcemanager.address.rm2", "node2" + ":" + YarnConfiguration.DEFAULT_RM_PORT);

        return yarnConf;
    }
}

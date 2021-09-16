package com.lifei;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.concurrent.TimeUnit;

/**
 * @Author lifei
 * @Description:
 * @Date 2021/9/7
 */
public class FlinkStarter {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /**
         * 开启checkpoint自我恢复
         */
        CheckpointConfig config = env.getCheckpointConfig();
        config.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);

        //自我失败恢复设定
        env.setRestartStrategy(RestartStrategies.failureRateRestart(
                // 每个时间间隔的最大故障次数
                180,
                // 测量故障率的时间间隔
                Time.of(30, TimeUnit.MINUTES),
                // 延时
                Time.of(10, TimeUnit.SECONDS)
        ));

        System.out.println("test sql1");
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        String template = "CREATE TABLE device_raw_data_test (" +
                "    log_level STRING," +
                "    operation_by_id STRING," +
                "    operation_by_name STRING," +
                "    operation_code STRING," +
                "    operation_name STRING," +
                "    operation_target STRING," +
                "    operation_target_info STRING," +
                "    org_id STRING," +
                "    request_ip STRING," +
                "    request_params STRING," +
                "    request_time STRING," +
                "    message_id STRING," +
                "    sequence_id STRING," +
                "    service_type STRING," +
                "    activate_timestamp STRING," +
                "    status_code STRING" +
                ") WITH (" +
                "  'connector' = 'kafka'," +
                "  'topic' = 'ck.device.rawdata'," +
                "  'properties.bootstrap.servers' = '172.30.125.52:9092'," +
                "  'properties.group.id' = 'test.consumer'," +
//                "  'key.format' = 'org.apache.kafka.common.serialization.StringDeserializer'," +
//                "  'value.format' = 'org.apache.kafka.common.serialization.StringDeserializer'," +
                "  'scan.startup.mode' = 'earliest-offset'," +
                "  'json.fail-on-missing-field' = 'false'," +
                "  'json.ignore-parse-errors' = 'true'," +
                "  'format' = 'json'" +
                ")";
        System.out.println("test sql2");

        TableResult tableResult1 = tableEnv.executeSql(template);
        tableResult1.print();

        System.out.println("test sql3");

        String sinkSql = "CREATE TABLE test_result (" +
                "    log_level STRING," +
                "    operation_by_id STRING," +
                "    operation_by_name STRING," +
                "    operation_code STRING," +
                "    operation_name STRING," +
                "    operation_target STRING," +
                "    operation_target_info STRING," +
                "    org_id STRING," +
                "    request_ip STRING," +
                "    request_params STRING," +
                "    request_time STRING," +
                "    message_id STRING," +
                "    sequence_id STRING," +
                "    service_type STRING," +
                "    activate_timestamp STRING," +
                "    status_code STRING" +
                ") WITH (" +
                "    'connector' = 'kafka'," +
                "    'topic' = 'test_result'," +
                "    'scan.startup.mode' = 'latest-offset'," +
                "    'properties.bootstrap.servers' = '172.30.125.52:9092'," +
                "    'properties.group.id' = 'group_14'," +
                "    'properties.max.poll.records' = '1000'," +
                "    'properties.enable.auto.commit' = 'true'," +
                "    'properties.auto.commit.interval.ms' = '1000'," +
                "    'properties.retries' = '3'," +
                "    'format' = 'json'" +
                ")";
        tableEnv.executeSql(sinkSql);
        System.out.println("test sql4");

//        StatementSet statementSet = tableEnv.createStatementSet();
//
//        String insertSql = "INSERT INTO test_result " +
//                "select org_id " +
//                "from device_raw_data_test";
//        statementSet.addInsertSql(insertSql);
//        statementSet.execute();
        System.out.println("test sql5");

        Table table = tableEnv.from("device_raw_data_test");


        DataStream<Row> dataStream = tableEnv.toDataStream(table);
        dataStream.print();

        Table inputTable = tableEnv.fromDataStream(dataStream);
        tableEnv.createTemporaryView("InputTable", inputTable);
        tableEnv.from("InputTable").executeInsert("test_result").print();

//        String sourceSql = "CREATE TABLE ck_device_activate (" +
//                "    product_key STRING," +
//                "    device_key STRING," +
//                "    data_time STRING," +
//                "    request_time STRING," +
//                "    request_ip STRING," +
//                "    operation_code STRING," +
//                "    operation_name STRING," +
//                "    operation_by_id STRING," +
//                "    operation_by_name STRING," +
//                "    operation_target_info STRING," +
//                "    service_type INT," +
//                "    log_level STRING," +
//                "    status_code STRING," +
//                "    status_msg STRING," +
//                "    message_id STRING," +
//                "    sequence_id STRING," +
//                "    activate_timestamp STRING," +
//                "    insert_time STRING" +
//                ") WITH (" +
//                "    'connector' = 'kafka'," +
//                "    'topic' = 'ck.device.activate'," +
//                "    'scan.startup.mode' = 'group-offsets'," +
//                "    'properties.bootstrap.servers' = '172.30.208.108:9092'," +
//                "    'properties.group.id' = 'test_consumer'," +
//                "    'properties.enable.auto.commit' = 'true'," +
//                "    'properties.auto.commit.interval.ms' = '1000'," +
//                "    'properties.retries' = '3'," +
//                "    'value.format' = 'json'" +
//                ")";
//        tableEnv.executeSql(sourceSql);
//
//        String sinkSql = "CREATE TABLE ruleEngine_target_test_prk (" +
//                "    dataType STRING," +
//                "    productKey STRING," +
//                "    productName STRING," +
//                "    deviceKey STRING," +
//                "    deviceName STRING," +
//                "    orgId STRING," +
//                "    orgName STRING," +
//                "    ruleExecuteTime TIMESTAMP," +
//                "    ruleId STRING," +
//                "    ruleName STRING," +
//                "    messageId STRING," +
//                "    properties STRING," +
//                "    dataTime STRING," +
//                "    orgKey STRING," +
//                "    recordMode STRING," +
//                "    iotId STRING," +
//                "    nodeType STRING," +
//                "    createdBy STRING," +
//                "    connectionProtocol STRING," +
//                "    authType STRING," +
//                "    IP STRING," +
//                "    updater STRING," +
//                "    updatedOn STRING," +
//                "    description STRING," +
//                "    activatedOn STRING," +
//                "    gatewayId STRING," +
//                "    imei STRING," +
//                "    iccid STRING," +
//                "    imsi STRING," +
//                "    sequenceId STRING," +
//                "    deviceNumber STRING" +
//                ") WITH (" +
//                "    'connector' = 'kafka'," +
//                "    'topic' = 'listener_ruleEngine_target_test_prk'," +
//                "    'scan.startup.mode' = 'latest-offset'," +
//                "    'properties.bootstrap.servers' = '172.30.125.52:9092'," +
//                "    'properties.group.id' = 'group_14'," +
//                "    'properties.max.poll.records' = '1000'," +
//                "    'properties.enable.auto.commit' = 'true'," +
//                "    'properties.auto.commit.interval.ms' = '1000'," +
//                "    'properties.retries' = '3'," +
//                "    'value.format' = 'json'" +
//                ")";
//        tableEnv.executeSql(sinkSql);
//        StatementSet statementSet = tableEnv.createStatementSet();
//        String insertSql = "INSERT INTO listener_ruleEngine_target_test_prk " +
//                "select '0',0,'750','七七省份账号',LOCALTIMESTAMP,'5029','奉贤0824002',0,request_time,'20210209','1' " +
//                "from ck_device_activate where  request_time < 1";
//        statementSet.addInsertSql(insertSql);
    }
}

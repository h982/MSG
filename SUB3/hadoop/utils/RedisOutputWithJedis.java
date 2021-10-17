package ssafy.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class RedisOutputWithJedis {

    final static private String IP = "localhost";
    final static private int PORT = 6379;
    final static private String PASSWORD = "ssafy";
    final static private int TIME_OUT = 1000;
    final static private int REDUCER_COUNT = 1;
    public static boolean hdfsToRedis(String store, String hdfs_path) throws IOException{

//        Configuration hdfs_config = new Configuration();
//        FileSystem hdfs = FileSystem.get(hdfs_config);
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        JedisPool pool = new JedisPool(jedisPoolConfig , IP , PORT , TIME_OUT , PASSWORD );
//        String storeKey = "store:" + store;
//        Jedis jedis = pool.getResource();
//
//        jedis.sadd("store", store);
//        for(int i = 0; i < 1; i++) {
//            Path filePath = new Path(hdfs_path);
//            BufferedReader br = new BufferedReader(new InputStreamReader(hdfs.open(filePath)));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                String[] str = line.split(" ");
//                jedis.hset(storeKey, str[0], str[1]);
//            }
//        }
//
//
//        if (jedis != null) {
//            jedis.close();
//        }

        return true;
    }


}

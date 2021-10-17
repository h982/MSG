package ssafy.utils;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RedisOutputWithLettuce {
    final static private String URL = "localhost";
    final static private int PORT = 6379;
    final static private String PASSWORD = "ssafy";
    final static private int TIME_OUT = 1000;
    final static private int REDUCER_COUNT = 1;

    public static boolean hdfsToRedis(String store, String output_path) throws IOException {

        Configuration hdfs_config = new Configuration();
        FileSystem hdfs = FileSystem.get(hdfs_config);

//        RedisURI redisURI = RedisURI.Builder.redis(URL)
//                .withPassword(PASSWORD.toCharArray())
//                .withDatabase(0)
//                .build();
        RedisURI redisURI = RedisURI.Builder
                .redis(URL, PORT)
                .withDatabase(0)
                .build();

        RedisClient client = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisCommands<String, String> commands = connection.sync();

        String storeKey = "store:" + store;
        RemoteIterator<LocatedFileStatus> fileStatusListIterator = hdfs.listFiles(
                new Path(output_path), true);

        Map<String, String> map = new HashMap<>();
        while (fileStatusListIterator.hasNext()) {
            FileStatus fs = fileStatusListIterator.next();
            System.out.println("하위파일: " + fs.getPath());;
            BufferedReader br = new BufferedReader(new InputStreamReader(hdfs.open(fs.getPath())));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] str = line.split("\t");
                if(str.length < 2)
                    continue;
                map.put(str[0], str[1]);
            }
        }
        System.out.println("한글 테스트 : " + storeKey);
        commands.hset(storeKey, map);

        connection.close();
        return true;
    }
}

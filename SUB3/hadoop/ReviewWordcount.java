package ssafy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import ssafy.utils.RedisOutputWithJedis;
import ssafy.utils.RedisOutputWithLettuce;

import java.io.IOException;
import java.util.StringTokenizer;

public class ReviewWordcount extends Configured implements Tool {


    public static class TokenizerMapper
            extends Mapper<Object, Text,Text, IntWritable> {

        // variable declairations
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        // map function (Context -> fixed parameter)
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            String val = value.toString();
            int start = val.indexOf('"');
            String str = "";
            if(start != -1)
                str = val.substring(val.indexOf('"'));
            else{
                String[] temp = str.split(",");
                if(temp.length <= 5)
                    return;
                str = temp[4];
            }
            String replacedStr = str.replaceAll("[^\\uAC00-\\uD7A3]",  " ");
            StringTokenizer st = new StringTokenizer(replacedStr);

            while ( st.hasMoreTokens() ) {
                word.set(st.nextToken().trim());
                // emit a key-value pair
                context.write(word,one);
            }

        }
    }

    public static class IntSumReducer
            extends Reducer<Text,IntWritable,Text,IntWritable> {

        // variables
        private IntWritable result = new IntWritable();

        // key : a disticnt word
        // values :  Iterable type (data list)
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {

            int sum = 0;
            for ( IntWritable val : values ) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key,result);
        }
    }


    /* Main function */
    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new ReviewWordcount(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        for(int i = 0; i < args.length; i++)
            System.out.println(args[i]);
//        if ( args.length != 2 ) {
//            System.err.println("Usage: <in> <out>");
//            System.exit(2);
//        }
        FileSystem hdfs = FileSystem.get(conf);
        Path output = new Path(args[1]);
        if (hdfs.exists(output))
            hdfs.delete(output, true);

        Job job = new Job(conf,"word count");
        job.setJarByClass(ReviewWordcount.class);

        // let hadoop know my map and reduce classes
        job.setMapperClass(ReviewWordcount.TokenizerMapper.class);
        job.setReducerClass(ReviewWordcount.IntSumReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // set number of reduces
        job.setNumReduceTasks(2);

        // set input and output directories
        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        if(job.waitForCompletion(true)){
            RedisOutputWithLettuce.hdfsToRedis("한글", args[1]);

            System.exit(0);
        }
        System.exit(1);
        return 0;
    }
}

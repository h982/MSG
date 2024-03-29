import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MatrixMulti {
    public static class MMMapper extends Mapper<Object, Text, Text, Text>{
        private String Matrix1name;
        private String Matrix2name;

        private int n;	// number of rows in matrix A
        private int l;	// number of columns in matrix A
        private int m;	// number of columns matrix B
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration config = context.getConfiguration();
            Matrix1name = config.get("Matrix1name");
            Matrix2name = config.get("Matrix2name");
            n = config.getInt("n", 1);
            l = config.getInt("l", 1);
            m = config.getInt("m", 1);
        }
        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            setup(context);
            StringTokenizer token = new StringTokenizer(value.toString());
            String mat = token.nextToken();
            int row = Integer.parseInt(token.nextToken());
            int col = Integer.parseInt(token.nextToken());
            int val = Integer.parseInt(token.nextToken());

            if(mat.equals(Matrix1name)){
                for(int i = 0; i < m; i++) {
                    context.write(new Text(row + " " + i), new Text(col + " " + val));
                }
            }else if(mat.equals(Matrix2name)){
                for(int i = 0; i < n; i++) {
                    context.write(new Text(i + " " + col), new Text(row + " " + val));
                }
            }
        }
    }
    // Reduce
    public static class MMReducer extends Reducer<Text, Text, Text, Text> {
        private IntWritable val = new IntWritable();	// emit�� value�� ����� ����

        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            StringTokenizer st = new StringTokenizer(key.toString());
            int row = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken());
            int sum = 0;
            Map<Integer, Integer> map = new HashMap<>();
            for(Text value : values){
                st = new StringTokenizer(value.toString());
                int loc = Integer.parseInt(st.nextToken());
                int val = Integer.parseInt(st.nextToken());

                if(map.containsKey(loc)){
                    sum += val * map.get(loc);
                    continue;
                }
                map.put(loc, val);
            }
            context.write(new Text(row + "," + col), new Text(Integer.toString(sum)));
        }
    }
    // Main
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 7) {
            System.err.println("Usage: <Matrix 1 name> <Matrix 2 name> <Number of row in Matrix 1><Number of columns in Matrix 1 (i.e., Number of rows in Matrix 2)> <Number of columns in Matrix 2> <in> <out>");
            System.exit(2);
        }

        FileSystem hdfs = FileSystem.get(conf);
        Path output = new Path(otherArgs[6]);
        if (hdfs.exists(output))
            hdfs.delete(output, true);

        Job job = new Job(conf, "matrix multiplication prepare");
        Configuration config = job.getConfiguration();
        config.set("Matrix1name", otherArgs[0]);
        config.set("Matrix2name", otherArgs[1]);
        config.setInt("n",Integer.parseInt(otherArgs[2]));
        config.setInt("l",Integer.parseInt(otherArgs[3]));
        config.setInt("m",Integer.parseInt(otherArgs[4]));


        job.setJarByClass(MatrixMulti.class);
        job.setMapperClass(MatrixMulti.MMMapper.class);
        job.setReducerClass(MatrixMulti.MMReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setNumReduceTasks(2);

        FileInputFormat.addInputPath(job, new Path(otherArgs[5]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[6]));
        FileSystem.get(config).delete(new Path(otherArgs[1]),true);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

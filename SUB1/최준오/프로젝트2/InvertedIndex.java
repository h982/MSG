import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.StringTokenizer;

public class InvertedIndex {
    /*
	Object, Text : input key-value pair type (always same (to get a line of input file))
	Text, IntWritable : output key-value pair type
	*/
    public static class TokenizerMapper
            extends Mapper<Object, Text,Text, Text> {

        // variable declairations
//        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private Text offset = new Text();
        private String fileName;
        protected void setUp(Context context)throws IOException, InterruptedException{
            fileName = ((FileSplit)context.getInputSplit()).getPath().getName();
        }

        // map function (Context -> fixed parameter)
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            setUp(context);
            // value.toString() : get a line
            StringTokenizer itr = new StringTokenizer(value.toString());
            long p = ((LongWritable)key).get();
            while ( itr.hasMoreTokens() ) {
                String temp = itr.nextToken();
                word.set(temp);
                if(!" ".equals(temp)) {
                    offset.set(fileName + ":" + p);
                    // emit a key-value pair
                    context.write(word, offset);
                }
            }
        }
    }

    /*
    Text, IntWritable : input key type and the value type of input value list
    Text, IntWritable : output key-value pair type
    */
    public static class IntSumReducer
            extends Reducer<Text,Text,Text,Text> {

        // variables
        private Text result = new Text();

        // key : a disticnt word
        // values :  Iterable type (data list)
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {

            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Text val : values ) {
                if(first){
                    sb.append(":" + val.toString());
                    first = false;
                    continue;
                }
                sb.append(",    " + val.toString());
            }
            result.set(sb.toString());
            context.write(key,result);
        }
    }


    /* Main function */
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if ( otherArgs.length != 2 ) {
            System.err.println("Usage: <in> <out>");
            System.exit(2);
        }
        FileSystem hdfs = FileSystem.get(conf);
        Path output = new Path(otherArgs[1]);
        if(hdfs.exists(output)){
            hdfs.delete(output, true);
        }
        Job job = new Job(conf,"word count");
        job.setJarByClass(InvertedIndex.class);

        // let hadoop know my map and reduce classes
        job.setMapperClass(InvertedIndex.TokenizerMapper.class);
        job.setReducerClass(InvertedIndex.IntSumReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // set number of reduces
        job.setNumReduceTasks(2);

        // set input and output directories
        FileInputFormat.addInputPath(job,new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1 );
    }
}

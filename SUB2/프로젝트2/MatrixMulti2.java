package ssafy;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MatrixMulti2 {
	public static class M1Mapper extends Mapper<Object, Text, Text, IntWritable>{
		// phase1 Map
		private Text pair = new Text();	
		private IntWritable val = new IntWritable();
                private String Matrix1name;
                private String Matrix2name;
		private int n;	
		private int m;
		protected void setup(Context context) throws IOException, InterruptedException {
			Configuration config = context.getConfiguration();
                       // TODO
                       // ------------------------------------------------------
                       Matrix1name = config.get("Matrix1name", "A");
			Matrix2name = config.get("Matrix2name", "B");
			n = config.getInt("n", 10);
			m = config.getInt("m", 10);
			//
                       // ------------------------------------------------------
		}
		public void map(Object key, Text value, Context context
				) throws IOException, InterruptedException {
                       // TODO
                       // ------------------------------------------------------
                        StringTokenizer st = new StringTokenizer(value.toString());
			String mat = st.nextToken();
			int row = Integer.parseInt(st.nextToken());
			int col = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());

			val.set(v);
			if(mat.equals(Matrix1name)) {
				for(int i = 0 ; i < m ; i++) {
					pair.set("" + row + " " + i + " " + col);
					context.write(pair, val);
				}
			} else if(mat.equals(Matrix2name)) {
				for(int i = 0 ; i < n ; i++) { 
					pair.set("" + i + " " + col + " " + row);
					context.write(pair, val);
				}
			}

                       // ------------------------------------------------------
		}
	}
	// phase1 Reduce
	public static class M1Reducer extends Reducer<Text,IntWritable,Text,IntWritable> {
		private IntWritable result = new IntWritable();
		private Text pair = new Text();		
		public void reduce(Text key, Iterable<IntWritable> values, Context context) 
			throws IOException, InterruptedException {
                       // TODO
                       // ------------------------------------------------------
			int sum = 1;
			for(IntWritable val : values) {
				sum *= val.get();
			}
			result.set(sum);
			context.write(key, result);
                       // ------------------------------------------------------
		}
	}
	// phase2 Map
	public static class M2Mapper extends Mapper<Object, Text, Text, IntWritable>{    
		private Text pair = new Text();	
		private IntWritable val = new IntWritable();
		public void map(Object key, Text value, Context context) 
			throws IOException, InterruptedException {
                       // TODO
                       // ------------------------------------------------------
                       
			StringTokenizer st = new StringTokenizer(value.toString());

			pair.set(st.nextToken() + " " + st.nextToken());
			st.nextToken();
			val.set(Integer.parseInt(st.nextToken()));
			context.write(pair, val);

                       // ------------------------------------------------------
		}
	}
	// phase2 Reduce
	public static class M2Reducer extends Reducer<Text,IntWritable,Text,IntWritable> {
		private IntWritable result = new IntWritable();
		public void reduce(Text key, Iterable<IntWritable> values, Context context) 
			throws IOException, InterruptedException {
                       // TODO
                       // ------------------------------------------------------
                       
			int sum = 0;
			for(IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			
			context.write(key, result);
                       // ------------------------------------------------------
		}
	}  
	// main
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

                if (otherArgs.length != 7) {
                        System.err.println("Usage: <Matrix 1 name> <Matrix 2 name> <Number of rows in Matrix 1><Number of columns in Matrix 1 (i.e., Number of rows in Matrix 2)> <Number of columns in Matrix 2> <in> <out>");
                        System.exit(2);
                }

                FileSystem hdfs = FileSystem.get(conf);
                Path output = new Path(otherArgs[6]);
                if (hdfs.exists(output))
                        hdfs.delete(output, true);

                // phase 1
                Job job1 = new Job(conf, "2-phase matrix multiplication 1");
                Configuration config = job1.getConfiguration();
                config.set("Matrix1name", otherArgs[0]);
                config.set("Matrix2name", otherArgs[1]);
                config.setInt("n",Integer.parseInt(otherArgs[2]));
                config.setInt("l",Integer.parseInt(otherArgs[3]));
                config.setInt("m",Integer.parseInt(otherArgs[4]));

                job1.setJarByClass(MatrixMulti2.class);
                job1.setMapperClass(M1Mapper.class);
                job1.setReducerClass(M1Reducer.class);
                job1.setOutputKeyClass(Text.class);
                job1.setOutputValueClass(IntWritable.class);
                job1.setNumReduceTasks(2);

		FileInputFormat.addInputPath(job1, new Path(otherArgs[5]));
		FileOutputFormat.setOutputPath(job1, new Path("tmp"));
		FileSystem.get(conf).delete(new Path("tmp"),true);

		// phase 2
		Job job2 = new Job(conf, "2-phase matrix multiplication 2");
		job2.setJarByClass(MatrixMulti2.class);
		job2.setMapperClass(M2Mapper.class);
		job2.setReducerClass(M2Reducer.class);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(IntWritable.class);
		job2.setNumReduceTasks(2);

		FileInputFormat.addInputPath(job2, new Path("tmp"));
		FileOutputFormat.setOutputPath(job2, new Path(otherArgs[6]));

		job1.waitForCompletion(true);
		job2.waitForCompletion(true);
	}

}


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

public class MatrixMulti {
	// Map
	public static class MMMapper extends Mapper<Object, Text, Text, Text>{
                private String Matrix1name;
                private String Matrix2name;

		private int n;	// number of rows in matrix A
		private int l;	// number of columns in matrix A
		private int m;	// number of columns matrix B
		protected void setup(Context context) throws IOException, InterruptedException {
			Configuration config = context.getConfiguration();
                        // TODO
                        // ------------------------------------------------------
                        
                        Matrix1name = config.get("Matrix1name","A");
                        Matrix2name = config.get("Matrix2name","B");
                        
                        n = config.getInt("n",2);
                        l = config.getInt("l",2);
                        m = config.getInt("m",2);
                        
                        // ------------------------------------------------------
		}
		public void map(Object key, Text value, Context context
				) throws IOException, InterruptedException {
			StringTokenizer token = new StringTokenizer(value.toString());
                        // TODO
                        // ------------------------------------------------------
                        
                        String matrixName = token.nextToken();
                        int row = Integer.parseInt(token.nextToken());
                        int col = Integer.parseInt(token.nextToken());
                        int wage = Integer.parseInt(token.nextToken());                                         
                        
                        Text newVal = new Text();
                        if(matrixName.equals(Matrix1name)){
                        	newVal.set( col + " " + wage);
                        	for(int i = 0; i < m ; i++){
                        		Text newKey = new Text();
                        		newKey.set( row +" " + i);
                        		context.write(newKey,newVal);
                        	}
                        	
                        } else if(matrixName.equals(Matrix2name)) {
                        	newVal.set( row + " " + wage);
                        	for(int i = 0; i < m ; i++){
                        		Text newKey = new Text();
                        		newKey.set( i +" " + col );
                        		context.write(newKey,newVal);
                        	}
                        	
                        }
		}
	}
	// Reduce
	public static class MMReducer extends Reducer<Text, Text, Text, Text> {
		private IntWritable val = new IntWritable();
		
		
		private int m;	// number of columns matrix B
		protected void setup(Context context) throws IOException, InterruptedException {
			Configuration config = context.getConfiguration();
                        m = config.getInt("m",2);
		}
		

		public void reduce(Text key, Iterable<Text> values, Context context) 
			throws IOException, InterruptedException {
			
			int arr[] = new int[m];
			
			for(int i = 0; i < m ; i++){
				arr[i] = 1;
                       }
			
			for(Text tx : values){
				StringTokenizer itr = new StringTokenizer(tx.toString());
				
				while ( itr.hasMoreTokens() ) {
					int t = Integer.parseInt(itr.nextToken());
					int v = Integer.parseInt(itr.nextToken());
					arr[t] *= v;
				}
				
			}
			
			int sum = 0;
			for(int i = 0; i < m ; i++){
				sum += arr[i];
                       }
			Text result = new Text(Integer.toString(sum));
			
			context.write(key,result);
		}
		
//		public void reduce(Text key, Iterable<Text> values, Context context) 
//			throws IOException, InterruptedException {			
//			for(Text tx : values) {
//				context.write(key,tx);
//			}
//		}
		
		
		
		
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
		job.setMapperClass(MMMapper.class);
		job.setReducerClass(MMReducer.class);
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

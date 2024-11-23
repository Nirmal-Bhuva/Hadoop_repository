package average;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



public class average {
	public static class mapper extends Mapper<LongWritable, Text, Text, IntWritable>{
		public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException{
			String line=value.toString();
			StringTokenizer token=new StringTokenizer(line);
		
			while(token.hasMoreElements()) {
				value.set(token.nextToken());
				context.write(value,new IntWritable(1));
			}
		
	
		
		}
	}
	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable>{
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException,InterruptedException{
			int sum=0;
			for(IntWritable i:values) {
				sum+=i.get();
				
			}
			
			context.write(key, new IntWritable(sum));
		
			
		}
	}
	public static void main(String args[]) throws Exception{
		Configuration conf=new Configuration();
		Job job=Job.getInstance(conf,"average");
		job.setJarByClass(average.class);
		job.setMapperClass(mapper.class);
		job.setReducerClass(Reduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);	
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
		long total=job.getCounters().findCounter("org.apache.hadoop.mapred.Task$Counter","MAP_INPUT_RECORDS").getValue();
		long words=job.getCounters().findCounter("org.apache.hadoop.mapred.Task$Counter","REDUCE_INPUT_GROUPS").getValue();
		double ave=(double)words/total;
		System.out.println("average:"+ave);
	}
}

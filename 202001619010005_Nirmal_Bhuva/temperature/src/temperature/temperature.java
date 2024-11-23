package temperature;

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


public class temperature {
	public static class mapper extends Mapper<LongWritable,Text , Text, IntWritable> {
		public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException{
			String field[]=value.toString().split(",");
			String country=field[0];
			int temp=Integer.parseInt(field[1]);
			context.write(new Text(country),new IntWritable(temp));
			
		}
	}
	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable>{
		public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException,InterruptedException{
			int min=Integer.MAX_VALUE;
			for(IntWritable details:values) {
				if(min > details.get()) {
					min=details.get();
				}	
			}
			context.write(key, new IntWritable(min));
		}
		
	}
	public static void main(String args[]) throws Exception{
	
		
		Configuration conf=new Configuration();
		Job job=Job.getInstance(conf,"temperature");
		
		job.setJarByClass(temperature.class);
		job.setMapperClass(mapper.class);
		job.setReducerClass(Reduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setNumReduceTasks(1);
		
		
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		System.exit(job.waitForCompletion(true)?0:1);
		
	}
	}

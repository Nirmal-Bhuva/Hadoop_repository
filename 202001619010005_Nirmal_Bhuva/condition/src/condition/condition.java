package condition;
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
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class condition {

	public static class mapper extends Mapper<LongWritable, Text, Text, Text>{
		public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException{
			String line=value.toString();
			StringTokenizer token=new StringTokenizer(line);
		
			while(token.hasMoreElements()) {
				value.set(token.nextToken());
				if(value.getLength()>=4) {
				context.write(value,new Text(""));
			}
		
		}
	}
}
		public static void main(String args[]) throws Exception{
		Configuration conf=new Configuration();
		Job job=Job.getInstance(conf,"wordcount");
		job.setJarByClass(condition.class);
		job.setMapperClass(mapper.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		Path outputpath=new Path(args[1]);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		outputpath.getFileSystem(conf).delete(outputpath,true);
		System.exit(job.waitForCompletion(true)?0:1);
		
	}

}

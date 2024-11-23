package movie;

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
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class movie {
	public static class mapper extends Mapper<LongWritable, Text, Text, Text>{
		public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException{
			String field[]=value.toString().split(",");
				String line=field[2].toString();
				StringTokenizer token=new StringTokenizer(line,"|");
			
				while(token.hasMoreElements()) {
					value.set(token.nextToken());
					String type=value.toString().trim();
					if(type.equalsIgnoreCase("Comedy")){
					context.write(new Text(field[1]),new Text(field[2]));
					}
				}
			}
		
		}
	


	public static void main(String args[]) throws Exception{
		Configuration conf=new Configuration();
		Job job=Job.getInstance(conf,"movie");
		job.setJarByClass(movie.class);
		job.setMapperClass(mapper.class);
	
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
	
		System.exit(job.waitForCompletion(true)?0:1);
		
	}

}


// USA,30
// Canada,25
// India,35
// USA,20
// India,40
// Canada,15
// Mexico,28
// USA,25
// Mexico,18
// Canada,30

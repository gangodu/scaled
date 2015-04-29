package compress;

import java.util.*;
import java.util.zip.*;
import java.util.zip.Deflater;
import java.io.*;

@SuppressWarnings("unused")
public class Zip {
	
	public void zipper(){
	 
		ZipOutputStream out = null;
		InputStream in = null;
	
		try 
		{
			File inputFile = new File("userdata/users/");
			File outputFile = new File("users.zip");
			
			OutputStream rawOut = new BufferedOutputStream(new FileOutputStream(outputFile));
			out = new ZipOutputStream(rawOut);
			out.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);
			
			InputStream rawIn = new FileInputStream(inputFile);
			in = new BufferedInputStream(rawIn);
			
			ZipEntry entry = new ZipEntry("userdata/users");
			out.putNextEntry(entry);
			
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) 
			{
				out.write(buf, 0, len);
			}
		}
		
		catch(IOException e) 
		{
			e.printStackTrace();
		}
		
		finally 
		{
			try 
			{
				if(in != null) 
				{
					in.close();
				}
				if(out != null) 
				{
					out.close();
				}
			}
			catch(IOException ignored) 
			{ 
				System.out.println("Error in archiving !");
			}
		}
	}
}
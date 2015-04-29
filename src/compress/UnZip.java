package compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.zip.*;

public class UnZip 
{
	public static void main(String[] args) 
	{	 
		  try 
		  {
			  File file = new File("users.zip");
			  ZipFile zipFile = new ZipFile(file);
			  
			  File zipDir = new File(file.getParentFile(), "users");
			  zipDir.mkdir();
			  
			  @SuppressWarnings("unchecked")
			  Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
	
			  while(entries.hasMoreElements()) 
			  {
			  	ZipEntry entry = entries.nextElement();
				
				String name = entry.getName();
				File entryDestination = new File(zipDir, name);				
				entryDestination.getParentFile().mkdirs();
				
				if(!entry.isDirectory()) 
				{
					generateFile(entryDestination, entry, zipFile);
				}
			  }
		  }
		  
		  catch(IOException e) 
		  {
		  	e.printStackTrace();
		  }
	}

	private static void generateFile(File destination, ZipEntry entry, ZipFile owner) throws IOException 
	{
		InputStream in = null;
		OutputStream out = null;
		try 
		{
			InputStream rawIn = owner.getInputStream(entry);
			in = new BufferedInputStream(rawIn);
						
			FileOutputStream rawOut = new FileOutputStream(destination);
			out = new BufferedOutputStream(rawOut);
						
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) 
			{
				out.write(buf, 0, len);
			}
		}
		
		finally 
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
	}
}

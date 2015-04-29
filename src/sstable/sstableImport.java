package sstable;
/**
 * @author praveengangodu
 */

import java.nio.ByteBuffer;
import java.io.*;
import java.util.UUID;

import org.apache.cassandra.config.Config;
import org.apache.cassandra.db.marshal.*;
import org.apache.cassandra.io.sstable.SSTableSimpleUnsortedWriter;

import static org.apache.cassandra.utils.ByteBufferUtil.bytes;
import static org.apache.cassandra.utils.UUIDGen.decompose;

import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.RandomPartitioner;
import org.zeroturnaround.zip.ZipUtil;

import compress.Zip;
  
@SuppressWarnings("unused")
public class sstableImport 
{
    static String inputFile;
    
    @SuppressWarnings({ "resource", "rawtypes" })
	public static void main(String[] args) throws IOException
    {
        inputFile = "conf/dummy.csv";
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        
        IPartitioner partition = new RandomPartitioner();
        String keySpace = "userdata";
        String columnFamily = "users";
        AbstractType comparator = AsciiType.instance;
        AbstractType subComparator = null;
        int bufferSizeinMB = 64;
        
        File folder = new File(keySpace);
        
        if (!folder.exists())
        {
        	folder.mkdir();
        	folder.setWritable(true);
        }
        //Config.setClientMode(true);

        SSTableSimpleUnsortedWriter userDataWriter = new SSTableSimpleUnsortedWriter(folder,partition, keySpace,columnFamily,comparator,subComparator,bufferSizeinMB);
        
        String line;
        int lineNumber = 1;
        csvReaderMapper reader = new csvReaderMapper();
        
        long timestamp = System.currentTimeMillis() * 1000;
        while ((line = br.readLine()) != null)
        {
          if (reader.parse(line, lineNumber))
          {
            ByteBuffer uuid = ByteBuffer.wrap(decompose(reader.key));

            /*Add new row if data available*/
            userDataWriter.newRow(uuid);
            
            /*Add needed columns*/
            userDataWriter.addColumn(bytes("fname"), bytes(reader.fname), timestamp);
            userDataWriter.addColumn(bytes("lname"), bytes(reader.lname), timestamp);
            userDataWriter.addColumn(bytes("password"), bytes(reader.password), timestamp);
            userDataWriter.addColumn(bytes("age"), bytes(reader.age), timestamp);
            userDataWriter.addColumn(bytes("email"), bytes(reader.email), timestamp);
          }
          lineNumber++;
        }
        userDataWriter.close();
//      Zip archiver = new Zip();
//      archiver.zipper();

     	ZipUtil.pack(new File("userdata/users"), new File("userdata/users.zip"));
        System.exit(0);
    }

	/**
	 * Mapping fields from file to Cassandra to create SSTable
	**/

    static class csvReaderMapper
    {
        UUID key;
        String fname;
        String lname;
        String password;
        long age;
        String email;
  
        boolean parse(String line, int lineNumber)
        {
            String[] columns = line.split(",");
            if (columns.length != 6)
            {
                System.out.println(String.format("Invalid input '%s' at line %d of %s", line, lineNumber, inputFile));
                return false;
            }
            
            try
            {                                                                                                  
                fname = columns[0].trim();
                lname = columns[1].trim();
                password = columns[2].trim();
                age = Long.parseLong(columns[3].trim());
                email = columns[4].trim();
                key = UUID.fromString(columns[5].trim());
                return true;
            }
            catch (NumberFormatException e)
            {
                System.out.println(String.format("Invalid number in input '%s' at line %d of %s", line, lineNumber, inputFile));
                return false;
            }
        }
    }
}
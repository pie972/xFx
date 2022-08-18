package Assignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringTokenizer;


public class XfxClient {
    public static void main(String[] args) throws IOException {
        
        String command = args[0];
        String fileName = args[1];
        String header;
        String[]  files = new String[100];
        
        Socket connectionToServer = new Socket("localhost", 80);
		// I/O operations
        InputStream in = connectionToServer.getInputStream();
        OutputStream out = connectionToServer.getOutputStream();
        
        BufferedReader headerReader = new BufferedReader(new InputStreamReader(in));
        BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(out));
        
        DataInputStream dataIn = new DataInputStream(in);
        //DataOutputStream dataOut = new DataOutputStream(out);
        
        boolean isExist = false;
        boolean isSameSize = false;
        int size;  
        
        if(command.equals("d")) {
            
            header = "getSize" + fileName + "\n";
            headerWriter.write(header, 0, header.length());
            headerWriter.flush();
            header = headerReader.readLine();
            
            StringTokenizer strk = new StringTokenizer(header, " ");
            String var = strk.nextToken();
            
            size = Integer.parseInt(var);
            
            File actualfile = new File("ClientShare/" + fileName);
            
            if(actualfile.exists()) {  
                isExist = true;
                double realSize = actualfile.length();
                
                if (realSize == size) {
                    System.out.println("File Already exists!");
                    isSameSize = true;
                }
            }
            if(isSameSize == false || isExist == false) {
                
                header = "download" + fileName + "\n";
                headerWriter.write(header, 0, header.length());
		headerWriter.flush();
		header = headerReader.readLine();
                
		if(header.equals("NOT FOUND"))
                    System.out.println("We're extremely sorry, the file you specified is not available!");
                else {
                    if(header.equals("OK")) {
                        
                        byte[] space = new byte[size];
                        dataIn.readFully(space);
                        
                        FileOutputStream file = new FileOutputStream(" ClientShare/ " + fileName);
			file.write(space, 0, size);
			file.close();
                    }
                    else
                        System.out.println("You're not connected to the right Server!");
		}
            }
            
            else if(command.equals("u")){
			//To do
            }
            
            //list of files that are shareable by the server
            else if(command.equals("L")){
                
		header = "list";
		headerWriter.write(header, 0, header.length());
		headerWriter.flush();
                header = headerReader.readLine();
                
                StringTokenizer str = new StringTokenizer(header, " ");
                String status = strk.nextToken();
                
                if(status.equals("OK")) {
                    String var2 = str.nextToken();
                    int length = Integer.parseInt(var2);
                    for (int a = 0; a < length; a++) {
                        header = headerReader.readLine();
                        files[a] = header;
                    }
                    System.out.println("Files:\n" + "\t" + Arrays.toString(files) + "\n");     
                }
                else
                    System.out.println("Empty Folder!");
            }
            
            //check if the file was downloaded completely or partially
            else if(command.equals("C")) {
                
                FileInputStream isFileCompletelyDownloaded = new FileInputStream(" ClientShare/ " + fileName);
		int fSize = isFileCompletelyDownloaded.available();
                
                if(fSize == size)
                    System.out.println("File Completely Downloaded!");
                
                else{    
                    header = "resume" + fileName + fSize + "\n";
                    headerWriter.write(header, 0, header.length());
                    headerWriter.flush();      
                }    
            }
            connectionToServer.close();
        }
    }     
}

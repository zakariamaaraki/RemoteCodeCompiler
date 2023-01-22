import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class ProcessExecution {
    
    public static void main(String[] args) throws Exception {
        ProcessBuilder processbuilder = new ProcessBuilder("ls");
        Process process = processbuilder.start();
        process.wait();
        BufferedReader containerOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String stdOut = readOutput(containerOutputReader);
        System.out.println(stdOut);
    }
    
    public static String readOutput(BufferedReader reader) throws IOException {
        String line;
        StringBuilder builder = new StringBuilder();
        
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        
        return builder.toString();
    }
}

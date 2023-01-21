import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class CreateFiles {
  public static void main(String[] args) {
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < 1000000; i++) {
        str.append("Z");
    }
    String res = str.toString();
    try {
      for (int i = 0; i < 100000; i++) {
        FileWriter myObj = new FileWriter("filename" + i);
        myObj.write(res);
        myObj.close();
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}

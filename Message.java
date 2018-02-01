import java.io.*;

/*
  Basic Object for a MEssage, it should have a name and a text
*/
public class Message implements Serializable {
   public String name;
   public String text;

  public Message(String name, String text) {
      this.name = name;
      this.text = text;
    }

}

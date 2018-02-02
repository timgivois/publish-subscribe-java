import java.io.*;

/*
  Basic Object for a Message, it should have a name, a topic and the text
*/
public class Message implements Serializable {
   public String user;
   public String text;
   public String topic;

  public Message(String user, String topic, String text) {
      this.user = user;
      this.topic = topic;
      this.text = text;
    }

}

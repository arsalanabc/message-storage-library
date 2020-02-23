import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        MessageApp mApp = new MessageApp();
        // these test will only pass for the first run. Delete the sender files and re-run to make them pass
        String id1 = mApp.addMessage("bob", "Hello");// should be bob_1
        String id2 = mApp.addMessage("bob", "World"); // should be bob_2
        String id3 = mApp.addMessage("alice", "Test"); // should be Alice_1
        System.out.println(id1.equals("bob_1"));
        System.out.println(id2.equals("bob_2"));
        System.out.println(id3.equals("alice_1"));

        String msg1 = mApp.readMessage(id2);  //# Should return "World"
        System.out.println(msg1.equals("World"));

        String msg2 = mApp.readMessage(id3);  //# Should return "World"
        System.out.println(msg2.equals("Test"));

        List<String> ids = mApp.findMessages("alice");

        System.out.println(ids);
    }
}

class MessageApp{

    public String addMessage(String senderName, String body){
            Sender senderObj = fetchASenderFromFile(senderName).orElse(new Sender(senderName, new ArrayList<Message>()));

            int counter = senderObj.messages.size()+1;
            String msgID = senderName+"_"+counter;
            Message m = new Message(msgID, body);
            senderObj.messages.add(m);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(senderName);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(senderObj);
                objectOutputStream.close();

                return msgID;

            } catch (Exception e){
                e.printStackTrace();
            }
        return null;
    }

    public List<String> findMessages(String sender){
        return fetchASenderFromFile(sender).get()
                .getMessages()
                .stream()
                .map(Message::getMsgId)
                .collect(Collectors.toList());

    }
//
    public String readMessage(String msgId){
        String[] decodeMsgId = msgId.split("_");
        String senderName = decodeMsgId[0];
        Optional<Sender> optional = fetchASenderFromFile(senderName);
        if(optional.isPresent()){
            Sender sender = optional.get();
            Message m = sender.messages.stream().filter(message -> message.id.equals(msgId)).findFirst().get();
            return m.body;
        };

        return null;
    }

    private boolean senderFileExists(String senderFileName){
        return Files.exists(Paths.get(senderFileName));
    }

    private Optional<Sender> fetchASenderFromFile(String senderName){
        Optional<Sender> senderOptional = Optional.empty();
       try{
           if(senderFileExists(senderName)){
               FileInputStream fileInputStream = new FileInputStream(new File(senderName));
               ObjectInputStream os = new ObjectInputStream(fileInputStream);
               senderOptional = Optional.of((Sender) os.readObject());
               os.close();
           }
       } catch (IOException e){
           e.printStackTrace();
       } catch (ClassNotFoundException e){
           e.printStackTrace();
       }
       return senderOptional;
    }
}

class Sender implements Serializable{
    public String name;
    public List<Message> messages;
    public Sender(String name, ArrayList<Message> messages){
        this.name = name;
        this.messages = messages;
    }
    public List<Message> getMessages(){return this.messages;}
}

class Message implements Serializable{
    public String id;
    public String body;
    public Message(String id, String body){this.id = id; this.body = body;}
    public String getMsgId(){return this.id;}
}
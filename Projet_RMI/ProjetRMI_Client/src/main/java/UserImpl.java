

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class UserImpl implements User {

    private TextArea chat; // pour afficher les messages
    private TextArea qui; //pour afficher qui est conncete
    private TextField messages; // pour entrer le messags
    private int id; // identifier de l'utilisateur
    private JFrame frame;//L'interface
    private Forum Myforum = null; // La refernce de l'objet forum remote
    public UserImpl() {
        frame=new JFrame("HcnChatApp");
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);






        Button connecter = new Button("Se connecter");
        connecter.addActionListener(new Connecter(this));
        frame.add(connecter);



        Button deconnecter = new Button("Se deconnecter");
        deconnecter.addActionListener(new Quitter(this));
        frame.add(deconnecter);

        chat=new TextArea(20,50);
        chat.setEditable(false);
        frame.add(chat);

        messages=new TextField(50);
        frame.add(messages);

        Button Ecrire = new Button("Envoyer");
        Ecrire.addActionListener(new Ecrire(this));
        frame.add(Ecrire);

        Button Qui = new Button("Utilisateurs connectes");
        Qui.addActionListener(new Qui(this));
        frame.add(Qui);

        frame.setSize(500,500);
        chat.setBackground(Color.WHITE);
        frame.setVisible(true);
    }

    public TextArea getChat() {
        return chat;
    }

    public TextField getMessages() {
        return messages;
    }

    public int getId() {
        return id;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Forum getMyforum() {
        return Myforum;
    }

    public void setChat(TextArea chat) {
        this.chat = chat;
    }

    public void setMessages(TextField messages) {
        this.messages = messages;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setMyforum(Forum myforum) {
        Myforum = myforum;
    }

    public void ecrire(String msg) {
        try {
            this.chat.append(msg+"\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public static void main(String args[]) {
        new UserImpl();
    }
}

class Connecter implements ActionListener {
    UserImpl irc;
    public Connecter (UserImpl i) {
        irc = i;
    }
    public void actionPerformed (ActionEvent e) {
        try {
            Forum server = (Forum) Naming.lookup("//"+irc.getMessages().getText()+"/IRCServer");
            irc.setMyforum(server);
            Proxy c=new ProxyImpl(irc);
            irc.setId(irc.getMyforum().entrer(c));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}



class Qui implements ActionListener {
    UserImpl us;
    public Qui (UserImpl i) {
        us = i;
    }
    public void actionPerformed (ActionEvent e) {
        try {
            us.getMyforum().qui();
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }
}


class Ecrire implements ActionListener {
    UserImpl us;
    public Ecrire (UserImpl i) {
        us = i;
    }
    public void actionPerformed (ActionEvent e) {
        try{
            us.getMyforum().dire(us.getId(),us.getMessages().getText());
        }catch( RemoteException ex){
            ex.printStackTrace();
        }
    }
}

class Quitter implements ActionListener {
    UserImpl us;
    public Quitter (UserImpl i) {
        us = i;
    }
    public void actionPerformed (ActionEvent e) {
        try{
            us.getMyforum().quiter(us.getId());

        }catch( RemoteException ex){
            ex.printStackTrace();
        }
    }
}

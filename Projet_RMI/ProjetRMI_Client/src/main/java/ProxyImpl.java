import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ProxyImpl extends UnicastRemoteObject implements Proxy {
    private User user;

    public ProxyImpl() throws RemoteException {
        super();
    }
    public ProxyImpl(User c) throws RemoteException {
        super();
        user = c;
        try{
            String Host = InetAddress.getLocalHost().getHostName();
            Naming.rebind("//"+Host+"/IRCClient", this);
            System.out.println("Server running.......");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ecouter(String msg) throws RemoteException{
        user.ecrire(msg);
    }
}
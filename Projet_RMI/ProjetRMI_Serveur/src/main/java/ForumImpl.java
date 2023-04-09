import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;

public class ForumImpl extends UnicastRemoteObject implements Forum {
        private int i;//id du client;
        private String Host;
        TreeMap<Integer,Proxy> Clients;
        public ForumImpl() throws RemoteException {
            super();
            i=0;
            Clients= new TreeMap<Integer,Proxy>();

            try{
                Host = InetAddress.getLocalHost().getHostName();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public String qui() throws RemoteException {
            dire(0,Clients.keySet().toString());
            return "";
        }

        public int entrer(Proxy P) throws RemoteException{
            i++;
            System.out.println("Client "+i+" est connecté");
            P.ecouter("Client "+i+" est connecté");
            Clients.put(i,P);
            return i;
        }

        public void dire(int id, String msg) throws RemoteException{
            Clients.forEach((k,v)->{
                try {
                    v.ecouter(id+":"+msg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public void quiter(int id) throws RemoteException{
            Proxy P;
            P=Clients.get(id);
            Clients.remove(id);
            P.ecouter("Vous etes deconnecté...");
        }


        public static void main(String args[]) {
            try {
                Registry registry = LocateRegistry.createRegistry(1099);
                Forum server = new ForumImpl();
                String Host = InetAddress.getLocalHost().getHostName();
                Naming.rebind("//"+Host+"/IRCServer", server);
                System.out.println("Server Running...");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
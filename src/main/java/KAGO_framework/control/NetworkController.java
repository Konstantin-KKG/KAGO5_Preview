package KAGO_framework.control;

import KAGO_framework.Config;
import KAGO_framework.model.abitur.netz.Client;
import my_project.control.ProgramController;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

/**
 * Der Network-Controller kann eine Verbindung zu einem Server herstellen. Dazu verwendet er die Abiturklasse Client.
 * Er ist fähig in einem lokalen Netzwerk nach einem offenen Server zu suchen.
 */
public abstract class NetworkController{

    /**
     * Intere Unterklasse der Abiturklasse Client. Dient zum Verbindungsaufbau.
     * Normalerweise wird die Verarbeitung von Servernachrichten direkt in dieser Klasse erledigt. Bei Verwendung
     * des NetworkControllers ist es allerdings auch möglich die Methoden direkt in der implementierten Unterklasse
     * des NetworkControllers zu schreiben.
     * Für eine ausführliche Dokumentation, bitte die Klasse Client betrachten
     */
    private class NetworkClient extends Client {

        // Referenzen
        private NetworkController networkController;

        public NetworkClient(String pServerIP, int pServerPort, NetworkController networkController) {
            super(pServerIP, pServerPort);
            this.networkController = networkController;
        }

        @Override
        public void processMessage(String pMessage) {
            networkController.processServerRequest(pMessage);
            if(Config.DEBUG)System.out.println("Received: "+pMessage);
        }

        @Override
        public void send(String pMessage){
            super.send(pMessage);
            if(Config.DEBUG)System.out.println("Sending: "+pMessage);
        }
    }

    //Referenzen
    protected NetworkClient networkClient;
    protected ProgramController programController;

    //Attribute
    protected String serverIP;
    protected int port;
    protected boolean isWorking;
    protected int maximumCycles;
    protected int currentCycle;

    /**
     * Erzeugt ein neues Objekt der Klasse NetworkController
     * @param programController der zum Framework gehörende ProgramController
     */
    public NetworkController(ProgramController programController){
        port = -1;
        serverIP = null;
        isWorking = false;
        maximumCycles = 20; // Anzahl der Suchdurchläufe
        this.programController = programController;
    }

    /**
     * Initiiert einen Netzwerkscan auf dem entsprechenden Port. Falls ein Server entdeckt wird,
     * wird die IP im String serverIP gesetzt. Solange gescannt wird, ist diese null, wenn es
     * kein Ergebnis gibt, ist sie gleich timeout
     * @param port Der zu prüfende Port
     */
    public void startNetworkScan(int port){
        this.port = port;
        if(!isWorking) {
            this.serverIP = null;
            isWorking = true;
            currentCycle = 0;
            new SwingWorker() {
                @Override
                protected Object doInBackground() {
                    while (serverIP == null && currentCycle < maximumCycles) {
                        currentCycle++;
                        serverIP = scanForServerIP(port);
                        System.out.println("Last scan resulted in: "+serverIP);
                    }
                    if(serverIP == null) serverIP = "timeout";
                    isWorking = false;
                    return null;
                }
            }.execute();
        } else{
            System.out.println("INFO (Network Controller): Der letzte Netzwerk-Scan ist noch nicht abgeschlossen.");
        }
    }

    /**
     * Scannt das lokale Netzwerk nach einem Gerät mit offenem Port und liefert die
     * IP zurück, falls irgend eine Verbindung gelingt.
     * @param port Der zu prüfende Port
     * @return die IP Adresse des gefundenen Servers
     */
    private String scanForServerIP(int port) {
        boolean localhostChecked = false;
        String iIPv4 = getNetworkPartOfIP();
        for (int i = 1; i < 255; i++) {
            try {

                Socket mySocket = new Socket();
                if(i == 254 && !localhostChecked){
                    localhostChecked = true;
                    // Prüfe ob Server auf localhost läuft (Priorität)
                    //System.out.println("Checking IP: "+"127.0.0."+ i);
                    i--;
                    SocketAddress address = new InetSocketAddress("127.0.0.1", port);
                    mySocket.connect(address, 5);
                    // Hier nach ist Connection acquired!
                    mySocket.close();
                    return "127.0.0.1";
                }
                //System.out.println("Checking IP: "+iIPv4 + i);
                SocketAddress address = new InetSocketAddress(iIPv4 + i, port);
                mySocket.connect(address, 5);
                mySocket.close();
                // Hier nach ist Connection acquired!
                return iIPv4+i;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                //System.out.println("No success.");
            }
        }
        return null;
    }

    /**
     * Gibt für die Subnetzmaske 255.255.255.0 den Netzwerkteil der lokalen IP-Adresse zurück.
     * @return der Netzwerkteil, falls bestimmbar
     */
    private String getNetworkPartOfIP(){
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            String[] adressTokens = (ip.getHostAddress().replace(ip.getHostName()+"/","")).split("\\.");
            return adressTokens[0]+"."+adressTokens[1]+"."+adressTokens[2]+".";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Erstellt einen neuen Client, der sich mit dem Server verbindet. Dafür müssen die nötigen Daten
     * (IP und Port) vorliegen.
     */
    public void startConnection(){
        if(serverIP != null && !serverIP.equals("timeout")){
            System.out.println("[#]<~~?~~>[#] NetworkController: Trying to connect to: "+serverIP+" on "+port);
            networkClient = new NetworkClient(serverIP,port,this);
        }
    }

    /**
     * Verwendet den beim Scan verwendeten Port und die angegebene IP
     * für einen sortigen Verbindungsversuch.
     * @param ip IP des Zielsystems
     */
    public void startConnection(String ip){
        if (port != -1){
            this.serverIP = ip;
            startConnection();
        }
    }

    /**
     * Versucht sofort eine Verbindung zum Zielsystem herzustellen.
     * @param ip IP des Zielsystems.
     * @param port Port der Serveranwendung auf dem Zielsystem.
     */
    public void startConnection(String ip, int port){
        this.serverIP = ip;
        this.port = port;
        startConnection();
    }



    /**
     * Zu implementierende Methode, die Reaktionen auf Nachrichten vom Server bestimmt.
     * @param msg Die vom Server eingehende Nachricht als String
     */
    protected abstract void processServerRequest(String msg);

    /**
     * Methode zum Senden von Strings an den Server
     * @param msg die Nachricht, die an den Server gesendet werden soll.
     */
    protected void send(String msg){
        this.networkClient.send(msg);
    }

    /**
     * Die Rückgabe ist null, falls momentan keine IP bekannt ist und gesucht wird. Die Rückgabe ist "Timeout", falls
     * die Suche erfolglos eingestellt wurde.
     * @return die Server IP wie beschrieben
     */
    public String getServerIP() {
        return serverIP;
    }

}

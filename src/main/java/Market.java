import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Market implements Runnable {

    private int noOfClients;
    private int noOfQueues;
    private int simulation;
    private int minArrivalTime;
    private int maxAriivalTime;
    private int minServiceTime;
    private int maxServiceTime;
    private ArrayList<Client> clients;
    private ArrayList<Queue> queues;

    private File file = new File("log.txt");
    private FileWriter myWriter;
    private BufferedWriter out;
    private AtomicInteger period;

    private SimulatorGUI sg ;

    public void setSg(SimulatorGUI sg) {
        this.sg = sg;
    }

    public int getSimulation() {
        return simulation;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public AtomicInteger getPeriod() {
        return period;
    }

    public ArrayList<Queue> getQueues() {
        return queues;
    }

    public Client getFirstClient(){
        return clients.get(0);
    }

    public void deleteFirstClient(){
        clients.remove(0);
        Collections.sort(clients);
    }

    public Market(int a, int b, int c, int d, int e, int f, int g) {
        period = new AtomicInteger();
        this.noOfClients = a;
        this.noOfQueues = b;
        this.simulation=c;
        this.minArrivalTime = d;
        this.maxAriivalTime = e;
        this.minServiceTime = f;
        this.maxServiceTime = g;
        this.period = new AtomicInteger(0);

        try {
            if(file.createNewFile()){
               System.out.println("hurra");
            }
        } catch (IOException ioException) {
            System.out.println("File creating failed");
            ioException.printStackTrace();
        }
        try {
            myWriter = new FileWriter("log.txt");
            out = new BufferedWriter(myWriter);

        } catch (IOException ioException) {
            System.out.println("FileWriter creating failed");
            ioException.printStackTrace();
        }

    }

    public synchronized void printGeneratedList(){
        try{
            myWriter.write("number of clients: " + this.noOfClients +
            "\nnumber of queues: " + this.noOfQueues + "\nsimulation time: "+
            this.simulation + "\nMinimum arrival time: "+
            this.minArrivalTime + "\nMaximum arrival time: "+
            this.maxAriivalTime + "\nMinimum service time: " +
            this.minServiceTime + "\nMaximum servicet time: " +
            this.maxServiceTime + "\nRandom generated client list:\n");
            myWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Client c: clients){
            try {
                myWriter.write(c.toString()+"\n");
                myWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void generateRandom(){
        Random rand = new Random();
        clients = new ArrayList<>(noOfClients);
        for(int i = 0 ; i < noOfClients; ++i) {
            int randArrival = rand.nextInt(maxAriivalTime - minArrivalTime) + minArrivalTime;
            int randService = rand.nextInt(maxServiceTime - minServiceTime) + minServiceTime;
            Client c = new Client(i+1,randArrival,randService);
            clients.add(c);
        }
        Collections.sort(clients);
        for(Client c: clients){
            System.out.println(c.toString());
        }
        printGeneratedList();
        queues = new ArrayList<>(noOfQueues);
        for( int i = 0 ; i < noOfQueues; ++i){
            Queue q = new Queue(i+1,0,period);
            queues.add(q);
        }

        /*for(Queue q: queues){
            System.out.println(q.getIdQueue());
            q.printQueue();
        }*/
    }

    @Override
    public synchronized void run(){

        for(Queue q: this.queues){
            q.start();
        }
        //sg.go();
        while(this.getPeriod().intValue() < this.getSimulation()) {
            //System.out.println();

            this.period.incrementAndGet();
            for(int i = 0; i < clients.size(); ++i){
                if(clients.get(i).getArrival() <= this.period.intValue()) {
                    Collections.sort(this.getQueues());
                    queues.get(0).addClient(clients.get(i));
                    queues.get(0).setNoOfClients(1);
                    queues.get(0).setAveragetime(clients.get(i).getService());
                    queues.get(0).setWaitingTime(queues.get(0).getWaitingTime()+clients.get(i).getService());
                    clients.remove(i);
                    i--;
                }
               // for(Client c: clients){
                    //System.out.println(c.toString());
                //}
            }
            printQueuesInFile();
            printQueue();
           // sg.run();


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(this.getPeriod().intValue() >= this.getSimulation()){
                for(Queue q: this.queues){
                    q.setTimeOut(1);
                }
                //this.sg.setRunningTime(1);
                printLastFile();
            }

        }
    }

    public void printQueuesInFile(){
        String clientsS = "";
        String ququesS = "";
        try {
            myWriter.write("Waiting clients:\n");
            if(!clients.isEmpty()) {
                for (Client c : clients) {
                    myWriter.write(c.toString());
                    clientsS = clientsS + c.toString();
                }
                myWriter.write("\n");
                clientsS = clientsS + "\n";
            }
                else{
                    myWriter.write("empty\n");
                    clientsS = clientsS + "empty\n";
                }
            //this.sg.setJtime(Integer.toString(period.get()));
            myWriter.write("#" + period + "#:\n");
            //this.sg.setText1(clientsS);

            if (!this.queues.isEmpty()) {
                for (Queue q : this.queues) {
                    if (!q.getClients().isEmpty()) {
                        ququesS = ququesS + "Queue:[" + q.getIdQueue() + "]: ";
                        myWriter.write("Queue:[" + q.getIdQueue() + "]: ");
                        for (Client c : q.getClients()) {
                            ququesS = ququesS + c.toString();
                            myWriter.write(c.toString());
                        }
                        ququesS = ququesS + "\n";
                        myWriter.write("\n");
                    }
                    else{
                        ququesS = ququesS + "Queue:[" + q.getIdQueue() + "]: closed\n";
                        myWriter.write("Queue:[" + q.getIdQueue() + "]: closed");
                        myWriter.write("\n");
                    }
                }
            }
           // sg.setText2(ququesS);
            myWriter.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        //sg.setContet();
    }

    public SimulatorGUI getSg() {
        return sg;
    }

    public void printQueue(){
        if(!clients.isEmpty()) {
            for (Client c : clients) {
                System.out.print(c.toString());
            }
            System.out.print("\n");
        }
        else{
            System.out.print("empty\n");
        }
        System.out.print("#" + period + "#:\n");
        if (!this.queues.isEmpty()) {
            for (Queue q : this.queues) {
                if (!q.getClients().isEmpty()) {
                    System.out.print("Queue:[" + q.getIdQueue() + "]: ");
                    for (Client c : q.getClients()) {
                        System.out.print(c.toString());
                    }
                    System.out.print("\n");
                }
                else{
                    System.out.print("Queue:[" + q.getIdQueue() + "]: closed");
                    System.out.print("\n");
                }
            }
        }
    }

    public void printLastFile(){

        for(Queue q: queues) {
            try {
                myWriter.write("Queue:["+q.getIdQueue()+"]: Average waiting time: "+q.getAveragetime()/q.getNoOfClients()+"" +
                        " Maximum at: "+q.getWhen() + " nr clients: "+ q.getMax() + "\n");
                System.out.print("Queue:["+q.getIdQueue()+"]: Average waiting time: "+q.getAveragetime()/q.getNoOfClients()+"" +
                        " Maximum at: "+q.getWhen() + " nr clients: "+ q.getMax() + "\n");
                myWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

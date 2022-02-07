import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue extends Thread implements Comparable{
    private ArrayList<Client> clients = new ArrayList<>();
    private int waitingTime;
    private AtomicInteger time;

    private int max;
    private int when;

    public void setNoOfClients(int noOfClients) {
        this.noOfClients += noOfClients;
    }

    public int getMax() {
        return max;
    }

    public int getWhen() {
        return when;
    }

    public float getAveragetime() {
        return averagetime;
    }

    private float averagetime = 0;
    private int noOfClients = 0;

    public void setAveragetime(float averagetime) {
        this.averagetime += averagetime;
    }


    private int idQueue;
    private int timeOut = 0;

    public int getNoOfClients() {
        return noOfClients;
    }

    public int getIdQueue() {
        return idQueue;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public Queue(int id, int noC, AtomicInteger t){
        this.idQueue = id;
        this.noOfClients = noC;
        this.time = t;
        this.waitingTime = 0;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void addClient(Client c){
        clients.add(c);
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    @Override
    public synchronized void run(){
            while(true && timeOut == 0) {
                if(!clients.isEmpty()){
                    //System.out.println(waitingTime);
                    // time.decrementAndGet();
                    //System.out.println((time.intValue())+": ");
                    if(this.clients.size() > max){
                        max = this.clients.size();
                        when = time.get();
                    }
                    System.out.println();
                     int serviceTime = clients.get(0).getService();
                     clients.get(0).setService(serviceTime-1);
                     if(clients.get(0).getService() == 0){
                         clients.remove(0);
                         if(clients.isEmpty()){
                             this.waitingTime = 0;
                         }
                     }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(timeOut == 1){
                    break;
                }
            }
    }

    @Override
    public int compareTo(Object o) {
        Queue q = (Queue)o;
        if( this.waitingTime == q.getWaitingTime()){
            if(this.idQueue > q.getIdQueue())
                return 1;
            else
                return -1;
        }
        if(this.waitingTime > q.getWaitingTime())
            return 1;
        return -1;
    }

}

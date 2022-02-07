public class Client implements Comparable {
    private int ID;
    private int arrival;
    private int service;

    public Client(int ID, int arrival, int service){
        this.ID = ID;
        this.arrival = arrival;
        this.service = service;
    }

    public int getID() {
        return ID;
    }

    public int getArrival() {
        return arrival;
    }

    public int getService() {
        return service;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public void setService(int service) {
        this.service = service;
    }

    public void subServiceTime(){
        this.service = this.service - 1;
    }

    @Override
    public int compareTo(Object o) {
        Client c = (Client)o;
        if(this.arrival == c.getArrival()) {
            return 0;
        }
        if( this.arrival > c.getArrival())
            return 1;
        return -1;
    }

    @Override
    public String toString(){
        return "(ID: " + this.ID + ", arrival: " +this.arrival + ", service: " + this.service+"); ";
    }

}

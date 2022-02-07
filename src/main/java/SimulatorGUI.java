import javax.swing.*;

import java.io.SyncFailedException;

import static java.lang.Thread.sleep;

public class SimulatorGUI extends JFrame implements Runnable{
    private Market market;
    private JLabel titleLabel = new JLabel("QUEUE SIMULATOR!");
    private JTextField jtime= new JTextField(1);
    private JPanel finalpanel = new JPanel();
    private int runningTime = 0;

    private JLabel client = new JLabel("Clients:");
    private JTextArea text1 = new JTextArea(5,20);
    private JLabel queues = new JLabel("Queues:");
    private JTextArea text2 = new JTextArea(5,20);

    public void setText1(String text) {
        this.text1.setText(text);
        this.setVisible(true);
    }

    public void setText2(String text) {
        this.text2.setText(text);
    }

    public void setJtime(String text){
        this.jtime.setText(text);
    }

    public SimulatorGUI(Market m){
        super("Simulator");
        super.setSize(300,400);
        this.text1.setEditable(false);
        this.text2.setEditable(false);
        this.jtime.setEditable(false);
        this.finalpanel.add(titleLabel);
        finalpanel.add(jtime);
        this.finalpanel.add(client);
        this.finalpanel.add(text1);
        this.finalpanel.add(queues);
        this.finalpanel.add(text2);
        this.finalpanel.setLayout(new BoxLayout(finalpanel,BoxLayout.Y_AXIS));
        this.setContentPane(finalpanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.market = m;
        m.setSg(this);
        setVisible(true);
        //setContet();
        //this.pack();
        //m.generateRandom();
        //m.run();
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    @Override
    public void run() {
        while(true && runningTime == 0) {
            this.setContentPane(finalpanel);
            //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);
            try { sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(runningTime == 1){
                break;
            }
        }
    }
}

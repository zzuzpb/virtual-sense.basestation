

package it.uniurb.disbef.virtualsense.basestation;

import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.samples.fusiontables.cmdline.FusionTablesSample;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lattanzi
 *
 */
public class FusionTableUpdater extends Thread{
  private Hashtable<Short,LinkedList<Packet>> packetsOnSend = new Hashtable<Short,LinkedList<Packet>>(); 
  private Hashtable<Short,LinkedList<Packet>> packetsOnFill = new Hashtable<Short,LinkedList<Packet>>(); 
  private ReentrantLock lock = new ReentrantLock();
  private java.text.DecimalFormat format = new java.text.DecimalFormat("0.00");

  
  public FusionTableUpdater(){
    
  }
  
  @Override
  public void run(){
    while(true){
      this.lock.lock();
      try {
        this.packetsOnSend = this.packetsOnFill;
        this.packetsOnFill = new Hashtable<Short,LinkedList<Packet>>();
        this.lock.unlock();
        
        // process nodesOnSend
        Enumeration<Short> e = this.packetsOnSend.keys();
        while(e.hasMoreElements()){
          short nodeID = e.nextElement().shortValue();
          LinkedList<Packet> p = this.packetsOnSend.get(nodeID);
          // make average on this packet and send to the fusion table
          FusionTableNodeRecord nr = new FusionTableNodeRecord();
          Node nn = BaseStationLogger.nodes.get(nodeID);
          // for each packet 
          for(int i = 0; i < p.size(); i++){
            Packet pa = p.get(i);
            if(nn.hasCapability("Counter"))
              nr.counter = pa.counter;
            if(nn.hasCapability("Noise")  && pa.noise != 0){
              nr.noise += pa.noise;
              nr.noiseCounter++;
            }
            if(nn.hasCapability("CO2") && pa.co2 > 100){
              nr.co2 += pa.co2;
              nr.co2Counter++;
            }
            if(nn.hasCapability("People")){
              nr.in = pa.in;
              nr.out = pa.out;
            }
            if(nn.hasCapability("Pressure")  && (pa.pressure < 1400) && (pa.pressure > 700)){
              nr.pressure += pa.pressure;
              nr.pressureCounter++;
            }
            if(nn.hasCapability("Temp") &&(pa.temperature < 10000)  &&(pa.temperature > 0)){
              nr.temperature += (pa.temperature/100);
              nr.temperatureCounter++;
            }
            if(nn.hasCapability("Light") && (pa.luminosity > 15)){
              nr.luminosity += pa.luminosity;
              nr.luminosityCounter++;
            }            
          } // end for each packet
          if(!p.isEmpty()){
            // send record to the fusion table
            
            
            String tableId = "";
            Table toUpdate = BaseStationLogger.findTableByNodeID(nn.ID);
            
            // send data to fusion tables
            try {
            	System.out.println("Inserting record for node "+nn.ID);
            FusionTablesSample.insertData(toUpdate.getTableId(), System.currentTimeMillis(), 
                  ""+nr.counter, nr.noiseCounter>0?format.format(nr.noise/nr.noiseCounter):"0", 
                  nr.co2Counter>0?format.format(nr.co2/nr.co2Counter):"0", ""+nr.in, ""+nr.out, 
                  nr.pressureCounter>0?format.format(nr.pressure/nr.pressureCounter):"0", 
                  nr.temperatureCounter>0?format.format(nr.temperature/nr.temperatureCounter):"0", 
                  nr.luminosityCounter>0?format.format(nr.luminosity/nr.luminosityCounter):"0");
            } catch (IOException exception) {
              // TODO Auto-generated catch block
              exception.printStackTrace();
            }
          }
        }// end for each node
        
        
        Thread.sleep(1000*60*5);
      } catch (InterruptedException exception) {
        // TODO Auto-generated catch block
        exception.printStackTrace();
      } 
    }
  }
  
  public void addPacket(Packet p){
    this.lock.lock();
    try{
      if(!this.packetsOnFill.containsKey(p.sender)){
        this.packetsOnFill.put(p.sender, new LinkedList<Packet>());
      }    
      this.packetsOnFill.get(p.sender).add(p);
    }finally {
      this.lock.unlock();
    }    
  }

}
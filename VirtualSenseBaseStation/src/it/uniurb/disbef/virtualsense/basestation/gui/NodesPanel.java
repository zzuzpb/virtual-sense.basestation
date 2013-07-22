package it.uniurb.disbef.virtualsense.basestation.gui;



import it.uniurb.disbef.virtualsense.basestation.Node;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * 
 * @author  lattanzi
 */
public class NodesPanel extends javax.swing.JPanel implements MouseListener {
    private Hashtable<Short, Node> tableNodes;
    //private DataMsg actualMessage;
    private Node[] nodes;
    //private Node sink;
    private java.text.DecimalFormat format = new java.text.DecimalFormat("0.00");
    
    public double maxLati;
    public double minLati;
    public double maxLongi;
    public double minLongi;
    private int screenHeight = 1100;
    private int screenWidth = 1150;
    //private NodeFlow flowToPlot = null;
    private int maxFlow = 0;
    private boolean plottingFlow = false;
    private boolean routed = false;    
    private BufferedImage image;

    /** Creates new form NewJPanel */
    public NodesPanel(Hashtable<Short, Node> nodes) {
    	this.tableNodes = nodes;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
    	try {                
    		//System.out.println("Drawing image");
            image = ImageIO.read(new File("image.png"));
         } catch (IOException ex) {
             System.out.println(ex);
         }
    
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        addMouseListener(this);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    public void paintComponent(Graphics g){
		super.paintComponent(g); 
				if(image != null)
					g.drawImage(image, 0, 0, 745,720,null); // see javadoc for more info on the parameters 
		 		g.setColor(Color.LIGHT_GRAY);
                //g.fillRect(0, 0, this.getWidth(), this.getHeight());
                //if(this.plottingFlow)
                //    this.drawFlow(g);
                this.drawNodes(g);
                //this.drawLinks(g);
                
                
        
    }
    
    private void drawNodes(Graphics g){
        // draw nodes
        if(this.tableNodes != null){
            this.nodes = new Node[this.tableNodes.size()];
            Enumeration e = this.tableNodes.elements();

            while(e.hasMoreElements()){

                Node nn = (Node)e.nextElement();
               /* if(nn.type == Node.SINK)
                    g.setColor(Color.CYAN);
                else*/
                g.setColor(new Color(135,206,250,100));                            
                //String address = nn.getShortStringID();
                String address = ""+nn.ID;
                g.fillOval(nn.xLocation, nn.yLocation, 30, 30);
                g.setColor(Color.darkGray);
                /*if(SentillaBaseStationView.nodeIdCheckBox.getState())
                    g.drawString("ID: "+address, nn.xLocation+40, nn.yLocation+50); */
                
                /*if(SentillaBaseStationView.packetSentCheckBox.getState())
                     g.drawString(nn.arrived+"/"+nn.sent, nn.x+40, nn.y+90);
                if(SentillaBaseStationView.packetRoutedCheckBox.getState())
                     g.drawString(""+nn.forwarded, nn.x+90, nn.y+90);
                if(SentillaBaseStationView.tempCheckBox.getState())
                     g.drawString("T: "+format.format(nn.temp), nn.x+40, nn.y+105);
                if(SentillaBaseStationView.voltageCheckBox.getState())
                     g.drawString("V: "+format.format(nn.v), nn.x+40, nn.y+120);*/
              
            }
           
        }
    }
    
    /*private void drawLinks(Graphics g){
        //draws link (message)
        if(this.actualMessage != null){
            //g.setColor(Color.BLACK);
            long sendingNode = this.actualMessage.srcAddress;
            Node n = this.tableNodes.get(new Long(sendingNode));                   
            g.setColor(Color.RED);
            g.fillOval(n.x+50, n.y+50, 20, 20);
            //System.out.println("Found node: "+index);
            if(this.actualMessage.destAddress == 0xFFFFFFFFFFFFFFFFL){
                g.drawOval(n.x+40, n.y+40, 50, 50);
                g.drawOval(n.x+30, n.y+30, 70, 70);
                g.drawOval(n.x+20, n.y+20, 90, 90);
            }else {
                if(this.actualMessage.hops == 0){                                            
                    g.drawLine(n.x+65, n.y+65, this.sink.x+65, this.sink.y+65);
                    g.fillOval(this.sink.x+60, this.sink.y+60, 10 , 10);
                }else {
                    for(int i = 0; i < this.actualMessage.hops; i++){
                        Node nn = findNode(this.actualMessage.nodes[i]);                            
                        g.drawLine(n.x+65, n.y+65, nn.x+65, nn.y+65);
                        g.fillOval(nn.x+60, nn.y+60, 10 , 10);
                        n = nn;
                    }
                    g.drawLine(n.x+65, n.y+65, this.sink.x+65, this.sink.y+65);
                    g.fillOval(this.sink.x+60, this.sink.y+60, 10 , 10);
                }
            }

        }
    } */
    
    /*private void drawFlow(Graphics g){
        if(this.flowToPlot != null){
            Enumeration<LinkFlow> en;
            en = this.flowToPlot.getLinkFlows();
            while(en.hasMoreElements()){
                if(!this.routed)
                g.setColor(Color.MAGENTA);
                LinkFlow l = en.nextElement();
                Node src = findNodeByNumber(l.srcId);
                Node dest = findNodeByNumber(l.destId);
                if(this.routed){
                    g.setColor(Color.GREEN);
                    if(src.getNumber() == this.flowToPlot.getNodeID())
                        g.setColor(Color.RED);
                }
                // drawing flow 
                	//generate polygon points
                int x[] = new int[4];
                int y[] = new int[4];
                x[0] = src.x+60;
                x[1] = src.x+60;
                x[2] = dest.x+60;
                x[3] = dest.x+60;

                y[0] = src.y+60;//(int);(this.nodes[this.edges[i].Sn].y*SCALE);
                y[1] = src.y+60;//(int);(this.nodes[this.edges[i].Sn].y*SCALE);
                y[2] = dest.y+60;//(int);(this.nodes[this.edges[i].Dn].y*SCALE);
                y[3] = dest.y+60;//(int);(this.nodes[this.edges[i].Dn].y*SCALE);

                double DyDx = (double)(y[2]-y[0])/(double)(x[2]-x[0]);
                DyDx = Math.cos(Math.atan(DyDx));
                //System.out.println("DDDDD DyDx "+DyDx);

                
                double flowRap = ((double)l.value)/this.maxFlow;
                //double flowRap = Math.log10(l.value);///this.maxFlowlow;

                int dx = (int)((1-DyDx)*(flowRap)*10);
                int dy = (int)((DyDx)*(flowRap)*10);

                if(dx == 0)
                        dx =2;
                if(dy == 0)
                        dy = 2; 
                //System.out.println("Dx "+dx+" Dy "+dy+" FlowRap "+flowRap+" MaxF "+this.maxFlow);
                //int dx = (int)((1-DyDx)*5);
                //int dy = (int)((DyDx)*5);                
                x[0] = x[0]- dx;
                x[1] = x[1]+ dx;
                x[2] = x[2]+ dx;
                x[3] = x[3]-dx;

                y[0] = y[0]- dy;
                y[1] = y[1]+ dy;
                y[2] = y[2]+ dy;
                y[3] = y[3]- dy;
                g.fillPolygon(x,y, 4);
                //System.out.println("Flow between "+src.getNumber()+"x: "+src.x+" and "+dest.getNumber()+" x: "+dest.x+" done ");
            }
        }
    }
    
    public void plotFlow(NodeFlow flow, boolean routed, int maxFlow){
        this.flowToPlot = flow;
        this.plottingFlow = true;
        this.routed = routed;
        this.maxFlow = maxFlow;
        System.out.println("Plotting "+(this.routed?"routed":"generated" )+" flow of "+this.flowToPlot.getNodeID());
        this.flowToPlot.printFlow();
        System.out.println("----------------");
        this.repaint();
    }
    
    public void updateMessage(DataMsg m){
        this.actualMessage = m;
        
    } */
    
    /*public void updateNodes(Hashtable<Long, Node> t){
        this.tableNodes = t;
        Enumeration e = this.tableNodes.elements();
        //System.out.println("Diff lat "+(this.maxLati-this.minLati));
        //System.out.println("Diff lon "+(this.maxLongi-this.minLongi));
        double maxD = this.maxLati-this.minLati;
        if((this.maxLongi-this.minLongi) > maxD)
            maxD = this.maxLongi-this.minLongi;
        while(e.hasMoreElements()){
            Node n = (Node)e.nextElement();
            n.y = ((int)(n.getLatitude()*this.screenHeight/this.maxLati))-(int)this.minLati*2;//(int)(((n.getLatitude()-this.minLati)/maxD)*this.screenHeight); //screenHeight
            //System.out.println(" sh "+this.screenHeight+" mL "+this.minLati);
            //System.out.println("Node "+n.getShortStringID()+" lx "+(n.getLatitude()-this.minLati));
            //System.out.println("Node "+n.getShortStringID()+" y "+n.y);
            n.x = ((int)(n.getLongitude()*this.screenWidth/this.maxLongi))-(int)this.minLongi/3;//(int)(((n.getLongitude()-this.minLongi)/maxD)*this.screenWidth); //screenWidth
            //System.out.println(" sw "+this.screenWidth+" mLong "+this.minLongi);
           // System.out.println("Node "+n.getShortStringID()+" x "+n.x);
            
        }
    }    */
    
    public Node findNode(short id){        
        
        Node ret = null;
        Enumeration e = this.tableNodes.elements();
        while(e.hasMoreElements()){
            Node nn = (Node)e.nextElement();            
            if(nn.ID == id){
                ret = nn;
                break;
            }
        }
        return ret;        
    }
    public Node findNodeByPosition(Point p){        
        
        Node ret = null;
        double distance = Double.MAX_VALUE;
        Enumeration e = this.tableNodes.elements();
        while(e.hasMoreElements()){
            Node nn = (Node)e.nextElement();     
            double newDistance = p.distance(nn.xLocation, nn.yLocation);
            if(newDistance < distance){
                ret = nn;
                distance = newDistance;
                //break;
            }
        }
        return ret;        
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int x = e.getX();
		int y = e.getY();
		Node nn  = findNodeByPosition(new Point(x,y));
		System.out.println("Clicked to "+x+" "+y);
		System.out.println("Selected node is "+nn.ID);
		
		// choose what to plot
		Object[] possibilities = {"Counter", "Noise", "CO2"};		
		String s = (String)JOptionPane.showInputDialog(
		                    this,
		                    "Select the potting value",
		                    "Plotting selection Dialog",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    possibilities,
		                    "Counter");
		
	if ((s != null) && (s.length() > 0)) {
		TimeSerieGraph timeSerie = new TimeSerieGraph();
		timeSerie.createTimeSeriesXYChart(nn, s, this);
	}
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
    
    
}

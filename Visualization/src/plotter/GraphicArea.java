/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plotter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JScrollPane;

/**
 *
 * @author Fernando Soares de Aguiar Neto
 */

/*
* OBS: EVERY implementation of Graphic area have to call super.initComponent() in order to print the frame.
* Otherwise the frame will not show up.
*
*/
abstract class GraphicArea extends javax.swing.JPanel{

    /**
     * Creates new form GraphicArea
     */
    protected double scale;
    
    protected final int ORIGIN_X = 40;
    
    protected final int RANGE0_1 = 5; //range (in pixels) between the 1 cut and the origin. Used in Log10 rescale.
    
    public static final int NO_RESCALE = 0;
    //public static final int UNIFORM_RESCALE = 1; 
    //public static final int MEDIAN_RESCALE  = 2;
    //public static final int LOG10_RESCALE = 3;
    public static final int LOG1P_RESCALE = 1;
    public static final int LOG10_RESCALE = 2;
    
    protected ArrayList<ArrayList<Integer>> matrix; 
    protected ArrayList<Double> densities;
    protected HashMap<Double, Integer> yCutsInUse; //maps a value to a pixel in the y Axis. This indicates the scale in use at the moment.
    protected HashMap<Double, Integer> yCutsLogN; //maps a value to a pixel in the y Axis. This indicates the natural log scale.
    protected HashMap<Double, Integer> yCutsLog10; //maps a value to a pixel in the y Axis. This indicates the log 10 scale.
    protected HashMap<Double, Integer> yCutsNoRescale; //maps a value to a pixel in the y Axis. This indicates the scale without changes (linear).
    
    protected Integer maxClusterID;
    protected Color[] colors;
    protected Graphics2D g2d;
    protected String yLabel;
    protected int vScaling;
    protected int elemDist;
    protected JScrollPane parentScrollPane;
    
    public GraphicArea(ArrayList<ArrayList<Integer>> matrix, ArrayList<Double> eps, int elemDist)
    {
        this.matrix = matrix;
        this.densities = eps;
        this.yLabel = "Eps";
        this.vScaling = NO_RESCALE;
        this.elemDist = elemDist;
        
        this.initComponents();
    }
    
    public GraphicArea(ArrayList<ArrayList<Integer>> matriz, ArrayList<Double> eps, Integer maxClusterID, String yLabel, int elemDist) {
        this.matrix = matriz;
        this.densities = eps;
        this.maxClusterID = maxClusterID;
        this.yLabel = yLabel;
        this.vScaling = NO_RESCALE;
        this.elemDist = elemDist;
        

        this.initComponents();
    }
    
    //this one is called when some child Overrides the method paintComponent(Graphics g),
    //in order to avoid multiple prints.
    protected void paintComponentALT(Graphics g)
    {
        super.paintComponent(g);
    }

    @Override
     protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      this.g2d = (Graphics2D)g.create();
      
      this.g2d.drawLine(this.ORIGIN_X,getHeight()-40,40,40);
      
      this.elemDist = Math.max(1,(int)Math.floor((getWidth()-80)/this.matrix.size()));
      
      this.plotYAxis();
      
      //There is no need to draw a x axis, since the bars will fill all x axis. And the axis line will be overwritten.
      //drawing bars
      int x = 40;
      for(int i = 0; i < this.matrix.size(); i++) {
          int wasPrint = this.plotMiddle(i, x, this.elemDist);
          if(wasPrint == 1)
          {
              x = x + this.elemDist;
          }
      }
      
      //drawing labels.
      this.g2d.drawString("Objects", (getWidth()-40)/2, getHeight()-20);
      this.g2d.translate(20.0, (getHeight()/2)-20); 
      this.g2d.rotate(300);  
      this.g2d.drawString(this.yLabel, 0, 0);
     
      revalidate();
      this.g2d.dispose();
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        this.elemDist = Math.max(1,(int)Math.floor((preferredSize.getWidth()-80)/this.matrix.size()));
        preferredSize.setSize(this.elemDist*this.matrix.size() + 80, preferredSize.getHeight());
        super.setPreferredSize(preferredSize); //To change body of generated methods, choose Tools | Templates.
    }
     
    public Color getColor(int i)
    {
        return this.colors[i % this.colors.length];
    }
    
    public void setElemDist(int elemDist)
    {
        this.elemDist = Math.max(1, elemDist);
    }
           
    public void setVScaling(int vs)
    {
        this.vScaling = vs;
    }
    
    abstract void plotYAxis();
    
    //Responsible for printing the each bar. Each calling to this method creates the column regarding the object of index i.
    //i = index of the object
    //posX = Axis X position.
    //elemDist = width of each bar.
    //It will return 0 if no bar was print.
    abstract int plotMiddle(int i, int posX, int elemDist);
    
    /*public void zoom(MouseWheelEvent e)
    {
        if(e.isControlDown())
        {
            if(e.getButton() == e.BUTTON3)
            {
                this.scale = 1;
                this.setSize((int)Math.ceil(1280*scale), (int)Math.ceil(640*scale));
            }
            else
            {
                this.scale = this.scale - 0.5*e.getPreciseWheelRotation();
                this.setSize((int)Math.ceil(1280*scale), (int)Math.ceil(640*scale));
            }
            
        }
        
    }*/
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    protected void initComponents() {

		setMaximumSize(new java.awt.Dimension(2147483647,2147483647));
	
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
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

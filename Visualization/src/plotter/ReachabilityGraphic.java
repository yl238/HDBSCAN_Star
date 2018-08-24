/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plotter;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Fernando Soares de Aguiar Neto.
 */
public class ReachabilityGraphic extends GraphicArea{
    
    private double[] rDist;
    
    public ReachabilityGraphic(ArrayList<ArrayList<Integer>> matrix, ArrayList<Double> densities, int elemDist) {        
        super(matrix, densities, elemDist);       
        
        ReachabilityPlot rp = new ReachabilityPlot(densities, matrix);
        this.rDist = rp.getReachabilityDistances();
        
        //Generating yCuts
        //drawing axis
          //y axis
        //cleanning structures
        this.yCutsInUse = new HashMap<Double, Integer>();
        this.yCutsInUse.clear();
        this.yCutsLogN = new HashMap<Double, Integer>();
        this.yCutsLogN.clear();
        this.yCutsLog10 = new HashMap<Double, Integer>();
        this.yCutsLog10.clear();
        this.yCutsNoRescale = new HashMap<Double, Integer>();
        this.yCutsNoRescale.clear();    
       
        switch(this.vScaling)
        {
            case NO_RESCALE: this.yCutsInUse = this.yCutsNoRescale; break;
            case LOG1P_RESCALE: this.yCutsInUse = this.yCutsLogN; break;
            case LOG10_RESCALE: this.yCutsLog10 = this.yCutsLog10; break;
        }
        
        super.initComponents();
    }
    
    @Override
    protected void plotYAxis()
    {     
      double y;
      
      switch(this.vScaling)
      {
            //no rescale
            case NO_RESCALE:{   
                for(int i = 0; i < this.densities.size(); i++)
                {
                    //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                    double norm = (this.densities.get(i))/(this.densities.get(0));
                    //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                    y = (1-norm)*((getHeight()-80)) + 40;
                    this.yCutsNoRescale.put(this.densities.get(i),(int)y);
                    //this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));
                }
        
                this.yCutsNoRescale.put(0.0,getHeight()-40);

                //indicating that the scale used is No rescale;
                this.yCutsInUse = this.yCutsNoRescale;

                //drawing scale
                //notice that I ignore the last line where density = 0.0
                double maxValue = this.densities.get(0);
                for(int i = 0; i <=10; i++)
                {
                    y = (1 - ((0.1)*i))*((getHeight()-80)) + 40;
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));
                    this.g2d.drawString(String.format("%.3f",(0.1*i)*maxValue), ORIGIN_X-40 , (int)y);
                }

                }break;
          
          //Rescaling uniformily the cuts in the Y axis.
          /*case UNIFORM_RESCALE:   int cutQtd = this.densities.size();

                    for(int i = 0; i < cutQtd ; i++)
                    {
                        double step = ((double)(i))/(double)(cutQtd);
                        y = (step)*((getHeight()-80)) + 40;
                        this.yCuts.put(this.densities.get(i),(int)y);
                        this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));                
                    }
                    break;
              
         
          //Rescaling putting the median in the middle of the Y axis, and ajusting the rest accordingly.
          case MEDIAN_RESCALE:   //Discovering Median value.
                    
                    double median;
                    int middleIdx;
                    if(this.densities.size() % 2 == 0)
                    {   
                        middleIdx = (int)Math.floor(this.densities.size()/2) -1;
                        median = (this.densities.get(middleIdx)
                                    +
                                 this.densities.get(middleIdx +1)) / 2.0;
                    }
                    else 
                    {
                        middleIdx = (int)Math.floor(this.densities.size()/2) -1;
                        median = this.densities.get(middleIdx);
                    }

                    double maxValue = this.densities.get(0);
                    
                    //Using (y - y0) = m(x-x0) in order to discover the two equations
                    double m1 = (maxValue/2.0)/(maxValue-median);
                    double m2 = (maxValue/2.0)/(median);
                    double b1 = maxValue - m1*maxValue;
                    
                    for(int i = 0; i <= middleIdx ; i++)
                    {
                        double newDensitieValue = m1*this.densities.get(i) + b1;
                        //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                        double norm = (newDensitieValue)/(maxValue);
                        //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                        y = (1-norm)*((getHeight()-80)) + 40;
                        //notice that I put densities.get(i) on the dictionary instead of the newValue.
                        this.yCuts.put(this.densities.get(i),(int)y);
                        this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));                
                    }
                    
                    for(int i = middleIdx+1; i<this.densities.size(); i++)
                    {
                        double newDensitieValue = m2*this.densities.get(i);
                        //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                        double norm = (newDensitieValue)/(maxValue);
                        //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                        y = (1-norm)*((getHeight()-80)) + 40;
                        //notice that I put densities.get(i) on the dictionary instead of the newValue.
                        this.yCuts.put(this.densities.get(i),(int)y);
                        this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));   
                    }
                    break;
                         */
            case LOG1P_RESCALE:{
                //We add 1 to every density in order to avoid values between 0 and 1. Given that a densitie will never be negative I can be sure that adding 1 will give me numbers at least greater than 1.
                double maxLog = Math.log1p(this.densities.get(0));
                double minLog = Math.log1p(this.densities.get(this.densities.size()-1));
                double range = maxLog - minLog;
                int idx;
                for(idx = 0; (idx < this.densities.size()); idx++)
                {
                    //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                    double norm = (Math.log1p(this.densities.get(idx)) - minLog)/(range);
                    //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                    y = (1-norm)*((getHeight()-80-RANGE0_1)) + 40;
                    //notice that I don't add 1 when I put on yCuts.
                    this.yCutsLogN.put(this.densities.get(idx),(int)y);
                    //In this plot the cuts are not shown on the Y axis
                    //this.g2d.drawLine(this.ORIGIN_X-3, (int)(y), this.ORIGIN_Y + 3, (int)(y));
                }

                this.yCutsLogN.put(0.0,getHeight()-40);
                
                //indicating that the scale used is LogN;
                this.yCutsInUse = this.yCutsLogN;
                
                //Writing scale number.
                //the first cut point is equal to the second smallest density. On the smallest density everyone is noise.
                maxLog = Math.log1p(this.densities.get(0));
                minLog = Math.log1p(this.densities.get(this.densities.size()-1));
                range = maxLog - minLog;
                
                double cutPoint = this.densities.get(this.densities.size()-2);
                while(cutPoint < this.densities.get(0))
                {
                    //here we add 1 to cutPoint for the same reason as we add 1 to densities above.
                    double norm = (Math.log1p(cutPoint)-minLog)/(range);
                    y = (1-norm)*((getHeight()-80)) + 40;
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));
                    this.g2d.drawLine(this.ORIGIN_X-5, (int)y, this.ORIGIN_X + 5, (int)y);
                    this.g2d.drawString(String.format("%.3f", cutPoint), ORIGIN_X-40 , (int)y);
                    cutPoint = cutPoint*Math.E;
                }

                this.g2d.drawLine(this.ORIGIN_X-5, 40, this.ORIGIN_X + 5, 40);
                //writing the last one.
                this.g2d.drawString(String.format("%.3f", this.densities.get(0)), ORIGIN_X-40 , 40);
                }break;    
              
            case LOG10_RESCALE:{
                //We add 1 to every density in order to avoid values between 0 and 1. Given that a densitie will never be negative I can be sure that adding 1 will give me numbers at least greater than 1.
                double maxLog = Math.log10(this.densities.get(0) +1);
                double minLog = Math.log10(this.densities.get(this.densities.size()-1)+1);
                double range = maxLog - minLog;

                int idx;
                for(idx = 0; (idx < this.densities.size()); idx++)
                {
                    //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                    double norm = (Math.log10(this.densities.get(idx) +1) - minLog)/(range);
                    //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                    y = (1-norm)*((getHeight()-80-RANGE0_1)) + 40;
                    //notice that I don't add 1 when I put on yCuts.
                    this.yCutsLog10.put(this.densities.get(idx),(int)y);
                }
                this.yCutsLog10.put(0.0,getHeight()-40);

                //indicating that the scale used is Log10;
                this.yCutsInUse = this.yCutsLog10;
                
                //Writing scale number.
                //the first cut point is equal to the second smallest density. On the smallest density everyone is noise.
                maxLog = Math.log10(this.densities.get(0) +1);
                minLog = Math.log10(this.densities.get(this.densities.size()-1)+1);
                range = maxLog - minLog;

                double cutPoint = this.densities.get(this.densities.size()-2);
                while(cutPoint < this.densities.get(0))
                {
                    //here we add 1 to cutPoint for the same reason as we add 1 to densities above.
                    double norm = (Math.log10(cutPoint +1)-minLog)/(range);
                    y = (1-norm)*((getHeight()-80)) + 40;
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));
                    this.g2d.drawLine(this.ORIGIN_X-5, (int)y, this.ORIGIN_X + 5, (int)y);
                    this.g2d.drawString(String.format("%.3f", cutPoint), ORIGIN_X-40 , (int)y);
                    cutPoint = cutPoint*10;
                }

                this.g2d.drawLine(this.ORIGIN_X-5, 40, this.ORIGIN_X + 5, 40);
                //writing the last one.
                this.g2d.drawString(String.format("%.3f", this.densities.get(0)), ORIGIN_X-40 , 40);
                }break;
          //no rescale
          default:  {
                    for(int i = 0; i < this.densities.size(); i++)
                    {
                        //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                        double norm = (this.densities.get(i))/(this.densities.get(0));
                        //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                        y = (1-norm)*((getHeight()-80)) + 40;
                        this.yCutsInUse.put(this.densities.get(i),(int)y);
                        this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));
                    }
                    }break;
              
      }
     
      this.g2d.drawLine(this.ORIGIN_X-2, getHeight()-40, this.ORIGIN_X + 2, getHeight()-40); //originLine
      
    }
    
    @Override
    protected int plotMiddle(int i, int posX, int elemDist)
    {
        //Stores g2d.color and g2d.paint before the calling of this method in order to restore them after the method ends.
        Color lastColor = g2d.getColor();
        Paint lastPaint = g2d.getPaint();
    
        double h1 = this.yCutsInUse.get(new Double(this.rDist[i]));

        double height = (getHeight()-40) - h1;
        Rectangle r = new Rectangle ( posX,
                                      (int)java.lang.Math.floor(h1),
                                      elemDist,
                                      (int)java.lang.Math.floor(height));

        this.g2d.setColor(new Color(0, 0, 255));
        this.g2d.setPaint(new Color(0, 0, 255));
        this.g2d.fill(r);
        this.g2d.draw(r);

        this.g2d.setColor(lastColor);
        this.g2d.setPaint(lastPaint);
        
        return 1;
    }
}

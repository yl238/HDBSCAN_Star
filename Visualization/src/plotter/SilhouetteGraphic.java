/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plotter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Fernando Soares de Aguiar Neto.
 */
public class SilhouetteGraphic extends GraphicArea{
    
    protected ArrayList<ObjectSilhouetteInfo> silhouetteInfos;
    protected double minDistanceValue;
    
    protected int hs;
    protected int k;
    protected int Htype;
    //supported horizontal sampling types
    public final int NO_HSAMPLE = 0; //maintain all the data,.
    public final int SMALL_SAMPLE = 1; //maintain less than 50% (inclusive)
    public final int BIG_SAMPLE = 2;   //maintain more than 50% 
    
    public final int HS_FACTOR = 2; //percentage ammount of objects to be (in)decreased in each zoom (in)Out.
    private double xAxisQtd;
    
    public SilhouetteGraphic(ArrayList<ArrayList<Integer>> matrix, ArrayList<Double> densities, Integer maxClusterID, String yLabel, Color[] colors, int elemDist) {        
        super(matrix, densities, maxClusterID, yLabel, elemDist);       
        try
        {
            SilhouettePlot sp = new SilhouettePlot(densities, matrix);
            this.silhouetteInfos = sp.getDensities();
            this.minDistanceValue = sp.getMinDistanceValue();
            
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        this.colors = colors;
        //DEBUG
        //System.out.println(Arrays.toString(this.colors));
        
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

        //No Rescale
        
        
        //Log Natural
        
        
        //Log 10
        
        //End Generation
        
        switch(this.vScaling)
        {
            case NO_RESCALE: this.yCutsInUse = this.yCutsNoRescale; break;
            case LOG1P_RESCALE: this.yCutsInUse = this.yCutsLogN; break;
            case LOG10_RESCALE: this.yCutsInUse = this.yCutsLog10; break;
        }
        
        this.setHs(100);
        super.initComponents();
    }
    
    @Override
    protected void plotYAxis()
    {     
        double y;
        switch(this.vScaling)
        {
            
            //No Y axis rescale.
            case NO_RESCALE:
            {
                for(int i = 0; i < this.densities.size()-1 ; i++)
                {
                    //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                    double norm = (1/this.densities.get(i))/(1/this.densities.get(this.densities.size()-2));
                    //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                    y = (1-norm)*((getHeight()-80)) + 40;
                    this.yCutsNoRescale.put(1/this.densities.get(i),(int)y);
                    //this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));

                }
        
                this.yCutsNoRescale.put(0.0,getHeight()-40);
                
                //indicating that the scale used is No rescale;
                this.yCutsInUse = this.yCutsNoRescale;
                
                //notice that I ignore the last line where density = 0.0
                double scaleQtd = Math.ceil(this.scale*10);
                double maxValue = 1/this.densities.get(this.densities.size()-2);
                for(int i = 0; i <=10; i++)
                {
                    y = (1-(i*0.1))*((getHeight()-80)) + 40;
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));
                    this.g2d.drawString(String.format("%.3f",(i*0.1)*maxValue), ORIGIN_X-40 , (int)y);
                }   

            }break;
          /*    
          //Rescaling uniformily the cuts in the Y axis.
          case UNIFORM_RESCALE: 
                //cut Qtd is the number of densities except the last one (that has only noise)
                int cutQtd = this.densities.size();
                
                for(int i = 0; i < cutQtd ; i++)
                {
                    double step = ((double)(cutQtd-1)-i)/(double)(cutQtd);
                    y = (step)*((getHeight()-80)) + 40;
                    this.yCuts.put(1/this.densities.get(i),(int)y);
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));                
                }
                break;
              
           //Rescaling putting the median in the middle of the Y axis, and ajusting the rest accordingly.
          case MEDIAN_RESCALE:
                //Discovering Median value.
                double median;
                int middleIdx;
                //remembering that the last density is not show because it is full of noise
                if((this.densities.size()-1) % 2 == 0)
                {   
                    middleIdx = (int)Math.floor((this.densities.size()-1)/2);
                    median = ((1.0/this.densities.get(middleIdx))
                                +
                             (1.0/this.densities.get(middleIdx +1))) / 2.0;
                }
                else 
                {
                    middleIdx = (int)Math.floor((this.densities.size()-1)/2);
                    median = 1.0/this.densities.get(middleIdx);
                }

                double maxValue = 1.0/(this.densities.get(this.densities.size()-2));

                //Using (y - y0) = m(x-x0) in order to discover the two equations
                double m1 = (maxValue/2.0)/(median);
                double m2 = (maxValue/2.0)/(maxValue-median);
                double b2 = maxValue - m2*maxValue;
                

                for(int i = 0; i <= middleIdx ; i++)
                {
                    double newDensitieValue = m1*(1.0/this.densities.get(i));
                    //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                    double norm = (newDensitieValue)/(maxValue);
                    //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                    y = (1-norm)*((getHeight()-80)) + 40;
                    //notice that I put densities.get(i) on the dictionary instead of the newValue.
                    this.yCuts.put(1/this.densities.get(i),(int)y);
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));                
                }

                for(int i = middleIdx+1; i<this.densities.size(); i++)
                {
                    double newDensitieValue = m2*(1.0/this.densities.get(i)) + b2;
                    //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                    double norm = (newDensitieValue)/(maxValue);
                    //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                    y = (1-norm)*((getHeight()-80)) + 40;
                    //notice that I put 1/densities.get(i) on the dictionary instead of the 1/newValue.
                    this.yCuts.put(1/this.densities.get(i),(int)y);
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));   
                }
                break;  
              */
          case LOG1P_RESCALE:{
                //We add 1 to every density in order to avoid values between 0 and 1. Given that a densitie will never be negative I can be sure that adding 1 will give me numbers at least greater than 1.
                double minLog = Math.log1p(this.densities.get(this.densities.size()-2));
                double maxLog = Math.log1p(this.densities.get(0));
                double range = (maxLog-minLog);

                int idx;
                for(idx = 0; idx < this.densities.size(); idx++)
                {
                    //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                    double norm = (Math.log1p(this.densities.get(idx))-minLog)/(range);
                    //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                    y = (norm)*((getHeight()-80-RANGE0_1)) + 40;
                    this.yCutsLogN.put(1/this.densities.get(idx),(int)y);
                    //In this plot the cuts are not shown on the Y axis
                    //this.g2d.drawLine(this.ORIGIN_X-3, (int)(y), this.ORIGIN_Y + 3, (int)(y));
                }
        
                this.yCutsLogN.put(0.0,getHeight()-40);

                //indicating that the scale used is Log Natural;
                this.yCutsInUse = this.yCutsLogN;

                //Writing scale number.
                //the greatest silhouette has only noise.
                minLog = Math.log1p(this.densities.get(this.densities.size()-2));
                maxLog = Math.log1p(this.densities.get(0));
                range = (maxLog-minLog);
                
                double cutPoint = this.densities.get(this.densities.size() -2);
                while(cutPoint <= this.densities.get(0))
                {
                    //Adding 1 to cutPoint for the same reason as we add 1 to densitites above.
                    double norm = (Math.log1p(cutPoint)-minLog)/(range);
                    //the first cut will return norm = 0 beacuse log10(1) == 0, so is neeed to reajust the 1 cut in order keep it away from the origin.
                    y = (norm)*((getHeight()-80-RANGE0_1)) + 40;
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));

                    this.g2d.drawLine(this.ORIGIN_X-5, (int)y, this.ORIGIN_X + 5, (int)y);
                    this.g2d.drawString("1/"+String.format("%.3f",cutPoint), ORIGIN_X-40 , (int)y);
                    cutPoint = cutPoint*Math.E;
                }

               
                }break; 
              
          case LOG10_RESCALE:{
              
                int idx;
                //We add 1 to every density in order to avoid values between 0 and 1. Given that a densitie will never be negative I can be sure that adding 1 will give me numbers at least greater than 1.
                double minLog = Math.log10(this.densities.get(this.densities.size()-2)  + 1);
                double maxLog = Math.log10(this.densities.get(0) +1);
                double range = (maxLog-minLog);

                for(idx = 0; idx < this.densities.size(); idx++)
                {
                    //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                    double norm = (Math.log10(this.densities.get(idx) +1)-minLog)/(range);
                    //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                    y = (norm)*((getHeight()-80-RANGE0_1)) + 40;
                    this.yCutsLog10.put(1/this.densities.get(idx),(int)y);
                    //In this plot the cuts are not shown on the Y axis
                    //this.g2d.drawLine(this.ORIGIN_X-3, (int)(y), this.ORIGIN_Y + 3, (int)(y));
                }

                this.yCutsLog10.put(0.0,getHeight()-40);
              
                //indicating that the scale used is Log 10;
                this.yCutsInUse = this.yCutsLog10;
              
                //Writing scale number.
                //the greatest silhouette has only noise.
                minLog = Math.log10(this.densities.get(this.densities.size()-2)  + 1);
                maxLog = Math.log10(this.densities.get(0) +1);
                range = (maxLog-minLog);
              
                double cutPoint = this.densities.get(this.densities.size() -2);
                while(cutPoint <= this.densities.get(0))
                {
                    //Adding 1 to cutPoint for the same reason as we add 1 to densitites above.
                    double norm = (Math.log10(cutPoint+1)-minLog)/(range);
                    //the first cut will return norm = 0 beacuse log10(1) == 0, so is neeed to reajust the 1 cut in order keep it away from the origin.
                    y = (norm)*((getHeight()-80-RANGE0_1)) + 40;
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));

                    this.g2d.drawLine(this.ORIGIN_X-5, (int)y, this.ORIGIN_X + 5, (int)y);
                    this.g2d.drawString("1/"+String.format("%.3f",cutPoint), ORIGIN_X-40 , (int)y);
                    cutPoint = cutPoint*10;
                }

                /*this.g2d.drawLine(this.ORIGIN_X-2, 40, this.ORIGIN_X + 2, 40);
                this.g2d.drawString("1/"+Double.toString(this.densities.get(0)), ORIGIN_X-40 , (getHeight()-80-RANGE0_1));*/
                }break;
              
          //default is the same as case 0.
          default:{
                //print all densities except the greater one, because this line will have only noise (0);
                for(int i = 0; i < this.densities.size()-1 ; i++)
                {
                    //Normalizing all densities between 0 and 1, after that multiplying the norm by the Y axis height in order to put the values in scale.
                    double norm = (1/this.densities.get(i))/(1/this.densities.get(this.densities.size()-2));
                    //(1 - norm) inverts the direction, because java prints from the top of the screen to down.
                    y = (1-norm)*((getHeight()-80)) + 40;
                    this.yCutsInUse.put(1/this.densities.get(i),(int)y);
                    this.g2d.drawLine(this.ORIGIN_X-2, (int)(y), this.ORIGIN_X + 2, (int)(y));
                
                }
                }break;      
          
      }   
      
        //                                getheight - 80 + 40
       this.g2d.drawLine(this.ORIGIN_X-2, getHeight()-40, this.ORIGIN_X + 2, getHeight()-40); //originLine    

    }
    
    @Override
    protected int plotMiddle(int i, int posX, int elemDist)
    {
        boolean print = false;
        switch(this.Htype)
        {
            case SMALL_SAMPLE: if(i % this.k == 0) print = true; 
                                break;
            case BIG_SAMPLE: if(i % this.k != 0) print = true;
                                break;
            case NO_HSAMPLE: print = true; break;
        }
        
        if(print)
        {
            //Stores g2d.color and g2d.paint before the calling of this method in order to restore them after the method ends.
            Color lastColor = g2d.getColor();
            Paint lastPaint = g2d.getPaint();

            ObjectSilhouetteInfo actualObject = this.silhouetteInfos.get(i);
            int j = 0;
            for(double[] d : actualObject.getDensities())
            {

                double h0 = this.yCutsInUse.get(d[0]);
                double h1 = this.yCutsInUse.get(d[1]);

                double height = (h0 - h1);

                Rectangle r = new Rectangle ( posX,
                                              (int)java.lang.Math.floor(h1),
                                              elemDist,
                                              (int)java.lang.Math.floor(height));

                this.g2d.setColor(this.getColor(actualObject.getColorsId().get(j)));
                this.g2d.setPaint(this.getColor(actualObject.getColorsId().get(j)));
                this.g2d.fill(r);
                this.g2d.draw(r);
                j++;
            }


            this.g2d.setColor(lastColor);
            this.g2d.setPaint(lastPaint);
            if(j!=0)
            {
                return 1;
            }else
            {
                return 0;
            }
        }
        
        return 0;
    }
    
    public void setHs(int hs)
    {
        
        if(hs <= 50)
        {
            this.k = (int)Math.ceil(100/hs);
            this.xAxisQtd = Math.ceil((double)matrix.size()/(double)this.k);
            this.Htype = SMALL_SAMPLE;
            this.hs = hs;
        }
        else if(hs != 100)
        {
            this.k = (int)Math.ceil(100/(100-hs));
            this.xAxisQtd = Math.ceil(matrix.size() - ((double)matrix.size()/(double)this.k));
            this.Htype = BIG_SAMPLE;
            this.hs = hs;
        }else
        {
            this.Htype = NO_HSAMPLE;
            this.k = 0;
            this.xAxisQtd = matrix.size();
            this.hs = 100;
        }
        
        this.setFocusable(true);
    }

    public void decreaseHs()
    {
        if (this.hs > HS_FACTOR)
        {
            this.setHs(this.hs - HS_FACTOR);
        }
    }
    
    public void increaseHs()
    {
        int aux;
        aux = this.hs + HS_FACTOR;
        
        if(aux > 100)
        {
            this.setHs(100);
        }
        else
        {
            this.setHs(aux);
        }
    }
    
    public int getHs()
    {
        return this.hs;
    }
    
    
    public double getXAxisQtd()
    {
        return this.xAxisQtd;
    }
}

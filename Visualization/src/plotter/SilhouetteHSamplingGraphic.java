/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plotter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Fernando Soares de Aguiar Neto.
 */
public class SilhouetteHSamplingGraphic extends SilhouetteGraphic{
    
    private int skip;
    
    /**
     *
     * @param matrix
     * @param densities
     * @param maxClusterID
     * @param yLabel
     * @param colors
     * @param k
     * @param VSampling
     */
    public SilhouetteHSamplingGraphic(ArrayList<ArrayList<Integer>> matrix, ArrayList<Double> densities, Integer maxClusterID, String yLabel, Color[] colors, int k, int elemDist) {        
        
        
        super(matrix, densities, maxClusterID, yLabel, colors, elemDist);       
        
        this.skip = k;
        super.initComponents();
    }
    
    @Override
    //skips every skip-esimal element.
    protected int plotMiddle(int i, int posX, int elemDist)
    {
        if(!(i % this.skip == 0))
        {
            //Stores g2d.color and g2d.paint before the calling of this method in order to restore them after the method ends.
            Color lastColor = g2d.getColor();
            Paint lastPaint = g2d.getPaint();

            ObjectSilhouetteInfo actualObject = super.silhouetteInfos.get(i);
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
            
            return 1;
        }
        
        return 0;
    }
}

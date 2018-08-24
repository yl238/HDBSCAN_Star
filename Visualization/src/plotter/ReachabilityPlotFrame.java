/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plotter;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Fernando Soares de Aguiar Neto
 */
public class ReachabilityPlotFrame extends javax.swing.JFrame {

    protected double scale;
    protected int originalW;
    protected int originalH;
    protected int originalElemDist;
    public final double SCROLL_AMOUNT = 0.2;
    
    protected ArrayList<ArrayList<Integer>> matrix;
    protected ArrayList<Double> densities;
    protected int vSampling;
    
    //suportedTypes (copied from GraphicArea.java)
    protected final int NO_RESCALE = 0;
    protected final int LOG1P_RESCALE= 1;
    protected final int LOG10_RESCALE = 2;
    
    
    /**
     * Creates new form SilhouettePlot
     * @param matrix
     * @param densities
     * @param vSampling
     */
    
    public ReachabilityPlotFrame(ArrayList<ArrayList<Integer>> matrix, ArrayList<Double> densities) {
        
        this.matrix = matrix;
        this.densities = densities;
        this.vSampling = vSampling;
        
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                zoom(e);
            }
        });
        
        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                resetZoom(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                resetZoom(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                resetZoom(e);
            }
        });

        //Setting the minimum screenSize in order to have a decent plot.
        //Setting the minimum screenSize in order to have a decent plot.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        int xAxisQtd = matrix.size();
        //First we set the size of the bars as if it would fit on a fullscreen, if there are too many bars, each bar will have at least size 1px
        this.originalElemDist = Math.max(1,(int)Math.ceil((width-80)/xAxisQtd));
        
        //Then we multiply the barsize by the quantity of objects, in order to set the minimum size.
        int w = (int)(this.originalElemDist*xAxisQtd+80);
        
        this.originalW = w;
        this.originalH = (int)height-200;
        
        this.setFocusable(true);
        
        initComponents();     
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel1.setFocusable(true);
        b_save = new javax.swing.JButton();
        b_zoomIn = new javax.swing.JButton();
        b_zoomOut = new javax.swing.JButton();
        b_resetZoom = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        p_GraphicArea = new ReachabilityGraphic(this.matrix, this.densities, this.originalElemDist);
        b_noRescale = new javax.swing.JToggleButton();
        b_noRescale.setSelected(true);
        b_logN = new javax.swing.JToggleButton();
        b_log10 = new javax.swing.JToggleButton();
        b_allImg = new javax.swing.JButton();

        setExtendedState(6);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setFocusable(false);
        jPanel1.setMaximumSize(new java.awt.Dimension(105, 32767));
        jPanel1.setMinimumSize(new java.awt.Dimension(105, 290));
        jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel1FocusGained(evt);
            }
        });

        b_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plotter/save.png"))); // NOI18N
        b_save.setText("Save");
        b_save.setToolTipText("Saves the image of the graphic, using the selected scale");
        b_save.setMargin(new java.awt.Insets(2, 2, 2, 2));
        b_save.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                b_saveFocusGained(evt);
            }
        });
        b_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_saveActionPerformed(evt);
            }
        });

        b_zoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plotter/Zoom-In.png"))); // NOI18N
        b_zoomIn.setText("Zoom In");
        b_zoomIn.setToolTipText("Zoom In, on the center, for a mouse-centered zoom, please use Ctrl+MouseWheel");
        b_zoomIn.setMargin(new java.awt.Insets(2, 2, 2, 2));
        b_zoomIn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                b_zoomInFocusGained(evt);
            }
        });
        b_zoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_zoomInActionPerformed(evt);
            }
        });

        b_zoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plotter/Zoom-Out.png"))); // NOI18N
        b_zoomOut.setText("Zoom Out");
        b_zoomOut.setToolTipText("Zoom Out, on the center, for a mouse-centered zoom, please use Ctrl+MouseWheel");
        b_zoomOut.setMargin(new java.awt.Insets(2, 2, 2, 2));
        b_zoomOut.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                b_zoomOutFocusGained(evt);
            }
        });
        b_zoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_zoomOutActionPerformed(evt);
            }
        });

        b_resetZoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plotter/resetZoom.png"))); // NOI18N
        b_resetZoom.setText("Default Zoom");
        b_resetZoom.setToolTipText("Resets the zoom (Ctrl+0)");
        b_resetZoom.setMargin(new java.awt.Insets(2, 2, 2, 2));
        b_resetZoom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                b_resetZoomFocusGained(evt);
            }
        });
        b_resetZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_resetZoomActionPerformed(evt);
            }
        });

        jScrollPane1.setWheelScrollingEnabled(false);

        p_GraphicArea.setPreferredSize(new java.awt.Dimension(this.originalW, this.originalH));

        javax.swing.GroupLayout p_GraphicAreaLayout = new javax.swing.GroupLayout(p_GraphicArea);
        p_GraphicArea.setLayout(p_GraphicAreaLayout);
        p_GraphicAreaLayout.setHorizontalGroup(
            p_GraphicAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 838, Short.MAX_VALUE)
        );
        p_GraphicAreaLayout.setVerticalGroup(
            p_GraphicAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1143, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(p_GraphicArea);

        b_noRescale.setText("No Rescale");
        b_noRescale.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                b_noRescaleFocusGained(evt);
            }
        });
        b_noRescale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_noRescaleActionPerformed(evt);
            }
        });

        b_logN.setText("Log e");
        b_logN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                b_logNFocusGained(evt);
            }
        });
        b_logN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_logNActionPerformed(evt);
            }
        });

        b_log10.setText("Log 10");
        b_log10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                b_log10FocusGained(evt);
            }
        });
        b_log10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_log10ActionPerformed(evt);
            }
        });

        b_allImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plotter/resetZoom.png"))); // NOI18N
        b_allImg.setText("View All Image");
        b_allImg.setToolTipText("Shows the entire plot in a popup window.");
        b_allImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_allImgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_save)
                    .addComponent(b_zoomIn)
                    .addComponent(b_zoomOut)
                    .addComponent(b_resetZoom)
                    .addComponent(b_noRescale)
                    .addComponent(b_logN)
                    .addComponent(b_log10)
                    .addComponent(b_allImg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {b_allImg, b_log10, b_logN, b_noRescale, b_resetZoom, b_save, b_zoomIn, b_zoomOut});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_noRescale)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_logN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_log10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_save)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_zoomIn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_zoomOut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_resetZoom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_allImg)
                .addContainerGap(828, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1144, Short.MAX_VALUE)
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {b_allImg, b_log10, b_logN, b_noRescale, b_resetZoom, b_save, b_zoomIn, b_zoomOut});

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_saveActionPerformed
        saveImage();
    }//GEN-LAST:event_b_saveActionPerformed

    private void b_zoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_zoomInActionPerformed
         //Java sees a Scrol up as a negative number.
        double change = this.SCROLL_AMOUNT;
        this.scale = this.scale*(1+change);

        this.updateScale();
    }//GEN-LAST:event_b_zoomInActionPerformed

    private void updateScale()
    {
        if(this.scale <1)
        {
            //resetZoom
            this.scale = 1;
            this.p_GraphicArea.setPreferredSize(new Dimension((int)Math.ceil(this.originalW*scale), (int)Math.ceil((this.originalH-50)*scale)));
            this.p_GraphicArea.setElemDist((int)Math.ceil(this.originalElemDist*scale));

        }
        else
        {
            p_GraphicArea.setVisible(false);

            Rectangle rect = this.jScrollPane1.getViewport().getViewRect();
            Point2D.Double rectCenter = new Point2D.Double(rect.x + (rect.width/2.0), 
                                                           rect.y + rect.height/2.0);

           
            //getting relative rectCenter position (in percentage, norm), according to the entire plot.
            Point2D.Double rectRelativePosition = new Point2D.Double(rectCenter.x / this.p_GraphicArea.getPreferredSize().getWidth(),
                                                                      rectCenter.y / this.p_GraphicArea.getPreferredSize().getHeight());

            //rescaling plot
            Dimension newSize = new Dimension((int)Math.ceil(this.originalW*scale), (int)Math.ceil((this.originalH-50)*scale));

            this.p_GraphicArea.setPreferredSize(newSize);

            //translating viewRect in order to make the mouse position the center of the screen.
            //newCenter = mouseRelativePosition<dotProduct>TransformedPlotSize
            //again considering the above as points.
            Point2D.Double newCenter = new Point2D.Double(rectRelativePosition.getX()*newSize.getWidth(),
                                                          rectRelativePosition.getY()*newSize.getHeight());

            //notice that since rect.translate accepts only integers, I had to round the value here.
            Point translate = new Point((int)Math.ceil(newCenter.getX() - rectCenter.getX()),
                                        (int)Math.ceil(newCenter.getY() - rectCenter.getY()));

            //DEBUG
            //System.out.println("rect="+rect+"\nrectCenter="+rectCenter+"\nMouseCenterRelPos="+mouseRelativePosition+"\nnewCenter="+newCenter+"\ntranslate="+translate);

            rect.translate(translate.x, translate.y);

            //DEBUG
            //System.out.println("TrasnlatedRect="+rect);
            //showing rect
            this.jScrollPane1.getViewport().setViewPosition(new Point(Math.max(0, rect.x), rect.y));

            //repainting
            p_GraphicArea.setVisible(true);
            //repaint();
        }
    }
    
    private void b_resetZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_resetZoomActionPerformed
        this.scale = 1;
        this.p_GraphicArea.setPreferredSize(new Dimension((int)Math.ceil(this.originalW*scale), (int)Math.ceil((this.originalH-50)*scale)));
        this.p_GraphicArea.setElemDist((int)Math.ceil(this.originalElemDist*scale));
        repaint();
    }//GEN-LAST:event_b_resetZoomActionPerformed

    private void b_noRescaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_noRescaleActionPerformed
        
        p_GraphicArea.setVScaling(GraphicArea.NO_RESCALE);
        b_noRescale.setSelected(true);
        b_logN.setSelected(false);
        b_log10.setSelected(false);
        repaint();
            
    }//GEN-LAST:event_b_noRescaleActionPerformed

    private void b_logNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_logNActionPerformed
        p_GraphicArea.setVScaling(GraphicArea.LOG1P_RESCALE);
        b_logN.setSelected(true);
        b_noRescale.setSelected(false);
        b_log10.setSelected(false);
        repaint();
    }//GEN-LAST:event_b_logNActionPerformed

    private void b_log10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_log10ActionPerformed
        p_GraphicArea.setVScaling(GraphicArea.LOG10_RESCALE);
        b_log10.setSelected(true);
        b_logN.setSelected(false);
        b_noRescale.setSelected(false);
        repaint();
    }//GEN-LAST:event_b_log10ActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        
    }//GEN-LAST:event_formKeyPressed

    private void b_zoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_zoomOutActionPerformed
         //Java sees a Scrol up as a negative number.
        double change = (-1)*this.SCROLL_AMOUNT;
        this.scale = this.scale*(1+change);

        this.updateScale();
        
    }//GEN-LAST:event_b_zoomOutActionPerformed

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        //this.requestFocus();
    }//GEN-LAST:event_formFocusLost

    private void jPanel1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusGained
        this.requestFocus();
    }//GEN-LAST:event_jPanel1FocusGained

    private void b_log10FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_b_log10FocusGained
        this.requestFocus();
    }//GEN-LAST:event_b_log10FocusGained

    private void b_noRescaleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_b_noRescaleFocusGained
        this.requestFocus();
    }//GEN-LAST:event_b_noRescaleFocusGained

    private void b_logNFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_b_logNFocusGained
        this.requestFocus();
    }//GEN-LAST:event_b_logNFocusGained

    private void b_saveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_b_saveFocusGained
        this.requestFocus();
    }//GEN-LAST:event_b_saveFocusGained

    private void b_zoomInFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_b_zoomInFocusGained
        this.requestFocus();
    }//GEN-LAST:event_b_zoomInFocusGained

    private void b_zoomOutFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_b_zoomOutFocusGained
        this.requestFocus();
    }//GEN-LAST:event_b_zoomOutFocusGained

    private void b_resetZoomFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_b_resetZoomFocusGained
        this.requestFocus();
    }//GEN-LAST:event_b_resetZoomFocusGained

    private void b_allImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_allImgActionPerformed
        //Showing Dialog meanwhile the file is loaded.
        JFrame FramePtr = this;

        Dimension size = p_GraphicArea.getSize();
        
        double toScaleW = (double)(639.0/size.width);
        double toScaleH = (double)(359.0/size.height);
        
        BufferedImage outImg = new BufferedImage((int)Math.ceil(toScaleW*size.width), (int)Math.ceil(toScaleH*size.height), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outImg.createGraphics();
        
        g2d.scale(toScaleW, toScaleH);
        
        p_GraphicArea.print(g2d);
      
        SwingWorker<?,?> worker = new SwingWorker<Object,Object>(){

            @Override
            protected Object doInBackground() throws Exception {
                imageShow imgFrame = new imageShow(new ImageIcon(outImg), FramePtr.getTitle());
                imgFrame.pack();
                imgFrame.setVisible(true);
                
                return null;
            }

        };

        worker.execute();
        
    }//GEN-LAST:event_b_allImgActionPerformed

    public void saveImage()
    {
        JFileChooser fc = new JFileChooser(Plotter.dir);
        FileFilter filter = new FileNameExtensionFilter("Portable Network Graphics","PNG");
        fc.addChoosableFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);

        int ret = fc.showSaveDialog(this);

        if (ret == JFileChooser.APPROVE_OPTION)
        {
            Dimension size = p_GraphicArea.getSize();
            BufferedImage outImg = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outImg.createGraphics();
            p_GraphicArea.print(g2d);

            try 
            {
                File outFile = fc.getSelectedFile();
                String filename = fc.getSelectedFile().getCanonicalPath();
                String[] parts = filename.split(".");
                int extIdx = filename.lastIndexOf(".");
                if(extIdx == -1)
                {
                    filename = filename + ".png";
                }
                else
                {
                    filename = filename.substring(0, filename.lastIndexOf(".")) + ".png";

                }
                System.out.println(filename);
                ImageIO.write(outImg, "png", new File(filename));
                JOptionPane.showMessageDialog(this, "Image saved successfully");

            }catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "An error occurred when tried to save the image, please check disk space and permissions, and try again.");
                ex.printStackTrace();
            }
        }
    }
    
   public void resetZoom(KeyEvent e)
    {
        if(e.isControlDown())
        {
            int pressedKey = e.getKeyCode();
            
            if(pressedKey == KeyEvent.VK_0)
            {
                this.scale = 1;
                this.p_GraphicArea.setPreferredSize(new Dimension((int)Math.ceil(this.originalW*scale), (int)Math.ceil((this.originalH-50)*scale)));
                this.p_GraphicArea.setElemDist((int)Math.ceil(this.originalElemDist*scale));
                
                repaint();
            }
            else if(pressedKey == KeyEvent.VK_P)
            {
               saveImage();
            }
        }
    }
    
    public void zoom(MouseWheelEvent e)
    {

        if(e.isControlDown())
        {
            //Java sees a Scrol up as a negative number.
            double change = (-1)*this.SCROLL_AMOUNT*e.getPreciseWheelRotation();
            this.scale = this.scale*(1+change);
            
            if(this.scale <1)
            {
                //resetZoom
                this.scale = 1;
                this.p_GraphicArea.setPreferredSize(new Dimension((int)Math.ceil(this.originalW*scale), (int)Math.ceil((this.originalH-50)*scale)));
                this.p_GraphicArea.setElemDist((int)Math.ceil(this.originalElemDist*scale));
                
            }
            else
            {
                p_GraphicArea.setVisible(false);
                //getting view Rect
                Rectangle rect = this.jScrollPane1.getViewport().getViewRect();
                Point2D.Double rectCenter = new Point2D.Double(rect.x + (rect.width/2.0), 
                                                               rect.y + rect.height/2.0);

                //getting absolute mouse position accoring to the entire plot.
                //equals mousePosition + rectPoition
                //considering mousePosition and PlotSize as Points.
                Point2D.Double mouseAbsolutePosition = new Point2D.Double((rect.x + e.getX()),
                                                                          (rect.y + e.getY()));

                //getting relative mouse position (in percentage, norm), according to the entire plot.
                Point2D.Double mouseRelativePosition = new Point2D.Double(mouseAbsolutePosition.x / this.p_GraphicArea.getPreferredSize().getWidth(),
                                                                          mouseAbsolutePosition.y / this.p_GraphicArea.getPreferredSize().getHeight());

                //rescaling plot
                Dimension newSize = new Dimension((int)Math.ceil(this.originalW*scale), (int)Math.ceil((this.originalH-50)*scale));

                this.p_GraphicArea.setPreferredSize(newSize);

                //translating viewRect in order to make the mouse position the center of the screen.
                //newCenter = mouseRelativePosition<dotProduct>TransformedPlotSize
                //again considering the above as points.
                Point2D.Double newCenter = new Point2D.Double(mouseRelativePosition.getX()*newSize.getWidth(),
                                                              mouseRelativePosition.getY()*newSize.getHeight());

                //notice that since rect.translate accepts only integers, I had to round the value here.
                Point translate = new Point((int)Math.ceil(newCenter.getX() - rectCenter.getX()),
                                            (int)Math.ceil(newCenter.getY() - rectCenter.getY()));

                //DEBUG
                //System.out.println("rect="+rect+"\nrectCenter="+rectCenter+"\nMouseCenterRelPos="+mouseRelativePosition+"\nnewCenter="+newCenter+"\ntranslate="+translate);
                
                rect.translate(translate.x, translate.y);
                
                //DEBUG
                //System.out.println("TrasnlatedRect="+rect);
                //showing rect
                this.jScrollPane1.getViewport().setViewPosition(new Point(Math.max(0, rect.x), rect.y));
               
                //repainting
                p_GraphicArea.setVisible(true);
                //repaint();
            }
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_allImg;
    private javax.swing.JToggleButton b_log10;
    private javax.swing.JToggleButton b_logN;
    private javax.swing.JToggleButton b_noRescale;
    private javax.swing.JButton b_resetZoom;
    private javax.swing.JButton b_save;
    private javax.swing.JButton b_zoomIn;
    private javax.swing.JButton b_zoomOut;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private ReachabilityGraphic p_GraphicArea;
    // End of variables declaration//GEN-END:variables
}

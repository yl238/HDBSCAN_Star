/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plotter;

import java.awt.Dimension;
import javax.swing.Icon;

/**
 *
 * @author fsan
 */
public class imageShow extends javax.swing.JFrame {

    /**
     * Creates new form imageShow
     */
    private Icon img;
    private String title;
    
    public imageShow(Icon img, String title) {
        this.img = img;
        this.title = title;
        
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

        l_img = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(this.title);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setMinimumSize(new java.awt.Dimension(640, 360));
        setPreferredSize(new Dimension(this.img.getIconWidth()+20,this.img.getIconHeight()+50));
        setSize(new Dimension(this.img.getIconWidth()+20,this.img.getIconHeight()+50));

        l_img.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        l_img.setIcon(this.img);
        l_img.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        l_img.setAlignmentY(0.0F);
        l_img.setIconTextGap(0);
        l_img.setMaximumSize(getMaximumSize());
        l_img.setMinimumSize(getMinimumSize());
        l_img.setPreferredSize(getPreferredSize());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(l_img, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(l_img, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel l_img;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DlgAdmin.java
 *
 * Created on 04 Des 13, 12:59:34
 */
package khanzahmsanjungan;

import fungsi.sekuel;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Kode
 */
public class DlgCekinMobileJKN extends javax.swing.JDialog {
    private final sekuel Sequel = new sekuel();

    /**
     * Creates new form DlgAdmin
     *
     * @param parent
     * @param id
     */
    public DlgCekinMobileJKN(java.awt.Frame parent, boolean id) {
        super(parent, id);
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        PanelWall = new usu.widget.glass.PanelGlass();
        jPanel1 = new component.Panel();
        textInput = new component.TextBox();
        jLabel28 = new component.Label();
        btnTutup = new widget.ButtonBig();
        btnCekRujukanMobileJKN = new widget.ButtonBig();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new java.awt.BorderLayout(1, 1));

        jPanel2.setBackground(new java.awt.Color(238, 238, 255));
        jPanel2.setForeground(new java.awt.Color(238, 238, 255));

        PanelWall.setBackground(new java.awt.Color(238, 238, 255));
        PanelWall.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/48x48/mobilejkn.png"))); // NOI18N
        PanelWall.setForeground(new java.awt.Color(238, 238, 255));
        PanelWall.setPreferredSize(new java.awt.Dimension(500, 150));
        PanelWall.setRound(false);
        PanelWall.setWarna(new java.awt.Color(238, 238, 255));

        javax.swing.GroupLayout PanelWallLayout = new javax.swing.GroupLayout(PanelWall);
        PanelWall.setLayout(PanelWallLayout);
        PanelWallLayout.setHorizontalGroup(
            PanelWallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        PanelWallLayout.setVerticalGroup(
            PanelWallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jPanel2.add(PanelWall);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel1.setBackground(new java.awt.Color(238, 238, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 215, 255)), "::[ Cek Data Pasien!!! ]::", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 30), new java.awt.Color(0, 131, 62))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(0, 131, 62));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 70));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        textInput.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 131, 62), 2, true));
        textInput.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textInput.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        textInput.setPreferredSize(new java.awt.Dimension(350, 75));
        textInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textInputKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(textInput, gridBagConstraints);

        jLabel28.setForeground(new java.awt.Color(0, 131, 62));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("No. Rujukan / No. Peserta / NIK / No. RM");
        jLabel28.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel28.setPreferredSize(new java.awt.Dimension(500, 75));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.ipady = 5;
        jPanel1.add(jLabel28, gridBagConstraints);

        btnTutup.setBackground(new java.awt.Color(255, 255, 255));
        btnTutup.setForeground(new java.awt.Color(51, 51, 51));
        btnTutup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/exit.png"))); // NOI18N
        btnTutup.setMnemonic('U');
        btnTutup.setToolTipText("Alt+U");
        btnTutup.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTutup.setHorizontalTextPosition(javax.swing.SwingConstants.TRAILING);
        btnTutup.setIconTextGap(2);
        btnTutup.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnTutup.setPreferredSize(new java.awt.Dimension(100, 75));
        btnTutup.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        jPanel1.add(btnTutup, gridBagConstraints);

        btnCekRujukanMobileJKN.setBackground(new java.awt.Color(255, 255, 255));
        btnCekRujukanMobileJKN.setForeground(new java.awt.Color(51, 51, 51));
        btnCekRujukanMobileJKN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/konfirmasi.png"))); // NOI18N
        btnCekRujukanMobileJKN.setMnemonic('U');
        btnCekRujukanMobileJKN.setToolTipText("Alt+U");
        btnCekRujukanMobileJKN.setFont(new java.awt.Font("Poppins", 1, 11)); // NOI18N
        btnCekRujukanMobileJKN.setIconTextGap(0);
        btnCekRujukanMobileJKN.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCekRujukanMobileJKN.setPreferredSize(new java.awt.Dimension(100, 75));
        btnCekRujukanMobileJKN.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnCekRujukanMobileJKN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCekRujukanMobileJKNActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        jPanel1.add(btnCekRujukanMobileJKN, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            cekRujukanPasienMobileJKN();
            this.setCursor(Cursor.getDefaultCursor());
        }

    }//GEN-LAST:event_textInputKeyPressed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        dispose();
    }//GEN-LAST:event_btnTutupActionPerformed

    private void btnCekRujukanMobileJKNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCekRujukanMobileJKNActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        cekRujukanPasienMobileJKN();
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnCekRujukanMobileJKNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgCekinMobileJKN dialog = new DlgCekinMobileJKN(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private usu.widget.glass.PanelGlass PanelWall;
    private widget.ButtonBig btnCekRujukanMobileJKN;
    private widget.ButtonBig btnTutup;
    private component.Label jLabel28;
    private component.Panel jPanel1;
    private javax.swing.JPanel jPanel2;
    private component.TextBox textInput;
    // End of variables declaration//GEN-END:variables

    private void cekRujukanPasienMobileJKN() {
        String kodeBooking;
        
        if (Sequel.cariIntegerSmc("select count(*) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.status = 'Belum' and referensi_mobilejkn_bpjs.nomorreferensi = ?", textInput.getText().trim()) > 0) {
            kodeBooking = Sequel.cariIsiSmc("select max(referensi_mobilejkn_bpjs.nobooking) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.status = 'Belum' and referensi_mobilejkn_bpjs.nomorreferensi = ?", textInput.getText().trim());
        } else if (Sequel.cariIntegerSmc("select count(*) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.status = 'Belum' and referensi_mobilejkn_bpjs.nik = ?", textInput.getText().trim()) > 0) {
            kodeBooking = Sequel.cariIsiSmc("select max(referensi_mobilejkn_bpjs.nobooking) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.status = 'Belum' and referensi_mobilejkn_bpjs.nik = ?", textInput.getText().trim());
        } else if (Sequel.cariIntegerSmc("select count(*) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.status = 'Belum' and referensi_mobilejkn_bpjs.nomorkartu = ?", textInput.getText().trim()) > 0) {
            kodeBooking = Sequel.cariIsiSmc("select max(referensi_mobilejkn_bpjs.nobooking) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.status = 'Belum' and referensi_mobilejkn_bpjs.nomorkartu = ?", textInput.getText().trim());
        } else if (Sequel.cariIntegerSmc("select count(*) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.status = 'Belum' and referensi_mobilejkn_bpjs.norm = ?", textInput.getText().trim()) > 0) {
            kodeBooking = Sequel.cariIsiSmc("select max(referensi_mobilejkn_bpjs.nobooking) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.status = 'Belum' and referensi_mobilejkn_bpjs.norm = ?", textInput.getText().trim());
        } else {
            JOptionPane.showMessageDialog(rootPane, "Data booking MobileJKN tidak ditemukan!");
            return;
        }
        
        DlgRegistrasiSEPMobileJKN form = new DlgRegistrasiSEPMobileJKN(null, true);
        form.tampilRujukanMobileJKN(kodeBooking);
        form.setSize(this.getWidth(), this.getHeight());
        form.setLocationRelativeTo(jPanel1);
        this.dispose();
        form.setVisible(true);
    }
}

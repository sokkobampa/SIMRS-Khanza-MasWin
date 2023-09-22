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

import bridging.ApiBPJS;
import bridging.BPJSCekHistoriPelayanan;
import bridging.BPJSCekReferensiDokterDPJP;
import bridging.BPJSCekReferensiDokterDPJP1;
import bridging.BPJSCekReferensiPenyakit;
import bridging.BPJSCekReferensiPoli;
import bridging.BPJSCekRiwayatRujukanTerakhir;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.akses;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.bouncycastle.crypto.engines.TnepresEngine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 *
 * @author Kode
 */
public class DlgRegistrasiSEPMobileJKN extends javax.swing.JDialog {

    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    
    private PreparedStatement ps, ps2;
    private ResultSet rs, rs2;
    private ApiBPJS api = new ApiBPJS();
    private BPJSCekReferensiDokterDPJP1 dokter = new BPJSCekReferensiDokterDPJP1(null, true);
    private BPJSCekReferensiPenyakit penyakit = new BPJSCekReferensiPenyakit(null, true);
    private DlgCariPoliBPJS poli = new DlgCariPoliBPJS(null, true);
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
    private BPJSCekRiwayatRujukanTerakhir rujukanterakhir = new BPJSCekRiwayatRujukanTerakhir(null, true);
    private BPJSCekHistoriPelayanan historiPelayanan = new BPJSCekHistoriPelayanan(null, true);
    
    private String BASE_NOREG = "",
                   URUT_NOREG = "",
                   BPJS_APLIKASI_FINGERPRINT = "",
                   BPJS_USERNAME_FINGERPRINT = "",
                   BPJS_PASSWORD_FINGERPRINT = "";
    
    private String umur = "0",
                   sttsumur = "Th",
                   hari = "",
                   kdDokter = "",
                   kdPoli = "",
                   namaInstansi,
                   alamatInstansi,
                   kabupaten,
                   propinsi,
                   kontak,
                   email,
                   kdkel = "",
                   kdkec = "",
                   kdkab = "",
                   kdprop = "",
                   nosisrute = "",
                   BASENOREG = "",
                   URUTNOREG = "",
                   link = "",
                   hubunganKeluarga = "SAUDARA",
                   statusPasien = "",
                   pengurutan = "",
                   tahun = "",
                   bulan = "",
                   posisitahun = "",
                   awalantahun = "",
                   awalanbulan = "",
                   no_ktp = "",
                   tmp_lahir = "",
                   nm_ibu = "",
                   alamat = "",
                   pekerjaan = "",
                   no_tlp = "",
                   tglKLL = "0000-00-00",
                   umurdaftar = "0",
                   namakeluarga = "",
                   noPeserta = "",
                   kelurahan = "",
                   kecamatan = "",
                   datajam = "",
                   jamMulai = "",
                   jamSelesai = "",
                   kabupatenPJ = "",
                   hariAwal = "",
                   requestJson,
                   URL = "",
                   noSEP = "",
                   user = "",
                   prb = "",
                   peserta = "",
                   kdDokterBPJS = "",
                   kdPoliBPJS = "",
                   status = "Baru",
                   utc = "",
                   jenisKunjungan = "",
                   noReg = "",
                   urlaplikasi = "",
                   urlfinger = "",
                   userfinger = "",
                   passfinger = "",
                   tampilkantni = Sequel.cariIsi("select tampilkan_tni_polri from set_tni_polri");
    
    private int kuota = 0;
    private Properties prop = new Properties();
    private File file;
    private DlgCariPoli poli2 = new DlgCariPoli(null, true);

    private FileWriter fileWriter;
    private String iyem;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private FileReader myObj;
    private Calendar cal = Calendar.getInstance();
    private boolean statusfinger = false;
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private JsonNode nameNode;
    private int day = cal.get(Calendar.DAY_OF_WEEK);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date parsedDate;

    /**
     * Creates new form DlgAdmin
     *
     * @param parent
     * @param id
     */
    public DlgRegistrasiSEPMobileJKN(java.awt.Frame parent, boolean id) {
        super(parent, id);
        initComponents();

        try {
            ps = koneksi.prepareStatement("select nama_instansi, alamat_instansi, kabupaten, propinsi, aktifkan, wallpaper,kontak,email,logo from setting");
            rs = ps.executeQuery();
            while (rs.next()) {
                namaInstansi = rs.getString("nama_instansi");
                alamatInstansi = rs.getString("alamat_instansi");
                kabupaten = rs.getString("kabupaten");
                propinsi = rs.getString("propinsi");
                kontak = rs.getString("kontak");
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (dokter.getTable().getSelectedRow() != -1) {
                    textDPJPRujukanKode.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    textDPJPRujukanNama.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 2).toString());
                    if (dropdownJenisPelayanan.getSelectedIndex() == 1) {
                        textDPJPLayananKode.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                        textDPJPLayananNama.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 2).toString());
                    }
                    textDPJPRujukanKode.requestFocus();

                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        poli.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (poli.getTable().getSelectedRow() != -1) {
                    textKdPoli.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(), 0).toString());
                    textNamaPoli.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(), 1).toString());
                    textDPJPRujukanKode.requestFocus();

                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        penyakit.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (penyakit.getTable().getSelectedRow() != -1) {

                    textKodeDiagnosa.setText(penyakit.getTable().getValueAt(penyakit.getTable().getSelectedRow(), 1).toString());
                    textNamaDiagnosa.setText(penyakit.getTable().getValueAt(penyakit.getTable().getSelectedRow(), 2).toString());
                    textKodeDiagnosa.requestFocus();

                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        rujukanterakhir.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (rujukanterakhir.getTable().getSelectedRow() != -1) {
                    textKodeDiagnosa.setText(rujukanterakhir.getTable().getValueAt(rujukanterakhir.getTable().getSelectedRow(), 0).toString());
                    textNamaDiagnosa.setText(rujukanterakhir.getTable().getValueAt(rujukanterakhir.getTable().getSelectedRow(), 1).toString());
                    textNoRujukan.setText(rujukanterakhir.getTable().getValueAt(rujukanterakhir.getTable().getSelectedRow(), 2).toString());
                    textKdPoli.setText(rujukanterakhir.getTable().getValueAt(rujukanterakhir.getTable().getSelectedRow(), 3).toString());
                    textNamaPoli.setText(rujukanterakhir.getTable().getValueAt(rujukanterakhir.getTable().getSelectedRow(), 4).toString());
                    textPPKRujukanKode.setText(rujukanterakhir.getTable().getValueAt(rujukanterakhir.getTable().getSelectedRow(), 6).toString());
                    textPPKRujukanNama.setText(rujukanterakhir.getTable().getValueAt(rujukanterakhir.getTable().getSelectedRow(), 7).toString());
                    Valid.SetTgl(dateTglRujuk, rujukanterakhir.getTable().getValueAt(rujukanterakhir.getTable().getSelectedRow(), 5).toString());
                    Catatan.requestFocus();
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        historiPelayanan.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (historiPelayanan.getTable().getSelectedRow() != -1) {
                    if ((historiPelayanan.getTable().getSelectedColumn() == 6) || (historiPelayanan.getTable().getSelectedColumn() == 7)) {
                        textNoRujukan.setText(historiPelayanan.getTable().getValueAt(historiPelayanan.getTable().getSelectedRow(), historiPelayanan.getTable().getSelectedColumn()).toString());
                    }
                }
                textNoRujukan.requestFocus();
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        try {
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            link = prop.getProperty("URLAPIBPJS");
            URUTNOREG = prop.getProperty("URUTNOREG");
            BASENOREG = prop.getProperty("BASENOREG");
        } catch (Exception ex) {

            URUTNOREG = "";
            BASENOREG = "";
        }

        try {
            textPPKPelayananKode.setText(Sequel.cariIsi("select setting.kode_ppk from setting"));
            textPPKPelayananNama.setText(Sequel.cariIsi("select setting.nama_instansi from setting"));
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            link = koneksiDB.URLAPIBPJS();
            urlfinger = koneksiDB.URLFINGERPRINTBPJS();
            userfinger = koneksiDB.USERFINGERPRINTBPJS();
            passfinger = koneksiDB.PASSWORDFINGERPRINTBPJS();
            urlaplikasi = koneksiDB.URLAPLIKASIFINGERPRINTBPJS();
        } catch (Exception e) {
            System.out.println("E : " + e);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new component.Panel();
        jPanel2 = new component.Panel();
        textNamaPasien = new widget.TextBox();
        textNoRM = new widget.TextBox();
        textNoPeserta = new widget.TextBox();
        jLabel20 = new widget.Label();
        dateTglSEP = new widget.Tanggal();
        jLabel22 = new widget.Label();
        dateTglRujuk = new widget.Tanggal();
        jLabel23 = new widget.Label();
        textNoRujukan = new widget.TextBox();
        jLabel9 = new widget.Label();
        textPPKPelayananKode = new widget.TextBox();
        textPPKPelayananNama = new widget.TextBox();
        jLabel10 = new widget.Label();
        textPPKRujukanKode = new widget.TextBox();
        textPPKRujukanNama = new widget.TextBox();
        jLabel11 = new widget.Label();
        textKodeDiagnosa = new widget.TextBox();
        textNamaDiagnosa = new widget.TextBox();
        textNamaPoli = new widget.TextBox();
        textKdPoli = new widget.TextBox();
        LabelPoli = new widget.Label();
        jLabel13 = new widget.Label();
        jLabel14 = new widget.Label();
        Catatan = new widget.TextBox();
        dropdownJenisPelayanan = new widget.ComboBox();
        LabelKelas = new widget.Label();
        dropdownKelas = new widget.ComboBox();
        dropdownLakaLantas = new widget.ComboBox();
        jLabel8 = new widget.Label();
        textTglLahir = new widget.TextBox();
        jLabel18 = new widget.Label();
        textJenisKelamin = new widget.TextBox();
        jLabel24 = new widget.Label();
        textJenisPeserta = new widget.TextBox();
        jLabel25 = new widget.Label();
        textStatus = new widget.TextBox();
        jLabel27 = new widget.Label();
        dropdownAsalRujukan = new widget.ComboBox();
        textNoTelp = new widget.TextBox();
        dropdownKatarak = new widget.ComboBox();
        jLabel37 = new widget.Label();
        jLabel38 = new widget.Label();
        dateTglKLL = new widget.Tanggal();
        LabelPoli2 = new widget.Label();
        textDPJPRujukanKode = new widget.TextBox();
        textDPJPRujukanNama = new widget.TextBox();
        jLabel36 = new widget.Label();
        textKeterangan = new widget.TextBox();
        jLabel40 = new widget.Label();
        Suplesi = new widget.ComboBox();
        NoSEPSuplesi = new widget.TextBox();
        jLabel41 = new widget.Label();
        LabelPoli3 = new widget.Label();
        KdPropinsi = new widget.TextBox();
        NmPropinsi = new widget.TextBox();
        LabelPoli4 = new widget.Label();
        KdKabupaten = new widget.TextBox();
        NmKabupaten = new widget.TextBox();
        LabelPoli5 = new widget.Label();
        KdKecamatan = new widget.TextBox();
        NmKecamatan = new widget.TextBox();
        jLabel42 = new widget.Label();
        dropdownTujuanKunjungan = new widget.ComboBox();
        dropdownFlagProsedur = new widget.ComboBox();
        jLabel43 = new widget.Label();
        jLabel44 = new widget.Label();
        dropdownPenunjang = new widget.ComboBox();
        jLabel45 = new widget.Label();
        dropdownAsesmenPelayanan = new widget.ComboBox();
        LabelPoli6 = new widget.Label();
        textDPJPLayananKode = new widget.TextBox();
        textDPJPLayananNama = new widget.TextBox();
        btnDPJPLayanan = new widget.Button();
        jLabel55 = new widget.Label();
        jLabel56 = new widget.Label();
        jLabel12 = new widget.Label();
        jLabel6 = new widget.Label();
        textNoSKDP = new widget.TextBox();
        jLabel26 = new widget.Label();
        textNIK = new widget.TextBox();
        jLabel7 = new widget.Label();
        btnDPJPLayanan1 = new widget.Button();
        btnDiagnosaAwal = new widget.Button();
        btnPilihRujukan = new widget.Button();
        btnDiagnosaAwal2 = new widget.Button();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new component.Button();
        btnFingerPrint = new component.Button();
        btnKeluar = new component.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(1, 1));

        jPanel1.setBackground(new java.awt.Color(238, 238, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(238, 238, 255), 1, true), "DATA ELIGIBILITAS PESERTA JKN", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 24), new java.awt.Color(0, 131, 62))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 70));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 1));

        jPanel2.setBackground(new java.awt.Color(238, 238, 255));
        jPanel2.setForeground(new java.awt.Color(0, 131, 62));
        jPanel2.setPreferredSize(new java.awt.Dimension(390, 120));
        jPanel2.setLayout(null);

        textNamaPasien.setBackground(new java.awt.Color(245, 250, 240));
        textNamaPasien.setHighlighter(null);
        jPanel2.add(textNamaPasien);
        textNamaPasien.setBounds(340, 10, 230, 30);

        textNoRM.setHighlighter(null);
        textNoRM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textNoRMActionPerformed(evt);
            }
        });
        jPanel2.add(textNoRM);
        textNoRM.setBounds(230, 10, 110, 30);

        textNoPeserta.setEditable(false);
        textNoPeserta.setBackground(new java.awt.Color(255, 255, 153));
        textNoPeserta.setHighlighter(null);
        jPanel2.add(textNoPeserta);
        textNoPeserta.setBounds(730, 70, 300, 30);

        jLabel20.setForeground(new java.awt.Color(0, 131, 62));
        jLabel20.setText("Tgl.SEP :");
        jLabel20.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel20.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel20);
        jLabel20.setBounds(660, 130, 70, 30);

        dateTglSEP.setForeground(new java.awt.Color(50, 70, 50));
        dateTglSEP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "05-09-2023" }));
        dateTglSEP.setDisplayFormat("dd-MM-yyyy");
        dateTglSEP.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dateTglSEP.setOpaque(false);
        dateTglSEP.setPreferredSize(new java.awt.Dimension(95, 25));
        dateTglSEP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dateTglSEPKeyPressed(evt);
            }
        });
        jPanel2.add(dateTglSEP);
        dateTglSEP.setBounds(730, 130, 170, 30);

        jLabel22.setForeground(new java.awt.Color(0, 131, 62));
        jLabel22.setText("Tgl.Rujuk :");
        jLabel22.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel22.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel22);
        jLabel22.setBounds(650, 160, 80, 30);

        dateTglRujuk.setForeground(new java.awt.Color(50, 70, 50));
        dateTglRujuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "05-09-2023" }));
        dateTglRujuk.setDisplayFormat("dd-MM-yyyy");
        dateTglRujuk.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dateTglRujuk.setOpaque(false);
        dateTglRujuk.setPreferredSize(new java.awt.Dimension(95, 23));
        dateTglRujuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dateTglRujukKeyPressed(evt);
            }
        });
        jPanel2.add(dateTglRujuk);
        dateTglRujuk.setBounds(730, 160, 170, 30);

        jLabel23.setForeground(new java.awt.Color(0, 131, 62));
        jLabel23.setText("No.SKDP / S. Kontrol :");
        jLabel23.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel23.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel23);
        jLabel23.setBounds(90, 70, 140, 30);

        textNoRujukan.setEditable(false);
        textNoRujukan.setBackground(new java.awt.Color(255, 255, 153));
        textNoRujukan.setHighlighter(null);
        textNoRujukan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textNoRujukanKeyPressed(evt);
            }
        });
        jPanel2.add(textNoRujukan);
        textNoRujukan.setBounds(230, 100, 340, 30);

        jLabel9.setForeground(new java.awt.Color(0, 131, 62));
        jLabel9.setText("PPK Pelayanan :");
        jLabel9.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel9.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel9);
        jLabel9.setBounds(80, 250, 150, 30);

        textPPKPelayananKode.setEditable(false);
        textPPKPelayananKode.setBackground(new java.awt.Color(245, 250, 240));
        textPPKPelayananKode.setHighlighter(null);
        jPanel2.add(textPPKPelayananKode);
        textPPKPelayananKode.setBounds(230, 250, 75, 30);

        textPPKPelayananNama.setEditable(false);
        textPPKPelayananNama.setBackground(new java.awt.Color(245, 250, 240));
        textPPKPelayananNama.setHighlighter(null);
        jPanel2.add(textPPKPelayananNama);
        textPPKPelayananNama.setBounds(310, 250, 260, 30);

        jLabel10.setForeground(new java.awt.Color(0, 131, 62));
        jLabel10.setText("PPK Rujukan :");
        jLabel10.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel10);
        jLabel10.setBounds(110, 130, 120, 30);

        textPPKRujukanKode.setEditable(false);
        textPPKRujukanKode.setBackground(new java.awt.Color(245, 250, 240));
        textPPKRujukanKode.setHighlighter(null);
        jPanel2.add(textPPKRujukanKode);
        textPPKRujukanKode.setBounds(230, 130, 75, 30);

        textPPKRujukanNama.setEditable(false);
        textPPKRujukanNama.setBackground(new java.awt.Color(245, 250, 240));
        textPPKRujukanNama.setHighlighter(null);
        jPanel2.add(textPPKRujukanNama);
        textPPKRujukanNama.setBounds(310, 130, 260, 30);

        jLabel11.setForeground(new java.awt.Color(0, 131, 62));
        jLabel11.setText("Diagnosa Awal :");
        jLabel11.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel11);
        jLabel11.setBounds(90, 160, 140, 30);

        textKodeDiagnosa.setEditable(false);
        textKodeDiagnosa.setBackground(new java.awt.Color(255, 255, 153));
        textKodeDiagnosa.setHighlighter(null);
        jPanel2.add(textKodeDiagnosa);
        textKodeDiagnosa.setBounds(230, 160, 75, 30);

        textNamaDiagnosa.setEditable(false);
        textNamaDiagnosa.setBackground(new java.awt.Color(255, 255, 153));
        textNamaDiagnosa.setHighlighter(null);
        jPanel2.add(textNamaDiagnosa);
        textNamaDiagnosa.setBounds(310, 160, 260, 30);

        textNamaPoli.setEditable(false);
        textNamaPoli.setBackground(new java.awt.Color(255, 255, 153));
        textNamaPoli.setHighlighter(null);
        jPanel2.add(textNamaPoli);
        textNamaPoli.setBounds(310, 190, 260, 30);

        textKdPoli.setEditable(false);
        textKdPoli.setBackground(new java.awt.Color(255, 255, 153));
        textKdPoli.setHighlighter(null);
        jPanel2.add(textKdPoli);
        textKdPoli.setBounds(230, 190, 75, 30);

        LabelPoli.setForeground(new java.awt.Color(0, 131, 62));
        LabelPoli.setText("Poli Tujuan :");
        LabelPoli.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        LabelPoli.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(LabelPoli);
        LabelPoli.setBounds(120, 190, 110, 30);

        jLabel13.setForeground(new java.awt.Color(0, 131, 62));
        jLabel13.setText("Jns.Pelayanan :");
        jLabel13.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel13.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel13);
        jLabel13.setBounds(90, 280, 140, 30);

        jLabel14.setForeground(new java.awt.Color(0, 131, 62));
        jLabel14.setText("Catatan :");
        jLabel14.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(jLabel14);
        jLabel14.setBounds(640, 460, 90, 30);

        Catatan.setEditable(false);
        Catatan.setHighlighter(null);
        Catatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CatatanKeyPressed(evt);
            }
        });
        jPanel2.add(Catatan);
        Catatan.setBounds(730, 460, 300, 30);

        dropdownJenisPelayanan.setBackground(new java.awt.Color(255, 255, 153));
        dropdownJenisPelayanan.setForeground(new java.awt.Color(0, 131, 62));
        dropdownJenisPelayanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Ranap", "2. Ralan" }));
        dropdownJenisPelayanan.setSelectedIndex(1);
        dropdownJenisPelayanan.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dropdownJenisPelayanan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dropdownJenisPelayananItemStateChanged(evt);
            }
        });
        dropdownJenisPelayanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dropdownJenisPelayananKeyPressed(evt);
            }
        });
        jPanel2.add(dropdownJenisPelayanan);
        dropdownJenisPelayanan.setBounds(230, 280, 110, 30);

        LabelKelas.setForeground(new java.awt.Color(0, 131, 62));
        LabelKelas.setText("Kelas :");
        LabelKelas.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(LabelKelas);
        LabelKelas.setBounds(350, 280, 50, 30);

        dropdownKelas.setForeground(new java.awt.Color(0, 131, 62));
        dropdownKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Kelas 1", "2. Kelas 2", "3. Kelas 3" }));
        dropdownKelas.setSelectedIndex(2);
        dropdownKelas.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dropdownKelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dropdownKelasKeyPressed(evt);
            }
        });
        jPanel2.add(dropdownKelas);
        dropdownKelas.setBounds(400, 280, 100, 30);

        dropdownLakaLantas.setForeground(new java.awt.Color(0, 131, 62));
        dropdownLakaLantas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0. Bukan KLL", "1. KLL Bukan KK", "2. KLL dan KK", "3. KK" }));
        dropdownLakaLantas.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dropdownLakaLantas.setPreferredSize(new java.awt.Dimension(64, 25));
        dropdownLakaLantas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dropdownLakaLantasItemStateChanged(evt);
            }
        });
        dropdownLakaLantas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dropdownLakaLantasKeyPressed(evt);
            }
        });
        jPanel2.add(dropdownLakaLantas);
        dropdownLakaLantas.setBounds(730, 250, 160, 30);

        jLabel8.setForeground(new java.awt.Color(0, 131, 62));
        jLabel8.setText("Data Pasien : ");
        jLabel8.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel8);
        jLabel8.setBounds(90, 10, 140, 30);

        textTglLahir.setEditable(false);
        textTglLahir.setBackground(new java.awt.Color(245, 250, 240));
        textTglLahir.setHighlighter(null);
        jPanel2.add(textTglLahir);
        textTglLahir.setBounds(230, 40, 110, 30);

        jLabel18.setForeground(new java.awt.Color(0, 131, 62));
        jLabel18.setText("J.K :");
        jLabel18.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(jLabel18);
        jLabel18.setBounds(910, 10, 30, 30);

        textJenisKelamin.setEditable(false);
        textJenisKelamin.setBackground(new java.awt.Color(245, 250, 240));
        textJenisKelamin.setHighlighter(null);
        jPanel2.add(textJenisKelamin);
        textJenisKelamin.setBounds(940, 10, 90, 30);

        jLabel24.setForeground(new java.awt.Color(0, 131, 62));
        jLabel24.setText("Peserta :");
        jLabel24.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel24.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel24);
        jLabel24.setBounds(670, 10, 60, 30);

        textJenisPeserta.setEditable(false);
        textJenisPeserta.setBackground(new java.awt.Color(245, 250, 240));
        textJenisPeserta.setHighlighter(null);
        jPanel2.add(textJenisPeserta);
        textJenisPeserta.setBounds(730, 10, 173, 30);

        jLabel25.setForeground(new java.awt.Color(0, 131, 62));
        jLabel25.setText("Status :");
        jLabel25.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel25.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel25);
        jLabel25.setBounds(390, 40, 50, 30);

        textStatus.setEditable(false);
        textStatus.setBackground(new java.awt.Color(245, 250, 240));
        textStatus.setHighlighter(null);
        jPanel2.add(textStatus);
        textStatus.setBounds(440, 40, 130, 30);

        jLabel27.setForeground(new java.awt.Color(0, 131, 62));
        jLabel27.setText("Asal Rujukan :");
        jLabel27.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(jLabel27);
        jLabel27.setBounds(630, 100, 100, 30);

        dropdownAsalRujukan.setForeground(new java.awt.Color(0, 131, 62));
        dropdownAsalRujukan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Faskes 1", "2. Faskes 2(RS)" }));
        dropdownAsalRujukan.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dropdownAsalRujukan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dropdownAsalRujukanKeyPressed(evt);
            }
        });
        jPanel2.add(dropdownAsalRujukan);
        dropdownAsalRujukan.setBounds(730, 100, 170, 30);

        textNoTelp.setHighlighter(null);
        textNoTelp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textNoTelpKeyPressed(evt);
            }
        });
        jPanel2.add(textNoTelp);
        textNoTelp.setBounds(730, 190, 160, 30);

        dropdownKatarak.setForeground(new java.awt.Color(0, 131, 62));
        dropdownKatarak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0. Tidak", "1.Ya" }));
        dropdownKatarak.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dropdownKatarak.setPreferredSize(new java.awt.Dimension(64, 25));
        dropdownKatarak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dropdownKatarakKeyPressed(evt);
            }
        });
        jPanel2.add(dropdownKatarak);
        dropdownKatarak.setBounds(730, 220, 160, 30);

        jLabel37.setForeground(new java.awt.Color(0, 131, 62));
        jLabel37.setText("Katarak :");
        jLabel37.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(jLabel37);
        jLabel37.setBounds(640, 220, 87, 30);

        jLabel38.setForeground(new java.awt.Color(0, 131, 62));
        jLabel38.setText("Tgl KLL :");
        jLabel38.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel38.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel38);
        jLabel38.setBounds(650, 280, 80, 30);

        dateTglKLL.setForeground(new java.awt.Color(50, 70, 50));
        dateTglKLL.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "05-09-2023" }));
        dateTglKLL.setDisplayFormat("dd-MM-yyyy");
        dateTglKLL.setEnabled(false);
        dateTglKLL.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dateTglKLL.setOpaque(false);
        dateTglKLL.setPreferredSize(new java.awt.Dimension(64, 25));
        dateTglKLL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dateTglKLLKeyPressed(evt);
            }
        });
        jPanel2.add(dateTglKLL);
        dateTglKLL.setBounds(730, 280, 140, 30);

        LabelPoli2.setForeground(new java.awt.Color(0, 131, 62));
        LabelPoli2.setText("Dokter DPJP :");
        LabelPoli2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        LabelPoli2.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(LabelPoli2);
        LabelPoli2.setBounds(120, 220, 110, 30);

        textDPJPRujukanKode.setEditable(false);
        textDPJPRujukanKode.setBackground(new java.awt.Color(255, 255, 153));
        textDPJPRujukanKode.setHighlighter(null);
        textDPJPRujukanKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textDPJPRujukanKodeActionPerformed(evt);
            }
        });
        jPanel2.add(textDPJPRujukanKode);
        textDPJPRujukanKode.setBounds(230, 220, 75, 30);

        textDPJPRujukanNama.setEditable(false);
        textDPJPRujukanNama.setBackground(new java.awt.Color(255, 255, 153));
        textDPJPRujukanNama.setHighlighter(null);
        jPanel2.add(textDPJPRujukanNama);
        textDPJPRujukanNama.setBounds(310, 220, 260, 30);

        jLabel36.setForeground(new java.awt.Color(0, 131, 62));
        jLabel36.setText("Keterangan :");
        jLabel36.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel36.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel36);
        jLabel36.setBounds(640, 310, 87, 30);

        textKeterangan.setEditable(false);
        textKeterangan.setHighlighter(null);
        textKeterangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textKeteranganKeyPressed(evt);
            }
        });
        jPanel2.add(textKeterangan);
        textKeterangan.setBounds(730, 310, 257, 30);

        jLabel40.setForeground(new java.awt.Color(0, 131, 62));
        jLabel40.setText("Suplesi :");
        jLabel40.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(jLabel40);
        jLabel40.setBounds(640, 340, 87, 30);

        Suplesi.setForeground(new java.awt.Color(0, 131, 62));
        Suplesi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0. Tidak", "1.Ya" }));
        Suplesi.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        Suplesi.setPreferredSize(new java.awt.Dimension(64, 25));
        Suplesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuplesiKeyPressed(evt);
            }
        });
        jPanel2.add(Suplesi);
        Suplesi.setBounds(730, 340, 90, 30);

        NoSEPSuplesi.setHighlighter(null);
        NoSEPSuplesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoSEPSuplesiKeyPressed(evt);
            }
        });
        jPanel2.add(NoSEPSuplesi);
        NoSEPSuplesi.setBounds(890, 340, 140, 30);

        jLabel41.setForeground(new java.awt.Color(0, 131, 62));
        jLabel41.setText("Suplesi :");
        jLabel41.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel41.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel41);
        jLabel41.setBounds(820, 340, 68, 30);

        LabelPoli3.setForeground(new java.awt.Color(0, 131, 62));
        LabelPoli3.setText("Propinsi KLL :");
        LabelPoli3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(LabelPoli3);
        LabelPoli3.setBounds(640, 370, 87, 30);

        KdPropinsi.setEditable(false);
        KdPropinsi.setBackground(new java.awt.Color(245, 250, 240));
        KdPropinsi.setHighlighter(null);
        jPanel2.add(KdPropinsi);
        KdPropinsi.setBounds(730, 370, 55, 30);

        NmPropinsi.setEditable(false);
        NmPropinsi.setBackground(new java.awt.Color(245, 250, 240));
        NmPropinsi.setHighlighter(null);
        jPanel2.add(NmPropinsi);
        NmPropinsi.setBounds(790, 370, 240, 30);

        LabelPoli4.setForeground(new java.awt.Color(0, 131, 62));
        LabelPoli4.setText("Kabupaten KLL :");
        LabelPoli4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(LabelPoli4);
        LabelPoli4.setBounds(620, 400, 110, 30);

        KdKabupaten.setEditable(false);
        KdKabupaten.setBackground(new java.awt.Color(245, 250, 240));
        KdKabupaten.setHighlighter(null);
        jPanel2.add(KdKabupaten);
        KdKabupaten.setBounds(730, 400, 55, 30);

        NmKabupaten.setEditable(false);
        NmKabupaten.setBackground(new java.awt.Color(245, 250, 240));
        NmKabupaten.setHighlighter(null);
        jPanel2.add(NmKabupaten);
        NmKabupaten.setBounds(790, 400, 240, 30);

        LabelPoli5.setForeground(new java.awt.Color(0, 131, 62));
        LabelPoli5.setText("Kecamatan KLL :");
        LabelPoli5.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(LabelPoli5);
        LabelPoli5.setBounds(610, 430, 120, 30);

        KdKecamatan.setEditable(false);
        KdKecamatan.setBackground(new java.awt.Color(245, 250, 240));
        KdKecamatan.setHighlighter(null);
        jPanel2.add(KdKecamatan);
        KdKecamatan.setBounds(730, 430, 55, 30);

        NmKecamatan.setEditable(false);
        NmKecamatan.setBackground(new java.awt.Color(245, 250, 240));
        NmKecamatan.setHighlighter(null);
        jPanel2.add(NmKecamatan);
        NmKecamatan.setBounds(790, 430, 240, 30);

        jLabel42.setForeground(new java.awt.Color(0, 131, 62));
        jLabel42.setText("Tujuan Kunjungan :");
        jLabel42.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel42.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel42);
        jLabel42.setBounds(90, 310, 140, 30);

        dropdownTujuanKunjungan.setBackground(new java.awt.Color(255, 255, 153));
        dropdownTujuanKunjungan.setForeground(new java.awt.Color(0, 131, 62));
        dropdownTujuanKunjungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0. Normal", "1. Prosedur", "2. Konsul Dokter" }));
        dropdownTujuanKunjungan.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dropdownTujuanKunjungan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dropdownTujuanKunjunganItemStateChanged(evt);
            }
        });
        dropdownTujuanKunjungan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dropdownTujuanKunjunganKeyPressed(evt);
            }
        });
        jPanel2.add(dropdownTujuanKunjungan);
        dropdownTujuanKunjungan.setBounds(230, 310, 340, 30);

        dropdownFlagProsedur.setForeground(new java.awt.Color(0, 131, 62));
        dropdownFlagProsedur.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "0. Prosedur Tidak Berkelanjutan", "1. Prosedur dan Terapi Berkelanjutan" }));
        dropdownFlagProsedur.setEnabled(false);
        dropdownFlagProsedur.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dropdownFlagProsedur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dropdownFlagProsedurKeyPressed(evt);
            }
        });
        jPanel2.add(dropdownFlagProsedur);
        dropdownFlagProsedur.setBounds(230, 340, 340, 30);

        jLabel43.setForeground(new java.awt.Color(0, 131, 62));
        jLabel43.setText("Flag Prosedur :");
        jLabel43.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel43.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel43);
        jLabel43.setBounds(90, 340, 140, 30);

        jLabel44.setForeground(new java.awt.Color(0, 131, 62));
        jLabel44.setText("Penunjang :");
        jLabel44.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel44.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel44);
        jLabel44.setBounds(90, 370, 140, 30);

        dropdownPenunjang.setForeground(new java.awt.Color(0, 131, 62));
        dropdownPenunjang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "1. Radioterapi", "2. Kemoterapi", "3. Rehabilitasi Medik", "4. Rehabilitasi Psikososial", "5. Transfusi Darah", "6. Pelayanan Gigi", "7. Laboratorium", "8. USG", "9. Farmasi", "10. Lain-Lain", "11. MRI", "12. HEMODIALISA" }));
        dropdownPenunjang.setEnabled(false);
        dropdownPenunjang.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dropdownPenunjang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dropdownPenunjangKeyPressed(evt);
            }
        });
        jPanel2.add(dropdownPenunjang);
        dropdownPenunjang.setBounds(230, 370, 340, 30);

        jLabel45.setForeground(new java.awt.Color(0, 131, 62));
        jLabel45.setText("Asesmen Pelayanan :");
        jLabel45.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel45.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel45);
        jLabel45.setBounds(90, 400, 140, 30);

        dropdownAsesmenPelayanan.setBackground(new java.awt.Color(255, 255, 153));
        dropdownAsesmenPelayanan.setForeground(new java.awt.Color(0, 131, 62));
        dropdownAsesmenPelayanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "1. Poli spesialis tidak tersedia pada hari sebelumnya", "2. Jam Poli telah berakhir pada hari sebelumnya", "3. Spesialis yang dimaksud tidak praktek pada hari sebelumnya", "4. Atas Instruksi RS", "5. Tujuan Kontrol" }));
        dropdownAsesmenPelayanan.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dropdownAsesmenPelayanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dropdownAsesmenPelayananKeyPressed(evt);
            }
        });
        jPanel2.add(dropdownAsesmenPelayanan);
        dropdownAsesmenPelayanan.setBounds(230, 400, 340, 30);

        LabelPoli6.setForeground(new java.awt.Color(0, 131, 62));
        LabelPoli6.setText("DPJP Layanan :");
        LabelPoli6.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        LabelPoli6.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(LabelPoli6);
        LabelPoli6.setBounds(90, 430, 140, 30);

        textDPJPLayananKode.setEditable(false);
        textDPJPLayananKode.setBackground(new java.awt.Color(255, 255, 153));
        textDPJPLayananKode.setHighlighter(null);
        jPanel2.add(textDPJPLayananKode);
        textDPJPLayananKode.setBounds(230, 430, 80, 30);

        textDPJPLayananNama.setEditable(false);
        textDPJPLayananNama.setBackground(new java.awt.Color(255, 255, 153));
        textDPJPLayananNama.setHighlighter(null);
        jPanel2.add(textDPJPLayananNama);
        textDPJPLayananNama.setBounds(310, 430, 260, 30);

        btnDPJPLayanan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        btnDPJPLayanan.setMnemonic('X');
        btnDPJPLayanan.setToolTipText("Alt+X");
        btnDPJPLayanan.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnDPJPLayanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDPJPLayananActionPerformed(evt);
            }
        });
        btnDPJPLayanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDPJPLayananKeyPressed(evt);
            }
        });
        jPanel2.add(btnDPJPLayanan);
        btnDPJPLayanan.setBounds(570, 220, 40, 30);

        jLabel55.setForeground(new java.awt.Color(0, 131, 62));
        jLabel55.setText("Laka Lantas :");
        jLabel55.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(jLabel55);
        jLabel55.setBounds(640, 250, 87, 30);

        jLabel56.setForeground(new java.awt.Color(0, 131, 62));
        jLabel56.setText("No.Telp :");
        jLabel56.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel56.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel56);
        jLabel56.setBounds(670, 190, 58, 30);

        jLabel12.setForeground(new java.awt.Color(0, 131, 62));
        jLabel12.setText("Tgl.Lahir :");
        jLabel12.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel12);
        jLabel12.setBounds(120, 40, 110, 30);

        jLabel6.setForeground(new java.awt.Color(0, 131, 62));
        jLabel6.setText("NIK :");
        jLabel6.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(jLabel6);
        jLabel6.setBounds(650, 40, 80, 30);

        textNoSKDP.setEditable(false);
        textNoSKDP.setBackground(new java.awt.Color(255, 255, 153));
        textNoSKDP.setHighlighter(null);
        textNoSKDP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textNoSKDPKeyPressed(evt);
            }
        });
        jPanel2.add(textNoSKDP);
        textNoSKDP.setBounds(230, 70, 340, 30);

        jLabel26.setForeground(new java.awt.Color(0, 131, 62));
        jLabel26.setText("No.Rujukan :");
        jLabel26.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel26.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel26);
        jLabel26.setBounds(130, 100, 100, 30);

        textNIK.setEditable(false);
        textNIK.setBackground(new java.awt.Color(255, 255, 153));
        textNIK.setHighlighter(null);
        jPanel2.add(textNIK);
        textNIK.setBounds(730, 40, 300, 30);

        jLabel7.setForeground(new java.awt.Color(0, 131, 62));
        jLabel7.setText("No.Kartu :");
        jLabel7.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jPanel2.add(jLabel7);
        jLabel7.setBounds(650, 70, 80, 30);

        btnDPJPLayanan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        btnDPJPLayanan1.setMnemonic('X');
        btnDPJPLayanan1.setToolTipText("Alt+X");
        btnDPJPLayanan1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnDPJPLayanan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDPJPLayanan1ActionPerformed(evt);
            }
        });
        btnDPJPLayanan1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDPJPLayanan1KeyPressed(evt);
            }
        });
        jPanel2.add(btnDPJPLayanan1);
        btnDPJPLayanan1.setBounds(570, 190, 40, 30);

        btnDiagnosaAwal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        btnDiagnosaAwal.setMnemonic('X');
        btnDiagnosaAwal.setToolTipText("Alt+X");
        btnDiagnosaAwal.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnDiagnosaAwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiagnosaAwalActionPerformed(evt);
            }
        });
        btnDiagnosaAwal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDiagnosaAwalKeyPressed(evt);
            }
        });
        jPanel2.add(btnDiagnosaAwal);
        btnDiagnosaAwal.setBounds(570, 160, 40, 30);

        btnPilihRujukan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        btnPilihRujukan.setMnemonic('X');
        btnPilihRujukan.setToolTipText("Alt+X");
        btnPilihRujukan.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnPilihRujukan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihRujukanActionPerformed(evt);
            }
        });
        btnPilihRujukan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPilihRujukanKeyPressed(evt);
            }
        });
        jPanel2.add(btnPilihRujukan);
        btnPilihRujukan.setBounds(570, 100, 40, 30);

        btnDiagnosaAwal2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        btnDiagnosaAwal2.setMnemonic('X');
        btnDiagnosaAwal2.setText("Riwayat Layanan BPJS");
        btnDiagnosaAwal2.setToolTipText("Alt+X");
        btnDiagnosaAwal2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnDiagnosaAwal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiagnosaAwal2ActionPerformed(evt);
            }
        });
        btnDiagnosaAwal2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDiagnosaAwal2KeyPressed(evt);
            }
        });
        jPanel2.add(btnDiagnosaAwal2);
        btnDiagnosaAwal2.setBounds(1040, 160, 220, 30);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(238, 238, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(615, 200));

        btnSimpan.setForeground(new java.awt.Color(0, 131, 62));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/konfirmasi.png"))); // NOI18N
        btnSimpan.setMnemonic('S');
        btnSimpan.setText("Konfirmasi");
        btnSimpan.setToolTipText("Alt+S");
        btnSimpan.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnSimpan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpan.setPreferredSize(new java.awt.Dimension(300, 45));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        btnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSimpanKeyPressed(evt);
            }
        });
        jPanel3.add(btnSimpan);

        btnFingerPrint.setForeground(new java.awt.Color(0, 131, 62));
        btnFingerPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/fingerprint.png"))); // NOI18N
        btnFingerPrint.setMnemonic('K');
        btnFingerPrint.setText("FINGERPRINT BPJS");
        btnFingerPrint.setToolTipText("Alt+K");
        btnFingerPrint.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnFingerPrint.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnFingerPrint.setPreferredSize(new java.awt.Dimension(300, 45));
        btnFingerPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFingerPrintActionPerformed(evt);
            }
        });
        btnFingerPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnFingerPrintKeyPressed(evt);
            }
        });
        jPanel3.add(btnFingerPrint);

        btnKeluar.setForeground(new java.awt.Color(0, 131, 62));
        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/reset.png"))); // NOI18N
        btnKeluar.setMnemonic('K');
        btnKeluar.setText("Batal");
        btnKeluar.setToolTipText("Alt+K");
        btnKeluar.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnKeluar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnKeluar.setPreferredSize(new java.awt.Dimension(300, 45));
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        btnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnKeluarKeyPressed(evt);
            }
        });
        jPanel3.add(btnKeluar);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void btnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnKeluarActionPerformed(null);
        }
    }//GEN-LAST:event_btnKeluarKeyPressed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSimpanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSimpanActionPerformed(null);
        }
    }//GEN-LAST:event_btnSimpanKeyPressed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        cekFinger(textNoPeserta.getText());
        if (TNoRw.getText().trim().equals("") || textNamaPasien.getText().trim().equals("")) {
            Valid.textKosong(TNoRw, "Pasien");
        } else if (textNoPeserta.getText().trim().equals("")) {
            Valid.textKosong(textNoPeserta, "Nomor Kartu");
        } else if (textPPKRujukanKode.getText().trim().equals("") || textPPKRujukanNama.getText().trim().equals("")) {
            Valid.textKosong(textPPKRujukanKode, "PPK Rujukan");
        } else if (textPPKPelayananKode.getText().trim().equals("") || textPPKPelayananNama.getText().trim().equals("")) {
            Valid.textKosong(textPPKPelayananKode, "PPK Pelayanan");
        } else if (textKodeDiagnosa.getText().trim().equals("") || textNamaDiagnosa.getText().trim().equals("")) {
            Valid.textKosong(textKodeDiagnosa, "Diagnosa");
        } else if (Catatan.getText().trim().equals("")) {
            Valid.textKosong(Catatan, "Catatan");
        } else if ((dropdownJenisPelayanan.getSelectedIndex() == 1) && (textKdPoli.getText().trim().equals("") || textNamaPoli.getText().trim().equals(""))) {
            Valid.textKosong(textKdPoli, "Poli Tujuan");
        } else if ((dropdownLakaLantas.getSelectedIndex() == 1) && textKeterangan.getText().equals("")) {
            Valid.textKosong(textKeterangan, "Keterangan");
        } else if (textDPJPRujukanKode.getText().trim().equals("") || textDPJPRujukanNama.getText().trim().equals("")) {
            Valid.textKosong(textDPJPRujukanKode, "DPJP");
        } else if (statusfinger == false && Sequel.cariInteger("select timestampdiff(year, '" + textTglLahir.getText() + "', CURRENT_DATE())") >= 17 && dropdownJenisPelayanan.getSelectedIndex() != 0 && !textKdPoli.getText().equals("IGD")) {
            JOptionPane.showMessageDialog(rootPane, "Maaf, Pasien belum melakukan Fingerprint");
            BukaFingerPrint(textNoPeserta.getText());
        } else {
            kdPoliBPJS = Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", textKdPoli.getText());
            kdDokterBPJS = Sequel.cariIsi("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs=?", textDPJPRujukanKode.getText());
            if (!kdPoliBPJS.equals("")) {
                isPoli();
            } else {
                isPoli();
            }
            isCekPasien();
            isNumber();
            if (dropdownJenisPelayanan.getSelectedIndex() == 0) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                insertSEP();
                this.setCursor(Cursor.getDefaultCursor());
            } else if (dropdownJenisPelayanan.getSelectedIndex() == 1) {
                if (textNamaPoli.getText().toLowerCase().contains("darurat")) {
                    if (Sequel.cariInteger("select count(bridging_sep.no_kartu) from bridging_sep where bridging_sep.no_kartu='" + noPeserta + "' and bridging_sep.jnspelayanan='" + dropdownJenisPelayanan.getSelectedItem().toString().substring(0, 1) + "' and bridging_sep.tglsep like '%" + Valid.SetTgl(dateTglSEP.getSelectedItem() + "") + "%' and bridging_sep.nmpolitujuan like '%darurat%'") >= 3) {
                        JOptionPane.showMessageDialog(rootPane, "Maaf, sebelumnya sudah dilakukan 3x pembuatan SEP di jenis pelayanan yang sama..!!");

                    } else {
                        if ((!kdDokterBPJS.equals("")) && (!kdPoliBPJS.equals(""))) {
                            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            SimpanAntrianOnSite();
                            this.setCursor(Cursor.getDefaultCursor());
                        }
                        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        insertSEP();
                        this.setCursor(Cursor.getDefaultCursor());
                    }
                } else if (!textNamaPoli.getText().toLowerCase().contains("darurat")) {
                    if (Sequel.cariInteger("select count(bridging_sep.no_kartu) from bridging_sep where bridging_sep.no_kartu='" + noPeserta + "' and bridging_sep.jnspelayanan='" + dropdownJenisPelayanan.getSelectedItem().toString().substring(0, 1) + "' and bridging_sep.tglsep like '%" + Valid.SetTgl(dateTglSEP.getSelectedItem() + "") + "%' and bridging_sep.nmpolitujuan not like '%darurat%'") >= 1) {
                        JOptionPane.showMessageDialog(rootPane, "Maaf, sebelumnya sudah dilakukan pembuatan SEP di jenis pelayanan yang sama..!!");
                    } else {
                        if ((!kdDokterBPJS.equals("")) && (!kdPoliBPJS.equals(""))) {
                            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            SimpanAntrianOnSite();
                            this.setCursor(Cursor.getDefaultCursor());
                        }
                        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        insertSEP();
                        this.setCursor(Cursor.getDefaultCursor());
                    }
                }
            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnDPJPLayananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDPJPLayananKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDPJPLayananKeyPressed

    private void btnDPJPLayananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDPJPLayananActionPerformed
        dokter.setSize(jPanel1.getWidth() - 75, jPanel1.getHeight() - 75);
        dokter.setLocationRelativeTo(jPanel1);
        dokter.carinamadokter(textKdPoli.getText(), textNamaPoli.getText());
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDPJPLayananActionPerformed

    private void dropdownAsesmenPelayananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dropdownAsesmenPelayananKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dropdownAsesmenPelayananKeyPressed

    private void dropdownPenunjangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dropdownPenunjangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dropdownPenunjangKeyPressed

    private void dropdownFlagProsedurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dropdownFlagProsedurKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dropdownFlagProsedurKeyPressed

    private void dropdownTujuanKunjunganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dropdownTujuanKunjunganKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dropdownTujuanKunjunganKeyPressed

    private void dropdownTujuanKunjunganItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dropdownTujuanKunjunganItemStateChanged
        if (dropdownTujuanKunjungan.getSelectedIndex() == 0) {
            dropdownFlagProsedur.setEnabled(false);
            dropdownFlagProsedur.setSelectedIndex(0);
            dropdownPenunjang.setEnabled(false);
            dropdownPenunjang.setSelectedIndex(0);
            dropdownAsesmenPelayanan.setEnabled(true);
        } else {
            if (dropdownTujuanKunjungan.getSelectedIndex() == 1) {
                dropdownAsesmenPelayanan.setSelectedIndex(0);
                dropdownAsesmenPelayanan.setEnabled(false);
            } else {
                dropdownAsesmenPelayanan.setEnabled(true);
            }
            if (dropdownFlagProsedur.getSelectedIndex() == 0) {
                dropdownFlagProsedur.setSelectedIndex(2);
            }
            dropdownFlagProsedur.setEnabled(true);
            if (dropdownPenunjang.getSelectedIndex() == 0) {
                dropdownPenunjang.setSelectedIndex(10);
            }
            dropdownPenunjang.setEnabled(true);
        }
    }//GEN-LAST:event_dropdownTujuanKunjunganItemStateChanged

    private void NoSEPSuplesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoSEPSuplesiKeyPressed

    }//GEN-LAST:event_NoSEPSuplesiKeyPressed

    private void SuplesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuplesiKeyPressed
        Valid.pindah(evt, textKeterangan, NoSEPSuplesi);
    }//GEN-LAST:event_SuplesiKeyPressed

    private void textKeteranganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textKeteranganKeyPressed
        Valid.pindah(evt, dateTglKLL, Suplesi);
    }//GEN-LAST:event_textKeteranganKeyPressed

    private void dateTglKLLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateTglKLLKeyPressed
        Valid.pindah(evt, dropdownLakaLantas, textKeterangan);
    }//GEN-LAST:event_dateTglKLLKeyPressed

    private void dropdownKatarakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dropdownKatarakKeyPressed
        Valid.pindah(evt, Catatan, textNoTelp);
    }//GEN-LAST:event_dropdownKatarakKeyPressed

    private void textNoTelpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNoTelpKeyPressed
        Valid.pindah(evt, dropdownKatarak, dropdownLakaLantas);
    }//GEN-LAST:event_textNoTelpKeyPressed

    private void dropdownAsalRujukanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dropdownAsalRujukanKeyPressed

    }//GEN-LAST:event_dropdownAsalRujukanKeyPressed

    private void dropdownLakaLantasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dropdownLakaLantasKeyPressed
        Valid.pindah(evt, textNoTelp, dateTglKLL);
    }//GEN-LAST:event_dropdownLakaLantasKeyPressed

    private void dropdownLakaLantasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dropdownLakaLantasItemStateChanged
        if (dropdownLakaLantas.getSelectedIndex() == 0) {
            dateTglKLL.setEnabled(false);
            textKeterangan.setEditable(false);
            textKeterangan.setText("");
        } else {
            dateTglKLL.setEnabled(true);
            textKeterangan.setEditable(true);
        }
    }//GEN-LAST:event_dropdownLakaLantasItemStateChanged

    private void dropdownKelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dropdownKelasKeyPressed

    }//GEN-LAST:event_dropdownKelasKeyPressed

    private void dropdownJenisPelayananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dropdownJenisPelayananKeyPressed

    }//GEN-LAST:event_dropdownJenisPelayananKeyPressed

    private void dropdownJenisPelayananItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dropdownJenisPelayananItemStateChanged
        if (dropdownJenisPelayanan.getSelectedIndex() == 0) {
            textKdPoli.setText("");
            textNamaPoli.setText("");
            LabelPoli.setVisible(false);
            textKdPoli.setVisible(false);
            textNamaPoli.setVisible(false);

            textDPJPLayananKode.setText("");
            textDPJPLayananNama.setText("");
            btnDPJPLayanan.setEnabled(false);
        } else if (dropdownJenisPelayanan.getSelectedIndex() == 1) {
            LabelPoli.setVisible(true);
            textKdPoli.setVisible(true);
            textNamaPoli.setVisible(true);

            btnDPJPLayanan.setEnabled(true);
        }
    }//GEN-LAST:event_dropdownJenisPelayananItemStateChanged

    private void CatatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CatatanKeyPressed

    }//GEN-LAST:event_CatatanKeyPressed

    private void textNoRujukanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNoRujukanKeyPressed

    }//GEN-LAST:event_textNoRujukanKeyPressed

    private void dateTglRujukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateTglRujukKeyPressed
        Valid.pindah(evt, textNoRujukan, dateTglSEP);
    }//GEN-LAST:event_dateTglRujukKeyPressed

    private void dateTglSEPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateTglSEPKeyPressed
        Valid.pindah(evt, dateTglRujuk, dropdownAsalRujukan);
    }//GEN-LAST:event_dateTglSEPKeyPressed

    private void textNoRMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textNoRMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textNoRMActionPerformed

    private void textNoSKDPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNoSKDPKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textNoSKDPKeyPressed

    private void btnFingerPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFingerPrintActionPerformed
        if (!textNoPeserta.toString().equals("")) {
            BukaFingerPrint(textNoPeserta.getText());
        }
    }//GEN-LAST:event_btnFingerPrintActionPerformed

    private void btnFingerPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnFingerPrintKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFingerPrintKeyPressed

    private void btnDPJPLayanan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDPJPLayanan1ActionPerformed
        poli.setSize(jPanel1.getWidth() - 75, jPanel1.getHeight() - 75);
        poli.tampil();
        poli.setLocationRelativeTo(jPanel1);
        poli.setVisible(true);
    }//GEN-LAST:event_btnDPJPLayanan1ActionPerformed

    private void btnDPJPLayanan1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDPJPLayanan1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDPJPLayanan1KeyPressed

    private void btnDiagnosaAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiagnosaAwalActionPerformed
        penyakit.setSize(jPanel1.getWidth() - 70, jPanel1.getHeight() - 70);
        penyakit.setLocationRelativeTo(jPanel1);
        penyakit.setVisible(true);
    }//GEN-LAST:event_btnDiagnosaAwalActionPerformed

    private void btnDiagnosaAwalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDiagnosaAwalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDiagnosaAwalKeyPressed

    private void btnPilihRujukanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihRujukanActionPerformed
        if (textNoPeserta.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "No.Kartu masih kosong...!!");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            rujukanterakhir.setSize(jPanel1.getWidth() - 20, jPanel1.getHeight() - 20);
            rujukanterakhir.setLocationRelativeTo(jPanel1);
            rujukanterakhir.tampil(textNoPeserta.getText(), textNamaPasien.getText());
            rujukanterakhir.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_btnPilihRujukanActionPerformed

    private void btnPilihRujukanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPilihRujukanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPilihRujukanKeyPressed

    private void btnDiagnosaAwal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiagnosaAwal2ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        historiPelayanan.setSize(jPanel1.getWidth() - 20, jPanel1.getHeight() - 20);
        historiPelayanan.setLocationRelativeTo(jPanel1);
        historiPelayanan.setKartu(textNoPeserta.getText());
        historiPelayanan.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnDiagnosaAwal2ActionPerformed

    private void btnDiagnosaAwal2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDiagnosaAwal2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDiagnosaAwal2KeyPressed

    private void textDPJPRujukanKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textDPJPRujukanKodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textDPJPRujukanKodeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgRegistrasiSEPMobileJKN dialog = new DlgRegistrasiSEPMobileJKN(new javax.swing.JFrame(), true);
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
    private widget.TextBox Catatan;
    private widget.TextBox KdKabupaten;
    private widget.TextBox KdKecamatan;
    private widget.TextBox KdPropinsi;
    private widget.Label LabelKelas;
    private widget.Label LabelPoli;
    private widget.Label LabelPoli2;
    private widget.Label LabelPoli3;
    private widget.Label LabelPoli4;
    private widget.Label LabelPoli5;
    private widget.Label LabelPoli6;
    private widget.TextBox NmKabupaten;
    private widget.TextBox NmKecamatan;
    private widget.TextBox NmPropinsi;
    private widget.TextBox NoSEPSuplesi;
    private widget.ComboBox Suplesi;
    private widget.Button btnDPJPLayanan;
    private widget.Button btnDPJPLayanan1;
    private widget.Button btnDiagnosaAwal;
    private widget.Button btnDiagnosaAwal2;
    private component.Button btnFingerPrint;
    private component.Button btnKeluar;
    private widget.Button btnPilihRujukan;
    private component.Button btnSimpan;
    private widget.Tanggal dateTglKLL;
    private widget.Tanggal dateTglRujuk;
    private widget.Tanggal dateTglSEP;
    private widget.ComboBox dropdownAsalRujukan;
    private widget.ComboBox dropdownAsesmenPelayanan;
    private widget.ComboBox dropdownFlagProsedur;
    private widget.ComboBox dropdownJenisPelayanan;
    private widget.ComboBox dropdownKatarak;
    private widget.ComboBox dropdownKelas;
    private widget.ComboBox dropdownLakaLantas;
    private widget.ComboBox dropdownPenunjang;
    private widget.ComboBox dropdownTujuanKunjungan;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel18;
    private widget.Label jLabel20;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private component.Panel jPanel1;
    private component.Panel jPanel2;
    private javax.swing.JPanel jPanel3;
    private widget.TextBox textDPJPLayananKode;
    private widget.TextBox textDPJPLayananNama;
    private widget.TextBox textDPJPRujukanKode;
    private widget.TextBox textDPJPRujukanNama;
    private widget.TextBox textJenisKelamin;
    private widget.TextBox textJenisPeserta;
    private widget.TextBox textKdPoli;
    private widget.TextBox textKeterangan;
    private widget.TextBox textKodeDiagnosa;
    private widget.TextBox textNIK;
    private widget.TextBox textNamaDiagnosa;
    private widget.TextBox textNamaPasien;
    private widget.TextBox textNamaPoli;
    private widget.TextBox textNoPeserta;
    private widget.TextBox textNoRM;
    private widget.TextBox textNoRujukan;
    private widget.TextBox textNoSKDP;
    private widget.TextBox textNoTelp;
    private widget.TextBox textPPKPelayananKode;
    private widget.TextBox textPPKPelayananNama;
    private widget.TextBox textPPKRujukanKode;
    private widget.TextBox textPPKRujukanNama;
    private widget.TextBox textStatus;
    private widget.TextBox textTglLahir;
    // End of variables declaration//GEN-END:variables

    public void setPasien(String norm) {

    }

    public void isCek(String norm) {

    }

    private void UpdateUmur() {

    }

    private void isNumber() {

        if (Sequel.cariInteger("select count(booking_registrasi.no_rkm_medis) from booking_registrasi where booking_registrasi.no_rkm_medis='" + textNoRM.getText() + "' and "
                + "booking_registrasi.tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "' and booking_registrasi.kd_dokter='" + kdDokterBPJS + "' and booking_registrasi.kd_poli='" + kdPoliBPJS + "'") > 0) {
            NoReg.setText(Sequel.cariIsi("select booking_registrasi.no_reg from booking_registrasi where booking_registrasi.no_rkm_medis='" + textNoRM.getText() + "' and "
                    + "booking_registrasi.tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "' and booking_registrasi.kd_dokter='" + kdDokterBPJS + "' and booking_registrasi.kd_poli='" + kdPoliBPJS + "'"));

        } else {
            if (BASENOREG.equals("booking")) {
                switch (URUTNOREG) {
                    case "poli":
                        if (Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='" + kdPoliBPJS + "' and tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'")
                                >= Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kdPoliBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'")) {
                            Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='" + kdPoliBPJS + "' and tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        } else {
                            Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kdPoliBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        }
                        break;
                    case "dokter":
                        if (Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='" + kdDokterBPJS + "' and tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'")
                                >= Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kdDokterBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'")) {
                            Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='" + kdDokterBPJS + "' and tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        } else {
                            Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kdDokterBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        }
                        break;
                    case "dokter + poli":
                        if (Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='" + kdDokterBPJS + "' and kd_poli='" + kdPoliBPJS + "' and tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'")
                                >= Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kdDokterBPJS + "' and kd_poli='" + kdPoliBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'")) {
                            Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='" + kdDokterBPJS + "' and kd_poli='" + kdPoliBPJS + "' and tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        } else {
                            Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kdDokterBPJS + "' and kd_poli='" + kdPoliBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        }
                        break;
                    default:
                        if (Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='" + kdPoliBPJS + "' and tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'")
                                >= Sequel.cariInteger("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kdPoliBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'")) {
                            Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='" + kdPoliBPJS + "' and tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        } else {
                            Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kdPoliBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        }
                        break;
                }
            } else {
                switch (URUTNOREG) {
                    case "poli":
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_poli='" + kdPoliBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        break;
                    case "dokter":
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kdDokterBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        break;
                    case "dokter + poli":
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kdDokterBPJS + "' and kd_poli='" + kdPoli + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        break;
                    default:
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from reg_periksa where kd_dokter='" + kdDokterBPJS + "' and tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'", "", 3, NoReg);
                        break;
                }
            }
        }

        Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_rawat,6),signed)),0) from reg_periksa where tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "' ", Valid.SetTgl(dateTglSEP.getSelectedItem().toString()).replaceAll("-", "/") + "/", 6, TNoRw);
    }

    private void tentukanHari() {
        try {
            java.sql.Date hariperiksa = java.sql.Date.valueOf(Valid.SetTgl(dateTglSEP.getSelectedItem().toString()));
            cal.setTime(hariperiksa);
            day = cal.get(Calendar.DAY_OF_WEEK);
            switch (day) {
                case 1:
                    hari = "AKHAD";
                    break;
                case 2:
                    hari = "SENIN";
                    break;
                case 3:
                    hari = "SELASA";
                    break;
                case 4:
                    hari = "RABU";
                    break;
                case 5:
                    hari = "KAMIS";
                    break;
                case 6:
                    hari = "JUMAT";
                    break;
                case 7:
                    hari = "SABTU";
                    break;
                default:
                    break;
            }
            System.out.println(hari);

        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

    }

    private void isCekPasien() {
        try {
            ps2 = koneksi.prepareStatement("select nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"
                    + "namakeluarga,keluarga,pasien.kd_pj,penjab.png_jawab,if(tgl_daftar=?,'Baru','Lama') as daftar, "
                    + "TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()) as tahun,pasien.no_peserta, "
                    + "(TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12)) as bulan, "
                    + "TIMESTAMPDIFF(DAY, DATE_ADD(DATE_ADD(tgl_lahir,INTERVAL TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()) YEAR), INTERVAL TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12) MONTH), CURDATE()) as hari,pasien.no_ktp,pasien.no_tlp "
                    + "from pasien inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "
                    + "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "
                    + "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "
                    + "inner join penjab on pasien.kd_pj=penjab.kd_pj "
                    + "where pasien.no_rkm_medis=?");
            try {
                ps2.setString(1, Valid.SetTgl(dateTglSEP.getSelectedItem() + ""));
                ps2.setString(2, textNoRM.getText());
                rs = ps2.executeQuery();
                while (rs.next()) {
                    TAlmt.setText(rs.getString("asal"));
                    TPngJwb.setText(rs.getString("namakeluarga"));
                    THbngn.setText(rs.getString("keluarga"));
                    NoTelpPasien.setText(rs.getString("no_tlp"));
                    umur = "0";
                    sttsumur = "Th";
                    statusPasien = rs.getString("daftar");
                    if (rs.getInt("tahun") > 0) {
                        umur = rs.getString("tahun");
                        sttsumur = "Th";
                    } else if (rs.getInt("tahun") == 0) {
                        if (rs.getInt("bulan") > 0) {
                            umur = rs.getString("bulan");
                            sttsumur = "Bl";
                        } else if (rs.getInt("bulan") == 0) {
                            umur = rs.getString("hari");
                            sttsumur = "Hr";
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps2 != null) {
                    ps2.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        status = "Baru";
        if (Sequel.cariInteger("select count(no_rkm_medis) from reg_periksa where no_rkm_medis=? and kd_poli=?", textNoRM.getText(), kdPoliBPJS) > 0) {
            status = "Lama";
        }

    }

    private void MnCetakRegisterActionPerformed(String norawat) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Map<String, Object> param = new HashMap<>();
        param.put("namars", Sequel.cariIsi("select nama_instansi from setting"));
        param.put("alamatrs", Sequel.cariIsi("select alamat_instansi from setting"));
        param.put("kotars", Sequel.cariIsi("select kabupaten from setting"));
        param.put("propinsirs", Sequel.cariIsi("select propinsi from setting"));
        param.put("kontakrs", Sequel.cariIsi("select kontak from setting"));
        param.put("emailrs", Sequel.cariIsi("select email from setting"));
        param.put("logo", Sequel.cariGambar("select logo from setting"));
        Valid.MyReportqryabdul("rptBuktiRegister.jasper", "report", "::[ Bukti Register ]::",
                "select IF ((SELECT count( booking_registrasi.no_rkm_medis ) FROM booking_registrasi WHERE booking_registrasi.STATUS = 'Terdaftar'  AND booking_registrasi.no_rkm_medis = reg_periksa.no_rkm_medis AND booking_registrasi.tanggal_periksa = reg_periksa .tgl_registrasi AND kd_dokter = reg_periksa.kd_dokter )= 1,CONCAT( 'A', reg_periksa.no_reg ),CONCAT( 'W', reg_periksa.no_reg ) ) AS no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,pasien.no_tlp,"
                + "reg_periksa.kd_dokter,dokter.nm_dokter,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.umur as umur,poliklinik.nm_poli,"
                + "reg_periksa.p_jawab,reg_periksa.almt_pj,reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.stts_daftar,penjab.png_jawab "
                + "from reg_periksa inner join dokter inner join pasien inner join poliklinik inner join penjab "
                + "on reg_periksa.kd_dokter=dokter.kd_dokter and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "and reg_periksa.kd_pj=penjab.kd_pj and reg_periksa.kd_poli=poliklinik.kd_poli where reg_periksa.no_rawat='" + norawat + "' ", param);
        System.out.println(norawat);
        this.setCursor(Cursor.getDefaultCursor());
    }
    
    private void printSEPdanBuktiRegistrasi(String norawat, String nosep) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        Map<String, Object> param = new HashMap<>();
        param.put("nama", Sequel.cariIsi("select pasien.nm_pasien from reg_periksa join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis where reg_periksa.no_rawat = '" + norawat + "'"));
        param.put("norm", Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat = '"+norawat+"'"));
        param.put("no_rawat", norawat);
        param.put("namars",akses.getnamars());
        param.put("alamatrs",akses.getalamatrs());
        param.put("kotars",akses.getkabupatenrs());
        param.put("propinsirs",akses.getpropinsirs());
        param.put("kontakrs",akses.getkontakrs());
        param.put("emailrs",akses.getemailrs());
        param.put("norawat",norawat);
        param.put("prb",Sequel.cariIsi("select bpjs_prb.prb from bpjs_prb where bpjs_prb.no_sep=?", nosep));
        param.put("dokter",Sequel.cariIsi("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs=?", textDPJPRujukanKode.getText()));
        param.put("noreg",Sequel.cariIsi("select no_reg from reg_periksa where no_rawat=?", norawat));
        param.put("logo",Sequel.cariGambar("select gambar.bpjs from gambar")); 
        param.put("parameter",nosep);
        
        if(dropdownJenisPelayanan.getSelectedIndex()==0){
            Valid.printReport("rptBridgingSEP7.jasper", koneksiDB.PRINTER_REGISTRASI(), "::[ SEP Model 4 ]::", 1, param);
        }else{
            Valid.printReport("rptBridgingSEP8.jasper", koneksiDB.PRINTER_REGISTRASI(), "::[ SEP Model 4 ]::", 1, param);
        }
        Valid.printReport("rptBarcodeRawat.jasper", koneksiDB.PRINTER_BARCODE(), "::[ Barcode Perawatan ]::", 3, param);
        
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void insertSEP() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            tglKLL = "0000-00-00";
            
            if (dropdownLakaLantas.getSelectedIndex() > 0) {
                tglKLL = Valid.SetTgl(dateTglKLL.getSelectedItem() + "");
            }

            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
            utc = String.valueOf(api.GetUTCdatetimeAsString());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
            URL = link + "/SEP/2.0/insert";
            requestJson = "{"
                + "\"request\":{"
                    + "\"t_sep\":{"
                        + "\"noKartu\":\"" + textNoPeserta.getText() + "\","
                        + "\"tglSep\":\"" + Valid.SetTgl(dateTglSEP.getSelectedItem() + "") + "\","
                        + "\"ppkPelayanan\":\"" + textPPKPelayananKode.getText() + "\","
                        + "\"jnsPelayanan\":\"" + dropdownJenisPelayanan.getSelectedItem().toString().substring(0, 1) + "\","
                        + "\"klsRawat\":{"
                            + "\"klsRawatHak\":\"" + dropdownKelas.getSelectedItem().toString().substring(0, 1) + "\","
                            + "\"klsRawatNaik\":\"\","
                            + "\"pembiayaan\":\"\","
                            + "\"penanggungJawab\":\"\""
                        + "},"
                        + "\"noMR\":\"" + textNoRM.getText() + "\","
                        + "\"rujukan\": {"
                        + "\"asalRujukan\":\"" + dropdownAsalRujukan.getSelectedItem().toString().substring(0, 1) + "\","
                        + "\"tglRujukan\":\"" + Valid.SetTgl(dateTglRujuk.getSelectedItem() + "") + "\","
                        + "\"noRujukan\":\"" + textNoRujukan.getText() + "\","
                        + "\"ppkRujukan\":\"" + textPPKRujukanKode.getText() + "\""
                    + "},"
                    + "\"catatan\":\"" + Catatan.getText() + "\","
                    + "\"diagAwal\":\"" + textKodeDiagnosa.getText() + "\","
                    + "\"poli\": {"
                        + "\"tujuan\": \"" + textKdPoli.getText() + "\","
                        + "\"eksekutif\": \"0\""
                    + "},"
                    + "\"cob\": {"
                        + "\"cob\": \"0\""
                    + "},"
                    + "\"katarak\": {"
                        + "\"katarak\": \"" + dropdownKatarak.getSelectedItem().toString().substring(0, 1) + "\""
                    + "},"
                    + "\"jaminan\": {"
                        + "\"lakaLantas\":\"" + dropdownLakaLantas.getSelectedItem().toString().substring(0, 1) + "\","
                        + "\"penjamin\": {"
                            + "\"tglKejadian\": \"" + tglKLL.replaceAll("0000-00-00", "") + "\","
                            + "\"keterangan\": \"" + textKeterangan.getText() + "\","
                            + "\"suplesi\": {"
                                + "\"suplesi\": \"" + Suplesi.getSelectedItem().toString().substring(0, 1) + "\","
                                + "\"noSepSuplesi\": \"" + NoSEPSuplesi.getText() + "\","
                                + "\"lokasiLaka\": {"
                                    + "\"kdPropinsi\": \"" + KdPropinsi.getText() + "\","
                                    + "\"kdKabupaten\": \"" + KdKabupaten.getText() + "\","
                                    + "\"kdKecamatan\": \"" + KdKecamatan.getText() + "\""
                                + "}"
                            + "}"
                        + "}"
                    + "},"
                    + "\"tujuanKunj\": \"" + dropdownTujuanKunjungan.getSelectedItem().toString().substring(0, 1) + "\","
                    + "\"flagProcedure\": \"" + (dropdownFlagProsedur.getSelectedIndex() > 0 ? dropdownFlagProsedur.getSelectedItem().toString().substring(0, 1) : "") + "\","
                    + "\"kdPenunjang\": \"" + (dropdownPenunjang.getSelectedIndex() > 0 ? dropdownPenunjang.getSelectedIndex() + "" : "") + "\","
                    + "\"assesmentPel\": \"" + (dropdownAsesmenPelayanan.getSelectedIndex() > 0 ? dropdownAsesmenPelayanan.getSelectedItem().toString().substring(0, 1) : "") + "\","
                    + "\"skdp\": {"
                        + "\"noSurat\": \"" + textNoSKDP.getText() + "\","
                        + "\"kodeDPJP\": \"" + textDPJPRujukanKode.getText() + "\""
                    + "},"
                    + "\"dpjpLayan\": \"" + (textDPJPLayananKode.getText().equals("") ? "" : textDPJPLayananKode.getText()) + "\","
                    + "\"noTelp\": \"" + textNoTelp.getText() + "\","
                    + "\"user\":\"" + textNoPeserta.getText() + "\""
                    + "}"
                + "}"
            + "}";
            
            requestEntity = new HttpEntity(requestJson, headers);
            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
            nameNode = root.path("metaData");
            System.out.println("code : " + nameNode.path("code").asText());
            JOptionPane.showMessageDialog(rootPane, "Respon BPJS : " + nameNode.path("message").asText());
            if (nameNode.path("code").asText().equals("200")) {
                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("sep").path("noSep");
                
                
                
                if (Sequel.menyimpantf2("reg_periksa", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 19,
                        new String[]{NoReg.getText(), TNoRw.getText(), Valid.SetTgl(dateTglSEP.getSelectedItem() + ""), Sequel.cariIsi("select current_time()"),
                            kdDokterBPJS, textNoRM.getText(), kdPoliBPJS, TPngJwb.getText(), TAlmt.getText(), THbngn.getText(), TBiaya.getText(), "Belum",
                            statusPasien, "Ralan", Kdpnj.getText(), umur, sttsumur, "Belum Bayar", status}) == true) {

                    Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(rujuk_masuk.no_rawat,4),signed)),0) from reg_periksa inner join rujuk_masuk on reg_periksa.no_rawat=rujuk_masuk.no_rawat where reg_periksa.tgl_registrasi='" + Valid.SetTgl(dateTglSEP.getSelectedItem() + "") + "' ", "BR/" + dateformat.format(dateTglSEP.getDate()) + "/", 4, NoRujukMasuk);
                    Sequel.menyimpan("rujuk_masuk", "?,?,?,?,?,?,?,?,?,?", 10, new String[]{
                        TNoRw.getText(), textPPKRujukanNama.getText(), "-", textNoRujukan.getText(), "0", textPPKRujukanNama.getText(), textKodeDiagnosa.getText(), "-",
                        "-", NoRujukMasuk.getText()
                    });

                    if (Sequel.menyimpantf2("bridging_sep", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "SEP", 52, new String[]{
                        response.asText(),
                        TNoRw.getText(),
                        Valid.SetTgl(dateTglSEP.getSelectedItem() + ""),
                        Valid.SetTgl(dateTglRujuk.getSelectedItem() + ""),
                        textNoRujukan.getText(),
                        textPPKRujukanKode.getText(),
                        textPPKRujukanNama.getText(),
                        textPPKPelayananKode.getText(),
                        textPPKPelayananNama.getText(),
                        dropdownJenisPelayanan.getSelectedItem().toString().substring(0, 1),
                        Catatan.getText(),
                        textKodeDiagnosa.getText(),
                        textNamaDiagnosa.getText(),
                        textKdPoli.getText(),
                        textNamaPoli.getText(),
                        dropdownKelas.getSelectedItem().toString().substring(0, 1),
                        "",
                        "",
                        "",
                        dropdownLakaLantas.getSelectedItem().toString().substring(0, 1),
                        textNoRM.getText(),
                        textNoRM.getText(),
                        textNamaPasien.getText(),
                        textTglLahir.getText(),
                        textJenisPeserta.getText(),
                        textJenisKelamin.getText(),
                        textNoPeserta.getText(),
                        "0000-00-00 00:00:00",
                        dropdownAsalRujukan.getSelectedItem().toString(),
                        "0. Tidak",
                        "0. Tidak",
                        textNoTelp.getText(),
                        dropdownKatarak.getSelectedItem().toString(),
                        tglKLL,
                        textKeterangan.getText(),
                        Suplesi.getSelectedItem().toString(),
                        NoSEPSuplesi.getText(),
                        KdPropinsi.getText(),
                        NmPropinsi.getText(),
                        KdKabupaten.getText(),
                        NmKabupaten.getText(),
                        KdKecamatan.getText(),
                        NmKecamatan.getText(),
                        textNoSKDP.getText(),
                        textDPJPRujukanKode.getText(),
                        textDPJPRujukanNama.getText(),
                        dropdownTujuanKunjungan.getSelectedItem().toString().substring(0, 1),
                        (dropdownFlagProsedur.getSelectedIndex() > 0 ? dropdownFlagProsedur.getSelectedItem().toString().substring(0, 1) : ""),
                        (dropdownPenunjang.getSelectedIndex() > 0 ? dropdownPenunjang.getSelectedIndex() + "" : ""),
                        (dropdownAsesmenPelayanan.getSelectedIndex() > 0 ? dropdownAsesmenPelayanan.getSelectedItem().toString().substring(0, 1) : ""),
                        textDPJPLayananKode.getText(),
                        textDPJPLayananNama.getText()
                    }) == true) {
//                        CetakSEPOtomatis(response.asText());

                    }

                    if (!prb.equals("")) {
                        if (Sequel.menyimpantf("bpjs_prb", "?,?", "PRB", 2, new String[]{response.asText(), prb}) == true) {
                            prb = "";
                        }
                    }

                    if (Sequel.cariInteger("select count(booking_registrasi.no_rkm_medis) from booking_registrasi where booking_registrasi.no_rkm_medis='" + textNoRM.getText() + "' and "
                            + "booking_registrasi.tanggal_periksa='" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "' and booking_registrasi.kd_dokter='" + kdDokterBPJS + "' and booking_registrasi.kd_poli='" + kdPoliBPJS + "'") > 0) {
                        Sequel.queryu2("update booking_registrasi set status='Terdaftar' where no_rkm_medis=? and tanggal_periksa=? and kd_dokter=? and kd_poli=? ", 4, new String[]{
                            textNoRM.getText(), Valid.SetTgl(dateTglSEP.getSelectedItem().toString()), kdDokterBPJS, kdPoliBPJS
                        });
                        Sequel.queryu2("update booking_registrasi set waktu_kunjungan=now() where no_rkm_medis=? and tanggal_periksa=? and kd_dokter=? and kd_poli=? ", 4, new String[]{
                            textNoRM.getText(), Valid.SetTgl(dateTglSEP.getSelectedItem().toString()), kdDokterBPJS, kdPoliBPJS
                        });
                    }
                    printSEPdanBuktiRegistrasi(TNoRw.getText(), response.asText());

                    emptTeks();
                    dispose();
                }

            }
        } catch (Exception ex) {
            System.out.println("Notifikasi Bridging : " + ex);
            if (ex.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server BPJS terputus...!");
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }
    
    private boolean registerPasien() {
        int coba = 0, maxCoba = 5;
        
        String[] values = new String[] {
            NoReg.getText(),
            TNoRw.getText(),
            Valid.SetTgl(dateTglSEP.getSelectedItem() + ""),
            Sequel.cariIsi("select current_time()"),
            kdDokterBPJS,
            textNoRM.getText(),
            kdPoliBPJS,
            TPngJwb.getText(),
            TAlmt.getText(),
            THbngn.getText(),
            TBiaya.getText(),
            "Belum",
            statusPasien,
            "Ralan",
            Kdpnj.getText(),
            umur,
            sttsumur,
            "Belum Bayar",
            status
        };
                
        while (! Sequel.menyimpantfSmc("reg_periksa", null, values) && coba < maxCoba) {
            isNumber();
            
            values = new String[] {
                NoReg.getText(),
                TNoRw.getText(),
                Valid.SetTgl(dateTglSEP.getSelectedItem() + ""),
                Sequel.cariIsi("select current_time()"),
                kdDokterBPJS,
                textNoRM.getText(),
                kdPoliBPJS,
                TPngJwb.getText(),
                TAlmt.getText(),
                THbngn.getText(),
                TBiaya.getText(),
                "Belum",
                statusPasien,
                "Ralan",
                Kdpnj.getText(),
                umur,
                sttsumur,
                "Belum Bayar",
                status
            };
            
            coba++;
        }
        
        if (coba == maxCoba && ! Sequel.cariIsi("select reg_periksa.no_rawat from reg_periksa where reg_periksa.no_rawat = ?", TNoRw.getText()).equals(TNoRw.getText())) {
            return false;
        }
        
        UpdateUmur();
        
        return true;
    }
    
    private boolean simpanRujukan() {
        int coba = 0, maxCoba = 5;
        
        String[] values = new String[] {
            TNoRw.getText(),
            textPPKRujukanNama.getText(),
            "-",
            textNoRujukan.getText(),
            "0",
            textPPKRujukanNama.getText(),
            textKodeDiagnosa.getText(),
            "-",
            "-",
            NoRujukMasuk.getText()
        };
        
        while (! Sequel.menyimpantfSmc("rujuk_masuk", null, values) && coba < maxCoba) {
            Valid.autoNomer3("select ifnull(max(convert(right(rujuk_masuk.no_rawat, 4),signed)), 0) from reg_periksa inner join rujuk_masuk on reg_periksa.no_rawat = rujuk_masuk.no_rawat where reg_periksa.tgl_registrasi = '" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString()) + "'",
                "BR/" + dateformat.format(dateTglSEP.getDate()) + "/",
                4,
                NoRujukMasuk
            );
            
            values = new String[] {
                TNoRw.getText(),
                textPPKRujukanNama.getText(),
                "-",
                textNoRujukan.getText(),
                "0",
                textPPKRujukanNama.getText(),
                textKodeDiagnosa.getText(),
                "-",
                "-",
                NoRujukMasuk.getText()
            };
            
            coba++;
        }
        
        if (coba == maxCoba && ! Sequel.cariIsi("select rujuk_masuk.no_rawat from rujuk_masuk where rujuk_masuk.no_rawat = ?", TNoRw.getText()).equals(TNoRw.getText())) {
            return false;
        }
        
        return true;
    }

    private void cekFinger(String noka) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        statusfinger = false;

        if (!textNoPeserta.getText().equals("")) {
            try {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("X-Timestamp", utc);
                headers.add("X-Signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                URL = link + "/SEP/FingerPrint/Peserta/" + noka + "/TglPelayanan/" + Valid.SetTgl(dateTglSEP.getSelectedItem() + "");
                requestEntity = new HttpEntity(headers);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
                nameNode = root.path("metaData");
                System.out.println("kodecekstatus : " + nameNode.path("code").asText());
                //System.out.println("message : "+nameNode.path("message").asText());
                if (nameNode.path("code").asText().equals("200")) {
                    response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                    if (response.path("kode").asText().equals("1")) {
                        if (response.path("status").asText().contains(Sequel.cariIsi("select current_date()"))) {
                            statusfinger = true;
                        } else {
                            statusfinger = false;
                            JOptionPane.showMessageDialog(rootPane, response.path("status").asText());
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(rootPane, response.path("status").asText());
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server BPJS terputus...!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Maaf, silahkan pilih data peserta!");
        }

        this.setCursor(Cursor.getDefaultCursor());
    }

    public void tampil(String nomorrujukan) {
        try {
            URL = link + "/Rujukan/Peserta/" + nomorrujukan;
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
            utc = String.valueOf(api.GetUTCdatetimeAsString());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
            requestEntity = new HttpEntity(headers);
            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
            nameNode = root.path("metaData");
            System.out.println("URL : " + URL);
            peserta = "";
            if (nameNode.path("code").asText().equals("200")) {
                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                textKodeDiagnosa.setText(response.path("diagnosa").path("kode").asText());
                textNamaDiagnosa.setText(response.path("diagnosa").path("nama").asText());
                textNoRujukan.setText(response.path("noKunjungan").asText());
                dropdownKelas.setSelectedItem(response.path("peserta").path("hakKelas").path("kode").asText() + ". " + response.path("peserta").path("hakKelas").path("keterangan").asText().replaceAll("KELAS", "Kelas"));
                prb = response.path("peserta").path("informasi").path("prolanisPRB").asText().replaceAll("null", "");
                peserta = response.path("peserta").path("jenisPeserta").path("keterangan").asText();
                textNoTelp.setText(response.path("peserta").path("mr").path("noTelepon").asText());
                textNamaPasien.setText(response.path("peserta").path("nama").asText());
                textNoPeserta.setText(response.path("peserta").path("noKartu").asText());
                textNoRM.setText(Sequel.cariIsi("select pasien.no_rkm_medis from pasien where pasien.no_peserta='" + textNoPeserta.getText() + "'"));
                textNIK.setText(Sequel.cariIsi("select pasien.no_ktp from pasien where pasien.no_rkm_medis='" + textNoRM.getText() + "'"));
                textJenisKelamin.setText(response.path("peserta").path("sex").asText());
                textStatus.setText(response.path("peserta").path("statusPeserta").path("kode").asText() + " " + response.path("peserta").path("statusPeserta").path("keterangan").asText());
                textTglLahir.setText(response.path("peserta").path("tglLahir").asText());
                textKdPoli.setText(response.path("poliRujukan").path("kode").asText());
                textNamaPoli.setText(response.path("poliRujukan").path("nama").asText());
                textJenisPeserta.setText(response.path("peserta").path("jenisPeserta").path("keterangan").asText());
                kdpoli.setText(Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", response.path("poliRujukan").path("kode").asText()));
                kdPoliBPJS = Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", response.path("poliRujukan").path("kode").asText());
                kdDokterBPJS = Sequel.cariIsi("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs=?", textDPJPRujukanKode.getText());
//                if (!kodepolireg.equals("")) {
//                    isPoli();
//                } else {
//                    isPoli();
//                }
                textPPKRujukanKode.setText(response.path("provPerujuk").path("kode").asText());
                textPPKRujukanNama.setText(response.path("provPerujuk").path("nama").asText());
                Valid.SetTgl(dateTglRujuk, response.path("tglKunjungan").asText());
                isNumber();
                Kdpnj.setText("BPJ");
                nmpnj.setText("BPJS");
                Catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
                ps = koneksi.prepareStatement(
                        "select maping_dokter_dpjpvclaim.kd_dokter,maping_dokter_dpjpvclaim.kd_dokter_bpjs,maping_dokter_dpjpvclaim.nm_dokter_bpjs from maping_dokter_dpjpvclaim inner join jadwal "
                        + "on maping_dokter_dpjpvclaim.kd_dokter=jadwal.kd_dokter where jadwal.kd_poli=? and jadwal.hari_kerja=?");
                try {
                    if (day == 1) {
                        hari = "AKHAD";
                    } else if (day == 2) {
                        hari = "SENIN";
                    } else if (day == 3) {
                        hari = "SELASA";
                    } else if (day == 4) {
                        hari = "RABU";
                    } else if (day == 5) {
                        hari = "KAMIS";
                    } else if (day == 6) {
                        hari = "JUMAT";
                    } else if (day == 7) {
                        hari = "SABTU";
                    }

                    ps.setString(1, kdpoli.getText());
                    ps.setString(2, hari);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        textDPJPRujukanKode.setText(rs.getString("kd_dokter_bpjs"));
                        textDPJPRujukanNama.setText(rs.getString("nm_dokter_bpjs"));
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                }
            } else {
                emptTeks();
//                dispose();
                JOptionPane.showMessageDialog(rootPane, nameNode.path("message").asText());
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi Peserta : " + ex);
            if (ex.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server BPJS terputus...!");
            }
        }
    }

    public void tampilPecahSEP(String nomorrujukan) {
        try {
            URL = link + "/Rujukan/Peserta/" + nomorrujukan;
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
            utc = String.valueOf(api.GetUTCdatetimeAsString());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
            requestEntity = new HttpEntity(headers);
            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
            nameNode = root.path("metaData");
            System.out.println("URL : " + URL);
            peserta = "";
            if (nameNode.path("code").asText().equals("200")) {
                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                textKodeDiagnosa.setText(response.path("diagnosa").path("kode").asText());
                textNamaDiagnosa.setText(response.path("diagnosa").path("nama").asText());
                textNoRujukan.setText(response.path("noKunjungan").asText());
                dropdownKelas.setSelectedItem(response.path("peserta").path("hakKelas").path("kode").asText() + ". " + response.path("peserta").path("hakKelas").path("keterangan").asText().replaceAll("KELAS", "Kelas"));
                prb = response.path("peserta").path("informasi").path("prolanisPRB").asText().replaceAll("null", "");
                peserta = response.path("peserta").path("jenisPeserta").path("keterangan").asText();
                textNoTelp.setText(response.path("peserta").path("mr").path("noTelepon").asText());
                textNamaPasien.setText(response.path("peserta").path("nama").asText());
                textNoPeserta.setText(response.path("peserta").path("noKartu").asText());
                textNoRM.setText(Sequel.cariIsi("select pasien.no_rkm_medis from pasien where pasien.no_peserta='" + textNoPeserta.getText() + "'"));
                textNIK.setText(Sequel.cariIsi("select pasien.no_ktp from pasien where pasien.no_rkm_medis='" + textNoRM.getText() + "'"));
                textJenisKelamin.setText(response.path("peserta").path("sex").asText());
                textStatus.setText(response.path("peserta").path("statusPeserta").path("kode").asText() + " " + response.path("peserta").path("statusPeserta").path("keterangan").asText());
                textTglLahir.setText(response.path("peserta").path("tglLahir").asText());
                textKdPoli.setText(response.path("poliRujukan").path("kode").asText());
                textNamaPoli.setText(response.path("poliRujukan").path("nama").asText());
                dropdownAsesmenPelayanan.setSelectedIndex(1);
                textJenisPeserta.setText(response.path("peserta").path("jenisPeserta").path("keterangan").asText());
                kdpoli.setText(Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", response.path("poliRujukan").path("kode").asText()));
                kdPoliBPJS = Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", response.path("poliRujukan").path("kode").asText());
                kdDokterBPJS = Sequel.cariIsi("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs=?", textDPJPRujukanKode.getText());
//                if (!kodepolireg.equals("")) {
//                    isPoli();
//                } else {
//                    isPoli();
//                }
                textPPKRujukanKode.setText(response.path("provPerujuk").path("kode").asText());
                textPPKRujukanNama.setText(response.path("provPerujuk").path("nama").asText());
                Valid.SetTgl(dateTglRujuk, response.path("tglKunjungan").asText());
                isNumber();
                Kdpnj.setText("BPJ");
                nmpnj.setText("BPJS");
                Catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");

            } else {
                emptTeks();
//                dispose();
                JOptionPane.showMessageDialog(rootPane, nameNode.path("message").asText());
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi Peserta : " + ex);
            if (ex.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server BPJS terputus...!");
            }
        }
    }

    public void tampilKontrol(String noSKDP) {

        String noSEP = Sequel.cariIsi("select bridging_surat_kontrol_bpjs.no_sep from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'");
        String nokapesertakontrol = Sequel.cariIsi("SELECT\n"
                + "	bridging_sep.no_kartu\n"
                + "FROM\n"
                + "	bridging_sep where bridging_sep.no_sep='" + noSEP + "' ");
        String tglkontrol = Sequel.cariIsi("select bridging_surat_kontrol_bpjs.tgl_rencana from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'");

        if (!tglkontrol.equals(Sequel.cariIsi("select current_date()"))) {
            String kdDokterKontrol = Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'");
            String KdPoliKontrol = Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_poli_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'");
            UpdateSuratKontrol(noSKDP, noSEP, kdDokterKontrol, KdPoliKontrol, Sequel.cariIsi("select current_date()"), "anjungan");
        }

        if (Sequel.cariIsi("SELECT\n"
                + "	bridging_sep.jnspelayanan\n"
                + "FROM\n"
                + "	bridging_sep where bridging_sep.no_sep='" + noSEP + "' ").equals("1")) {
//            kondisi post ranap
            try {
                URL = link + "/Peserta/nokartu/" + nokapesertakontrol + "/tglSEP/" + Valid.SetTgl(dateTglSEP.getSelectedItem().toString());
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("X-Timestamp", utc);
                headers.add("X-Signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                requestEntity = new HttpEntity(headers);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
                nameNode = root.path("metaData");
                System.out.println("URL : " + URL);
                peserta = "";
                if (nameNode.path("code").asText().equals("200")) {
                    response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("peserta");
                    textKodeDiagnosa.setText("Z09.8");
                    textNamaDiagnosa.setText("Z09.8 - Follow-up examination after other treatment for other conditions");

                    if (Sequel.cariIsi("SELECT\n"
                            + "	bridging_sep.jnspelayanan\n"
                            + "FROM\n"
                            + "	bridging_sep where bridging_sep.no_sep='" + noSEP + "' ").equals("1")) {
                        textNoRujukan.setText(noSEP);
                        dropdownTujuanKunjungan.setSelectedIndex(0);
                        dropdownFlagProsedur.setSelectedIndex(0);
                        dropdownPenunjang.setSelectedIndex(0);
                        dropdownAsesmenPelayanan.setSelectedIndex(0);
                        dropdownAsalRujukan.setSelectedIndex(0);
                        textKdPoli.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_poli_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textNamaPoli.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.nm_poli_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPRujukanKode.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPRujukanNama.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.nm_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPLayananKode.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPLayananNama.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.nm_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        kdpoli.setText(Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", textKdPoli.getText()));
                        kdPoliBPJS = Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", textKdPoli.getText());
                        kdDokterBPJS = Sequel.cariIsi("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs=?", textDPJPRujukanKode.getText());
                    }
                    textNoSKDP.setText(noSKDP);
                    dropdownKelas.setSelectedItem(response.path("hakKelas").path("kode").asText() + ". " + response.path("hakKelas").path("keterangan").asText().replaceAll("KELAS", "Kelas"));
                    prb = "";
                    peserta = response.path("jenisPeserta").path("keterangan").asText();
                    textNoTelp.setText(response.path("mr").path("noTelepon").asText());
                    textNamaPasien.setText(response.path("nama").asText());
                    textNoPeserta.setText(response.path("noKartu").asText());
                    textNoRM.setText(Sequel.cariIsi("select pasien.no_rkm_medis from pasien where pasien.no_peserta='" + textNoPeserta.getText() + "'"));
                    textNIK.setText(Sequel.cariIsi("select pasien.no_ktp from pasien where pasien.no_rkm_medis='" + textNoRM.getText() + "'"));
                    textJenisKelamin.setText(response.path("sex").asText());
                    textStatus.setText(response.path("statusPeserta").path("kode").asText() + " " + response.path("statusPeserta").path("keterangan").asText());
                    textTglLahir.setText(response.path("tglLahir").asText());
                    textJenisPeserta.setText(response.path("jenisPeserta").path("keterangan").asText());
                    textPPKRujukanKode.setText(response.path("provUmum").path("kdProvider").asText());
                    textPPKRujukanNama.setText(response.path("provUmum").path("nmProvider").asText());
                    isNumber();
                    Kdpnj.setText("BPJ");
                    nmpnj.setText("BPJS");
                    Catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
                } else {
                    emptTeks();
                    JOptionPane.showMessageDialog(rootPane, nameNode.path("message").asText());
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Peserta : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server BPJS terputus...!");
                }
            }

        } else {

            try {
                URL = link + "/Rujukan/Peserta/" + nokapesertakontrol;
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("X-Timestamp", utc);
                headers.add("X-Signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                requestEntity = new HttpEntity(headers);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
                nameNode = root.path("metaData");
                System.out.println("URL : " + URL);
                peserta = "";
                if (nameNode.path("code").asText().equals("200")) {
                    response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                    textKodeDiagnosa.setText(response.path("diagnosa").path("kode").asText());
                    textNamaDiagnosa.setText(response.path("diagnosa").path("nama").asText());

                    if (Sequel.cariIsi("SELECT\n"
                            + "	bridging_sep.jnspelayanan\n"
                            + "FROM\n"
                            + "	bridging_sep where bridging_sep.no_sep='" + noSEP + "' ").equals("1")) {
                        textNoRujukan.setText(noSEP);
                        dropdownTujuanKunjungan.setSelectedIndex(0);
                        dropdownFlagProsedur.setSelectedIndex(0);
                        dropdownPenunjang.setSelectedIndex(0);
                        dropdownAsesmenPelayanan.setSelectedIndex(0);
                        dropdownAsalRujukan.setSelectedIndex(1);
                        textKdPoli.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_poli_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textNamaPoli.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.nm_poli_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPRujukanKode.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPRujukanNama.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.nm_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPLayananKode.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPLayananNama.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.nm_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        kdpoli.setText(Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", textKdPoli.getText()));
                        kdPoliBPJS = Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", textKdPoli.getText());
                        kdDokterBPJS = Sequel.cariIsi("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs=?", textDPJPRujukanKode.getText());
                    } else {
                        textNoRujukan.setText(response.path("noKunjungan").asText());
                        dropdownTujuanKunjungan.setSelectedIndex(2);
                        textKdPoli.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_poli_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textNamaPoli.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.nm_poli_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPRujukanKode.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPRujukanNama.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.nm_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        kdpoli.setText(Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", textKdPoli.getText()));
                        textDPJPLayananKode.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.kd_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        textDPJPLayananNama.setText(Sequel.cariIsi("select bridging_surat_kontrol_bpjs.nm_dokter_bpjs from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + noSKDP + "'"));
                        kdPoliBPJS = Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", textKdPoli.getText());
                        kdDokterBPJS = Sequel.cariIsi("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs=?", textDPJPRujukanKode.getText());
                        dropdownFlagProsedur.setSelectedIndex(0);
                        dropdownPenunjang.setSelectedIndex(0);
                        dropdownAsesmenPelayanan.setSelectedIndex(5);
                        Valid.SetTgl(dateTglRujuk, response.path("tglKunjungan").asText());
                    }
                    textNoSKDP.setText(noSKDP);
                    dropdownKelas.setSelectedItem(response.path("peserta").path("hakKelas").path("kode").asText() + ". " + response.path("peserta").path("hakKelas").path("keterangan").asText().replaceAll("KELAS", "Kelas"));
                    prb = response.path("peserta").path("informasi").path("prolanisPRB").asText().replaceAll("null", "");
                    peserta = response.path("peserta").path("jenisPeserta").path("keterangan").asText();
                    textNoTelp.setText(response.path("peserta").path("mr").path("noTelepon").asText());
                    textNamaPasien.setText(response.path("peserta").path("nama").asText());
                    textNoPeserta.setText(response.path("peserta").path("noKartu").asText());
                    textNoRM.setText(Sequel.cariIsi("select pasien.no_rkm_medis from pasien where pasien.no_peserta='" + textNoPeserta.getText() + "'"));
                    textNIK.setText(Sequel.cariIsi("select pasien.no_ktp from pasien where pasien.no_rkm_medis='" + textNoRM.getText() + "'"));
                    textJenisKelamin.setText(response.path("peserta").path("sex").asText());
                    textStatus.setText(response.path("peserta").path("statusPeserta").path("kode").asText() + " " + response.path("peserta").path("statusPeserta").path("keterangan").asText());
                    textTglLahir.setText(response.path("peserta").path("tglLahir").asText());
                    textJenisPeserta.setText(response.path("peserta").path("jenisPeserta").path("keterangan").asText());
                    textPPKRujukanKode.setText(response.path("provPerujuk").path("kode").asText());
                    textPPKRujukanNama.setText(response.path("provPerujuk").path("nama").asText());
                    isNumber();
                    Kdpnj.setText("BPJ");
                    nmpnj.setText("BPJS");
                    Catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
                } else {
                    emptTeks();
//                dispose();
                    JOptionPane.showMessageDialog(rootPane, nameNode.path("message").asText());
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Peserta : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server BPJS terputus...!");
                }
            }

        }

    }

    public void SimpanAntrianOnSite() {
        if ((!textNoRujukan.getText().equals("")) || (!textNoSKDP.getText().equals(""))) {
            if (dropdownTujuanKunjungan.getSelectedItem().toString().equals("0. Normal") && dropdownFlagProsedur.getSelectedItem().toString().equals("") && dropdownPenunjang.getSelectedItem().toString().equals("") && dropdownAsesmenPelayanan.getSelectedItem().toString().equals("")) {
                if (dropdownAsalRujukan.getSelectedIndex() == 0) {
                    jenisKunjungan = "1";
                } else {
                    jenisKunjungan = "4";
                }
            } else if (dropdownTujuanKunjungan.getSelectedItem().toString().equals("2. Konsul Dokter") && dropdownFlagProsedur.getSelectedItem().toString().equals("") && dropdownPenunjang.getSelectedItem().toString().equals("") && dropdownAsesmenPelayanan.getSelectedItem().toString().equals("5. Tujuan Kontrol")) {
                jenisKunjungan = "3";
            } else if (dropdownTujuanKunjungan.getSelectedItem().toString().equals("0. Normal") && dropdownFlagProsedur.getSelectedItem().toString().equals("") && dropdownPenunjang.getSelectedItem().toString().equals("") && dropdownAsesmenPelayanan.getSelectedItem().toString().equals("4. Atas Instruksi RS")) {
                jenisKunjungan = "2";
            } else if (dropdownTujuanKunjungan.getSelectedItem().toString().equals("0. Normal") && dropdownFlagProsedur.getSelectedItem().toString().equals("") && dropdownPenunjang.getSelectedItem().toString().equals("") && dropdownAsesmenPelayanan.getSelectedItem().toString().equals("1. Poli spesialis tidak tersedia pada hari sebelumnya")) {
                jenisKunjungan = "2";
            } else {
                if (dropdownTujuanKunjungan.getSelectedItem().toString().equals("2. Konsul Dokter") && dropdownAsesmenPelayanan.getSelectedItem().toString().equals("5. Tujuan Kontrol")) {
                    jenisKunjungan = "3";
                } else {
                    jenisKunjungan = "2";
                }
            }

            try {
                day = cal.get(Calendar.DAY_OF_WEEK);
                switch (day) {
                    case 1:
                        hari = "AKHAD";
                        break;
                    case 2:
                        hari = "SENIN";
                        break;
                    case 3:
                        hari = "SELASA";
                        break;
                    case 4:
                        hari = "RABU";
                        break;
                    case 5:
                        hari = "KAMIS";
                        break;
                    case 6:
                        hari = "JUMAT";
                        break;
                    case 7:
                        hari = "SABTU";
                        break;
                    default:
                        break;
                }

                ps = koneksi.prepareStatement("select jadwal.jam_mulai,jadwal.jam_selesai,jadwal.kuota from jadwal where jadwal.hari_kerja=? and jadwal.kd_poli=? and jadwal.kd_dokter=?");
                try {
                    ps.setString(1, hari);
                    ps.setString(2, kdPoliBPJS);
                    ps.setString(3, kdDokterBPJS);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        jamSelesai = rs.getString("jam_mulai");
                        jamMulai = rs.getString("jam_selesai");
                        kuota = rs.getInt("kuota");
                        datajam = Sequel.cariIsi("select DATE_ADD(concat('" + Valid.SetTgl(dateTglSEP.getSelectedItem() + "") + "',' ','" + jamSelesai + "'),INTERVAL " + (Integer.parseInt(NoReg.getText()) * 10) + " MINUTE) ");
                        parsedDate = dateFormat.parse(datajam);
                    } else {
                        System.out.println("Jadwal tidak ditemukan...!");
                    }
                } catch (Exception e) {
                    System.out.println("Notif jadwal: " + e);
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                }

                if (!textNoSKDP.getText().equals("")) {
                    try {
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                        utc = String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp", utc);
                        headers.add("x-signature", api.getHmac(utc));
                        headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                        requestJson = "{"
                                + "\"kodebooking\": \"" + TNoRw.getText() + "\","
                                + "\"jenispasien\": \"JKN\","
                                + "\"nomorkartu\": \"" + textNoPeserta.getText() + "\","
                                + "\"nik\": \"" + textNIK.getText() + "\","
                                + "\"nohp\": \"" + textNoTelp.getText() + "\","
                                + "\"kodepoli\": \"" + textKdPoli.getText() + "\","
                                + "\"namapoli\": \"" + textNamaPoli.getText() + "\","
                                + "\"pasienbaru\": 0,"
                                + "\"norm\": \"" + textNoRM.getText() + "\","
                                + "\"tanggalperiksa\": \"" + Valid.SetTgl(dateTglSEP.getSelectedItem() + "") + "\","
                                + "\"kodedokter\": " + textDPJPRujukanKode.getText() + ","
                                + "\"namadokter\": \"" + textDPJPRujukanNama.getText() + "\","
                                + "\"jampraktek\": \"" + jamSelesai.substring(0, 5) + "-" + jamMulai.substring(0, 5) + "\","
                                + "\"jeniskunjungan\": " + jenisKunjungan + ","
                                + "\"nomorreferensi\": \"" + textNoSKDP.getText().toString() + "\","
                                + "\"nomorantrean\": \"" + NoReg.getText() + "\","
                                + "\"angkaantrean\": " + Integer.parseInt(NoReg.getText()) + ","
                                + "\"estimasidilayani\": " + parsedDate.getTime() + ","
                                + "\"sisakuotajkn\": " + (kuota - Integer.parseInt(NoReg.getText())) + ","
                                + "\"kuotajkn\": " + kuota + ","
                                + "\"sisakuotanonjkn\": " + (kuota - Integer.parseInt(NoReg.getText())) + ","
                                + "\"kuotanonjkn\": " + kuota + ","
                                + "\"keterangan\": \"Peserta harap 30 menit lebih awal guna pencatatan administrasi. Estimasi pelayanan 10 menit per pasien\""
                                + "}";
                        requestEntity = new HttpEntity(requestJson, headers);
                        URL = koneksiDB.URLAPIMOBILEJKN() + "/antrean/add";
                        System.out.println("URL : " + URL);
                        System.out.println(requestEntity);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class
                        ).getBody());
                        nameNode = root.path("metadata");
                        if (nameNode.path("code").asText().equals("200")) {
                            Sequel.menyimpan2("referensi_mobilejkn_bpjs_taskid_status200", "?,?,?,?,?,?,?,?", 8, new String[]{
                                TNoRw.getText(), "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00"
                            });
                        } else {
                            Sequel.menyimpan2("referensi_mobilejkn_bpjs_taskid_status201", "?,?,?", 3, new String[]{
                                TNoRw.getText(), nameNode.path("message").asText(), nameNode.path("code").asText()
                            });
                        }
                        System.out.println("respon WS BPJS Kirim Pakai SKDP : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                    } catch (Exception e) {
                        System.out.println("Notif SKDP : " + e);
                    }
                }

                if (!textNoRujukan.getText().equals("")) {
                    try {
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                        utc = String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp", utc);
                        headers.add("x-signature", api.getHmac(utc));
                        headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson = "{"
                                + "\"kodebooking\": \"" + TNoRw.getText() + "\","
                                + "\"jenispasien\": \"JKN\","
                                + "\"nomorkartu\": \"" + textNoPeserta.getText() + "\","
                                + "\"nik\": \"" + textNIK.getText() + "\","
                                + "\"nohp\": \"" + textNoTelp.getText() + "\","
                                + "\"kodepoli\": \"" + textKdPoli.getText() + "\","
                                + "\"namapoli\": \"" + textNamaPoli.getText() + "\","
                                + "\"pasienbaru\": 0,"
                                + "\"norm\": \"" + textNoRM.getText() + "\","
                                + "\"tanggalperiksa\": \"" + Valid.SetTgl(dateTglSEP.getSelectedItem() + "") + "\","
                                + "\"kodedokter\": " + textDPJPRujukanKode.getText() + ","
                                + "\"namadokter\": \"" + textDPJPRujukanNama.getText() + "\","
                                + "\"jampraktek\": \"" + jamSelesai.substring(0, 5) + "-" + jamMulai.substring(0, 5) + "\","
                                + "\"jeniskunjungan\": " + jenisKunjungan + ","
                                + "\"nomorreferensi\": \"" + textNoRujukan.getText() + "\","
                                + "\"nomorantrean\": \"" + NoReg.getText() + "\","
                                + "\"angkaantrean\": " + Integer.parseInt(NoReg.getText()) + ","
                                + "\"estimasidilayani\": " + parsedDate.getTime() + ","
                                + "\"sisakuotajkn\": " + (kuota - Integer.parseInt(NoReg.getText())) + ","
                                + "\"kuotajkn\": " + kuota + ","
                                + "\"sisakuotanonjkn\": " + (kuota - Integer.parseInt(NoReg.getText())) + ","
                                + "\"kuotanonjkn\": " + kuota + ","
                                + "\"keterangan\": \"Peserta harap 30 menit lebih awal guna pencatatan administrasi.\""
                                + "}";
                        System.out.println("JSON : " + requestJson + "\n");
                        requestEntity = new HttpEntity(requestJson, headers);
                        URL = koneksiDB.URLAPIMOBILEJKN() + "/antrean/add";
                        System.out.println("URL Kirim Pakai No.Rujuk : " + URL);
                        root
                                = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class
                                ).getBody());
                        nameNode = root.path("metadata");
                        if (nameNode.path("code").asText().equals("200")) {
                            Sequel.menyimpan2("referensi_mobilejkn_bpjs_taskid_status200", "?,?,?,?,?,?,?,?", 8, new String[]{
                                TNoRw.getText(), "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00", "0000-00-00 00:00:00"
                            });
                        } else {
                            Sequel.menyimpan2("referensi_mobilejkn_bpjs_taskid_status201", "?,?,?", 3, new String[]{
                                TNoRw.getText(), nameNode.path("message").asText(), nameNode.path("code").asText()
                            });
                        }
                        System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                    } catch (Exception e) {
                        System.out.println("Notif No.Rujuk : " + e);
                    }
                }

            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
        }
    }

    private void emptTeks() {
        textNamaPasien.setText("");
        dateTglSEP.setDate(new Date());
        dateTglRujuk.setDate(new Date());
        textTglLahir.setText("");
        textNoPeserta.setText("");
        textJenisPeserta.setText("");
        textStatus.setText("");
        textJenisKelamin.setText("");
        textNoRujukan.setText("");
        textPPKRujukanKode.setText("");
        textPPKRujukanNama.setText("");
        dropdownJenisPelayanan.setSelectedIndex(1);
        Catatan.setText("");
        textKodeDiagnosa.setText("");
        textNamaDiagnosa.setText("");
        textKdPoli.setText("");
        textNamaPoli.setText("");
        dropdownKelas.setSelectedIndex(2);
        dropdownLakaLantas.setSelectedIndex(0);
        textNoRM.setText("");
        textDPJPRujukanKode.setText("");
        textDPJPRujukanNama.setText("");
        textKeterangan.setText("");
        NoSEPSuplesi.setText("");
        KdPropinsi.setText("");
        NmPropinsi.setText("");
        KdKabupaten.setText("");
        NmKabupaten.setText("");
        KdKecamatan.setText("");
        NmKecamatan.setText("");
        dropdownKatarak.setSelectedIndex(0);
        Suplesi.setSelectedIndex(0);
        dateTglKLL.setDate(new Date());
        dateTglKLL.setEnabled(false);
        textKeterangan.setEditable(false);
        dropdownTujuanKunjungan.setSelectedIndex(0);
        dropdownFlagProsedur.setSelectedIndex(0);
        dropdownFlagProsedur.setEnabled(false);
        dropdownPenunjang.setSelectedIndex(0);
        dropdownPenunjang.setEnabled(false);
        dropdownAsesmenPelayanan.setSelectedIndex(0);
        dropdownAsesmenPelayanan.setEnabled(true);
        textDPJPLayananKode.setText("");
        textDPJPLayananNama.setText("");
        btnDPJPLayanan.setEnabled(true);
        textNoRujukan.requestFocus();
        kdPoliBPJS = "";
        kdDokterBPJS = "";
    }

    private void isPoli() {
        try {
            ps = koneksi.prepareStatement("select registrasi, registrasilama "
                    + " from poliklinik where kd_poli=? order by nm_poli");
            try {
                ps.setString(1, kdPoliBPJS);
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (statusPasien.equals("Lama")) {
                        TBiaya.setText(rs.getString("registrasilama"));
                    } else if (statusPasien.equals("Baru")) {
                        TBiaya.setText(rs.getString("registrasi"));
                    } else {
                        TBiaya.setText(rs.getString("registrasi"));
                    }
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Cari Poli : " + e);
        }
    }

    private void CetakSEPOtomatis(String nomorsep) {

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Map<String, Object> param = new HashMap<>();
        param.put("namars", akses.getnamars());
        param.put("alamatrs", akses.getalamatrs());
        param.put("kotars", akses.getkabupatenrs());
        param.put("propinsirs", akses.getpropinsirs());
        param.put("kontakrs", akses.getkontakrs());
        param.put("prb", Sequel.cariIsi("select bpjs_prb.prb from bpjs_prb where bpjs_prb.no_sep=?", nomorsep));
        param.put("logo", Sequel.cariGambar("select gambar.bpjs from gambar"));
        param.put("parameter", nomorsep);
        if (dropdownJenisPelayanan.getSelectedIndex() == 0) {
            Valid.MyReport("rptBridgingSEP.jasper", "report", "::[ Cetak SEP ]::", param);
        } else {
            Valid.MyReport("rptBridgingSEP2.jasper", "report", "::[ Cetak SEP ]::", param);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void BukaFingerPrint(String NomorKartu) {
        if (!textNoPeserta.getText().equals("")) {
//            toFront();
            try {
                Runtime.getRuntime().exec(urlaplikasi);
                Robot robot = new Robot();
//                StringSelection stringSelection = new StringSelection(urlfinger);
//                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//                clipboard.setContents(stringSelection, stringSelection);
//                Thread.sleep(1000);
//                robot.keyPress(KeyEvent.VK_CONTROL);
//                robot.keyPress(KeyEvent.VK_V);
//                robot.keyRelease(KeyEvent.VK_V);
//                robot.keyRelease(KeyEvent.VK_CONTROL);
//                robot.keyPress(KeyEvent.VK_TAB);
//                robot.keyRelease(KeyEvent.VK_TAB);
//                robot.keyPress(KeyEvent.VK_ENTER);
//                robot.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(1500);
                StringSelection stringSelectionuser = new StringSelection(userfinger);
                Clipboard clipboarduser = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboarduser.setContents(stringSelectionuser, stringSelectionuser);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                Thread.sleep(1500);
                StringSelection stringSelectionpass = new StringSelection(passfinger);
                Clipboard clipboardpass = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboardpass.setContents(stringSelectionpass, stringSelectionpass);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(1500);
                StringSelection stringSelectionnokartu = new StringSelection(textNoPeserta.getText());
                Clipboard clipboardnokartu = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboardnokartu.setContents(stringSelectionnokartu, stringSelectionnokartu);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
            } catch (IOException ex) {
                System.out.println("Notif Finger : " + ex);
            } catch (AWTException ex) {
                System.out.println("Notif Finger : " + ex);
            } catch (InterruptedException ex) {
                System.out.println("Notif Finger : " + ex);
            }
        }
    }

    private void UpdateSuratKontrol(String NoSurat, String NoSEPKontrol, String KdDokterKontrol, String KdPoliKontrol, String Tanggalkontrol, String userKontrol) {
        if (!NoSurat.equals("")) {
            String namapoliKontrol = Sequel.cariIsi("select maping_poli_bpjs.nm_poli_bpjs from maping_poli_bpjs where maping_poli_bpjs.kd_poli_bpjs='" + KdPoliKontrol + "'");
            String namadokterkontrol = Sequel.cariIsi("select maping_dokter_dpjpvclaim.nm_dokter_bpjs from maping_dokter_dpjpvclaim where maping_dokter_dpjpvclaim.kd_dokter_bpjs='" + KdDokterKontrol + "'");
            String tanggalsuratkontrol = Sequel.cariIsi("select bridging_surat_kontrol_bpjs.tgl_surat from bridging_surat_kontrol_bpjs where bridging_surat_kontrol_bpjs.no_surat='" + NoSurat + "'");
            try {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("X-Timestamp", utc);
                headers.add("X-Signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                URL = link + "/RencanaKontrol/Update";
                requestJson = "{"
                        + "\"request\": {"
                        + "\"noSuratKontrol\":\"" + NoSurat + "\","
                        + "\"noSEP\":\"" + NoSEPKontrol + "\","
                        + "\"kodeDokter\":\"" + KdDokterKontrol + "\","
                        + "\"poliKontrol\":\"" + KdPoliKontrol + "\","
                        + "\"tglRencanaKontrol\":\"" + Tanggalkontrol + "\","
                        + "\"user\":\"" + userKontrol + "\""
                        + "}"
                        + "}";
                System.out.println("JSON : " + requestJson);
                requestEntity = new HttpEntity(requestJson, headers);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.PUT, requestEntity, String.class).getBody());
                nameNode = root.path("metaData");
                System.out.println("code : " + nameNode.path("code").asText());
                System.out.println("message : " + nameNode.path("message").asText());
                if (nameNode.path("code").asText().equals("200")) {
                    if (Sequel.mengedittf("bridging_surat_kontrol_bpjs", "no_surat=?", "tgl_surat=?,tgl_rencana=?,kd_dokter_bpjs=?,nm_dokter_bpjs=?,kd_poli_bpjs=?,nm_poli_bpjs=?", 7, new String[]{
                        tanggalsuratkontrol, Tanggalkontrol, KdDokterKontrol, namadokterkontrol, KdPoliKontrol, namapoliKontrol, NoSurat
                    }) == true) {
                        System.out.println("Respon BPJS : " + nameNode.path("message").asText());
//                        JOptionPane.showMessageDialog(rootPane, "Respon BPJS : "+nameNode.path("message").asText());
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, nameNode.path("message").asText());
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server BPJS terputus...!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Maaf, Silahkan anda pilih terlebih dulu data yang mau anda ganti...\n Klik data pada table untuk memilih data...!!!!");
        }

    }

}

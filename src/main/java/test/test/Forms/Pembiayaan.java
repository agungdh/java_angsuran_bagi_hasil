/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Forms;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DBException;
import org.javalite.activejdbc.LazyList;
import test.test.Helpers.ADHhelper;
import test.test.Models.AnggotaModel;
import test.test.Models.PembiayaanModel;
import test.test.Reports.Config;

/**
 *
 * @author user
 */
public class Pembiayaan extends javax.swing.JFrame {
    private List<Integer> comboAnggotaID = new ArrayList<Integer>();
    private int comboAnggotaIndex;
    private int selectedComboAnggotaIndex;
    
    private DefaultTableModel model = new DefaultTableModel();
    private String ID;
    private String state;
    
    private double valPlafon;
    private double valBasil;
    private double valPokok;
    private int valWaktu;
    private Date valTanggal;
    private Date valJatuh;
    /**
     * Creates new form PangkatGol
     */
    public Pembiayaan() {
        initComponents();
                
        loadTable();
                
        TextCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                cari();
            }

            public void removeUpdate(DocumentEvent e) {
                cari();
            }

            public void changedUpdate(DocumentEvent e) {
                cari();
            }
        });
        
        Plafon.addChangeListener(new PlafonListener());
        Waktu.addChangeListener(new WaktuListener());
        Tanggal.getDateEditor().addPropertyChangeListener( new PropertyChangeListener() 
        {
            @Override
            public void propertyChange(PropertyChangeEvent e) 
            {
                if ("date".equals(e.getPropertyName())) 
                {
                    valTanggal = (Date) e.getNewValue();
                    
                    
                    if (valTanggal != null) {
                        Waktu.setValue(0);
                        Jatuh.setDate(ADHhelper.dateTambahBulan(valTanggal, valWaktu));
                    }
                    
                    hitungHitungan();
                }
            }
        });
        Jatuh.getDateEditor().addPropertyChangeListener( new PropertyChangeListener() 
        {
            @Override
            public void propertyChange(PropertyChangeEvent e) 
            {
                if ("date".equals(e.getPropertyName())) 
                {
                    valJatuh = (Date) e.getNewValue();
                    
                    hitungHitungan();
                }
            }
        });
        
        loadComboBox();
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void loadComboBox() {
        Anggota.removeAllItems();
        
        Base.open();
        LazyList<AnggotaModel> anggotas = AnggotaModel.findAll();
        Base.close();
        
        Base.open();
        for(AnggotaModel anggota : anggotas) {
            Base.close();
            comboAnggotaID.add(Integer.parseInt(anggota.getString("id")));
            Anggota.addItem(anggota.getString("nama"));
            Base.open();
        }
        Base.close();
    }

    private void hitungHitungan() {
        valBasil = (valPlafon * 2.5 / 100) * valWaktu;
        Bagi.setValue((int) valBasil);
        
        valPokok = valPlafon / valWaktu;
        Pokok.setValue((int) valPokok);
    }
    
    class PlafonListener implements ChangeListener {
        public void stateChanged(ChangeEvent evt) {
          JSpinner spinner = (JSpinner) evt.getSource();
          
          valPlafon = (int) spinner.getValue();
          
          hitungHitungan();
        }
    }

    class WaktuListener implements ChangeListener {
        public void stateChanged(ChangeEvent evt) {
            if (Tanggal.getDate() != null) {
                JSpinner spinner = (JSpinner) evt.getSource();

                valWaktu = (int) spinner.getValue();

                valJatuh = ADHhelper.dateTambahBulan(valTanggal, valWaktu);
                Jatuh.setDate(valJatuh);

                hitungHitungan();
            } 
        }
    }
   
    public void cari() {
        if (TextCari.getText().equals("")) {
            loadTable();
        } else {
            loadTable(TextCari.getText());
        }
    }
    
    private void loadTableHelper(LazyList<PembiayaanModel> pembiayaans) {
        model = new DefaultTableModel();
                
        model.addColumn("#ID");
        model.addColumn("No Pembiayaan");
        model.addColumn("Anggota");
        model.addColumn("Tanggal");
        model.addColumn("Plafon");
        model.addColumn("Jatuh Tempo");
        model.addColumn("Bagi Hasil");
        model.addColumn("Pokok");
        model.addColumn("Administrasi");
        model.addColumn("Jaminan");
        
        Base.open();
        
        try {
            for(PembiayaanModel pembiayaan : pembiayaans) {     
                AnggotaModel anggota = pembiayaan.parent(AnggotaModel.class);
                model.addRow(new Object[]{
                    pembiayaan.getId(),
                    pembiayaan.getString("no_pembiayaan"),
                    anggota.getString("nama"),
                    ADHhelper.tanggalIndo(pembiayaan.getString("tanggal")),
                    ADHhelper.rupiah(pembiayaan.getInteger("plafon")),
                    ADHhelper.tanggalIndo(pembiayaan.getString("jatuh_tempo")),
                    ADHhelper.rupiah(pembiayaan.getInteger("basil")),
                    ADHhelper.rupiah(pembiayaan.getInteger("pokok")),
                    ADHhelper.rupiah(pembiayaan.getInteger("administrasi")),
                    pembiayaan.getString("jaminan")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
        
        TablePegawai.setModel(model);
        
        setState("index");
    }
    
    private void formatTextInteger() {
       
    }
    
    private void loadTable() {
        Base.open();
        LazyList<PembiayaanModel> pembiayaans = PembiayaanModel.findAll();
        Base.close();
        
        loadTableHelper(pembiayaans);
    }

    private void loadTable(String cari) {
        Base.open();
        LazyList<PembiayaanModel> pembiayaans = PembiayaanModel.findBySQL("SELECT p.* FROM pembiayaan p, anggota a WHERE p.id_anggota = a.id AND (p.no_pembiayaan LIKE ? OR a.nama LIKE ?)", '%' + cari + '%', '%' + cari + '%');
        Base.close();
        
        loadTableHelper(pembiayaans);
    }

    
    private void hapusData() {
        Base.open();
        PembiayaanModel pembiayaa = PembiayaanModel.findById(ID);
        try {
            pembiayaa.delete();
        } catch (DBException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        Base.close();
    }
    
    private void setState(String IndexOrEdit) {
        if (IndexOrEdit.equals("index")) {
            ButtonTambahUbah.setText("Tambah");
            ButtonResetHapus.setText("Reset");
            
            state = IndexOrEdit;
        } else if (IndexOrEdit.equals("edit")) {
            ButtonTambahUbah.setText("Ubah");
            ButtonResetHapus.setText("Hapus");
            
            state = IndexOrEdit;
        } else {
            JOptionPane.showMessageDialog(null, "Index/Edit ?");
        }
    }
    
    private void tambahData() {
        Base.open();
        try {
            PembiayaanModel pembiayaan = new PembiayaanModel();
            pembiayaan.set("no_pembiayaan", No.getText());
            pembiayaan.set("id_anggota", selectedComboAnggotaIndex);
            pembiayaan.set("tanggal", ADHhelper.parseTanggal(Tanggal.getDate()));
            pembiayaan.set("plafon", Plafon.getValue());
            pembiayaan.set("waktu", Waktu.getValue());
            pembiayaan.set("jatuh_tempo", ADHhelper.parseTanggal(Jatuh.getDate()));
            pembiayaan.set("basil", Bagi.getValue());
            pembiayaan.set("pokok", Pokok.getValue());
            pembiayaan.set("administrasi", Adm.getValue());
            pembiayaan.set("jaminan", Jaminan.getText());
            pembiayaan.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }
    
    private void ubahData() {
        Base.open();
        try {
            PembiayaanModel pembiayaan = PembiayaanModel.findById(ID);
            pembiayaan.set("no_pembiayaan", No.getText());
            pembiayaan.set("id_anggota", selectedComboAnggotaIndex);
            pembiayaan.set("tanggal", ADHhelper.parseTanggal(Tanggal.getDate()));
            pembiayaan.set("plafon", Plafon.getValue());
            pembiayaan.set("waktu", Waktu.getValue());
            pembiayaan.set("jatuh_tempo", ADHhelper.parseTanggal(Jatuh.getDate()));
            pembiayaan.set("basil", Bagi.getValue());
            pembiayaan.set("pokok", Pokok.getValue());
            pembiayaan.set("administrasi", Adm.getValue());
            pembiayaan.set("jaminan", Jaminan.getText());
            pembiayaan.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }

    private void resetForm() {
        No.setText("");
        Jaminan.setText("");
        Anggota.setSelectedIndex(0);
        Plafon.setValue(0);
        Bagi.setValue(0);
        Pokok.setValue(0);
        Adm.setValue(0);
        Tanggal.setDate(null);
        Jatuh.setDate(null);
        Waktu.setValue(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        ScrollPane = new javax.swing.JScrollPane();
        TablePegawai = new javax.swing.JTable();
        TextCari = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        LabelCari = new javax.swing.JLabel();
        LabelCari7 = new javax.swing.JLabel();
        Anggota = new javax.swing.JComboBox<>();
        Tanggal = new com.toedter.calendar.JDateChooser();
        LabelCari3 = new javax.swing.JLabel();
        Plafon = new javax.swing.JSpinner();
        LabelCari9 = new javax.swing.JLabel();
        LabelCari6 = new javax.swing.JLabel();
        LabelCari8 = new javax.swing.JLabel();
        LabelCari4 = new javax.swing.JLabel();
        LabelCari10 = new javax.swing.JLabel();
        No = new javax.swing.JTextField();
        Pokok = new javax.swing.JSpinner();
        LabelCari2 = new javax.swing.JLabel();
        LabelCari5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        ButtonRefresh = new javax.swing.JButton();
        ButtonTambahUbah = new javax.swing.JButton();
        ButtonResetHapus = new javax.swing.JButton();
        Jaminan = new javax.swing.JTextField();
        Waktu = new javax.swing.JSpinner();
        LabelCari1 = new javax.swing.JLabel();
        Jatuh = new com.toedter.calendar.JDateChooser();
        Adm = new javax.swing.JSpinner();
        Bagi = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pembiayaan");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));

        TablePegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TablePegawai.getTableHeader().setReorderingAllowed(false);
        TablePegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablePegawaiMouseClicked(evt);
            }
        });
        ScrollPane.setViewportView(TablePegawai);

        TextCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextCariActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PEMBIAYAAN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(546, 546, 546)
                .addComponent(jLabel1)
                .addContainerGap(587, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        LabelCari.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari.setText("Cari (No/Nama)");

        LabelCari7.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari7.setText("Pokok");

        Anggota.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Anggota.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                AnggotaItemStateChanged(evt);
            }
        });
        Anggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnggotaActionPerformed(evt);
            }
        });

        Tanggal.setDateFormatString("dd-MM-yyyy");

        LabelCari3.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari3.setText("Tanggal");

        LabelCari9.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari9.setText("Waktu (Bulan)");

        LabelCari6.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari6.setText("Bagi Hasil");

        LabelCari8.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari8.setText("Administrasi");

        LabelCari4.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari4.setText("Plafon");

        LabelCari10.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari10.setText("Jaminan");

        No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoActionPerformed(evt);
            }
        });

        Pokok.setEnabled(false);

        LabelCari2.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari2.setText("Anggota");

        LabelCari5.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari5.setText("Jatuh Tempo");

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        ButtonRefresh.setText("Refresh");
        ButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonRefreshActionPerformed(evt);
            }
        });

        ButtonTambahUbah.setText("Tambah");
        ButtonTambahUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonTambahUbahActionPerformed(evt);
            }
        });

        ButtonResetHapus.setText("Reset");
        ButtonResetHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonResetHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ButtonTambahUbah)
                .addGap(44, 44, 44)
                .addComponent(ButtonRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(ButtonResetHapus)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonTambahUbah)
                    .addComponent(ButtonRefresh)
                    .addComponent(ButtonResetHapus))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        LabelCari1.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari1.setText("No Pembiayaan");

        Jatuh.setDateFormatString("dd-MM-yyyy");
        Jatuh.setEnabled(false);

        Bagi.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Pokok, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Bagi, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Jatuh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Plafon))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Anggota, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(No))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari9, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Waktu, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelCari8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelCari10, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(Adm)
                                    .addComponent(Jaminan, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(81, 81, 81)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCari)
                    .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari1)
                            .addComponent(No, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCari2)
                            .addComponent(Anggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCari3)
                            .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari4)
                            .addComponent(Plafon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari9)
                            .addComponent(Waktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCari5)
                            .addComponent(Jatuh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCari6)
                            .addComponent(Bagi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCari7)
                            .addComponent(Pokok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCari8)
                            .addComponent(Adm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari10)
                            .addComponent(Jaminan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonRefreshActionPerformed
        resetForm();
        loadTable();
        TextCari.setText("");
    }//GEN-LAST:event_ButtonRefreshActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        new MenuUtama().setVisible(true);
        
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void TablePegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablePegawaiMouseClicked
        int i =TablePegawai.getSelectedRow();
        if(i>=0){
            ID = model.getValueAt(i, 0).toString();

            Base.open();
            PembiayaanModel pembiayaan = PembiayaanModel.findById(ID);
            Base.close();

            No.setText(pembiayaan.getString("no_pembiayaan"));
            Jaminan.setText(pembiayaan.getString("jaminan"));
            Anggota.setSelectedIndex(comboAnggotaID.indexOf(Integer.parseInt(pembiayaan.getString("id_anggota"))));
            try {
                Tanggal.setDate(ADHhelper.getTanggalFromDB(pembiayaan.getString("tanggal")));
                Jatuh.setDate(ADHhelper.getTanggalFromDB(pembiayaan.getString("jatuh_tempo")));
            } catch (ParseException ex) {
                Logger.getLogger(Pembiayaan.class.getName()).log(Level.SEVERE, null, ex);
            }
            Plafon.setValue(pembiayaan.getInteger("plafon"));
            Bagi.setValue(pembiayaan.getInteger("basil"));
            Pokok.setValue(pembiayaan.getInteger("pokok"));
            Adm.setValue(pembiayaan.getInteger("administrasi"));
            Waktu.setValue(pembiayaan.getInteger("waktu"));
            setState("edit");
        }
    }//GEN-LAST:event_TablePegawaiMouseClicked

    private void ButtonTambahUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonTambahUbahActionPerformed
        if (state.equals("index")) {
            if (No.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form No Pembiayaan Masih Kosong !!!");
            } else if (Jaminan.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Jaminan Masih Kosong !!!");
            } else if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else if (Jatuh.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Jatuh Tempo Masih Kosong !!!");
            } else if ((int)Waktu.getValue() < 3 || (int)Waktu.getValue() > 20) {
                JOptionPane.showMessageDialog(null, "Tempo Waktu Antara 3 Sampai 20 Bulan !!!");
            } else {
                Base.open();
                LazyList<PembiayaanModel> pembiayaans = PembiayaanModel.where("no_pembiayaan = ?", No.getText());
                Base.close();

                Base.open();
                if (pembiayaans.size() > 0) {
                    JOptionPane.showMessageDialog(null, "No Pembiayaan Sudah Ada !!!");
                } else {
                    Base.close();
                    
                    Base.open();
                    LazyList<PembiayaanModel> pembiayaansBelumLunas = PembiayaanModel.findBySQL("SELECT * FROM pembiayaan p, anggota a WHERE p.id_anggota = a.id AND p.tanggal_pelunasan IS NULL AND a.id = ?", selectedComboAnggotaIndex);
                    
                    if (pembiayaansBelumLunas.size() > 0) {
                        JOptionPane.showMessageDialog(null, "Anggota Sedang Mempunyai Pembiayaan Yang Belum Lunas !!!");
                    } else {
                        Base.close();
                    
                        tambahData();
                        resetForm();
                        loadTable();

                        Base.open();
                    }
                }
                Base.close();
            }
        } else {
            if (No.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form No Pembiayaan Masih Kosong !!!");
            } else if (Jaminan.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Form Jaminan Masih Kosong !!!");
            } else if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else if (Jatuh.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Jatuh Tempo Masih Kosong !!!");
            } else if ((int)Waktu.getValue() < 3 || (int)Waktu.getValue() > 20) {
                JOptionPane.showMessageDialog(null, "Tempo Waktu Antara 3 Sampai 20 Bulan !!!");
            } else {
                Base.open();
                LazyList<PembiayaanModel> pembiayaans = PembiayaanModel.where("no_pembiayaan = ?", No.getText());
                Base.close();
                
                Base.open();
                if (pembiayaans.size() > 0) {
                    Base.close();
                    
                    Base.open();
                    PembiayaanModel pembiayaan = pembiayaans.get(0);
                    Base.close();
                    
                    if ((int)pembiayaan.getInteger("id") == Integer.parseInt(ID)) {
                        ubahData();
                        resetForm();
                        loadTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "No Pembiayaan Sudah Ada !!!");
                    }
                    
                    Base.open();
                } else {
                    Base.close();
                    
                    ubahData();
                    resetForm();
                    loadTable();
                    
                    Base.open();
                }
                Base.close();
            }
        }
    }//GEN-LAST:event_ButtonTambahUbahActionPerformed

    private void ButtonResetHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonResetHapusActionPerformed
        if (state.equals("edit")) {
            hapusData();
            loadTable();
        }

        resetForm();
    }//GEN-LAST:event_ButtonResetHapusActionPerformed

    private void TextCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextCariActionPerformed
        cari();
    }//GEN-LAST:event_TextCariActionPerformed

    private void NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoActionPerformed

    private void AnggotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnggotaActionPerformed
        comboAnggotaIndex = Anggota.getSelectedIndex();
        if (comboAnggotaIndex >= 0) {
            selectedComboAnggotaIndex = comboAnggotaID.get(comboAnggotaIndex);
        }
    }//GEN-LAST:event_AnggotaActionPerformed

    private void AnggotaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_AnggotaItemStateChanged

    }//GEN-LAST:event_AnggotaItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pembiayaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pembiayaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pembiayaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pembiayaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pembiayaan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner Adm;
    private javax.swing.JComboBox<String> Anggota;
    private javax.swing.JSpinner Bagi;
    private javax.swing.JButton ButtonRefresh;
    private javax.swing.JButton ButtonResetHapus;
    private javax.swing.JButton ButtonTambahUbah;
    private javax.swing.JTextField Jaminan;
    private com.toedter.calendar.JDateChooser Jatuh;
    private javax.swing.JLabel LabelCari;
    private javax.swing.JLabel LabelCari1;
    private javax.swing.JLabel LabelCari10;
    private javax.swing.JLabel LabelCari2;
    private javax.swing.JLabel LabelCari3;
    private javax.swing.JLabel LabelCari4;
    private javax.swing.JLabel LabelCari5;
    private javax.swing.JLabel LabelCari6;
    private javax.swing.JLabel LabelCari7;
    private javax.swing.JLabel LabelCari8;
    private javax.swing.JLabel LabelCari9;
    private javax.swing.JTextField No;
    private javax.swing.JSpinner Plafon;
    private javax.swing.JSpinner Pokok;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JTable TablePegawai;
    private com.toedter.calendar.JDateChooser Tanggal;
    private javax.swing.JTextField TextCari;
    private javax.swing.JSpinner Waktu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}

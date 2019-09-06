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
import test.test.Models.AngsuranModel;
import test.test.Models.PembiayaanModel;
import test.test.Reports.Config;

/**
 *
 * @author user
 */
public class Angsuran extends javax.swing.JFrame {
    private int IDPembiayaan = 0;
    
    private DefaultTableModel model = new DefaultTableModel();
    private String ID;
    private String state;
    
    private double valPlafon;
    private double valBasil;
    private double valPokok;
    private int valWaktu;
    private Date valTanggal;
    private Date valJatuh;
    
    int pokok;
    int basil;
    int totalSebelumDenda;
    int jumlahAngsuran;
    int angsuranKe;
    int bulanJava;
    int bulanNormal;
    double denda;
    int sisaPeriodeAngsuran;
    int sisaAngsuran;
    Date periodeAngsuran_raw;
    int telatHari;
    Date tanggalAngsuran;
            
    /**
     * Creates new form PangkatGol
     */
    public Angsuran() {
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
        
        Pembiayaan.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                cariPembiayaan();
            }

            public void removeUpdate(DocumentEvent e) {
                cariPembiayaan();
            }

            public void changedUpdate(DocumentEvent e) {
                cariPembiayaan();
            }
        });
        
        Tanggal.getDateEditor().addPropertyChangeListener( new PropertyChangeListener() 
        {
            @Override
            public void propertyChange(PropertyChangeEvent e) 
            {
                if ("date".equals(e.getPropertyName())) 
                {
                    valTanggal = (Date) e.getNewValue();
                    
                    
                    if (valTanggal != null) {
                        cariPembiayaan();
                    }
                }
            }
        });
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void cariPembiayaan() {
        Base.open();
        LazyList<PembiayaanModel> pembiayaans = PembiayaanModel.where("no_pembiayaan = ?", Pembiayaan.getText());
        Base.close();
        
        Base.open();
        if (pembiayaans.size() > 0) {
            Base.close();
            
            Base.open();
            PembiayaanModel pembiayaan = pembiayaans.get(0);
            Base.close();
            
            Base.open();
            AnggotaModel anggota = pembiayaan.parent(AnggotaModel.class);
            Base.close();
            
            pokok = pembiayaan.getInteger("pokok");
            basil = pembiayaan.getInteger("basil") / pembiayaan.getInteger("waktu");
            totalSebelumDenda = pokok + basil;
            
            Pokok.setValue(pokok);
            Bagi.setValue(basil);
            Nama.setText(anggota.getString("nama"));
            
            IDPembiayaan = pembiayaan.getInteger("id");
            
            Base.open();
            LazyList<AngsuranModel> angsurans = AngsuranModel.where("id_pembiayaan = ?", IDPembiayaan);
            jumlahAngsuran = angsurans.size();
            
            
            if (state.equals("index")) {
                angsuranKe = jumlahAngsuran + 1;
            } else if (state.equals("edit")) {
                LazyList<AngsuranModel> ass = AngsuranModel.where("id <= ? AND id_pembiayaan = ?", ID, IDPembiayaan);
                int sizeAss = ass.size();
                angsuranKe = sizeAss;
            } else {
                JOptionPane.showMessageDialog(null, "Index/Edit ?");
            }
            
            
            try {
                periodeAngsuran_raw = ADHhelper.dateTambahBulan(ADHhelper.getTanggalFromDB(pembiayaan.getString("tanggal")), angsuranKe);
                bulanJava = ADHhelper.dateGetMonth(periodeAngsuran_raw);
                bulanNormal = bulanJava + 1;
                
                String bulanPeriodeAngsuran = ADHhelper.bulan(bulanNormal);
                String tahunPeriodeAngsuran = String.valueOf(ADHhelper.dateGetYear(periodeAngsuran_raw));
                
                String periodeAngsuran = bulanPeriodeAngsuran + " " + tahunPeriodeAngsuran;
                Periode.setText(periodeAngsuran);
                
                String jatuhTempo = ADHhelper.tanggalIndo(ADHhelper.parseTanggal(periodeAngsuran_raw));
                Jatuh.setText(jatuhTempo);
                
                telatHari = ADHhelper.dateDayBetween(periodeAngsuran_raw, Tanggal.getDate());
                if (telatHari > 0) {
                    denda = ((totalSebelumDenda * 0.25) / 100) * telatHari;
                    Denda.setText(ADHhelper.rupiah((int)denda));                
                    Total.setText(ADHhelper.rupiah((int)denda + totalSebelumDenda));                
                } else {
                    Denda.setText("");                 
                    Total.setText(ADHhelper.rupiah(totalSebelumDenda));                
                }
            } catch (ParseException ex) {
                Logger.getLogger(Angsuran.class.getName()).log(Level.SEVERE, null, ex);
            }
            Base.close();
            
            Angsuran.setText(String.valueOf(angsuranKe));
            
            sisaPeriodeAngsuran = pembiayaan.getInteger("waktu") - jumlahAngsuran;
            
            if (sisaPeriodeAngsuran > 0) {
                sisaAngsuran = (pokok + basil) * sisaPeriodeAngsuran;
            } else {
                sisaAngsuran = 0;
            }
            
            Sisa.setText(ADHhelper.rupiah(sisaAngsuran));
            btnPelunasan.setEnabled(true);
            Base.open();
        } else {
            Pokok.setValue(0);
            Bagi.setValue(0);
            Nama.setText("");
            
            Angsuran.setText("");
            Periode.setText("");
            Jatuh.setText("");
            Denda.setText("");
            Total.setText("");
            Sisa.setText("");
            btnPelunasan.setEnabled(false);
            IDPembiayaan = 0;
        }
        Base.close();
    }
    
    public void cari() {
        if (TextCari.getText().equals("")) {
            loadTable();
        } else {
            loadTable(TextCari.getText());
        }
    }
    
    private void loadTableHelper(LazyList<AngsuranModel> angsurans) {
        model = new DefaultTableModel();
                
        model.addColumn("#ID");
        model.addColumn("No Pembiayaan");
        model.addColumn("Nama");
        model.addColumn("Tanggal");
        model.addColumn("Angsuran Pokok");
        model.addColumn("Angsuran Bagi Hasil");
        
        Base.open();
        
        try {
            for(AngsuranModel angsuran : angsurans) {       
                PembiayaanModel pembiayaan = angsuran.parent(PembiayaanModel.class);
                AnggotaModel anggota = pembiayaan.parent(AnggotaModel.class);
                model.addRow(new Object[]{
                    angsuran.getId(),
                    pembiayaan.getString("no_pembiayaan"),
                    anggota.getString("nama"),
                    ADHhelper.tanggalIndo(angsuran.getString("tanggal")),
                    ADHhelper.rupiah(angsuran.getInteger("pokok")),
                    ADHhelper.rupiah(angsuran.getInteger("basil")),
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
        
        TablePegawai.setModel(model);
        
        setState("index");
        
        btnCetak.setEnabled(false);
    }
    
    private void formatTextInteger() {
       
    }
    
    private void loadTable() {
        Base.open();
        LazyList<AngsuranModel> angsurans = AngsuranModel.findAll();
        Base.close();
        
        loadTableHelper(angsurans);
    }

    private void loadTable(String cari) {
        Base.open();
        LazyList<AngsuranModel> angsurans = AngsuranModel.findBySQL("SELECT a.* FROM pembiayaan p, angsuran a, anggota ag WHERE a.id_pembiayaan = p.id AND p.id_anggota = ag.id AND (p.no_pembiayaan like ? OR ag.nama like ?)", '%' + cari + '%', '%' + cari + '%');
        Base.close();
        
        loadTableHelper(angsurans);
    }

    
    private void hapusData() {
        Base.open();
        AngsuranModel angsuran = AngsuranModel.findById(ID);
        try {
            angsuran.delete();
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
            AngsuranModel angsuran = new AngsuranModel();
            angsuran.set("id_pembiayaan", IDPembiayaan);
            angsuran.set("tanggal", ADHhelper.parseTanggal(Tanggal.getDate()));
            angsuran.set("basil", Bagi.getValue());
            angsuran.set("pokok", Pokok.getValue());
            angsuran.save();
            
            PembiayaanModel pby = PembiayaanModel.findById(IDPembiayaan);
            LazyList<AngsuranModel> angs = AngsuranModel.where("id_pembiayaan = ?", IDPembiayaan);
            
            if (angs.size() >= pby.getInteger("waktu")) {
                pby.set("tanggal_pelunasan", ADHhelper.parseTanggal(Tanggal.getDate()));
                pby.save();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }
    
    private void ubahData() {
        Base.open();
        try {
            AngsuranModel angsuran = AngsuranModel.findById(ID);
            angsuran.set("id_pembiayaan", IDPembiayaan);
            angsuran.set("tanggal", ADHhelper.parseTanggal(Tanggal.getDate()));
            angsuran.set("basil", Bagi.getValue());
            angsuran.set("pokok", Pokok.getValue());
            angsuran.save();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        Base.close();
    }

    private void resetForm() {
        Pembiayaan.setText("");
        Nama.setText("");
        Pokok.setValue(0);
        Bagi.setValue(0);
        Tanggal.setDate(null);
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
        LabelCari = new javax.swing.JLabel();
        TextCari = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        ButtonRefresh = new javax.swing.JButton();
        ButtonTambahUbah = new javax.swing.JButton();
        ButtonResetHapus = new javax.swing.JButton();
        btnPelunasan = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        LabelCari9 = new javax.swing.JLabel();
        Pembiayaan = new javax.swing.JTextField();
        Periode = new javax.swing.JTextField();
        LabelCari10 = new javax.swing.JLabel();
        LabelCari11 = new javax.swing.JLabel();
        Angsuran = new javax.swing.JTextField();
        Jatuh = new javax.swing.JTextField();
        LabelCari1 = new javax.swing.JLabel();
        LabelCari12 = new javax.swing.JLabel();
        LabelCari13 = new javax.swing.JLabel();
        Denda = new javax.swing.JTextField();
        LabelCari14 = new javax.swing.JLabel();
        Total = new javax.swing.JTextField();
        Sisa = new javax.swing.JTextField();
        LabelCari15 = new javax.swing.JLabel();
        LabelCari2 = new javax.swing.JLabel();
        Nama = new javax.swing.JTextField();
        LabelCari3 = new javax.swing.JLabel();
        LabelCari4 = new javax.swing.JLabel();
        Tanggal = new com.toedter.calendar.JDateChooser();
        Pokok = new javax.swing.JSpinner();
        Bagi = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Angsuran");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(0, 153, 153));

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

        LabelCari.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        LabelCari.setForeground(new java.awt.Color(255, 255, 255));
        LabelCari.setText("Cari (No/Nama)");

        TextCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextCariActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

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

        btnPelunasan.setText("PELUNASAN");
        btnPelunasan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPelunasanActionPerformed(evt);
            }
        });

        btnCetak.setText("CETAK");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ButtonTambahUbah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButtonRefresh)
                .addGap(66, 66, 66)
                .addComponent(ButtonResetHapus)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(btnPelunasan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPelunasan)
                    .addComponent(btnCetak))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonRefresh)
                    .addComponent(ButtonResetHapus)
                    .addComponent(ButtonTambahUbah))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        LabelCari9.setText("Basil");

        Pembiayaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PembiayaanActionPerformed(evt);
            }
        });

        Periode.setEditable(false);

        LabelCari10.setText("Periode");

        LabelCari11.setText("Angsuran Ke");

        Angsuran.setEditable(false);

        Jatuh.setEditable(false);

        LabelCari1.setText("No Pembiayaan");

        LabelCari12.setText("Jatuh Tempo");

        LabelCari13.setText("Denda");

        Denda.setEditable(false);

        LabelCari14.setText("Total");

        Total.setEditable(false);

        Sisa.setEditable(false);

        LabelCari15.setText("Sisa Angsuran");

        LabelCari2.setText("Nama");

        Nama.setEditable(false);
        Nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NamaActionPerformed(evt);
            }
        });

        LabelCari3.setText("Tanggal");

        LabelCari4.setText("Pokok");

        Tanggal.setDateFormatString("dd-MM-yyyy");

        Pokok.setEnabled(false);

        Bagi.setEnabled(false);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ANGSURAN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(313, 313, 313)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari15, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Sisa))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari14, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Total))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari13, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Denda))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari12, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Jatuh))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Pokok))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelCari2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelCari1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Nama)
                                    .addComponent(Pembiayaan)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelCari9, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelCari10, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Periode)
                                    .addComponent(Bagi)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LabelCari11, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Angsuran, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(LabelCari)
                        .addGap(18, 18, 18)
                        .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelCari1)
                    .addComponent(Pembiayaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelCari)
                    .addComponent(TextCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari2)
                            .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelCari3)
                            .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari4)
                            .addComponent(Pokok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelCari9)
                            .addComponent(Bagi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Periode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelCari10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Angsuran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelCari11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Jatuh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelCari12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Denda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelCari13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelCari14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Sisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelCari15))
                        .addGap(62, 62, 62)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            AngsuranModel angsuran = AngsuranModel.findById(ID);
            Base.close();

            Base.open();
            PembiayaanModel pembiayaan = angsuran.parent(PembiayaanModel.class);
            Base.close();
            
            Pembiayaan.setText(pembiayaan.getString("no_pembiayaan"));
            cariPembiayaan();
            try {
                Tanggal.setDate(ADHhelper.getTanggalFromDB(angsuran.getString("tanggal")));
                tanggalAngsuran = ADHhelper.getTanggalFromDB(angsuran.getString("tanggal"));
            } catch (ParseException ex) {
                Logger.getLogger(Angsuran.class.getName()).log(Level.SEVERE, null, ex);
            }
            Pokok.setValue(angsuran.getInteger("pokok"));
            Bagi.setValue(angsuran.getInteger("basil"));
            
            setState("edit");
            
            btnCetak.setEnabled(true);
        }
    }//GEN-LAST:event_TablePegawaiMouseClicked

    private void ButtonTambahUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonTambahUbahActionPerformed
        if (state.equals("index")) {
            if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else {
                Base.open();
                LazyList<PembiayaanModel> pembiayaans = PembiayaanModel.where("no_pembiayaan = ?", Pembiayaan.getText());
                Base.close();

                Base.open();
                if (pembiayaans.size() > 0) {
                    Base.close();
                    
                    Base.open();
                    PembiayaanModel pby = PembiayaanModel.findById(IDPembiayaan);
                    Base.close();
                    
                    if (pby.getString("tanggal_pelunasan") == null) {
                        tambahData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Pembiayaan Sudah Lunas !!!");
                    }
                   
                    resetForm();
                    loadTable();
                    
                    Base.open();
                } else {
                    JOptionPane.showMessageDialog(null, "No Pembiayaan Tidak Ada !!!");
                }
                Base.close();
            }
        } else {
            if (Tanggal.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
            } else {
                 Base.open();
                LazyList<PembiayaanModel> pembiayaans = PembiayaanModel.where("no_pembiayaan = ?", Pembiayaan.getText());
                Base.close();

                Base.open();
                if (pembiayaans.size() > 0) {
                    Base.close();
                    
                    ubahData();
                    resetForm();
                    loadTable();
                    
                    Base.open();
                } else {
                    JOptionPane.showMessageDialog(null, "No Pembiayaan Tidak Ada !!!");
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

    private void NamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NamaActionPerformed

    private void btnPelunasanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPelunasanActionPerformed
        if (Tanggal.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Form Tanggal Masih Kosong !!!");
        } else {
            Base.open();
            LazyList<AngsuranModel> angsurans = AngsuranModel.where("id_pembiayaan = ?", IDPembiayaan);
            Base.close();
            
            Base.open();
            PembiayaanModel pembiayaan = PembiayaanModel.findById(IDPembiayaan);
            Base.close();
            
            Base.open();
            if (pembiayaan.getString("tanggal_pelunasan") == null) {
                Base.close();
                int pokok = pembiayaan.getInteger("pokok");
                int basil = pembiayaan.getInteger("basil");
                int waktu = pembiayaan.getInteger("waktu");
                Base.open();
                int jumlahAngsuran  = angsurans.size();
                Base.close();
                int sisa = waktu - jumlahAngsuran;
                int total;
                if (sisa >= 2) {
                    pokok = pokok * sisa;
                    basil = basil / waktu * (sisa - 2);
                } else {
                    pokok = pokok * sisa;
                    basil = basil / waktu * sisa;                
                }
                total = pokok + basil;

                String kataKata = "Untuk pelunasan diharuskan membayar seluruh kekurangan yang ada.\n"
                        + "Pokok: " + ADHhelper.rupiah(pokok) + "\n"
                        + "Bagi Hasil: " + ADHhelper.rupiah(basil) + "\n"
                        + "Total: " + ADHhelper.rupiah(total) + "\n"
                        + "Lakukan Pelunasan ?";

                int rslt = JOptionPane.showConfirmDialog(null, kataKata);
                if (rslt == JOptionPane.YES_OPTION) {
                    for (int i = 1; i <= sisa; i++) {
                        Base.open();
                        AngsuranModel angsuran = new AngsuranModel();
                        angsuran.set("id_pembiayaan", IDPembiayaan);
                        try {
                            angsuran.set("tanggal", ADHhelper.parseTanggal(Tanggal.getDate()));
                        } catch (ParseException ex) {
                            Logger.getLogger(Angsuran.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        angsuran.set("pokok", pembiayaan.getInteger("pokok"));
                        angsuran.set("basil", pembiayaan.getInteger("basil") / pembiayaan.getInteger("waktu"));
                        angsuran.save();
                        Base.close();
                    }

                    if (sisa >= 2) {
                        Base.open();
                        LazyList<AngsuranModel> angsuransCheck = AngsuranModel.findBySQL("SELECT * FROM angsuran WHERE id_pembiayaan = ? LIMIT 2", IDPembiayaan);
                        Base.close();

                        Base.open();
                        for(AngsuranModel angsuran : angsuransCheck) {
                            angsuran.set("basil", 0);
                            angsuran.save();
                        }
                        Base.close();
                    }

                    try {
                        pembiayaan.set("tanggal_pelunasan", ADHhelper.parseTanggal(ADHhelper.dateToday()));
                        Base.open();
                        pembiayaan.save();
                        Base.close();
                    } catch (ParseException ex) {
                        Logger.getLogger(Angsuran.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                resetForm();
                loadTable();
            } else {
                Base.close();
                JOptionPane.showMessageDialog(null, "Pembiayaan Sudah Lunas !!!");
            }
        }
    }//GEN-LAST:event_btnPelunasanActionPerformed

    private void PembiayaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PembiayaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PembiayaanActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        try{
            Config objkoneksi = new Config();
            Connection con = objkoneksi.bukakoneksi();
            String fileName="src/main/java/test/test/Reports/slip.jrxml";
            String filetoFill="src/main/java/test/test/Reports/slip.jasper";
            JasperCompileManager.compileReport(fileName);
            
            Map param= new HashMap();
            param.put("tanggal", ADHhelper.tanggalIndo(ADHhelper.parseTanggal(tanggalAngsuran)));
            param.put("terima_dari", Nama.getText());
            param.put("angsuran_ke", angsuranKe);
            param.put("pokok", ADHhelper.rupiah(pokok));
            param.put("basil", ADHhelper.rupiah(basil));
            param.put("denda", ADHhelper.rupiah((int)denda));
            param.put("total", ADHhelper.rupiah((int)denda + totalSebelumDenda));
            
            JasperFillManager.fillReport(filetoFill, param, con);
            JasperPrint jp=JasperFillManager.fillReport(filetoFill, param,con);
            JasperViewer.viewReport(jp,false);

        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }//GEN-LAST:event_btnCetakActionPerformed

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
            java.util.logging.Logger.getLogger(Angsuran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Angsuran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Angsuran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Angsuran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new Angsuran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Angsuran;
    private javax.swing.JSpinner Bagi;
    private javax.swing.JButton ButtonRefresh;
    private javax.swing.JButton ButtonResetHapus;
    private javax.swing.JButton ButtonTambahUbah;
    private javax.swing.JTextField Denda;
    private javax.swing.JTextField Jatuh;
    private javax.swing.JLabel LabelCari;
    private javax.swing.JLabel LabelCari1;
    private javax.swing.JLabel LabelCari10;
    private javax.swing.JLabel LabelCari11;
    private javax.swing.JLabel LabelCari12;
    private javax.swing.JLabel LabelCari13;
    private javax.swing.JLabel LabelCari14;
    private javax.swing.JLabel LabelCari15;
    private javax.swing.JLabel LabelCari2;
    private javax.swing.JLabel LabelCari3;
    private javax.swing.JLabel LabelCari4;
    private javax.swing.JLabel LabelCari9;
    private javax.swing.JTextField Nama;
    private javax.swing.JTextField Pembiayaan;
    private javax.swing.JTextField Periode;
    private javax.swing.JSpinner Pokok;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JTextField Sisa;
    private javax.swing.JTable TablePegawai;
    private com.toedter.calendar.JDateChooser Tanggal;
    private javax.swing.JTextField TextCari;
    private javax.swing.JTextField Total;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnPelunasan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}

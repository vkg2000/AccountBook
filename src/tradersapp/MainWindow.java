/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tradersapp;

import com.mysql.jdbc.Connection;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import sun.net.www.content.image.png;

/**
 *
 * @author offic
 */
public class MainWindow extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        //MySqlConnection(); this was for testing data connection set or not
        fillTable();
        
    }
    
    
    //Setting Up database
    
    public Connection MySqlConnection(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/anushkatrader", "root", "Nightchanges@microsoftmysql");
            //JOptionPane.showMessageDialog(null,"MySql Connection Successful....");
            return conn;
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"MySql Connection Failed....");
            return null;
        }
            
            
    }
        
    //for printing no. of customer registered

    public void printcount(){
        int maxx=0;
        try{
            String qry = "SELECT *FROM customer";
            Connection conn=MySqlConnection();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(qry);

            
            while(rs.next()){
                if(maxx<rs.getInt("id")){
                    maxx=rs.getInt("id");
                }
            }
        }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+"....../Error in fetching data from form4444");
                 }
                
        jTextField_count.setText("#"+String.valueOf(maxx));
        
    }
    
    
    //for resetting size of image
    
    String photopath="";
    public ImageIcon resetImageSize(String photopath,byte[] photo){
        
        ImageIcon myPhoto=null;
        if(photopath!=null){
            myPhoto=new ImageIcon(photopath);
            
        }
        else{
            myPhoto=new ImageIcon(photo);
        }
        
        Image img=myPhoto.getImage();
        Image img1=img.getScaledInstance(label_photo.getWidth(),label_photo.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon ph=new ImageIcon(img1);
        return ph;
        
    }
    
    //for retrieving data from database to show on table
    
    public ArrayList<CustomerTable> retrieveData(){
        
        ArrayList<CustomerTable> al=null;
        al=new ArrayList<CustomerTable>();
        if(jTextField_mobile.getText().equals("") && jTextField_id.getText()!="" ){    
        try{
            String curr_id=jTextField_id.getText().toString();
            String table_name="table"+curr_id;
            Connection conn=MySqlConnection();
            String qry="SELECT *FROM "+table_name;
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(qry);
            CustomerTable customer ;
            while(rs.next()){
                customer=new CustomerTable(rs.getInt("id"),rs.getString("date"),rs.getInt("deposit"));
                al.add(customer);
            }
      
        }catch(Exception e){
            System.out.println("Error in retrieving method:" +e);
        }
        }
        else if(jTextField_id.getText().equals("") && jTextField_mobile.getText()!=""){ 

            try{
                //for getting table name of respective row through mobile
                
                String mqry="SELECT *FROM customer WHERE mobile="+jTextField_mobile.getText().toString();
                Connection conn=MySqlConnection();
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(mqry);
                String table_name=null;
                if(rs.next())
                    table_name=rs.getString("link");
                
                try{
                    String qry="SELECT *FROM "+table_name;
                    Statement st2=conn.createStatement();
                    ResultSet rs2=st.executeQuery(qry);
                    CustomerTable customer ;
                    while(rs2.next()){
                        customer=new CustomerTable(rs2.getInt("id"),rs2.getString("date"),rs2.getInt("deposit"));
                        al.add(customer);
                        }
      
                    }catch(Exception e){
                         System.out.println("Error in retrieving method:" +e);
                            }
            }catch(Exception e){
                System.out.println("Error in retrieving method:" +e);
                    }
        }
        else if(jTextField_id.getText()!="" && jTextField_mobile.getText()!=""){
            
            try{
                String curr_id=jTextField_id.getText().toString();
                String table_name="table"+curr_id;                
                String mqry="SELECT *FROM customer WHERE id="+jTextField_id.getText().toString();
                Connection conn=MySqlConnection();
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(mqry);
                String curr_mobile=null;
                if(rs.next())
                    curr_mobile=rs.getString("mobile");
                
                if(Long.parseLong(curr_mobile.toString())==Long.parseLong(jTextField_mobile.getText().toString())){    
                    
                    try{                    
                    String qry="SELECT *FROM "+table_name;
                    Statement st2=conn.createStatement();
                    ResultSet rs2=st.executeQuery(qry);
                    CustomerTable customer ;
                    while(rs2.next()){
                        customer=new CustomerTable(rs2.getInt("id"),rs2.getString("date"),rs2.getInt("deposit"));
                        al.add(customer);
                        }
      
                    }catch(Exception e){
                                JOptionPane.showMessageDialog(null,e+"...../Error in fetching data from form");}
                
                    }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+"...../Error in fetching data from form");}            
            
            
        } 
        
        return al;
         
    }
    
    
    //////////////////////////////////////////
    
    public void clearall()
    {
                         
        jTextField_id.setText("");
        jTextField_mobile.setText("");
        jTextField_name.setText("");
        jTextField_Address.setText("");
        jDateChooser_curr.setCalendar(null);
        label_photo.setIcon(null);
        jTextField_deposit.setText("");
    }
    
    
    ///////////////////////////////////////////
    
    
    
    public void fillTable(){
        //for updating counting
        printcount();
        
        ArrayList<CustomerTable> al=retrieveData();
        DefaultTableModel model=(DefaultTableModel)jTable1.getModel();
        model.setRowCount(0);
        Object[] row=new Object[3];
        int sum=0;
        for(int i=0;i<al.size();i++)
        {
            row[0]=al.get(i).getId();
            row[1]=al.get(i).getDate();
            row[2]=al.get(i).getDeposit();
            sum=sum+al.get(i).getDeposit();
            model.addRow(row);
        }
        jTextField_balance.setText(String.valueOf(sum));
    }    
    
    
    ///////////////////////////////////////////

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField_name = new javax.swing.JTextField();
        jTextField_mobile = new javax.swing.JTextField();
        jTextField_Address = new javax.swing.JTextField();
        jTextField_id = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField_deposit = new javax.swing.JTextField();
        jDateChooser_curr = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField_balance = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton_photo = new javax.swing.JButton();
        label_photo = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField_count = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 255), new java.awt.Color(204, 204, 204)));
        jPanel1.setLayout(null);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(290, 20, 185, 187);

        jLabel1.setFont(new java.awt.Font("Charlemagne Std", 0, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ACCOUNT BOOK");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(480, 90, 491, 49);

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 22)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 51, 0));
        jLabel3.setText("Customer ID");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(100, 280, 170, 40);

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 22)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 51, 0));
        jLabel4.setText("Customer Name");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(100, 360, 170, 20);

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 22)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 51, 0));
        jLabel5.setText("Mobile NO.");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(100, 430, 170, 40);

        jLabel6.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 22)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 51, 0));
        jLabel6.setText("Address");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(100, 510, 170, 40);
        jPanel1.add(jTextField_name);
        jTextField_name.setBounds(320, 350, 260, 50);
        jPanel1.add(jTextField_mobile);
        jTextField_mobile.setBounds(320, 420, 260, 50);
        jPanel1.add(jTextField_Address);
        jTextField_Address.setBounds(320, 500, 490, 70);
        jPanel1.add(jTextField_id);
        jTextField_id.setBounds(320, 280, 260, 50);

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 51, 0));
        jLabel9.setText("Deposit");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(100, 590, 150, 60);

        jTextField_deposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_depositActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField_deposit);
        jTextField_deposit.setBounds(320, 590, 210, 60);

        jDateChooser_curr.setDateFormatString("dd-MM-yyyy");
        jPanel1.add(jDateChooser_curr);
        jDateChooser_curr.setBounds(540, 590, 140, 60);

        jButton1.setBackground(new java.awt.Color(153, 153, 255));
        jButton1.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 24)); // NOI18N
        jButton1.setText("INSERT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(260, 680, 130, 60);

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 24)); // NOI18N
        jButton2.setText("DELETE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(550, 680, 120, 60);

        jButton3.setBackground(new java.awt.Color(153, 255, 153));
        jButton3.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 24)); // NOI18N
        jButton3.setText("UPDATE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(400, 680, 140, 60);

        jButton5.setBackground(new java.awt.Color(204, 204, 204));
        jButton5.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 24)); // NOI18N
        jButton5.setText("SAVE");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);
        jButton5.setBounds(690, 590, 130, 60);
        jPanel1.add(jTextField_balance);
        jTextField_balance.setBounds(1080, 680, 170, 60);

        jLabel10.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("BALANCE");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(950, 680, 120, 60);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "SI No.", "Date", "Deposit"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(870, 260, 380, 410);

        jButton_photo.setBackground(new java.awt.Color(255, 255, 255));
        jButton_photo.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jButton_photo.setText("SELECT PHOTO");
        jButton_photo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_photoActionPerformed(evt);
            }
        });
        jPanel1.add(jButton_photo);
        jButton_photo.setBounds(640, 420, 170, 40);

        label_photo.setOpaque(true);
        jPanel1.add(label_photo);
        label_photo.setBounds(670, 280, 120, 130);

        jButton4.setBackground(new java.awt.Color(255, 204, 204));
        jButton4.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 24)); // NOI18N
        jButton4.setText("SEARCH");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(110, 680, 140, 60);

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 24)); // NOI18N
        jButton6.setForeground(new java.awt.Color(204, 0, 0));
        jButton6.setText("RESET");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);
        jButton6.setBounds(680, 680, 140, 60);

        jTextField_count.setBackground(new java.awt.Color(204, 204, 204));
        jTextField_count.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 24)); // NOI18N
        jPanel1.add(jTextField_count);
        jTextField_count.setBounds(1111, 86, 80, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1317, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 781, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel1.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField_depositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_depositActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_depositActionPerformed

    
    
    
    
    //FOR INSERTING NEW CUSTOMER
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
 
        if((jTextField_id.getText()!=null || jTextField_name!=null || 
                jTextField_mobile!=null || jTextField_Address!=null  || jDateChooser_curr!=null )&&(photopath!=null)){
            
            try{
                
                PreparedStatement ps=MySqlConnection().prepareStatement("insert into customer"+ "(id,name,mobile,Address,date,photo,link) values(?,?,?,?,?,?,?)");               
                ps.setInt(1,Integer.parseInt(jTextField_id.getText()));
                ps.setString(2,jTextField_name.getText());
                ps.setString(3,jTextField_mobile.getText().toString());
                ps.setString(4,jTextField_Address.getText());               
                SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
                String dob1=sdf.format(jDateChooser_curr.getDate());
                ps.setString(5,dob1);               
                InputStream is=new FileInputStream(new File(photopath));
                ps.setBlob(6,is);
                
                //for linking table with each row
                String curr_id=jTextField_id.getText().toString();
                String table_name="table"+curr_id;
                ps.setString(7,table_name);
                
                //final update               
                int res=ps.executeUpdate();               
                
                if(res>=1){
                    {
                        JOptionPane.showMessageDialog(null,res+" Number of customer inserted in database...");
                        
                        //for creating table for each customer
                        try{
                             PreparedStatement ps1=MySqlConnection().prepareStatement("CREATE TABLE `"+ table_name + "`("+
                                                                                        "`id` INT NOT NULL AUTO_INCREMENT,"+
                                                                                        "`date` VARCHAR(45) NULL,"+
                                                                                        "`deposit` INT NULL,"+
                                                                                        "PRIMARY KEY (`id`))");
                             ps1.executeUpdate();
                             
                        }catch(Exception e){
                    JOptionPane.showMessageDialog(null,e+" ..../Table not created for respective customer");
                }
                        
                        //inserting intial row in respected id table
                        PreparedStatement ps2=MySqlConnection().prepareStatement("insert into "+ table_name + "(date,deposit) values(?,?)");
                        ps2.setString(1,dob1);
                        ps2.setInt(2,Integer.parseInt(jTextField_deposit.getText()));
                        ps2.executeUpdate();
                        fillTable();
                        
                    }
                    
                }else
                    JOptionPane.showMessageDialog(null,"Customer Insertion failed....///Update Request Error");
                
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,e+"...../Error in fetching data from form");
                }          
        }else
            JOptionPane.showMessageDialog(null,"ALL FIELDS ARE COMPULSARY");                      
    }//GEN-LAST:event_jButton1ActionPerformed

////////////////////////////////////////////////////
    
    //FOR DELETING
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
                
        
        if(jTextField_id.getText().equals("") && jTextField_mobile.getText().equals("") ){
            JOptionPane.showMessageDialog(null,"Id OR Mobile NO. is compulsory.");
           
        }
        else
            
        {
                                                                       
            String curr_id=jTextField_id.getText().toString();
            String table_name="table"+curr_id;
            
            if(jTextField_mobile.getText().equals("") && jTextField_id.getText()!="" ){ 
            try{
                String qry="delete from customer where id=?";
                Connection conn=MySqlConnection();
                PreparedStatement ps=conn.prepareStatement(qry);
                ps.setInt(1,Integer.parseInt(jTextField_id.getText().toString()));
                int res=ps.executeUpdate();
                if(res>=1){
                    JOptionPane.showMessageDialog(null,"Customer deleted successfully..");
                    
                    //for deleting linked table
                    
                    PreparedStatement ps1=MySqlConnection().prepareStatement("DROP TABLE "+table_name);
                    int res2=ps1.executeUpdate();
                    clearall();
                    fillTable();
                    
                    }
            else
                JOptionPane.showMessageDialog(null,"Customer deletion failed...///Update Request Error");
                   
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+".../Error in fetching data from form");}
            }
            
            else if(jTextField_id.getText().equals("") && jTextField_mobile.getText()!=""){ 
            try{
                //for getting table name of respective row through mobile
                
                String mqry="SELECT *FROM customer WHERE mobile="+jTextField_mobile.getText().toString();
                Connection conn=MySqlConnection();
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(mqry);
                if(rs.next())
                    table_name=rs.getString("link");
                              
                String qry="delete from customer where mobile=?";
                PreparedStatement ps=conn.prepareStatement(qry);
                ps.setString(1,jTextField_mobile.getText().toString());
                int res=ps.executeUpdate();
                if(res>=1){
                    JOptionPane.showMessageDialog(null,"Customer deleted successfully..");
                    
                    //for deleting linked table
                    
                    PreparedStatement ps1=MySqlConnection().prepareStatement("DROP TABLE "+table_name);
                    int res2=ps1.executeUpdate();
                    clearall();
                    fillTable();
                    
                    }
                else
                    JOptionPane.showMessageDialog(null,"Customer deletion failed.....///Update Request Error");
                   
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,e+".../Error in fetching data from form");}
            }
            
            else
            {

            try{
                String mqry="SELECT *FROM customer WHERE id="+jTextField_id.getText().toString();
                Connection conn=MySqlConnection();
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(mqry);
                String curr_mobile="";
                if(rs.next())
                    curr_mobile=rs.getString("mobile");
                
                if(Long.parseLong(curr_mobile.toString())!=Long.parseLong(jTextField_mobile.getText().toString())){
                    JOptionPane.showMessageDialog(null,"Mobile NO. not matched to entered ID");
                    }  

                else{
                    try{
                        String qry="delete from customer where mobile=?";
                        PreparedStatement ps=conn.prepareStatement(qry);
                        ps.setString(1,jTextField_mobile.getText().toString());
                        int res=ps.executeUpdate();
                
                        if(res>=1){
                            JOptionPane.showMessageDialog(null,"Customer deleted successfully..");
                    
                            //for deleting linked table
                            PreparedStatement ps1=MySqlConnection().prepareStatement("DROP TABLE "+table_name);
                            ps1.executeUpdate();
                            clearall();
                            fillTable();
                            
                            }
                        else
                            JOptionPane.showMessageDialog(null,"Customer deletion failed....///Update Request Error");
                        }catch(Exception e){
                                JOptionPane.showMessageDialog(null,e+".../Error in fetching data from form");}
                
                    }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+".../Error in extracting mobile no.data from database");}
                
            
            }
        }    
    }//GEN-LAST:event_jButton2ActionPerformed

    
////////////////////////////////////////////////////    
    
    //FOR UPDATING
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if((jTextField_id!=null || jTextField_mobile!=null || 
             jTextField_name!=null || jTextField_Address!=null)){
             String qry=null;
             PreparedStatement ps=null;
             Connection conn =MySqlConnection();
             
            if(photopath!=""){
                try{
                    qry="update customer set name=?,mobile=?,Address=?,photo=? where id=?";
                    ps=conn.prepareStatement(qry);
                    ps.setString(1,jTextField_name.getText());
                    ps.setString(2,jTextField_mobile.getText());
                    ps.setString(3,jTextField_Address.getText());
                    InputStream is=new FileInputStream(new File(photopath));                   
                    ps.setBlob(4,is);
                    ps.setInt(5,Integer.parseInt(jTextField_id.getText()));

                    //final update
                    int res=ps.executeUpdate();
                    fillTable();
                    
                    if(res>=1){
                        JOptionPane.showMessageDialog(null,"Customer updated successfully...");
                     }else
                        JOptionPane.showMessageDialog(null,"Customer Updation failed....///Update Request Error");

                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null,e);
                    }
            }else{
                try{
                    qry="update customer set name=?,mobile=?,Address=? where id=?";
                    ps=conn.prepareStatement(qry);
                    ps.setString(1,jTextField_name.getText());
                    ps.setString(2,jTextField_mobile.getText());
                    ps.setString(3,jTextField_Address.getText());
                    ps.setInt(4,Integer.parseInt(jTextField_id.getText()));

                    //final update
                    int res=ps.executeUpdate();
                    fillTable();
                    
                    if(res>=1){
                        JOptionPane.showMessageDialog(null,"Customer updated successfully...");

                    }else
                        JOptionPane.showMessageDialog(null,"Customer Updation failed....///Update Request Error");

                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null,e);
                    }                
             }               
        }else
             JOptionPane.showMessageDialog(null,"Customer Upadation Failed.....//Required Field is Empty");
                                            
    }//GEN-LAST:event_jButton3ActionPerformed

    
//////////////////////////////////////////////////    
    
    
    //FOR SAVING DEPOSIT
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       
                
        if(jTextField_id.getText().equals("") && jTextField_mobile.getText().equals("") ){
            JOptionPane.showMessageDialog(null,"Id OR Mobile NO. is compulsory for depositing");
           
        }else   
        {
                                                                        
            String curr_id=jTextField_id.getText().toString();
            String table_name="table"+curr_id;            
            if(jTextField_mobile.getText().equals("") && jTextField_id.getText()!="" ){ 
            try{
                PreparedStatement ps=MySqlConnection().prepareStatement("insert into "+ table_name + "(date,deposit) values(?,?)");
                SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
                String dob1=sdf.format(jDateChooser_curr.getDate());
                ps.setString(1,dob1);
                ps.setInt(2,Integer.parseInt(jTextField_deposit.getText()));
                int res = ps.executeUpdate(); 
                fillTable();
                
                if(res>=1){
                    JOptionPane.showMessageDialog(null,"Deposit successfull..");
                }else
                    JOptionPane.showMessageDialog(null,"Deposit failed...///Update Request Error");
                   
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+"......../Error in fetching data from form");
                }
            }
            else if(jTextField_id.getText().equals("") && jTextField_mobile.getText()!=""){ 
            try{
                
                //for getting table name of respective row through mobile   
                String mqry="SELECT *FROM customer WHERE mobile="+jTextField_mobile.getText().toString();
                Connection conn=MySqlConnection();
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(mqry);
                if(rs.next())
                    table_name=rs.getString("link");

                PreparedStatement ps=MySqlConnection().prepareStatement("insert into "+ table_name + "(date,deposit) values(?,?)");
                SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
                String dob1=sdf.format(jDateChooser_curr.getDate());
                ps.setString(1,dob1);
                ps.setInt(2,Integer.parseInt(jTextField_deposit.getText()));
                //final update
                int res = ps.executeUpdate(); 
                fillTable();
                
                if(res>=1){
                    JOptionPane.showMessageDialog(null,"Deposit successfull..");
                    }
                else
                    JOptionPane.showMessageDialog(null,"Deposit failed.....///Update Request Error");                
                
                }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+"......./Error in fetching data from form");
                }
            }
            else{
            try{
                String mqry="SELECT *FROM customer WHERE id="+jTextField_id.getText().toString();
                Connection conn=MySqlConnection();
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(mqry);
                String curr_mobile="";
                if(rs.next())
                    curr_mobile=rs.getString("mobile");               
                if(Long.parseLong(curr_mobile.toString())!=Long.parseLong(jTextField_mobile.getText().toString())){
                    JOptionPane.showMessageDialog(null,"Mobile NO. not matched to entered ID");
                    }  
                else{
                    try{
                    PreparedStatement ps=MySqlConnection().prepareStatement("insert into "+ table_name + "(date,deposit) values(?,?)");
                    SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
                    String dob1=sdf.format(jDateChooser_curr.getDate());
                    ps.setString(1,dob1);
                    ps.setInt(2,Integer.parseInt(jTextField_deposit.getText()));
                    int res = ps.executeUpdate();                
                    fillTable();
                    if(res>=1){
                        JOptionPane.showMessageDialog(null,"Deposit successfull..");
                        }
                    else
                        JOptionPane.showMessageDialog(null,"Deposit failed...///Update Request Error");
                   
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null,e+"....../Error in fetching data from form");}
                
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+"....../Error in fetching data from form");}
                
            }    
        
        }                                              
    }//GEN-LAST:event_jButton5ActionPerformed


/////////////////////////////////////////////////    
    
 
    
    //FOR SELECTING PHOTO
    
    private void jButton_photoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_photoActionPerformed
        JFileChooser chooser=new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter fnef=new FileNameExtensionFilter("*.image","jpg","png");
        chooser.addChoosableFileFilter(fnef);
        int ans=chooser.showSaveDialog(null);
        if(ans==JFileChooser.APPROVE_OPTION){
            File selectedPhoto=chooser.getSelectedFile();
            String path=selectedPhoto.getAbsolutePath();
            label_photo.setIcon(resetImageSize(path,null));
            this.photopath=path;
        }    
    }//GEN-LAST:event_jButton_photoActionPerformed

////////////////////////////////////////////////    
    
    //FOR SEARCHING
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        
        if(jTextField_id.getText().equals("") && jTextField_mobile.getText().equals("") ){
            JOptionPane.showMessageDialog(null,"Id OR Mobile NO. is compulsory.");
        }else   
        {
            if(jTextField_mobile.getText().equals("") && jTextField_id.getText()!="" ){ 
            try{
                String mqry="SELECT *FROM customer WHERE id="+jTextField_id.getText().toString();
                Connection conn=MySqlConnection();
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(mqry);
                int flag=0;
                if(rs.next())
                {
                    int curr_id=rs.getInt("id");
                    jTextField_id.setText(String.valueOf(curr_id));
                    jTextField_mobile.setText(rs.getString("mobile"));
                    jTextField_name.setText(rs.getString("name"));
                    jTextField_Address.setText(rs.getString("Address"));
                    String date_str=rs.getString("date");
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    Date date = format.parse(date_str);
                    Calendar calender = Calendar.getInstance();
                    calender.setTime(date);
                    jDateChooser_curr.setCalendar(calender);
                    byte[] curr_photo = rs.getBytes("photo");
                    Image img = Toolkit.getDefaultToolkit().createImage(curr_photo);
                    Image imgl=img.getScaledInstance(label_photo.getWidth(),label_photo.getHeight(),Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(imgl);
                    label_photo.setIcon(icon);
                    
                    fillTable();
                    flag=1;
                    
                }
                if(flag==1){
                    JOptionPane.showMessageDialog(null,"Customer searching successful");
                }
                else
                    JOptionPane.showMessageDialog(null,"Customer searching failed....//Update Request Error");
                   
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+"....../Error in fetching data from form");
                }
            }
            else if(jTextField_id.getText().equals("") && jTextField_mobile.getText()!=""){ 
            try{
                String mqry="SELECT *FROM customer WHERE mobile="+jTextField_mobile.getText().toString();
                Connection conn=MySqlConnection();
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(mqry);
                int flag=0;
                if(rs.next())
                {
                    int curr_id=rs.getInt("id");
                    jTextField_id.setText(String.valueOf(curr_id));
                    jTextField_mobile.setText(rs.getString("mobile"));
                    jTextField_name.setText(rs.getString("name"));
                    jTextField_Address.setText(rs.getString("Address"));
                    String date_str=rs.getString("date");
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    Date date = format.parse(date_str);
                    Calendar calender = Calendar.getInstance();
                    calender.setTime(date);
                    jDateChooser_curr.setCalendar(calender);
                    byte[] curr_photo = rs.getBytes("photo");
                    Image img = Toolkit.getDefaultToolkit().createImage(curr_photo);
                    Image imgl=img.getScaledInstance(label_photo.getWidth(),label_photo.getHeight(),Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(imgl);
                    label_photo.setIcon(icon);
                    
                    fillTable();
                    flag=1;
                    
                }
                if(flag==1){
                    JOptionPane.showMessageDialog(null,"Customer searching successful");
                }
                else
                    JOptionPane.showMessageDialog(null,"Customer searching failed...//Update Request Error");
                   
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+"....../Error in fetching data from form");
                }
            }
            else
            {
            try{
                String mqry="SELECT *FROM customer WHERE id="+jTextField_id.getText().toString();
                Connection conn=MySqlConnection();
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(mqry);
                String curr_mobile="";
                if(rs.next())
                    curr_mobile=rs.getString("mobile");                
                if(Long.parseLong(curr_mobile.toString())!=Long.parseLong(jTextField_mobile.getText().toString())){
                    JOptionPane.showMessageDialog(null,"Mobile NO. not matched to entered ID");
                    }  
                else{
                try{
                String qry="SELECT *FROM customer WHERE id="+jTextField_id.getText().toString();
                rs=st.executeQuery(qry);
                int flag=0;
                if(rs.next())
                {
                    int curr_id=rs.getInt("id");
                    jTextField_id.setText(String.valueOf(curr_id));
                    jTextField_mobile.setText(rs.getString("mobile"));
                    jTextField_name.setText(rs.getString("name"));
                    jTextField_Address.setText(rs.getString("Address"));
                    String date_str=rs.getString("date");
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    Date date = format.parse(date_str);
                    Calendar calender = Calendar.getInstance();
                    calender.setTime(date);
                    jDateChooser_curr.setCalendar(calender);
                    byte[] curr_photo = rs.getBytes("photo");
                    Image img = Toolkit.getDefaultToolkit().createImage(curr_photo);
                    Image imgl=img.getScaledInstance(label_photo.getWidth(),label_photo.getHeight(),Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(imgl);
                    label_photo.setIcon(icon);
                    
                    fillTable();
                    flag=1;
                    
                }
                if(flag==1){
                    JOptionPane.showMessageDialog(null,"Customer searching successful");
                    }
                else
                    JOptionPane.showMessageDialog(null,"Customer searching failed...//Update Request Error");
                   
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,e+"....../Error in fetching data from form");
                    }
                
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e+"....../Error in fetching data from form");}
                
            
            }                                        
        }        
          
    }//GEN-LAST:event_jButton4ActionPerformed

    
 ///////////////////////////////////////////////
    
    //For Resetting
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        clearall();
        fillTable();
    }//GEN-LAST:event_jButton6ActionPerformed

    
    ////////////////////////////////////////////
    
    
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
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton_photo;
    private com.toedter.calendar.JDateChooser jDateChooser_curr;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField_Address;
    private javax.swing.JTextField jTextField_balance;
    private javax.swing.JLabel jTextField_count;
    private javax.swing.JTextField jTextField_deposit;
    private javax.swing.JTextField jTextField_id;
    private javax.swing.JTextField jTextField_mobile;
    private javax.swing.JTextField jTextField_name;
    private javax.swing.JLabel label_photo;
    // End of variables declaration//GEN-END:variables
}

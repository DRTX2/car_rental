package views;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.mariadb.jdbc.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import models.State;

public class StateView
        extends javax.swing.JFrame {

    
    private class DBConn{
        private static String url="jdbc:mariadb://localhost:3307/car_rental";
        private static String user="root";
        private static String password="root";
        
        private static Connection con;
        
        private DBConn(){}
        
        public static Connection getConnectionInstance(){
            if(con==null){
                try {
                    con=(Connection) DriverManager.getConnection(url, user, password);
                } catch (SQLException ex) {
                    Logger.getLogger(StateView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return con;
        }
        
        public static boolean closeConnection(){
            if(con==null) return false;
            try {
                con.close();
                con=null;
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(StateView.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }

    private ArrayList<State> states;
    private Connection connection;      
    
    public StateView() {
        states=new ArrayList<>();
        connection=DBConn.getConnectionInstance();
        initComponents();
        configWindow();
    }
    
    private void configWindow(){
        setLocationRelativeTo(null);
        setTitle("States");
        configTable();
        configSaveBtn();
    }
    
    private void configTable(){
        DefaultTableModel model = (DefaultTableModel) getjTblStates().getModel();
        model.setColumnIdentifiers(new String[]{"Id", "Name"});
        model.setRowCount(0);
        
        getjTblStates().addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                handleTableClick(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleTableClick(e);
            }

        });
        
        configTableSubmenu();
    }
    
    private void handleTableClick(MouseEvent e){
        int selectedRow=getjTblStates().getSelectedRow();
        if(e.isPopupTrigger() && selectedRow != -1){
            //desplegar submenu
            getjPpmSubMenu().show(e.getComponent(), e.getX(), e.getY());
        }
    }
    
    
    private void configTableSubmenu(){
        
        
        JMenuItem editar= new JMenuItem("Editar");
        getjPpmSubMenu().add(editar);
        
        editar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = getjTblStates().getSelectedRow();
                if(n!=-1){
                    //q hay q hacer XD
                    //vamos a tomar ese id
                    String id= (String) getjTblStates().getValueAt(n, 0);
                    String name= (String) getjTblStates().getValueAt(n, 1);
                    //ahora toca ponerlos en los jtxt
                    getjTxtId().setText(id);
                    getjTxtName().setText(name);
                }
            }
        });
        
        JMenuItem eliminar= new JMenuItem("Eliminar");
        getjPpmSubMenu().add(eliminar);
        
        eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = getjTblStates().getSelectedRow();
                if(n!=-1){
                    //tomar el modelo y eliminar la row
                    DefaultTableModel model = (DefaultTableModel) getjTblStates().getModel();
                    model.removeRow(n);
                }
            }
        });
    }
    
    private void configSaveBtn(){
        getJbtnSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveState(e);
                
            }
        });
    }
    
    private void saveState(ActionEvent e){
        String name=getjTxtName().getText().trim();
        String idString=getjTxtId().getText().trim();
        State state=null;
        if(idString.isEmpty()){
            //just create a state without id
            state=new State(name);    
        }else{
            //  replace its data
            int id=Integer.parseInt(idString);
            state=new State(id, name);
        }
        states.add(state);
    }
    
    public boolean getStatesFromDB(){
        String sql="SELECT * FROM states";
        try{
            PreparedStatement st= connection.prepareStatement(sql);
            int executedQuerys= st.executeUpdate();
            if(executedQuerys==0) return false;
            while(st.){
            
            }
        }catch(Exception e){
            System.out.println("Something went bad at trying to get states from db: "+e.getMessage());
        }
        return false;
    }
    
    public boolean addStateToDB(State state){
        if(state==null || state.getName().isEmpty()) return false;
        
        String sql="INSERT INTO states (Des_Sta) VALUES( ? )";
        
        try{
            PreparedStatement st=connection.prepareStatement(sql);
            st.setString(1, state.getName());
            return st.executeUpdate() != 0;
        }catch(Exception e){
            System.out.println("Error. " + e.getMessage() );
        }
        
        return false;
    }
    
    public boolean editStateToDB(State state){
        if(state==null || state.getId()==null || state.getName().isEmpty() ) return false;
        
        String sql="UPDATE FROM states SET Des_Sta = ? WHERE Id_Sta = ? ";
        
        try{
            PreparedStatement st=connection.prepareStatement(sql);
            st.setString(1, state.getName());
            st.setInt(2, state.getId());

            return st.executeUpdate() != 0;
        }catch(Exception e){
            System.out.println("Error. " + e.getMessage() );
        }
        return false;
    }

    public JTable getjTblStates() {
        return jTblStates;
    }


    public JTextField getjTxtId() {
        return jTxtId;
    }

    public JTextField getjTxtName() {
        return jTxtName;
    }

    public JPopupMenu getjPpmSubMenu() {
        return jPpmSubMenu;
    }

    public JButton getJbtnSave() {
        return jbtnSave;
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPpmSubMenu = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblStates = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTxtId = new javax.swing.JTextField();
        jTxtName = new javax.swing.JTextField();
        jbtnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTblStates.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTblStates);

        jLabel1.setText("States");

        jLabel2.setText("Id");

        jLabel3.setText("Name");

        jTxtId.setEnabled(false);

        jbtnSave.setText("Save");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTxtId, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 247, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTxtName, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbtnSave)
                                .addGap(59, 59, 59))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(177, 177, 177)
                            .addComponent(jLabel1))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTxtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jbtnSave))
                .addGap(56, 56, 56)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(StateView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StateView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StateView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StateView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StateView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPpmSubMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTblStates;
    private javax.swing.JTextField jTxtId;
    private javax.swing.JTextField jTxtName;
    private javax.swing.JButton jbtnSave;
    // End of variables declaration//GEN-END:variables
}

package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.table.DefaultTableModel;
import models.Brand;
import org.mariadb.jdbc.Connection;
import views.BrandView;
import views.DBConnection;

public class BrandController extends Controller{
    
    public static BrandView view=new BrandView();
    
    public BrandController(){
        configWindow();
    }
    
    public void configWindow(){
        view.setLocationRelativeTo(null);
        view.setTitle("Brands");
        
        configBrandIdField();
        configTable();
        configPopUpMenu();
        addListennerToBtnAddBrand();
    }
    
    public void configBrandIdField(){
        view.getjLblId().setVisible(false);
        view.getjTxtIdBrand().setVisible(false);
    }
    
    public void configPopUpMenu(){
        addListennerToDeletePopUpBrand();
        addListennerToEditPopUpBrand();
    }
     
    public void addListennerToDeletePopUpBrand(){
        JMenuItem eliminar=new JMenuItem("Eliminar");
        view.getjPopupMenu1().add(eliminar);
        eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = view.getjTblBrands().getSelectedRow();
                if(n!=-1){
                    int brandId=(int) view.getjTblBrands().getValueAt(n,0);
                    if(!deleteBrand(brandId))   return;
                    System.out.println("Operation ends with succesfull");
                    DefaultTableModel model =(DefaultTableModel) view.getjTblBrands().getModel();
                    model.removeRow(n);
                }
            }
        });
    }
    
    public boolean deleteBrand(int brandId){
        String sql="DELETE FROM brands WHERE Id_Bra = ?";
        DBConnection db=new DBConnection();
        Connection c = db.getConnection();
        PreparedStatement ps;
        try{
            ps=c.prepareStatement(sql);
            ps.setInt(1, brandId);
            return ps.executeUpdate() !=0;
        }catch(Exception e){
            System.out.println("Error al eliminar la marca. "+e.getMessage());
        }
        return false;
    }
    
    private void configTable(){
        DefaultTableModel model =(DefaultTableModel) view.getjTblBrands().getModel();
        String[] columnNames={"Id","Name"};
        model.setColumnIdentifiers(columnNames);
        
        addListennerToTblBrand();
        showBrands();
    }
    
    public void addListennerToTblBrand(){
        view.getjTblBrands().addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.isPopupTrigger() && view.getjTblBrands().getSelectedRow()!=-1){
                    view.getjPopupMenu1().show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.isPopupTrigger() && view.getjTblBrands().getSelectedRow()!=-1){
                    view.getjPopupMenu1().show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    public void addListennerToEditPopUpBrand(){
        JMenuItem editar=new JMenuItem("Editar");
        view.getjPopupMenu1().add(editar);

        editar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = view.getjTblBrands().getSelectedRow();
                if(n!=-1){
                    //  show id data
                    view.getjLblId().setVisible(true);
                    view.getjTxtIdBrand().setVisible(true);
                    
                    int id = (int) view.getjTblBrands().getValueAt(n, 0);    
                    String name = (String) view.getjTblBrands().getValueAt(n, 1);
                    view.getjTxtNameBrand().setText(name);
                    String idLikeString=String.valueOf(id);
                    view.getjTxtIdBrand().setText(idLikeString);
                }
            }
        });
    }

    public void addListennerToBtnAddBrand(){
        view.getJbtnAddBrand().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //verifiar si todo esta ok
                String name = view.getjTxtNameBrand().getText().trim();
                Brand brand = null;
                if(view.getjLblId().isVisible()){
                    String input=view.getjTxtIdBrand().getText().trim();
                    int id = Integer.parseInt(input);
                    brand=new Brand(id, name);
                }else{
                    brand=new Brand(name);
                }
                if(safeBrand(brand) ){
                    showBrands();
                    view.getjTxtNameBrand().setText("");
                    view.getjTxtIdBrand().setText("");
                    System.out.println("Operation complete with succesfull");
                }else{
                    System.out.println("Something went wrong handling brands");
                }
            }
        });
    }
    
    public boolean safeBrand(Brand brand){
        boolean opeartionEndsWithSuccesfull;
        if(brand.getId()==null){
            opeartionEndsWithSuccesfull=addBrand(brand);
        }else{
            opeartionEndsWithSuccesfull=editBrand(brand);
        }
        return opeartionEndsWithSuccesfull;
    }
    
    public boolean editBrand(Brand brand){
        String sql="UPDATE brands SET Nam_Bra = ? WHERE Id_Bra = ?";
        PreparedStatement st=null;
        DBConnection db=new DBConnection();
        Connection con=db.getConnection();
        try{
            st=con.prepareStatement(sql);
            st.setString(1, brand.getName());
            st.setInt(2, brand.getId());
            return st.executeUpdate() != 0;
        }catch(Exception e){
            System.out.println("Error al editar. "+e.getMessage());
        }
        return false;
    }
    
    public boolean addBrand(Brand brand){
        String sql="INSERT INTO brands(Nam_Bra) VALUES (?)";
        PreparedStatement st=null;
        DBConnection db=new DBConnection();
        Connection con=db.getConnection();
        try{
            st=con.prepareStatement(sql);
            st.setString(1, brand.getName());
            return st.executeUpdate() != 0;
        }catch(Exception e){
            System.out.println("Error al añadir. "+e.getMessage());
        }
        return false;
    }
    
    private void showBrands(){
        ArrayList<Brand> brands=getBrandsInDB();
        if(brands.isEmpty()) return;
        DefaultTableModel model =(DefaultTableModel) view.getjTblBrands().getModel();
        model.setRowCount(0);
        for (Brand brand:brands) {
            Object[] row={brand.getId(),brand.getName()};
            model.addRow(row);
        }
    }
    
    public ArrayList<Brand> getBrandsInDB(){
        DBConnection db=new DBConnection();
        Connection conection= db.getConnection();
        String sql="SELECT * FROM brands";
        ArrayList<Brand> brands=new ArrayList<Brand>();
        try{
            Statement a=conection.createStatement();
            ResultSet set = a.executeQuery(sql);
            while(set.next()){
                int id=set.getInt("Id_Bra");
                String name=set.getString("Nam_Bra");
                Brand brand=new Brand(id,name);
                brands.add(brand);
            }
        }catch(Exception e){
            System.out.println("Error al consultar los datos. "+e.getMessage());
        }
        return brands;
    }
    
    public static void show(){
        view.setVisible(true);
    }
    
    public static void hide(){
        view.setVisible(false);
    }    
    
    public static void main(String[] args) {
        
    }
    
}

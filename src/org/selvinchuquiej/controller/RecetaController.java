package org.selvinchuquiej.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import groovy.sql.Sql;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.selvinchuquiej.bean.Doctor;
import org.selvinchuquiej.bean.Especialidad;
import org.selvinchuquiej.bean.Receta;
import org.selvinchuquiej.db.Conexion;
import org.selvinchuquiej.report.GenerarReporte;
import org.selvinchuquiej.system.Principal;

public class RecetaController implements Initializable{
    private enum operaciones {NUEVO, ELIMINAR, EDITAR, GUARDAR, CANCELAR, ACTUALIZAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Receta> listaReceta;
    private ObservableList<Doctor> listaDoctor;
    //private ObservableList<Especialidad> listaEspecialidad;
    private DatePicker fr;
    private Principal escenarioPrincipal;
    
    @FXML private TextField txtCodigoReceta;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private TableView tblRecetas;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colFechaReceta;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private GridPane grpFechas;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbNumeroColegiado.setDisable(true);
        cmbNumeroColegiado.setItems(getDoctor());
        
       
        
        fr = new DatePicker(Locale.ENGLISH);
        fr.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fr.getCalendarView().todayButtonTextProperty().set("Today");
        fr.getCalendarView().setShowWeeks(false);
        grpFechas.add(fr, 2, 2);
        fr.getStylesheets().add("/org/selvinchuquiej/resource/DatePicker.css");
        
    }
    
    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory<Receta, Date>("fechaReceta"));
        colNumeroColegiado.setCellValueFactory((new PropertyValueFactory<Receta, Integer>("numeroColegiado")));
    }

    public ObservableList<Receta> getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas}");
                ResultSet resultado = procedimiento.executeQuery();
                    while(resultado.next()){
                        lista.add(new Receta(resultado.getInt("codigoReceta"),
                                             resultado.getDate("fechaReceta"),
                                             resultado.getInt("numeroColegiado")));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaReceta = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores}");
                ResultSet resultado = procedimiento.executeQuery();
                    while(resultado.next()){
                        lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                                             resultado.getString("nombresDoctor"),
                                             resultado.getString("apellidosDoctor"),
                                             resultado.getString("telefonoContacto"),
                                             resultado.getInt("codigoEspecialidad")));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaDoctor = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        try{
            txtCodigoReceta.setText(String.valueOf(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            fr.selectedDateProperty().set(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getFechaReceta());
            cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Por favor selecciona un registro");
        }
    }
    
    public Doctor buscarDoctor(int numeroColegiado){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, numeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Doctor(registro.getInt("numeroColegiado"),
                                           registro.getString("nombresDoctor"),
                                           registro.getString("apellidosDoctor"),
                                           registro.getString("telefonoContacto"),
                                           registro.getInt("codigoEspecialidad"));
                }
        }catch(Exception e){
            e.printStackTrace();
        }           
        return resultado;
    }
    
    
    
    
    public void nuevo(){
       switch(tipoDeOperacion){
           case NINGUNO:
               limpiarControles();
               activarControler();
               btnNuevo.setText("Guardar");
               btnEliminar.setText("Cancelar");
               btnEditar.setDisable(true);
               btnReporte.setDisable(true);
               imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Guardar.png"));
               imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Cancelar.png"));
               tipoDeOperacion = operaciones.GUARDAR;  
               break;
               
           case GUARDAR:
               guardar();
               limpiarControles();
               desactivarControler();
               btnNuevo.setText("Nuevo");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Nuevo.png"));
               imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Eliminar.png"));
               tipoDeOperacion = operaciones.NINGUNO;
               cargarDatos();
               break;
       }
    }
 
    public void guardar(){
        Receta registro = new Receta();
        registro.setFechaReceta(fr.getSelectedDate());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarReceta(?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.setInt(2, registro.getNumeroColegiado());
            procedimiento.execute();
            listaReceta.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControler();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            default:
                if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                    int resp = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar esta Receta?", "Eliminar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(resp == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
                                procedimiento.setInt(1, ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                                procedimiento.execute();
                                listaReceta.remove(tblRecetas.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }catch(Exception e){
                                 e.printStackTrace();
                            }
                        }else{
                            limpiarControles();
                        } 
                }else{
                    JOptionPane.showMessageDialog(null, "Selecciona un registro para esta acción.");
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                    activarControler();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/selvinchuquiej/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/selvinchuquiej/images/Cancelar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    cmbNumeroColegiado.setDisable(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Selecciona un registro para esta acción.");
                }
                break; 
                
                
            case ACTUALIZAR: 
                actualizar();
                limpiarControles();
                desactivarControler();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/selvinchuquiej/images/Editar.png"));
                imgReporte.setImage(new Image("/org/selvinchuquiej/images/Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;             
        }
    }

    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarReceta(?,?)}");
            Receta registro = (Receta)tblRecetas.getSelectionModel().getSelectedItem();
            
            registro.setFechaReceta(fr.getSelectedDate());
            
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.execute();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                break;
            
            
            case ACTUALIZAR:
                limpiarControles();
                desactivarControler();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/selvinchuquiej/images/Editar.png"));
                imgReporte.setImage(new Image("/org/selvinchuquiej/images/Reporte.png"));
                tblRecetas.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("FondoReporte", GenerarReporte.class.getResource("/org/selvinchuquiej/images/FondoRecetas.png"));
        GenerarReporte.mostrarReporte("ReporteRecetas.jasper", "Reporte de Receta", parametros);
    }
    
    public void desactivarControler(){
        txtCodigoReceta.setEditable(false);
        cmbNumeroColegiado.setDisable(true);
    }
    
    public void activarControler(){
        cmbNumeroColegiado.setDisable(false);
        fr.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoReceta.clear();
        cmbNumeroColegiado.setValue("");
        tblRecetas.getSelectionModel().clearSelection();
        
        fr = null;
        fr = new DatePicker(Locale.ENGLISH);
        fr.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fr.getCalendarView().todayButtonTextProperty().set("Today");
        fr.getCalendarView().setShowWeeks(false);
        grpFechas.add(fr, 2, 2);
        fr.getStylesheets().add("/org/selvinchuquiej/resource/DatePicker.css");
    }


    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }


    
}
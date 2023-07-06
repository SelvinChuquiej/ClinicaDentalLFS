package org.selvinchuquiej.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.selvinchuquiej.bean.Medicamento;
import org.selvinchuquiej.db.Conexion;
import org.selvinchuquiej.report.GenerarReporte;
import org.selvinchuquiej.system.Principal;

public class MedicamentoController implements Initializable{

    private enum opereaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR ,ACTUALIZAR, CANCELAR, NINGUNO}
    private opereaciones tipoDeOperacion = opereaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Medicamento> listaMedicamentos; 
    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtNombreMedicamento;
    @FXML private TableView tblMedicamentos;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colNombreMedicamento;
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
    }

    public void cargarDatos(){
        tblMedicamentos.setItems(getMedicamentos());
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, Integer>("codigoMedicamento"));
        colNombreMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("nombreMedicamento"));
    }

    public ObservableList<Medicamento> getMedicamentos(){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>(); 
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"),
                                          resultado.getString("nombreMedicamento")));
            } 
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicamentos = FXCollections.observableArrayList(lista);
    }
     
    public void seleccionarElemento(){
        try{
            txtCodigoMedicamento.setText(String.valueOf(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
            txtNombreMedicamento.setText(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getNombreMedicamento());
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Por favor selecciona un registro");
        }
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnReporte.setDisable(true);
                btnEditar.setDisable(true);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Cancelar.png"));
                tipoDeOperacion = opereaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Nuevo.png"));
                imgEliminar.setImage(new Image("org/selvinchuquiej/images/Eliminar.png"));
                tipoDeOperacion = opereaciones.NINGUNO;
                break;     
        }        
    }
    
    public void guardar(){
        Medicamento registro = new Medicamento();
        registro.setNombreMedicamento(txtNombreMedicamento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarMedicamento(?)}");
            procedimiento.setString(1, registro.getNombreMedicamento());
            procedimiento.execute();
            listaMedicamentos.add(registro);
            cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }     
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Nuevo.png"));
                imgEliminar.setImage(new Image("org/selvinchuquiej/images/Eliminar.png"));
                tipoDeOperacion = opereaciones.NINGUNO;
                break;
             
            default:    
                if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                    int resp = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar este Medicamento?", "Eliminar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(resp == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");
                                procedimiento.setInt(1, ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                                procedimiento.execute();
                                listaMedicamentos.remove(tblMedicamentos.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        } else{
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
                if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/selvinchuquiej/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/selvinchuquiej/images/Cancelar.png"));
                    txtCodigoMedicamento.setEditable(false);
                    tipoDeOperacion = opereaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Selecciona un registro para esta acción.");
                }
                break;
                
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/selvinchuquiej/images/Editar.png"));
                imgReporte.setImage(new Image("/org/selvinchuquiej/images/Reporte.png"));
                cargarDatos();
                tipoDeOperacion = opereaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamento(?, ?)}");
            Medicamento registro = (Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem();
            registro.setNombreMedicamento(txtNombreMedicamento.getText());
            procedimiento.setInt(1, registro.getCodigoMedicamento());
            procedimiento.setString(2, registro.getNombreMedicamento());
            procedimiento.execute();
        }catch(Exception e){
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
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/selvinchuquiej/images/Editar.png"));
                imgReporte.setImage(new Image("/org/selvinchuquiej/images/Reporte.png"));
                tipoDeOperacion = opereaciones.NINGUNO;
                break;
        }
    }

    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoMedicamento", null);
        parametros.put("FondoReporte", GenerarReporte.class.getResource("/org/selvinchuquiej/images/FondoMedicamentos.png"));
        GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper", "Reporte de Medicamentos", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtNombreMedicamento.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtNombreMedicamento.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoMedicamento.clear();
        txtNombreMedicamento.clear();
        tblMedicamentos.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    
}














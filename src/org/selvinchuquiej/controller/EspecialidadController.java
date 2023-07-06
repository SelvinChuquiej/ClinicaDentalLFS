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
import org.selvinchuquiej.bean.Especialidad;
import org.selvinchuquiej.db.Conexion;
import org.selvinchuquiej.report.GenerarReporte;
import org.selvinchuquiej.system.Principal;

public class EspecialidadController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaEspecialidad;
    
    @FXML private TextField txtCodigoEspecialidad;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblEspecialidades;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colDescripcion;
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
        tblEspecialidades.setItems(getEspecialidad());
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Especialidad, String>("descripcion"));
    }
    
    public ObservableList<Especialidad> getEspecialidad(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
                while (resultado.next()) {
                    lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                               resultado.getString("descripcion")));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEspecialidad = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        try{
            txtCodigoEspecialidad.setText(String.valueOf(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
            txtDescripcion.setText(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getDescripcion());
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Por favor selecciona un registro");
        }
    }

    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
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
                desactivarController();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
   
    public void guardar(){
        Especialidad registro = new Especialidad();
        registro.setDescripcion(txtDescripcion.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEspecialidad(?)}");
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.execute();
                listaEspecialidad.add(registro);
                cargarDatos();
            }catch(Exception e){
                e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarController();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            default:
                if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                    int resp = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar esta Especialidad?", "Eliminar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(resp == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}");
                                procedimiento.setInt(1, ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                                procedimiento.execute();
                                listaEspecialidad.remove(tblEspecialidades.getSelectionModel().getSelectedIndex());
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
                if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/selvinchuquiej/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/selvinchuquiej/images/Cancelar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Selecciona un registro para esta acción.");
                }
                break;
                
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarController();
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
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEspecialidad(?,?)}");
            Especialidad registro = (Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.setString(2, registro.getDescripcion());
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
                desactivarController();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/selvinchuquiej/images/Editar.png"));
                imgReporte.setImage(new Image("/org/selvinchuquiej/images/Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEspecialidad", null);
        parametros.put("FondoReporte", GenerarReporte.class.getResource("/org/selvinchuquiej/images/FondoEspecialidades.png"));
        GenerarReporte.mostrarReporte("ReporteEspecialidades.jasper", "Reporte de Especialidades", parametros);
    }
    
    
    public void desactivarController(){
        txtCodigoEspecialidad.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEspecialidad.setEditable(false);
        txtDescripcion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEspecialidad.clear();
        txtDescripcion.clear();
        tblEspecialidades.getSelectionModel().clearSelection();
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

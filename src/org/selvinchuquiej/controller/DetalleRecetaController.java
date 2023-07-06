package org.selvinchuquiej.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.selvinchuquiej.bean.DetalleReceta;
import org.selvinchuquiej.bean.Medicamento;
import org.selvinchuquiej.bean.Receta;
import org.selvinchuquiej.db.Conexion;
import org.selvinchuquiej.report.GenerarReporte;

import org.selvinchuquiej.system.Principal;

public class DetalleRecetaController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {NUEVO, ELIMINAR, EDITAR, NINGUNO, CANCELAR, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleReceta> listaDetalle;
    private ObservableList<Receta> listReceta;
    private ObservableList<Medicamento> listaMedicamento;
    // private DatePicker fDetalleReceta;

    @FXML private TextField txtCodigoDetalleReceta;
    @FXML private TextField txtDosis;
    @FXML private ComboBox cmbCodigoReceta;
    @FXML private ComboBox cmbCodigoMedicamento;
    @FXML private TableView tblDetalleRecetas;
    @FXML private TableColumn colCodigoDetalleReceta;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colCodigoMedicamento;
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
        cmbCodigoReceta.setItems(getReceta());
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setItems(getMedicamento());
        cmbCodigoMedicamento.setDisable(true);
    }

    public void cargarDatos() {
        tblDetalleRecetas.setItems(getDetalleReceta());
        colCodigoDetalleReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoDetalleReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta, String>("dosis"));
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoReceta"));
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoMedicamento"));
    }

    public ObservableList<DetalleReceta> getDetalleReceta() {
        ArrayList<DetalleReceta> lista = new ArrayList<DetalleReceta>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetallesRecetas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new DetalleReceta(resultado.getInt("codigoDetalleReceta"),
                        resultado.getString("dosis"),
                        resultado.getInt("codigoReceta"),
                        resultado.getInt("codigoMedicamento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDetalle = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Receta> getReceta() {
        ArrayList<Receta> lista = new ArrayList<Receta>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Receta(resultado.getInt("codigoReceta"),
                        resultado.getDate("fechaReceta"),
                        resultado.getInt("numeroColegiado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listReceta = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Medicamento> getMedicamento() {
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"),
                        resultado.getString("nombreMedicamento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaMedicamento = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        try {
            txtCodigoDetalleReceta.setText(String.valueOf(((DetalleReceta) tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta()));
            txtDosis.setText(((DetalleReceta) tblDetalleRecetas.getSelectionModel().getSelectedItem()).getDosis());
            cmbCodigoMedicamento.getSelectionModel().select((buscarMedicamento(((DetalleReceta) tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoMedicamento())));
            cmbCodigoReceta.getSelectionModel().select((buscarReceta(((DetalleReceta) tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta())));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona un registro");
        }
    }

    public Medicamento buscarMedicamento(int codigoMedicamento) {
        Medicamento resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarMedicamento(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Medicamento(registro.getInt("codigoMedicamento"),
                        registro.getString("nombreMedicamento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Receta buscarReceta(int codigoReceta) {
        Receta resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarReceta(?)");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Receta(registro.getInt("codigoReceta"),
                        registro.getDate("fechaReceta"),
                        registro.getInt("numeroColegiado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControler();
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
                limpiarControler();
                desactivarControles();
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

    public void guardar() {
        DetalleReceta registro = new DetalleReceta();
        registro.setDosis(txtDosis.getText());
        registro.setCodigoMedicamento(((Medicamento) cmbCodigoMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
        registro.setCodigoReceta(((Receta) cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleReceta(?,?,?)}");
            procedimiento.setString(1, registro.getDosis());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.setInt(3, registro.getCodigoMedicamento());
            procedimiento.execute();
            listaDetalle.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControler();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            default:
                if(tblDetalleRecetas.getSelectionModel().getSelectedItem() != null){
                    int resp = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar este Detalle de la Receta?", "Eliminar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if( resp == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDetalleReceta(?)}");
                                procedimiento.setInt(1, ((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta());
                                procedimiento.execute();
                                listaDetalle.remove(tblDetalleRecetas.getSelectionModel().getSelectedIndex());
                                limpiarControler();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            limpiarControler();
                        }
                }else{
                    JOptionPane.showMessageDialog(null, "Selecciona un registro para esta acción.");
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblDetalleRecetas.getSelectionModel().getSelectedItem() != null){
                    activarControler();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/selvinchuquiej/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/selvinchuquiej/images/Cancelar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    cmbCodigoMedicamento.setDisable(true);
                    cmbCodigoReceta.setDisable(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Selecciona un registro para esta acción.");
                }
                break;
        
            case ACTUALIZAR:
                actualizar();
                limpiarControler();
                desactivarControles();
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDetalleReceta(?,?)}");
            DetalleReceta registro = (DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem(); 
            registro.setDosis(txtDosis.getText());
            procedimiento.setInt(1, registro.getCodigoDetalleReceta());
            procedimiento.setString(2, registro.getDosis());
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
                desactivarControles();
                limpiarControler();
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
        parametros.put("FondoReporte", GenerarReporte.class.getResource("/org/selvinchuquiej/images/FondoDetalleReceta.png"));
        GenerarReporte.mostrarReporte("ReporteDetalleReceta.jasper", "Reporte de Detalle Recetas", parametros);
    }
    
    public void desactivarControles() {
        txtCodigoDetalleReceta.setEditable(false);
        txtDosis.setEditable(false);
        cmbCodigoMedicamento.setDisable(true);
        cmbCodigoReceta.setDisable(true);
    }

    public void activarControler() {
        txtDosis.setEditable(true);
        cmbCodigoMedicamento.setDisable(false);
        cmbCodigoReceta.setDisable(false);
    }

    public void limpiarControler() {
        txtCodigoDetalleReceta.clear();
        txtDosis.clear();
        cmbCodigoMedicamento.setValue("");
        cmbCodigoReceta.setValue("");
        tblDetalleRecetas.getSelectionModel().clearSelection();
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

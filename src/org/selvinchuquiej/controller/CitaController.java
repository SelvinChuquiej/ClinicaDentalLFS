package org.selvinchuquiej.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
import org.selvinchuquiej.bean.Cita;
import org.selvinchuquiej.bean.Doctor;
import org.selvinchuquiej.bean.Paciente;
import org.selvinchuquiej.db.Conexion;
import org.selvinchuquiej.report.GenerarReporte;
import org.selvinchuquiej.system.Principal;

public class CitaController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR, ELIMINAR, EDITAR, REPORTE, CANCELAR, ACTUALIZAR}; 
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Cita> listaCita;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<Doctor> listaDoctor;
    private DatePicker fCita;
    
    @FXML private TextField txtCodigoCita;
    @FXML private TextField txtHoraCita;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtDescripCondActual;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private GridPane grpFechas;
    @FXML private TableView tblCitas;
    @FXML private TableColumn colCodigoCita;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colHoraCita;
    @FXML private TableColumn colTratamiento;
    @FXML private TableColumn colDescripCondActual;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colNumeroColegiado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPaciente.setItems(getPaciente());
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(true);
        
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFechas.add(fCita, 2, 1);
        fCita.getStylesheets().add("/org/selvinchuquiej/resource/DatePicker.css");
        
        
        
    }

    public void cargarDatos(){
        tblCitas.setItems(getCita());
        colCodigoCita.setCellValueFactory(new PropertyValueFactory<Cita, String>("codigoCita"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Cita, Date>("fechaCita"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<Cita, Time>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<Cita, String>("tratamiento"));
        colDescripCondActual.setCellValueFactory(new PropertyValueFactory<Cita, String>("descripCondActual"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoPaciente"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("numeroColegiado"));
    }
    
    public ObservableList<Cita> getCita(){
        ArrayList<Cita> lista = new ArrayList<Cita>();
            try{
                PreparedStatement procedimieto = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCitas()}");
                ResultSet resultado = procedimieto.executeQuery();
                    while(resultado.next()){
                        lista.add(new Cita(resultado.getInt("codigoCita"), 
                                           resultado.getDate("fechaCita"),
                                           resultado.getString("horaCita"), 
                                           resultado.getString("tratamiento"), 
                                           resultado.getString("descripCondActual"), 
                                           resultado.getInt("codigoPaciente"), 
                                           resultado.getInt("numeroColegiado")));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaCita = FXCollections.observableArrayList(lista);
    }
    
   public ObservableList<Paciente> getPaciente(){
       ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes()}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                                           resultado.getString("nombresPaciente"),
                                           resultado.getString("apellidosPaciente"),
                                           resultado.getString("sexo"),
                                           resultado.getDate("fechaNacimiento"),
                                           resultado.getString("direccionPaciente"),
                                           resultado.getString("telefonoPersonal"),
                                           resultado.getDate("fechaPrimeraVisita")));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableArrayList(lista);
   }
   
   public ObservableList<Doctor> getDoctor(){
       ArrayList<Doctor> lista = new ArrayList<Doctor>();
       try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores()}");
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
            txtCodigoCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
            fCita.selectedDateProperty().set(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
            txtHoraCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
            txtTratamiento.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento());
            txtDescripCondActual.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDescripCondActual());
            cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));   
            cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
        }catch(Exception e) {
           JOptionPane.showMessageDialog(null, "Por favor selecciona un registro");
        }
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
                procedimiento.setInt(1, codigoPaciente);
                ResultSet registro = procedimiento.executeQuery();
                    while(registro.next()){
                        resultado = new Paciente(registro.getInt("codigoPaciente"),
                                                 registro.getString("nombresPaciente"), 
                                                 registro.getString("apellidosPaciente"), 
                                                 registro.getString("sexo"), 
                                                 registro.getDate("fechaNacimiento"),
                                                 registro.getString("direccionPaciente"), 
                                                 registro.getString("telefonoPersonal"), 
                                                 registro.getDate("fechaPrimeraVisita"));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }       
        return resultado;
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
        switch(tipoDeOperaciones){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Cancelar.png"));
                tipoDeOperaciones = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Cita registro = new Cita();
        registro.setFechaCita(fCita.getSelectedDate());
        registro.setHoraCita(txtHoraCita.getText()); 
        registro.setTratamiento(txtTratamiento.getText());
        registro.setDescripCondActual(txtDescripCondActual.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCita(?, ?, ?, ?, ?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setString(2, registro.getHoraCita());
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescripCondActual());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCita.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperaciones){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/selvinchuquiej/images/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/selvinchuquiej/images/Eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;   
                
            default:
                if(tblCitas.getSelectionModel().getSelectedItem() != null){
                    int resp = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar esta Cita?", "Eliminar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
                        if(resp == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCita(?)}");
                                procedimiento.setInt(1, ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                                procedimiento.execute();
                                listaCita.remove(tblCitas.getSelectionModel().getSelectedIndex());
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
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblCitas.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image ("/org/selvinchuquiej/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/selvinchuquiej/images/Cancelar.png"));
                    txtCodigoCita.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    cmbCodigoPaciente.setDisable(true);
                    cmbNumeroColegiado.setDisable(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Selecciona un registro para esta acción.");
                }
                break;
                
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image ("/org/selvinchuquiej/images/Editar.png"));
                imgReporte.setImage(new Image("/org/selvinchuquiej/images/Reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCita(?, ?, ?, ?, ?)}");
            Cita registro = (Cita)tblCitas.getSelectionModel().getSelectedItem();
            
            registro.setFechaCita(fCita.getSelectedDate());
            registro.setHoraCita(txtHoraCita.getText());
            registro.setTratamiento(txtTratamiento.getText());
            registro.setDescripCondActual(txtDescripCondActual.getText());
            
            procedimiento.setInt(1, registro.getCodigoCita());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setString(3, registro.getHoraCita());
            procedimiento.setString(4, registro.getTratamiento());
            procedimiento.setString(5, registro.getDescripCondActual());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                imprimirReporte();
                break;
            
            case ACTUALIZAR:               
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image ("/org/selvinchuquiej/images/Editar.png"));
                imgReporte.setImage(new Image("/org/selvinchuquiej/images/Reporte.png"));
                tblCitas.getSelectionModel().clearSelection();
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("FondoReporte", GenerarReporte.class.getResource("/org/selvinchuquiej/images/FondoCitas.png"));
        GenerarReporte.mostrarReporte("ReporteCitas.jasper", "Reporte de Citas", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoCita.setEditable(false);
        txtDescripCondActual.setEditable(false);
        txtTratamiento.setEditable(false);
        txtHoraCita.setEditable(false);
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setDisable(true);
        fCita.setDisable(true);
    }
    
    public void activarControles(){
        txtDescripCondActual.setEditable(true);
        txtTratamiento.setEditable(true);
        txtHoraCita.setEditable(true);
        cmbCodigoPaciente.setDisable(false);
        cmbNumeroColegiado.setDisable(false);
        fCita.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCita.clear();
        txtHoraCita.clear();
        txtDescripCondActual.clear();
        txtTratamiento.clear();
        cmbCodigoPaciente.setValue("");
        cmbNumeroColegiado.setValue("");
        tblCitas.getSelectionModel().clearSelection();
        
        fCita = null;
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFechas.add(fCita, 2, 1);
        fCita.getStylesheets().add("/org/selvinchuquiej/resource/DatePicker.css");
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
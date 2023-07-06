/*
Nombre completo: Selvin Raúl Chuquiej Andrade
Código Técnico: IN5AM
Carné: 2018354
Fecha de creación: 05/04/2022
Modificaciones: 20/04/2022 | 25/04/2022 | 26/04/22 | 03/05/2022 | 10/05/2022 | 24/05/2022 | 27/05/2022
*/
package org.selvinchuquiej.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.application.Application; 
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;    
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.selvinchuquiej.controller.CitaController;
import org.selvinchuquiej.controller.DetalleRecetaController;
import org.selvinchuquiej.controller.DoctorController;
import org.selvinchuquiej.controller.EspecialidadController;
import org.selvinchuquiej.controller.LoginController;
import org.selvinchuquiej.controller.MedicamentoController;
import org.selvinchuquiej.controller.MenuPrincipalController;
import org.selvinchuquiej.controller.PacienteController;
import org.selvinchuquiej.controller.ProgramadorController;
import org.selvinchuquiej.controller.RecetaController;
import org.selvinchuquiej.controller.UsuarioController;

public class Principal extends Application {

    private final String PAQUETE_VISTA = "/org/selvinchuquiej/view/";
    private Stage escenarioPrincipal;
    private Scene escena;

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Clina Dental LFS");
        escenarioPrincipal.getIcons().add(new Image("/org/selvinchuquiej/images/Logo.png"));
        ventanaLogin();
        escenarioPrincipal.show();
    }

    public void ventanaLogin(){
        try{
            LoginController vistaLogin = (LoginController) cambiarEscena("LoginView.fxml", 619, 419);
            vistaLogin.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController vistaUsuario = (UsuarioController)cambiarEscena("UsuarioView.fxml", 757, 446);
            vistaUsuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipal() {
        try {
            MenuPrincipalController vistaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 897, 528);
            vistaMenu.setEscenarioPrincipal(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaProgramador(){
        try{
            ProgramadorController vistaProgramador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 669, 429);
            vistaProgramador.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }     
    }
    
    public void ventanaPacientes(){
        try{
            PacienteController vistaPacientes = (PacienteController) cambiarEscena("PacientesView.fxml", 1125, 604);
            vistaPacientes.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicamentos(){
        try{
            MedicamentoController vistaMedicamentos = (MedicamentoController) cambiarEscena("MedicamentosView.fxml", 852, 574);
            vistaMedicamentos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEspecialidades(){
        try{
            EspecialidadController vistaEspecialidades = (EspecialidadController) cambiarEscena("EspecialidadesView.fxml", 923, 570);
            vistaEspecialidades.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDoctores(){
        try{
            DoctorController vistDoctores = (DoctorController) cambiarEscena("DoctoresView.fxml", 1019, 616);
            vistDoctores.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaReceta(){
        try{
            RecetaController vistaReceta = (RecetaController) cambiarEscena("RecetasView.fxml", 837, 568);
            vistaReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDetalleReceta(){
        try{
            DetalleRecetaController vistaDetalleReceta = (DetalleRecetaController) cambiarEscena("DetalleRecetaView.fxml", 854, 613);
            vistaDetalleReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCita(){
        try{
            CitaController vistaCita = (CitaController) cambiarEscena("CitaView.fxml", 1033, 583);
            vistaCita.setEscenarioPrincipal(this); 
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto)throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+(fxml));
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+(fxml)));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }
    
}
    
    
    
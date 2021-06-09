/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appinformes;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import static javafx.application.Platform.exit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;



/**
 *
 * @author Aldobg
 */
public class AppinformesController implements Initializable {

    @FXML
    private MenuItem listadoFacturaMenuItem;
    @FXML
    private MenuItem ventasTotalesMenuItem;
    @FXML
    private MenuItem FacturasporClientesMenuItem;
    @FXML
    private MenuItem SubinformeListadoFacturasMenuItem;
    @FXML
    private MenuItem salirMenuItem;

    public static Connection conexion = null;
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        conectaBD();
        
        //EVENTOS
        this.salirMenuItem.setOnAction(e -> {
            System.exit(0);
        });

        this.listadoFacturaMenuItem.setOnAction(e -> {
            try {
                //carga de archivo
                JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("/recursos/Facturas.jasper"));
 
                //llenado del informe
                JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,
                        null, conexion);
                
                //visor del informe
                JasperViewer.viewReport(jp);
            } catch (JRException ex) {
                System.out.println("Error al recuperar el jasper");
                JOptionPane.showMessageDialog(null, ex);
            }
        });

        this.ventasTotalesMenuItem.setOnAction(e -> {
            try {
                //carga de archivo
                JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("/recursos/Pedidos_Meses.jasper"));
 
                //llenado del informe
                JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,
                        null, conexion);
                
                //visor del informe
                JasperViewer.viewReport(jp);
            } catch (JRException ex) {
                System.out.println("Error al recuperar el jasper");
                JOptionPane.showMessageDialog(null, ex);
            }
        });

        this.FacturasporClientesMenuItem.setOnAction(e -> {
            
            // VENTANA
            HBox root = new HBox();
            Label lbIdUsuario = new Label("ID USUARIO:");
            TextField tfIdUsuario = new TextField();
            Button btnEnviar = new Button("Enviar");
              
            root.getChildren().addAll(lbIdUsuario,tfIdUsuario,btnEnviar);
            
            
            root.setPrefSize(300, 50);
            root.setSpacing(10);
            Parent content = root;
            // escena
            Scene scene = new Scene(content);
            Stage window = new Stage();
            window.setScene(scene);
            //LEVANTAR VENTANA
            window.show();
            
            /*EVENTO BOTON*/           
            btnEnviar.setOnAction(f -> {
                generaInformeCliente(tfIdUsuario);
                
                window.close();
            });
            
     
        });

        this.SubinformeListadoFacturasMenuItem.setOnAction(e -> {
            
            generaInformeSubinforme();
        });
        
     
    }// FIN INIT

    
    //CONEXION BD
    public void conectaBD() {
        //Establecemos conexión con la BD
        String baseDatos = "jdbc:hsqldb:hsql://localhost:9001/xdb";
        String usuario = "sa";
        String clave = "";
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            conexion = DriverManager.getConnection(baseDatos, usuario, clave);
            
            System.out.println("Conexion establecida con la base de datos");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Fallo al cargar JDBC");
            System.exit(1);
        } catch (SQLException sqle) {
            System.err.println("No se pudo conectar a BD");
            System.exit(1);
        } catch (java.lang.InstantiationException sqlex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        } catch (Exception ex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }
    }
    
    //parametros
    public void generaInformeCliente(TextField tintro) {
        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("/recursos/Facturas_Cliente.jasper"));
            //Map de parámetros
            Map parametros = new HashMap();
            int idCliente = Integer.valueOf(tintro.getText());
            parametros.put("idCliente", idCliente);

            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,
                    parametros, conexion);
            JasperViewer.viewReport(jp,false);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    //parametros
    public void generaInformeSubinforme() {
        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("/recursos/Facturas_conSubinforme.jasper"));
            JasperReport jrSub = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("/recursos/Factura_subreport1.jasper"));
            
            //Map de parámetros
            Map parametros = new HashMap();
        
            parametros.put("subreportParam", jrSub);

            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,
                    parametros, conexion);
            
            
            
            JasperViewer.viewReport(jp,false);
        } catch (JRException ex) {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    

}// FIN CLASE

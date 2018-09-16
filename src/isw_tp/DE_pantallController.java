/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isw_tp;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author aleex
 */
public class DE_pantallController implements Initializable {
    //Se harckodean detalles pedidos para ser ingresados en la tabla
    //En el programa final deberian ser los incluidos en el carrito de compras
    DE_DetallePedido dp1 = new DE_DetallePedido(new DE_Articulo("Vazo termico 500ml", 200), 5);
    DE_DetallePedido dp2 = new DE_DetallePedido(new DE_Articulo("Termo 1L", 100), 1);
    DE_DetallePedido dp3 = new DE_DetallePedido(new DE_Articulo("Guantes Negros Lana", 80), 3);
    DE_DetallePedido dp4 = new DE_DetallePedido(new DE_Articulo("Pasamontaña Negro", 100), 2);
    
    @FXML
    private TableView<DE_DetallePedido> tbPedidos;
    @FXML
    private DatePicker fechaEntrega;
    @FXML
    private CheckBox chkEfectivo;
    @FXML
    private CheckBox chkTarjeta;
    @FXML
    private TextField txtEfectivoCantidad;
    @FXML
    private TextField txtTitularTarjeta;
    @FXML
    private TextField txtNroTarjeta;
    @FXML
    private TextField txtCodTarjeta;
    @FXML
    private TextField txtVencimientoTarjeta;
    @FXML
    private TextField txtCalle;
    @FXML
    private TextField txtCalleNro;
    @FXML
    private TextField txtPiso;
    @FXML
    private TextField txtDepto;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TableColumn<DE_DetallePedido, String> columnArticulo;
    @FXML
    private TableColumn<DE_DetallePedido, Integer> columnCantidad;
    @FXML
    private TableColumn<DE_DetallePedido, Float> columnSubtotal;
    @FXML
    private Pane paneEfectivo;
    @FXML
    private Pane paneTarjeta;
    @FXML
    private ComboBox<String> cmbHoraEntrega;
    @FXML
    private Label FechaHoraActual;
    @FXML
    private Label lblTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        columnArticulo.setCellValueFactory(new PropertyValueFactory("articulo"));
        columnCantidad.setCellValueFactory(new PropertyValueFactory("cantidad"));
        columnSubtotal.setCellValueFactory(new PropertyValueFactory("subtotal"));
        ObservableList<DE_DetallePedido> detallesPedidos = FXCollections.observableArrayList();
        detallesPedidos.add(dp1);
        detallesPedidos.add(dp2);
        detallesPedidos.add(dp3);
        detallesPedidos.add(dp4);
        tbPedidos.setItems(detallesPedidos);
        cmbHoraEntrega.getItems().addAll( 
            "Lo antes posible",
            "9:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "15:00",
            "16:00",
            "17:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00"
            );
        cmbHoraEntrega.setValue("Lo antes posible");
        
    }    

    @FXML
    private void changeStateEfectivo(ActionEvent event) {
        if (chkEfectivo.isSelected()){
            chkTarjeta.setSelected(false);
            paneEfectivo.setVisible(true);
            paneTarjeta.setVisible(false);
        }
        else {
            paneEfectivo.setVisible(false);
        }
        
    }

    @FXML
    private void changeStateTarjeta(ActionEvent event) {
        if (chkTarjeta.isSelected()){
            chkEfectivo.setSelected(false);
            paneTarjeta.setVisible(true);
            paneEfectivo.setVisible(false);
        }
        else {
            paneTarjeta.setVisible(false);
        }
    }
    
    private boolean validateCard(){
        return false;
    
    }
    private boolean validateData(){
        if (txtCalle.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Debe completar la calle donde se entregará el pedido");
            a.showAndWait();
            return false;
        }
        if (txtCalleNro.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Debe completar el numero de calle donde se entregará el pedido");
            a.showAndWait();
            return false;
        }
        if (!txtDepto.getText().isEmpty() && txtPiso.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Debe completar el piso del dpto");
            a.showAndWait();
            return false;
        }
        if(fechaEntrega.getValue().compareTo(LocalDate.now()) < 0){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("La fecha de entrega no debe ser anterior a la actual");
            a.showAndWait();
            return false;
        }
        if(fechaEntrega.getValue().compareTo(LocalDate.now()) == 0){
            Date fechaHora = new Date();
            if(Integer.parseInt(cmbHoraEntrega.getValue()) <= fechaHora.getHours()){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("La hora de entrega no debe ser menor que la hora actual");
                a.showAndWait();
                return false;
            }
        }
        if(!chkEfectivo.isSelected() && !chkTarjeta.isSelected()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Debe seleccionar un medio de pago");
            a.showAndWait();
            return false;
        }
        
        return true;
        
    
    }
    private boolean validateCash(){
        return false;
        
    }

    @FXML
    private void confirmation(ActionEvent event) {
        if (this.validateData()){
            if(this.validateCash() || this.validateCard()){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Exito");
                a.setHeaderText("Su pedido se ha realizado correctamenta");
                a.showAndWait();
            }
        }
        
    }
}

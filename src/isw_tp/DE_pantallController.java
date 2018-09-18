package isw_tp;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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

    private float total;
    
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
    private Label lblTotal;
    @FXML
    private Label lblVuelto;
    @FXML
    private Label fechaHoraActual;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
            harcodeo de articulos. Cuando el sistema esté completo 
            estos deberían ser los artículos seleccionados en el carrito de 
            compras. Además se cargan los horarios posibles de entrega, se supone
            que sólo se realizan entregas entre las 9 hs y las 21 hs.
        */

        columnArticulo.setCellValueFactory(new PropertyValueFactory("articulo"));
        columnCantidad.setCellValueFactory(new PropertyValueFactory("cantidad"));
        columnSubtotal.setCellValueFactory(new PropertyValueFactory("subtotal"));
        ObservableList<DE_DetallePedido> pedido = FXCollections.observableArrayList();
        pedido.add(dp1);
        pedido.add(dp2);
        pedido.add(dp3);
        pedido.add(dp4);
        tbPedidos.setItems(pedido);
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
        total = 0;
        for (int i = 0; i < pedido.size() ; i++) {
            total += pedido.get(i).getSubtotal();
        }
        lblTotal.setText("Total: " + total + "$");
        fechaHoraActual.setText(fechaHoraActual.getText().toString() + LocalDate.now().toString());
        fechaEntrega.setValue(LocalDate.now());
    }    

    
    
    @FXML
    private void changeStateEfectivo(ActionEvent event) {
        /*
            habilita los campos necesarios del pago con efectivo 
        */

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
        /*
            habilita los campos necesarios del pago con tarjeta 
        */
        
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
        /*
            validación de datos de tarjeta
            valida sólo si está marcada este medio de pago
        */

        if(!this.chkTarjeta.isSelected()){
            // en caso de no ser este medio de pago evita hacer todas las validaciones
            return false;
        }
        
        // valida que el número de tarjeta realmente sea un número
        double nroTarjeta = 0;
        try {
            nroTarjeta = Double.valueOf(txtNroTarjeta.getText());
        }
        catch(NumberFormatException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Formato de número de tarjeta invalido");
            a.showAndWait();
            return false;
        }
        
        // que el número de tarjeta no sea número negativo
        if (nroTarjeta <= 0 || txtNroTarjeta.getText().length() != 12) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Número de tarjeta invalido");
            a.showAndWait();
            return false;
        }
        
        
        // valida que la tarjeta no este vencida
        if (txtVencimientoTarjeta.getText().toString().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Fecha vencimiento no ingresada");
            a.showAndWait();
            return false;
        }

        Date fechaVencimiento = this.ParseFecha(txtVencimientoTarjeta.getText().trim());

        if (fechaVencimiento == null) return false;

        if (fechaVencimiento.before(Date.from(Instant.now()))) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Tarjeta vencida");
            a.showAndWait();
            return false;
        }


        // que se haya cargado el titular de la tarjeta
        if (txtTitularTarjeta.getText().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Falta cargar el nombre impreso en la tarjeta");
            a.showAndWait();
            return false;
        }
        
        // valida que el código de seguridad (número de 3 o 4 dígitos)
        if (!(txtCodTarjeta.getText().length() == 3) && !(txtCodTarjeta.getText().length() == 4)) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Código de seguridad de tarjeta invalido");
            a.showAndWait();
            return false;
        }
        else{
            // que realmente sea un número
            try {
                Integer.parseInt(txtCodTarjeta.getText());
            }
            catch(NumberFormatException e){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Código de seguridad de tarjeta invalido");
                a.showAndWait();
                return false;
            }
        }
        
        return true;
    }
    
    
    
    private Date ParseFecha(String fecha)
    {
        /*
            método que transforma un string tipo "MM/AAAA" al tipo Date
            utilizado para validar que la tarjeta no esté vencida
        */
        
        StringTokenizer st = new StringTokenizer(fecha,"/");
        int mes = 0;
        mes = Integer.parseInt((String) st.nextElement());
        
        // valida que se ha ingresado un mes válido
        if(mes > 12 || mes < 1){ 
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Fecha de vencimiento no válida");
            a.showAndWait();
            return null;
        
        }
        
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        
        // conversion a tipo date
        try {            
            date = formato.parse("01/"+fecha);        
        } 
        catch (ParseException ex) 
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Formato de fecha de vencimiento incorrecto");
            a.showAndWait();
        }
        return date;
    }


    
    private boolean validateData(){
        /*
            validación de los datos necesarios del pedido. Se los valida
            independientemente del medio de pago seleccionado
        */
        
        // validacion de calle de entrega del pedido
        if (txtCalle.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Debe completar la calle donde se entregará el pedido");
            a.showAndWait();
            return false;
        }
        
        // validación de nro de calle de entrega del pedido
        if (txtCalleNro.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Debe completar el numero de calle donde se entregará el pedido");
            a.showAndWait();
            return false;
        }
        
        // en caso de cargar el depto (ej: a) debe cargar en qué piso está el
        // mismo. Permite cargar solamente el piso SIN cargar el depto, pero no
        // viceversa
        if (!txtDepto.getText().isEmpty() && txtPiso.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Debe completar el piso del dpto");
            a.showAndWait();
            return false;
        }
        
        // valida que la fecha de entrega seleccionada sea igual o posterior
        // a la actual
        if(fechaEntrega.getValue().isBefore(LocalDate.now())){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("La fecha de entrega no debe ser anterior a la actual");
            a.showAndWait();
            return false;
        }
        
        // en caso de seleccionar la entrega para la fecha actual que la hora
        // de entrega seleccionada sea "Lo antes posible" o posterior a la 
        // hora actual
        if(fechaEntrega.getValue().isEqual(LocalDate.now()) && cmbHoraEntrega.getValue() != "Lo antes posible"){
            Date fechaHora = new Date();
            StringTokenizer st = new StringTokenizer(cmbHoraEntrega.getValue().toString(),":");
            int horaEntrega = 0;
            horaEntrega = Integer.parseInt((String) st.nextElement());
            if(horaEntrega <= fechaHora.getHours()){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("La hora de entrega no debe ser menor que la hora actual");
                a.showAndWait();
                return false;
            }
        }
        
        // valida que se ha seleccionado un medio de pago
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
        /*
            valida el campo necesario para el medio de pago en efectivo
        */
        
        float cantidad = 0; //útilizado como bandera para comprobar el monto positvo
        
        // en caso de no estar seleccionado este medio de pago no valida
        if (!chkEfectivo.isSelected()) {
            return false;
        }
        
        // valida que se ha cargado la cantidad con que se pagará
        if (txtEfectivoCantidad.getText().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Debe indicar con cuanto pagará");
            a.showAndWait();
            return false;
        }
        // valida que realmente se ha ingresado un número positivo
        else{
            try{
                cantidad = Float.valueOf(txtEfectivoCantidad.getText().trim());
                if(cantidad < total){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Debe ingresar un dinero mayor o igual al total a pagar");
                    a.showAndWait();
                    return false;
                }
            }
            catch(NumberFormatException e){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Cantidad con la que pagará invalida");
                a.showAndWait();
                return false;
            }
        }
        
        // valida que no sea una cantidad negativa
        if (cantidad <= 0) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("El monto con el cual pagará debe ser positivo");
            a.showAndWait();
            return false;
        }
        
        return true;
    }

    @FXML
    private void confirmation(ActionEvent event) {
        /*
            desencadena las validaciones de los datos
        */
        if (this.validateData()){
            if(this.validateCash() || this.validateCard()){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Exito");
                a.setHeaderText("Su pedido se ha realizado correctamente");
                a.showAndWait();
                Stage stage = (Stage) btnConfirmar.getScene().getWindow();
                stage.close();
            }
        }
        
    }


    @FXML
    private void change(ActionEvent event) {
        /*
            calcula el vuelto en caso de seleccionar medio de pago efectivo
        */
        if (txtEfectivoCantidad.getText().isEmpty()) return;
        float pago = Float.valueOf(txtEfectivoCantidad.getText());
        double vuelto = pago - this.total;
        if (vuelto <= 0) return;
        lblVuelto.setText("Su vuelto: " + String.valueOf(vuelto) + "$");
    }

    
    @FXML
    private void close(ActionEvent event) {
        /*
            cierra la ventana
        */
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    
}

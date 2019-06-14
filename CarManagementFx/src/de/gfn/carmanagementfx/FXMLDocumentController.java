package de.gfn.carmanagementfx;

import de.gfn.carmanagement.entity.Customer;
import de.gfn.carmanagement.mapper.CustomerMapper;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField txtFirstname;

    @FXML
    private TextField txtLastname;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private TableView<Customer> tblCustomers;

    private CustomerMapper mapper = new CustomerMapper();

    //Aktion, wenn User "speichern" drückt
    //Input für Perönliche Daten
    @FXML
    private void saveAction(ActionEvent event) {

        String firstname = txtFirstname.getText();
        String lastname = txtLastname.getText();
        String email = txtEmail.getText();

        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();
        if(customer == null) {
            customer = new Customer();
        }
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setEmail(email);

        try {
            mapper.save(customer);
            refreshTable();
            resetForm();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    //Aktion, wenn User "Bearbeiten" drückt
    @FXML
    private void loadFromTableAction(ActionEvent event) {
        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();
        txtFirstname.setText(customer.getFirstname());
        txtLastname.setText(customer.getLastname());
        txtEmail.setText(customer.getEmail());
    }
    
    //Aktion, wenn User "neu" drückt
    @FXML
    private void newAction(ActionEvent event) {
        resetForm();
        tblCustomers.getSelectionModel().select(null);
    }

    //Aktion, wenn User "löschen" drückt
    @FXML
    private void deleteAction(ActionEvent event) {

        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();

        try {
            mapper.delete(customer);
            refreshTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Funktionen die von den oberen aufergufen werden
    private void resetForm() {
        txtFirstname.setText("");
        txtLastname.setText("");
        txtEmail.setText("");
    }

    private void refreshTable() {
        try {
            List<Customer> list = mapper.find();
            tblCustomers.setItems(FXCollections.observableList(list));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshTable();
    }
}

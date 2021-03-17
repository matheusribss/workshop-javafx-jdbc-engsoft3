package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable  {
	
	//Injentando a Classe Departamento
	//entidade relacionada a esse formulario
	private Department entity;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	
	//IMPLEMENTO DO METODO SET DO ENTITY 
	//AGORA O CONTROLADOR TEM UMA INSTANCIA DO DEPARTAMENTO
	public void setDepartment (Department entity) {
		this.entity = entity;
	}
	
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
	}
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	//controlador basico
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entidade estava nula");
		}
		
		//A CAIXA DE TEXTO É STRING POR ISSO TEM Q CONVERTER O ID (INTERGER) PARA STRING COM String.valueOF
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

}

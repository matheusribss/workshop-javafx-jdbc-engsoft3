package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable  {
	
	//Injentando a Classe Departamento
	//entidade relacionada a esse formulario
	private Department entity;
	
	//DEPENDENCIA
	private DepartmentService service;
	
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
	
	public void setDepartmentService (DepartmentService service) {
		this.service = service;
	}
	
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		//System.out.println("onBtSaveAction");
		if (entity == null) {
			throw new IllegalStateException ("Entidade estava nula");
		}
		if (service == null) {
			throw new IllegalStateException ("Serviçõ estava nulo");
		}
		
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Error na hora de salvar", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private Department getFormData() {
		Department obj = new Department();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		//System.out.println("onBtCancelAction");
		
		Utils.currentStage(event).close();
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

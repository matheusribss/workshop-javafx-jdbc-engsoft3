package gui;


import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable  {
	
	//Injentando a Classe Departamento
	//entidade relacionada a esse formulario
	private Seller entity;
	
	//DEPENDENCIA
	private SellerService service;
	
	//PERMITIR OUTROS OBEJTOS SE INSCREVEREM NESSA LISTA E RECEBEREM O EVENTO
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	
	//IMPLEMENTO DO METODO SET DO ENTITY 
	//AGORA O CONTROLADOR TEM UMA INSTANCIA DO DEPARTAMENTO
	public void setSeller (Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService (SellerService service) {
		this.service = service;
	}
	
	public void subscribeDateChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		//System.out.println("onBtSaveAction");
		if (entity == null) {
			throw new IllegalStateException ("Entidade estava nula");
		}
		if (service == null) {
			throw new IllegalStateException ("Servi�� estava nulo");
		}
		
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			//EMITIR EVENTO
			Utils.currentStage(event).close();
		}
		catch(ValidationException e){
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error na hora de salvar", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	//EMITINDO EVENTOS
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChange();
		}
		
	}

	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Erro na Valida��o");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "O campo nao pode ser Vazio");
		}
		obj.setName(txtName.getText());
		
		if (exception.getErrors().size()>0) {
			throw exception;
		}
		
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
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entidade estava nula");
		}
		
		//A CAIXA DE TEXTO � STRING POR ISSO TEM Q CONVERTER O ID (INTERGER) PARA STRING COM String.valueOF
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if(entity.getBirthDate()!= null) {
		dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(),ZoneId.systemDefault()));
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		//Set = Outra cole��o ou Conjunto
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}

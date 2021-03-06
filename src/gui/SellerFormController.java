//package gui;
//
//import java.net.URL;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.ResourceBundle;
//import java.util.Set;
//
//import db.DbException;
//import gui.listeners.DataChangeListener;
//import gui.util.Alerts;
//import gui.util.Constraints;
//import gui.util.Utils;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListCell;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextField;
//import javafx.util.Callback;
//import model.entities.Department;
//import model.entities.Seller;
//import model.exceptions.ValidationException;
//import model.services.DepartmentService;
//import model.services.SellerService;
//
//public class SellerFormController implements Initializable {
//
//	// Injentando a Classe Departamento
//	// entidade relacionada a esse formulario
//	private Seller entity;
//
//	// DEPENDENCIA
//	private SellerService service;
//
//	private DepartmentService departmentService;
//
//	// PERMITIR OUTROS OBEJTOS SE INSCREVEREM NESSA LISTA E RECEBEREM O EVENTO
//	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
//
//	@FXML
//	private TextField txtId;
//
//	@FXML
//	private TextField txtEmail;
//
//	@FXML
//	private DatePicker dpBirthDate;
//
//	@FXML
//	private TextField txtBaseSalary;
//
//	@FXML
//	private TextField txtName;
//
//	@FXML
//	private ComboBox<Department> comboBoxDepartment;
//
//	@FXML
//	private Label labelErrorName;
//
//	@FXML
//	private Label labelErrorEmail;
//
//	@FXML
//	private Label labelErrorBirthDate;
//
//	@FXML
//	private Label labelErrorBaseSalary;
//
//	@FXML
//	private Button btSave;
//
//	@FXML
//	private Button btCancel;
//
//	private ObservableList<Department> obsList;
//
//	// IMPLEMENTO DO METODO SET DO ENTITY
//	// AGORA O CONTROLADOR TEM UMA INSTANCIA DO DEPARTAMENTO
//	public void setSeller(Seller entity) {
//		this.entity = entity;
//	}
//
//	// ele vai injetar dois services agora , de uma vez: o SellerService e o
//	// DepartmentService
//	public void setServices(SellerService service, DepartmentService departmentService) {
//		this.service = service;
//		this.departmentService = departmentService;
//	}
//
//	public void subscribeDataChangeListener(DataChangeListener listener) {
//		dataChangeListeners.add(listener);
//	}
//
//	@FXML
//	public void onBtSaveAction(ActionEvent event) {
//		// System.out.println("onBtSaveAction");
//		if (entity == null) {
//			throw new IllegalStateException("Entidade estava nula");
//		}
//		if (service == null) {
//			throw new IllegalStateException("Servi?? estava nulo");
//		}
//
//		try {
//			entity = getFormData();
//			service.saveOrUpdate(entity);
//			notifyDataChangeListeners();
//			// EMITIR EVENTO
//			Utils.currentStage(event).close();
//		} catch (ValidationException e) {
//			setErrorMessages(e.getErrors());
//		} catch (DbException e) {
//			e.printStackTrace();
//			Alerts.showAlert("Error na hora de salvar", null, e.getMessage(), AlertType.ERROR);
//		}
//
//	}
//
//	// EMITINDO EVENTOS
//	private void notifyDataChangeListeners() {
//		for (DataChangeListener listener : dataChangeListeners) {
//			listener.onDataChange();
//		}
//
//	}
//
//	//ELE PEGA OS DADOS QUE FORAM  PREENCHIDOS NO FORMULARIO E ELE CARREGA UM OBJETO COM ESSES DADOS E RETORNANDO O OBJETO NO FINAL
//	private Seller getFormData() {
//		Seller obj = new Seller();
//
//		ValidationException exception = new ValidationException("Erro na Valida??o");
//
//		obj.setId(Utils.tryParseToInt(txtId.getText()));
//
//		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
//			exception.addError("name", "O campo nao pode ser Vazio");
//		}
//		obj.setName(txtName.getText());
//		
//
//		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
//			exception.addError("email", "O campo nao pode ser Vazio");
//		}
//		obj.setName(txtEmail.getText());
//		
//		//pegando o valor do datepicker
//		//.atStartOfDay = Converte a data do pc do usuario para uma data independente de Localidade
//		if (dpBirthDate.getValue() == null) {
//			exception.addError("birthDate", "O campo nao pode ser Vazio");
//		}
//		else {
//			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
//			obj.setBirthDate(Date.from(instant));
//		}
//		
//		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
//			exception.addError("baseSalary", "O campo nao pode ser Vazio");
//		}
//		//tem que converter o string para double
//		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
//		
//		obj.setDepartment(comboBoxDepartment.getValue());
//		
//		if (exception.getErrors().size() > 0) {
//			throw exception;
//		}
//
//		return obj;
//	}
//
//	@FXML
//	public void onBtCancelAction(ActionEvent event) {
//		// System.out.println("onBtCancelAction");
//
//		Utils.currentStage(event).close();
//	}
//
//	@Override
//	public void initialize(URL url, ResourceBundle rb) {
//		initializeNodes();
//
//	}
//
//	// controlador basico
//	private void initializeNodes() {
//		Constraints.setTextFieldInteger(txtId);
//		Constraints.setTextFieldMaxLength(txtName, 70);
//		Constraints.setTextFieldDouble(txtBaseSalary);
//		Constraints.setTextFieldMaxLength(txtEmail, 60);
//		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
//		
//		initializeComboBoxDepartment();
//	}
//
//	//METODO QUE PEGAS OS DADOS DO MEU OBEJTO NO CASO VENDEDOR E PREENCHE O FORMULARIO COM ESSES DADOS
//	public void updateFormData() {
//		if (entity == null) {
//			throw new IllegalStateException("Entidade estava nula");
//		}
//
//		// A CAIXA DE TEXTO ? STRING POR ISSO TEM Q CONVERTER O ID (INTERGER) PARA
//		// STRING COM String.valueOF
//		txtId.setText(String.valueOf(entity.getId()));
//		txtName.setText(entity.getName());
//		txtEmail.setText(entity.getEmail());
//		Locale.setDefault(Locale.US);
//		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
//		if (entity.getBirthDate() != null) {
//			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
//		}
//		
//		//SO VAI FUNCIONAR SE O DEPARTAMENTO NAO FOR NULO, SE FOR UM VENDEDOR NOVO E NAO TEM DEPARTAMENTO ASSOCIADO AO VENDEDOR
//		if(entity.getDepartment()== null) {
//			comboBoxDepartment.getSelectionModel().selectFirst();
//		}
//		comboBoxDepartment.setValue(entity.getDepartment());
//		
//		
//	}
//
//	public void loadAssociatedObjects() {
//
//		// SE O CARA ESQUECE DE FAZER A INJE??O DE DEPENDENCIA
//		if (departmentService == null) {
//			throw new IllegalStateException("DepartmentService estava nulo");
//		}
//		List<Department> list = departmentService.findAll();
//
//		// APRENDIDO NA AULA DE COMBOBOX
//		obsList = FXCollections.observableArrayList(list);
//		comboBoxDepartment.setItems(obsList);
//	}
//
//	private void setErrorMessages(Map<String, String> errors) {
//		// Set = Outra cole??o ou Conjunto
//		Set<String> fields = errors.keySet();
//		
//// SEM OPERADOR TERNARIO
////
////		if (fields.contains("name")) {
////			labelErrorName.setText(errors.get("name"));
////		}
////		else {
////			labelErrorName.setText("");
////		}
//		
//		
//		//COM OPERADOR TERNARIO PARA NAO SOBRECARREGAR DE IF
//		labelErrorName.setText(fields.contains("name") ? errors.get("name"): "");
//		labelErrorEmail.setText(fields.contains("email") ? errors.get("email"): "");
//		labelErrorBirthDate.setText(fields.contains("birthDate") ? errors.get("birthDate"): "");
//		labelErrorBaseSalary.setText(fields.contains("baseSalary") ? errors.get("baseSalary"): "");
//		
//	}
//
//	private void initializeComboBoxDepartment() {
//		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
//			@Override
//			protected void updateItem(Department item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty ? "" : item.getName());
//			}
//		};
//		comboBoxDepartment.setCellFactory(factory);
//		comboBoxDepartment.setButtonCell(factory.call(null));
//	}
//
//}

package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

//	// Injentando a Classe Departamento
//	// entidade relacionada a esse formulario
	private Seller entity;

	// DEPENDENCIA
	private SellerService service;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

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

	private ObservableList<Department> obsList;

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChange();
		}
	}

	private Seller getFormData() {
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty");
		}
		obj.setEmail(txtEmail.getText());
		
		if (dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		}
		else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
		
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can't be empty");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		obj.setDepartment(comboBoxDepartment.getValue());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if (entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
		labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}


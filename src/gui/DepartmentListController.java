package gui;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable,DataChangeListener {
	
	//injetar a dependencia sem colocar a imolementação da classe
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);//referencia do stage atual para paassar para o createDialogForm e criar a janela de formulario
		Department obj = new Department();
		createDialogForm(obj,"/gui/DepartmentForm.fxml", parentStage);
	}
	
	//injetando dependencia com injeção de controle
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	//padrao para iniciar o comportamento das colunas (Massete)
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//Massete para fazer o table view acompanhar a altura da janela - Pegando refrencia do Stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}
	
	
	//função = logica para abrir a janelinha de formulario
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			//instanciando controlador da janela DepartmentForm
			DepartmentFormController controller = loader.getController();
			
			//setando o obj Department no controller do DepartmentForm
			//INJEÇÃO DE DEPENDENCIA 
			//INJETANDO DEPARTAMENTO
			controller.setDepartment(obj);
			
			//INJEÇÃO DE DEPENDENCIA 
			//INJETANDO DEPARTAMENTOSERVICE
			controller.setDepartmentService(new DepartmentService());
			
			//SE INSCREVENDO PARA RECEBER EVENTO (PADRAO DE OBJETO OBSERVER)
			controller.subscribeDateChangeListener(this);
			
			//Atualizando a tabela com  o metodo Update do obj setado acima
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);//Modal = enquanto voce nao fechar ela , voc enao pode fechar a janela anterior
			dialogStage.showAndWait();
			
			
		}
		catch (IOException e){
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChange() {
		updateTableView();
		
	}

}

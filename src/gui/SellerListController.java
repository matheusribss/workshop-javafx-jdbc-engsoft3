package gui;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	// injetar a dependencia sem colocar a imolementação da classe
	private SellerService service;

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);// referencia do stage atual para paassar para o createDialogForm
														// e criar a janela de formulario
		Seller obj = new Seller();
		//createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	// injetando dependencia com injeção de controle
	public void setSellerService(SellerService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	// padrao para iniciar o comportamento das colunas (Massete)
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Massete para fazer o table view acompanhar a altura da janela - Pegando
		// refrencia do Stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	// função = logica para abrir a janelinha de formulario
//	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//
//			// instanciando controlador da janela SellerForm
//			SellerFormController controller = loader.getController();
//
//			// setando o obj Seller no controller do SellerForm
//			// INJEÇÃO DE DEPENDENCIA
//			// INJETANDO DEPARTAMENTO
//			controller.setSeller(obj);
//
//			// INJEÇÃO DE DEPENDENCIA
//			// INJETANDO DEPARTAMENTOSERVICE
//			controller.setSellerService(new SellerService());
//
//			// SE INSCREVENDO PARA RECEBER EVENTO (PADRAO DE OBJETO OBSERVER)
//			controller.subscribeDateChangeListener(this);
//
//			// Atualizando a tabela com o metodo Update do obj setado acima
//			controller.updateFormData();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Enter Seller data");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);// Modal = enquanto voce nao fechar ela , voc enao pode
//															// fechar a janela anterior
//			dialogStage.showAndWait();
//
//		} catch (IOException e) {
//			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}
//	}

	@Override
	public void onDataChange() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				//button.setOnAction(
						//event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	
	//METODO PARA CRIAR BOTAO PARA REMOVER
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}
	
	//METODO PARA REMOVER UMA ENTIDADE
	private void  removeEntity(Seller obj) {
		Optional<ButtonType> result =Alerts.showConfirmation("Confirmação", "Você tem certeza que voce quer deletar?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço estava nulo");
			}
			
			try {
				
				
			service.remove(obj);
			updateTableView();
			
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover o objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
		
	}

}

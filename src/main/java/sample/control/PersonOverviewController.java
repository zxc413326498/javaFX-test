package sample.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.dialog.CommandLinksDialog;
import org.controlsfx.dialog.ExceptionDialog;
import sample.app.Main;
import sample.model.Person;
import sample.util.DateUtil;

import static sample.util.DialogUtil.showDialog;

public class PersonOverviewController {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    // Reference to the main application.
    private Main main;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public PersonOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("PersonOverviewController.initialize");
        // Initialize the person table with the two columns.
        //我们在表格列上使用setCellValueFactory(...) 来确定为特定列使用Person对象的某个属性
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param person the person or null
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            //fill the labels with info from the person object
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());

            // we need a way to convert the birthday into a String
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
        } else {
            //if person is null,remove all the text
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        //根据当前选择的项，获取index并在数据列表中移除
        try {
            System.out.println("PersonOverviewController.handleDeletePerson" + "----selectedIndex 值= " + selectedIndex);
            personTable.getItems().remove(selectedIndex);
        } catch (IndexOutOfBoundsException e) {//如果没有选择，index=-1，移除时会报错索引越界，需要catch处理
//            ExceptionDialog exceptionDialog = new ExceptionDialog(e);
//            exceptionDialog.show();

//            CommandLinksDialog commandLinksDialog = new CommandLinksDialog();
//            commandLinksDialog.setTitle("No Selection");
//            commandLinksDialog.setContentText("Please select a person in the table.");
//            commandLinksDialog.show();

            //要么添加至少一个按钮，要么添加多个按钮，其中一个按钮的类型ButtonData.CANCEL_CLOSE才能关闭窗口
            showDialog();
        }
    }
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewPerson(){
        Person newPerson=new Person();
        boolean okClicked=main.showPersonEditDialog(newPerson);
        if(okClicked){
            main.getPersonData().add(newPerson);
        }
    }
    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson=personTable.getSelectionModel().getSelectedItem();
        if(selectedPerson!=null){
            boolean okClicked=main.showPersonEditDialog(selectedPerson);
            if(okClicked){
                showPersonDetails(selectedPerson);
            }
        }else {
            showDialog();
        }
    }

    /**
     * No Selection showDialog
     */


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;
        personTable.setItems(main.getPersonData());
    }
}

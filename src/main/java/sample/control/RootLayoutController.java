package sample.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import sample.app.Main;
import sample.util.DialogUtil;

import java.io.File;

public class RootLayoutController {

    private Main main;

    @FXML
    public void initialize(){
    }

    public void setMain(Main main) {
        this.main=main;
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
        main.getPersonData().clear();
        main.setPersonFilePath(null);
    }
    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen(){
        FileChooser fileChooser=new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extensionFilter=new FileChooser.ExtensionFilter("XML files(*.xml)","*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);
        // Show save file dialog
        File file=fileChooser.showOpenDialog(main.getPrimaryStage());

        if(file!=null){
            main.loadPersonDataFromFile(file);
        }
    }
    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave(){
        File personFile=main.getPersonFilePath();
        if(personFile!=null){
            main.savePersonDataToFile(personFile);
        }else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser=new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extensionFilter=new FileChooser.ExtensionFilter("XML files(*.xml)","*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);
        // Show save file dialog
        File file=fileChooser.showSaveDialog(main.getPrimaryStage());

        if(file!=null){
            // Make sure it has the correct extension
            if(!file.getPath().endsWith(".xml")){
                file=new File(file.getPath()+".xml");
            }
            main.savePersonDataToFile(file);
        }
    }

    /**
     * Opens the birthday statistics.
     */
    @FXML
    private void handleShowBirthdayStatistics() {
        main.showBirthdayStatistics();
    }
    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout(){
        DialogUtil.showDialogMessage("AddressApp","About","SJQ,XueLian Tec.");
    }
    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}

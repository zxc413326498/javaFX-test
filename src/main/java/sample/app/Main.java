package sample.app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import sample.control.LoginController;
import sample.control.PersonEditDialogController;
import sample.control.PersonOverviewController;
import sample.model.Person;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public Main() {
        // Add some sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    /**
     * Returns the data as an observable list of Persons.
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        login();

//        initRootLayout();
//        showPersonOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../../RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../../PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            PersonOverviewController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person){
        try{
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(Main.class.getResource("../../PersonEditDialog.fxml"));
            AnchorPane pane=(AnchorPane) loader.load();
            Stage stage=new Stage();
            stage.setTitle("Edit Person");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            Scene scene=new Scene(pane);
            stage.setScene(scene);

            // Set the person into the controller.
            PersonEditDialogController controller=loader.getController();
            controller.setDialogStage(stage);
            controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            stage.showAndWait();
            //
            return controller.isOkClicked();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 登录窗口
     */
    public void login(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../../login.fxml"));
            GridPane pane = (GridPane) loader.load();

            primaryStage.setTitle("Hello World");
            Scene scene = new Scene(pane, 800, 600);
            primaryStage.setScene(scene);
            //加载背景图片
            scene.getStylesheets().add(getClass().getClassLoader().getResource("Login.css").toExternalForm());
            primaryStage.show();
            //加载control类，传递main对象
            LoginController controller = loader.getController();
            controller.setMain(this);
        }catch (IOException e){
            ExceptionDialog exceptionDialog = new ExceptionDialog(e);
            exceptionDialog.show();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

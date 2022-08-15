package sample.app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import sample.control.LoginController;
import sample.control.PersonEditDialogController;
import sample.control.PersonOverviewController;
import sample.control.RootLayoutController;
import sample.model.Person;
import sample.model.PersonListWrapper;
import sample.util.DialogUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;

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

        //加载项目图标
        URL url=getClass().getClassLoader().getResource("logo.png");
//        URL url=Main.class.getResource("../../logo.png");
        primaryStage.getIcons().add(new Image(url.toExternalForm()));

        login();

//        initRootLayout();
//        showPersonOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
//        primaryStage.getIcons().add(new Image("file:resources/logo.png"));
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("RootLayout.fxml"));
//            loader.setLocation(Main.class.getResource("../../RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            primaryStage.setTitle("AddressApp");
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            // 连接根布局control
            RootLayoutController rootLayoutController=loader.getController();
            rootLayoutController.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened person file.
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("PersonOverview.fxml"));
//            loader.setLocation(Main.class.getResource("../../PersonOverview.fxml"));
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
            loader.setLocation(getClass().getClassLoader().getResource("PersonEditDialog.fxml"));
//            loader.setLocation(Main.class.getResource("../../PersonEditDialog.fxml"));
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
            loader.setLocation(getClass().getClassLoader().getResource("login.fxml"));
//            loader.setLocation(Main.class.getResource("../../login.fxml"));
            GridPane pane = (GridPane) loader.load();

            primaryStage.setTitle("Login");
            Scene scene = new Scene(pane, 400, 300);
            primaryStage.setScene(scene);
            //加载背景图片
            scene.getStylesheets().add(getClass().getClassLoader().getResource("Login.css").toExternalForm());
            primaryStage.show();
            //加载control类，传递main对象
            LoginController controller = loader.getController();
            controller.setMain(this);

            getLoginName(controller);
        }catch (IOException e){
            ExceptionDialog exceptionDialog = new ExceptionDialog(e);
            exceptionDialog.show();
        }
    }
    /**
     * 获取存储的用户名
     * Error：Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5.
     */
    public void getLoginName(LoginController controller){
        Preferences preferences=Preferences.userNodeForPackage(Main.class);
        String username=preferences.get("username",null);
        controller.setUsername(username);
    }
    /**
     * 存储用户名
     */
    public void setLoginName(String name){
        Preferences preferences=Preferences.userNodeForPackage(Main.class);
        preferences.put("username",name);
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getPersonFilePath(){
        Preferences preferences=Preferences.userNodeForPackage(Main.class);
        String filePath=preferences.get("filePath",null);
        if(filePath!=null){
            return new File(filePath);
        }else {
            return null;
        }
    }
    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setPersonFilePath(File file){
        Preferences preferences=Preferences.userNodeForPackage(Main.class);
        if(file!=null){
            preferences.put("filePath", file.getPath());

            //update the stage title
            primaryStage.setTitle("AddressApp -"+file.getName());
        }else {
            preferences.remove("filePath");

            //update the stage title
            primaryStage.setTitle("AddressAppp");
        }
    }
    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
    public void loadPersonDataFromFile(File file){
        try{
            JAXBContext context=JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um=context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            PersonListWrapper wrapper=(PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            // Save the file path to the registry.
            setPersonFilePath(file);
        }catch (Exception e){
            DialogUtil.showDialogMessage("Error",
                    "Could not load data from file:\n" + file.getPath(),
                    e.getMessage());
        }
    }
    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void savePersonDataToFile(File file){
        try{
            JAXBContext context=JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller marshaller=context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            // Wrapping our person data.
            PersonListWrapper wrapper=new PersonListWrapper();
            wrapper.setPersons(personData);
            // Marshalling and saving XML to the file.
            marshaller.marshal(wrapper,file);

            // Save the file path to the registry.
            setPersonFilePath(file);
        }catch (Exception e){
            DialogUtil.showDialogMessage("Error",
                    "Could not save data from file:\n" + file.getPath(),
                    e.getMessage());
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

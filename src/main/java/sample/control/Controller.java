package sample.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Controller {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Text loginmessage;

    private String usernameString;
    private String passwordString;

    @FXML
    public void initialize(){
    }
    @FXML
    public void login(ActionEvent event){
        System.out.println("Controller.login" + "----event.getEventType() 值= " + event.getEventType());
        usernameString=username.getText();
        passwordString=password.getText();

        System.out.println("Controller.initialize" + "----usernameString 值= " + usernameString);
        System.out.println("Controller.initialize" + "----passwordString 值= " + passwordString);

        loginmessage.setText("正在登录，请稍后");
        //正在登录中，进行登录验证和跳转操作
    }
}

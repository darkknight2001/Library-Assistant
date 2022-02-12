package library.assistant.ui.about;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.util.LibraryAssistantUtil;

public class AboutController implements Initializable {

    private static final String LINKED_IN1 = "https://www.linkedin.com/in/lakshya-khandelwal-8148a213b/";
    private static final String LINKED_IN2 = "https://www.linkedin.com/in/kushagra-gupta2702";
    private static final String LINKED_IN3 = "https://www.linkedin.com/in/himanshu-vaswani-941875144/";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AlertMaker.showTrayMessage(String.format("Hello %s!", System.getProperty("user.name")), "Thanks for trying out Library Assistant");
    }

    private void loadWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
            handleWebpageLoadException(url);
        }
    }

    private void handleWebpageLoadException(String url) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        Stage stage = new Stage();
        Scene scene = new Scene(new StackPane(browser));
        stage.setScene(scene);
        stage.setTitle("MSRIT CSE");
        stage.show();
        LibraryAssistantUtil.setStageIcon(stage);
    }

   

    @FXML
    private void loadLinkedIN1(ActionEvent event) {
        loadWebpage(LINKED_IN1);
    }
     @FXML
    private void loadLinkedIN2(ActionEvent event) {
        loadWebpage(LINKED_IN2);
    }
     @FXML
    private void loadLinkedIN3(ActionEvent event) {
        loadWebpage(LINKED_IN3);
    }

  
}

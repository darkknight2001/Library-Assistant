package library.assistant.ui.listmember;

import library.assistant.ui.addmember.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DataHelper;
import library.assistant.database.DatabaseHandler;


import library.assistant.ui.listmember.MemberListController;
import library.assistant.ui.listmember.MemberListController.Member;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SearchMemberController implements Initializable {
     private final static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());
     static ObservableList<MemberListController.Member> list = FXCollections.observableArrayList();
    DatabaseHandler handler;

    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField location;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXButton cancelButton;
    
    @FXML
    private JFXButton selectButton;

    private Boolean isInEditMode = false;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getInstance();
    }

    @FXML
    private void searchMember(ActionEvent event) {
        String mName = StringUtils.trimToEmpty(name.getText());
        String mID = StringUtils.trimToEmpty(id.getText());
        String mMobile = StringUtils.trimToEmpty(mobile.getText());
        String mEmail = StringUtils.trimToEmpty(email.getText());
        String query;
        int count=0;
        if (mName.isEmpty() && mID.isEmpty() && mMobile.isEmpty() && mEmail.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in atleast one field.");
            return;
        }
        query = "SELECT * FROM MEMBER WHERE";
        if(!mID.isEmpty())
            if(count==0){
                query=query+" id= ?";
                count++;}
        if(!mName.isEmpty())
            if(count==0){
                query=query+" upper(name) like '%"+mName+"%'";
                count++;
            }else
                query=query+"and upper(name) like '%"+mName+"%'";
        if(!mMobile.isEmpty())
            if(count==0){
                query=query+" upper(mobile) like '%"+mMobile+"%'";
                count++;
            }else
                query=query+"and upper(mobile) like '%"+mMobile+"%'";    
        if(!mEmail.isEmpty())
            if(count==0){
                query=query+" upper(email) like '%"+mEmail+"%'";
                count++;
            }else
                query=query+"and upper(email) like '%"+mEmail+"%'";    
            
       query=StringUtils.upperCase(query);
       // Book book = new Book(bookID, bookName, bookAuthor, bookPublisher, Boolean.TRUE);
       boolean result;
       boolean flag=false;
       try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    query);
            if(!mID.isEmpty()) 
            statement.setString(1, mID);
       
             ResultSet rs = statement.executeQuery();
           result = true;
           while(rs.next()){
               flag=true;
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");
                String id = rs.getString("id");
                String email = rs.getString("email");

                list.add(new Member(name, id, mobile, email));

           }
        } catch (SQLException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", ex);
            result=false;
        }
        
        if (result && flag) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Search Results", "Found");
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }else if (!result) {
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Search Results", "query error");
        }
        
        else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Search Reults", "Not found");
        }
        
    }
    public static ObservableList<MemberListController.Member> sendList()
    {
        return list;
    }
    
    
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

   


   

}

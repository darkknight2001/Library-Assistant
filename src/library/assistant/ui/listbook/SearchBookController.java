package library.assistant.ui.listbook;

import library.assistant.ui.addbook.*;
import be.quodlibet.boxable.Row;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import library.assistant.data.model.Book;
import library.assistant.database.DataHelper;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbook.BookListController;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class SearchBookController implements Initializable {
     private final static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());
     static ObservableList<BookListController.Book> list = FXCollections.observableArrayList();
    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXTextField location;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    private DatabaseHandler databaseHandler;
    private Boolean isInEditMode = Boolean.FALSE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
    }

    @FXML
    private void searchBook(ActionEvent event) {
        String bookID = StringUtils.trimToEmpty(id.getText());
        String bookAuthor = StringUtils.trimToEmpty(author.getText());
        String bookName = StringUtils.trimToEmpty(title.getText());
        String bookPublisher = StringUtils.trimToEmpty(publisher.getText());
        String query;
        int count=0;
        if (bookID.isEmpty() && bookAuthor.isEmpty() && bookName.isEmpty() && bookPublisher.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in atleast one field.");
            return;
        }
        query = "SELECT * FROM BOOK WHERE";
        if(!bookID.isEmpty())
            if(count==0){
                query=query+" id= ?";
                count++;}
        if(!bookAuthor.isEmpty())
            if(count==0){
                query=query+" upper(author) like '%"+bookAuthor+"%'";
                count++;
            }else
                query=query+"and upper(author) like '%"+bookAuthor+"%'";
        if(!bookName.isEmpty())
            if(count==0){
                query=query+" upper(title) like '%"+bookName+"%'";
                count++;
            }else
                query=query+"and upper(title) like '%"+bookName+"%'";    
        if(!bookPublisher.isEmpty())
            if(count==0){
                query=query+" upper(publisher) like '%"+bookPublisher+"%'";
                count++;
            }else
                query=query+"and upper(publisher) like '%"+bookPublisher+"%'";    
            
       query=StringUtils.upperCase(query);
       // Book book = new Book(bookID, bookName, bookAuthor, bookPublisher, Boolean.TRUE);
       boolean result;
       boolean flag=false;
       try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    query);
            if(!bookID.isEmpty()) 
            statement.setString(1, bookID);
        //    statement.setString(2, bookAuthor);
     //       statement.setString(1, bookName);
           // statement.setString(4, bookPublisher);
             ResultSet rs = statement.executeQuery();
           result = true;
           while(rs.next()){
               flag=true;
                String titlex = rs.getString("title");
                String author = rs.getString("author");
                String id = rs.getString("id");
                String publisher = rs.getString("publisher");
                Boolean avail = rs.getBoolean("isAvail");

                list.add(new BookListController.Book(titlex, id, author, publisher, avail));
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
    public static ObservableList<BookListController.Book> sendList()
    {
        return list;
    }
  
 

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }





}

package ui; 
 
import javafx.application.Application; 
import javafx.fxml.FXMLLoader; 
import javafx.scene.Parent; 
import javafx.scene.Scene; 
import javafx.stage.Stage; 
import javafx.animation.FadeTransition; 
import javafx.util.Duration; 
 
import java.io.IOException; 
 
public class UIDemoLauncher extends Application { 
 
    private static Stage primaryStage; 
 
    @Override 
    public void start(Stage stage) { 
        primaryStage = stage; 
        primaryStage.setTitle("Employee Management System - UI Demo"); 
 
        // Load initial login scene 
        changeScene("LoginPage.fxml"); 
 
        // Set window size 
        primaryStage.setWidth(1000); 
        primaryStage.setHeight(700); 
        primaryStage.setMinWidth(800); 
        primaryStage.setMinHeight(600); 
 
        primaryStage.show(); 
    } 
 
    public static void changeScene(String fxml) { 
        try { 
            System.out.println("Loading FXML: " + fxml); 
 
            // Load the FXML file 
            FXMLLoader loader = new FXMLLoader(UIDemoLauncher.class.getResource("/ui/fxml/" + fxml)); 
            Parent root = loader.load(); 
 
            // Create new scene 
            Scene scene = new Scene(root); 
            scene.getStylesheets().add(UIDemoLauncher.class.getResource("/ui/css/styles.css").toExternalForm()); 
 
            // Apply fade transition 
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(600), root); 
            fadeTransition.setFromValue(0.0); 
            fadeTransition.setToValue(1.0); 
 
            // Set the scene and play the transition 
            primaryStage.setScene(scene); 
            fadeTransition.play(); 
 
        } catch (IOException e) { 
            e.printStackTrace(); 
            System.err.println("Error loading FXML file: " + fxml); 
        } catch (Exception e) { 
            e.printStackTrace(); 
            System.err.println("Unexpected error: " + e.getMessage()); 
        } 
    } 
 
    public static void main(String[] args) { 
        launch(args); 
    } 
} 

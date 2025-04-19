package ui; 
 
public class FXMLScriptHandlers { 
    public static void handleLogin() { 
        UIDemoLauncher.changeScene("Dashboard.fxml"); 
    } 
 
    public static void handleLogout() { 
        UIDemoLauncher.changeScene("LoginPage.fxml"); 
    } 
} 

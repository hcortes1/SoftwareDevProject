import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.util.Random;

public class SimpleUIDemo extends Application {

    private Stage primaryStage;
    private Scene loginScene;
    private Scene dashboardScene;
    private Timeline backgroundAnimation;
    private ParallelTransition loginParallelTransition;
    private SequentialTransition dashboardSequentialTransition;
    private Random random = new Random();

    // Constants for animations
    private final Duration HOVER_DURATION = Duration.millis(150);
    private final Duration TRANSITION_DURATION = Duration.millis(800);
    private final Duration STAGGER_DURATION = Duration.millis(100);
    private final double HOVER_SCALE = 1.05;
    private final double BUTTON_OPACITY = 0.9;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Employee Management System - UI Demo");
        
        // Create scenes
        createLoginScene();
        createDashboardScene();
        
        // Set initial scene with fade-in animation
        primaryStage.setScene(loginScene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Start the background animation for login scene
        startBackgroundAnimation();
        
        // Play entrance animations when the stage is shown
        primaryStage.setOnShown(e -> {
            playLoginEntranceAnimations();
        });
        
        primaryStage.show();
    }
    
    /**
     * Creates a subtle animated background effect
     */
    private void startBackgroundAnimation() {
        // Create a subtle animation for the background gradient
        backgroundAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(
                ((StackPane)loginScene.getRoot()).getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius(),
                20)),
            new KeyFrame(Duration.seconds(15), new KeyValue(
                ((StackPane)loginScene.getRoot()).getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius(),
                20))
        );
        backgroundAnimation.setCycleCount(Timeline.INDEFINITE);
        backgroundAnimation.setAutoReverse(true);
        backgroundAnimation.play();
    }
    
    /**
     * Plays entrance animations for login page elements
     */
    private void playLoginEntranceAnimations() {
        // Get the login container (VBox) - it's the last child in the StackPane
        VBox loginBox = (VBox) ((StackPane)loginScene.getRoot()).getChildren()
            .filtered(node -> node instanceof VBox).get(0);
        
        // Create staggered animations for each child
        loginParallelTransition = new ParallelTransition();
        
        // Add fade and slide animations for each element
        for (int i = 0; i < loginBox.getChildren().size(); i++) {
            Node node = loginBox.getChildren().get(i);
            
            // Create fade transition
            FadeTransition fadeIn = new FadeTransition(TRANSITION_DURATION, node);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setDelay(Duration.millis(i * 100));
            
            // Create translate transition (slide from bottom)
            TranslateTransition slideIn = new TranslateTransition(TRANSITION_DURATION, node);
            slideIn.setFromY(20);
            slideIn.setToY(0);
            slideIn.setDelay(Duration.millis(i * 100));
            
            // Add to parallel transition
            loginParallelTransition.getChildren().addAll(fadeIn, slideIn);
        }
        
        // Play the animations
        loginParallelTransition.play();
    }
    
    private void createLoginScene() {
        // Main container
        StackPane root = new StackPane();
        
        // Create animated gradient background
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e6e6fa, #f0e6ff, #ffe6f2, #e6f0ff);");
        
        // Add floating decorative elements
        for (int i = 0; i < 5; i++) {
            Circle decorCircle = new Circle(random.nextInt(30) + 20);
            decorCircle.setFill(Color.web(
                new String[]{"#e6e6fa", "#f0e6ff", "#ffe6f2", "#e6f0ff"}[random.nextInt(4)]
            ));
            decorCircle.setOpacity(0.3);
            decorCircle.setTranslateX(random.nextInt(800) - 400);
            decorCircle.setTranslateY(random.nextInt(600) - 300);
            
            // Add subtle floating animation
            TranslateTransition floatAnim = new TranslateTransition(
                Duration.seconds(random.nextInt(10) + 15), decorCircle);
            floatAnim.setByX(random.nextInt(60) - 30);
            floatAnim.setByY(random.nextInt(60) - 30);
            floatAnim.setCycleCount(TranslateTransition.INDEFINITE);
            floatAnim.setAutoReverse(true);
            floatAnim.play();
            
            root.getChildren().add(decorCircle);
        }
        
        // Login form container with glass morphism effect
        VBox loginBox = new VBox(20);
        loginBox.setMaxWidth(450);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(40));
        loginBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); " +
                         "-fx-background-radius: 20px; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 15, 0.1, 0, 4);");
        
        // Add a subtle blur effect for glass morphism
        BoxBlur blur = new BoxBlur();
        blur.setWidth(10);
        blur.setHeight(10);
        blur.setIterations(3);
        
        // Logo with pulsing animation
        StackPane logoContainer = new StackPane();
        Circle logoCircle = new Circle(60);
        logoCircle.setFill(Color.web("#f0e6ff"));
        logoCircle.setStroke(Color.web("#b8a6db"));
        logoCircle.setStrokeWidth(2);
        Label logoLabel = new Label("EMS");
        logoLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 28));
        logoLabel.setTextFill(Color.web("#7b68ee"));
        logoContainer.getChildren().addAll(logoCircle, logoLabel);
        
        // Add pulsing animation to logo
        ScaleTransition pulseLogo = new ScaleTransition(Duration.seconds(2), logoCircle);
        pulseLogo.setFromX(1.0);
        pulseLogo.setFromY(1.0);
        pulseLogo.setToX(1.05);
        pulseLogo.setToY(1.05);
        pulseLogo.setCycleCount(ScaleTransition.INDEFINITE);
        pulseLogo.setAutoReverse(true);
        pulseLogo.play();
        
        // Title
        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);
        Label subtitleLabel = new Label("Welcome to");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #9370db;");
        Label titleLabel = new Label("Employee Management System");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #9370db;");
        titleLabel.setWrapText(true);
        titleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        titleBox.getChildren().addAll(subtitleLabel, titleLabel);
        
        // Separator
        Separator separator = new Separator();
        separator.setOpacity(0.3);
        
        // Form fields
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        
        // Username field with icon
        HBox usernameBox = new HBox(15);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        
        // Username icon
        Label userIcon = new Label("👤");
        userIcon.setStyle("-fx-font-size: 18px;");
        
        Label usernameLabel = new Label("Username");
        usernameLabel.setMinWidth(90);
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #7b68ee;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-background-color: #f8f9ff; " +
                              "-fx-background-radius: 10px; " +
                              "-fx-border-color: #e0e0e0; " +
                              "-fx-border-radius: 10px; " +
                              "-fx-padding: 12px 15px;");
        
        // Add focus animation to text field
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                usernameField.setStyle("-fx-background-color: #f8f9ff; " +
                                      "-fx-background-radius: 10px; " +
                                      "-fx-border-color: #9370db; " +
                                      "-fx-border-radius: 10px; " +
                                      "-fx-border-width: 2px; " +
                                      "-fx-padding: 12px 15px;");
            } else {
                usernameField.setStyle("-fx-background-color: #f8f9ff; " +
                                      "-fx-background-radius: 10px; " +
                                      "-fx-border-color: #e0e0e0; " +
                                      "-fx-border-radius: 10px; " +
                                      "-fx-padding: 12px 15px;");
            }
        });
        
        HBox.setHgrow(usernameField, Priority.ALWAYS);
        usernameBox.getChildren().addAll(userIcon, usernameLabel, usernameField);
        
        // Password field with icon
        HBox passwordBox = new HBox(15);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        
        // Password icon
        Label passIcon = new Label("🔒");
        passIcon.setStyle("-fx-font-size: 18px;");
        
        Label passwordLabel = new Label("Password");
        passwordLabel.setMinWidth(90);
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #7b68ee;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-background-color: #f8f9ff; " +
                              "-fx-background-radius: 10px; " +
                              "-fx-border-color: #e0e0e0; " +
                              "-fx-border-radius: 10px; " +
                              "-fx-padding: 12px 15px;");
        
        // Add focus animation to password field
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle("-fx-background-color: #f8f9ff; " +
                                      "-fx-background-radius: 10px; " +
                                      "-fx-border-color: #9370db; " +
                                      "-fx-border-radius: 10px; " +
                                      "-fx-border-width: 2px; " +
                                      "-fx-padding: 12px 15px;");
            } else {
                passwordField.setStyle("-fx-background-color: #f8f9ff; " +
                                      "-fx-background-radius: 10px; " +
                                      "-fx-border-color: #e0e0e0; " +
                                      "-fx-border-radius: 10px; " +
                                      "-fx-padding: 12px 15px;");
            }
        });
        
        HBox.setHgrow(passwordField, Priority.ALWAYS);
        passwordBox.getChildren().addAll(passIcon, passwordLabel, passwordField);
        
        // Remember me and forgot password
        HBox optionsBox = new HBox(5);
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        CheckBox rememberMeCheckbox = new CheckBox("Remember me");
        
        // Add animation to checkbox
        rememberMeCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), rememberMeCheckbox);
            st.setFromX(1.0);
            st.setFromY(1.0);
            st.setToX(1.1);
            st.setToY(1.1);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot password?");
        forgotPasswordLink.setStyle("-fx-text-fill: #9370db; -fx-underline: false;");
        
        // Add hover effect to hyperlink
        forgotPasswordLink.setOnMouseEntered(e -> {
            forgotPasswordLink.setStyle("-fx-text-fill: #7b68ee; -fx-underline: true;");
        });
        
        forgotPasswordLink.setOnMouseExited(e -> {
            forgotPasswordLink.setStyle("-fx-text-fill: #9370db; -fx-underline: false;");
        });
        
        optionsBox.getChildren().addAll(rememberMeCheckbox, spacer, forgotPasswordLink);
        
        // Login button with hover and click effects
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(200);
        loginButton.setPrefHeight(45);
        loginButton.setStyle("-fx-background-color: linear-gradient(to right, #a18cd1, #b8a6db); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 30px; " +
                            "-fx-cursor: hand;");
        
        // Add hover effects to login button
        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle("-fx-background-color: linear-gradient(to right, #8a7bb9, #a18cd1); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 30px; " +
                                "-fx-cursor: hand; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.1, 0, 4);");
            
            ScaleTransition st = new ScaleTransition(HOVER_DURATION, loginButton);
            st.setToX(HOVER_SCALE);
            st.setToY(HOVER_SCALE);
            st.play();
        });
        
        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle("-fx-background-color: linear-gradient(to right, #a18cd1, #b8a6db); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 30px; " +
                                "-fx-cursor: hand;");
            
            ScaleTransition st = new ScaleTransition(HOVER_DURATION, loginButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        
        loginButton.setOnMousePressed(e -> {
            loginButton.setStyle("-fx-background-color: linear-gradient(to right, #7a6ba9, #9370db); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 30px; " +
                                "-fx-cursor: hand;");
            
            ScaleTransition st = new ScaleTransition(Duration.millis(100), loginButton);
            st.setToX(0.95);
            st.setToY(0.95);
            st.play();
        });
        
        loginButton.setOnMouseReleased(e -> {
            loginButton.setStyle("-fx-background-color: linear-gradient(to right, #a18cd1, #b8a6db); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 30px; " +
                                "-fx-cursor: hand;");
            
            ScaleTransition st = new ScaleTransition(Duration.millis(100), loginButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        
        loginButton.setOnAction(e -> switchToDashboard());
        
        // Sign up link
        HBox signupBox = new HBox(5);
        signupBox.setAlignment(Pos.CENTER);
        Label noAccountLabel = new Label("Don't have an account?");
        Hyperlink signUpLink = new Hyperlink("Sign Up");
        signUpLink.setStyle("-fx-text-fill: #9370db; -fx-underline: false;");
        
        // Add hover effect to sign up link
        signUpLink.setOnMouseEntered(e -> {
            signUpLink.setStyle("-fx-text-fill: #7b68ee; -fx-underline: true;");
        });
        
        signUpLink.setOnMouseExited(e -> {
            signUpLink.setStyle("-fx-text-fill: #9370db; -fx-underline: false;");
        });
        
        signupBox.getChildren().addAll(noAccountLabel, signUpLink);
        
        // Add all to form
        formBox.getChildren().addAll(usernameBox, passwordBox, optionsBox, loginButton, signupBox);
        
        // Add all to login box
        loginBox.getChildren().addAll(logoContainer, titleBox, separator, formBox);
        
        // Add login box to root
        root.getChildren().add(loginBox);
        
        // Create scene
        loginScene = new Scene(root);
    }
    
    private void createDashboardScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9ff;");
        
        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #a18cd1, #b8a6db);");
        
        Label titleLabel = new Label("Employee Management System");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(200);
        searchField.setStyle("-fx-background-color: rgba(255,255,255,0.2); " +
                            "-fx-background-radius: 20px; " +
                            "-fx-text-fill: white; " +
                            "-fx-prompt-text-fill: rgba(255,255,255,0.7); " +
                            "-fx-padding: 8px 15px;");
        
        Button userButton = new Button("Admin");
        userButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        
        header.getChildren().addAll(titleLabel, spacer, searchField, userButton);
        
        // Sidebar
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(25, 15, 25, 15));
        sidebar.setStyle("-fx-background-color: white; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.08), 5, 0, 0, 2);");
        
        // Profile section
        VBox profileSection = new VBox(12);
        profileSection.setAlignment(Pos.CENTER);
        
        StackPane profileImageContainer = new StackPane();
        Circle profileCircle = new Circle(45);
        profileCircle.setFill(Color.web("#f0e6ff"));
        profileCircle.setStroke(Color.web("#b8a6db"));
        profileCircle.setStrokeWidth(2);
        Label profileInitial = new Label("A");
        profileInitial.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #7b68ee;");
        profileImageContainer.getChildren().addAll(profileCircle, profileInitial);
        
        Label profileName = new Label("Admin User");
        profileName.setStyle("-fx-font-weight: bold; -fx-text-fill: #7b68ee; -fx-font-size: 16px;");
        
        Label profileRole = new Label("Administrator");
        profileRole.setStyle("-fx-text-fill: #9e9e9e; -fx-font-size: 14px;");
        
        Separator profileSeparator = new Separator();
        profileSeparator.setOpacity(0.3);
        
        profileSection.getChildren().addAll(profileImageContainer, profileName, profileRole, profileSeparator);
        
        // Navigation menu
        Label mainMenuLabel = new Label("MAIN MENU");
        mainMenuLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #9e9e9e;");
        
        Button dashboardButton = new Button("Dashboard");
        dashboardButton.setMaxWidth(Double.MAX_VALUE);
        dashboardButton.setAlignment(Pos.CENTER_LEFT);
        dashboardButton.setStyle("-fx-background-color: #f0e6ff; -fx-text-fill: #7b68ee; " +
                                "-fx-font-weight: bold; -fx-background-radius: 10px; " +
                                "-fx-padding: 12px 15px;");
        
        Button employeesButton = new Button("Employees");
        employeesButton.setMaxWidth(Double.MAX_VALUE);
        employeesButton.setAlignment(Pos.CENTER_LEFT);
        employeesButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #7b68ee; " +
                                "-fx-background-radius: 10px; -fx-padding: 12px 15px;");
        
        Button departmentsButton = new Button("Departments");
        departmentsButton.setMaxWidth(Double.MAX_VALUE);
        departmentsButton.setAlignment(Pos.CENTER_LEFT);
        departmentsButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #7b68ee; " +
                                 "-fx-background-radius: 10px; -fx-padding: 12px 15px;");
        
        Button reportsButton = new Button("Reports");
        reportsButton.setMaxWidth(Double.MAX_VALUE);
        reportsButton.setAlignment(Pos.CENTER_LEFT);
        reportsButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #7b68ee; " +
                             "-fx-background-radius: 10px; -fx-padding: 12px 15px;");
        
        Label settingsLabel = new Label("SETTINGS");
        settingsLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #9e9e9e;");
        
        Button userSettingsButton = new Button("User Settings");
        userSettingsButton.setMaxWidth(Double.MAX_VALUE);
        userSettingsButton.setAlignment(Pos.CENTER_LEFT);
        userSettingsButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #7b68ee; " +
                                  "-fx-background-radius: 10px; -fx-padding: 12px 15px;");
        
        Button systemSettingsButton = new Button("System Settings");
        systemSettingsButton.setMaxWidth(Double.MAX_VALUE);
        systemSettingsButton.setAlignment(Pos.CENTER_LEFT);
        systemSettingsButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #7b68ee; " +
                                    "-fx-background-radius: 10px; -fx-padding: 12px 15px;");
        
        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);
        
        Button logoutButton = new Button("Logout");
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setAlignment(Pos.CENTER_LEFT);
        logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff6b8b; " +
                            "-fx-background-radius: 10px; -fx-padding: 12px 15px;");
        logoutButton.setOnAction(e -> switchToLogin());
        
        sidebar.getChildren().addAll(
            profileSection, 
            mainMenuLabel, 
            dashboardButton, 
            employeesButton, 
            departmentsButton, 
            reportsButton, 
            settingsLabel, 
            userSettingsButton, 
            systemSettingsButton, 
            sidebarSpacer, 
            logoutButton
        );
        
        // Main content
        ScrollPane contentScroll = new ScrollPane();
        contentScroll.setFitToWidth(true);
        contentScroll.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: transparent;");
        
        // Welcome banner
        HBox welcomeBanner = new HBox();
        welcomeBanner.setAlignment(Pos.CENTER_LEFT);
        welcomeBanner.setSpacing(15);
        welcomeBanner.setPadding(new Insets(25, 30, 25, 30));
        welcomeBanner.setStyle("-fx-background-color: linear-gradient(to right, #e6e6fa, #f0e6ff); " +
                              "-fx-background-radius: 15px;");
        
        VBox welcomeTextBox = new VBox(5);
        Label welcomeText = new Label("Hello, Admin!");
        welcomeText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #7b68ee;");
        Label welcomeSubtext = new Label("Welcome back to your dashboard");
        welcomeSubtext.setStyle("-fx-font-size: 14px; -fx-text-fill: #9e9e9e;");
        welcomeTextBox.getChildren().addAll(welcomeText, welcomeSubtext);
        
        Region welcomeSpacer = new Region();
        HBox.setHgrow(welcomeSpacer, Priority.ALWAYS);
        
        Button viewProfileButton = new Button("View Profile");
        viewProfileButton.setStyle("-fx-background-color: linear-gradient(to right, #fbc2eb, #e6dee9); " +
                                  "-fx-text-fill: #7b68ee; -fx-font-weight: bold; " +
                                  "-fx-background-radius: 30px; -fx-padding: 12px 25px;");
        
        welcomeBanner.getChildren().addAll(welcomeTextBox, welcomeSpacer, viewProfileButton);
        
        // Stats cards
        HBox statsCards = new HBox(20);
        statsCards.setAlignment(Pos.CENTER);
        
        // Employees card
        VBox employeesCard = createStatCard("👥", "Total Employees", "248", "↑ 12% from last month", true);
        
        // Departments card
        VBox departmentsCard = createStatCard("🏢", "Departments", "8", "Active departments", false);
        
        // New hires card
        VBox newHiresCard = createStatCard("🆕", "New Hires", "18", "This Month", false);
        
        statsCards.getChildren().addAll(employeesCard, departmentsCard, newHiresCard);
        
        // Recent activity section
        VBox activitySection = new VBox(15);
        activitySection.setStyle("-fx-background-color: white; -fx-background-radius: 15px; " +
                                "-fx-padding: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.08), 8, 0, 0, 2);");
        
        HBox activityHeader = new HBox();
        Label activityTitle = new Label("Recent Activity");
        activityTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #7b68ee;");
        
        Region activitySpacer = new Region();
        HBox.setHgrow(activitySpacer, Priority.ALWAYS);
        
        Button viewAllButton = new Button("View All");
        viewAllButton.setStyle("-fx-background-color: linear-gradient(to right, #fbc2eb, #e6dee9); " +
                              "-fx-text-fill: #7b68ee; -fx-font-weight: bold; " +
                              "-fx-background-radius: 30px; -fx-padding: 12px 25px;");
        
        activityHeader.getChildren().addAll(activityTitle, activitySpacer, viewAllButton);
        
        // Table
        TableView<String> activityTable = new TableView<>();
        activityTable.setPrefHeight(250);
        activityTable.setStyle("-fx-background-color: transparent;");
        
        TableColumn<String, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setPrefWidth(120);
        
        TableColumn<String, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setPrefWidth(150);
        
        TableColumn<String, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setPrefWidth(200);
        
        TableColumn<String, String> detailsColumn = new TableColumn<>("Details");
        detailsColumn.setPrefWidth(180);
        
        activityTable.getColumns().addAll(dateColumn, employeeColumn, actionColumn, detailsColumn);
        
        activitySection.getChildren().addAll(activityHeader, activityTable);
        
        // Add all to content
        content.getChildren().addAll(welcomeBanner, statsCards, activitySection);
        
        contentScroll.setContent(content);
        
        // Set all regions to root
        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(contentScroll);
        
        // Create scene
        dashboardScene = new Scene(root);
    }
    
    private VBox createStatCard(String icon, String title, String number, String subtitle, boolean isPositive) {
        VBox card = new VBox(10);
        card.setPrefWidth(220);
        card.setPrefHeight(150);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15px; " +
                     "-fx-padding: 20px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.08), 8, 0, 0, 2);");
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 28px;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #9370db; -fx-opacity: 0.9;");
        
        Label numberLabel = new Label(number);
        numberLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #7b68ee;");
        
        HBox subtitleBox = new HBox(5);
        subtitleBox.setAlignment(Pos.CENTER);
        
        if (isPositive) {
            Label arrowLabel = new Label("↑");
            arrowLabel.setTextFill(Color.web("#6bcb77"));
            
            Label subtitleLabel = new Label(subtitle);
            subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6bcb77;");
            
            subtitleBox.getChildren().addAll(arrowLabel, subtitleLabel);
        } else {
            Label subtitleLabel = new Label(subtitle);
            subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #9e9e9e;");
            
            subtitleBox.getChildren().add(subtitleLabel);
        }
        
        card.getChildren().addAll(iconLabel, titleLabel, numberLabel, subtitleBox);
        
        return card;
    }
    
    private void switchToDashboard() {
        // Create a parallel transition for exit animations
        ParallelTransition exitTransition = new ParallelTransition();
        
        // Get the login container (VBox) - it's the last child in the StackPane
        VBox loginBox = (VBox) ((StackPane)loginScene.getRoot()).getChildren()
            .filtered(node -> node instanceof VBox).get(0);
        
        // Add fade out and slide up animations
        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), loginBox);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(400), loginBox);
        scaleOut.setFromX(1.0);
        scaleOut.setFromY(1.0);
        scaleOut.setToX(0.95);
        scaleOut.setToY(0.95);
        
        // Add to parallel transition
        exitTransition.getChildren().addAll(fadeOut, scaleOut);
        
        // When exit animation completes, switch scenes and play entrance animation
        exitTransition.setOnFinished(e -> {
            primaryStage.setScene(dashboardScene);
            
            // Create entrance animations for dashboard
            dashboardSequentialTransition = new SequentialTransition();
            
            // Get dashboard components
            BorderPane dashboardRoot = (BorderPane) dashboardScene.getRoot();
            HBox header = (HBox) dashboardRoot.getTop();
            VBox sidebar = (VBox) dashboardRoot.getLeft();
            ScrollPane contentScroll = (ScrollPane) dashboardRoot.getCenter();
            VBox content = (VBox) contentScroll.getContent();
            
            // Header animation
            TranslateTransition headerSlide = new TranslateTransition(Duration.millis(500), header);
            headerSlide.setFromY(-50);
            headerSlide.setToY(0);
            
            FadeTransition headerFade = new FadeTransition(Duration.millis(500), header);
            headerFade.setFromValue(0);
            headerFade.setToValue(1);
            
            ParallelTransition headerAnim = new ParallelTransition(headerSlide, headerFade);
            
            // Sidebar animation
            TranslateTransition sidebarSlide = new TranslateTransition(Duration.millis(500), sidebar);
            sidebarSlide.setFromX(-50);
            sidebarSlide.setToX(0);
            
            FadeTransition sidebarFade = new FadeTransition(Duration.millis(500), sidebar);
            sidebarFade.setFromValue(0);
            sidebarFade.setToValue(1);
            
            ParallelTransition sidebarAnim = new ParallelTransition(sidebarSlide, sidebarFade);
            
            // Content animations - staggered for each child
            ParallelTransition contentAnim = new ParallelTransition();
            
            for (int i = 0; i < content.getChildren().size(); i++) {
                Node node = content.getChildren().get(i);
                
                // Set initial opacity
                node.setOpacity(0);
                
                // Create fade transition
                FadeTransition fade = new FadeTransition(Duration.millis(400), node);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setDelay(Duration.millis(i * 150 + 300)); // Staggered delay
                
                // Create translate transition
                TranslateTransition translate = new TranslateTransition(Duration.millis(400), node);
                translate.setFromY(30);
                translate.setToY(0);
                translate.setDelay(Duration.millis(i * 150 + 300)); // Same delay as fade
                
                // Add to content animation
                contentAnim.getChildren().addAll(fade, translate);
            }
            
            // Add all animations to sequential transition
            dashboardSequentialTransition.getChildren().addAll(
                headerAnim,
                sidebarAnim,
                contentAnim
            );
            
            // Play the entrance animation
            dashboardSequentialTransition.play();
        });
        
        // Play the exit animation
        exitTransition.play();
    }
    
    private void switchToLogin() {
        // Create a parallel transition for exit animations
        ParallelTransition exitTransition = new ParallelTransition();
        
        // Get dashboard components
        BorderPane dashboardRoot = (BorderPane) dashboardScene.getRoot();
        
        // Add fade out animation
        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), dashboardRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        // Add to parallel transition
        exitTransition.getChildren().add(fadeOut);
        
        // When exit animation completes, switch scenes and play entrance animation
        exitTransition.setOnFinished(e -> {
            primaryStage.setScene(loginScene);
            
            // Get the login container (VBox) - it's the last child in the StackPane
            VBox loginBox = (VBox) ((StackPane)loginScene.getRoot()).getChildren()
                .filtered(node -> node instanceof VBox).get(0);
            
            // Reset opacity and scale
            loginBox.setOpacity(0);
            loginBox.setScaleX(0.9);
            loginBox.setScaleY(0.9);
            
            // Create entrance animations
            ParallelTransition entranceTransition = new ParallelTransition();
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), loginBox);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            
            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(600), loginBox);
            scaleIn.setFromX(0.9);
            scaleIn.setFromY(0.9);
            scaleIn.setToX(1.0);
            scaleIn.setToY(1.0);
            
            // Add to parallel transition
            entranceTransition.getChildren().addAll(fadeIn, scaleIn);
            
            // Play the entrance animation
            entranceTransition.play();
        });
        
        // Play the exit animation
        exitTransition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

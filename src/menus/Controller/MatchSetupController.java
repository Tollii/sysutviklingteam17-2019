package menus.Controller;

import Runnables.RunnableInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import database.Database;
import gameplay.GameLogic;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import menus.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static database.Variables.*;


    public class MatchSetupController extends Controller {

        private boolean findGameClicked, gameEntered, threadStarted, createGameClicked = false;
        private Thread matchSetupThread;

        //TODO: DESPAGHETTI



        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private JFXButton joinGameButton;

        @FXML
        private TableColumn<Match, Boolean> passwordTable;

        @FXML
        private JFXButton createGameButton;

        @FXML
        private Label gameTitleLabel;

        @FXML
        private TableColumn<Match, String> player_table;

        @FXML
        private TableColumn<Match, Integer> matchID_Table;

        @FXML
        private TableView<Match> table;

        @FXML
        private JFXButton matchSetup_refreshButton;

        @FXML
        private JFXButton backToMenuButton;


        @FXML
        private ImageView paperLegionLogo;

        @FXML
        void initialize() {




            Runnable matchSetupRunnable = new RunnableInterface() {

                private boolean doStop = false;

                @Override
                public synchronized void doStop() {
                    this.doStop = true;
                }

                @Override
                public boolean keepRunning() {
                    return !this.doStop;
                }

                @Override
                public void run() {
                    while(keepRunning()){
                        if(!gameEntered){
                            gameEntered = db.pollGameStarted(match_id);

                        } else if(!createGameClicked){
                            doStop();
                        } else{
                            Platform.runLater(
                                    () -> {
                                        if(gameEntered){
                                            yourTurn = true;
                                            enterGame();
                                            doStop();
                                            gameEntered = false;
                                            matchSetupThread = null;
                                        }

                                    });
                        }

                    }
                }
            };



            //Sets up variables for connecting Match class with Table Columns
            table.setPlaceholder(new Label("No available matches found"));
            player_table.setCellValueFactory(new PropertyValueFactory<>("player"));
            matchID_Table.setCellValueFactory(new PropertyValueFactory<>("match_id"));
            passwordTable.setCellValueFactory(new PropertyValueFactory<>("passwordProtected"));
            table.setItems(getMatches());

            matchSetup_refreshButton.setOnAction(event -> {
                table.setItems(getMatches()); //Refreshes tables.
            });

            // ABORTS CREATED MATCHES AND EXITS TO MAIN MENU
            backToMenuButton.setOnAction(event -> {
                db.abortMatch(user_id);
                changeScene("mainMenu.fxml");
            });

            //JOINS SELECTED GAME ROW IN TABLE
            joinGameButton.setOnAction(event -> {
              Match selectedMatch = table.getSelectionModel().getSelectedItem();

              // IF NOT PASSWORD PROTECTED, JOIN AND ENTER GAME
              if(selectedMatch != null && selectedMatch.getPasswordProtected() == false){
                  db.joinGame(selectedMatch.getMatch_id(),user_id);
                  match_id =  selectedMatch.getMatch_id();
                  yourTurn = false;
                  enterGame();
                  System.out.println(match_id);

                  // IF THE GAME IS PASSWORD PROTECTED, POPUP WINDOW WITH USER INPUT
              } else if(selectedMatch != null && selectedMatch.getPasswordProtected() == true){
                  // SETS UP WINDOW WITH PANES IF PASSWORD PROTECTED
                  StackPane stackpane = new StackPane();
                  VBox vBox = new VBox();
                  Label dialog = new Label("Enter password");
                  HBox hBox = new HBox();
                  JFXButton abort = new JFXButton("Cancel");
                  JFXButton join = new JFXButton("Join Game");
                  JFXTextField inputPassword = new JFXTextField();

                  //STYLING, ALIGNMENT AND SPACING FOR CONTENT IN POPUP WINDOW
                  hBox.getChildren().addAll(join, abort);
                  hBox.setAlignment(Pos.CENTER);
                  vBox.setAlignment(Pos.CENTER);
                  vBox.setSpacing(20);
                  vBox.getChildren().addAll(dialog, inputPassword,hBox);
                  stackpane.getChildren().add(vBox);
                  inputPassword.setPromptText("Enter password");
                  inputPassword.setPrefWidth(150);
                  inputPassword.setMaxWidth(150);
                  join.setStyle("-fx-background-color: Black;" +"-fx-text-fill: White;");
                  abort.setStyle("-fx-background-color: Black;" +"-fx-text-fill: White;");
                  hBox.setSpacing(10);
                  hBox.setSpacing(10);

                  //CREATES A POPUP WINDOW
                  Stage window = new Stage();
                  window.initStyle(StageStyle.UNDECORATED);
                  Scene scene = new Scene(stackpane, 300,150);
                  window.setScene(scene);
                  window.show();

                  //CANCEL
                  abort.setOnAction(event1 -> {
                      window.close();
                  });

                  // CHECKS INPUT PASSWORD AGAINST MATCH PASSWORD.
                  join.setOnAction(event1 -> {
                      String passwordUserInput = inputPassword.getText().trim().toString();
                      if(passwordUserInput.equals(selectedMatch.getPassword())){
                          window.close();
                          db.joinGame(selectedMatch.getMatch_id(), user_id);
                          match_id =  selectedMatch.getMatch_id();
                          yourTurn = false;
                          enterGame();
                          System.out.println(match_id);

                      } else {
                          dialog.setText("Wrong password, try again");
                      }
                  });

              }
            });

            createGameButton.setOnAction(event -> {
                if(!createGameClicked){
                    //SETS UP PANES FOR POPUP WINDOW
                    StackPane stackpane = new StackPane();
                    VBox vBox = new VBox();
                    Label dialog = new Label("Do you want to password protect this game?");
                    JFXButton yes_button = new JFXButton("Yes");
                    JFXButton no_button = new JFXButton("No");
                    HBox hBox = new HBox();
                    JFXButton abort = new JFXButton("Abort");
                    JFXButton submitPassword = new JFXButton("Submit");
                    JFXTextField inputPassword = new JFXTextField();

                    //STYLING, ALIGNMENT AND SPACINGS FOR BUTTONS AND INPUT IN POPUP WINDOW
                    yes_button.setStyle("-fx-background-color: Black;" +"-fx-text-fill: White;");
                    no_button.setStyle("-fx-background-color: Black;" +"-fx-text-fill: White;");
                    abort.setStyle("-fx-background-color: Black;" +"-fx-text-fill: White;");
                    submitPassword.setStyle("-fx-background-color: Black;" +"-fx-text-fill: White;");
                    hBox.setSpacing(10);
                    hBox.setAlignment(Pos.CENTER);
                    hBox.getChildren().addAll(yes_button,no_button, abort);
                    vBox.getChildren().addAll(dialog,hBox);
                    vBox.setAlignment(Pos.CENTER);
                    vBox.setSpacing(20);
                    stackpane.getChildren().add(vBox);
                    inputPassword.setPromptText("Enter password");
                    inputPassword.setPrefWidth(150);
                    inputPassword.setMaxWidth(150);


                    //CREATES POPUP WINDOW
                    Stage window = new Stage();
                    window.initStyle(StageStyle.UNDECORATED);
                    Scene scene = new Scene(stackpane, 300,150);
                    window.setScene(scene);
                    window.show();

                    // IN CASE USER WANTS TO PASSWORD PROTECT GAME
                    yes_button.setOnAction(event1 -> {
                        dialog.setText("Input password for game:");
                        hBox.getChildren().removeAll(yes_button,no_button, abort);
                        vBox.getChildren().removeAll(hBox);
                        hBox.getChildren().addAll(submitPassword, abort);
                        vBox.getChildren().addAll(inputPassword, hBox);
                    });

                    // ADD MATCH IN GAME AND WAIT FOR PLAYER TO JOIN
                    submitPassword.setOnAction(event1 -> {
                        String passwordInserted = inputPassword.getText().trim().toString();
                        match_id = db.createGame(user_id, passwordInserted);
                        table.setItems(getMatches()); //Refreshes tables.
                        window.close();
                        createGameClicked = true;
                        createGameButton.setText("Abort Match");
                        matchSetupThread = new Thread(matchSetupRunnable);
                        matchSetupThread.start();
                    });

                    // CREATE GAME WITHOUT PASSWORD
                    no_button.setOnAction(event1 -> {
                        match_id = db.createGame(user_id, "null");
                        table.setItems(getMatches()); //Refreshes tables.
                        window.close();
                        createGameClicked = true;
                        createGameButton.setText("Abort Match");
                        matchSetupThread = new Thread(matchSetupRunnable);
                        matchSetupThread.start();
                    });

                    //CANCEL
                    abort.setOnAction(event1 -> {
                        window.close();
                    });

                } else{
                    db.abortMatch(user_id);
                    createGameClicked = false;
                    createGameButton.setText("Create Game");
                    table.setItems(getMatches()); //Refreshes tables.
                    matchSetupThread.stop();
                    matchSetupThread = null;
                }


            });

        }

    public ObservableList<Match> getMatches(){
        ArrayList<Match> matches =  db.findGamesAvailable();
        ObservableList<Match> match_ids = FXCollections.observableArrayList();

        for (Match i : matches) {
            match_ids.add(i);
        }
        return match_ids;
    }


    private void enterGame() {
        try {
            GameLogic game = new GameLogic();
            game.start(Main.window);
            System.out.println("Success!!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

////  ██████╗ ██╗███╗   ██╗ █████╗ ██████╗ ██╗   ██╗    ██╗    ██╗ █████╗ ██████╗ ███████╗ █████╗ ██████╗ ███████╗  //
////  ██╔══██╗██║████╗  ██║██╔══██╗██╔══██╗╚██╗ ██╔╝    ██║    ██║██╔══██╗██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝  //
////  ██████╔╝██║██╔██╗ ██║███████║██████╔╝ ╚████╔╝     ██║ █╗ ██║███████║██████╔╝█████╗  ███████║██████╔╝█████╗    //
////  ██╔══██╗██║██║╚██╗██║██╔══██║██╔══██╗  ╚██╔╝      ██║███╗██║██╔══██║██╔══██╗██╔══╝  ██╔══██║██╔══██╗██╔══╝    //
////  ██████╔╝██║██║ ╚████║██║  ██║██║  ██║   ██║       ╚███╔███╔╝██║  ██║██║  ██║██║     ██║  ██║██║  ██║███████╗  //
////  ╚═════╝ ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝        ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝  //
////                                                                                                                //
//
////   ######## ########    ###    ##     ##       ##   ########     //
////      ##    ##         ## ##   ###   ###     ####   ##    ##     //
////      ##    ##        ##   ##  #### ####       ##       ##       //
////      ##    ######   ##     ## ## ### ##       ##      ##        //
////      ##    ##       ######### ##     ##       ##     ##         //
////      ##    ##       ##     ## ##     ##       ##     ##         //
////      ##    ######## ##     ## ##     ##     ######   ##         //
//
//package backup;
//
//import Runnables.RunnableInterface;
//import com.jfoenix.controls.JFXButton;
//import gameplay.Unit;
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//import javafx.scene.media.AudioClip;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.Paint;
//import javafx.scene.shape.StrokeType;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextBoundsType;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//import menus.Main;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//
//import static database.Variables.*;
//
//public class GameMain extends Application {
//
//
//
//    ////LAYOUT////
//    private static final int boardSize = 7; // 7x7 for example
//    static final int tileSize = 100; //Size(in pixels) of each tile
//    private static Unit[][] unitPosition = new Unit[boardSize][boardSize];
//    private static final int offsetX = 100;
//    private static final int offsetY = 100;
//    private final int descriptionOffsetLeft = 0;
//    private final int descriptionOffsetRight = 0;
//    private final int descriptionOffsetTop = 0;
//    private final int descriptionOffsetBottom = 350;
//    private final int initialWindowSizeX = 1024;
//    private final int initialWindowSizeY = 768;
//
//    ////SCENE ELEMENTS////
//    private Stage window;                               //Main stage for the game.
//    private Scene scene;                                //Scene for second and third phase of the game
//    private HBox root = new HBox();                     //Root container
//    private StackPane pieceContainer = new StackPane(); //Unit and obstacle placement
//    private VBox rSidePanel = new VBox();               //Sidepanel for unit description and End turn button
//    private HBox rSidePanelBtns = new HBox();           //Box for the buttons "end turn" and "surrender"
//    private Label description = new Label();            //Displays unit info when selected
//    private Label phaseLabel = new Label();             //Displays what phase of the game you are in.
//    private JFXButton endTurnButton = new JFXButton("End turn");
//    private JFXButton surrenderButton = new JFXButton("Surrender");
//    private Pane board = new Pane();                    // Holds all the tiles.
//    private Grid grid = new Grid(boardSize, boardSize); //Sets up a grid which is equivalent to boardSize x boardSize.
//    private Label turnCounter = new Label("TURN: " + turn);            //Describes what turn it is.
//
//    ////GAME CONTROL VARIABLES////
//    private int selectedPosX;                                           //Holds the X position to the selected piece.
//    private int selectedPosY;                                           //Holds the Y position to the selected piece.
//    private int moveCounter = 0;                                        // Counter for movement phase.
//    private int attackCount = 0;                                        // Counter for attack phase.
//    private Unit selectedUnit;                                          //Reference to the selected unit. Used for move, attack, etc.
//    private boolean selected = false;                                   // True or false for selected piece.
//    private ArrayList<Move> movementList = new ArrayList<>();           //Keeps track of the moves made for the current turn.
//    private ArrayList<Attack> attackList = new ArrayList<>();           //Keeps track of the attacks made for the current turn.
//    private ArrayList<Move> importedMovementList = new ArrayList<>();   //Keeps track of the moves made during the opponents turn
//    private ArrayList<Attack> importedAttackList = new ArrayList<>();   //Keeps track of the attacks made during the opponents turn
//    private boolean movementPhase = true;                               //Controls if the player is in movement or attack phase
//    private UnitGenerator unitGenerator = new UnitGenerator();
//    ArrayList<PieceSetup> setupPieces;
//    ArrayList<Unit> unitList = new ArrayList<>();
//    private boolean surrendered = false;
//
//    ////AUDIO ELEMENTS////
//    private AudioClip sword = new AudioClip(this.getClass().getResource("/gameplay/assets/hitSword.wav").toString());
//    private AudioClip bow = new AudioClip(this.getClass().getResource("/gameplay/assets/arrow.wav").toString());
//
//    ////STYLING////
//    private String gameTitle = "Paper Legion";
//    private String descriptionFont = "-fx-font-family: 'Arial Black'";
//    private String endTurnButtonBackgroundColor = "-fx-background-color: #000000";
//    private String turnCounterFontSize = "-fx-font-size: 32px";
//    private String phaseLabelFontSize = "-fx-font-size: 32px";
//    private Paint selectionOutlineColor = Color.RED;
//    private Paint endTurnButtonTextColor = Color.WHITE;
//    private Paint movementHighlightColor = Color.GREENYELLOW;
//    private Paint attackHighlightColor = Color.DARKRED;
//
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//        window = primaryStage; // Program window
//
//        // Sets static variables for players and opponent id.
//        db.getPlayers();
//
//        sceneSetUp();
//
//        SetUp setUp = new SetUp();
//        setUp.importUnitTypes();
//
//        //TODO placementPhase code goes here
//        ///Inserts units into DB for game. Only player1 draws the units. This is a temporary filler for the placement phase.
//        if (user_id == player1) {
//            db.insertPieces();
//        } else {
//            int number;
//            do {
//                number = db.pollForUnits();
//                Thread.sleep(5000);
//            } while (number != 10);
//        }
//        createUnits();
//
//        drawUnits();
//
//        //If you are player 2. Start polling the database for next turn.
//        if (!yourTurn) {
//            endTurnButton.setText("Waiting for other player");
//            waitForTurn();
//        } else {
//            //If you are player 1. Enters turn 1 into database.
//            db.sendTurn(turn);
//        }
//
//        ////END TURN HANDLER////
//        endTurnButton.setOnAction(event -> {
//            endTurn();
//        });
//
//        ////SURRENDER HANDLER////
//        surrenderButton.setOnAction(event -> {
//            surrender();
//        });
//
//
//        ///////////////////////////////////SELECTION//////////////////////////////////////////////
//        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//            if (!selected) {
//                select(event, rSidePanel, description);
//            }
//            /////////////////////////////////MOVE/////////////////////////////////////////////////
//            if (event.getClickCount() == 1) {
//                if (selected && movementPhase && event.getButton() == MouseButton.PRIMARY) {
//                    move(event);
//                }
//            }
//            /////////////////////////////////ATTACK///////////////////////////////////////////////
//            if (event.getClickCount() == 2) {
//                if (selected && !movementPhase && !selectedUnit.getHasAttackedThisTurn()) {
//                    attack(event);
//
//                }
//            }
//            //////////////////////////////DESELECT/////////////////////////////////////////////
//            if (event.getButton() == MouseButton.SECONDARY) {
//                deSelect(rSidePanel, description);
//            }
//        });
//
//        window.setTitle(gameTitle);
//        window.setScene(scene);
//        window.show();
//    }
//
//    private void sceneSetUp() {
//        root.getChildren().add(pieceContainer);
//        root.getChildren().add(rSidePanel);
//        root.setSpacing(150);
//        root.setPadding(new Insets(offsetY, offsetX, offsetY, offsetX));
//
//        pieceContainer.setAlignment(Pos.BASELINE_LEFT); //Only baseline_Left is correct according to positions.
//        pieceContainer.setPrefWidth(900);
//
//        board.getChildren().add(grid); //Insert grid from Grid class.
//        pieceContainer.getChildren().add(board);  //Legger alle tiles til i stackpane som blir lagt til scenen.
//
//        turnCounter.setStyle(turnCounterFontSize);
//
//        endTurnButton.setMinWidth(175);
//        endTurnButton.setMinHeight(75);
//        endTurnButton.setTextFill(endTurnButtonTextColor);
//        endTurnButton.setStyle(endTurnButtonBackgroundColor);
//
//        description.setStyle(descriptionFont);
//        description.setPadding(new Insets(descriptionOffsetTop, descriptionOffsetRight, descriptionOffsetBottom, descriptionOffsetLeft));
//
//        surrenderButton.setMinWidth(175);
//        surrenderButton.setMinHeight(75);
//        surrenderButton.setTextFill(Color.WHITE);
//        surrenderButton.setStyle("-fx-background-color: #000000");
//
//        rSidePanelBtns.getChildren().addAll(endTurnButton,surrenderButton);
//        rSidePanelBtns.setSpacing(5);
//        rSidePanelBtns.setAlignment(Pos.CENTER);
//
//        description.setStyle("-fx-font-family: 'Arial Black'");
//        description.setPadding(new Insets(0, 0, 350, 0));
//
//        phaseLabel.setText("MOVEMENT PHASE");
//        phaseLabel.setStyle(phaseLabelFontSize);
//
//        rSidePanel.getChildren().addAll(phaseLabel,turnCounter, rSidePanelBtns);
//        rSidePanel.setPrefWidth(650);
//        rSidePanel.setSpacing(10);
//        rSidePanel.setAlignment(Pos.BOTTOM_CENTER);
//
//        // IF INSERTS ARE ADDED THEN REMEMBER THAT THE OFFSET VALUE HAS TO WORK WITH THE TILES AND PIECES POSITION.
//        //window.widthProperty().addListener();
//
//        scene = new Scene(root, initialWindowSizeX, initialWindowSizeY);
//    }
//
//    private void endTurn() {
//        if (yourTurn) {
//
//            //Increments turn. Opponents Turn.
//            turn++;
//
//            turnCounter.setText("TURN: " + turn);
//            endTurnButton.setText("Waiting for other player");
//            yourTurn = false;
//
//
//            ////SEND MOVEMENT////
//
//            if (movementList.size() != 0) {
//                System.out.println("SENDING MOVE LIST!");
//
//                //COMMENTED OUT DURING BACKUP
//                //db.exportMoveList(movementList); //when we use movement table use this
//
//                ////Old method////
//                //db.exportPieceMoveList(movementList);
//                movementList = new ArrayList<>(); //Resets the movementList for the next turn.
//            }
//
//
//            /////SEND ATTACKS////
//
//            if (attackList.size() != 0) {
//
//                //COMMENTED OUT DURING BACKUP
//                //db.exportAttackList(attackList);
//                attackList = new ArrayList<>(); //Resets the attackList for the next turn.
//            }
//
//            // Finds every enemy unit that was damaged and sends their new info the database.
//
//
//            //Add the next turn into the database.
//            db.sendTurn(turn);
//
//            //de-selects the currently selected unit
//            deSelect(rSidePanel, description);
//
//            //Resets hasAttackedThisTurn for all units
//            for (int i = 0; i < unitList.size(); i++) {
//
//                unitList.get(i).setHasAttackedThisTurn(false);
//
//            }
//
//            //Check if you have won or lost.
//            checkForGameOver();
//
//            //Wait for you next turn. Does not trigger if you have surrendered.
//            if (!surrendered) {
//                waitForTurn();
//            }
//        }
//    }
//
//    private void surrender() {
//        Stage confirm_alert = new Stage();
//        confirm_alert.initModality(Modality.APPLICATION_MODAL);
//        confirm_alert.setTitle("Game over!");
//
//        Text surrender_text = new Text();
//        surrender_text.setText("Are you sure?");
//        surrender_text.setStyle("-fx-font-size:32px;");
//
//        JFXButton surrender_yes = new JFXButton("Yes");
//        JFXButton surrender_no = new JFXButton("No");
//
//        surrender_yes.setOnAction(event -> {
//            db.surrenderGame();
//            surrendered = true;
//
//            if (yourTurn) {
//                endTurn();
//            } else {
//                checkForGameOver();
//            }
//            confirm_alert.close();
//        });
//
//        surrender_no.setOnAction(event -> {
//            confirm_alert.close();
//        });
//
//        HBox buttons = new HBox();
//        buttons.getChildren().addAll(surrender_yes, surrender_no);
//        buttons.setAlignment(Pos.CENTER);
//        buttons.setSpacing(50);
//
//        VBox content = new VBox();
//        content.setAlignment(Pos.CENTER);
//        content.setSpacing(20);
//
//        content.getChildren().addAll(surrender_text, buttons);
//        Scene scene = new Scene(content, 250, 150);
//        confirm_alert.initStyle(StageStyle.UNDECORATED);
//        confirm_alert.setScene(scene);
//        confirm_alert.showAndWait();
//    }
//
//    private void createUnits() {
//        setupPieces = db.importPlacementPieces();
//        for (int i = 0; i < setupPieces.size(); i++) {
//
//            int pieceId = setupPieces.get(i).getPieceId();
//            int playerId = setupPieces.get(i).getPlayerId();
//            int positionX = setupPieces.get(i).getPositionX();
//            int positionY = setupPieces.get(i).getPositionY();
//            int unitType_id = setupPieces.get(i).getUnit_type_id();
//
//            boolean enemyStatus;
//            if (playerId == user_id) {
//                enemyStatus = false;
//            } else {
//                enemyStatus = true;
//            }
//
//            if (unitType_id == 1) {
//                unitList.add(new Unit(positionY * tileSize, positionX * tileSize, enemyStatus, unitGenerator.newSwordsMan(), pieceId));
//
//            } else if (unitType_id == 2) {
//                unitList.add(new Unit(positionY * tileSize, positionX * tileSize, enemyStatus, unitGenerator.newArcher(), pieceId));
//            }
//        }
//    }
//
//
//    private void drawUnits() {
//        ArrayList<PieceSetup> pieces = db.importPlacementPieces();
//        int posX;
//        int posY;
//
//        ///////////////////////////////LOAD ALL PIECES ONTO BOARD ///////////////////////////////
//        for (int i = 0; i < unitList.size(); i++) {
//            System.out.println(i + " playerid: " + pieces.get(i).getPlayerId() + " pos X: " + pieces.get(i).getPositionX() + " pos y: "
//                    + pieces.get(i).getPositionY() + " hp: " + pieces.get(i).getCurrent_health() + " piece id: "
//                    + pieces.get(i).getPieceId());
//            if (unitList.get(i).getHp() > 0) {
//
//                PieceSetup correspondingPiece = null;
//
//                for (int j = 0; j < pieces.size(); j++) {
//                    if (unitList.get(i).getEnemy()) {
//                        if (pieces.get(j).getPlayerId() != user_id && pieces.get(j).getPieceId() == unitList.get(i).getPieceID()) {
//                            correspondingPiece = pieces.get(j);
//                            System.out.println(i + " is enemy " + j + " in this position in Pieces");
//
//                        }
//                    } else {
//                        if (pieces.get(j).getPlayerId() == user_id && pieces.get(j).getPieceId() == unitList.get(i).getPieceID()) {
//                            correspondingPiece = pieces.get(j);
//                            System.out.println(i + " is friendly " + j + " in this position in Pieces");
//                        }
//                    }
//                }
//
//                if (correspondingPiece != null) {
//
//                    unitList.get(i).setHasAttackedThisTurn(false);
//                    unitList.get(i).setHp(correspondingPiece.getCurrent_health());
//                    unitList.get(i).setPosition(correspondingPiece.getPositionX(), correspondingPiece.getPositionY());
//                    if (unitList.get(i).getHp() > 0) {
//                        pieceContainer.getChildren().add(unitList.get(i).getPieceAvatar());
//                        posX = unitList.get(i).getPositionX();
//                        posY = unitList.get(i).getPositionY();
//                        unitPosition[posY][posX] = unitList.get(i);
//                    }
//
//                }
//            }
//            System.out.println(i + " enemy: " + unitList.get(i).getEnemy() + " pos X: " + unitList.get(i).getPositionX() + " pos y: "
//                    + unitList.get(i).getPositionY() + " hp: " + unitList.get(i).getHp() + " piece id: "
//                    + unitList.get(i).getPieceID());
//        }
//        ///////////////////////////////////////////////////////////////////////////////////
//    }
//
//    public void deDrawUnits() {
//        for (int i = 0; i < unitList.size(); i++) {
//            pieceContainer.getChildren().remove(unitList.get(i).getPieceAvatar());
//        }
//
//
//        for (int i = 0; i < unitPosition.length; i++) {
//            for (int j = 0; j < unitPosition[i].length; j++) {
//                unitPosition[i][j] = null;
//            }
//        }
//        System.out.println(pieceContainer.getChildren().size());
//    }
//
//    private void select(MouseEvent event, VBox vBox, Label description) {
//
//        int posX = getPosXFromEvent(event);
//        int posY = getPosYFromEvent(event);
//
//
//        if (!(posX > boardSize || posY > boardSize || posX < 0 || posY < 0) && (unitPosition[posY][posX] != null) && !unitPosition[posY][posX].getEnemy() && unitPosition[posY][posX].getHp() > 0) {
//
//            unitPosition[posY][posX].setPosition((int) (unitPosition[posY][posX].getTranslateX() / tileSize), (int) (unitPosition[posY][posX].getTranslateY() / tileSize));
//            unitPosition[posY][posX].setStrokeType(StrokeType.INSIDE);
//            unitPosition[posY][posX].setStrokeWidth(3);
//            unitPosition[posY][posX].setStroke(selectionOutlineColor);
//
//            selected = true;
//
//            selectedPosX = posX;
//            selectedPosY = posY;
//
//            selectedUnit = unitPosition[selectedPosY][selectedPosX];
//
//            //Decides based on the phase and whether or not it's your turn, what will happen when you select a unit.
//            //If it's not your turn, you will have the ability to see the units info and description, but not the movement/attach highlight
//            if (yourTurn) {
//                if (movementPhase) {
//                    highlightPossibleMoves();
//                } else if (!selectedUnit.getHasAttackedThisTurn()) {
//                    highlightPossibleAttacks();
//                }
//            }
//
//            description.setText(selectedUnit.getDescription());
//            vBox.getChildren().add(description);
//            description.toBack();
//
//        }
//    }
//
//    private void deSelect(VBox sidePanel, Label description) {
//
//        for (int i = 0; i < unitPosition.length; i++) {
//            for (int j = 0; j < unitPosition[i].length; j++) {
//                if (unitPosition[i][j] != null) {
//                    unitPosition[i][j].setStroke(Color.TRANSPARENT);
//                }
//            }
//        }
//
//        sidePanel.getChildren().remove(description);
//        selectedUnit = null;
//        selected = false;
//        clearHighlight();
//    }
//
//    private void move(MouseEvent event) {
//        if (yourTurn) {
//            int nyPosX = getPosXFromEvent(event);
//            int nyPosY = getPosYFromEvent(event);
//
//            if (movementRange(nyPosX, nyPosY)) {
//                if (unitPosition[nyPosY][nyPosX] == null) {
//
//                    ////ADDS A NEW MOVE TO THE MOVE LIST////
//                    movementList.add(new Move(turn, selectedUnit.getPieceID(), match_id, selectedUnit.getPositionX(), selectedUnit.getPositionY(), nyPosX, nyPosY));
//
//                    selectedUnit.setTranslate(nyPosX, nyPosY);
//                    clearHighlight();
//
//                    ////EXECUTES THE MOVE////
//                    unitPosition[nyPosY][nyPosX] = selectedUnit;
//                    unitPosition[selectedPosY][selectedPosX] = null;
//
//                    selectedPosX = nyPosX;
//                    selectedPosY = nyPosY;
//
//                    selectedUnit.setPosition(nyPosX, nyPosY);
//
//                    moveCounter++;
//
//                    ////CHANGES PHASE////
//                    movementPhase = false;
//                    phaseLabel.setText("ATTACK PHASE");
//                    clearHighlight();
//                    highlightPossibleAttacks();
//                }
//            }
//        }
//    }
//
//    private void attack(MouseEvent event) {
//
//        int attackPosX = getPosXFromEvent(event);
//        int attackPosY = getPosYFromEvent(event);
//
//
//        if (yourTurn) {
//
//            // If there is a unit on the selected tile.
//            if (unitPosition[attackPosY][attackPosX] != null && unitPosition[attackPosY][attackPosX].getHp() > 0) {
//                // If within attack range.
//                if (attackRange(attackPosX, attackPosY)) {
//                    // If attacked unit is not itself.
//                    if (selectedUnit != unitPosition[attackPosY][attackPosX] && unitPosition[attackPosY][attackPosX].getEnemy()) {
//                        // Attack is executed and unit takes damage.
//                        unitPosition[attackPosY][attackPosX].takeDamage(selectedUnit.getAttack());
//
//                        //Adds an attack to the attack list
//                        //This is used to transfer the attack to the other player
//                        attackList.add(new Attack(turn, match_id, user_id, selectedUnit.getPieceID(), unitPosition[attackPosY][attackPosX].getPieceID(), selectedUnit.getAttack()));
//
//                        attackCount++;
//
//                        //Plays audio cue for each type.
//                        if (selectedUnit.getType().equalsIgnoreCase("Swordsman")) {
//                            sword.play();
//                        } else if (selectedUnit.getType().equalsIgnoreCase("Archer")) {
//                            bow.play();
//                        }
//                        //db.sendHealthInfo(unitPosition[attackPosY][attackPosX].getPieceID(), unitPosition[attackPosY][attackPosX].getHp());
//
//
//                        //If units health is zero. Remove it from the board.
//                        if (unitPosition[attackPosY][attackPosX].getHp() <= 0) {
//                            pieceContainer.getChildren().removeAll(unitPosition[attackPosY][attackPosX].getPieceAvatar());
//                            //unitPosition[attackPosY][attackPosX].setHp(0);
//                            for (int i = 0; i < unitList.size(); i++) {
//                                if (attackPosX == unitList.get(i).getPositionX() && attackPosY == unitList.get(i).getPositionY()) {
//                                    //unitList.remove(i);
//                                }
//                            }
//                            unitPosition[attackPosY][attackPosX] = null;
//                            unitList.removeAll(Collections.singletonList(null));
//                        }
//
//                        selectedUnit.setHasAttackedThisTurn(true);
//                        clearHighlight();
//                    }
//                }
//            }
//        }
//    }
//
//
//    private void highlightPossibleMoves() {
//        //Goes through the grid and paints all the squared that is within movement range. Does not paint itself or ones containing units or obstacles.
//        for (int i = 0; i < unitPosition.length; i++) {
//            for (int j = 0; j < unitPosition[i].length; j++) {
//                if (unitPosition[i][j] == null && unitPosition[i][j] != selectedUnit) {
//                    // Currently shows swordsman attack range wrong.
//                    //(((Math.abs(selectedPosX - unitPosition[i][j].getTranslateX() / tileSize)) + Math.abs(selectedPosY - unitPosition[i][j].getTranslateY() / tileSize)) <= selectedUnit.getMaxAttackRange())
//                    //                            && ((Math.abs(selectedPosX - unitPosition[i][j].getTranslateX() / tileSize)) + Math.abs(selectedPosY - unitPosition[i][j].getTranslateY() / tileSize)) >= selectedUnit.getMinAttackRange()
//                    if (movementRange(j, i)) {
//                        grid.liste[i][j].setFill(movementHighlightColor);
//                    }
//                }
//            }
//        }
//    }
//
//
//    private void highlightPossibleAttacks() {
//        // Uses attackrange to paint possible attacks. Changes can be done by editing attackRange.
//        for (int i = 0; i < unitPosition.length; i++) {
//            for (int j = 0; j < unitPosition[i].length; j++) {
//                if (unitPosition[i][j] != null && unitPosition[i][j] != selectedUnit) {
//                    if (attackRange(j, i)) {
//                        if (unitPosition[i][j].getEnemy()) {
//                            grid.liste[i][j].setFill(attackHighlightColor);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void clearHighlight() {
//        for (int i = 0; i < grid.liste.length; i++) {
//            for (int j = 0; j < grid.liste[i].length; j++) {
//                grid.liste[i][j].setFill(Color.TRANSPARENT);
//            }
//        }
//    }
//
//    //Calculates the movement range for the selected unit. Used for movement and highlight movement.
//    private boolean movementRange(int nyPosX, int nyPosY) {
//        return Math.abs(nyPosX - selectedPosX) + Math.abs(nyPosY - selectedPosY) <= selectedUnit.getMovementRange();
//    }
//
//    //Returns true if enemy is within attack range of the selected unit.
//    private boolean attackRange(int nyPosX, int nyPosY) {
//
//        double attackLocation = Math.abs(nyPosX - selectedPosX) + Math.abs(nyPosY - selectedPosY);
//
//        return selectedUnit.getMinAttackRange() <= attackLocation && attackLocation <= selectedUnit.getMaxAttackRange();
//
//    }
//
//    private int getPosXFromEvent(MouseEvent event2) {
//        double rectPosX1 = tileSize + offsetX;
//        double posX1 = event2.getSceneX();
//        double movementX1 = posX1 - rectPosX1;
//        return (int) (Math.ceil(movementX1 / tileSize)); // Runder til nærmeste 100 for snap to grid funksjonalitet
//    }
//
//    private int getPosYFromEvent(MouseEvent event2) {
//        double rectPosY1 = tileSize + offsetY;
//        double posY1 = event2.getSceneY();
//        double movementY1 = posY1 - rectPosY1;
//        return (int) (Math.ceil(movementY1 / tileSize)); // Runder til nærmeste 100 for snap to grid funksjonalitet
//    }
//
//    public void waitForTurn() {
//        // Runnable lambda implementation for turn waiting with it's own thread
//        waitTurnRunnable = new RunnableInterface() {
//            private boolean doStop = false;
//
//            @Override
//            public void run() {
//                while (keepRunning()) {
//                    try {
//                        while (!yourTurn) {
//                            //Wait time for polling.
//                            System.out.println("Sleeps thread " + Thread.currentThread());
//                            Thread.sleep(1000);
//                            //When player in database matches your own user_id it is your turn again.
//                            int getTurnPlayerResult = db.getTurnPlayer();
//                            // If its your turn or you have left the game.
//                            System.out.println("Whose turn is it? " + getTurnPlayerResult);
//                            if (getTurnPlayerResult == user_id || getTurnPlayerResult == -1) {
//                                yourTurn = true;
//                                this.doStop();
//                            }
//                        }
//
//                        //What will happen when it is your turn again.
//                        Platform.runLater(() -> {
//                            setUpNewTurn();
//                        });
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public synchronized void doStop() {
//                this.doStop = true;
//            }
//
//            @Override
//            public synchronized boolean keepRunning() {
//                return !this.doStop;
//            }
//        };
//
//        waitTurnThread = new Thread(waitTurnRunnable);
//        waitTurnThread.start();
//
//    }
//
//    //Contains everything that needs to be done when the turn is returned to the player.
//    private void setUpNewTurn() {
//        deSelect(rSidePanel, description);
//        selectedUnit = null;
//
//        turn++;
//        movementPhase = true;
//        phaseLabel.setText("MOVEMENT PHASE");
//        turnCounter.setText("TURN: " + turn);
//        endTurnButton.setText("End turn");
//
//        importedMovementList = db.importMoveList(turn - 1, match_id);
//        importedAttackList = db.importAttackList(turn - 1, match_id, opponent_id);
//
//        System.out.println("importedAttackList size is: " + importedAttackList.size());
//
//        ////EXECUTES MOVES FROM OPPONENTS TURN////
//        for (int i = 0; i < importedMovementList.size(); i++) {
//
//            Unit movingUnit = unitPosition[importedMovementList.get(i).getStartPosY()][importedMovementList.get(i).getStartPosX()];
//
//            movingUnit.setPosition(importedMovementList.get(i).getEndPosX(), importedMovementList.get(i).getEndPosY());
//
//            ////EXECUTES THE MOVE////
//            unitPosition[importedMovementList.get(i).getEndPosY()][importedMovementList.get(i).getEndPosX()] = movingUnit;
//            unitPosition[importedMovementList.get(i).getStartPosY()][importedMovementList.get(i).getStartPosX()] = null;
//        }
//
//        ////EXECUTES ATTACKS FROM OPPONENTS TURN////
//        for (int i = 0; i < importedAttackList.size(); i++) {
//
//            for (int j = 0; j < unitList.size(); j++) {
//
//                if (!unitList.get(j).getEnemy() && unitList.get(j).getPieceID() == importedAttackList.get(i).getReceiverPieceID()) {
//                    System.out.println(importedAttackList.get(i).getDamage());
//
//                    System.out.println("DOING AN ATTACK!" + unitList.get(j).getHp());
//
//                    unitList.get(j).takeDamage(importedAttackList.get(i).getDamage());
//
//                    //If units health is zero. Remove it from the board.
//                    if (unitList.get(j).getHp() <= 0) {
//                        pieceContainer.getChildren().removeAll(unitList.get(j).getPieceAvatar());
//
//                        unitPosition[unitList.get(j).getPositionY()][unitList.get(j).getPositionX()] = null;
//                        unitList.remove(j);
//                        unitList.removeAll(Collections.singletonList(null));
//                    }
//                }
//
//            }
//        }
//        checkForGameOver();
//    }
//
//
//    @Override
//    public void stop() {
//        // Executed when the application shuts down. User is logged out and database connection is closed.
//        surrender();
//        Main.closeAndLogout();
//    }
//
//    // Opens the win/lose pop-up on the screen and ends the game.
//    public void checkForGameOver() {
//        String win_loseText;
//        String gameSummary = "";
//        int loser = -1;
//        int eliminationResult = checkForEliminationVictory();
//        int surrenderResult = db.checkForSurrender();
//
//        if (eliminationResult != -1) {
//            gameSummary = "The game ended after a player's unit were all eliminated after " + turn + " turns\n";
//            loser = eliminationResult;
//        } else if (surrenderResult == user_id || surrenderResult == opponent_id) {
//            gameSummary = "The game ended after a player surrendered the match after " + turn + " turns\n";
//            loser = surrenderResult;
//        }
//
//        if (loser != -1) {
//            //Game is won or lost.
//            gameCleanUp();
//            //Open alert window.
//            Stage winner_alert = new Stage();
//            //winner_alert.initModality(Modality.APPLICATION_MODAL);
//            winner_alert.setTitle("Game over!");
//
//            Text winnerTextHeader = new Text();
//            Text winnerText = new Text();
//            winnerTextHeader.setStyle("-fx-font-size:32px;");
//            winnerTextHeader.setBoundsType(TextBoundsType.VISUAL);
//            db.incrementGamesPlayed();
//
//            if (loser == user_id) {
//                win_loseText = "You Lose!\n";
//            } else if (loser == opponent_id) {
//                win_loseText = "You Win!\n";
//                db.incrementGamesWon();
//            } else {
//                win_loseText = "Something went wrong\n";
//            }
//
//            winnerTextHeader.setText(win_loseText);
//            winnerText.setText(gameSummary);
//
//            JFXButton endGameBtn = new JFXButton("Return to menu");
//
//            endGameBtn.setOnAction(event -> {
//                String fxmlDir = "/menus/View/mainMenu.fxml";
//                Parent root = null;
//                try {
//                    root = FXMLLoader.load(this.getClass().getResource(fxmlDir));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    System.out.println("load failed");
//                }
//                winner_alert.close();
//                Main.window.setScene(new Scene(root));
//            });
//
//            VBox content = new VBox();
//            content.setAlignment(Pos.CENTER);
//            content.setSpacing(20);
//            content.getChildren().addAll(winnerTextHeader, winnerText, endGameBtn);
//            Scene scene = new Scene(content, 450, 200);
//            winner_alert.initStyle(StageStyle.UNDECORATED);
//            winner_alert.setScene(scene);
//            winner_alert.showAndWait();
//        }
//    }
//
//    // method that checks if the game has been won by eliminating other player's units. Returns the user_id of the winning player.
//    private int checkForEliminationVictory() {
//        int yourPieces = 0;
//        int opponentsPieces = 0;
//        //Goes through all units and counts how many are alive for each player.
//        for (int i = 0; i < unitList.size(); i++) {
//            if (unitList.get(i).getHp() > 0) {
//                if (unitList.get(i).getEnemy()) {
//                    opponentsPieces++;
//                } else {
//                    yourPieces++;
//                }
//            }
//        }
//        if (yourPieces == 0) {
//            return opponent_id;
//        } else if (opponentsPieces == 0) {
//            return user_id;
//        } else {
//            return -1;
//        }
//    }
//
//    private void gameCleanUp() {
//        //Stuff that need to be closed or reset. Might not warrant its own method.
//        if (waitTurnThread != null) {
//            waitTurnThread.stop();
//        }
//        //Resets turns to 1 for next game.
//        turn = 1;
//    }
//
//    public void main(String[] args) {
//        System.out.println("SHUTDOWN HOOK CALLED");
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            Main.closeAndLogout();
//        }));
//        launch(args);
//    }
//}

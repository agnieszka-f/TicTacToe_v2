import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TicTacToe extends Application {
    private VBox vBox;
    private MenuBar menuBar;
    private Menu menu1;
    private Menu menu2;
    private MenuItem newGameMenuItem;
    private MenuItem closeMenuItem;
    private MenuItem aboutMenuItem;
    private Button [][] buttons;
    private GridPane gridPane;
    private Label labelInfo;
    private Label firstMove;
    private Button buttonFirstHuman;
    private Button buttonFirstComputer;
    private BorderPane borderPane;
    private Board board;
    private int countOfMove;

    @Override
    public void start(Stage primaryStage) throws Exception {
        board = new Board();
        board.initBoard();
        countOfMove = board.getSize() * board.getSize();

        Scene scene = new Scene(initVBox(), 300,360);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Kółko i krzyżyk");
        primaryStage.show();
        initEventMenuItem();
        firstMoveSlection();
    }
    public static void main(String[] args){
        launch(args);
    }
    private void buttonsAction(){
        for(int i = 0; i < buttons.length; i++){
            for(int j = 0; j < buttons[i].length; j++){
                final int x = i, y = j;
                buttons[i][j].setOnAction( event -> playerAction(x,y));
            }
        }
    }
    private void playerAction(int x, int y){
        if(board.playerMove(new Coordy(x, y))){
            if(board.getActivePlayer() == board.HUMAN && countOfMove > 0) {
                buttons[x][y].setText("X");
                buttons[x][y].setDisable(true);
                countOfMove--;
                if (board.checkWin(board.getActivePlayer()) == false && countOfMove >0) {
                    labelInfo.setText("Ruch komputera");
                    board.switchActivePlayer();
                    computerAction();
                } else {
                    reflectWinningStateOnBoard();
                }
            }
        }
    }
    private void computerAction(){
        if(board.getActivePlayer() == board.COMPUTER ) {
            Coordy coordy = board.computerBlockMove();

            buttons[coordy.getX()][coordy.getY()].setText("O");
            buttons[coordy.getX()][coordy.getY()].setDisable(true);
            countOfMove--;

            if(board.checkWin(board.getActivePlayer()) == false && countOfMove>0) {
                labelInfo.setText("Twój ruch");
                board.switchActivePlayer();
            } else { reflectWinningStateOnBoard(); }
        }
    }
    private void buttonsClean(){
        for(int i =0; i<buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setDisable(false);
                buttons[i][j].setTextFill(Color.BLACK);
            }
        }
    }
    private void reflectWinningStateOnBoard(){
        disableBoardButtons();
        highlightWinningLine();
    }
    private void disableBoardButtons(){
        for(int i =0; i<buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setDisable(true);
            }
        }
    }
    private void availableBoardButtons(){
        for(int i =0; i<buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setDisable(false);
            }
        }
    }
    private void highlightWinningLine(){
        List temp = board.getWinnerList();
        if(temp.isEmpty()){
            labelInfo.setText("Remis!!!");
        } else {
            for(int i=0; i<temp.size(); i++){
                Coordy t = (Coordy) temp.get(i);
                buttons[t.getX()][t.getY()].setTextFill(Color.GREEN);
            }
            labelInfo.setText("Wygrana!!!");
        }
    }
    private MenuBar initMenuBar(){
        menuBar = new MenuBar();
        menu1 = new Menu("Edycja");
        menu2 = new Menu("Pomoc");
        newGameMenuItem = new MenuItem("Nowa gra");
        closeMenuItem = new MenuItem("Zamknij");
        menu1.getItems().addAll(newGameMenuItem,closeMenuItem);
        aboutMenuItem = new MenuItem("O grze");
        menu2.getItems().addAll(aboutMenuItem);
        menuBar.getMenus().addAll(menu1,menu2);

        return menuBar;
    }
    private GridPane initGridPane(){
        gridPane=new GridPane();
        initButtons();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxHeight(Region.USE_COMPUTED_SIZE);
        gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);

        return gridPane;
    }
    private void initButtons(){
        buttons = new Button[board.getSize()][board.getSize()];
        for(int i =0; i<buttons.length; i++){
            for(int j=0; j<buttons[i].length; j++){
                buttons[i][j] = new Button("");
                buttons[i][j].setMinWidth(Region.USE_COMPUTED_SIZE);
                buttons[i][j].setMaxWidth(Region.USE_COMPUTED_SIZE);
                buttons[i][j].setMinHeight(Region.USE_COMPUTED_SIZE);
                buttons[i][j].setMaxHeight(Region.USE_COMPUTED_SIZE);
                buttons[i][j].setPrefWidth(100);
                buttons[i][j].setPrefHeight(100);
                buttons[i][j].setFont(Font.font("Arial", FontWeight.BOLD, 40));
                buttons[i][j].setTextFill(Color.BLACK);
                buttons[i][j].setDisable(true);
                gridPane.addRow(i, buttons[i][j]);
            }
        }
    }
    private VBox initVBox(){
        vBox = new VBox();
        vBox.getChildren().addAll(initMenuBar(),initBorderPane(),initGridPane(),initLabelInfo());

        return vBox;
    }
    private void initEventMenuItem(){
        newGameMenuItem.addEventHandler(ActionEvent.ACTION,  event -> {
                labelInfo.setText("");
                board = new Board();
                board.initBoard();
                countOfMove = board.getSize() * board.getSize();
                buttonsClean();
                disableBoardButtons();
                setAvailableButtonsFirstMove();
                firstMoveSlection();
            });

        closeMenuItem.addEventHandler(ActionEvent.ACTION,  event -> Platform.exit());
    }
    private Label initLabelInfo(){
        labelInfo = new Label("");
        return labelInfo;
    }
    private void firstMoveSlection(){

        buttonFirstHuman.setOnAction(event -> {
            availableBoardButtons();
            buttonsAction();
            labelInfo.setText("Twój ruch");
            setDisableButtonsFirstMove(Board.HUMAN);
        });
        buttonFirstComputer.setOnAction(event -> {
            board.setActivePlayer(board.COMPUTER);
            labelInfo.setText("Ruch komputera");
            availableBoardButtons();
            computerAction();
            buttonsAction();
            setDisableButtonsFirstMove(Board.COMPUTER);
        });
    }
    private BorderPane initBorderPane(){
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(15, 20, 10, 10));
        buttonFirstHuman = new Button("Ja");
        buttonFirstComputer = new Button("Komputer");
        firstMove = new Label("Kto ma pierwszy ruch?");
        borderPane.setLeft(firstMove);
        borderPane.setCenter(buttonFirstHuman);
        borderPane.setRight(buttonFirstComputer);
        return borderPane;
    }
    private void setAvailableButtonsFirstMove(){
        buttonFirstHuman.setDisable(false);
        buttonFirstComputer.setDisable(false);
        buttonFirstComputer.setTextFill(Color.BLACK);
        buttonFirstHuman.setTextFill(Color.BLACK);
    }
    private void setDisableButtonsFirstMove(int player){
        buttonFirstHuman.setDisable(true);
        buttonFirstComputer.setDisable(true);
        if(player == Board.HUMAN){
            buttonFirstHuman.setTextFill(Color.RED);
        } else { buttonFirstComputer.setTextFill(Color.RED); }
    }
}

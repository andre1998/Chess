package ax.ha.it.chess;


import ax.ha.it.chessgame.board.*;
import ax.ha.it.chessgame.pieces.*;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
public class MainApp extends Application {
    
    private Board board;
    
    @Override
    public void start(Stage stage) {
        
        BorderPane root = new BorderPane();
        Board gameBoard = new Board();
       
        // Will reset the game board
        MenuItem reset = new Menu("New Game");
        reset.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                gameBoard.clearGameState();
            }
        });
        
        Menu menu = new Menu("Reset");
        menu.getItems().add(reset);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        root.setTop(menuBar);
        root.setCenter(gameBoard);
        root.maxHeightProperty().bind( root.heightProperty());
        root.maxHeightProperty().bind( root.widthProperty());
        
        Scene scene = new Scene(root, 800, 825);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }

    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ax.ha.it.chess.Dialog;

import ax.ha.it.chessgame.board.Board;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author André
 */
public class PostGame extends Dialog {
    //String winner;
    //int result;
    
    public PostGame(String winner,  int result, Board board) {
        setTitle("Result Screen");
        GridPane grid = new GridPane();
        
        Text text = new Text();
        text.setText(winner+" Wins !");
        
        Button exit = new Button("Exit Game");
        exit.setOnAction((event) -> {    // lambda expression
            System.exit(0);
        });
        
        Button reset = new Button("New Game");
        reset.setOnAction( e -> newGame(board));
        
        grid.add(text, 1, 0);
        grid.add(exit, 0, 2);
        grid.add(reset, 2, 2);
        
        getDialogPane().setContent(grid);    
        close();
    }

    private void newGame(Board board) {
        //board.clearGameState();
        this.setResult(Boolean.TRUE);
        this.close();
    }
    
}

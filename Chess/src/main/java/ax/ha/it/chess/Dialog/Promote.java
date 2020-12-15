/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ax.ha.it.chess.Dialog;

import ax.ha.it.chessgame.pieces.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
/**
 *
 * @author André
 */
public class Promote extends Dialog {
    
    public Promote(Pawn pawn) {
        setTitle("Promote "+pawn.getColor()+" Pawn");
        
        Button queen = new Button();
        queen.setGraphic(new ImageView(new Queen(pawn.getColor()).getImage()));
        
        Button knight = new Button();
        knight.setGraphic(new ImageView(new Knight(pawn.getColor()).getImage()));
        
        Button rook = new Button();
        rook.setGraphic(new ImageView(new Rook(pawn.getColor()).getImage()));
        
        Button bishop = new Button();
        bishop.setGraphic(new ImageView(new Bishop(pawn.getColor()).getImage()));
        
        queen.setOnAction( e -> returnPromote(new Queen(pawn.getColor())));
        knight.setOnAction( e -> returnPromote(new Knight(pawn.getColor())));
        rook.setOnAction( e -> returnPromote(new Rook(pawn.getColor())));
        bishop.setOnAction( e -> returnPromote(new Bishop(pawn.getColor())));
        
        HBox hbox = new HBox(queen, knight, rook, bishop);
        
        getDialogPane().setContent(hbox);    
    }
    
    private void returnPromote(Piece piece) {
        this.setResult(piece);
        this.close();
    }
     
}

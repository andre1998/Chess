/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ax.ha.it.chessgame.board;

import ax.ha.it.chessgame.pieces.Piece;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author André
 */
public class Square extends Button {

    private Piece piece;
    private int x;
    private int y;
    private final String highlight = "-fx-background-color: palegreen";
    private final String highlightKill = "-fx-background-color: darkred";
    private final String background;
    private Board board;
    /**
     * Constructor which creates a square for the chess board,
     * @param squareColor sets the square to 1 of 2 colors, this is used to help with movement regarding bishops
     * @param piece 
     */
    public Square(Board board, int x, int y, Piece piece, boolean check) {
        super();
        
        this.board = board;
        this.x = x;
        this.y = y;
        this.piece = piece;
        this.setGraphic(null);
        
        if (check) {
            this.background = "-fx-background-color: white";
        } else {
            this.background = "-fx-background-color: black";
        }
        this.setStyle(background);
    }

    public Board getBoard() {
        return this.board;
    }
    
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
    public Piece movePiece() {
        Piece tmp = this.piece;
        setPiece(null);
        return tmp;
    }
    
    public void resetBackGround() {
        this.setStyle(background);
    }
    
    public void setHighlightKill() {
        this.setStyle(highlightKill);
    }
    
    public void setHighlight() {
        this.setStyle(highlight);
    }
    
    /**
     * 
     * @return Returns the current chess piece in this square
     */
    public Piece getPiece() {
        return this.piece;
    }
    
    /**
     * 
     * @param piece Sets a new chess piece in the current square and inserts the pieces image
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
        if (this.piece != null) {
            this.setGraphic(new ImageView(piece.getImage()));
        } else {
            this.setGraphic(new ImageView());
        }
    }
    
}

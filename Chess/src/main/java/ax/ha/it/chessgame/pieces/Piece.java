/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ax.ha.it.chessgame.pieces;

import ax.ha.it.chessgame.board.Board;
import ax.ha.it.chessgame.board.Square;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 *
 * @author André
 */
public abstract class Piece {
    
    final protected String type;
    final protected String color;
    protected int turnMoved;
    protected int count;
    /**
    *   Constructor for a chess piece
    *   @param color    the player who owns this piece
    **/
    public Piece(String color, String type) {
        this.color = color;
        this.type = type;
        this.turnMoved = -1;
        this.count = 0;
    }
    
    public void setTurnMoved(int current) {
        this.turnMoved = current;
        this.count += 1;
    }
    
    public String getType() {
        return this.type;
    }
    
    /**
    *   Function which returns this chess piece color
    *   @return a string which corresponds to either player
    **/ 
    public String getColor() {
        return this.color;
    }
    
    public int getTurnMoved() {
        return this.turnMoved;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public boolean valid(int x, int y) {
        if ((x >= 0 && x <= 7) && (y >= 0 && y <= 7)) {
            
            return true;
        }

        return false;
    }
    
    public abstract Image getImage();
    
    public abstract List<Square> getMoves(Board board, int x, int y);
    
    private boolean validateMove(Board board, Square oldSquare, Square newSquare) {
        
        Boolean check = false;
        
        Square[][] list  = board.getSquares();
        
        Piece old = null;
        
        if (newSquare.getPiece() != null) {
            old = newSquare.getPiece();
        }
        
        newSquare.setPiece(oldSquare.movePiece());

        board.updatePieces();
        board.updateAccessibleFields();

        Square kingSquare = board.getKing(color);
        King king = (King) kingSquare.getPiece();
        
        if (king.isCheck(board)) {
            check = true;
        }
        
        oldSquare.setPiece(newSquare.movePiece());
        if (old != null) {
            newSquare.setPiece(old);
        }
       
        newSquare.resetBackGround();
        oldSquare.resetBackGround();
        return check;
    }
    
    public List<Square> move(Board board, int x, int y) {
        board.getSquares();
        
        Square[][] list  = board.getSquares();
        
        Square old = list[x][y];
        
        List<Square> availableSquares = getMoves(board, x, y);
                
        List<Square> goodSquares = new ArrayList<>();

        for (Square temp: availableSquares) {
            if (validateMove(board, old, temp) == false) {
                goodSquares.add(temp);
            }
        }
        return goodSquares;
    }
  
}
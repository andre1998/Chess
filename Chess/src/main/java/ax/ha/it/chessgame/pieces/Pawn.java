/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ax.ha.it.chessgame.pieces;

import ax.ha.it.chessgame.board.Board;
import ax.ha.it.chessgame.board.Square;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author André
 */
public class Pawn extends Piece {

    private Image image;
    
    public Pawn(String color) {
        super(color, "Pawn");
        
        try {
            BufferedImage bi = ImageIO.read(getClass().getClassLoader()
                                    .getResource("img/chessIcons.png"));
            
            if (color.equals("Black")) {
                this.image = SwingFXUtils.toFXImage(bi.getSubimage(
                            5 * 64, 0 * 64, 64, 64), null);
            } else {
                this.image = SwingFXUtils.toFXImage(bi.getSubimage(
                            5 * 64, 1 * 64, 64, 64), null);
            }
        } catch (Exception e) {
            System.out.println("Image not found");
        }
    }
    
    @Override
    public List<Square> getMoves(Board board, int x, int y) {
        List<Square> possibleMoves = new ArrayList<>();
        
        addMove(board, possibleMoves, x, y);

        return possibleMoves;
    }

    private void addMove(Board board, List<Square> possibleMoves, int x, int y) {
        
        Square[][] list  = board.getSquares();
        
        moveTwice(list, possibleMoves, x ,y);
        
        if (this.color == "White") {
            standardMove(list, possibleMoves, x, y-1);
            Capture(list, possibleMoves, x+1, y-1);
            Capture(list, possibleMoves, x-1, y-1);
            Enpassant(list, possibleMoves, x+1, y, board.getCurrentTurn());
            Enpassant(list, possibleMoves, x-1, y, board.getCurrentTurn());
        }
        else {
            standardMove(list, possibleMoves, x, y+1);
            Capture(list, possibleMoves, x+1, y+1);
            Capture(list, possibleMoves, x-1, y+1);
            Enpassant(list, possibleMoves, x+1, y, board.getCurrentTurn());
            Enpassant(list, possibleMoves, x-1, y, board.getCurrentTurn());
        }
    }
    
    private void standardMove(Square[][] list, List<Square> possibleMoves, int x, int y) {
        if (valid(x, y)) {
            if (list[x][y].getPiece() == null) {
                possibleMoves.add(list[x][y]);
            }
        }
        
    }
    
    private void moveTwice(Square[][] list, List<Square> possibleMoves, int x, int y) {
        
        if (this.count == 0) {
            
            if (this.color == "White") {
                if (list[x][y-1].getPiece() == null && list[x][y-2].getPiece() == null) {
                    possibleMoves.add(list[x][y-2]);
                }
            } else {
                if (list[x][y+1].getPiece() == null && list[x][y+2].getPiece() == null) {
                    possibleMoves.add(list[x][y+2]);
                }      
            }
        }
    }
    
    private void Capture(Square[][] list, List<Square> possibleMoves, int x, int y) {
        if(valid(x, y)) {
            if (list[x][y].getPiece() != null && this.color != list[x][y].getPiece().getColor()) {
                possibleMoves.add(list[x][y]);
            }
        }
    }
    
    private void Enpassant(Square[][] list, List<Square> possibleMoves, int x, int y, int currentTurn) {
        if (valid(x, y)) {
            if (isOpponentPiece(list, x, y) && list[x][y].getPiece().getTurnMoved() == currentTurn-1 ) {
                
                if (list[x][y].getPiece().getType() == "Pawn" && (y == 3 && list[x][y].getPiece().getCount() == 1)) {
                    possibleMoves.add(list[x][y-1]);
                }
                if (list[x][y].getPiece().getType() == "Pawn" && (y == 4 && list[x][y].getPiece().getCount() == 1)) {
                    possibleMoves.add(list[x][y+1]);
                }
            }
        }
        
    }
    
    private boolean isOpponentPiece(Square[][] list, int x , int y) {
        if (list[x][y].getPiece() != null && this.color != list[x][y].getPiece().getColor()) {
            return true;
        }
        return false;
    }
    
    @Override
    public Image getImage() {
        return this.image;
    }
    
}



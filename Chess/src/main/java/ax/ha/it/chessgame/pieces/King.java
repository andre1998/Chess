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
import java.util.Iterator;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author André
 */
public class King extends Piece {
    
    private Image image;
    
    public King(String color) {
        
        super(color, "King");
            
        try {
            BufferedImage bi = ImageIO.read(getClass().getClassLoader()
                                    .getResource("img/chessIcons.png"));
            if (color.equals("Black")) {
                this.image = SwingFXUtils.toFXImage(bi.getSubimage(
                            0 * 64, 0 * 64, 64, 64), null);
            } else {
                this.image = SwingFXUtils.toFXImage(bi.getSubimage(
                            0 * 64, 1 * 64, 64, 64), null);
            }
        } catch (Exception e) {
            System.out.println("Image not found");
        }
        
    }
    
    @Override
    public Image getImage() {
        return this.image;
    }
    
    @Override
    public List<Square> getMoves(Board board, int x, int y) {
        Square[][] list = board.getSquares();
        List<Square> possibleMoves = new ArrayList<>();
        
        for (int i = -1; i<=1; i++)  {
            addSquare(list, possibleMoves, x+i, y+1);
            addSquare(list, possibleMoves, x+i, y-1);
        }
        
        addSquare(list, possibleMoves, x+1, y);
        addSquare(list, possibleMoves, x-1, y);
        
        if (this.count == 0 && x == 4) {
            castling(board, list, possibleMoves, x, y);
        }
        
        
        return possibleMoves;
    }
    
    
    
    public Boolean isCheck(Board board) {
        
        List<Square> attackedSquare = board.getAccessibleFields(this.color);
        Square currentSquare = board.getKing(this.color);
        
        return attackedSquare.contains(currentSquare);
        
    }
    
    public Boolean isCheckMate(Board board) {

        List<Square> sameColor = new ArrayList<Square>(board.getPieces(this.color));
        System.out.println( sameColor.size());

        for (int i = 0; i<sameColor.size(); i++) {
            Square temp = sameColor.get(i);
            if (temp.getPiece().move(board, temp.getX(), temp.getY()).size() > 0) {
                return false;
            }
        }

        return true;
    }
    
    private void addSquare(Square[][] list, List<Square> possibleMoves, int x, int y) {
        
        if (valid(x, y)) {
            if (list[x][y].getPiece() == null) {
                possibleMoves.add(list[x][y]);
            }
            else if(this.color != list[x][y].getPiece().getColor()) {
                possibleMoves.add(list[x][y]);
            }
                
        }
    }
    
    private void castling(Board board, Square[][] list, List<Square> possibleMoves, int x, int y) {
        
        int left = -1;
        int right = 1;
        List<Square> check = board.getAccessibleFields(this.color);

        if (verifyRookCondition(list, 0, y)) {
            if (validateCastling(list, check, x, y, left)) {
                possibleMoves.add(list[x-2][y]);
            }
        }

        if (verifyRookCondition(list, 7, y) && validateCastling(list, check, x, y, right)) {
           if (validateCastling(list, check, x, y, right)) {
                possibleMoves.add(list[x+2][y]);
           }
        }
          
    }
       
    
    private boolean validateCastling(Square[][] list, List<Square> check, int x, int y, int direction) {
        
        int xPos = x;
        
        while (1 < xPos && xPos < 6) {
            xPos+=direction;
                    
            if (list[xPos][y].getPiece() != null) {
                return false;
            }
        }
        if (direction == 1) {
            for (int i = 0; i < 2; i++) {
                if (check.contains(list[x][y])) {
                    return false;
                }
                
                x+=direction;
            }
        } else {
            for (int i = 0; i < 3; i++) {
                if (check.contains(list[x][y])) {
                    return false;
                }
                
                x+=direction;
            }
        }
        
        
        return true;
    }
    
    private boolean verifyRookCondition(Square[][] list, int x, int y) {
        if (list[x][y].getPiece() != null) { 
            if (list[x][y].getPiece().getType() == "Rook") {
                if (list[x][y].getPiece().getCount() == 0) {
                    return true;
                }
            }
        }

        return false;
    }
}

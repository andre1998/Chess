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
public class Bishop extends Piece {

    private Image image;
    
    public Bishop(String color) {

        super(color, "Bishop");
            
        try {
            BufferedImage bi = ImageIO.read(getClass().getClassLoader()
                                    .getResource("img/chessIcons.png"));

            if (color.equals("Black")) {
                this.image = SwingFXUtils.toFXImage(bi.getSubimage(
                            4 * 64, 0 * 64, 64, 64), null);
            } else {
                this.image = SwingFXUtils.toFXImage(bi.getSubimage(
                            4 * 64, 1 * 64, 64, 64), null);
            }
        } catch (Exception e) {
            System.out.println("Image not found");
        }
        
        
    }
    
    @Override
    public List<Square> getMoves(Board board, int x, int y) {
        Square[][] list = board.getSquares();
        List<Square> possibleMoves = new ArrayList<>();
        
        moveDiagonal(list, possibleMoves, x, y, +1, +1);
        moveDiagonal(list, possibleMoves, x, y, +1, -1);
        moveDiagonal(list, possibleMoves, x, y, -1, +1);
        moveDiagonal(list, possibleMoves, x, y, -1, -1);
        
        return possibleMoves;
    }

    @Override
    public Image getImage() {
        return this.image;
    }
    
    public void moveDiagonal(Square[][] list , List<Square> possibleMoves, int x, int y, int xDirection, int yDirection) {
        
        int col = x+xDirection;
        int row = y+yDirection;

        while (this.valid(col, row)) {

            if (list[col][row].getPiece() == null) {
                possibleMoves.add(list[col][row]);
            }
            else {
                if (list[col][row].getPiece().getColor() != this.color) {
                    possibleMoves.add(list[col][row]);
                }
                break;
            }
            col += xDirection;
            row += yDirection;
        } 
    }
    
}

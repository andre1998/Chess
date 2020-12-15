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
public class Knight extends Piece {
    
    private Image image;
    
    public Knight(String color) {
        super(color, "Knight");
            
        try {
            BufferedImage bi = ImageIO.read(getClass().getClassLoader()
                                    .getResource("img/chessIcons.png"));

            if (color.equals("Black")) {
                this.image = SwingFXUtils.toFXImage(bi.getSubimage(
                            3 * 64, 0 * 64, 64, 64), null);
            } else {
                this.image = SwingFXUtils.toFXImage(bi.getSubimage(
                            3 * 64, 1 * 64, 64, 64), null);
            }
        } catch (Exception e) {
            System.out.println("Image not found");
        }
        
        
    }
    
    @Override
    public List<Square> getMoves(Board board, int x, int y) {
        Square[][] list = board.getSquares();
        List<Square> possibleMoves = new ArrayList<>();
        
        addSquare(list, possibleMoves, x-2, y+1);
        addSquare(list, possibleMoves, x-1, y+2);
        addSquare(list, possibleMoves, x+2, y+1);
        addSquare(list, possibleMoves, x+1, y+2);
        addSquare(list, possibleMoves, x-2, y-1);
        addSquare(list, possibleMoves, x-1, y-2);
        addSquare(list, possibleMoves, x+2, y-1);
        addSquare(list, possibleMoves, x+1, y-2);
        
        return possibleMoves;
    }

    private void addSquare(Square[][] list, List<Square> possibleMoves, int x, int y) {
        
        if (valid(x, y)) {
            if (list[x][y].getPiece() == null) {
                possibleMoves.add(list[x][y]);
            } 
            else if (list[x][y].getPiece().getColor() != this.color) {
                possibleMoves.add(list[x][y]);
                
            }
        }
    }
    
    @Override
    public Image getImage() {
        return this.image;
    }
    
    
}

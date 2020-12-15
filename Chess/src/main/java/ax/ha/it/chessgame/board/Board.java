/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ax.ha.it.chessgame.board;

import ax.ha.it.chess.Dialog.PostGame;
import ax.ha.it.chess.Dialog.Promote;
import ax.ha.it.chessgame.board.*;
import ax.ha.it.chessgame.pieces.*;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 *
 * @author André
 */
public class Board extends GridPane {
    
    private List<Square> highlighted;
    private Square[][] squares;
    private Square lastClicked;
    private Square enPassantSquare;
    private int currentTurn;
    
    //private boolean clicked = false;
    
    // Lists which contains the squares where the currently alive pieces are in
    private List<Square> whitePieces;
    private List<Square> blackPieces;
    
    // Lists which contains squares a certain color can attack/reach
    private List<Square> whiteAccessibleFields = new ArrayList<>();
    private List<Square> blackAccessibleFields = new ArrayList<>();

    public Board() {
        initBoard();
        initPieces();
        this.currentTurn = 0;
        this.lastClicked = null;
        this.enPassantSquare = null;
        this.highlighted = new ArrayList<>();
    }
    
    // Function which creates and places all the starting pieces and adds them to their respective list
    private void initPieces() {
        
        // Init Rooks
        squares[0][0].setPiece(new Rook("Black"));
        squares[7][0].setPiece(new Rook("Black"));
        squares[0][7].setPiece(new Rook("White"));    
        squares[7][7].setPiece(new Rook("White"));
        
        // Init Knights
        squares[1][0].setPiece(new Knight("Black"));
        squares[6][0].setPiece(new Knight("Black"));
        squares[1][7].setPiece(new Knight("White"));    
        squares[6][7].setPiece(new Knight("White"));
        
        // Init Bishops
        squares[2][0].setPiece(new Bishop("Black"));
        squares[5][0].setPiece(new Bishop("Black"));
        squares[2][7].setPiece(new Bishop("White"));    
        squares[5][7].setPiece(new Bishop("White"));
        
        // Init Kings & Queens
        squares[3][0].setPiece(new Queen("Black"));
        squares[4][0].setPiece(new King("Black"));
        squares[3][7].setPiece(new Queen("White"));    
        squares[4][7].setPiece(new King("White"));
        
        
        // Init Black Pawns
        for (int i = 0; i<8; i++) {
            squares[i][1].setPiece(new Pawn("Black"));
        }
        
        // Init White Pawns
        for (int i = 0; i<8; i++) {
            squares[i][6].setPiece(new Pawn("White"));
        }
        
        whitePieces = new ArrayList<Square>();
        blackPieces = new ArrayList<Square>();
        
        for (int i = 0; i<8; i++) {
            blackPieces.add(squares[i][0]);
            blackPieces.add(squares[i][1]);
            whitePieces.add(squares[i][6]);
            whitePieces.add(squares[i][7]);
        }

        updateAccessibleFields();
        //resetBackground();
    }
    
    // Creates a new chess board where squares extends buttons
    private void initBoard() {
        
        squares = new Square[8][9];
                
        for (int row = 0; row<8; row++) {
            for (int col = 0; col<8; col++) {

                final int finalRow = row;
                final int finalCol = col;  
                
                if (row % 2 == 0 && col % 2 == 0 || row % 2 == 1 && col % 2 == 1) {
                    squares[row][col] = new Square(this, row, col, null, true);
                } else {
                    squares[row][col] = new Square(this, row, col, null, false);
                }

                squares[row][col].setMinSize(100, 100);
                
                // Puts an setOnAction for each square
                squares[row][col].setOnAction( e -> onSquareClick(finalRow, finalCol));
                
                // When initilised the squares doesnt have an image
                add(squares[row][col], row, col);
                  
            }
        }  
    }
    
    // Function which activates when a square has been clicked on
    private void onSquareClick(int x, int y) {
        Square current = squares[x][y];
        
        // Checks if the square has a chess pice
        if (lastClicked != null && lastClicked.getPiece() != null) {
            
            if (validMove(current)) {
                if (enPassantSquare != null) {
                    verifyEnpassant(current);
                }
                
                // Checks if En Passant was used
                specificMoveKingOrPawn(lastClicked, current);
                updateGameState(current);
                
                // Check to see if pawn can get promoted (This should have been used in the pawn class, but due to how the program was designed it ended up here)
                if (current.getPiece() != null && current.getPiece().getType() == "Pawn") {
                    checkPromote(current);
                }
                
            }
            
            lastClicked = null;
            resetBackground();
            
        // If the square doesnt have a piece
        } else {
            validatePieceSelection(current);
        }
    }
    
    // Function which checks if a valid piece was selected
    public void validatePieceSelection(Square current) {
        if (current.getPiece() != null){
            if (currentTurn % 2 == 0 && current.getPiece().getColor() == "White") {
                lastClicked = current;
                showValidMoves(lastClicked);
            } else if (currentTurn % 2 != 0 && current.getPiece().getColor() == "Black") {
                lastClicked = current;
                showValidMoves(lastClicked);
            }
        }
        else {
            System.out.println("Invalid piece selected");
        }
    }
    
    // Function which promotes a pawn if it has reached the end of the board
    public void checkPromote(Square pawn) {
        if (pawn.getY() == 0 || pawn.getY() == 7) {
            System.out.println("Can Promote");
            Optional<Piece> result = new Promote((Pawn) pawn.getPiece()).showAndWait();

            result.ifPresent(f -> pawn.setPiece(f));
        }
    }
    
    // Marks the square where en passant can be performed or moves Rook for castling
    public void specificMoveKingOrPawn(Square oldSquare, Square newSquare) {
        
        int valueY = Math.abs(oldSquare.getY()-newSquare.getY());
        int valueX = Math.abs(oldSquare.getX()-newSquare.getX());
        
        if (oldSquare.getPiece() != null) {
            if (oldSquare.getPiece().getType() == "Pawn" &&  valueY == 2) {
                this.enPassantSquare = newSquare;
                return;
            }
            if (oldSquare.getPiece().getType() == "King" &&  valueX == 2) {
                if (newSquare.getX() == 2) {
                    squares[newSquare.getX()+1][newSquare.getY()]
                            .setPiece(squares[0][newSquare.getY()].movePiece());
                } else {
                    squares[newSquare.getX()-1][newSquare.getY()]
                            .setPiece(squares[7][newSquare.getY()].movePiece());
                }
            }
        }
        this.enPassantSquare = null;
    }
    
    // Function which checks if a pawn has performed enpassant and then removes the pawn
    public void verifyEnpassant(Square current) {
        if (lastClicked.getPiece().getType() == "Pawn" && lastClicked.getPiece().getColor() != enPassantSquare.getPiece().getColor()) {
            if (lastClicked.getPiece().getColor() == "White") {
                if (current.getY() == enPassantSquare.getY()-1 && current.getX() == enPassantSquare.getX()) {
                    enPassantSquare.setPiece(null);
                }
            }
            else {
                if (current.getY() == enPassantSquare.getY()+1 && current.getX() == enPassantSquare.getX()) {
                    enPassantSquare.setPiece(null);
                }
            }
        }  
    }
    
    // Function which returns the gameboard
    public Square[][] getSquares() {
        return this.squares;
    }
    
    // Function which gets all fields a color can get;
    public void updateAccessibleFields() {
        
        whiteAccessibleFields.clear();
        blackAccessibleFields.clear();
                
        whitePieces.forEach(temp -> {
            whiteAccessibleFields.addAll(temp.getPiece().getMoves(this, temp.getX(), temp.getY()));
        });
        
        
        blackPieces.forEach(temp -> {
            blackAccessibleFields.addAll(temp.getPiece().getMoves(this, temp.getX(), temp.getY()));
        });
    }
    
    // Function which updates the positions of the currently alive pieces
    public void updatePieces() {
        
        whitePieces.clear();
        blackPieces.clear();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (squares[row][col].getPiece() != null) {
                    if (squares[row][col].getPiece().getColor() == "White") {
                        whitePieces.add(squares[row][col]);
                    } else {
                        blackPieces.add(squares[row][col]);
                    }
                }
            }
        }
    }
    
    // Function which is performed at the end of each turn
    public void updateGameState(Square current) {
        
        current.setPiece(lastClicked.movePiece());
        current.getPiece().setTurnMoved(currentTurn);
        currentTurn += 1;
        updatePieces();
        updateAccessibleFields();
        
        // Checks if the opposing players king is in check and then checkmate
        Square kingSquare = getKing(getCurrentColor());
        King king = (King) kingSquare.getPiece();

        if (king.isCheck(this)) {
            if (king.isCheckMate(this)) {
                System.out.println(getCurrentColor()+"'s king is in checkmate!");
                Optional<Boolean> result = new PostGame(getCurrentColor(), 0, this).showAndWait();
                if (result.get() == true) {
                    this.clearGameState();
                }
            }
            else {
                System.out.println(getCurrentColor()+"'s king is in check!!");
            }
        }
    }
    
    // Function which gets a speciefed colors fields they can reach
    public List<Square> getAccessibleFields(String color) {
        if (color == "Black") {
            return whiteAccessibleFields;
        }
        return blackAccessibleFields;
    }
    
    // Gets all pieces of a specified color
    public List<Square> getPieces(String color) {
        if (color == "White") {
            return whitePieces;
        }
        else {
            return blackPieces;
        }
    }
    
    // Function which highlights the squares a piece can move to
    private boolean validMove(Square square) {
        return highlighted.stream().anyMatch((temp) -> (square == temp));
    }
    
    // Function which highlights the available moves a piece can make
    private void showValidMoves(Square square) {
        highlighted = square.getPiece().move(this, square.getX(), square.getY());
        setHighLight();
    }
    
    // Function which decides the highlight color
    private void setHighLight() {
        highlighted.forEach(temp -> {
            if (temp.getPiece() != null) {
                temp.setHighlightKill();
            } else {
                temp.setHighlight();
                
            }
        });
    }
    
    // Resests the background color to their original color
    private void resetBackground() {
        for (int row = 0; row < 8; row++ ) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].resetBackGround();
            }
        }
    }
    
    // Gets the current turn of the game
    public int getCurrentTurn() {
        return this.currentTurn;
    }

    // Gets the current players color
    public String getCurrentColor() {
        if (currentTurn % 2 == 0) {
            return "White";
        }
        return "Black";
    }    
    
    // Resets the game
    public void clearGameState() {
        resetBackground();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setPiece(null);
            }
        }  
        enPassantSquare = null;
        currentTurn = 0;
        lastClicked = null;
        initPieces();
    }
    
    // Function which returns the square where the specified colord king is currently in
    public Square getKing(String color) {
        if (color == "White") {
            for (Square temp: whitePieces) {
                if (temp.getPiece().getType() == "King") {
                    return temp;
                }
            }
        }
        else {
            for (Square temp: blackPieces) {
                if (temp.getPiece().getType() == "King") {
                    return temp;
                }
            }
        }
        return null;
    }
    
    
}

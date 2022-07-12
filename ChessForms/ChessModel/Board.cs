using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChessModel
{
    public class Board
    {
        public int Size { get; set; } // the size of the board usualy 8x8
        public Cell[,] Grid { get; set; }

        // constructor
        public Board(int s)
        {
            Size = s; // intial board size is defined by parameter s

            Grid = new Cell[Size, Size]; // create new 2d array of type cell

            // fill the 2d array with new cells, each with unique x and y coordinates
            for (int i = 0; i < Size; i++)
                for (int j = 0; j < Size; j++)
                {
                    Grid[i, j] = new Cell(i, j);
                    
                    // place pieces as defined by rules of chess
                    if (j == 0 || j == 7)
                    {
                        if (i == 0 || i == 7) // Rook
                            Grid[i, j].Piece = new Piece("Rook", "R", j/7);
                        if (i == 1 || i == 6) // Knight
                            Grid[i, j].Piece = new Piece("Knight", "H", j/7);
                        if (i == 2 || i == 5) // Bishop
                            Grid[i, j].Piece = new Piece("Bishop", "B", j/7);
                        if (i == 3) // Queen
                            Grid[i, j].Piece = new Piece("Queen", "Q", j/7);
                        if (i == 4) // King
                            Grid[i, j].Piece = new Piece("King", "K", j/7);
                        Grid[i, j].IsOccupied = true;
                    }
                    if (j == 1 || j == 6) // Pawn
                    {
                        Grid[i, j].Piece = new Pawn("Pawn", "P", (j-1)/5);
                        Grid[i, j].IsOccupied = true;
                    }
                }
        }

        public void MovePiece(Cell previousCell, Cell currentCell)
        {

            // if pawn add to moves counter
            if (previousCell.Piece.Title == "Pawn")
            {
                switch (((Pawn)previousCell.Piece).Moves)
                {
                    case 0:
                    case 1:
                        ((Pawn)previousCell.Piece).Moves += 1;
                        break;
                    case 2:
                        break;
                }

                if (!currentCell.IsOccupied && currentCell.Row != previousCell.Row) // crossing diagonally for take and space unocuppied == en passant
                {
                    if (previousCell.Piece.Colour == 1) // white - moves upwards
                    {
                        Grid[currentCell.Row, currentCell.Column + 1].Piece = default;
                        Grid[currentCell.Row, currentCell.Column + 1].IsOccupied = false;
                    }
                    if (previousCell.Piece.Colour == 0) // black - moves downwards
                    {
                        Grid[currentCell.Row, currentCell.Column - 1].Piece = default;
                        Grid[currentCell.Row, currentCell.Column - 1].IsOccupied = false;
                    }
                }
            }

            // move piece to new cell and mark occupied
            Grid[currentCell.Row, currentCell.Column].Piece = previousCell.Piece;
            Grid[currentCell.Row, currentCell.Column].IsOccupied = true;

            // remove piece from old cell and mark unoccupied
            Grid[previousCell.Row, previousCell.Column].Piece = default;
            Grid[previousCell.Row, previousCell.Column].IsOccupied = false;

        }

        // checks position of move is displayable on the board
        public bool IsSafe(int x, int y, int pieceColour)
        {
            if ((x >= 0 && x < Size) && (y >= 0 && y < Size)) // x and y is in bounds and cell is not occupied
            {
                if ((Grid[x, y].IsOccupied && pieceColour != Grid[x, y].Piece.Colour) || !Grid[x, y].IsOccupied) // piece to be moved and piece to be taken are different colours
                {
                    //Console.WriteLine("Pos " + x + ", " + y + " is safe");
                    return true;
                }
            }

            //Console.WriteLine("Pos " + x + ", " + y + " is NOT safe");
            return false;
        }

        // finds legal moves for each chess piece based on an origin cell
        public void MarkNextLegalMoves(Cell currentCell)
        {
            // clear the board of legal moves
            for (int i = 0; i < Size; i++)
            {
                for (int j = 0; j < Size; j++)
                {
                    Grid[i, j].IsLegalMove = false;
                }
            }

            // find all legal moves and mark the cell as "legal"
            switch (currentCell.Piece.Title)
            {
                case "Pawn":
                    int PieceColour = Grid[currentCell.Row, currentCell.Column].Piece.Colour;
                    if (PieceColour == 1) // white - moves upwards
                    {
                        // checking for movement - only allow if space is unoccupied (no taking)
                        if (IsSafe(currentCell.Row, currentCell.Column - 1, currentCell.Piece.Colour)
                            && !Grid[currentCell.Row, currentCell.Column - 1].IsOccupied)
                            Grid[currentCell.Row, currentCell.Column - 1].IsLegalMove = true;

                        // checking for possibility of piece taking - only allow diagonal movement if space occupied
                        if (IsSafe(currentCell.Row + 1, currentCell.Column - 1, currentCell.Piece.Colour)
                            && Grid[currentCell.Row + 1, currentCell.Column - 1].IsOccupied)
                            Grid[currentCell.Row + 1, currentCell.Column - 1].IsLegalMove = true;

                        if (IsSafe(currentCell.Row - 1, currentCell.Column - 1, currentCell.Piece.Colour)
                            && Grid[currentCell.Row - 1, currentCell.Column - 1].IsOccupied)
                            Grid[currentCell.Row - 1, currentCell.Column - 1].IsLegalMove = true;

                        // en passant
                        if (currentCell.Row > 0 && Grid[currentCell.Row - 1, currentCell.Column].IsOccupied
                            && Grid[currentCell.Row - 1, currentCell.Column].Piece.Title == "Pawn"
                            && ((Pawn)Grid[currentCell.Row - 1, currentCell.Column].Piece).Moves == 1)
                            Grid[currentCell.Row - 1, currentCell.Column - 1].IsLegalMove = true;

                        if (currentCell.Row < Size - 1 &&  Grid[currentCell.Row + 1, currentCell.Column].IsOccupied
                            && Grid[currentCell.Row + 1, currentCell.Column].Piece.Title == "Pawn"
                            && ((Pawn)Grid[currentCell.Row + 1, currentCell.Column].Piece).Moves == 1)
                            Grid[currentCell.Row + 1, currentCell.Column - 1].IsLegalMove = true;

                        // if pawn in starting position allow for two space move
                        if (((Pawn)currentCell.Piece).Moves == 0)
                        {
                            if (IsSafe(currentCell.Row, currentCell.Column - 2, currentCell.Piece.Colour))
                                Grid[currentCell.Row, currentCell.Column - 2].IsLegalMove = true;
                        }
                    }
                    if (PieceColour == 0) // black - moves downwards
                    {
                        // checking for movement - only allow if space is unoccupied (no taking)
                        if (IsSafe(currentCell.Row, currentCell.Column + 1, currentCell.Piece.Colour)
                            && !Grid[currentCell.Row, currentCell.Column + 1].IsOccupied)
                            Grid[currentCell.Row, currentCell.Column + 1].IsLegalMove = true;

                        // checking for possibility of piece taking - only allow diagonal movement if space occupied
                        if (IsSafe(currentCell.Row + 1, currentCell.Column + 1, currentCell.Piece.Colour)
                            && Grid[currentCell.Row + 1, currentCell.Column + 1].IsOccupied)
                            Grid[currentCell.Row + 1, currentCell.Column + 1].IsLegalMove = true;

                        if (IsSafe(currentCell.Row - 1, currentCell.Column + 1, currentCell.Piece.Colour)
                            && Grid[currentCell.Row - 1, currentCell.Column + 1].IsOccupied)
                            Grid[currentCell.Row - 1, currentCell.Column + 1].IsLegalMove = true;

                        // en passant
                        if (currentCell.Row > 0 && Grid[currentCell.Row - 1, currentCell.Column].IsOccupied
                            && Grid[currentCell.Row - 1, currentCell.Column].Piece.Title == "Pawn"
                            && ((Pawn)Grid[currentCell.Row - 1, currentCell.Column].Piece).Moves == 1)
                            Grid[currentCell.Row - 1, currentCell.Column + 1].IsLegalMove = true;

                        if (currentCell.Row < Size - 1 && Grid[currentCell.Row + 1, currentCell.Column].IsOccupied
                            && Grid[currentCell.Row + 1, currentCell.Column].Piece.Title == "Pawn"
                            && ((Pawn)Grid[currentCell.Row + 1, currentCell.Column].Piece).Moves == 1)
                            Grid[currentCell.Row + 1, currentCell.Column + 1].IsLegalMove = true;

                        // if pawn in starting position allow for two space move
                        if (((Pawn)currentCell.Piece).Moves == 0)
                        {
                            if (IsSafe(currentCell.Row, currentCell.Column + 2, currentCell.Piece.Colour))
                                Grid[currentCell.Row, currentCell.Column + 2].IsLegalMove = true;
                        }
                    }

                    break;

                case "Knight":
                    if (IsSafe(currentCell.Row + 2, currentCell.Column + 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row + 2, currentCell.Column + 1].IsLegalMove = true;
                    if (IsSafe(currentCell.Row + 2, currentCell.Column - 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row + 2, currentCell.Column - 1].IsLegalMove = true;
                    if (IsSafe(currentCell.Row - 2, currentCell.Column + 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row - 2, currentCell.Column + 1].IsLegalMove = true;
                    if (IsSafe(currentCell.Row - 2, currentCell.Column - 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row - 2, currentCell.Column - 1].IsLegalMove = true;
                    if (IsSafe(currentCell.Row + 1, currentCell.Column + 2, currentCell.Piece.Colour))
                        Grid[currentCell.Row + 1, currentCell.Column + 2].IsLegalMove = true;
                    if (IsSafe(currentCell.Row + 1, currentCell.Column - 2, currentCell.Piece.Colour))
                        Grid[currentCell.Row + 1, currentCell.Column - 2].IsLegalMove = true;
                    if (IsSafe(currentCell.Row - 1, currentCell.Column + 2, currentCell.Piece.Colour))
                        Grid[currentCell.Row - 1, currentCell.Column + 2].IsLegalMove = true;
                    if (IsSafe(currentCell.Row - 1, currentCell.Column - 2, currentCell.Piece.Colour))
                        Grid[currentCell.Row - 1, currentCell.Column - 2].IsLegalMove = true;

                    break;

                case "King":
                    if (IsSafe(currentCell.Row, currentCell.Column + 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row, currentCell.Column + 1].IsLegalMove = true;
                    if (IsSafe(currentCell.Row, currentCell.Column - 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row, currentCell.Column - 1].IsLegalMove = true;
                    if (IsSafe(currentCell.Row + 1, currentCell.Column, currentCell.Piece.Colour))
                        Grid[currentCell.Row + 1, currentCell.Column].IsLegalMove = true;
                    if (IsSafe(currentCell.Row - 1, currentCell.Column, currentCell.Piece.Colour))
                        Grid[currentCell.Row - 1, currentCell.Column].IsLegalMove = true;
                    if (IsSafe(currentCell.Row + 1, currentCell.Column + 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row + 1, currentCell.Column + 1].IsLegalMove = true;
                    if (IsSafe(currentCell.Row + 1, currentCell.Column - 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row + 1, currentCell.Column - 1].IsLegalMove = true;
                    if (IsSafe(currentCell.Row - 1, currentCell.Column + 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row - 1, currentCell.Column + 1].IsLegalMove = true;
                    if (IsSafe(currentCell.Row - 1, currentCell.Column - 1, currentCell.Piece.Colour))
                        Grid[currentCell.Row - 1, currentCell.Column - 1].IsLegalMove = true;

                    break;

                case "Rook":
                    int pos = 1;
                    bool[] direction = new bool[4] { true, true, true, true }; // north, east, south, west

                    for (int i = 0; i <= Size; i++)
                    {
                        // checking movement direction is safe
                        // if piece encountered the movement possibilities for that direction are halted
                        if (direction[0]) // holds previous cells north safe check
                        {
                            direction[0] = IsSafe(currentCell.Row, currentCell.Column - pos, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction[0]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row, currentCell.Column - pos].IsLegalMove = true;
                                direction[0] = !Grid[currentCell.Row, currentCell.Column - pos].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction[1]) // holds previous cells east safe check
                        {
                            direction[1] = IsSafe(currentCell.Row + pos, currentCell.Column, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction[1]) // continue legal move approval if true
                            { 
                                Grid[currentCell.Row + pos, currentCell.Column].IsLegalMove = true;
                                direction[1] = !Grid[currentCell.Row + pos, currentCell.Column].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction[2]) // holds previous cells south safe check
                        {
                            direction[2] = IsSafe(currentCell.Row, currentCell.Column + pos, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction[2]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row, currentCell.Column + pos].IsLegalMove = true;
                                direction[2] = !Grid[currentCell.Row, currentCell.Column + pos].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction[3]) // holds previous cells west safe check
                        {
                            direction[3] = IsSafe(currentCell.Row - pos, currentCell.Column, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction[3]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row - pos, currentCell.Column].IsLegalMove = true;
                                direction[3] = !Grid[currentCell.Row - pos, currentCell.Column].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        pos += 1;
                    }

                    break;

                case "Bishop":
                    int pos2 = 1;
                    bool[] direction2 = new bool[4] { true, true, true, true }; // north-east, north-west, south-east, south-west

                    for (int i = 0; i <= Size; i++)
                    {
                        // checking movement direction is safe
                        // if piece encountered the movement possibilities for that direction are halted
                        if (direction2[0]) // holds previous cells north-east safe check
                        {
                            direction2[0] = IsSafe(currentCell.Row + pos2, currentCell.Column - pos2, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction2[0]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row + pos2, currentCell.Column - pos2].IsLegalMove = true;
                                direction2[0] = !Grid[currentCell.Row + pos2, currentCell.Column - pos2].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction2[1]) // holds previous cells north-west safe check
                        {
                            direction2[1] = IsSafe(currentCell.Row - pos2, currentCell.Column - pos2, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction2[1]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row - pos2, currentCell.Column - pos2].IsLegalMove = true;
                                direction2[1] = !Grid[currentCell.Row - pos2, currentCell.Column - pos2].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction2[2]) // holds previous cells south-east safe check
                        {
                            direction2[2] = IsSafe(currentCell.Row + pos2, currentCell.Column + pos2, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction2[2]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row + pos2, currentCell.Column + pos2].IsLegalMove = true;
                                direction2[2] = !Grid[currentCell.Row + pos2, currentCell.Column + pos2].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction2[3]) // holds previous cells south-west safe check
                        {
                            direction2[3] = IsSafe(currentCell.Row - pos2, currentCell.Column + pos2, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction2[3]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row - pos2, currentCell.Column + pos2].IsLegalMove = true;
                                direction2[3] = !Grid[currentCell.Row - pos2, currentCell.Column + pos2].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        pos2 += 1;
                    }

                    break;

                case "Queen":
                    int pos3 = 1;
                    bool[] direction3 = new bool[8] { true, true, true, true, true, true, true, true }; // north, east, south, west, north-east, north-west, south-east, south-west

                    for (int i = 0; i < Size; i++)
                    {
                        // checking movement direction is safe
                        // if piece encountered the movement possibilities for that direction are halted
                        if (direction3[0]) // holds previous cells north safe check
                        {
                            direction3[0] = IsSafe(currentCell.Row, currentCell.Column - pos3, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction3[0]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row, currentCell.Column - pos3].IsLegalMove = true;
                                direction3[0] = !Grid[currentCell.Row, currentCell.Column - pos3].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction3[1]) // holds previous cells east safe check
                        {
                            direction3[1] = IsSafe(currentCell.Row + pos3, currentCell.Column, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction3[1]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row + pos3, currentCell.Column].IsLegalMove = true;
                                direction3[1] = !Grid[currentCell.Row + pos3, currentCell.Column].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction3[2]) // holds previous cells south safe check
                        {
                            direction3[2] = IsSafe(currentCell.Row, currentCell.Column + pos3, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction3[2]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row, currentCell.Column + pos3].IsLegalMove = true;
                                direction3[2] = !Grid[currentCell.Row, currentCell.Column + pos3].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction3[3]) // holds previous cells west safe check
                        {
                            direction3[3] = IsSafe(currentCell.Row - pos3, currentCell.Column, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction3[3]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row - pos3, currentCell.Column].IsLegalMove = true;
                                direction3[3] = !Grid[currentCell.Row - pos3, currentCell.Column].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction3[4]) // holds previous cells north-east safe check
                        {
                            direction3[4] = IsSafe(currentCell.Row + pos3, currentCell.Column - pos3, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction3[4]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row + pos3, currentCell.Column - pos3].IsLegalMove = true;
                                direction3[4] = !Grid[currentCell.Row + pos3, currentCell.Column - pos3].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction3[5]) // holds previous cells north-west safe check
                        {
                            direction3[5] = IsSafe(currentCell.Row - pos3, currentCell.Column - pos3, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction3[5]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row - pos3, currentCell.Column - pos3].IsLegalMove = true;
                                direction3[5] = !Grid[currentCell.Row - pos3, currentCell.Column - pos3].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction3[6]) // holds previous cells south-east safe check
                        {
                            direction3[6] = IsSafe(currentCell.Row + pos3, currentCell.Column + pos3, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction3[6]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row + pos3, currentCell.Column + pos3].IsLegalMove = true;
                                direction3[6] = !Grid[currentCell.Row + pos3, currentCell.Column + pos3].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        if (direction3[7]) // holds previous cells south-west safe check
                        {
                            direction3[7] = IsSafe(currentCell.Row - pos3, currentCell.Column + pos3, currentCell.Piece.Colour); // update to current cells safe check
                            if (direction3[7]) // continue legal move approval if true
                            {
                                Grid[currentCell.Row - pos3, currentCell.Column + pos3].IsLegalMove = true;
                                direction3[7] = !Grid[currentCell.Row - pos3, currentCell.Column + pos3].IsOccupied; // an occupied space by the opposite colour considered legal, update direction if encountered
                            }
                        }

                        pos3 += 1;
                    }

                    break;
            }
        }
    }
}

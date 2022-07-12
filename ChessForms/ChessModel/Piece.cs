using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChessModel
{
    public class Piece
    {

        public string Title { get; set; }
        public string Symbol { get; set; }
        public string ImagePath { get; set; }
        public int Colour { get; set; } // 1 == White, 0 == Black

        public Piece(string t, string s, int c)
        {
            Title = t;
            Symbol = s;
            Colour = c;

            InitImage();
        }

        private void InitImage()
        {
            switch (Title)
            {
                case "Pawn":
                    ImagePath = Colour == 1 ? "assets/piece_pawn_white.png" : "assets/piece_pawn_black.png";
                    break;
                case "Rook":
                    ImagePath = Colour == 1 ? "assets/piece_rook_white.png" : "assets/piece_rook_black.png";
                    break;
                case "Knight":
                    ImagePath = Colour == 1 ? "assets/piece_knight_white.png" : "assets/piece_knight_black.png";
                    break;
                case "Bishop":
                    ImagePath = Colour == 1 ? "assets/piece_bishop_white.png" : "assets/piece_bishop_black.png";
                    break;
                case "Queen":
                    ImagePath = Colour == 1 ? "assets/piece_queen_white.png" : "assets/piece_queen_black.png";
                    break;
                case "King":
                    ImagePath = Colour == 1 ? "assets/piece_king_white.png" : "assets/piece_king_black.png";
                    break;
            }
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChessModel
{
    internal class Pawn : Piece
    {
        public string Title { get; set; }
        public string Symbol { get; set; }
        public int Colour { get; set; } // 1 == White, 0 == Black

        public int Moves { get; set; } // 0 = first turn, 1 = second turn, 2 = third turn plus

        public Pawn(string t, string s, int c) : base(t, s, c)
        {
            Title = t;
            Symbol = s; 
            Colour = c;

            Moves = 0;
        }
    }
}

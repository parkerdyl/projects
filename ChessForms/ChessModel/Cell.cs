using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChessModel
{
    public class Cell
    {

        public int Row { get; set; } // rank
        public int Column { get; set; } // file
        public Piece Piece { get; set; }
        public bool IsOccupied { get; set; }
        public bool IsLegalMove { get; set; }

        public Cell(int x, int y)
        {
            Row = x;
            Column = y;
        }

    }
}

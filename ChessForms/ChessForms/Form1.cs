using ChessModel;

namespace ChessForms
{
    public partial class Form1 : Form
    {

        Cell PreviousCell;

        static Board Board = new Board(8); // reference to the class board. Contains the values of board.

        public Button[,] btnGrid = new Button[Board.Size, Board.Size]; // 2D array of buttons which values is determined by Board

        public Form1()
        {
            InitializeComponent();
            PopulateGrid();

        }

        private void PopulateGrid()
        {
            int ButtonSize = panel1.Width / Board.Size;

            // make panel a perfect square
            panel1.Height = panel1.Width;

            // nested loops. create button and print them to the screen
            for (int i = 0; i < Board.Size; i++)
            {
                for (int j = 0; j < Board.Size; j++)
                {
                    btnGrid[i, j] = new Button();

                    btnGrid[i, j].Height = ButtonSize;
                    btnGrid[i, j].Width = ButtonSize;

                    // add a click event to each button
                    btnGrid[i, j].Click += Grid_Button_Click;

                    // add button to the panel
                    panel1.Controls.Add(btnGrid[i, j]);

                    // set the location of the new button
                    btnGrid[i, j].Location = new Point(i * ButtonSize, j * ButtonSize);

                    if (Board.Grid[i, j].IsOccupied) {
                        Image pieceImage = Image.FromFile(Board.Grid[i, j].Piece.ImagePath); // generate image from file
                        Bitmap pieceBitmap = new Bitmap(pieceImage, btnGrid[i, j].Width, btnGrid[i, j].Height); // stretch or shrink to fit w and h of button
                        btnGrid[i, j].Image = pieceBitmap;
                    }
                    btnGrid[i, j].Tag = new Point(i, j);    
                }
            }
        }

        private void Grid_Button_Click(object? sender, EventArgs e)
        {
            // get the row and column number of button clicked
            Button clickedButton = (Button)sender;
            Point location = (Point)clickedButton.Tag;

            int x = location.X;
            int y = location.Y;

            Cell currentCell = Board.Grid[x, y];

            if (!currentCell.IsOccupied && !currentCell.IsLegalMove)
            {
                // not occupied or legal move so clear board of legal moves
                for (int i = 0; i < Board.Size; i++)
                {
                    for (int j = 0; j < Board.Size; j++)
                    {
                        btnGrid[i, j].BackColor = Color.Transparent;
                        Board.Grid[i, j].IsLegalMove = false;
                    }
                }
            }

            if (currentCell.IsLegalMove)
            {
                // move piece to new cell
                Board.MovePiece(PreviousCell, currentCell);
                  
                // mark legal moves for new position
                PrintLegalMoves(currentCell);

                // write images
                for (int i = 0; i < Board.Size; i++)
                {
                    for (int j = 0; j < Board.Size; j++)
                    {
                        if (Board.Grid[i, j].IsOccupied)
                        {
                            Image pieceImage = Image.FromFile(Board.Grid[i, j].Piece.ImagePath); // generate image from file
                            Bitmap pieceBitmap = new Bitmap(pieceImage, btnGrid[i, j].Width, btnGrid[i, j].Height); // stretch or shrink to fit w and h of button
                            btnGrid[i, j].Image = pieceBitmap;
                        }
                        else
                        {
                            btnGrid[i, j].Image = null;
                        }
                    }
                }

                // update current cell
                PreviousCell = currentCell;
            }
            else if (currentCell.IsOccupied)
            {
                // mark legal moves for piece to move to
                PrintLegalMoves(currentCell);

                PreviousCell = currentCell;
            }
        }

        public void PrintLegalMoves(Cell cell)
        {
            // determine legal moves
            Board.MarkNextLegalMoves(cell);

            // update the colour on each button
            for (int i = 0; i < Board.Size; i++)
            {
                for (int j = 0; j < Board.Size; j++)
                {
                    btnGrid[i, j].BackColor = Color.Transparent;
                    if (Board.Grid[i, j].IsLegalMove == true)
                    {
                        if (Board.Grid[i, j].IsOccupied || (cell.Piece.Title == "Pawn" && cell.Row != i))
                        {
                            btnGrid[i, j].BackColor = Color.PaleVioletRed; // legal space occupied by piece, or unoccupied but able to be taken by en passant
                        }
                        else
                        {
                            btnGrid[i, j].BackColor = Color.Honeydew; // legal space unoccupied
                        }
                    }
                }
            }
        }

        public void PrintPieces()
        {

        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }
    }
}
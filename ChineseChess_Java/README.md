
如何移动棋子？
示例：在程序其他地方实现三个棋子的移动动画
// 导入相关包
import com.Controller.InGameObjects;
import com.GUI.Piece.MovePiece;

MovePiece movePiece = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);
movePiece.move(7, 5, 6, 5);
movePiece.move(3, 2, 4, 2);
movePiece.move(10, 9, 9, 9);
// 记得：move(row1, column1, row2, column2)！！！

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


如何移除棋子？
// 示例：移除棋盘上的棋子并动画移到观战区
RemovePiece removePiece = new RemovePiece(InGameObjects.piecesSession);
// 方法定义
public boolean remove(int row, int col, boolean isBlack)
// 移除黑方棋子（假设1,1有棋子）
removePiece.remove(1, 1, true);
// 移除红方棋子（假设10,1有棋子）
removePiece.remove(10, 1, false);
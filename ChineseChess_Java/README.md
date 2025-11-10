
如何移动棋子？
示例：在程序其他地方实现三个棋子的移动动画
// 导入相关包
```java
import com.Controller.InGameObjects;
import com.GUI.Piece.MovePiece;

MovePiece movePiece = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);
movePiece.move(7, 5, 6, 5);
movePiece.move(3, 2, 4, 2);
movePiece.move(10, 9, 9, 9);
```
// 记得：move(row1, column1, row2, column2)！！！



如何移除棋子？
```java
// 示例：移除棋盘上的棋子并动画移到观战区
RemovePiece removePiece = new RemovePiece(InGameObjects.piecesSession);
// 方法定义
public boolean remove(int row, int col, boolean isBlack)
// 移除黑方棋子（假设1,1有棋子）
removePiece.remove(1, 1, true);
// 移除红方棋子（假设10,1有棋子）
removePiece.remove(10, 1, false);
```


如何显示红框蓝框？
```java
// 显示
InGameObjects.blueBoxSession.setBlueBox(4, 5);
InGameObjects.redBoxSession.setRedBox(5, 5);
// 移除
InGameObjects.blueBoxSession.removeBlueBox(4, 5);
InGameObjects.redBoxSession.removeRedBox(5, 5);

```

如何获取点击坐标？
 GameClick 类用于监听棋盘组件上的鼠标点击事件，并将点击位置转换为棋盘的行列坐标。
 
 功能说明：
 1. 构造时传入棋盘对应的 JComponent，并自动注册鼠标监听器。
 2. 每次点击会根据像素坐标计算棋盘格的行列（不在棋盘范围则返回 [0, 0]）。
 3. 所有点击坐标（包括无效点击）都会放入阻塞队列，支持多线程安全获取。
 4. 提供 waitForClick() 方法，阻塞直到有新的点击坐标返回。
 
 用法示例：
 ```java
new Thread(() -> {
    int count = 0;
    while(count < 5) {
        int[] result = gameclick.waitForClick();
        System.out.println("In InitGame: Clicked at row: " + result[0] + ", col: " + result[1]);
        count++;
    }
}).start();
```
要新建一个线程，才能保证其它东西不被阻塞


投降相关配置：
在com.GUI.GameObjects.SurrenderButton中的row 79-88中的
```java
// 接下来就是调用DoSurrender的方法了
                // 交给你来写了
                //
                //
                //
                //
                //
                //
                //
                // 记得写完之后在这里调用！
```
中加入调用你写的投降的按钮

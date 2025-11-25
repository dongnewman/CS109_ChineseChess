
# Chinese Chess (Java Implementation)

这是一个基于 Java Swing 开发的中国象棋游戏，支持人人对战 (P2P) 和人机对战 (AI) 模式，并包含账户系统和历史记录保存功能。

## 📁 项目结构

项目源代码位于 `src/main/java/com` 目录下，主要分为以下几个包：

*   **`chinesechess`**: 包含程序的入口类 `Main.java`。
*   **`Controller`**: 负责游戏的核心逻辑控制。
    *   `DoGame.java`: 处理游戏循环、玩家交互和规则判定。
    *   `HistoryGame`: 处理历史记录的读取和保存。
    *   `InGameObjects.java`: 管理游戏运行时的全局对象。
*   **`GUI`**: 负责图形用户界面的实现。
    *   `GameFrame.java`: 游戏主窗口。
    *   `Menu.java`: 主菜单界面。
    *   `GameObjects`: 包含棋盘上的各种组件（棋子、按钮、提示框等）。
        *   `Piece`: 棋子相关的显示和动画逻辑。
        *   `Box`: 选中框和提示框的显示逻辑。
*   **`Model`**: 包含游戏的数据模型和算法。
    *   `InGame/playroom`: 棋盘 (`Board`) 和规则 (`Legal`) 的定义。
    *   `InGame/playersAI`: AI 算法实现（Minimax, Alpha-Beta, Zobrist）。
    *   `Account`: 账户管理和会话处理。

## 🚀 功能实现

### 1. 游戏模式
*   **人人对战 (P2P)**：两位玩家在同一台电脑上轮流走棋。
*   **人机对战 (AI)**：玩家与内置的 AI 进行对战。AI 具有一定的棋力，能够进行多步思考。

### 2. 账户系统
*   **注册与登录**：用户可以注册新账户并登录。
*   **数据存储**：用户信息存储在 `accounts/` 目录下的 JSON 文件中。

### 3. 游戏辅助
*   **悔棋 (Undo)**：允许玩家撤销上一步操作。
*   **投降 (Surrender)**：玩家可以主动认输。
*   **合法走法提示**：点击棋子时，会显示所有合法的移动位置。
*   **计时器**：记录每一步的思考时间。
*   **背景音乐**：提供背景音乐播放和切换功能。

## 🛠️ 核心功能实现原理

### 1. 棋子移动动画
动画效果由 `com.GUI.GameObjects.Piece.MovePiece` 类实现。
*   **原理**：使用 `javax.swing.Timer` 创建定时器。
*   **过程**：
    1.  计算棋子起始位置和目标位置的像素坐标差。
    2.  在定时器的每个 tick 中，根据插值更新棋子的位置 (`setLocation`)。
    3.  当达到目标位置时，停止定时器并更新棋盘逻辑状态。
*   **代码示例**：
    ```java
    // 移动棋子从 (7, 5) 到 (6, 5)
    MovePiece movePiece = new MovePiece(InGameObjects.plate, InGameObjects.piecesSession);
    movePiece.move(7, 5, 6, 5);
    ```

### 2. 本地存储与历史记录
游戏支持在关闭时自动保存当前进度，并在下次登录时恢复。
*   **序列化**：`com.Controller.HistoryGame.SetHistory` 类负责将当前的 `Board` 对象（包括棋子分布、当前走棋方、Zobrist 哈希）序列化为一个特定格式的字符串。
    *   格式示例：`<ZobristKey>;<Side>;<GameType>;<90个棋盘字符>`
*   **存储**：在 `GameFrame` 关闭时 (`windowClosing` 事件)，系统会将序列化后的字符串保存到用户对应的 JSON 文件 (`accounts/<username>.json`) 的 `history` 字段中。
*   **读取**：用户登录后，`HistoryGame` 类会读取 JSON 文件，解析 `history` 字段，并使用 `ReadHistory` 类将字符串反序列化为 `Board` 对象，从而恢复游戏场景。

### 3. AI 算法
AI 位于 `com.Model.InGame.playersAI` 包中，主要由 `HinatsuruAI` 类实现。
*   **核心算法**：**Minimax** 算法配合 **Alpha-Beta 剪枝**，用于在博弈树中搜索最佳走法。
*   **置换表 (Transposition Table)**：使用 **Zobrist Hashing** 对棋盘状态进行哈希映射。如果当前局面已经被搜索过，直接从表中读取结果，避免重复计算。
*   **历史启发 (History Heuristic)**：记录历史上表现好的走法（即引发剪枝的走法），在搜索新节点时优先尝试这些走法，从而提高剪枝效率。
*   **迭代加深**：先进行浅层搜索，逐步增加搜索深度，确保在有限时间内能返回一个较优解。

### 4. 鼠标交互与坐标转换
*   **监听**：`GameClick` 类实现了鼠标监听器。
*   **转换**：将鼠标点击的像素坐标转换为棋盘的行列坐标 (Row, Col)。
*   **阻塞等待**：使用 `waitForClick()` 方法阻塞游戏逻辑线程，直到用户进行有效点击，实现了逻辑层与 UI 层的同步。

### 5. 悔棋 (Undo) 功能
*   **功能说明**：
    *   在游戏中，玩家可以点击 "Undo" 按钮撤销上一步操作。
    *   **限制**：为了保证游戏公平性和简化逻辑，**每步棋只能悔棋一次**。如果已经悔棋过，或者尚未走棋，将无法再次悔棋。
    *   **效果**：悔棋后，棋盘将恢复到上一步的状态，包括棋子位置、被吃掉的棋子（从观战区恢复）、当前走棋方以及倒计时。
*   **实现原理**：
    *   **状态保存 (`UndoMove.save`)**：在每次玩家或 AI 走棋之前，系统会深拷贝当前的 `PiecesSession`（包含所有棋子对象及其位置、被吃掉的棋子列表）和 `Board`（逻辑棋盘数组、当前走棋方）的状态，并存储在 `UndoMove` 类的私有变量中。
    *   **状态恢复 (`UndoMove.undo`)**：当用户确认悔棋时，系统将保存的状态覆盖回当前的 `PiecesSession` 和 `Board`。
    *   **UI 更新**：
        *   根据恢复后的数据，重新设置每个棋子的位置 (`setBoardPosition`)。
        *   清空并重新绘制观战区（被吃掉的棋子），确保视觉上与数据一致。
        *   重置倒计时 (`CountdownTimer.changeSide`) 和当前走棋方图标 (`SideIcon`)。
        *   清除所有选中的高亮框。
    *   **一次性限制**：`UndoMove` 维护一个 `canUndo` 标志。`save()` 时设为 `true`，`undo()` 执行后设为 `false`，从而实现只能悔棋一次的限制。

## 📝 待办事项 (TODO)
1.  [x] 实现 Undo (悔棋) 按钮的具体逻辑。

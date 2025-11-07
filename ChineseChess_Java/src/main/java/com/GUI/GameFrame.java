package com.GUI;


import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.GUI.Piece.Pieces;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;


import com.Model.InGame.CountdownTimer;
import com.Controller.InGameObjects;
import com.GUI.Piece.InitPieces;
import com.GUI.Piece.PiecesSession;
import com.GUI.Box.BlueBox;
import com.GUI.Box.BoxSession;
import com.GUI.Box.RedBox;



/**
 * 游戏界面
 * 
 * 使用示例:
 */
public class GameFrame {
    private BufferedImage boardImage;
    // 运行时优先从 classpath 读取资源（打包到 resources 下时可用），
    // 如果找不到再回退到源码树中的文件路径，便于开发时运行
    private final String resourceName = "/Board.jpg"; // classpath 资源名（以 / 开头表示从 classpath 根查找）
    private final String filePath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "Board.jpg";

    public GameFrame() {
        // 初始化游戏界面

    JFrame GameFrame = new JFrame();
    GameFrame.setLayout(new BorderLayout());
    GameFrame.setTitle("Chinese Chess Game");

        
        try{
            // 1) 尝试从 classpath（resources）读取
            InputStream in = getClass().getResourceAsStream(resourceName);
            if (in != null) {
                boardImage = ImageIO.read(in);
                in.close();
            } else {
                // 2) 回退到源码目录下的文件（开发时运行）
                File f = new File(filePath);
                if (f.exists()) {
                    boardImage = ImageIO.read(f);
                } else {
                    System.err.println("Board image not found in classpath (" + resourceName + ") or file system (" + filePath + ")");
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }


        // 依据加载到的图片设置棋盘面板尺寸
        int plateWidth = 700;
        int plateHeight = 800;
        if (boardImage != null) {
            plateWidth = boardImage.getWidth();
            plateHeight = boardImage.getHeight();
        } else {
            System.err.println("Warning: boardImage is null, using default plate size 400x400");
        }

        // 需要在匿名内部类中使用最终或有效最终变量
        final int pw = plateWidth;
        final int ph = plateHeight;



    // 初始化所有棋子（放入 PiecesSession）
    InitPieces initPieces = new InitPieces();
    final PiecesSession piecesSession = new PiecesSession();
    boolean ok = initPieces.initAllPieces(piecesSession);
    if (!ok) {
        System.err.println("Warning: 初始化棋子失败");
    }

    // 全局注册，便于其它地方访问
    try {
        Class<?> inGameObjectsClass = Class.forName("com.Controller.InGameObjects");
        inGameObjectsClass.getField("piecesSession").set(null, piecesSession);
    } catch (Exception e) {
        System.err.println("无法注册InGameObjects.piecesSession: " + e.getMessage());
    }


    // 初始化 BlueBoxSession 和 RedBoxSession
    BoxSession blueBoxSession = new BoxSession();
    BoxSession redBoxSession = new BoxSession();

    // 全局注册，便于其它地方访问
    try {
        Class<?> inGameObjectsClass = Class.forName("com.Controller.InGameObjects");
        inGameObjectsClass.getField("blueBoxSession").set(null, blueBoxSession);
        inGameObjectsClass.getField("redBoxSession").set(null, redBoxSession);
    } catch (Exception e) {
        System.err.println("无法注册InGameObjects.boxSession: " + e.getMessage());
    }

    JPanel plate = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(pw, ph);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // 绘制背景等父类逻辑
                if (boardImage != null) {
                    // 绘制图片：按原始像素大小绘制，不拉伸
                    g.drawImage(boardImage, 0, 0, pw, ph, this);
                }
                // 从 PiecesSession 中逐格绘制棋子（使用 1-based 索引：行 1..10，列 1..9）
                for (int r = 1; r <= 10; r++) {
                    for (int c = 1; c <= 9; c++) {
                        Pieces p = piecesSession.getPiece(r, c);
                        if (p != null) p.paint(g);
                    }
                }
                // 绘制 BlueBox（使用 1-based 索引：行 1..10，列 1..9）
                for(int r = 1; r <= 10; r++) {
                    for(int c = 1; c <= 9; c++) {
                        BlueBox blueBox = (BlueBox) InGameObjects.blueBoxSession.getBoxAt(r, c);
                        if (blueBox != null) blueBox.paint(g);
                    }
                }
                // 绘制 RedBox（使用 1-based 索引：行 1..10，列 1..9）
                for(int r = 1; r <= 10; r++) {
                    for(int c = 1; c <= 9; c++) {
                        RedBox redBox = (RedBox) InGameObjects.redBoxSession.getRedBoxAt(r, c);
                        if (redBox != null) redBox.paint(g);
                    }
                }
            }
        };



    // 注册plate到InGameObjects，便于全局访问
    try {
        Class<?> inGameObjectsClass = Class.forName("com.Controller.InGameObjects");
        inGameObjectsClass.getField("plate").set(null, plate);
    } catch (Exception e) {
        System.err.println("无法注册InGameObjects.plate: " + e.getMessage());
    }
    // 通知其它线程界面已初始化完成
    


    // 将棋盘放入一个 JLayeredPane，以便把计时器覆盖显示在右上角
    // 将棋盘与计时器放在不同的 panel 中，centerPanel 负责承载棋盘（CENTER）和计时器（EAST）
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.setOpaque(false);

    // 保持 plate 的 preferredSize，直接放在 CENTER
    plate.setPreferredSize(new Dimension(pw, ph));
    centerPanel.add(plate, BorderLayout.CENTER);

    // 下面的代码用于测试：
        // 点击棋盘时把相对像素坐标输出到控制台（模块：点击取坐标）
        // plate.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         int x = e.getX();
        //         int y = e.getY();
        //         System.out.println("Plate clicked at: x=" + x + ", y=" + y);
        //     }
        // });

        // GameFrame.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         int x = e.getX();
        //         int y = e.getY();
        //         System.out.println("Frame clicked at: x=" + x + ", y=" + y);
        //     }
        // });



    // 在 centerContainer 的右上角放置倒计时模块（绝对定位）
    // 使用 CountdownTimer（避免与 Swing Timer 冲突）
    CountdownTimer timerModule = new CountdownTimer(60);
    JPanel timerPanel = timerModule.getPanel();
        // 将计时器放到右侧的 timerWrapper 的顶部，timerWrapper 放在 centerPanel 的 EAST
        timerPanel.setOpaque(true);
        Dimension tps = timerPanel.getPreferredSize();
        // 设置一个合适的宽度（确保不会太窄），并把高度设置为棋盘高度以便对齐
        int wrapperWidth = Math.max(100, tps.width + 8);
        JPanel timerWrapper = new JPanel(new BorderLayout());
        timerWrapper.setOpaque(false);
        timerWrapper.setPreferredSize(new Dimension(wrapperWidth, ph));
        // 把计时器面板放在 timerWrapper 的 NORTH（顶部）以靠上显示
        timerWrapper.add(timerPanel, BorderLayout.NORTH);

        centerPanel.add(timerWrapper, BorderLayout.EAST);

        GameFrame.add(centerPanel, BorderLayout.CENTER);

    JLabel label = new JLabel("欢迎来到中国象棋游戏!", SwingConstants.CENTER);
    GameFrame.add(label, BorderLayout.EAST);



    // 使用 pack 让基于 preferredSize 的组件确定初始大小，然后将宽度调整为棋盘宽度的 1.5 倍
    GameFrame.pack();
    int frameWidth = (int) Math.round(plateWidth * 1.5);
    int frameHeight = GameFrame.getHeight();
    GameFrame.setSize(frameWidth, frameHeight);
    GameFrame.setLocationRelativeTo(null);



    GameFrame.setVisible(true);
    

    // 通知其它线程界面已初始化完成
    try {
        Class<?> inGameObjectsClass = Class.forName("com.Controller.InGameObjects");
        java.util.concurrent.CountDownLatch latch = (java.util.concurrent.CountDownLatch) inGameObjectsClass.getField("uiReadyLatch").get(null);
        latch.countDown();
    } catch (Exception e) {
        System.err.println("无法countDown InGameObjects.uiReadyLatch: " + e.getMessage());
    }

    
    }
}



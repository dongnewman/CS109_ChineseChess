package com.GUI;

import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import com.Model.InGame.CountdownTimer;
import com.Model.InGame.playersAI.HinatsuruAI;
import com.Controller.InGameObjects;
import com.Controller.HistoryGame.SetHistory;
import com.Controller.HistoryGame.HistoryInfo;
import com.Model.Account.AccountSession;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.GUI.GameObjects.Box.*;
import com.GUI.GameObjects.Piece.*;
import com.GUI.GameObjects.*;

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
    private final String filePath = "src" + File.separator + "main" + File.separator + "resources" + File.separator
            + "Board.jpg";
    private JFrame gameFrame;

    public GameFrame() {
        // 初始化游戏界面
        gameFrame = new JFrame();
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setTitle("Chinese Chess Game");

        gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // 保存当前对局到当前登录账户的 rawJson（如果已登录）
                try {
                    if (AccountSession.isLoggedIn() && InGameObjects.board != null) {
                        HistoryInfo hInfo = new HistoryInfo(InGameObjects.board, InGameObjects.gametype);
                        String s = SetHistory.BoardtoString(hInfo);
                        AccountSession.setRawJson(s);

                        // 同步写回 accounts/<username>.json（保留原有字段，更新或插入 history 字段）
                        String username = AccountSession.getUsername();
                        if (username != null && !username.isEmpty()) {
                            try {
                                Path dir = Paths.get("accounts");
                                if (!Files.exists(dir))
                                    Files.createDirectories(dir);
                                Path acct = dir.resolve(sanitizeFileName(username) + ".json");
                                String orig = null;
                                if (Files.exists(acct)) {
                                    orig = new String(Files.readAllBytes(acct), StandardCharsets.UTF_8);
                                }
                                String escaped = escapeJson(s);
                                String newJson;
                                boolean isGameOver = false;
                                try {
                                    isGameOver = InGameObjects.board != null && InGameObjects.board.gameOver();
                                } catch (Exception ex) {
                                    isGameOver = false;
                                }

                                if (isGameOver) {
                                    // If game is over, clear stored history: remove history field and delete
                                    // .history file
                                    AccountSession.setRawJson(null);
                                }

                                if (orig != null) {
                                    // 如果已有 history 字段，替换之；否则在末尾插入
                                    Pattern p = Pattern.compile("(\"history\"\\s*:\\s*\")[^\"]*(\")", Pattern.DOTALL);
                                    Matcher m = p.matcher(orig);
                                    if (m.find()) {
                                        if (isGameOver) {
                                            // remove the history field entirely
                                            newJson = m.replaceFirst("");
                                        } else {
                                            newJson = m.replaceFirst("$1" + escaped + "$2");
                                        }
                                    } else {
                                        int idx = orig.lastIndexOf('}');
                                        if (idx >= 0) {
                                            // 插入一个 history 字段
                                            // 如果原始 JSON 不是以 { } 包裹，降级为覆盖写入
                                            String prefix = orig.substring(0, idx).trim();
                                            if (prefix.endsWith(",")) {
                                                if (isGameOver) {
                                                    newJson = prefix + "}";
                                                } else {
                                                    newJson = prefix + "\"history\":\"" + escaped + "\"}";
                                                }
                                            } else if (prefix.endsWith("{")) {
                                                if (isGameOver) {
                                                    newJson = prefix + "}";
                                                } else {
                                                    newJson = prefix + "\"history\":\"" + escaped + "\"}";
                                                }
                                            } else {
                                                if (isGameOver) {
                                                    // remove history by leaving original content unchanged
                                                    newJson = orig.substring(0, idx) + "}";
                                                } else {
                                                    newJson = orig.substring(0, idx) + ",\"history\":\"" + escaped
                                                            + "\"}";
                                                }
                                            }
                                        } else {
                                            // 无法解析原始 JSON，直接创建新 JSON
                                            newJson = isGameOver ? "{}" : "{\"history\":\"" + escaped + "\"}";
                                        }
                                    }
                                } else {
                                    // 原文件不存在：创建一个最小的 account JSON（不含密码）
                                    String email = AccountSession.getEmail();
                                    if (isGameOver) {
                                        newJson = "{" +
                                                "\"loggedIn\":false," +
                                                "\"username\":\"" + escapeJson(username) + "\"," +
                                                "\"email\":\"" + escapeJson(email) + "\"," +
                                                "\"password\":\"\"}";
                                    } else {
                                        newJson = "{" +
                                                "\"loggedIn\":false," +
                                                "\"username\":\"" + escapeJson(username) + "\"," +
                                                "\"email\":\"" + escapeJson(email) + "\"," +
                                                "\"password\":\"\"," +
                                                "\"history\":\"" + escaped + "\"}";
                                    }
                                }
                                // 写回文件
                                try {
                                    Files.write(acct, newJson.getBytes(StandardCharsets.UTF_8));
                                    // 如果游戏结束，删除 .history 文件；否则写入历史串
                                    try {
                                        Path hist = dir.resolve(sanitizeFileName(username) + ".history");
                                        if (isGameOver) {
                                            try {
                                                if (Files.exists(hist))
                                                    Files.delete(hist);
                                            } catch (Exception dex) {
                                                // ignore
                                            }
                                        } else {
                                            Files.write(hist, s.getBytes(StandardCharsets.UTF_8));
                                        }
                                    } catch (Exception ex) {
                                        System.err.println("Failed to write/delete history file: " + ex.getMessage());
                                    }
                                } catch (Exception ex) {
                                    System.err.println("Failed to write account file: " + ex.getMessage());
                                }
                            } catch (Exception ex) {
                                System.err.println("Failed to update account file: " + ex.getMessage());
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Failed to save history on window close: " + ex.getMessage());
                }
                gameFrame.dispose();
                if (Menu.frame != null) {
                    Menu.frame.setVisible(true);
                    if (Menu.musicButton.isPlaying == true) {
                        Menu.musicButton.doPlayMusic(); // 播放菜单音乐
                    }
                }
            }
        });

        try {
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
                    System.err.println("Board image not found in classpath (" + resourceName + ") or file system ("
                            + filePath + ")");
                }
            }
        } catch (IOException e) {
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
                        if (p != null)
                            p.paint(g);
                    }
                }
                // 绘制 BlueBox（使用 1-based 索引：行 1..10，列 1..9）
                for (int r = 1; r <= 10; r++) {
                    for (int c = 1; c <= 9; c++) {
                        BlueBox blueBox = (BlueBox) InGameObjects.blueBoxSession.getBoxAt(r, c);
                        if (blueBox != null)
                            blueBox.paint(g);
                    }
                }
                // 绘制 RedBox（使用 1-based 索引：行 1..10，列 1..9）
                for (int r = 1; r <= 10; r++) {
                    for (int c = 1; c <= 9; c++) {
                        RedBox redBox = (RedBox) InGameObjects.redBoxSession.getRedBoxAt(r, c);
                        if (redBox != null)
                            redBox.paint(g);
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

        // 棋盘和计时器放在centerPanel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        plate.setPreferredSize(new Dimension(pw, ph));
        centerPanel.add(plate, BorderLayout.CENTER);

        // 使用Model包下的CountdownTimer
        CountdownTimer timerModule = new CountdownTimer(60);
        JPanel timerPanel = timerModule.getPanel();
        int wrapperWidth = Math.max(100, timerPanel.getPreferredSize().width + 8);
        JPanel timerWrapper = new JPanel(new BorderLayout());
        timerWrapper.setOpaque(false);
        timerWrapper.setPreferredSize(new Dimension(wrapperWidth, ph));
        timerWrapper.add(timerPanel, BorderLayout.NORTH);
        centerPanel.add(timerWrapper, BorderLayout.EAST);

        gameFrame.add(centerPanel, BorderLayout.CENTER);

        // 使用 pack 让基于 preferredSize 的组件确定初始大小，然后将宽度调整为棋盘宽度的 1.5 倍
        gameFrame.pack();
        int frameWidth = (int) Math.round(plateWidth * 1.5);
        int frameHeight = gameFrame.getHeight();
        gameFrame.setSize(frameWidth, frameHeight);
        gameFrame.setLocationRelativeTo(null);

        // 设置其它小组件
        // 设置帮助页面
        HelpButton help = new HelpButton(gameFrame);
        // 设置投降按钮
        SurrenderButton surrender = new SurrenderButton(gameFrame);
        // 设置当前边图标
        // SideIcon sideIcon = new SideIcon(gameFrame);
        // 设置重开按钮
        RestartButton restartbutton = new RestartButton(gameFrame);
        // 设置Undo按钮
        UndoButton undobutton = new UndoButton(gameFrame);
        // 设置提示框
        MessageLabel messageLabel = new MessageLabel(gameFrame.getLayeredPane());

        gameFrame.setVisible(true);

        Menu.frame.setVisible(false); // 隐藏菜单界面
        if (Menu.musicButton.isPlaying == true) {
            Menu.musicButton.stopMusic(); // 停止菜单音乐
        }

        // 通知其它线程界面已初始化完成
        try {
            Class<?> inGameObjectsClass = Class.forName("com.Controller.InGameObjects");
            java.util.concurrent.CountDownLatch latch = (java.util.concurrent.CountDownLatch) inGameObjectsClass
                    .getField("uiReadyLatch").get(null);
            latch.countDown();
        } catch (Exception e) {
            System.err.println("无法countDown InGameObjects.uiReadyLatch: " + e.getMessage());
        }

        
        new Thread(() -> {
            try {
                if (InGameObjects.gametype == 1) {
                    // 设置Hinatsuru
                    Hinatsuru h = new Hinatsuru(gameFrame);
                    messageLabel.setMessage("Ciallo~~(∠・ω< )⌒★");
                    // 播放Ciallo.mav
                    try {
                        javax.sound.sampled.AudioInputStream audioInputStream = javax.sound.sampled.AudioSystem.getAudioInputStream(new java.io.File("src/main/resources/Hinatsuru/Callio.wav"));
                        javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                    } catch (Exception ex) {
                        System.err.println("播放音频失败: " + ex.getMessage());
                    }

                    Thread.sleep(2000);
                    messageLabel.setMessage("I'm Hinatsuru, your AI opponent!");
                    Thread.sleep(2000);
                    messageLabel.setMessage("Let's have a fun game together!");
                    Thread.sleep(2000);
                    messageLabel.setDefault();
                } else {
                    messageLabel.setMessage("欢迎来到中国象棋游戏");
                    Thread.sleep(1500);
                    messageLabel.setDefault();
                }
            } catch (InterruptedException e) {
                // 忽略
            }
            InGameObjects.messageLabel = messageLabel;
        }).start();

        // 如果设置为自动开始倒计时，则初始化后立刻启动
        if (com.chinesechess.Main.settingsSession != null && com.chinesechess.Main.settingsSession.isStartTimer()) {
            InGameObjects.countdownTimer.start();
        }
    }

    private static String sanitizeFileName(String name) {
        if (name == null)
            return "";
        return name.replaceAll("[\\/:*?\"<>|]", "_");
    }

    private static String escapeJson(String s) {
        if (s == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20 || c > 0x7E) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}
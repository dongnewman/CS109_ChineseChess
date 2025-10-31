package main.java.com.Model.About;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.SwingUtilities;


/**
 * 显示关于信息的模态对话框。
 *
 * 使用示例:
 *   new DoAbout();
 */
public class DoAbout {
    public DoAbout(){
        SwingUtilities.invokeLater(new Runnable () {
            @Override
            public void run() {
                showDialog();
            }
        });
    }

    
    private void showDialog() {
        // 此方法可根据需要实现显示逻辑
        JFrame aboutFrame = new JFrame("关于");
        aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        aboutFrame.setSize(400, 300);
        aboutFrame.setLocationRelativeTo(null);

        // 添加关于信息标签
        JLabel jl = new JLabel();
        jl.setText("<html><body style='text-align: center;'>" +
                "<h2>中国象棋 (Chinese Chess)</h2>" +
                "<p>版本: 1.0.0</p>" +
                "<p>作者: DONG Xinyu & Chen Kaian</p>" +
                "<p>版权所有 © 2025</p>" +
                "</body></html>");
        aboutFrame.add(jl);

        // 添加返回按钮
        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> aboutFrame.dispose());
        aboutFrame.add(backButton, BorderLayout.SOUTH);

        aboutFrame.setVisible(true);
    }
}
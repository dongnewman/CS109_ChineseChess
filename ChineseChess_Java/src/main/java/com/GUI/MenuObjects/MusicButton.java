package com.GUI.MenuObjects;


import javax.swing.JFrame;
import javax.swing.JButton;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;


public class MusicButton {
    private final String music_path = "src\\main\\resources\\audio\\BGM.wav";
    private final String active_image_path = "src\\main\\resources\\audio\\sound.png";
    private final String inactive_image_path = "src\\main\\resources\\audio\\silence.png";
    JFrame parentFrame;
    public boolean isPlaying = true;
    private Clip clip;
    private BufferedImage active_image = null;
    private BufferedImage inactive_image = null;
    private JButton musicbutton;

    public MusicButton(JFrame parentFrame, JButton musicbutton) {
        this.parentFrame = parentFrame;
        this.musicbutton = musicbutton;

        // 加载图片
        try {
            File f = new File(active_image_path);
            if (f.exists()) {
                active_image = ImageIO.read(f);
            }
        } catch (IOException e) {
            System.out.println("音乐按钮图片加载失败: " + e.getMessage());
        }
        if (active_image == null) {
            try (java.io.InputStream in = MenuBackgroundInit.class.getResourceAsStream("/Menu.png")) {
                if (in != null) {
                    active_image = ImageIO.read(in);
                }
            } catch (IOException e) {
                System.out.println("音乐按钮图片加载失败: " + e.getMessage());
            }
        }

        try {
            File f = new File(inactive_image_path);
            if (f.exists()) {
                inactive_image = ImageIO.read(f);
            }
        } catch (IOException e) {
            // ignore, try classpath next
        }
        if (inactive_image == null) {
            try (java.io.InputStream in = MenuBackgroundInit.class.getResourceAsStream("/Menu.png")) {
                if (in != null) {
                    inactive_image = ImageIO.read(in);
                }
            } catch (IOException e) {
                System.out.println("音乐按钮图片加载失败: " + e.getMessage());
            }
        }
        if(active_image != null) {
        // 去掉原有的
        musicbutton.setBorderPainted(false);
        musicbutton.setContentAreaFilled(false);
        musicbutton.setFocusPainted(false);
        // 加上自己的
        musicbutton.setIcon(new ImageIcon(active_image));

        musicbutton.setPreferredSize(new java.awt.Dimension(60, 60));
        // 由外部决定布局和添加方式
        // 默认直接add，布局由Menu控制
    doPlayMusic();
    parentFrame.repaint();

        musicbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // 切换状态
                isPlaying = !isPlaying;
                if(isPlaying) {
                    // 切换到播放状态
                    musicbutton.setIcon(new ImageIcon(active_image));
                    doPlayMusic();
                } else {
                    // 切换到静音状态
                    musicbutton.setIcon(new ImageIcon(inactive_image));
                    stopMusic();
                }
            }
        });
        }
        

    }

    public void doPlayMusic() {
        try {
            File musicFile = new File(music_path);
            if (musicFile.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } else {
                System.out.println("音乐文件未找到: " + music_path);
            }
        } catch (Exception e) {
            System.out.println("音乐播放失败: " + e.getMessage());
        }
    }

    public void stopMusic() {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        } catch (Exception e) {
            System.out.println("停止音乐失败: " + e.getMessage());
        }
    }

}

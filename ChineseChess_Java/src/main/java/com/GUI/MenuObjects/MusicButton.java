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

import com.chinesechess.Main;

public class MusicButton {
    JFrame parentFrame;
    public boolean isPlaying = true;
    private Clip clip;
    private BufferedImage active_image = null;
    private BufferedImage inactive_image = null;
    private JButton musicbutton;

    public MusicButton(JFrame parentFrame, JButton musicbutton) {
        this.parentFrame = parentFrame;
        this.musicbutton = musicbutton;

        if (Main.settingsSession.isDisableMusic()) {
            isPlaying = false;
        }

        // 加载图片：优先类路径，再回退到文件系统
        final String activeRes = "/audio/sound.png";
        final String inactiveRes = "/audio/silence.png";
        final String activeFs = "src\\main\\resources\\audio\\sound.png";
        final String inactiveFs = "src\\main\\resources\\audio\\silence.png";
        try (java.io.InputStream in = getClass().getResourceAsStream(activeRes)) {
            if (in != null)
                active_image = ImageIO.read(in);
        } catch (IOException e) {
            System.out.println("音乐按钮图片加载失败 (active): " + e.getMessage());
        }
        if (active_image == null) {
            try {
                File f = new File(activeFs);
                if (f.exists())
                    active_image = ImageIO.read(f);
            } catch (IOException e) {
                System.out.println("音乐按钮图片加载失败 (active fallback): " + e.getMessage());
            }
        }

        try (java.io.InputStream in = getClass().getResourceAsStream(inactiveRes)) {
            if (in != null)
                inactive_image = ImageIO.read(in);
        } catch (IOException e) {
            System.out.println("音乐按钮图片加载失败 (inactive): " + e.getMessage());
        }
        if (inactive_image == null) {
            try {
                File f = new File(inactiveFs);
                if (f.exists())
                    inactive_image = ImageIO.read(f);
            } catch (IOException e) {
                System.out.println("音乐按钮图片加载失败 (inactive fallback): " + e.getMessage());
            }
        }
        if (active_image != null) {
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
                    if (isPlaying) {
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
        if (Main.settingsSession.isDisableMusic()) {
            return;
        }

        try {
            // 先尝试从类路径加载音频
            final String musicRes = "/audio/BGM.wav";
            final String musicFs = "src\\main\\resources\\audio\\BGM.wav";
            AudioInputStream audioInputStream = null;
            java.io.InputStream in = getClass().getResourceAsStream(musicRes);
            if (in != null) {
                audioInputStream = AudioSystem.getAudioInputStream(new java.io.BufferedInputStream(in));
            } else {
                File musicFile = new File(musicFs);
                if (musicFile.exists()) {
                    audioInputStream = AudioSystem.getAudioInputStream(musicFile);
                }
            }
            if (audioInputStream != null) {
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // 自动循环播放
                clip.start();
            } else {
                System.out.println("音乐文件未找到: " + musicFs + " or classpath " + musicRes);
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

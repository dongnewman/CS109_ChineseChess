在Java中使用Swing插入并播放音乐，通常流程是：

1. **准备音乐文件**：常用格式如 `.wav` 或 `.au`，因为 Java 官方 API（`javax.sound.sampled`）对这些格式支持最好（MP3 需额外库）。
2. **使用 javax.sound.sampled API 加载和播放**。
3. **集成到Swing界面**，通过按钮或其他事件来控制音乐播放。

下面是一个基础完整示例：

```java
import javax.swing.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;

public class MusicPlayerSwing extends JFrame {
    private JButton playButton;
    private Clip clip;

    public MusicPlayerSwing() {
        playButton = new JButton("播放音乐");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playMusic();
            }
        });

        this.add(playButton);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(200, 100);
        this.setVisible(true);
    }

    private void playMusic() {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("test.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "音乐播放失败：" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new MusicPlayerSwing();
    }
}
```

### 注意事项

- 推荐音乐文件使用 `.wav` 格式，且16位、44100Hz PCM格式兼容性最好。
- 若需播放 mp3，需要额外如 `jlayer` 或其它第三方库。
- 将音乐文件 `test.wav` 放在工程根目录或指定路径下。

---

**如需更高级功能（暂停/继续/停止/多个音乐文件等），可以升级代码逻辑和界面。** 若有特定功能需求或遇到具体报错，可以继续提问！\

音乐选择：广陵散，高山流水

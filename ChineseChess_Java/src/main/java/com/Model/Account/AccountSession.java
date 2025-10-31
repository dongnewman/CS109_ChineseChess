package main.java.com.Model.Account;

/**
 * 公共的会话信息类（简易实现）。
 * 其他类可以通过 AccountSession.* 访问当前登录状态与基本账号信息。
 */
public class AccountSession {

    // 是否已登录（私有，仅通过 getter/setter 访问）
    private static boolean loggedIn = false;

    
    // 基础账号信息（示例字段） - 私有，仅通过 getter/setter 访问
    private static String username = null;
    private static String email = null;

    // 原始 JSON 内容（若需要调试或进一步解析）
    private static String rawJson = null;

    public static void clear() {
        setLoggedIn(false);
        username = null;
        email = null;
        rawJson = null;
    }

    public static void setFromJson(String json) {
        setRawJson(json);
        if (json == null) {
            clear();
            return;
        }

        // 简易解析：查找 loggedIn, username, email（不依赖外部 JSON 库，容错性有限）
    String lowered = json.toLowerCase();
    // 使用 setter 来改变登录状态
    setLoggedIn(lowered.contains("\"loggedin\"") && lowered.contains("true"));

        // 提取 username 和 email 的值（如果存在）
        setUsername(extractString(json, "username"));
        setEmail(extractString(json, "email"));
    }

    private static String extractString(String json, String key) {
        try {
                // 注意：Java 字符串中需要对双引号与反斜杠进行转义
                String pattern = "\\\"" + key + "\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) return m.group(1);
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    // Getter / Setter for loggedIn
    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(boolean value) {
        loggedIn = value;
    }

    // Getters / Setters for username, email, rawJson
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String u) {
        username = u;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String e) {
        email = e;
    }

    public static String getRawJson() {
        return rawJson;
    }

    public static void setRawJson(String r) {
        rawJson = r;
    }
}

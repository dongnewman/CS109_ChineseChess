package com.Controller;

/**
 * 枚举化的菜单行为标识，避免在 MenuListener 中直接使用字符串匹配。
 * 提供 fromText() 将菜单项显示文本映射到枚举值，便于支持多种文本别名。
 */
public enum MenuAction {
    PREFERENCES,
    VIEW_PROFILE,
    LOGIN,
    REGISTER,
    LOGOUT,
    DELETE_ACCOUNT,
    NEW_GAME,
    LOAD_GAME,
    VIEW_RECORD,
    DELETE_RECORD,
    HELP_CONTENTS,
    ABOUT,
    LEAVE;

    public static MenuAction fromText(String text) {
        if (text == null) return null;
        switch (text) {
            case "偏好设置": return PREFERENCES;
            case "查看资料": return VIEW_PROFILE;
            case "登录": return LOGIN;
            case "注册": return REGISTER;
            case "注销": return LOGOUT;
            case "删除账号": return DELETE_ACCOUNT;
            case "新游戏": return NEW_GAME;
            case "加载游戏": return LOAD_GAME;
            case "查看记录": return VIEW_RECORD;
            case "删除记录": return DELETE_RECORD;
            case "帮助内容": return HELP_CONTENTS;
            case "关于":
            case "关于本软件": return ABOUT;
            case "离开": return LEAVE;
            default: return null;
        }
    }
}

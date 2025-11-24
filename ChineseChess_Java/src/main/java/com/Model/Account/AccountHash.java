package com.Model.Account;

public abstract class AccountHash {
    private static final long P = 998244353L;
    private static final long Mo = 1000000007L;

    /**
     * 简单多项式哈希（保持兼容性但修复字符处理错误）。
     * 注意：这是弱哈希，不推荐用于真实密码存储，建议使用 PBKDF2/bcrypt/scrypt 等。
     */
    public static long hash(String password) {
        if (password == null || password.length() == 0)
            return 0L;
        long hashValue = 0L;
        long pPower = 1L;
        for (int i = 0; i < password.length(); i++) {
            int ch = (int) password.charAt(i);
            long add = ((long) ch % Mo) * pPower % Mo;
            hashValue = (hashValue + add) % Mo;
            pPower = (pPower * P) % Mo;
        }
        if (hashValue < 0)
            hashValue += Mo;
        return hashValue;
    }

    public static long hash(char[] password) {
        if (password == null || password.length == 0)
            return 0L;
        long hashValue = 0L;
        long pPower = 1L;
        for (int i = 0; i < password.length; i++) {
            int ch = (int) password[i];
            long add = ((long) ch % Mo) * pPower % Mo;
            hashValue = (hashValue + add) % Mo;
            pPower = (pPower * P) % Mo;
        }
        if (hashValue < 0)
            hashValue += Mo;
        return hashValue;
    }
}

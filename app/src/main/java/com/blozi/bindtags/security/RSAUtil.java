package com.blozi.bindtags.security;

/**
 * Created by loongzhangtao on 2017/9/a.
 */
public class RSAUtil extends RSACoder {
    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCa69S13rsOXDSP6dRoJqtK+mH7t09CHDuUNs1/5WWkeiWnKCSn7DfLkn3rVmTHALJjaDyiJIRBT5GQs1fx4P2W8yoXUc6UFXEaB9LyCXVQi8KgW0Xrtn8j/8CxRHKYrZDaLG7nGQCEPc0rhydoOehBkIY8jxchXTeI3v2l/Ew78QIDAQAB";
    private static final String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJrr1LXeuw5cNI/p1Ggmq0r6Yfu3T0IcO5Q2zX/lZaR6JacoJKfsN8uSfetWZMcAsmNoPKIkhEFPkZCzV/Hg/ZbzKhdRzpQVcRoH0vIJdVCLwqBbReu2fyP/wLFEcpitkNosbucZAIQ9zSuHJ2g56EGQhjyPFyFdN4je/aX8TDvxAgMBAAECgYAI4PMz6Sv3SrmIdfJCddTjWy46ausT8uJHQEzOw6rrnkSCK/7N0O/xrtegoTbtYNDYeaTf9g/OXm0NSfoEVsyxvyYQUwCCPsZ4KcPypQFQ2YEn3/ZCBSaP3JChk0diuTFCGd9QvyqFKT3fya1NWElOayQiFVCFAqza76EGpKWNMQJBANRliXDLeMeR/0VrlOr8SiYqGGO8iiqOm3TZaVzl7xizFwlBe/7Mj8nkZuLOVoECnXs9BBuEyeq5CuWCAaEzxC8CQQC6ua9XARz9H8pRQSrj3CLUsT8ZMoypndt7VHW2aRWA9ypAQJi8MgYwoqHGoAUSrzQAZGZtZXjDVpQGTJg/AFnfAkEAiBZSvLGyPnS+EbW/odxqig94txbLlD7xPio7RymQS8wb23NYxkY9bwdoILRCWsDZNGhA5HztfvZ3E9x7vNTVQwJBAJ9Zwbo1WADotWccfzbP8eWjkd62FfbWoa0higdurIxP4n3M4suQwVmWIQt6Gno0jONcy1DGzQWfCFpL2Y3Che8CQQCO0tC7UFnySw3Xc3Ttk01KOWyO4oqI1/Wq11Tdcgn3G/D9A8i6FO+n5shaeLmHuixkrIkT/oaq79WKtYmbVnu1";

    public static String encryptionPri(String source) {
        try {
            if (source != null)
                return RSACoder.encryptBASE64(RSACoder.encryptByPrivateKey(source.getBytes("utf-8"), privateKey));
        } catch (Exception e) {
        }
        return source;
    }

    public static String decryPri(String enPubStr) {
        try {
            return new String(decryptByPrivateKey(decryptBASE64(enPubStr), privateKey), "utf-8");
        } catch (Exception e) {
        }
        return "";
    }

    public static String encryptionPub(String source) {
        try {
            if (source != null)
                return encryptBASE64(encryptByPublicKey(source.getBytes("utf-8"), publicKey));

        } catch (Exception e) {
        }
        return source;
    }

    public static String decryPub(String enPrivStr) {
        try {
            return new String(decryptByPublicKey(decryptBASE64(enPrivStr), publicKey), "utf-8");
        } catch (Exception e) {
        }
        return "";
    }
}

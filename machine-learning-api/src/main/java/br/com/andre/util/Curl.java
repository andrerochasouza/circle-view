package br.com.andre.util;

public class Curl {

    public static String get(String url) {
        String result = "";
        try {
            ProcessBuilder pb = new ProcessBuilder("curl", "-i", url);
            Process p = pb.start();
            p.waitFor();
            result = new String(p.getInputStream().readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String post(String url, String body) {
        String result = "";
        try {
            ProcessBuilder pb = new ProcessBuilder("curl", "-i", "-X", "POST", "-d", body, url);
            Process p = pb.start();
            p.waitFor();
            result = new String(p.getInputStream().readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

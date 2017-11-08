package com.pook.youtubelinebot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pook on 8/11/2017.
 */
public class YoutubeUrlUtil {
    private static Pattern youtubeUrlPattern = Pattern.compile("((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?");

    public static String getYoutubeVideoId(String str) {
        Matcher matcher = youtubeUrlPattern.matcher(str);
        if (matcher.find() && matcher.groupCount() >= 5) {
            return matcher.group(5);
        } else {
            return "";
        }
    }

    public static boolean containsYoutubeUrl(String str) {
        Matcher matcher = youtubeUrlPattern.matcher(str);
        if (matcher.find() && matcher.groupCount() >= 5) {
            return true;
        } else {
            return false;
        }
    }
}
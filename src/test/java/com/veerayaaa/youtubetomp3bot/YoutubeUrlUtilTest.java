package com.veerayaaa.youtubetomp3bot;

import com.veerayaaa.youtubetomp3bot.util.YoutubeUrlUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by pook on 8/11/2017.
 */
public class YoutubeUrlUtilTest {
    @Test
    public void getUrlFromString() {
        String id = YoutubeUrlUtil.getYoutubeVideoId("please convert https://www.youtube.com/v/DFYRQ_zQ-gk?fs=1&hl=en_US");
        Assert.assertEquals("DFYRQ_zQ-gk", id);
    }

    @Test
    public void getUrlFromString_whenNoUrl() {
        String url = YoutubeUrlUtil.getYoutubeVideoId("please convert");
        Assert.assertEquals("", url);
    }
}

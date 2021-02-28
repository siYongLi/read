package net.ninemm.upms.cron;

import cn.hutool.core.io.FileUtil;
import com.jfinal.kit.PathKit;
import io.jboot.components.schedule.annotation.Cron;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: lsy
 * @create: 2021-02-28 21:17
 **/

@Cron("*/1 * * * *")
public class ClearFolder implements Runnable{
    @Override
    public void run() {
        String folderName = new StringBuilder(PathKit.getWebRootPath())
                .append(File.separator).append("excel").toString();
        boolean del = FileUtil.del(folderName);
        System.out.println(del);
    }
}

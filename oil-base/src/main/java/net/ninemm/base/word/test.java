package net.ninemm.base.word;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

import java.io.File;

/**
 * @description:
 * @author: lsy
 * @create: 2019-07-10 15:00
 **/
public class test {
    public static void main(String[] args) {
        /*IDocument doc = new Document2004();
        doc.addEle(Heading1.with("Heading01").create());
        doc.addEle(Paragraph.with("This document is an example of paragraph").create());
        doc.addEle(new Image("http://image.ejuster.cn/528ba575f54f41eba8803c8c5d1517ad.png", ImageLocation.WEB_URL).create());
        String myWord = doc.getContent();
        File fileObj = new File("D:\\3333.doc");
        //将生成的xml内容写入到文件中。
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.println(myWord);
        writer.close();*/
        String word = "D://3333.doc";
        String name = "D://3333.pdf";
        wToPdfChange(word, name);
    }

    public static void wToPdfChange(String wordFile,String pdfFile){
        ActiveXComponent app = null;
        try {
            app = new ActiveXComponent("Word.Application");
            Dispatch documents = app.getProperty("Documents").toDispatch();
            System.out.println("打开文件: " + wordFile);
            // 打开文档
            Dispatch document = Dispatch.call(documents, "Open", wordFile, false, true).toDispatch();
            // 如果文件存在的话，不会覆盖，会直接报错，所以我们需要判断文件是否存在
            File target = new File(pdfFile);
            if (target.exists()) {
                target.delete();
            }
            System.out.println("另存为: " + pdfFile);
            Dispatch.call(document, "SaveAs", pdfFile, 17);
            // 关闭文档
            Dispatch.call(document, "Close", false);
        }catch(Exception e) {
            System.out.println("转换失败"+e.getMessage());
        }finally {
            // 关闭office
            app.invoke("Quit", 0);
        }
    }
}

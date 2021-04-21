package com.hzfc.management.yjzx;

import com.hzfc.management.yjzx.utils.wordutils2.Word2Html;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallTinyApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void mytest() throws IOException {
        Word2Html word2Html = new Word2Html();
        word2Html.doc2Html("G:\\wordTest\\file\\word\\hhh.docx");
    }
}

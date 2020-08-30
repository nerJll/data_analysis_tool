import com.ner.Application;
import com.ner.service.SizeAnalysisService;
import com.ner.utils.DataAnalysisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @autor jiangll
 * @date 2020/8/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ObjTest {
    @Autowired
    private SizeAnalysisService sizeAnalysisService;

    @Test
    public void t1() {
        int j = 10;
        for (int i = 0; i < 3; i++) {
            System.out.println(j * i / 3 + " " + j * (i + 1) / 3);
        }
    }

    private Integer v = 0;

    @Test
    public void t2() {
        ThreadTest t1 = new ThreadTest(3);
        ThreadTest t2 = new ThreadTest(5);
        Thread th1 = new Thread(t1);
        Thread th2 = new Thread(t2);
        th1.start();
        th2.start();
        synchronized (v) {
            try {
                th1.join();
                th2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            v += t1.getValue();
            System.out.println(v);
            v += t2.getValue();
            System.out.println(v);
        }
        System.out.println(t1.getValue());
        System.out.println(t2.getValue());
    }
}

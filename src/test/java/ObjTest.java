import com.ner.Application;
import com.ner.service.StuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @autor jiangll
 * @date 2020/8/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ObjTest {
    @Autowired
    private StuService stuService;

    @Test
    public void t1() {
        stuService.updStu(null);
    }

    @Test
    public void t2() {

    }
}

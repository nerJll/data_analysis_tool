import com.ner.Application;
import com.ner.service.SizeAnalysisService;
import com.ner.utils.DataAnalysisUtil;
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
    private SizeAnalysisService sizeAnalysisService;

    @Test
    public void t1() {
    }

    @Test
    public void t2() {
        DataAnalysisUtil.analysisTmall("胸罩");
    }
}

import lombok.Data;

/**
 * @autor jiangll
 * @date 2020/8/29
 */
@Data
public class ThreadTest implements Runnable {
    private int value;

    public ThreadTest(int value) {
        this.value = value;
    }

    @Override
    public void run() {
        this.value = value * 5 * 7;
    }
}

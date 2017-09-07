import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static java.lang.System.out;

public class MainTest {



    @Test
    public void testaa() {
        List<Integer> l = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            l.add(i*i);
        }
        Stream<Integer> s = l.stream().filter(i -> i<222).map(i -> i-2);
        s.peek(out::println).map(i->i-1).forEach(out::println);

        assertTrue(false);
    }

}
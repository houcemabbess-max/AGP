package business;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class TransportSelectorTest {

    @Autowired
    private TransportSelector transportSelector;

    @Test
    public void chooseTransport_shouldReturnTransport() {
        Transport t = transportSelector.chooseTransport(
                new Coordinates(0, 0),
                new Coordinates(1, 1),
                50.0
        );
        assertNotNull(t);
    }

    @Test
    public void chooseTransport_shouldReturnOnFoot_whenBudgetZero() {
        Transport t = transportSelector.chooseTransport(
                new Coordinates(0, 0),
                new Coordinates(1, 1),
                0.0
        );
        assertNotNull(t);
        assertTrue(t instanceof OnFoot);
    }
}

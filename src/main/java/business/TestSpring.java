package business;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {
    public static void main(String[] args) {

        ClassPathXmlApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        OfferBuilder builder = ctx.getBean("offerBuilder", OfferBuilder.class);
        TransportSelector selector = ctx.getBean("transportSelector", TransportSelector.class);

        System.out.println("OK Spring: OfferBuilder = " + builder);
        System.out.println("OK Spring: TransportSelector = " + selector);

        ctx.close();
    }
}

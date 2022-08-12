package cloud.lihan.rewardsprogram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @author lihanyun
 */
@EnableAsync
@SpringBootApplication
public class RewardsProgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardsProgramApplication.class, args);
    }

}

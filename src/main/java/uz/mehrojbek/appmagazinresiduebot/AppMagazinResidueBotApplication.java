package uz.mehrojbek.appmagazinresiduebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class AppMagazinResidueBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppMagazinResidueBotApplication.class, args);
        while (true){
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://tvkdbot.herokuapp.com/";
            restTemplate.exchange(url, HttpMethod.GET,null,String.class);
            try {
                TimeUnit.MINUTES.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

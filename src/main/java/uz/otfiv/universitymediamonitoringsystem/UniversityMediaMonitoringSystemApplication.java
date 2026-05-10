package uz.otfiv.universitymediamonitoringsystem;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UniversityMediaMonitoringSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityMediaMonitoringSystemApplication.class, args);
    }

    @Value("${exception.telegram.bot.token}")
    private String BOT_TOKEN;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(BOT_TOKEN);
    }

}

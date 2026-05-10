package uz.otfiv.universitymediamonitoringsystem.exception;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class ExceptionService {

    @Value("${exception.telegram.bot.chat.id}")
    private Long chatId;

    private final TelegramBot telegramBot;

    public void sendDocument(String caption, byte[] fileBytes, String filename) {
        ByteArrayInputStream file = new ByteArrayInputStream(fileBytes);
        SendDocument sendDocument = new SendDocument(chatId, file.readAllBytes())
                .fileName(filename)
                .caption(caption);

        telegramBot.execute(sendDocument);
    }
}

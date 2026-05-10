package uz.otfiv.universitymediamonitoringsystem.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionService exceptionService;

    public void sendExceptionMessage(Exception exception) {
        String exceptionType = exception.getClass().getName();
        String message = exception.getMessage();

        String collect = Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));

        String occurredAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String format = String.format("""
                "🚨 *Exception Alert!* 🚨\n\n" +
                                        "*🔧 Exception Type:* %s\n" +
                                        "*📝 Message:* %s\n\n" +
                                        "*📝 Stack Trace:*\n%s\n\n" +
                                        "*⏰ Occurred At:* %s\n\n" +
                                        "*📌 Additional Information:*\n"
                """, exceptionType, message, collect.replaceAll("(?m)^", "• "), occurredAt);

        File file = new File("exception_report.txt");

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));) {
            bufferedWriter.write(format);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            exceptionService.sendDocument("Exception Repost", fileBytes, "exception_report.txt");
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

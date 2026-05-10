package uz.otfiv.universitymediamonitoringsystem.projection;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GetAllBroadcasts {
    UUID getId();

    String getTitle();

    LocalDate getEventDate();

    List<String> getStuff();

    String getMessenger();

    Integer getNumberOfPeople(); // chatda yoki efirda qatnashgan insonlar soni

    String getLink();

    String getGrade();
}

package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;
import org.joda.time.DateTime;
import java.util.List;


@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class InformationAboutQuantities {
    private String startTime;
    private String endTime;
    private List<AmountOfCurrenciesOnTheRelevantDates> amounts;
}

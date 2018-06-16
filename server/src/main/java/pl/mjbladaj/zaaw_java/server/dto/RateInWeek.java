package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RateInWeek {
    private double monday;
    private double tuesday;
    private double wednesday;
    private double thursday;
    private double friday;
    private double saturday;
    private double sunday;
}

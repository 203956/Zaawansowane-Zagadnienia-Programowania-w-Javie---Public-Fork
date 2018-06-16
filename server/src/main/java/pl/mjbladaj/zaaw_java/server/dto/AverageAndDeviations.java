package pl.mjbladaj.zaaw_java.server.dto;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AverageAndDeviations {
    private double average;
    private List<Double> deviations;
}

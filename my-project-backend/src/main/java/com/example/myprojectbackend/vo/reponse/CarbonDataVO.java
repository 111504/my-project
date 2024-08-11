package com.example.myprojectbackend.vo.reponse;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarbonDataVO {
    // Getters and Setters
    private int id;
    private String month;
    private double peak;
    private double halfPeak;
    private double offPeak;
    private double totalUsage;
    private String description;
    private double emissions;
    private String file;
}

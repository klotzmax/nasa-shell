package de.maxiklotz.nasashell.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class NasaDate {

    private LocalDate date;
}

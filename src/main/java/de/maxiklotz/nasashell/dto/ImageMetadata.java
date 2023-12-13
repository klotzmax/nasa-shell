package de.maxiklotz.nasashell.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageMetadata {

    private String image;

    private String date;

    private String caption;

    private CentroidCoordinates centroid_coordinates;

    private Coordinates dscovr_j2000_position;

    private Coordinates lunar_j2000_position;

    private Coordinates sun_j2000_position;

    private AttitudeQuaternions attitude_quaternions;
}

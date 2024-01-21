package web.xelj8.lab4.http.resources;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import web.xelj8.lab4.model.Shot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ShotResource {
    Long id;
    Double x;
    Double y;
    Double r;
    Boolean inArea;
    String shotTime;

    public ShotResource(Shot shot) {
        id = shot.getId();
        x = shot.getX();
        y = shot.getY();
        r = shot.getR();
        inArea = shot.getInArea();
        shotTime = shot.getShotTime();
    }

    static public List<ShotResource> list(List<Shot> shots) {
        return shots.stream()
                .map(ShotResource::new)
                .collect(Collectors.toList());
    }
}

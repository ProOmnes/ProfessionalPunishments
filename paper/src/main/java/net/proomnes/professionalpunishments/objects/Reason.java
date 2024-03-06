package net.proomnes.professionalpunishments.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Reason {

    private final String id;
    private final String reason;
    private final String duration;

}

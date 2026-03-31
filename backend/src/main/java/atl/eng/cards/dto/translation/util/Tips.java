package atl.eng.cards.dto.translation.util;

import java.util.ArrayList;
import java.util.List;

import atl.eng.cards.dto.translation.Tip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tips {
    private List<Tip> tips = new ArrayList<>();
}

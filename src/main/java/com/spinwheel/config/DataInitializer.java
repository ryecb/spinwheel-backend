package com.spinwheel.config;

import com.spinwheel.model.Theme;
import com.spinwheel.model.WheelConfig;
import com.spinwheel.model.WheelItem;
import com.spinwheel.repository.ThemeRepository;
import com.spinwheel.repository.WheelConfigRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final WheelConfigRepository wheelConfigRepository;
    private final ThemeRepository themeRepository;

    public DataInitializer(WheelConfigRepository wheelConfigRepository, ThemeRepository themeRepository) {
        this.wheelConfigRepository = wheelConfigRepository;
        this.themeRepository = themeRepository;
    }

    @Override
    public void run(String... args) {
        if (wheelConfigRepository.count() == 0) {
            seedWheelConfigs();
        }
        if (themeRepository.count() == 0) {
            seedThemes();
        }
    }

    private void seedWheelConfigs() {
        WheelConfig loreli = new WheelConfig("PM Demo", true, "Loreli");
        List<String> loreliNames = Arrays.asList(
                "Sophie", "Marcus", "Elena", "Loreli", "Olivia",
                "Lucas", "Emma", "Noah", "Aria", "James",
                "Maya", "Leo", "Zoe", "Alex", "Claire"
        );
        for (int i = 0; i < loreliNames.size(); i++) {
            WheelItem item = new WheelItem(loreliNames.get(i), i);
            item.setWheelConfig(loreli);
            loreli.getItems().add(item);
        }
        wheelConfigRepository.save(loreli);

        WheelConfig teamLunch = new WheelConfig("Team Lunch", false, null);
        List<String> lunchNames = Arrays.asList(
                "Pizza", "Sushi", "Tacos", "Burgers", "Pasta", "Salad", "Ramen", "Curry"
        );
        for (int i = 0; i < lunchNames.size(); i++) {
            WheelItem item = new WheelItem(lunchNames.get(i), i);
            item.setWheelConfig(teamLunch);
            teamLunch.getItems().add(item);
        }
        wheelConfigRepository.save(teamLunch);
    }

    private void seedThemes() {
        themeRepository.save(new Theme(
                "Classic",
                Arrays.asList("#FF6B6B", "#4ECDC4", "#45B7D1", "#FFA07A", "#98D8C8",
                        "#F7DC6F", "#BB8FCE", "#85C1E2", "#F8B739", "#52B788",
                        "#FF8C94", "#A8E6CF", "#FFD3B6", "#FFAAA5", "#FF8B94"),
                "#667eea", "#764ba2", "#ff4444"
        ));

        themeRepository.save(new Theme(
                "Ocean",
                Arrays.asList("#0077B6", "#00B4D8", "#90E0EF", "#023E8A", "#48CAE4",
                        "#0096C7", "#ADE8F4", "#CAF0F8", "#005F73", "#0A9396",
                        "#94D2BD", "#E9D8A6", "#001219", "#3D5A80", "#98C1D9"),
                "#0077B6", "#023E8A", "#00B4D8"
        ));

        themeRepository.save(new Theme(
                "Sunset",
                Arrays.asList("#FF6B35", "#F7C59F", "#EFEFD0", "#004E89", "#1A659E",
                        "#FF4500", "#FF8C00", "#FFD700", "#FF6347", "#FF7F50",
                        "#E76F51", "#F4A261", "#E9C46A", "#264653", "#2A9D8F"),
                "#FF6B35", "#C62828", "#FFD700"
        ));

        themeRepository.save(new Theme(
                "Forest",
                Arrays.asList("#2D6A4F", "#40916C", "#52B788", "#74C69D", "#95D5B2",
                        "#B7E4C7", "#D8F3DC", "#1B4332", "#344E41", "#3A5A40",
                        "#588157", "#A3B18A", "#DAD7CD", "#606C38", "#283618"),
                "#2D6A4F", "#1B4332", "#95D5B2"
        ));
    }
}

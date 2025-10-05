package unicon.Achiva.member.interfaces;

import lombok.Getter;

import java.util.List;

@Getter
public class CategoryCountResponse {

    private final List<CategoryCount> categoryCounts;

    public CategoryCountResponse(List<CategoryCount> categoryCounts) {
        this.categoryCounts = categoryCounts;
    }

    public static CategoryCountResponse fromObjectList(List<Object[]> categoryCountObjects) {
        List<CategoryCount> categoryCounts = categoryCountObjects.stream()
                .map(obj -> new CategoryCount((String) obj[0], (Long) obj[1]))
                .toList();
        return new CategoryCountResponse(categoryCounts);
    }


    @Getter
    public static class CategoryCount {
        private final String category;
        private final Long count;

        public CategoryCount(String category, Long count) {
            this.category = category;
            this.count = count;
        }
    }
}

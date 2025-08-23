package unicon.Achiva.member.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicon.Achiva.member.infrastructure.MemberCategoryCounterRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CounterHelper {
    private final MemberCategoryCounterRepository counterRepo;

    @Transactional
    public MemberCategoryCounter lockOrInit(Long memberId, Category category) {
        MemberCategoryKey key = new MemberCategoryKey(memberId, category);
        return counterRepo.lockById(key).orElseGet(() -> {
            MemberCategoryCounter c = new MemberCategoryCounter();
            c.setId(key);
            c.setSize(0L);
            return counterRepo.saveAndFlush(c);
        });
    }

    // 교착 방지용: 항상 작은 쪽 먼저 락
    public List<MemberCategoryKey> orderedKeys(Long memberId, Category a, Category b) {
        Category first = a.name().compareTo(b.name()) <= 0 ? a : b;
        Category second = (first == a) ? b : a;
        return List.of(new MemberCategoryKey(memberId, first),
                new MemberCategoryKey(memberId, second));
    }
}
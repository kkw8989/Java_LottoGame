package lotto;

import java.util.*;

public class LottoTicket {
    private static final int LOTTO_NUMBERS = 6;
    private static final int MAX_NUMBER = 45;
    private Set<Integer> numbers;
    private static final Random random = new Random();

    public LottoTicket(Set<Integer> numbers) {
        this.numbers = numbers;
    }

    public Set<Integer> getNumbers() { // 로또 번호 반환 
        return numbers;
    }

    public List<Integer> getSortedNumbers() { // 로또 번호 정렬
        List<Integer> sortedNumbers = new ArrayList<>(numbers);
        Collections.sort(sortedNumbers);
        return sortedNumbers;
    }

    public static LottoTicket generateLottoNumbers() { // 로또 번호 생성
        Set<Integer> numbers = new HashSet<>();
        while (numbers.size() < LOTTO_NUMBERS) {
            numbers.add(random.nextInt(MAX_NUMBER) + 1);
        }
        return new LottoTicket(numbers);
    }

    public static int generateBonusNumber(Set<Integer> winningNumbers) { // 중복되지 않는 번호 생성
        int bonus;
        do {
            bonus = random.nextInt(MAX_NUMBER) + 1;
        } while (winningNumbers.contains(bonus));
        return bonus;
    }

    public int getMatchedCount(Set<Integer> winningNumbers) { // 당첨 번호랑 비교해서 일치하는 번호 개수 반환
        int count = 0;
        for (Integer number : numbers) {
            if (winningNumbers.contains(number)) {
                count++;
            }
        }
        return count;
    }

    public static int determineRank(int matchedCount, boolean hasBonus) {
        if (matchedCount == 6) {
            return 1; // 1등
        } else if (matchedCount == 5 && hasBonus) {
            return 2; // 2등
        } else if (matchedCount == 5) {
            return 3; // 3등
        } else if (matchedCount == 4) {
            return 4; // 4등
        } else if (matchedCount == 3) {
            return 5; // 5등
        } else {
            return 0; // 미당첨
        }
    }
}

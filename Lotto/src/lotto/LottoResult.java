package lotto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LottoResult {
    private static final int LOTTO_PRICE = 1000;
    
    public static String purchaseOutomaticTickets(int amount) { // 자동
        int numberOfTickets = amount / LOTTO_PRICE;
        List<LottoTicket> lottoTickets = new ArrayList<>();
        StringBuilder result = new StringBuilder();

        result.append(numberOfTickets).append("개 구매했습니다\n\n");

        // 로또 번호 생성
        for (int i = 0; i < numberOfTickets; i++) {
            LottoTicket ticket = LottoTicket.generateLottoNumbers();
            lottoTickets.add(ticket);
            result.append("발행된 로또 번호 ").append(i + 1).append(" : ").append(ticket.getSortedNumbers()).append("\n\n");
        }

        // 당첨 번호 및 보너스 번호 생성
        LottoTicket winningTicket = LottoTicket.generateLottoNumbers();
        int bonusNumber = LottoTicket.generateBonusNumber(winningTicket.getNumbers());

        result.append("당첨 번호 : ").append(winningTicket.getSortedNumbers()).append("\n");
        result.append("보너스 번호 : ").append(bonusNumber).append("\n\n");

        // 결과 출력
        appendResults(lottoTickets, winningTicket, bonusNumber, result);
        return result.toString();
    }

    public static String purchaseManualTickets(int amount, List<HashSet<Integer>> manualNumbersList) { // 수동
        StringBuilder result = new StringBuilder();
        result.append(manualNumbersList.size()).append("개의 수동 로또 번호를 구매했습니다\n\n");

        // 당첨 번호 및 보너스 번호 생성
        LottoTicket winningTicket = LottoTicket.generateLottoNumbers();
        int bonusNumber = LottoTicket.generateBonusNumber(winningTicket.getNumbers());

        result.append("당첨 번호 : ").append(winningTicket.getSortedNumbers()).append("\n");
        result.append("보너스 번호 : ").append(bonusNumber).append("\n\n");

        List<LottoTicket> manualTickets = new ArrayList<>();
        for (HashSet<Integer> numbers : manualNumbersList) {
            manualTickets.add(new LottoTicket(numbers));
        }
        appendResults(manualTickets, winningTicket, bonusNumber, result);
        return result.toString();
    }
    
    // 결과 출력
    private static void appendResults(List<LottoTicket> tickets, LottoTicket winningTicket, int bonusNumber, StringBuilder result) {
        for (LottoTicket ticket : tickets) {
            int matchedCount = ticket.getMatchedCount(winningTicket.getNumbers());
            boolean hasBonus = ticket.getNumbers().contains(bonusNumber);
            int rank = LottoTicket.determineRank(matchedCount, hasBonus);

            result.append("로또 번호 : ")
                    .append(ticket.getSortedNumbers())
                    .append(" -> 일치하는 번호 수 : ")
                    .append(matchedCount)
                    .append("개 -> ")
                    .append(hasBonus ? " (보너스 번호 포함) -> " : "")
                    .append(rank).append("등\n");
        }
    }
}

package lotto;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

public class LottoSwingEx extends JFrame {
    private JTextField amountField;
    private JTextArea resultArea;
    private JTextField manualInputField;
    private JRadioButton autoButton;
    private JRadioButton manualButton;
    private JButton purchaseButton;
    private int ticketsToBuy;
    private List<HashSet<Integer>> manualNumbersList = new ArrayList<>();
    private static final Random random = new Random();

    public LottoSwingEx() {
        setTitle("Lotto Game");
        setSize(1000, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙 배치
        setLayout(new BorderLayout(30, 30));

        // 메인 패널 설정
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel amountLabel = new JLabel("구매할 금액을 입력하세요 :");
        amountLabel.setFont(amountLabel.getFont().deriveFont(30f)); // 레이블 글자 크기 30으로 설정

        // 금액 입력 필드
        amountField = new JTextField(15);
        amountField.setFont(amountField.getFont().deriveFont(30f)); // 입력 필드 글자 크기
        
        mainPanel.add(amountLabel, createConstraints(0, 0));
        mainPanel.add(amountField, createConstraints(1, 0));

        // 라디오 버튼 설정
        autoButton = new JRadioButton("자동", true);
        manualButton = new JRadioButton("수동");
        ButtonGroup group = new ButtonGroup();
        group.add(autoButton);
        group.add(manualButton);
        
        autoButton.setFont(autoButton.getFont().deriveFont(24f)); 
        manualButton.setFont(manualButton.getFont().deriveFont(24f));

        mainPanel.add(autoButton, createConstraints(0, 1));
        mainPanel.add(manualButton, createConstraints(1, 1));

        JLabel manualInputLabel = new JLabel("수동 번호 입력 (1~45, 공백으로 구분) :");
        manualInputLabel.setFont(manualInputLabel.getFont().deriveFont(25f)); // 레이블 글자 크기 설정
        
        manualInputField = new JTextField(10);
        manualInputField.setFont(manualInputLabel.getFont().deriveFont(30f)); // 입력 필드 글자 크기
        
        mainPanel.add(manualInputLabel, createConstraints(0, 2));
        mainPanel.add(manualInputField, createConstraints(1, 2));

        // 구매 버튼
        purchaseButton = new JButton("구매");
        purchaseButton.setFont(purchaseButton.getFont().deriveFont(24f));
        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePurchase();
            }
        });
        
        mainPanel.add(purchaseButton, createConstraints(0, 3, 2, 1)); // 버튼 두 열로 확장

        // 결과 영역
        resultArea = new JTextArea(20, 30);
        resultArea.setEditable(false);
        resultArea.setFont(resultArea.getFont().deriveFont(12f)); // 결과 영역 글자 크기
        resultArea.setBackground(new Color(240, 240, 240));

        // 테두리 설정
        TitledBorder titledBorder = BorderFactory.createTitledBorder("결과");
        titledBorder.setTitleFont(titledBorder.getTitleFont().deriveFont(16f)); // "결과" 제목 글자 크기 설정
        resultArea.setBorder(titledBorder);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        // 메인 패널에 추가
        add(mainPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private GridBagConstraints createConstraints(int gridx, int gridy) { 
        return createConstraints(gridx, gridy, 1, 1);
    }

    private GridBagConstraints createConstraints(int gridx, int gridy, int width, int height) { 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx; 
        gbc.gridy = gridy; 
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(40, 10, 10, 10);
        return gbc;
    }

    private void handlePurchase() {
        try {
            int amount = Integer.parseInt(amountField.getText());

            // 1000원 단위 체크
            if (amount % 1000 != 0) {
                JOptionPane.showMessageDialog(this, "금액은 1000원 단위로 입력해야 합니다", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ticketsToBuy = amount / 1000; // 1000원 단위로 티켓 수 계산
            String result;

            if (autoButton.isSelected()) {
                result = LottoResult.purchaseOutomaticTickets(amount);
                resultArea.setText(result); // 자동 구매 결과 출력
            } else {
                // 수동 번호 입력 처리
                String manualInput = manualInputField.getText();
                HashSet<Integer> manualNumbers = new HashSet<>();
                String[] inputNumbers = manualInput.trim().split(" ");

                // 유효한 번호 추가
                for (String num : inputNumbers) {
                    if (!num.matches("\\d+")) { // 숫자가 아닌 경우
                        JOptionPane.showMessageDialog(this, "유효한 번호를 입력하세요", "입력 오류", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int number = Integer.parseInt(num.trim());
                    if (number < 1 || number > 45) {
                        JOptionPane.showMessageDialog(this, "번호는 1에서 45 사이여야 합니다", "입력 오류", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    manualNumbers.add(number);
                }

                // 6개 미만일 경우 무작위로 번호 추가
                while (manualNumbers.size() < 6) {
                    int randomNumber;
                    do {
                        randomNumber = random.nextInt(45) + 1; // 1~45 랜덤 숫자 생성
                    } while (manualNumbers.contains(randomNumber)); // 중복 제거
                    manualNumbers.add(randomNumber);
                }

                // 정렬된 번호 리스트 생성
                List<Integer> sortedNumbers = new ArrayList<>(manualNumbers);
                Collections.sort(sortedNumbers);

                // 리스트에 추가 및 결과 출력
                manualNumbersList.add(new HashSet<>(sortedNumbers));
                resultArea.setText("입력한 번호 : " + sortedNumbers + "\n");
                manualInputField.setText(""); // 입력 필드 초기화

                // 모든 수동 번호가 입력되었을 때 결과 출력
                if (manualNumbersList.size() == ticketsToBuy) {
                    result = LottoResult.purchaseManualTickets(amount, manualNumbersList);
                    resultArea.append(result);
                    manualNumbersList.clear(); // 리스트 초기화
                } else {
                    resultArea.append("수동 번호가 " + (ticketsToBuy - manualNumbersList.size()) + "개 더 필요합니다");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "유효한 금액을 입력하세요", "입력 오류", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "오류가 발생했습니다 : " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LottoSwingEx());
    }
}

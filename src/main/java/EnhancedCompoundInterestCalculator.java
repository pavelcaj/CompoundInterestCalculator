package main.java;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class EnhancedCompoundInterestCalculator {

    // Глобальные переменные для компонентов
    private static JPanel inputPanel;
    private static JScrollPane scrollPane;
    private static JButton calculateButton;
    private static JButton resetButton;

    public static void main(String[] args) {
        // Устанавливаем тему FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Создаем главное окно
        JFrame frame = new JFrame("Калькулятор сложного процента");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null); // Центрирование окна

        // Создаем панель для ввода данных
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 15, 15));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Введите данные"));
        inputPanel.setOpaque(true);

        JLabel principalLabel = new JLabel("Начальная сумма (BYN):");
        JTextField principalField = new JTextField();
        principalField.setToolTipText("Введите начальную сумму (например, 1000)");

        JLabel rateLabel = new JLabel("Процентная ставка (%):");
        JTextField rateField = new JTextField();
        rateField.setToolTipText("Введите процентную ставку (например, 10)");

        JLabel yearsLabel = new JLabel("Количество лет:");
        JTextField yearsField = new JTextField();
        yearsField.setToolTipText("Введите количество лет (например, 5)");

        JLabel frequencyLabel = new JLabel("Частота начисления (в год):");

        JLabel contributionLabel = new JLabel("Ежегодное довложение (BYN):");
        JTextField contributionField = new JTextField();
        contributionField.setToolTipText("Введите ежегодное довложение (например, 500)");

        resetButton = new JButton("Сбросить");
        resetButton.setFocusPainted(false);

        calculateButton = new JButton("Рассчитать");
        calculateButton.setFocusPainted(false);

        inputPanel.add(principalLabel);
        inputPanel.add(principalField);
        inputPanel.add(rateLabel);
        inputPanel.add(rateField);
        inputPanel.add(yearsLabel);
        inputPanel.add(yearsField);
        inputPanel.add(contributionLabel);
        inputPanel.add(contributionField);
        inputPanel.add(resetButton);
        inputPanel.add(calculateButton);

        // Панель для результатов
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createTitledBorder("Результаты"));
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPane = new JScrollPane(resultArea);
        
        // Добавляем панели на окно
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Обработчик нажатия кнопки рассчитать
        calculateButton.addActionListener(e -> {
            try {
                double principal = Double.parseDouble(principalField.getText());
                double rate = Double.parseDouble(rateField.getText()) / 100;
                int years = Integer.parseInt(yearsField.getText());
                double contribution = Double.parseDouble(contributionField.getText());

                resultArea.setText("");  // очищаем текст перед расчетами
                double currentAmount = 0;

                // Проводим расчеты по каждому году
                for (int year = 1; year <= years; year++) {
                    double principalGrowth = principal * Math.pow((1 + rate), year);
                    double contributionsGrowth = contribution * ((Math.pow((1 + rate), year) - 1) / (rate));
                    currentAmount = principalGrowth + contributionsGrowth;

                    // Добавляем результат для каждого года в текстовое поле
                    resultArea.append(String.format("Год %d: %.2f BYN\n", year, currentAmount));
                }

                // Итоговая сумма
                resultArea.append(String.format("\nИтоговая сумма за %d лет: %.2f BYN", years, currentAmount));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Введите корректные данные!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Обработчик нажатия кнопки сбросить
        resetButton.addActionListener(e -> {
            // Сброс значений всех полей
            principalField.setText("");
            rateField.setText("");
            yearsField.setText("");
            contributionField.setText("");
            resultArea.setText("");
        });

        // Масштабируемость шрифтов
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int frameWidth = frame.getWidth();

                // Пересчитываем размер шрифта на основе ширины окна
                int newFontSize = Math.max(12, frameWidth / 50); // Минимальный размер — 12
                Font newFont = new Font("SansSerif", Font.BOLD, newFontSize);

                // Обновляем шрифт для всех компонентов
                updateFont(inputPanel, newFont);
                updateFont(scrollPane, newFont);
                updateFont(calculateButton, newFont);
                updateFont(resetButton, newFont);

            }
        });


        // Делаем окно видимым
        frame.setVisible(true);
    }

    private static void updateFont(Component component, Font font) {
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                updateFont(child, font);
            }
        }
    }
}
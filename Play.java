package org.example;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Play extends Frame implements ActionListener {

    private static final int SIZE = 4;
    private static final int BUTTON_SIZE = 50;
    private static final int GAME_BUTTON_WIDTH = 80;
    private static final int GAME_BUTTON_HEIGHT = 50;

    private final Button[][] buttons = new Button[SIZE][SIZE];
    private final Button newGameButton = new Button("Новая игра");
    private final TextField timeInput = new TextField(3);
    private final java.awt.List selectedNumbers = new java.awt.List(16);
    private final Label timeLabel = new Label("Введите время в секундах :");

    private final Random random = new Random();
    private Timer timer;
    private GameTimerTask timerTask;

    private int[] numbers = new int[SIZE * SIZE];
    private int gameDuration;
    private int elapsedTime;
    private boolean isRunning;

    public Play() {
        setTitle("Игра");
        setLayout(null);
        setBounds(0, 0, 400, 400);
        addWindowListener(new MyWindowAdapter());

        setupUI();
    }

    private void setupUI() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Button button = new Button();
                button.setBounds((i + 1) * BUTTON_SIZE, (j + 2) * BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
                button.addActionListener(this);
                buttons[i][j] = button;
                add(button);
            }
        }

        newGameButton.setBounds(50, 40, GAME_BUTTON_WIDTH, GAME_BUTTON_HEIGHT);
        newGameButton.addActionListener(this);
        add(newGameButton);

        timeInput.setBounds(150, 65, 130, 20);
        add(timeInput);

        timeLabel.setBounds(150, 40, 150, 20);
        add(timeLabel);

        selectedNumbers.setBounds(280, 100, 50, 200);
        add(selectedNumbers);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (isRunning && handleNumberClick(source)) {
            repaint();
            return;
        }

        if (source == newGameButton) {
            startGame();
        }
    }

    private boolean handleNumberClick(Object source) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (source == buttons[i][j]) {
                    String label = buttons[i][j].getLabel();
                    int value = Integer.parseInt(label);

                    if (selectedNumbers.getItemCount() == 0 ||
                            Integer.parseInt(selectedNumbers.getItem(selectedNumbers.getItemCount() - 1)) <= value) {

                        if (value != numbers[selectedNumbers.getItemCount()]) {
                            return true;
                        }

                        selectedNumbers.add(label);
                        checkWinCondition();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void startGame() {
        try {
            gameDuration = Integer.parseInt(timeInput.getText());
            elapsedTime = 0;
            isRunning = true;
            selectedNumbers.removeAll();
            newGameButton.setEnabled(false);

            generateRandomNumbers();

            timerTask = new GameTimerTask(this);
            timer = new Timer();
            timer.schedule(timerTask, 1000, 1000);

        } catch (NumberFormatException e) {
            setTitle("ВВЕДИТЕ ПРАВИЛЬНОЕ ЧИСЛО!");
        }
    }

    private void generateRandomNumbers() {
        Set<Integer> uniqueNumbers = new HashSet<>();
        int index = 0;

        while (uniqueNumbers.size() < SIZE * SIZE) {
            int num = random.nextInt(100);
            if (uniqueNumbers.add(num)) {
                numbers[index] = num;
                int i = index / SIZE;
                int j = index % SIZE;
                buttons[i][j].setLabel(String.valueOf(num));
                index++;
            }
        }

        Arrays.sort(numbers);
    }

    public void updateTime() {
        elapsedTime++;
        setTitle("Прошло " + elapsedTime + " секунд");

        if (elapsedTime >= gameDuration) {
            endGame(selectedNumbers.getItemCount() == SIZE * SIZE);
        }
    }

    private void checkWinCondition() {
        if (selectedNumbers.getItemCount() == SIZE * SIZE) {
            endGame(true);
        }
    }

    private void endGame(boolean won) {
        isRunning = false;
        timer.cancel();
        setTitle(won ? "ВЫ ВЫИГРАЛИ!" : "ВЫ ПРОИГРАЛИ!");
        newGameButton.setEnabled(true);
    }

    public static void main(String[] args) {
        Play play = new Play();
        play.setVisible(true);
    }
}

class GameTimerTask extends TimerTask {
    private final Play play;

    public GameTimerTask(Play play) {
        this.play = play;
    }

    @Override
    public void run() {
        play.updateTime();
    }
}

class MyWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}
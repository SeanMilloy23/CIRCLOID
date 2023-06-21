package circlegame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.FileWriter;
import java.io.IOException;

public class CircularMotionSprite extends JPanel {
    private int centerX;
    private int centerY;
    private int spriteRadius;
    private double angle;
    private double angularSpeed;
    private boolean clockwise;
    private double speedMultiplier;
    private Point2D.Double blueCircleCenter;
    private int blueCircleRadius;
    private boolean blueCircleVisible;
    private double blueCircleShrink;
    private int score;
    private boolean gameRunning;
    private JButton playButton;
    private boolean yellowSquareVisible;
    private boolean titleVisible;
    private Clip musicClip;
    private int highscore;
    private JButton quitButton;

    public CircularMotionSprite(int width, int height, int radius, double speed) {
        centerX = width / 2;
        centerY = height / 2;
        spriteRadius = radius;
        angularSpeed = speed;
        clockwise = true;
        speedMultiplier = 1.0;
        blueCircleRadius = 50;
        blueCircleVisible = false;
        blueCircleShrink = 0.00;
        score = 0;
        gameRunning = false;
        yellowSquareVisible = true;
        titleVisible = true;

        initPlayButton();
        initQuitButton();
        loadMusic("candyland.wav");

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!gameRunning) {
                        resetGame();
                        return;
                    }

                    clockwise = !clockwise;
                    speedMultiplier += 0.1;

                    if (blueCircleVisible) {
                        if (isOverlap()) {
                            blueCircleVisible = false;
                            spawnBlueCircle();
                            score++;
                        } else {
                            gameRunning = false;
                            playButton.setVisible(true);
                            quitButton.setVisible(true);
                            playButton.setVisible(true);   
                            playButton.setEnabled(true);
                            if (highscore < score) {
                                highscore = score;
                                playButton.setVisible(true);                             
                                try {
                                    FileWriter writer = new FileWriter("highscore.txt");
                                    writer.write(String.valueOf(highscore));
                                    writer.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                
                            }
                        }
                    }
                }
            }
        });

        spawnBlueCircle();
    }

    private void resetGame() {
        score = 0;
        speedMultiplier = 1.0;
        gameRunning = true;
        spawnBlueCircle();
        yellowSquareVisible = true;
        playButton.setVisible(true);
        playButton.setEnabled(false); // Disable the play button
        titleVisible = false; // Hide the "Circle Game" text
        
       
    }

    private void spawnBlueCircle() {
        double randomAngle = Math.random() * Math.PI * 2;
        int randomX = (int) (centerX + Math.cos(randomAngle) * spriteRadius);
        int randomY = (int) (centerY + Math.sin(randomAngle) * spriteRadius);
        blueCircleCenter = new Point2D.Double(randomX, randomY);
        blueCircleVisible = true;
    }

    private boolean isOverlap() {
        double distance = blueCircleCenter.distance(centerX + Math.cos(angle) * spriteRadius,
                centerY + Math.sin(angle) * spriteRadius);
        boolean overlap = distance <= blueCircleRadius;
        return overlap;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(28f));
        g2d.setColor(Color.DARK_GRAY);
        int pathRadius = spriteRadius;
        int pathX = centerX - pathRadius;
        int pathY = centerY - pathRadius;
        int pathWidth = pathRadius * 2;
        int pathHeight = pathRadius * 2;
        g2d.draw(new Ellipse2D.Double(pathX, pathY, pathWidth, pathHeight));

        if (blueCircleVisible) {
            g2d.setColor(Color.YELLOW);
            int x = (int) blueCircleCenter.x - blueCircleRadius;
            int y = (int) blueCircleCenter.y - blueCircleRadius;
            int diameter = (blueCircleRadius * 2);
            g2d.fillOval(x, y, diameter, diameter);
        }

        g2d.setColor(Color.GREEN);
        int x = (int) (centerX + Math.cos(angle) * spriteRadius);
        int y = (int) (centerY + Math.sin(angle) * spriteRadius);
        int dotRadius = 10;
        g2d.fillOval(x - dotRadius, y - dotRadius, dotRadius * 2, dotRadius * 2);

        g2d.setColor(Color.BLACK);
        int fillRadius = 188;
        g2d.fillOval(centerX - fillRadius, centerY - fillRadius, fillRadius * 2, fillRadius * 2);

        g2d.setStroke(new BasicStroke(600f));
        g2d.setColor(Color.BLACK);
        double FpathRadius = spriteRadius * 0.43;
        double FpathX = centerX - FpathRadius;
        double FpathY = centerY - FpathRadius;
        double FpathWidth = FpathRadius * 2;
        double FpathHeight = FpathRadius * 2;
        g2d.draw(new Ellipse2D.Double(FpathX, FpathY, FpathWidth, FpathHeight));

        g2d.setStroke(new BasicStroke(900f));
        g2d.setColor(Color.BLACK);
        double F2pathRadius = spriteRadius * 0.43;
        double F2pathX = centerX - F2pathRadius;
        double F2pathY = centerY - F2pathRadius;
        double F2pathWidth = F2pathRadius * 2;
        double F2pathHeight = F2pathRadius * 2;
        g2d.draw(new Ellipse2D.Double(F2pathX, F2pathY, F2pathWidth, F2pathHeight));

        if (yellowSquareVisible) {
            // Create the yellow square in the middle of the screen
            g2d.setColor(Color.BLACK);
            int squareSize = 800;
            int squareX = centerX - squareSize / 2;
            int squareY = centerY - squareSize / 2;
            g2d.fillRect(squareX, squareY, squareSize, squareSize);
        }
        // Draw the score or "Game Over"
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Calibri Light", Font.BOLD, 80));
        FontMetrics fm = g2d.getFontMetrics();
        if (gameRunning) {
            String scoreText = String.valueOf(score);
            int scoreWidth = fm.stringWidth(scoreText);
            int scoreHeight = fm.getHeight();
            int scoreX = centerX - scoreWidth / 2;
            int scoreY = centerY + scoreHeight / 4;
            g2d.drawString(scoreText, scoreX, scoreY);
        } else {
            String gameOverText = "CIRCLOID";
            int gameOverWidth = fm.stringWidth(gameOverText);
            int gameOverHeight = fm.getHeight();
            int gameOverX = centerX - gameOverWidth / 2;
            int gameOverY = centerY + gameOverHeight / 4;
            g2d.drawString(gameOverText, gameOverX, gameOverY);
            playButton.setVisible(true);
        }

        if (titleVisible) {
            // Create the yellow square in the middle of the screen
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Calibri Light", Font.BOLD, 80));
            FontMetrics fm1 = g2d.getFontMetrics();
            String titleText = String.valueOf("CIRCLOID");
            int scoreWidth = fm1.stringWidth(titleText);
            int scoreHeight = fm1.getHeight();
            int scoreX = centerX - scoreWidth / 2;
            int scoreY = centerY + scoreHeight / 4;
            g2d.drawString(titleText, scoreX, scoreY);
        }
        if (!titleVisible) {
        	
           
        }
    }

    public void update() {
        if (clockwise) {
            angle += angularSpeed * speedMultiplier;
        } else {
            angle -= angularSpeed * speedMultiplier;
        }
        repaint();
    }

    private void initPlayButton() {
        playButton = new JButton("Play");

        int buttonWidth = 100;
        int buttonHeight = 50;
        int buttonX = centerX - buttonWidth / 2;
        int buttonY = centerY + spriteRadius * 2 + 50; // Move the button down by 50 pixels
        playButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        add(playButton);

      
		playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
                yellowSquareVisible = false;
                requestFocus();
                playButton.setVisible(false);
                quitButton.setVisible(false);
                
                // Hide the quit button
            }
        });
    }
    
    private void initQuitButton() {
        quitButton = new JButton("Quit");

        int buttonWidth = 100;
        int buttonHeight = 50;
        int buttonX = centerX - buttonWidth / 2;
        int buttonY = centerY + spriteRadius * 2 + 50; // Move the button down by 50 pixels
        quitButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        add(quitButton);

      
		quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
                
            }
        });
    }


    public static void main(String[] args) {
        int width = 800;
        int height = 600;
        int spriteRadius = 200;
        double angularSpeed = 0.02;

        CircularMotionSprite sprite = new CircularMotionSprite(width, height, spriteRadius, angularSpeed);

        JFrame frame = new JFrame("Circular Motion Sprite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(sprite);
        frame.pack();
        frame.setSize(new Dimension(width, height));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (true) {
            if (sprite.gameRunning) {
                sprite.update();
                
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // PlayMusicThread class to play music in the background
    static class PlayMusicThread extends Thread {
        private String musicFilePath;

        public PlayMusicThread(String musicFilePath) {
            this.musicFilePath = musicFilePath;
        }

        public void run() {
            try {
                File musicFile = new File(musicFilePath);
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
                Thread.sleep(Long.MAX_VALUE); // Sleep indefinitely to keep the music playing
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to load and play background music
    private void loadMusic(String musicFilePath) {
        PlayMusicThread playMusicThread = new PlayMusicThread(musicFilePath);
        playMusicThread.start();
    }
}

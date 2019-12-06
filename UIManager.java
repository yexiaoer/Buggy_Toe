import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UIManager {
    private static UIManager m_instance;
    private JFrame frameMenu;
    private ActionListener m_actionListener;

    private ImageIcon o = new ImageIcon(Constants.ICON_MAIN_FRAME);
    private ImageIcon music = new ImageIcon(Constants.ICON_MUSIC);
    private ImageIcon musicM = new ImageIcon(Constants.ICON_MUSIC_MUTE);
    private ImageIcon pvp = new ImageIcon(Constants.ICON_PVP);
    private ImageIcon pvc = new ImageIcon(Constants.ICON_PVC);
    private ImageIcon pvcT = new ImageIcon(Constants.ICON_PVCT);
    private ImageIcon pvpT = new ImageIcon(Constants.ICON_PVPT);
    private ImageIcon bG = new ImageIcon(Constants.ICON_BACKGROUD);
    private ImageIcon normalL = new ImageIcon(Constants.ICON_NO_BUG);
    private ImageIcon hardL = new ImageIcon(Constants.ICON_WITH_BUG);

    private JButton buttonSoundToggle;
    private JButton buttonPlayerOScore;
    private JButton buttonPlayerXScore;
    private JButton numMove;
    private JLabel turnShow;
    private JLabel bugThread;

    public static UIManager getInstance() {
        if (m_instance == null)
            m_instance = new UIManager();
        return m_instance;
    }

    private UIManager() {
        frameMenu = new JFrame();
        frameMenu.setSize(1200, 1050);
        frameMenu.setLayout(null);
        frameMenu.setTitle("Buggy Game");
        frameMenu.setVisible(true);
        frameMenu.setResizable(false);
        frameMenu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setUpMainFrame() {
        Image newMainImg = o.getImage().getScaledInstance(1200, 1050, Image.SCALE_SMOOTH);
        JLabel newO = new JLabel(new ImageIcon(newMainImg));
        try {
            frameMenu.setContentPane(newO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton buttonPlayerVSplayer = new JButton(pvp);
        buttonPlayerVSplayer.setBounds(475, 300, 250, 100);
        frameMenu.add(buttonPlayerVSplayer);
        buttonPlayerVSplayer.setActionCommand(Constants.ACTION_PLAYER_VS_PLAYER);
        buttonPlayerVSplayer.addActionListener(m_actionListener);

        JButton buttonPlayerVScomputer = new JButton(pvc);
        buttonPlayerVScomputer.setBounds(475, 400, 250, 100);
        frameMenu.add(buttonPlayerVScomputer);
        buttonPlayerVScomputer.setActionCommand(Constants.ACTION_PLAYER_VS_COMPUTER);
        buttonPlayerVScomputer.addActionListener(m_actionListener);

        if(SoundManager.getInstance().isMuted())
            buttonSoundToggle =  new JButton(musicM);
        else
            buttonSoundToggle =  new JButton(music);
        buttonSoundToggle.setBounds(0, 0, 50, 50);
        frameMenu.add(buttonSoundToggle);
        buttonSoundToggle.setActionCommand(Constants.ACTION_SOUND_TOGGLE);
        buttonSoundToggle.addActionListener(m_actionListener);
        frameMenu.setVisible(true);
    }

    public void setUpGameFrame() {
        try {
            frameMenu.setContentPane(new JLabel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        frameMenu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton buttonQuit = new JButton("EXIT");
        buttonQuit.setBounds(665, 60, 250, 100);
        frameMenu.add(buttonQuit);
        buttonQuit.setActionCommand(Constants.ACTION_QUIT_GAME);
        buttonQuit.addActionListener(m_actionListener);

        JButton buttonReset = new JButton("RESET");
        buttonReset.setBounds(665, 160, 250, 100);
        frameMenu.add(buttonReset);
        buttonReset.setActionCommand(Constants.ACTION_RESET_GAME);
        buttonReset.addActionListener(m_actionListener);

        JButton buttonReturnToMainPage = new JButton("Return To Main Page");
        buttonReturnToMainPage.setBounds(665, 260, 250, 100);
        frameMenu.add(buttonReturnToMainPage);
        buttonReturnToMainPage.setActionCommand(Constants.ACTION_RETURN_MAIN_PAGE);
        buttonReturnToMainPage.addActionListener(m_actionListener);

        if(SoundManager.getInstance().isMuted())
            buttonSoundToggle =  new JButton(musicM);
        else
            buttonSoundToggle =  new JButton(music);
        buttonSoundToggle.setBounds(0, 0, 50, 50);
        frameMenu.add(buttonSoundToggle);
        buttonSoundToggle.setActionCommand(Constants.ACTION_SOUND_TOGGLE);
        buttonSoundToggle.addActionListener(m_actionListener);

        JButton labelGameMode = new JButton();
        labelGameMode.setBounds(0, 660, 1200, 150);
        if (GameController.getInstance().getGameMode()
                == GameController.GameMode.PlayerVsComputer) {
            labelGameMode.setIcon(pvcT);

            JButton tempX = new JButton("X");
            tempX.setBounds(665, 360, 60, 60);
            tempX.setBackground(Color.BLUE);
            tempX.setOpaque(true);
            frameMenu.add(tempX);

            JLabel tempXX = new JLabel();
            tempXX.setBounds(725, 250, 200, 300);
            tempXX.setFont(new Font("Serif", Font.BOLD, 20));
            tempXX.setText("COMPUTER");
            frameMenu.add(tempXX);
            

            JButton tempO = new JButton("O");
            tempO.setBounds(665, 420, 60, 60);
            tempO.setBackground(Color.RED);
            tempO.setOpaque(true);
            frameMenu.add(tempO);

            JLabel tempOO = new JLabel();
            tempOO.setBounds(725, 310, 100, 300);
            tempOO.setFont(new Font("Serif", Font.BOLD, 20));
            tempOO.setText("YOU");
            frameMenu.add(tempOO);     

        } else {
            labelGameMode.setIcon(pvpT);

            JButton bug1 = new JButton();
            bug1.setBounds(665, 480, 60, 60);
            bug1.setBackground(Color.YELLOW);
            bug1.setEnabled(false);
            bug1.setOpaque(true);
            frameMenu.add(bug1);

            JButton bug2 = new JButton();
            bug2.setBounds(730, 480, 60, 60);
            bug2.setBackground(Color.BLACK);
            bug2.setEnabled(false);
            bug2.setOpaque(true);
            frameMenu.add(bug2);

            JLabel bugText = new JLabel();
            bugText.setBounds(800, 370, 100, 300);
            bugText.setFont(new Font("Serif", Font.BOLD, 20));
            bugText.setText("BUG");
            frameMenu.add(bugText);

            JButton tempX = new JButton("X");
            tempX.setBounds(665, 360, 60, 60);
            tempX.setBackground(Color.BLUE);
            tempX.setOpaque(true);
            frameMenu.add(tempX);

            JLabel tempXX = new JLabel();
            tempXX.setBounds(725, 250, 200, 300);
            tempXX.setFont(new Font("Serif", Font.BOLD, 20));
            tempXX.setText("Player 2");
            frameMenu.add(tempXX);
            

            JButton tempO = new JButton("O");
            tempO.setBounds(665, 420, 60, 60);
            tempO.setBackground(Color.RED);
            tempO.setOpaque(true);
            frameMenu.add(tempO);

            JLabel tempOO = new JLabel();
            tempOO.setBounds(725, 310, 100, 300);
            tempOO.setFont(new Font("Serif", Font.BOLD, 20));
            tempOO.setText("Player 1");
            frameMenu.add(tempOO);

            turnShow = new JLabel();
            turnShow.setBounds(665, 560, 250, 100);
            turnShow.setFont(new Font("Serif", Font.BOLD, 20));
            turnShow.setText("Player 1 turn");
            frameMenu.add(turnShow);
        }
        frameMenu.add(labelGameMode);
        
        numMove = new JButton("Number of move: " + 0);
        numMove.setBounds(915, 260, 250, 100);
        frameMenu.add(numMove);

        buttonPlayerOScore = new JButton("Number of times O wins: " + 0);
        buttonPlayerOScore.setBounds(915, 60, 250, 100);
        frameMenu.add(buttonPlayerOScore);

        buttonPlayerXScore = new JButton("Number of times X wins: " + 0);
        buttonPlayerXScore.setBounds(915, 160, 250, 100);
        frameMenu.add(buttonPlayerXScore);

        for (JButton button : BoardManager.getInstance().getButtons()) {
            frameMenu.add(button);
        }
        frameMenu.setVisible(true);
    }
    //set action listioner
    public void setActionListener(ActionListener actionListener) {
        m_actionListener = actionListener;
    }
    //showing number of times O wins
    public void updateOScore(int newScore){
        buttonPlayerOScore.setText("Number of times O wins: " + newScore);
    }
    //showing number of times X wins
    public void updateXScore(int newScore){
        buttonPlayerXScore.setText("Number of times X wins: " + newScore);
    }
    //showing number of move
    public void updateNumOfTurn(int numTurn){
        numMove.setText("Number of move: " + numTurn);

        if(GameController.getInstance().getGameMode()
                != GameController.GameMode.PlayerVsComputer)
        {
            if (numTurn % 2 == 0)
                turnShow.setText("Player 1 turn");
            else
                turnShow.setText("Player 2 turn");
        }
    }
    //tie dialog
    public void showTieDialog(){
        JOptionPane.showMessageDialog(frameMenu, "Almost tie! Clean up the Game!");
    }
    //Dispose the frame
    public void disposeMenuFrame(){
        frameMenu.getContentPane().removeAll();
        frameMenu.revalidate();
    }
    //Set mute music icon
    public void setMute() {
        buttonSoundToggle.setIcon(musicM);
    }
    //Set unmute music icon
    public void setUnMute() {
        buttonSoundToggle.setIcon(music);
    }
    //Set up the choosing level frame
    public void setUpLevelFrame() {
        Image newLevelImg = bG.getImage().getScaledInstance(1200, 1050, Image.SCALE_SMOOTH);
        JLabel newbG = new JLabel(new ImageIcon(newLevelImg));
        try {
            frameMenu.setContentPane(newbG);
        } catch (Exception e) {
            e.printStackTrace();
        }
		Image newImg1 = normalL.getImage().getScaledInstance(230, 80, Image.SCALE_SMOOTH);
        JButton buttonEasyLevel = new JButton(new ImageIcon(newImg1));
        buttonEasyLevel.setBounds(475, 300, 250, 100);
        frameMenu.add(buttonEasyLevel);
        buttonEasyLevel.setActionCommand(Constants.ACTION_NORMAL_MODE);
        buttonEasyLevel.addActionListener(m_actionListener);

        Image newImg2 = hardL.getImage().getScaledInstance(230, 80, Image.SCALE_SMOOTH);
        JButton buttonHardLevel = new JButton(new ImageIcon(newImg2));
        buttonHardLevel.setBounds(475, 400, 250, 100);
        frameMenu.add(buttonHardLevel);
        buttonHardLevel.setActionCommand(Constants.ACTION_HARD_MODE);
        buttonHardLevel.addActionListener(m_actionListener);

        if(SoundManager.getInstance().isMuted())
            buttonSoundToggle =  new JButton(musicM);
        else
            buttonSoundToggle =  new JButton(music);
        buttonSoundToggle.setBounds(0, 0, 50, 50);
        frameMenu.add(buttonSoundToggle);
        buttonSoundToggle.setActionCommand(Constants.ACTION_SOUND_TOGGLE);
        buttonSoundToggle.addActionListener(m_actionListener);
        frameMenu.setVisible(true);
    }
}

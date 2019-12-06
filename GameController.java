import javax.swing.*;
import java.awt.event.*;

public class GameController implements ActionListener{
    private static GameController m_instance;

    private SoundManager soundManager;
    private UIManager uiManager;
    private BoardManager boardManager;

    public enum GameMode{ PlayerVsPlayer, PlayerVsComputer }
    public enum LevelMode{ NormalMode, HardMode }

    private int numOwins;
    private int numXwins;
    private int numToWin = 5;
    private boolean m_isRoundFinished;

    private GameMode m_gameMode;
    private LevelMode levelMode;

    public static GameController getInstance() {
        if (m_instance == null) {
            m_instance = new GameController();
        }
        return m_instance;
    }
    //Constructor
    private GameController() {
        soundManager = SoundManager.getInstance();
        uiManager = UIManager.getInstance();
        uiManager.setActionListener(this);
        boardManager = BoardManager.getInstance();
        boardManager.setActionListener(this);
    }

    //set initial menu game
    public void initGame() {
        setUpMainScreen();
        soundManager.playBackGroundMusic();
    }

    //set up main menu game function
    public void setUpMainScreen() {
        uiManager.setUpMainFrame();
        numOwins = 0;
        numXwins = 0;
    }

    //set up game frame
    public void setUpGameScreen() {
        m_isRoundFinished = false;
        boardManager.initBoard();
        uiManager.setUpGameFrame();
        uiManager.updateOScore(numOwins);
        uiManager.updateXScore(numXwins);
    }
    
    //update unber of turn
    public void upadateNumTurn() {
        uiManager.updateNumOfTurn(boardManager.getTurn());
    }

    //function return current game mode
    public GameMode getGameMode() {
        return m_gameMode;
    }

    //function return winning times
    public int getNumToWin() {
        return numToWin;
    }

    //function checks if a game finish
    public boolean isRoundFinished() {
        return m_isRoundFinished;
    }

    //function sets the game finish
    public void setRoundFinished(boolean isFinished) {
        m_isRoundFinished = isFinished;
    }

    //function set the O wins
    public void winnerO() {
        numOwins++;
        soundManager.playWave("d.wav");
        uiManager.updateOScore(numOwins);
    }

    //function set the X wins
    public void winnerX() {
        numXwins++;
        soundManager.playWave("c.wav");
        uiManager.updateXScore(numXwins);
    }

    //function get the number of times X wins
    public int getXwins() {
        return numXwins;
    }

    //function get the number of times O wins
    public int getOwins() {
        return numOwins;
    }

    //set  up choosing level frame
    public void setLevelScreen() {
        boardManager.initBoard();
        uiManager.setUpLevelFrame();
    }

    //get current level mode
    public LevelMode getLevelMode() {
        return levelMode;
    }

    //set all the action performance
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {

            case Constants.ACTION_PLAYER_VS_PLAYER:
                m_gameMode = GameMode.PlayerVsPlayer;
                uiManager.disposeMenuFrame();
                setLevelScreen();
                return;
            case Constants.ACTION_PLAYER_VS_COMPUTER:
                m_gameMode = GameMode.PlayerVsComputer;
                uiManager.disposeMenuFrame();
                setUpGameScreen();
                return;
            case Constants.ACTION_SOUND_TOGGLE:
                soundManager.toggleBackgroundMusic();
                return;
            case Constants.ACTION_QUIT_GAME:
                uiManager.disposeMenuFrame();
                System.exit(0);
                return;
            case Constants.ACTION_RESET_GAME:
                uiManager.disposeMenuFrame();
                setUpGameScreen();
                return;
            case Constants.ACTION_RETURN_MAIN_PAGE:
                uiManager.disposeMenuFrame();
                setUpMainScreen();
                return;
            case Constants.ACTION_NORMAL_MODE:
                levelMode = LevelMode.NormalMode;
                uiManager.disposeMenuFrame();
                setUpGameScreen();
                return;
            case Constants.ACTION_HARD_MODE:
                levelMode = LevelMode.HardMode;
                uiManager.disposeMenuFrame();
                setUpGameScreen();
                return;
        }
        boardManager.handleButtonClick((JButton) actionEvent.getSource());
    }
}
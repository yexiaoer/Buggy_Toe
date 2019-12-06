import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;
import java.lang.Object.*;


public class BoardManager {

    private static BoardManager m_instance;
    private ActionListener m_actionListener;
    private JButton[] buttons;
    private int gridSize = 10;
    private int[] board;
    private boolean isXTurn;

    ArrayList<Integer> path1, path2, path3, path4;
    ArrayList<Integer> countX2, countX3, countX4, countX5;
    ArrayList<Integer> countO2, countO3, countO4, countO5;

    private int turnCount;
    private int numToWin;
    String tempBoard[];
    boolean bugOnBoard; 
    int eatBugPos;
    int[] blockBugPos;
    int[] blockNum;

    //constructor
    private BoardManager() {}

    //get current board
    public static BoardManager getInstance() {
        if (m_instance == null)
            m_instance = new BoardManager();
        return m_instance;
    }

    //Build the initial board
    public void initBoard() {
        if (GameController.getInstance().getLevelMode()
                == GameController.LevelMode.HardMode) {
            bugOnBoard = false; 
            eatBugPos = -1;
            blockBugPos = new int[5];
            blockNum = new int[5];
            for (int i = 0; i < 5; i++) { 
                blockBugPos[i] = -1;
                blockNum[i] = -1;
            }
        }
        turnCount = 0;
        numToWin = GameController.getInstance().getNumToWin();
        isXTurn = true;
        board = new int[gridSize * 10];
        tempBoard = new String[gridSize*10];
        buttons = new JButton[gridSize * 10];
        for (int i = 0; i < buttons.length; i++) {
            JButton button = new JButton(" ");
            button.setBounds(((i%10)+1) * 60, 60 * ((i/10) +1), 60, 60);
            buttons[i] = button;
            buttons[i].addActionListener(m_actionListener);
            board[i] = 0;
            tempBoard[i] = " ";
        }
    }

    //Action listener handler
    public void setActionListener(ActionListener actionListener) {
        m_actionListener = actionListener;
    }

    //get the board
    public JButton[] getButtons() {
        return buttons;
    }

    //On Click Button handles function
    public void handleButtonClick(JButton button) {
        if (GameController.getInstance().isRoundFinished()) { return; }
        for (int i = 0; i < buttons.length; i++) {
            if (button == buttons[i] && buttons[i].getText().equals(" ")) {
                turnCount++;
                GameController.getInstance().upadateNumTurn();
                isXTurn = !isXTurn;
                buttons[i].setText((!isXTurn)? "O" : "X");
                tempBoard[i] = ((!isXTurn)? "O" : "X");
                if(turnCount % 2 ==0) {
                    buttons[i].setBackground(Color.BLUE);
                    buttons[i].setOpaque(true);
                } else {
                    buttons[i].setBackground(Color.RED);
                    buttons[i].setOpaque(true);
                }
                SoundManager.getInstance().playWave("b.wav");
                if (checkIfWin(i, (!isXTurn)? "O" : "X", numToWin)) showPath();
                if (GameController.getInstance().getLevelMode()
                    == GameController.LevelMode.HardMode
                    && GameController.getInstance().getGameMode()
                    != GameController.GameMode.PlayerVsComputer
                    && !GameController.getInstance().isRoundFinished()) {
                    for (int j = 0; j < 5; j++) {
                        if (blockNum[j] > 0) { blockNum[j]++; }
                    }
                    if(!GameController.getInstance().isRoundFinished() && bugOnBoard) {
                        bugOnBoard = false;
                        buttons[eatBugPos].setEnabled(true);
                        buttons[eatBugPos].setBackground(null);
                        buttons[eatBugPos].setOpaque(true);
                    }

                    int maxBug = -1;
                    int maxBugPos = -1;
                    for (int j = 0; j < 5; j++) {
                        if (blockNum[j] > maxBug) {
                            maxBug = blockNum[j];
                            maxBugPos = j;
                        }
                    }
                    if(!GameController.getInstance().isRoundFinished()
                        && maxBugPos > -1 && blockNum[maxBugPos] >= 6) {
                        buttons[blockBugPos[maxBugPos]].setEnabled(true);
                        buttons[blockBugPos[maxBugPos]].setBackground(null);
                        buttons[blockBugPos[maxBugPos]].setOpaque(true);
                        blockNum[maxBugPos] = -1;
                        blockBugPos[maxBugPos] = -1;
                    }
                    if(!GameController.getInstance().isRoundFinished() && turnCount > 8) {
                        double rands = Math.random();
                        if (rands <= 0.15 || (rands >= 0.3 && rands <= 0.4) 
                                || (rands >= 0.7 && rands <= 0.8) || (rands >= 0.95)) {
                            double randomBug = Math.random();
                            if (randomBug < 0.15) { flipBug(); }
                            else if (randomBug >= 0.3 && randomBug <= 0.65) { eatBug(); }
                            else if  (randomBug >= 0.75) { blockBug(); }
                        }
                    }
                }
 
                if(GameController.getInstance().getGameMode()
                    == GameController.GameMode.PlayerVsComputer
                    && !GameController.getInstance().isRoundFinished()) {
                    turnCount++;
                    GameController.getInstance().upadateNumTurn();
                    isXTurn = !isXTurn;
                    int pos = computerTurn(i);
                    tempBoard[pos] = ((!isXTurn)? "O" : "X");
                    buttons[pos].setText(((!isXTurn)? "O" : "X"));
                    buttons[pos].setBackground(Color.BLUE);
                    buttons[pos].setOpaque(true);
                    SoundManager.getInstance().playWave("b.wav");
                    if (checkIfWin(pos, (!isXTurn)? "O" : "X", numToWin)) showPath();
                }
                if(checkIfTie()) UIManager.getInstance().showTieDialog();
            }
        }
    }

    //Function to check if the board has winner
    public boolean checkIfWin(int position, String OorX, int numToWin) {
        int p;
        int r = position / gridSize;
        int c = position % gridSize;
        path1 = new ArrayList<>();
        path2 = new ArrayList<>();
        path3 = new ArrayList<>();
        path4 = new ArrayList<>();
        path1.add(position);
        path2.add(position);
        path3.add(position);
        path4.add(position);
        for(p = 1; p < numToWin; p++){
            if(r - p >= 0 && c + p < gridSize
                && buttons[(r - p) * gridSize + (c + p)].getText().equals(OorX))
                path1.add((r - p) * gridSize + (c + p));
            else break;
        }
        for(p = 1; p < numToWin; p++){
            if(r + p < gridSize && c - p >= 0
                && buttons[(r + p) * gridSize + (c - p)].getText().equals(OorX))
                path1.add((r + p) * gridSize + (c - p));
            else break;
        }
        for(p = 1; p < numToWin; p++){
            if(r - p >= 0
                && buttons[(r - p) * gridSize + c].getText().equals(OorX))
                path2.add((r - p) * gridSize + c);
            else break;
        }
        for(p = 1; p < numToWin; p++){
            if(r + p < gridSize
                && buttons[(r + p) * gridSize + c].getText().equals(OorX))
                path2.add((r + p) * gridSize + c);
            else break;
        }
        for(p = 1; p < numToWin; p++){
            if(c - p >= 0
                && buttons[r * gridSize +(c - p)].getText().equals(OorX))
                path3.add(r * gridSize +(c - p));
            else break;
        }
        for(p = 1; p < numToWin; p++){
            if(c + p < gridSize
                && buttons[r * gridSize +(c + p)].getText().equals(OorX))
                path3.add(r * gridSize +(c + p));
            else break;
        }
        for(p = 1; p < numToWin; p++){
            if(r - p >= 0 && c - p >= 0
                && buttons[(r - p)* gridSize +(c - p)].getText().equals(OorX))
                path4.add((r - p)* gridSize +(c - p));
            else break;
        }
        for(p = 1; p < numToWin; p++){
            if(r + p < gridSize && c + p < gridSize 
                && buttons[(r + p) * gridSize +(c + p)].getText().equals(OorX))
                path4.add((r + p) * gridSize +(c + p));
            else break;
        }
        if(path1.size() >= numToWin||path2.size() >= numToWin
            ||path3.size() >= numToWin||path4.size() >= numToWin)
            return true;
        return false;
    }

    //Functio to check if the board is filled 80%
    public boolean checkIfTie(){
        int numToDel = 20;
        if(turnCount < gridSize * gridSize - 10) return false;
        int xCount = 0;
        int oCount = 0;
        ArrayList<Integer> listO = new ArrayList<>();
        ArrayList<Integer> listX = new ArrayList<>();
        for(int i = 0; i < buttons.length; i++){
            if(buttons[i].getText().equals("X")){ listX.add(i); xCount++;}
            else if(buttons[i].getText().equals("O")){ listO.add(i); oCount++;}
        }
        if (((double)(xCount + oCount)) / ((double)(gridSize * gridSize)) >= 0.8) {
            int j = 0;
            while (j < numToDel) {
                int tempX = (int) (Math.random() * listX.size());
                int tempO = (int) (Math.random() * listO.size());
                if (buttons[listX.get(tempX)].getText() != " "
                        && buttons[listO.get(tempO)].getText() != " ") {
                    buttons[listX.get(tempX)].setText(" ");
                    tempBoard[listX.get(tempX)] = " ";
                    buttons[listX.get(tempX)].setBackground(null);
                    buttons[listX.get(tempX)].setEnabled(true);
                    buttons[listX.get(tempX)].setOpaque(true);
                    buttons[listO.get(tempO)].setText(" ");
                    tempBoard[listO.get(tempO)] = " ";
                    buttons[listO.get(tempO)].setBackground(null);
                    buttons[listO.get(tempO)].setEnabled(true);
                    buttons[listO.get(tempO)].setOpaque(true);
                    j++;
                }
            }
            SoundManager.getInstance().playWave("d.wav");
            return true;
        }
        return false;
    }

    //Function show the path when a player win
    public void showPath() {
        SoundManager.getInstance().playWave("d.wav");
        GameController.getInstance().setRoundFinished(true);
        if(!isXTurn){ GameController.getInstance().winnerO();
        } else { GameController.getInstance().winnerX(); }

        if(path1.size() >= numToWin)
            for (Integer aPath1 : path1) buttons[aPath1].setEnabled(false);
        if(path2.size() >= numToWin)
            for (Integer aPath2 : path2) buttons[aPath2].setEnabled(false);
        if(path3.size() >= numToWin)
            for (Integer aPath3 : path3) buttons[aPath3].setEnabled(false);
        if(path4.size() >= numToWin)
            for (Integer aPath4 : path4) buttons[aPath4].setEnabled(false);
    }

    //Computer turn mode function
    public int computerTurn(int Opos) {
        computerTurnTT(Opos);
        ArrayList<Integer> temp;
        ArrayList<Integer> tempCount;
        if(countX5.size() != 0)
            return countX5.get((int) (Math.random() * countX5.size()));
        if(countO5.size() != 0)
            return countO5.get((int) (Math.random() * countX5.size()));
        if(countX4.size() != 0) {
            temp = new ArrayList<Integer>();
            for (int i = 0; i < countX4.size(); i++) {
                if (checkEnd(countX4.get(i), 4, "X" ))
                    temp.add(countX4.get(i));
            }
            if (temp.size() != 0) {
                tempCount = new ArrayList<Integer>();
                for (int i = 0; i < temp.size(); i++) {
                    checkIfWin(temp.get(i), "X",4);
                    tempCount.add(path1.size() + path2.size() + path3.size() + path4.size());
                }
                return temp.get(getMaxPos(tempCount, Opos));
            }
        }
        if(countO4.size() != 0) {
            temp = new ArrayList<Integer>();
            for (int i = 0; i < countO4.size(); i++)
                if (checkEnd(countO4.get(i), 4, "O" )) temp.add(countO4.get(i));
            if (temp.size() != 0) {
                tempCount = new ArrayList<Integer>();
                for (int i = 0; i < temp.size(); i++) {
                    checkIfWin(temp.get(i), "0",4);
                    tempCount.add(path1.size() + path2.size() + path3.size() + path4.size());
                }
                return temp.get(getMaxPos(tempCount, Opos));
            } else {
                tempCount = new ArrayList<Integer>();
                for (int i = 0; i < countO4.size(); i++) {
                    checkIfWin(countO4.get(i), "0",4);
                    tempCount.add(path1.size() + path2.size() + path3.size() + path4.size());
                }
                return countO4.get(getMaxPos(tempCount, Opos));
            }
        }
        if(countX4.size() != 0) {
            tempCount = new ArrayList<Integer>();
            for (int i = 0; i < countX4.size(); i++) {
                checkIfWin(countX4.get(i), "X",4);
                tempCount.add(path1.size() + path2.size() + path3.size() + path4.size());
            }
            return countX4.get(getMaxPos(tempCount, Opos));
        }

        if(countO3.size() != 0) {
            System.out.println("O3 Check end");
            temp = new ArrayList<Integer>();
            for (int i = 0; i < countO3.size(); i++) {
                if (checkEnd(countO3.get(i), 3, "O" ))
                    temp.add(countO3.get(i));
            }
            if (temp.size() != 0) {
                tempCount = new ArrayList<Integer>();
                for (int i = 0; i < temp.size(); i++) {
                    checkIfWin(temp.get(i), "0",3);
                    tempCount.add(path1.size() + path2.size() + path3.size() + path4.size());
                }
                return temp.get(getMaxPos(tempCount, Opos));
            }
        }
        
        if(countX3.size() != 0) {
            temp = new ArrayList<Integer>();
            for (int i = 0; i < countX3.size(); i++) {
                if (checkEnd(countX3.get(i), 3, "O" ))
                    temp.add(countX3.get(i));
            }
            if (temp.size() != 0) {
                tempCount = new ArrayList<Integer>();
                for (int i = 0; i < temp.size(); i++) {
                    checkIfWin(temp.get(i), "X",3);
                    tempCount.add(path1.size() + path2.size() + path3.size() + path4.size());
                }
                return temp.get(getMaxPos(tempCount, Opos));
            }   
        }
        int pos = findTrickyMove(Opos);
        if(pos != -1) return pos;
        if(countO2.size() != 0) {
            temp = new ArrayList<Integer>();
            for (int i = 0; i < countO2.size(); i++) {
                if (checkEnd(countO2.get(i), 2, "O" ))
                    temp.add(countO2.get(i));
            }
            if (temp.size() != 0) {
                tempCount = new ArrayList<Integer>();
                for (int i = 0; i < temp.size(); i++) {
                    checkIfWin(temp.get(i), "0",2);
                    tempCount.add(path1.size() + path2.size() + path3.size() + path4.size());
                }
                return temp.get(getMaxPos(tempCount, Opos));
            } else {
                tempCount = new ArrayList<Integer>();
                for (int i = 0; i < countO2.size(); i++) {
                    checkIfWin(countO2.get(i), "0",2);
                    tempCount.add(path1.size() + path2.size() + path3.size() + path4.size());
                }
                return countO2.get(getMaxPos(tempCount, Opos));
            }
        }
        return -1;
    }

    public int findTrickyMove(int Opos) {
        int r, c, r1,c1;
        int find;
        ArrayList<Integer> temp = new ArrayList<Integer>();
        ArrayList<Integer> empty = new ArrayList<Integer>();
        for(int i = 0; i < tempBoard.length; i++)
            if (tempBoard[i].equals(" "))empty.add(i);
        for(int i = 0; i < empty.size(); i++){
            r = empty.get(i) / gridSize;
            c = empty.get(i) % gridSize;
            find = 0;
            r1 = r - 1; c1 = c - 1;
            while (r1 >=0  && c1 >=0 && tempBoard[(r1 * 10) + c1].equals("O")){
                find += 1; r1--; c1--;
            }
            r1 = r + 1; c1 = c + 1;
            while (r1 < 10  && c1 < 10 && tempBoard[(r1 * 10) + c1].equals("O")) {
                find += 1; r1++; c1++;
            }
            r1 = r; c1 = c - 1;
            while (c1 >=0 && tempBoard[(r1 * 10) + c1].equals("O")) {
                find += 1; c1--;
            }
            r1 = r - 1; c1 = c;
            while (r1 >=0 && tempBoard[(r1 * 10) + c1].equals("O")) {
                find += 1; r1--;
            }
            r1 = r + 1; c1 = c;
            while (r1 < 10  && tempBoard[(r1 * 10) + c1].equals("O")) {
                find += 1; r1++;
            }
            r1 = r; c1 = c + 1;
            while (c1 < 10 && tempBoard[(r1 * 10) + c1].equals("O")) {
                find += 1; c1++;
            }
            r1 = r - 1; c1 = c + 1;
            while (r1 >=0  && c1 < 10 && tempBoard[(r1 * 10) + c1].equals("O")) {
                find += 1; r1--; c1++;
            }
            r1 = r + 1; c1 = c - 1;
            while (r1 < 10  && c1 >=0 && tempBoard[(r1 * 10) + c1].equals("O")) {
                find += 1; r1++; c1--;
            }
            temp.add(find);
        }
        if(temp.size() > 0) {
            int max = temp.get(0);
            int maxPos = 0;
            for(int i = 0; i < temp.size(); i++) {
                if(temp.get(i) > max) {
                    max = temp.get(i);
                    maxPos = i;
                }
            }
            if(max >=2) return empty.get(maxPos);
        }
        return -1;
    }

    public void computerTurnTT(int Opos) {
        int[] boardX = new int[gridSize*10];
        int[] boardO = new int[gridSize*10];;
        for(int i = 0; i < boardX.length; i++) {
            boardX[i] = 0;
            boardO[i] = 0;
        }
        for(int i = 0; i < tempBoard.length; i++) {
            if (tempBoard[i].equals("X")) {
                boardX[i] = -1;
                boardO[i] = -1;
            }
            if (tempBoard[i].equals("O")) {
                boardX[i] = -2;
                boardO[i] = -2;
            }
        }
        for(int i = 0; i < tempBoard.length; i++) {
            if (tempBoard[i].equals(" ")) {
                if(checkIfWin(i, "X",5)) boardX[i] = 5;   
                else if(checkIfWin(i, "X",4)) { boardX[i] = 4; }   
                else if(checkIfWin(i, "X",3)) { boardX[i] = 3; }  
                else if(checkIfWin(i, "X",2)) { boardX[i] = 2; }  
            }     
        }
        for(int i = 0; i < tempBoard.length; i++) {
            if (tempBoard[i].equals(" ")) {
                if(checkIfWin(i, "O",5)) { boardO[i] = 5; } 
                else if(checkIfWin(i, "O", 4)) { boardO[i] = 4; }   
                else if(checkIfWin(i, "O", 3)) { boardO[i] = 3; }  
                else if(checkIfWin(i, "O", 2)) { boardO[i] = 2; }  
            }        
        }
        countX2 = new ArrayList<Integer>();
        countX3 = new ArrayList<Integer>();
        countX4 = new ArrayList<Integer>();
        countX5 = new ArrayList<Integer>();
        for (int i = 0; i < boardX.length; i++) {
            if(boardX[i] == 5) { countX5.add(i); }
            else if(boardX[i] == 4) { countX4.add(i); }
            else if(boardX[i] == 3) { countX3.add(i); }
            else if(boardX[i] == 2) { countX2.add(i); }
        }
        countO2 = new ArrayList<Integer>();
        countO3 = new ArrayList<Integer>();
        countO4 = new ArrayList<Integer>();
        countO5 = new ArrayList<Integer>();
        for (int i = 0; i < boardO.length; i++) {
            if(boardO[i] == 5) { countO5.add(i); }
            else if(boardO[i] == 4) { countO4.add(i); }
            else if(boardO[i] == 3) { countO3.add(i); }
            else if(boardO[i] == 2) { countO2.add(i); }       
        }
    }

    public boolean checkEnd(int position, int winNum, String OorX) {
        int r = position / gridSize;
        int c = position % gridSize;
        if (checkIfWin(position, OorX, winNum)) {
            int length1 = path1.size();
            int length2 = path2.size();
            int length3 = path3.size();
            int length4 = path4.size();
            if (length1 >= winNum) {
                boolean empty1 = false;
                boolean empty2 = false;
                int r1 = r - 1;
                int c1 = c + 1;
                while (r1 >= 0 && c1 < 10) {
                    if (tempBoard[(r1 * 10) + c1].equals(OorX)) {
                        r1 = r1 - 1;
                        c1 = c1 + 1;
                    } else if(tempBoard[(r1 * 10) + c1].equals(" ")) {
                        empty1 = true;
                        break;
                    } else break;
                }
                r1 = r + 1;
                c1 = c - 1;
                while (r1 < 10 && c1 >= 0 ) {
                    if(tempBoard[(r1 * 10) + c1].equals(OorX)) {
                        r1 = r1 + 1;
                        c1 = c1 - 1;    
                    } else if (tempBoard[(r1 * 10) + c1].equals(" ")) {
                        empty2 = true;
                        break;
                    } else break;
                }
                if (empty1 && empty2) return true;
            }
            if (length2 >= winNum) {
                boolean empty1 = false;
                boolean empty2 = false;
                int r1 = r - 1;
                while (r1 >= 0) {
                    if(tempBoard[(r1 * 10) + c].equals(OorX)) { r1 = r1 - 1; }
                    else if (tempBoard[(r1 * 10) + c].equals(" ")) {
                        empty1 = true;
                        break;
                    } else break;
                }
                r1 = r + 1;
                while (r1 < 10) {
                    if(tempBoard[(r1 * 10) + c].equals(OorX)) {
                        r1 = r1 + 1;     
                    } else if (tempBoard[(r1 * 10) + c].equals(" ")){
                        empty2 = true;
                        break;
                    } else break;
                }
                if (empty1 && empty2)
                    return true;
            }
            if (length3 >= winNum) {
                boolean empty1 = false;
                boolean empty2 = false;
                int c1 = c + 1;
                while (c1 < 10) {
                    if(tempBoard[(r * 10) + c1].equals(OorX)){ c1 = c1 + 1; }
                    else if (tempBoard[(r * 10) + c1].equals(" ")) {
                        empty1 = true;
                        break;
                    } else break;
                }
                c1 = c - 1;
                while (c1 >= 0) {
                    if(tempBoard[(r * 10) + c1].equals(OorX)){ c1 = c1 - 1; }
                    else if(tempBoard[(r * 10) + c1].equals(" ")) {
                        empty2 = true;
                        break;
                    } else break;
                }
                if (empty1 && empty2) return true;
            }
            if (length4 >= winNum) {
                boolean empty1 = false;
                boolean empty2 = false;
                int r1 = r - 1;
                int c1 = c - 1;
                while (r1 >= 0 && c1 >= 0) {
                    if(tempBoard[(r1 * 10) + c1].equals(OorX)) {
                        r1 = r1 - 1;
                        c1 = c1 - 1;
                    } else if(tempBoard[(r1 * 10) + c1].equals(" ")) {
                        empty1 = true;
                        break;
                    }
                    else break;
                }
                r1 = r + 1;
                c1 = c + 1;
                while (r1 < 10 && c1 < 10) {
                    if(tempBoard[(r1 * 10) + c1].equals(OorX)){
                        r1 = r1 + 1;
                        c1 = c1 + 1;
                    }
                    else if(tempBoard[(r1 * 10) + c1].equals(OorX)){
                        empty2 = true;
                        break;
                    } else break;
                }
                if (empty1 && empty2) return true;
            }
        }
        return false;
    }

    public int getMaxPos(ArrayList<Integer> temp, int pos) {
        int max = 0;
        boolean allEqual = true;
        for (int k = 1; k < temp.size(); k++)
            if(temp.get(k) != temp.get(k-1)) { allEqual = false; }
        int num = (int )(Math.random() * 4 + 1);
        if(num == 1 || num == 3) {
            if(allEqual) {
                int minDistance = temp.get(0) - pos; 
                int minPos = 0;
                for (int k = 1; k < temp.size(); k++) {
                    if(temp.get(k) - pos <= minDistance) {
                        minPos = k;
                        minDistance = temp.get(k) - pos; 
                    }
                }
                return minPos;
            } else {
                for (int i = 1; i < temp.size(); i++) {
                    if(temp.get(i) >= temp.get(max))
                        max = i;
                }
                return max;
            }
        } else {
            if(allEqual) {
                int minDistance = temp.get(0) - pos; 
                int minPos = 0;
                for (int k = 1; k < temp.size(); k++) {
                    if(temp.get(k) - pos < minDistance) {
                        minPos = k;
                        minDistance = temp.get(k) - pos; 
                    }
                }
                return minPos;
            } else {
                for (int i = 1; i < temp.size(); i++)
                    if(temp.get(i) > temp.get(max)) { max = i; } 
                return max;
            }
        }
    }

    //Flip bug function
    public void flipBug() {
        try { Thread.sleep(1000); } 
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        for (JButton button : buttons) {
            switch (button.getText()) {
                case "O":
                    button.setText("X");
                    button.setBackground(Color.BLUE);
                    button.setOpaque(true);
                    break;
                case "X":
                    button.setText("O");
                    button.setBackground(Color.RED);
                    button.setOpaque(true);
                    break;
            }
        }
        SoundManager.getInstance().playWave("f.wav");
    }

    //Eat bug function
    public void eatBug() {
        ArrayList<Integer> list = new ArrayList<>();
        for(int p = 0; p < buttons.length; p++)
            if(!buttons[p].getText().equals(" ")) { list.add(p); }
        int temp = (int)(Math.random() * list.size());
        bugOnBoard = true;
        eatBugPos = list.get(temp);
        buttons[list.get(temp)].setText(" ");
        tempBoard[list.get(temp)] = " ";
        buttons[list.get(temp)].setBackground(Color.YELLOW);
        buttons[list.get(temp)].setEnabled(false);
        buttons[list.get(temp)].setOpaque(true);
        SoundManager.getInstance().playWave("e.wav");
    }

    //Block bug function
    public void blockBug() {
        int curr = -1;
        for (int i = 0; i < 5; i++) {
            if (i == 4 && blockNum[i] > 0) { return; }
            if (blockNum[i] < 1) {
                curr = i;
                break;
            }
        }
        if (curr == -1) { return; }
        ArrayList<Integer> list = new ArrayList<>();
        for(int p = 0; p < buttons.length; p++)
            if(buttons[p].getText().equals(" ")) { list.add(p); }
        int temp = (int)(Math.random() * list.size());
        blockBugPos[curr] = list.get(temp);
        buttons[list.get(temp)].setText(" ");
        tempBoard[list.get(temp)] = " ";
        buttons[list.get(temp)].setBackground(Color.BLACK);
        buttons[list.get(temp)].setEnabled(false);
        buttons[list.get(temp)].setOpaque(true);
        SoundManager.getInstance().playWave("f.wav");
        blockNum[curr] = 1;
    }
    
    public int getTurn() {
        return turnCount;
    }
}
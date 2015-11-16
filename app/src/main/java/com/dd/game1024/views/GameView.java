package com.dd.game1024.views;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dong on 2015-11-16 0016.
 */
public class GameView extends GridLayout {
    private Context mContext;

    private static final int BACKGROUNDCOLOR = 0xffb8af9e;

    private float startX = 0, startY = 0, offsetX = 0, offsetY = 0;
    private float swipeMin = 100;
    private static final int COLUMS = 4;
    private static final int ROWS = 4;
    private static final int CARD_GAP = 10;

    private int mScore = 0;
    private boolean isOver = true;

    private Card[][] mCards = new Card[ROWS][COLUMS];
    private List<Point> mInvalidCardPostion = new ArrayList<>();

    private onGameListener mListener = null;

    public interface onGameListener {
        public void onScore(int score);
        public void gameOver(int score);
    }

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        setColumnCount(COLUMS);
        setBackgroundColor(BACKGROUNDCOLOR);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardSize = ((w > h ? h : w) - CARD_GAP) / COLUMS;
        initCard(cardSize);
    }

    public void startGame() {
        clearCard();
        addValidCard();
        addValidCard();
    }

    public void setOnScoreListener(onGameListener listener) {
        mListener = listener;
    }

    private void initCard(int cardSize) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMS; j++) {
                Card card = new Card(mContext);
                card.setNum(0);
                addView(card, cardSize, cardSize);
                mCards[i][j] = card;
            }
        }
    }

    private void clearCard() {
        isOver = false;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMS; j++) {
                mCards[i][j].setNum(0);
            }
        }
    }

    private void addValidCard() {
        mInvalidCardPostion.clear();
        for(int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLUMS; y++) {
                if(mCards[x][y].getNum() == 0) {
                    mInvalidCardPostion.add(new Point(x, y));
                }
            }
        }
        if (mInvalidCardPostion.size() == 0) return;
        int ramPositon = (int) (Math.random()*mInvalidCardPostion.size());
        Point point = mInvalidCardPostion.remove(ramPositon);
        int ramNum = Math.random() > 0.15 ? 2 : 4;
        mCards[point.x][point.y].setNum(ramNum);
    }

    private void gameOverOberver() {
        boolean isGameOver = true;

        if(mInvalidCardPostion.size() == 0) {
            ALL:
            for (int x = 0; x < ROWS; x++) {
                for (int y = 0; y < COLUMS; y++) {
                    if( (x>1&&mCards[x][y].isEquals(mCards[x - 1][y]))  ||
                        (x<3&&mCards[x][y].isEquals(mCards[x + 1][y]))  ||
                        (y>0&&mCards[x][y].isEquals(mCards[x][y - 1]))  ||
                        (x<3&&mCards[x][y].isEquals(mCards[x+1][y])) ) {
                        isGameOver = false;
                        break ALL;
                    }
                }
            }
        }else {
            isGameOver = false;
        }
        isOver = isGameOver;
        if(isGameOver) {
            if(mListener != null) {
                mListener.gameOver(mScore);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                offsetX = event.getRawX() - startX;
                offsetY = event.getRawY() - startY;
                swipeScreen(offsetX, offsetY);
                break;
        }
        return true;
    }

    private void swipeScreen(float ofX, float ofY) {
        if(isOver) return;
        if (Math.abs(ofX) > Math.abs(ofY)) {//Horizontal
            if (Math.abs(ofX) < swipeMin) return;
            if (ofX > 0) {
                swipeRigth();
            } else {
                swipeLeft();
            }
            addValidCard();
            gameOverOberver();
        } else {//vertical
            if (Math.abs(ofY) < swipeMin) return;
            if (ofY > 0) {
                swipeDown();
            } else {
                swipeUp();
            }
            addValidCard();
            gameOverOberver();
        }
    }

    private void calculateScore(int num) {
        mScore += (num);
        if(mListener != null) {
            mListener.onScore(mScore);
        }
    }

    private void swipeUp() {
        for(int y = 0; y < COLUMS; y++) {
            for (int x = 0; x < ROWS; x++) {
                for (int x1 = x+1; x1 < ROWS; x1++) {
                    if(mCards[x1][y].getNum() > 0) {
                        if(mCards[x][y].isEquals(mCards[x1][y])) {
                            mCards[x][y].setNum(mCards[x][y].getNum()*2);
                            mCards[x1][y].setNum(0);
                            mCards[x][y].showAnimate();
                            calculateScore(mCards[x][y].getNum());
                        }else if(mCards[x][y].getNum() == 0){
                            mCards[x][y].setNum(mCards[x1][y].getNum());
                            mCards[x1][y].setNum(0);
                            x--;
                        }
                        break;
                    }
                }
            }
        }
    }

    private void swipeDown() {
        for(int y = 0; y < COLUMS; y++) {
            for (int x = ROWS - 1; x >= 0; x--) {
                for (int x1 = x -1; x1 >= 0; x1--) {
                    if(mCards[x1][y].getNum() > 0) {
                        if(mCards[x][y].isEquals(mCards[x1][y])) {
                            mCards[x][y].setNum(mCards[x][y].getNum()*2);
                            mCards[x1][y].setNum(0);
                            mCards[x][y].showAnimate();
                            calculateScore(mCards[x][y].getNum());
                        }else if(mCards[x][y].getNum() == 0){
                            mCards[x][y].setNum(mCards[x1][y].getNum());
                            mCards[x1][y].setNum(0);
                            x++;
                        }
                        break;
                    }
                }
            }
        }
    }

    private void swipeLeft() {
        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLUMS; y++) {
                for (int y1 = y+1; y1 < COLUMS; y1++) {
                    if(mCards[x][y1].getNum()>0) {
                        if (mCards[x][y].isEquals(mCards[x][y1])) {
                            mCards[x][y].setNum(mCards[x][y].getNum() * 2);
                            mCards[x][y1].setNum(0);
                            mCards[x][y].showAnimate();
                            calculateScore(mCards[x][y].getNum());
                        } else if(mCards[x][y].getNum() == 0){
                            mCards[x][y].setNum(mCards[x][y1].getNum());
                            mCards[x][y1].setNum(0);
                            y--;
                        }
                        break;
                    }
                }
            }
        }
    }

    private void swipeRigth() {
        for (int x = 0; x < ROWS; x++) {
            for (int y = COLUMS-1; y >= 0; y--) {
                for (int y1 = y-1; y1 >= 0; y1--) {
                    if(mCards[x][y1].getNum()>0) {
                        if (mCards[x][y].isEquals(mCards[x][y1])) {
                            mCards[x][y].setNum(mCards[x][y].getNum() * 2);
                            mCards[x][y1].setNum(0);
                            mCards[x][y].showAnimate();
                            calculateScore(mCards[x][y].getNum());
                        } else if(mCards[x][y].getNum() == 0){
                            mCards[x][y].setNum(mCards[x][y1].getNum());
                            mCards[x][y1].setNum(0);
                            y++;
                        }
                        break;
                    }
                }
            }
        }
    }

}

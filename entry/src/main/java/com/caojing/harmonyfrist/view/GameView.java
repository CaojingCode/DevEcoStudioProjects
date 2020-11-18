package com.caojing.harmonyfrist.view;

import com.caojing.harmonyfrist.entity.MPoint;
import com.caojing.harmonyfrist.slice.MainAbilitySlice;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.TableLayout;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.media.audio.SoundPlayer;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class GameView extends TableLayout {
    MainAbilitySlice mainAbilitySlice;
    //标示是否可以悔棋，默认不能悔棋，滑动一次后才能悔棋
    boolean isCanRetract = false;

    //记录卡片view的二维数组
    private CardView[][] cardMap = new CardView[4][4];

    //记录上一步卡片位置的二维数组
    private int[][] retractMap = new int[4][4];

    //记录所有空点的集合，随机数只能添加在空点内
    private List<MPoint> emptyPoints = new ArrayList<>();

    private int score = 0;
    float volume = 1.0f; //音频音量


    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initGameView();
    }

    public GameView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initGameView();
    }

    // 音频播放实例化对象
    SoundPlayer soundPlayer = new SoundPlayer("com.caojing.harmonyfrist");

    /**
     * 播放声音
     */
    public void playSound() {
        // 播放键盘敲击音，音量为1.0
        soundPlayer.playSound(SoundPlayer.SoundType.KEY_CLICK, volume);
    }


    void initGameView() {
        mainAbilitySlice = ((MainAbilitySlice) getContext());

        setColumnCount(4);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(185, 176, 160));
        setBackground(element);
        setPadding(0, 0, 10, 10);
        addCards();
        startGame();

        //设置触摸监听事件
        setTouchEventListener(new TouchEventListener() {
            private float starX, starY, offsetX, offsetY;

            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                MmiPoint point = touchEvent.getPointerScreenPosition(0);
                switch (touchEvent.getAction()) {
                    case TouchEvent.PRIMARY_POINT_DOWN:
                        starX = point.getX(); //记录手指按下的x点坐标
                        starY = point.getY(); //记录手指按下的y点坐标
                        break;
                    case TouchEvent.PRIMARY_POINT_UP:
                        offsetX = point.getX() - starX;//横向滑动距离
                        offsetY = point.getY() - starY;//纵向滑动距离
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                //左滑监听
                                System.out.println("左边");
                                swipeLeft();
                            } else if (offsetX > 5) {
                                //右滑监听
                                System.out.println("右边");
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                //上滑监听
                                System.out.println("上边");
                                swipeUp();
                            } else if (offsetY > 5) {
                                //下滑监听
                                System.out.println("下边");
                                swipeDown();
                            }
                        }

                        break;

                }
                return true;
            }
        });

    }

    /**
     * 添加数字卡片
     */
    private void addCards() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                CardView cardView = new CardView(getContext());
                cardView.setNum(0);
                cardView.setPadding(10, 10, 0, 0);
                addComponent(cardView, 180, 180);
                cardMap[x][y] = cardView;
            }
        }
    }

    /**
     * 开始游戏
     */
    public void startGame() {
        mainAbilitySlice.clearScore();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardMap[x][y].setNum(0);
            }
        }
        //添加两个随机数卡片
        addRoundNum();
        addRoundNum();
        isCanRetract = false;
    }


    /**
     * 添加随机数
     */
    private void addRoundNum() {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardMap[x][y].getNum() <= 0) {
                    //将空点添加到空点集合中
                    emptyPoints.add(new MPoint(x, y));
                }
            }
        }
        if (emptyPoints.size() > 0) {
            MPoint point = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
            cardMap[point.getX()][point.getY()].setNum(Math.random() > 0.1 ? 2 : 4);
//            mainAbilitySlice.getAnimLayer().createScaleTo1(cardMap[point.getX()][point.getY()]);
        }
        gameOver();
    }

    private void swipeLeft() {
        retractMap();
        boolean isMerge = false;
        //向左边滑动
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    //如果同一行的右边的点数字大于0
                    if (cardMap[x1][y].getNum() > 0) {
                        //如果最左边没有数字时，则将右边数字移动到左边
                        if (cardMap[x][y].getNum() <= 0) {
                            //添加移动动画，目前还有问题
//                            mainAbilitySlice.getAnimLayer().createMoveAnim(cardMap[x1][y], cardMap[x][y], x1, x, y, y);

                            cardMap[x][y].setNum(cardMap[x1][y].getNum());
                            cardMap[x1][y].setNum(0);
                            x--;
                            isMerge = true;
                        } else if (cardMap[x][y].equals(cardMap[x1][y])) {
                            //如果有右边的数字和左边的数字相等，则进行合并
//                            mainAbilitySlice.getAnimLayer().createMoveAnim(cardMap[x1][y], cardMap[x][y], x1, x, y, y);
                            cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
                            cardMap[x1][y].setNum(0);
                            isMerge = true;
                            mainAbilitySlice.addScore(cardMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        //如果存在合并或者移动，则添加一个随机棋子
        if (isMerge) addRoundNum();
    }

    private void swipeRight() {
        retractMap();
        //向右滑动
        //是否存在合并或者移动
        boolean isMerge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    //如果同一行的右边的点数字大于0
                    if (cardMap[x1][y].getNum() > 0) {
                        //如果最左边没有数字时，则将右边数字移动到左边
                        if (cardMap[x][y].getNum() <= 0) {
//                            mainAbilitySlice.getAnimLayer().createMoveAnim(cardMap[x1][y], cardMap[x][y], x1, x, y, y);
                            cardMap[x][y].setNum(cardMap[x1][y].getNum());
                            cardMap[x1][y].setNum(0);
                            x++;
                            isMerge = true;
                        } else if (cardMap[x][y].equals(cardMap[x1][y])) {
                            //如果有右边的数字和左边的数字相等，则进行合并
//                            mainAbilitySlice.getAnimLayer().createMoveAnim(cardMap[x1][y], cardMap[x][y], x1, x, y, y);
                            cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
                            cardMap[x1][y].setNum(0);
                            isMerge = true;
                            mainAbilitySlice.addScore(cardMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        //如果存在合并或者移动，则添加一个随机棋子
        if (isMerge) addRoundNum();
    }

    private void swipeUp() {
        retractMap();
        //是否存在合并或者移动
        boolean isMerge = false;
        //向上滑动
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {
                    //如果同一行的右边的点数字大于0
                    if (cardMap[x][y1].getNum() > 0) {
                        //如果最左边没有数字时，则将右边数字移动到左边
                        if (cardMap[x][y].getNum() <= 0) {
//                            mainAbilitySlice.getAnimLayer().createMoveAnim(cardMap[x][y1], cardMap[x][y], x, x, y1, y);
                            cardMap[x][y].setNum(cardMap[x][y1].getNum());
                            cardMap[x][y1].setNum(0);
                            y--;
                            isMerge = true;
                        } else if (cardMap[x][y].equals(cardMap[x][y1])) {
                            //如果有右边的数字和左边的数字相等，则进行合并
//                            mainAbilitySlice.getAnimLayer().createMoveAnim(cardMap[x][y1], cardMap[x][y], x, x, y1, y);
                            cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
                            cardMap[x][y1].setNum(0);
                            isMerge = true;
                            mainAbilitySlice.addScore(cardMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        //如果存在合并或者移动，则添加一个随机棋子
        if (isMerge) addRoundNum();
    }

    private void swipeDown() {
        retractMap();
        //是否存在合并或者移动
        boolean isMerge = false;
        //向下滑动
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    //如果同一行的右边的点数字大于0
                    if (cardMap[x][y1].getNum() > 0) {
                        //如果最左边没有数字时，则将右边数字移动到左边
                        if (cardMap[x][y].getNum() <= 0) {
//                            mainAbilitySlice.getAnimLayer().createMoveAnim(cardMap[x][y1], cardMap[x][y], x, x, y1, y);
                            cardMap[x][y].setNum(cardMap[x][y1].getNum());
                            cardMap[x][y1].setNum(0);
                            y++;
                            isMerge = true;
                        } else if (cardMap[x][y].equals(cardMap[x][y1])) {
                            //如果有右边的数字和左边的数字相等，则进行合并
//                            mainAbilitySlice.getAnimLayer().createMoveAnim(cardMap[x][y1], cardMap[x][y], x, x, y1, y);
                            cardMap[x][y].setNum(cardMap[x][y].getNum() * 2);
                            cardMap[x][y1].setNum(0);
                            isMerge = true;
                            mainAbilitySlice.addScore(cardMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        //如果存在合并或者移动，则添加一个随机棋子
        if (isMerge) addRoundNum();
    }


    /**
     * 记录悔棋的二维数组
     */
    private void retractMap() {
        //是否可以悔棋，滑动后则可以开始悔棋
        isCanRetract = true;
        score = mainAbilitySlice.getScore();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                retractMap[x][y] = cardMap[x][y].getNum();
            }
        }
        //播放声音
        playSound();
    }

    /**
     * 执行悔棋操作
     */
    public void retractGame() {
        //如果没有滑动则不能悔棋。
        if (!isCanRetract)
            return;
        mainAbilitySlice.showScore(score);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardMap[x][y].setNum(retractMap[x][y]);
            }
        }
    }


    /**
     * 游戏结束
     * 如果有cardMap里面有值为0即还有空格，或者当前格上下左右有相等的数字，则游戏继续
     * 否则游戏结束
     */
    private void gameOver() {
        boolean isOver = true; //标示游戏是否结束，默认结束
        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardMap[x][y].getNum() == 0 ||
                        (x > 0 && cardMap[x][y].equals(cardMap[x - 1][y])) ||
                        (x < 3 && cardMap[x][y].equals(cardMap[x + 1][y])) ||
                        (y > 0 && cardMap[x][y].equals(cardMap[x][y - 1])) ||
                        (y < 3 && cardMap[x][y].equals(cardMap[x][y + 1]))) {
                    isOver = false;
                    break ALL;
                }
            }
        }
        if (isOver) {
            //游戏结束,保存总分
            DatabaseHelper databaseHelper = new DatabaseHelper(mainAbilitySlice); // context入参类型为ohos.app.Context。
            String fileName = "game"; // fileName表示文件名，其取值不能为空，也不能包含路径，默认存储目录可以通过context.getPreferencesDir()获取。
            Preferences preferences = databaseHelper.getPreferences(fileName);
            int totalScore = preferences.getInt("totalScore", 0);//保存的总分
            if (totalScore < mainAbilitySlice.getScore()) {
                //如果保存的总分小于当前总分，那么就保存大当前总分
                preferences.putInt("totalScore", mainAbilitySlice.getScore());
                preferences.flush();
            }

            mainAbilitySlice.gameOver();
            //设置最高分
            mainAbilitySlice.showTotalScore(preferences.getInt("totalScore", 0));
        }

    }
}
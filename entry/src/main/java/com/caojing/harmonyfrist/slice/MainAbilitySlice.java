package com.caojing.harmonyfrist.slice;

import com.caojing.harmonyfrist.ResourceTable;
import com.caojing.harmonyfrist.view.GameView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.components.element.ElementContainer;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;

public class MainAbilitySlice extends AbilitySlice {

    private DirectionalLayout myLayout = new DirectionalLayout(this);
    private DirectionalLayout myLayoutTop = new DirectionalLayout(this);
    private DirectionalLayout myLayoutBottom = new DirectionalLayout(this);
    private StackLayout stackLayout = new StackLayout(this);

    private int score = 0;
    private Text tvScore, tvTotal,textGameOver;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
//        setUIContent(ResourceTable.Layout_main_layout);
        //设置根布局，全屏铺满，剧中，背景等
        LayoutConfig configParent = new LayoutConfig(LayoutConfig.MATCH_PARENT, LayoutConfig.MATCH_PARENT);
        LayoutConfig configContent = new LayoutConfig(LayoutConfig.MATCH_CONTENT, LayoutConfig.MATCH_CONTENT);

        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(255, 255, 255));//最外层布局背景
        ShapeElement scoreBg = new ShapeElement();
        scoreBg.setRgbColor(new RgbColor(181, 171, 160));//分数背景
        scoreBg.setCornerRadius(20f);

        myLayout.setAlignment(LayoutAlignment.CENTER);
        myLayout.setLayoutConfig(configParent);
        myLayout.setBackground(element);

        myLayoutTop.setLayoutConfig(configContent);
        myLayoutTop.setAlignment(LayoutAlignment.CENTER);
        myLayoutTop.setMarginTop(40);
        myLayoutTop.setOrientation(Component.HORIZONTAL);

        stackLayout.setLayoutConfig(configContent);

        myLayoutBottom.setLayoutConfig(configContent);
        myLayoutBottom.setAlignment(LayoutAlignment.CENTER);
        myLayoutBottom.setOrientation(Component.HORIZONTAL);
        myLayoutBottom.setMarginsTopAndBottom(20,10);

        myLayout.addComponent(myLayoutTop);
        myLayout.addComponent(myLayoutBottom);
        myLayout.addComponent(stackLayout);


        Text gameName=new Text(this);
        gameName.setLayoutConfig(configContent);
        gameName.setText("2048");
        gameName.setTextSize(100);
        gameName.setMarginRight(200);
        myLayoutTop.addComponent(gameName);

        DirectionalLayout scoreLayout = new DirectionalLayout(this);
        scoreLayout.setLayoutConfig(configContent);
        scoreLayout.setAlignment(LayoutAlignment.CENTER);
        scoreLayout.setOrientation(Component.VERTICAL);
        scoreLayout.setBackground(scoreBg);
        scoreLayout.setMarginRight(30);
        scoreLayout.setWidth(200);
        scoreLayout.setPaddingTop(5);
        scoreLayout.setPaddingBottom(5);
        myLayoutTop.addComponent(scoreLayout);

        Text tvScoreName = new Text(this);
        tvScoreName.setLayoutConfig(configContent);
        tvScoreName.setText("分数");
        tvScoreName.setTextColor(new Color(0xFFFFFFFF));
        tvScoreName.setTextSize(25);
        scoreLayout.addComponent(tvScoreName);

        tvScore = new Text(this);
        tvScore.setLayoutConfig(configContent);
        tvScore.setText("0");
        tvScore.setTextColor(new Color(0xFFFFFFFF));
        tvScore.setTextSize(40);
        scoreLayout.addComponent(tvScore);


        DirectionalLayout totalLayout = new DirectionalLayout(this);
        totalLayout.setLayoutConfig(configContent);
        totalLayout.setAlignment(LayoutAlignment.CENTER);
        totalLayout.setOrientation(Component.VERTICAL);
        totalLayout.setBackground(scoreBg);
        totalLayout.setWidth(200);
        totalLayout.setPaddingTop(5);
        totalLayout.setPaddingBottom(5);
        myLayoutTop.addComponent(totalLayout);

        Text tvTotalName = new Text(this);
        tvTotalName.setLayoutConfig(configContent);
        tvTotalName.setText("最高分");
        tvTotalName.setTextColor(new Color(0xFFFFFFFF));
        tvTotalName.setTextSize(25);
        totalLayout.addComponent(tvTotalName);

        tvTotal = new Text(this);
        tvTotal.setLayoutConfig(configContent);
        tvTotal.setText("0");
        tvTotal.setTextColor(new Color(0xFFFFFFFF));
        tvTotal.setTextSize(40);
        totalLayout.addComponent(tvTotal);

        GameView gameView = new GameView(this);
        gameView.setLayoutConfig(configContent);
        gameView.setAlignmentType(LayoutAlignment.CENTER);
        stackLayout.addComponent(gameView);


        textGameOver=new Text(this);
        textGameOver.setLayoutConfig(configContent);
        textGameOver.setText("游戏结束");
        textGameOver.setTextSize(100);
        textGameOver.setTextAlignment(TextAlignment.CENTER);
        ShapeElement textElement=new ShapeElement();
        textElement.setRgbColor(new RgbColor(255,255,255,150));
        textGameOver.setBackground(textElement);
        stackLayout.addComponent(textGameOver);
        textGameOver.setVisibility(Component.INVISIBLE);

        Text textDetails=new Text(this);
        textDetails.setText("合并相同方块，得到2048的方块!");
        textDetails.setTextSize(30);
        textDetails.setMarginRight(230);
        myLayoutBottom.addComponent(textDetails);



        Image imgRetract = new Image(this);
        imgRetract.setLayoutConfig(configContent);
        imgRetract.setWidth(80);
        imgRetract.setHeight(80);
        imgRetract.setMarginRight(40);
        imgRetract.setPadding(15,15,15,15);
        imgRetract.setImageAndDecodeBounds(ResourceTable.Media_jiantou);
        imgRetract.setScaleMode(Image.ScaleMode.INSIDE);
        imgRetract.setBackground(scoreBg);
        myLayoutBottom.addComponent(imgRetract);

        Image imgRestart = new Image(this);
        imgRestart.setLayoutConfig(configContent);
        imgRestart.setWidth(80);
        imgRestart.setHeight(80);
        imgRestart.setPadding(15,15,15,15);
        imgRestart.setImageAndDecodeBounds(ResourceTable.Media_shuaxingengxin);
        imgRestart.setScaleMode(Image.ScaleMode.INSIDE);
        imgRestart.setBackground(scoreBg);
        myLayoutBottom.addComponent(imgRestart);



        DatabaseHelper databaseHelper = new DatabaseHelper(this); // context入参类型为ohos.app.Context。
        String fileName = "game"; // fileName表示文件名，其取值不能为空，也不能包含路径，默认存储目录可以通过context.getPreferencesDir()获取。
        Preferences preferences = databaseHelper.getPreferences(fileName);
        int totalScore = preferences.getInt("totalScore", 0);//保存的总分
        tvTotal.setText("" + totalScore);

        //重新开始游戏点击事件
        imgRestart.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                gameView.startGame();
                startGame();
            }
        });

        //悔棋点击事件
        imgRetract.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                gameView.retractGame();
            }
        });

        super.setUIContent(myLayout);

    }

    /**
     * 游戏结束，显示结束UI
     */
    public void gameOver(){
        textGameOver.setVisibility(Component.VISIBLE);
    }

    /**
     * 游戏开始，隐藏结束UI
     */
    public void startGame(){
        textGameOver.setVisibility(Component.INVISIBLE);
    }

    /**
     * 清除分数
     */
    public void clearScore() {
        score = 0;
        showScore();
    }

    /**
     * 设置分数
     */
    public void showScore() {
        tvScore.setText("" + score);
    }

    public void showScore(int score) {
        tvScore.setText("" + score);
    }

    public void showTotalScore(int totalScore) {
        tvTotal.setText("" + totalScore);
    }

    /**
     * 添加分数
     * @param score
     */
    public void addScore(int score) {
        this.score += score;
        showScore();

    }

    /**
     * 获取分数
     *
     * @return
     */
    public Integer getScore() {
        return score;
    }


    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}

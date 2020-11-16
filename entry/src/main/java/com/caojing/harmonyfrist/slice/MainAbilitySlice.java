package com.caojing.harmonyfrist.slice;

import com.caojing.harmonyfrist.AnimLayer;
import com.caojing.harmonyfrist.GameView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.agp.components.*;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;

public class MainAbilitySlice extends AbilitySlice {

    private DirectionalLayout myLayout = new DirectionalLayout(this);
    private DirectionalLayout myLayoutSun = new DirectionalLayout(this);
    private StackLayout stackLayout = new StackLayout(this);

    private int score = 0;
    private Text tvScore, tvTotal;
    private AnimLayer animLayer = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        //设置跟布局，全屏铺满，剧中，背景等
        LayoutConfig config = new LayoutConfig(LayoutConfig.MATCH_PARENT, LayoutConfig.MATCH_PARENT);
        myLayout.setLayoutConfig(config);
        myLayout.setAlignment(LayoutAlignment.CENTER);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(255, 255, 255));
        myLayout.setBackground(element);

        LayoutConfig configSun = new LayoutConfig(LayoutConfig.MATCH_CONTENT, LayoutConfig.MATCH_CONTENT);
        myLayoutSun.setLayoutConfig(configSun);
        myLayoutSun.setAlignment(LayoutAlignment.CENTER);
        myLayoutSun.setOrientation(Component.HORIZONTAL);
        myLayout.addComponent(myLayoutSun);

        LayoutConfig configBtn = new LayoutConfig(LayoutConfig.MATCH_CONTENT, LayoutConfig.MATCH_CONTENT);
        tvScore = new Text(this);
        tvScore.setLayoutConfig(configBtn);
        tvScore.setText("分数：0");
        tvScore.setTextColor(new Color(0xFF000000));
        tvScore.setTextSize(50);
        myLayoutSun.addComponent(tvScore);
        tvScore.setPadding(0, 0, 200, 0);

        tvTotal = new Text(this);
        tvTotal.setLayoutConfig(configBtn);
        tvTotal.setText("最高分：0");
        tvTotal.setTextColor(new Color(0xFF000000));
        tvTotal.setTextSize(50);
        myLayoutSun.addComponent(tvTotal);

        LayoutConfig stackConfig = new LayoutConfig(LayoutConfig.MATCH_CONTENT, LayoutConfig.MATCH_CONTENT);
        stackLayout.setLayoutConfig(stackConfig);
        myLayout.addComponent(stackLayout);

        LayoutConfig configBgv = new LayoutConfig(LayoutConfig.MATCH_CONTENT, LayoutConfig.MATCH_CONTENT);
        GameView gameView = new GameView(this);
        config.alignment = LayoutAlignment.CENTER;
        gameView.setLayoutConfig(configBgv);
        gameView.setAlignmentMode(LayoutAlignment.CENTER);
        stackLayout.addComponent(gameView);

        animLayer = new AnimLayer(this);
        config.alignment = LayoutAlignment.CENTER;
        animLayer.setLayoutConfig(configBgv);
        stackLayout.addComponent(animLayer);

        Button button = new Button(this);
        button.setLayoutConfig(configBtn);
        button.setText("重新开始");
        button.setTextSize(50);
        myLayout.addComponent(button);

        DatabaseHelper databaseHelper = new DatabaseHelper(this); // context入参类型为ohos.app.Context。
        String fileName = "game"; // fileName表示文件名，其取值不能为空，也不能包含路径，默认存储目录可以通过context.getPreferencesDir()获取。
        Preferences preferences = databaseHelper.getPreferences(fileName);
        int totalScore = preferences.getInt("totalScore", 0);//保存的总分
        tvTotal.setText("最高分："+totalScore);

        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                gameView.startGame();

            }
        });
        super.setUIContent(myLayout);

    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        tvScore.setText("分数：" + score);
    }

    public void showTotalScore(int totalScore){
        tvTotal.setText("最高分：" + totalScore);
    }

    public void addScore(int score) {
        this.score += score;
        showScore();

    }

    /**
     * 获取分数
     * @return
     */
    public Integer getScore(){
        return score;
    }

    public AnimLayer getAnimLayer() {
        return animLayer;
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

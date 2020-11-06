package com.caojing.harmonyfrist.slice;

import com.caojing.harmonyfrist.GameView;
import com.caojing.harmonyfrist.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.components.Text;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;

public class MainAbilitySlice extends AbilitySlice {

    private DirectionalLayout myLayout = new DirectionalLayout(this);

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        LayoutConfig config = new LayoutConfig(LayoutConfig.MATCH_PARENT, LayoutConfig.MATCH_PARENT);
        myLayout.setLayoutConfig( config);
        myLayout.setAlignment(LayoutAlignment.CENTER);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(255, 255, 255));
        myLayout.setBackground(element);
        LayoutConfig configBtn=new LayoutConfig(LayoutConfig.MATCH_CONTENT,LayoutConfig.MATCH_CONTENT);
//        Text text = new Text(this);
//        text.setLayoutConfig(configBtn);
//        text.setText("Hello World");
//        text.setTextColor(new Color(0xFF000000));
//        text.setTextSize(50);
//        myLayout.addComponent(text);
        Button button=new Button(this);
        button.setLayoutConfig(configBtn);
        button.setText("重新开始");
        button.setTextSize(50);
        myLayout.addComponent(button);
        LayoutConfig configBgv=new LayoutConfig(LayoutConfig.MATCH_CONTENT,LayoutConfig.MATCH_CONTENT);
        GameView gameView=new GameView(this);
        config.alignment=LayoutAlignment.CENTER;
        gameView.setLayoutConfig(configBgv);
        gameView.setAlignmentMode(LayoutAlignment.CENTER);
        myLayout.addComponent(gameView);

        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                gameView.startGame();

            }
        });
        super.setUIContent(myLayout);

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
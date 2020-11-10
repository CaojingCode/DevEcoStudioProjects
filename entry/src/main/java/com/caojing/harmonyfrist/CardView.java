package com.caojing.harmonyfrist;

import ohos.aafwk.ability.Ability;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.StackLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

import java.util.Random;

public class CardView extends StackLayout {
    private int num = 0;

    private Text lable;


    public CardView(Context context) {
        super(context);
        initView();
    }

    public CardView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initView();
    }

    public CardView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView();
    }

    void initView() {
        lable = new Text(getContext());
        lable.setTextSize(80);
        lable.setTextAlignment(TextAlignment.CENTER);

        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(198, 187, 183));
        lable.setBackground(element);
        LayoutConfig layoutConfig = new LayoutConfig(-1, -1);
        addComponent(lable, layoutConfig);
        setNum(0);
    }

    void setTextColor(int num){
        ShapeElement element = new ShapeElement();
       switch (num){
           case 0:
               element.setRgbColor(new RgbColor(198, 187, 183));
               break;
           case 2:
               lable.setTextColor(Color.RED);
               element.setRgbColor(new RgbColor(100, 187, 183));
               break;
           case 4:
               lable.setTextColor(Color.BLACK);
               element.setRgbColor(new RgbColor(100, 100, 183));
               break;
           case 8:
               lable.setTextColor(Color.BLUE);
               element.setRgbColor(new RgbColor(100, 100, 100));
               break;
           case 16:
               lable.setTextColor(Color.CYAN);
               element.setRgbColor(new RgbColor(150, 150, 150));
               break;
           case 32:
               lable.setTextColor(Color.DKGRAY);
               element.setRgbColor(new RgbColor(200, 150, 150));
               break;
           case 64:
               lable.setTextColor(Color.GRAY);
               element.setRgbColor(new RgbColor(200, 200, 150));
               break;
           case 128:
               lable.setTextColor(Color.GREEN);
               element.setRgbColor(new RgbColor(200, 200, 200));
               break;
           case 256:
               lable.setTextColor(Color.LTGRAY);
               element.setRgbColor(new RgbColor(250, 200, 200));
               break;
           case 512:
               lable.setTextColor(Color.WHITE);
               element.setRgbColor(new RgbColor(250, 250, 200));
               break;
           case 1024:
               lable.setTextColor(Color.YELLOW);
               element.setRgbColor(new RgbColor(250, 250, 250));
               break;
           case 2048:
               lable.setTextColor(Color.RED);
               element.setRgbColor(new RgbColor(100, 250, 250));
               break;
       }

        lable.setBackground(element);
    }

    public int getNum() {
        return num;
    }

    //设置卡片的数字
    public void setNum(int num) {
        this.num = num;
        if (num<=0){
            lable.setText("");
        }else{
            lable.setText(num + "");
        }
        setTextColor(num);
    }

    /**
     * 判断卡片是否相同
     * @param card
     * @return
     */
    public boolean equals(CardView card) {
        return getNum() == card.getNum();
    }
}

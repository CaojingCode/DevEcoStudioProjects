package com.caojing.harmonyfrist.view;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.StackLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

/**
 * 自定义卡片视图
 */
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

    /**
     * 设置卡片文字和背景颜色
     * @param num
     */
    void setTextColor(int num){
        ShapeElement element = new ShapeElement();
       switch (num){
           case 0:
               element.setRgbColor(new RgbColor(205, 193, 180));
               break;
           case 2:
               lable.setTextColor(new Color(Color.getIntColor("#645B52")));
               element.setRgbColor(new RgbColor(238,228,218));
               break;
           case 4:
               lable.setTextColor(new Color(Color.getIntColor("#645B52")));
               element.setRgbColor(new RgbColor(237,224,200));
               break;
           case 8:
               lable.setTextColor(new Color(Color.getIntColor("#FFFFFF")));
               element.setRgbColor(new RgbColor(242,177,121));
               break;
           case 16:
               lable.setTextColor(new Color(Color.getIntColor("#FFFFFF")));
               element.setRgbColor(new RgbColor(245,149,99));
               break;
           case 32:
               lable.setTextColor(new Color(Color.getIntColor("#FFFFFF")));
               element.setRgbColor(new RgbColor(246,124,95));
               break;
           case 64:
               lable.setTextColor(new Color(Color.getIntColor("#FFFFFF")));
               element.setRgbColor(new RgbColor(246,94,59));
               break;
           case 128:
               lable.setTextColor(new Color(Color.getIntColor("#FFFFFF")));
               element.setRgbColor(new RgbColor(237,207,114));
               break;
           case 256:
               lable.setTextColor(new Color(Color.getIntColor("#FFFFFF")));
               element.setRgbColor(new RgbColor(237,204,97));
               break;
           case 512:
               lable.setTextColor(new Color(Color.getIntColor("#FFFFFF")));
               element.setRgbColor(new RgbColor(153,204,0));
               break;
           case 1024:
               lable.setTextColor(new Color(Color.getIntColor("#FFFFFF")));
               element.setRgbColor(new RgbColor(131,175,155));
               break;
           case 2048:
               lable.setTextColor(new Color(Color.getIntColor("#FFFFFF")));
               element.setRgbColor(new RgbColor(0,153,204));
               break;
       }

        lable.setBackground(element);
    }

    /**
     * 获得卡片数字
     * @return
     */
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

    public Component getLabel() {
        return lable;
    }
}

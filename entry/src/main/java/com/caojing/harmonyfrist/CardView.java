package com.caojing.harmonyfrist;

import ohos.aafwk.ability.Ability;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.StackLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

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

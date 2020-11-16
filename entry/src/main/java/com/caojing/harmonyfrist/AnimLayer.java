package com.caojing.harmonyfrist;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.StackLayout;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

public class AnimLayer extends StackLayout {
    public static final int LINES = 4;
    public static int CARD_WIDTH = 150;

    private List<CardView> cards = new ArrayList<CardView>();
    AnimatorProperty animator;

    public AnimLayer(Context context) {
        super(context);
    }

    public AnimLayer(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public AnimLayer(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }


    public void createMoveAnim(final CardView from, final CardView to, int fromX, int toX, int fromY, int toY) {

        final CardView c = getCard(from.getNum());

        LayoutConfig layoutConfig = new LayoutConfig(CARD_WIDTH, CARD_WIDTH);
        layoutConfig.setMargins(fromX * CARD_WIDTH, fromY * CARD_WIDTH, 0, 0);
        c.setLayoutConfig(layoutConfig);

        if (to.getNum() <= 0) {
            to.getLabel().setVisibility(Component.INVISIBLE);
        }

        animator = c.createAnimatorProperty();
        animator.moveFromX(0)
                .moveToX(CARD_WIDTH * (toX - fromX))
                .moveFromY(0)
                .moveToY(CARD_WIDTH * (toY - fromY))
                .setDuration(100);
        animator.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {

            }

            @Override
            public void onStop(Animator animator) {

            }

            @Override
            public void onCancel(Animator animator) {

            }

            @Override
            public void onEnd(Animator animator) {
                to.getLabel().setVisibility(Component.VISIBLE);
                recycleCard(c);
            }

            @Override
            public void onPause(Animator animator) {

            }

            @Override
            public void onResume(Animator animator) {

            }
        });
        animator.start();
    }


    private CardView getCard(int num) {
        CardView c;
        if (cards.size() > 0) {
            c = cards.remove(0);
        } else {
            c = new CardView(getContext());
            addComponent(c);
        }
        c.setVisibility(Component.VISIBLE);
        c.setNum(num);
        return c;
    }

    private void recycleCard(CardView c) {
        c.setVisibility(Component.INVISIBLE);
        animator.setTarget(null);
        cards.add(c);
    }
//
//    public void createScaleTo1(CardView cardView) {
//
//        ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        sa.setDuration(100);
//        cardView.setAnimation(null);
//        cardView.getLabel().startAnimation(sa);
//    }
}

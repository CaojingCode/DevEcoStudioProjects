# DevEcoStudioProjects
鸿蒙2048小游戏

2048大家应该都玩过，今天我们就来实现一个可以在鸿蒙系统上运行的2048小游戏，因为没有智慧屏，所以这里是在鸿蒙远程TV模拟器上运行的，大概长下面这样：
![鸿蒙TV模拟器](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/6a5974665ba0410381cfb2dd537e3252~tplv-k3u1fbpfcp-watermark.image)
在开始写代码之前，我们来分析下，要实现这个小游戏大概需要这么几步：
- **1.自定义数字卡片的样式CardView，包含设置卡片的文本数字，以及卡片的数字的颜色，以及单个卡片的背景。**
- **2.自定义一个游戏视图GameView，所有和游戏相关的逻辑都在这个View处理，当然最重要的还是手势的监听。**
- **3.在主页面引用自定义的GameView，以及添加一些分数，最高分，悔棋，重新开始等按钮，和游戏介绍等。**
## 1.自定义游戏卡片CardView
**PS：为什么要定一个`CardView`，你不直接用鸿蒙中的`Text`控件，给它设置一个背景呢，主要是想把设置数字和数字颜色和背景的相关逻辑单独处理。**

这里要介绍一下两个鸿蒙中的控件，`Text`和`StackLayout`，用法上相当于Android中的`TextView`和`FramLayout`，用来展示文本和包裹子控件的，当然这里也不一定需要用`StackLayout`，也可以用鸿蒙中的其他ViewGrop来代替，这里就不作过多介绍了，具体可以自行查询官网了解。我们可以分几步来实现自定义的CardView：
- 1.继承自`StackLayout`，实现其中3个构造方法。
- 2.在初始化方法中声明一个`Text`，设置`Text`的背景。
- 3.定义一个方法，用来根据数字设置文本的字体颜色和背景。
```
    /**
     * 初始化方法
     * 在初始化方法中声明一个Text，设置Text的背景
     */
    void initView() {
        //声明一个Text
        lable = new Text(getContext());
        lable.setTextSize(60);
        lable.setTextAlignment(TextAlignment.CENTER);

        //设置textView的背景
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
        element.setCornerRadius(10);
        lable.setBackground(element);
    }
```
**PS：这里要注意下，设置背景颜色的话目前只能通过`ShapeElement`，调用其中的`setRgbColor`方法，设置RGB颜色。**
## 2.自定义游戏视图GameView
- **1.继承TabLayout，实现其构造方法，其中TabLayout和Android中的很像，只是目前api还没有Android中那么多买单时也可以用来显示表哥布局，这里我们要实现TabLayout来绘制游戏的16宫格视图。**
- **2.定义一个卡片二维数组`cardMap`，用来保存卡片的位置和数字信息。定义个用来记录滑动自前卡片位置的二维数组`retractMap`用来实现悔棋。**
- **3.使用`setTouchEventListener`方法重写`onTouchEvent`来监听手势的滑动，实现上下左右滑动的监听。**
- **4.实现上下左右滑动监听的逻辑，合并数字卡片以及悔棋相关逻辑。使用`SoundPlayer`来播放滑动时候的声音。**
- **5.实现游戏结束逻辑，如果有cardMap里面有值为0即还有空格，或者当前格上下左右有相等的数字，则游戏继续，否则游戏结束**
具体逻辑实现可以参考已下代码：
### 实现手势滑动监听，区别上下左右代码
```
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
```
### 实现上下左右滑动业务逻辑代码
```
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
```
### 游戏结束相关逻辑代码
```
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
```
## 3.在主页面应用GameView实现游戏相关的介绍，分数，以及重新开始和悔棋按钮的点击事件
新建一个MainAbilitySlice继承AbilitySlice，AbilitySlice是用来展示应用的页面的，这和Android的Activity还是有点区别的，一个页面可以有多个AbilitySlice，这和Android中的fragment有点像，但是页面在前台时只能展示一个AbilitySlice，所以说它和两者还是有点区别的。

**PS：这里要注意的是由于DevEco Studio中目前xml布局还不支持自定义视图，所以我们这里只能在java代码中实现游戏页面的整体布局，还是比较麻烦的，**

通过这个小项目我们可以了解到鸿蒙开发和Android 还是比较像的，当时开发这个小游戏的时候，好的的api也是不知道，由于有Android项目开发经验，以及对java sdk比较熟悉，很多东西都是慢慢试出来，比如，滑动事件的监听，卡片字体的颜色和背景的等，希望鸿蒙后面会越来越完善。
具体代码可以在我的[github](https://github.com/CaojingCode/DevEcoStudioProjects.git)上查看

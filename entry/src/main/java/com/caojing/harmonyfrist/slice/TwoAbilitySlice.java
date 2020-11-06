package com.caojing.harmonyfrist.slice;

import com.caojing.harmonyfrist.ResourceTable;
import com.caojing.harmonyfrist.provider.ListProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.window.dialog.ToastDialog;

import java.util.ArrayList;
import java.util.List;

public class TwoAbilitySlice extends AbilitySlice {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_second_layout);
        //根据findById从布局中获取ListContainer控件。
        ListContainer listText = (ListContainer) findComponentById(ResourceTable.Id_listText);
        //拿到ListProvider的对象
        ListProvider listProvider=new ListProvider(this,getList());
        //将ListProvider的对象设置给ListContainer控件。展示数据
        listText.setItemProvider(listProvider);
        listText.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                new ToastDialog(TwoAbilitySlice.this).setContentText(String.format("你点击了第%d行", i)).show();
            }
        });

    }



    /**
     * 添加本地数据集合
     *
     * @return List<String>  数据集合
     */
    private List<String> getList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(String.format("我是第%d行", i));
        }
        return list;
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

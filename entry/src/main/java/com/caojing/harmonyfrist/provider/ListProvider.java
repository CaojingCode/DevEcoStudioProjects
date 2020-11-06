package com.caojing.harmonyfrist.provider;

import com.caojing.harmonyfrist.ResourceTable;
import ohos.agp.components.*;
import ohos.app.Context;
import ohos.global.resource.ResourceType;

import java.util.List;

/**
 * 类似于Android中ListView的适配器Adapter，只是名字不一样。
 */
public class ListProvider extends RecycleItemProvider {
    private List<String> data;
    LayoutScatter layoutScatter;

    public ListProvider(Context context, List<String> data) {
        this.data = data;
        this.layoutScatter = LayoutScatter.getInstance(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        ViewHolder viewHolder = null;
        //component相当于Android中的view，其他的和Android中ListView的适配器adapter差不多。
        // 名字区别也不大，不过Android中ListView基本被淘汰了。
        if (component == null) {
            component = layoutScatter.parse(ResourceTable.Layout_item_layout, null, false);
            viewHolder = new ViewHolder((ComponentContainer) component);
            component.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) component.getTag();
        }
        viewHolder.tvItemName.setText(data.get(i));
        return component;
    }

    /**
     * 类似于Android中的listView缓存。
     * 将已经显示在屏幕上的item缓存在ViewHolder中，下次再次出现直接从缓存中读取
     */
    static class ViewHolder {
        Text tvItemName;
        public ViewHolder(ComponentContainer componentContainer) {
            tvItemName = (Text) componentContainer.findComponentById(ResourceTable.Id_tvItemName);
        }
    }
}

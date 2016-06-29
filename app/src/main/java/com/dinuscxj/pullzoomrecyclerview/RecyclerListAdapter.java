package com.dinuscxj.pullzoomrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

public abstract class RecyclerListAdapter<T, VH extends RecyclerListAdapter.ViewHolder<T>> extends RecyclerView.Adapter<VH> {

    public static final Object ITEM_HEADER = new Object() { };
    public static final Object ITEM_FOOTER = new Object() { };
    public static final Class<?> TYPE_HEADER = ITEM_HEADER.getClass();
    public static final Class<?> TYPE_FOOTER = ITEM_FOOTER.getClass();

    private HashMap<Class<?>, Integer>          mViewHolderTypeRegistry     = new HashMap<>();
    private HashMap<Integer, ViewHolderFactory> mViewHolderFactoryRegistry  = new HashMap<>();
    /**
     * Register different type of data with different ViewHolder using ViewHolderFactory.
     * @param clazz type of data. Hierarchy tree of every data instance will be traversed to match the type.
     * @param factory ViewHolderFactory creates ViewHolder when data matches given data type.
     */
    public <F> void addViewType(Class<? extends F> clazz, ViewHolderFactory<? extends ViewHolder<? extends F>> factory) {
        int id = mViewHolderFactoryRegistry.size();
        mViewHolderTypeRegistry.put(clazz, id);
        mViewHolderFactoryRegistry.put(id, factory);
    }

    /**
     * Creates a view holder for the given view type.
     * Should be override only when view type requires to be determined dynamically
     * (requires {@link #getItemViewType(int)} to be overwritten).
     */
    @Override
    @SuppressWarnings("unchecked")
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mViewHolderFactoryRegistry.size() > 0) {
            return (VH) mViewHolderFactoryRegistry.get(viewType).onCreateViewHolder(parent);
        } else {
            return onCreateViewHolder(parent);
        }
    }

    /**
     * Creates a view holder.
     * Overwrite the method when used as an adapter for single view type.
     */
    public VH onCreateViewHolder(ViewGroup parent) {
        throw new RuntimeException("onCreateViewHolder(ViewGroup, int) is not implemented.");
    }

    public abstract T getItem(int position);

    public void setItem(int position, T object) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int getItemViewType(int position) {
        if (mViewHolderTypeRegistry.size() > 0) {
            Class clazz = getItem(position).getClass();
            while (clazz != Object.class) {
                if (mViewHolderTypeRegistry.containsKey(clazz)) {
                    return mViewHolderTypeRegistry.get(clazz);
                }
                clazz = clazz.getSuperclass();
            }
            throw new RuntimeException("Cannot resolve view type for (" + getItem(position) + ")");
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(getItem(position), position);
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        private final View mRootView;

        public ViewHolder(@NonNull View view) {
            super(view);
            mRootView = view;
            mRootView.setTag(this);
        }

        @SuppressWarnings("unchecked")
        public <VT extends View> VT getRootView() {
            return (VT) mRootView;
        }

        public abstract void bind(T item, int position);

    }

    public interface ViewHolderFactory<VH extends ViewHolder> {
        VH onCreateViewHolder(ViewGroup parent);
    }

}

package material.kcci.mystudio;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by User on 2017-05-23.
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    ItemTouchHelperListener _listener;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener) {
        this._listener = listener;
        Log.d("ItemTouch",listener.toString());
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return _listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.i(TAG, "onSwiped");

        _listener.onItemRemove(viewHolder.getAdapterPosition());


    }
}

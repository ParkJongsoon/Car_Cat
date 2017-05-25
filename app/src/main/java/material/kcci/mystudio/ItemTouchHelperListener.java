package material.kcci.mystudio;

/**
 * Created by User on 2017-05-23.
 */

public interface ItemTouchHelperListener
{
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemRemove(int position);
}

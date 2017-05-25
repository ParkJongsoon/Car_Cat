package material.kcci.mystudio;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by db2 on 2017-05-17.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> implements ItemTouchHelperListener
{
    //넘겨버릴 Data값들을 담을 배열
    private ArrayList<Recent> _recent;

    //생성자 ㄱㄱ
    public RecentAdapter(ArrayList<Recent> recents)
    {
        _recent = recents;
    }

    //새로운 View를 생성 오버라이드 필요 onCreateViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //ViewHolder가 생성될떄!!!!!!!!
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        //화면에 보일 new ViewHolder 객체를 생성해서 리턴
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recent recent = _recent.get(position);
        //Recent에 있는 데이터들을 해당 View에 갖다 붙여버린다.
        holder.imageID.setImageBitmap(recent.get_imageID());
        holder.dataId.setText(recent.get_Id());
        holder.titleText.setText(recent.get_title());
        holder.infoText.setText(recent.get_info());
    }

    @Override
    public int getItemCount() {
        //size (갯수)를 반환
        return _recent.size();
    }

    //아이템 이동
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < 0 || fromPosition >= _recent.size() || toPosition < 0 || toPosition >= _recent.size()){
            return false;
        }

        Recent fromItem = _recent.get(fromPosition);
        _recent.remove(fromPosition);
        _recent.add(toPosition, fromItem);

        notifyItemMoved(fromPosition, toPosition);
        return true;

    }


    //아이템 삭제
    @Override
    public void onItemRemove(int position)
    {
        Recent recent = _recent.get(position);
        String delIdnum = recent.get_Id();
        ConnectPHP conPHP = new ConnectPHP();
        conPHP.deleteToDatabase(delIdnum);
        conPHP.clearToDatabase();
        _recent.remove(position);
        notifyItemRemoved(position);
    }



    //각 데이터 아이템에 대하여 참조를 제공한다?
    //복잡한 데이터 항목은 항목 당 하나 이상의 보기가 필요할 수 있다.
    //뷰 홀더에서 데이터 아이템(항목)들에 대한 엑세스 권한을 제공한다.
    class ViewHolder extends RecyclerView.ViewHolder
    {
        //여기서 각 데이터 항목을 저장할 변수 선언
        TextView infoText;
        TextView titleText;
        ImageView imageID;
        TextView dataId;

        public ViewHolder(View itemView)
        {
            //RecyclerView.ViewHolder의 생성자를 다시 호출하며 매개변수로 TextView v를 전달
            //public RecyclerView(Context context) {this(context, null);}
            super(itemView);
            infoText = (TextView) itemView.findViewById(R.id.textInfo);
            dataId = (TextView) itemView.findViewById(R.id.dataID);
            titleText = (TextView) itemView.findViewById(R.id.textTitle);
            imageID = (ImageView) itemView.findViewById(R.id.imageID);
        }
    }
}

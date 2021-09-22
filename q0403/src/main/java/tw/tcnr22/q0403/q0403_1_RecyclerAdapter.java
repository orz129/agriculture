package tw.tcnr22.q0403;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tw.tcnr22.q0403.q0403_1_RecyclerAdapter;

public class q0403_1_RecyclerAdapter extends RecyclerView.Adapter<q0403_1_RecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<q0403_Post> mData;
    //    -------------------------------------------------------------------
    private OnItemClickListener mOnItemClickListener = null;
    //--------------------------------------------
    public q0403_1_RecyclerAdapter(Context context, ArrayList<q0403_Post> data) {
        this.mContext = context;
        this.mData = data;
    }
    //    -------------------------------------------------------------------
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.cell_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.title = (TextView) view.findViewById(R.id.name);
        holder.link = (TextView) view.findViewById(R.id.text_url);
        holder.Description = (TextView) view.findViewById(R.id.Content);
        //----------------------------------------------------
        //將創建的View註冊點擊事件
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final q0403_Post post = mData.get(position);
        holder.title.setText(post.title);
        holder.Description.setText(post.Description);
        holder.link.setText("發布日期 : "+post.cDate);

//            if (post.Zipcode.length()>0){
//                holder.Zipcode.setText("["+post.Zipcode+"]");
//            }else{
//                holder.Zipcode.setText("[000]");
//            }
//            if (post.Website.length()>0){
//                holder.Website.setText(post.Website);
//            }else{
//                holder.Website.setText("無網站");
//            }
//============網址轉圖片================
//        Glide.with(mContext)
//                .load(post.Picture1)
////                .skipMemoryCache(true)
////                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .override(100, 75)
//                .transition(withCrossFade())
//                .error(
//                        Glide.with(mContext)
//                                .load("https://tcnr2021a12.000webhostapp.com/picture/nopic1.jpg"))
//                .into(holder.img);
//
//        //將position保存在itemView的Tag中，以便點擊時進行獲取
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意這裡使用getTag方法獲取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //======= sub class   ==================
    class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView img;
        public TextView title;
        public TextView Description;
        public TextView link;


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    //-----------------------------------------------
}

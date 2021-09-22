package tw.tcnr22.q0403;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tw.tcnr22.q0403.Q0422_RecyclerAdapter;


public class Q0422_RecyclerAdapter extends RecyclerView.Adapter<Q0422_RecyclerAdapter.ViewHolder> implements View.OnClickListener {
        private Context mContext;
        private ArrayList<Q0422_Post> mData;
        //    -------------------------------------------------------------------
        private OnItemClickListener mOnItemClickListener = null;
        //--------------------------------------------
        public Q0422_RecyclerAdapter(Context context, ArrayList<Q0422_Post> data) {
            this.mContext = context;
            this.mData = data;
        }
        //    -------------------------------------------------------------------
        public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }
        //-------------------------------------------------------------------
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.q0422_cell_post, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.育苗場名稱 = (TextView) view.findViewById(R.id.name);
            holder.簡介 = (TextView) view.findViewById(R.id.Content);
            holder.地址 = (TextView) view.findViewById(R.id.text_url);
            holder.服務項目 = (TextView) view.findViewById(R.id.description);
            //----------------------------------------------------
            //將創建的View註冊點擊事件
            view.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Q0422_Post post = mData.get(position);
            holder.育苗場名稱.setText(post.育苗場名稱);
            holder.服務項目.setText(post.服務項目);
            holder.簡介.setText(post.簡介);
            holder.地址.setText("地址 : "+post.地址);

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
            public TextView 育苗場名稱;
            public TextView 服務項目;
            public TextView 簡介;
            public TextView 地址;

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    //-----------------------------------------------
    }
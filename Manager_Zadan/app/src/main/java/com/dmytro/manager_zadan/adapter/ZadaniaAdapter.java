package com.dmytro.manager_zadan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmytro.manager_zadan.R;
import com.dmytro.manager_zadan.TaskEditActivity;
import com.dmytro.manager_zadan.db.DatabaseHandler;
import com.dmytro.manager_zadan.db.TouchListener;

import java.util.List;

public class ZadaniaAdapter extends RecyclerView.Adapter<ZadaniaAdapter.ZadaniaHolder> {
    private DatabaseHandler db;
    private List<Zadania> listData;
    private LayoutInflater inflater;

    public ZadaniaAdapter(List<Zadania> listData, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public ZadaniaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_for_main, parent, false);
        return new ZadaniaHolder(view);
    }

    @Override
    public void onBindViewHolder(final ZadaniaHolder holder, int position) {
        final Zadania item = listData.get(position);
        db = new DatabaseHandler(inflater.getContext());

        if (item.getDesc().length()>42){
            String f = item.getDesc().substring(0,40)+"...";
            holder.taskDesc.setText(f);
        } else {
            holder.taskDesc.setText(item.getDesc());
        }
        holder.time.setText(item.getDate().substring(11,16));
        holder.container.setBackgroundColor(Color.parseColor(item.getBackColor()));
        if (item.getAction() == 1){
            holder.taskDesc.setPaintFlags(holder.taskDesc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (item.getAction() == 0){
            holder.taskDesc.setPaintFlags(holder.taskDesc.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.container.setOnTouchListener(new TouchListener(inflater.getContext()){
            @Override
            public void onClick() {
                super.onClick();
                Intent intent = new Intent(inflater.getContext(), TaskEditActivity.class);
                intent.putExtra("origin", "forEdit");
                intent.putExtra("editDate", item.getDate().substring(0,10));
                intent.putExtra("editTime", item.getDate().substring(11,16));
                intent.putExtra("editTaskDesc", item.getDesc());
                intent.putExtra("editColor", item.getBackColor());
                intent.putExtra("editAction", item.getAction());
                intent.putExtra("editId", item.getId());
                inflater.getContext().startActivity(intent);
            }

            @Override
            public void onLongClick() {
                super.onLongClick();
                Toast.makeText(inflater.getContext(), "Długie naciśnięcie", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                //Robie normalnym moj tekst
                db.updateAction(item.getId(),0);
                item.setAction(0);
                holder.taskDesc.setPaintFlags(holder.taskDesc.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                notifyDataSetChanged();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                //przekreśla tekst
                db.updateAction(item.getId(),1);
                item.setAction(1);
                holder.taskDesc.setPaintFlags(holder.taskDesc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class ZadaniaHolder extends RecyclerView.ViewHolder {

        private TextView taskDesc;
        private TextView time;
        private View container;

        public ZadaniaHolder(View itemView) {
            super(itemView);
            taskDesc = (TextView) itemView.findViewById(R.id.task_desc);
            Typeface tf = Typeface.createFromAsset(taskDesc.getContext().getAssets(), "fonts/Delius-Regular.ttf");
            taskDesc.setTypeface(tf);
            time = (TextView) itemView.findViewById(R.id.task_time);
            time.setTypeface(tf);
            container = itemView.findViewById(R.id.cont_item_task);
        }
    }
}

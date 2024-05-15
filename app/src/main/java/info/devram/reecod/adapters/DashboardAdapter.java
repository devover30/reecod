package info.devram.reecod.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import info.devram.reecod.R;
import info.devram.reecod.data.model.NoteEntity;
import info.devram.reecod.databinding.DashboardRowItemBinding;
import info.devram.reecod.libs.Helpers;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private static final String TAG = "DashboardAdapter";

    private final List<NoteEntity> noteEntityList;
    private Context context;

    public DashboardAdapter(List<NoteEntity> noteEntityList) {
        this.noteEntityList = noteEntityList;
    }

    @NonNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.context = parent.getContext();
        DashboardRowItemBinding binding = DashboardRowItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull DashboardAdapter.ViewHolder holder, int position) {
        NoteEntity noteEntity = noteEntityList.get(position);
        String desc;
        String regex = ",";
        String[] myArray = noteEntity.getDesc().split(regex);
        if (myArray.length == 2) {
            desc = myArray[0] + "\n" + myArray[1];
            Log.d(TAG, "onBindViewHolder: " + desc);
        } else {
            desc = noteEntity.getDesc();
        }

        int[] colorToUse = getColorsArray(R.array.note_bg_colors);
        switch((position+1)%5) {
            case 0:
                holder.binding.getRoot().setCardBackgroundColor(colorToUse[0]);
                break;
            case 1:
                holder.binding.getRoot().setCardBackgroundColor(colorToUse[1]);
                //dc.setCardBackgroundColor(Color.parseColor("#Color1"));
                break;
            case 2:
                holder.binding.getRoot().setCardBackgroundColor(colorToUse[2]);
                //dc.setCardBackgroundColor(Color.parseColor("#Color2"));
                break;
            case 3:
                holder.binding.getRoot().setCardBackgroundColor(colorToUse[3]);
                //dc.setCardBackgroundColor(Color.parseColor("#Color3"));
                break;
            case 4:
                holder.binding.getRoot().setCardBackgroundColor(colorToUse[4]);
                //dc.setCardBackgroundColor(Color.parseColor("#Color4"));
                break;
            default:
                holder.binding.getRoot().setCardBackgroundColor(colorToUse[5]);
        }
        holder.binding.noteHeadingTV.setText(noteEntity.getHeading());
        holder.binding.noteDateAddTV.setText(Helpers.formatDate(noteEntity.getCreatedAt()));
        holder.binding.noteDescTV.setText(desc);
        String tag = "#" + noteEntity.getTag();
        holder.binding.noteTagTV.setText(tag);
    }

    private int[] getColorsArray(@ArrayRes Integer arrayResId) {
        try (TypedArray typedArray = context.getResources().obtainTypedArray(arrayResId)) {

            int length = typedArray.length();
            int[] colors = new int[length];
            for (int i = 0; i < length; i++) {
                colors[i] = typedArray.getColor(i, 0);
            }
            typedArray.recycle();
            return colors;
        }
    }


    @Override
    public int getItemCount() {
        return noteEntityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final DashboardRowItemBinding binding;

        public ViewHolder(@NonNull DashboardRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

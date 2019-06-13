package id.sapasampah.petugas;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FinishedAdapter extends FirestoreRecyclerAdapter<Finished, FinishedAdapter.FinishedHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FinishedAdapter(@NonNull FirestoreRecyclerOptions<Finished> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FinishedHolder holder, int position, @NonNull Finished model) {

        holder.nameFinished.setText(model.getUsername());
        holder.addrFinished.setText(model.getFullAddr() + ", " + model.getDistrict() + ", " + model.getCity() + ", " + model.getProvince() + ", " + model.getPostal());
        Log.d("FinishedAdapter", "onBindViewHolder: INFLATING");
    }

    @NonNull
    @Override
    public FinishedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.finished_layout, viewGroup, false);
        return new FinishedHolder(v);
    }


    class FinishedHolder extends RecyclerView.ViewHolder{

        TextView nameFinished, addrFinished;

        public FinishedHolder(@NonNull View itemView) {
            super(itemView);
            nameFinished = itemView.findViewById(R.id.nameFinished);
            addrFinished = itemView.findViewById(R.id.addrFinished);
        }
    }
}

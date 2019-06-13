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

public class ActiveAdapter extends FirestoreRecyclerAdapter<Active, ActiveAdapter.ActiveHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ActiveAdapter(@NonNull FirestoreRecyclerOptions<Active> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ActiveHolder holder, int position, @NonNull Active model) {

        holder.nameActive.setText(model.getUsername());
        holder.addrActive.setText(model.getFullAddr() + ", " + model.getDistrict() + ", " + model.getCity() + ", " + model.getProvince() + ", " + model.getPostal());
        Log.d("ActiveAdapter", "onBindViewHolder: INFLATING");
    }

    @NonNull
    @Override
    public ActiveHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.active_layout, viewGroup, false);
        return new ActiveHolder(v);
    }


    class ActiveHolder extends RecyclerView.ViewHolder{


        TextView nameActive, addrActive;

        public ActiveHolder(@NonNull View itemView) {
            super(itemView);
            nameActive = itemView.findViewById(R.id.nameActive);
            addrActive = itemView.findViewById(R.id.addrActive);
        }
    }
}

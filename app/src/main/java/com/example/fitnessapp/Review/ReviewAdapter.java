package com.example.fitnessapp.Review;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.models.Review;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private Context context;

    private ArrayList<Review> reviews;

    private IFitnessApi fitnessApi;

    public ReviewAdapter(Context context, ArrayList<Review> reviewsList, IFitnessApi fitnessApi) {
        this.context = context;
        this.reviews = reviewsList;
        this.fitnessApi = fitnessApi;
    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_review_item, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //get data
        Review reviewList = reviews.get(position);
        int reviewId = reviewList.getId();
        String reviewName = reviewList.getUsername();
        String reviewDesc = reviewList.getDescription();


        //set data
        holder.revName.setText("Korisničko ime: " + reviewName);




        holder.revCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewDetails.class);
                intent.putExtra("id", reviewId);
                intent.putExtra("name", reviewName);
                intent.putExtra("desc", reviewDesc);
                context.startActivity(intent); //dohvati na ReviewDetails

            }
        });

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReview(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
    private void deleteReview(int position) {
        int reviewId = reviews.get(position).getId();
        Call<Void> call = fitnessApi.deleteReview(reviewId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    reviews.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, reviews.size());
                    Toast.makeText(context, "Recenzija obrisana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Ne moze obrisat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Neuspješno brisanje recenzije: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


class ReviewViewHolder extends RecyclerView.ViewHolder
{

    TextView revName;
    CardView revCard;
    ImageButton deleteImg;

    public ReviewViewHolder(@NonNull View itemView)
    {
        super(itemView);
        revName = itemView.findViewById(R.id.revName);
        revCard = itemView.findViewById(R.id.revCard);
        deleteImg = itemView.findViewById(R.id.deleteImg);

    }

}

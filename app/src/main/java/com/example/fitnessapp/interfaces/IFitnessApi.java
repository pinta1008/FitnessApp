package com.example.fitnessapp.interfaces;

import com.example.fitnessapp.models.Category;
import com.example.fitnessapp.models.Exercise;
import com.example.fitnessapp.models.ExerciseMaxWeight;
import com.example.fitnessapp.models.ExerciseMaxWeightWithUsername;
import com.example.fitnessapp.models.ExerciseRequest;
import com.example.fitnessapp.models.ExerciseUser;
import com.example.fitnessapp.models.ModelTraining;
import com.example.fitnessapp.models.MonthlyTrainingStatistic;
import com.example.fitnessapp.models.Review;
import com.example.fitnessapp.models.ReviewRequest;
import com.example.fitnessapp.models.YearlyTrainingStatistic;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IFitnessApi {
    @GET("api/Categories")
    Call<List<Category>> getCategory();

    @POST("api/Categories")
    Call<Category> postCategory(@Body Category category);

    @GET("api/Categories/{id}")
    Call<List<Category>> getCategoryById(@Path("id") int id);

    @DELETE("api/Categories/{id}")
    Call<Void> deleteCategory(@Path("id") String categoryId);

    //reviews
    @POST("api/Reviews")
    Call<Void> addReview(@Body ReviewRequest reviewRequest);

    @DELETE("api/Reviews/{id}")
    Call<Void> deleteReview(@Path("id") int reviewId);

    @GET("api/Reviews")
    Call<List<Review>> getReview();

    //Statistika

    @GET("api/statistics/trainings-per-month")
    Call<List<MonthlyTrainingStatistic>> getTrainingsPerMonth();

    @GET("api/statistics/trainings-per-year")
    Call<List<YearlyTrainingStatistic>> getTrainingsPerYear();

    @GET("api/statistics/exercise-with-max-weight")
    Call<ExerciseUser> getExerciseWithMaxWeight();

    @GET("api/statistics/exercise-max-weights")
    Call<List<ExerciseMaxWeight>> getExerciseMaxWeights();

    @GET("api/statistics/exercise-max-weights/{userId}")
    Call<List<ExerciseMaxWeight>> getExerciseMaxWeights(@Path("userId") String userId);

    @GET("api/statistics/exercise-max-weights-with-username")
    Call<List<ExerciseMaxWeightWithUsername>> getExerciseMaxWeightsWithUsername();



}


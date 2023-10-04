
package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

    public class LoginResponse {

        @SerializedName("userId")
        String userId;
        @SerializedName("jwtToken")
        String jwtToken;
        @SerializedName("username")

        String username;
        @SerializedName("email")
        String email;

        @SerializedName("photo")

        String photo;

        @SerializedName("roleName")

        String roleName;

        @SerializedName("treningId")

        Integer treningID;

        public String getUserId() {
            return userId;
        }

        public String getJwtToken() {
            return jwtToken;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoto() {
            return photo;
        }

        public String getRoleName() {
            return roleName;
        }

        public Integer getTreningID() {
            return treningID;
        }
    }


package com.example.namayesh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.room.util.DBUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.namayesh.databinding.ActivityRegisterLoginBinding;
import com.example.namayesh.models.User;
import com.example.namayesh.retrofitApi.ApiClient;
import com.example.namayesh.retrofitApi.ApiInterface;
import com.example.namayesh.retrofitApi.AuthenticationApiClient;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterLoginActivity extends AppCompatActivity {

    ActivityRegisterLoginBinding binding;
    SharedPreferences userInfos;
    SharedPreferences.Editor userInfosEditor;
    ApiInterface request;
    String response;
    int authenticationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_login);
        binding.setRegisterLoginActivity(this);
        binding.setOnClickViewListenerOnRegisterLoginActivity(new OnClickViewListenerOnRegisterLoginActivity());
        userInfos = getSharedPreferences("userInfos", MODE_PRIVATE);
        userInfosEditor = userInfos.edit();
        request = ApiClient.getApiClient().create(ApiInterface.class);

    }

    @Override
    public void onBackPressed() {
        binding.layerRegister.setVisibility(View.VISIBLE);
        binding.layerLogin.setVisibility(View.GONE);
        binding.layerEnterCode.setVisibility(View.GONE);
        binding.layerForgetPassword.setVisibility(View.GONE);
        binding.layerNewPassword.setVisibility(View.GONE);

        binding.editTextEmailLogin.getText().clear();
        binding.editTextPassLogin.getText().clear();
        binding.editTextNameRegister.getText().clear();
        binding.editTextEmailRegister.getText().clear();
        binding.editTextPhoneRegister.getText().clear();
        binding.editTextPassRegister.getText().clear();
        binding.editTextConfirmPassRegister.getText().clear();
        binding.PinView.getText().clear();
        binding.editTextEmailForgetPassword.getText().clear();
        binding.editTextPassNewPassword.getText().clear();
        binding.editTextConfirmPassNewPassword.getText().clear();
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isPhoneValid(String phone) {

        return true;
    }

    public void sendAuthenticationCode(String receptor) {
        authenticationCode = (int) (Math.random() * 10000);
        /*ApiInterface request = AuthenticationApiClient.getApiClient().create(ApiInterface.class);
        request.sendAuthenticationCode(receptor,authenticationCode,"namayesh")
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });*/
        Toast.makeText(this, authenticationCode + "", Toast.LENGTH_LONG).show();
    }

    public static class OnClickViewListenerOnRegisterLoginActivity {

        public void onClickCardLogin(View view, RegisterLoginActivity registerLoginActivity) {
            registerLoginActivity.binding.layerRegister.setVisibility(View.GONE);
            registerLoginActivity.binding.layerLogin.setVisibility(View.VISIBLE);
            registerLoginActivity.binding.layerEnterCode.setVisibility(View.GONE);
            registerLoginActivity.binding.layerForgetPassword.setVisibility(View.GONE);
            registerLoginActivity.binding.layerNewPassword.setVisibility(View.GONE);
        }

        public void onClickCardRegister(View view, RegisterLoginActivity registerLoginActivity) {
            registerLoginActivity.binding.layerRegister.setVisibility(View.VISIBLE);
            registerLoginActivity.binding.layerLogin.setVisibility(View.GONE);
            registerLoginActivity.binding.layerEnterCode.setVisibility(View.GONE);
            registerLoginActivity.binding.layerForgetPassword.setVisibility(View.GONE);
            registerLoginActivity.binding.layerNewPassword.setVisibility(View.GONE);
        }

        public void onClickForgetPassword(View view, RegisterLoginActivity registerLoginActivity) {
            registerLoginActivity.binding.layerRegister.setVisibility(View.GONE);
            registerLoginActivity.binding.layerLogin.setVisibility(View.GONE);
            registerLoginActivity.binding.layerEnterCode.setVisibility(View.GONE);
            registerLoginActivity.binding.layerForgetPassword.setVisibility(View.VISIBLE);
            registerLoginActivity.binding.layerNewPassword.setVisibility(View.GONE);
        }

        public void onClickSendCode(View view, RegisterLoginActivity registerLoginActivity) {
            if (TextUtils.isEmpty(registerLoginActivity.binding.editTextEmailForgetPassword.getText().toString())) {
                Toast.makeText(registerLoginActivity, "please Enter your Email / UserName ", Toast.LENGTH_SHORT).show();
            } else {
                if (registerLoginActivity.isEmailValid(registerLoginActivity.binding.editTextEmailForgetPassword.getText().toString())) {
                    registerLoginActivity.request
                            .getUserInfo(registerLoginActivity.binding.editTextEmailForgetPassword.getText().toString())
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    User user = response.body();
                                    if (!user.getName().equals("nouser")) {
                                        registerLoginActivity.sendAuthenticationCode(user.getPhone());
                                        registerLoginActivity.response = "forgetPassword";

                                        registerLoginActivity.binding.PinView.setAnimationEnable(true);
                                        registerLoginActivity.binding.PinView.getText().clear();

                                        registerLoginActivity.binding.layerRegister.setVisibility(View.GONE);
                                        registerLoginActivity.binding.layerLogin.setVisibility(View.GONE);
                                        registerLoginActivity.binding.layerEnterCode.setVisibility(View.VISIBLE);
                                        registerLoginActivity.binding.layerForgetPassword.setVisibility(View.GONE);
                                        registerLoginActivity.binding.layerNewPassword.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(registerLoginActivity, "This email is not register", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                } else {
                    Toast.makeText(registerLoginActivity, "Email is wrong ", Toast.LENGTH_SHORT).show();
                }

            }
        }

        public void onClickConfirmCode(View view, RegisterLoginActivity registerLoginActivity) {

            if (Integer.parseInt(registerLoginActivity.binding.PinView.getText().toString()) == registerLoginActivity.authenticationCode) {
                if (registerLoginActivity.response.equals("valid")) {
                    registerLoginActivity.request.addUserIntoServer(registerLoginActivity.binding.editTextNameRegister.getText().toString(),
                            registerLoginActivity.binding.editTextEmailRegister.getText().toString(),
                            registerLoginActivity.binding.editTextPhoneRegister.getText().toString(),
                            registerLoginActivity.binding.editTextPassRegister.getText().toString(),
                            Long.toString((System.currentTimeMillis() / 1000) + 604800))
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    User user = response.body();
                                    if (user.getName().equals("nouser")) {
                                        Toast.makeText(registerLoginActivity, "Registeration faild\nPlease try again", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(registerLoginActivity, "welcom to Namayesh", Toast.LENGTH_SHORT).show();
                                        registerLoginActivity.userInfosEditor.putString("id", user.getId());
                                        registerLoginActivity.userInfosEditor.putString("name", user.getName());
                                        registerLoginActivity.userInfosEditor.putString("email", user.getEmail());
                                        registerLoginActivity.userInfosEditor.putString("phone", user.getPhone());
                                        registerLoginActivity.userInfosEditor.putString("password", user.getPassword());
                                        registerLoginActivity.userInfosEditor.putLong("accunt", user.getAccunt());
                                        registerLoginActivity.userInfosEditor.apply();

                                        Intent intent = new Intent(registerLoginActivity, MainActivity.class);
                                        registerLoginActivity.startActivity(intent);
                                        registerLoginActivity.finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    /*Toast.makeText(registerLoginActivity, t.getMessage(), Toast.LENGTH_SHORT).show();*/
                                    Log.i("Tag", t.getMessage());
                                }
                            });
                } else if (registerLoginActivity.response.equals("forgetPassword")) {
                    registerLoginActivity.binding.layerRegister.setVisibility(View.GONE);
                    registerLoginActivity.binding.layerLogin.setVisibility(View.GONE);
                    registerLoginActivity.binding.layerEnterCode.setVisibility(View.GONE);
                    registerLoginActivity.binding.layerForgetPassword.setVisibility(View.GONE);
                    registerLoginActivity.binding.layerNewPassword.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(registerLoginActivity, "code is wrong", Toast.LENGTH_SHORT).show();
            }
        }

        public void onClickConfirmNewPassword(View view, RegisterLoginActivity registerLoginActivity) {

            if (TextUtils.isEmpty(registerLoginActivity.binding.editTextPassNewPassword.getText().toString()) ||
                    TextUtils.isEmpty(registerLoginActivity.binding.editTextConfirmPassNewPassword.getText().toString())) {

                Toast.makeText(registerLoginActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (registerLoginActivity.binding.editTextPassNewPassword.getText().toString()
                        .equals(registerLoginActivity.binding.editTextConfirmPassNewPassword.getText().toString())) {

                    registerLoginActivity.request.updatePassword(
                            registerLoginActivity.binding.editTextPassNewPassword.getText().toString(),
                            registerLoginActivity.binding.editTextEmailForgetPassword.getText().toString())
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String[] rec = response.body().split(System.lineSeparator());
                                    if (rec[1].equals("successfull")){

                                        Toast.makeText(registerLoginActivity, "change password successfully done", Toast.LENGTH_SHORT).show();

                                        registerLoginActivity.binding.layerRegister.setVisibility(View.GONE);
                                        registerLoginActivity.binding.layerLogin.setVisibility(View.VISIBLE);
                                        registerLoginActivity.binding.layerEnterCode.setVisibility(View.GONE);
                                        registerLoginActivity.binding.layerForgetPassword.setVisibility(View.GONE);
                                        registerLoginActivity.binding.layerNewPassword.setVisibility(View.GONE);
                                    }else {
                                        Toast.makeText(registerLoginActivity, "change password failed", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });

                } else {
                    Toast.makeText(registerLoginActivity, "password and confirm passsword isnot match", Toast.LENGTH_SHORT).show();
                }
            }

        }

        public void onClickRegisterButton(View view, RegisterLoginActivity registerLoginActivity) {
            if (TextUtils.isEmpty(registerLoginActivity.binding.editTextNameRegister.getText().toString()) ||
                    TextUtils.isEmpty(registerLoginActivity.binding.editTextEmailRegister.getText().toString()) ||
                    TextUtils.isEmpty(registerLoginActivity.binding.editTextPhoneRegister.getText().toString()) ||
                    TextUtils.isEmpty(registerLoginActivity.binding.editTextPassRegister.getText().toString()) ||
                    TextUtils.isEmpty(registerLoginActivity.binding.editTextConfirmPassRegister.getText().toString())) {

                Toast.makeText(registerLoginActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (registerLoginActivity.binding.editTextPassRegister.getText().toString()
                        .equals(registerLoginActivity.binding.editTextConfirmPassRegister.getText().toString())) {
                    if (registerLoginActivity.isEmailValid(registerLoginActivity.binding.editTextEmailRegister.getText().toString())) {
                        if (registerLoginActivity.isPhoneValid(registerLoginActivity.binding.editTextPhoneRegister.getText().toString())) {
                            registerLoginActivity.request.checkEmailInServer(registerLoginActivity.binding.editTextEmailRegister.getText().toString())
                                    .enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String[] rec = response.body().split(System.lineSeparator());
                                            registerLoginActivity.response = rec[1];
                                            if (registerLoginActivity.response.equals("valid")) {

                                                registerLoginActivity.binding.layerRegister.setVisibility(View.GONE);
                                                registerLoginActivity.binding.layerLogin.setVisibility(View.GONE);
                                                registerLoginActivity.binding.layerEnterCode.setVisibility(View.VISIBLE);
                                                registerLoginActivity.binding.layerForgetPassword.setVisibility(View.GONE);
                                                registerLoginActivity.binding.layerNewPassword.setVisibility(View.GONE);

                                                registerLoginActivity.binding.PinView.setAnimationEnable(true);
                                                registerLoginActivity.binding.PinView.getText().clear();
                                                registerLoginActivity.sendAuthenticationCode(registerLoginActivity.binding
                                                        .editTextPhoneRegister.getText().toString());

                                            } else {
                                                Toast.makeText(registerLoginActivity, "This email is already registered", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(registerLoginActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(registerLoginActivity, "The entered phone isnot valid", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(registerLoginActivity, "Email is wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(registerLoginActivity, "password and confirm passsword isnot match", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void onClickLoginButton(View view, RegisterLoginActivity registerLoginActivity) {

            if (TextUtils.isEmpty(registerLoginActivity.binding.editTextEmailLogin.getText().toString()) ||
                    TextUtils.isEmpty(registerLoginActivity.binding.editTextPassLogin.getText().toString())) {

                Toast.makeText(registerLoginActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (registerLoginActivity.isEmailValid(registerLoginActivity.binding.editTextEmailLogin.getText().toString())) {
                    registerLoginActivity.request.loginUser(registerLoginActivity.binding.editTextEmailLogin.getText().toString(),
                            registerLoginActivity.binding.editTextPassLogin.getText().toString())
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    User user = response.body();
                                    if (user.getName().equals("nouser")) {
                                        Toast.makeText(registerLoginActivity, "Email or Password is wrong", Toast.LENGTH_SHORT).show();
                                    } else {
                                        registerLoginActivity.userInfosEditor.putString("id", user.getId());
                                        registerLoginActivity.userInfosEditor.putString("name", user.getName());
                                        registerLoginActivity.userInfosEditor.putString("email", user.getEmail());
                                        registerLoginActivity.userInfosEditor.putString("phone", user.getPhone());
                                        registerLoginActivity.userInfosEditor.putString("password", user.getPassword());
                                        registerLoginActivity.userInfosEditor.putLong("accunt", user.getAccunt());
                                        registerLoginActivity.userInfosEditor.apply();

                                        Toast.makeText(registerLoginActivity, "Wellcom to namayesh", Toast.LENGTH_SHORT).show();


                                        Intent intent = new Intent(registerLoginActivity, MainActivity.class);
                                        registerLoginActivity.startActivity(intent);
                                        registerLoginActivity.finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(registerLoginActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(registerLoginActivity, "Email is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
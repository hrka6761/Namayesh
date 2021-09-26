package com.example.namayesh;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.namayesh.databinding.ActivityPayBinding;
import com.example.namayesh.models.User;
import com.example.namayesh.retrofitApi.ApiClient;
import com.example.namayesh.retrofitApi.ApiInterface;
import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity {

    ActivityPayBinding binding;
    ApiInterface request;
    SharedPreferences userInfos;
    SharedPreferences.Editor userInfosEditor;
    int preCredit;
    Long preAccunt;
    String currentAccunt;
    int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay);
        binding.setPayActivity(this);
        binding.setOnClickViewListenerOnPayActivity(new OnClickViewListenerOnPayActivity());

        userInfos = getSharedPreferences("userInfos", MODE_PRIVATE);
        userInfosEditor = userInfos.edit();

        request = ApiClient.getApiClient().create(ApiInterface.class);
        request.getUserInfo(userInfos.getString("email", "")).enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                preAccunt = response.body().getAccunt();
                if (response.body().getAccunt() - (System.currentTimeMillis()/1000) > 0) {
                    Long minute = (response.body().getAccunt() - (System.currentTimeMillis() / 1000)) / 60;
                    Long hour = minute / 60;
                    Long day = hour / 24;

                    preCredit = Math.toIntExact(day);
                    binding.txtPreviousCreditPayActivity.setText(day + " Days");
                } else {
                    preCredit = 0;
                    binding.txtPreviousCreditPayActivity.setText("0 Days");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        Uri data = getIntent().getData();
        ZarinPal.getPurchase(this).verificationPayment(data, new OnCallbackVerificationPaymentListener() {
            @Override
            public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {


                if (isPaymentSuccess) {
                    /* When Payment Request is Success :) */
                    String message = "Your Payment is Success \n your Tracking Code is " + refID;
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    if (preAccunt < System.currentTimeMillis()/1000){
                        preAccunt = System.currentTimeMillis()/1000;

                        switch ((int) paymentRequest.getAmount()) {
                            case 1500:
                                currentAccunt = Long.toString(preAccunt + 86400L);
                                break;
                            case 8500:
                                currentAccunt = Long.toString(preAccunt + (86400 * 7));
                                break;
                            case 32000:
                                currentAccunt = Long.toString(preAccunt + (86400 * 30));
                                break;
                            case 90000:
                                currentAccunt = Long.toString(preAccunt + (86400 * 90));
                                break;
                            case 170000:
                                currentAccunt = Long.toString(preAccunt + (86400 * 180));
                                break;
                            case 320000:
                                currentAccunt = Long.toString(preAccunt + (86400 * 360));
                                break;

                        }
                    }else {
                        switch ((int) paymentRequest.getAmount()) {
                            case 1500:
                                currentAccunt = Long.toString(preAccunt + 86400L);
                                break;
                            case 8500:
                                currentAccunt = Long.toString(preAccunt + (86400 * 7));
                                break;
                            case 32000:
                                currentAccunt = Long.toString(preAccunt + (86400 * 30));
                                break;
                            case 90000:
                                currentAccunt = Long.toString(preAccunt + (86400 * 90));
                                break;
                            case 170000:
                                currentAccunt = Long.toString(preAccunt + (86400 * 180));
                                break;
                            case 320000:
                                currentAccunt = Long.toString(preAccunt + (86400 * 360));
                                break;

                        }
                    }

                    Toast.makeText(PayActivity.this, currentAccunt+"", Toast.LENGTH_SHORT).show();

                    request.updateAccunt(currentAccunt, userInfos.getString("email", "")).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            userInfosEditor.putLong("accunt", user.getAccunt());
                            userInfosEditor.apply();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.i("Tag", t.getMessage());
                        }
                    });


                } else {

                    Toast.makeText(getApplicationContext(), "your payment is failur", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public static class OnClickViewListenerOnPayActivity {
        public void onClickTime(View view, PayActivity payActivity) {
            int id = view.getId();
            switch (id) {
                case R.id.card_view_time1_pay_activity:
                    payActivity.binding.txtBoughtCreditPayActivity.setText("1 day");
                    payActivity.binding.txtCurrentCreditPayActivity.setText(payActivity.preCredit + 1 + " Days");
                    payActivity.amount = 1500;
                    break;
                case R.id.card_view_time2_pay_activity:
                    payActivity.binding.txtBoughtCreditPayActivity.setText("7 days");
                    payActivity.binding.txtCurrentCreditPayActivity.setText(payActivity.preCredit + 7 + " Days");
                    payActivity.amount = 8500;
                    break;
                case R.id.card_view_time3_pay_activity:
                    payActivity.binding.txtBoughtCreditPayActivity.setText("30 days");
                    payActivity.binding.txtCurrentCreditPayActivity.setText(payActivity.preCredit + 30 + " Days");
                    payActivity.amount = 32000;
                    break;
                case R.id.card_view_time4_pay_activity:
                    payActivity.binding.txtBoughtCreditPayActivity.setText("90 days");
                    payActivity.binding.txtCurrentCreditPayActivity.setText(payActivity.preCredit + 90 + " Days");
                    payActivity.amount = 90000;
                    break;
                case R.id.card_view_time5_pay_activity:
                    payActivity.binding.txtBoughtCreditPayActivity.setText("180 days");
                    payActivity.binding.txtCurrentCreditPayActivity.setText(payActivity.preCredit + 180 + " Days");
                    payActivity.amount = 170000;
                    break;
                case R.id.card_view_time6_pay_activity:
                    payActivity.binding.txtBoughtCreditPayActivity.setText("360 days");
                    payActivity.binding.txtCurrentCreditPayActivity.setText(payActivity.preCredit + 360 + " Days");
                    payActivity.amount = 320000;
                    break;
                case R.id.btn_purchase_pay_activity:

                    if (payActivity.amount != 0) {

                        ZarinPal purchase = ZarinPal.getPurchase(payActivity);
                        /*PaymentRequest payment  = ZarinPal.getPaymentRequest();*/
                        PaymentRequest payment = ZarinPal.getSandboxPaymentRequest();


                        payment.setMerchantID("71c705f8-bd37-11e6-aa0c-000c295eb8fc");
                        payment.setAmount(payActivity.amount);
                        payment.isZarinGateEnable(true);  // If you actived `ZarinGate`, can handle payment by `ZarinGate`
                        payment.setDescription("segsdggsgsdgdg");
                        payment.setCallbackURL("app://app");     /* Your App Scheme */
                        payment.setMobile("09128575851");            /* Optional Parameters */
                        payment.setEmail("hrka6761@gmail.com");     /* Optional Parameters */


                        purchase.startPayment(payment, new OnCallbackRequestPaymentListener() {
                            @Override
                            public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {


                                if (status == 100) {
                   /*
                   When Status is 100 Open Zarinpal PG on Browser
                   */
                                    Toast.makeText(payActivity, payActivity.amount + " Tomans", Toast.LENGTH_SHORT).show();
                                    payActivity.startActivity(intent);
                                } else {
                                    Toast.makeText(payActivity, "Your Payment Failure :(", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    } else {
                        Toast.makeText(payActivity, "please select a subscribtion", Toast.LENGTH_LONG).show();
                    }

                    break;
                default:
                    payActivity.binding.txtBoughtCreditPayActivity.setText("0 days");
                    break;
            }
        }
    }
}
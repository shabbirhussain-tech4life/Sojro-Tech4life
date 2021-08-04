package com.mdcbeta.authenticate;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseActivity;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Invited;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.healthprovider.cases.CreateCaseFragment;
import com.mdcbeta.healthprovider.cases.MyCasesFragment;
import com.mdcbeta.supportservice.MainSupportServiceActivity;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TermsActivity extends BaseActivity{

  TextView terms,terms2,terms3,terms4,terms5,terms6,terms7,terms8,terms9;

    @Inject
    ApiFactory apiFactory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);
      terms = (TextView)findViewById(R.id.terms);
      terms2 = (TextView)findViewById(R.id.terms2);
      terms3 = (TextView)findViewById(R.id.terms3);
      terms4 = (TextView)findViewById(R.id.terms4);
      terms5 = (TextView)findViewById(R.id.terms5);
      terms6 = (TextView)findViewById(R.id.terms6);
      terms7 = (TextView)findViewById(R.id.terms7);
      terms8 = (TextView)findViewById(R.id.terms8);
      terms9 = (TextView)findViewById(R.id.terms9);
      terms.setText("Welcome to the Sojro and thank you for taking the time to read these terms of use which apply to your use of our Website. Accessing and using our website will be deemed as agreement with these terms of use.");
      terms2.setText("Please read these carefully before accessing or using Sojro through this website or downloading the software application. Using Sojro or MySojro indicates that you accept these terms and conditions. If you do not accept these terms and conditions (‘terms and conditions’), do not use this software. Tech4Life Enterprises will not be liable for any use thereof if you use Sojro and do not accept these terms and conditions. The Sojro Website and Application are offered to you conditioned on your acceptance without modification of the terms, conditions, and notices contained herein. Your use of the Sojro or MySojro constitutes your agreement to all such terms, conditions, and notices.");
      terms4.setText("The Sojro may contain links to other Web Sites ('Linked Sites'). The Linked Sites are not under the control of Tech4Life Enterprises, and Tech4Life Enterprises is not responsible for the contents of any Linked Site, including without limitation any link contained in a Linked Site, or any changes or updates to a Linked Site. Tech4Life Enterprises is not responsible for webcasting or any other form of transmission received from any Linked Site. Sojro is providing these links to you only as a convenience, and the inclusion of any link does not imply endorsement by Tech4Life Enterprises or Sojro of the site or any association with its operators.");
      terms3.setText("Tech4Life Enterprises reserves the right to change the terms, conditions, and notices under which the Sojro is offered, including but not limited to the charges associated with the use of Sojro.");
      terms5.setText("As a condition of your use of the Sojro, you warrant to Sojro and Tech4Life Enterprises that you will not use the Sojro application or Web Site for any purpose that is unlawful or prohibited by these terms, conditions, and notices. You may not use Sojro in any manner which could damage, disable, overburden, or impair the Sojro application or website or interfere with any other party's use and enjoyment of Sojro. You may not obtain or attempt to obtain any materials or information through any means not intentionally made available or provided for through the Sojro application or Web Sites.");
      terms6.setText("The Sojro application and Web Site may contain bulletin board services, chat areas, news groups, forums, communities, personal web pages, calendars, and/or other message or communication facilities designed to enable you to communicate with the care givers and health providers (collectively, 'Telemedicine Services'), you agree to use the Telemedicine Services only to post, send and receive messages and material that are proper and related to the particular Telemedicine Service. By way of example, and not as a limitation, you agree that when using a Telemedicine Service, you will not: Defame, abuse, harass, stalk, threaten or otherwise violate the legal rights (such as rights of privacy and publicity) of others. Publish, post, upload, distribute or disseminate any inappropriate, profane, defamatory, infringing, obscene, indecent or unlawful topic, name, material or information. Upload files that contain software or other material protected by intellectual property laws (or by rights of privacy of publicity) unless you own or control the rights thereto or have received all necessary consents. Upload files that contain viruses, corrupted files, or any other similar software or programs that may damage the operation of another's computer. Advertise or offer to sell or buy any goods or services for any business purpose, unless such Communication Service specifically allows such messages. Conduct or forward surveys, contests, pyramid schemes or chain letters. Download any file posted by another user of a Communication Service that you know, or reasonably should know, cannot be legally distributed in such manner. Falsify or delete any author attributions, legal or other proper notices or proprietary designations or labels of the origin or source of software or other material contained in a file that is uploaded. Restrict or inhibit any other user from using and enjoying the Telemedicine Services. Violate any code of conduct or other guidelines which may be applicable for any particular Telemedicine Service. Harvest or otherwise collect information about others, including e-mail addresses, without their consent. Violate any applicable laws or regulations. Tech4Life Enterprises has no obligation to monitor the telemedicine Services. However, it reserves the right to review materials posted to a telemedicine Service and to remove any materials in its sole discretion. Tech4Life Enterprises reserves the right to terminate your access to any or all of the telemedicine Services at any time without notice for any reason whatsoever. Tech4Life Enterprises reserves the right at all times to disclose any information as necessary to satisfy any applicable law, regulation, legal process or governmental request, or to edit, refuse to post or to remove any information or materials, in whole or in part, in Tech4Life Enterprises’ sole discretion. Always use caution when giving out any personally identifying information about yourself or your patients in any telemedicine Service. Tech4Life Enterprises does not control or endorse the content, messages or information found in any telemedicine Service and, therefore, Tech4Life Enterprises specifically disclaims any liability with regard to the Communication Services and any actions resulting from your participation in any telemedicine Service. Managers and hosts are not authorized Sojro/ Tech4Life Enterprises spokespersons, and their views do not necessarily reflect those of Tech4Life Enterprises. Materials uploaded to a telemedicine Service may be subject to posted limitations on usage, reproduction and/or dissemination. You are responsible for adhering to such limitations if you download the materials. Tech4Life Enterprises does not claim ownership of the materials you provide to Sojro (including feedback and suggestions) or post, upload, input or submit to any Sojro or its associated services (collectively 'Submissions'). By posting, uploading, inputting, providing or submitting your Submission you warrant and represent that you own or otherwise control all of the rights to your Submission as described in this section including, without limitation, all the rights necessary for you to provide, post, upload, input or submit the Submissions.");
      terms7.setText("The informatiin, software, Products, and services included in or available through the Sojro application or web site may include inaccuracies or typographical errors.Changes are periodically added to the information here in. Tech4life Enterprises add/or its suppliers may make improvements and/or changes in the Sojro at any time. advice received via Sojro should not be relied upon for personal,medical, legal or financial decisions and you should consult an appropriate professional for specific advice tailored to your situation. Tech4Life Enterprises and/or its suppliers make no representations about the suitability,reliablity, availability,timeliness,and accuracy of the information, software,products,services and related graphics contained on Sojro for any purpose.To the maximum extent permitted by applicable law,all such information,software,products, services and related graphics are provided 'as it'without warranty or condition of any kind.Tech4life Enterprises and/or its suppliers here by disclaim all warranties and conditions with regard to this information,software, products, services and related graphics, including all implied warranties or conditions of merchantability, fitness for a particular purpose,title and non-infringement. To the maxium extent permitted by applicale law,in no event shall Tech4life Enterprises and/or its suppliers be liable for ant direct, indirect,punitive,incidental,special,consquential damages or any damages whatsoever including,without limitation,damages for loss of use, data or profits,arising out of or in any way connected with the usse or performance of the Sojro,with the delay or inability to use the Sojro website/application or related services,");
      terms8.setText("Tech4Life Enterprises reserves the right, in its sole discretion, to terminate your access to Sojro and the related services or any portion thereof at any time, without notice. GENERAL To the maximum extent permitted by law, this agreement is governed by the laws of the Province of Ontario, Canada. And you hereby consent to the exclusive jurisdiction and venue of courts in Ontario, Canada. In all disputes arising out of or relating to the use of the Sojro. Use of Sojro is unauthorized in any jurisdiction that does not give effect to all provisions of these terms and conditions, including without limitation this paragraph.");
      terms9.setText("All contents of the Sojro are: © 2021 by Tech4Life Enterprises Canada Inc. All rights reserved.");
    }





    @Override
    public void performInjection(AppComponent appComponent) {
        appComponent.inject(this);

    }









}

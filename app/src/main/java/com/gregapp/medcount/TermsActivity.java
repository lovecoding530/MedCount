package com.gregapp.medcount;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

public class TermsActivity extends AppCompatActivity {

    private MyPreference myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPreference = new MyPreference(this);
        setContentView(R.layout.activity_terms);

        PDFView pdfView = (PDFView)findViewById(R.id.pdfView);
        pdfView.fromAsset("terms.pdf").load();

        Button btnAccept = findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreference.setIsAcceptedTerms(true);
                Intent i = new Intent(TermsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button btnDecline = findViewById(R.id.btnDecline);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreference.setIsAcceptedTerms(false);
                finish();
            }
        });
    }
    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(576);
        val = val * 100d;
        return val.intValue();
    }
}

package com2027.cw.dp00405.vsa;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WebsiteFragment extends Fragment {

    EditText editText;
    EditText editText1;
    TextView textView;

    View view;
    WebView webView;
    String url = "https://www.currys.co.uk/gbuk/index.html";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_website, container, false);

        webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        return view;
    }

    public void bookmarkPage(final Context context, final String email) {
        if (webView.getUrl() == url) {
            Toast.makeText(context, "Cannot bookmark the main page", Toast.LENGTH_SHORT).show();
        }  else {
            View root = RelativeLayout.inflate(context, R.layout.add_bookmark, null);
            editText = root.findViewById(R.id.editbook);
            editText1 = root.findViewById(R.id.editbook2);
            //textView.setText(webView.getUrl());
            new AlertDialog.Builder(context)
                    .setTitle("Enter data values:")
                    .setMessage("Please enter a Title and Description")
                    .setView(root)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseClass receive = new DatabaseClass(null, null, null);
                            receive.execute(email + "," + editText.getText().toString() + "," + editText1.getText().toString() + "," + webView.getUrl() + "#BOOKSET");
                        }
                    })
                    .setNeutralButton(android.R.string.cancel, null)
                    .show();
        }
    }

    public void setURL(String url) {
        this.url = url;
    }

}
